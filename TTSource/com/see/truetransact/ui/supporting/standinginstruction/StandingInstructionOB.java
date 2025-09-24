/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * StandingInstructionOB.java
 *
 * Created on February 2, 2004, 8:14 PM
 */

package com.see.truetransact.ui.supporting.standinginstruction;

/**
 *
 * @author  Hemant
 */

import java.util.HashMap;
import java.util.Observable;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.transferobject.supporting.standinginstruction.*;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.uicomponent.CTable;

import java.util.Date;

public class StandingInstructionOB extends CObservable {
    
    Date curDate = null;
    private final String DAY_END = "DAYEND" ;
    private final String DAY_BEGIN = "DAY_BEGIN" ;
    private String txtSINo = "";
    private String cboSIType = "";
    private String txtMultiplieSI = "";
    private String txtBeneficiarySI = "";
    private String txtMinBalSI = "";
    private String txtGraceDaysSI = "";
    private String txtCommChargesSI = "";
    private String txtRettCommChargesSI = "";
    private String cboFrequencySI = "";
    private String cboMoRSI = "";
    private String dtdEndDtSI = "";
    private String dtdSuspendDt = "";
    private String dtdStartDtSI = "";
    private String txtSiDt = "";
    private boolean rdoCommSI_Yes = false;
    private boolean rdoCommSI_No = false;
    private boolean rdoRettCommSI_Yes = false;
    private boolean rdoRettCommSI_No = false;
    private String txtParticularsDSI = "";
    private String txtAccNoDSI = "";
    private String txtAmountDSI = "";
    private String cboProdIDDSI = "";
    private String txtParticularsCSI = "";
    private String txtAccNoCSI = "";
    private String txtAmountCSI = "";
    private String cboProdIDCSI = "";
    private String txtAccHeadValueDSI = "";
    private String txtAccHeadValueCSI = "";
    private String lblNameValueCSI = "";
    private String lblNameValueDSI = "";
    private String cboWeekDay = "";
    private String cboWeek = "";
    private String cboSpecificDate = "";
    private String cboProductTypeCSI = "";
    private String cboProductTypeDSI = "";
    private String cboExecutionDay = "";
    private boolean rdoHolidayExecution_Yes = false;
    private boolean rdoHolidayExecution_No = false;
    private boolean rdoSIAutoPosting_Yes = false;
    private boolean rdoSIAutoPosting_No = false;
    private String txtForwardCount = "";
    private String cboExecConfig = "";
    private String txtAcceptanceCharges = "";
    private String txtExecutionCharges = "";
    private String txtFailureST = "";
    private String txtServiceTax = "";
    private String txtFailureCharges = "";
    private String lbl1NextExecutonDate="";
    private String lbl1LastExecutonDate="";
    private String lbl1HolidayExecutonDate="";
    private String lbl1FwdExecutionDate = "";
    private boolean chkSuspendUser = false;
    private boolean chkCloseSI = false;
    private String suspended="";
    private String close = "";
    private Date nxtDt = null;
    private Date suspDt = null;
    private Date lstDt = null;
    private Date endDt = null;
    private String freq = "";
    private Date checkThisCDate=null;
    private ArrayList crdActLst = new ArrayList();
   
 
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private StandingInstructionRB resourceBundle = new StandingInstructionRB();
    private ComboBoxModel cbmSIType;
    private ComboBoxModel cbmProdIDDSI;
    private ComboBoxModel cbmProdIDCSI;
    private ComboBoxModel cbmFrequencySI;
    private ComboBoxModel cbmMoRSI;
    private ComboBoxModel cbmWeekDay;
    private ComboBoxModel cbmWeek;
    private ComboBoxModel cbmSpecificDate;
    private ComboBoxModel cbmExecConfig;
    private ComboBoxModel cbmProductTypeCSI;
    private ComboBoxModel cbmProductTypeDSI;
    private ComboBoxModel cbmExecutionDay;
    
    
    private EnhancedTableModel tbmDebitSI;
    private EnhancedTableModel tbmCreditSI;
    
    private List listDebitSI;
    private List listCreditSI;
    
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private HashMap lookUpHash;
    private HashMap map;
    private ProxyFactory proxy;
    private ArrayList columnList;
    private int actionType;
    private int result;
    public boolean debitAcNo = false;
    public boolean creditAcNo = false;
    
    private CTable _tblData;
    private ArrayList _heading;
    private HashMap dataHash;
    private ArrayList data;
    private boolean _isAvailable = true;
    private String dbranchid="";
    private String CbranchID="";
    private boolean chkIntalmentsYN =false;
    private String noOfInst ;
    private boolean chkPendingInstalment = false;

    public boolean isChkPendingInstalment() {
        return chkPendingInstalment;
    }

    public void setChkPendingInstalment(boolean chkPendingInstalment) {
        this.chkPendingInstalment = chkPendingInstalment;
    }
    
    public boolean isChkIntalmentsYN() {
        return chkIntalmentsYN;
    }

    public void setChkIntalmentsYN(boolean chkIntalmentsYN) {
        this.chkIntalmentsYN = chkIntalmentsYN;
    }

    public String getNoOfInst() {
        return noOfInst;
    }

    public void setNoOfInst(String noOfInst) {
        this.noOfInst = noOfInst;
    }
    

    public String getCbranchID() {
        return CbranchID;
    }

    public void setCbranchID(String CbranchID) {
        this.CbranchID = CbranchID;
    }

    public String getDbranchid() {
        return dbranchid;
    }

    public void setDbranchid(String dbranchid) {
        this.dbranchid = dbranchid;
    }
       
    private final static com.see.truetransact.clientexception.ClientParseException parseException = com.see.truetransact.clientexception.ClientParseException.getInstance();
    
