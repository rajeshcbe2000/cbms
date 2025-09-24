/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigOB.java
 *
 * Created on Thu Jan 20 15:43:27 IST 2005
 */

package com.see.truetransact.ui.servicetax.servicetaxSettings;

import org.apache.log4j.Logger;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.servicetax.servicetaxsettings.ServiceTaxSettingsTO;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.transaction.token.tokenconfig.TokenConfigTO;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.*;

/**
 *
 * @author Ashok Vijayakumar
 */

public class ServiceTaxSettingsOB extends CObservable{
    
    /** Variables Declaration - Corresponding each Variable is in TokenConfigUI*/
    private String txtServiceTaxId = "";
    private  Date txtfromDate = new Date();
    private String txtTaxRate = "";
    private String txtEduCess = "";
    private String txthigherCess = "";
    private String txtSwachhCess="";
    private String txtKrishiKayanCess="";
    /** Other Varibales Declartion */
    private ProxyFactory proxy;
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private static ServiceTaxSettingsOB objTokenConfigOB;//Singleton Object Reference
    private final static Logger log = Logger.getLogger(ServiceTaxSettingsOB.class);//Creating Instace of Log
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private  ServiceTaxSettingsTO objServiceTaxSettingsTO;//Reference for the EntityBean TokenConfigTO
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String txttaxHeadId="";
    private String txtSwatchhHeadId="";
    private String txtKrishiKalyanHeadId="";
    private ComboBoxModel cbmCessRoundOff; // Added by nithya on 23-04-2020 for KD-1837
    private String cboCessRoundOff = "";
    private boolean isNewSettings = false;
    /** Consturctor Declaration  for  TokenConfigOB */
    private ServiceTaxSettingsOB() {
        try {
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            initUIComboBoxModel();
            fillDropdown();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            log.info("Creating ParameterOB...");
            objTokenConfigOB = new ServiceTaxSettingsOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "ServiceTaxSettingsJNDI");
        map.put(CommonConstants.HOME, "servicetax.servicetaxsettings.ServiceTaxSettingsHome");
        map.put(CommonConstants.REMOTE, "servicetax.servicetaxsettings.ServiceTaxSettings");
    }
    
    /** Creating instance for ComboboxModel cbmTokenType */
    private void initUIComboBoxModel(){
    }
    
