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

package com.see.truetransact.ui.gdsapplication.gdsprizedmoneydetailsentry;

import com.see.truetransact.ui.mdsapplication.mdsprizedmoneydetailsentry.*;
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
import com.see.truetransact.transferobject.gdsapplication.gdsprizedmoneydetailsentry.GDSPrizedMoneyDetailsEntryTO;
import com.see.truetransact.transferobject.mdsapplication.mdsprizedmoneydetailsentry.MDSPrizedMoneyDetailsEntryTO;
import java.util.Date;
/**
 *
 * @author
 */

public class GDSPrizedMoneyDetailsEntryOB extends CObservable{
    
    private String txtDivisionNo = "";
    private String txtInstallmentNo = "";
    private String txtSchemeName = "";
    private String tdtDrawOrAuctionDt = "";
    private String txtPrizedAmount = "";
    private String txtTotalBonusAmount = "";
    private String txtNextBonusAmount = "";
    private String txtCommisionAmount = "";
    private String txtTotalDiscount = "";
    private String txtNetAmountPayable = "";
    private String txtApplicationNo = "";
    private String lblMembType = "";
    private String lblMemberName = "";
    private String txtChittalNo = "";
    private String txtInstallmentsDue = "";
    private String txtInstallmentsAmountPaid = "";
    private String txtInstallmentsPaid = "";
    private String chkDraw = "";
    private String chkAuction = "";
    private String chkUserDefined = "";
    private String tdtNextInstallmentDt = "";
    private String tdtPayementDate = "";
    private String txtInstallmentOverDueAmount = "";
    private String txtSubNo = "";
    private String oldChittalNo = "";
    private String oldSubNo = "";
    private String gds_no = "";
    
