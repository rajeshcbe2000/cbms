/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MDSApplicationOB.java
 *
 * Created on January 7, 2004, 5:14 PM
 */
package com.see.truetransact.ui.mdsapplication;

/**
 *
 * @author Sathiya
 *
 */
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.transferobject.common.mobile.SMSSubscriptionTO;
import com.see.truetransact.transferobject.mdsapplication.MDSApplicationTO;
import com.see.truetransact.ui.common.nominee.NomineeOB;

import com.see.truetransact.ui.common.transaction.TransactionOB;
import java.util.Date;

public class MDSApplicationOB extends CObservable {

    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    //    private static RemittanceProductRB objRemittanceProductRB = new RemittanceProductRB();
    java.util.ResourceBundle objRemittanceProductRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.mdsapplication.MDSApplicationRB", ProxyParameters.LANGUAGE);
    private HashMap hash;
    private HashMap operationMap;
    private ProxyFactory proxy;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    private int result;
    private HashMap oldTransDetMap = new HashMap();
    private TransactionOB transactionOB;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private ComboBoxModel cbmCity, cbmState;
    private MDSApplicationTO mdsApplicationTO = null;
    private String txtSchemeName = "";
    private Integer txtDivisionNo = 0;  //AJITH Changed from String to Integer
    private String txtChittalNo = "";
    private Integer txtSubNo = 0;  //AJITH Changed from String to Integer
    private String tdtChitStartDt = "";
    private String tdtChitEndDt = "";
    private Double txtInstAmt = 0.0;  //AJITH Changed from String to Double
    private Integer txtApplnNo = 0;  //AJITH Changed from String to Integer
    private String tdtApplnDate = "";
    private boolean chkThalayal = false;
    private boolean chkMunnal = false;
    private boolean chkCoChittal = false;
    private String txtMembershipNo = "";
    private String txtMembershipType = "";
    private String txtMembershipName = "";
    private String txtHouseStNo = "";
    private String txtArea = "";
    private String cboCity = "";
    private String cboState = "";
    private String txtpin = "";
    private ComboBoxModel cbmProdType;
    private String cboProdType = "";
    private ComboBoxModel cbmProdId;
    private String cboProdId = "";
    private String txtCustomerIdCr = "";
    private boolean chkStandingInstn = false;
    private boolean chkNominee = false;
    private String txtRemarks = "";
    private String transId = "";
    private String nextActNo = "";
    private String multipleMember = "";
    private String rdoSalaryRecovery = "";
    private String updateChitNo = "";    
    boolean chkMobileBankingAD = false;
    private String txtMobileNo = "";
    private String subscribtionDt = null;
    SMSSubscriptionTO objSMSSubscriptionTO = null;
    private static Date currDt = null;
    private double predefinedInstallBonus = 0.0;

    public double getPredefinedInstallBonus() {
        return predefinedInstallBonus;
    }

    public void setPredefinedInstallBonus(double predefinedInstallBonus) {
        this.predefinedInstallBonus = predefinedInstallBonus;
    }

    
    
    public boolean isChkMobileBankingAD() {
        return chkMobileBankingAD;
    }

    public void setChkMobileBankingAD(boolean chkMobileBankingAD) {
        this.chkMobileBankingAD = chkMobileBankingAD;
    }

    public String getSubscribtionDt() {
        return subscribtionDt;
    }

    public void setSubscribtionDt(String subscribtionDt) {
        this.subscribtionDt = subscribtionDt;
    }

    public String getTxtMobileNo() {
        return txtMobileNo;
    }

    public void setTxtMobileNo(String txtMobileNo) {
        this.txtMobileNo = txtMobileNo;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }
    private String isTran = "";
    private String cust_id = "";
    private HashMap authorizeMap = new HashMap();
    private HashMap masterMap = new HashMap();
    private HashMap productMap = new HashMap();
    NomineeOB nomineeOB = new NomineeOB();
    private ArrayList nomineeList = new ArrayList();
    private static MDSApplicationOB MDSApplicationOB;

