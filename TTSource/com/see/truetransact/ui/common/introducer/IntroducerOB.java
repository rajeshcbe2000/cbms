/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * IntroducerOB.java
 *
 * Created on Wed Dec 29 10:30:10 IST 2004
 */

package com.see.truetransact.ui.common.introducer;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientexception.ClientParseException;

import com.see.truetransact.transferobject.common.introducer.IntroDocDetailsTO;
import com.see.truetransact.transferobject.common.introducer.IntroIdentityTO;
import com.see.truetransact.transferobject.common.introducer.IntroOtherBankTO;
import com.see.truetransact.transferobject.common.introducer.IntroOthersTO;
import com.see.truetransact.transferobject.common.introducer.IntroSelfTO;

import java.util.Observable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 *
 * @author 152721
 */

public class IntroducerOB extends CObservable{
    private HashMap lookUpHash;
    private HashMap keyValue;
    
    private ArrayList key;
    private ArrayList value;
    
    private ComboBoxModel cbmCity;
    private ComboBoxModel cbmCountry;
    private ComboBoxModel cbmState;
    private ComboBoxModel cbmDocTypeDD;
    private ComboBoxModel cbmIdentityTypeID;
    Date curDate = null;
    private java.util.ResourceBundle objIntroducerRB = null;
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    /** Creates a new instance of IntroducerOB */
    public IntroducerOB() {
        try {
            curDate = ClientUtil.getCurrentDate();
            objIntroducerRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.common.introducer.IntroducerRB");
            initianSetup();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void initianSetup() throws Exception{
        fillDropdown();     //__ To Fill all the Combo Boxes
    }
    
    // To Fill the Combo boxes in the UI
    public void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("CUSTOMER.COUNTRY");
        lookup_keys.add("CUSTOMER.STATE");
        lookup_keys.add("CUSTOMER.CITY");
        lookup_keys.add("INDENTITY_TYPE");
        lookup_keys.add("INTRO_DOCUMENT");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.COUNTRY"));
        cbmCountry = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.STATE"));
        cbmState = new ComboBoxModel(key,value);
        
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
        cbmCity = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("INDENTITY_TYPE"));
        cbmIdentityTypeID = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("INTRO_DOCUMENT"));
        cbmDocTypeDD = new ComboBoxModel(key,value);
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /** To set the Data in the TransferObjects...
     */
    private IntroSelfTO setAccountSelfIntro() {
        final IntroSelfTO objAccountSelfIntroTO = new IntroSelfTO();
        try{
            objAccountSelfIntroTO.setActNum(CommonUtil.convertObjToStr(getAcctNum()));
//            objAccountSelfIntroTO.setActNumIntro(CommonUtil.convertObjToStr(getLblCustValue()));
              objAccountSelfIntroTO.setActNumIntro(CommonUtil.convertObjToStr(getTxtAcctNo()));
              objAccountSelfIntroTO.setIntroacctNo(CommonUtil.convertObjToStr(getTxtActNum()));
        }catch(Exception e){
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objAccountSelfIntroTO;
    }
    
    private IntroOtherBankTO setAccountBankIntro() {
        final IntroOtherBankTO objAccountBankIntroTO = new IntroOtherBankTO();
        try{
            objAccountBankIntroTO.setActNum(CommonUtil.convertObjToStr(getAcctNum()));
            objAccountBankIntroTO.setBankName(CommonUtil.convertObjToStr(getTxtBankOB()));
            objAccountBankIntroTO.setBranchName(CommonUtil.convertObjToStr(getTxtBranchOB()));
            objAccountBankIntroTO.setOtherActNum(CommonUtil.convertObjToStr(getTxtAcctNoOB()));
            objAccountBankIntroTO.setOtherActName(CommonUtil.convertObjToStr(getTxtNameOB()));
            objAccountBankIntroTO.setProdId(CommonUtil.convertObjToStr(getTxtProdId()));
            System.out.println ("objAccountBankIntroTO:" + objAccountBankIntroTO);
        }catch(Exception e){
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objAccountBankIntroTO;
    }
    
    private IntroIdentityTO setAccountIdentityIntro() {
        final IntroIdentityTO objAccountIdentityIntroTO = new IntroIdentityTO();
        try{
            objAccountIdentityIntroTO.setActNum(CommonUtil.convertObjToStr(getAcctNum()));
            objAccountIdentityIntroTO.setProofTypeId(CommonUtil.convertObjToStr(cbmIdentityTypeID.getKeyForSelected()));
            objAccountIdentityIntroTO.setProofNum(CommonUtil.convertObjToStr(getTxtDocID()));
            objAccountIdentityIntroTO.setIssueAuth(CommonUtil.convertObjToStr(getTxtIssuedAuthID()));
        }catch(Exception e){
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objAccountIdentityIntroTO;
    }
    
    private IntroDocDetailsTO setAccountDocIntro() {
        final IntroDocDetailsTO objAccountDocIntroTO = new IntroDocDetailsTO();
        try{
            objAccountDocIntroTO.setActNum(CommonUtil.convertObjToStr(getAcctNum()));
            objAccountDocIntroTO.setDocType(CommonUtil.convertObjToStr(cbmDocTypeDD.getKeyForSelected()));
//            objAccountDocIntroTO.setIssueDt(DateUtil.getDateMMDDYYYY(tdtIssuedDateDD));
//            objAccountDocIntroTO.setExpiryDt(DateUtil.getDateMMDDYYYY(tdtExpiryDateDD));
            
            Date IsDt = DateUtil.getDateMMDDYYYY(tdtIssuedDateDD);
            if(IsDt != null){
            Date isDate = (Date)curDate.clone();
            isDate.setDate(IsDt.getDate());
            isDate.setMonth(IsDt.getMonth());
            isDate.setYear(IsDt.getYear());
            objAccountDocIntroTO.setIssueDt(isDate);
            }else{
                objAccountDocIntroTO.setIssueDt(DateUtil.getDateMMDDYYYY(tdtIssuedDateDD));
            }
            
            Date ExDt = DateUtil.getDateMMDDYYYY(tdtExpiryDateDD);
            if(ExDt != null){
            Date exDate = (Date)curDate.clone();
            exDate.setDate(ExDt.getDate());
            exDate.setMonth(ExDt.getMonth());
            exDate.setYear(ExDt.getYear());
            objAccountDocIntroTO.setExpiryDt(exDate);
            }else{
                objAccountDocIntroTO.setExpiryDt(DateUtil.getDateMMDDYYYY(tdtExpiryDateDD));
            }
            
            objAccountDocIntroTO.setIssuedBy(CommonUtil.convertObjToStr(txtIssuedByDD));
            objAccountDocIntroTO.setDocNo(CommonUtil.convertObjToStr(txtDocNoDD));
        }catch(Exception e){
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objAccountDocIntroTO;
    }
    
    private IntroOthersTO setAccountOthersIntro() {
        final IntroOthersTO objAccountOthersIntroTO = new IntroOthersTO();
        try{
            objAccountOthersIntroTO.setActNum(CommonUtil.convertObjToStr(getAcctNum()));
            objAccountOthersIntroTO.setIntroName(CommonUtil.convertObjToStr(txtIntroName));
            objAccountOthersIntroTO.setIntroDesig(CommonUtil.convertObjToStr(txtDesig));
            objAccountOthersIntroTO.setStreet(CommonUtil.convertObjToStr(txtStreet));
            objAccountOthersIntroTO.setArea(CommonUtil.convertObjToStr(txtArea));
            objAccountOthersIntroTO.setCity(CommonUtil.convertObjToStr(cbmCity.getKeyForSelected()));
            objAccountOthersIntroTO.setState(CommonUtil.convertObjToStr(cbmState.getKeyForSelected()));
            objAccountOthersIntroTO.setPinCode(CommonUtil.convertObjToStr(txtPinCode));
            objAccountOthersIntroTO.setAreaCode(CommonUtil.convertObjToStr(txtACode));
            objAccountOthersIntroTO.setPhNo(CommonUtil.convertObjToStr(txtPhone));
            objAccountOthersIntroTO.setCountryCode(CommonUtil.convertObjToStr(cbmCountry.getKeyForSelected()));
        }catch(Exception e){
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objAccountOthersIntroTO;
    }
    
    
    /** To get the Data from the TransferObjects...
     */
    
    private void setAccountSelfIntroTO(IntroSelfTO objAccountSelfIntroTO) throws Exception{
        setAcctNum(CommonUtil.convertObjToStr(objAccountSelfIntroTO.getActNum()));
        setTxtAcctNo(CommonUtil.convertObjToStr(objAccountSelfIntroTO.getActNumIntro()));
        getSelfCustomerIntroDetails(CommonUtil.convertObjToStr(objAccountSelfIntroTO.getActNumIntro()));
        setTxtActNum(CommonUtil.convertObjToStr(objAccountSelfIntroTO.getIntroacctNo()));
    }
    
    private void setAccountBankIntroTO(IntroOtherBankTO objAccountBankIntroTO) throws Exception{
        setAcctNum(CommonUtil.convertObjToStr(objAccountBankIntroTO.getActNum()));
        setTxtBankOB(CommonUtil.convertObjToStr(objAccountBankIntroTO.getBankName()));
        setTxtBranchOB(CommonUtil.convertObjToStr(objAccountBankIntroTO.getBranchName()));
        setTxtAcctNoOB(CommonUtil.convertObjToStr(objAccountBankIntroTO.getOtherActNum()));
        setTxtNameOB(CommonUtil.convertObjToStr(objAccountBankIntroTO.getOtherActName()));
        setTxtProdId(CommonUtil.convertObjToStr(objAccountBankIntroTO.getProdId()));
    }
    
    private void setAccountIdentityIntroTO(IntroIdentityTO objAccountIdentityIntroTO) throws Exception{
        setAcctNum(CommonUtil.convertObjToStr(objAccountIdentityIntroTO.getActNum()));
        setCboIdentityTypeID((String) getCbmIdentityTypeID().getDataForKey(CommonUtil.convertObjToStr(objAccountIdentityIntroTO.getProofTypeId())));
        setTxtDocID(CommonUtil.convertObjToStr(objAccountIdentityIntroTO.getProofNum()));
        setTxtIssuedAuthID(CommonUtil.convertObjToStr(objAccountIdentityIntroTO.getIssueAuth()));
    }
    
    private void setAccountDocIntroTO(IntroDocDetailsTO objAccountDocIntroTO) throws Exception{
        setAcctNum(CommonUtil.convertObjToStr(objAccountDocIntroTO.getActNum()));
        setCboDocTypeDD((String) getCbmDocTypeDD().getDataForKey(CommonUtil.convertObjToStr(objAccountDocIntroTO.getDocType())));
        setTdtIssuedDateDD(DateUtil.getStringDate(objAccountDocIntroTO.getIssueDt()));
        setTdtExpiryDateDD(DateUtil.getStringDate(objAccountDocIntroTO.getExpiryDt()));
        setTxtIssuedByDD(CommonUtil.convertObjToStr(objAccountDocIntroTO.getIssuedBy()));
        setTxtDocNoDD(CommonUtil.convertObjToStr(objAccountDocIntroTO.getDocNo()));
    }
    
    private void setAccountOthersIntroTO(IntroOthersTO objAccountOthersIntroTO) throws Exception{
        setAcctNum(CommonUtil.convertObjToStr(objAccountOthersIntroTO.getActNum()));
        setTxtIntroName(CommonUtil.convertObjToStr(objAccountOthersIntroTO.getIntroName()));
        setTxtDesig(CommonUtil.convertObjToStr(objAccountOthersIntroTO.getIntroDesig()));
        setTxtStreet(CommonUtil.convertObjToStr(objAccountOthersIntroTO.getStreet()));
        setTxtArea(CommonUtil.convertObjToStr(objAccountOthersIntroTO.getArea()));
        setCboCity((String) getCbmCity().getDataForKey(CommonUtil.convertObjToStr(objAccountOthersIntroTO.getCity())));
        setCboState((String) getCbmState().getDataForKey(CommonUtil.convertObjToStr(objAccountOthersIntroTO.getState())));
        setTxtPinCode(CommonUtil.convertObjToStr(objAccountOthersIntroTO.getPinCode()));
        setTxtACode(CommonUtil.convertObjToStr(objAccountOthersIntroTO.getAreaCode()));
        setTxtPhone(CommonUtil.convertObjToStr(objAccountOthersIntroTO.getPhNo()));
        setCboCountry((String) getCbmCountry().getDataForKey(CommonUtil.convertObjToStr(objAccountOthersIntroTO.getCountryCode())));
    }
    
    
    /** this mehtod will return the SELF_CUSTOMER introducer's details as a hashmap
     * this method will use the AccountMap
     */
    public void getSelfCustomerIntroDetails(String custId) {
        try{
            final HashMap data = new HashMap();
            data.put("CUST_ID",custId);
            final List resultList = ClientUtil.executeQuery("getIntroDetails", data);
            if(resultList.size() > 0){
                final HashMap resultMap = (HashMap)resultList.get(0);
//                setLblCustValue(CommonUtil.convertObjToStr(resultMap.get("CUSTOMER_ID")));
                setLblNameValue(CommonUtil.convertObjToStr(resultMap.get("CUSTOMER_NAME")));
                setLblAcctValue(CommonUtil.convertObjToStr(resultMap.get("AC_HEAD")));
                setLblBranchCodeValue(CommonUtil.convertObjToStr(resultMap.get("BRANCH_CODE")));
                setLblBranchValue(CommonUtil.convertObjToStr(resultMap.get("BRANCH_NAME")));
                setChanged();
            }
        }catch(Exception e){
            System.out.println("Error in getSelfCustomerIntroDetails");
            parseException.logException(e,true);
        }
    }
    
    /* To Set the Introducer data from the Database, to the particular SetMethod of the Introducer selected...*/
    public void setData(String intro, HashMap dataMap){
        try{
            if(intro.equalsIgnoreCase("SELF_CUSTOMER")){
                setAccountSelfIntroTO((IntroSelfTO)((List)dataMap.get("SELF_CUSTOMER")).get(0));
                
            }else if(intro.equalsIgnoreCase("IDENTITY")){
                setAccountIdentityIntroTO((IntroIdentityTO)((List)dataMap.get("IDENTITY")).get(0));
                
            }else if(intro.equalsIgnoreCase("OTHERS")){
                setAccountOthersIntroTO((IntroOthersTO)((List)dataMap.get("OTHERS")).get(0));
                
            }else if(intro.equalsIgnoreCase("DOC_DETAILS")){
                setAccountDocIntroTO((IntroDocDetailsTO)((List)dataMap.get("DOC_DETAILS")).get(0));
                
            }else if(intro.equalsIgnoreCase("OTHER_BANK")){
                setAccountBankIntroTO((IntroOtherBankTO)((List)dataMap.get("OTHER_BANK")).get(0));
            }
            setPrevIntroType(intro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public HashMap getData(String introType){
        
        final HashMap introMap = new HashMap();
        
        try{
            if(introType.equalsIgnoreCase("SELF_CUSTOMER")){
                introMap.put(introType, setAccountSelfIntro());
                
            }else if(introType.equalsIgnoreCase("IDENTITY")){
                introMap.put(introType, setAccountIdentityIntro());
                
            }else if(introType.equalsIgnoreCase("OTHERS")){
                introMap.put(introType, setAccountOthersIntro());
                
            }else if(introType.equalsIgnoreCase("DOC_DETAILS")){
                introMap.put(introType, setAccountDocIntro());
                
            }else if(introType.equalsIgnoreCase("OTHER_BANK")){
                introMap.put(introType, setAccountBankIntro());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return introMap;
    }
    
    public String prevIntroType = "";
    
    // Setter method for prevIntroType
    void setPrevIntroType(String prevIntroType){
        this.prevIntroType = prevIntroType;
    }
    // Getter method for prevIntroType
    public String getPrevIntroType(){
        return this.prevIntroType;
    }
    
    
    
    private String txtAcctNo = "";
    private String txtBankOB = "";
    private String txtBranchOB = "";
    private String txtProdId = "";
    private String txtAcctNoOB = "";
    private String txtNameOB = "";
    private String cboDocTypeDD = "";
    private String txtDocNoDD = "";
    private String txtIssuedByDD = "";
    private String tdtIssuedDateDD = "";
    private String tdtExpiryDateDD = "";
    private String cboIdentityTypeID = "";
    private String txtIssuedAuthID = "";
    private String txtDocID = "";
    private String txtIntroName = "";
    private String txtDesig = "";
    private String txtACode = "";
    private String txtPhone = "";
    private String txtStreet = "";
    private String txtArea = "";
    private String cboCountry = "";
    private String cboState = "";
    private String cboCity = "";
    private String txtPinCode = "";
    //Data for the labels to be set in case of Self Introduction...
    private String lblCustValue = "";
    private String lblNameValue = "";
    private String lblAcctValue = "";
    private String lblBranchCodeValue = "";
    private String lblBranchValue = "";
    
    private String acctNum = "";
    private String introType = "";
    private String CustUserid = "";
    private String custId = ""; 
    private String txtActNum = "";
  
    /**
     * To Reset the Labels value in the Screen...
     */
    public void resetLabels(){
        setLblCustValue("");
        setLblNameValue("");
        setLblAcctValue("");
        setLblBranchCodeValue("");
        setLblBranchValue("");
    }
    
    public void resetForm(){
        setTxtAcctNo("");
        setTxtBankOB("");
        setTxtBranchOB("");
        setTxtProdId("");
        setTxtAcctNoOB("");
        setTxtNameOB("");
        setCboDocTypeDD("");
        setTxtDocNoDD("");
        setTxtIssuedByDD("");
        setTdtIssuedDateDD("");
        setTdtExpiryDateDD("");
        setCboIdentityTypeID("");
        setTxtIssuedAuthID("");
        setTxtDocID("");
        setTxtIntroName("");
        setTxtDesig("");
        setTxtACode("");
        setTxtPhone("");
        setTxtStreet("");
        setTxtArea("");
        setCboCountry("");
        setCboState("");
        setCboCity("");
        setTxtPinCode("");
        setTxtActNum("");
    }
    
    
    // Setter method for txtAcctNo
    void setTxtAcctNo(String txtAcctNo){
        this.txtAcctNo = txtAcctNo;
        setChanged();
    }
    // Getter method for txtAcctNo
    String getTxtAcctNo(){
        return this.txtAcctNo;
    }
    
    // Setter method for txtBankOB
    void setTxtBankOB(String txtBankOB){
        this.txtBankOB = txtBankOB;
        setChanged();
    }
    // Getter method for txtBankOB
    String getTxtBankOB(){
        return this.txtBankOB;
    }
    
    // Setter method for txtBranchOB
    void setTxtBranchOB(String txtBranchOB){
        this.txtBranchOB = txtBranchOB;
        setChanged();
    }
    // Getter method for txtBranchOB
    String getTxtBranchOB(){
        return this.txtBranchOB;
    }
    
    // Setter method for txtAcctNoOB
    void setTxtAcctNoOB(String txtAcctNoOB){
        this.txtAcctNoOB = txtAcctNoOB;
        setChanged();
    }
    // Getter method for txtAcctNoOB
    String getTxtAcctNoOB(){
        return this.txtAcctNoOB;
    }
    
    // Setter method for txtNameOB
    void setTxtNameOB(String txtNameOB){
        this.txtNameOB = txtNameOB;
        setChanged();
    }
    // Getter method for txtNameOB
    String getTxtNameOB(){
        return this.txtNameOB;
    }
    
    // Setter method for cboDocTypeDD
    void setCboDocTypeDD(String cboDocTypeDD){
        this.cboDocTypeDD = cboDocTypeDD;
        setChanged();
    }
    // Getter method for cboDocTypeDD
    String getCboDocTypeDD(){
        return this.cboDocTypeDD;
    }
    
    // Setter method for txtDocNoDD
    void setTxtDocNoDD(String txtDocNoDD){
        this.txtDocNoDD = txtDocNoDD;
        setChanged();
    }
    // Getter method for txtDocNoDD
    String getTxtDocNoDD(){
        return this.txtDocNoDD;
    }
    
    // Setter method for txtIssuedByDD
    void setTxtIssuedByDD(String txtIssuedByDD){
        this.txtIssuedByDD = txtIssuedByDD;
        setChanged();
    }
    // Getter method for txtIssuedByDD
    String getTxtIssuedByDD(){
        return this.txtIssuedByDD;
    }
    
    // Setter method for tdtIssuedDateDD
    void setTdtIssuedDateDD(String tdtIssuedDateDD){
        this.tdtIssuedDateDD = tdtIssuedDateDD;
        setChanged();
    }
    // Getter method for tdtIssuedDateDD
    String getTdtIssuedDateDD(){
        return this.tdtIssuedDateDD;
    }
    
    // Setter method for tdtExpiryDateDD
    void setTdtExpiryDateDD(String tdtExpiryDateDD){
        this.tdtExpiryDateDD = tdtExpiryDateDD;
        setChanged();
    }
    // Getter method for tdtExpiryDateDD
    String getTdtExpiryDateDD(){
        return this.tdtExpiryDateDD;
    }
    
    // Setter method for cboIdentityTypeID
    void setCboIdentityTypeID(String cboIdentityTypeID){
        this.cboIdentityTypeID = cboIdentityTypeID;
        setChanged();
    }
    // Getter method for cboIdentityTypeID
    String getCboIdentityTypeID(){
        return this.cboIdentityTypeID;
    }
    
    // Setter method for txtIssuedAuthID
    void setTxtIssuedAuthID(String txtIssuedAuthID){
        this.txtIssuedAuthID = txtIssuedAuthID;
        setChanged();
    }
    // Getter method for txtIssuedAuthID
    String getTxtIssuedAuthID(){
        return this.txtIssuedAuthID;
    }
    
    // Setter method for txtDocID
    void setTxtDocID(String txtDocID){
        this.txtDocID = txtDocID;
        setChanged();
    }
    // Getter method for txtDocID
    String getTxtDocID(){
        return this.txtDocID;
    }
    
    // Setter method for txtIntroName
    void setTxtIntroName(String txtIntroName){
        this.txtIntroName = txtIntroName;
        setChanged();
    }
    // Getter method for txtIntroName
    String getTxtIntroName(){
        return this.txtIntroName;
    }
    
    // Setter method for txtDesig
    void setTxtDesig(String txtDesig){
        this.txtDesig = txtDesig;
        setChanged();
    }
    // Getter method for txtDesig
    String getTxtDesig(){
        return this.txtDesig;
    }
    
    // Setter method for txtACode
    void setTxtACode(String txtACode){
        this.txtACode = txtACode;
        setChanged();
    }
    // Getter method for txtACode
    String getTxtACode(){
        return this.txtACode;
    }
    
    // Setter method for txtPhone
    void setTxtPhone(String txtPhone){
        this.txtPhone = txtPhone;
        setChanged();
    }
    // Getter method for txtPhone
    String getTxtPhone(){
        return this.txtPhone;
    }
    
    // Setter method for txtStreet
    void setTxtStreet(String txtStreet){
        this.txtStreet = txtStreet;
        setChanged();
    }
    // Getter method for txtStreet
    String getTxtStreet(){
        return this.txtStreet;
    }
    
    // Setter method for txtArea
    void setTxtArea(String txtArea){
        this.txtArea = txtArea;
        setChanged();
    }
    // Getter method for txtArea
    String getTxtArea(){
        return this.txtArea;
    }
    
    // Setter method for cboCountry
    void setCboCountry(String cboCountry){
        this.cboCountry = cboCountry;
        setChanged();
    }
    // Getter method for cboCountry
    String getCboCountry(){
        return this.cboCountry;
    }
    
    // Setter method for cboState
    void setCboState(String cboState){
        this.cboState = cboState;
        setChanged();
    }
    // Getter method for cboState
    String getCboState(){
        return this.cboState;
    }
    
    // Setter method for cboCity
    void setCboCity(String cboCity){
        this.cboCity = cboCity;
        setChanged();
    }
    // Getter method for cboCity
    String getCboCity(){
        return this.cboCity;
    }
    
    // Setter method for txtPinCode
    void setTxtPinCode(String txtPinCode){
        this.txtPinCode = txtPinCode;
        setChanged();
    }
    // Getter method for txtPinCode
    String getTxtPinCode(){
        return this.txtPinCode;
    }
    
    // Getter method for cbmCity
    ComboBoxModel getCbmCity(){
        return cbmCity;
    }
    
    // Getter method for cbmCountry
    ComboBoxModel getCbmCountry(){
        return cbmCountry;
    }
    
    // Getter method for cbmState
    ComboBoxModel getCbmState(){
        return cbmState;
    }
    
    // Getter method for cbmDocTypeDD
    ComboBoxModel getCbmDocTypeDD(){
        return cbmDocTypeDD;
    }
    
    // Getter method for cbmIdentityTypeID
    ComboBoxModel getCbmIdentityTypeID(){
        return cbmIdentityTypeID;
    }
    
    // Setter method for lblCustValue
    void setLblCustValue(String lblCustValue){
        this.lblCustValue = lblCustValue;
        setChanged();
    }
    // Getter method for lblCustValue
    String getLblCustValue(){
        return this.lblCustValue;
    }
    
    // Setter method for lblNameValue
    void setLblNameValue(String lblNameValue){
        this.lblNameValue = lblNameValue;
        setChanged();
    }
    // Getter method for lblNameValue
    String getLblNameValue(){
        return this.lblNameValue;
    }
    
    // Setter method for lblAcctValue
    void setLblAcctValue(String lblAcctValue){
        this.lblAcctValue = lblAcctValue;
        setChanged();
    }
    // Getter method for lblAcctValue
    String getLblAcctValue(){
        return this.lblAcctValue;
    }
    
    // // Getter method for lblBranchValue method for lblBranchCodeValue
    void setLblBranchCodeValue(String lblBranchCodeValue){
        this.lblBranchCodeValue = lblBranchCodeValue;
        setChanged();
    }
    // Getter method for lblBranchCodeValue
    String getLblBranchCodeValue(){
        return this.lblBranchCodeValue;
    }
    
    // Setter method for lblBranchValue
    void setLblBranchValue(String lblBranchValue){
        this.lblBranchValue = lblBranchValue;
        setChanged();
    }
    // Getter method for lblBranchValue
    String getLblBranchValue(){
        return this.lblBranchValue;
    }
    
    // Setter method for acctNum
    void setAcctNum(String acctNum){
        this.acctNum = acctNum;
    }
    // Getter method for acctNum
    String getAcctNum(){
        return this.acctNum;
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    // Setter method for introType
    void setIntroType(String introType){
        this.introType = introType;
    }
    // Getter method for introType
    String getIntroType(){
        return this.introType;
    }
    
    /**
     * Getter for property CustUserid.
     * @return Value of property CustUserid.
     */
    public java.lang.String getCustUserid() {
        return CustUserid;
    }    
    
    /**
     * Setter for property CustUserid.
     * @param CustUserid New value of property CustUserid.
     */
    public void setCustUserid(java.lang.String CustUserid) {
        this.CustUserid = CustUserid;
    }    
   
    /**
     * Getter for property custId.
     * @return Value of property custId.
     */
    public java.lang.String getCustId() {
        return custId;
    }    
   
    /**
     * Setter for property custId.
     * @param custId New value of property custId.
     */
    public void setCustId(java.lang.String custId) {
        this.custId = custId;
    }    
    
    /**
     * Getter for property txtProdId.
     * @return Value of property txtProdId.
     */
    public java.lang.String getTxtProdId() {
        return txtProdId;
    }    
    
    /**
     * Setter for property txtProdId.
     * @param txtProdId New value of property txtProdId.
     */
    public void setTxtProdId(java.lang.String txtProdId) {
        this.txtProdId = txtProdId;
    }
    
    /**
     * Getter for property txtActNum.
     * @return Value of property txtActNum.
     */
    public java.lang.String getTxtActNum() {
        return txtActNum;
    }
    
    /**
     * Setter for property txtActNum.
     * @param txtActNum New value of property txtActNum.
     */
    public void setTxtActNum(java.lang.String txtActNum) {
        this.txtActNum = txtActNum;
    }
    
}