/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TermLoanOtherDetailsOB.java
 *
 * Created on April 2, 2005, 3:12 PM
 */

package com.see.truetransact.ui.termloan;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.termloan.TermLoanOtherDetailsTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;


import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Date;
/**
 *
 * @author  152713
 */
public class TermLoanOtherDetailsOB extends CObservable{
    
    private       static TermLoanOtherDetailsOB termLoanOtherDetailsOB;
    
    private final static Logger log = Logger.getLogger(TermLoanOtherDetailsOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private ComboBoxModel cbmOpModeAI;
    private ComboBoxModel cbmSettlementModeAI;
    private ComboBoxModel cbmStmtFreqAD;
    
    private String cboOpModeAI = "";
    private String cboSettlementModeAI = "";
    private String cboStmtFreqAD = "";
    private String tdtATMFromDateAD = "";
    private String tdtATMToDateAD = "";
    private String tdtCredit = "";
    private String tdtCreditFromDateAD = "";
    private String tdtCreditToDateAD = "";
    private String tdtDebit = "";
    private String tdtDebitFromDateAD = "";
    private String tdtDebitToDateAD = "";
    private String tdtNPAChrgAD = "";
    private String txtABBChrgAD = "";
    private String txtATMNoAD = "";
    private String txtAccCloseChrgAD = "";
    private String txtAccOpeningChrgAD = "";
    private String txtChequeBookChrgAD = "";
    private String txtCreditNoAD = "";
    private String txtDebitNoAD = "";
    private String txtExcessWithChrgAD = "";
    private String txtFolioChrgAD = "";
    private String txtMinActBalanceAD = "";
    private String txtMisServiceChrgAD = "";
    private String lblRateCodeValueIN = "";
    private String lblCrInterestRateValueIN = "";
    private String lblDrInterestRateValueIN = "";
    private String lblPenalInterestValueIN = "";
    private String lblAgClearingValueIN = "";
    private boolean chkABBChrgAD = false;
    private boolean chkATMAD = false;
    private boolean chkChequeBookAD = false;
    private boolean chkChequeRetChrgAD = false;
    private boolean chkCreditAD = false;
    private boolean chkCustGrpLimitValidationAD = false;
    private boolean chkDebitAD = false;
    private boolean chkInopChrgAD = false;
    private boolean chkMobileBankingAD = false;
    private boolean chkNPAChrgAD = false;
    private boolean chkNROStatusAD = false;
    private boolean chkNonMainMinBalChrgAD = false;
    private boolean chkPayIntOnCrBalIN = false;
    private boolean chkPayIntOnDrBalIN = false;
    private boolean chkStmtChrgAD = false;
    private boolean chkStopPmtChrgAD = false;
    
    private String command = "";
    private String lblProdID_Disp_ODetails = "";
    private String lblAcctHead_Disp_ODetails = "";
    private String lblAcctNo_Disp_ODetails = "";
    private String otherDetailsMode  = CommonConstants.TOSTATUS_INSERT;     // Mode of Other Details
    private String lblBorrowerNo_2 = "";
    private String strACNumber = "";
    
    private final String NO = "N";
    private final String YES = "Y";
    Date curDate = null;
    /** Creates a new instance of TermLoanOtherDetailsOB */
    public TermLoanOtherDetailsOB() throws Exception{
        curDate = ClientUtil.getCurrentDate();
        termLoanOtherDetailsOB();
    }
    
    static {
        try {
            termLoanOtherDetailsOB = new TermLoanOtherDetailsOB();
        } catch(Exception e) {
            log.info("try: " + e);
            parseException.logException(e,true);
        }
    }
    
    private void termLoanOtherDetailsOB() throws Exception{
        
    }
    
    public static TermLoanOtherDetailsOB getInstance() {
        return termLoanOtherDetailsOB;
    }
    
    public void resetOtherDetailsFields(){
        setCboOpModeAI("");
        setCboSettlementModeAI("");
        setCboStmtFreqAD("");
        setChkABBChrgAD(false);
        setChkATMAD(false);
        setChkChequeBookAD(false);
        setChkChequeRetChrgAD(false);
        setChkCreditAD(false);
        setChkCustGrpLimitValidationAD(false);
        setChkDebitAD(false);
        setChkInopChrgAD(false);
        setChkMobileBankingAD(false);
        setChkNPAChrgAD(false);
        setChkNROStatusAD(false);
        setChkNonMainMinBalChrgAD(false);
        setChkPayIntOnCrBalIN(false);
        setChkPayIntOnDrBalIN(false);
        setChkStmtChrgAD(false);
        setChkStopPmtChrgAD(false);
        setTdtATMFromDateAD("");
        setTdtATMToDateAD("");
        setTdtCredit("");
        setTdtCreditFromDateAD("");
        setTdtCreditToDateAD("");
        setTdtDebit("");
        setTdtDebitFromDateAD("");
        setTdtDebitToDateAD("");
        setTdtNPAChrgAD("");
        setTxtABBChrgAD("");
        setTxtATMNoAD("");
        setTxtAccCloseChrgAD("");
        setTxtAccOpeningChrgAD("");
        setTxtChequeBookChrgAD("");
        setTxtCreditNoAD("");
        setTxtDebitNoAD("");
        setTxtExcessWithChrgAD("");
        setTxtFolioChrgAD("");
        setTxtMinActBalanceAD("");
        setTxtMisServiceChrgAD("");
        setLblAcctHead_Disp_ODetails("");
        setLblAcctNo_Disp_ODetails("");
        setLblProdID_Disp_ODetails("");
    }
    
    public void ttNotifyObservers(){
        this.notifyObservers();
    }
    
    public void setTermLoanOtherDetailsTO(TermLoanOtherDetailsTO termLoanOtherDetailsTO){
        try{
            setCboOpModeAI(CommonUtil.convertObjToStr(getCbmOpModeAI().getDataForKey(termLoanOtherDetailsTO.getModeOfOperation())));
            setCboSettlementModeAI(CommonUtil.convertObjToStr(getCbmSettlementModeAI().getDataForKey(termLoanOtherDetailsTO.getSettlementMode())));
            setCboStmtFreqAD(CommonUtil.convertObjToStr(getCbmStmtFreqAD().getDataForKey(CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getStatFreq()))));
            
            setTdtNPAChrgAD(DateUtil.getStringDate(termLoanOtherDetailsTO.getNpaDt()));
            
            setTxtABBChrgAD(CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getAbbChrg()));
            setTxtAccCloseChrgAD(CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getActClosingChrg()));
            setTxtAccOpeningChrgAD(CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getActOpenChrg()));
            setTxtChequeBookChrgAD(CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getChkBookChrg()));
            setTxtExcessWithChrgAD(CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getExcessWithdChrg()));
            setTxtFolioChrgAD(CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getFolioChrg()));
            setTxtMinActBalanceAD(CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getMinActBal()));
            setTxtMisServiceChrgAD(CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getMiscServChrg()));
            setTdtCredit(CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getLastCrIntAppldt()));
            if (CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getAbb()).equals(YES)){
                setChkABBChrgAD(true);
            }else{
                setChkABBChrgAD(false);
            }
            if (CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getAtmCard()).equals(YES)){
                setChkATMAD(true);
                setTxtATMNoAD(CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getAtmCardNo()));
                setTdtATMFromDateAD(DateUtil.getStringDate(termLoanOtherDetailsTO.getAtmCardValidfrom()));
                setTdtATMToDateAD(DateUtil.getStringDate(termLoanOtherDetailsTO.getAtmCardExprdt()));
            }else{
                setChkATMAD(false);
                setTxtATMNoAD("");
                setTdtATMFromDateAD("");
                setTdtATMToDateAD("");
            }
            if (CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getChkBook()).equals(YES)){
                setChkChequeBookAD(true);
            }else{
                setChkChequeBookAD(false);
            }
            if (CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getChkReturn()).equals(YES)){
                setChkChequeRetChrgAD(true);
            }else{
                setChkChequeRetChrgAD(false);
            }
            if (CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getCrCard()).equals(YES)){
                setChkCreditAD(true);
                setTxtCreditNoAD(CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getCrCardNo()));
                setTdtCreditFromDateAD(DateUtil.getStringDate(termLoanOtherDetailsTO.getCrCardValidfrom()));
                setTdtCreditToDateAD(DateUtil.getStringDate(termLoanOtherDetailsTO.getCrCardExprdt()));
            }else{
                setChkCreditAD(false);
                setTxtCreditNoAD("");
                setTdtCreditFromDateAD("");
                setTdtCreditToDateAD("");
            }
            if (CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getCustgrpLimitValidation()).equals(YES)){
                setChkCustGrpLimitValidationAD(true);
            }else{
                setChkCustGrpLimitValidationAD(false);
            }
            if (CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getDrCard()).equals(YES)){
                setChkDebitAD(true);
                setTxtDebitNoAD(CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getDrCardNo()));
                setTdtDebitFromDateAD(DateUtil.getStringDate(termLoanOtherDetailsTO.getDrCardValidfrom()));
                setTdtDebitToDateAD(DateUtil.getStringDate(termLoanOtherDetailsTO.getDrCardExprdt()));
            }else{
                setChkDebitAD(false);
                setTxtDebitNoAD("");
                setTdtDebitFromDateAD("");
                setTdtDebitToDateAD("");
            }
            if (CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getInopChrg()).equals(YES)){
                setChkInopChrgAD(true);
            }else{
                setChkInopChrgAD(false);
            }
            if (CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getMobileBanking()).equals(YES)){
                setChkMobileBankingAD(true);
            }else{
                setChkMobileBankingAD(false);
            }
            if (CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getNpa()).equals(YES)){
                setChkNPAChrgAD(true);
            }else{
                setChkNPAChrgAD(false);
            }
            if (CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getNroStatus()).equals(YES)){
                setChkNROStatusAD(true);
            }else{
                setChkNROStatusAD(false);
            }
            if (CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getNonmainChrg()).equals(YES)){
                setChkNonMainMinBalChrgAD(true);
            }else{
                setChkNonMainMinBalChrgAD(false);
            }
            if (CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getIntCrBal()).equals(YES)){
                setChkPayIntOnCrBalIN(true);
            }else{
                setChkPayIntOnCrBalIN(false);
            }
            if (CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getIntDrBal()).equals(YES)){
                setChkPayIntOnDrBalIN(true);
            }else{
                setChkPayIntOnDrBalIN(false);
            }
            if (CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getStatChrg()).equals(YES)){
                setChkStmtChrgAD(true);
            }else{
                setChkStmtChrgAD(false);
            }
            if (CommonUtil.convertObjToStr(termLoanOtherDetailsTO.getStopPayChrg()).equals(YES)){
                setChkStopPmtChrgAD(true);
            }else{
                setChkStopPmtChrgAD(false);
            }
            otherDetailsMode = CommonConstants.TOSTATUS_UPDATE;
        }catch(Exception e){
            log.info("Error in setTermLoanOtherDetailsTO: "+e);
            parseException.logException(e,true);
        }
    }
    
    public TermLoanOtherDetailsTO setTermLoanOtherDetails(){
        final TermLoanOtherDetailsTO termLoanOtherDetailsTO = new TermLoanOtherDetailsTO();
        try{
            
            termLoanOtherDetailsTO.setActNum(getStrACNumber());
            termLoanOtherDetailsTO.setCommand(getCommand());
            
            Date NpaCrDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtNPAChrgAD()));
            if(NpaCrDt != null){
            Date npaCrDate = (Date)curDate.clone();
            npaCrDate.setDate(NpaCrDt.getDate());
            npaCrDate.setMonth(NpaCrDt.getMonth());
            npaCrDate.setYear(NpaCrDt.getYear());
            termLoanOtherDetailsTO.setNpaDt(npaCrDate);
            }else{
                termLoanOtherDetailsTO.setNpaDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtNPAChrgAD())));
            }
