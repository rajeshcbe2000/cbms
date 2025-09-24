/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSPrizedMoneyDetailsEntryOB.java
 *
 * Created on Wed Jun 08 17:36:25 IST 2011
 */

package com.see.truetransact.ui.mdsapplication.mdsprizedmoneydetailsentry;

import java.util.List;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.transferobject.mdsapplication.mdsprizedmoneydetailsentry.MDSPrizedMoneyDetailsEntryTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import java.util.Date;
/**
 *
 * @author
 */

public class MDSPrizedMoneyDetailsEntryOB extends CObservable{
    
    private Integer txtDivisionNo = 0;   //AJITH
    private Integer txtInstallmentNo = 0;  //AJITH
    private String txtSchemeName = "";
    private String tdtDrawOrAuctionDt = "";
    private Double txtPrizedAmount = 0.0;  //AJITH
    private Double txtTotalBonusAmount = 0.0;  //AJITH
    private Double txtNextBonusAmount = 0.0;   //AJITH
    private Double txtCommisionAmount = 0.0;   //AJITH
    private Double txtTotalDiscount = 0.0;   //AJITH
    private Double txtNetAmountPayable = 0.0;   //AJITH
    private Integer txtApplicationNo = 0;   //AJITH
    private String lblMembType = "";
    private String lblMemberName = "";
    private String txtChittalNo = "";
    private Integer txtInstallmentsDue = 0;   //AJITH
    private Double txtInstallmentsAmountPaid = 0.0;  //AJITH
    private Double txtInstallmentsPaid = 0.0;  //AJITH
    private String chkDraw = "";
    private String chkAuction = "";
    private String chkUserDefined = "";
    private String tdtNextInstallmentDt = "";
    private String tdtPayementDate = "";
    private Double txtInstallmentOverDueAmount = 0.0;  //AJITH
    private Integer txtSubNo = 0;    //AJITH
    private String oldChittalNo = "";
    private Integer oldSubNo = 0;    //AJITH
    
    private Double totalAmtDivision = 0.0;  //AJITH Changed From String to Double and value from ""
    private Double auctionMaxAmt = 0.0;  //AJITH Changed From String to Double and value from ""
    private Double auctionMinAmt = 0.0;  //AJITH Changed From String to Double and value from ""
    private Integer noOfMemberPerDiv = 0;  //AJITH Changed From String to Double and value from ""
    private String commisionType = "";
    private Double commisionRate = 0.0;  //AJITH Changed From String to Double and value from ""
    private Integer noOfDivision = 0;  //AJITH Changed From String to Double and value from ""
    private String multipleMember = "";
    private int auctDay = 0;
    