    static {
        try {
            currDt = ClientUtil.getCurrentDate();
            MDSApplicationOB = new MDSApplicationOB();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /* Sets the HashMap required to set JNDI,Home and Remote*/
    public MDSApplicationOB() throws Exception {
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "MDSApplicationJNDI");
        operationMap.put(CommonConstants.HOME, "mdsapplication.MDSApplicationHome");
        operationMap.put(CommonConstants.REMOTE, "mdsapplication.MDSApplication");
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        notifyObservers();
        fillDropdown();
    }

    public static MDSApplicationOB getInstance() {
        return MDSApplicationOB;
    }

    /**
     * Setter for property lblStatus.
     *
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }

    public String getLblStatus() {
        return lblStatus;
    }

    public void resetStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }

    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        notifyObservers();
    }

    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        notifyObservers();
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
        setChanged();
    }

    public void setActionType(int action) {
        actionType = action;
        setChanged();
    }

    public int getActionType() {
        return actionType;
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
                setTxtCustomerIdCr(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                setSelectedBranchID(CommonUtil.convertObjToStr(mapData.get("BRANCH_ID")));
                cbmProdType.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                setCboProdType(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                setCbmProdId(getCboProdType());
                cbmProdId.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                isExists = true;
            } else {
               isExists = false;
            }
            mapDataList = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapData = null;
        return isExists;
    }
    /**
     * A method to set the combo box values
     */
    private void fillDropdown() throws Exception {
        try {
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();

            lookup_keys.add("CUSTOMER.CITY");
            lookup_keys.add("CUSTOMER.STATE");
            lookup_keys.add("PRODUCTTYPE");

            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
            lookUpHash = null;

            getKeyValue((HashMap) keyValue.get("CUSTOMER.CITY"));
            cbmCity = new ComboBoxModel(key, value);

            getKeyValue((HashMap) keyValue.get("CUSTOMER.STATE"));
            cbmState = new ComboBoxModel(key, value);

            getKeyValue((HashMap) keyValue.get("PRODUCTTYPE"));
            cbmProdType = new ComboBoxModel(key, value);
           // cbmProdType.removeKeyAndElement("TD");
            cbmProdType.removeKeyAndElement("TL");
            cbmProdId = new ComboBoxModel();
            keyValue = null;
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    public void doSave(NomineeOB objNomineeOB) {
        if (getActionType() != ClientConstants.ACTIONTYPE_DELETE) {
        }
        doAction(objNomineeOB);
    }

    /**
     * To perform the appropriate operation
     */
    public void doAction(NomineeOB objNomineeOB) {
        try {
            doActionPerform(objNomineeOB);
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }

    /**
     * To perform the necessary action
     */
    private void doActionPerform(NomineeOB objNomineeOB) throws Exception {
        System.out.println("doActionPerformdoActionPerformdoActionPerform");
        final HashMap data = new HashMap();
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        if (getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            data.put("MASTER_DATA", getMasterMap());
            data.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        }
        data.put("COMMAND", getCommand());
        System.out.println("getCommandgetCommand" + getCommand());
        mdsApplicationTO = setMDSApplicationTO();
        System.out.println("getTransId()getTransId()" + getTransId());
        mdsApplicationTO.setCommand(CommonUtil.convertObjToStr(data.get("COMMAND")));
        data.put("mdsApplicationTO", mdsApplicationTO);
        System.out.println("transactionDetailsTOtransactionDet" + transactionDetailsTO);
        if (transactionDetailsTO == null) {
            transactionDetailsTO = new LinkedHashMap();
        }
        if (deletedTransactionDetailsTO != null) {
            transactionDetailsTO.put(DELETED_TRANS_TOs, deletedTransactionDetailsTO);
            deletedTransactionDetailsTO = null;
        }
        transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
        allowedTransactionDetailsTO = null;

        if (mdsApplicationTO.getNominee().equals("Y")) {
            data.put("AccountNomineeTO", objNomineeOB.getNomimeeList());
            data.put("AccountNomineeDeleteTO", objNomineeOB.getDeleteNomimeeList());
        }
        System.out.println("oldTransDetMap.size()oldTransDetMap.size()" + oldTransDetMap.size());
        if (oldTransDetMap != null && oldTransDetMap.size() > 0) {
            if (oldTransDetMap.containsKey("AMT_TRANSACTION")) {
                data.put("AMT_TRANSACTION", oldTransDetMap.get("AMT_TRANSACTION"));
            }
            if (oldTransDetMap.containsKey("SERVICE_TAX_AMT_TRANSACTION")) {
                data.put("SERVICE_TAX_AMT_TRANSACTION", oldTransDetMap.get("SERVICE_TAX_AMT_TRANSACTION"));
            }
            if (oldTransDetMap.containsKey("TOTAL_AMT_TRANSACTION")) {
                data.put("TOTAL_AMT_TRANSACTION", oldTransDetMap.get("TOTAL_AMT_TRANSACTION"));
            }
        }

        data.put("UPDATE_CHIT_NO", CommonUtil.convertObjToStr(getUpdateChitNo()));
        System.out.println("transactionDetailsTOtransactionDetai" + transactionDetailsTO);
        data.put("TransactionTO", transactionDetailsTO);
        data.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            data.put("NO_TRANSACTION", "NO_TRANSACTION");
        }  
        if (isChkMobileBankingAD()) {
            data.put("SMSSubscriptionTO", setSMSSubcription());
        }
        //if(getActionType() == ClientConstants.ACTIONTYPE_NEW){
        //    System.out.println("###### :getPredefinedInstallBonus " + getPredefinedInstallBonus());
        //    if(getPredefinedInstallBonus() > 0){
         //       data.put("PREDEFINED_INSTALL_BONUS", getPredefinedInstallBonus());
         //   }
       // }  
        System.out.println("data ###### : " + data);
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        System.out.println("###### : " + proxyResultMap);
        setProxyReturnMap(proxyResultMap);
        if (getActionType() == ClientConstants.ACTIONTYPE_NEW && proxyResultMap != null && proxyResultMap.containsKey("CHITTAL_NO")) {
            ClientUtil.showMessageWindow("Chittal No" + ": " + CommonUtil.convertObjToStr(proxyResultMap.get("CHITTAL_NO")));
        }
        if(getActionType()==ClientConstants.ACTIONTYPE_EDIT && proxyResultMap!=null && proxyResultMap.containsKey("CHITTAL_NO")){
            ClientUtil.showMessageWindow("Updated details with chittal No : "+CommonUtil.convertObjToStr(proxyResultMap.get("CHITTAL_NO")));
        }
        setResult(getActionType());
    }

    public void schemeDetailsList() {
    }
    
    public SMSSubscriptionTO setSMSSubcription() {
        if (isChkMobileBankingAD()) {
            objSMSSubscriptionTO = new SMSSubscriptionTO();
            objSMSSubscriptionTO.setProdType("MDS");
            objSMSSubscriptionTO.setProdId(getTxtSchemeName());
            objSMSSubscriptionTO.setActNum(getTxtChittalNo());
            objSMSSubscriptionTO.setMobileNo(CommonUtil.convertObjToStr(getTxtMobileNo()));
            Date smsSubscriptionDt = DateUtil.getDateMMDDYYYY(getSubscribtionDt());
            if(smsSubscriptionDt != null){
                Date smsDt = (Date) ClientUtil.getCurrentDate().clone();
                smsDt.setDate(smsSubscriptionDt.getDate());
                smsDt.setMonth(smsSubscriptionDt.getMonth());
                smsDt.setYear(smsSubscriptionDt.getYear());
                objSMSSubscriptionTO.setSubscriptionDt(smsDt);
            }else{
                objSMSSubscriptionTO.setSubscriptionDt(DateUtil.getDateMMDDYYYY(getSubscribtionDt()));
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){            
                objSMSSubscriptionTO.setStatus(CommonConstants.STATUS_MODIFIED);
            }else if(getActionType()==ClientConstants.ACTIONTYPE_DELETE){    
                objSMSSubscriptionTO.setStatus(CommonConstants.STATUS_DELETED);
            }else{
                objSMSSubscriptionTO.setStatus(CommonConstants.STATUS_CREATED);                
            } 
            objSMSSubscriptionTO.setCreatedDt(ClientUtil.getCurrentDate());
            objSMSSubscriptionTO.setCreatedBy(TrueTransactMain.USER_ID);
            objSMSSubscriptionTO.setStatusBy(TrueTransactMain.USER_ID);
            objSMSSubscriptionTO.setStatusDt(ClientUtil.getCurrentDate());
        }else
             objSMSSubscriptionTO = null; 
        
        return objSMSSubscriptionTO;
    }
    

    /**
     * Gets the command issued Insert , Upadate or Delete *
     */
    private String getCommand() throws Exception {
        String command = null;
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                command = CommonConstants.TOSTATUS_INSERT;
                break;
            case ClientConstants.ACTIONTYPE_COPY:
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

    private MDSApplicationTO setMDSApplicationTO() {
        try {
            mdsApplicationTO = new MDSApplicationTO();
            mdsApplicationTO.setSchemeName(getTxtSchemeName());
            mdsApplicationTO.setDivisionNo(getTxtDivisionNo()); //AJITH
            mdsApplicationTO.setChittalNo(CommonUtil.convertObjToStr(getTxtChittalNo()));
            mdsApplicationTO.setSubNo(getTxtSubNo()); //AJITH
            mdsApplicationTO.setChitNo(getTxtSubNo());  //AJITH 
            mdsApplicationTO.setChitStartDt(DateUtil.getDateMMDDYYYY(getTdtChitStartDt()));
            mdsApplicationTO.setChitEndDt(DateUtil.getDateMMDDYYYY(getTdtChitEndDt()));
            mdsApplicationTO.setInstAmt(getTxtInstAmt());   //AJITH
            mdsApplicationTO.setApplnNo(getTxtApplnNo());   //AJITH
            mdsApplicationTO.setApplnDate(DateUtil.getDateMMDDYYYY(getTdtApplnDate()));
            mdsApplicationTO.setIsTran(getIsTran());
            if (getChkThalayal() == true) {
                mdsApplicationTO.setThalayal("Y");
            } else {
                mdsApplicationTO.setThalayal("N");
            }
            if (getChkMunnal() == true) {
                mdsApplicationTO.setMunnal("Y");
            } else {
                mdsApplicationTO.setMunnal("N");
            }
            if (getChkCoChittal() == true) {
                mdsApplicationTO.setCoChittal("Y");
            } else {
                mdsApplicationTO.setCoChittal("N");
            }
            mdsApplicationTO.setMembershipNo(CommonUtil.convertObjToStr(getTxtMembershipNo()));
            mdsApplicationTO.setMembershipType(CommonUtil.convertObjToStr(getTxtMembershipType()));
            mdsApplicationTO.setMembershipName(getTxtMembershipName());
            mdsApplicationTO.setHouseStNo(getTxtHouseStNo());
            mdsApplicationTO.setCudt_id(getCust_id());
            mdsApplicationTO.setArea(getTxtArea());
            mdsApplicationTO.setCity(getCboCity());
            mdsApplicationTO.setSalaryRecovery(getRdoSalaryRecovery());
            mdsApplicationTO.setState(getCboState());
            mdsApplicationTO.setPin(CommonUtil.convertObjToDouble(getTxtpin()));
            if (getChkStandingInstn() == true) {
                mdsApplicationTO.setStandingInstn("Y");
                mdsApplicationTO.setProdType(CommonUtil.convertObjToStr(getCbmProdType().getKeyForSelected()));
                mdsApplicationTO.setProdId(CommonUtil.convertObjToStr(getCbmProdId().getKeyForSelected()));
                if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){// Added by nithya on 16-08-2019 for KD 585 - gds standing instruction issue-Kuttilanji
                    mdsApplicationTO.setProdType(getCboProdType());
                    mdsApplicationTO.setProdId(getCboProdId());
                }
                mdsApplicationTO.setDrAccNo(getTxtCustomerIdCr());
                System.out.println("in yyyy" + mdsApplicationTO);
            } else {
                mdsApplicationTO.setStandingInstn("N");
            }
            if (getChkNominee() == true) {
                mdsApplicationTO.setNominee("Y");
            } else {
                mdsApplicationTO.setNominee("N");
            }
            mdsApplicationTO.setRemarks(getTxtRemarks());
            mdsApplicationTO.setStatusDt(currDt);
            mdsApplicationTO.setBranchCode(TrueTransactMain.BRANCH_ID);
            System.out.println("getTransId()..." + getTransId());
            if (getTransId() != null && !getTransId().equals("")) {
                mdsApplicationTO.setTransId(getTransId());
            } else {
                mdsApplicationTO.setTransId(null);
                System.out.println("mmmm");
            }
            if (getIsTran().equals("N")) {
                mdsApplicationTO.setTransId(null);
                System.out.println("nnnyyy");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mdsApplicationTO;
    }

    public boolean populateData(HashMap whereMap, NomineeOB objNomineeOB) {
        boolean aliasBranchTableFlag = false;
        System.out.println("aliasBranchTableFlag" + aliasBranchTableFlag);
        HashMap mapData = new HashMap();
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            System.out.println("mapData.asdsd." + mapData);
            populateOB(mapData, objNomineeOB);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        System.out.println(aliasBranchTableFlag);
        return aliasBranchTableFlag;
    }

    private void populateOB(HashMap mapData, NomineeOB objNomineeOB) {
        try {
            System.out.println("nnnnn");
            mdsApplicationTO = new MDSApplicationTO();
            mdsApplicationTO = (MDSApplicationTO) ((List) mapData.get("mdsApplicationTO")).get(0);
            List list = (List) mapData.get("TransactionTO");
            System.out.println("list" + list);
            if (list != null & list.size() > 0) {
                transactionOB.setDetails(list);
            }
            System.out.println("list" + list);
            getMDSApplicationTO(mdsApplicationTO);

            if (getChkNominee() == true && mapData.containsKey("AccountNomineeList")) {
                nomineeList = (ArrayList) mapData.get("AccountNomineeList");
                objNomineeOB.setNomimeeList(nomineeList);
                objNomineeOB.setNomineeTabData();
                objNomineeOB.ttNotifyObservers();
            }

            objSMSSubscriptionTO = null;
            if (mapData.containsKey("SMSSubscriptionTO") && ((List) mapData.get("SMSSubscriptionTO")).size() > 0){
                objSMSSubscriptionTO = (SMSSubscriptionTO) ((List) mapData.get("SMSSubscriptionTO")).get(0);
                setSMSSubscriptionTO(objSMSSubscriptionTO);
            }
        
            oldTransDetMap = new HashMap();
            if (mapData.containsKey("AMT_TRANSACTION")) {
                oldTransDetMap.put("AMT_TRANSACTION", mapData.get("AMT_TRANSACTION"));
            }
            if (mapData.containsKey("SERVICE_TAX_AMT_TRANSACTION")) {
                oldTransDetMap.put("SERVICE_TAX_AMT_TRANSACTION", mapData.get("SERVICE_TAX_AMT_TRANSACTION"));
            }
            if (mapData.containsKey("TOTAL_AMT_TRANSACTION")) {
                oldTransDetMap.put("TOTAL_AMT_TRANSACTION", mapData.get("TOTAL_AMT_TRANSACTION"));
            }
            notifyObservers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMDSApplicationTO(MDSApplicationTO mdsApplicationTO) {
        System.out.println("mmm" + mdsApplicationTO);
        //System.out.println("mdsApplicationTO" + mdsApplicationTO.getChitNo());
        try {
            setTxtSchemeName(mdsApplicationTO.getSchemeName());
            setTxtDivisionNo(mdsApplicationTO.getDivisionNo()); //AJITH
            setTxtChittalNo(CommonUtil.convertObjToStr(mdsApplicationTO.getChittalNo()));
            setTxtSubNo(mdsApplicationTO.getSubNo());   //AJITH
            setTdtChitStartDt(DateUtil.getStringDate(mdsApplicationTO.getChitStartDt()));
            setTdtChitEndDt(DateUtil.getStringDate(mdsApplicationTO.getChitEndDt()));
            setTxtInstAmt(mdsApplicationTO.getInstAmt());   //AJITH
            setTxtApplnNo(mdsApplicationTO.getApplnNo());   //AJITH
            setTdtApplnDate(DateUtil.getStringDate(mdsApplicationTO.getApplnDate()));
            setCust_id(CommonUtil.convertObjToStr(mdsApplicationTO.getCudt_id()));
            if (mdsApplicationTO.getThalayal() != null && !mdsApplicationTO.getThalayal().equals("")) {
                if (mdsApplicationTO.getThalayal().equals("Y")) {
                    setChkThalayal(true);
                } else {
                    setChkThalayal(false);
                }
            } else {
                setChkThalayal(false);
            }
            if (mdsApplicationTO.getMunnal() != null && !mdsApplicationTO.getMunnal().equals("")) {
                if (mdsApplicationTO.getMunnal().equals("Y")) {
                    setChkMunnal(true);
                } else {
                    setChkMunnal(false);
                }
            } else {
                setChkMunnal(false);
            }
            if (mdsApplicationTO.getCoChittal() != null && !mdsApplicationTO.getCoChittal().equals("")) {
                if (mdsApplicationTO.getCoChittal().equals("Y")) {
                    setChkCoChittal(true);
                } else {
                    setChkCoChittal(false);
                }
            } else {
                setChkCoChittal(false);
            }
            if (mdsApplicationTO.getStandingInstn() != null && !mdsApplicationTO.getStandingInstn().equals("")) {
                if (mdsApplicationTO.getStandingInstn().equals("Y")) {
                    setChkStandingInstn(true);
                    getCbmProdType().setKeyForSelected(mdsApplicationTO.getProdType());
                    //String prodType= getCbmProdType().getDataForKey(mdsApplicationTO.getProdType()).toString();
                    System.out.println("mdsApplicationTO.getProdId() " + mdsApplicationTO.getProdId());
                    String prodType = mdsApplicationTO.getProdType();
                    // System.out.println("dddd "+S);
                    setCbmProdId(prodType);
                    getCbmProdId().setKeyForSelected(mdsApplicationTO.getProdId());
                    if (!prodType.equals("GL")) {
                        getCbmProdId().setSelectedItem(getCbmProdId().getDataForKey(mdsApplicationTO.getProdId()));
                        setCboProdId(getCbmProdId().getDataForKey(mdsApplicationTO.getProdId()).toString());
                        System.out.println("sdffdsfnnnn" + getCbmProdId().getDataForKey(mdsApplicationTO.getProdId()));

                    }
                    setTxtCustomerIdCr(mdsApplicationTO.getDrAccNo());
                    System.out.println("mdsApplicationTO.getDrAccNo(),,,," + mdsApplicationTO.getDrAccNo());
                    System.out.println("vvvv" + getTxtCustomerIdCr());
                } else {
                    setChkStandingInstn(false);
                }
            } else {
                setChkStandingInstn(false);
            }
            setTxtMembershipNo(CommonUtil.convertObjToStr(mdsApplicationTO.getMembershipNo()));
            setTxtMembershipName(mdsApplicationTO.getMembershipName());
            setTxtMembershipType(mdsApplicationTO.getMembershipType());
            setTxtHouseStNo(mdsApplicationTO.getHouseStNo());
            setTxtArea(mdsApplicationTO.getArea());
            setCboCity(mdsApplicationTO.getCity());
            setCboState(mdsApplicationTO.getState());
            setRdoSalaryRecovery(CommonUtil.convertObjToStr(mdsApplicationTO.getSalaryRecovery()));
            setTxtpin(CommonUtil.convertObjToStr(mdsApplicationTO.getPin()));

            if (mdsApplicationTO.getNominee().equals("Y")) {
                setChkNominee(true);
            } else {
                setChkNominee(false);
            }
            setTxtRemarks(mdsApplicationTO.getRemarks());
            System.out.println("aaa");
            if (mdsApplicationTO.getTransId() != null && !mdsApplicationTO.getTransId().equals("")) {
                setTransId(mdsApplicationTO.getTransId());
            } else {
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean getChittalNo() {
        boolean flag = false;
        boolean sameNoFlag = false;
        try {
            HashMap where = new HashMap();
            HashMap mapData = new HashMap();
            String strPrefix = "";
            String strNum = "";
            int len = 13;
            int nofOfMember = 0;
            int suffix = 0;
            int lastValue = 0;
            int minDivisionNo = 0;
            int maxNofCoMember = 0;
            HashMap schemeMap = new HashMap();
            schemeMap.put("SCHEME_NAME", getTxtSchemeName());
//            HashMap deletedStatusMap = new HashMap();
//            deletedStatusMap.put("SCHEME_NAME",getTxtSchemeName());
//            List lst = ClientUtil.executeQuery("getSelectDeletedRecordNo", deletedStatusMap);       //Deleted Or Rejected Record
//            if(lst!=null && lst.size()>0){
//                deletedStatusMap = (HashMap)lst.get(0);
//                int minChitNo = CommonUtil.convertObjToInt(deletedStatusMap.get("MIN_CHIT_NO"));
//                lst = (List) ClientUtil.executeQuery("getChittalNOUISide", schemeMap);
//                if(lst != null && lst.size() > 0){
//                    mapData = (HashMap)lst.get(0);
//                }
//                if (mapData.containsKey("PREFIX")) {
//                    strPrefix = (String) mapData.get("PREFIX");
//                }
//                suffix = CommonUtil.convertObjToInt(mapData.get("SUFFIX"));
//                int nofOfDivision = CommonUtil.convertObjToInt(mapData.get("NO_OF_DIVISIONS"));
//                nofOfMember = CommonUtil.convertObjToInt(mapData.get("NO_OF_MEMBER_PER_DIVISION"));
//                maxNofCoMember = CommonUtil.convertObjToInt(mapData.get("CO_NO_OF_INSTALLMENTS"));
//                lastValue = CommonUtil.convertObjToInt(mapData.get("LAST_VALUE"));
//                int numFrom = strPrefix.trim().length();
//                if(minChitNo == 1 && !deletedStatusMap.get("STATUS").equals("DELETED") && !deletedStatusMap.get("AUTHORIZE_STATUS").equals("REJECTED") && 
//                !deletedStatusMap.get("AUTHORIZE_STATUS").equals("DELETED")){
//                    String newID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))));
//                    String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE")))+1);
//                    String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
//                    //suresh
//                    if(getMultipleMember().equals("N")){
//                        if((nofOfDivision * nofOfMember)<lastValue){
//                            ClientUtil.showAlertWindow("Maximum No of Member already reached in the Scheme, can not add more Member... !!!");
//                            flag = true;
//                        }else{
//                            setTxtChittalNo(String.valueOf(genID));
//                            setNextActNo(String.valueOf(nxtID));
//                            flag = false;
//                        }
//                    }else if(getMultipleMember().equals("Y")){
//                        if(maxNofCoMember < lastValue){
//                            ClientUtil.showAlertWindow("Maximum No of Member already reached in the Scheme, can not add more Member... !!!");
//                            setTxtChittalNo("");
//                            flag = false;
//                        }else{
//                            setTxtChittalNo(String.valueOf(genID));
//                            setNextActNo(String.valueOf(nxtID));
//                            flag = false;
//                        }
//                    }
//                }else{
//                    String minChit = String.valueOf(Integer.parseInt(String.valueOf(deletedStatusMap.get("MIN_CHIT_NO"))));
//                    String genID = strPrefix.toUpperCase() + CommonUtil.lpad(minChit, len - numFrom, '0');
//                    setTxtChittalNo(String.valueOf(genID));
//                    setNextActNo(String.valueOf(minChitNo));
//                    HashMap divisionMap = new HashMap();
//                    divisionMap.put("CHIT_NO",String.valueOf(minChitNo));
//                    divisionMap.put("SCHEME_NAME",getTxtSchemeName());
//                    lst = ClientUtil.executeQuery("getSelectDeletedRecordDivisionNo", divisionMap);
//                    if(lst!=null && lst.size()>0){
//                        divisionMap = (HashMap)lst.get(0);
//                        minDivisionNo = CommonUtil.convertObjToInt(divisionMap.get("DIVISION_NO"));
//                    }
//                    sameNoFlag = true;
//                }
//                if(getMultipleMember().equals("N")){
//                    if(sameNoFlag == false){
//                        lst = (List) ClientUtil.executeQuery("getSchemeDetailsList", schemeMap);
//                        if(lst != null && lst.size() > 0){
//                            productMap = (HashMap)lst.get(0);
//                            int noOfDiv = CommonUtil.convertObjToInt(productMap.get("NO_OF_DIVISIONS"));
//                            int totMember = CommonUtil.convertObjToInt(productMap.get("TOTAL_NO_OF_MEMBERS"));
//                            maxNofCoMember = CommonUtil.convertObjToInt(mapData.get("CO_NO_OF_INSTALLMENTS"));
//                            int totChitNo = CommonUtil.convertObjToInt(productMap.get("TOTAL_CHIT_NO"));
//                            if(suffix*nofOfMember>=lastValue){
//                                suffix = suffix;
//                                setTxtDivisionNo(String.valueOf(suffix));
//                            }else{
//                                suffix = suffix;
//                                setTxtDivisionNo(String.valueOf(suffix+1));
//                            }
//                        }
//                    }else{
//                        setTxtDivisionNo(String.valueOf(minDivisionNo));
//                    }
//                }
//            }else{
            List lst = (List) ClientUtil.executeQuery("getChittalNOUISide", schemeMap);
            if (lst != null && lst.size() > 0) {
                mapData = (HashMap) lst.get(0);
            }
            if (mapData.containsKey("PREFIX")) {
                strPrefix = (String) mapData.get("PREFIX");
            }
            double totalSchemeAmt = 0.0;
            double totalChittalAmt = 0.0;
            List applicationList = (List) ClientUtil.executeQuery("getSumOfInstAmtForChittal", schemeMap);
            if (applicationList != null && applicationList.size() > 0) {
                where = (HashMap) applicationList.get(0);
                totalChittalAmt = CommonUtil.convertObjToDouble(where.get("TOTAL_INST_AMT")).doubleValue();
            }
            totalSchemeAmt = CommonUtil.convertObjToDouble(mapData.get("TOTAL_AMOUNT_SCHEME")).doubleValue();
            suffix = CommonUtil.convertObjToInt(mapData.get("SUFFIX"));
            int nofOfDivision = CommonUtil.convertObjToInt(mapData.get("NO_OF_DIVISIONS"));
            nofOfMember = CommonUtil.convertObjToInt(mapData.get("NO_OF_MEMBER_PER_DIVISION"));
            maxNofCoMember = CommonUtil.convertObjToInt(mapData.get("CO_NO_OF_INSTALLMENTS"));
            lastValue = CommonUtil.convertObjToInt(mapData.get("LAST_VALUE"));
            int numFrom = strPrefix.trim().length();
            String newID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))));
            String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))) + 1);
            String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
            if (getMultipleMember().equals("N")) {
                //                    if((nofOfDivision * nofOfMember)<lastValue){
                if (totalSchemeAmt <= totalChittalAmt) {
                    ClientUtil.showAlertWindow("Maximum No of Member already reached in the Scheme, can not add more Member... !!!");
                    flag = true;
                } else {
                    setTxtChittalNo(String.valueOf(genID));
                    setNextActNo(String.valueOf(newID));
                    flag = false;
                }
            } else if (getMultipleMember().equals("Y")) {
                //                    if(maxNofCoMember < lastValue){
                if (totalSchemeAmt <= totalChittalAmt) {
                    ClientUtil.showAlertWindow("Maximum No of Member already reached in the Scheme, can not add more Member... !!!");
                    setTxtChittalNo("");
                    flag = false;
                } else {
                    setTxtChittalNo(String.valueOf(genID));
                    setNextActNo(String.valueOf(nxtID));
                    flag = false;
                }
            }
            if (getMultipleMember().equals("N")) {
                lst = (List) ClientUtil.executeQuery("getSchemeDetailsList", schemeMap);
                if (lst != null && lst.size() > 0) {
                    productMap = (HashMap) lst.get(0);
                    int noOfDiv = CommonUtil.convertObjToInt(productMap.get("NO_OF_DIVISIONS"));
                    int totMember = CommonUtil.convertObjToInt(productMap.get("TOTAL_NO_OF_MEMBERS"));
                    int totChitNo = CommonUtil.convertObjToInt(productMap.get("TOTAL_CHIT_NO"));
                    double installmentAmt = CommonUtil.convertObjToInt(productMap.get("INSTALLMENT_AMOUNT"));
                    if (noOfDiv <= 1) {
                        lastValue = (int) (totalChittalAmt / installmentAmt);
                    }
//                        System.out.println("################ lastValue : "+lastValue);
                    if (suffix * nofOfMember >= lastValue) {
                        suffix = suffix;
                        setTxtDivisionNo(suffix);    //AJITH Changed from String.valueOf(suffix)
                    } else {
                        suffix = suffix;
                        setTxtDivisionNo((suffix + 1));   //AJITH Changed from String.valueOf(suffix+1)
                    }
                }
            }
//            }
            HashMap detailsMap = new HashMap();
            detailsMap.put("SCHEME_NAME", getTxtSchemeName());
            lst = ClientUtil.executeQuery("getSelectSchemeAcctHead", detailsMap);
            if (lst != null && lst.size() > 0) {
                productMap = (HashMap) lst.get(0);
            }
        } catch (Exception e) {
        }
        return flag;
    }

    protected void getCustomerAddressDetails(String value) {
        HashMap custAddressMap = new HashMap();
        custAddressMap.put("CUST_ID", value);
        List lst = ClientUtil.executeQuery("getCustomerAddressDetails", custAddressMap);
        if (lst != null && lst.size() > 0) {
            custAddressMap = (HashMap) lst.get(0);
            setTxtHouseStNo(CommonUtil.convertObjToStr(custAddressMap.get("street")));
            setTxtArea(CommonUtil.convertObjToStr(custAddressMap.get("area")));
            setCboCity(CommonUtil.convertObjToStr(getCbmCity().getDataForKey(CommonUtil.convertObjToStr(custAddressMap.get("city")))));
            setCboState(CommonUtil.convertObjToStr(getCbmState().getDataForKey(CommonUtil.convertObjToStr(custAddressMap.get("state")))));
            setTxtpin(CommonUtil.convertObjToStr(custAddressMap.get("pinCode")));
        }
    }

    /**
     * Resets the General Remittance Fields to Null
     */
    public void resetOBFields() {
        setTxtSchemeName("");
        setTxtDivisionNo(null); //AJITH
        setTxtChittalNo("");
        setTxtSubNo(null);  //AJITH
        setTdtChitStartDt("");
        setTdtChitEndDt("");
        setTxtInstAmt(null);    //AJITH
        setTxtApplnNo(null);    //AJITH
        setTdtApplnDate("");
        setChkThalayal(false);
        setChkMunnal(false);
        setChkCoChittal(false);
        setTxtMembershipNo("");
        setTxtMembershipName("");
        setTxtHouseStNo("");
        setTxtArea("");
        setCboCity("");
        setCboState("");
        setRdoSalaryRecovery("");
//        setCboProdType("");
//        setCboProdId("");
        getCbmProdType().setKeyForSelected("");
        getCbmProdId().setKeyForSelected("");
        setTxtCustomerIdCr("");
        setTxtpin("");
        setChkStandingInstn(false);
        setChkNominee(false);
        setTxtRemarks("");
        setChkMobileBankingAD(false);
        setTxtMobileNo("");
        setSubscribtionDt("");
        setPredefinedInstallBonus(0.0);
    }
    
    private void setSMSSubscriptionTO(SMSSubscriptionTO objSMSSubscriptionTO) {
        setChkMobileBankingAD(true);
        setTxtMobileNo(CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()));
        setSubscribtionDt(DateUtil.getStringDate(objSMSSubscriptionTO.getSubscriptionDt()));
    }

    public void setCbmProdId(String prodType) {
        if (prodType.equals("GL")) {
            key = new ArrayList();
            value = new ArrayList();
        } else {
            try {
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + prodType);
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        cbmProdId = new ComboBoxModel(key, value);
        this.cbmProdId = cbmProdId;
        setChanged();
    }

    /**
     * Getter for property txtSchemeName.
     *
     * @return Value of property txtSchemeName.
     */
    public java.lang.String getTxtSchemeName() {
        return txtSchemeName;
    }

    /**
     * Setter for property txtSchemeName.
     *
     * @param txtSchemeName New value of property txtSchemeName.
     */
    public void setTxtSchemeName(java.lang.String txtSchemeName) {
        this.txtSchemeName = txtSchemeName;
    }

    /**
     * Getter for property tdtChitStartDt.
     *
     * @return Value of property tdtChitStartDt.
     */
    public java.lang.String getTdtChitStartDt() {
        return tdtChitStartDt;
    }

    /**
     * Setter for property tdtChitStartDt.
     *
     * @param tdtChitStartDt New value of property tdtChitStartDt.
     */
    public void setTdtChitStartDt(java.lang.String tdtChitStartDt) {
        this.tdtChitStartDt = tdtChitStartDt;
    }

    /**
     * Getter for property tdtChitEndDt.
     *
     * @return Value of property tdtChitEndDt.
     */
    public java.lang.String getTdtChitEndDt() {
        return tdtChitEndDt;
    }

    /**
     * Setter for property tdtChitEndDt.
     *
     * @param tdtChitEndDt New value of property tdtChitEndDt.
     */
    public void setTdtChitEndDt(java.lang.String tdtChitEndDt) {
        this.tdtChitEndDt = tdtChitEndDt;
    }

    public Double getTxtInstAmt() {
        return txtInstAmt;
    }

    public void setTxtInstAmt(Double txtInstAmt) {
        this.txtInstAmt = txtInstAmt;
    }

    /**
     * Getter for property tdtApplnDate.
     *
     * @return Value of property tdtApplnDate.
     */
    public java.lang.String getTdtApplnDate() {
        return tdtApplnDate;
    }

    /**
     * Setter for property tdtApplnDate.
     *
     * @param tdtApplnDate New value of property tdtApplnDate.
     */
    public void setTdtApplnDate(java.lang.String tdtApplnDate) {
        this.tdtApplnDate = tdtApplnDate;
    }

    /**
     * Getter for property txtMembershipNo.
     *
     * @return Value of property txtMembershipNo.
     */
    public java.lang.String getTxtMembershipNo() {
        return txtMembershipNo;
    }

    /**
     * Setter for property txtMembershipNo.
     *
     * @param txtMembershipNo New value of property txtMembershipNo.
     */
    public void setTxtMembershipNo(java.lang.String txtMembershipNo) {
        this.txtMembershipNo = txtMembershipNo;
    }

    /**
     * Getter for property txtMembershipName.
     *
     * @return Value of property txtMembershipName.
     */
    public java.lang.String getTxtMembershipName() {
        return txtMembershipName;
    }

    /**
     * Setter for property txtMembershipName.
     *
     * @param txtMembershipName New value of property txtMembershipName.
     */
    public void setTxtMembershipName(java.lang.String txtMembershipName) {
        this.txtMembershipName = txtMembershipName;
    }

    /**
     * Getter for property txtHouseStNo.
     *
     * @return Value of property txtHouseStNo.
     */
    public java.lang.String getTxtHouseStNo() {
        return txtHouseStNo;
    }

    /**
     * Setter for property txtHouseStNo.
     *
     * @param txtHouseStNo New value of property txtHouseStNo.
     */
    public void setTxtHouseStNo(java.lang.String txtHouseStNo) {
        this.txtHouseStNo = txtHouseStNo;
    }

    /**
     * Getter for property txtArea.
     *
     * @return Value of property txtArea.
     */
    public java.lang.String getTxtArea() {
        return txtArea;
    }

    /**
     * Setter for property txtArea.
     *
     * @param txtArea New value of property txtArea.
     */
    public void setTxtArea(java.lang.String txtArea) {
        this.txtArea = txtArea;
    }

    /**
     * Getter for property cboCity.
     *
     * @return Value of property cboCity.
     */
    public java.lang.String getCboCity() {
        return cboCity;
    }

    /**
     * Setter for property cboCity.
     *
     * @param cboCity New value of property cboCity.
     */
    public void setCboCity(java.lang.String cboCity) {
        this.cboCity = cboCity;
    }

    /**
     * Getter for property cboState.
     *
     * @return Value of property cboState.
     */
    public java.lang.String getCboState() {
        return cboState;
    }

    /**
     * Setter for property cboState.
     *
     * @param cboState New value of property cboState.
     */
    public void setCboState(java.lang.String cboState) {
        this.cboState = cboState;
    }

    /**
     * Getter for property txtRemarks.
     *
     * @return Value of property txtRemarks.
     */
    public java.lang.String getTxtRemarks() {
        return txtRemarks;
    }

    /**
     * Setter for property txtRemarks.
     *
     * @param txtRemarks New value of property txtRemarks.
     */
    public void setTxtRemarks(java.lang.String txtRemarks) {
        this.txtRemarks = txtRemarks;
    }

    /**
     * Getter for property txtMembershipType.
     *
     * @return Value of property txtMembershipType.
     */
    public java.lang.String getTxtMembershipType() {
        return txtMembershipType;
    }

    /**
     * Setter for property txtMembershipType.
     *
     * @param txtMembershipType New value of property txtMembershipType.
     */
    public void setTxtMembershipType(java.lang.String txtMembershipType) {
        this.txtMembershipType = txtMembershipType;
    }

    /**
     * Getter for property cbmCity.
     *
     * @return Value of property cbmCity.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCity() {
        return cbmCity;
    }

    /**
     * Setter for property cbmCity.
     *
     * @param cbmCity New value of property cbmCity.
     */
    public void setCbmCity(com.see.truetransact.clientutil.ComboBoxModel cbmCity) {
        this.cbmCity = cbmCity;
    }

    /**
     * Getter for property cbmState.
     *
     * @return Value of property cbmState.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmState() {
        return cbmState;
    }

    /**
     * Setter for property cbmState.
     *
     * @param cbmState New value of property cbmState.
     */
    public void setCbmState(com.see.truetransact.clientutil.ComboBoxModel cbmState) {
        this.cbmState = cbmState;
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
     * Getter for property transactionDetailsTO.
     *
     * @return Value of property transactionDetailsTO.
     */
    public java.util.LinkedHashMap getTransactionDetailsTO() {
        return transactionDetailsTO;
    }

    /**
     * Setter for property transactionDetailsTO.
     *
     * @param transactionDetailsTO New value of property transactionDetailsTO.
     */
    public void setTransactionDetailsTO(java.util.LinkedHashMap transactionDetailsTO) {
        this.transactionDetailsTO = transactionDetailsTO;
    }

    /**
     * Getter for property deletedTransactionDetailsTO.
     *
     * @return Value of property deletedTransactionDetailsTO.
     */
    public java.util.LinkedHashMap getDeletedTransactionDetailsTO() {
        return deletedTransactionDetailsTO;
    }

    /**
     * Setter for property deletedTransactionDetailsTO.
     *
     * @param deletedTransactionDetailsTO New value of property
     * deletedTransactionDetailsTO.
     */
    public void setDeletedTransactionDetailsTO(java.util.LinkedHashMap deletedTransactionDetailsTO) {
        this.deletedTransactionDetailsTO = deletedTransactionDetailsTO;
    }

    /**
     * Getter for property chkStandingInstn.
     *
     * @return Value of property chkStandingInstn.
     */
    public boolean getChkStandingInstn() {
        return chkStandingInstn;
    }

    /**
     * Setter for property chkStandingInstn.
     *
     * @param chkStandingInstn New value of property chkStandingInstn.
     */
    public void setChkStandingInstn(boolean chkStandingInstn) {
        this.chkStandingInstn = chkStandingInstn;
    }

    /**
     * Getter for property chkThalayal.
     *
     * @return Value of property chkThalayal.
     */
    public boolean getChkThalayal() {
        return chkThalayal;
    }

    /**
     * Setter for property chkThalayal.
     *
     * @param chkThalayal New value of property chkThalayal.
     */
    public void setChkThalayal(boolean chkThalayal) {
        this.chkThalayal = chkThalayal;
    }

    /**
     * Getter for property chkCoChittal.
     *
     * @return Value of property chkCoChittal.
     */
    public boolean getChkCoChittal() {
        return chkCoChittal;
    }

    /**
     * Setter for property chkCoChittal.
     *
     * @param chkCoChittal New value of property chkCoChittal.
     */
    public void setChkCoChittal(boolean chkCoChittal) {
        this.chkCoChittal = chkCoChittal;
    }

    /**
     * Getter for property chkMunnal.
     *
     * @return Value of property chkMunnal.
     */
    public boolean getChkMunnal() {
        return chkMunnal;
    }

    /**
     * Setter for property chkMunnal.
     *
     * @param chkMunnal New value of property chkMunnal.
     */
    public void setChkMunnal(boolean chkMunnal) {
        this.chkMunnal = chkMunnal;
    }

    /**
     * Getter for property oldTransDetMap.
     *
     * @return Value of property oldTransDetMap.
     */
    public java.util.HashMap getOldTransDetMap() {
        return oldTransDetMap;
    }

    /**
     * Setter for property oldTransDetMap.
     *
     * @param oldTransDetMap New value of property oldTransDetMap.
     */
    public void setOldTransDetMap(java.util.HashMap oldTransDetMap) {
        this.oldTransDetMap = oldTransDetMap;
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
     * Getter for property authorizeMap.
     *
     * @return Value of property authorizeMap.
     */
    public java.util.HashMap getAuthorizeMap() {
        return authorizeMap;
    }

    /**
     * Setter for property authorizeMap.
     *
     * @param authorizeMap New value of property authorizeMap.
     */
    public void setAuthorizeMap(java.util.HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }

    public Integer getTxtDivisionNo() {
        return txtDivisionNo;
    }

    public void setTxtDivisionNo(Integer txtDivisionNo) {
        this.txtDivisionNo = txtDivisionNo;
    }

    /**
     * Getter for property txtChittalNo.
     *
     * @return Value of property txtChittalNo.
     */
    public java.lang.String getTxtChittalNo() {
        return txtChittalNo;
    }

    /**
     * Setter for property txtChittalNo.
     *
     * @param txtChittalNo New value of property txtChittalNo.
     */
    public void setTxtChittalNo(java.lang.String txtChittalNo) {
        this.txtChittalNo = txtChittalNo;
    }

    public Integer getTxtApplnNo() {
        return txtApplnNo;
    }

    public void setTxtApplnNo(Integer txtApplnNo) {
        this.txtApplnNo = txtApplnNo;
    }

    /**
     * Getter for property txtpin.
     *
     * @return Value of property txtpin.
     */
    public java.lang.String getTxtpin() {
        return txtpin;
    }

    /**
     * Setter for property txtpin.
     *
     * @param txtpin New value of property txtpin.
     */
    public void setTxtpin(java.lang.String txtpin) {
        this.txtpin = txtpin;
    }

    /**
     * Getter for property nextActNo.
     *
     * @return Value of property nextActNo.
     */
    public java.lang.String getNextActNo() {
        return nextActNo;
    }

    /**
     * Setter for property nextActNo.
     *
     * @param nextActNo New value of property nextActNo.
     */
    public void setNextActNo(java.lang.String nextActNo) {
        this.nextActNo = nextActNo;
    }

    /**
     * Getter for property productMap.
     *
     * @return Value of property productMap.
     */
    public java.util.HashMap getProductMap() {
        return productMap;
    }

    /**
     * Setter for property productMap.
     *
     * @param productMap New value of property productMap.
     */
    public void setProductMap(java.util.HashMap productMap) {
        this.productMap = productMap;
    }

    /**
     * Getter for property chkNominee.
     *
     * @return Value of property chkNominee.
     */
    public boolean getChkNominee() {
        return chkNominee;
    }

    /**
     * Setter for property chkNominee.
     *
     * @param chkNominee New value of property chkNominee.
     */
    public void setChkNominee(boolean chkNominee) {
        this.chkNominee = chkNominee;
    }

    /**
     * Getter for property nomineeList.
     *
     * @return Value of property nomineeList.
     */
    public java.util.ArrayList getNomineeList() {
        return nomineeList;
    }

    /**
     * Setter for property nomineeList.
     *
     * @param nomineeList New value of property nomineeList.
     */
    public void setNomineeList(java.util.ArrayList nomineeList) {
        this.nomineeList = nomineeList;
    }

    /**
     * Getter for property masterMap.
     *
     * @return Value of property masterMap.
     */
    public java.util.HashMap getMasterMap() {
        return masterMap;
    }

    /**
     * Setter for property masterMap.
     *
     * @param masterMap New value of property masterMap.
     */
    public void setMasterMap(java.util.HashMap masterMap) {
        this.masterMap = masterMap;
    }

    /**
     * Getter for property cbmProdType.
     *
     * @return Value of property cbmProdType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }

    /**
     * Setter for property cbmProdType.
     *
     * @param cbmProdType New value of property cbmProdType.
     */
    public void setCbmProdType(com.see.truetransact.clientutil.ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
    }

    /**
     * Getter for property cboProdType.
     *
     * @return Value of property cboProdType.
     */
    public java.lang.String getCboProdType() {
        return cboProdType;
    }

    /**
     * Setter for property cboProdType.
     *
     * @param cboProdType New value of property cboProdType.
     */
    public void setCboProdType(java.lang.String cboProdType) {
        this.cboProdType = cboProdType;
    }

    /**
     * Getter for property cbmProdId.
     *
     * @return Value of property cbmProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }

    /**
     * Setter for property cbmProdId.
     *
     * @param cbmProdId New value of property cbmProdId.
     */
    public void setCbmProdId(com.see.truetransact.clientutil.ComboBoxModel cbmProdId) {
        this.cbmProdId = cbmProdId;
    }

    /**
     * Getter for property cboProdId.
     *
     * @return Value of property cboProdId.
     */
    public java.lang.String getCboProdId() {
        return cboProdId;
    }

    /**
     * Setter for property cboProdId.
     *
     * @param cboProdId New value of property cboProdId.
     */
    public void setCboProdId(java.lang.String cboProdId) {
        this.cboProdId = cboProdId;
    }

    /**
     * Getter for property txtCustomerIdCr.
     *
     * @return Value of property txtCustomerIdCr.
     */
    public java.lang.String getTxtCustomerIdCr() {
        return txtCustomerIdCr;
    }

    /**
     * Setter for property txtCustomerIdCr.
     *
     * @param txtCustomerIdCr New value of property txtCustomerIdCr.
     */
    public void setTxtCustomerIdCr(java.lang.String txtCustomerIdCr) {
        this.txtCustomerIdCr = txtCustomerIdCr;
    }

    public Integer getTxtSubNo() {
        return txtSubNo;
    }

    public void setTxtSubNo(Integer txtSubNo) {
        this.txtSubNo = txtSubNo;
    }

    /**
     * Getter for property multipleMember.
     *
     * @return Value of property multipleMember.
     */
    public java.lang.String getMultipleMember() {
        return multipleMember;
    }

    /**
     * Setter for property multipleMember.
     *
     * @param multipleMember New value of property multipleMember.
     */
    public void setMultipleMember(java.lang.String multipleMember) {
        this.multipleMember = multipleMember;
    }

    /**
     * Getter for property rdoSalaryRecovery.
     *
     * @return Value of property rdoSalaryRecovery.
     */
    public java.lang.String getRdoSalaryRecovery() {
        return rdoSalaryRecovery;
    }

    /**
     * Setter for property rdoSalaryRecovery.
     *
     * @param rdoSalaryRecovery New value of property rdoSalaryRecovery.
     */
    public void setRdoSalaryRecovery(java.lang.String rdoSalaryRecovery) {
        this.rdoSalaryRecovery = rdoSalaryRecovery;
    }

    /**
     * Getter for property updateChitNo.
     *
     * @return Value of property updateChitNo.
     */
    public java.lang.String getUpdateChitNo() {
        return updateChitNo;
    }

    /**
     * Setter for property updateChitNo.
     *
     * @param updateChitNo New value of property updateChitNo.
     */
    public void setUpdateChitNo(java.lang.String updateChitNo) {
        this.updateChitNo = updateChitNo;
    }

    /**
     * Getter for property isTran.
     *
     * @return Value of property isTran.
     */
    public String getIsTran() {
        return isTran;
    }

    /**
     * Setter for property isTran.
     *
     * @param isTran New value of property isTran.
     */
    public void setIsTran(String isTran) {
        this.isTran = isTran;
    }
}