//            termLoanOtherDetailsTO.setNpaDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtNPAChrgAD())));
            
            termLoanOtherDetailsTO.setAbbChrg(CommonUtil.convertObjToDouble(getTxtABBChrgAD()));
            termLoanOtherDetailsTO.setActClosingChrg(CommonUtil.convertObjToDouble(getTxtAccCloseChrgAD()));
            termLoanOtherDetailsTO.setActOpenChrg(CommonUtil.convertObjToDouble(getTxtAccOpeningChrgAD()));
            termLoanOtherDetailsTO.setChkBookChrg(CommonUtil.convertObjToDouble(getTxtChequeBookChrgAD()));
            termLoanOtherDetailsTO.setExcessWithdChrg(CommonUtil.convertObjToDouble(getTxtExcessWithChrgAD()));
            termLoanOtherDetailsTO.setFolioChrg(CommonUtil.convertObjToDouble(getTxtFolioChrgAD()));
            termLoanOtherDetailsTO.setMinActBal(CommonUtil.convertObjToDouble(getTxtMinActBalanceAD()));
            termLoanOtherDetailsTO.setMiscServChrg(CommonUtil.convertObjToDouble(getTxtMisServiceChrgAD()));
            
            termLoanOtherDetailsTO.setModeOfOperation(CommonUtil.convertObjToStr(getCbmOpModeAI().getKeyForSelected()));
            termLoanOtherDetailsTO.setSettlementMode(CommonUtil.convertObjToStr(getCbmSettlementModeAI().getKeyForSelected()));
            termLoanOtherDetailsTO.setStatFreq(CommonUtil.convertObjToDouble(getCbmStmtFreqAD().getKeyForSelected()));
            
            termLoanOtherDetailsTO.setStatusBy(TrueTransactMain.USER_ID);
            termLoanOtherDetailsTO.setStatusDt(curDate);
            
            if (getChkABBChrgAD()){
                termLoanOtherDetailsTO.setAbb(YES);
            }else{
                termLoanOtherDetailsTO.setAbb(NO);
            }
            if (getChkATMAD()){
                termLoanOtherDetailsTO.setAtmCard(YES);
                termLoanOtherDetailsTO.setAtmCardNo(CommonUtil.convertObjToStr(getTxtATMNoAD()));
//                termLoanOtherDetailsTO.setAtmCardValidfrom(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtATMFromDateAD())));
//                termLoanOtherDetailsTO.setAtmCardExprdt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtATMToDateAD())));
                Date AtmFrDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtATMFromDateAD()));
                if(AtmFrDt != null){
                Date atmfrDate = (Date)curDate.clone();
                atmfrDate.setDate(AtmFrDt.getDate());
                atmfrDate.setMonth(AtmFrDt.getMonth());
                atmfrDate.setYear(AtmFrDt.getYear());
                termLoanOtherDetailsTO.setAtmCardValidfrom(atmfrDate);
                }else{
                   termLoanOtherDetailsTO.setAtmCardValidfrom(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtATMFromDateAD()))); 
                }
                
                Date AtmToDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtATMToDateAD()));
                if(AtmToDt != null){
                Date atmToDate = (Date)curDate.clone();
                atmToDate.setDate(AtmToDt.getDate());
                atmToDate.setMonth(AtmToDt.getMonth());
                atmToDate.setYear(AtmToDt.getYear());
                termLoanOtherDetailsTO.setAtmCardExprdt(atmToDate);
                }else{
                    termLoanOtherDetailsTO.setAtmCardExprdt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtATMToDateAD())));
                }
            }else{
                termLoanOtherDetailsTO.setAtmCard(NO);
            }
            if (getChkChequeBookAD()){
                termLoanOtherDetailsTO.setChkBook(YES);
            }else{
                termLoanOtherDetailsTO.setChkBook(NO);
            }
            if (getChkChequeRetChrgAD()){
                termLoanOtherDetailsTO.setChkReturn(YES);
            }else{
                termLoanOtherDetailsTO.setChkReturn(NO);
            }
            if (getChkCreditAD()){
                termLoanOtherDetailsTO.setCrCard(YES);
                termLoanOtherDetailsTO.setCrCardNo(CommonUtil.convertObjToStr(getTxtCreditNoAD()));
//                termLoanOtherDetailsTO.setCrCardValidfrom(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtCreditFromDateAD())));
//                termLoanOtherDetailsTO.setCrCardExprdt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtCreditToDateAD())));
                Date TdCrFrDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtCreditFromDateAD()));
                if(TdCrFrDt != null){
                Date tdcrfrDate = (Date)curDate.clone();
                tdcrfrDate.setDate(TdCrFrDt.getDate());
                tdcrfrDate.setMonth(TdCrFrDt.getMonth());
                tdcrfrDate.setYear(TdCrFrDt.getYear());
                termLoanOtherDetailsTO.setCrCardValidfrom(tdcrfrDate);
                }else{
                    termLoanOtherDetailsTO.setCrCardValidfrom(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtCreditFromDateAD())));
                }
                
                Date TdCrToDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtCreditToDateAD()));
                if(TdCrToDt != null){
                Date tdcrtoDate = (Date)curDate.clone();
                tdcrtoDate.setDate(TdCrToDt.getDate());
                tdcrtoDate.setMonth(TdCrToDt.getMonth());
                tdcrtoDate.setYear(TdCrToDt.getYear());
                termLoanOtherDetailsTO.setCrCardExprdt(tdcrtoDate);
                }else{
                     termLoanOtherDetailsTO.setCrCardExprdt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtCreditToDateAD())));
                 }
            }else{
                termLoanOtherDetailsTO.setCrCard(NO);
            }
            if (getChkCustGrpLimitValidationAD()){
                termLoanOtherDetailsTO.setCustgrpLimitValidation(YES);
            }else{
                termLoanOtherDetailsTO.setCustgrpLimitValidation(NO);
            }
            if (getChkDebitAD()){
                termLoanOtherDetailsTO.setDrCard(YES);
                termLoanOtherDetailsTO.setDrCardNo(CommonUtil.convertObjToStr(getTxtDebitNoAD()));
//                termLoanOtherDetailsTO.setDrCardValidfrom(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtDebitFromDateAD())));
//                termLoanOtherDetailsTO.setDrCardExprdt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtDebitToDateAD())));
                Date DebFrDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtDebitFromDateAD()));
                 if(DebFrDt != null){
                Date debfrDate = (Date)curDate.clone();
                debfrDate.setDate(DebFrDt.getDate());
                debfrDate.setMonth(DebFrDt.getMonth());
                debfrDate.setYear(DebFrDt.getYear());
                termLoanOtherDetailsTO.setDrCardValidfrom(debfrDate);
                 }else{
                     termLoanOtherDetailsTO.setDrCardValidfrom(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtDebitFromDateAD())));
                 }
                
                Date DebToDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtDebitToDateAD()));
                 if(DebToDt != null){
                Date debtoDate = (Date)curDate.clone();
                debtoDate.setDate(DebToDt.getDate());
                debtoDate.setMonth(DebToDt.getMonth());
                debtoDate.setYear(DebToDt.getYear());
                termLoanOtherDetailsTO.setDrCardExprdt(debtoDate);
                 }else{
                    termLoanOtherDetailsTO.setDrCardExprdt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtDebitToDateAD()))); 
                 }
            }else{
                termLoanOtherDetailsTO.setDrCard(NO);
            }
            if (getChkInopChrgAD()){
                termLoanOtherDetailsTO.setInopChrg(YES);
            }else{
                termLoanOtherDetailsTO.setInopChrg(NO);
            }
            if (getChkMobileBankingAD()){
                termLoanOtherDetailsTO.setMobileBanking(YES);
            }else{
                termLoanOtherDetailsTO.setMobileBanking(NO);
            }
            if (getChkNPAChrgAD()){
                termLoanOtherDetailsTO.setNpa(YES);
            }else{
                termLoanOtherDetailsTO.setNpa(NO);
            }
            if (getChkNROStatusAD()){
                termLoanOtherDetailsTO.setNroStatus(YES);
            }else{
                termLoanOtherDetailsTO.setNroStatus(NO);
            }
            if (getChkNonMainMinBalChrgAD()){
                termLoanOtherDetailsTO.setNonmainChrg(YES);
            }else{
                termLoanOtherDetailsTO.setNonmainChrg(NO);
            }
            if (getChkPayIntOnCrBalIN()){
                termLoanOtherDetailsTO.setIntCrBal(YES);
            }else{
                setChkPayIntOnCrBalIN(false);
            }
            if (getChkPayIntOnDrBalIN()){
                termLoanOtherDetailsTO.setIntDrBal(YES);
            }else{
                termLoanOtherDetailsTO.setIntDrBal(NO);
            }
            if (getChkStmtChrgAD()){
                termLoanOtherDetailsTO.setStatChrg(YES);
            }else{
                termLoanOtherDetailsTO.setStatChrg(NO);
            }
            if (getChkStopPmtChrgAD()){
                termLoanOtherDetailsTO.setStopPayChrg(YES);
            }else{
                termLoanOtherDetailsTO.setStopPayChrg(NO);
            }
            
        }catch(Exception e){
            log.info("Error In setTermLoanOtherDetails: "+e);
            parseException.logException(e,true);
        }
        return termLoanOtherDetailsTO;
    }
    
    public void populateProdLevelValB4AcctCreation(String strProdID){
        try{
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            transactionMap.put("PROD_ID", strProdID);
            List resultList = ClientUtil.executeQuery("getProdLevelOtherDetails", transactionMap);
            if (resultList.size() > 0){
                // If atleast one Record exist
                retrieve = (HashMap) resultList.get(0);
                setTxtAccCloseChrgAD(CommonUtil.convertObjToStr(retrieve.get("AC_CLOSING_CHRG")));
                setTxtMisServiceChrgAD(CommonUtil.convertObjToStr(retrieve.get("MISC_SERV_CHRG")));
                if (CommonUtil.convertObjToStr(retrieve.get("ATM_CARD_ISSUED")).equals(YES)){
                    setChkATMAD(true);
                }else{
                    setChkATMAD(false);
                }
                if (CommonUtil.convertObjToStr(retrieve.get("CR_CARD_ISSUED")).equals(YES)){
                    setChkCreditAD(true);
                }else{
                    setChkCreditAD(false);
                }
                if (CommonUtil.convertObjToStr(retrieve.get("DEBIT_CARD_ISSUED")).equals(YES)){
                    setChkDebitAD(true);
                }else{
                    setChkDebitAD(false);
                }
                if (CommonUtil.convertObjToStr(retrieve.get("MOBILE_BANK_CLIENT")).equals(YES)){
                    setChkMobileBankingAD(true);
                }else{
                    setChkMobileBankingAD(false);
                }
                if (CommonUtil.convertObjToStr(retrieve.get("CHK_ALLOWED")).equals(YES)){
                    setChkChequeBookAD(true);
                }else{
                    setChkChequeBookAD(false);
                }
                if (CommonUtil.convertObjToStr(retrieve.get("CHQBK_ISSUED_CHRG")).equals(YES)){
                    setTxtChequeBookChrgAD(CommonUtil.convertObjToStr(retrieve.get("CHQBK_ISSUED_CHRG_PER")));
                }else{
                    setTxtChequeBookChrgAD("");
                }
            }
            transactionMap = null;
            retrieve = null;
            resultList = null;
        }catch(Exception e){
            log.info("Exception caught in populateProdLevelValB4AcctCreation: "+e);
            parseException.logException(e,true);
        }
    }
    
    /**
     * Getter for property cbmOpModeAI.
     * @return Value of property cbmOpModeAI.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmOpModeAI() {
        return cbmOpModeAI;
    }
    
    /**
     * Setter for property cbmOpModeAI.
     * @param cbmOpModeAI New value of property cbmOpModeAI.
     */
    public void setCbmOpModeAI(com.see.truetransact.clientutil.ComboBoxModel cbmOpModeAI) {
        this.cbmOpModeAI = cbmOpModeAI;
    }
    
    /**
     * Getter for property cbmSettlementModeAI.
     * @return Value of property cbmSettlementModeAI.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSettlementModeAI() {
        return cbmSettlementModeAI;
    }
    
    /**
     * Setter for property cbmSettlementModeAI.
     * @param cbmSettlementModeAI New value of property cbmSettlementModeAI.
     */
    public void setCbmSettlementModeAI(com.see.truetransact.clientutil.ComboBoxModel cbmSettlementModeAI) {
        this.cbmSettlementModeAI = cbmSettlementModeAI;
    }
    
    /**
     * Getter for property cbmStmtFreqAD.
     * @return Value of property cbmStmtFreqAD.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmStmtFreqAD() {
        return cbmStmtFreqAD;
    }
    
    /**
     * Setter for property cbmStmtFreqAD.
     * @param cbmStmtFreqAD New value of property cbmStmtFreqAD.
     */
    public void setCbmStmtFreqAD(com.see.truetransact.clientutil.ComboBoxModel cbmStmtFreqAD) {
        this.cbmStmtFreqAD = cbmStmtFreqAD;
    }
    
    /**
     * Getter for property cboOpModeAI.
     * @return Value of property cboOpModeAI.
     */
    public java.lang.String getCboOpModeAI() {
        return cboOpModeAI;
    }
    
    /**
     * Setter for property cboOpModeAI.
     * @param cboOpModeAI New value of property cboOpModeAI.
     */
    public void setCboOpModeAI(java.lang.String cboOpModeAI) {
        this.cboOpModeAI = cboOpModeAI;
    }
    
    /**
     * Getter for property cboSettlementModeAI.
     * @return Value of property cboSettlementModeAI.
     */
    public java.lang.String getCboSettlementModeAI() {
        return cboSettlementModeAI;
    }
    
    /**
     * Setter for property cboSettlementModeAI.
     * @param cboSettlementModeAI New value of property cboSettlementModeAI.
     */
    public void setCboSettlementModeAI(java.lang.String cboSettlementModeAI) {
        this.cboSettlementModeAI = cboSettlementModeAI;
    }
    
    /**
     * Getter for property cboStmtFreqAD.
     * @return Value of property cboStmtFreqAD.
     */
    public java.lang.String getCboStmtFreqAD() {
        return cboStmtFreqAD;
    }
    
    /**
     * Setter for property cboStmtFreqAD.
     * @param cboStmtFreqAD New value of property cboStmtFreqAD.
     */
    public void setCboStmtFreqAD(java.lang.String cboStmtFreqAD) {
        this.cboStmtFreqAD = cboStmtFreqAD;
    }
    
    /**
     * Getter for property tdtATMFromDateAD.
     * @return Value of property tdtATMFromDateAD.
     */
    public java.lang.String getTdtATMFromDateAD() {
        return tdtATMFromDateAD;
    }
    
    /**
     * Setter for property tdtATMFromDateAD.
     * @param tdtATMFromDateAD New value of property tdtATMFromDateAD.
     */
    public void setTdtATMFromDateAD(java.lang.String tdtATMFromDateAD) {
        this.tdtATMFromDateAD = tdtATMFromDateAD;
    }
    
    /**
     * Getter for property tdtATMToDateAD.
     * @return Value of property tdtATMToDateAD.
     */
    public java.lang.String getTdtATMToDateAD() {
        return tdtATMToDateAD;
    }
    
    /**
     * Setter for property tdtATMToDateAD.
     * @param tdtATMToDateAD New value of property tdtATMToDateAD.
     */
    public void setTdtATMToDateAD(java.lang.String tdtATMToDateAD) {
        this.tdtATMToDateAD = tdtATMToDateAD;
    }
    
    /**
     * Getter for property tdtCredit.
     * @return Value of property tdtCredit.
     */
    public java.lang.String getTdtCredit() {
        return tdtCredit;
    }
    
    /**
     * Setter for property tdtCredit.
     * @param tdtCredit New value of property tdtCredit.
     */
    public void setTdtCredit(java.lang.String tdtCredit) {
        this.tdtCredit = tdtCredit;
    }
    
    /**
     * Getter for property tdtCreditFromDateAD.
     * @return Value of property tdtCreditFromDateAD.
     */
    public java.lang.String getTdtCreditFromDateAD() {
        return tdtCreditFromDateAD;
    }
    
    /**
     * Setter for property tdtCreditFromDateAD.
     * @param tdtCreditFromDateAD New value of property tdtCreditFromDateAD.
     */
    public void setTdtCreditFromDateAD(java.lang.String tdtCreditFromDateAD) {
        this.tdtCreditFromDateAD = tdtCreditFromDateAD;
    }
    
    /**
     * Getter for property tdtCreditToDateAD.
     * @return Value of property tdtCreditToDateAD.
     */
    public java.lang.String getTdtCreditToDateAD() {
        return tdtCreditToDateAD;
    }
    
    /**
     * Setter for property tdtCreditToDateAD.
     * @param tdtCreditToDateAD New value of property tdtCreditToDateAD.
     */
    public void setTdtCreditToDateAD(java.lang.String tdtCreditToDateAD) {
        this.tdtCreditToDateAD = tdtCreditToDateAD;
    }
    
    /**
     * Getter for property tdtDebit.
     * @return Value of property tdtDebit.
     */
    public java.lang.String getTdtDebit() {
        return tdtDebit;
    }
    
    /**
     * Setter for property tdtDebit.
     * @param tdtDebit New value of property tdtDebit.
     */
    public void setTdtDebit(java.lang.String tdtDebit) {
        this.tdtDebit = tdtDebit;
    }
    
    /**
     * Getter for property tdtDebitFromDateAD.
     * @return Value of property tdtDebitFromDateAD.
     */
    public java.lang.String getTdtDebitFromDateAD() {
        return tdtDebitFromDateAD;
    }
    
    /**
     * Setter for property tdtDebitFromDateAD.
     * @param tdtDebitFromDateAD New value of property tdtDebitFromDateAD.
     */
    public void setTdtDebitFromDateAD(java.lang.String tdtDebitFromDateAD) {
        this.tdtDebitFromDateAD = tdtDebitFromDateAD;
    }
    
    /**
     * Getter for property tdtDebitToDateAD.
     * @return Value of property tdtDebitToDateAD.
     */
    public java.lang.String getTdtDebitToDateAD() {
        return tdtDebitToDateAD;
    }
    
    /**
     * Setter for property tdtDebitToDateAD.
     * @param tdtDebitToDateAD New value of property tdtDebitToDateAD.
     */
    public void setTdtDebitToDateAD(java.lang.String tdtDebitToDateAD) {
        this.tdtDebitToDateAD = tdtDebitToDateAD;
    }
    
    /**
     * Getter for property tdtNPAChrgAD.
     * @return Value of property tdtNPAChrgAD.
     */
    public java.lang.String getTdtNPAChrgAD() {
        return tdtNPAChrgAD;
    }
    
    /**
     * Setter for property tdtNPAChrgAD.
     * @param tdtNPAChrgAD New value of property tdtNPAChrgAD.
     */
    public void setTdtNPAChrgAD(java.lang.String tdtNPAChrgAD) {
        this.tdtNPAChrgAD = tdtNPAChrgAD;
    }
    
    /**
     * Getter for property txtABBChrgAD.
     * @return Value of property txtABBChrgAD.
     */
    public java.lang.String getTxtABBChrgAD() {
        return txtABBChrgAD;
    }
    
    /**
     * Setter for property txtABBChrgAD.
     * @param txtABBChrgAD New value of property txtABBChrgAD.
     */
    public void setTxtABBChrgAD(java.lang.String txtABBChrgAD) {
        this.txtABBChrgAD = txtABBChrgAD;
    }
    
    /**
     * Getter for property txtATMNoAD.
     * @return Value of property txtATMNoAD.
     */
    public java.lang.String getTxtATMNoAD() {
        return txtATMNoAD;
    }
    
    /**
     * Setter for property txtATMNoAD.
     * @param txtATMNoAD New value of property txtATMNoAD.
     */
    public void setTxtATMNoAD(java.lang.String txtATMNoAD) {
        this.txtATMNoAD = txtATMNoAD;
    }
    
    /**
     * Getter for property txtAccCloseChrgAD.
     * @return Value of property txtAccCloseChrgAD.
     */
    public java.lang.String getTxtAccCloseChrgAD() {
        return txtAccCloseChrgAD;
    }
    
    /**
     * Setter for property txtAccCloseChrgAD.
     * @param txtAccCloseChrgAD New value of property txtAccCloseChrgAD.
     */
    public void setTxtAccCloseChrgAD(java.lang.String txtAccCloseChrgAD) {
        this.txtAccCloseChrgAD = txtAccCloseChrgAD;
    }
    
    /**
     * Getter for property txtAccOpeningChrgAD.
     * @return Value of property txtAccOpeningChrgAD.
     */
    public java.lang.String getTxtAccOpeningChrgAD() {
        return txtAccOpeningChrgAD;
    }
    
    /**
     * Setter for property txtAccOpeningChrgAD.
     * @param txtAccOpeningChrgAD New value of property txtAccOpeningChrgAD.
     */
    public void setTxtAccOpeningChrgAD(java.lang.String txtAccOpeningChrgAD) {
        this.txtAccOpeningChrgAD = txtAccOpeningChrgAD;
    }
    
    /**
     * Getter for property txtChequeBookChrgAD.
     * @return Value of property txtChequeBookChrgAD.
     */
    public java.lang.String getTxtChequeBookChrgAD() {
        return txtChequeBookChrgAD;
    }
    
    /**
     * Setter for property txtChequeBookChrgAD.
     * @param txtChequeBookChrgAD New value of property txtChequeBookChrgAD.
     */
    public void setTxtChequeBookChrgAD(java.lang.String txtChequeBookChrgAD) {
        this.txtChequeBookChrgAD = txtChequeBookChrgAD;
    }
    
    /**
     * Getter for property txtCreditNoAD.
     * @return Value of property txtCreditNoAD.
     */
    public java.lang.String getTxtCreditNoAD() {
        return txtCreditNoAD;
    }
    
    /**
     * Setter for property txtCreditNoAD.
     * @param txtCreditNoAD New value of property txtCreditNoAD.
     */
    public void setTxtCreditNoAD(java.lang.String txtCreditNoAD) {
        this.txtCreditNoAD = txtCreditNoAD;
    }
    
    /**
     * Getter for property txtDebitNoAD.
     * @return Value of property txtDebitNoAD.
     */
    public java.lang.String getTxtDebitNoAD() {
        return txtDebitNoAD;
    }
    
    /**
     * Setter for property txtDebitNoAD.
     * @param txtDebitNoAD New value of property txtDebitNoAD.
     */
    public void setTxtDebitNoAD(java.lang.String txtDebitNoAD) {
        this.txtDebitNoAD = txtDebitNoAD;
    }
    
    /**
     * Getter for property txtExcessWithChrgAD.
     * @return Value of property txtExcessWithChrgAD.
     */
    public java.lang.String getTxtExcessWithChrgAD() {
        return txtExcessWithChrgAD;
    }
    
    /**
     * Setter for property txtExcessWithChrgAD.
     * @param txtExcessWithChrgAD New value of property txtExcessWithChrgAD.
     */
    public void setTxtExcessWithChrgAD(java.lang.String txtExcessWithChrgAD) {
        this.txtExcessWithChrgAD = txtExcessWithChrgAD;
    }
    
    /**
     * Getter for property txtFolioChrgAD.
     * @return Value of property txtFolioChrgAD.
     */
    public java.lang.String getTxtFolioChrgAD() {
        return txtFolioChrgAD;
    }
    
    /**
     * Setter for property txtFolioChrgAD.
     * @param txtFolioChrgAD New value of property txtFolioChrgAD.
     */
    public void setTxtFolioChrgAD(java.lang.String txtFolioChrgAD) {
        this.txtFolioChrgAD = txtFolioChrgAD;
    }
    
    /**
     * Getter for property txtMinActBalanceAD.
     * @return Value of property txtMinActBalanceAD.
     */
    public java.lang.String getTxtMinActBalanceAD() {
        return txtMinActBalanceAD;
    }
    
    /**
     * Setter for property txtMinActBalanceAD.
     * @param txtMinActBalanceAD New value of property txtMinActBalanceAD.
     */
    public void setTxtMinActBalanceAD(java.lang.String txtMinActBalanceAD) {
        this.txtMinActBalanceAD = txtMinActBalanceAD;
    }
    
    /**
     * Getter for property txtMisServiceChrgAD.
     * @return Value of property txtMisServiceChrgAD.
     */
    public java.lang.String getTxtMisServiceChrgAD() {
        return txtMisServiceChrgAD;
    }
    
    /**
     * Setter for property txtMisServiceChrgAD.
     * @param txtMisServiceChrgAD New value of property txtMisServiceChrgAD.
     */
    public void setTxtMisServiceChrgAD(java.lang.String txtMisServiceChrgAD) {
        this.txtMisServiceChrgAD = txtMisServiceChrgAD;
    }
    
    /**
     * Getter for property chkABBChrgAD.
     * @return Value of property chkABBChrgAD.
     */
    public boolean getChkABBChrgAD() {
        return chkABBChrgAD;
    }
    
    /**
     * Setter for property chkABBChrgAD.
     * @param chkABBChrgAD New value of property chkABBChrgAD.
     */
    public void setChkABBChrgAD(boolean chkABBChrgAD) {
        this.chkABBChrgAD = chkABBChrgAD;
    }
    
    /**
     * Getter for property chkATMAD.
     * @return Value of property chkATMAD.
     */
    public boolean getChkATMAD() {
        return chkATMAD;
    }
    
    /**
     * Setter for property chkATMAD.
     * @param chkATMAD New value of property chkATMAD.
     */
    public void setChkATMAD(boolean chkATMAD) {
        this.chkATMAD = chkATMAD;
    }
    
    /**
     * Getter for property chkChequeBookAD.
     * @return Value of property chkChequeBookAD.
     */
    public boolean getChkChequeBookAD() {
        return chkChequeBookAD;
    }
    
    /**
     * Setter for property chkChequeBookAD.
     * @param chkChequeBookAD New value of property chkChequeBookAD.
     */
    public void setChkChequeBookAD(boolean chkChequeBookAD) {
        this.chkChequeBookAD = chkChequeBookAD;
    }
    
    /**
     * Getter for property chkChequeRetChrgAD.
     * @return Value of property chkChequeRetChrgAD.
     */
    public boolean getChkChequeRetChrgAD() {
        return chkChequeRetChrgAD;
    }
    
    /**
     * Setter for property chkChequeRetChrgAD.
     * @param chkChequeRetChrgAD New value of property chkChequeRetChrgAD.
     */
    public void setChkChequeRetChrgAD(boolean chkChequeRetChrgAD) {
        this.chkChequeRetChrgAD = chkChequeRetChrgAD;
    }
    
    /**
     * Getter for property chkCreditAD.
     * @return Value of property chkCreditAD.
     */
    public boolean getChkCreditAD() {
        return chkCreditAD;
    }
    
    /**
     * Setter for property chkCreditAD.
     * @param chkCreditAD New value of property chkCreditAD.
     */
    public void setChkCreditAD(boolean chkCreditAD) {
        this.chkCreditAD = chkCreditAD;
    }
    
    /**
     * Getter for property chkCustGrpLimitValidationAD.
     * @return Value of property chkCustGrpLimitValidationAD.
     */
    public boolean getChkCustGrpLimitValidationAD() {
        return chkCustGrpLimitValidationAD;
    }
    
    /**
     * Setter for property chkCustGrpLimitValidationAD.
     * @param chkCustGrpLimitValidationAD New value of property chkCustGrpLimitValidationAD.
     */
    public void setChkCustGrpLimitValidationAD(boolean chkCustGrpLimitValidationAD) {
        this.chkCustGrpLimitValidationAD = chkCustGrpLimitValidationAD;
    }
    
    /**
     * Getter for property chkDebitAD.
     * @return Value of property chkDebitAD.
     */
    public boolean getChkDebitAD() {
        return chkDebitAD;
    }
    
    /**
     * Setter for property chkDebitAD.
     * @param chkDebitAD New value of property chkDebitAD.
     */
    public void setChkDebitAD(boolean chkDebitAD) {
        this.chkDebitAD = chkDebitAD;
    }
    
    /**
     * Getter for property chkInopChrgAD.
     * @return Value of property chkInopChrgAD.
     */
    public boolean getChkInopChrgAD() {
        return chkInopChrgAD;
    }
    
    /**
     * Setter for property chkInopChrgAD.
     * @param chkInopChrgAD New value of property chkInopChrgAD.
     */
    public void setChkInopChrgAD(boolean chkInopChrgAD) {
        this.chkInopChrgAD = chkInopChrgAD;
    }
    
    /**
     * Getter for property chkMobileBankingAD.
     * @return Value of property chkMobileBankingAD.
     */
    public boolean getChkMobileBankingAD() {
        return chkMobileBankingAD;
    }
    
    /**
     * Setter for property chkMobileBankingAD.
     * @param chkMobileBankingAD New value of property chkMobileBankingAD.
     */
    public void setChkMobileBankingAD(boolean chkMobileBankingAD) {
        this.chkMobileBankingAD = chkMobileBankingAD;
    }
    
    /**
     * Getter for property chkNPAChrgAD.
     * @return Value of property chkNPAChrgAD.
     */
    public boolean getChkNPAChrgAD() {
        return chkNPAChrgAD;
    }
    
    /**
     * Setter for property chkNPAChrgAD.
     * @param chkNPAChrgAD New value of property chkNPAChrgAD.
     */
    public void setChkNPAChrgAD(boolean chkNPAChrgAD) {
        this.chkNPAChrgAD = chkNPAChrgAD;
    }
    
    /**
     * Getter for property chkNROStatusAD.
     * @return Value of property chkNROStatusAD.
     */
    public boolean getChkNROStatusAD() {
        return chkNROStatusAD;
    }
    
    /**
     * Setter for property chkNROStatusAD.
     * @param chkNROStatusAD New value of property chkNROStatusAD.
     */
    public void setChkNROStatusAD(boolean chkNROStatusAD) {
        this.chkNROStatusAD = chkNROStatusAD;
    }
    
    /**
     * Getter for property chkNonMainMinBalChrgAD.
     * @return Value of property chkNonMainMinBalChrgAD.
     */
    public boolean getChkNonMainMinBalChrgAD() {
        return chkNonMainMinBalChrgAD;
    }
    
    /**
     * Setter for property chkNonMainMinBalChrgAD.
     * @param chkNonMainMinBalChrgAD New value of property chkNonMainMinBalChrgAD.
     */
    public void setChkNonMainMinBalChrgAD(boolean chkNonMainMinBalChrgAD) {
        this.chkNonMainMinBalChrgAD = chkNonMainMinBalChrgAD;
    }
    
    /**
     * Getter for property chkPayIntOnCrBalIN.
     * @return Value of property chkPayIntOnCrBalIN.
     */
    public boolean getChkPayIntOnCrBalIN() {
        return chkPayIntOnCrBalIN;
    }
    
    /**
     * Setter for property chkPayIntOnCrBalIN.
     * @param chkPayIntOnCrBalIN New value of property chkPayIntOnCrBalIN.
     */
    public void setChkPayIntOnCrBalIN(boolean chkPayIntOnCrBalIN) {
        this.chkPayIntOnCrBalIN = chkPayIntOnCrBalIN;
    }
    
    /**
     * Getter for property chkPayIntOnDrBalIN.
     * @return Value of property chkPayIntOnDrBalIN.
     */
    public boolean getChkPayIntOnDrBalIN() {
        return chkPayIntOnDrBalIN;
    }
    
    /**
     * Setter for property chkPayIntOnDrBalIN.
     * @param chkPayIntOnDrBalIN New value of property chkPayIntOnDrBalIN.
     */
    public void setChkPayIntOnDrBalIN(boolean chkPayIntOnDrBalIN) {
        this.chkPayIntOnDrBalIN = chkPayIntOnDrBalIN;
    }
    
    /**
     * Getter for property chkStmtChrgAD.
     * @return Value of property chkStmtChrgAD.
     */
    public boolean getChkStmtChrgAD() {
        return chkStmtChrgAD;
    }
    
    /**
     * Setter for property chkStmtChrgAD.
     * @param chkStmtChrgAD New value of property chkStmtChrgAD.
     */
    public void setChkStmtChrgAD(boolean chkStmtChrgAD) {
        this.chkStmtChrgAD = chkStmtChrgAD;
    }
    
    /**
     * Getter for property chkStopPmtChrgAD.
     * @return Value of property chkStopPmtChrgAD.
     */
    public boolean getChkStopPmtChrgAD() {
        return chkStopPmtChrgAD;
    }
    
    /**
     * Setter for property chkStopPmtChrgAD.
     * @param chkStopPmtChrgAD New value of property chkStopPmtChrgAD.
     */
    public void setChkStopPmtChrgAD(boolean chkStopPmtChrgAD) {
        this.chkStopPmtChrgAD = chkStopPmtChrgAD;
    }
    
    /**
     * Getter for property lblRateCodeValueIN.
     * @return Value of property lblRateCodeValueIN.
     */
    public java.lang.String getLblRateCodeValueIN() {
        return lblRateCodeValueIN;
    }
    
    /**
     * Setter for property lblRateCodeValueIN.
     * @param lblRateCodeValueIN New value of property lblRateCodeValueIN.
     */
    public void setLblRateCodeValueIN(java.lang.String lblRateCodeValueIN) {
        this.lblRateCodeValueIN = lblRateCodeValueIN;
    }
    
    /**
     * Getter for property lblCrInterestRateValueIN.
     * @return Value of property lblCrInterestRateValueIN.
     */
    public java.lang.String getLblCrInterestRateValueIN() {
        return lblCrInterestRateValueIN;
    }
    
    /**
     * Setter for property lblCrInterestRateValueIN.
     * @param lblCrInterestRateValueIN New value of property lblCrInterestRateValueIN.
     */
    public void setLblCrInterestRateValueIN(java.lang.String lblCrInterestRateValueIN) {
        this.lblCrInterestRateValueIN = lblCrInterestRateValueIN;
    }
    
    /**
     * Getter for property lblDrInterestRateValueIN.
     * @return Value of property lblDrInterestRateValueIN.
     */
    public java.lang.String getLblDrInterestRateValueIN() {
        return lblDrInterestRateValueIN;
    }
    
    /**
     * Setter for property lblDrInterestRateValueIN.
     * @param lblDrInterestRateValueIN New value of property lblDrInterestRateValueIN.
     */
    public void setLblDrInterestRateValueIN(java.lang.String lblDrInterestRateValueIN) {
        this.lblDrInterestRateValueIN = lblDrInterestRateValueIN;
    }
    
    /**
     * Getter for property lblPenalInterestValueIN.
     * @return Value of property lblPenalInterestValueIN.
     */
    public java.lang.String getLblPenalInterestValueIN() {
        return lblPenalInterestValueIN;
    }
    
    /**
     * Setter for property lblPenalInterestValueIN.
     * @param lblPenalInterestValueIN New value of property lblPenalInterestValueIN.
     */
    public void setLblPenalInterestValueIN(java.lang.String lblPenalInterestValueIN) {
        this.lblPenalInterestValueIN = lblPenalInterestValueIN;
    }
    
    /**
     * Getter for property lblAgClearingValueIN.
     * @return Value of property lblAgClearingValueIN.
     */
    public java.lang.String getLblAgClearingValueIN() {
        return lblAgClearingValueIN;
    }
    
    /**
     * Setter for property lblAgClearingValueIN.
     * @param lblAgClearingValueIN New value of property lblAgClearingValueIN.
     */
    public void setLblAgClearingValueIN(java.lang.String lblAgClearingValueIN) {
        this.lblAgClearingValueIN = lblAgClearingValueIN;
    }
    
    /**
     * Getter for property command.
     * @return Value of property command.
     */
    public java.lang.String getCommand() {
        return command;
    }
    
    /**
     * Setter for property command.
     * @param command New value of property command.
     */
    public void setCommand(java.lang.String command) {
        this.command = command;
    }
    
    /**
     * Getter for property otherDetailsMode.
     * @return Value of property otherDetailsMode.
     */
    public java.lang.String getOtherDetailsMode() {
        return otherDetailsMode;
    }
    
    /**
     * Setter for property otherDetailsMode.
     * @param otherDetailsMode New value of property otherDetailsMode.
     */
    public void setOtherDetailsMode(java.lang.String otherDetailsMode) {
        this.otherDetailsMode = otherDetailsMode;
    }
    
    /**
     * Getter for property lblBorrowerNo_2.
     * @return Value of property lblBorrowerNo_2.
     */
    public java.lang.String getLblBorrowerNo_2() {
        return lblBorrowerNo_2;
    }
    
    /**
     * Setter for property lblBorrowerNo_2.
     * @param lblBorrowerNo_2 New value of property lblBorrowerNo_2.
     */
    public void setLblBorrowerNo_2(java.lang.String lblBorrowerNo_2) {
        this.lblBorrowerNo_2 = lblBorrowerNo_2;
    }
    
    /**
     * Getter for property strACNumber.
     * @return Value of property strACNumber.
     */
    public java.lang.String getStrACNumber() {
        return strACNumber;
    }
    
    /**
     * Setter for property strACNumber.
     * @param strACNumber New value of property strACNumber.
     */
    public void setStrACNumber(java.lang.String strACNumber) {
        this.strACNumber = strACNumber;
    }
    
    /**
     * Getter for property lblAcctNo_Disp_ODetails.
     * @return Value of property lblAcctNo_Disp_ODetails.
     */
    public java.lang.String getLblAcctNo_Disp_ODetails() {
        return lblAcctNo_Disp_ODetails;
    }
    
    /**
     * Setter for property lblAcctNo_Disp_ODetails.
     * @param lblAcctNo_Disp_ODetails New value of property lblAcctNo_Disp_ODetails.
     */
    public void setLblAcctNo_Disp_ODetails(java.lang.String lblAcctNo_Disp_ODetails) {
        this.lblAcctNo_Disp_ODetails = lblAcctNo_Disp_ODetails;
    }
    
    /**
     * Getter for property lblAcctHead_Disp_ODetails.
     * @return Value of property lblAcctHead_Disp_ODetails.
     */
    public java.lang.String getLblAcctHead_Disp_ODetails() {
        return lblAcctHead_Disp_ODetails;
    }
    
    /**
     * Setter for property lblAcctHead_Disp_ODetails.
     * @param lblAcctHead_Disp_ODetails New value of property lblAcctHead_Disp_ODetails.
     */
    public void setLblAcctHead_Disp_ODetails(java.lang.String lblAcctHead_Disp_ODetails) {
        this.lblAcctHead_Disp_ODetails = lblAcctHead_Disp_ODetails;
    }
    
    /**
     * Getter for property lblProdID_Disp_ODetails.
     * @return Value of property lblProdID_Disp_ODetails.
     */
    public java.lang.String getLblProdID_Disp_ODetails() {
        return lblProdID_Disp_ODetails;
    }
    
    /**
     * Setter for property lblProdID_Disp_ODetails.
     * @param lblProdID_Disp_ODetails New value of property lblProdID_Disp_ODetails.
     */
    public void setLblProdID_Disp_ODetails(java.lang.String lblProdID_Disp_ODetails) {
        this.lblProdID_Disp_ODetails = lblProdID_Disp_ODetails;
    }
    
}