    private String totalAmtDivision = "";
    private String auctionMaxAmt = "";
    private String auctionMinAmt = "";
    private String noOfMemberPerDiv = "";
    private String commisionType = "";
    private String commisionRate = "";
    private String noOfDivision = "";
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
    private final static Logger log = Logger.getLogger(GDSPrizedMoneyDetailsEntryOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private GDSPrizedMoneyDetailsEntryTO objPrizedMoneyDetailsEntryTO;
    private Date currDt = null;
    private String chkTransaction = "";
    private String transId = "";
    private String predefinedInstall = "";
    private String chkNoAuction= "";
    private String commonScheme = "";
    
    /** Creates a new instance of TDS MiantenanceOB */
    public GDSPrizedMoneyDetailsEntryOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "GDSPrizedMoneyDetailsEntryJNDI");
            map.put(CommonConstants.HOME, "gdsapplication.gdsprizedmoneydetailsentry.GDSPrizedMoneyDetailsEntryHome");
            map.put(CommonConstants.REMOTE, "gdsapplication.gdsprizedmoneydetailsentry.GDSPrizedMoneyDetailsEntry");
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
                data.put("GDSPrizedMoneyDetailsEntry",setPrizedMoneyDetailsEntryDataNoAuction());
            }else{
                data.put("GDSPrizedMoneyDetailsEntry",setPrizedMoneyDetailsEntryData());
            }
        }
        else{
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        System.out.println("data in FixedAssets OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        System.out.println("data in proxyResultMap OB : " + proxyResultMap);
        setProxyReturnMap(proxyResultMap);
        _authorizeMap = null;
        setResult(getActionType());
    }
    
    /** To populate data into the screen */
    public GDSPrizedMoneyDetailsEntryTO setPrizedMoneyDetailsEntryData() {
        
        final GDSPrizedMoneyDetailsEntryTO objMDSPrizedMoneyDetailsEntryTO = new GDSPrizedMoneyDetailsEntryTO();
        try{
            objMDSPrizedMoneyDetailsEntryTO.setMdsSchemeName(getTxtSchemeName());
            objMDSPrizedMoneyDetailsEntryTO.setGroup_no(getTxtSchemeName());
            objMDSPrizedMoneyDetailsEntryTO.setGds_no(getTxtChittalNo());
            objMDSPrizedMoneyDetailsEntryTO.setDrawAuctionDate(DateUtil.getDateMMDDYYYY(getTdtDrawOrAuctionDt()));
            objMDSPrizedMoneyDetailsEntryTO.setInstallmentNo(CommonUtil.convertObjToInt(getTxtInstallmentNo()));
            objMDSPrizedMoneyDetailsEntryTO.setDivisionNo(CommonUtil.convertObjToInt(getTxtDivisionNo()));
            objMDSPrizedMoneyDetailsEntryTO.setDraw(getChkDraw());
            objMDSPrizedMoneyDetailsEntryTO.setAuction(getChkAuction());
            objMDSPrizedMoneyDetailsEntryTO.setUserDefined(getChkUserDefined());
            objMDSPrizedMoneyDetailsEntryTO.setNextInstallmentDate(DateUtil.getDateMMDDYYYY(getTdtNextInstallmentDt()));
            objMDSPrizedMoneyDetailsEntryTO.setPrizedAmount(CommonUtil.convertObjToDouble(getTxtPrizedAmount()));
            objMDSPrizedMoneyDetailsEntryTO.setTotalBonusAmount(CommonUtil.convertObjToDouble(getTxtTotalBonusAmount()));
            objMDSPrizedMoneyDetailsEntryTO.setNextBonusAmount(CommonUtil.convertObjToDouble(getTxtNextBonusAmount()));
            objMDSPrizedMoneyDetailsEntryTO.setCommisionAmount(CommonUtil.convertObjToDouble(getTxtCommisionAmount()));
            objMDSPrizedMoneyDetailsEntryTO.setTotalDiscount(CommonUtil.convertObjToDouble(getTxtTotalDiscount()));
            objMDSPrizedMoneyDetailsEntryTO.setNetAmountPayable(CommonUtil.convertObjToDouble(getTxtNetAmountPayable()));
            objMDSPrizedMoneyDetailsEntryTO.setAppNo(CommonUtil.convertObjToInt(getTxtApplicationNo()));
            objMDSPrizedMoneyDetailsEntryTO.setChittalNo(getTxtChittalNo());
            objMDSPrizedMoneyDetailsEntryTO.setMemberType(getLblMembType());
            objMDSPrizedMoneyDetailsEntryTO.setMemberName(getLblMemberName());
            objMDSPrizedMoneyDetailsEntryTO.setInstallmentPaid(CommonUtil.convertObjToDouble(getTxtInstallmentsPaid()));
            objMDSPrizedMoneyDetailsEntryTO.setInstallAmountPaid(CommonUtil.convertObjToDouble(getTxtInstallmentsAmountPaid()));
            objMDSPrizedMoneyDetailsEntryTO.setInstallmentDue(CommonUtil.convertObjToInt(getTxtInstallmentsDue()));
            objMDSPrizedMoneyDetailsEntryTO.setInstalOverdueAmt(CommonUtil.convertObjToDouble(getTxtInstallmentOverDueAmount()));
            objMDSPrizedMoneyDetailsEntryTO.setSubNo(CommonUtil.convertObjToInt(getTxtSubNo()));
            objMDSPrizedMoneyDetailsEntryTO.setStatus(getAction());
            objMDSPrizedMoneyDetailsEntryTO.setStatusBy(TrueTransactMain.USER_ID);
            objMDSPrizedMoneyDetailsEntryTO.setStatusDt(ClientUtil.getCurrentDateWithTime());
            objMDSPrizedMoneyDetailsEntryTO.setPaymentDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtPayementDate())));
            //Set Old Chittal No
            objMDSPrizedMoneyDetailsEntryTO.setOldChittalNo(getOldChittalNo());
            objMDSPrizedMoneyDetailsEntryTO.setOldSubNo(CommonUtil.convertObjToInt(getOldSubNo()));
            objMDSPrizedMoneyDetailsEntryTO.setNoOfMemberPerDiv(getNoOfMemberPerDiv());
            objMDSPrizedMoneyDetailsEntryTO.setBranchId(TrueTransactMain.BRANCH_ID);
            objMDSPrizedMoneyDetailsEntryTO.setAuctionTrans(getChkTransaction());
            if(getPredefinedInstall()!=null && !getPredefinedInstall().equals("")){
                objMDSPrizedMoneyDetailsEntryTO.setPredefinedInstall(getPredefinedInstall());
            }else{
                objMDSPrizedMoneyDetailsEntryTO.setPredefinedInstall("N");
            }
        }catch(Exception e){
            log.info("Error In setMDSPrizedMoneyDetailsEntryTOData()");
            e.printStackTrace();
        }
        return objMDSPrizedMoneyDetailsEntryTO;
    }
    
        public GDSPrizedMoneyDetailsEntryTO setPrizedMoneyDetailsEntryDataNoAuction() {
        
        final GDSPrizedMoneyDetailsEntryTO objMDSPrizedMoneyDetailsEntryTO = new GDSPrizedMoneyDetailsEntryTO();
        try{
            objMDSPrizedMoneyDetailsEntryTO.setNoOfMemberPerDiv(getNoOfMemberPerDiv());
            objMDSPrizedMoneyDetailsEntryTO.setMdsSchemeName(getTxtSchemeName());
            objMDSPrizedMoneyDetailsEntryTO.setGroup_no(getTxtSchemeName());
            objMDSPrizedMoneyDetailsEntryTO.setGds_no(getTxtChittalNo());
            objMDSPrizedMoneyDetailsEntryTO.setDrawAuctionDate(DateUtil.getDateMMDDYYYY(getTdtDrawOrAuctionDt()));
            objMDSPrizedMoneyDetailsEntryTO.setInstallmentNo(CommonUtil.convertObjToInt(getTxtInstallmentNo()));
            objMDSPrizedMoneyDetailsEntryTO.setDivisionNo(CommonUtil.convertObjToInt(getTxtDivisionNo()));
            objMDSPrizedMoneyDetailsEntryTO.setDraw(getChkDraw());
            objMDSPrizedMoneyDetailsEntryTO.setAuction(getChkAuction());
            objMDSPrizedMoneyDetailsEntryTO.setUserDefined(getChkUserDefined());
            objMDSPrizedMoneyDetailsEntryTO.setNextInstallmentDate(DateUtil.getDateMMDDYYYY(getTdtNextInstallmentDt()));
            objMDSPrizedMoneyDetailsEntryTO.setPrizedAmount(CommonUtil.convertObjToDouble(getTxtPrizedAmount()));
            objMDSPrizedMoneyDetailsEntryTO.setTotalBonusAmount(CommonUtil.convertObjToDouble(getTxtTotalBonusAmount()));
            objMDSPrizedMoneyDetailsEntryTO.setNextBonusAmount(CommonUtil.convertObjToDouble(getTxtNextBonusAmount()));
            objMDSPrizedMoneyDetailsEntryTO.setCommisionAmount(CommonUtil.convertObjToDouble(getTxtCommisionAmount()));
            objMDSPrizedMoneyDetailsEntryTO.setTotalDiscount(CommonUtil.convertObjToDouble(getTxtTotalDiscount()));
            objMDSPrizedMoneyDetailsEntryTO.setNetAmountPayable(CommonUtil.convertObjToDouble(getTxtNetAmountPayable()));
            objMDSPrizedMoneyDetailsEntryTO.setAppNo(CommonUtil.convertObjToInt(getTxtApplicationNo()));
            objMDSPrizedMoneyDetailsEntryTO.setChittalNo(getTxtChittalNo());
            objMDSPrizedMoneyDetailsEntryTO.setMemberType(getLblMembType());
            objMDSPrizedMoneyDetailsEntryTO.setMemberName(getLblMemberName());
            objMDSPrizedMoneyDetailsEntryTO.setInstallmentPaid(CommonUtil.convertObjToDouble(getTxtInstallmentsPaid()));
            objMDSPrizedMoneyDetailsEntryTO.setInstallAmountPaid(CommonUtil.convertObjToDouble(getTxtInstallmentsAmountPaid()));
            objMDSPrizedMoneyDetailsEntryTO.setInstallmentDue(CommonUtil.convertObjToInt(getTxtInstallmentsDue()));
            objMDSPrizedMoneyDetailsEntryTO.setInstalOverdueAmt(CommonUtil.convertObjToDouble(getTxtInstallmentOverDueAmount()));
            objMDSPrizedMoneyDetailsEntryTO.setSubNo(CommonUtil.convertObjToInt(getTxtSubNo()));
            objMDSPrizedMoneyDetailsEntryTO.setStatus(getAction());
            objMDSPrizedMoneyDetailsEntryTO.setStatusBy(TrueTransactMain.USER_ID);
            objMDSPrizedMoneyDetailsEntryTO.setStatusDt(ClientUtil.getCurrentDateWithTime());
            objMDSPrizedMoneyDetailsEntryTO.setPaymentDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtPayementDate())));
            //Set Old Chittal No
            objMDSPrizedMoneyDetailsEntryTO.setOldChittalNo(getOldChittalNo());
            objMDSPrizedMoneyDetailsEntryTO.setOldSubNo(CommonUtil.convertObjToInt(getOldSubNo()));
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
        }catch(Exception e){
            log.info("Error In setMDSPrizedMoneyDetailsEntryTOData()");
            e.printStackTrace();
        }
        return objMDSPrizedMoneyDetailsEntryTO;
    }
        
    protected void getCustomerAddressDetails(){
        HashMap custAddressMap = new HashMap();
        custAddressMap.put("GDS_NO",getGds_no());
        custAddressMap.put("GROUP_NO",getTxtSchemeName());
        custAddressMap.put("CHITTAL_NO",getTxtChittalNo());
        
        custAddressMap.put("SUB_NO",CommonUtil.convertObjToInt(getTxtSubNo()));
        System.out.println("custAddressMap :: "+custAddressMap);
        List lst = ClientUtil.executeQuery("getGDSCustomerAddressDetailsinAppln",custAddressMap);
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
        System.out.println("Inside calculationDate()");
        boolean flag = false;
        HashMap schemeMap = new HashMap();
        schemeMap.put("SCHEME_NAME",getCommonScheme());
        schemeMap.put("GROUP_NO",getTxtSchemeName());
        List lst = ClientUtil.executeQuery("getGDSSchemeNameDetailsProductLevel",schemeMap);
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
            setTotalAmtDivision(CommonUtil.convertObjToStr(schemeMap.get("TOTAL_AMOUNT_DIVISION")));
            setAuctionMaxAmt(CommonUtil.convertObjToStr(schemeMap.get("AUCTION_MAXAMT")));
            setAuctionMinAmt(CommonUtil.convertObjToStr(schemeMap.get("AUCTION_MINAMT")));
            setNoOfMemberPerDiv(CommonUtil.convertObjToStr(schemeMap.get("NO_OF_MEMBER_PER_DIVISION")));
            setCommisionType(CommonUtil.convertObjToStr(schemeMap.get("COMMISION_RATE_TYPE")));
            setCommisionRate(CommonUtil.convertObjToStr(schemeMap.get("COMMISION_RATE_AMT")));
            setNoOfDivision(CommonUtil.convertObjToStr(schemeMap.get("NO_OF_DIVISIONS")));
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
            if(whereMap.containsKey("SUB_NO") && whereMap.get("SUB_NO") != null){
                whereMap.put("SUB_NO",CommonUtil.convertObjToInt(whereMap.get("SUB_NO")));
            }
            final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
            System.out.println("#@@%@@#%@#data"+data);
            List prizedLIst=(List)data.get("PrizedMoneyDetailsTO");
            int sizeOfprizedLIst=prizedLIst.size();
            objPrizedMoneyDetailsEntryTO = (GDSPrizedMoneyDetailsEntryTO) ((List) data.get("PrizedMoneyDetailsTO")).get(0);
            System.out.println("objPrizedMoneyDetailsEntryTO... "+ objPrizedMoneyDetailsEntryTO);
            setTxtChittalNo(CommonUtil.convertObjToStr(objPrizedMoneyDetailsEntryTO.getGds_no()));
            objPrizedMoneyDetailsEntryTO.setPrizedAmount(CommonUtil.convertObjToDouble(objPrizedMoneyDetailsEntryTO.getPrizedAmount()*sizeOfprizedLIst));
            objPrizedMoneyDetailsEntryTO.setTotalBonusAmount(CommonUtil.convertObjToDouble(objPrizedMoneyDetailsEntryTO.getTotalBonusAmount()*sizeOfprizedLIst));
            objPrizedMoneyDetailsEntryTO.setNextBonusAmount(CommonUtil.convertObjToDouble(objPrizedMoneyDetailsEntryTO.getNextBonusAmount()*sizeOfprizedLIst));
            objPrizedMoneyDetailsEntryTO.setCommisionAmount(CommonUtil.convertObjToDouble(objPrizedMoneyDetailsEntryTO.getCommisionAmount()*sizeOfprizedLIst));
            objPrizedMoneyDetailsEntryTO.setTotalDiscount(CommonUtil.convertObjToDouble(objPrizedMoneyDetailsEntryTO.getTotalDiscount()*sizeOfprizedLIst));
            objPrizedMoneyDetailsEntryTO.setNetAmountPayable(CommonUtil.convertObjToDouble(objPrizedMoneyDetailsEntryTO.getNetAmountPayable()*sizeOfprizedLIst));
            objPrizedMoneyDetailsEntryTO.setInstallAmountPaid(CommonUtil.convertObjToDouble(objPrizedMoneyDetailsEntryTO.getInstallAmountPaid())*sizeOfprizedLIst);
            objPrizedMoneyDetailsEntryTO.setInstallmentDue(CommonUtil.convertObjToInt(objPrizedMoneyDetailsEntryTO.getInstallmentDue())*sizeOfprizedLIst);
            objPrizedMoneyDetailsEntryTO.setInstalOverdueAmt(CommonUtil.convertObjToDouble(objPrizedMoneyDetailsEntryTO.getInstalOverdueAmt())*sizeOfprizedLIst);
            populatePrizedMoneyDetailsData(objPrizedMoneyDetailsEntryTO);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populatePrizedMoneyDetailsData(GDSPrizedMoneyDetailsEntryTO objPrizedTO) throws Exception{
        System.out.println("objPrizedTO.. "+ objPrizedTO);        
        HashMap whrMap = new HashMap();
        whrMap.put("GDS_NO", objPrizedTO.getGds_no());       
        List grpLst = ClientUtil.executeQuery("getSelectGroupNameTO", whrMap);
        HashMap grpLstMap = (HashMap)grpLst.get(0);
        this.setTxtSchemeName(CommonUtil.convertObjToStr(grpLstMap.get("GROUP_NO")));
        this.setTdtDrawOrAuctionDt(CommonUtil.convertObjToStr(objPrizedTO.getDrawAuctionDate()));
        this.setTxtInstallmentNo(CommonUtil.convertObjToStr(objPrizedTO.getInstallmentNo()));
        this.setTxtDivisionNo(CommonUtil.convertObjToStr(objPrizedTO.getDivisionNo()));
        this.setChkDraw(CommonUtil.convertObjToStr(objPrizedTO.getDraw()));
        this.setChkAuction(CommonUtil.convertObjToStr(objPrizedTO.getAuction()));
        this.setChkUserDefined(CommonUtil.convertObjToStr(objPrizedTO.getUserDefined()));
        this.setTdtNextInstallmentDt(CommonUtil.convertObjToStr(objPrizedTO.getNextInstallmentDate()));
        this.setTxtPrizedAmount(CommonUtil.convertObjToStr(objPrizedTO.getPrizedAmount()));
        this.setTxtTotalBonusAmount(CommonUtil.convertObjToStr(objPrizedTO.getTotalBonusAmount()));
        this.setTxtNextBonusAmount(CommonUtil.convertObjToStr(objPrizedTO.getNextBonusAmount()));
        this.setTxtCommisionAmount(CommonUtil.convertObjToStr(objPrizedTO.getCommisionAmount()));
        this.setTxtTotalDiscount(CommonUtil.convertObjToStr(objPrizedTO.getTotalDiscount()));
        this.setTxtNetAmountPayable(CommonUtil.convertObjToStr(objPrizedTO.getNetAmountPayable()));
        this.setTxtApplicationNo(CommonUtil.convertObjToStr(objPrizedTO.getAppNo()));
        this.setTxtChittalNo(CommonUtil.convertObjToStr(objPrizedTO.getGds_no()));
        this.setLblMembType(CommonUtil.convertObjToStr(objPrizedTO.getMemberType()));
        this.setLblMemberName(CommonUtil.convertObjToStr(objPrizedTO.getMemberName()));
        this.setTxtInstallmentsPaid(CommonUtil.convertObjToStr(objPrizedTO.getInstallmentPaid()));
        this.setTxtInstallmentsAmountPaid(CommonUtil.convertObjToStr(objPrizedTO.getInstallAmountPaid()));
        this.setTxtInstallmentsDue(CommonUtil.convertObjToStr(objPrizedTO.getInstallmentDue()));
        this.setTxtInstallmentOverDueAmount(CommonUtil.convertObjToStr(objPrizedTO.getInstalOverdueAmt()));
        this.setTxtSubNo(CommonUtil.convertObjToStr(objPrizedTO.getSubNo()));
        this.setTdtPayementDate(CommonUtil.convertObjToStr(objPrizedTO.getPaymentDate()));
        setChanged();
        notifyObservers();
    }
    
    public void resetForm() {
        setTxtInstallmentNo("");
        setPredefinedInstall("");
    }
    
    
    public void setReceiptDetails(HashMap map){
        HashMap chittalMap = new HashMap();
        long count =0;
        chittalMap.put("CHITTAL_NO",getTxtChittalNo());
        chittalMap.put("GDS_NO",map.get("GDS_NO"));
        System.out.println(map+"   map"); 
       //chittalMap.put("SUB_NO",CommonUtil.convertObjToStr(getTxtSubNo()));
       chittalMap.put("SUB_NO",CommonUtil.convertObjToInt(getTxtSubNo()));// Added by nithya on 16-01-2020 for KD-1279
        List lst = ClientUtil.executeQuery("getGDSSelctApplnReceiptDetails", chittalMap);
        if(lst!=null && lst.size()>0){
            chittalMap = (HashMap)lst.get(0);            
            Date currDate = (Date) currDt.clone();
            int instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
            Date startDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalMap.get("SCHEME_START_DT")));
            setNoOfDivision(CommonUtil.convertObjToStr(chittalMap.get("NO_OF_DIVISIONS")));
            HashMap whereMap = new HashMap();
            whereMap.put("CHITTAL_NO",getTxtChittalNo());
            whereMap.put("SUB_NO",CommonUtil.convertObjToInt(getTxtSubNo()));
            List list = ClientUtil.executeQuery("getGDSTotalInstAmount", map);
            if(list!=null && list.size()>0){
                whereMap = (HashMap)list.get(0);
                setTxtInstallmentsPaid(CommonUtil.convertObjToStr(Math.round(CommonUtil.convertObjToDouble(whereMap.get("NO_INST_PAID")))));
                setTxtInstallmentsAmountPaid(CommonUtil.convertObjToStr(whereMap.get("PAID_AMT")));
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
            
            if(stYear == currYear && stMonth == currMonth){
                pending = 0;
                setTxtInstallmentsDue(String.valueOf("0"));
            }else if(stYear == currYear && stMonth != currMonth){
                pending = 0;
                value = currMonth - stMonth+1;
                int diffMonth = currMonth - stMonth;
                int pendingVal = diffMonth - (int)count;
                if(instDay<currDate.getDate()){
                    pending = pendingVal+1;
                }
                else{
                    pending = pendingVal;
                }
                if(pending>0)
                    setTxtInstallmentsDue(String.valueOf(pending));
                else
                    setTxtInstallmentsDue(String.valueOf(0));
            }else{
                int year = currYear - stYear;
                value = (year * 12) + currMonth - stMonth;
                int pendingVal = value - (int)count;
                if(instDay<currDate.getDate()){
                    pending = pendingVal+1;
                }
                else{
                    pending = pendingVal;
                }
                if(pending>0)
                    setTxtInstallmentsDue(String.valueOf(pending));
                else
                    setTxtInstallmentsDue(String.valueOf(0));
            }
            setTxtInstallmentOverDueAmount(String.valueOf(CommonUtil.convertObjToLong(getTxtInstallmentsDue()) * instAmt));
        }
    }
    
    // Setter method for txtDivisionNo
    void setTxtDivisionNo(String txtDivisionNo){
        this.txtDivisionNo = txtDivisionNo;
        setChanged();
    }
    // Getter method for txtDivisionNo
    String getTxtDivisionNo(){
        return this.txtDivisionNo;
    }
    
    // Setter method for txtInstallmentNo
    void setTxtInstallmentNo(String txtInstallmentNo){
        this.txtInstallmentNo = txtInstallmentNo;
        setChanged();
    }
    // Getter method for txtInstallmentNo
    String getTxtInstallmentNo(){
        return this.txtInstallmentNo;
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
    
    // Setter method for txtPrizedAmount
    void setTxtPrizedAmount(String txtPrizedAmount){
        this.txtPrizedAmount = txtPrizedAmount;
        setChanged();
    }
    // Getter method for txtPrizedAmount
    String getTxtPrizedAmount(){
        return this.txtPrizedAmount;
    }
    
    // Setter method for txtTotalBonusAmount
    void setTxtTotalBonusAmount(String txtTotalBonusAmount){
        this.txtTotalBonusAmount = txtTotalBonusAmount;
        setChanged();
    }
    // Getter method for txtTotalBonusAmount
    String getTxtTotalBonusAmount(){
        return this.txtTotalBonusAmount;
    }
    
    // Setter method for txtNextBonusAmount
    void setTxtNextBonusAmount(String txtNextBonusAmount){
        this.txtNextBonusAmount = txtNextBonusAmount;
        setChanged();
    }
    // Getter method for txtNextBonusAmount
    String getTxtNextBonusAmount(){
        return this.txtNextBonusAmount;
    }
    
    // Setter method for txtCommisionAmount
    void setTxtCommisionAmount(String txtCommisionAmount){
        this.txtCommisionAmount = txtCommisionAmount;
        setChanged();
    }
    // Getter method for txtCommisionAmount
    String getTxtCommisionAmount(){
        return this.txtCommisionAmount;
    }
    
    // Setter method for txtTotalDiscount
    void setTxtTotalDiscount(String txtTotalDiscount){
        this.txtTotalDiscount = txtTotalDiscount;
        setChanged();
    }
    // Getter method for txtTotalDiscount
    String getTxtTotalDiscount(){
        return this.txtTotalDiscount;
    }
    
    // Setter method for txtNetAmountPayable
    void setTxtNetAmountPayable(String txtNetAmountPayable){
        this.txtNetAmountPayable = txtNetAmountPayable;
        setChanged();
    }
    // Getter method for txtNetAmountPayable
    String getTxtNetAmountPayable(){
        return this.txtNetAmountPayable;
    }
    
    // Setter method for txtApplicationNo
    void setTxtApplicationNo(String txtApplicationNo){
        this.txtApplicationNo = txtApplicationNo;
        setChanged();
    }
    // Getter method for txtApplicationNo
    String getTxtApplicationNo(){
        return this.txtApplicationNo;
    }
    
    
    
    
    // Setter method for txtInstallmentsDue
    void setTxtInstallmentsDue(String txtInstallmentsDue){
        this.txtInstallmentsDue = txtInstallmentsDue;
        setChanged();
    }
    // Getter method for txtInstallmentsDue
    String getTxtInstallmentsDue(){
        return this.txtInstallmentsDue;
    }
    
    // Setter method for txtInstallmentsAmountPaid
    void setTxtInstallmentsAmountPaid(String txtInstallmentsAmountPaid){
        this.txtInstallmentsAmountPaid = txtInstallmentsAmountPaid;
        setChanged();
    }
    // Getter method for txtInstallmentsAmountPaid
    String getTxtInstallmentsAmountPaid(){
        return this.txtInstallmentsAmountPaid;
    }
    
    // Setter method for txtInstallmentsPaid
    void setTxtInstallmentsPaid(String txtInstallmentsPaid){
        this.txtInstallmentsPaid = txtInstallmentsPaid;
        setChanged();
    }
    // Getter method for txtInstallmentsPaid
    String getTxtInstallmentsPaid(){
        return this.txtInstallmentsPaid;
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
    
    /**
     * Getter for property txtInstallmentOverDueAmount.
     * @return Value of property txtInstallmentOverDueAmount.
     */
    public java.lang.String getTxtInstallmentOverDueAmount() {
        return txtInstallmentOverDueAmount;
    }
    
    /**
     * Setter for property txtInstallmentOverDueAmount.
     * @param txtInstallmentOverDueAmount New value of property txtInstallmentOverDueAmount.
     */
    public void setTxtInstallmentOverDueAmount(java.lang.String txtInstallmentOverDueAmount) {
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
    
    /**
     * Getter for property totalAmtDivision.
     * @return Value of property totalAmtDivision.
     */
    public java.lang.String getTotalAmtDivision() {
        return totalAmtDivision;
    }
    
    /**
     * Setter for property totalAmtDivision.
     * @param totalAmtDivision New value of property totalAmtDivision.
     */
    public void setTotalAmtDivision(java.lang.String totalAmtDivision) {
        this.totalAmtDivision = totalAmtDivision;
    }
    
    /**
     * Getter for property auctionMaxAmt.
     * @return Value of property auctionMaxAmt.
     */
    public java.lang.String getAuctionMaxAmt() {
        return auctionMaxAmt;
    }
    
    /**
     * Setter for property auctionMaxAmt.
     * @param auctionMaxAmt New value of property auctionMaxAmt.
     */
    public void setAuctionMaxAmt(java.lang.String auctionMaxAmt) {
        this.auctionMaxAmt = auctionMaxAmt;
    }
    
    /**
     * Getter for property auctionMinAmt.
     * @return Value of property auctionMinAmt.
     */
    public java.lang.String getAuctionMinAmt() {
        return auctionMinAmt;
    }
    
    /**
     * Setter for property auctionMinAmt.
     * @param auctionMinAmt New value of property auctionMinAmt.
     */
    public void setAuctionMinAmt(java.lang.String auctionMinAmt) {
        this.auctionMinAmt = auctionMinAmt;
    }
    
    /**
     * Getter for property noOfMemberPerDiv.
     * @return Value of property noOfMemberPerDiv.
     */
    public java.lang.String getNoOfMemberPerDiv() {
        return noOfMemberPerDiv;
    }
    
    /**
     * Setter for property noOfMemberPerDiv.
     * @param noOfMemberPerDiv New value of property noOfMemberPerDiv.
     */
    public void setNoOfMemberPerDiv(java.lang.String noOfMemberPerDiv) {
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
    
    /**
     * Getter for property commisionRate.
     * @return Value of property commisionRate.
     */
    public java.lang.String getCommisionRate() {
        return commisionRate;
    }
    
    /**
     * Setter for property commisionRate.
     * @param commisionRate New value of property commisionRate.
     */
    public void setCommisionRate(java.lang.String commisionRate) {
        this.commisionRate = commisionRate;
    }
    
    /**
     * Getter for property noOfDivision.
     * @return Value of property noOfDivision.
     */
    public java.lang.String getNoOfDivision() {
        return noOfDivision;
    }
    
    /**
     * Setter for property noOfDivision.
     * @param noOfDivision New value of property noOfDivision.
     */
    public void setNoOfDivision(java.lang.String noOfDivision) {
        this.noOfDivision = noOfDivision;
    }
    
    /**
     * Getter for property txtSubNo.
     * @return Value of property txtSubNo.
     */
    public java.lang.String getTxtSubNo() {
        return txtSubNo;
    }
    
    /**
     * Setter for property txtSubNo.
     * @param txtSubNo New value of property txtSubNo.
     */
    public void setTxtSubNo(java.lang.String txtSubNo) {
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
    
    /**
     * Getter for property oldSubNo.
     * @return Value of property oldSubNo.
     */
    public java.lang.String getOldSubNo() {
        return oldSubNo;
    }
    
    /**
     * Setter for property oldSubNo.
     * @param oldSubNo New value of property oldSubNo.
     */
    public void setOldSubNo(java.lang.String oldSubNo) {
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

    public String getCommonScheme() {
        return commonScheme;
    }

    public void setCommonScheme(String commonScheme) {
        this.commonScheme = commonScheme;
    }

    public String getGds_no() {
        return gds_no;
    }

    public void setGds_no(String group_no) {
        this.gds_no = group_no;
    }
    
    
}