    private static StandingInstructionOB standingInstructionOB;
    static {
        try {
            //log.info("Creating AccountCreationOB...");
            standingInstructionOB = new StandingInstructionOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public StandingInstructionOB()throws Exception{
        curDate = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "StandingInstructionJNDI");
        map.put(CommonConstants.HOME, "supporting.standinginstruction.StandingInstructionHome");
        map.put(CommonConstants.REMOTE, "supporting.standinginstruction.StandingInstruction");
        /**/
        
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
        initUIComboBoxModel();
        fillDropdown();
        createColumnList();
        createTableModel();
        //setTableModel();
    }
    
    private void createColumnList(){
        columnList = new ArrayList();
        columnList.add(resourceBundle.getString("tblColounHeading1"));
        columnList.add(resourceBundle.getString("tblColounHeading2"));
        columnList.add(resourceBundle.getString("tblColounHeading3"));
        columnList.add(resourceBundle.getString("tblColounHeading4"));
        columnList.add(resourceBundle.getString("tblColounHeading5"));
    }
    
    private void createTableModel() throws Exception{
        tbmDebitSI = new EnhancedTableModel(null, columnList);
        tbmCreditSI = new EnhancedTableModel(null, columnList);
    }
    
    //    private void setTableModel(){
    //        StandingInstructionRB siRB = new StandingInstructionRB();
    //        tbmDebitSI = new EnhancedTableModel(
    //        new Object [][] {
    //
    //        },
    //        new String [] {
    //            siRB.getString("tblColounHeading1"), siRB.getString("tblColounHeading2"), siRB.getString("tblColounHeading3"), siRB.getString("tblColounHeading4"), siRB.getString("tblColounHeading5")
    //        }
    //        ) {
    //            Class[] types = new Class [] {
    //                java.lang.String.class, java.lang.String.class, java.lang.String.class
    //            };
    //            boolean[] canEdit = new boolean [] {
    //                false, false, false
    //            };
    //
    //            public Class getColumnClass(int columnIndex) {
    //                return types [columnIndex];
    //            }
    //
    //            public boolean isCellEditable(int rowIndex, int columnIndex) {
    //                return canEdit [columnIndex];
    //            }
    //        };
    //
    //        tbmCreditSI = new EnhancedTableModel(
    //        new Object [][] {
    //        },
    //        new String [] {
    //            siRB.getString("tblColounHeading1"), siRB.getString("tblColounHeading2"), siRB.getString("tblColounHeading3"), siRB.getString("tblColounHeading4"), siRB.getString("tblColounHeading5")
    //        }
    //        ) {
    //            Class[] types = new Class [] {
    //                java.lang.String.class, java.lang.String.class, java.lang.String.class
    //            };
    //            boolean[] canEdit = new boolean [] {
    //                false, false, false
    //            };
    //
    //            public Class getColumnClass(int columnIndex) {
    //                return types [columnIndex];
    //            }
    //
    //            public boolean isCellEditable(int rowIndex, int columnIndex) {
    //                return canEdit [columnIndex];
    //            }
    //        };
    //
    //    }
    public void initUIComboBoxModel(){
        cbmSIType = new ComboBoxModel();
        cbmProdIDDSI = new ComboBoxModel();
        cbmProdIDCSI = new ComboBoxModel();
        cbmFrequencySI = new ComboBoxModel();
        cbmMoRSI = new ComboBoxModel();
        cbmWeekDay = new ComboBoxModel();
        cbmWeek = new ComboBoxModel();
        cbmSpecificDate = new ComboBoxModel();
        cbmExecConfig = new ComboBoxModel();
        cbmExecutionDay = new ComboBoxModel();
    }
    
    
    public static StandingInstructionOB getInstance() {
        return standingInstructionOB;
    }
    void setTxtSINo(String txtSINo){
        this.txtSINo = txtSINo;
        setChanged();
    }
    String getTxtSINo(){
        return this.txtSINo;
    }
    
    void setCboSIType(String cboSIType){
        this.cboSIType = cboSIType;
        setChanged();
    }
    String getCboSIType(){
        return this.cboSIType;
    }
    
    void setTxtMultiplieSI(String txtMultiplieSI){
        this.txtMultiplieSI = txtMultiplieSI;
        setChanged();
    }
    String getTxtMultiplieSI(){
        return this.txtMultiplieSI;
    }
    
    void setTxtBeneficiarySI(String txtBeneficiarySI){
        this.txtBeneficiarySI = txtBeneficiarySI;
        setChanged();
    }
    String getTxtBeneficiarySI(){
        return this.txtBeneficiarySI;
    }
    
    void setTxtMinBalSI(String txtMinBalSI){
        this.txtMinBalSI = txtMinBalSI;
        setChanged();
    }
    String getTxtMinBalSI(){
        return this.txtMinBalSI;
    }
    
    void setTxtGraceDaysSI(String txtGraceDaysSI){
        this.txtGraceDaysSI = txtGraceDaysSI;
        setChanged();
    }
    String getTxtGraceDaysSI(){
        return this.txtGraceDaysSI;
    }
    
    void setTxtCommChargesSI(String txtCommChargesSI){
        this.txtCommChargesSI = txtCommChargesSI;
        setChanged();
    }
    String getTxtCommChargesSI(){
        return this.txtCommChargesSI;
    }
    
    void setTxtRettCommChargesSI(String txtRettCommChargesSI){
        this.txtRettCommChargesSI = txtRettCommChargesSI;
        setChanged();
    }
    String getTxtRettCommChargesSI(){
        return this.txtRettCommChargesSI;
    }
    
    void setCboFrequencySI(String cboFrequencySI){
        this.cboFrequencySI = cboFrequencySI;
        setChanged();
    }
    String getCboFrequencySI(){
        return this.cboFrequencySI;
    }
    
    void setCboMoRSI(String cboMoRSI){
        this.cboMoRSI = cboMoRSI;
        setChanged();
    }
    String getCboMoRSI(){
        return this.cboMoRSI;
    }
    
    void setDtdEndDtSI(String dtdEndDtSI){
        this.dtdEndDtSI = dtdEndDtSI;
        setChanged();
    }
    String getDtdEndDtSI(){
        return this.dtdEndDtSI;
    }
    
    void setDtdStartDtSI(String dtdStartDtSI){
        this.dtdStartDtSI = dtdStartDtSI;
        setChanged();
    }
    String getDtdStartDtSI(){
        return this.dtdStartDtSI;
    }
    
    void setRdoCommSI_Yes(boolean rdoCommSI_Yes){
        this.rdoCommSI_Yes = rdoCommSI_Yes;
        setChanged();
    }
    boolean getRdoCommSI_Yes(){
        return this.rdoCommSI_Yes;
    }
    
    void setRdoCommSI_No(boolean rdoCommSI_No){
        this.rdoCommSI_No = rdoCommSI_No;
        setChanged();
    }
    boolean getRdoCommSI_No(){
        return this.rdoCommSI_No;
    }
    
    void setRdoRettCommSI_Yes(boolean rdoRettCommSI_Yes){
        this.rdoRettCommSI_Yes = rdoRettCommSI_Yes;
        setChanged();
    }
    boolean getRdoRettCommSI_Yes(){
        return this.rdoRettCommSI_Yes;
    }
    
    void setRdoRettCommSI_No(boolean rdoRettCommSI_No){
        this.rdoRettCommSI_No = rdoRettCommSI_No;
        setChanged();
    }
    boolean getRdoRettCommSI_No(){
        return this.rdoRettCommSI_No;
    }
    
    void setTxtParticularsDSI(String txtParticularsDSI){
        this.txtParticularsDSI = txtParticularsDSI;
        setChanged();
    }
    String getTxtParticularsDSI(){
        return this.txtParticularsDSI;
    }
    
    void setTxtAccNoDSI(String txtAccNoDSI){
        this.txtAccNoDSI = txtAccNoDSI;
        setChanged();
    }
    String getTxtAccNoDSI(){
        return this.txtAccNoDSI;
    }
    
    void setTxtAmountDSI(String txtAmountDSI){
        this.txtAmountDSI = txtAmountDSI;
        setChanged();
    }
    String getTxtAmountDSI(){
        return this.txtAmountDSI;
    }
    
    void setCboProdIDDSI(String cboProdIDDSI){
        this.cboProdIDDSI = cboProdIDDSI;
        setChanged();
    }
    String getCboProdIDDSI(){
        return this.cboProdIDDSI;
    }
    
    void setTxtParticularsCSI(String txtParticularsCSI){
        this.txtParticularsCSI = txtParticularsCSI;
        setChanged();
    }
    String getTxtParticularsCSI(){
        return this.txtParticularsCSI;
    }
    
    void setTxtAccNoCSI(String txtAccNoCSI){
        this.txtAccNoCSI = txtAccNoCSI;
        setChanged();
    }
    String getTxtAccNoCSI(){
        return this.txtAccNoCSI;
    }
    
    void setTxtAmountCSI(String txtAmountCSI){
        this.txtAmountCSI = txtAmountCSI;
        setChanged();
    }
    String getTxtAmountCSI(){
        return this.txtAmountCSI;
    }
    
    void setCboProdIDCSI(String cboProdIDCSI){
        this.cboProdIDCSI = cboProdIDCSI;
        setChanged();
    }
    String getCboProdIDCSI(){
        return this.cboProdIDCSI;
    }
    /**
     * Getter for property cbmWeekDay.
     * @return Value of property cbmWeekDay.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmWeekDay() {
        return cbmWeekDay;
    }
    
    /**
     * Setter for property cbmWeekDay.
     * @param cbmWeekDay New value of property cbmWeekDay.
     */
    public void setCbmWeekDay(com.see.truetransact.clientutil.ComboBoxModel cbmWeekDay) {
        this.cbmWeekDay = cbmWeekDay;
    }
    
    /**
     * Getter for property cbmWeek.
     * @return Value of property cbmWeek.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmWeek() {
        return cbmWeek;
    }
    
    /**
     * Setter for property cbmWeek.
     * @param cbmWeek New value of property cbmWeek.
     */
    public void setCbmWeek(com.see.truetransact.clientutil.ComboBoxModel cbmWeek) {
        this.cbmWeek = cbmWeek;
    }
    
    /**
     * Getter for property cbmSpecificDate.
     * @return Value of property cbmSpecificDate.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSpecificDate() {
        return cbmSpecificDate;
    }
    
    /**
     * Setter for property cbmSpecificDate.
     * @param cbmSpecificDate New value of property cbmSpecificDate.
     */
    public void setCbmSpecificDate(com.see.truetransact.clientutil.ComboBoxModel cbmSpecificDate) {
        this.cbmSpecificDate = cbmSpecificDate;
    }
    
    /** Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     *
     */
    /**
     * Getter for property cboWeekDay.
     * @return Value of property cboWeekDay.
     */
    public java.lang.String getCboWeekDay() {
        return cboWeekDay;
    }
    
    /**
     * Setter for property cboWeekDay.
     * @param cboWeekDay New value of property cboWeekDay.
     */
    public void setCboWeekDay(java.lang.String cboWeekDay) {
        this.cboWeekDay = cboWeekDay;
        setChanged();
    }
    
    /**
     * Getter for property cboWeek.
     * @return Value of property cboWeek.
     */
    public java.lang.String getCboWeek() {
        return cboWeek;
    }
    
    /**
     * Setter for property cboWeek.
     * @param cboWeek New value of property cboWeek.
     */
    public void setCboWeek(java.lang.String cboWeek) {
        this.cboWeek = cboWeek;
        setChanged();
    }
    
    /**
     * Getter for property cboSpecificDate.
     * @return Value of property cboSpecificDate.
     */
    public java.lang.String getCboSpecificDate() {
        return cboSpecificDate;
    }
    
    /**
     * Setter for property cboSpecificDate.
     * @param cboSpecificDate New value of property cboSpecificDate.
     */
    public void setCboSpecificDate(java.lang.String cboSpecificDate) {
        this.cboSpecificDate = cboSpecificDate;
        setChanged();
    }
    
    /**
     * Getter for property rdoHolidayExecution_Yes.
     * @return Value of property rdoHolidayExecution_Yes.
     */
    public boolean getRdoHolidayExecution_Yes() {
        return rdoHolidayExecution_Yes;
    }
    
    /**
     * Setter for property rdoHolidayExecution_Yes.
     * @param rdoHolidayExecution_Yes New value of property rdoHolidayExecution_Yes.
     */
    public void setRdoHolidayExecution_Yes(boolean rdoHolidayExecution_Yes) {
        this.rdoHolidayExecution_Yes = rdoHolidayExecution_Yes;
        setChanged();
    }
    
    /**
     * Getter for property rdoHolidayExecution_No.
     * @return Value of property rdoHolidayExecution_No.
     */
    public boolean getRdoHolidayExecution_No() {
        return rdoHolidayExecution_No;
    }
    
    /**
     * Setter for property rdoHolidayExecution_No.
     * @param rdoHolidayExecution_No New value of property rdoHolidayExecution_No.
     */
    public void setRdoHolidayExecution_No(boolean rdoHolidayExecution_No) {
        this.rdoHolidayExecution_No = rdoHolidayExecution_No;
        setChanged();
    }
    
    /**
     * Getter for property rdoSIAutoPosting_Yes.
     * @return Value of property rdoSIAutoPosting_Yes.
     */
    public boolean getRdoSIAutoPosting_Yes() {
        return rdoSIAutoPosting_Yes;
    }
    
    /**
     * Setter for property rdoSIAutoPosting_Yes.
     * @param rdoSIAutoPosting_Yes New value of property rdoSIAutoPosting_Yes.
     */
    public void setRdoSIAutoPosting_Yes(boolean rdoSIAutoPosting_Yes) {
        this.rdoSIAutoPosting_Yes = rdoSIAutoPosting_Yes;
        setChanged();
    }
    
    /**
     * Getter for property rdoSIAutoPosting_No.
     * @return Value of property rdoSIAutoPosting_No.
     */
    public boolean getRdoSIAutoPosting_No() {
        return rdoSIAutoPosting_No;
    }
    
    /**
     * Setter for property rdoSIAutoPosting_No.
     * @param rdoSIAutoPosting_No New value of property rdoSIAutoPosting_No.
     */
    public void setRdoSIAutoPosting_No(boolean rdoSIAutoPosting_No) {
        this.rdoSIAutoPosting_No = rdoSIAutoPosting_No;
        setChanged();
    }
    
    /**
     * Getter for property txtForwardCount.
     * @return Value of property txtForwardCount.
     */
    public java.lang.String getTxtForwardCount() {
        return txtForwardCount;
    }
    
    /**
     * Setter for property txtForwardCount.
     * @param txtForwardCount New value of property txtForwardCount.
     */
    public void setTxtForwardCount(java.lang.String txtForwardCount) {
        this.txtForwardCount = txtForwardCount;
        setChanged();
    }
    
    /**
     * Getter for property cboExecConfig.
     * @return Value of property cboExecConfig.
     */
    public java.lang.String getCboExecConfig() {
        return cboExecConfig;
    }
    
    /**
     * Setter for property cboExecConfig.
     * @param cboExecConfig New value of property cboExecConfig.
     */
    public void setCboExecConfig(java.lang.String cboExecConfig) {
        this.cboExecConfig = cboExecConfig;
        setChanged();
    }
    
    /**
     * Getter for property txtAcceptanceCharges.
     * @return Value of property txtAcceptanceCharges.
     */
    public java.lang.String getTxtAcceptanceCharges() {
        return txtAcceptanceCharges;
    }
    
    /**
     * Setter for property txtAcceptanceCharges.
     * @param txtAcceptanceCharges New value of property txtAcceptanceCharges.
     */
    public void setTxtAcceptanceCharges(java.lang.String txtAcceptanceCharges) {
        this.txtAcceptanceCharges = txtAcceptanceCharges;
        setChanged();
    }
    
    /**
     * Getter for property txtExecutionCharges.
     * @return Value of property txtExecutionCharges.
     */
    public java.lang.String getTxtExecutionCharges() {
        return txtExecutionCharges;
    }
    
    /**
     * Setter for property txtExecutionCharges.
     * @param txtExecutionCharges New value of property txtExecutionCharges.
     */
    public void setTxtExecutionCharges(java.lang.String txtExecutionCharges) {
        this.txtExecutionCharges = txtExecutionCharges;
        setChanged();
    }
    
    /**
     * Getter for property txtFailureCharges.
     * @return Value of property txtFailureCharges.
     */
    public java.lang.String getTxtFailureCharges() {
        return txtFailureCharges;
    }
    
    /**
     * Setter for property txtFailureCharges.
     * @param txtFailureCharges New value of property txtFailureCharges.
     */
    public void setTxtFailureCharges(java.lang.String txtFailureCharges) {
        this.txtFailureCharges = txtFailureCharges;
        setChanged();
    }
    
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    public String getLblStatus(){
        return lblStatus;
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        notifyObservers();
    }
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        notifyObservers();
    }
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    public int getResult(){
        return result;
    }
    public void setActionType(int action) {
        this.actionType = action;
        setChanged();
    }
    
    public int getActionType(){
        return this.actionType;
    }
    
