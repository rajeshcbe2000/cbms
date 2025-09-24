/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ConfigurationOB.java
 *
 * Created on Fri Feb 11 14:17:13 IST 2005
 */
package com.see.truetransact.ui.sysadmin.config;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import org.apache.log4j.Logger;
import com.see.truetransact.transferobject.sysadmin.config.ConfigPasswordTO;
import com.see.truetransact.transferobject.sysadmin.config.SIChargesHeadTO;
import com.see.truetransact.uicomponent.CObservable;

import com.see.truetransact.ui.TrueTransactMain;

/**
 *
 * @author 152715
 */
public class ConfigurationOB extends CObservable {

    private boolean chkPwdNeverExpire = false;
    private String txtMinLength = "";
    private String txtMaxLength = "";
    private boolean chkCantChangePwd = false;
    private String txtAttempts = "";
    private String txtUpperCase = "";
    private String txtSplChar = "";
    private String txtNo = "";
    private String txtDays = "";
    private String lblStatus = "";
    /**
     * Fields newly added for the configuration of Age and the ActHead *
     */
    private String txtMinorAge = "";
    private String txtRetireAge = "";
    private String txtCashActHead = "";
    private String txtIBRActHead = "";
    private String txtSIChargesHead = "";
    private String txtRemitChargesHead = "";
    private String txtAcceptChargesHead = "";
    private String txtExecChargesHead = "";
    private String txtFailChargesHead = "";
    private String txtServiceTaxHead = "";
    private String txtServiceTax = "";
//

    private String txtAppSuspenseAcHd = "";
    private String txtSeniorCitizenAge = "";
    private String tdtLastFinancialYearEnd = "";
    private String tdtYearEndProcessDate = "";
    private String txtSalarySuspense = "";
    private String btnSuspenseAcHd = "";
    private String btnSalarySuspense = "";
    private String btnServiceTaxHead = "";
    private String btnRTGS_GL = "";
    private String txtRTGS_GL = "";
    private String tdtFromDate = "";
    private String tdtToDate = "";

    private String txtAmcAlertTime = "";
    private String txtGahanPeriod = "";
    private String panAuthorizationByStaff = "";
    private String panDenomination = "";
    private String panMultiShare = "";
    private String panServiceTax = "";
    private String panTokenNoAllow = "";
    private String panCashierAuthorization = "";
    private String panExcludePenalIntFromReports = "";
    private String txtPendingTxnAllowedDays = "";
    private String txtPanDetails = "";

    private String tdtEffectiveFrom = "";

    private String txtServicePeriod = "";

    private boolean rdoCashierAuthorizationNo = false;
    private boolean rdoAllowAuthorizationNo = false;
    private boolean rdoCashierAuthorizationYes = false;
    private boolean rdoExcludePenalIntFromReportsYes = false;
    private boolean rdoExcludePenalIntFromReportsNo = false;
    private boolean rdoAllowAuthorizationYes = false;
    private boolean rdoTokenNoAllowYes = false;
    private boolean rdoTokenNoAllowNo = false;
    private boolean rdoDenominationYes = false;
    private boolean rdoDenominationNo = false;
    private boolean rdoServiceTaxYes = false;
    private boolean rdoServiceTaxNo = false;
    private boolean rdoMultiShareYes = false;
    private boolean rdoMultiShareNo = false;

//
    private String txtNominalMemFee = "";

    private String txtNmfAcHead = "";

    private String tdtEffectiveDate = "";
    /**
     * ******************************************************************
     */
    private boolean chkAccLocked = false;
    private boolean chkFirstLogin = false;
    private String txtPwds = "";
    private boolean isAuth = false;

    // CHECKBOX VALUES
    private final String TRUE = "T";
    private final String FALSE = "F";

    HashMap authorizeMap;
    private int actionType;
    private int viewType = 0;
    ;
    
    
    private static ConfigurationOB objConfigurationOB; // singleton object
    private HashMap operationMap;
    private ProxyFactory proxy;
    private ConfigPasswordTO objConfigPasswordTO;
    private SIChargesHeadTO objSIChargesTO;
    private List dataList = null;
    private List siList = null;
    private List serviceList = null;

    /* The following set of objects for ComboBox (added by Rajesh) */
    private ComboBoxModel cbmBranches;
    private String cboBranches;
    private HashMap lookUpHash;
    private String GET_BRANCHES = "getOwnBranches";
    private boolean rdoDayEndType_Branch;
    private boolean rdoDayEndType_Bank;
    private boolean rdoIB_OnHoliday_Yes;
    private boolean rdoIB_OnHoliday_No;
    //private String PanAmount = "";
    HashMap keyValue;
    ArrayList key, value;
    private String logOutTime = "";

    public String getLogOutTime() {
        return logOutTime;
    }

    public void setLogOutTime(String logOutTime) {
        this.logOutTime = logOutTime;
    }

