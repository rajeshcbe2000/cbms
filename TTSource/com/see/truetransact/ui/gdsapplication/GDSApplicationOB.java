/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * GDSApplicationOB.java
 *
 * Created on January 7, 2004, 5:14 PM
 */
package com.see.truetransact.ui.gdsapplication;

/**
 *
 * @author Nithya
 *
 */
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.ui.mdsapplication.*;
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
import com.see.truetransact.transferobject.gdsapplication.GDSApplicationTO;
import com.see.truetransact.transferobject.mdsapplication.MDSApplicationTO;
import com.see.truetransact.ui.common.nominee.NomineeOB;

import com.see.truetransact.ui.common.transaction.TransactionOB;
import java.util.*;

public class GDSApplicationOB extends CObservable {

    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    //    private static RemittanceProductRB objRemittanceProductRB = new RemittanceProductRB();
    java.util.ResourceBundle objRemittanceProductRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.mdsapplication.MDSApplicationRB", ProxyParameters.LANGUAGE);
    private static SqlMap sqlMap = null;
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
  //  private MDSApplicationTO mdsApplicationTO = null;
    private GDSApplicationTO gdsApplicationTO = null;
    private String txtGroupName = "";
    private String txtGDSNo = "";
    private String txtDivisionNo = "";
    private String txtChittalNo = ""; 
    private String installAmount ="";
    private String schemeName="";
    private String txtSubNo = "";
    private String tdtChitStartDt = "";
    private String tdtChitEndDt = "";
    private String txtInstAmt = "";
    private String txtApplnNo = "";
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
    private String nextGDSNo="";
    private String gDSNo="";
    private String prodId="";
    private String schemeStartDt=null;
    private String schemeEndDt=null;
    private String noOfDivisions="";
    private String branchId="";
    private String totalNoOfMembers="";
    private String schemeCount="";


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
    private static GDSApplicationOB GDSApplicationOB;

