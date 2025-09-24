/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanCompanyOB.java
 *
 * Created on July 7, 2004, 10:42 AM
 */

package com.see.truetransact.ui.termloan.agritermloan;

/**
 *
 * @author  shanmuga
 *
 */

//import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.termloan.agritermloan.AgriTermLoanCompanyTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.Date;

import org.apache.log4j.Logger;


public class AgriTermLoanCompanyOB extends CObservable{
    
    /** Creates a new instance of TermLoanCompanyOB */
    private AgriTermLoanCompanyOB() throws Exception{
        curDate = ClientUtil.getCurrentDate();
        termLoanCompanyOB();
    }
    
    private       static AgriTermLoanCompanyOB termLoanCompanyOB;
    
    private final static Logger log = Logger.getLogger(AgriTermLoanCompanyOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
//    private final   java.util.ResourceBundle objTermLoanRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.TermLoanRB", ProxyParameters.LANGUAGE);
    
//    private final   TermLoanRB objTermLoanRB = new TermLoanRB();
    
    private ComboBoxModel cbmNatureBusiness;
    private ComboBoxModel cbmAddressType;
    private ComboBoxModel cbmCity_CompDetail;
    private ComboBoxModel cbmState_CompDetail;
    private ComboBoxModel cbmCountry_CompDetail;
    
    private String borrowerNo = "";
    private String txtCompanyRegisNo = "";
    private String tdtDateEstablishment = "";
    private String tdtDealingWithBankSince = "";
    private String txtRiskRating = "";
    private String cboNatureBusiness = "";
    private String txtRemarks__CompDetail = "";
    private String txtNetWorth = "";
    private String tdtAsOn = "";
    private String tdtCreditFacilityAvailSince = "";
    private String txtChiefExecutiveName = "";
    private String cboAddressType = "";
    private String txtStreet_CompDetail = "";
    private String txtArea_CompDetail = "";
    private String cboCity_CompDetail = "";
    private String cboState_CompDetail = "";
    private String cboCountry_CompDetail = "";
    private String txtPin_CompDetail = "";
    private String txtPhone_CompDetail = "";
    Date curDate = null;
    
    // This field contains Customer's Networth value in DataBase
    // Used to chk whether the Networth value is changed in Loan Account Level
    // If it is changed then update the value in Customer Table
    private String netWorthInCust = "";
    
    private void termLoanCompanyOB() throws Exception{
        
    }
    
    static {
        try {
            termLoanCompanyOB = new AgriTermLoanCompanyOB();
        } catch(Exception e) {
            log.info("try: " + e);
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    public static AgriTermLoanCompanyOB getInstance() {
        return termLoanCompanyOB;
    }
    
    public void resetCustomerDetails(){
        
        setTxtCompanyRegisNo("");
        setTxtRiskRating("");
        setCboNatureBusiness("");
        setTxtRemarks__CompDetail("");
        setTxtNetWorth("");
        setTxtChiefExecutiveName("");
        setCboAddressType("");
        setTxtStreet_CompDetail("");
        setTxtArea_CompDetail("");
        setCboCity_CompDetail("");
        setCboState_CompDetail("");
        setCboCountry_CompDetail("");
        setTxtPin_CompDetail("");
        setTxtPhone_CompDetail("");
        
        setTdtDealingWithBankSince("");
        setTdtAsOn("");
        setTdtCreditFacilityAvailSince("");
        setTdtDateEstablishment("");
    }
    
    // Populate the Company Details Tab
    public void setTermLoanCompanyTO(AgriTermLoanCompanyTO objTermLoanCompanyTO) {
        try{
            log.info("In setTermLoanCompanyTO...");
            setBorrowerNo(objTermLoanCompanyTO.getBorrowNo());
            setTxtCompanyRegisNo(objTermLoanCompanyTO.getCoRegNo());
            setTxtRiskRating(CommonUtil.convertObjToStr(objTermLoanCompanyTO.getRiskRate()));
            setTdtDateEstablishment(DateUtil.getStringDate(objTermLoanCompanyTO.getEstablishDt()));
            setTdtDealingWithBankSince(DateUtil.getStringDate(objTermLoanCompanyTO.getDealBankSince()));
            setTdtAsOn(DateUtil.getStringDate(objTermLoanCompanyTO.getNetWorthOn()));
            setTdtCreditFacilityAvailSince(DateUtil.getStringDate(objTermLoanCompanyTO.getCrFacilitiesSince()));
            setCboNatureBusiness(CommonUtil.convertObjToStr(getCbmNatureBusiness().getDataForKey(objTermLoanCompanyTO.getBusinessNature())));
            setCboAddressType(CommonUtil.convertObjToStr(getCbmAddressType().getDataForKey(objTermLoanCompanyTO.getAddrType())));
            setCboCity_CompDetail(CommonUtil.convertObjToStr(getCbmCity_CompDetail().getDataForKey(objTermLoanCompanyTO.getCity())));
            setCboCountry_CompDetail(CommonUtil.convertObjToStr(getCbmCountry_CompDetail().getDataForKey(objTermLoanCompanyTO.getCountryCode())));
            setTxtRemarks__CompDetail(objTermLoanCompanyTO.getRemarks());
            setTxtNetWorth(CommonUtil.convertObjToStr(objTermLoanCompanyTO.getNetWorth()));
            setNetWorthInCust(CommonUtil.convertObjToStr(objTermLoanCompanyTO.getNetWorth()));
            setTxtChiefExecutiveName(objTermLoanCompanyTO.getChiefExecName());
            setTxtStreet_CompDetail(objTermLoanCompanyTO.getStreet());
            setTxtArea_CompDetail(objTermLoanCompanyTO.getArea());
            setCboState_CompDetail(CommonUtil.convertObjToStr(getCbmState_CompDetail().getDataForKey(objTermLoanCompanyTO.getState())));
            setTxtPin_CompDetail(objTermLoanCompanyTO.getPincode());
            setTxtPhone_CompDetail(objTermLoanCompanyTO.getPhone());
        }catch(Exception e){
            log.info("try: " + e);
            parseException.logException(e,true);
//            e.printStackTrace();
        }
    }
    
    public AgriTermLoanCompanyTO setTermLoanCompany() {
        log.info("In setTermLoanCompany...");
        
        final AgriTermLoanCompanyTO objTermLoanCompanyTO = new AgriTermLoanCompanyTO();
        try{
            objTermLoanCompanyTO.setCoRegNo(txtCompanyRegisNo);
            objTermLoanCompanyTO.setBorrowNo(borrowerNo);
            
            Date EstDt = DateUtil.getDateMMDDYYYY(tdtDateEstablishment);
            if(EstDt != null){
            Date estDate = (Date)curDate.clone();
            estDate.setDate(EstDt.getDate());
            estDate.setMonth(EstDt.getMonth());
            estDate.setYear(EstDt.getYear());
            objTermLoanCompanyTO.setEstablishDt(estDate);
            }else{
                objTermLoanCompanyTO.setEstablishDt(DateUtil.getDateMMDDYYYY(tdtDateEstablishment));
            }
//            objTermLoanCompanyTO.setEstablishDt(DateUtil.getDateMMDDYYYY(tdtDateEstablishment));
            
            Date DelDt = DateUtil.getDateMMDDYYYY(tdtDealingWithBankSince);
            if(DelDt != null){
            Date delDate = (Date)curDate.clone();
            delDate.setDate(DelDt.getDate());
            delDate.setMonth(DelDt.getMonth());
            delDate.setYear(DelDt.getYear());
            objTermLoanCompanyTO.setDealBankSince(delDate);
            }else{
                objTermLoanCompanyTO.setDealBankSince(DateUtil.getDateMMDDYYYY(tdtDealingWithBankSince));
            }
//            objTermLoanCompanyTO.setDealBankSince(DateUtil.getDateMMDDYYYY(tdtDealingWithBankSince));
            objTermLoanCompanyTO.setNetWorth(CommonUtil.convertObjToDouble(txtNetWorth));
            
            Date AsDt = DateUtil.getDateMMDDYYYY(tdtAsOn);
            if(AsDt != null){
            Date asDate = (Date)curDate.clone();
            asDate.setDate(AsDt.getDate());
            asDate.setMonth(AsDt.getMonth());
            asDate.setYear(AsDt.getYear());
            objTermLoanCompanyTO.setNetWorthOn(DateUtil.getDateMMDDYYYY(tdtAsOn));
            }else{
              objTermLoanCompanyTO.setNetWorthOn(DateUtil.getDateMMDDYYYY(tdtAsOn)); 
            }
//            objTermLoanCompanyTO.setNetWorthOn(DateUtil.getDateMMDDYYYY(tdtAsOn));
            objTermLoanCompanyTO.setChiefExecName(txtChiefExecutiveName);
            objTermLoanCompanyTO.setAddrType(CommonUtil.convertObjToStr(cbmAddressType.getKeyForSelected()));
            objTermLoanCompanyTO.setStreet(txtStreet_CompDetail);
            objTermLoanCompanyTO.setArea(txtArea_CompDetail);
            objTermLoanCompanyTO.setCity(CommonUtil.convertObjToStr(cbmCity_CompDetail.getKeyForSelected()));
            objTermLoanCompanyTO.setState(CommonUtil.convertObjToStr(cbmState_CompDetail.getKeyForSelected()));
            objTermLoanCompanyTO.setCountryCode(CommonUtil.convertObjToStr(cbmCountry_CompDetail.getKeyForSelected()));
            objTermLoanCompanyTO.setPincode(txtPin_CompDetail);
            objTermLoanCompanyTO.setPhone(txtPhone_CompDetail);
            objTermLoanCompanyTO.setRiskRate(CommonUtil.convertObjToDouble(txtRiskRating));
            objTermLoanCompanyTO.setBusinessNature(CommonUtil.convertObjToStr(cbmNatureBusiness.getKeyForSelected()));
            objTermLoanCompanyTO.setRemarks(txtRemarks__CompDetail);
            
            Date CrDt = DateUtil.getDateMMDDYYYY(tdtCreditFacilityAvailSince);
            if(CrDt != null){
            Date crDate = (Date)curDate.clone();
            crDate.setDate(CrDt.getDate());
            crDate.setMonth(CrDt.getMonth());
            crDate.setYear(CrDt.getYear());
            objTermLoanCompanyTO.setCrFacilitiesSince(crDate);
            }else{
                objTermLoanCompanyTO.setCrFacilitiesSince(DateUtil.getDateMMDDYYYY(tdtCreditFacilityAvailSince));
            }
//            objTermLoanCompanyTO.setCrFacilitiesSince(DateUtil.getDateMMDDYYYY(tdtCreditFacilityAvailSince));
            objTermLoanCompanyTO.setStatusBy(TrueTransactMain.USER_ID);
            objTermLoanCompanyTO.setStatusDt(curDate);
        }catch(Exception e){
            log.info("Error in setTermLoanCompany()"+e);
            parseException.logException(e,true);
//            e.printStackTrace();
        }
        return objTermLoanCompanyTO;
    }
    
    public java.util.HashMap getNetworthDetails(){
        java.util.HashMap netWorthDetailsMap = new java.util.HashMap();
        try{
            if (!(CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(getTxtNetWorth())).equals(getNetWorthInCust()))){
                AgriTermLoanBorrowerOB termLoanBorrowerOB = AgriTermLoanBorrowerOB.getInstance();
                netWorthDetailsMap.put("NETWORTH", CommonUtil.convertObjToDouble(getTxtNetWorth()));
                Date TdDt = DateUtil.getDateMMDDYYYY(getTdtAsOn());
                if(TdDt != null){
                Date tdDate = (Date)curDate.clone();
                tdDate.setDate(TdDt.getDate());
                tdDate.setMonth(TdDt.getMonth());
                tdDate.setYear(TdDt.getYear());
                netWorthDetailsMap.put("NETWORTH_AS_ON", tdDate);
                }else{
                    netWorthDetailsMap.put("NETWORTH_AS_ON", DateUtil.getDateMMDDYYYY(getTdtAsOn()));
                }
//                netWorthDetailsMap.put("NETWORTH_AS_ON", DateUtil.getDateMMDDYYYY(getTdtAsOn()));
                netWorthDetailsMap.put("CUST_ID", termLoanBorrowerOB.getTxtCustID());
            }
        }catch(Exception e){
            log.info("Error in getNetworthDetails: "+e);
            parseException.logException(e,true);
        }
        return netWorthDetailsMap;
    }
    
    void setTxtCompanyRegisNo(String txtCompanyRegisNo){
        this.txtCompanyRegisNo = txtCompanyRegisNo;
        setChanged();
    }
    String getTxtCompanyRegisNo(){
        return this.txtCompanyRegisNo;
    }
    
    void setTdtDateEstablishment(String tdtDateEstablishment){
        this.tdtDateEstablishment = tdtDateEstablishment;
        setChanged();
    }
    String getTdtDateEstablishment(){
        return this.tdtDateEstablishment;
    }
    
    void setTdtDealingWithBankSince(String tdtDealingWithBankSince){
        this.tdtDealingWithBankSince = tdtDealingWithBankSince;
        setChanged();
    }
    String getTdtDealingWithBankSince(){
        return this.tdtDealingWithBankSince;
    }
    
    void setTxtRiskRating(String txtRiskRating){
        this.txtRiskRating = txtRiskRating;
        setChanged();
    }
    String getTxtRiskRating(){
        return this.txtRiskRating;
    }
    
    public void setCbmNatureBusiness(ComboBoxModel cbmNatureBusiness){
        this.cbmNatureBusiness = cbmNatureBusiness;
        setChanged();
    }
    
    ComboBoxModel getCbmNatureBusiness(){
        return cbmNatureBusiness;
    }
    
    void setCboNatureBusiness(String cboNatureBusiness){
        this.cboNatureBusiness = cboNatureBusiness;
        setChanged();
    }
    String getCboNatureBusiness(){
        return this.cboNatureBusiness;
    }
    
    void setTxtRemarks__CompDetail(String txtRemarks__CompDetail){
        this.txtRemarks__CompDetail = txtRemarks__CompDetail;
        setChanged();
    }
    String getTxtRemarks__CompDetail(){
        return this.txtRemarks__CompDetail;
    }
    
    void setTxtNetWorth(String txtNetWorth){
        this.txtNetWorth = txtNetWorth;
        setChanged();
    }
    String getTxtNetWorth(){
        return this.txtNetWorth;
    }
    
    void setTdtAsOn(String tdtAsOn){
        this.tdtAsOn = tdtAsOn;
        setChanged();
    }
    String getTdtAsOn(){
        return this.tdtAsOn;
    }
    
    void setTdtCreditFacilityAvailSince(String tdtCreditFacilityAvailSince){
        this.tdtCreditFacilityAvailSince = tdtCreditFacilityAvailSince;
        setChanged();
    }
    String getTdtCreditFacilityAvailSince(){
        return this.tdtCreditFacilityAvailSince;
    }
    
    void setTxtChiefExecutiveName(String txtChiefExecutiveName){
        this.txtChiefExecutiveName = txtChiefExecutiveName;
        setChanged();
    }
    String getTxtChiefExecutiveName(){
        return this.txtChiefExecutiveName;
    }
    
    public void setCbmAddressType(ComboBoxModel cbmAddressType){
        this.cbmAddressType = cbmAddressType;
        setChanged();
    }
    
    ComboBoxModel getCbmAddressType(){
        return cbmAddressType;
    }
    
    void setCboAddressType(String cboAddressType){
        this.cboAddressType = cboAddressType;
        setChanged();
    }
    String getCboAddressType(){
        return this.cboAddressType;
    }
    
    void setTxtStreet_CompDetail(String txtStreet_CompDetail){
        this.txtStreet_CompDetail = txtStreet_CompDetail;
        setChanged();
    }
    String getTxtStreet_CompDetail(){
        return this.txtStreet_CompDetail;
    }
    
    void setTxtArea_CompDetail(String txtArea_CompDetail){
        this.txtArea_CompDetail = txtArea_CompDetail;
        setChanged();
    }
    String getTxtArea_CompDetail(){
        return this.txtArea_CompDetail;
    }
    
    
    public void setCbmCity_CompDetail(ComboBoxModel cbmCity_CompDetail){
        this.cbmCity_CompDetail = cbmCity_CompDetail;
        setChanged();
    }
    
    ComboBoxModel getCbmCity_CompDetail(){
        return cbmCity_CompDetail;
    }
    
    void setCboCity_CompDetail(String cboCity_CompDetail){
        this.cboCity_CompDetail = cboCity_CompDetail;
        setChanged();
    }
    String getCboCity_CompDetail(){
        return this.cboCity_CompDetail;
    }
    
    
    public void setCbmState_CompDetail(ComboBoxModel cbmState_CompDetail){
        this.cbmState_CompDetail = cbmState_CompDetail;
        setChanged();
    }
    
    ComboBoxModel getCbmState_CompDetail(){
        return cbmState_CompDetail;
    }
    
    void setCboState_CompDetail(String cboState_CompDetail){
        this.cboState_CompDetail = cboState_CompDetail;
        setChanged();
    }
    String getCboState_CompDetail(){
        return this.cboState_CompDetail;
    }
    
    public void setCbmCountry_CompDetail(ComboBoxModel cbmCountry_CompDetail){
        this.cbmCountry_CompDetail = cbmCountry_CompDetail;
        setChanged();
    }
    
    ComboBoxModel getCbmCountry_CompDetail(){
        return cbmCountry_CompDetail;
    }
    
    void setCboCountry_CompDetail(String cboCountry_CompDetail){
        this.cboCountry_CompDetail = cboCountry_CompDetail;
        setChanged();
    }
    String getCboCountry_CompDetail(){
        return this.cboCountry_CompDetail;
    }
    
    void setTxtPin_CompDetail(String txtPin_CompDetail){
        this.txtPin_CompDetail = txtPin_CompDetail;
        setChanged();
    }
    String getTxtPin_CompDetail(){
        return this.txtPin_CompDetail;
    }
    
    void setTxtPhone_CompDetail(String txtPhone_CompDetail){
        this.txtPhone_CompDetail = txtPhone_CompDetail;
        setChanged();
    }
    String getTxtPhone_CompDetail(){
        return this.txtPhone_CompDetail;
    }
    
    public void setBorrowerNo(String borrowerNo){
        this.borrowerNo = borrowerNo;
        setChanged();
    }
    
    public String getBorrowerNo(){
        return this.borrowerNo;
    }
    
    /**
     * Getter for property netWorthInCust.
     * @return Value of property netWorthInCust.
     */
    public java.lang.String getNetWorthInCust() {
        return netWorthInCust;
    }
    
    /**
     * Setter for property netWorthInCust.
     * @param netWorthInCust New value of property netWorthInCust.
     */
    public void setNetWorthInCust(java.lang.String netWorthInCust) {
        this.netWorthInCust = netWorthInCust;
    }
    
}