    private String lblHouseStNo = "";
    private String lblArea = "";
    private String lblCity = "";
    private String lblState = "";
    private String lblpin = "";
    private String holidayType = "";
    private HashMap _authorizeMap;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int result;
    private ProxyFactory proxy;
    private HashMap map;
    private int actionType;
    private final static Logger log = Logger.getLogger(MDSPrizedMoneyDetailsEntryOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private MDSPrizedMoneyDetailsEntryTO objPrizedMoneyDetailsEntryTO;
    private Date currDt = null;
    private String chkTransaction = "";
    private String transId = "";
    private String predefinedInstall = "";
    private String chkNoAuction= "";
    private String txtWitness1CustId = "";
    private String txtWitness1Chittal = "";
    private String txtWitness2CustId = "";
    private String txtWitness2Chittal = "";
    private HashMap serviceTax_Map=null;
    private String lblServiceTaxval="";
    
    
    /** Creates a new instance of TDS MiantenanceOB */
    public MDSPrizedMoneyDetailsEntryOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "MDSPrizedMoneyDetailsEntryJNDI");
            map.put(CommonConstants.HOME, "mdsapplication.mdsprizedmoneydetailsentry.MDSPrizedMoneyDetailsEntryHome");
            map.put(CommonConstants.REMOTE, "mdsapplication.mdsprizedmoneydetailsentry.MDSPrizedMoneyDetailsEntry");
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** To perform the necessary operation */
    public void doAction() {
        try {
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null){
                doActionPerform();
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        
        final HashMap data = new HashMap();
        data.put("COMMAND",getCommand());
        if(get_authorizeMap() == null){
            if(getChkNoAuction()!=null && !getChkNoAuction().equals("") && getChkNoAuction().equals("Y")){
                data.put("MDSPrizedMoneyDetailsEntry",setPrizedMoneyDetailsEntryDataNoAuction());
            }else{
                data.put("MDSPrizedMoneyDetailsEntry",setPrizedMoneyDetailsEntryData());
            }
        }
        else{
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        System.out.println("data in FixedAssets OB : " + data);
        if (getActionType() == ClientConstants.ACTIONTYPE_NEW && getLblServiceTaxval() != null && CommonUtil.convertObjToDouble(getLblServiceTaxval()) > 0) {
            data.put("serviceTaxDetails", getServiceTax_Map());
            data.put("serviceTaxDetailsTO", setServiceTaxDetails());
        }
        HashMap proxyResultMap = proxy.execute(data, map);
        System.out.println("data in proxyResultMap OB : " + proxyResultMap);
        setProxyReturnMap(proxyResultMap);
        _authorizeMap = null;
        setResult(getActionType());
    }
    
    /** To populate data into the screen */
    public MDSPrizedMoneyDetailsEntryTO setPrizedMoneyDetailsEntryData() {
        
        final MDSPrizedMoneyDetailsEntryTO objMDSPrizedMoneyDetailsEntryTO = new MDSPrizedMoneyDetailsEntryTO();
        try{
            objMDSPrizedMoneyDetailsEntryTO.setMdsSchemeName(getTxtSchemeName());
            objMDSPrizedMoneyDetailsEntryTO.setDrawAuctionDate(DateUtil.getDateMMDDYYYY(getTdtDrawOrAuctionDt()));
            objMDSPrizedMoneyDetailsEntryTO.setInstallmentNo(getTxtInstallmentNo());
            objMDSPrizedMoneyDetailsEntryTO.setDivisionNo(getTxtDivisionNo());
            objMDSPrizedMoneyDetailsEntryTO.setDraw(getChkDraw());
            objMDSPrizedMoneyDetailsEntryTO.setAuction(getChkAuction());
            objMDSPrizedMoneyDetailsEntryTO.setUserDefined(getChkUserDefined());
            objMDSPrizedMoneyDetailsEntryTO.setNextInstallmentDate(DateUtil.getDateMMDDYYYY(getTdtNextInstallmentDt()));
            objMDSPrizedMoneyDetailsEntryTO.setPrizedAmount(getTxtPrizedAmount());
            objMDSPrizedMoneyDetailsEntryTO.setTotalBonusAmount(getTxtTotalBonusAmount());
            objMDSPrizedMoneyDetailsEntryTO.setNextBonusAmount(getTxtNextBonusAmount());
            objMDSPrizedMoneyDetailsEntryTO.setCommisionAmount(getTxtCommisionAmount());
            objMDSPrizedMoneyDetailsEntryTO.setTotalDiscount(getTxtTotalDiscount());
            objMDSPrizedMoneyDetailsEntryTO.setNetAmountPayable(getTxtNetAmountPayable());
            objMDSPrizedMoneyDetailsEntryTO.setAppNo(getTxtApplicationNo());
            objMDSPrizedMoneyDetailsEntryTO.setChittalNo(getTxtChittalNo());
            objMDSPrizedMoneyDetailsEntryTO.setMemberType(getLblMembType());
            objMDSPrizedMoneyDetailsEntryTO.setMemberName(getLblMemberName());
            objMDSPrizedMoneyDetailsEntryTO.setInstallmentPaid(getTxtInstallmentsPaid());
            objMDSPrizedMoneyDetailsEntryTO.setInstallAmountPaid(getTxtInstallmentsAmountPaid());
            objMDSPrizedMoneyDetailsEntryTO.setInstallmentDue(getTxtInstallmentsDue());
            objMDSPrizedMoneyDetailsEntryTO.setInstalOverdueAmt(getTxtInstallmentOverDueAmount());
            objMDSPrizedMoneyDetailsEntryTO.setSubNo(getTxtSubNo());
            objMDSPrizedMoneyDetailsEntryTO.setStatus(getAction());
            objMDSPrizedMoneyDetailsEntryTO.setStatusBy(TrueTransactMain.USER_ID);
            objMDSPrizedMoneyDetailsEntryTO.setStatusDt(ClientUtil.getCurrentDateWithTime());
            objMDSPrizedMoneyDetailsEntryTO.setPaymentDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtPayementDate())));
            //Set Old Chittal No
            objMDSPrizedMoneyDetailsEntryTO.setOldChittalNo(getOldChittalNo());
            objMDSPrizedMoneyDetailsEntryTO.setOldSubNo(getOldSubNo());
            objMDSPrizedMoneyDetailsEntryTO.setBranchId(TrueTransactMain.BRANCH_ID);
            objMDSPrizedMoneyDetailsEntryTO.setAuctionTrans(getChkTransaction());
            if(getPredefinedInstall()!=null && !getPredefinedInstall().equals("")){
                objMDSPrizedMoneyDetailsEntryTO.setPredefinedInstall(getPredefinedInstall());
            }else{
                objMDSPrizedMoneyDetailsEntryTO.setPredefinedInstall("N");
            }
            objMDSPrizedMoneyDetailsEntryTO.setWitness1Chittal(getTxtWitness1Chittal());
            objMDSPrizedMoneyDetailsEntryTO.setWitness1CustId(getTxtWitness1CustId());
            objMDSPrizedMoneyDetailsEntryTO.setWitness2Chittal(getTxtWitness2Chittal());
            objMDSPrizedMoneyDetailsEntryTO.setWitness2CustId(getTxtWitness2CustId());
            
        }catch(Exception e){
            log.info("Error In setMDSPrizedMoneyDetailsEntryTOData()");
            e.printStackTrace();
        }
        return objMDSPrizedMoneyDetailsEntryTO;
    }
    
        public MDSPrizedMoneyDetailsEntryTO setPrizedMoneyDetailsEntryDataNoAuction() {
        
        final MDSPrizedMoneyDetailsEntryTO objMDSPrizedMoneyDetailsEntryTO = new MDSPrizedMoneyDetailsEntryTO();
        try{
            objMDSPrizedMoneyDetailsEntryTO.setMdsSchemeName(getTxtSchemeName());
            objMDSPrizedMoneyDetailsEntryTO.setDrawAuctionDate(DateUtil.getDateMMDDYYYY(getTdtDrawOrAuctionDt()));
            objMDSPrizedMoneyDetailsEntryTO.setInstallmentNo(getTxtInstallmentNo());
            objMDSPrizedMoneyDetailsEntryTO.setDivisionNo(getTxtDivisionNo());
            objMDSPrizedMoneyDetailsEntryTO.setDraw(getChkDraw());
            objMDSPrizedMoneyDetailsEntryTO.setAuction(getChkAuction());
            objMDSPrizedMoneyDetailsEntryTO.setUserDefined(getChkUserDefined());
            objMDSPrizedMoneyDetailsEntryTO.setNextInstallmentDate(DateUtil.getDateMMDDYYYY(getTdtNextInstallmentDt()));
            objMDSPrizedMoneyDetailsEntryTO.setPrizedAmount(getTxtPrizedAmount());
            objMDSPrizedMoneyDetailsEntryTO.setTotalBonusAmount(getTxtTotalBonusAmount());
            objMDSPrizedMoneyDetailsEntryTO.setNextBonusAmount(getTxtNextBonusAmount());
            objMDSPrizedMoneyDetailsEntryTO.setCommisionAmount(getTxtCommisionAmount());
            objMDSPrizedMoneyDetailsEntryTO.setTotalDiscount(getTxtTotalDiscount());
            objMDSPrizedMoneyDetailsEntryTO.setNetAmountPayable(getTxtNetAmountPayable());
            objMDSPrizedMoneyDetailsEntryTO.setAppNo(getTxtApplicationNo());
            objMDSPrizedMoneyDetailsEntryTO.setChittalNo(getTxtChittalNo());
            objMDSPrizedMoneyDetailsEntryTO.setMemberType(getLblMembType());
            objMDSPrizedMoneyDetailsEntryTO.setMemberName(getLblMemberName());
            objMDSPrizedMoneyDetailsEntryTO.setInstallmentPaid(getTxtInstallmentsPaid());
            objMDSPrizedMoneyDetailsEntryTO.setInstallAmountPaid(getTxtInstallmentsAmountPaid());
            objMDSPrizedMoneyDetailsEntryTO.setInstallmentDue(getTxtInstallmentsDue());
            objMDSPrizedMoneyDetailsEntryTO.setInstalOverdueAmt(getTxtInstallmentOverDueAmount());
            objMDSPrizedMoneyDetailsEntryTO.setSubNo(getTxtSubNo());
            objMDSPrizedMoneyDetailsEntryTO.setStatus(getAction());
            objMDSPrizedMoneyDetailsEntryTO.setStatusBy(TrueTransactMain.USER_ID);
            objMDSPrizedMoneyDetailsEntryTO.setStatusDt(ClientUtil.getCurrentDateWithTime());
            objMDSPrizedMoneyDetailsEntryTO.setPaymentDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtPayementDate())));
            //Set Old Chittal No
            objMDSPrizedMoneyDetailsEntryTO.setOldChittalNo(getOldChittalNo());
            objMDSPrizedMoneyDetailsEntryTO.setOldSubNo(getOldSubNo());
            objMDSPrizedMoneyDetailsEntryTO.setBranchId(TrueTransactMain.BRANCH_ID);
            objMDSPrizedMoneyDetailsEntryTO.setAuctionTrans(getChkTransaction());
            if(getPredefinedInstall()!=null && !getPredefinedInstall().equals("")){
                objMDSPrizedMoneyDetailsEntryTO.setPredefinedInstall(getPredefinedInstall());
            }else{
                objMDSPrizedMoneyDetailsEntryTO.setPredefinedInstall("N");
            }
            if(getChkDraw()!=null && getChkDraw().equals("N") && getChkAuction()!=null && getChkAuction().equals("N")){
                objMDSPrizedMoneyDetailsEntryTO.setPredefinedInstall("Y");
            }            
            objMDSPrizedMoneyDetailsEntryTO.setWitness1Chittal(getTxtWitness1Chittal());
            objMDSPrizedMoneyDetailsEntryTO.setWitness1CustId(getTxtWitness1CustId());
            objMDSPrizedMoneyDetailsEntryTO.setWitness2Chittal(getTxtWitness2Chittal());
            objMDSPrizedMoneyDetailsEntryTO.setWitness2CustId(getTxtWitness2CustId());
            
        }catch(Exception e){
            log.info("Error In setMDSPrizedMoneyDetailsEntryTOData()");
            e.printStackTrace();
        }
        return objMDSPrizedMoneyDetailsEntryTO;
    }
        
    protected void getCustomerAddressDetails(){
        HashMap custAddressMap = new HashMap();
        custAddressMap.put("SCHEME_NAME",getTxtSchemeName());
        custAddressMap.put("CHITTAL_NO",getTxtChittalNo());
        custAddressMap.put("SUB_NO",CommonUtil.convertObjToInt(getTxtSubNo())); //AJITH
        List lst = ClientUtil.executeQuery("getCustomerAddressDetailsinAppln",custAddressMap);
        if(lst!=null && lst.size()>0){
            custAddressMap = (HashMap)lst.get(0);
            setLblHouseStNo(CommonUtil.convertObjToStr(custAddressMap.get("HOUSE_ST")));
            setLblArea(CommonUtil.convertObjToStr(custAddressMap.get("AREA")));
            setLblCity(CommonUtil.convertObjToStr(custAddressMap.get("CITY")));
            setLblState(CommonUtil.convertObjToStr(custAddressMap.get("STATE")));
            setLblpin(CommonUtil.convertObjToStr(custAddressMap.get("PIN")));
        }
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
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    public boolean calculationDate(){
        boolean flag = false;
        HashMap schemeMap = new HashMap();
        schemeMap.put("SCHEME_NAME",getTxtSchemeName());
        List lst = ClientUtil.executeQuery("getSchemeNameDetailsProductLevel",schemeMap);
        if(lst!=null && lst.size()>0){
            schemeMap = (HashMap)lst.get(0);
            int instFreq=0;
//            setTxtInstallmentNo(String.valueOf(CommonUtil.convertObjToLong(schemeMap.get("INST_COUNT"))+1));
            Date schemeStartDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(schemeMap.get("SCHEME_START_DT")));
            auctDay = CommonUtil.convertObjToInt(schemeMap.get("DRAW_AUCT_DAY"));
            int instDay = CommonUtil.convertObjToInt(schemeMap.get("INSTALLMENT_DAY"));
            int instPayDay = CommonUtil.convertObjToInt(schemeMap.get("DAY_PAYMENT"));
            int inst = CommonUtil.convertObjToInt(schemeMap.get("INSTALLMENTS"));
            String holidayType = CommonUtil.convertObjToStr(schemeMap.get("HOLIDAY_INT"));
            setHolidayType(CommonUtil.convertObjToStr(schemeMap.get("HOLIDAY_INT")));
            int dayPayment = CommonUtil.convertObjToInt(schemeMap.get("DAY_PAYMENT"));
            setTotalAmtDivision(CommonUtil.convertObjToDouble(schemeMap.get("TOTAL_AMOUNT_DIVISION")));  //AJITH
            setAuctionMaxAmt(CommonUtil.convertObjToDouble(schemeMap.get("AUCTION_MAXAMT")));  //AJITH
            setAuctionMinAmt(CommonUtil.convertObjToDouble(schemeMap.get("AUCTION_MINAMT")));   //AJITH
            setNoOfMemberPerDiv(CommonUtil.convertObjToInt(schemeMap.get("NO_OF_MEMBER_PER_DIVISION")));    //AJITH
            setCommisionType(CommonUtil.convertObjToStr(schemeMap.get("COMMISION_RATE_TYPE")));
            setCommisionRate(CommonUtil.convertObjToDouble(schemeMap.get("COMMISION_RATE_AMT")));   //AJITH
            setNoOfDivision(CommonUtil.convertObjToInt(schemeMap.get("NO_OF_DIVISIONS")));  //AJITH
            instFreq = CommonUtil.convertObjToInt(schemeMap.get("INSTALLMENT_FREQUENCY"));
            System.out.println("#######getHolidayType"+getHolidayType());
            //            int total = 0;
            //            if(instPayDay>0){
            //                total = auctDay + instPayDay;
            //            }else{
            //                total = auctDay;
            //            }
//            Date currDt = currDt.clone();
            Date DrawOrAucDt = (Date) currDt.clone();
            Date PaymentDt = (Date) currDt.clone();
            int stYear = schemeStartDt.getYear()+1900;
            int currYear = currDt.getYear()+1900;
            int stMonth = schemeStartDt.getMonth();
            int currMonth = currDt.getMonth();
            int value = 0;
            if(currYear == stYear){
                value = currMonth - stMonth + 1;
            }else{
                int yearDiff = currYear - stYear;
                value = (yearDiff * 12) + currMonth - stMonth + 1 ;
            }
            if(instFreq!=7){
            DrawOrAucDt.setDate(auctDay);
            setTdtDrawOrAuctionDt(CommonUtil.convertObjToStr(DrawOrAucDt));
            }
            if(dayPayment>0){
                Date Payment = DateUtil.addDays(DrawOrAucDt,dayPayment);
                setTdtPayementDate(CommonUtil.convertObjToStr(Payment));
            }else if(inst>0){
                PaymentDt.setDate(auctDay);
                PaymentDt.setMonth(PaymentDt.getMonth()+inst);
                setTdtPayementDate(CommonUtil.convertObjToStr(PaymentDt));
            }
            Date nextPay = DrawOrAucDt;
            if(instFreq!=7){
            nextPay.setMonth(nextPay.getMonth()+1);
            }
            //            PaymentDt.setDate(total);
            if(value<0){
                value = 1;
            }
            if(holidayType.equals("any next working day")){
                HashMap holidayMap = new HashMap();
                holidayMap.put("NEXT_DATE",nextPay);
                holidayMap.put("CURR_DATE",nextPay);
                holidayMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                List lst1 = ClientUtil.executeQuery("checkHolidayProvisionTD", holidayMap);
                if(lst1!=null && lst1.size()>0){
                    int count = 0;
                    for (int i = 0; i<lst1.size();i++){
                        count = i+1;
                    }
                    setTdtNextInstallmentDt(CommonUtil.convertObjToStr(DateUtil.addDays(nextPay,count)));
                }else{
                    setTdtNextInstallmentDt(CommonUtil.convertObjToStr(nextPay));
                }
            }else{
                HashMap holidayMap = new HashMap();
                holidayMap.put("NEXT_DATE",DateUtil.addDays(nextPay,-1));
                holidayMap.put("CURR_DATE",DateUtil.addDays(nextPay,-1));
                holidayMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                List lst1 = ClientUtil.executeQuery("checkHolidayProvisionTD", holidayMap);
                if(lst1!=null && lst1.size()>0){
                    int count = 0;
                    for (int i = 0; i<lst1.size();i++){
                        count += count;
                    }
                    setTdtNextInstallmentDt(CommonUtil.convertObjToStr(DateUtil.addDays(nextPay,-count)));
                }else{
                    setTdtNextInstallmentDt(CommonUtil.convertObjToStr(DateUtil.addDays(nextPay,-1)));
                }
            }
        }
        return flag;
    }
    
    /** To retrieve a particular customer's accountclosing record */
    public void getData(HashMap whereMap) {
        try{
            System.out.println("#@@%@@#%@#whereMap"+whereMap);
            final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
            System.out.println("#@@%@@#%@#data"+data);
            objPrizedMoneyDetailsEntryTO = (MDSPrizedMoneyDetailsEntryTO) ((List) data.get("PrizedMoneyDetailsTO")).get(0);
            populatePrizedMoneyDetailsData(objPrizedMoneyDetailsEntryTO);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populatePrizedMoneyDetailsData(MDSPrizedMoneyDetailsEntryTO objPrizedTO) throws Exception{
        this.setTxtSchemeName(CommonUtil.convertObjToStr(objPrizedTO.getMdsSchemeName()));
        this.setTdtDrawOrAuctionDt(CommonUtil.convertObjToStr(objPrizedTO.getDrawAuctionDate()));
        this.setTxtInstallmentNo(objPrizedTO.getInstallmentNo());  //AJITH
        this.setTxtDivisionNo(objPrizedTO.getDivisionNo());  //AJITH
        this.setChkDraw(CommonUtil.convertObjToStr(objPrizedTO.getDraw()));
        this.setChkAuction(CommonUtil.convertObjToStr(objPrizedTO.getAuction()));
        this.setChkUserDefined(CommonUtil.convertObjToStr(objPrizedTO.getUserDefined()));
        this.setTdtNextInstallmentDt(CommonUtil.convertObjToStr(objPrizedTO.getNextInstallmentDate()));
        this.setTxtPrizedAmount(objPrizedTO.getPrizedAmount()); //AJITH
        this.setTxtTotalBonusAmount(objPrizedTO.getTotalBonusAmount()); //AJITH
        this.setTxtNextBonusAmount(objPrizedTO.getNextBonusAmount());   //AJITH
        this.setTxtCommisionAmount(objPrizedTO.getCommisionAmount());   //AJITH
        this.setTxtTotalDiscount(objPrizedTO.getTotalDiscount());   //AJITH
        this.setTxtNetAmountPayable(objPrizedTO.getNetAmountPayable()); //AJITH
        this.setTxtApplicationNo(objPrizedTO.getAppNo());   //AJITH
        this.setTxtChittalNo(CommonUtil.convertObjToStr(objPrizedTO.getChittalNo()));
        this.setLblMembType(CommonUtil.convertObjToStr(objPrizedTO.getMemberType()));
        this.setLblMemberName(CommonUtil.convertObjToStr(objPrizedTO.getMemberName()));
        this.setTxtInstallmentsPaid(objPrizedTO.getInstallmentPaid());  //AJITH
        this.setTxtInstallmentsAmountPaid(objPrizedTO.getInstallAmountPaid());  //AJITH
        this.setTxtInstallmentsDue(objPrizedTO.getInstallmentDue());    //AJITH
        this.setTxtInstallmentOverDueAmount(objPrizedTO.getInstalOverdueAmt()); //AJITH
        this.setTxtSubNo(objPrizedTO.getSubNo());   //AJITH
        this.setTdtPayementDate(CommonUtil.convertObjToStr(objPrizedTO.getPaymentDate()));
        this.setTxtWitness1Chittal(CommonUtil.convertObjToStr(objPrizedTO.getWitness1Chittal()));
        this.setTxtWitness1CustId(CommonUtil.convertObjToStr(objPrizedTO.getWitness1CustId()));
        this.setTxtWitness2Chittal(CommonUtil.convertObjToStr(objPrizedTO.getWitness2Chittal()));
        this.setTxtWitness2CustId(CommonUtil.convertObjToStr(objPrizedTO.getWitness2CustId()));
        setChanged();
        notifyObservers();
    }
    
    public void resetForm() {
        setTxtInstallmentNo(null);
        setPredefinedInstall("");
        setTxtWitness1Chittal("");
        setTxtWitness1CustId("");
        setTxtWitness2Chittal("");
        setTxtWitness2CustId("");
    }
    
    
    public void setReceiptDetails(HashMap map){
        HashMap chittalMap = new HashMap();
        long count =0;
        chittalMap.put("CHITTAL_NO",getTxtChittalNo());
        chittalMap.put("SUB_NO",CommonUtil.convertObjToInt(getTxtSubNo())); //AJITH
        List lst = ClientUtil.executeQuery("getSelctApplnReceiptDetails", chittalMap);
        if(lst!=null && lst.size()>0){
            chittalMap = (HashMap)lst.get(0);            
            Date currDate = (Date) currDt.clone();
            int instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
            Date startDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalMap.get("SCHEME_START_DT")));
            setNoOfDivision(CommonUtil.convertObjToInt(chittalMap.get("NO_OF_DIVISIONS"))); //AJITH
            HashMap whereMap = new HashMap();
            whereMap.put("CHITTAL_NO",getTxtChittalNo());
            whereMap.put("SUB_NO",CommonUtil.convertObjToInt(getTxtSubNo())); //AJITH Changed From CommonUtil.convertObjToStr(getTxtSubNo())
            List list = ClientUtil.executeQuery("getTotalInstAmount", whereMap);
            if(list!=null && list.size()>0){
                whereMap = (HashMap)list.get(0);
                setTxtInstallmentsPaid(CommonUtil.convertObjToDouble(whereMap.get("NO_INST_PAID")));  //AJITH
                setTxtInstallmentsAmountPaid(CommonUtil.convertObjToDouble(whereMap.get("PAID_AMT")));  //AJITH
                count = CommonUtil.convertObjToLong(whereMap.get("NO_INST_PAID"));
            }
            int stYear = startDate.getYear()+1900;
            int stMonth = startDate.getMonth();
            int currYear = currDate.getYear()+1900;
            int currMonth = currDate.getMonth();
            int value = 0;
            int pending = 0;
            
            double instAmt = CommonUtil.convertObjToDouble(chittalMap.get("INST_AMT")).doubleValue();
            int totInst = CommonUtil.convertObjToInt(chittalMap.get("NO_OF_INSTALLMENTS"));
            
//            if(stYear == currYear && stMonth == currMonth){
//                pending = 0;
//                setTxtInstallmentsDue(String.valueOf("0"));
//            }else if(stYear == currYear && stMonth != currMonth){
//                pending = 0;
//                value = currMonth - stMonth+1;
//                int diffMonth = currMonth - stMonth;
//                int pendingVal = diffMonth - (int)count;
//                if(instDay<currDate.getDate()){
//                    pending = pendingVal+1;
//                }
//                else{
//                    pending = pendingVal;
//                }
//                if(pending>0)
//                    setTxtInstallmentsDue(String.valueOf(pending));
//                else
//                    setTxtInstallmentsDue(String.valueOf(0));
//            }else{
//                int year = currYear - stYear;
//                value = (year * 12) + currMonth - stMonth;
//                int pendingVal = value - (int)count;
//                if(instDay<currDate.getDate()){
//                    pending = pendingVal+1;
//                }
//                else{
//                    pending = pendingVal;
//                }
//                if(pending>0)
//                    setTxtInstallmentsDue(String.valueOf(pending));
//                else
//                    setTxtInstallmentsDue(String.valueOf(0));
//            }
//            setTxtInstallmentOverDueAmount(String.valueOf(CommonUtil.convertObjToLong(getTxtInstallmentsDue()) * instAmt));
            
            // Calculating due amount
            HashMap insDateMap = new HashMap();
            int currInst = 0;
            int pendinginst = 0;
            insDateMap.put("DIVISION_NO", getTxtDivisionNo());
            insDateMap.put("SCHEME_NAME", getTxtSchemeName());
            insDateMap.put("CURR_DATE", currDate.clone());
            insDateMap.put("ADD_MONTHS", "-1");           
            List insDateLst = ClientUtil.executeQuery("getMDSCurrentInsDate", insDateMap);
            if (insDateLst != null && insDateLst.size() > 0) {
                insDateMap =  (HashMap)insDateLst.get(0);
                currInst = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO"));
            }
            pendinginst = currInst - CommonUtil.convertObjToInt(count);
            if(pendinginst>0)
                setTxtInstallmentsDue(pendinginst);
                else
                setTxtInstallmentsDue(0);
            setTxtInstallmentOverDueAmount(CommonUtil.convertObjToLong(getTxtInstallmentsDue()) * instAmt);
            // End
            
            
        }
    }

    public Integer getTxtDivisionNo() {
        return txtDivisionNo;
    }

    public void setTxtDivisionNo(Integer txtDivisionNo) {
        this.txtDivisionNo = txtDivisionNo;
    }
    
   
    public Integer getTxtInstallmentNo() {
        return txtInstallmentNo;
    }

    public void setTxtInstallmentNo(Integer txtInstallmentNo) {
        this.txtInstallmentNo = txtInstallmentNo;
        setChanged();
    }

    // Setter method for txtSchemeName
    void setTxtSchemeName(String txtSchemeName){
        this.txtSchemeName = txtSchemeName;
        setChanged();
    }
    // Getter method for txtSchemeName
    String getTxtSchemeName(){
        return this.txtSchemeName;
    }
    
    // Setter method for tdtDrawOrAuctionDt
    void setTdtDrawOrAuctionDt(String tdtDrawOrAuctionDt){
        this.tdtDrawOrAuctionDt = tdtDrawOrAuctionDt;
        setChanged();
    }
    // Getter method for tdtDrawOrAuctionDt
    String getTdtDrawOrAuctionDt(){
        return this.tdtDrawOrAuctionDt;
    }

    public Double getTxtPrizedAmount() {
        return txtPrizedAmount;
    }

    public void setTxtPrizedAmount(Double txtPrizedAmount) {
        this.txtPrizedAmount = txtPrizedAmount;
        setChanged();
    }

    public Double getTxtTotalBonusAmount() {
        return txtTotalBonusAmount;
    }

    public void setTxtTotalBonusAmount(Double txtTotalBonusAmount) {
        this.txtTotalBonusAmount = txtTotalBonusAmount;
        setChanged();
    }

    public Double getTxtNextBonusAmount() {
        return txtNextBonusAmount;
    }

    public void setTxtNextBonusAmount(Double txtNextBonusAmount) {
        this.txtNextBonusAmount = txtNextBonusAmount;
        setChanged();
    }

    public Double getTxtCommisionAmount() {
        return txtCommisionAmount;
    }

    public void setTxtCommisionAmount(Double txtCommisionAmount) {
        this.txtCommisionAmount = txtCommisionAmount;
        setChanged();
    }

    public Double getTxtTotalDiscount() {
        return txtTotalDiscount;
    }

    public void setTxtTotalDiscount(Double txtTotalDiscount) {
        this.txtTotalDiscount = txtTotalDiscount;
        setChanged();
    }

    public Double getTxtNetAmountPayable() {
        return txtNetAmountPayable;
    }

    public void setTxtNetAmountPayable(Double txtNetAmountPayable) {
        this.txtNetAmountPayable = txtNetAmountPayable;
        setChanged();
    }

    public Integer getTxtApplicationNo() {
        return txtApplicationNo;
    }

    public void setTxtApplicationNo(Integer txtApplicationNo) {
        this.txtApplicationNo = txtApplicationNo;
        setChanged();
    }

    public Integer getTxtInstallmentsDue() {
        return txtInstallmentsDue;
    }

    public void setTxtInstallmentsDue(Integer txtInstallmentsDue) {
        this.txtInstallmentsDue = txtInstallmentsDue;
        setChanged();
    }

    public Double getTxtInstallmentsAmountPaid() {
        return txtInstallmentsAmountPaid;
    }

    public void setTxtInstallmentsAmountPaid(Double txtInstallmentsAmountPaid) {
        this.txtInstallmentsAmountPaid = txtInstallmentsAmountPaid;
        setChanged();
    }

    public Double getTxtInstallmentsPaid() {
        return txtInstallmentsPaid;
    }

    public void setTxtInstallmentsPaid(Double txtInstallmentsPaid) {
        this.txtInstallmentsPaid = txtInstallmentsPaid;
        setChanged();
    }

    /**
     * Getter for property _authorizeMap.
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }
    
    /**
     * Setter for property _authorizeMap.
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
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
     * Getter for property lblStatus.
     * @return Value of property lblStatus.
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    
    /**
     * Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
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
    
    /**
     * Getter for property chkDraw.
     * @return Value of property chkDraw.
     */
    public java.lang.String getChkDraw() {
        return chkDraw;
    }
    
    /**
     * Setter for property chkDraw.
     * @param chkDraw New value of property chkDraw.
     */
    public void setChkDraw(java.lang.String chkDraw) {
        this.chkDraw = chkDraw;
    }
    
    /**
     * Getter for property chkAuction.
     * @return Value of property chkAuction.
     */
    public java.lang.String getChkAuction() {
        return chkAuction;
    }
    
    /**
     * Setter for property chkAuction.
     * @param chkAuction New value of property chkAuction.
     */
    public void setChkAuction(java.lang.String chkAuction) {
        this.chkAuction = chkAuction;
    }

    public String getChkUserDefined() {
        return chkUserDefined;
    }

    public void setChkUserDefined(String chkUserDefined) {
        this.chkUserDefined = chkUserDefined;
    }
    
    /**
     * Getter for property tdtNextInstallmentDt.
     * @return Value of property tdtNextInstallmentDt.
     */
    public java.lang.String getTdtNextInstallmentDt() {
        return tdtNextInstallmentDt;
    }
    
    /**
     * Setter for property tdtNextInstallmentDt.
     * @param tdtNextInstallmentDt New value of property tdtNextInstallmentDt.
     */
    public void setTdtNextInstallmentDt(java.lang.String tdtNextInstallmentDt) {
        this.tdtNextInstallmentDt = tdtNextInstallmentDt;
    }
    
    /**
     * Getter for property txtChittalNo.
     * @return Value of property txtChittalNo.
     */
    public java.lang.String getTxtChittalNo() {
        return txtChittalNo;
    }
    
    /**
     * Setter for property txtChittalNo.
     * @param txtChittalNo New value of property txtChittalNo.
     */
    public void setTxtChittalNo(java.lang.String txtChittalNo) {
        this.txtChittalNo = txtChittalNo;
    }
    
    /**
     * Getter for property lblMembType.
     * @return Value of property lblMembType.
     */
    public java.lang.String getLblMembType() {
        return lblMembType;
    }
    
    /**
     * Setter for property lblMembType.
     * @param lblMembType New value of property lblMembType.
     */
    public void setLblMembType(java.lang.String lblMembType) {
        this.lblMembType = lblMembType;
    }
    
    /**
     * Getter for property lblMemberName.
     * @return Value of property lblMemberName.
     */
    public java.lang.String getLblMemberName() {
        return lblMemberName;
    }
    
    /**
     * Setter for property lblMemberName.
     * @param lblMemberName New value of property lblMemberName.
     */
    public void setLblMemberName(java.lang.String lblMemberName) {
        this.lblMemberName = lblMemberName;
    }

    public Double getTxtInstallmentOverDueAmount() {
        return txtInstallmentOverDueAmount;
    }

    public void setTxtInstallmentOverDueAmount(Double txtInstallmentOverDueAmount) {
        this.txtInstallmentOverDueAmount = txtInstallmentOverDueAmount;
    }

    /**
     * Getter for property tdtPayementDate.
     * @return Value of property tdtPayementDate.
     */
    public java.lang.String getTdtPayementDate() {
        return tdtPayementDate;
    }
    
    /**
     * Setter for property tdtPayementDate.
     * @param tdtPayementDate New value of property tdtPayementDate.
     */
    public void setTdtPayementDate(java.lang.String tdtPayementDate) {
        this.tdtPayementDate = tdtPayementDate;
    }
    
    /**
     * Getter for property lblHouseStNo.
     * @return Value of property lblHouseStNo.
     */
    public java.lang.String getLblHouseStNo() {
        return lblHouseStNo;
    }
    
    /**
     * Setter for property lblHouseStNo.
     * @param lblHouseStNo New value of property lblHouseStNo.
     */
    public void setLblHouseStNo(java.lang.String lblHouseStNo) {
        this.lblHouseStNo = lblHouseStNo;
    }
    
    /**
     * Getter for property lblArea.
     * @return Value of property lblArea.
     */
    public java.lang.String getLblArea() {
        return lblArea;
    }
    
    /**
     * Setter for property lblArea.
     * @param lblArea New value of property lblArea.
     */
    public void setLblArea(java.lang.String lblArea) {
        this.lblArea = lblArea;
    }
    
    /**
     * Getter for property lblCity.
     * @return Value of property lblCity.
     */
    public java.lang.String getLblCity() {
        return lblCity;
    }
    
    /**
     * Setter for property lblCity.
     * @param lblCity New value of property lblCity.
     */
    public void setLblCity(java.lang.String lblCity) {
        this.lblCity = lblCity;
    }
    
    /**
     * Getter for property lblState.
     * @return Value of property lblState.
     */
    public java.lang.String getLblState() {
        return lblState;
    }
    
    /**
     * Setter for property lblState.
     * @param lblState New value of property lblState.
     */
    public void setLblState(java.lang.String lblState) {
        this.lblState = lblState;
    }
    
    /**
     * Getter for property lblpin.
     * @return Value of property lblpin.
     */
    public java.lang.String getLblpin() {
        return lblpin;
    }
    
    /**
     * Setter for property lblpin.
     * @param lblpin New value of property lblpin.
     */
    public void setLblpin(java.lang.String lblpin) {
        this.lblpin = lblpin;
    }
    
    /**
     * Getter for property holidayType.
     * @return Value of property holidayType.
     */
    public java.lang.String getHolidayType() {
        return holidayType;
    }
    
    /**
     * Setter for property holidayType.
     * @param holidayType New value of property holidayType.
     */
    public void setHolidayType(java.lang.String holidayType) {
        this.holidayType = holidayType;
    }

    public Double getTotalAmtDivision() {
        return totalAmtDivision;
    }

    public void setTotalAmtDivision(Double totalAmtDivision) {
        this.totalAmtDivision = totalAmtDivision;
    }

    public Double getAuctionMaxAmt() {
        return auctionMaxAmt;
    }

    public void setAuctionMaxAmt(Double auctionMaxAmt) {
        this.auctionMaxAmt = auctionMaxAmt;
    }

    public Double getAuctionMinAmt() {
        return auctionMinAmt;
    }

    public void setAuctionMinAmt(Double auctionMinAmt) {
        this.auctionMinAmt = auctionMinAmt;
    }

    public Integer getNoOfMemberPerDiv() {
        return noOfMemberPerDiv;
    }

    public void setNoOfMemberPerDiv(Integer noOfMemberPerDiv) {
        this.noOfMemberPerDiv = noOfMemberPerDiv;
    }

    /**
     * Getter for property commisionType.
     * @return Value of property commisionType.
     */
    public java.lang.String getCommisionType() {
        return commisionType;
    }
    
    /**
     * Setter for property commisionType.
     * @param commisionType New value of property commisionType.
     */
    public void setCommisionType(java.lang.String commisionType) {
        this.commisionType = commisionType;
    }

    public Double getCommisionRate() {
        return commisionRate;
    }

    public void setCommisionRate(Double commisionRate) {
        this.commisionRate = commisionRate;
    }

    public Integer getNoOfDivision() {
        return noOfDivision;
    }

    public void setNoOfDivision(Integer noOfDivision) {
        this.noOfDivision = noOfDivision;
    }

    public Integer getTxtSubNo() {
        return txtSubNo;
    }

    public void setTxtSubNo(Integer txtSubNo) {
        this.txtSubNo = txtSubNo;
    }

    /**
     * Getter for property multipleMember.
     * @return Value of property multipleMember.
     */
    public java.lang.String getMultipleMember() {
        return multipleMember;
    }
    
    /**
     * Setter for property multipleMember.
     * @param multipleMember New value of property multipleMember.
     */
    public void setMultipleMember(java.lang.String multipleMember) {
        this.multipleMember = multipleMember;
    }
    
    /**
     * Getter for property oldChittalNo.
     * @return Value of property oldChittalNo.
     */
    public java.lang.String getOldChittalNo() {
        return oldChittalNo;
    }
    
    /**
     * Setter for property oldChittalNo.
     * @param oldChittalNo New value of property oldChittalNo.
     */
    public void setOldChittalNo(java.lang.String oldChittalNo) {
        this.oldChittalNo = oldChittalNo;
    }

    public Integer getOldSubNo() {
        return oldSubNo;
    }

    public void setOldSubNo(Integer oldSubNo) {
        this.oldSubNo = oldSubNo;
    }

    public String getChkTransaction() {
        return chkTransaction;
    }

    public void setChkTransaction(String chkTransaction) {
        this.chkTransaction = chkTransaction;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getPredefinedInstall() {
        return predefinedInstall;
    }

    public void setPredefinedInstall(String predefinedInstall) {
        this.predefinedInstall = predefinedInstall;
    }

    public String getChkNoAuction() {
        return chkNoAuction;
    }

    public void setChkNoAuction(String chkNoAuction) {
        this.chkNoAuction = chkNoAuction;
    }
    
     
    public boolean calculationDateOnEditMode(){
        boolean flag = false;
        HashMap schemeMap = new HashMap();
        schemeMap.put("SCHEME_NAME",getTxtSchemeName());
        List lst = ClientUtil.executeQuery("getSchemeNameDetailsProductLevel",schemeMap);
        if(lst!=null && lst.size()>0){
            schemeMap = (HashMap)lst.get(0);
            int instFreq=0;
            Date schemeStartDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(schemeMap.get("SCHEME_START_DT")));
            auctDay = CommonUtil.convertObjToInt(schemeMap.get("DRAW_AUCT_DAY"));
            int instDay = CommonUtil.convertObjToInt(schemeMap.get("INSTALLMENT_DAY"));
            int instPayDay = CommonUtil.convertObjToInt(schemeMap.get("DAY_PAYMENT"));
            int inst = CommonUtil.convertObjToInt(schemeMap.get("INSTALLMENTS"));
            String holidayType = CommonUtil.convertObjToStr(schemeMap.get("HOLIDAY_INT"));
            setHolidayType(CommonUtil.convertObjToStr(schemeMap.get("HOLIDAY_INT")));
            int dayPayment = CommonUtil.convertObjToInt(schemeMap.get("DAY_PAYMENT"));
            setTotalAmtDivision(CommonUtil.convertObjToDouble(schemeMap.get("TOTAL_AMOUNT_DIVISION"))); //AJITH
            setAuctionMaxAmt(CommonUtil.convertObjToDouble(schemeMap.get("AUCTION_MAXAMT")));   //AJITH
            setAuctionMinAmt(CommonUtil.convertObjToDouble(schemeMap.get("AUCTION_MINAMT")));   //AJITH
            setNoOfMemberPerDiv(CommonUtil.convertObjToInt(schemeMap.get("NO_OF_MEMBER_PER_DIVISION")));    //AJITH
            setCommisionType(CommonUtil.convertObjToStr(schemeMap.get("COMMISION_RATE_TYPE")));
            setCommisionRate(CommonUtil.convertObjToDouble(schemeMap.get("COMMISION_RATE_AMT")));   //AJITH
            setNoOfDivision(CommonUtil.convertObjToInt(schemeMap.get("NO_OF_DIVISIONS")));  //AJITH
            instFreq = CommonUtil.convertObjToInt(schemeMap.get("INSTALLMENT_FREQUENCY"));
            //System.out.println("#######getHolidayType"+getHolidayType());        
        }   
        return flag;
    }

    public String getTxtWitness1Chittal() {
        return txtWitness1Chittal;
    }

    public void setTxtWitness1Chittal(String txtWitness1Chittal) {
        this.txtWitness1Chittal = txtWitness1Chittal;
    }

    public String getTxtWitness1CustId() {
        return txtWitness1CustId;
    }

    public void setTxtWitness1CustId(String txtWitness1CustId) {
        this.txtWitness1CustId = txtWitness1CustId;
    }

    public String getTxtWitness2Chittal() {
        return txtWitness2Chittal;
    }

    public void setTxtWitness2Chittal(String txtWitness2Chittal) {
        this.txtWitness2Chittal = txtWitness2Chittal;
    }

    public String getTxtWitness2CustId() {
        return txtWitness2CustId;
    }

    public void setTxtWitness2CustId(String txtWitness2CustId) {
        this.txtWitness2CustId = txtWitness2CustId;
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
    
}