    static {
        try {
            currDt = ClientUtil.getCurrentDate();
            GDSApplicationOB = new GDSApplicationOB();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /* Sets the HashMap required to set JNDI,Home and Remote*/
    public GDSApplicationOB() throws Exception {
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "GDSApplicationJNDI");
        operationMap.put(CommonConstants.HOME, "gdsapplication.GDSApplicationHome");
        operationMap.put(CommonConstants.REMOTE, "gdsapplication.GDSApplication");
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        notifyObservers();
        fillDropdown();
    }

    public static GDSApplicationOB getInstance() {
        return GDSApplicationOB;
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
        gdsApplicationTO = setMDSApplicationTO();
        System.out.println("getTransId()getTransId()" + getTransId());
        gdsApplicationTO.setCommand(CommonUtil.convertObjToStr(data.get("COMMAND")));
        data.put("gdsApplicationTO", gdsApplicationTO);
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

        if (gdsApplicationTO.getNominee().equals("Y")) {
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
        data.put("MEMBER_GDSNO", getTxtGDSNo());
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
        if (getChkThalayal() == true) {
            data.put("THALAYAL_CHITTAL","Y");
        }
        System.out.println("data ###### : " + data);
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        System.out.println("###### : " + proxyResultMap);
        setProxyReturnMap(proxyResultMap);
        if (getActionType() == ClientConstants.ACTIONTYPE_NEW && proxyResultMap != null && proxyResultMap.containsKey("CHITTAL_NO")) {
            ClientUtil.showMessageWindow("Chittal No" + ": " + CommonUtil.convertObjToStr(gdsApplicationTO.getGds_No()));
        }
        if(getActionType()==ClientConstants.ACTIONTYPE_EDIT && proxyResultMap!=null && proxyResultMap.containsKey("CHITTAL_NO")){
            ClientUtil.showMessageWindow("Updated details with chittal No : "+CommonUtil.convertObjToStr(gdsApplicationTO.getGds_No()));
        }
        setResult(getActionType());
    }

    public void schemeDetailsList() {
    }
    
    public SMSSubscriptionTO setSMSSubcription() {
        if (isChkMobileBankingAD()) {
            objSMSSubscriptionTO = new SMSSubscriptionTO();
            objSMSSubscriptionTO.setProdType("MDS");
            objSMSSubscriptionTO.setProdId(getTxtGroupName());
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

    private GDSApplicationTO setMDSApplicationTO() {
        try {
            
            gdsApplicationTO = new GDSApplicationTO();
            gdsApplicationTO.setGroupName(getTxtGroupName());
            gdsApplicationTO.setGds_No(CommonUtil.convertObjToStr(getTxtGDSNo()));
            gdsApplicationTO.setGroupNo(CommonUtil.convertObjToStr(getgDSNo()));
            gdsApplicationTO.setDivisionNo(CommonUtil.convertObjToInt(getTxtDivisionNo()));
            gdsApplicationTO.setChittalNo(CommonUtil.convertObjToStr(getTxtChittalNo()));            
            gdsApplicationTO.setSubNo(CommonUtil.convertObjToInt(getTxtSubNo()));
            //gdsApplicationTO.setChitNo(CommonUtil.convertObjToDouble(getTxtSubNo()));
            gdsApplicationTO.setChitStartDt(DateUtil.getDateMMDDYYYY(getTdtChitStartDt()));
            gdsApplicationTO.setChitEndDt(DateUtil.getDateMMDDYYYY(getTdtChitEndDt()));
            gdsApplicationTO.setInstallmentAmount(CommonUtil.convertObjToDouble(getTxtInstAmt()));
            gdsApplicationTO.setApplnNo(CommonUtil.convertObjToInt(getTxtApplnNo()));
            gdsApplicationTO.setApplnDate(DateUtil.getDateMMDDYYYY(getTdtApplnDate()));
            gdsApplicationTO.setNextGDSNo(CommonUtil.convertObjToStr(getNextGDSNo()));
            gdsApplicationTO.setProdId(CommonUtil.convertObjToStr(getProdId()));
            gdsApplicationTO.setSchemeStartDt(DateUtil.getDateMMDDYYYY(getSchemeStartDt()));
            gdsApplicationTO.setSchemeEndDt(DateUtil.getDateMMDDYYYY(getSchemeEndDt()));
            gdsApplicationTO.setNoOfDivisions(CommonUtil.convertObjToInt(getNoOfDivisions()));
            gdsApplicationTO.setBranchid(CommonUtil.convertObjToStr(getBranchId()));
            gdsApplicationTO.setSchemeCount(CommonUtil.convertObjToInt(getSchemeCount()));
            gdsApplicationTO.setMultipleMember(CommonUtil.convertObjToStr(getMultipleMember()));
            gdsApplicationTO.setIsTran(getIsTran());
            if (getChkThalayal() == true) {
                gdsApplicationTO.setThalayal("Y");
            } else {
                gdsApplicationTO.setThalayal("N");
            }
            if (getChkMunnal() == true) {
                gdsApplicationTO.setMunnal("Y");
            } else {
                gdsApplicationTO.setMunnal("N");
            }
            if (getChkCoChittal() == true) {
                gdsApplicationTO.setCoChittal("Y");
            } else {
                gdsApplicationTO.setCoChittal("N");
            }
            gdsApplicationTO.setMembershipNo(CommonUtil.convertObjToStr(getTxtMembershipNo()));
            gdsApplicationTO.setMembershipType(CommonUtil.convertObjToStr(getTxtMembershipType()));
            gdsApplicationTO.setMembershipName(getTxtMembershipName());
            gdsApplicationTO.setHouseStNo(getTxtHouseStNo());
            gdsApplicationTO.setCudt_id(getCust_id());
            gdsApplicationTO.setArea(getTxtArea());
            gdsApplicationTO.setCity(getCboCity());
            gdsApplicationTO.setSalaryRecovery(getRdoSalaryRecovery());
            gdsApplicationTO.setState(getCboState());
            gdsApplicationTO.setPin(CommonUtil.convertObjToDouble(getTxtpin()));
            if (getChkStandingInstn() == true) {                
                gdsApplicationTO.setStandingInstn("Y");
                gdsApplicationTO.setProdType(CommonUtil.convertObjToStr(getCbmProdType().getKeyForSelected()));
                gdsApplicationTO.setProdId(CommonUtil.convertObjToStr(getCbmProdId().getKeyForSelected()));
                if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){// Added by nithya on 16-08-2019 for KD 585 - gds standing instruction issue-Kuttilanji
                    gdsApplicationTO.setProdType(getCboProdType());
                    gdsApplicationTO.setProdId(getCboProdId());
                }
                gdsApplicationTO.setDrAccNo(getTxtCustomerIdCr());
                System.out.println("in yyyy" + gdsApplicationTO);
            } else {
                gdsApplicationTO.setStandingInstn("N");
            }
            if (getChkNominee() == true) {
                gdsApplicationTO.setNominee("Y");
            } else {
                gdsApplicationTO.setNominee("N");
            }
            gdsApplicationTO.setRemarks(getTxtRemarks());
            gdsApplicationTO.setStatusDt(currDt);
            gdsApplicationTO.setBranchCode(TrueTransactMain.BRANCH_ID);
            System.out.println("getTransId()..." + getTransId());
            if (getTransId() != null && !getTransId().equals("")) {
                gdsApplicationTO.setTransId(getTransId());
            } else {
                gdsApplicationTO.setTransId(null);
                System.out.println("mmmm");
            }
            if (getIsTran().equals("N")) {
                gdsApplicationTO.setTransId(null);
                System.out.println("nnnyyy");
            }
            gdsApplicationTO.setCommencementStatus(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gdsApplicationTO;
    }

    public boolean populateData(HashMap whereMap, NomineeOB objNomineeOB) {
        boolean aliasBranchTableFlag = false;
        System.out.println("aliasBranchTableFlag" + aliasBranchTableFlag);
        System.out.println("  whereMap " + whereMap);
        System.out.println("  objNomineeOB " + objNomineeOB);
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
            gdsApplicationTO = new GDSApplicationTO();
            gdsApplicationTO = (GDSApplicationTO) ((List) mapData.get("gdsApplicationTO")).get(0);
            List groupNameList = (List) mapData.get("groupNameTO");
            String groupName = "";
            String groupNo = "";
            prodId=gdsApplicationTO.getProdId();
            if(groupNameList != null && groupNameList.size() > 0){
                HashMap groupNameMap = (HashMap)groupNameList.get(0);
                groupName = CommonUtil.convertObjToStr(groupNameMap.get("GROUP_NAME"));  
                groupNo=CommonUtil.convertObjToStr(groupNameMap.get("GROUP_NO"));
            }
            List list = (List) mapData.get("TransactionTO");
            System.out.println("list..." + list);
            System.out.println("groupNameTO  " + groupName);
            if (list != null & list.size() > 0) {
                transactionOB.setDetails(list);
            }
            System.out.println("list" + list);
            getMDSApplicationTO(gdsApplicationTO);
            List list2=(List)mapData.get("groupNameTO");
            setTxtGroupName(groupName);
            setgDSNo(groupNo);
            System.out.println("getTxtGroupName..." + getTxtGroupName());
            

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

    private void getMDSApplicationTO(GDSApplicationTO gdsApplicationTO) {
        System.out.println("mmm" + gdsApplicationTO);
        System.out.println("gdsApplicationTO" + gdsApplicationTO.getGds_No());
        try {
            setTxtGroupName(gdsApplicationTO.getGroupName());
            setTxtDivisionNo(CommonUtil.convertObjToStr(gdsApplicationTO.getDivisionNo()));
            setTxtGDSNo(CommonUtil.convertObjToStr(gdsApplicationTO.getGds_No()));
    //        setTxtChittalNo(CommonUtil.convertObjToStr(gdsApplicationTO.updateChittalNo()));
            setTxtSubNo(CommonUtil.convertObjToStr(gdsApplicationTO.getSubNo()));
            setTdtChitStartDt(DateUtil.getStringDate(gdsApplicationTO.getChitStartDt()));
            setTdtChitEndDt(DateUtil.getStringDate(gdsApplicationTO.getChitEndDt()));
            setTxtInstAmt(CommonUtil.convertObjToStr(gdsApplicationTO.getInstallmentAmount()));
            setTxtApplnNo(CommonUtil.convertObjToStr(gdsApplicationTO.getApplnNo()));
            setTdtApplnDate(DateUtil.getStringDate(gdsApplicationTO.getApplnDate()));
            setCust_id(CommonUtil.convertObjToStr(gdsApplicationTO.getCudt_id()));
            if (gdsApplicationTO.getThalayal() != null && !gdsApplicationTO.getThalayal().equals("")) {
                if (gdsApplicationTO.getThalayal().equals("Y")) {
                    setChkThalayal(true);
                } else {
                    setChkThalayal(false);
                }
            } else {
                setChkThalayal(false);
            }
            if (gdsApplicationTO.getMunnal() != null && !gdsApplicationTO.getMunnal().equals("")) {
                if (gdsApplicationTO.getMunnal().equals("Y")) {
                    setChkMunnal(true);
                } else {
                    setChkMunnal(false);
                }
            } else {
                setChkMunnal(false);
            }
            if (gdsApplicationTO.getCoChittal() != null && !gdsApplicationTO.getCoChittal().equals("")) {
                if (gdsApplicationTO.getCoChittal().equals("Y")) {
                    setChkCoChittal(true);
                } else {
                    setChkCoChittal(false);
                }
            } else {
                setChkCoChittal(false);
            }
            if (gdsApplicationTO.getStandingInstn() != null && !gdsApplicationTO.getStandingInstn().equals("")) {
                if (gdsApplicationTO.getStandingInstn().equals("Y")) {
                    setChkStandingInstn(true);
                    getCbmProdType().setKeyForSelected(gdsApplicationTO.getProdType());
                    //String prodType= getCbmProdType().getDataForKey(gdsApplicationTO.getProdType()).toString();
                    System.out.println("gdsApplicationTO.getProdId() " + gdsApplicationTO.getProdId());
                    String prodType = gdsApplicationTO.getProdType();
                    // System.out.println("dddd "+S);
                    setCbmProdId(prodType);
                    getCbmProdId().setKeyForSelected(gdsApplicationTO.getProdId());
                    if (!prodType.equals("GL")) {
                        getCbmProdId().setSelectedItem(getCbmProdId().getDataForKey(gdsApplicationTO.getProdId()));
                        setCboProdId(getCbmProdId().getDataForKey(gdsApplicationTO.getProdId()).toString());
                        System.out.println("sdffdsfnnnn" + getCbmProdId().getDataForKey(gdsApplicationTO.getProdId()));

                    }
                    setTxtCustomerIdCr(gdsApplicationTO.getDrAccNo());
                    System.out.println("gdsApplicationTO.getDrAccNo(),,,," + gdsApplicationTO.getDrAccNo());
                    System.out.println("vvvv" + getTxtCustomerIdCr());
                } else {
                    setChkStandingInstn(false);
                }
            } else {
                setChkStandingInstn(false);
            }
            setTxtMembershipNo(CommonUtil.convertObjToStr(gdsApplicationTO.getMembershipNo()));
            setTxtMembershipName(gdsApplicationTO.getMembershipName());
            setTxtMembershipType(gdsApplicationTO.getMembershipType());
            setTxtHouseStNo(gdsApplicationTO.getHouseStNo());
            setTxtArea(gdsApplicationTO.getArea());
            setCboCity(gdsApplicationTO.getCity());
            setCboState(gdsApplicationTO.getState());
            setRdoSalaryRecovery(CommonUtil.convertObjToStr(gdsApplicationTO.getSalaryRecovery()));
            setTxtpin(CommonUtil.convertObjToStr(gdsApplicationTO.getPin()));

            if (gdsApplicationTO.getNominee().equals("Y")) {
                setChkNominee(true);
            } else {
                setChkNominee(false);
            }
            setTxtRemarks(gdsApplicationTO.getRemarks());
            System.out.println("aaa");
            if (gdsApplicationTO.getTransId() != null && !gdsApplicationTO.getTransId().equals("")) {
                setTransId(gdsApplicationTO.getTransId());
            } else {
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
//    public String getGDSNo(Object hash){
//    gDSNo=CommonUtil.convertObjToStr(hash.get("BRANCH_ID"));
//        
//        
//        return gDSNo;
//    }

//    public boolean getChittalNo() {
//        boolean flag = false;
//        boolean sameNoFlag = false;
//        try {
//            HashMap where = new HashMap();
//            HashMap mapData = new HashMap();
//            String strPrefix = "";
//            String strNum = "";
//            int len = 13;
//            int nofOfMember = 0;
//            int suffix = 0;
//            int lastValue = 0;
//            int minDivisionNo = 0;
//            int maxNofCoMember = 0;
//            HashMap schemeMap = new HashMap();
//            System.out.println("###########FLAG" +getgDSNo());
//           // String schemeName=CommonUtil.convertObjToStr(getSchemeName());
//      //      HashMap schemeMap = new HashMap();
//        schemeMap.put("GROUP_NO", getgDSNo());
//        List list = ClientUtil.executeQuery("getGDSSelectGroupMDSDepositTO", schemeMap);
//         HashMap deletedStatusMap = new HashMap();
//        if(list!=null && list.size()>0){
//            for(int i=0;i<list.size();i++){
//                 deletedStatusMap = (HashMap)list.get(i);
//                String value =CommonUtil.convertObjToStr(deletedStatusMap.get("SCHEME_NAME"));
//                System.out.println("###########FLAG" + value);
//            schemeMap.put("SCHEME_NAME", value);
//            List lst = (List) ClientUtil.executeQuery("getGDSChittalNOUISide", schemeMap);
//            if (lst != null && lst.size() > 0) {
//                mapData = (HashMap) lst.get(0);
//            }
//            if (mapData.containsKey("PREFIX")) {
//                strPrefix = (String) mapData.get("PREFIX");
//            }
//            double totalSchemeAmt = 0.0;
//            double totalChittalAmt = 0.0;
//            List applicationList = (List) ClientUtil.executeQuery("getGDSSumOfInstAmtForChittal", schemeMap);
//            if (applicationList != null && applicationList.size() > 0) {
//                where = (HashMap) applicationList.get(0);
//                totalChittalAmt = CommonUtil.convertObjToDouble(where.get("TOTAL_INST_AMT")).doubleValue();
//            }
//
////            totalSchemeAmt = CommonUtil.convertObjToDouble(mapData.get("TOTAL_AMOUNT_SCHEME")).doubleValue();
////            
////            System.out.println("totalSchemeAmt:: "+totalSchemeAmt+" + totalChittalAmt:: "+totalChittalAmt);
////            suffix = CommonUtil.convertObjToInt(mapData.get("SUFFIX"));
////            int nofOfDivision = CommonUtil.convertObjToInt(mapData.get("NO_OF_DIVISIONS"));
////            nofOfMember = CommonUtil.convertObjToInt(mapData.get("NO_OF_MEMBER_PER_DIVISION"));
////            maxNofCoMember = CommonUtil.convertObjToInt(mapData.get("CO_NO_OF_INSTALLMENTS"));
////            lastValue = CommonUtil.convertObjToInt(mapData.get("LAST_VALUE"));
////            int numFrom = strPrefix.trim().length();
////            String newID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))));
////            String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))) + 1);
////            String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
////            System.out.println("genID"+genID); 
////           if (getMultipleMember().equals("N")) {
////                //                    if((nofOfDivision * nofOfMember)<lastValue){
////                if (totalSchemeAmt <= totalChittalAmt) {
////                    ClientUtil.showAlertWindow("Maximum No of Member already reached in the Scheme, can not add more Member... !!!");
////                    flag = true;
////                } else {
////                    setTxtChittalNo(String.valueOf(genID));
////                    setNextActNo(String.valueOf(newID));
////                    flag = false;
////                }
////            } else if (getMultipleMember().equals("Y")) {
////                //                    if(maxNofCoMember < lastValue){
////                if (totalSchemeAmt <= totalChittalAmt) {
////                    ClientUtil.showAlertWindow("Maximum No of Member already reached in the Scheme, can not add more Member... !!!");
////                    setTxtChittalNo("");
////                    flag = false;
////                } else {
////                    setTxtChittalNo(String.valueOf(genID));
////                    setNextActNo(String.valueOf(nxtID));
////                    flag = false;
////                }
////            }
////            if (getMultipleMember().equals("N")) {
////                lst = (List) ClientUtil.executeQuery("getGDSSchemeDetailsList", schemeMap);
////                if (lst != null && lst.size() > 0) {
////                    productMap = (HashMap) lst.get(0);
////                    int noOfDiv = CommonUtil.convertObjToInt(productMap.get("NO_OF_DIVISIONS"));
////                    int totMember = CommonUtil.convertObjToInt(productMap.get("TOTAL_NO_OF_MEMBERS"));
////                    int totChitNo = CommonUtil.convertObjToInt(productMap.get("TOTAL_CHIT_NO"));
////                    double installmentAmt = CommonUtil.convertObjToInt(productMap.get("INSTALLMENT_AMOUNT"));
////                    if (noOfDiv <= 1) {
////                        lastValue = (int) (totalChittalAmt / installmentAmt);
////                    }
//////                        System.out.println("################ lastValue : "+lastValue);
////                    if (suffix * nofOfMember >= lastValue) {
////                        suffix = suffix;
////                        setTxtDivisionNo(String.valueOf(suffix));
////                    } else {
////                        suffix = suffix;
////                        setTxtDivisionNo(String.valueOf(suffix + 1));
////                    }
////                }
////                 
////            }
////            }
//            HashMap detailsMap = new HashMap();
//           // detailsMap.put("SCHEME_NAME", getSchemeName());
//            lst = ClientUtil.executeQuery("getGDSSelectSchemeAcctHead",schemeMap) ;
//            if (lst != null && lst.size() > 0) {
//                productMap = (HashMap) lst.get(0);
//            }
//            System.out.println("genID"+getTxtChittalNo());
//            }
//        }   
//        } catch (Exception e) {
//        }
//        
//        return flag;
//    }
    public HashMap getChittalNo() {
        boolean flag = false;
        boolean sameNoFlag = false;
        HashMap returnMap=new HashMap();
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
            double trans_amt_total=0.0;
            double total_inst_amt=CommonUtil.convertObjToDouble(getTxtInstAmt());
            HashMap schemeMap = new HashMap();
            System.out.println("###########FLAGgetgDSNo" + getgDSNo() +"####Rish total inst amt0"+total_inst_amt);
            // String schemeName=CommonUtil.convertObjToStr(getSchemeName());
            //      HashMap schemeMap = new HashMap();
            schemeMap.put("GROUP_NO", getgDSNo());
            List list = ClientUtil.executeQuery("getGDSSelectGroupMDSDepositTO", schemeMap);
            HashMap deletedStatusMap = new HashMap();
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    deletedStatusMap = (HashMap) list.get(i);
                    String value = CommonUtil.convertObjToStr(deletedStatusMap.get("SCHEME_NAME"));
                    System.out.println("###########FLAGvalueSCHEME_NAME" + value);
                    schemeMap.put("SCHEME_NAME", value);                   
                    HashMap detailsMap = new HashMap();
                     List  lst1 = ClientUtil.executeQuery("getGDSSelectSchemeAcctHead", schemeMap);
                    if (lst1 != null && lst1.size() > 0) {
                        productMap = (HashMap) lst1.get(0);
                    }
                    
                    System.out.println("#Rish product map is"+productMap);
                       if (productMap.get("BONUS_FIRST_INSTALLMENT").equals("Y") && !productMap.get("BONUS_FIRST_INSTALLMENT").equals("")) {
                        double auction_maxamt = 0.0;
                        double installment_amt = 0.0;
                        double trans_amt = 0.0;
                        double commisionRateAmt = 0.0;
                        HashMap predefinedMap = new HashMap();
                        
                        if(productMap.get("PREDEFINITION_INSTALLMENT")!=null && !productMap.get("PREDEFINITION_INSTALLMENT").equals("")
                            && productMap.get("PREDEFINITION_INSTALLMENT").equals("Y")){
                            
                            List predefinedList = ClientUtil.executeQuery("getFirstPredefinedInstallment", productMap);
                            if (predefinedList != null && predefinedList.size() > 0) {
                                predefinedMap = (HashMap) predefinedList.get(0);
                                //observable.setPredefinedInstallBonus(CommonUtil.convertObjToDouble(predefinedMap.get("NEXT_BONUS_AMOUNT")));
                                trans_amt = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT"))-CommonUtil.convertObjToDouble(predefinedMap.get("NEXT_BONUS_AMOUNT"));
                               trans_amt_total+=trans_amt;
                                System.out.println("trans_amt$&&$&$&"+trans_amt);
                            }else{
                                commisionRateAmt = CommonUtil.convertObjToDouble(productMap.get("COMMISION_RATE_AMT")).doubleValue();
                                String commisionType = CommonUtil.convertObjToStr(productMap.get("COMMISION_RATE_TYPE"));
                                auction_maxamt = CommonUtil.convertObjToDouble(productMap.get("AUCTION_MAXAMT")).doubleValue();
                                installment_amt =CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT"));
                                if (commisionType != null && !commisionType.equals("") && commisionType.equals("Percent")) {
                                    trans_amt = installment_amt - (installment_amt * (auction_maxamt - commisionRateAmt) / 100);
                                      trans_amt_total+=trans_amt;
                                } else {
                                    trans_amt = installment_amt - (installment_amt * auction_maxamt / 100);
                                    trans_amt -= commisionRateAmt;
                                      trans_amt_total+=trans_amt;
                                }
                            }
                        }else{                            
                            commisionRateAmt = CommonUtil.convertObjToDouble(productMap.get("COMMISION_RATE_AMT")).doubleValue();
                            String commisionType = CommonUtil.convertObjToStr(productMap.get("COMMISION_RATE_TYPE"));
                            auction_maxamt = CommonUtil.convertObjToDouble(productMap.get("AUCTION_MAXAMT")).doubleValue();
                            installment_amt = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT"));
                            if (commisionType != null && !commisionType.equals("") && commisionType.equals("Percent")) {
                                trans_amt = installment_amt - (installment_amt * (auction_maxamt - commisionRateAmt) / 100);
                                  trans_amt_total+=trans_amt;
                            } else {
                                trans_amt = installment_amt - (installment_amt * auction_maxamt / 100);
                                trans_amt -= commisionRateAmt;
                                  trans_amt_total+=trans_amt;
                            }
                        }
                    } else {
                             trans_amt_total+=CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT"));
                    }
//                    
                    //
                    System.out.println("genID" + getTxtChittalNo());
                }
                 returnMap.put("TRANS_AMT",trans_amt_total);
            }
        } catch (Exception e) {
        }

        return returnMap;
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
        setNextGDSNo("");
        setProdId("");
        setSchemeStartDt("");
        setSchemeEndDt("");
        setBranchId("");
        setTotalNoOfMembers("");
        setSchemeCount("");
        setTxtGroupName("");
        setgDSNo("");
        setTxtDivisionNo("");
        setTxtChittalNo("");
        setInstallAmount("");
        setSchemeName("");
        setTxtSubNo("");
        setTdtChitStartDt("");
        setTdtChitEndDt("");
        setTxtInstAmt("");
        setTxtApplnNo("");
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
        setgDSNo("");
        setTxtGDSNo("");
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
    public java.lang.String getTxtGroupName() {
        return txtGroupName;
    }

    /**
     * Setter for property txtSchemeName.
     *
     * @param txtSchemeName New value of property txtSchemeName.
     */
    public void setTxtGroupName(java.lang.String txtGroupName) {
        this.txtGroupName = txtGroupName;
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

    /**
     * Getter for property txtInstAmt.
     *
     * @return Value of property txtInstAmt.
     */
    public java.lang.String getTxtInstAmt() {
        return txtInstAmt;
    }

    /**
     * Setter for property txtInstAmt.
     *
     * @param txtInstAmt New value of property txtInstAmt.
     */
    public void setTxtInstAmt(java.lang.String txtInstAmt) {
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

    /**
     * Getter for property txtDivisionNo.
     *
     * @return Value of property txtDivisionNo.
     */
    public java.lang.String getTxtDivisionNo() {
        return txtDivisionNo;
    }

    /**
     * Setter for property txtDivisionNo.
     *
     * @param txtDivisionNo New value of property txtDivisionNo.
     */
    public void setTxtDivisionNo(java.lang.String txtDivisionNo) {
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

    /**
     * Getter for property txtApplnNo.
     *
     * @return Value of property txtApplnNo.
     */
    public java.lang.String getTxtApplnNo() {
        return txtApplnNo;
    }

    /**
     * Setter for property txtApplnNo.
     *
     * @param txtApplnNo New value of property txtApplnNo.
     */
    public void setTxtApplnNo(java.lang.String txtApplnNo) {
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

    /**
     * Getter for property txtSubNo.
     *
     * @return Value of property txtSubNo.
     */
    public java.lang.String getTxtSubNo() {
        return txtSubNo;
    }

    /**
     * Setter for property txtSubNo.
     *
     * @param txtSubNo New value of property txtSubNo.
     */
    public void setTxtSubNo(java.lang.String txtSubNo) {
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

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public String getTxtGDSNo() {
        return txtGDSNo;
    }

    public void setTxtGDSNo(String txtGDSNo) {
        this.txtGDSNo = txtGDSNo;
    }

    public String getInstallAmount() {
        return installAmount;
    }

    public void setInstallAmount(String installAmount) {
        this.installAmount = installAmount;
    }

    public String getgDSNo() {
        return gDSNo;
    }

    public void setgDSNo(String gDSNo) {
        this.gDSNo = gDSNo;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getSchemeEndDt() {
        return schemeEndDt;
    }

    public void setSchemeEndDt(String schemeEndDt) {
        this.schemeEndDt = schemeEndDt;
    }

    public String getSchemeStartDt() {
        return schemeStartDt;
    }

    public void setSchemeStartDt(String schemeStartDt) {
        this.schemeStartDt = schemeStartDt;
    }

   

    public String getTotalNoOfMembers() {
        return totalNoOfMembers;
    }

    public void setTotalNoOfMembers(String totalNoOfMembers) {
        this.totalNoOfMembers = totalNoOfMembers;
    }

    public String getNextGDSNo() {
        return nextGDSNo;
    }

    public void setNextGDSNo(String nextGDSNo) {
        this.nextGDSNo = nextGDSNo;
    }

    public String getNoOfDivisions() {
        return noOfDivisions;
    }

    public void setNoOfDivisions(String noOfDivisions) {
        this.noOfDivisions = noOfDivisions;
    }

    public String getSchemeCount() {
        return schemeCount;
    }

    public void setSchemeCount(String schemeCount) {
        this.schemeCount = schemeCount;
    }

  

   
   
    
    
}