    private final static Logger log = Logger.getLogger(ConfigurationOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();

    static {
        try {
            log.info("Creating ConfigurationOB...");
            objConfigurationOB = new ConfigurationOB();
        } catch (Exception e) {
            //_log.error(e);
            parseException.logException(e, true);
        }
    }

    /**
     * Returns an instance of ConfigurationOB.
     *
     * @return ConfigurationOB
     */
    public static ConfigurationOB getInstance() {
        return objConfigurationOB;
    }

    /**
     * Creates a new instance of ConfigurationOB
     */
    private ConfigurationOB() throws Exception {
        proxy = ProxyFactory.createProxy();
        setOperationMap();
        fillDropdown();
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    /**
     * Sets the values to Combos
     */
    public void fillDropdown() throws Exception {
        lookUpHash = new HashMap();
        keyValue = new HashMap();
        HashMap paramMap = new HashMap();
        paramMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        lookUpHash.put(CommonConstants.MAP_NAME, GET_BRANCHES);
        lookUpHash.put(CommonConstants.PARAMFORQUERY, paramMap);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
        cbmBranches = new ComboBoxModel(key, value);

        makeNull();
    }

    private void makeNull() {
        key = null;
        value = null;
        lookUpHash = null;
        keyValue = null;

    }

    /* Splits the keyValue HashMap into key and value arraylists*/
    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception {
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "ConfigurationJNDI");
        operationMap.put(CommonConstants.HOME, "com.see.truetransact.serverside.sysadmin.config.ConfigurationHome");
        operationMap.put(CommonConstants.REMOTE, "com.see.truetransact.serverside.sysadmin.config.Configuration");
    }

    // Setter method for chkPwdNeverExpire
    void setChkPwdNeverExpire(boolean chkPwdNeverExpire) {
        this.chkPwdNeverExpire = chkPwdNeverExpire;
        setChanged();
    }

    // Getter method for chkPwdNeverExpire
    boolean getChkPwdNeverExpire() {
        return this.chkPwdNeverExpire;
    }

    // Setter method for txtMinLength
    void setTxtMinLength(String txtMinLength) {
        this.txtMinLength = txtMinLength;
        setChanged();
    }

    // Getter method for txtMinLength
    String getTxtMinLength() {
        return this.txtMinLength;
    }

    // Setter method for txtMaxLength
    void setTxtMaxLength(String txtMaxLength) {
        this.txtMaxLength = txtMaxLength;
        setChanged();
    }

    // Getter method for txtMaxLength
    String getTxtMaxLength() {
        return this.txtMaxLength;
    }

    // Setter method for chkCantChangePwd
    void setChkCantChangePwd(boolean chkCantChangePwd) {
        this.chkCantChangePwd = chkCantChangePwd;
        setChanged();
    }

    // Getter method for chkCantChangePwd
    boolean getChkCantChangePwd() {
        return this.chkCantChangePwd;
    }

    // Setter method for txtAttempts
    void setTxtAttempts(String txtAttempts) {
        this.txtAttempts = txtAttempts;
        setChanged();
    }

    // Getter method for txtAttempts
    String getTxtAttempts() {
        return this.txtAttempts;
    }

    // Setter method for txtUpperCase
    void setTxtUpperCase(String txtUpperCase) {
        this.txtUpperCase = txtUpperCase;
        setChanged();
    }

    // Getter method for txtUpperCase
    String getTxtUpperCase() {
        return this.txtUpperCase;
    }

    // Setter method for txtSplChar
    void setTxtSplChar(String txtSplChar) {
        this.txtSplChar = txtSplChar;
        setChanged();
    }

    // Getter method for txtSplChar
    String getTxtSplChar() {
        return this.txtSplChar;
    }

    // Setter method for txtNo
    void setTxtNo(String txtNo) {
        this.txtNo = txtNo;
        setChanged();
    }

    // Getter method for txtNo
    String getTxtNo() {
        return this.txtNo;
    }

    // Setter method for txtDays
    void setTxtDays(String txtDays) {
        this.txtDays = txtDays;
        setChanged();
    }

    // Getter method for txtDays
    String getTxtDays() {
        return this.txtDays;
    }

    // Setter method for chkAccLocked
    void setChkAccLocked(boolean chkAccLocked) {
        this.chkAccLocked = chkAccLocked;
        setChanged();
    }

    // Getter method for chkAccLocked
    boolean getChkAccLocked() {
        return this.chkAccLocked;
    }

    // Setter method for chkFirstLogin
    void setChkFirstLogin(boolean chkFirstLogin) {
        this.chkFirstLogin = chkFirstLogin;
        setChanged();
    }

    // Getter method for chkFirstLogin
    boolean getChkFirstLogin() {
        return this.chkFirstLogin;
    }

    // Setter method for txtPwds
    void setTxtPwds(String txtPwds) {
        this.txtPwds = txtPwds;
        setChanged();
    }

    // Getter method for txtPwds
    String getTxtPwds() {
        return this.txtPwds;
    }

    /**
     * Getter for property txtMinorAge.
     *
     * @return Value of property txtMinorAge.
     */
    public java.lang.String getTxtMinorAge() {
        return txtMinorAge;
    }

    /**
     * Setter for property txtMinorAge.
     *
     * @param txtMinorAge New value of property txtMinorAge.
     */
    public void setTxtMinorAge(java.lang.String txtMinorAge) {
        this.txtMinorAge = txtMinorAge;
    }

    /**
     * Getter for property txtRetireAge.
     *
     * @return Value of property txtRetireAge.
     */
    public java.lang.String getTxtRetireAge() {
        return txtRetireAge;
    }

    /**
     * Setter for property txtRetireAge.
     *
     * @param txtRetireAge New value of property txtRetireAge.
     */
    public void setTxtRetireAge(java.lang.String txtRetireAge) {
        this.txtRetireAge = txtRetireAge;
    }

    /**
     * Getter for property txtCashActHead.
     *
     * @return Value of property txtCashActHead.
     */
    public java.lang.String getTxtCashActHead() {
        return txtCashActHead;
    }

    /**
     * Setter for property txtCashActHead.
     *
     * @param txtCashActHead New value of property txtCashActHead.
     */
    public void setTxtCashActHead(java.lang.String txtCashActHead) {
        this.txtCashActHead = txtCashActHead;
    }

    /**
     * Getter for property txtIBRActHead.
     *
     * @return Value of property txtIBRActHead.
     */
    public java.lang.String getTxtIBRActHead() {
        return txtIBRActHead;
    }

    /**
     * Setter for property txtIBRActHead.
     *
     * @param txtIBRActHead New value of property txtIBRActHead.
     */
    public void setTxtIBRActHead(java.lang.String txtIBRActHead) {
        this.txtIBRActHead = txtIBRActHead;
    }

    public String getTxtNmfAcHead() {
        return txtNmfAcHead;
    }

    public void setTxtNmfAcHead(String txtNmfAcHead) {
        this.txtNmfAcHead = txtNmfAcHead;
    }

    public String getTxtNominalMemFee() {
        return txtNominalMemFee;
    }

    public void setTxtNominalMemFee(String txtNominalMemFee) {
        this.txtNominalMemFee = txtNominalMemFee;
    }

    /**
     * reset all fields in the OB
     */
    public void resetForm() {
        setChkPwdNeverExpire(false);
        setTxtMinLength("");
        setTxtMaxLength("");
        setChkCantChangePwd(false);
        setTxtAttempts("");
        setTxtUpperCase("");
        setTxtSplChar("");
        setTxtNo("");
        setTxtDays("");
        setChkAccLocked(false);
        setChkFirstLogin(false);
        setTxtPwds("");
        setTxtMinorAge("");
        setTxtRetireAge("");
       
        setTxtCashActHead("");
        setTxtIBRActHead("");
        setTxtSIChargesHead("");
        setTxtRemitChargesHead("");
        setTxtAcceptChargesHead("");
        setTxtExecChargesHead("");
        setTxtFailChargesHead("");
        setTxtRTGS_GL("");
        setTxtAppSuspenseAcHd("");
        setTxtServiceTaxHead("");
        setTxtServiceTax("");
        setTdtEffectiveDate("");
        setTxtServicePeriod("");
        getCbmBranches().setKeyForSelected("");
        setRdoDayEndType_Bank(false);
        setRdoDayEndType_Branch(false);
        setRdoIB_OnHoliday_Yes(false);
        setRdoIB_OnHoliday_No(false);
        objConfigPasswordTO = null;
        objSIChargesTO = null;
        setLogOutTime("");
        setTxtNmfAcHead("");
        setTxtNominalMemFee("");
     
        setTdtEffectiveDate("");
        setTdtFromDate("");
        setTdtEffectiveFrom("");
        setTdtLastFinancialYearEnd("");
        setTdtToDate("");
        setTdtYearEndProcessDate("");
        setTxtAmcAlertTime("");
        setTxtServiceTaxHead("");
        setTxtSeniorCitizenAge("");
        setTxtSalarySuspense("");
        setTxtRTGS_GL("");
        setTxtPendingTxnAllowedDays("");
        setTxtPanDetails("");
        setTxtGahanPeriod("");
       
        setRdoCashierAuthorizationYes(false);
        
        setRdoAllowAuthorizationNo(false);
        setRdoCashierAuthorizationYes(false);
        setRdoExcludePenalIntFromReportsYes(false);
        setRdoExcludePenalIntFromReportsNo(false);
        setRdoAllowAuthorizationYes(false);
        setRdoAllowAuthorizationYes(false);
        setRdoAllowAuthorizationYes(false);
        setRdoDenominationYes(false);
        setRdoDenominationNo(false);
        setRdoServiceTaxYes(false);
        setRdoServiceTaxNo(false);
        setRdoMultiShareYes(false);
        setRdoMultiShareNo(false);
  
        ttNotifyObservers();
    }

    /**
     * To set the data in ConfigPasswordTO TO
     */
    public ConfigPasswordTO setConfigPasswordTO() {
        log.info("In ConfigPasswordTO...");
        if (objConfigPasswordTO == null) {
            objConfigPasswordTO = new ConfigPasswordTO();
        }

        try {

            if (getChkPwdNeverExpire() == true) {
                objConfigPasswordTO.setPasswordNeverExpire(CommonUtil.convertObjToStr(TRUE));
            } else if (getChkPwdNeverExpire() == false) {
                objConfigPasswordTO.setPasswordNeverExpire(CommonUtil.convertObjToStr(FALSE));
            }

            objConfigPasswordTO.setPasswordExpiry(CommonUtil.convertObjToDouble(getTxtDays()));
            objConfigPasswordTO.setMinLength(CommonUtil.convertObjToDouble(getTxtMinLength()));
            objConfigPasswordTO.setMaxLength(CommonUtil.convertObjToDouble(getTxtMaxLength()));
            objConfigPasswordTO.setSpecialChars(CommonUtil.convertObjToDouble(getTxtSplChar()));
            objConfigPasswordTO.setUppercaseChars(CommonUtil.convertObjToDouble(getTxtUpperCase()));
            objConfigPasswordTO.setNumberChars(CommonUtil.convertObjToDouble(getTxtNo()));
            objConfigPasswordTO.setShouldNotLastpwd(CommonUtil.convertObjToDouble(getTxtPwds()));
            objConfigPasswordTO.setNoOfAttempts(CommonUtil.convertObjToDouble(getTxtAttempts()));

            if (getChkFirstLogin() == true) {
                objConfigPasswordTO.setChangePwdFirst(CommonUtil.convertObjToStr(TRUE));
            } else if (getChkFirstLogin() == false) {
                objConfigPasswordTO.setChangePwdFirst(CommonUtil.convertObjToStr(FALSE));
            }

            if (getChkCantChangePwd() == true) {
                objConfigPasswordTO.setUserCannotChangepwd(CommonUtil.convertObjToStr(TRUE));
            } else if (getChkCantChangePwd() == false) {
                objConfigPasswordTO.setUserCannotChangepwd(CommonUtil.convertObjToStr(FALSE));
            }

            if (getChkAccLocked() == true) {
                objConfigPasswordTO.setUserAcctLocked(CommonUtil.convertObjToStr(TRUE));
            } else if (getChkAccLocked() == false) {
                objConfigPasswordTO.setUserAcctLocked(CommonUtil.convertObjToStr(FALSE));
            }
            objConfigPasswordTO.setPendingTxnAllowedDays(CommonUtil.convertObjToInt(getTxtPendingTxnAllowedDays()));
            /**
             * Setting the To Object related to Age Configuration *
             */
            objConfigPasswordTO.setMinorAge(CommonUtil.convertObjToDouble(getTxtMinorAge()));
            objConfigPasswordTO.setRetirementAge(CommonUtil.convertObjToDouble(getTxtRetireAge()));
            objConfigPasswordTO.setSeniorCitizenAge(CommonUtil.convertObjToInt(getTxtSeniorCitizenAge()));
            objConfigPasswordTO.setGahanPeriod(CommonUtil.convertObjToInt(getTxtGahanPeriod()));
            
            /**
             * Setting the To Object related to Service PeriodConfiguration *
             */
            objConfigPasswordTO.setEffectiveFrom((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtEffectiveDate()))));
            objConfigPasswordTO.setPayroll_mnt_cnt(CommonUtil.convertObjToStr(getTxtServicePeriod())); 
                    objConfigPasswordTO.setYearEndProcessDt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtYearEndProcessDate()))));
                    objConfigPasswordTO.setLast_financial_year_end((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtLastFinancialYearEnd()))));
                   
                    objConfigPasswordTO.setAmcFromDt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtFromDate()))));
                    objConfigPasswordTO.setAmcToDt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtToDate()))));
                    objConfigPasswordTO.setAmcAlertTime(CommonUtil.convertObjToInt(getTxtAmcAlertTime()));
            /**
             * Setting the TO Object related to the AccountHead Configuration *
             */
            objConfigPasswordTO.setCashAcHd(getTxtCashActHead());
            objConfigPasswordTO.setIbrAcHd(getTxtIBRActHead());
            objConfigPasswordTO.setSalary_suspense(getTxtSalarySuspense());
            objConfigPasswordTO.setApp_suspense_achd(getTxtAppSuspenseAcHd());
                    objConfigPasswordTO.setRtgs_gl(getTxtRTGS_GL());
              objConfigPasswordTO.setEffectiveFrom((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtEffectiveDate()))));
            objConfigPasswordTO.setServicePeriod(CommonUtil.convertObjToStr(getTxtServicePeriod())); 
                    objConfigPasswordTO.setYearEndProcessDt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtYearEndProcessDate()))));
                    objConfigPasswordTO.setLast_financial_year_end((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtLastFinancialYearEnd()))));
                   
                    objConfigPasswordTO.setAmcFromDt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtFromDate()))));
                    objConfigPasswordTO.setAmcToDt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtToDate()))));
                    objConfigPasswordTO.setAmcAlertTime(CommonUtil.convertObjToInt(getTxtAmcAlertTime()));
            
            
            
          if (isRdoCashierAuthorizationYes()== true) {
                objConfigPasswordTO.setCashierAuthAllowed("Y");
            } else {
                objConfigPasswordTO.setCashierAuthAllowed("N");
            }

              if (isRdoDenominationYes()== true) {
                objConfigPasswordTO.setDenomination_allowed("Y");
            } else {
                objConfigPasswordTO.setDenomination_allowed("N");
            }
              
               if (isRdoTokenNoAllowYes()== true) {
                objConfigPasswordTO.setTokenNoReq("Y");
            } else {
                objConfigPasswordTO.setTokenNoReq("N");
            }
              if (isRdoServiceTaxYes()== true) {
                objConfigPasswordTO.setServiceTaxReq("Y");
            } else {
                objConfigPasswordTO.setServiceTaxReq("N");
            }
               if (isRdoMultiShareYes()== true) {
                objConfigPasswordTO.setMultiShareAllowed("Y");
            } else {
                objConfigPasswordTO.setMultiShareAllowed("N");
            } 
                if (isRdoExcludePenalIntFromReportsYes()== true) {
                objConfigPasswordTO.setExclude_penal_int_from_reports("Y");
            } else {
                objConfigPasswordTO.setExclude_penal_int_from_reports("N");
            }
               
               
               
               
             
            /*Setting the TO Object related to the Head Office and Branches */
            objConfigPasswordTO.setCboBranches(CommonUtil.convertObjToStr(
                    cbmBranches.getKeyForSelected()));

            //__ To reset the Authorize Status
            objConfigPasswordTO.setAuthorizeStatus("");

            if (isRdoDayEndType_Branch()) {
                objConfigPasswordTO.setDayEndType("BRANCH_LEVEL");
            } else {
                objConfigPasswordTO.setDayEndType("BANK_LEVEL");
            }
            if (isRdoIB_OnHoliday_Yes()) {
                objConfigPasswordTO.setInterBranchOnHoliday("Y");
            } else {
                objConfigPasswordTO.setInterBranchOnHoliday("N");
            }
            objConfigPasswordTO.setPanAmount(CommonUtil.convertObjToDouble(txtPanDetails));
            objConfigPasswordTO.setLogOutTime(CommonUtil.convertObjToStr(logOutTime));
            objConfigPasswordTO.setNominalMemFee(CommonUtil.convertObjToDouble(getTxtNominalMemFee()));
            if (isRdoAllowAuthorizationYes()== true) {
                objConfigPasswordTO.setAllowAuth("Y");
            } else {
                objConfigPasswordTO.setAllowAuth("N");
            }

        } catch (Exception e) {
            log.info("Error in setConfigPasswordTO()");
            parseException.logException(e, true);
        }
        return objConfigPasswordTO;
    }

    public SIChargesHeadTO setSIChargesHeadTO() {
        log.info("In SIChargesHeadTO...");

        if (objSIChargesTO == null) {
            objSIChargesTO = new SIChargesHeadTO();
        }

        try {
            objSIChargesTO.setSiChargesHd(getTxtSIChargesHead());
            objSIChargesTO.setRemitChargesHd(getTxtRemitChargesHead());
            objSIChargesTO.setAcceptChargesHd(getTxtAcceptChargesHead());
            objSIChargesTO.setExecChargesHd(getTxtExecChargesHead());
            objSIChargesTO.setFailChargesHd(getTxtFailChargesHead());
            objSIChargesTO.setServiceTaxHd(getTxtServiceTaxHead());
            objSIChargesTO.setServiceTax(CommonUtil.convertObjToDouble(getTxtServiceTax()));
            objSIChargesTO.setNmfAcHd(getTxtNmfAcHead());
           
        } catch (Exception e) {
            log.info("Error in setConfigPasswordTO()");
            parseException.logException(e, true);
        }
        return objSIChargesTO;
    }

    /**
     * To set the data in ConfigPasswordOB
     */
    private void setConfigPasswordOB() throws Exception {
        log.info("In setConfigPasswordOB...");

        if (objConfigPasswordTO.getPasswordNeverExpire().equals(TRUE)) {
            setChkPwdNeverExpire(true);
        } else if (objConfigPasswordTO.getPasswordNeverExpire().equals(FALSE)) {
            setChkPwdNeverExpire(false);
        }

        if (objConfigPasswordTO.getChangePwdFirst().equals(TRUE)) {
            setChkFirstLogin(true);
        } else if (objConfigPasswordTO.getChangePwdFirst().equals(FALSE)) {
            setChkFirstLogin(false);
        }

        if (objConfigPasswordTO.getUserCannotChangepwd().equals(TRUE)) {
            setChkCantChangePwd(true);
        } else if (objConfigPasswordTO.getUserCannotChangepwd().equals(FALSE)) {
            setChkCantChangePwd(false);
        }

        if (objConfigPasswordTO.getUserAcctLocked().equals(TRUE)) {
            setChkAccLocked(true);
        } else if (objConfigPasswordTO.getUserAcctLocked().equals(FALSE)) {
            setChkAccLocked(false);
        }

        setTxtDays(CommonUtil.convertObjToStr(objConfigPasswordTO.getPasswordExpiry()));
        setTxtMinLength(CommonUtil.convertObjToStr(objConfigPasswordTO.getMinLength()));
        setTxtMaxLength(CommonUtil.convertObjToStr(objConfigPasswordTO.getMaxLength()));
        setTxtSplChar(CommonUtil.convertObjToStr(objConfigPasswordTO.getSpecialChars()));
        setTxtUpperCase(CommonUtil.convertObjToStr(objConfigPasswordTO.getUppercaseChars()));
        setTxtNo(CommonUtil.convertObjToStr(objConfigPasswordTO.getNumberChars()));
        setTxtPwds(CommonUtil.convertObjToStr(objConfigPasswordTO.getShouldNotLastpwd()));
        setTxtAttempts(CommonUtil.convertObjToStr(objConfigPasswordTO.getNoOfAttempts()));
        setTxtDays(CommonUtil.convertObjToStr(objConfigPasswordTO.getPasswordExpiry()));
        setTxtDays(CommonUtil.convertObjToStr(objConfigPasswordTO.getPasswordExpiry()));
        setTxtPanDetails(CommonUtil.convertObjToStr(objConfigPasswordTO.getPanAmount()));
        setLogOutTime(CommonUtil.convertObjToStr(objConfigPasswordTO.getLogOutTime()));
        setTxtNominalMemFee(CommonUtil.convertObjToStr(objConfigPasswordTO.getNominalMemFee()));
        setTxtServicePeriod(CommonUtil.convertObjToStr(objConfigPasswordTO.getPayroll_mnt_cnt()));
        setTxtGahanPeriod(CommonUtil.convertObjToStr(objConfigPasswordTO.getGahanPeriod()));
        setTxtPendingTxnAllowedDays(CommonUtil.convertObjToStr(objConfigPasswordTO.getPendingTxnAllowedDays()));
        setTdtEffectiveFrom(CommonUtil.convertObjToStr(objConfigPasswordTO.getEffectiveFrom()));
        setTxtAmcAlertTime(CommonUtil.convertObjToStr(objConfigPasswordTO.getAmcAlertTime()));
        setTdtToDate(CommonUtil.convertObjToStr(objConfigPasswordTO.getAmcToDt()));
        setTdtFromDate(CommonUtil.convertObjToStr(objConfigPasswordTO.getAmcFromDt()));
        
        
        setTdtLastFinancialYearEnd(CommonUtil.convertObjToStr(objConfigPasswordTO.getLast_financial_year_end()));
        setTdtYearEndProcessDate(CommonUtil.convertObjToStr(objConfigPasswordTO.getYearEndProcessDt()));
        if (objConfigPasswordTO.getAllowAuth() != null && objConfigPasswordTO.getAllowAuth().equals("Y")) {
            setRdoAllowAuthorizationYes(true);
            setRdoAllowAuthorizationNo(false);
        } else {
            setRdoAllowAuthorizationYes(false);
            setRdoAllowAuthorizationNo(true);
        }
        
        //s
        
          if (objConfigPasswordTO.getDenomination_allowed() != null && objConfigPasswordTO.getDenomination_allowed().equals("Y")) {
            setRdoDenominationYes(true);
            setRdoDenominationNo(false);
        } else {
            setRdoDenominationYes(false);
            setRdoDenominationNo(true);
        }
          
          if (objConfigPasswordTO.getCashierAuthAllowed() != null && objConfigPasswordTO.getCashierAuthAllowed().equals("Y")) {
            setRdoCashierAuthorizationYes(true);
            setRdoCashierAuthorizationNo(false);
        } else {
            setRdoCashierAuthorizationYes(false);
            setRdoCashierAuthorizationNo(true);
        }
          
            if (objConfigPasswordTO.getTokenNoReq() != null && objConfigPasswordTO.getTokenNoReq().equals("Y")) {
            setRdoTokenNoAllowYes(true);
            setRdoTokenNoAllowNo(false);
        } else {
            setRdoTokenNoAllowYes(false);
            setRdoTokenNoAllowNo(true);
        }
              if (objConfigPasswordTO.getServiceTaxReq() != null && objConfigPasswordTO.getServiceTaxReq().equals("Y")) {
            setRdoServiceTaxYes(true);
            setRdoServiceTaxNo(false);
        } else {
            setRdoServiceTaxYes(false);
            setRdoServiceTaxNo(true);
        }
                if (objConfigPasswordTO.getExclude_penal_int_from_reports() != null && objConfigPasswordTO.getExclude_penal_int_from_reports().equals("Y")) {
            setRdoExcludePenalIntFromReportsYes(true);
            setRdoExcludePenalIntFromReportsNo(false);
        } else {
            setRdoExcludePenalIntFromReportsYes(false);
            setRdoExcludePenalIntFromReportsNo(true);
        }
                  if (objConfigPasswordTO.getMultiShareAllowed() != null && objConfigPasswordTO.getMultiShareAllowed().equals("Y")) {
            setRdoMultiShareYes(true);
            setRdoMultiShareNo(false);
        } else {
            setRdoMultiShareYes(false);
            setRdoMultiShareNo(true);
        }
        
        
        //s
        if (CommonUtil.convertObjToStr(objConfigPasswordTO.getAuthorizeStatus()).length() > 0) {
            setIsAuth(true);
        } else {
            setIsAuth(false);
        }
        /**
         * Setting the OBfields Related to Age configuration *
         */
        setTxtMinorAge(CommonUtil.convertObjToStr(objConfigPasswordTO.getMinorAge()));
        setTxtRetireAge(CommonUtil.convertObjToStr(objConfigPasswordTO.getRetirementAge()));
        setTxtSeniorCitizenAge(CommonUtil.convertObjToStr(objConfigPasswordTO.getSeniorCitizenAge()));

        /**
         * Setting the OBfields Related to Service period configuration *
         */
        HashMap mapData = new HashMap();

        serviceList = (List) ClientUtil.executeQuery("getServicePeriodTO", mapData);
        if (serviceList != null && serviceList.size() > 0) {
            // setServicePeriodOB();

            mapData = (HashMap) serviceList.get(0);
            setTdtEffectiveDate(CommonUtil.convertObjToStr(mapData.get("EFFECTIVE_FROM")));
            setTxtServicePeriod(CommonUtil.convertObjToStr(mapData.get("EMP_SERVICE_PERIOD")));
            /**
             * Setting the OBFields related to Account Head Configuration *
             */
        }
        setTxtCashActHead(objConfigPasswordTO.getCashAcHd());
        setTxtIBRActHead(objConfigPasswordTO.getIbrAcHd());
        setTxtAppSuspenseAcHd((objConfigPasswordTO.getApp_suspense_achd()));
        setTxtSalarySuspense((objConfigPasswordTO.getSalary_suspense()));
        setTxtRTGS_GL((objConfigPasswordTO.getRtgs_gl()));
        /* Setting the OBFields related to Head Office and Branches configuration */
//        setCboBranches(CommonUtil.convertObjToStr(getCbmBranches().getDataForKey(CommonUtil.convertObjToStr(objConfigPasswordTO.getCboBranches()))));
        cbmBranches.setKeyForSelected(CommonUtil.convertObjToStr(objConfigPasswordTO.getCboBranches()));
        if (!CommonUtil.convertObjToStr(objConfigPasswordTO.getDayEndType()).equals("")) {
            if (CommonUtil.convertObjToStr(objConfigPasswordTO.getDayEndType()).equals("BRANCH_LEVEL")) {
                setRdoDayEndType_Branch(true);
                setRdoDayEndType_Bank(false);
            } else {
                setRdoDayEndType_Branch(false);
                setRdoDayEndType_Bank(true);
            }
        }
        if (!CommonUtil.convertObjToStr(objConfigPasswordTO.getInterBranchOnHoliday()).equals("")) {
            if (CommonUtil.convertObjToStr(objConfigPasswordTO.getInterBranchOnHoliday()).equals("Y")) {
                setRdoIB_OnHoliday_Yes(true);
                setRdoIB_OnHoliday_No(false);
            } else {
                setRdoIB_OnHoliday_Yes(false);
                setRdoIB_OnHoliday_No(true);
            }
        }
    }

    private void setSIChargesHeadOB() throws Exception {
        log.info("In setSIChargesHeadOB...");

        setTxtSIChargesHead(objSIChargesTO.getSiChargesHd());
        setTxtRemitChargesHead(objSIChargesTO.getRemitChargesHd());
        setTxtAcceptChargesHead(objSIChargesTO.getAcceptChargesHd());
        setTxtExecChargesHead(objSIChargesTO.getExecChargesHd());
        setTxtFailChargesHead(objSIChargesTO.getFailChargesHd());
        setTxtServiceTaxHead(objSIChargesTO.getServiceTaxHd());
        setTxtServiceTax(CommonUtil.convertObjToStr(objSIChargesTO.getServiceTax()));
        setTxtNmfAcHead(objSIChargesTO.getNmfAcHd());
        
        
    }

    /**
     * To perform the appropriate operation
     */
    public void doAction() {
        try {
            log.info("Inside doAction()");
            final HashMap data = new HashMap();

            //__ If the AuthMap is Not null...
            if (getAuthorizeMap() == null) {
                if (isPresent()) {
                    data.put("TOSTATUS", CommonConstants.TOSTATUS_UPDATE);
                    setLblStatus(ClientConstants.RESULT_STATUS[2]);
                } else {
                    data.put("TOSTATUS", CommonConstants.TOSTATUS_INSERT);
                    setLblStatus(ClientConstants.RESULT_STATUS[1]);
                }
                data.put("ConfigPasswordTO", setConfigPasswordTO());
                data.put("SIChargesHeadTO", setSIChargesHeadTO());
            } else { //__ to Set the Result Status for Authorized...
                setLblStatus(ClientConstants.RESULT_STATUS[getActionType()]);
            }

            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());

            data.put(CommonConstants.MODULE, getModule());
            data.put(CommonConstants.SCREEN, getScreen());
            System.out.println("#$#$#$ data before execute : " + data);
            HashMap proxyResultMap = proxy.execute(data, operationMap);
        } catch (Exception e) {
            //__ Set the Status Field as Failed...
            setLblStatus(ClientConstants.RESULT_STATUS[4]);
            parseException.logException(e, true);
        }
    }

    public void populateData() {
        try {
            HashMap whereMap = new HashMap();
            HashMap mapName = new HashMap();

            mapName.put(CommonConstants.MAP_NAME, "getSelectConfigPasswordTO");

            if (getViewType() != 0) {
                whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            }

            mapName.put(CommonConstants.MAP_WHERE, whereMap);

            HashMap mapData = proxy.executeQuery(mapName, operationMap);
            mapName = null;

            populateOB(mapData);
            ttNotifyObservers();
            mapData = null;
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /**
     * To Populate the Data in the Screen
     */
    private void populateOB(HashMap mapData) throws Exception {
        log.info("In populateOB...");
        if (mapData != null && mapData.size() > 0) {
            dataList = (List) mapData.get("ConfigPasswordTO");
            if (dataList != null && dataList.size() > 0) {
                setLblStatus(ClientConstants.ACTION_STATUS[2]);
                objConfigPasswordTO = (ConfigPasswordTO) dataList.get(0);
                setConfigPasswordOB();

//                setSIChargesHeadOB();
//                dataList = null;
                siList = (List) mapData.get("SIChargesHeadTO");
                if (siList != null && siList.size() > 0) {
                    setLblStatus(ClientConstants.ACTION_STATUS[2]);
                    objSIChargesTO = (SIChargesHeadTO) siList.get(0);
                    setSIChargesHeadOB();
                }
            } else {
                setIsAuth(true);
                setLblStatus(ClientConstants.ACTION_STATUS[1]);
                resetForm();
            }
        }
    }

    /**
     * To Check Whether Data is Already Present in the database PARAMETERS TABLE
     */
    private boolean isPresent() {
        boolean recordExist = false;
        if (dataList != null && dataList.size() > 0) {
            recordExist = true;
        } else {
            recordExist = false;
        }
        return recordExist;
    }

    /**
     * To validate Pwd Conditions Applied
     */
    public boolean validatePwdRules(String min, String splChar, String numeric, String upperCase) {
        int value1 = 0, value2 = 0, value3 = 0, value4 = 0;
        boolean valid = true;

        if ((min != null && min.length() > 0 && !min.equals(""))
                && (splChar != null && splChar.length() > 0 && !splChar.equals(""))
                && (numeric != null && numeric.length() > 0 && !numeric.equals(""))
                && (upperCase != null && upperCase.length() > 0 && !upperCase.equals(""))) {

            value1 = convertStrToInt(min);
            value2 = convertStrToInt(splChar);
            value3 = convertStrToInt(numeric);
            value4 = convertStrToInt(upperCase);
            value2 += value3 + value4;
            if (value1 >= value2) {
                valid = false;
            }
        }
        return valid;
    }

    /**
     * To validate whether maxlength is greater than minlength
     */
    public boolean validateMaxLength(String min, String max) {
        int value1 = 0, value2 = 0;
        boolean valid = true;

        if ((max != null && max.length() > 0 && !max.equals(""))
                && (min != null && min.length() > 0 && !min.equals(""))) {
            value1 = convertStrToInt(max);
            value2 = convertStrToInt(min);

            if (value1 > value2) {
                valid = false;
            }
        }
        return valid;
    }

    /**
     * Parses String as Int
     */
    private int convertStrToInt(String str) {
        int temp = 0;
        if (str.length() > 0 && !str.equals("") && str != null) {
            temp = Integer.parseInt(str);
        }
        return temp;
    }

    public String getLblStatus() {
        return this.lblStatus;
    }

    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }

    public java.util.HashMap getAuthorizeMap() {
        return this.authorizeMap;
    }

    public void setAuthorizeMap(HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }

    public void setActionType(int action) {
        this.actionType = action;
        setChanged();
    }

    public int getActionType() {
        return this.actionType;
    }

    public int getViewType() {
        return this.viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public boolean getIsAuth() {
        return this.isAuth;
    }

    public void setIsAuth(boolean isAuth) {
        this.isAuth = isAuth;
    }

    /**
     * Getter for property cbmBranches.
     *
     * @return Value of property cbmBranches.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBranches() {
        return cbmBranches;
    }

    /**
     * Setter for property cbmBranches.
     *
     * @param cbmBranches New value of property cbmBranches.
     */
    public void setCbmBranches(com.see.truetransact.clientutil.ComboBoxModel cbmBranches) {
        this.cbmBranches = cbmBranches;
    }

    /**
     * Getter for property cboBranches.
     *
     * @return Value of property cboBranches.
     */
    public java.lang.String getCboBranches() {
        return cboBranches;
    }

    /**
     * Setter for property cboBranches.
     *
     * @param cboBranches New value of property cboBranches.
     */
    public void setCboBranches(java.lang.String cboBranches) {
        this.cboBranches = cboBranches;
    }

    /**
     * Getter for property txtSIChargesHead.
     *
     * @return Value of property txtSIChargesHead.
     */
    public java.lang.String getTxtSIChargesHead() {
        return txtSIChargesHead;
    }

    /**
     * Setter for property txtSIChargesHead.
     *
     * @param txtSIChargesHead New value of property txtSIChargesHead.
     */
    public void setTxtSIChargesHead(java.lang.String txtSIChargesHead) {
        this.txtSIChargesHead = txtSIChargesHead;
    }

    /**
     * Getter for property txtRemitChargesHead.
     *
     * @return Value of property txtRemitChargesHead.
     */
    public java.lang.String getTxtRemitChargesHead() {
        return txtRemitChargesHead;
    }

    /**
     * Setter for property txtRemitChargesHead.
     *
     * @param txtRemitChargesHead New value of property txtRemitChargesHead.
     */
    public void setTxtRemitChargesHead(java.lang.String txtRemitChargesHead) {
        this.txtRemitChargesHead = txtRemitChargesHead;
    }

    /**
     * Getter for property txtAcceptChargesHead.
     *
     * @return Value of property txtAcceptChargesHead.
     */
    public java.lang.String getTxtAcceptChargesHead() {
        return txtAcceptChargesHead;
    }

    /**
     * Setter for property txtAcceptChargesHead.
     *
     * @param txtAcceptChargesHead New value of property txtAcceptChargesHead.
     */
    public void setTxtAcceptChargesHead(java.lang.String txtAcceptChargesHead) {
        this.txtAcceptChargesHead = txtAcceptChargesHead;
    }

    /**
     * Getter for property txtExecChargesHead.
     *
     * @return Value of property txtExecChargesHead.
     */
    public java.lang.String getTxtExecChargesHead() {
        return txtExecChargesHead;
    }

    /**
     * Setter for property txtExecChargesHead.
     *
     * @param txtExecChargesHead New value of property txtExecChargesHead.
     */
    public void setTxtExecChargesHead(java.lang.String txtExecChargesHead) {
        this.txtExecChargesHead = txtExecChargesHead;
    }

    /**
     * Getter for property txtFailChargesHead.
     *
     * @return Value of property txtFailChargesHead.
     */
    public java.lang.String getTxtFailChargesHead() {
        return txtFailChargesHead;
    }

    /**
     * Setter for property txtFailChargesHead.
     *
     * @param txtFailChargesHead New value of property txtFailChargesHead.
     */
    public void setTxtFailChargesHead(java.lang.String txtFailChargesHead) {
        this.txtFailChargesHead = txtFailChargesHead;
    }

    /**
     * Getter for property rdoDayEndType_Branch.
     *
     * @return Value of property rdoDayEndType_Branch.
     */
    public boolean isRdoDayEndType_Branch() {
        return rdoDayEndType_Branch;
    }

    /**
     * Setter for property rdoDayEndType_Branch.
     *
     * @param rdoDayEndType_Branch New value of property rdoDayEndType_Branch.
     */
    public void setRdoDayEndType_Branch(boolean rdoDayEndType_Branch) {
        this.rdoDayEndType_Branch = rdoDayEndType_Branch;
    }

    /**
     * Getter for property rdoDayEndType_Bank.
     *
     * @return Value of property rdoDayEndType_Bank.
     */
    public boolean isRdoDayEndType_Bank() {
        return rdoDayEndType_Bank;
    }

    /**
     * Setter for property rdoDayEndType_Bank.
     *
     * @param rdoDayEndType_Bank New value of property rdoDayEndType_Bank.
     */
    public void setRdoDayEndType_Bank(boolean rdoDayEndType_Bank) {
        this.rdoDayEndType_Bank = rdoDayEndType_Bank;
    }

    /**
     * Getter for property rdoIB_OnHoliday_Yes.
     *
     * @return Value of property rdoIB_OnHoliday_Yes.
     */
    public boolean isRdoIB_OnHoliday_Yes() {
        return rdoIB_OnHoliday_Yes;
    }

    /**
     * Setter for property rdoIB_OnHoliday_Yes.
     *
     * @param rdoIB_OnHoliday_Yes New value of property rdoIB_OnHoliday_Yes.
     */
    public void setRdoIB_OnHoliday_Yes(boolean rdoIB_OnHoliday_Yes) {
        this.rdoIB_OnHoliday_Yes = rdoIB_OnHoliday_Yes;
    }

    /**
     * Getter for property rdoIB_OnHoliday_No.
     *
     * @return Value of property rdoIB_OnHoliday_No.
     */
    public boolean isRdoIB_OnHoliday_No() {
        return rdoIB_OnHoliday_No;
    }

    /**
     * Setter for property rdoIB_OnHoliday_No.
     *
     * @param rdoIB_OnHoliday_No New value of property rdoIB_OnHoliday_No.
     */
    public void setRdoIB_OnHoliday_No(boolean rdoIB_OnHoliday_No) {
        this.rdoIB_OnHoliday_No = rdoIB_OnHoliday_No;
    }

    /**
     * Getter for property txtServiceTaxHead.
     *
     * @return Value of property txtServiceTaxHead.
     */
    public java.lang.String getTxtServiceTaxHead() {
        return txtServiceTaxHead;
    }

    /**
     * Setter for property txtServiceTaxHead.
     *
     * @param txtServiceTaxHead New value of property txtServiceTaxHead.
     */
    public void setTxtServiceTaxHead(java.lang.String txtServiceTaxHead) {
        this.txtServiceTaxHead = txtServiceTaxHead;
    }

    /**
     * Getter for property txtServiceTax.
     *
     * @return Value of property txtServiceTax.
     */
    public java.lang.String getTxtServiceTax() {
        return txtServiceTax;
    }

    /**
     * Setter for property txtServiceTax.
     *
     * @param txtServiceTax New value of property txtServiceTax.
     */
    public void setTxtServiceTax(java.lang.String txtServiceTax) {
        this.txtServiceTax = txtServiceTax;
    }

    /**
     * Getter for property PanAmount.
     *
     * @return Value of property PanAmount.
     */
  

    /**
     * Getter for property tdtEffectiveDate.
     *
     * @return Value of property tdtEffectiveDate.
     */
    public String getTdtEffectiveDate() {
        return tdtEffectiveDate;
    }

    /**
     * Setter for property tdtEffectiveDate.
     *
     * @param tdtEffectiveDate New value of property tdtEffectiveDate.
     */
    public void setTdtEffectiveDate(String tdtEffectiveDate) {
        this.tdtEffectiveDate = tdtEffectiveDate;
    }

    /**
     * Getter for property txtServicePeriod.
     *
     * @return Value of property txtServicePeriod.
     */
    public String getTxtServicePeriod() {
        return txtServicePeriod;
    }

    public boolean isRdoTokenNoAllowYes() {
        return rdoTokenNoAllowYes;
    }

    public void setRdoTokenNoAllowYes(boolean rdoTokenNoAllowYes) {
        this.rdoTokenNoAllowYes = rdoTokenNoAllowYes;
    }

    public boolean isRdoTokenNoAllowNo() {
        return rdoTokenNoAllowNo;
    }

    public void setRdoTokenNoAllowNo(boolean rdoTokenNoAllowNo) {
        this.rdoTokenNoAllowNo = rdoTokenNoAllowNo;
    }

    /**
     * Setter for property txtServicePeriod.
     *
     * @param txtServicePeriod New value of property txtServicePeriod.
     */
    public void setTxtServicePeriod(String txtServicePeriod) {
        this.txtServicePeriod = txtServicePeriod;
    }

    public String getTxtSeniorCitizenAge() {
        return txtSeniorCitizenAge;
    }

    public void setTxtSeniorCitizenAge(String txtSeniorCitizenAge) {
        this.txtSeniorCitizenAge = txtSeniorCitizenAge;
    }

    public String getTxtAppSuspenseAcHd() {
        return txtAppSuspenseAcHd;
    }

    public void setTxtAppSuspenseAcHd(String txtAppSuspenseAcHd) {
        this.txtAppSuspenseAcHd = txtAppSuspenseAcHd;
    }

    public String getTdtLastFinancialYearEnd() {
        return tdtLastFinancialYearEnd;
    }

    public void setTdtLastFinancialYearEnd(String tdtLastFinancialYearEnd) {
        this.tdtLastFinancialYearEnd = tdtLastFinancialYearEnd;
    }

    public String getTdtYearEndProcessDate() {
        return tdtYearEndProcessDate;
    }

    public void setTdtYearEndProcessDate(String tdtYearEndProcessDate) {
        this.tdtYearEndProcessDate = tdtYearEndProcessDate;
    }

    public String getTxtSalarySuspense() {
        return txtSalarySuspense;
    }

    public void setTxtSalarySuspense(String txtSalarySuspense) {
        this.txtSalarySuspense = txtSalarySuspense;
    }

    public String getBtnSuspenseAcHd() {
        return btnSuspenseAcHd;
    }

    public void setBtnSuspenseAcHd(String btnSuspenseAcHd) {
        this.btnSuspenseAcHd = btnSuspenseAcHd;
    }

    public String getBtnSalarySuspense() {
        return btnSalarySuspense;
    }

    public void setBtnSalarySuspense(String btnSalarySuspense) {
        this.btnSalarySuspense = btnSalarySuspense;
    }

    public String getBtnServiceTaxHead() {
        return btnServiceTaxHead;
    }

    public void setBtnServiceTaxHead(String btnServiceTaxHead) {
        this.btnServiceTaxHead = btnServiceTaxHead;
    }

    public String getBtnRTGS_GL() {
        return btnRTGS_GL;
    }

    public void setBtnRTGS_GL(String btnRTGS_GL) {
        this.btnRTGS_GL = btnRTGS_GL;
    }

    public String getTxtRTGS_GL() {
        return txtRTGS_GL;
    }

    public void setTxtRTGS_GL(String txtRTGS_GL) {
        this.txtRTGS_GL = txtRTGS_GL;
    }

    public String getTdtFromDate() {
        return tdtFromDate;
    }

    public void setTdtFromDate(String tdtFromDate) {
        this.tdtFromDate = tdtFromDate;
    }

    public String getTdtToDate() {
        return tdtToDate;
    }

    public void setTdtToDate(String tdtToDate) {
        this.tdtToDate = tdtToDate;
    }

    public String getTxtAmcAlertTime() {
        return txtAmcAlertTime;
    }

    public void setTxtAmcAlertTime(String txtAmcAlertTime) {
        this.txtAmcAlertTime = txtAmcAlertTime;
    }

    public String getTxtGahanPeriod() {
        return txtGahanPeriod;
    }

    public void setTxtGahanPeriod(String txtGahanPeriod) {
        this.txtGahanPeriod = txtGahanPeriod;
    }

    public String getPanAuthorizationByStaff() {
        return panAuthorizationByStaff;
    }

    public void setPanAuthorizationByStaff(String panAuthorizationByStaff) {
        this.panAuthorizationByStaff = panAuthorizationByStaff;
    }

    public String getPanDenomination() {
        return panDenomination;
    }

    public void setPanDenomination(String panDenomination) {
        this.panDenomination = panDenomination;
    }

    public String getPanMultiShare() {
        return panMultiShare;
    }

    public void setPanMultiShare(String panMultiShare) {
        this.panMultiShare = panMultiShare;
    }

    public String getPanServiceTax() {
        return panServiceTax;
    }

    public void setPanServiceTax(String panServiceTax) {
        this.panServiceTax = panServiceTax;
    }

    public String getPanTokenNoAllow() {
        return panTokenNoAllow;
    }

    public void setPanTokenNoAllow(String panTokenNoAllow) {
        this.panTokenNoAllow = panTokenNoAllow;
    }

    public String getPanCashierAuthorization() {
        return panCashierAuthorization;
    }

    public void setPanCashierAuthorization(String panCashierAuthorization) {
        this.panCashierAuthorization = panCashierAuthorization;
    }

    public String getPanExcludePenalIntFromReports() {
        return panExcludePenalIntFromReports;
    }

    public void setPanExcludePenalIntFromReports(String panExcludePenalIntFromReports) {
        this.panExcludePenalIntFromReports = panExcludePenalIntFromReports;
    }

    public String getTxtPendingTxnAllowedDays() {
        return txtPendingTxnAllowedDays;
    }

    public void setTxtPendingTxnAllowedDays(String txtPendingTxnAllowedDays) {
        this.txtPendingTxnAllowedDays = txtPendingTxnAllowedDays;
    }

    public String getTxtPanDetails() {
        return txtPanDetails;
    }

    public void setTxtPanDetails(String txtPanDetails) {
        this.txtPanDetails = txtPanDetails;
    }

    public String getTdtEffectiveFrom() {
        return tdtEffectiveFrom;
    }

    public void setTdtEffectiveFrom(String tdtEffectiveFrom) {
        this.tdtEffectiveFrom = tdtEffectiveFrom;
    }

    public boolean isRdoCashierAuthorizationNo() {
        return rdoCashierAuthorizationNo;
    }

    public void setRdoCashierAuthorizationNo(boolean rdoCashierAuthorizationNo) {
        this.rdoCashierAuthorizationNo = rdoCashierAuthorizationNo;
    }

    public boolean isRdoAllowAuthorizationNo() {
        return rdoAllowAuthorizationNo;
    }

    public void setRdoAllowAuthorizationNo(boolean rdoAllowAuthorizationNo) {
        this.rdoAllowAuthorizationNo = rdoAllowAuthorizationNo;
    }

    public boolean isRdoCashierAuthorizationYes() {
        return rdoCashierAuthorizationYes;
    }

    public void setRdoCashierAuthorizationYes(boolean rdoCashierAuthorizationYes) {
        this.rdoCashierAuthorizationYes = rdoCashierAuthorizationYes;
    }

    public boolean isRdoExcludePenalIntFromReportsYes() {
        return rdoExcludePenalIntFromReportsYes;
    }

    public void setRdoExcludePenalIntFromReportsYes(boolean rdoExcludePenalIntFromReportsYes) {
        this.rdoExcludePenalIntFromReportsYes = rdoExcludePenalIntFromReportsYes;
    }

    public boolean isRdoExcludePenalIntFromReportsNo() {
        return rdoExcludePenalIntFromReportsNo;
    }

    public void setRdoExcludePenalIntFromReportsNo(boolean rdoExcludePenalIntFromReportsNo) {
        this.rdoExcludePenalIntFromReportsNo = rdoExcludePenalIntFromReportsNo;
    }

    public boolean isRdoAllowAuthorizationYes() {
        return rdoAllowAuthorizationYes;
    }

    public void setRdoAllowAuthorizationYes(boolean rdoAllowAuthorizationYes) {
        this.rdoAllowAuthorizationYes = rdoAllowAuthorizationYes;
    }

    public boolean isRdoDenominationYes() {
        return rdoDenominationYes;
    }

    public void setRdoDenominationYes(boolean rdoDenominationYes) {
        this.rdoDenominationYes = rdoDenominationYes;
    }

    public boolean isRdoDenominationNo() {
        return rdoDenominationNo;
    }

    public void setRdoDenominationNo(boolean rdoDenominationNo) {
        this.rdoDenominationNo = rdoDenominationNo;
    }

    public boolean isRdoServiceTaxYes() {
        return rdoServiceTaxYes;
    }

    public void setRdoServiceTaxYes(boolean rdoServiceTaxYes) {
        this.rdoServiceTaxYes = rdoServiceTaxYes;
    }

    public boolean isRdoServiceTaxNo() {
        return rdoServiceTaxNo;
    }

    public void setRdoServiceTaxNo(boolean rdoServiceTaxNo) {
        this.rdoServiceTaxNo = rdoServiceTaxNo;
    }

    public boolean isRdoMultiShareYes() {
        return rdoMultiShareYes;
    }

    public void setRdoMultiShareYes(boolean rdoMultiShareYes) {
        this.rdoMultiShareYes = rdoMultiShareYes;
    }

    public boolean isRdoMultiShareNo() {
        return rdoMultiShareNo;
    }

    public void setRdoMultiShareNo(boolean rdoMultiShareNo) {
        this.rdoMultiShareNo = rdoMultiShareNo;
    }

}
