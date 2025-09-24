/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EarningsDeductionOB.java
 *
 * 
 */
package com.see.truetransact.ui.payroll.earningsDeductionGlobal;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.payroll.earningsDeductions.EarnDeduAccTO;
import com.see.truetransact.transferobject.payroll.earningsDeductions.EarnDeduPaySettingsTO;
import com.see.truetransact.transferobject.payroll.earningsDeductions.EarnDeduPayTO;
import com.see.truetransact.transferobject.payroll.earningsDeductions.EarningsDeductionTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.CObservable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 *
 * @author anjuanand
 */
public class EarningsDeductionOB extends CObservable {

    private static EarningsDeductionOB earnDeduOB;
    private static CInternalFrame frame;
    private Date curDate = null;
    private HashMap operationMap;
    private int actionType;
    private int result;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(com.see.truetransact.ui.payroll.earningsDeductionGlobal.EarningsDeductionUI.class);
    private ProxyFactory proxy = null;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private String cboModuleType = "";
    private String txtDescription = "";
    private String cboCalculationType = "";
    private String txtAmount = "";
    private String txtAccountHead = "";
    private String cboProductType = "";
    private String cboAccountType = "";
    private String cboCalcModType = "";
    private String txtPercentage = "";
    private String txtMinAmt = "";
    private String txtMaxAmt = "";
    private boolean rdoEarnings = false;
    private boolean rdoDeduction = false;
    private boolean rdoContra = false;
    private boolean chkActive = false;
    private boolean chkExcludeFromTax = false;
    private boolean chkPaymentVoucherRequired = false;
    private boolean chkIndividualRequired = false;
    private boolean chkIncludePersonalPay = false;
    private boolean chkOnlyForContra = false;
    private boolean chkGl = false;
    private ComboBoxModel cbmModuleType;
    private ComboBoxModel cbmCalcModType;
    private ComboBoxModel cbmProductType;
    private EarnDeduAccTO objEarnDeduAccTO;
    private EarningsDeductionTO objEarningsDeductionTO;
    private TableModel tbmAccountDetails;
    private TableModel tbmCalcDetails;
    private ArrayList accRecordData, deleteAccData;
    private ArrayList calcRecordData, deleteCalcData;
    private int accSlNo = 1;
    private int calcSlNo = 1;
    private boolean check;
    private String modType = "";
    public String payCodeId = "";
    private String tblModType = "";
    private String tblPayCode = "";
    private String tdtFromDate = "";
    private boolean isFilled = false;
    private String payCode = "";

    public boolean isChkGl() {
        return chkGl;
    }