    private void fillDropdown() throws Exception{
        try{
            //log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,null);
            final ArrayList lookup_keys = new ArrayList();
            
            lookup_keys.add("DEPOSITSPRODUCT.INT_CALC_METHOD");
            lookup_keys.add("STANDINGINST.REMIT_MODE");
            lookup_keys.add("SUPPORTING.STANDINGINSTRUCTIONTYPE");
            lookup_keys.add("SI.WEEK_DAYS");
            lookup_keys.add("SI.WEEK");
            lookup_keys.add("SI.EXECUTION");
            lookup_keys.add("PRODUCTTYPE");
            lookup_keys.add("SI.HOLIDAYEXECUTION");
            
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
            
            getKeyValue((HashMap)keyValue.get("DEPOSITSPRODUCT.INT_CALC_METHOD"));
            cbmFrequencySI = new ComboBoxModel(key,value);
            makeNull();
            
            getKeyValue((HashMap)keyValue.get("STANDINGINST.REMIT_MODE"));
            cbmMoRSI = new ComboBoxModel(key,value);
            makeNull();
            
            getKeyValue((HashMap)keyValue.get("SUPPORTING.STANDINGINSTRUCTIONTYPE"));
            cbmSIType = new ComboBoxModel(key,value);
            makeNull();
            
            getKeyValue((HashMap)keyValue.get("SI.WEEK_DAYS"));
            cbmWeekDay = new ComboBoxModel(key,value);
            makeNull();
            
            getKeyValue((HashMap)keyValue.get("SI.WEEK"));
            cbmWeek = new ComboBoxModel(key,value);
            makeNull();
            
            getKeyValue((HashMap)keyValue.get("SI.EXECUTION"));
            cbmExecConfig = new ComboBoxModel(key,value);
            makeNull();
            
            getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
            cbmProductTypeDSI = new ComboBoxModel(key,value);
            makeNull();
            
            getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
            key.add("RM");
            value.add("Remittance");
            key.add("MDS");
            value.add("Mds");
            cbmProductTypeCSI = new ComboBoxModel(key,value);
            makeNull();
            
            getKeyValue((HashMap)keyValue.get("SI.HOLIDAYEXECUTION"));
            cbmExecutionDay = new ComboBoxModel(key,value);
            makeNull();
            
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
            for(int i=1; i<=31; i++){
                key.add(String.valueOf(i));
                value.add(String.valueOf(i));
                cbmSpecificDate = new ComboBoxModel(key,value);
            }
            makeNull();
            
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,"getOpAccProductLookUp");
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            cbmProdIDDSI = new ComboBoxModel(key,value);
            cbmProdIDCSI = new ComboBoxModel(key,value);
            makeNull();
        }catch(NullPointerException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /** Getter for property cbmSIType.
     * @return Value of property cbmSIType.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSIType() {
        return cbmSIType;
    }
    
    /** Setter for property cbmSIType.
     * @param cbmSIType New value of property cbmSIType.
     *
     */
    public void setCbmSIType(com.see.truetransact.clientutil.ComboBoxModel cbmSIType) {
        this.cbmSIType = cbmSIType;
    }
    
    /** Getter for property cbmProdIDDSI.
     * @return Value of property cbmProdIDDSI.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdIDDSI() {
        return cbmProdIDDSI;
    }
    
    /** Setter for property cbmProdIDDSI.
     * @param cbmProdIDDSI New value of property cbmProdIDDSI.
     *
     */
    public void setCbmProdIDDSI(com.see.truetransact.clientutil.ComboBoxModel cbmProdIDDSI) {
        this.cbmProdIDDSI = cbmProdIDDSI;
    }
    
    /** Getter for property cbmProdIDCSI.
     * @return Value of property cbmProdIDCSI.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdIDCSI() {
        return cbmProdIDCSI;
    }
    
    /** Setter for property cbmProdIDCSI.
     * @param cbmProdIDCSI New value of property cbmProdIDCSI.
     *
     */
    public void setCbmProdIDCSI(com.see.truetransact.clientutil.ComboBoxModel cbmProdIDCSI) {
        this.cbmProdIDCSI = cbmProdIDCSI;
    }
    
    /** Getter for property cbmFrequencySI.
     * @return Value of property cbmFrequencySI.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmFrequencySI() {
        return cbmFrequencySI;
    }
    
    /** Setter for property cbmFrequencySI.
     * @param cbmFrequencySI New value of property cbmFrequencySI.
     *
     */
    public void setCbmFrequencySI(com.see.truetransact.clientutil.ComboBoxModel cbmFrequencySI) {
        this.cbmFrequencySI = cbmFrequencySI;
    }
    
    /** Getter for property cbmMoRSI.
     * @return Value of property cbmMoRSI.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmMoRSI() {
        return cbmMoRSI;
    }
    
    /** Setter for property cbmMoRSI.
     * @param cbmMoRSI New value of property cbmMoRSI.
     *
     */
    public void setCbmMoRSI(com.see.truetransact.clientutil.ComboBoxModel cbmMoRSI) {
        this.cbmMoRSI = cbmMoRSI;
    }
    
    /**
     * Getter for property cbmExecConfig.
     * @return Value of property cbmExecConfig.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmExecConfig() {
        return cbmExecConfig;
    }
    
    /**
     * Setter for property cbmExecConfig.
     * @param cbmExecConfig New value of property cbmExecConfig.
     */
    public void setCbmExecConfig(com.see.truetransact.clientutil.ComboBoxModel cbmExecConfig) {
        this.cbmExecConfig = cbmExecConfig;
    }
    
    private void makeNull(){
        key=null;
        value=null;
    }
    
    public String getAccHeadLabelCaption(String prodID,String prodType){
        String lblCaption = "";
            try{
            HashMap where = new HashMap();
            where.put("PROD_ID",prodID);
            List list = ClientUtil.executeQuery("getAccountHeadProd"+prodType,where);
            if(list!=null && list.size() != 0){
                lblCaption = (String)((HashMap)list.get(0)).get("AC_HEAD");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        //setLblAccHeadValueDSI(lblCaption);
        return lblCaption;
    }
    
    public StandingInstructionTO getStandingInstructionTO(String command){
        StandingInstructionTO objStandingInstructionTO = new StandingInstructionTO();
        objStandingInstructionTO.setCommand(command);
        if(objStandingInstructionTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)){
            objStandingInstructionTO.setStatus(CommonConstants.STATUS_DELETED);
        }else if(objStandingInstructionTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)){
            objStandingInstructionTO.setStatus(CommonConstants.STATUS_MODIFIED);
        } if(objStandingInstructionTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
            objStandingInstructionTO.setStatus(CommonConstants.STATUS_CREATED);
        }
        objStandingInstructionTO.setStatusBy(TrueTransactMain.USER_ID);
        objStandingInstructionTO.setStatusDt(curDate);
        objStandingInstructionTO.setSiId(getTxtSINo());
        Date SiDt = DateUtil.getDateMMDDYYYY(getTxtSiDt());
        if(SiDt != null){
        Date siDate = (Date)curDate.clone();
        siDate.setDate(SiDt.getDate());
        siDate.setMonth(SiDt.getMonth());
        siDate.setYear(SiDt.getYear());
//        objStandingInstructionTO.setSiDt(DateUtil.getDateMMDDYYYY(getTxtSiDt()));
        objStandingInstructionTO.setSiDt(siDate);
        }else{
            objStandingInstructionTO.setSiDt(DateUtil.getDateMMDDYYYY(getTxtSiDt()));
        }
        objStandingInstructionTO.setSiType(CommonUtil.convertObjToStr(cbmSIType.getKeyForSelected()));
        if(!(getTxtMultiplieSI().equals(""))){
            objStandingInstructionTO.setMultiplesOf(CommonUtil.convertObjToDouble(getTxtMultiplieSI()));
        }
        if(!(getTxtMinBalSI().equals(""))){
            objStandingInstructionTO.setMinBalance(CommonUtil.convertObjToDouble(getTxtMinBalSI()));
        }
        if(isChkIntalmentsYN())
            objStandingInstructionTO.setChkInstalment("Y");
        else{
          objStandingInstructionTO.setChkInstalment("N");  
        }
         if(isChkPendingInstalment())
            objStandingInstructionTO.setChkPendingInstalment("Y");
        else{
          objStandingInstructionTO.setChkPendingInstalment("N");  
        }
         objStandingInstructionTO.setNoOfInstalments(CommonUtil.convertObjToInt(getNoOfInst()));
        Date StDt = DateUtil.getDateMMDDYYYY(getDtdStartDtSI());
        if(StDt != null){
        Date stDate = (Date)curDate.clone();
        stDate.setDate(StDt.getDate());
        stDate.setMonth(StDt.getMonth());
        stDate.setYear(StDt.getYear());
//        objStandingInstructionTO.setSiStartDt(DateUtil.getDateMMDDYYYY(getDtdStartDtSI()));
        objStandingInstructionTO.setSiStartDt(stDate);
        }else{
            objStandingInstructionTO.setSiStartDt(DateUtil.getDateMMDDYYYY(getDtdStartDtSI()));
        }
        
         Date EndDt = DateUtil.getDateMMDDYYYY(getDtdEndDtSI());
         if(EndDt != null){
        Date endDate = (Date)curDate.clone();
        endDate.setDate(EndDt.getDate());
        endDate.setMonth(EndDt.getMonth());
        endDate.setYear(EndDt.getYear());
//        objStandingInstructionTO.setSiEndDt(DateUtil.getDateMMDDYYYY(getDtdEndDtSI()));
        objStandingInstructionTO.setSiEndDt(endDate);
         }else{
             objStandingInstructionTO.setSiEndDt(DateUtil.getDateMMDDYYYY(getDtdEndDtSI()));
         }
         Date SuspendDt = DateUtil.getDateMMDDYYYY(getDtdSuspendDt());
         if(SuspendDt != null){
        Date suspDate = (Date)curDate.clone();
        suspDate.setDate(SuspendDt.getDate());
        suspDate.setMonth(SuspendDt.getMonth());
        suspDate.setYear(SuspendDt.getYear());
//        objStandingInstructionTO.setSiEndDt(DateUtil.getDateMMDDYYYY(getDtdEndDtSI()));
        objStandingInstructionTO.setSiSuspendDt(suspDate);
         }else{
             objStandingInstructionTO.setSiSuspendDt(DateUtil.getDateMMDDYYYY(getDtdSuspendDt()));
         }
        objStandingInstructionTO.setFrequency(CommonUtil.convertObjToStr(cbmFrequencySI.getKeyForSelected()));
        
        objStandingInstructionTO.setWeekDay(CommonUtil.convertObjToStr(cbmWeekDay.getKeyForSelected()));
        objStandingInstructionTO.setWeek(CommonUtil.convertObjToStr(cbmWeek.getKeyForSelected()));
        objStandingInstructionTO.setChangeHolidayExec(CommonUtil.convertObjToStr(cbmExecutionDay.getKeyForSelected()));
        if(!(CommonUtil.convertObjToStr(cbmSpecificDate.getKeyForSelected()).equals("")))
            objStandingInstructionTO.setSpecificDate(CommonUtil.convertObjToDouble(cbmSpecificDate.getKeyForSelected()));
        
        if(getRdoHolidayExecution_Yes())
            objStandingInstructionTO.setSiHolidayExec("Y");
        else
            objStandingInstructionTO.setSiHolidayExec("N");
        if(getRdoSIAutoPosting_Yes())
            objStandingInstructionTO.setAutomaticPosting("Y");
        else
            objStandingInstructionTO.setAutomaticPosting("N");
        
        objStandingInstructionTO.setGraceDays(CommonUtil.convertObjToDouble(getTxtGraceDaysSI()));
        
        if(getRdoCommSI_Yes())
            objStandingInstructionTO.setCollectSiComm("Y");
        else
            objStandingInstructionTO.setCollectSiComm("N");
        
        if(!(getTxtCommChargesSI().equals("")))
            objStandingInstructionTO.setSiCharges(CommonUtil.convertObjToDouble(getTxtCommChargesSI()));
        
        if(getRdoRettCommSI_Yes())
            objStandingInstructionTO.setCollectRemitComm("Y");
        else
            objStandingInstructionTO.setCollectRemitComm("N");
        
        if(!(getTxtForwardCount().equals("")))
            objStandingInstructionTO.setCarriedForwardCount(CommonUtil.convertObjToDouble(getTxtForwardCount()));
        
        if(!(getTxtRettCommChargesSI().equals("")))
            objStandingInstructionTO.setRemitCharges(CommonUtil.convertObjToDouble(getTxtRettCommChargesSI()));
        
        if(!(getTxtExecutionCharges().equals("")))
            objStandingInstructionTO.setExecCharge(CommonUtil.convertObjToDouble(getTxtExecutionCharges()));
        
        if(!(getTxtAcceptanceCharges().equals("")))
            objStandingInstructionTO.setAcceptanceCharge(CommonUtil.convertObjToDouble(getTxtAcceptanceCharges()));
        
        if(!(getTxtFailureCharges().equals("")))
            objStandingInstructionTO.setFailureCharge(CommonUtil.convertObjToDouble(getTxtFailureCharges()));
        
        if(!(getTxtServiceTax().equals("")))
            objStandingInstructionTO.setServiceTax(CommonUtil.convertObjToDouble(getTxtServiceTax()));
        
        if(!(getTxtFailureST().equals("")))
            objStandingInstructionTO.setFailureServiceTax(CommonUtil.convertObjToDouble(getTxtFailureST()));
        
        objStandingInstructionTO.setExecConfig(CommonUtil.convertObjToStr(cbmExecConfig.getKeyForSelected()));
        objStandingInstructionTO.setRemitMode(CommonUtil.convertObjToStr(cbmMoRSI.getKeyForSelected()));
        objStandingInstructionTO.setBeneficiary(getTxtBeneficiarySI());
        objStandingInstructionTO.setBranchCode(getSelectedBranchID());
        objStandingInstructionTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
                 
        return objStandingInstructionTO;
    }
    
    public void  setStandingInstructionTO(StandingInstructionTO objStandingInstructionTO){
        setStatusBy(objStandingInstructionTO.getStatusBy());
        setAuthorizeStatus(objStandingInstructionTO.getAuthorizeStatus());
        setTxtSINo(objStandingInstructionTO.getSiId());
        setTxtSiDt(DateUtil.getStringDate(objStandingInstructionTO.getSiDt()));
        setCboSIType(CommonUtil.convertObjToStr(getCbmSIType().getDataForKey(objStandingInstructionTO.getSiType())));
        setTxtMultiplieSI(CommonUtil.convertObjToStr(objStandingInstructionTO.getMultiplesOf()));
        setTxtMinBalSI(CommonUtil.convertObjToStr(objStandingInstructionTO.getMinBalance()));
        setDtdStartDtSI(DateUtil.getStringDate(objStandingInstructionTO.getSiStartDt()));
        setDtdEndDtSI(DateUtil.getStringDate(objStandingInstructionTO.getSiEndDt()));
        setDtdSuspendDt(DateUtil.getStringDate(objStandingInstructionTO.getSiSuspendDt()));
        setNxtDt(objStandingInstructionTO.getNextRunDt());
        setSuspDt(objStandingInstructionTO.getSiSuspendDt());
        setLstDt(objStandingInstructionTO.getLastRunDt());
        setEndDt(objStandingInstructionTO.getSiStartDt());
        setCboFrequencySI(CommonUtil.convertObjToStr(getCbmFrequencySI().getDataForKey(objStandingInstructionTO.getFrequency())));
        setFreq(CommonUtil.convertObjToStr(objStandingInstructionTO.getFrequency()));
        setTxtGraceDaysSI(CommonUtil.convertObjToStr(objStandingInstructionTO.getGraceDays()));
        if (objStandingInstructionTO.getChkPendingInstalment() != null && objStandingInstructionTO.getChkPendingInstalment().equals("Y")) {
            setChkPendingInstalment(true);
        } else {
            setChkPendingInstalment(false);
        }
        if (objStandingInstructionTO.getChkInstalment() != null && objStandingInstructionTO.getChkInstalment().equals("Y")) {
            setChkIntalmentsYN(true);
        } else {
            setChkIntalmentsYN(false);
        }
        setNoOfInst(CommonUtil.convertObjToStr(objStandingInstructionTO.getNoOfInstalments()));
        if(objStandingInstructionTO.getCollectSiComm().equals("Y"))
            setRdoCommSI_Yes(true);
        else
            setRdoCommSI_No(true);
        
        setTxtCommChargesSI(CommonUtil.convertObjToStr(objStandingInstructionTO.getSiCharges()));
        
        if(objStandingInstructionTO.getCollectRemitComm().equals("Y"))
            setRdoRettCommSI_Yes(true);
        else
            setRdoRettCommSI_No(true);
        
        setTxtRettCommChargesSI(CommonUtil.convertObjToStr(objStandingInstructionTO.getRemitCharges()));
        
        setCboMoRSI(CommonUtil.convertObjToStr(getCbmMoRSI().getDataForKey(objStandingInstructionTO.getRemitMode())));
        setTxtBeneficiarySI(objStandingInstructionTO.getBeneficiary());
        setCboWeekDay(CommonUtil.convertObjToStr(getCbmWeekDay().getDataForKey(objStandingInstructionTO.getWeekDay())));
        setCboWeek(CommonUtil.convertObjToStr(getCbmWeek().getDataForKey(objStandingInstructionTO.getWeek())));
        setCboSpecificDate(CommonUtil.convertObjToStr(getCbmSpecificDate().getDataForKey(CommonUtil.convertObjToStr(objStandingInstructionTO.getSpecificDate()))));
        setCboExecutionDay(CommonUtil.convertObjToStr(getCbmExecutionDay().getDataForKey(CommonUtil.convertObjToStr(objStandingInstructionTO.getChangeHolidayExec()))));
        if(objStandingInstructionTO.getSiHolidayExec().equals("Y"))
            setRdoHolidayExecution_Yes(true);
        else
            setRdoHolidayExecution_No(true);
        
        if(objStandingInstructionTO.getAutomaticPosting().equals("Y"))
            setRdoSIAutoPosting_Yes(true);
        else
            setRdoSIAutoPosting_No(true);
        
        setTxtForwardCount(CommonUtil.convertObjToStr(objStandingInstructionTO.getCarriedForwardCount()));
        setCboExecConfig(CommonUtil.convertObjToStr(getCbmExecConfig().getDataForKey(objStandingInstructionTO.getExecConfig())));
        setTxtAcceptanceCharges(CommonUtil.convertObjToStr(objStandingInstructionTO.getAcceptanceCharge()));
        setTxtExecutionCharges(CommonUtil.convertObjToStr(objStandingInstructionTO.getExecCharge()));
        setTxtFailureST(CommonUtil.convertObjToStr(objStandingInstructionTO.getFailureServiceTax()));
        setTxtServiceTax(CommonUtil.convertObjToStr(objStandingInstructionTO.getServiceTax()));
        setTxtFailureCharges(CommonUtil.convertObjToStr(objStandingInstructionTO.getFailureCharge()));
        setLbl1LastExecutonDate(CommonUtil.convertObjToStr(objStandingInstructionTO.getLastRunDt()));
        setLbl1NextExecutonDate(CommonUtil.convertObjToStr(objStandingInstructionTO.getNextRunDt()));
        setLbl1HolidayExecutonDate(CommonUtil.convertObjToStr(objStandingInstructionTO.getExecDtHoliday()));
        setLbl1FwdExecutionDate(CommonUtil.convertObjToStr(objStandingInstructionTO.getForwardRunDt()));
        String lbl= CommonUtil.convertObjToStr(objStandingInstructionTO.getStatus());
        //System.out.println("^^^^lbl"+lbl);
        if(lbl.equals("SUSPENDED"))
            setChkSuspendUser(true);
        else
            setChkSuspendUser(false);
         String lblClose= CommonUtil.convertObjToStr(objStandingInstructionTO.getStatus());
        //System.out.println("^^^^lblClose"+lblClose);
        if(lblClose.equals("CLOSED"))
            setChkCloseSI(true);
        else
            setChkCloseSI(false);
    }
     public String executeQueryForCharge(String productId, String category, String amount, String chargeType ){
        double inputAmt = CommonUtil.convertObjToDouble(amount).doubleValue() ;
        double calculatedCharge = 0.0;
        HashMap where = new HashMap();
        where.put("PROD_ID", productId);
        where.put("CATEGORY", category);
        where.put("AMOUNT", amount);
        where.put("CHARGE_TYPE", chargeType);
        where.put("PAYABLE", "ISSU_BRANCH" );
//        where.put("BANK_CODE", getCbmDraweeBank().getKeyForSelected());
//        where.put("BRANCH_CODE", getCbmBranchCode().getKeyForSelected());
        List outList = executeQuery("getExchange", where);
        //System.out.println("where:" + where);
        //System.out.println("outList:" + outList);
        where = null ;
        if(outList.size() > 0){ //If a charge is configured. else return default zero
            //            for(int i = 0;i < outList.size(); i++){
            HashMap outputMap  = (HashMap)outList.get(0);
            
            if(outputMap != null){
                double toAmt = CommonUtil.convertObjToDouble(outputMap.get("TO_AMT")).doubleValue() ;
                double fixedRate = CommonUtil.convertObjToDouble(outputMap.get("CHARGE")).doubleValue() ;
                double percentage = CommonUtil.convertObjToDouble(outputMap.get("PERCENTAGE")).doubleValue() ;
                
                double forEveryAmt = CommonUtil.convertObjToDouble(outputMap.get("FOR_EVERY_AMT")).doubleValue() ;
                double forEveryRate = CommonUtil.convertObjToDouble(outputMap.get("FOR_EVERY_RATE")).doubleValue() ;
                String forEveryType = CommonUtil.convertObjToStr(outputMap.get("FOR_EVERY_TYPE")) ;
                outputMap = null ;
                if(percentage != 0)
                    calculatedCharge += (inputAmt * percentage) / 100;
                if(fixedRate != 0)
                    calculatedCharge += fixedRate ;
                
                if (inputAmt > toAmt) {
                    if(forEveryAmt != 0){
                        double remainder = inputAmt - toAmt ;
                        if(forEveryType.toUpperCase().equals("AMOUNT")) //Value from Lookup Table
                            calculatedCharge += (remainder / forEveryAmt) * forEveryRate ;
                        else if(forEveryType.toUpperCase().equals("PERCENTAGE"))//Value from Lookup Table
                            calculatedCharge += ((remainder / forEveryAmt) * percentage)/100 ;
                    }
                }
            }
            //        }
        }
        //System.out.println("===========>>>>>>>>calculatedCharge : " + calculatedCharge);
        calculatedCharge = (double)getNearest((long)(calculatedCharge *100),100)/100;
        return String.valueOf(calculatedCharge) ;
        
    }
     
      public double calServiceTax(String exchange,String productId,String category,String amount, String chargeType){
        double inputAmt = CommonUtil.convertObjToDouble(amount).doubleValue() ;
        double exchangeAmt = CommonUtil.convertObjToDouble(exchange).doubleValue() ;
        double calculatedCharge = 0.0;
        HashMap where = new HashMap();
        where.put("PROD_ID", productId);
        where.put("CATEGORY", category);
        where.put("AMOUNT", amount);
        where.put("CHARGE_TYPE", chargeType);
        where.put("PAYABLE", "ISSU_BRANCH" );
//        if(DraweeBank.equals(""))
//            where.put("BANK_CODE", getCbmDraweeBank().getKeyForSelected());
//        else
//            where.put("BANK_CODE", DraweeBank);
//        if(branchCode.equals(""))
//            where.put("BRANCH_CODE", getCbmBranchCode().getKeyForSelected());
//        else
//            where.put("BRANCH_CODE", branchCode);
        List outList = executeQuery("getServiceTax", where);
        //System.out.println("where:" + where);
        //System.out.println("outList:" + outList);
        where = null ;
        if(outList.size() > 0){ //If a charge is configured. else return default zero
            HashMap outputMap  = (HashMap)outList.get(0);
            
            if(outputMap != null){
                double serviceTax = CommonUtil.convertObjToDouble(outputMap.get("SERVICE_TAX")).doubleValue() ;
                if(serviceTax != 0)
                    calculatedCharge = (exchangeAmt * serviceTax) / 100;
            }
        }
        //System.out.println("===========>>>>>>>>calculatedCharge : " + calculatedCharge);
//        calculatedCharge = (double)getNearest((long)(calculatedCharge *100),100)/100;
        return calculatedCharge;
    }
      
      public double calServiceTaxForSiCharges(String exchange){
//        double inputAmt = CommonUtil.convertObjToDouble(amount).doubleValue() ;
        double exchangeAmt = CommonUtil.convertObjToDouble(exchange).doubleValue() ;
        double calculatedCharge = 0.0;
        HashMap where = new HashMap();
//        where.put("PROD_ID", productId);
//        where.put("CATEGORY", category);
//        where.put("AMOUNT", amount);
//        where.put("CHARGE_TYPE", chargeType);
//        where.put("PAYABLE", "ISSU_BRANCH" );
//        if(DraweeBank.equals(""))
//            where.put("BANK_CODE", getCbmDraweeBank().getKeyForSelected());
//        else
//            where.put("BANK_CODE", DraweeBank);
//        if(branchCode.equals(""))
//            where.put("BRANCH_CODE", getCbmBranchCode().getKeyForSelected());
//        else
//            where.put("BRANCH_CODE", branchCode);
        List outList = executeQuery("getServiceTaxSiCharges", where);
        //System.out.println("where:" + where);
        //System.out.println("outList:" + outList);
        where = null ;
        if(outList.size() > 0){ //If a charge is configured. else return default zero
            HashMap outputMap  = (HashMap)outList.get(0);
            
            if(outputMap != null){
                double serviceTax = CommonUtil.convertObjToDouble(outputMap.get("siFailureSt")).doubleValue() ;
                if(serviceTax != 0)
                    calculatedCharge = (exchangeAmt * serviceTax) / 100;
            }
        }
        //System.out.println("===========>>>>>>>>calculatedCharge : " + calculatedCharge);
        calculatedCharge = (double)getNearest((long)(calculatedCharge *100),100)/100;
        return calculatedCharge;
    }
      public double roundInterest(double intAmt) {
       double intamt = (double)getNearest((long)(intAmt *100),100)/100;
       return intamt;
    }
      public long getNearest(long number,long roundingFactor)  {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor%2) != 0)
            roundingFactorOdd +=1;
        long mod = number%roundingFactor;
        if ((mod < (roundingFactor/2)) || (mod < (roundingFactorOdd/2)))
            return lower(number,roundingFactor);
        else
            return higher(number,roundingFactor);
    }
    public long lower(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        return number-mod;
    }
    
    public long higher(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        if ( mod == 0)
            return number;
        return (number-mod) + roundingFactor ;
    }
    
    void holiydaychecking(Date lstintCr,String execDt){
        try{
            HashMap MonthEnd=new HashMap(); //traansferto holidaychecking method;
            boolean checkHoliday=true;

           Date lstDate = (Date)curDate.clone();

            lstDate.setMonth(lstintCr.getMonth());
            lstDate.setYear(lstintCr.getYear());
            lstDate.setDate(lstintCr.getDate());

            MonthEnd.put("NEXT_DATE",lstDate);//lstintCr abi
            MonthEnd.put("BRANCH_CODE",ProxyParameters.BRANCH_ID );

            while(checkHoliday){
                boolean tholiday = false;

                List Holiday=ClientUtil.executeQuery("checkHolidayDateOD",MonthEnd);
                List weeklyOf=ClientUtil.executeQuery("checkWeeklyOffOD",MonthEnd);
                boolean isHoliday = Holiday.size()>0 ? true : false;
                boolean isWeekOff = weeklyOf.size()>0 ? true : false;
                if (isHoliday || isWeekOff) {
                    MonthEnd = dateMinusPlus(MonthEnd,execDt);
                    checkHoliday=true;
                } else {
                    checkHoliday=false;
                    checkThisCDate = (Date) MonthEnd.get("NEXT_DATE");
                    
                    
                }
               
                
            }
         
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private HashMap dateMinusPlus(HashMap dateMap,String execDt) {
        String day=CommonUtil.convertObjToStr(dateMap.get("NEXT_DATE"));
     
        Date lastDay=(Date)dateMap.get("NEXT_DATE");
        int days=lastDay.getDate();
        if(execDt.equalsIgnoreCase("PREVIOUSDAY")) {
        
            lastDay = DateUtil.addDaysProperFormat(lastDay, -1);
        } else if(execDt.equalsIgnoreCase("NEXTDAY")) {
            lastDay = DateUtil.addDaysProperFormat(lastDay, 1);
            //            days++;
        }
        //            lastDay.setDate(days);
        dateMap.put("NEXT_DATE",lastDay);
        dateMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
        return dateMap;
    }
      private List executeQuery(String mapName, HashMap where){
        List returnList = null;
        try{
            returnList = (List) ClientUtil.executeQuery(mapName, where);
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return returnList;
    }
    public StandingInstructionCreditTO getStandingInstructionCreditTO(){
        StandingInstructionCreditTO objStandingInstructionCreditTO = new StandingInstructionCreditTO();
        objStandingInstructionCreditTO.setSiId(getTxtSINo());
        objStandingInstructionCreditTO.setAcHdId(getTxtAccHeadValueCSI());
        objStandingInstructionCreditTO.setProdId(CommonUtil.convertObjToStr(cbmProdIDCSI.getKeyForSelected()));
        objStandingInstructionCreditTO.setAcctNo(getTxtAccNoCSI());
        objStandingInstructionCreditTO.setAmount(CommonUtil.convertObjToDouble((getTxtAmountCSI())));
        objStandingInstructionCreditTO.setParticulars(getTxtParticularsCSI());
        objStandingInstructionCreditTO.setProdType(CommonUtil.convertObjToStr(getCbmProductTypeCSI().getKeyForSelected()));
        objStandingInstructionCreditTO.setBranchId(CommonUtil.convertObjToStr(getCbranchID()));
        return objStandingInstructionCreditTO;
    }
    
    public void  setStandingInstructionCreditTO(StandingInstructionCreditTO objStandingInstructionCreditTO){
        try{
            setTxtAccHeadValueCSI( objStandingInstructionCreditTO.getAcHdId());
            setCboProductTypeCSI(CommonUtil.convertObjToStr(getCbmProductTypeCSI().getDataForKey(objStandingInstructionCreditTO.getProdType())));
            if(!objStandingInstructionCreditTO.getProdType().equals("GL")){
                getCreditProductIdByType(CommonUtil.convertObjToStr(objStandingInstructionCreditTO.getProdType()));
            }
            setCboProdIDCSI(CommonUtil.convertObjToStr(getCbmProdIDCSI().getDataForKey(objStandingInstructionCreditTO.getProdId())));
            setTxtAccNoCSI(objStandingInstructionCreditTO.getAcctNo());
            setTxtAmountCSI(CommonUtil.convertObjToStr(objStandingInstructionCreditTO.getAmount()));
            setTxtParticularsCSI(objStandingInstructionCreditTO.getParticulars());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public StandingInstructionDebitTO getStandingInstructionDebitTO(){
        StandingInstructionDebitTO objStandingInstructionDebitTO = new StandingInstructionDebitTO();
        objStandingInstructionDebitTO.setAcHdId(getTxtAccHeadValueDSI());
        objStandingInstructionDebitTO.setSiId(getTxtSINo());
        objStandingInstructionDebitTO.setProdId(CommonUtil.convertObjToStr(cbmProdIDDSI.getKeyForSelected()));
        objStandingInstructionDebitTO.setAcctNo(getTxtAccNoDSI());
        objStandingInstructionDebitTO.setAmount(CommonUtil.convertObjToDouble((getTxtAmountDSI())));
        objStandingInstructionDebitTO.setParticulars(getTxtParticularsDSI());
        objStandingInstructionDebitTO.setProdType(CommonUtil.convertObjToStr(getCbmProductTypeDSI().getKeyForSelected()));
        objStandingInstructionDebitTO.setBranchId(CommonUtil.convertObjToStr(getDbranchid()));
        return objStandingInstructionDebitTO;
    }
    
    public void  setStandingInstructionDebitTO(StandingInstructionDebitTO objStandingInstructionDebitTO){
        try{
            setTxtAccHeadValueDSI(objStandingInstructionDebitTO.getAcHdId());
            setCboProductTypeDSI(CommonUtil.convertObjToStr(getCbmProductTypeDSI().getDataForKey(objStandingInstructionDebitTO.getProdType())));
            if(!objStandingInstructionDebitTO.getProdType().equals("GL")){
                getDebitProductIdByType(CommonUtil.convertObjToStr(objStandingInstructionDebitTO.getProdType()));
            }
            setCboProdIDDSI(CommonUtil.convertObjToStr(getCbmProdIDDSI().getDataForKey(objStandingInstructionDebitTO.getProdId())));
            setTxtAccNoDSI(objStandingInstructionDebitTO.getAcctNo());
            setTxtAmountDSI(CommonUtil.convertObjToStr(objStandingInstructionDebitTO.getAmount()));
            setTxtParticularsDSI(objStandingInstructionDebitTO.getParticulars());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /** Getter for property lblAccHeadValueDSI.
     * @return Value of property lblAccHeadValueDSI.
     *
     */
    public java.lang.String getTxtAccHeadValueDSI() {
        return txtAccHeadValueDSI;
    }
    
    /** Setter for property lblAccHeadValueDSI.
     * @param lblAccHeadValueDSI New value of property lblAccHeadValueDSI.
     *
     */
    public void setTxtAccHeadValueDSI(java.lang.String txtAccHeadValueDSI) {
        this.txtAccHeadValueDSI = txtAccHeadValueDSI;
    }
    
    /*This method Fires a Query to the Database and 'll
    returm a HashMap which 'll contain the result Lists.
    /**/
    public HashMap populateData(HashMap whereMap) {
        HashMap mapData = null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            //System.out.println("####mapDataPOB"+mapData);

                 
        } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
        return mapData;
    }
    public void setComboDBProdTypes(){
        cbmProductTypeDSI.setKeyForSelected("");                    
    }
      public void setComboCDProdTypes(){  
        cbmProductTypeCSI.setKeyForSelected("");
    }
    public boolean checkAcNoWithoutProdType(String actNum) {
        HashMap mapData=new HashMap();
        boolean isExists = false;
        try{//dont delete chck selectalldao
            mapData.put("ACT_NUM", actNum);
            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData); 
            //System.out.println("#### mapDataList :"+mapDataList);
            if (mapDataList!=null && mapDataList.size()>0) {
                mapData=(HashMap)mapDataList.get(0);
                if(debitAcNo == true){
                    setTxtAccNoDSI(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                    cbmProductTypeDSI.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));                    
                    setCboProdIDDSI(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                    cbmProdIDDSI.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                    getAccNoDetailsWithoutProdType(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")),CommonUtil.convertObjToStr(mapData.get("ACT_NUM")),"DIAccountNo");
                    setDbranchid(CommonUtil.convertObjToStr(mapData.get("BRANCH_ID")));
                }
                if(creditAcNo == true){
                    setTxtAccNoCSI(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                    cbmProductTypeCSI.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                    setCboProdIDCSI(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                    cbmProdIDCSI.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                    getAccNoDetailsWithoutProdType(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")),CommonUtil.convertObjToStr(mapData.get("ACT_NUM")),"CIAccountNo");
                    setCbranchID(CommonUtil.convertObjToStr(mapData.get("BRANCH_ID")));
                }
                getAccHeadLabelCaption(CommonUtil.convertObjToStr(mapData.get("PROD_ID")),CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                isExists = true;
            } else {
//                ArrayList key=new ArrayList();
//                ArrayList value=new ArrayList();
//                key.add("");
//                value.add("");   
                getAccHeadLabelCaption("","");
                isExists = false;
//                key = null;
//                value = null;
                isExists = false;
            }
            mapDataList = null;
        }catch(Exception e){
            e.printStackTrace();
        }
        mapData = null;
        return isExists;
    }    
    
    public void getAccNoDetails(String prodType,String accNo, String btnKey){
        if(!prodType.equalsIgnoreCase("RM")){
        String lblName = "";
        String  mapName = "getAccountNumberName"+prodType;
        HashMap where = new HashMap();
        where.put("ACC_NUM",accNo);
        if(!prodType.equals("")){
            List result = ClientUtil.executeQuery(mapName, where);
            
            if(result.size()>0){
                HashMap resultMap = (HashMap)result.get(0);
                if(resultMap != null){
                    lblName = CommonUtil.convertObjToStr(resultMap.get("CUSTOMER_NAME"));
//                    if(resultMap.containsKey("INT_AMT")) {
//                        setTxtAmountDSI(CommonUtil.convertObjToStr(resultMap.get("INT_AMT")));
//                        setTxtAmountDSI("");                                                    // added by vinay because no need to set any value
//                    }
//                    if(resultMap.containsKey("PAY_FREQ")) {
//                        setCboFrequencySI(CommonUtil.convertObjToStr(getCbmFrequencySI().getDataForKey(CommonUtil.convertObjToStr(resultMap.get("PAY_FREQ")))));
//                        // setTxtParticularsDSI(CommonUtil.convertObjToStr(resultMap.get("PAY_FREQ")));
////                        setTxtParticularsDSI(CommonUtil.convertObjToStr(getCbmFrequencySI().getDataForKey(CommonUtil.convertObjToStr(resultMap.get("PAY_FREQ")))));
//                        setCboFrequencySI("");                                                  // added by vinay because no need to set any value
////                        setTxtParticularsDSI("");                                               // added by vinay because no need to set any value
//                    }
//                    if(resultMap.containsKey("DP_DT")){
//                        //                      setDtdStartDtSI(CommonUtil.convertObjToStr(resultMap.get("DP_DT")));
//                        //                      setDtdStartDtSI(CommonUtil.convertObjToStr(resultMap.get("DP_DT")) + ( CommonUtil.convertObjToStr(resultMap.get("PAY_FREQ"))));
//                        int a=CommonUtil.convertObjToInt(resultMap.get("PAY_FREQ"));
//                        int b=0;
//                        if(a!=b) {
//                            Date stDt =DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(resultMap.get("DP_DT")));
//                            System.out.println("Deposit Date : " + stDt);
//                            int dtInt=Integer.parseInt(CommonUtil.convertObjToStr(resultMap.get("PAY_FREQ")));
//                            stDt = new Date(stDt.getYear(),stDt.getMonth()+dtInt/30,stDt.getDate());
//                            System.out.println("Deposit Date Get Year, Month, Date : " + stDt);
//                            setDtdStartDtSI(CommonUtil.convertObjToStr(stDt));
//                            setDtdStartDtSI("");                                                // added by vinay because no need to set any value
//                        }
//                        else{
//                            setDtdStartDtSI(CommonUtil.convertObjToStr(resultMap.get("MT_DT")));
//                            setDtdStartDtSI("");                                                 // added by vinay because no need to set any value
//                        }
//                    }
//                    
//                    if(resultMap.containsKey("MT_DT")){
//                        setDtdEndDtSI(CommonUtil.convertObjToStr(resultMap.get("MT_DT")));
//                        setDtdEndDtSI("");                                                       // added by vinay because no need to set any value
//                    }
                }
            }
        }
        if(btnKey.equals("DIAccountNo")){
            setTxtAccNoDSI(accNo);
            setLblNameValueDSI(lblName);
//            setTxtAccNoDSI("");                                                                 // added by vinay because no need to set any value                                    
//            setLblNameValueDSI("");
        }else if(btnKey.equals("CIAccountNo")){
            setTxtAccNoCSI(accNo);
            setLblNameValueCSI(lblName);
//            setTxtAccNoCSI("");                                                                 // added by vinay because no need to set any value
//            setLblNameValueCSI("");
        }
        notifyObservers();
    }
    }
    
    public void getAccNoDetailsWithoutProdType(String prodType,String accNo, String btnKey){
        if(!prodType.equalsIgnoreCase("RM")){
            String lblName = "";
            String  mapName = "getAccountNumberName"+prodType;
            HashMap where = new HashMap();
            where.put("ACC_NUM",accNo);
            if(!prodType.equals("")){
                List result = ClientUtil.executeQuery(mapName, where);                
                if(result.size()>0){
                    HashMap resultMap = (HashMap)result.get(0);
                    if(resultMap != null){
                        lblName = CommonUtil.convertObjToStr(resultMap.get("CUSTOMER_NAME"));
                    }
                }
            }
            if(btnKey.equals("DIAccountNo")){
                setTxtAccNoDSI(accNo);
                setLblNameValueDSI(lblName);
            }else if(btnKey.equals("CIAccountNo")){
                setTxtAccNoCSI(accNo);
                setLblNameValueCSI(lblName);
            }
        }
    }
    
    /** Getter for property lblNameValueCSI.
     * @return Value of property lblNameValueCSI.
     *
     */
    public java.lang.String getLblNameValueCSI() {
        return lblNameValueCSI;
    }
    
    /** Setter for property lblNameValueCSI.
     * @param lblNameValueCSI New value of property lblNameValueCSI.
     *
     */
    public void setLblNameValueCSI(java.lang.String lblNameValueCSI) {
        this.lblNameValueCSI = lblNameValueCSI;
    }
    
    /** Getter for property lblNameValueDSI.
     * @return Value of property lblNameValueDSI.
     *
     */
    public java.lang.String getLblNameValueDSI() {
        return lblNameValueDSI;
    }
    
    /** Setter for property lblNameValueDSI.
     * @param lblNameValueDSI New value of property lblNameValueDSI.
     *
     */
    public void setLblNameValueDSI(java.lang.String lblNameValueDSI) {
        this.lblNameValueDSI = lblNameValueDSI;
    }
    
    /** Getter for property tbmDebitSI.
     * @return Value of property tbmDebitSI.
     *
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbmDebitSI() {
        return tbmDebitSI;
    }
    
    /** Setter for property tbmDebitSI.
     * @param tbmDebitSI New value of property tbmDebitSI.
     *
     */
    public void setTbmDebitSI(com.see.truetransact.clientutil.EnhancedTableModel tbmDebitSI) {
        this.tbmDebitSI = tbmDebitSI;
    }
    
    /** Getter for property tbmCreditSI.
     * @return Value of property tbmCreditSI.
     *
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbmCreditSI() {
        return tbmCreditSI;
    }
    
    /** Setter for property tbmCreditSI.
     * @param tbmCreditSI New value of property tbmCreditSI.
     *
     */
    public void setTbmCreditSI(com.see.truetransact.clientutil.EnhancedTableModel tbmCreditSI) {
        this.tbmCreditSI = tbmCreditSI;
    }
    
    /**
     * Getter for property cboProductTypeCSI.
     * @return Value of property cboProductTypeCSI.
     */
    public java.lang.String getCboProductTypeCSI() {
        return cboProductTypeCSI;
    }
    
    /**
     * Setter for property cboProductTypeCSI.
     * @param cboProductTypeCSI New value of property cboProductTypeCSI.
     */
    public void setCboProductTypeCSI(java.lang.String cboProductTypeCSI) {
        this.cboProductTypeCSI = cboProductTypeCSI;
        setChanged();
    }
    
    /**
     * Getter for property cbmProductTypeCSI.
     * @return Value of property cbmProductTypeCSI.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProductTypeCSI() {
        return cbmProductTypeCSI;
    }
    
    /**
     * Setter for property cbmProductTypeCSI.
     * @param cbmProductTypeCSI New value of property cbmProductTypeCSI.
     */
    public void setCbmProductTypeCSI(com.see.truetransact.clientutil.ComboBoxModel cbmProductTypeCSI) {
        this.cbmProductTypeCSI = cbmProductTypeCSI;
        setChanged();
    }
    
    /**
     * Getter for property cboProductTypeDSI.
     * @return Value of property cboProductTypeDSI.
     */
    public java.lang.String getCboProductTypeDSI() {
        return cboProductTypeDSI;
    }
    
    /**
     * Setter for property cboProductTypeDSI.
     * @param cboProductTypeDSI New value of property cboProductTypeDSI.
     */
    public void setCboProductTypeDSI(java.lang.String cboProductTypeDSI) {
        this.cboProductTypeDSI = cboProductTypeDSI;
        setChanged();
    }
    
    
    /**
     * Getter for property cbmProductTypeDSI.
     * @return Value of property cbmProductTypeDSI.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProductTypeDSI() {
        return cbmProductTypeDSI;
    }
    
    /**
     * Setter for property cbmProductTypeDSI.
     * @param cbmProductTypeDSI New value of property cbmProductTypeDSI.
     */
    public void setCbmProductTypeDSI(com.see.truetransact.clientutil.ComboBoxModel cbmProductTypeDSI) {
        this.cbmProductTypeDSI = cbmProductTypeDSI;
        setChanged();
    }
    
    /**
     * Getter for property cboExecutionDay.
     * @return Value of property cboExecutionDay.
     */
    public java.lang.String getCboExecutionDay() {
        return cboExecutionDay;
    }
    
    /**
     * Setter for property cboExecutionDay.
     * @param cboExecutionDay New value of property cboExecutionDay.
     */
    public void setCboExecutionDay(java.lang.String cboExecutionDay) {
        this.cboExecutionDay = cboExecutionDay;
    }
    
    /**
     * Getter for property cbmExecutionDay.
     * @return Value of property cbmExecutionDay.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmExecutionDay() {
        return cbmExecutionDay;
    }
    
    /**
     * Setter for property cbmExecutionDay.
     * @param cbmExecutionDay New value of property cbmExecutionDay.
     */
    public void setCbmExecutionDay(com.see.truetransact.clientutil.ComboBoxModel cbmExecutionDay) {
        this.cbmExecutionDay = cbmExecutionDay;
    }
    
    public void resetDebitSI(){
        setCboProdIDDSI("");
        setTxtAccNoDSI("");
        setTxtAmountDSI("");
        setTxtParticularsDSI("");
        setTxtAccHeadValueDSI("");
        setLblNameValueDSI("");
        setCboProductTypeDSI("");
        setDbranchid("");
        notifyObservers();
    }
    
    public void resetCreditSI(){
        setCboProdIDCSI("");
        setTxtAccNoCSI("");
        setTxtAmountCSI("");
        setTxtParticularsCSI("");
        setTxtAccHeadValueCSI("");
        setLblNameValueCSI("");
        setCboProductTypeCSI("");
        setCbranchID("");
        notifyObservers();
    }
    
    /** Getter for property lblAccHeadValueCSI.
     * @return Value of property lblAccHeadValueCSI.
     *
     */
    public java.lang.String getTxtAccHeadValueCSI() {
        return txtAccHeadValueCSI;
    }
    
    /** Setter for property lblAccHeadValueCSI.
     * @param lblAccHeadValueCSI New value of property lblAccHeadValueCSI.
     *
     */
    public void setTxtAccHeadValueCSI(java.lang.String txtAccHeadValueCSI) {
        this.txtAccHeadValueCSI = txtAccHeadValueCSI;
    }
    
    void doAction(String command){
        try{
            if(command.equals("NewDI")){
                resetDebitSI();
            } else if(command.equals("SaveDI")){
                saveDI();
            } else if(command.equals("DeleteDI")){
                deleteDI();
            }else if(command.equals("NewCI")){
                resetCreditSI();
            }else if(command.equals("SaveCI")){
                saveCI();
            }else if(command.equals("DeleteCI")){
                deleteCI();
            }else if(command.equals("UpdateSI")){
                updateSI();
//                resetForm();
            }else if(command.equals("DeleteSI")){
                deleteSI();
                resetForm();
            }else if(command.equals("InsertSI")){
                insertSI();
                resetForm();
            }else if(command.equals("ClearSI")){
                
            }else if(command.equals("ClearSI")){
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    void doExecute(ArrayList exeList){
         try {
            HashMap dataMap = new HashMap();
            dataMap.put("EXECUTE_LIST", exeList);
            map.put(CommonConstants.MODULE, getModule());
            map.put(CommonConstants.SCREEN, getScreen());
            map.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            map.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            HashMap proxyResultMap = proxy.execute(dataMap, map);
            setProxyReturnMap(proxyResultMap);
            if (proxyResultMap != null && proxyResultMap.containsKey(CommonConstants.TRANS_ID)) {
    		ClientUtil.showMessageWindow ("Status : " + CommonUtil.convertObjToStr(proxyResultMap.get("SUCESS")));
            }
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
    }
    void saveDI(){
        try{
            if(listDebitSI == null)
                listDebitSI = new ArrayList();
            
            StandingInstructionDebitTO dsi = getStandingInstructionDebitTO();
            List tableRow = new ArrayList();
            tableRow.add(dsi.getProdType());
            tableRow.add(dsi.getProdId());
            tableRow.add(dsi.getAcHdId());
            tableRow.add(dsi.getAcctNo());
            tableRow.add(CommonUtil.convertObjToStr(dsi.getAmount()));
            tableRow.add(dsi.getBranchId());
            
            int size = listDebitSI.size();
            for(int i=0;i<size;i++){
                if(((StandingInstructionDebitTO)listDebitSI.get(i)).getAcctNo()!= null){
                    if(!(((StandingInstructionDebitTO)listDebitSI.get(i)).getAcctNo()).equals("")){
                        if(((StandingInstructionDebitTO)listDebitSI.get(i)).getAcctNo().equals(dsi.getAcctNo()) ){
                            listDebitSI.set(i,dsi);
                            tbmDebitSI.removeRow(i);
                            tbmDebitSI.insertRow(i,(ArrayList)tableRow);
                            resetDebitSI();
                            return;
                        }
                    }
                }
//                if(((StandingInstructionDebitTO)listDebitSI.get(i)).getAcHdId()!= null){
//                    if(!(((StandingInstructionDebitTO)listDebitSI.get(i)).getAcHdId()).equals("")){
//                        if(((StandingInstructionDebitTO)listDebitSI.get(i)).getAcHdId().equals(dsi.getAcHdId()) ){
//                            listDebitSI.set(i,dsi);
//                            tbmDebitSI.removeRow(i);
//                            tbmDebitSI.insertRow(i,(ArrayList)tableRow);
//                            resetDebitSI();
//                            return;
//                        }
//                    }
//                }
            }
            
            listDebitSI.add(dsi);
            tbmDebitSI.addRow((ArrayList)tableRow);
            resetDebitSI();
        }catch(Exception e){
            e.printStackTrace();
        }
        return;
    }
    
    void saveCI(){
        try{
            if(listCreditSI == null)
                listCreditSI = new ArrayList();
            
            StandingInstructionCreditTO csi = getStandingInstructionCreditTO();
            List tableRow = new ArrayList();
            tableRow.add(csi.getProdType());
            tableRow.add(csi.getProdId());
            tableRow.add(csi.getAcHdId());
            tableRow.add(csi.getAcctNo());
            tableRow.add(CommonUtil.convertObjToStr(csi.getAmount()));
            tableRow.add(csi.getBranchId());
            
            int size = listCreditSI.size();
            int i=0;
            while(i<size){
                if(((StandingInstructionCreditTO)listCreditSI.get(i)).getAcctNo() != null){
                    if(!(((StandingInstructionCreditTO)listCreditSI.get(i)).getAcctNo().equals(""))){
                        if(((StandingInstructionCreditTO)listCreditSI.get(i)).getAcctNo().equals(csi.getAcctNo()) ){
                            listCreditSI.set(i,csi);
                            tbmCreditSI.removeRow(i);
                            tbmCreditSI.insertRow(i,(ArrayList)tableRow);
                            resetCreditSI();
                            return;
                        }
                    }
                }
//                if(((StandingInstructionCreditTO)listCreditSI.get(i)).getAcHdId() != null){
//                    if(!(((StandingInstructionCreditTO)listCreditSI.get(i)).getAcHdId().equals(""))){
//                        if(((StandingInstructionCreditTO)listCreditSI.get(i)).getAcHdId().equals(csi.getAcHdId()) ){
//                            listCreditSI.set(i,csi);
//                            tbmCreditSI.removeRow(i);
//                            tbmCreditSI.insertRow(i,(ArrayList)tableRow);
//                            resetCreditSI();
//                            return;
//                        }
//                    }
//                }
                i++;
            }
            
            listCreditSI.add(csi);
            tbmCreditSI.addRow((ArrayList)tableRow);
            resetCreditSI();
        }catch(Exception e){
            e.printStackTrace();
        }
        return;
    }
    
    void deleteDI(){
        int i = 0;
        int size = listDebitSI.size();
        while(i<size){
            if(((StandingInstructionDebitTO)listDebitSI.get(i)).getAcctNo() != null){
                if(!(((StandingInstructionDebitTO)listDebitSI.get(i)).getAcctNo().equals(""))){
                    if(((StandingInstructionDebitTO)listDebitSI.get(i)).getAcctNo().equals(txtAccNoDSI)){
                        listDebitSI.remove(i);
                        tbmDebitSI.removeRow(i);
                        break;
                    }
                }
            }
            if(((StandingInstructionDebitTO)listDebitSI.get(i)).getAcHdId()!= null){
                if(!(((StandingInstructionDebitTO)listDebitSI.get(i)).getAcHdId()).equals("")){
                    if(((StandingInstructionDebitTO)listDebitSI.get(i)).getAcHdId().equals(txtAccHeadValueDSI) ){
                        listDebitSI.remove(i);
                        tbmDebitSI.removeRow(i);
                        break;
                    }
                }
            }
            
            i++;
        }
        resetDebitSI();
        
    }
    
    void deleteCI(){
        int i = 0;
        int size = listCreditSI.size();
        while(i<size){
            if(((StandingInstructionCreditTO)listCreditSI.get(i)).getAcctNo()!=null){
                if(!(((StandingInstructionCreditTO)listCreditSI.get(i)).getAcctNo().equals(""))){
                    if(((StandingInstructionCreditTO)listCreditSI.get(i)).getAcctNo().equals(txtAccNoCSI)){
                        listCreditSI.remove(i);
                        tbmCreditSI.removeRow(i);
                        break;
                    }
                }
            }
            if(((StandingInstructionCreditTO)listCreditSI.get(i)).getAcHdId()!= null){
                if(!(((StandingInstructionCreditTO)listCreditSI.get(i)).getAcHdId()).equals("")){
                    if(((StandingInstructionCreditTO)listCreditSI.get(i)).getAcHdId().equals(txtAccHeadValueCSI) ){
                        listCreditSI.remove(i);
                        tbmCreditSI.removeRow(i);
                        break;
                    }
                }
            }
            
            i++;
        }
        resetCreditSI();
    }
    
    void getRowTableDI(int rowIndex){
        try{
            resetDebitSI();
            setStandingInstructionDebitTO((StandingInstructionDebitTO)listDebitSI.get(rowIndex));
            getAccNoDetails(CommonUtil.convertObjToStr(getCbmProductTypeDSI().getKeyForSelected()),txtAccNoDSI,"DIAccountNo");
            notifyObservers();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    void getRowTableCI(int rowIndex){
        try{
            resetCreditSI();
            setStandingInstructionCreditTO((StandingInstructionCreditTO)listCreditSI.get(rowIndex));
            getAccNoDetails(CommonUtil.convertObjToStr(getCbmProductTypeCSI().getKeyForSelected()),txtAccNoCSI,"CIAccountNo");
            notifyObservers();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /* When an Account no is selected. It'll check if it is already exist in the listDebitSI.
     * If exist it'll set Debit details components with the StandintInstructionDebitTO object.
     **/
    int isExistSetDI(String accNo){
        int exist = 0;
        if(listDebitSI != null && listDebitSI.size() >0){
            int size = listDebitSI.size();
            int i=0;
            while(i<size){
                if(((StandingInstructionDebitTO)listDebitSI.get(i)).getAcctNo()!= null){
                    if(((StandingInstructionDebitTO)listDebitSI.get(i)).getAcctNo().equals(accNo)){
                        setStandingInstructionDebitTO((StandingInstructionDebitTO)listDebitSI.get(i));
                        exist = 1;
                        break;
                    }
                }
                i++;
            }
            
        }
        notifyObservers();
        return exist;
    }
    
    /* When an Account no is selected. It'll check if it is already exist in the listCreditSI.
     * If exist it'll set Credit details components with the StandintInstructionCreditTO object.
     **/
    int isExistSetCI(String accNo){
        int exist = 0;
        if(listCreditSI != null && listCreditSI.size() >0){
            int size = listCreditSI.size();
            int i=0;
            while(i<size){
                if(((StandingInstructionCreditTO)listCreditSI.get(i)).getAcctNo()!= null){
                    if(((StandingInstructionCreditTO)listCreditSI.get(i)).getAcctNo().equals(accNo)){
                        setStandingInstructionCreditTO((StandingInstructionCreditTO)listCreditSI.get(i));
                        exist = 1;
                        break;
                    }
                }
                i++;
            }
        }
        notifyObservers();
        return exist;
    }
    
    /* When an Account Head is selected. It'll check if it is already exist in the listDebitSI.
     * If exist it'll set Debit details components with the StandintInstructionDebitTO object.
     **/
    int isAcHeadExistSetDI(String actHd){
        int exist = 0;
        if(listDebitSI != null && listDebitSI.size() >0){
            int size = listDebitSI.size();
            int i=0;
            while(i<size){
                if(((StandingInstructionDebitTO)listDebitSI.get(i)).getAcHdId()!= null){
                    if(((StandingInstructionDebitTO)listDebitSI.get(i)).getAcHdId().equals(actHd)){
                        setStandingInstructionDebitTO((StandingInstructionDebitTO)listDebitSI.get(i));
                        exist = 1;
                        break;
                    }
                }
                i++;
            }
        }
        notifyObservers();
        return exist;
    }
    
     /* When an Account Head is selected. It'll check if it is already exist in the listCreditSI.
      * If exist it'll set Credit details components with the StandintInstructionCreditTO object.
      **/
    int isAcHeadExistSetCI(String actHd){
        int exist = 0;
        if(listCreditSI != null && listCreditSI.size() >0){
            int size = listCreditSI.size();
            int i=0;
            while(i<size){
                if(((StandingInstructionCreditTO)listCreditSI.get(i)).getAcHdId()!= null){
                    if(((StandingInstructionCreditTO)listCreditSI.get(i)).getAcHdId().equals(actHd)){
                        setStandingInstructionCreditTO((StandingInstructionCreditTO)listCreditSI.get(i));
                        exist = 1;
                        break;
                    }
                }
                i++;
            }
        }
        notifyObservers();
        return exist;
    }
    private HashMap populateBean(String command) {
        HashMap siBeans = new HashMap();
        siBeans.put("StandingInstructionTO", getStandingInstructionTO(command));
        siBeans.put("StandingInstructionDebitTO", listDebitSI);
        siBeans.put("StandingInstructionCreditTO", listCreditSI);
        siBeans.put("SUSPEND",this.getSuspended());
        siBeans.put("CLOSE",this.getClose());
        return siBeans;
    }
    
    void insertSI(){
        try {
            HashMap dataMap = populateBean(CommonConstants.TOSTATUS_INSERT);
            map.put(CommonConstants.MODULE, getModule());
            map.put(CommonConstants.SCREEN, getScreen());
            map.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            map.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            HashMap proxyResultMap = proxy.execute(dataMap, map);
            setProxyReturnMap(proxyResultMap);
            if (proxyResultMap != null && proxyResultMap.containsKey(CommonConstants.TRANS_ID)) {
    		ClientUtil.showMessageWindow ("Standing Instruction No. : " + CommonUtil.convertObjToStr(proxyResultMap.get(CommonConstants.TRANS_ID)));
        }
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
    }
    
    void deleteSI(){
        try {
            HashMap proxyResultMap = proxy.execute(populateBean(CommonConstants.TOSTATUS_DELETE), map);
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
    }
    
    void updateSI(){
        try {
            HashMap proxyResultMap = proxy.execute(populateBean(CommonConstants.TOSTATUS_UPDATE), map);
            
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
    }
    
    void getDataSI(HashMap whereMap){
        StandingInstructionTO siTO;
        StandingInstructionDebitTO sidTO;
        StandingInstructionCreditTO sicTO;
        List tableRow;
        
        HashMap result = populateData(whereMap);
        if(result != null){
            siTO = (StandingInstructionTO)result.get("StandingInstructionTO");
            setStandingInstructionTO(siTO);
            
            //listDebitSI = (List)result.get("StandingInstructionDebitTO");
            listDebitSI = (List)result.get("SI_DEBIT_MASTER");
            if(listDebitSI!=null){
                int size = listDebitSI.size();
                int i=0;
                while(i<size){
                    tableRow = new ArrayList();
                    sidTO = (StandingInstructionDebitTO)listDebitSI.get(i);
                    tableRow.add(sidTO.getProdType());
                    tableRow.add(sidTO.getProdId());
                    tableRow.add(sidTO.getAcHdId());
                    tableRow.add(sidTO.getAcctNo());
                    tableRow.add(CommonUtil.convertObjToStr(sidTO.getAmount()));
                    tbmDebitSI.addRow((ArrayList)tableRow);
                    i++;
                }
            }
            
            //listCreditSI = (List)result.get("StandingInstructionCreditTO");
            listCreditSI = (List)result.get("SI_CREDIT_MASTER");
            if(listCreditSI!=null){
                int size = listCreditSI.size();
                int i=0;
                while(i<size){
                    tableRow = new ArrayList();
                    sicTO = (StandingInstructionCreditTO)listCreditSI.get(i);
                    tableRow.add(sicTO.getProdType());
                    tableRow.add(sicTO.getProdId());
                    tableRow.add(sicTO.getAcHdId());
                    tableRow.add(sicTO.getAcctNo());
                    tableRow.add(CommonUtil.convertObjToStr(sicTO.getAmount()));
                    tbmCreditSI.addRow((ArrayList)tableRow);
//                    List lst = (List)tableRow;
//                    lst.add(i, tableRow);
                    crdActLst.add(i, tableRow.get(3));
                    //System.out.println("$$$$$$$$$$$crdActLst"+crdActLst);
                    //System.out.println("$$$$$$$$$$$crdActLst"+getCrdActLst());
//                    setCrdActLst(crdActLst);
                    
                    i++;
                }
            }
        }
    }
    public ArrayList populateData(HashMap mapID, CTable tblData) {
        _tblData = tblData;
        
        // Adding Where Condition
        HashMap whereMap = null;
        if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
            if (mapID.get(CommonConstants.MAP_WHERE) instanceof HashMap)
                whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
            else 
                System.out.println ("Convert other data type to HashMap:" + mapID);
        } else {
            whereMap = new HashMap();
        }
        
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put (CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put (CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }
        
        mapID.put (CommonConstants.MAP_WHERE, whereMap);
        
        //System.out.println ("Screen   : " + getClass());
        //System.out.println ("Map Name : " + mapID.get(CommonConstants.MAP_NAME));
        //System.out.println ("Map      : " + mapID);
        
        dataHash = ClientUtil.executeTableQuery(mapID);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        if (_heading!=null && _heading.size()>0)
            _heading.add(0, "Select");
        ArrayList arrList = new ArrayList();
        if (data!=null && data.size()>0)
            for (int i=0; i<data.size();i++) {
                arrList = (ArrayList)data.get(i);
                arrList.add(0, new Boolean(false));
                data.set(i, arrList);
            }
        //System.out.println("### Data : "+data);
        populateTable();
        whereMap = null;
        return _heading;
        
    }
public void setSelectAll(Boolean selected) {
        for (int i=0, j=_tblData.getRowCount(); i < j; i++) {
            _tblData.setValueAt(selected, i, 0);
        }
    }
    public void populateTable() {
//        ArrayList heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        boolean dataExist;
        if (_heading != null){
            _isAvailable = true;
            dataExist = true;         
            setTblModel(_tblData, data, _heading);     
        }else{
            _isAvailable = false;
            dataExist = false;
            
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(_tblData);
            TableModel tableModel = new TableModel();
            tableModel.setHeading(new ArrayList());
            tableModel.setData(new ArrayList());
            tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            
            _tblData.setModel(tableSorter);
            _tblData.revalidate();
            
            ClientUtil.noDataAlert();
        }
      
    }
    
    private void setTblModel(CTable tbl, ArrayList tblData, ArrayList head) {
        TableSorter tableSorter = new TableSorter();
        tableSorter.addMouseListenerToHeaderInTable(tbl);
        TableModel tableModel = new TableModel(tblData, head) {
                public boolean isCellEditable(int rowIndex, int mColIndex) {
                    if (mColIndex == 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };

        tableModel.fireTableDataChanged();
        tableSorter.setModel(tableModel);
        tableSorter.fireTableDataChanged();

        tbl.setModel(tableSorter);
        tbl.revalidate();        
    }
    private void resetTables(EnhancedTableModel tbm){
        int rows = tbm.getRowCount();
        while(rows>0){
            tbm.removeRow(0);
            rows--;
        }
    }
    
    void resetForm(){
        resetTables(tbmDebitSI);
        resetTables(tbmCreditSI);
        resetCreditSI();
        resetDebitSI();
        resetSI();
        resetList();
        notifyObservers();
    }
    
    private void resetList(){
        listDebitSI = null;
        listCreditSI = null;
    }
    
    private void resetSI(){
        setTxtSINo("");
        setTxtSiDt("");
        setDtdStartDtSI("");
        setCboSIType("");
        setTxtMultiplieSI("");
        setTxtMinBalSI("");
        setDtdStartDtSI("");
        setDtdEndDtSI("");
        setDtdSuspendDt("");
        setCboFrequencySI("");
        setTxtGraceDaysSI("");
        setTxtCommChargesSI("");
        setTxtRettCommChargesSI("");
        setCboMoRSI("");
        setTxtBeneficiarySI("");
        setCboWeekDay("");
        setCboWeek("");
        setCboSpecificDate("");
        setRdoHolidayExecution_Yes(false);
        setRdoHolidayExecution_No(false);
        setRdoSIAutoPosting_Yes(false);
        setRdoSIAutoPosting_No(false);
        setTxtForwardCount("");
        setCboExecConfig("");
//        setTxtFailureCharges("");
//        setCboExecConfig("");
        setTxtAcceptanceCharges("");
        setTxtExecutionCharges("");
        setTxtFailureST("");
        setTxtServiceTax("");
        setTxtFailureCharges("");
        setCboExecutionDay("");
        setLbl1LastExecutonDate("");
        setLbl1NextExecutonDate("");
        setLbl1HolidayExecutonDate("");
        setLbl1FwdExecutionDate("");
        setChkIntalmentsYN(false);
        setChkPendingInstalment(false);
        setNoOfInst("");
        chkSuspendUser=false;
        
    }
    
    public void getDebitProductIdByType(String prodType) throws Exception{//String productType)
        /** The data to be show in Combo Box other than LOOKUP_MASTER table
         * Show Product Id */
        HashMap lookUpHash = new HashMap();
      //  lookUpHash.put(CommonConstants.MAP_NAME,"Charges.getProductData" + prodType);
        //added by rishad
        if(prodType.equals("TD"))
        {
             lookUpHash.put(CommonConstants.MAP_NAME,"Standing.getProductData" + prodType);
        }
        else
        {
             lookUpHash.put(CommonConstants.MAP_NAME,"Charges.getProductData" + prodType);
        }
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmProdIDDSI = new ComboBoxModel(key,value);
    }
    
    public void getCreditProductIdByType(String prodType) throws Exception{//String productType)
        /** The data to be show in Combo Box other than LOOKUP_MASTER table
         * Show Product Id */
        HashMap lookUpHash = new HashMap();
        if (prodType.equals("TD"))
            lookUpHash.put(CommonConstants.MAP_NAME,"Charges.getProductDataCredit" + prodType);
        else
            lookUpHash.put(CommonConstants.MAP_NAME,"Charges.getProductData" + prodType);
        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmProdIDCSI = new ComboBoxModel(key,value);
    }
    
    /**
     * Getter for property lbl1NextExecutonDate.
     * @return Value of property lbl1NextExecutonDate.
     */
    public java.lang.String getLbl1NextExecutonDate() {
        return lbl1NextExecutonDate;
    }
    
    /**
     * Setter for property lbl1NextExecutonDate.
     * @param lbl1NextExecutonDate New value of property lbl1NextExecutonDate.
     */
    public void setLbl1NextExecutonDate(java.lang.String lbl1NextExecutonDate) {
        this.lbl1NextExecutonDate = lbl1NextExecutonDate;
    }
    
    /**
     * Getter for property lbl1LastExecutonDate.
     * @return Value of property lbl1LastExecutonDate.
     */
    public java.lang.String getLbl1LastExecutonDate() {
        return lbl1LastExecutonDate;
    }
    
    /**
     * Setter for property lbl1LastExecutonDate.
     * @param lbl1LastExecutonDate New value of property lbl1LastExecutonDate.
     */
    public void setLbl1LastExecutonDate(java.lang.String lbl1LastExecutonDate) {
        this.lbl1LastExecutonDate = lbl1LastExecutonDate;
    }
    
    /**
     * Getter for property chkSuspendUser.
     * @return Value of property chkSuspendUser.
     */
    public boolean isChkSuspendUser() {
        return chkSuspendUser;
    }    
    
    /**
     * Setter for property chkSuspendUser.
     * @param chkSuspendUser New value of property chkSuspendUser.
     */
    public void setChkSuspendUser(boolean chkSuspendUser) {
        this.chkSuspendUser = chkSuspendUser;
    }    
    
    /**

     * Getter for property suspended.
     * @return Value of property suspended.
     */
    public java.lang.String getSuspended() {
        return suspended;
    }
    
    /**
     * Setter for property suspended.
     * @param suspended New value of property suspended.
     */
    public void setSuspended(java.lang.String suspended) {
        this.suspended = suspended;
    }
    
    /**
     * Getter for property txtSiDt.
     * @return Value of property txtSiDt.
     */
    public java.lang.String getTxtSiDt() {
        return txtSiDt;
    }
    
    /**
     * Setter for property txtSiDt.
     * @param txtSiDt New value of property txtSiDt.
     */
    public void setTxtSiDt(java.lang.String txtSiDt) {
        this.txtSiDt = txtSiDt;
    }
    
    /**
     * Getter for property lbl1HolidayExecutonDate.
     * @return Value of property lbl1HolidayExecutonDate.
     */
    public java.lang.String getLbl1HolidayExecutonDate() {
        return lbl1HolidayExecutonDate;
    }
    
    /**
     * Setter for property lbl1HolidayExecutonDate.
     * @param lbl1HolidayExecutonDate New value of property lbl1HolidayExecutonDate.
     */
    public void setLbl1HolidayExecutonDate(java.lang.String lbl1HolidayExecutonDate) {
        this.lbl1HolidayExecutonDate = lbl1HolidayExecutonDate;
    }
    
    /**
     * Getter for property lbl1FwdExecutionDate.
     * @return Value of property lbl1FwdExecutionDate.
     */
    public java.lang.String getLbl1FwdExecutionDate() {
        return lbl1FwdExecutionDate;
    }
    
    /**
     * Setter for property lbl1FwdExecutionDate.
     * @param lbl1FwdExecutionDate New value of property lbl1FwdExecutionDate.
     */
    public void setLbl1FwdExecutionDate(java.lang.String lbl1FwdExecutionDate) {
        this.lbl1FwdExecutionDate = lbl1FwdExecutionDate;
    }
    
    /**
     * Getter for property txtServiceTax.
     * @return Value of property txtServiceTax.
     */
    public java.lang.String getTxtServiceTax() {
        return txtServiceTax;
    }
    
    /**
     * Setter for property txtServiceTax.
     * @param txtServiceTax New value of property txtServiceTax.
     */
    public void setTxtServiceTax(java.lang.String txtServiceTax) {
        this.txtServiceTax = txtServiceTax;
    }
    
    /**
     * Getter for property txtFailureST.
     * @return Value of property txtFailureST.
     */
    public java.lang.String getTxtFailureST() {
        return txtFailureST;
    }
    
    /**
     * Setter for property txtFailureST.
     * @param txtFailureST New value of property txtFailureST.
     */
    public void setTxtFailureST(java.lang.String txtFailureST) {
        this.txtFailureST = txtFailureST;
    }
    
    /**
     * Getter for property dtdSuspendDt.
     * @return Value of property dtdSuspendDt.
     */
    public java.lang.String getDtdSuspendDt() {
        return dtdSuspendDt;
    }
    
    /**
     * Setter for property dtdSuspendDt.
     * @param dtdSuspendDt New value of property dtdSuspendDt.
     */
    public void setDtdSuspendDt(java.lang.String dtdSuspendDt) {
        this.dtdSuspendDt = dtdSuspendDt;
    }
    
    /**
     * Getter for property nxtDt.
     * @return Value of property nxtDt.
     */
    public java.util.Date getNxtDt() {
        return nxtDt;
    }
    
    /**
     * Setter for property nxtDt.
     * @param nxtDt New value of property nxtDt.
     */
    public void setNxtDt(java.util.Date nxtDt) {
        this.nxtDt = nxtDt;
    }
    
    /**
     * Getter for property suspDt.
     * @return Value of property suspDt.
     */
    public java.util.Date getSuspDt() {
        return suspDt;
    }
    
    /**
     * Setter for property suspDt.
     * @param suspDt New value of property suspDt.
     */
    public void setSuspDt(java.util.Date suspDt) {
        this.suspDt = suspDt;
    }
    
    /**
     * Getter for property freq.
     * @return Value of property freq.
     */
    public java.lang.String getFreq() {
        return freq;
    }
    
    /**
     * Setter for property freq.
     * @param freq New value of property freq.
     */
    public void setFreq(java.lang.String freq) {
        this.freq = freq;
    }
    
    /**
     * Getter for property checkThisCDate.
     * @return Value of property checkThisCDate.
     */
    public java.util.Date getCheckThisCDate() {
        return checkThisCDate;
    }
    
    /**
     * Setter for property checkThisCDate.
     * @param checkThisCDate New value of property checkThisCDate.
     */
    public void setCheckThisCDate(java.util.Date checkThisCDate) {
        this.checkThisCDate = checkThisCDate;
    }
    
    /**
     * Getter for property lstDt.
     * @return Value of property lstDt.
     */
    public java.util.Date getLstDt() {
        return lstDt;
    }
    
    /**
     * Setter for property lstDt.
     * @param lstDt New value of property lstDt.
     */
    public void setLstDt(java.util.Date lstDt) {
        this.lstDt = lstDt;
    }
    
    /**
     * Getter for property endDt.
     * @return Value of property endDt.
     */
    public java.util.Date getEndDt() {
        return endDt;
    }
    
    /**
     * Setter for property endDt.
     * @param endDt New value of property endDt.
     */
    public void setEndDt(java.util.Date endDt) {
        this.endDt = endDt;
    }
    
    /**
     * Getter for property crdActLst.
     * @return Value of property crdActLst.
     */
    public java.util.ArrayList getCrdActLst() {
        return crdActLst;
    }    
    
    /**
     * Setter for property crdActLst.
     * @param crdActLst New value of property crdActLst.
     */
    public void setCrdActLst(java.util.ArrayList crdActLst) {
        this.crdActLst = crdActLst;
    }
    
    /**
     * Getter for property chkCloseSI.
     * @return Value of property chkCloseSI.
     */
    public boolean isChkCloseSI() {
        return chkCloseSI;
    }
    
    /**
     * Setter for property chkCloseSI.
     * @param chkCloseSI New value of property chkCloseSI.
     */
    public void setChkCloseSI(boolean chkCloseSI) {
        this.chkCloseSI = chkCloseSI;
    }
    
    /**
     * Getter for property close.
     * @return Value of property close.
     */
    public java.lang.String getClose() {
        return close;
    }
    
    /**
     * Setter for property close.
     * @param close New value of property close.
     */
    public void setClose(java.lang.String close) {
        this.close = close;
    }
    
}