    /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception{
        try{
            log.info("Inside FillDropDown");
            // Added by nithya on 23-04-2020 for KD-1837
            HashMap param = new HashMap();
            ArrayList lookupKey = new ArrayList();
            HashMap lookupValues;
            HashMap keyValue;
            ArrayList key;
            ArrayList value;
            param.put(CommonConstants.MAP_NAME, null);
            lookupKey.add("OPERATIVEACCTPRODUCT.INTROUNDOFF");
            param.put(CommonConstants.PARAMFORQUERY, lookupKey);
            lookupValues = ClientUtil.populateLookupData(param);
            keyValue = (HashMap) lookupValues.get("OPERATIVEACCTPRODUCT.INTROUNDOFF");
            key = (ArrayList) keyValue.get(CommonConstants.KEY);
            value = (ArrayList) keyValue.get(CommonConstants.VALUE);
            cbmCessRoundOff = new ComboBoxModel(key, value);      
        }catch(NullPointerException e){
            parseException.logException(e,true);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    
    /**
     * Returns an instance of TokenConfigOB.
     * @return  TokenConfigOB
     */
    
    public static ServiceTaxSettingsOB getInstance()throws Exception{
        return objTokenConfigOB;
    }

    public String getTxtEduCess() {
        return txtEduCess;
    }

    public void setTxtEduCess(String txtEduCess) {
        this.txtEduCess = txtEduCess;
    }

    public String getTxtServiceTaxId() {
        return txtServiceTaxId;
    }

    public void setTxtServiceTaxId(String txtServiceTaxId) {
        this.txtServiceTaxId = txtServiceTaxId;
    }

    public String getTxtTaxRate() {
        return txtTaxRate;
    }

    public void setTxtTaxRate(String txtTaxRate) {
        this.txtTaxRate = txtTaxRate;
    }

    public Date getTxtfromDate() {
        return txtfromDate;
    }

    public void setTxtfromDate(Date txtfromDate) {
        this.txtfromDate = txtfromDate;
    }

    public String getTxthigherCess() {
        return txthigherCess;
    }

    public void setTxthigherCess(String txthigherCess) {
        this.txthigherCess = txthigherCess;
    }

    public String getTxtKrishiKayanCess() {
        return txtKrishiKayanCess;
    }

    public void setTxtKrishiKayanCess(String txtKrishiKayanCess) {
        this.txtKrishiKayanCess = txtKrishiKayanCess;
    }
   
    public String getTxtSwachhCess() {
        return txtSwachhCess;
    }

    public void setTxtSwachhCess(String txtSwachhCess) {
        this.txtSwachhCess = txtSwachhCess;
    }
    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }
    
    public int getActionType(){
        return _actionType;
    }
    
    public void setActionType(int actionType) {
        _actionType = actionType;
        setStatus();
        setChanged();
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }
    
    /** Getter for property lblStatus.
     * @return Value of property lblStatus.
     *
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    
    /** Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    public int getResult(){
        return _result;
    }

    
    
    /** Creates an Instance of TokenConfigTO Bean and sets its variables with OBMethods */
    private ServiceTaxSettingsTO getServiceTaxSettingsTO(String command){
        ServiceTaxSettingsTO objServiceTaxSettingsTO = new ServiceTaxSettingsTO();
        objServiceTaxSettingsTO.setCommand(command);
        objServiceTaxSettingsTO.setServiceTaxId(getTxtServiceTaxId());
        objServiceTaxSettingsTO.setFromDate(getTxtfromDate());
        objServiceTaxSettingsTO.setTaxRate(CommonUtil.convertObjToDouble(getTxtTaxRate()));
        objServiceTaxSettingsTO.setEducationCess(CommonUtil.convertObjToDouble(getTxtEduCess()));
        objServiceTaxSettingsTO.setHigherEdu_Cess(CommonUtil.convertObjToDouble(getTxthigherCess()));
        objServiceTaxSettingsTO.setSwachhCess(CommonUtil.convertObjToDouble(getTxtSwachhCess()));
        objServiceTaxSettingsTO.setKrishiKalyanCess(CommonUtil.convertObjToDouble(getTxtKrishiKayanCess()));
        objServiceTaxSettingsTO.setBranchId(TrueTransactMain.BRANCH_ID);
        objServiceTaxSettingsTO.setTaxHeadId(getTxttaxHeadId());
        objServiceTaxSettingsTO.setSwatchhHeadId(getTxtSwatchhHeadId());
        objServiceTaxSettingsTO.setKrishiKalyanHeadid(getTxtKrishiKalyanHeadId());
        if(command.equals(CommonConstants.TOSTATUS_INSERT)){
            objServiceTaxSettingsTO.setCreatedBy(TrueTransactMain.USER_ID);
            objServiceTaxSettingsTO.setCreatedDt(ClientUtil.getCurrentDate());
        }
        objServiceTaxSettingsTO.setStatusBy(TrueTransactMain.USER_ID);
        objServiceTaxSettingsTO.setStatusDt(ClientUtil.getCurrentDate());
        objServiceTaxSettingsTO.setCessRoundOff(getCboCessRoundOff()); // added by nithya on 23-04-2020 for KD-1837
        return objServiceTaxSettingsTO;
    }
    
    private void setServiceTaxSettingsTO(ServiceTaxSettingsTO objServiceTaxSettingsTO) {
        setTxtServiceTaxId(objServiceTaxSettingsTO.getServiceTaxId());
        setTxtfromDate(objServiceTaxSettingsTO.getFromDate());
        setTxtTaxRate(CommonUtil.convertObjToStr(objServiceTaxSettingsTO.getTaxRate()));
        setTxtEduCess(CommonUtil.convertObjToStr(objServiceTaxSettingsTO.getEducationCess()));
        setTxthigherCess(CommonUtil.convertObjToStr(objServiceTaxSettingsTO.getHigherEdu_Cess()));
        setTxtSwachhCess(CommonUtil.convertObjToStr(objServiceTaxSettingsTO.getSwachhCess()));
        setTxtKrishiKayanCess(CommonUtil.convertObjToStr(objServiceTaxSettingsTO.getKrishiKalyanCess()));
        setTxttaxHeadId(CommonUtil.convertObjToStr(objServiceTaxSettingsTO.getTaxHeadId()));
        setTxtSwatchhHeadId(CommonUtil.convertObjToStr(objServiceTaxSettingsTO.getSwatchhHeadId()));
        setTxtKrishiKalyanHeadId(CommonUtil.convertObjToStr(objServiceTaxSettingsTO.getKrishiKalyanHeadid()));
        setStatusBy(objServiceTaxSettingsTO.getStatusBy());
        setAuthorizeStatus(objServiceTaxSettingsTO.getAuthorizeStatus());
        setCboCessRoundOff(CommonUtil.convertObjToStr(objServiceTaxSettingsTO.getCessRoundOff())); // Added by nithya on 23-04-2020 for KD-1837
        notifyObservers();
    }
    
    /** Resets all the UI Fields */
    public void resetForm() {
        setTxtServiceTaxId("");
        setTxtfromDate(null);
        setTxtTaxRate("");
        setTxtEduCess("");
        setTxthigherCess("");
        setTxttaxHeadId("");
        setTxtSwatchhHeadId("");
        setTxtKrishiKalyanHeadId("");
        setTxtSwachhCess("");
        setTxtKrishiKayanCess("");
        setIsNewSettings(false);
        notifyObservers();
    }
    
    /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            term.put("ServiceTaxSettingsTO", getServiceTaxSettingsTO(command));
            if(isNewSettings){
                term.put("NEW_TAX_SETTINGS","NEW_TAX_SETTINGS");
            }
            HashMap proxyReturnMap = proxy.execute(term, map);
            setProxyReturnMap(proxyReturnMap);
            setResult(getActionType());
        } catch (Exception e) {
            e.printStackTrace();
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /* Populates the TO object by executing a Query */
    public void getData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            ServiceTaxSettingsTO objServiceTaxSettingsTO =
            (ServiceTaxSettingsTO) ((List) mapData.get("ServiceTaxSettingsTO")).get(0);
            System.out.println("objServiceTaxSettingsTO================="+objServiceTaxSettingsTO);
            setServiceTaxSettingsTO(objServiceTaxSettingsTO);
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
    public void verifyAcctHead(com.see.truetransact.uicomponent.CTextField accountHead, String mapName) {
        try{
            final HashMap data = new HashMap();
            data.put("ACCT_HD",accountHead.getText());
            data.put(CommonConstants.MAP_NAME , mapName);
            HashMap proxyResultMap = proxy.execute(data,map);
        }catch(Exception e){
            System.out.println("Error in verifyAcctHead");
            accountHead.setText("");
            parseException.logException(e,true);
        }
    }
    /*Checks for the duplication of TokenType if so retuns a boolean type vairable as true */
    public String isTokenTypeExists(Date frmDt){
        String exists = "";
        HashMap resultMap = null;
        HashMap where = new HashMap();
        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        where.put("WEFDATE",frmDt);
         try {
        ArrayList resultList = (ArrayList) ClientUtil.executeQuery("getValidateWEFDate", where);
        System.out.println("resultList--------------- ::"+resultList);
        where = null;
        if (resultList.size() > 0) {
            for (int i = 0; i < resultList.size(); i++) {
                resultMap = (HashMap) resultList.get(i);
                if (resultMap != null && resultMap.containsKey("SERVICETAX_GEN_ID")) {
                    String val = CommonUtil.convertObjToStr(resultMap.get("AUTHORIZED_STATUS"));
                    if (val != null && val.length() > 0) {
                        exists = val;
                    }else {
                       exists= CommonUtil.convertObjToStr(resultMap.get("STATUS"));
                    }
                        break;
                    
                }
            }
        }
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
        return exists;
    }
    
    /*Checks for the duplication of SeriesNo if so retuns a boolean type vairable as true */
    public boolean isSeriesNoExists(String tokenType,String seriesNo){
        boolean exists = false;
        HashMap resultMap = null;
        HashMap where = new HashMap();
        where.put("TOKEN_TYPE", tokenType);
        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        ArrayList resultList =(ArrayList) ClientUtil.executeQuery("getSelectTokenSeries", where);
        where = null;
        if(resultList.size() > 0){
            for(int i=0; i<resultList.size(); i++){
                resultMap= (HashMap)resultList.get(i);
                if(resultMap.get("SERIES_NO").equals(seriesNo)){
                    exists = true;
                    break;
                }
            }
        }
        return exists;
    }

    public String getTxttaxHeadId() {
        return txttaxHeadId;
    }

    public void setTxttaxHeadId(String txttaxHeadId) {
        this.txttaxHeadId = txttaxHeadId;
    }

    public String getTxtKrishiKalyanHeadId() {
        return txtKrishiKalyanHeadId;
    }

    public void setTxtKrishiKalyanHeadId(String txtKrishiKalyanHeadId) {
        this.txtKrishiKalyanHeadId = txtKrishiKalyanHeadId;
    }

    public String getTxtSwatchhHeadId() {
        return txtSwatchhHeadId;
    }

    public void setTxtSwatchhHeadId(String txtSwatchhHeadId) {
        this.txtSwatchhHeadId = txtSwatchhHeadId;
    }

    public ComboBoxModel getCbmCessRoundOff() {
        return cbmCessRoundOff;
    }

    public void setCbmCessRoundOff(ComboBoxModel cbmCessRoundOff) {
        this.cbmCessRoundOff = cbmCessRoundOff;
    }

    public String getCboCessRoundOff() {
        return cboCessRoundOff;
    }

    public void setCboCessRoundOff(String cboCessRoundOff) {
        this.cboCessRoundOff = cboCessRoundOff;
    }

    public boolean isIsNewSettings() {
        return isNewSettings;
    }

    public void setIsNewSettings(boolean isNewSettings) {
        this.isNewSettings = isNewSettings;
    }
    
}