    public void setChkGl(boolean chkGl) {
        this.chkGl = chkGl;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public boolean isIsFilled() {
        return isFilled;
    }

    public void setIsFilled(boolean isFilled) {
        this.isFilled = isFilled;
    }

    public String getTdtFromDate() {
        return tdtFromDate;
    }

    public void setTdtFromDate(String tdtFromDate) {
        this.tdtFromDate = tdtFromDate;
    }

    public String getTblModType() {
        return tblModType;
    }

    public void setTblModType(String tblModType) {
        this.tblModType = tblModType;
    }

    public String getTblPayCode() {
        return tblPayCode;
    }

    public void setTblPayCode(String tblPayCode) {
        this.tblPayCode = tblPayCode;
    }

    public String getPayCodeId() {
        return payCodeId;
    }

    public void setPayCodeId(String payCodeId) {
        this.payCodeId = payCodeId;
    }

    public String getModType() {
        return modType;
    }

    public void setModType(String modType) {
        this.modType = modType;
    }

    public boolean isChkOnlyForContra() {
        return chkOnlyForContra;
    }

    public void setChkOnlyForContra(boolean chkOnlyForContra) {
        this.chkOnlyForContra = chkOnlyForContra;
    }

    public int getAccSlNo() {
        return accSlNo;
    }

    public void setAccSlNo(int accSlNo) {
        this.accSlNo = accSlNo;
    }

    public int getCalcSlNo() {
        return calcSlNo;
    }

    public void setCalcSlNo(int calcSlNo) {
        this.calcSlNo = calcSlNo;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public TableModel getTbmCalcDetails() {
        return tbmCalcDetails;
    }

    public void setTbmCalcDetails(TableModel tbmCalcDetails) {
        this.tbmCalcDetails = tbmCalcDetails;
    }

    public TableModel getTbmAccountDetails() {
        return tbmAccountDetails;
    }

    public void setTbmAccountDetails(TableModel tbmAccountDetails) {
        this.tbmAccountDetails = tbmAccountDetails;
    }

    public ComboBoxModel getCbmCalcModType() {
        return cbmCalcModType;
    }

    public void setCbmCalcModType(ComboBoxModel cbmCalcModType) {
        this.cbmCalcModType = cbmCalcModType;
        setChanged();
    }

    public ComboBoxModel getCbmModuleType() {
        return cbmModuleType;
    }

    public void setCbmModuleType(ComboBoxModel cbmModuleType) {
        this.cbmModuleType = cbmModuleType;
        setChanged();
    }

    public ComboBoxModel getCbmProductType() {
        return cbmProductType;
    }

    public void setCbmProductType(ComboBoxModel cbmProductType) {
        this.cbmProductType = cbmProductType;
        setChanged();
    }

    public String getCboAccountType() {
        return cboAccountType;
    }

    public void setCboAccountType(String cboAccountType) {
        this.cboAccountType = cboAccountType;
    }

    public String getCboCalculationType() {
        return cboCalculationType;
    }

    public void setCboCalculationType(String cboCalculationType) {
        this.cboCalculationType = cboCalculationType;
    }

    public String getCboCalcModType() {
        return cboCalcModType;
    }

    public void setCboCalcModType(String cboCalcModType) {
        this.cboCalcModType = cboCalcModType;
    }

    public String getCboModuleType() {
        return cboModuleType;
    }

    public void setCboModuleType(String cboModuleType) {
        this.cboModuleType = cboModuleType;
    }

    public String getCboProductType() {
        return cboProductType;
    }

    public void setCboProductType(String cboProductType) {
        this.cboProductType = cboProductType;
    }

    public boolean isChkActive() {
        return chkActive;
    }

    public void setChkActive(boolean chkActive) {
        this.chkActive = chkActive;
    }

    public boolean isChkExcludeFromTax() {
        return chkExcludeFromTax;
    }

    public void setChkExcludeFromTax(boolean chkExcludeFromTax) {
        this.chkExcludeFromTax = chkExcludeFromTax;
    }

    public boolean isChkIncludePersonalPay() {
        return chkIncludePersonalPay;
    }

    public void setChkIncludePersonalPay(boolean chkIncludePersonalPay) {
        this.chkIncludePersonalPay = chkIncludePersonalPay;
    }

    public boolean isChkIndividualRequired() {
        return chkIndividualRequired;
    }

    public void setChkIndividualRequired(boolean chkIndividualRequired) {
        this.chkIndividualRequired = chkIndividualRequired;
    }

    public boolean isChkPaymentVoucherRequired() {
        return chkPaymentVoucherRequired;
    }

    public void setChkPaymentVoucherRequired(boolean chkPaymentVoucherRequired) {
        this.chkPaymentVoucherRequired = chkPaymentVoucherRequired;
    }

    public boolean isRdoContra() {
        return rdoContra;
    }

    public void setRdoContra(boolean rdoContra) {
        this.rdoContra = rdoContra;
    }

    public boolean isRdoDeduction() {
        return rdoDeduction;
    }

    public void setRdoDeduction(boolean rdoDeduction) {
        this.rdoDeduction = rdoDeduction;
    }

    public boolean isRdoEarnings() {
        return rdoEarnings;
    }

    public void setRdoEarnings(boolean rdoEarnings) {
        this.rdoEarnings = rdoEarnings;
    }

    public String getTxtAccountHead() {
        return txtAccountHead;
    }

    public void setTxtAccountHead(String txtAccountHead) {
        this.txtAccountHead = txtAccountHead;
    }

    public String getTxtAmount() {
        return txtAmount;
    }

    public void setTxtAmount(String txtAmount) {
        this.txtAmount = txtAmount;
    }

    public String getTxtDescription() {
        return txtDescription;
    }

    public void setTxtDescription(String txtDescription) {
        this.txtDescription = txtDescription;
    }

    public String getTxtMaxAmt() {
        return txtMaxAmt;
    }

    public void setTxtMaxAmt(String txtMaxAmt) {
        this.txtMaxAmt = txtMaxAmt;
    }

    public String getTxtMinAmt() {
        return txtMinAmt;
    }

    public void setTxtMinAmt(String txtMinAmt) {
        this.txtMinAmt = txtMinAmt;
    }

    public String getTxtPercentage() {
        return txtPercentage;
    }

    public void setTxtPercentage(String txtPercentage) {
        this.txtPercentage = txtPercentage;
    }

    static {
        try {
            log.info("In earnDeduOB Declaration");
            earnDeduOB = new EarningsDeductionOB();
        } catch (Exception e) {
            log.info("Error in earnDeduOB Declaration");
        }
    }

    public static EarningsDeductionOB getInstance(CInternalFrame frm) {
        frame = frm;
        return earnDeduOB;
    }

    public Date getCurrentDate() {
        return (Date) curDate.clone();
    }

    /**
     * Creates a new instance of EarningsDeductionOB
     */
    public EarningsDeductionOB() throws Exception {
        try {
            curDate = ClientUtil.getCurrentDate();
            initianSetup();
            setAccountTable();
            setCalcTable();
            accRecordData = new ArrayList();
            calcRecordData = new ArrayList();
            deleteAccData = new ArrayList();
            deleteCalcData = new ArrayList();
        } catch (Exception e) {
        }
    }

    private void initianSetup() throws Exception {
        setOperationMap();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        fillDropdown();
    }

    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception {
        log.info("In setOperationMap()");
        proxy = ProxyFactory.createProxy();
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "EarningsDeductionJNDI");
        operationMap.put(CommonConstants.HOME, "payroll.earningsDeductions.EarningsDeductionHome");
        operationMap.put(CommonConstants.REMOTE, "payroll.earningsDeductions.EarningsDeduction");
    }

    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) throws Exception {
        log.info("In populateData()");
        final HashMap mapData;
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            populateOB(mapData);
            final List accData;
            HashMap data = new HashMap();
            data.put("PAYCODE_ID", whereMap.get("PAYCODE_ID"));
            setPayCodeId(CommonUtil.convertObjToStr(whereMap.get("PAYCODE_ID")));
            accData = ClientUtil.executeQuery("getAllAccDetails", data);
            if (accData != null && accData.size() > 0) {
                HashMap result = new HashMap();
                for (int i = 0; i < accData.size(); i++) {
                    result = (HashMap) accData.get(i);
                    setPayCode(CommonUtil.convertObjToStr(result.get("PAY_CODE")));
                    setTxtAccountHead(CommonUtil.convertObjToStr(result.get("ACC_HD")));
                    setCboAccountType(CommonUtil.convertObjToStr(result.get("ACC_TYPE")));
                    if (result.get("PAY_PROD_TYPE").equals(CommonConstants.GL_TRANSMODE_TYPE)) {
                        setCboProductType(CommonConstants.GL_TRANSMODE_TYPE);
                    }
                }
                populateAccTblData(accData);
            }

            if (whereMap.get("PAY_CALC_TYPE").equals("Calculated")) {
                setCalcModType();
                final List calcData;
                calcData = ClientUtil.executeQuery("getAllCalcDetails", data);
                if (calcData != null && calcData.size() > 0) {
                    HashMap result = new HashMap();
                    for (int i = 0; i < calcData.size(); i++) {
                        result = (HashMap) calcData.get(i);
                        setTxtMinAmt(CommonUtil.convertObjToStr(result.get("PAY_MIN_AMT")));
                        setTxtMaxAmt(CommonUtil.convertObjToStr(result.get("PAY_MAX_AMT")));
                        setTxtPercentage(CommonUtil.convertObjToStr(result.get("PAY_PERCENT")));
                        setTdtFromDate(CommonUtil.convertObjToStr(result.get("FROM_DATE")));
                        if (result.get("PERSONAL_PAY").equals("Y")) {
                            setChkIncludePersonalPay(true);
                        } else {
                            setChkIncludePersonalPay(false);
                        }
                    }
                    populateCalcTblData(calcData);
                }
            }
            ttNotifyObservers();
        } catch (Exception e) {
            log.info("Error In populateData()");
            e.printStackTrace();
        }
    }

    private void populateOB(HashMap mapData) throws Exception {
        log.info("In populateOB()");
        EarningsDeductionTO objEarnDeductionTO = null;
        EarnDeduAccTO objEarnDeduAccTO = null;
        if (((List) mapData.get("EarningsDeduction")) != null && ((List) mapData.get("EarningsDeduction")).size() > 0) {
            objEarnDeductionTO = (EarningsDeductionTO) ((List) mapData.get("EarningsDeduction")).get(0);
            setEarnDeduct(objEarnDeductionTO);
        }
        if (((List) mapData.get("EarnDeduAccount")) != null && ((List) mapData.get("EarnDeduAccount")).size() > 0) {
            objEarnDeduAccTO = (EarnDeduAccTO) ((List) mapData.get("EarnDeduAccount")).get(0);
            setEarnDeductAcc(objEarnDeduAccTO);
        }
    }

    private void setEarnDeduct(EarningsDeductionTO objEarnDeductionTO) throws Exception {
        log.info("In setEarnDeduct()");
        if (objEarnDeductionTO.getPay_Code() != null) {
            setPayCode(objEarnDeductionTO.getPay_Code());
        }
        if (objEarnDeductionTO.getPay_EarnDedu().equals("EARNINGS")) {
            setRdoEarnings(true);
            HashMap earnMap = new HashMap();
            earnMap.put("LOOKUP_ID", "ELIGIBLE_ALLOWANCE");
            setPayModType(earnMap);
        } else if (objEarnDeductionTO.getPay_EarnDedu().equals("DEDUCTIONS")) {
            setRdoDeduction(true);
            HashMap deduMap = new HashMap();
            deduMap.put("LOOKUP_ID", "DEDUCTIONS");
            setPayModType(deduMap);
        } else if (objEarnDeductionTO.getPay_EarnDedu().equals("CONTRA")) {
            setRdoContra(true);
            HashMap deduMap = new HashMap();
            deduMap.put("LOOKUP_ID", "DEDUCTIONS");
            setPayModType(deduMap);
        }
        setCboModuleType(CommonUtil.convertObjToStr(objEarnDeductionTO.getPay_Mod_Type()));
        setTxtDescription(objEarnDeductionTO.getPay_Descri());
        setCboCalculationType(CommonUtil.convertObjToStr(objEarnDeductionTO.getPay_Calc_Type()));
        setCboProductType(CommonUtil.convertObjToStr(objEarnDeductionTO.getPay_Prod_Type()));
        if (objEarnDeductionTO.getPay_Calc_Type().equals("Fixed")) {
            setTxtAmount(CommonUtil.convertObjToStr(objEarnDeductionTO.getPay_Fix_Amt()));
        } else if (objEarnDeductionTO.getPay_Calc_Type().equals("Calculated")) {
            setTxtAmount(CommonUtil.convertObjToStr(objEarnDeductionTO.getPay_Fix_Amt()));
            setTxtMinAmt(CommonUtil.convertObjToStr(objEarnDeductionTO.getPay_Min_Amt()));
            setTxtMaxAmt(CommonUtil.convertObjToStr(objEarnDeductionTO.getPay_Max_Amt()));
            setTxtPercentage(CommonUtil.convertObjToStr(objEarnDeductionTO.getPay_Percent()));
            setTblPayCode(CommonUtil.convertObjToStr(objEarnDeductionTO.getPay_Calc_On()));
            if (objEarnDeductionTO.getPersonal_Pay().equals("Y")) {
                setChkIncludePersonalPay(true);
            } else {
                setChkIncludePersonalPay(false);
            }
        }
        if (objEarnDeductionTO.getActive().equals("Y")) {
            setChkActive(true);
        } else {
            setChkActive(false);
        }
        if (objEarnDeductionTO.getTaxable().equals("Y")) {
            setChkExcludeFromTax(true);
        } else {
            setChkExcludeFromTax(false);
        }
        if (objEarnDeductionTO.getPayment_Voucher().equals("Y")) {
            setChkPaymentVoucherRequired(true);
        } else {
            setChkPaymentVoucherRequired(false);
        }
        if (objEarnDeductionTO.getIndividual_reqd().equals("Y")) {
            setChkIndividualRequired(true);
        } else {
            setChkIndividualRequired(false);
        }
        if (objEarnDeductionTO.getContra_Only().equals("Y")) {
            setChkOnlyForContra(true);
        } else {
            setChkOnlyForContra(false);
        }
        log.info("End of setEarnDeduct()");
    }

    private void setEarnDeductAcc(EarnDeduAccTO objEarnDeductionAccTO) throws Exception {
        log.info("In setEarnDeductAcc()");
        setTxtAccountHead(objEarnDeductionAccTO.getAccHd());
        setCboAccountType(CommonUtil.convertObjToStr(objEarnDeductionAccTO.getAccType()));
        log.info("End of setEarnDeductAcc()");
    }

    public String getPayModule(HashMap dataMap) {
        String modType = "";
        List modTypes = ClientUtil.executeQuery("getPayModuleType", dataMap);
        if (modTypes != null && modTypes.size() > 0) {
            HashMap result = (HashMap) modTypes.get(0);
            modType = CommonUtil.convertObjToStr(result.get("PAY_MODULE_TYPE"));
            setTblModType(modType);
        }
        return modType;
    }

    public void doAction() {
        log.info("In doAction()");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if (actionType != ClientConstants.ACTIONTYPE_CANCEL) {
                //If actionType has got propervalue then doActionPerform, else throw error
                doActionPerform();
            }
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    /**
     * To perform the necessary action
     */
    private void doActionPerform() throws Exception {
        log.info("In doActionPerform()");
        try {
            final EarningsDeductionTO objEarnDeduTo = setEarningsDeduction();
            final EarnDeduAccTO objEarnDeduAccTo = setEarningsDeductionAccount();
            final EarnDeduPayTO objEarnDeduPayTo = setEarningsDeductionPay();
            final EarnDeduPaySettingsTO objEarnDeduSettingsTo = setEarningsDeductionSettings();
            final List employeeId = getEmployeeId();
            objEarnDeduTo.setCommand(getCommand());
            objEarnDeduAccTo.setCommand(getCommand());
            objEarnDeduPayTo.setCommand(getCommand());
            final HashMap data = new HashMap();
            data.put("EARNDEDU", objEarnDeduTo);
            data.put("EARNDEDUACC", objEarnDeduAccTo);
            data.put("ACCDATA", accRecordData);
            data.put("CALCDATA", calcRecordData);
            data.put("EMPID", employeeId);
            data.put("PAYDATA", objEarnDeduPayTo);
            data.put("SETTINGSDATA", objEarnDeduSettingsTo);
            HashMap proxyResultMap = proxy.execute(data, operationMap);
            if (proxyResultMap != null && proxyResultMap.containsKey("PayCode") && proxyResultMap.get("PayCode") != null) {
                ClientUtil.showMessageWindow("Successfully Created PayCode : " + CommonUtil.convertObjToStr(proxyResultMap.get("PayCode")));
            }
            setProxyReturnMap(proxyResultMap);
            resetForm();
        } catch (Exception e) {
            log.info("Error In doActionPerform()");
            ClientUtil.showMessageWindow(e.getMessage());
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        log.info("getKeyValue");
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    public void fillDropdown() throws Exception {
        log.info("fillDropdown");
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    public void setPayModType(HashMap payMap) {
        List lst = (List) ClientUtil.executeQuery("getPayType", payMap);
        if (lst != null && lst.size() > 0) {
            getMap(lst);
            cbmModuleType = new ComboBoxModel(key, value);
            setCbmModuleType(cbmModuleType);
        }
    }

    public List setCalcModType() {
        List lst = (List) ClientUtil.executeQuery("getCalcModType", null);
        if (lst != null && lst.size() > 0) {
            getMap(lst);
            cbmCalcModType = new ComboBoxModel(key, value);
            setCbmCalcModType(cbmCalcModType);
        }
        return lst;
    }

    public List setProductType() {
        List lst = (List) ClientUtil.executeQuery("getProductTypes", null);
        if (lst != null && lst.size() > 0) {
            getMap(lst);
            if (!isChkGl()) {
                key.remove("GL");
                value.remove("General Ledger");
            } else {
                key.clear();
                value.clear();
                key.add("");
                value.add("");
                key.add("GL");
                value.add("General Ledger");
            }
            cbmProductType = new ComboBoxModel(key, value);
            setCbmProductType(cbmProductType);
            return lst;
        }
        return null;
    }

    public List getEmployeeId() {
        List list = (List) ClientUtil.executeQuery("getAllEmpId", null);
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    private void getMap(List list) {
        if (list != null && list.size() > 0) {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
            for (int i = 0, j = list.size(); i < j; i++) {
                key.add(((HashMap) list.get(i)).get("KEY"));
                value.add(((HashMap) list.get(i)).get("VALUE"));
            }
        }
    }
    // to decide which action Should be performed...

    private String getCommand() {
        log.info("In getCommand()");
        String command = null;
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
            default:
        }
        return command;
    }

    private String getAction() {
        log.info("In getAction()");
        String action = null;
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
            default:
        }
        return action;
    }

    // Returns the Current Value of Action type...
    public int getActionType() {
        return actionType;
    }

    // Sets the value of the Action Type to the poperation we want to execute...
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }

    // To set and change the Status of the lable STATUS
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }

    public int getResult() {
        return this.result;
    }

    // To set the Value of the lblStatus...
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }

    public String getLblStatus() {
        return lblStatus;
    }

    //To reset the Value of lblStatus after each save action...
    public void resetStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }

    /**
     * To set the status based on ActionType, either New, Edit, etc.,
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }

    /**
     * To update the Status based on result performed by doAction() method
     */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }

    public void ttNotifyObservers() {
        setChanged();
        notifyObservers();
    }

    void resetForm() {
        setCboModuleType("");
        setTxtDescription("");
        setCboCalculationType("");
        setTxtAmount("");
        setChkActive(false);
        setChkExcludeFromTax(false);
        setChkPaymentVoucherRequired(false);
        setChkIndividualRequired(false);
        setChkIncludePersonalPay(false);
        setChkGl(false);
        setRdoEarnings(false);
        setRdoDeduction(false);
        setRdoContra(false);
        setCboProductType("");
        setCboAccountType("");
        setTxtAccountHead("");
        setCboCalcModType("");
        setTxtMaxAmt("");
        setTxtMinAmt("");
        setTxtPercentage("");
        setTdtFromDate(null);
        accRecordData = new ArrayList();
        calcRecordData = new ArrayList();
    }

    void resetOBFields() {
        this.setCboAccountType("");
        this.setCboProductType("");
        this.setTxtAccountHead("");
        this.tbmAccountDetails.setData(new ArrayList());
        this.tbmAccountDetails.fireTableDataChanged();
        this.accRecordData.clear();
        this.deleteAccData.clear();
        this.setCboModuleType("");
        this.setTxtMinAmt("");
        this.setTxtMaxAmt("");
        this.setTxtPercentage("");
        this.setChkIncludePersonalPay(false);
        this.tbmCalcDetails.setData(new ArrayList());
        this.tbmCalcDetails.fireTableDataChanged();
        this.calcRecordData.clear();
        this.deleteCalcData.clear();
        this.isFilled = false;
        this.chkGl = false;
    }

    public List getPayCode(HashMap dataMap) {
        final List payCode = ClientUtil.executeQuery("getPayModType", dataMap);
        return payCode;
    }

    public void setAccountTable() {
        ArrayList columnHeader = new ArrayList();
        columnHeader.add("Sl No.");
        columnHeader.add("Account Head");
        columnHeader.add("Account Type");
        ArrayList data = new ArrayList();
        tbmAccountDetails = new TableModel(data, columnHeader);
    }

    public void setCalcTable() {
        ArrayList columnHeader = new ArrayList();
        columnHeader.add("Sl No.");
        columnHeader.add("Paycode");
        columnHeader.add("Module Type");
        ArrayList data = new ArrayList();
        tbmCalcDetails = new TableModel(data, columnHeader);
    }

    public int insertIntoAccTableData(int rowNo) {
        objEarnDeduAccTO = (EarnDeduAccTO) setEarnDeduAccount();
        ArrayList row = new ArrayList();
        if (rowNo == -1) {
            if (objEarnDeduAccTO != null) {
                if (getAccSlNo() > 2) {
                    ClientUtil.showMessageWindow("Already entered two records!!!");
                } else if (accSlNo <= 2 && isCheck()) {
                    row.add(objEarnDeduAccTO);
                    accRecordData.add(objEarnDeduAccTO);
                    ArrayList irRow = this.setAccRow(objEarnDeduAccTO);
                    tbmAccountDetails.insertRow(tbmAccountDetails.getRowCount(), irRow);
                }
            }
        } else {
            objEarnDeduAccTO = updateAccTableDataTO((EarnDeduAccTO) accRecordData.get(rowNo), objEarnDeduAccTO);
            ArrayList irRow = setAccRow(objEarnDeduAccTO);
            accRecordData.set(rowNo, objEarnDeduAccTO);
            tbmAccountDetails.removeRow(rowNo);
            tbmAccountDetails.insertRow(rowNo, irRow);
        }
        tbmAccountDetails.fireTableDataChanged();
        ttNotifyObservers();
        return 0;
    }

    public EarnDeduAccTO setEarnDeduAccount() {
        EarnDeduAccTO objEarnDeduAccTO = new EarnDeduAccTO();
        objEarnDeduAccTO.setAccType(CommonUtil.convertObjToStr(cboAccountType));
        objEarnDeduAccTO.setAccHd(CommonUtil.convertObjToStr(txtAccountHead));
        return objEarnDeduAccTO;
    }

    private ArrayList setAccRow(EarnDeduAccTO objEarnDeduAccTO) {
        ArrayList row = new ArrayList();
        row.add(getAccSlNo());
        row.add(objEarnDeduAccTO.getAccHd());
        row.add(objEarnDeduAccTO.getAccType());
        return row;
    }

    private EarnDeduAccTO updateAccTableDataTO(EarnDeduAccTO oldEarnDeduAccTO, EarnDeduAccTO newEarnDeduAccTO) {
        oldEarnDeduAccTO.setAccType(newEarnDeduAccTO.getAccType());
        oldEarnDeduAccTO.setAccHd(newEarnDeduAccTO.getAccHd());
        return oldEarnDeduAccTO;
    }

    void deleteAccTblData(int rowSelected) {
        objEarnDeduAccTO = (EarnDeduAccTO) accRecordData.get(rowSelected);
        deleteAccData.add(objEarnDeduAccTO);
        accRecordData.remove(rowSelected);
        tbmAccountDetails.removeRow(rowSelected);
        tbmAccountDetails.fireTableDataChanged();
    }

    void deleteCalcTblData(int rowSelected) {
        objEarningsDeductionTO = (EarningsDeductionTO) calcRecordData.get(rowSelected);
        deleteCalcData.add(objEarningsDeductionTO);
        calcRecordData.remove(rowSelected);
        tbmCalcDetails.removeRow(rowSelected);
        tbmCalcDetails.fireTableDataChanged();
    }

    public int insertIntoCalcTableData(int rowNo) {
        objEarningsDeductionTO = (EarningsDeductionTO) setEarnDeduCalculation();
        ArrayList row = new ArrayList();
        if (rowNo == -1) {
            if (objEarningsDeductionTO != null) {
                if (isCheck()) {
                    row.add(objEarningsDeductionTO);
                    calcRecordData.add(objEarningsDeductionTO);
                    ArrayList irRow = this.setCalcRow(objEarningsDeductionTO);
                    tbmCalcDetails.insertRow(tbmCalcDetails.getRowCount(), irRow);
                } else {
                    ClientUtil.showMessageWindow("Please select another Module Type!!!");
                }
            }
        } else {
            objEarningsDeductionTO = updateCalcTableDataTO((EarningsDeductionTO) calcRecordData.get(rowNo), objEarningsDeductionTO);
            ArrayList irRow = setCalcRow(objEarningsDeductionTO);
            calcRecordData.set(rowNo, objEarningsDeductionTO);
            tbmCalcDetails.removeRow(rowNo);
            tbmCalcDetails.insertRow(rowNo, irRow);
        }
        tbmCalcDetails.fireTableDataChanged();
        ttNotifyObservers();
        return 0;
    }

    private ArrayList setCalcRow(EarningsDeductionTO objEarningsDeductionTO) {
        ArrayList row = new ArrayList();
        if (getActionType() == ClientConstants.ACTIONTYPE_VIEW || getActionType() == ClientConstants.ACTIONTYPE_DELETE || getActionType() == ClientConstants.ACTIONTYPE_EDIT && !isIsFilled()) {
            row.add(getCalcSlNo());
            row.add(getTblPayCode());
            row.add(getTblModType());
        } else {
            row.add(getCalcSlNo());
            row.add(objEarningsDeductionTO.getPay_Code());
            row.add(objEarningsDeductionTO.getPay_Mod_Type());
        }
        return row;
    }

    private EarningsDeductionTO updateCalcTableDataTO(EarningsDeductionTO oldEarnDeduCalcTO, EarningsDeductionTO newEarnDeduCalcTO) {
        oldEarnDeduCalcTO.setPay_Code(newEarnDeduCalcTO.getPay_Code());
        oldEarnDeduCalcTO.setPay_Mod_Type(newEarnDeduCalcTO.getPay_Mod_Type());
        return oldEarnDeduCalcTO;
    }

    public EarningsDeductionTO setEarnDeduCalculation() {
        EarningsDeductionTO objEarnDeduCalcTO = new EarningsDeductionTO();
        objEarnDeduCalcTO.setPay_Mod_Type(CommonUtil.convertObjToStr(cboCalcModType));
        objEarnDeduCalcTO.setPay_Code(CommonUtil.convertObjToStr(getCbmCalcModType().getKeyForSelected()));
        objEarnDeduCalcTO.setPay_Min_Amt(CommonUtil.convertObjToDouble(txtMinAmt));
        objEarnDeduCalcTO.setPay_Max_Amt(CommonUtil.convertObjToDouble(txtMaxAmt));
        objEarnDeduCalcTO.setPay_Percent(CommonUtil.convertObjToDouble(txtPercentage));
        if (chkIncludePersonalPay == true) {
            objEarnDeduCalcTO.setPersonal_Pay("Y");
        } else {
            objEarnDeduCalcTO.setPersonal_Pay("N");
        }
        return objEarnDeduCalcTO;
    }

    private EarningsDeductionTO setEarningsDeduction() {
        log.info("In setEarningsDeduction()");
        final EarningsDeductionTO objEarnDeduTO = new EarningsDeductionTO();
        objEarnDeduTO.setPay_Code(getPayCode());
        objEarnDeduTO.setPaycode_Id(getPayCodeId());
        objEarnDeduTO.setPay_Mod_Type(CommonUtil.convertObjToStr(cboModuleType));
        objEarnDeduTO.setPay_Descri(txtDescription);
        objEarnDeduTO.setPay_Calc_Type(CommonUtil.convertObjToStr(cboCalculationType));
        objEarnDeduTO.setPay_Fix_Amt(CommonUtil.convertObjToDouble(txtAmount));
        objEarnDeduTO.setPay_Min_Amt(CommonUtil.convertObjToDouble(txtMinAmt));
        objEarnDeduTO.setPay_Max_Amt(CommonUtil.convertObjToDouble(txtMaxAmt));
        objEarnDeduTO.setPay_Percent(CommonUtil.convertObjToDouble(txtPercentage));
        objEarnDeduTO.setPay_Calc_On(getModType());
        objEarnDeduTO.setPay_Prod_Type(getCboProductType());
        objEarnDeduTO.setStatusBy(TrueTransactMain.USER_ID);
        objEarnDeduTO.setCreatedBy(TrueTransactMain.USER_ID);
        objEarnDeduTO.setCreatedDate(curDate);
        objEarnDeduTO.setStatusDate(curDate);
        if (isRdoEarnings()) {
            objEarnDeduTO.setPay_EarnDedu("EARNINGS");
        } else if (isRdoDeduction()) {
            objEarnDeduTO.setPay_EarnDedu("DEDUCTIONS");
        } else if (isRdoContra()) {
            objEarnDeduTO.setPay_EarnDedu("CONTRA");
        }
        if (isChkActive()) {
            objEarnDeduTO.setActive("Y");
        } else {
            objEarnDeduTO.setActive("N");
        }
        if (isChkExcludeFromTax()) {
            objEarnDeduTO.setTaxable("Y");
        } else {
            objEarnDeduTO.setTaxable("N");
        }
        if (isChkPaymentVoucherRequired()) {
            objEarnDeduTO.setPayment_Voucher("Y");
        } else {
            objEarnDeduTO.setPayment_Voucher("N");
        }
        if (isChkIndividualRequired()) {
            objEarnDeduTO.setIndividual_reqd("Y");
        } else {
            objEarnDeduTO.setIndividual_reqd("N");
        }
        if (isChkIncludePersonalPay()) {
            objEarnDeduTO.setPersonal_Pay("Y");
        } else {
            objEarnDeduTO.setPersonal_Pay("N");
        }
        if (isChkOnlyForContra()) {
            objEarnDeduTO.setContra_Only("Y");
        } else {
            objEarnDeduTO.setContra_Only("N");
        }
        return objEarnDeduTO;
    }

    private EarnDeduAccTO setEarningsDeductionAccount() {
        log.info("In setEarningsDeductionAccount()");
        final EarnDeduAccTO objEarnDeduAccTO = new EarnDeduAccTO();
        objEarnDeduAccTO.setPayCode_Id(getPayCodeId());
        objEarnDeduAccTO.setAccHd(txtAccountHead);
        objEarnDeduAccTO.setAccType(CommonUtil.convertObjToStr(cboAccountType));
        return objEarnDeduAccTO;
    }

    private EarnDeduPayTO setEarningsDeductionPay() {
        log.info("In setEarningsDeductionPay()");
        final EarnDeduPayTO objEarnDeduPayTO = new EarnDeduPayTO();
        objEarnDeduPayTO.setProdType("");
        objEarnDeduPayTO.setProdId("");
        objEarnDeduPayTO.setAccNo("");
        objEarnDeduPayTO.setActive("");
        objEarnDeduPayTO.setCalcUpto(null);
        objEarnDeduPayTO.setStatus("CREATED");
        objEarnDeduPayTO.setStatusBy(TrueTransactMain.USER_ID);
        objEarnDeduPayTO.setStatusDate(curDate);
        objEarnDeduPayTO.setCreatedBy(TrueTransactMain.USER_ID);
        objEarnDeduPayTO.setCreatedDate(curDate);
        objEarnDeduPayTO.setFromDate(null);
        objEarnDeduPayTO.setToDate(null);
        objEarnDeduPayTO.setInterest(0.0);
        objEarnDeduPayTO.setPenalInterest(0.0);
        objEarnDeduPayTO.setPrincipal(0.0);
        objEarnDeduPayTO.setRemark("");
        if (isChkActive()) {
            objEarnDeduPayTO.setActive("Y");
        } else {
            objEarnDeduPayTO.setActive("N");
        }
        return objEarnDeduPayTO;
    }

    void populateCalc(HashMap hash) {
        final List payCode;
        HashMap data = new HashMap();
        data.put("MODTYPE", hash.get("PAY_MODULE_TYPE"));
        payCode = ClientUtil.executeQuery("getPayCodeId", data);
    }

    private void populateAccTblData(List lstData) {
        accRecordData = new ArrayList();
        int size = lstData.size();
        setAccountTable();
        for (int i = 0; i < size; i++) {
            EarnDeduAccTO objEarnDeduAccTO = new EarnDeduAccTO();
            HashMap newMap = new HashMap();
            newMap = (HashMap) lstData.get(i);
            if (newMap != null && newMap.size() > 0) {
                setAccSlNo(i + 1);
                objEarnDeduAccTO.setAccHd(CommonUtil.convertObjToStr(newMap.get("ACC_HD")));
                objEarnDeduAccTO.setAccType(CommonUtil.convertObjToStr(newMap.get("ACC_TYPE")));
                accRecordData.add(objEarnDeduAccTO);
                ArrayList irRow = this.setAccRow(objEarnDeduAccTO);
                tbmAccountDetails.insertRow(tbmAccountDetails.getRowCount(), irRow);
            }
        }
        tbmAccountDetails.fireTableDataChanged();
    }

    private void populateCalcTblData(List calcData) {
        calcRecordData = new ArrayList();
        int size = calcData.size();
        setCalcTable();
        int calSl = 1;
        for (int i = 0; i < size; i++) {
            EarningsDeductionTO objEarnDeduCalcTO = new EarningsDeductionTO();
            HashMap newMap = new HashMap();
            newMap = (HashMap) calcData.get(i);
            objEarnDeduCalcTO.setPay_Min_Amt(CommonUtil.convertObjToDouble(newMap.get("PAY_MIN_AMT")));
            objEarnDeduCalcTO.setPay_Max_Amt(CommonUtil.convertObjToDouble(newMap.get("PAY_MAX_AMT")));
            objEarnDeduCalcTO.setPay_Percent(CommonUtil.convertObjToDouble(newMap.get("PAY_PERCENT")));
            objEarnDeduCalcTO.setPay_Calc_On(CommonUtil.convertObjToStr(newMap.get("PAY_CALC_ON")));
            String mType = CommonUtil.convertObjToStr(newMap.get("PAY_CALC_ON"));
            String[] pay_calc_on = mType.split(Pattern.quote("+"));
            for (String s : pay_calc_on) {
                setCalcSlNo(calSl);
                objEarnDeduCalcTO.setPay_Calc_On(s);
                setTblPayCode(s);
                HashMap modMap = new HashMap();
                modMap.put("PAYCODE", s);
                String payMod = getPayModule(modMap);
                setTblModType(payMod);
                setCboCalcModType(getTblModType());
                calcRecordData.add(objEarnDeduCalcTO);
                ArrayList irRow = this.setCalcRow(objEarnDeduCalcTO);
                tbmCalcDetails.insertRow(tbmCalcDetails.getRowCount(), irRow);
                calSl++;
            }
        }
        tbmCalcDetails.fireTableDataChanged();
    }

    public void populateAccTableData(int rowNum) {
        objEarnDeduAccTO = (EarnDeduAccTO) accRecordData.get(rowNum);
        this.setAccTableValues(objEarnDeduAccTO);
        ttNotifyObservers();
    }

    private void setAccTableValues(EarnDeduAccTO objEarnDeduAccTO) {
        setTxtAccountHead(CommonUtil.convertObjToStr(objEarnDeduAccTO.getAccHd()));
        setCboAccountType(CommonUtil.convertObjToStr(objEarnDeduAccTO.getAccType()));
    }

    void populateCalcTableData(int rowNum) {
        if (rowNum >= 0) {
            objEarningsDeductionTO = (EarningsDeductionTO) calcRecordData.get(rowNum);
            this.setCalcTableValues(objEarningsDeductionTO);
            ttNotifyObservers();
        }
    }

    private void setCalcTableValues(EarningsDeductionTO objEarningsDeductionTO) {
        setCboCalcModType(CommonUtil.convertObjToStr(getTblModType()));
        setTxtMinAmt(CommonUtil.convertObjToStr(objEarningsDeductionTO.getPay_Min_Amt()));
        setTxtMaxAmt(CommonUtil.convertObjToStr(objEarningsDeductionTO.getPay_Max_Amt()));
        setTxtPercentage(CommonUtil.convertObjToStr(objEarningsDeductionTO.getPay_Percent()));
    }

    private EarnDeduPaySettingsTO setEarningsDeductionSettings() {
        final EarnDeduPaySettingsTO objEarnDeduPaySettingsTO = new EarnDeduPaySettingsTO();
        objEarnDeduPaySettingsTO.setPaycode_Id(getPayCodeId());
        objEarnDeduPaySettingsTO.setPayCalcOn(getModType());
        objEarnDeduPaySettingsTO.setFromDate(DateUtil.getDateMMDDYYYY(tdtFromDate));
        objEarnDeduPaySettingsTO.setToDate(null);
        objEarnDeduPaySettingsTO.setPayFixAmt(CommonUtil.convertObjToDouble(txtMinAmt));
        objEarnDeduPaySettingsTO.setPayMaxAmt(CommonUtil.convertObjToDouble(txtMaxAmt));
        objEarnDeduPaySettingsTO.setPayMinAmt(CommonUtil.convertObjToDouble(txtMinAmt));
        objEarnDeduPaySettingsTO.setPayPercent(CommonUtil.convertObjToDouble(txtPercentage));
        return objEarnDeduPaySettingsTO;
    }
}
