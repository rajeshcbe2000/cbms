/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanClassificationOB.java
 *
 * Created on July 6, 2004, 4:47 PM
 */

package com.see.truetransact.ui.termloan;

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
import com.see.truetransact.transferobject.termloan.TermLoanClassificationTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.commonutil.DateUtil;
import org.apache.log4j.Logger;
import java.util.Date;


public class TermLoanClassificationOB extends CObservable{
    
    /** Creates a new instance of TermLoanClassificationOB */
    private TermLoanClassificationOB() throws Exception{
        termLoanClassificationOB();
        curDate = ClientUtil.getCurrentDate();
    }
    
    private       static TermLoanClassificationOB termLoanClassificationOB;
    
    private final static Logger log = Logger.getLogger(TermLoanClassificationOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private final   String  YES = "Y";
    private final   String  NO = "N";
    private final   String    FULLY_SECURED = "FULLY_SECURED";
    private final   String    PARTLY_SECURED = "PARTLY_SECURED";
    private final   String    SECURITY_DETAILS = "SECURITY_DETAILS";
    private final   String    STANDARD_ASSETS = "STANDARD_ASSETS";
    private final   String    UNSECURED = "UNSECURED";
    
    //    private final   TermLoanRB objTermLoanRB = new TermLoanRB();
    
    //    private final   java.util.ResourceBundle objTermLoanRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.TermLoanRB", ProxyParameters.LANGUAGE);
    
    private ComboBoxModel cbmCommodityCode;
    private ComboBoxModel cbmGuaranteeCoverCode;
    private ComboBoxModel cbmSectorCode1;
    private ComboBoxModel cbmHealthCode;
    private ComboBoxModel cbmTypeFacility;
    private ComboBoxModel cbmDistrictCode;
    private ComboBoxModel cbmPurposeCode;
    private ComboBoxModel cbmSectorCode2;
    private ComboBoxModel cbmIndusCode;
    private ComboBoxModel cbmWeakerSectionCode;
    private ComboBoxModel cbm20Code;
    private ComboBoxModel cbmrefinancingInst;
    private ComboBoxModel cbmGovtSchemeCode;
    private ComboBoxModel cbmAssetCode;
    private ComboBoxModel cbmRefinancingInsti;
    
    private String lblBorrowerNo_2 = "";
    private String strACNumber = "";
    private String cboCommodityCode = "";
    private String cboGuaranteeCoverCode = "";
    private String cboSectorCode1 = "";
    private String cboHealthCode = "";
    private String cboTypeFacility = "";
    private String cboDistrictCode = "";
    private String cboPurposeCode = "";
    private String cboSectorCode2 = "";
    private String cboIndusCode = "";
    private String cboWeakerSectionCode = "";
    private String cbo20Code = "";
    private String cboRefinancingInsti = "";
    private String cboGovtSchemeCode = "";
    private String cboAssetCode = "";
    private String tdtNPADate = "";
    private String tdtNPAChangeDt="";
    private boolean rdoDirectFinance = false;
     private boolean chkDirectFinance = false;
    private boolean chkECGC = false;
    private boolean chkPrioritySector = false;
    private boolean chkDocumentcomplete = false;
    private boolean chkQIS = false;
    private String lblProdID_CD_Disp = "";
    private String lblAccHead_CD_2 = "";
    private String lblAccNo_CD_2 = "";
    private String lblSanctionNo2 = "";
    private String lblSanctionDate2 = "";
    private String command = "";
    private java.util.HashMap listAssetStatus=new java.util.HashMap();
    private String classifiMode  = CommonConstants.TOSTATUS_INSERT;     // Mode of Classification Details
    Date curDate = null;
    
    static {
        try {
            termLoanClassificationOB = new TermLoanClassificationOB();
        } catch(Exception e) {
            log.info("try: " + e);
            parseException.logException(e,true);
        }
    }
    
    private void termLoanClassificationOB() throws Exception{
    }
    
    public static TermLoanClassificationOB getInstance() {
        return termLoanClassificationOB;
    }
    
    public void resetClassificationDetails(){
        setLblProdID_CD_Disp("");
        setLblSanctionNo2("");
        setLblAccHead_CD_2("");
        setLblAccNo_CD_2("");
        setLblSanctionDate2("");
        setTdtNPADate("");
        setChkDirectFinance(false);
//        setRdoDirectFinance(false)
        setChkECGC(false);
        setChkPrioritySector(false);
        setChkDocumentcomplete(false);
        setChkQIS(false);
        setCboCommodityCode("");
        setCboSectorCode1("");
        setCboPurposeCode("");
        setCboIndusCode("");
        setCbo20Code("");
        setCboGovtSchemeCode("");
        setCboGuaranteeCoverCode("");
        setCboHealthCode("");
        setCboDistrictCode("");
        setCboWeakerSectionCode("");
        setCboRefinancingInsti("");
        getCbmAssetCode().setKeyForSelected("");
        setCboAssetCode("");
        setCboTypeFacility("");
        setListAssetStatus(null);
    }
    
    public void setTermLoanClassificationTO(TermLoanClassificationTO termLoanClassificationTO){
        try{
            setCbo20Code(CommonUtil.convertObjToStr(getCbm20Code().getDataForKey(termLoanClassificationTO.getTwentyCode())));
            setCboCommodityCode(CommonUtil.convertObjToStr(getCbmCommodityCode().getDataForKey(termLoanClassificationTO.getCommodityCode())));
            setCboSectorCode1(CommonUtil.convertObjToStr(getCbmSectorCode1().getDataForKey(termLoanClassificationTO.getSectorCode())));
            setCboPurposeCode(CommonUtil.convertObjToStr(getCbmPurposeCode().getDataForKey(termLoanClassificationTO.getPurposeCode())));
            setCboIndusCode(CommonUtil.convertObjToStr(getCbmIndusCode().getDataForKey(termLoanClassificationTO.getIndustryCode())));
            setCboGovtSchemeCode(CommonUtil.convertObjToStr(getCbmGovtSchemeCode().getDataForKey(termLoanClassificationTO.getGovtSchemeCode())));
            setCboGuaranteeCoverCode(CommonUtil.convertObjToStr(getCbmGuaranteeCoverCode().getDataForKey(termLoanClassificationTO.getGuaranteeCoverCode())));
            setCboHealthCode(CommonUtil.convertObjToStr(getCbmHealthCode().getDataForKey(termLoanClassificationTO.getHealthCode())));
            setCboDistrictCode(CommonUtil.convertObjToStr(getCbmDistrictCode().getDataForKey(termLoanClassificationTO.getDistrictCode())));
            setCboWeakerSectionCode(CommonUtil.convertObjToStr(getCbmWeakerSectionCode().getDataForKey(termLoanClassificationTO.getPaymentCode())));
            setCboRefinancingInsti(CommonUtil.convertObjToStr(getCbmRefinancingInsti().getDataForKey(termLoanClassificationTO.getRefinancingInstitution())));
            getCbmAssetCode().setKeyForSelected(termLoanClassificationTO.getAssetStatus());
            setCboAssetCode(CommonUtil.convertObjToStr(getCbmAssetCode().getDataForKey(termLoanClassificationTO.getAssetStatus())));
            setCboTypeFacility(CommonUtil.convertObjToStr(getCbmTypeFacility().getDataForKey(termLoanClassificationTO.getFacilityType())));
            setTdtNPADate(DateUtil.getStringDate(termLoanClassificationTO.getNpaDt()));
            if (termLoanClassificationTO.getDirectFinance().equals(YES)){
                setChkDirectFinance(true);
            }else if (termLoanClassificationTO.getDirectFinance().equals(NO)){
                setChkDirectFinance(false);
            }
            
            if (termLoanClassificationTO.getEcgc().equals(YES)){
                setChkECGC(true);
            }else if (termLoanClassificationTO.getEcgc().equals(NO)){
                setChkECGC(false);
            }
            
            if (termLoanClassificationTO.getPrioritySector().equals(YES)){
                setChkPrioritySector(true);
            }else if (termLoanClassificationTO.getPrioritySector().equals(NO)){
                setChkPrioritySector(false);
            }
            
            if (termLoanClassificationTO.getDocumentComplete().equals(YES)){
                setChkDocumentcomplete(true);
            }else if (termLoanClassificationTO.getDocumentComplete().equals(NO)){
                setChkDocumentcomplete(false);
            }
            
            if (termLoanClassificationTO.getQis().equals(YES)){
                setChkQIS(true);
            }else if (termLoanClassificationTO.getQis().equals(YES)){
                setChkQIS(false);
            }
            classifiMode = CommonConstants.TOSTATUS_UPDATE;
        }catch(Exception e){
            log.info("Error in setTermLoanClassificationTO()..."+e);
            parseException.logException(e,true);
        }
    }
    
    public Date getProperDateFormat(Object obj) {
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
        
    public TermLoanClassificationTO setTermLoanClassification(){
        final TermLoanClassificationTO termLoanClassificationTO = new TermLoanClassificationTO();
        try{
            
            termLoanClassificationTO.setAcctNum(getStrACNumber());
            //            termLoanClassificationTO.setBorrowNo(getLblBorrowerNo_2());
            termLoanClassificationTO.setCommand(getCommand());
            //            termLoanClassificationTO.setNpaDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtNPADate())));
//            Date dtDate = getProperDateFormat(getTdtNPADate());
//            if(dtDate != null){
//                Date Dt = (Date)curDate.clone();
//                Dt.setDate(dtDate.getDate());
//                Dt.setMonth(dtDate.getMonth());
//                Dt.setYear(dtDate.getYear());
//                termLoanClassificationTO.setNpaDt(Dt);
//            }else{
                termLoanClassificationTO.setNpaDt(getProperDateFormat(getTdtNPADate()));
//            }
            termLoanClassificationTO.setCommodityCode(CommonUtil.convertObjToStr(getCbmCommodityCode().getKeyForSelected()));
            termLoanClassificationTO.setAssetStatus(CommonUtil.convertObjToStr(getCbmAssetCode().getKeyForSelected()));
            termLoanClassificationTO.setBusinessSectorCode(CommonUtil.convertObjToStr(""));
            
            if (getChkDirectFinance()){
                termLoanClassificationTO.setDirectFinance(YES);
            }else{
                termLoanClassificationTO.setDirectFinance(NO);
            }
            
            termLoanClassificationTO.setDistrictCode(CommonUtil.convertObjToStr(getCbmDistrictCode().getKeyForSelected()));
            
            if (getChkDocumentcomplete()){
                termLoanClassificationTO.setDocumentComplete(YES);
            }else{
                termLoanClassificationTO.setDocumentComplete(NO);
            }
            
            if (getChkECGC()){
                termLoanClassificationTO.setEcgc(YES);
            }else{
                termLoanClassificationTO.setEcgc(NO);
            }
            
            termLoanClassificationTO.setFacilityType(CommonUtil.convertObjToStr(getCbmTypeFacility().getKeyForSelected()));
            termLoanClassificationTO.setGovtSchemeCode(CommonUtil.convertObjToStr(getCbmGovtSchemeCode().getKeyForSelected()));
            termLoanClassificationTO.setGuaranteeCoverCode(CommonUtil.convertObjToStr(getCbmGuaranteeCoverCode().getKeyForSelected()));
            termLoanClassificationTO.setHealthCode(CommonUtil.convertObjToStr(getCbmHealthCode().getKeyForSelected()));
            termLoanClassificationTO.setIndustryCode(CommonUtil.convertObjToStr(getCbmIndusCode().getKeyForSelected()));
            termLoanClassificationTO.setPaymentCode(CommonUtil.convertObjToStr(getCbmWeakerSectionCode().getKeyForSelected()));
            if (getChkPrioritySector()){
                termLoanClassificationTO.setPrioritySector(YES);
            }else{
                termLoanClassificationTO.setPrioritySector(NO);
            }
            
            termLoanClassificationTO.setProdId(getLblProdID_CD_Disp());
            termLoanClassificationTO.setPurposeCode(CommonUtil.convertObjToStr(getCbmPurposeCode().getKeyForSelected()));
            if (getChkQIS()){
                termLoanClassificationTO.setQis(YES);
            }else{
                termLoanClassificationTO.setQis(NO);
            }
            
            termLoanClassificationTO.setRefinancingInstitution(CommonUtil.convertObjToStr(getCbmRefinancingInsti().getKeyForSelected()));
            termLoanClassificationTO.setSectorCode(CommonUtil.convertObjToStr(getCbmSectorCode1().getKeyForSelected()));
            termLoanClassificationTO.setTwentyCode(CommonUtil.convertObjToStr(getCbm20Code().getKeyForSelected()));
            termLoanClassificationTO.setStatusBy(TrueTransactMain.USER_ID);
            termLoanClassificationTO.setStatusDt(curDate);
        }catch(Exception e){
            log.info("Error In setTermLoanClassificationTO() "+e);
            parseException.logException(e,true);
        }
        return termLoanClassificationTO;
    }
    
    public void populateClassiDetailsFromProd(){
        try{
            TermLoanOB termLoanOB = TermLoanOB.getInstance();
            java.util.HashMap transactionMap = new java.util.HashMap();
            java.util.HashMap retrieve;
            
            transactionMap.put("PROD_ID", termLoanOB.getCbmProductId().getKeyForSelected());
            
            java.util.List resultList = ClientUtil.executeQuery("getProdClassiDetails", transactionMap);
            
            if (resultList.size() > 0){
                retrieve = (java.util.HashMap) resultList.get(0);
                
                if (CommonUtil.convertObjToStr(retrieve.get(SECURITY_DETAILS)).equals(UNSECURED)){
                    termLoanOB.setRdoSecurityDetails_Unsec(true);
                    termLoanOB.setRdoSecurityDetails_Partly(false);
                    termLoanOB.setRdoSecurityDetails_Fully(false);
                }else if (CommonUtil.convertObjToStr(retrieve.get(SECURITY_DETAILS)).equals(PARTLY_SECURED)){
                    termLoanOB.setRdoSecurityDetails_Unsec(false);
                    termLoanOB.setRdoSecurityDetails_Partly(true);
                    termLoanOB.setRdoSecurityDetails_Fully(false);
                }else if (CommonUtil.convertObjToStr(retrieve.get(SECURITY_DETAILS)).equals(FULLY_SECURED)){
                    termLoanOB.setRdoSecurityDetails_Unsec(false);
                    termLoanOB.setRdoSecurityDetails_Partly(false);
                    termLoanOB.setRdoSecurityDetails_Fully(true);
                }
                setCbo20Code(CommonUtil.convertObjToStr(getCbm20Code().getDataForKey(CommonUtil.convertObjToStr(retrieve.get("TWENTY_CODE")))));
                setCboCommodityCode(CommonUtil.convertObjToStr(getCbmCommodityCode().getDataForKey(CommonUtil.convertObjToStr(retrieve.get("COMMODITY_CODE")))));
                setCboGuaranteeCoverCode(CommonUtil.convertObjToStr(getCbmGuaranteeCoverCode().getDataForKey(CommonUtil.convertObjToStr(retrieve.get("GUARANTEE_COVER_CODE")))));
                setCboSectorCode1(CommonUtil.convertObjToStr(getCbmSectorCode1().getDataForKey(CommonUtil.convertObjToStr(retrieve.get("SECTOR_CODE")))));
                setCboHealthCode(CommonUtil.convertObjToStr(getCbmHealthCode().getDataForKey(CommonUtil.convertObjToStr(retrieve.get("HEALTH_CODE")))));
                setCboTypeFacility(CommonUtil.convertObjToStr(getCbmTypeFacility().getDataForKey(CommonUtil.convertObjToStr(retrieve.get("FACILITY_TYPE")))));
                setCboPurposeCode(CommonUtil.convertObjToStr(getCbmPurposeCode().getDataForKey(CommonUtil.convertObjToStr(retrieve.get("PURPOSE_CODE")))));
                setCboIndusCode(CommonUtil.convertObjToStr(getCbmIndusCode().getDataForKey(CommonUtil.convertObjToStr(retrieve.get("INDUSTRY_CODE")))));
                setCboWeakerSectionCode(CommonUtil.convertObjToStr(getCbmWeakerSectionCode().getDataForKey(CommonUtil.convertObjToStr(retrieve.get("PAYMENT_CODE")))));
                setCboRefinancingInsti(CommonUtil.convertObjToStr(getCbmRefinancingInsti().getDataForKey(CommonUtil.convertObjToStr(retrieve.get("REFINANCING_INSTITUTION")))));
                setCboGovtSchemeCode(CommonUtil.convertObjToStr(getCbmGovtSchemeCode().getDataForKey(CommonUtil.convertObjToStr(retrieve.get("GOVT_SCHEME_CODE")))));
                
                if (CommonUtil.convertObjToStr(retrieve.get("DIRECT_FINANCE")).equals(YES)){
                    setChkDirectFinance(true);
                }else{
                    setChkDirectFinance(false);
                }
                
                if (CommonUtil.convertObjToStr(retrieve.get("ECGC")).equals(YES)){
                    setChkECGC(true);
                }else{
                    setChkECGC(false);
                }
                
                if (CommonUtil.convertObjToStr(retrieve.get("PRIORITY_SECTOR")).equals(YES)){
                    setChkPrioritySector(true);
                }else{
                    setChkPrioritySector(false);
                }
                
                if (CommonUtil.convertObjToStr(retrieve.get("QIS")).equals(YES)){
                    setChkQIS(true);
                }else{
                    setChkQIS(false);
                }
            }
        }catch(Exception e){
            log.info("Error In populateClassiDetailsFromProd: "+e);
            parseException.logException(e,true);
        }
    }
    public void updateAssetStatus(com.see.truetransact.uicomponent.CDateField tdtNpaDt){
        String actNum=getLblAccNo_CD_2();
        System.out.println("labelaccountno###"+actNum);
        if(actNum !=null)
            if(! actNum.equals("")){
                 String prevAssetStatus=CommonUtil.convertObjToStr(getCbmAssetCode().getKeyForSelected());// String prevAssetStatus=getCboAssetCode();
                Date prevDate=null;
                if(getTdtNPADate()!=null && getTdtNPADate().length()>0)
                    prevDate = getProperDateFormat(getTdtNPADate());
                else
                    prevDate = new Date();
                Date pDt = (Date)curDate.clone();
                pDt.setDate(prevDate.getDate());
                pDt.setMonth(prevDate.getMonth());
                pDt.setYear(prevDate.getYear());
                String prevSt = CommonUtil.convertObjToStr(getCbmAssetCode().getKey(getCbmAssetCode().getIndexOf(getCboAssetCode())));
//                String prevSt=CommonUtil.convertObjToStr(getCbmAssetCode().getKey(getCboAssetCode()));
                java.util.HashMap assetMap =new java.util.HashMap();
                
                assetMap.put("ACT_NUM",actNum);
                String assetStatus=CommonUtil.convertObjToStr(getCbmAssetCode().getKeyForSelected());
                assetMap.put("ASSET_STATUS",assetStatus);//getCboAssetCode());
                java.util.List lst=ClientUtil.executeQuery("getNPADate", assetMap);
                if(lst.size()>0){
                    assetMap=(java.util.HashMap)lst.get(0);
                    if (assetMap!=null && assetMap.get("TO_DT")!=null) {
                        String npaDate=CommonUtil.convertObjToStr(assetMap.get("TO_DT"));
                        if(getTdtNPAChangeDt() !=null && getTdtNPAChangeDt().length()>0){
                            java.util.Date changeDt = getProperDateFormat(getTdtNPAChangeDt());
                            java.util.Date sourDt = (java.util.Date)assetMap.get("TO_DT");
                            if(DateUtil.dateDiff(sourDt,changeDt)<0){
                                ClientUtil.showMessageWindow("PRIVIOUS DATE NOT ALLOWED");
                                setTdtNPADate("");
                                tdtNpaDt.setDateValue("");
//                                ttNotifyObservers();
                                return;
                            }
                        }
//                        ttNotifyObservers();
                        if(prevAssetStatus !=null)
                            if(getCboAssetCode().length()>0 && !getCboAssetCode().equals(prevAssetStatus)){
                                setTdtNPADate(DateUtil.getStringDate(curDate));
                                 tdtNpaDt.setDateValue(DateUtil.getStringDate(curDate));
                            }
                        if(prevAssetStatus !=null)
                            if(getCboAssetCode().length()>0 && !getCboAssetCode().equals(prevAssetStatus) && assetStatus.length()>0){
                                listAssetStatus=new java.util.HashMap();
                                listAssetStatus.put("PREV_NPA_STATUS",prevAssetStatus);
                                listAssetStatus.put("CURR_STATUS",assetStatus);//getCboAssetCode()
                                listAssetStatus.put("ACT_NUM",actNum);
                                listAssetStatus.put("FROM_DT",pDt);
                                listAssetStatus.put("TO_DATE",curDate);
                                listAssetStatus.put("MODE","UPDATE");
                                System.out.println("listAssetStatus#####"+listAssetStatus);
                            }
                            else
                                listAssetStatus=null;
                    }
                }
                
                
            }
        
    }
    public void setDefaultValB4AcctCreation(){
        // Sets the Asset Code as "Standard Assests"
        getCbmAssetCode().setKeyForSelected(STANDARD_ASSETS);
        setCboAssetCode(CommonUtil.convertObjToStr(getCbmAssetCode().getDataForKey(STANDARD_ASSETS)));
    }
    
    void setLblProdID_CD_Disp(String lblProdID_CD_Disp){
        this.lblProdID_CD_Disp = lblProdID_CD_Disp;
        setChanged();
    }
    String getLblProdID_CD_Disp(){
        return this.lblProdID_CD_Disp;
    }
    
    void setCboCommodityCode(String cboCommodityCode){
        this.cboCommodityCode = cboCommodityCode;
        setChanged();
    }
    String getCboCommodityCode(){
        return this.cboCommodityCode;
    }
    
    void setCbmCommodityCode(ComboBoxModel cbmCommodityCode){
        this.cbmCommodityCode = cbmCommodityCode;
        setChanged();
    }
    ComboBoxModel getCbmCommodityCode(){
        return this.cbmCommodityCode;
    }
    
    void setCbmGuaranteeCoverCode(ComboBoxModel cbmGuaranteeCoverCode){
        this.cbmGuaranteeCoverCode = cbmGuaranteeCoverCode;
        setChanged();
    }
    ComboBoxModel getCbmGuaranteeCoverCode(){
        return this.cbmGuaranteeCoverCode;
    }
    
    void setCboGuaranteeCoverCode(String cboGuaranteeCoverCode){
        this.cboGuaranteeCoverCode = cboGuaranteeCoverCode;
        setChanged();
    }
    String getCboGuaranteeCoverCode(){
        return this.cboGuaranteeCoverCode;
    }
    
    void setCbmSectorCode1(ComboBoxModel cbmSectorCode1){
        this.cbmSectorCode1 = cbmSectorCode1;
        setChanged();
    }
    ComboBoxModel getCbmSectorCode1(){
        return this.cbmSectorCode1;
    }
    
    void setCboSectorCode1(String cboSectorCode1){
        this.cboSectorCode1 = cboSectorCode1;
        setChanged();
    }
    String getCboSectorCode1(){
        return this.cboSectorCode1;
    }
    
    void setCbmHealthCode(ComboBoxModel cbmHealthCode){
        this.cbmHealthCode = cbmHealthCode;
        setChanged();
    }
    ComboBoxModel getCbmHealthCode(){
        return this.cbmHealthCode;
    }
    
    void setCboHealthCode(String cboHealthCode){
        this.cboHealthCode = cboHealthCode;
        setChanged();
    }
    String getCboHealthCode(){
        return this.cboHealthCode;
    }
    
    void setCbmTypeFacility(ComboBoxModel cbmTypeFacility){
        this.cbmTypeFacility = cbmTypeFacility;
        setChanged();
    }
    ComboBoxModel getCbmTypeFacility(){
        return this.cbmTypeFacility;
    }
    
    void setCboTypeFacility(String cboTypeFacility){
        this.cboTypeFacility = cboTypeFacility;
        setChanged();
    }
    String getCboTypeFacility(){
        return this.cboTypeFacility;
    }
    
    void setCbmDistrictCode(ComboBoxModel cbmDistrictCode){
        this.cbmDistrictCode = cbmDistrictCode;
        setChanged();
    }
    ComboBoxModel getCbmDistrictCode(){
        return this.cbmDistrictCode;
    }
    
    void setCboDistrictCode(String cboDistrictCode){
        this.cboDistrictCode = cboDistrictCode;
        setChanged();
    }
    String getCboDistrictCode(){
        return this.cboDistrictCode;
    }
    
    void setCbmPurposeCode(ComboBoxModel cbmPurposeCode){
        this.cbmPurposeCode = cbmPurposeCode;
        setChanged();
    }
    ComboBoxModel getCbmPurposeCode(){
        return this.cbmPurposeCode;
    }
    
    void setCboPurposeCode(String cboPurposeCode){
        this.cboPurposeCode = cboPurposeCode;
        setChanged();
    }
    String getCboPurposeCode(){
        return this.cboPurposeCode;
    }
    
    void setCbmSectorCode2(ComboBoxModel cbmSectorCode2){
        this.cbmSectorCode2 = cbmSectorCode2;
        setChanged();
    }
    ComboBoxModel getCbmSectorCode2(){
        return this.cbmSectorCode2;
    }
    
    void setCboSectorCode2(String cboSectorCode2){
        this.cboSectorCode2 = cboSectorCode2;
        setChanged();
    }
    String getCboSectorCode2(){
        return this.cboSectorCode2;
    }
    
    void setCbmIndusCode(ComboBoxModel cbmIndusCode){
        this.cbmIndusCode = cbmIndusCode;
        setChanged();
    }
    ComboBoxModel getCbmIndusCode(){
        return this.cbmIndusCode;
    }
    
    void setCboIndusCode(String cboIndusCode){
        this.cboIndusCode = cboIndusCode;
        setChanged();
    }
    String getCboIndusCode(){
        return this.cboIndusCode;
    }
    
    void setCbmWeakerSectionCode(ComboBoxModel cbmWeakerSectionCode){
        this.cbmWeakerSectionCode = cbmWeakerSectionCode;
        setChanged();
    }
    ComboBoxModel getCbmWeakerSectionCode(){
        return this.cbmWeakerSectionCode;
    }
    
    void setCboWeakerSectionCode(String cboWeakerSectionCode){
        this.cboWeakerSectionCode = cboWeakerSectionCode;
        setChanged();
    }
    String getCboWeakerSectionCode(){
        return this.cboWeakerSectionCode;
    }
    
    void setCbm20Code(ComboBoxModel cbm20Code){
        this.cbm20Code = cbm20Code;
        setChanged();
    }
    ComboBoxModel getCbm20Code(){
        return this.cbm20Code;
    }
    
    void setCbo20Code(String cbo20Code){
        this.cbo20Code = cbo20Code;
        setChanged();
    }
    String getCbo20Code(){
        return this.cbo20Code;
    }
    
    void setCbmRefinancingInsti(ComboBoxModel cbmRefinancingInsti){
        this.cbmRefinancingInsti = cbmRefinancingInsti;
        setChanged();
    }
    ComboBoxModel getCbmRefinancingInsti(){
        return this.cbmRefinancingInsti;
    }
    
    void setCboRefinancingInsti(String cboRefinancingInsti){
        this.cboRefinancingInsti = cboRefinancingInsti;
        setChanged();
    }
    String getCboRefinancingInsti(){
        return this.cboRefinancingInsti;
    }
    
    void setCbmGovtSchemeCode(ComboBoxModel cbmGovtSchemeCode){
        this.cbmGovtSchemeCode = cbmGovtSchemeCode;
        setChanged();
    }
    ComboBoxModel getCbmGovtSchemeCode(){
        return this.cbmGovtSchemeCode;
    }
    
    void setCboGovtSchemeCode(String cboGovtSchemeCode){
        this.cboGovtSchemeCode = cboGovtSchemeCode;
        setChanged();
    }
    String getCboGovtSchemeCode(){
        return this.cboGovtSchemeCode;
    }
    
    void setCbmAssetCode(ComboBoxModel cbmAssetCode){
        this.cbmAssetCode = cbmAssetCode;
        setChanged();
    }
    ComboBoxModel getCbmAssetCode(){
        return this.cbmAssetCode;
    }
    
    void setCboAssetCode(String cboAssetCode){
        this.cboAssetCode = cboAssetCode;
        setChanged();
    }
    String getCboAssetCode(){
        return this.cboAssetCode;
    }
    
    void setTdtNPADate(String tdtNPADate){
        this.tdtNPADate = tdtNPADate;
        setChanged();
    }
    String getTdtNPADate(){
        return this.tdtNPADate;
    }
    
    void setChkDirectFinance(boolean chkDirectFinance){
        this.chkDirectFinance = chkDirectFinance;
        setChanged();
    }
    boolean getChkDirectFinance(){
        return this.chkDirectFinance;
    }
    
    void setChkECGC(boolean chkECGC){
        this.chkECGC = chkECGC;
        setChanged();
    }
    boolean getChkECGC(){
        return this.chkECGC;
    }
    
    void setChkPrioritySector(boolean chkPrioritySector){
        this.chkPrioritySector = chkPrioritySector;
        setChanged();
    }
    boolean getChkPrioritySector(){
        return this.chkPrioritySector;
    }
    
    void setChkDocumentcomplete(boolean chkDocumentcomplete){
        this.chkDocumentcomplete = chkDocumentcomplete;
        setChanged();
    }
    boolean getChkDocumentcomplete(){
        return this.chkDocumentcomplete;
    }
    
    void setChkQIS(boolean chkQIS){
        this.chkQIS = chkQIS;
        setChanged();
    }
    
    boolean getChkQIS(){
        return this.chkQIS;
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    public void setLblSanctionNo2(String lblSanctionNo2){
        this.lblSanctionNo2 = lblSanctionNo2;
        setChanged();
    }
    
    public String getLblSanctionNo2(){
        return this.lblSanctionNo2;
    }
    
    public void setLblAccHead_CD_2(String lblAccHead_CD_2){
        this.lblAccHead_CD_2 = lblAccHead_CD_2;
        setChanged();
    }
    
    public String getLblAccHead_CD_2(){
        return this.lblAccHead_CD_2;
    }
    
    public void setLblAccNo_CD_2(String lblAccNo_CD_2){
        this.lblAccNo_CD_2 = lblAccNo_CD_2;
        setChanged();
    }
    
    public String getLblAccNo_CD_2(){
        return this.lblAccNo_CD_2;
    }
    
    public void setLblSanctionDate2(String lblSanctionDate2){
        this.lblSanctionDate2 = lblSanctionDate2;
        setChanged();
    }
    
    public String getLblSanctionDate2(){
        return this.lblSanctionDate2;
    }
    
    public void setClassifiDetails(String mode){
        classifiMode = mode;
    }
    
    String getClassifiDetails(){
        return this.classifiMode;
    }
    
    public void setStrACNumber(String strACNumber){
        this.strACNumber = strACNumber;
        setChanged();
    }
    
    public String getStrACNumber(){
        return this.strACNumber;
    }
    
    public void setLblBorrowerNo_2(String lblBorrowerNo_2){
        this.lblBorrowerNo_2 = lblBorrowerNo_2;
        setChanged();
    }
    
    public String getLblBorrowerNo_2(){
        return this.lblBorrowerNo_2;
    }
    
    public void setCommand(String command){
        this.command = command;
    }
    private String getCommand(){
        return this.command;
    }
    
    /**
     * Getter for property listAssetStatus.
     * @return Value of property listAssetStatus.
     */
    public java.util.HashMap getListAssetStatus() {
        return listAssetStatus;
    }
    
    /**
     * Setter for property listAssetStatus.
     * @param listAssetStatus New value of property listAssetStatus.
     */
    public void setListAssetStatus(java.util.HashMap listAssetStatus) {
        this.listAssetStatus = listAssetStatus;
    }
    
    /**
     * Getter for property tdtNPAChangeDt.
     * @return Value of property tdtNPAChangeDt.
     */
    public java.lang.String getTdtNPAChangeDt() {
        return tdtNPAChangeDt;
    }
    
    /**
     * Setter for property tdtNPAChangeDt.
     * @param tdtNPAChangeDt New value of property tdtNPAChangeDt.
     */
    public void setTdtNPAChangeDt(java.lang.String tdtNPAChangeDt) {
        this.tdtNPAChangeDt = tdtNPAChangeDt;
    }
    
    /**
     * Getter for property rdoDirectFinance.
     * @return Value of property rdoDirectFinance.
     */
    public boolean getRdoDirectFinance() {
        return rdoDirectFinance;
    }
    
    /**
     * Setter for property rdoDirectFinance.
     * @param rdoDirectFinance New value of property rdoDirectFinance.
     */
    public void setRdoDirectFinance(boolean rdoDirectFinance) {
        this.rdoDirectFinance = rdoDirectFinance;
    }
    
    /**
     * Getter for property chkDirectFinance.
     * @return Value of property chkDirectFinance.
     */
    public boolean isChkDirectFinance() {
        return chkDirectFinance;
    }
    
}
