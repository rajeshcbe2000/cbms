/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LodgementBillsOB.java
 *
 * Created on Mon Feb 07 12:02:03 IST 2005
 */

package com.see.truetransact.ui.bills.lodgement.multipleaccountlodgement;

import com.see.truetransact.ui.bills.lodgement.*;
import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import com.see.truetransact.commonutil.TTException;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.bills.lodgement.LodgementMasterTO;
import com.see.truetransact.transferobject.bills.lodgement.LodgementChequeTO;
import com.see.truetransact.transferobject.bills.lodgement.LodgementHundiTO;
import com.see.truetransact.transferobject.bills.lodgement.LodgementInstructionsTO;
import com.see.truetransact.transferobject.bills.lodgement.LodgementInstrTO;
import com.see.truetransact.transferobject.bills.lodgement.LodgementRemitTO;
import com.see.truetransact.transferobject.bills.lodgement.LodgementBillRatesTO;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.transferobject.bills.lodgement.*;
import com.see.truetransact.transferobject.product.bills.BillsTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import com.see.truetransact.uicomponent.CObservable;
import java.util.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ashok Vijayakumar
 */

public class MultipleAccountLodgementOB extends CObservable{
    
    private boolean _isAvailable = true;
    private String txtOtherName = "";
    private String txtOtherAddress = "";
    private String cboOtherCity = "";
    private String cboOtherState = "";
    private String cboOtherCountry = "";
    private String txtOtherPinCode = "";
    private String cboReceivedFrom = "";
    private String cboBillsType = "";
    private String cboActivities = "";
    private String cboCustCategory = "";
    private String cboTranstype = "";
    private String txtReference = "";
    private String txtRateForDelay = "";
    private String cboProductType = "";
    private String cboProductId = "";
    private String cboProductID = "";
    private String txtAccountNo = "";
    private String txtAccountNum = "";
    private String txtBranchCode = "";
    private String txtBankCode = "";
    private String txtOtherBranchCode = "";
    private String txtDraweeName = "";
    private String txtDraweeAddress = "";
    private String cboDraweeCity = "";
    private String cboDraweeState = "";
    private String cboDraweeCountry = "";
    private String cboRemitProdID = "";
    private String cboRemitCity = "";
    private String cboRemitDraweeBank = "";
    private String cboRemitBranchCode = "";
    private String txtDraweePinCode = "";
    private String txtDraweeNo = "";
    private String txtDraweeBankNameVal = "";
    private String txtSendingTo = "";
    private String txtDraweeBankCode = "";
    private String txtDraweeBranchCode = "";
    private String txtDraweeBankName = "";
    private String txtDraweeBranchName = "";
    private String txtInstrumentNo = "";
    private String txtInstPrefix="";
    private static Date currDt = null;
    public String getTxtInstPrefix() {
        return txtInstPrefix;
    }
    public void setTxtInstPrefix(String txtInstPrefix) {
        this.txtInstPrefix = txtInstPrefix;
    }
    private String txtInstrumentAmount = "";
    private String txtLodgementId = "";
    private String txtRemitFavour = "";
    private String txtRemitFavour1 = "";
    private String txtInstAmt = "";
    private String txtInst1 = "";
    private String txtInst2 = "";
    private String txtMICR = "";
    private String txtPayeeName = "";
    private String txtRemarks = "";
    private String tdtInstrumentDate = "";
    private String tdtRemitInstDate = "";
    private String txtBillTenor = "";
    private String cboBillTenor = "";
    private String tdtDueDate = "";
    private String tdtAcceptanceDate = "";
    private String txtHundiNo = "";
    private boolean rdoBillAcceptance_Yes = false;
    private boolean rdoBillAcceptance_No = false;
    private boolean cRadio_ICC_Yes = false;
    private boolean cRadio_ICC_No = false;
    private String tdtHundiDate = "";
    private boolean rdoDraweeHundi_Yes = false;
    private boolean rdoDraweeHundi_No = false;
    private String txtHundiAmount = "";
    private String txtPayable = "";
    private String txtHundiRemarks = "";
    private String cboInstrumentType = "";
    private String txtInvoiceNumber = "";
    private String tdtInvoiceDate = "";
    private String txtInvoiceAmount = "";
    private String txtTransportCompany = "";
    private String tdtRRLRDate = "";
    private String txtRRLRNumber = "";
    private String txtGoodsValue = "";
    private String txtGoodsAssigned = "";
    private String cboStdInstruction = "";
    private String cboInstruction = "";
    private String txtStdInstruction = "";
    private String txtAreaParticular = "";
    private String txtAmount = "";
    private String txtServiceTax = "";
    private String txttotalServTax = "";
    private String txtTotalAmt = "";
    private String payableAt = "" ;
    private String subRegType = "" ;
    
    private String otherBankProdType = "" ;
    private String txtAccountHeadValue= "" ;
    private String txtRateForDelay1= "" ;
    private String txtIntDays= "" ;
    private String cboIntDays= "" ;
    private String txtDiscountRateBills= "" ;
    private String txtOverdueRateBills= "" ;
    private String txtRateForCBP= "" ;
    private String txtAtParLimit= "" ;
    private String txtCleanBills= "" ;
    private String txtTransitPeriod= "" ;
    private String cboTransitPeriod= "" ;
    private String txtDefaultPostage= "" ;
    private String operatesLikeValue = "";
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private static MultipleAccountLodgementOB objLodgementBillsOB;//Singleton Object Reference
    private LodgementBillsRB resourceBundle = new LodgementBillsRB();
    private final static Logger log = Logger.getLogger(MultipleAccountLodgementOB.class);//Creating Instace of Log
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private ProxyFactory proxy;
    private final String YES = "Y";
    private final String NO = "N";
    private HashMap keyValue;
    private ArrayList key,value;
    private ArrayList rowData = new ArrayList();//This ArrayList holds the all the rows of the tblInsructions
    private ArrayList data;//This Arraylist holds the data in the particular row
    private ArrayList dataInst;//This Arraylist holds the data in the particular row
    private ArrayList existingData;//This ArrayList contains already existing data in the row its populated with data
    private ArrayList existingDataInst;//This ArrayList contains already existing data in the row its populated with data
    //when edit button is clicked in the ui, also contains the contains the data which is recently changed by clicking the row in table
    private ArrayList newData = new ArrayList();//This ArrayList holds  the data which is newly inserted
    private ArrayList newDataInstr = new ArrayList();//This ArrayList holds  the data which is newly inserted
    private ArrayList removedRow;//populated when a data is removed from the table model
    private ArrayList removedRowInst;//populated when a data is removed from the table model
    private ArrayList columnElement;//ArrayList to hold each column value of the table
    private ArrayList insertData;//ArrayList that contains the data to be inserted
    private ArrayList updateData;//ArrayList that contains the data to be updated
    private ArrayList deletedData;//ArrayList that containst the data to be deleted
    private ArrayList insertDataInst;//ArrayList that contains the data to be inserted
    private ArrayList updateDataInst;//ArrayList that contains the data to be updated
    private ArrayList deletedDataInst;//ArrayList that containst the data to be deleted
    private ArrayList newInstructionRow = null;//ArrayList to hold temporily the newly inserted data to check for duplication
    private ArrayList newInstructionRowInstr = null;//ArrayList to hold temporily the newly inserted data to check for duplication
    private ArrayList tblInstructionData;
    private ArrayList tblInstr;
    private ComboBoxModel cbmOtherCity,cbmOtherState,cbmOtherCountry,cbmReceivedFrom,cbmBillsType,cbmActivities,cbmCustCategory,cbmProductType,cbmProductId,cbmDraweeCity,cbmDraweeState,cbmDraweeCountry,cbmBillTenor,cbmStdInstruction,cbmInstrumentType,cbmRemitProdID,cbmRemitCity,cbmRemitDraweeBank,cbmRemitBranchCode,cbmTranstype,cbmInstruction,cbmProductID,cbmIntDays,cbmTransitPeriod;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private ArrayList tblHeadings;
    private ArrayList tblHeadings1;
    private ArrayList tblHeadings2;
    private String remitStatus;
    private EnhancedTableModel tbmInstructions;
    private EnhancedTableModel tbmInstructions1;
    private EnhancedTableModel tbmInstructions2;
    private int row = 0;
    private boolean deletingExists = false;
    private boolean deletingExistsInstr = false;
    private ArrayList existData = null;
    private HashMap _authorizeMap ;
    
    private TableModel _tableModel;
    private CTable _tblData;
    private ArrayList _heading;
    private HashMap dataHash;
    private ArrayList tranData;//This Arraylist holds the data in the particular row
    private EnhancedTableModel tblOBDModel;
    private ArrayList tblOBDetailsTitles = new ArrayList();
    private ArrayList tblData=null;
    private String tdtRemittedDt="";
    private boolean isFilled=false;
    public HashMap transaction =new HashMap();
    private String chkClering="";
    private String billsNo="";
    private HashMap serviceTax_Map=null;
    private String lblServiceTaxval="";
    private String lodgementId = "";
    private TableModel tbmActNo;
    private ArrayList tblHeadingsForMultipleLodgement;
    private LinkedHashMap lodgementRecords = new LinkedHashMap();
    private LinkedHashMap tempLodgementRecords = new LinkedHashMap();
    LodgementMasterTO objLodgementMasterTO ;
    MultipleLodgementMasterTO objMultipleLodgementMasterTO ;
    private String txtSplitAmnt ="";
    private String lblCustName = "";
    
    
    HashMap multipleServiceTaxMap = new HashMap();
    HashMap multipleInstructionListMap = new HashMap();
    HashMap proxyResultMap;
  
    public String getLblCustName() {
        return lblCustName;
    }

    public void setLblCustName(String lblCustName) {
        this.lblCustName = lblCustName;
    }

    public String getTxtSplitAmnt() {
        return txtSplitAmnt;
    }

    public void setTxtSplitAmnt(String txtSplitAmnt) {
        this.txtSplitAmnt = txtSplitAmnt;
    }
    
    public ArrayList getNewInstructionRowInstr() {
        return newInstructionRowInstr;
    }

    public void setNewInstructionRowInstr(ArrayList newInstructionRowInstr) {
        this.newInstructionRowInstr = newInstructionRowInstr;
    }

    public TableModel getTbmActNo() {
        return tbmActNo;
    }

    public void setTbmActNo(TableModel tbmActNo) {
        this.tbmActNo = tbmActNo;
    }

    public String getLblServiceTaxval() {
        return lblServiceTaxval;
    }
    public void setLblServiceTaxval(String lblServiceTaxval) {
        this.lblServiceTaxval = lblServiceTaxval;
    }
    public HashMap getServiceTax_Map() {
        return serviceTax_Map;
    }
    public void setServiceTax_Map(HashMap serviceTax_Map) {
        this.serviceTax_Map = serviceTax_Map;
    }
    public String getBillsNo() {
        return billsNo;
    }
    public void setBillsNo(String billsNo) {
        this.billsNo = billsNo;
    }
    public String getChkClering() {
        return chkClering;
    }
    public void setChkClering(String chkClering) {
        this.chkClering = chkClering;
    }
    public HashMap getTransaction() {
        return transaction;
    }
    public void setTransaction(HashMap transaction) {
        this.transaction = transaction;
    }

    public String getLodgementId() {
        return lodgementId;
    }

    public void setLodgementId(String lodgementId) {
        this.lodgementId = lodgementId;
    }
    
    
    /** Consturctor Declaration  for  LodgementBillsOB */
    private MultipleAccountLodgementOB() {
        try {
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            initComboBoxModel();
            createTblHeadings();
            createTblHeadingsForMultipleLodgement();
            createTbmInstructions();
            createTbmLodgementDetails();
            createTblInstHeadings();
            createTbmInstr();
            createTblActHeadings();
            createTbmAct();
            fillDropdown();
            setTitle();
            tblOBDModel = new EnhancedTableModel(null, tblOBDetailsTitles);
         
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            log.info("Creating ParameterOB...");
            currDt = ClientUtil.getCurrentDate();
            objLodgementBillsOB = new MultipleAccountLodgementOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
      private void setTitle(){
         tblOBDetailsTitles.add("Select");
         tblOBDetailsTitles.add("Lodgement Number");
         tblOBDetailsTitles.add("Lodgement Date");
         tblOBDetailsTitles.add("Amount");
         tblOBDetailsTitles.add("Instrument no");
         tblOBDetailsTitles.add("Borrower Account no");
         
         
     }
    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "LodgementJNDI");
        map.put(CommonConstants.HOME, "bills.lodgement.LodgementHome");
        map.put(CommonConstants.REMOTE, "bills.lodgement.Lodgement");
    }
    
    /** Creates an instance of ArrayList tblHeadings and headings to the tables are added **/
    private void createTblHeadings(){
        tblHeadings = new ArrayList();
        tblHeadings.add(resourceBundle.getString("tblHeading1"));
        tblHeadings.add(resourceBundle.getString("tblHeading2"));
        tblHeadings.add(resourceBundle.getString("tblHeading3"));
        tblHeadings.add(resourceBundle.getString("tblHeading4"));
        tblHeadings.add("Acct No");
        tblHeadings.add("Product Id");
        tblHeadings.add("Product Type");
        tblHeadings.add("Individual amount");
        tblHeadings.add("Other Bank charges"); 
        tblHeadings.add("Amount");
    }
    
    /** Creates an instance of EnhancedTableModel with arrayList for heading **/
    private void createTbmInstructions(){
        tbmInstructions = new EnhancedTableModel(null, tblHeadings);
    }
    
    
    private void createTblHeadingsForMultipleLodgement() {
        tblHeadingsForMultipleLodgement = new ArrayList();
        tblHeadingsForMultipleLodgement.add("Prod Id");
        tblHeadingsForMultipleLodgement.add("Prod Type");
        tblHeadingsForMultipleLodgement.add("Acct No");
        tblHeadingsForMultipleLodgement.add("Name");    
        tblHeadingsForMultipleLodgement.add("Individual Bill Amount"); 
    }
    
    private void createTbmLodgementDetails(){
        tbmActNo = new TableModel(null, tblHeadingsForMultipleLodgement);
    }
        
    public void setTableDetails(ArrayList list){
        int toSize=list.size();
        for (int i=0;i<toSize;i++){
            tblData = new ArrayList();
            HashMap columnData =(HashMap) list.get(i);
             boolean flag=false;             
             tblData.add(flag);
             tblData.add(columnData.get("LODGEMENT_ID"));
             tblData.add(columnData.get("CREATED_DT"));
             tblData.add(columnData.get("AMOUNT"));
             tblData.add(columnData.get("INSTRUMENT_NO"));
             tblData.add(columnData.get("BORROW_ACCT_NUM"));
           
            tblOBDModel.insertRow(0,tblData);
            isFilled=true;
        }
        
        
    }
    
    public void resetTable(){
        tblOBDModel.setDataArrayList(null,tblOBDetailsTitles);
    }
     private void createTblInstHeadings(){
        tblHeadings1 = new ArrayList();
        tblHeadings1.add(resourceBundle.getString("tblHeadingInst1"));
        tblHeadings1.add(resourceBundle.getString("tblHeadingInst2"));
        tblHeadings1.add(resourceBundle.getString("tblHeadingInst3"));
//        tblHeadings1.add(resourceBundle.getString("tblHeading4"));
    }
    
    /** Creates an instance of EnhancedTableModel with arrayList for heading **/
    private void createTbmInstr(){
        tbmInstructions1 = new EnhancedTableModel(null, tblHeadings1);
    }
    
     private void createTblActHeadings(){
        tblHeadings2 = new ArrayList();
        tblHeadings2.add(resourceBundle.getString("tblHeadingAct1"));
        tblHeadings2.add(resourceBundle.getString("tblHeadingAct2"));
        tblHeadings2.add(resourceBundle.getString("tblHeadingAct3"));
//        tblHeadings1.add(resourceBundle.getString("tblHeading4"));
    }
    
    /** Creates an instance of EnhancedTableModel with arrayList for heading **/
    private void createTbmAct(){
        tbmInstructions2 = new EnhancedTableModel(null, tblHeadings2);
    }
    /**
     * Returns an instance of TDSExemptionOB.
     * @return  TDSExemptionOB
     */
    
    public static MultipleAccountLodgementOB getInstance()throws Exception{
        return objLodgementBillsOB;
    }
    
    // Setter method for txtOtherName
    void setTxtOtherName(String txtOtherName){
        this.txtOtherName = txtOtherName;
        setChanged();
    }
    // Getter method for txtOtherName
    String getTxtOtherName(){
        return this.txtOtherName;
    }
    
    // Setter method for txtOtherAddress
    void setTxtOtherAddress(String txtOtherAddress){
        this.txtOtherAddress = txtOtherAddress;
        setChanged();
    }
    // Getter method for txtOtherAddress
    String getTxtOtherAddress(){
        return this.txtOtherAddress;
    }
    
    // Setter method for cboOtherCity
    void setCboOtherCity(String cboOtherCity){
        this.cboOtherCity = cboOtherCity;
        setChanged();
    }
    // Getter method for cboOtherCity
    String getCboOtherCity(){
        return this.cboOtherCity;
    }
    
    /**
     * Getter for property cbmOtherCity.
     * @return Value of property cbmOtherCity.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmOtherCity() {
        return cbmOtherCity;
    }
    
    /**
     * Setter for property cbmOtherCity.
     * @param cbmOtherCity New value of property cbmOtherCity.
     */
    public void setCbmOtherCity(com.see.truetransact.clientutil.ComboBoxModel cbmOtherCity) {
        this.cbmOtherCity = cbmOtherCity;
        setChanged();
    }
    
    // Setter method for cboOtherState
    void setCboOtherState(String cboOtherState){
        this.cboOtherState = cboOtherState;
        setChanged();
    }
    // Getter method for cboOtherState
    String getCboOtherState(){
        return this.cboOtherState;
    }
    /**
     * Getter for property cbmOtherState.
     * @return Value of property cbmOtherState.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmOtherState() {
        return cbmOtherState;
    }
    
    /**
     * Setter for property cbmOtherState.
     * @param cbmOtherState New value of property cbmOtherState.
     */
    public void setCbmOtherState(com.see.truetransact.clientutil.ComboBoxModel cbmOtherState) {
        this.cbmOtherState = cbmOtherState;
        setChanged();
    }
    
    // Setter method for cboOtherCountry
    void setCboOtherCountry(String cboOtherCountry){
        this.cboOtherCountry = cboOtherCountry;
        setChanged();
    }
    // Getter method for cboOtherCountry
    String getCboOtherCountry(){
        return this.cboOtherCountry;
    }
    
    /**
     * Getter for property cbmOtherCountry.
     * @return Value of property cbmOtherCountry.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmOtherCountry() {
        return cbmOtherCountry;
    }
    
    /**
     * Setter for property cbmOtherCountry.
     * @param cbmOtherCountry New value of property cbmOtherCountry.
     */
    public void setCbmOtherCountry(com.see.truetransact.clientutil.ComboBoxModel cbmOtherCountry) {
        this.cbmOtherCountry = cbmOtherCountry;
        setChanged();
    }
    
    // Setter method for txtOtherPinCode
    void setTxtOtherPinCode(String txtOtherPinCode){
        this.txtOtherPinCode = txtOtherPinCode;
        setChanged();
    }
    // Getter method for txtOtherPinCode
    String getTxtOtherPinCode(){
        return this.txtOtherPinCode;
    }
    
    // Setter method for cboReceivedFrom
    void setCboReceivedFrom(String cboReceivedFrom){
        this.cboReceivedFrom = cboReceivedFrom;
        setChanged();
    }
    // Getter method for cboReceivedFrom
    String getCboReceivedFrom(){
        return this.cboReceivedFrom;
    }
    
    /**
     * Setter for property cbmReceivedFrom.
     * @param cbmReceivedFrom New value of property cbmReceivedFrom.
     */
    public void setCbmReceivedFrom(com.see.truetransact.clientutil.ComboBoxModel cbmReceivedFrom) {
        this.cbmReceivedFrom = cbmReceivedFrom;
        setChanged();
    }
    
    /**
     * Setter for property cbmBillsType.
     * @param cbmBillsType New value of property cbmBillsType.
     */
    public void setCbmBillsType(com.see.truetransact.clientutil.ComboBoxModel cbmBillsType) {
        this.cbmBillsType = cbmBillsType;
        setChanged();
    }
    
    /**
     * Getter for property cbmReceivedFrom.
     * @return Value of property cbmReceivedFrom.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmReceivedFrom() {
        return cbmReceivedFrom;
    }
    
    // Setter method for cboBillsType
    public void setCboBillsType(String cboBillsType){
        this.cboBillsType = cboBillsType;
        setChanged();
    }
    
    // Getter method for cboBillsType
    public String getCboBillsType(){
        return this.cboBillsType;
    }
    
    /**
     * Getter for property cbmBillsType.
     * @return Value of property cbmBillsType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBillsType() {
        return cbmBillsType;
    }
    
    // Setter method for txtReference
    void setTxtReference(String txtReference){
        this.txtReference = txtReference;
        setChanged();
    }
    // Getter method for txtReference
    String getTxtReference(){
        return this.txtReference;
    }
    
    // Setter method for cboProductType
    void setCboProductType(String cboProductType){
        this.cboProductType = cboProductType;
        setChanged();
    }
    // Getter method for cboProductType
    String getCboProductType(){
        return this.cboProductType;
    }
    
    /**
     * Getter for property cbmProductType.
     * @return Value of property cbmProductType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProductType() {
        return cbmProductType;
    }
    
    /**
     * Setter for property cbmProductType.
     * @param cbmProductType New value of property cbmProductType.
     */
    public void setCbmProductType(com.see.truetransact.clientutil.ComboBoxModel cbmProductType) {
        this.cbmProductType = cbmProductType;
        setChanged();
    }
    // Setter method for cboProductId
    void setCboProductId(String cboProductId){
        this.cboProductId = cboProductId;
        setChanged();
    }
    // Getter method for cboProductId
    String getCboProductId(){
        return this.cboProductId;
    }
    
    /**
     * Getter for property cbmProductId.
     * @return Value of property cbmProductId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProductId() {
        return cbmProductId;
    }
    
    /**
     * Setter for property cbmProductId.
     * @param cbmProductId New value of property cbmProductId.
     */
    public void setCbmProductId(com.see.truetransact.clientutil.ComboBoxModel cbmProductId) {
        this.cbmProductId = cbmProductId;
        setChanged();
    }
    
    // Setter method for txtAccountNo
    void setTxtAccountNo(String txtAccountNo){
        this.txtAccountNo = txtAccountNo;
        setChanged();
    }
    // Getter method for txtAccountNo
    String getTxtAccountNo(){
        return this.txtAccountNo;
    }
    
    // Setter method for txtBranchCode
    void setTxtBranchCode(String txtBranchCode){
        this.txtBranchCode = txtBranchCode;
        setChanged();
    }
    // Getter method for txtBranchCode
    String getTxtBranchCode(){
        return this.txtBranchCode;
    }
    
    // Setter method for txtBankCode
    void setTxtBankCode(String txtBankCode){
        this.txtBankCode = txtBankCode;
        setChanged();
    }
    // Getter method for txtBankCode
    String getTxtBankCode(){
        return this.txtBankCode;
    }
    
    // Setter method for txtOtherBranchCode
    void setTxtOtherBranchCode(String txtOtherBranchCode){
        this.txtOtherBranchCode = txtOtherBranchCode;
        setChanged();
    }
    // Getter method for txtOtherBranchCode
    String getTxtOtherBranchCode(){
        return this.txtOtherBranchCode;
    }
    
    // Setter method for txtDraweeName
    void setTxtDraweeName(String txtDraweeName){
        this.txtDraweeName = txtDraweeName;
        setChanged();
    }
    // Getter method for txtDraweeName
    String getTxtDraweeName(){
        return this.txtDraweeName;
    }
    
    // Setter method for txtDraweeAddress
    void setTxtDraweeAddress(String txtDraweeAddress){
        this.txtDraweeAddress = txtDraweeAddress;
        setChanged();
    }
    // Getter method for txtDraweeAddress
    String getTxtDraweeAddress(){
        return this.txtDraweeAddress;
    }
    
    // Setter method for cboDraweeCity
    void setCboDraweeCity(String cboDraweeCity){
        this.cboDraweeCity = cboDraweeCity;
        setChanged();
    }
    // Getter method for cboDraweeCity
    String getCboDraweeCity(){
        return this.cboDraweeCity;
    }
    
    /**
     * Getter for property cbmDraweeCity.
     * @return Value of property cbmDraweeCity.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDraweeCity() {
        return cbmDraweeCity;
    }
    
    /**
     * Setter for property cbmDraweeCity.
     * @param cbmDraweeCity New value of property cbmDraweeCity.
     */
    public void setCbmDraweeCity(com.see.truetransact.clientutil.ComboBoxModel cbmDraweeCity) {
        this.cbmDraweeCity = cbmDraweeCity;
        setChanged();
    }
    
    // Setter method for cboDraweeState
    void setCboDraweeState(String cboDraweeState){
        this.cboDraweeState = cboDraweeState;
        setChanged();
    }
    // Getter method for cboDraweeState
    String getCboDraweeState(){
        return this.cboDraweeState;
    }
    
    /**
     * Getter for property cbmDraweeState.
     * @return Value of property cbmDraweeState.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDraweeState() {
        return cbmDraweeState;
    }
    
    /**
     * Setter for property cbmDraweeState.
     * @param cbmDraweeState New value of property cbmDraweeState.
     */
    public void setCbmDraweeState(com.see.truetransact.clientutil.ComboBoxModel cbmDraweeState) {
        this.cbmDraweeState = cbmDraweeState;
        setChanged();
    }
    
    // Setter method for cboDraweeCountry
    void setCboDraweeCountry(String cboDraweeCountry){
        this.cboDraweeCountry = cboDraweeCountry;
        setChanged();
    }
    // Getter method for cboDraweeCountry
    String getCboDraweeCountry(){
        return this.cboDraweeCountry;
    }
    
    /**
     * Getter for property cbmDraweeCountry.
     * @return Value of property cbmDraweeCountry.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDraweeCountry() {
        return cbmDraweeCountry;
    }
    
    /**
     * Setter for property cbmDraweeCountry.
     * @param cbmDraweeCountry New value of property cbmDraweeCountry.
     */
    public void setCbmDraweeCountry(com.see.truetransact.clientutil.ComboBoxModel cbmDraweeCountry) {
        this.cbmDraweeCountry = cbmDraweeCountry;
        setChanged();
    }
    
    // Setter method for txtDraweePinCode
    void setTxtDraweePinCode(String txtDraweePinCode){
        this.txtDraweePinCode = txtDraweePinCode;
        setChanged();
    }
    // Getter method for txtDraweePinCode
    String getTxtDraweePinCode(){
        return this.txtDraweePinCode;
    }
    
    // Setter method for txtDraweeNo
    void setTxtDraweeNo(String txtDraweeNo){
        this.txtDraweeNo = txtDraweeNo;
        setChanged();
    }
    // Getter method for txtDraweeNo
    String getTxtDraweeNo(){
        return this.txtDraweeNo;
    }
    
    // Setter method for txtSendingTo
    void setTxtSendingTo(String txtSendingTo){
        this.txtSendingTo = txtSendingTo;
        setChanged();
    }
    // Getter method for txtSendingTo
    String getTxtSendingTo(){
        return this.txtSendingTo;
    }
    
    // Setter method for txtDraweeBankCode
    void setTxtDraweeBankCode(String txtDraweeBankCode){
        this.txtDraweeBankCode = txtDraweeBankCode;
        setChanged();
    }
    // Getter method for txtDraweeBankCode
    String getTxtDraweeBankCode(){
        return this.txtDraweeBankCode;
    }
    
    // Setter method for txtDraweeBranchCode
    void setTxtDraweeBranchCode(String txtDraweeBranchCode){
        this.txtDraweeBranchCode = txtDraweeBranchCode;
        setChanged();
    }
    // Getter method for txtDraweeBranchCode
    String getTxtDraweeBranchCode(){
        return this.txtDraweeBranchCode;
    }
    
    // Setter method for txtDraweeBankName
    void setTxtDraweeBankName(String txtDraweeBankName){
        this.txtDraweeBankName = txtDraweeBankName;
        setChanged();
    }
    // Getter method for txtDraweeBankName
    String getTxtDraweeBankName(){
        return this.txtDraweeBankName;
    }
    
    // Setter method for txtDraweeBranchName
    void setTxtDraweeBranchName(String txtDraweeBranchName){
        this.txtDraweeBranchName = txtDraweeBranchName;
        setChanged();
    }
    // Getter method for txtDraweeBranchName
    String getTxtDraweeBranchName(){
        return this.txtDraweeBranchName;
    }
    
    // Setter method for txtInstrumentDate
    void setTxtInstrumentNo(String txtInstrumentNo){
        this.txtInstrumentNo = txtInstrumentNo;
        setChanged();
    }
    // Getter method for txtInstrumentDate
    String getTxtInstrumentNo(){
        return this.txtInstrumentNo;
    }
    
    // Setter method for txtInstrumentAmount
    void setTxtInstrumentAmount(String txtInstrumentAmount){
        this.txtInstrumentAmount = txtInstrumentAmount;
        setChanged();
    }
    // Getter method for txtInstrumentAmount
    String getTxtInstrumentAmount(){
        return this.txtInstrumentAmount;
    }
    
    // Setter method for txtMICR
    void setTxtMICR(String txtMICR){
        this.txtMICR = txtMICR;
        setChanged();
    }
    // Getter method for txtMICR
    String getTxtMICR(){
        return this.txtMICR;
    }
    
    // Setter method for txtPayeeName
    void setTxtPayeeName(String txtPayeeName){
        this.txtPayeeName = txtPayeeName;
        setChanged();
    }
    // Getter method for txtPayeeName
    String getTxtPayeeName(){
        return this.txtPayeeName;
    }
    
    // Setter method for txtRemarks
    void setTxtRemarks(String txtRemarks){
        this.txtRemarks = txtRemarks;
        setChanged();
    }
    // Getter method for txtRemarks
    String getTxtRemarks(){
        return this.txtRemarks;
    }
    
    // Setter method for tdtInstrumentDate
    void setTdtInstrumentDate(String tdtInstrumentDate){
        this.tdtInstrumentDate = tdtInstrumentDate;
        setChanged();
    }
    // Getter method for tdtInstrumentDate
    String getTdtInstrumentDate(){
        return this.tdtInstrumentDate;
    }
    
    // Setter method for txtBillTenor
    void setTxtBillTenor(String txtBillTenor){
        this.txtBillTenor = txtBillTenor;
        setChanged();
    }
    // Getter method for txtBillTenor
    String getTxtBillTenor(){
        return this.txtBillTenor;
    }
    
    // Setter method for cbBIllTenor
    void setCboBillTenor(String cboBillTenor){
        this.cboBillTenor = cboBillTenor;
        setChanged();
    }
    // Getter method for cbBIllTenor
    String getCboBillTenor(){
        return this.cboBillTenor;
    }
    
    /**
     * Getter for property cbmBillTenor.
     * @return Value of property cbmBillTenor.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBillTenor() {
        return cbmBillTenor;
    }
    
    /**
     * Setter for property cbmBillTenor.
     * @param cbmBillTenor New value of property cbmBillTenor.
     */
    public void setCbmBillTenor(com.see.truetransact.clientutil.ComboBoxModel cbmBillTenor) {
        this.cbmBillTenor = cbmBillTenor;
        setChanged();
    }
    
    // Setter method for tdtDueDate
    void setTdtDueDate(String tdtDueDate){
        this.tdtDueDate = tdtDueDate;
        setChanged();
    }
    // Getter method for tdtDueDate
    String getTdtDueDate(){
        return this.tdtDueDate;
    }
    
    // Setter method for tdtAcceptanceDate
    void setTdtAcceptanceDate(String tdtAcceptanceDate){
        this.tdtAcceptanceDate = tdtAcceptanceDate;
        setChanged();
    }
    // Getter method for tdtAcceptanceDate
    String getTdtAcceptanceDate(){
        return this.tdtAcceptanceDate;
    }
    
    // Setter method for txtHundiNo
    void setTxtHundiNo(String txtHundiNo){
        this.txtHundiNo = txtHundiNo;
        setChanged();
    }
    // Getter method for txtHundiNo
    String getTxtHundiNo(){
        return this.txtHundiNo;
    }
    
    // Setter method for rdoBillAcceptance_Yes
    void setRdoBillAcceptance_Yes(boolean rdoBillAcceptance_Yes){
        this.rdoBillAcceptance_Yes = rdoBillAcceptance_Yes;
        setChanged();
    }
    // Getter method for rdoBillAcceptance_Yes
    boolean getRdoBillAcceptance_Yes(){
        return this.rdoBillAcceptance_Yes;
    }
    
    // Setter method for rdoBillAcceptance_No
    void setRdoBillAcceptance_No(boolean rdoBillAcceptance_No){
        this.rdoBillAcceptance_No = rdoBillAcceptance_No;
        setChanged();
    }
    // Getter method for rdoBillAcceptance_No
    boolean getRdoBillAcceptance_No(){
        return this.rdoBillAcceptance_No;
    }
    
    // Setter method for tdtHundiDate
    void setTdtHundiDate(String tdtHundiDate){
        this.tdtHundiDate = tdtHundiDate;
        setChanged();
    }
    // Getter method for tdtHundiDate
    String getTdtHundiDate(){
        return this.tdtHundiDate;
    }
    
    // Setter method for rdoDraweeHundi_Yes
    void setRdoDraweeHundi_Yes(boolean rdoDraweeHundi_Yes){
        this.rdoDraweeHundi_Yes = rdoDraweeHundi_Yes;
        setChanged();
    }
    // Getter method for rdoDraweeHundi_Yes
    boolean getRdoDraweeHundi_Yes(){
        return this.rdoDraweeHundi_Yes;
    }
    
    // Setter method for rdoDraweeHundi_No
    void setRdoDraweeHundi_No(boolean rdoDraweeHundi_No){
        this.rdoDraweeHundi_No = rdoDraweeHundi_No;
        setChanged();
    }
    // Getter method for rdoDraweeHundi_No
    boolean getRdoDraweeHundi_No(){
        return this.rdoDraweeHundi_No;
    }
    
    // Setter method for txtHundiAmount
    void setTxtHundiAmount(String txtHundiAmount){
        this.txtHundiAmount = txtHundiAmount;
        setChanged();
    }
    // Getter method for txtHundiAmount
    String getTxtHundiAmount(){
        return this.txtHundiAmount;
    }
    
    // Setter method for txtPayable
    void setTxtPayable(String txtPayable){
        this.txtPayable = txtPayable;
        setChanged();
    }
    // Getter method for txtPayable
    String getTxtPayable(){
        return this.txtPayable;
    }
    
    // Setter method for txtHundiRemarks
    void setTxtHundiRemarks(String txtHundiRemarks){
        this.txtHundiRemarks = txtHundiRemarks;
        setChanged();
    }
    // Getter method for txtHundiRemarks
    String getTxtHundiRemarks(){
        return this.txtHundiRemarks;
    }
    
    // Setter method for cboInstrumentType
    void setCboInstrumentType(String cboInstrumentType){
        this.cboInstrumentType = cboInstrumentType;
        setChanged();
    }
    // Getter method for cboInstrumentType
    String getCboInstrumentType(){
        return this.cboInstrumentType;
    }
    
    /**
     * Getter for property cbmInstrumentType.
     * @return Value of property cbmInstrumentType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmInstrumentType() {
        return cbmInstrumentType;
    }
    
    /**
     * Setter for property cbmInstrumentType.
     * @param cbmInstrumentType New value of property cbmInstrumentType.
     */
    public void setCbmInstrumentType(com.see.truetransact.clientutil.ComboBoxModel cbmInstrumentType) {
        this.cbmInstrumentType = cbmInstrumentType;
        setChanged();
    }
    
    // Setter method for txtInvoiceNumber
    void setTxtInvoiceNumber(String txtInvoiceNumber){
        this.txtInvoiceNumber = txtInvoiceNumber;
        setChanged();
    }
    // Getter method for txtInvoiceNumber
    String getTxtInvoiceNumber(){
        return this.txtInvoiceNumber;
    }
    
    // Setter method for tdtInvoiceDate
    void setTdtInvoiceDate(String tdtInvoiceDate){
        this.tdtInvoiceDate = tdtInvoiceDate;
        setChanged();
    }
    // Getter method for tdtInvoiceDate
    String getTdtInvoiceDate(){
        return this.tdtInvoiceDate;
    }
    
    // Setter method for txtInvoiceAmount
    void setTxtInvoiceAmount(String txtInvoiceAmount){
        this.txtInvoiceAmount = txtInvoiceAmount;
        setChanged();
    }
    // Getter method for txtInvoiceAmount
    String getTxtInvoiceAmount(){
        return this.txtInvoiceAmount;
    }
    
    // Setter method for txtTransportCompany
    void setTxtTransportCompany(String txtTransportCompany){
        this.txtTransportCompany = txtTransportCompany;
        setChanged();
    }
    // Getter method for txtTransportCompany
    String getTxtTransportCompany(){
        return this.txtTransportCompany;
    }
    
    // Setter method for tdtRRLRDate
    void setTdtRRLRDate(String tdtRRLRDate){
        this.tdtRRLRDate = tdtRRLRDate;
        setChanged();
    }
    // Getter method for tdtRRLRDate
    String getTdtRRLRDate(){
        return this.tdtRRLRDate;
    }
    
    // Setter method for txtRRLRNumber
    void setTxtRRLRNumber(String txtRRLRNumber){
        this.txtRRLRNumber = txtRRLRNumber;
        setChanged();
    }
    // Getter method for txtRRLRNumber
    String getTxtRRLRNumber(){
        return this.txtRRLRNumber;
    }
    
    // Setter method for txtGoodsValue
    void setTxtGoodsValue(String txtGoodsValue){
        this.txtGoodsValue = txtGoodsValue;
        setChanged();
    }
    // Getter method for txtGoodsValue
    String getTxtGoodsValue(){
        return this.txtGoodsValue;
    }
    
    // Setter method for txtGoodsAssigned
    void setTxtGoodsAssigned(String txtGoodsAssigned){
        this.txtGoodsAssigned = txtGoodsAssigned;
        setChanged();
    }
    // Getter method for txtGoodsAssigned
    String getTxtGoodsAssigned(){
        return this.txtGoodsAssigned;
    }
    
    // Setter method for cboStdInstruction
    void setCboStdInstruction(String cboStdInstruction){
        this.cboStdInstruction = cboStdInstruction;
        setChanged();
    }
    // Getter method for cboStdInstruction
    String getCboStdInstruction(){
        return this.cboStdInstruction;
    }
    
    /**
     * Getter for property cbmStdInstruction.
     * @return Value of property cbmStdInstruction.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmStdInstruction() {
        return cbmStdInstruction;
    }
    
    /**
     * Setter for property cbmStdInstruction.
     * @param cbmStdInstruction New value of property cbmStdInstruction.
     */
    public void setCbmStdInstruction(com.see.truetransact.clientutil.ComboBoxModel cbmStdInstruction) {
        this.cbmStdInstruction = cbmStdInstruction;
    }
    
    // Setter method for txtStdInstruction
    void setTxtStdInstruction(String txtStdInstruction){
        this.txtStdInstruction = txtStdInstruction;
        setChanged();
    }
    // Getter method for txtStdInstruction
    String getTxtStdInstruction(){
        return this.txtStdInstruction;
    }
    
    /**
     * Getter for property txtLodgementId.
     * @return Value of property txtLodgementId.
     */
    public java.lang.String getTxtLodgementId() {
        return txtLodgementId;
    }
    
    /**
     * Setter for property txtLodgementId.
     * @param txtLodgementId New value of property txtLodgementId.
     */
    public void setTxtLodgementId(java.lang.String txtLodgementId) {
        this.txtLodgementId = txtLodgementId;
        setChanged();
    }
    
    /**
     * Getter for property tbmInstructions.
     * @return Value of property tbmInstructions.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbmInstructions() {
        return tbmInstructions;
    }
    
    /**
     * Setter for property tbmInstructions.
     * @param tbmInstructions New value of property tbmInstructions.
     */
    public void setTbmInstructions(com.see.truetransact.clientutil.EnhancedTableModel tbmInstructions) {
        this.tbmInstructions = tbmInstructions;
        setChanged();
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
    
    
    /** Creating comboBoxModels for the comboBoxes in the ui **/
    private void fillDropdown() throws Exception{
        try{
            //log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,null);
            final ArrayList lookup_keys = new ArrayList();
            
            lookup_keys.add("LODGEMENT.RECEIVED_FROM");
            lookup_keys.add("CUSTOMER.CITY");
            lookup_keys.add("CUSTOMER.STATE");
            lookup_keys.add("CUSTOMER.COUNTRY");
            lookup_keys.add("LODGEMENT.INSTRUMENT_TYPE");
            lookup_keys.add("PERIOD");
//            lookup_keys.add("LODGEMENT.INSTRUCTIONS");
            lookup_keys.add("LODGEMENT.CHARGES");
            lookup_keys.add("PRODUCTTYPE");
//            lookup_keys.add("LODGEMENT.INSTRUMENT_TYPE");
            lookup_keys.add("LODGEMENT.ACTIVITIES");
            lookup_keys.add("CATEGORY"); 
            lookup_keys.add("REMITTANCE_ISSUE.TRANSACTION_TYPE"); 
            lookup_keys.add("LODGEMENT.INSTR"); 
            
            
            
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
            
            getKeyValue((HashMap)keyValue.get("PERIOD"));
            cbmIntDays = new ComboBoxModel(key,value);
            makeNull();
            
            getKeyValue((HashMap)keyValue.get("PERIOD"));
            cbmTransitPeriod = new ComboBoxModel(key,value);
            makeNull();
            
             getKeyValue((HashMap)keyValue.get("PERIOD"));
            cbmBillTenor = new ComboBoxModel(key,value);
            makeNull();
            
            getKeyValue((HashMap)keyValue.get("LODGEMENT.RECEIVED_FROM"));
            cbmReceivedFrom = new ComboBoxModel(key,value);
            makeNull();
            
//            getKeyValue((HashMap)keyValue.get("LODGEMENT.INSTRUCTIONS"));
            getKeyValue((HashMap)keyValue.get("LODGEMENT.CHARGES"));
            cbmStdInstruction = new ComboBoxModel(key,value);
            makeNull();
            
            getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
            cbmOtherCity = new ComboBoxModel(key,value);
            cbmDraweeCity = new ComboBoxModel(key,value);
            makeNull();
            
            getKeyValue((HashMap)keyValue.get("CUSTOMER.STATE"));
            cbmOtherState = new ComboBoxModel(key,value);
            cbmDraweeState = new ComboBoxModel(key,value);
            makeNull();
            
            getKeyValue((HashMap)keyValue.get("REMITTANCE_ISSUE.TRANSACTION_TYPE"));
            cbmTranstype = new ComboBoxModel(key,value);
            cbmTranstype = new ComboBoxModel(key,value);
            makeNull();
            
            getKeyValue((HashMap)keyValue.get("CUSTOMER.COUNTRY"));
            cbmOtherCountry = new ComboBoxModel(key,value);
            cbmDraweeCountry = new ComboBoxModel(key,value);
            makeNull();
            
            getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
            cbmProductType = new ComboBoxModel(key,value);
            makeNull();
            
            getKeyValue((HashMap)keyValue.get("LODGEMENT.INSTRUMENT_TYPE"));
            cbmInstrumentType = new ComboBoxModel(key,value);
            makeNull();
            
            getKeyValue((HashMap)keyValue.get("LODGEMENT.ACTIVITIES"));
            cbmActivities = new ComboBoxModel(key,value);
            makeNull();
            
            getKeyValue((HashMap)keyValue.get("CATEGORY"));
            cbmCustCategory = new ComboBoxModel(key,value);
            makeNull();
            
             getKeyValue((HashMap)keyValue.get("LODGEMENT.INSTR"));
            cbmInstruction = new ComboBoxModel(key,value);
            makeNull();
            
            lookUpHash = null;
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,"getBillsType");
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            int j=value.size();
            for(int i=1;i<j;i++){
                value.set(i, (String)value.get(i)+" ("+(String)key.get(i)+")");
            }
            cbmBillsType = new ComboBoxModel(key,value);
             makeNull();
            if(cbmBillsType.getKeys().contains("607")){
                cbmBillsType.removeKeyAndElement("607");
            }
            if(cbmBillsType.getKeys().contains("608")){
                cbmBillsType.removeKeyAndElement("608");
            }
            
            lookUpHash = null;
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,"RemitIssuegetProdId");
//            lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            key.add("IBR");
            value.add("IBR");
            key.add("OBADV");
            value.add("OBADV");
            cbmRemitProdID = new ComboBoxModel(key,value);
            makeNull();
            
//            lookUpHash = null;
//            lookUpHash = new HashMap();
//            lookUpHash.put(CommonConstants.MAP_NAME,"RemitIssuegetProdId");
////            lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
//            keyValue = ClientUtil.populateLookupData(lookUpHash);
//            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
//            key.add("IBR");
//            value.add("IBR");
//            cbmRemitProdID = new ComboBoxModel(key,value);
//            makeNull();
            
        }catch(NullPointerException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
     public void setInstructionValue(String billType){
       
            try{
//                    lookUpHash = new HashMap();
//                    lookUpHash.put(CommonConstants.MAP_NAME, null);
//                    final ArrayList lookup_keys = new ArrayList();
//                    lookup_keys.add("BILLS_SUB_REG_CHQ");
//                    lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
//                    keyValue = ClientUtil.populateLookupData(lookUpHash);
//                    getKeyValue((HashMap)keyValue.get("BILLS_SUB_REG_CHQ"));
//                    cbmSubRegType = new ComboBoxModel(key,value);
//                    this.cbmSubRegType = cbmSubRegType;
//                    setChanged();
                  cbmStdInstruction = null;
                    HashMap data = new HashMap();
//                    data.put("PROD_ID", CommonUtil.convertObjToStr(billType));
//                    lookUpHash = null;
                     if (billType == null){
//                transactionMap.put("behavesLike", CommonUtil.convertObjToStr(cbmTypeOfFacility.getKeyForSelected()));
                    data.put("PROD_ID", CommonUtil.convertObjToStr(cbmBillsType.getKeyForSelected()));
                     }else{
        //                transactionMap.put("behavesLike", billType);
                        data.put("PROD_ID", CommonUtil.convertObjToStr(billType));
                    }
                    lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.PARAMFORQUERY,data);
                    lookUpHash.put(CommonConstants.MAP_NAME,"getBillsChargeType");
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
//                    key.add("IBR");
//                    value.add("IBR");
                    cbmStdInstruction = new ComboBoxModel(key,value);
                    makeNull();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
     }
     
    public void setProductID(){
//        try{
//         HashMap data = new HashMap();
//         data.put("BEHAVES_LIKE", "OD");
//         lookUpHash = null;
//         lookUpHash = new HashMap();
//         lookUpHash.put(CommonConstants.PARAMFORQUERY,data);
//         lookUpHash.put(CommonConstants.MAP_NAME,"getBillsProdID");
//         keyValue = ClientUtil.populateLookupData(lookUpHash);
//         getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
//         cbmProductID = new ComboBoxModel(key,value);
//         makeNull();
//        }catch (Exception ex) {
//                ex.printStackTrace();
//            }
          try{
            cbmProductID = null;
            HashMap transactionMap = new HashMap();
            HashMap retrieve = new HashMap();
            
            ArrayList keyList = new ArrayList();
            keyList.add("");
            
            ArrayList valList = new ArrayList();
            valList.add("");
            
//            if (strFacilityType == null){
//                transactionMap.put("behavesLike", CommonUtil.convertObjToStr(cbmTypeOfFacility.getKeyForSelected()));
//            }else{
                transactionMap.put("BEHAVES_LIKE", "BILLS");
//            }
              System.out.println("in get dppasda");
            List resultList = ClientUtil.executeQuery("getBillsProdID", transactionMap);
            
            for (int i = resultList.size() - 1, j = 0;i >= 0;--i,++j){
                // If the result contains atleast one record
                retrieve = (HashMap) resultList.get(j);
                keyList.add(retrieve.get("PROD_ID"));
                valList.add(retrieve.get("PROD_DESC"));
            }
            
            cbmProductID = new ComboBoxModel(keyList, valList);
            
            transactionMap = null;
            resultList = null;
            keyList = null;
            valList = null;
        }catch(Exception e){
            log.info("Exception caught in setFacilityProductID: "+e);
            parseException.logException(e,true);
        }
    }
    public void setCombo(){
        try{
            
         lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,null);
            final ArrayList lookup_keys = new ArrayList();
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
//            lookup_keys.add("LODGEMENT.INSTRUCTIONS");
//            getKeyValue((HashMap)keyValue.get("LODGEMENT.INSTRUCTIONS"));
            lookup_keys.add("LODGEMENT.CHARGES");
            getKeyValue((HashMap)keyValue.get("LODGEMENT.CHARGES"));
            cbmStdInstruction = new ComboBoxModel(key,value);
            makeNull();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
      public void setBillRates(String subRegType, String prodID, String lodgeID){
       HashMap rateMap = new HashMap();
       rateMap.put("SUB_REG_TYPE", subRegType);
       rateMap.put("PROD_ID", prodID);
       rateMap.put("LODGE_ID", lodgeID);
       List subLst;
       if(CommonUtil.convertObjToStr(lodgeID).equals("")){
            subLst = ClientUtil.executeQuery("getBillsRates", rateMap);
       }
       else{
           subLst = ClientUtil.executeQuery("getBillsRatesAct", rateMap);
       }
           
       rateMap = null;
       if(subLst != null && subLst.size() > 0){
           rateMap = (HashMap)subLst.get(0);
            int value = CommonUtil.convertObjToInt(rateMap.get("TRANS_PERIOD"));
        if((value/365 > 0 ) && (value%365 == 0)){
            setTxtTransitPeriod(CommonUtil.convertObjToStr(new Integer(value/365)));
            setCboTransitPeriod("Years");
        }else if((value/30 > 0 ) && (value%30 == 0)){
            setTxtTransitPeriod(CommonUtil.convertObjToStr(new Integer(value/30)));
            setCboTransitPeriod("Months");
        }else{
            setTxtTransitPeriod(CommonUtil.convertObjToStr(new Integer(value)));
            setCboTransitPeriod("Days");
        }
        
        int value1 = CommonUtil.convertObjToInt(rateMap.get("NO_INT_DAYS"));
        if((value1/365 > 0 ) && (value1%365 == 0)){
            setTxtIntDays(CommonUtil.convertObjToStr(new Integer(value1/365)));
            setCboIntDays("Years");
        }else if((value1/30 > 0 ) && (value1%30 == 0)){
            setTxtIntDays(CommonUtil.convertObjToStr(new Integer(value1/30)));
            setCboIntDays("Months");
        }else{
            setTxtIntDays(CommonUtil.convertObjToStr(new Integer(value1)));
            setCboIntDays("Days");
        }
        setTxtRateForDelay1(CommonUtil.convertObjToStr(rateMap.get("DELAYED_RATE_CHQ")));
        setTxtAtParLimit(CommonUtil.convertObjToStr(rateMap.get("AT_PAR_LIMIT")));
        setTxtDiscountRateBills(CommonUtil.convertObjToStr(rateMap.get("DISCOUNT_RATE_BD")));
        setTxtCleanBills(CommonUtil.convertObjToStr(rateMap.get("OVERDUE_RATE_CBP")));
        setTxtOverdueRateBills(CommonUtil.convertObjToStr(rateMap.get("OVERDUE_RATE_BD")));
        setTxtRateForCBP(CommonUtil.convertObjToStr(rateMap.get("INTEREST_RATE_CBP")));
        setTxtDefaultPostage(CommonUtil.convertObjToStr(rateMap.get("POSTAGE_RATE")));
        if(CommonUtil.convertObjToStr(rateMap.get("INT_FOR_ICC")).equals("Y")){
            setCRadio_ICC_Yes(true);
        }
        else{
            setCRadio_ICC_No(true);
        }
       }
    }
    public void billRatesClear(){
        setTxtTransitPeriod("");
        setCboTransitPeriod("");
        setTxtIntDays("");
        setCboIntDays("");
        setTxtRateForDelay1("");
        setTxtAtParLimit("");
        setTxtDiscountRateBills("");
        setTxtCleanBills("");
        setTxtOverdueRateBills("");
        setTxtRateForCBP("");
        setTxtDefaultPostage("");
        setCRadio_ICC_Yes(false);
        setCRadio_ICC_No(false);
    }
    /** Creates a two ArrayList key,value to create comboboxmodel with key,value pairs **/
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /** Freeing up the memory occupied by key,value ArrayLists **/
    private void makeNull(){
        key=null;
        value=null;
    }
    
    /** Creates the instances of comboBoxModels **/
    private void initComboBoxModel(){
        cbmOtherCity = new ComboBoxModel();
        cbmOtherState = new ComboBoxModel();
        cbmOtherCountry = new ComboBoxModel();
        cbmReceivedFrom = new ComboBoxModel();
        cbmBillsType = new ComboBoxModel();
        cbmActivities = new ComboBoxModel();
        cbmCustCategory = new ComboBoxModel();
        cbmProductType = new ComboBoxModel();
        cbmProductId = new ComboBoxModel();
        cbmDraweeCity = new ComboBoxModel();
        cbmDraweeState = new ComboBoxModel();
        cbmDraweeCountry = new ComboBoxModel();
        cbmBillTenor = new ComboBoxModel();
        cbmStdInstruction = new ComboBoxModel();
        cbmInstruction = new ComboBoxModel();
        cbmInstrumentType = new ComboBoxModel();
        cbmRemitProdID = new ComboBoxModel();
        cbmRemitCity = new ComboBoxModel();
        cbmRemitDraweeBank = new ComboBoxModel();
        cbmRemitBranchCode = new ComboBoxModel();
        cbmProductID = new ComboBoxModel();
        cbmIntDays = new ComboBoxModel();
        cbmTransitPeriod = new ComboBoxModel();
    }
    
    /** Returns an instance of LodgementMasterTO **/
    private LodgementMasterTO getLodgementMasterTO(String command, String prodType, String actNo){
        LodgementMasterTO objLodgementMasterTO = new LodgementMasterTO();
        objLodgementMasterTO.setCommand(command);
        objLodgementMasterTO.setProdId(CommonUtil.convertObjToStr(getCbmProductId().getKeyForSelected()));
        objLodgementMasterTO.setReceivedFrom(CommonUtil.convertObjToStr(getCbmReceivedFrom().getKeyForSelected()));
        objLodgementMasterTO.setRecBranchId(getTxtBranchCode());
        objLodgementMasterTO.setRecOtherBank(getTxtBankCode());
        objLodgementMasterTO.setTdtRemittedDt(DateUtil.getDateMMDDYYYY(getTdtRemittedDt()));
        objLodgementMasterTO.setRecOtherBranch(getTxtOtherBranchCode());
        objLodgementMasterTO.setRecName(getTxtOtherName());
        objLodgementMasterTO.setRecAddr(getTxtOtherAddress());
        objLodgementMasterTO.setRecCity(CommonUtil.convertObjToStr(getCbmOtherCity().getKeyForSelected()));
        objLodgementMasterTO.setRecState(CommonUtil.convertObjToStr(getCbmOtherState().getKeyForSelected()));
        objLodgementMasterTO.setRecCountry(CommonUtil.convertObjToStr(getCbmOtherCountry().getKeyForSelected()));
        objLodgementMasterTO.setRecPincode(getTxtOtherPinCode());
        objLodgementMasterTO.setReference(getTxtReference());
        objLodgementMasterTO.setRateForDelay(CommonUtil.convertObjToDouble(getTxtRateForDelay()));
        objLodgementMasterTO.setProdType(prodType);
        objLodgementMasterTO.setBorrowProdId(CommonUtil.convertObjToStr(getCbmProductId().getKeyForSelected()));
        objLodgementMasterTO.setBorrowAcctNum(actNo);
        objLodgementMasterTO.setDraweeNo(getTxtDraweeNo());
        objLodgementMasterTO.setSendingTo(getTxtSendingTo());
        objLodgementMasterTO.setDraweeBankCode(getTxtDraweeBankCode());
        objLodgementMasterTO.setDraweeBranchCode(getTxtDraweeBranchCode());
        objLodgementMasterTO.setDraweeName(getTxtDraweeName());
        objLodgementMasterTO.setDraweeBankName(getTxtDraweeBankNameVal());
        objLodgementMasterTO.setDraweeAddr(getTxtDraweeAddress());
        objLodgementMasterTO.setDraweeCity(CommonUtil.convertObjToStr(getCbmDraweeCity().getKeyForSelected()));
        objLodgementMasterTO.setDraweeState(CommonUtil.convertObjToStr(getCbmDraweeState().getKeyForSelected()));
        objLodgementMasterTO.setDraweeCountry(CommonUtil.convertObjToStr(getCbmDraweeCountry().getKeyForSelected()));
        objLodgementMasterTO.setDraweePincode(getTxtDraweePinCode());
        objLodgementMasterTO.setInstrumentType(CommonUtil.convertObjToStr(getCbmInstrumentType().getKeyForSelected()));
        objLodgementMasterTO.setLodgementId(getTxtLodgementId());
        objLodgementMasterTO.setBillsType(CommonUtil.convertObjToStr(getCbmBillsType().getKeyForSelected()));
        objLodgementMasterTO.setBillsStatus(CommonUtil.convertObjToStr(getCbmActivities().getKeyForSelected()));
        objLodgementMasterTO.setCustCategory(CommonUtil.convertObjToStr(getCbmCustCategory().getKeyForSelected()));
        System.out.println("TRANS TTYPEIN========="+CommonUtil.convertObjToStr(getCbmTrantype().getKeyForSelected()));
        objLodgementMasterTO.setTranType(CommonUtil.convertObjToStr(getCbmTrantype().getKeyForSelected()));
        objLodgementMasterTO.setCbpActNum(getTxtAccountNum());
        objLodgementMasterTO.setCbpProdID(CommonUtil.convertObjToStr(getCbmProductID().getKeyForSelected()));
        objLodgementMasterTO.setBillsClearing(getChkClering());
        objLodgementMasterTO.setBillsNo(getBillsNo());
        if(command.equals(CommonConstants.TOSTATUS_INSERT)){
            objLodgementMasterTO.setCreatedBy(TrueTransactMain.USER_ID);
            objLodgementMasterTO.setCreatedDt(currDt);
        }
        objLodgementMasterTO.setStatusBy(TrueTransactMain.USER_ID);
        objLodgementMasterTO.setStatusDt(currDt);
        objLodgementMasterTO.setIsMultipleLodgement("Y");
        return objLodgementMasterTO;
    }
    
    private MultipleLodgementMasterTO getMultipleLodgementMasterTO(String command, String acctNo, String prodId , String prodType, Double amount, String instr, Double instAmt, Double serviceTax){
        
        MultipleLodgementMasterTO objMultipleLodgementMasterTO = new MultipleLodgementMasterTO();
        objMultipleLodgementMasterTO.setLodgementId(lodgementId);
        objMultipleLodgementMasterTO.setProductId((prodId));
        objMultipleLodgementMasterTO.setProductType(prodType);
        objMultipleLodgementMasterTO.setAcctNo(acctNo);
        objMultipleLodgementMasterTO.setCommand(command);
        objMultipleLodgementMasterTO.setIndividualAmt(amount);
        objMultipleLodgementMasterTO.setInstruction(instr);
        objMultipleLodgementMasterTO.setInstAmount(instAmt);
        objMultipleLodgementMasterTO.setServiceTax(serviceTax);       
        return objMultipleLodgementMasterTO;
    }
    
    /** Sets the oB Fields thru the to LodgementMasterTO **/
    public void setLodgementMasterTO(LodgementMasterTO objLodgementMasterTO)throws Exception{
        System.out.println("setLodgementMasterTOsetLodgementMasterTO");
        setCboReceivedFrom(CommonUtil.convertObjToStr(getCbmReceivedFrom().getDataForKey(objLodgementMasterTO.getReceivedFrom())));
        setTxtBranchCode(objLodgementMasterTO.getRecBranchId());
        setTxtBankCode(objLodgementMasterTO.getRecOtherBank());
        setTdtRemittedDt(CommonUtil.convertObjToStr(objLodgementMasterTO.getTdtRemittedDt()));
        setTxtOtherBranchCode(objLodgementMasterTO.getRecOtherBranch());
        setTxtOtherName(objLodgementMasterTO.getRecName());
        setTxtOtherAddress(objLodgementMasterTO.getRecAddr());
        setCboOtherCity(CommonUtil.convertObjToStr(getCbmOtherCity().getDataForKey(objLodgementMasterTO.getRecCity())));
        setCboOtherState(CommonUtil.convertObjToStr(getCbmOtherState().getDataForKey(objLodgementMasterTO.getRecState())));
        setCboOtherCountry(CommonUtil.convertObjToStr(getCbmOtherCountry().getDataForKey(objLodgementMasterTO.getRecCountry())));
        setTxtOtherPinCode(objLodgementMasterTO.getRecPincode());
        setTxtReference(objLodgementMasterTO.getReference());
        setTxtRateForDelay(CommonUtil.convertObjToStr(objLodgementMasterTO.getRateForDelay()));
        setCboProductType(CommonUtil.convertObjToStr(getCbmProductType().getDataForKey(objLodgementMasterTO.getProdType())));
        System.out.println("hrerere   "+objLodgementMasterTO.getProdType());
        System.out.println("hrerere111   "+objLodgementMasterTO.getLodgementId());
        setBillsNo(objLodgementMasterTO.getBillsNo());
        if(objLodgementMasterTO.getProdType()!=null)
        {
        getProductIdByType(objLodgementMasterTO.getProdType());
        
        System.out.println("dfsfaaaa");
        setCboProductId(CommonUtil.convertObjToStr(getCbmProductId().getDataForKey(objLodgementMasterTO.getProdId())));
       
        }
        
        //setTxtAccountNo(objLodgementMasterTO.getBorrowAcctNum());
        setTxtAccountNum(objLodgementMasterTO.getCbpActNum());
//        getCbmProductID().setSelectedItem(objLodgementMasterTO.getCbpProdID());
        setProductID();
        setCboProductID(CommonUtil.convertObjToStr(getCbmProductID().getDataForKey(objLodgementMasterTO.getCbpProdID())));
        setTxtDraweeNo(objLodgementMasterTO.getDraweeNo());
        setTxtSendingTo(objLodgementMasterTO.getSendingTo());
        setTxtDraweeBankCode(objLodgementMasterTO.getDraweeBankCode());
        setTxtDraweeBranchCode(objLodgementMasterTO.getDraweeBranchCode());
        setTxtDraweeName(objLodgementMasterTO.getDraweeName());
        setTxtDraweeBankNameVal(objLodgementMasterTO.getDraweeBankName());
        setTxtDraweeAddress(objLodgementMasterTO.getDraweeAddr());
        setCboDraweeCity(CommonUtil.convertObjToStr(getCbmDraweeCity().getDataForKey(objLodgementMasterTO.getDraweeCity())));
        setCboDraweeState(CommonUtil.convertObjToStr(getCbmDraweeState().getDataForKey(objLodgementMasterTO.getDraweeState())));
        setCboDraweeCountry(CommonUtil.convertObjToStr(getCbmDraweeCountry().getDataForKey(objLodgementMasterTO.getDraweeCountry())));
        setTxtDraweePinCode(objLodgementMasterTO.getDraweePincode());
        setCboInstrumentType(CommonUtil.convertObjToStr(getCbmInstrumentType().getDataForKey(objLodgementMasterTO.getInstrumentType())));
        setTxtLodgementId(objLodgementMasterTO.getLodgementId());
        System.out.println("objLodgementMasterTO.getBillsType()"+objLodgementMasterTO.getBillsType());
        setCboBillsType(CommonUtil.convertObjToStr(getCbmBillsType().getDataForKey(objLodgementMasterTO.getBillsType())));
        System.out.println("getCboBillsType"+getCboBillsType());
        setCboActivities(CommonUtil.convertObjToStr(getCbmActivities().getDataForKey(objLodgementMasterTO.getBillsStatus())));
        setCboCustCategory(CommonUtil.convertObjToStr(getCbmCustCategory().getDataForKey(objLodgementMasterTO.getCustCategory())));
        setCboTranstype(CommonUtil.convertObjToStr(getCbmTrantype().getDataForKey(objLodgementMasterTO.getTranType())));
        
        
    }
    
    /** Returns an instance of LodgementChequeTO **/
    private LodgementChequeTO getLodgementChequeTO(String command, Double amount){
        LodgementChequeTO objLodgementChequeTO = new LodgementChequeTO();
        objLodgementChequeTO.setCommand(command);
        objLodgementChequeTO.setLodgementId(getTxtLodgementId());
        objLodgementChequeTO.setDraweeBankName(getTxtDraweeBankName());
        objLodgementChequeTO.setDraweeBranchName(getTxtDraweeBranchName());
        objLodgementChequeTO.setInstrumentNo(getTxtInstrumentNo());
        objLodgementChequeTO.setInstrPrefix(getTxtInstPrefix());
        objLodgementChequeTO.setInstrumentDt(DateUtil.getDateMMDDYYYY(getTdtInstrumentDate()));
        objLodgementChequeTO.setInstrumentAmount(amount);
        objLodgementChequeTO.setMicr(getTxtMICR());
        objLodgementChequeTO.setPayeeName(getTxtPayeeName());
        objLodgementChequeTO.setRemarks(getTxtRemarks());
        objLodgementChequeTO.setStatusBy(TrueTransactMain.USER_ID);
        objLodgementChequeTO.setStatusDt(currDt);
        
        return objLodgementChequeTO;
        
    }
    
    /** Sets the obvariables thru the TO LodgementChequeTO **/
    private void setLodgementChequeTO(LodgementChequeTO objLodgementChequeTO){
        setTxtDraweeBankName(objLodgementChequeTO.getDraweeBankName());
        setTxtDraweeBranchName(objLodgementChequeTO.getDraweeBranchName());
        setTxtInstrumentNo(objLodgementChequeTO.getInstrumentNo());
        setTxtInstPrefix(objLodgementChequeTO.getInstrPrefix());
        setTdtInstrumentDate(DateUtil.getStringDate(objLodgementChequeTO.getInstrumentDt()));
        setTxtInstrumentAmount(CommonUtil.convertObjToStr(objLodgementChequeTO.getInstrumentAmount()));
        setTxtMICR(objLodgementChequeTO.getMicr());
        setTxtPayeeName(objLodgementChequeTO.getPayeeName());
        setTxtRemarks(objLodgementChequeTO.getRemarks());
        notifyObservers();
    }
    
     private void setLodgementRemitTO(LodgementRemitTO objLodgementRemitTO){
        setCboRemitProdID((String) getCbmRemitProdID().getDataForKey(CommonUtil.convertObjToStr(objLodgementRemitTO.getRemitProdId())));
        cbmRemitProdID.setKeyForSelected(objLodgementRemitTO.getRemitProdId());
//        if((getCbmActivities().getKeyForSelected().equals("PROCEEDS_RECEIVED"))){
//            if((getCboActivities().equalsIgnoreCase("PROCEEDS RECEIVED")) && (!getCboReceivedFrom().equalsIgnoreCase("BRANCH"))){
//           setCboRemitCity(objLodgementRemitTO.getRemitCity()); 
           setCboRemitDraweeBank(objLodgementRemitTO.getRemitDraweeBank());
           setCboRemitBranchCode(objLodgementRemitTO.getRemitDraweeBranchCode());
//        }else{
//            populateCity();
            setCboRemitCity((String) getCbmRemitCity().getDataForKey(CommonUtil.convertObjToStr(objLodgementRemitTO.getRemitCity())));
            cbmRemitCity.setKeyForSelected(objLodgementRemitTO.getRemitCity());
            ////            populateDraweeBrank(getCboRemitProdID(),getCboRemitCity());
//            setCboRemitDraweeBank((String) getCbmRemitDraweeBank().getDataForKey(CommonUtil.convertObjToStr(objLodgementRemitTO.getRemitDraweeBank())));
//            populateBranchCode(getCboRemitProdID(), getCboRemitDraweeBank());
//            setCboRemitBranchCode((String) getCbmRemitBranchCode().getDataForKey(CommonUtil.convertObjToStr(objLodgementRemitTO.getRemitDraweeBranchCode())));
//        }
        setRemitStatus(objLodgementRemitTO.getRemitStatus());
        setTdtRemitInstDate(DateUtil.getStringDate(objLodgementRemitTO.getRemitInstDt()));
        setTxtRemitFavour(objLodgementRemitTO.getRemitFavouring());
        setTxtRemitFavour1(objLodgementRemitTO.getRemitFavouringIn());
        setTxtInstAmt(CommonUtil.convertObjToStr(objLodgementRemitTO.getRemitInstAmt()));
        setTxtInst1(objLodgementRemitTO.getRemitInst1());
        setTxtInst2(objLodgementRemitTO.getRemitInst2());
        notifyObservers();
    }
    
      private LodgementRemitTO getLodgementRemitTO(String command, String remitFavr, String balAmt) {//babu1
        LodgementRemitTO objLodgementRemitTO = new LodgementRemitTO();
        System.out.println("getCbmRemitProdID().getKeyForSelected() -----" + getCbmRemitProdID().getKeyForSelected());
        System.out.println("getTxtRemitFavour -----" + getTxtRemitFavour());
        objLodgementRemitTO.setCommand(command);
        objLodgementRemitTO.setRemitProdId(CommonUtil.convertObjToStr(getCbmRemitProdID().getKeyForSelected()));
        objLodgementRemitTO.setRemitDraweeBank(getCboRemitDraweeBank());
        objLodgementRemitTO.setRemitDraweeBranchCode(getCboRemitBranchCode());
        objLodgementRemitTO.setRemitCity(CommonUtil.convertObjToStr(getCbmRemitCity().getKeyForSelected()));
        objLodgementRemitTO.setRemitInstDt(DateUtil.getDateMMDDYYYY(getTdtRemitInstDate()));
        objLodgementRemitTO.setRemitFavouring(remitFavr);
        objLodgementRemitTO.setRemitFavouringIn(getTxtRemitFavour1());
        objLodgementRemitTO.setRemitInstAmt(CommonUtil.convertObjToDouble(remitFavr));
        objLodgementRemitTO.setRemitInst1(getTxtInst1());
        objLodgementRemitTO.setRemitInst2(getTxtInst2());
        objLodgementRemitTO.setLodgeID(getTxtLodgementId());
        objLodgementRemitTO.setRemitStatus(getRemitStatus());
        objLodgementRemitTO.setBillActivity(CommonUtil.convertObjToStr(getCbmActivities().getKeyForSelected()));
        System.out.println("objLodgementRemitTO -----" + objLodgementRemitTO);
        return objLodgementRemitTO;
    }
      
      private LodgementBillRatesTO getLodgementBillRatesTO(String command){
        LodgementBillRatesTO objLodgementBillRatesTO = new LodgementBillRatesTO();
          //Getting the Transit period
        int time = CommonUtil.convertObjToInt(CommonUtil.convertObjToStr(getTxtTransitPeriod()));
        String timeUnit = getCboTransitPeriod();
        if(timeUnit.equals("Years")){
            //If Years is selected in the CboTransitPeriod in the UI
            objLodgementBillRatesTO.setTransPeriod(new Double(time * 365));
        }else if(timeUnit.equals("Months")){
            //If Months is selected in the cboTransitPeriod in the UI
            objLodgementBillRatesTO.setTransPeriod(new Double(time * 30));
        }else{
            //If Days is selected in the cboTransitPeriod in the UI
            objLodgementBillRatesTO.setTransPeriod(new Double(time * 1));
        }
        
         int time1 = CommonUtil.convertObjToInt(CommonUtil.convertObjToStr(getTxtIntDays()));
        String timeUnit1 = getCboIntDays();
        if(timeUnit1.equals("Years")){
            //If Years is selected in the CboTransitPeriod in the UI
            objLodgementBillRatesTO.setNoOfIntDays(new Double(time1 * 365));
        }else if(timeUnit1.equals("Months")){
            //If Months is selected in the cboTransitPeriod in the UI
            objLodgementBillRatesTO.setNoOfIntDays(new Double(time1 * 30));
        }else{
            //If Days is selected in the cboTransitPeriod in the UI
            objLodgementBillRatesTO.setNoOfIntDays(new Double(time1 * 1));
        }
        objLodgementBillRatesTO.setAtParLimit(getTxtAtParLimit());
        objLodgementBillRatesTO.setDiscountRateBd(new Double(getTxtDiscountRateBills()));
        objLodgementBillRatesTO.setInterestRateCbp(new Double(getTxtRateForCBP()));
        objLodgementBillRatesTO.setLodgeID(getTxtLodgementId());
        objLodgementBillRatesTO.setOverdueRateBd(new Double(getTxtOverdueRateBills()));
        objLodgementBillRatesTO.setOverdueRateCbp(new Double(getTxtCleanBills()));
        objLodgementBillRatesTO.setPostageRate(new Double(getTxtDefaultPostage()));
        objLodgementBillRatesTO.setRateForDelay(new Double(getTxtRateForDelay1()));
        if(isCRadio_ICC_Yes()){
            objLodgementBillRatesTO.setIntIcc(YES);
        }else{
            objLodgementBillRatesTO.setIntIcc(NO);
        }
        return objLodgementBillRatesTO;  
    }
      public long calcIntDays(){
          long intDays = 0;
           int time1 = CommonUtil.convertObjToInt(CommonUtil.convertObjToStr(getTxtIntDays()));
        String timeUnit1 = getCboIntDays();
        if(timeUnit1.equals("Years")){
            //If Years is selected in the CboTransitPeriod in the UI
//            objLodgementBillRatesTO.setNoOfIntDays(new Double(time1 * 365));
            intDays = (time1 * 365);
            
        }else if(timeUnit1.equals("Months")){
            //If Months is selected in the cboTransitPeriod in the UI
//            objLodgementBillRatesTO.setNoOfIntDays(new Double(time1 * 30));
            intDays = (time1 * 30);
        }else{
            //If Days is selected in the cboTransitPeriod in the UI
//            objLodgementBillRatesTO.setNoOfIntDays(new Double(time1 * 1));
             intDays = (time1 * 1);
        }
        return intDays;
      }
       private void setLodgementBillRatesTO(LodgementBillRatesTO objLodgementBillRatesTO){
        int value = CommonUtil.convertObjToInt(objLodgementBillRatesTO.getTransPeriod());
        if((value/365 > 0 ) && (value%365 == 0)){
            setTxtTransitPeriod(CommonUtil.convertObjToStr(new Integer(value/365)));
            setCboTransitPeriod("Years");
        }else if((value/30 > 0 ) && (value%30 == 0)){
            setTxtTransitPeriod(CommonUtil.convertObjToStr(new Integer(value/30)));
            setCboTransitPeriod("Months");
        }else{
            setTxtTransitPeriod(CommonUtil.convertObjToStr(new Integer(value)));
            setCboTransitPeriod("Days");
        }
        
        int value1 = CommonUtil.convertObjToInt(objLodgementBillRatesTO.getNoOfIntDays());
        if((value1/365 > 0 ) && (value1%365 == 0)){
            setTxtIntDays(CommonUtil.convertObjToStr(new Integer(value1/365)));
            setCboIntDays("Years");
        }else if((value1/30 > 0 ) && (value1%30 == 0)){
            setTxtIntDays(CommonUtil.convertObjToStr(new Integer(value1/30)));
            setCboIntDays("Months");
        }else{
            setTxtIntDays(CommonUtil.convertObjToStr(new Integer(value1)));
            setCboIntDays("Days");
        }
        
        setTxtAtParLimit(objLodgementBillRatesTO.getAtParLimit());
        setTxtDiscountRateBills(CommonUtil.convertObjToStr(objLodgementBillRatesTO.getDiscountRateBd()));
        setTxtRateForCBP(CommonUtil.convertObjToStr(objLodgementBillRatesTO.getInterestRateCbp()));
        setTxtOverdueRateBills(CommonUtil.convertObjToStr(objLodgementBillRatesTO.getOverdueRateBd()));
        setTxtRateForCBP(CommonUtil.convertObjToStr(objLodgementBillRatesTO.getOverdueRateCbp()));
        setTxtDefaultPostage(CommonUtil.convertObjToStr(objLodgementBillRatesTO.getPostageRate()));
        
        notifyObservers();
    }
    /** Returns an instance of LodgementHundiTO **/
    private LodgementHundiTO getLodgementHundiTO(String command){
        
        LodgementHundiTO objLodgementHundiTO = new LodgementHundiTO();
        objLodgementHundiTO.setCommand(command);
        int time = CommonUtil.convertObjToInt(CommonUtil.convertObjToStr(getTxtBillTenor()));
        String timeUnit = getCboBillTenor();
        if(timeUnit.equals("Years")){
            //If Years is selected in the CboTransitPeriod in the UI
            objLodgementHundiTO.setTenorOfBill((new Double(time * 365)));
        }else if(timeUnit.equals("Months")){
            //If Months is selected in the cboTransitPeriod in the UI
            objLodgementHundiTO.setTenorOfBill((new Double(time * 30)));
        }else{
            //If Days is selected in the cboTransitPeriod in the UI
            objLodgementHundiTO.setTenorOfBill((new Double(time * 1)));
        }
        
        if(getRdoBillAcceptance_Yes()){
            objLodgementHundiTO.setAcceptanceBill(YES);
        }else{
            objLodgementHundiTO.setAcceptanceBill(NO);
        }
        
        objLodgementHundiTO.setBillDueDt(DateUtil.getDateMMDDYYYY(getTdtDueDate()));
        objLodgementHundiTO.setAcceptanceDt(DateUtil.getDateMMDDYYYY(getTdtAcceptanceDate()));
        objLodgementHundiTO.setHundiNo(getTxtHundiNo());
        objLodgementHundiTO.setHundiDt(DateUtil.getDateMMDDYYYY(getTdtHundiDate()));
        if(getRdoDraweeHundi_Yes()){
            objLodgementHundiTO.setDraweeHundi(YES);
        }else{
            objLodgementHundiTO.setDraweeHundi(NO);
        }
        objLodgementHundiTO.setHundiAmount(new Double(getTxtHundiAmount()));
        objLodgementHundiTO.setPayableAt(getTxtPayable());
        objLodgementHundiTO.setRemarks(getTxtRemarks());
        objLodgementHundiTO.setInvoiceNo(getTxtInvoiceNumber());
        objLodgementHundiTO.setInvoiceDt(DateUtil.getDateMMDDYYYY(getTdtInvoiceDate()));
        objLodgementHundiTO.setInvoiceAmount(new Double(getTxtInvoiceAmount()));
        objLodgementHundiTO.setTransCompany(getTxtTransportCompany());
        objLodgementHundiTO.setLrNo(getTxtRRLRNumber());
        objLodgementHundiTO.setLrDt(DateUtil.getDateMMDDYYYY(getTdtRRLRDate()));
        objLodgementHundiTO.setGoodsValue(new Double(getTxtGoodsValue()));
        objLodgementHundiTO.setGoodsAssigned(getTxtGoodsAssigned());
        objLodgementHundiTO.setStatusBy(TrueTransactMain.USER_ID);
        objLodgementHundiTO.setStatusDt(currDt);
        return objLodgementHundiTO;
    }
    
    /** Sets the OBVariables thru TO LodgementHundiTO **/
    private void setLodgementHundiTO(LodgementHundiTO objLodgementHundiTO){
        setTxtLodgementId(objLodgementHundiTO.getLodgementId());
        int value = CommonUtil.convertObjToInt(objLodgementHundiTO.getTenorOfBill());
        if((value/365 > 0 ) && (value%365 == 0)){
            setTxtBillTenor(CommonUtil.convertObjToStr(new Integer(value/365)));
            setCboBillTenor("Years");
        }else if((value/30 > 0 ) && (value%30 == 0)){
            setTxtBillTenor(CommonUtil.convertObjToStr(new Integer(value/30)));
            setCboBillTenor("Months");
        }else{
            setTxtBillTenor(CommonUtil.convertObjToStr(new Integer(value)));
            setCboBillTenor("Days");
        }
        
        if(objLodgementHundiTO.getAcceptanceBill().equals("Y")){
            setRdoBillAcceptance_Yes(true);
        }
        else{
            setRdoBillAcceptance_No(true);
        }
        
        
        
        setTdtDueDate(DateUtil.getStringDate(objLodgementHundiTO.getBillDueDt()));
        setTdtAcceptanceDate(DateUtil.getStringDate(objLodgementHundiTO.getAcceptanceDt()));
        setTxtHundiNo(objLodgementHundiTO.getHundiNo());
        setTdtHundiDate(DateUtil.getStringDate(objLodgementHundiTO.getHundiDt()));
        if(objLodgementHundiTO.getDraweeHundi().equals("Y")){
            setRdoDraweeHundi_Yes(true);
        }else{
            setRdoDraweeHundi_Yes(false);
        }
        setTxtHundiAmount(CommonUtil.convertObjToStr(objLodgementHundiTO.getHundiAmount()));
        setTxtPayable(objLodgementHundiTO.getPayableAt());
        setTxtRemarks(objLodgementHundiTO.getRemarks());
        setTxtInvoiceNumber(objLodgementHundiTO.getInvoiceNo());
        setTdtInvoiceDate(DateUtil.getStringDate(objLodgementHundiTO.getInvoiceDt()));
        setTxtInvoiceAmount(CommonUtil.convertObjToStr(objLodgementHundiTO.getInvoiceAmount()));
        setTxtTransportCompany(objLodgementHundiTO.getTransCompany());
        setTxtRRLRNumber(objLodgementHundiTO.getLrNo());
        setTdtRRLRDate(DateUtil.getStringDate(objLodgementHundiTO.getLrDt()));
        setTxtGoodsValue(CommonUtil.convertObjToStr(objLodgementHundiTO.getGoodsValue()));
        setTxtGoodsAssigned(objLodgementHundiTO.getGoodsAssigned());
        notifyObservers();
    }
    
    /** Resets the OBFields **/
    public void resetForm(){
        setCboProductId("");
        setCboReceivedFrom("");
        setTxtBranchCode("");
        setTxtBankCode("");
        setTdtRemittedDt("");
        setTxtOtherBranchCode("");
        setTxtOtherName("");
        setTxtOtherAddress("");
        setCboOtherCity("");
        setCboOtherState("");
        setCboOtherCountry("");
        setTxtOtherPinCode("");
        setTxtReference("");
        setTxtRateForDelay("");
        setCboProductType("");
        setCboProductId("");
        setTxtAccountNo("");
        setTxtAccountNum("");
        setCboProductID("");
        setTxtDraweeNo("");
        setTxtSendingTo("");
        setTxtDraweeBankCode("");
        setTxtDraweeBranchCode("");
        setTxtDraweeName("");
        setTxtDraweeBankNameVal("");
        setTxtDraweeAddress("");
        setCboDraweeCity("");
        setCboDraweeState("");
        setCboDraweeCountry("");
        setTxtDraweePinCode("");
        setCboInstrumentType("");
        setTxtDraweeBankName("");
        setTxtDraweeBranchName("");
        setTxtInstrumentNo("");
        setTxtInstPrefix("");
        setTdtInstrumentDate("");
        setTdtRemitInstDate("");
        setTxtInstrumentAmount("");
        setTxtMICR("");
        setTxtPayeeName("");
        setTxtRemarks("");
        setTxtBillTenor("");
        setCboBillTenor("");
        setRdoBillAcceptance_No(true);
        setTdtDueDate("");
        setTdtAcceptanceDate("");
        setTxtHundiNo("");
        setTdtHundiDate("");
        setRdoDraweeHundi_No(true);
        setTxtHundiAmount("");
        setTxtPayable("");
        setTxtRemarks("");
        setTxtInvoiceNumber("");
        setTdtInvoiceDate("");
        setTxtInvoiceAmount("");
        setTxtTransportCompany("");
        setTxtRRLRNumber("");
        setTdtRRLRDate("");
        setTxtGoodsValue("");
        setTxtGoodsAssigned("");
        setCboStdInstruction("");
        setTxtStdInstruction("");
        setTxtAmount("");
        setOtherBankProdType("");
        setTxtServiceTax("");
        setTxttotalServTax("");
        setTxtTotalAmt("");
        setCboBillsType("");
        setCboActivities("");
        setCboCustCategory("");
        setCboTranstype("");
        setTxtLodgementId("");
        setCboRemitProdID("");
        setCboRemitCity("");
        setCboRemitDraweeBank("");
        setCboRemitBranchCode("");
        setTxtRemitFavour("");
        setTxtRemitFavour1("");
        setTxtInstAmt("");
        setTxtInst1("");
        setTxtInst2("");
        setRemitStatus("");
        setSubRegType("");
        setOperatesLikeValue("");
        setBillsNo("");
        resetInstructions();
        resetInstr();
        resetAct();
        setLblServiceTaxval("");
        setServiceTax_Map(null); 
        setLodgementId("");
        setTxtAccountNo("");
        setLblCustName("");
        
   }
    
    /** Resets the OBfields related to Instructions **/
    public void resetInstructions(){
        setCboStdInstruction("");
        setTxtStdInstruction("");
        setTxtAmount("");
        setTxtServiceTax("");
        notifyObservers();
    }
     public void resetInstr(){
        setCboInstruction("");
        setTxtAreaParticular("");
        notifyObservers();
    }
     
   public void resetAccountMappingFields(){
        setCboProductId("");
        setCboProductType("");
        setTxtSplitAmnt("");
        setTxtAccountNo("");
        setTxtReference("");
        setLblCustName("");
        notifyObservers();
   }
    
    public void resetAct(){
//        setCboInstruction("");
//        setTxtAreaParticular("");
        notifyObservers();
    }
    
//    /** Populates the combbox cboProductId according to the ProductType selected in the cboProductType in the Ui **/
//    public void getProductIdByType(String prodType) throws Exception{//String productType)
//        /** The data to be show in Combo Box other than LOOKUP_MASTER table
//         * Show Product Id */
//        HashMap lookUpHash = new HashMap();
//        lookUpHash.put(CommonConstants.MAP_NAME,"Charges.getProductData" + prodType);
//        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
//        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
//        cbmProductId = new ComboBoxModel(key,value);
//    }
    
    public void getProductIdByType(String prodType) {
        System.out.println("prodType :: " + prodType);
        if (prodType.equals("GL")) {
            key = new ArrayList();
            value = new ArrayList();
            System.out.println("gllll");
        } else {
            try {
                lookUpHash = new HashMap();
                System.out.println("aaaa");
                lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + prodType);
        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
                System.out.println("bbbb "+lookUpHash.get("CommonConstants.MAP_NAME"));
        keyValue = ClientUtil.populateLookupData(lookUpHash);
                System.out.println("ccccc");
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("dsadad");
        cbmProductId = new ComboBoxModel(key,value);
        this.cbmProductId = cbmProductId;
        System.out.println("setCboProductId sa::" + cbmProductId );
    }
    
    /** This method will execute a query and sets the name to label fields in the UI **/
    public String getName(HashMap sqlMap){
        String name = "";
        String stmtName = CommonUtil.convertObjToStr(sqlMap.get(CommonConstants.MAP_NAME));
        HashMap where = (HashMap) sqlMap.get(CommonConstants.MAP_WHERE);
        ArrayList resultList = (ArrayList) ClientUtil.executeQuery(stmtName, where);
        if(resultList.size() > 0){
            HashMap resultMap = (HashMap) resultList.get(0);
            name = CommonUtil.convertObjToStr(resultMap.get("NAME"));
        }
        return name;
    }
    
    /** Populates the Table with the modified data **/
    public void setTableValueAt(int row){
        //System.out.println("kjhkhkh"+existingData.size());
        deletingExists = true;
        final ArrayList data = tbmInstructions.getDataArrayList();
        if(existingData!=null && existingData.size()>0){           
            existingData.add(data.get(row));
            System.out.println("ccc"+data);
        }
        if(!getCbmStdInstruction().getKeyForSelected().equals("OTHERS")){
            System.out.println("innnnnn");
            tbmInstructions.setValueAt(getCbmStdInstruction().getKeyForSelected(),row,1);
//            tbmInstructions.setValueAt(getCboStdInstruction(),row,1);
            tbmInstructions.setValueAt(getTxtAmount(),row,2);
            tbmInstructions.setValueAt(getTxtServiceTax(),row,3);
            tbmInstructions.setValueAt(getTxtAccountNo(),row,4);            
            tbmInstructions.setValueAt(getCbmProductId().getKeyForSelected(),row,5);
            tbmInstructions.setValueAt(getCbmProductType().getKeyForSelected(),row,6);
            tbmInstructions.setValueAt(getTxtSplitAmnt(),row,7);
            tbmInstructions.setValueAt(getTxtRemitFavour(),row,8); 
            tbmInstructions.setValueAt(getTxtInstAmt(),row,9);
            System.out.println("getCbmStdInstruction().getKeyForSelected().."+getCbmStdInstruction().getKeyForSelected());
            System.out.println("getTxtAmountmmmmm"+getTxtAmount());
        }else{
            tbmInstructions.setValueAt(getTxtStdInstruction(),row,1);
        }
         setTxtTotalAmt(calculateTotalAmount(data));
         setTxttotalServTax(calculateTotalServTaxAmount(data));
    }
    
      public void setTableValueAtInstr(int row){
        deletingExistsInstr = true;
        final ArrayList dataInst = tbmInstructions1.getDataArrayList();
        if(existingDataInst!=null){
            existingDataInst.add(dataInst.get(row));
        }
//        if(!getCbmStdInstruction().getKeyForSelected().equals("OTHERS")){
            tbmInstructions1.setValueAt(getCbmInstruction().getKeyForSelected(),row,1);
            tbmInstructions1.setValueAt(getTxtAreaParticular(),row,2);
//        }else{
//            tbmInstructions.setValueAt(getTxtStdInstruction(),row,1);
//        }
          System.out.println("");
    }
      public void setBillsType(){
        setInstructionValue(null);
    }
      public void setStdInstructionAsBlank(){
        setBlankKeyValue();
        cbmStdInstruction = new ComboBoxModel(key,value);
    }
        private void setBlankKeyValue(){
        key = new ArrayList();
        key.add("");
        value = new ArrayList();
        value.add("");
    }
    /** This method is used to fill up the tbmInstructions an model to the tblInstructionUI **/
    public int addTblInstructionData(){
        int optionSelected = -1;
        String columnData = new String();
        getCbmStdInstruction().setSelectedItem(getCboStdInstruction());
        if(!getCbmStdInstruction().getKeyForSelected().equals("OTHERS")){
            columnData = CommonUtil.convertObjToStr(getCbmStdInstruction().getKeyForSelected());
//            columnData = CommonUtil.convertObjToStr(getCboStdInstruction());
            columnData = CommonUtil.convertObjToStr(getTxtAmount());
            columnData = CommonUtil.convertObjToStr(getTxtServiceTax());
            columnData = CommonUtil.convertObjToStr(getTxtAccountNo());
        }else{
            columnData = getTxtStdInstruction();
            columnData = CommonUtil.convertObjToStr(getTxtAmount());
            columnData = CommonUtil.convertObjToStr(getTxtServiceTax());
            columnData = CommonUtil.convertObjToStr(getTxtAccountNo());
        }
        try{
            if (newInstructionRow == null)
                newInstructionRow = new ArrayList();
            newInstructionRow.add(new Integer(tbmInstructions.getRowCount()+1));
            if(!getCbmStdInstruction().getKeyForSelected().equals("OTHERS")){
                newInstructionRow.add(getCbmStdInstruction().getKeyForSelected());
//                newInstructionRow.add(getCboStdInstruction());
                newInstructionRow.add(getTxtAmount());
                newInstructionRow.add(getTxtServiceTax());
                newInstructionRow.add(getTxtAccountNo());
                newInstructionRow.add(getCbmProductId().getKeyForSelected());
                newInstructionRow.add(getCbmProductType().getKeyForSelected());
                newInstructionRow.add(getTxtSplitAmnt());
                newInstructionRow.add(getTxtRemitFavour()); 
                newInstructionRow.add(getTxtInstAmt());
            }else{
                newInstructionRow.add(getTxtStdInstruction());
                newInstructionRow.add(getTxtAmount());
                newInstructionRow.add(getTxtServiceTax());
                newInstructionRow.add(getTxtAccountNo());
                newInstructionRow.add(getCbmProductId().getKeyForSelected());
                newInstructionRow.add(getCbmProductType().getKeyForSelected());
                newInstructionRow.add(getTxtSplitAmnt());
                newInstructionRow.add(getTxtRemitFavour()); 
                newInstructionRow.add(getTxtInstAmt());
            }            
            ArrayList data = tbmInstructions.getDataArrayList();
            final int dataSize = data.size();
            boolean exist = false;
            for (int i=0;i<dataSize;i++){
                if (CommonUtil.convertObjToStr(((ArrayList)data.get(i)).get(1)).equalsIgnoreCase(columnData)){
                    // Checking whether existing Data is equal new data entered by the user
                    exist = true;
                    String[] options = {resourceBundle.getString("cDialogYes"),resourceBundle.getString("cDialogNo"),resourceBundle.getString("cDialogCancel")};
                    optionSelected = COptionPane.showOptionDialog(null, resourceBundle.getString("existanceWarning"), CommonConstants.WARNINGTITLE,
                    COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if (optionSelected == 0){
                        // Newly Entered data already exists and the user wants to modify it
                        updateTbmInstructions(i);
                        //doUpdateData(i);
                    }else if(optionSelected == 1){
                        // Newly Entered data already exists and the user doesn't wants to modify it
                        resetInstructions();
                    }
                    break;
                }
                
            }
            if (!exist){
                //The condition that the Entered data is not in the table
                doNewData();
                insertNewData();
            }
            setTxtTotalAmt(calculateTotalAmount(data));
            setTxttotalServTax(calculateTotalServTaxAmount(data));
            newInstructionRow = null;
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return optionSelected;
    }
    private void calculateServiceTaxForCharges1() {
        ArrayList alLData = new ArrayList();
        double taxamt = 0;
        alLData = tbmInstructions.getDataArrayList();
        for (int i = 0; i < alLData.size(); i++) {
            ArrayList temp = (ArrayList) alLData.get(i);
            if (temp != null && temp.size() > 1) {
                String chargeType = CommonUtil.convertObjToStr(temp.get(1));
                if (chargeType != null && chargeType.equalsIgnoreCase("BANK_CHARGES")) {
                    String checkFlag = checkServiceTaxApplicable();
                    if (checkFlag != null && checkFlag.equals("Y")) {
                        taxamt += CommonUtil.convertObjToDouble(temp.get(2));
                    }
                }

            }
        }
        if (taxamt > 0) {
            HashMap ser_Tax_Val = new HashMap();
            ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, currDt.clone());
            ser_Tax_Val.put(ServiceTaxCalculation.TOT_AMOUNT, CommonUtil.convertObjToStr(taxamt));
            try {
                ServiceTaxCalculation objServiceTax = new ServiceTaxCalculation();
                HashMap serviceTax_Maptemp = objServiceTax.calculateServiceTax(ser_Tax_Val);
                if (serviceTax_Maptemp != null && serviceTax_Maptemp.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                    String amt = CommonUtil.convertObjToStr(serviceTax_Maptemp.get(ServiceTaxCalculation.TOT_TAX_AMT));
                    setLblServiceTaxval(objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
                    serviceTax_Maptemp.put(ServiceTaxCalculation.TOT_TAX_AMT, objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
                    serviceTax_Maptemp.put("ACT_NUM",getTxtAccountNo());
                    multipleServiceTaxMap.put(getTxtAccountNo(),serviceTax_Maptemp); // Modified by nithya
                    setServiceTax_Map(multipleServiceTaxMap);

                } else {
                    setServiceTax_Map(null);
                    setLblServiceTaxval("0.00");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void calculateServiceTaxForCharges() {

        ArrayList alLData = new ArrayList();
        alLData = tbmInstructions.getDataArrayList();
        for (int i = 0; i < alLData.size(); i++) {
            ArrayList temp = (ArrayList) alLData.get(i);
            double taxamt = 0;
            if (temp != null && temp.size() > 1) {
                for (int j = 0; j < alLData.size(); j++) {
                    ArrayList temp1 = (ArrayList) alLData.get(j);
                    if (CommonUtil.convertObjToStr(temp.get(4)).equalsIgnoreCase(CommonUtil.convertObjToStr(temp1.get(4)))) {
                        String chargeType = CommonUtil.convertObjToStr(temp.get(1));
                        if (chargeType != null && chargeType.equalsIgnoreCase("BANK_CHARGES")) {
                            String checkFlag = checkServiceTaxApplicable();
                            if (checkFlag != null && checkFlag.equals("Y")) {
                                taxamt += CommonUtil.convertObjToDouble(temp.get(2));
                            }
                        }
                    }
                }

                if (taxamt > 0) {
                    HashMap ser_Tax_Val = new HashMap();
                    ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, currDt.clone());
                    ser_Tax_Val.put(ServiceTaxCalculation.TOT_AMOUNT, CommonUtil.convertObjToStr(taxamt));
                    try {
                        ServiceTaxCalculation objServiceTax = new ServiceTaxCalculation();
                        HashMap serviceTax_Maptemp = objServiceTax.calculateServiceTax(ser_Tax_Val);
                        if (serviceTax_Maptemp != null && serviceTax_Maptemp.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                            String amt = CommonUtil.convertObjToStr(serviceTax_Maptemp.get(ServiceTaxCalculation.TOT_TAX_AMT));
                            setLblServiceTaxval(objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
                            serviceTax_Maptemp.put(ServiceTaxCalculation.TOT_TAX_AMT, objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
                            serviceTax_Maptemp.put(CommonUtil.convertObjToStr(temp.get(4)), getTxtAccountNo());
                            multipleServiceTaxMap.put(CommonUtil.convertObjToStr(temp.get(4)), serviceTax_Maptemp); // Modified by nithya
                            setServiceTax_Map(multipleServiceTaxMap);

                        } else {
                            setServiceTax_Map(null);
                            setLblServiceTaxval("0.00");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

    }

     public int addTblInstrData(){
        int optionSelected = -1;
        String columnData = new String();
//        if(!getCbmStdInstruction().getKeyForSelected().equals("OTHERS")){
            columnData = CommonUtil.convertObjToStr(getCbmInstruction().getKeyForSelected());
            columnData = CommonUtil.convertObjToStr(getTxtAreaParticular());
//            columnData = CommonUtil.convertObjToStr(getTxtServiceTax());
//        }else{
//            columnData = getTxtStdInstruction();
//            columnData = CommonUtil.convertObjToStr(getTxtAmount());
//            columnData = CommonUtil.convertObjToStr(getTxtServiceTax());
//        }
        try{
            if (newInstructionRowInstr == null)
                newInstructionRowInstr = new ArrayList();
            newInstructionRowInstr.add(new Integer(tbmInstructions1.getRowCount()+1));
//            if(!getCbmInstruction().getKeyForSelected().equals("OTHERS")){
                newInstructionRowInstr.add(getCbmInstruction().getKeyForSelected());
                newInstructionRowInstr.add(getTxtAreaParticular());
//                newInstructionRow.add(getTxtServiceTax());
////            }else{
//                newInstructionRow.add(getTxtStdInstruction());
//                newInstructionRow.add(getTxtAmount());
//                newInstructionRow.add(getTxtServiceTax());
//            }
            ArrayList dataInst = tbmInstructions1.getDataArrayList();
            final int dataSize = dataInst.size();
            boolean exist = false;
            for (int i=0;i<dataSize;i++){
                if (CommonUtil.convertObjToStr(((ArrayList)dataInst.get(i)).get(1)).equalsIgnoreCase(columnData)){
                    // Checking whether existing Data is equal new data entered by the user
                    exist = true;
                    String[] options = {resourceBundle.getString("cDialogYes"),resourceBundle.getString("cDialogNo"),resourceBundle.getString("cDialogCancel")};
                    optionSelected = COptionPane.showOptionDialog(null, resourceBundle.getString("existanceWarning"), CommonConstants.WARNINGTITLE,
                    COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if (optionSelected == 0){
                        // Newly Entered data already exists and the user wants to modify it
                        updateTbmInstr(i);
                        //doUpdateData(i);
                    }else if(optionSelected == 1){
                        // Newly Entered data already exists and the user doesn't wants to modify it
                        resetInstr();
                    }
                    break;
                }
                
            }
            if (!exist){
                //The condition that the Entered data is not in the table
                doNewDataInstr();
                insertNewDataInstr();
            }
//            setTxtTotalAmt(calculateTotalAmount(data));
//            setTxttotalServTax(calculateTotalServTaxAmount(data));
            newInstructionRowInstr = null;
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return optionSelected;
    }
    private String calculateTotalAmount(ArrayList tableData) {
        final int column = 2;
        String returnTotalAmt = "";
        try {
            double totalAmount = 0.0;
            ArrayList rowData1 = new ArrayList();
            for (int i=0,j=tableData.size();i<j;i++) {
                rowData1 = (ArrayList) tableData.get(i);
                totalAmount += Double.parseDouble(CommonUtil.convertObjToStr(rowData1.get(column)));
                rowData1 = null;
            }
            returnTotalAmt =  CommonUtil.convertObjToStr(new Double(String.valueOf(totalAmount)));
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
            calculateServiceTaxForCharges();
        }
        return returnTotalAmt;
    }
    
    private String calculateTotalServTaxAmount(ArrayList tableData) {
        final int column = 3;
        String returnTotalAmt = "";
        try {
            double totalAmount = 0.0;
            ArrayList rowData1 = new ArrayList();
            for (int i=0,j=tableData.size();i<j;i++) {
                rowData1 = (ArrayList) tableData.get(i);
                totalAmount += Double.parseDouble(CommonUtil.convertObjToStr(rowData1.get(column)));
                rowData1 = null;
            }
            returnTotalAmt =  CommonUtil.convertObjToStr(new Double(String.valueOf(totalAmount)));
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        return returnTotalAmt;
    }
    /** This mehod updates the Data tblInstruction Table in the UI
     *according to the data entered in the TextFields of the UI **/
    private void updateTbmInstructions(int row) throws Exception{
        if(!getCbmStdInstruction().getKeyForSelected().equals("OTHERS")){
            tbmInstructions.setValueAt(getCbmStdInstruction().getKeyForSelected(), row, 1);
//            tbmInstructions.setValueAt(getCboStdInstruction(), row, 1);
        }else{
            tbmInstructions.setValueAt(getTxtStdInstruction(), row, 1);
        }
        System.out.println("modifffyy");
    }
    
    private void updateTbmInstr(int row) throws Exception{
//        if(!getCbmStdInstruction().getKeyForSelected().equals("OTHERS")){
            tbmInstructions1.setValueAt(getCbmInstruction().getKeyForSelected(), row, 1);
//        }else{
//            tbmInstructions.setValueAt(getTxtStdInstruction(), row, 1);
//        } 
    }
    
    /** This method get the Updated data entered into the tblInstruction Table in the UI **/
    private void doUpdateData(int row){
        if(existingData!=null){
            existingData.add(tbmInstructions.getDataArrayList().get(row));
        }
    }
    
    /** This method get the new data entered **/
    private void doNewData(){
        newData.add(newInstructionRow);
    }
    
     private void doNewDataInstr(){
        newDataInstr.add(newInstructionRowInstr);
    }
    
    /** Insert into the tblInstruction table in the UI **/
    private void insertNewData() throws Exception{
        //final TerminalTO objTerminalTO = new TerminalTO();
        int row = tbmInstructions.getRowCount();
        tbmInstructions.insertRow(row,newInstructionRow);
    }
     private void insertNewDataInstr() throws Exception{
        //final TerminalTO objTerminalTO = new TerminalTO();
        int row = tbmInstructions1.getRowCount();
        tbmInstructions1.insertRow(row,newInstructionRowInstr);
    }
    // to calculate the Total Amount for issue & Trans details
    public String calculateTotalAmount() {
        String returnTotalAmt = "";
        if(existData != null && existData.size() > 0) {
            final int column = 2;
            
            try {
                double totalAmount = 0.0;
                HashMap rowData = new HashMap();
                for (int i=0,j=existData.size();i<j;i++) {
//                    rowData = (HashMap) existData.get(i);
                    rowData.put("CHR",existData.get(column));
//                    List lstData = (List) rowData.get("CHR");
                    totalAmount += Double.parseDouble(CommonUtil.convertObjToStr(rowData.get("CHR")));
                    rowData = null;
                }
                returnTotalAmt =  CommonUtil.convertObjToStr(new Double(String.valueOf(totalAmount)));
            } catch (Exception e) {
                parseException.logException(e,true);
            }
        }
        return returnTotalAmt;
        
    }
    
    /** This method is used to populate the instruction related fields when the
     *tblInstruction row is selected in the UI **/
    public void populateSelectedRow(int row){        

        ArrayList data = (ArrayList)tbmInstructions.getDataArrayList().get(row);
        if(getCbmStdInstruction().containsElement(getCbmStdInstruction().getDataForKey(data.get(1)))){
            setCboStdInstruction(CommonUtil.convertObjToStr(getCbmStdInstruction().getDataForKey(data.get(1))));
            setTxtAmount(CommonUtil.convertObjToStr(data.get(2)));
            setTxtServiceTax(CommonUtil.convertObjToStr(data.get(3)));
            setCboProductType(CommonUtil.convertObjToStr(getCbmProductType().getDataForKey(data.get(6))));
            getProductIdByType(CommonUtil.convertObjToStr(getCbmProductType().getKeyForSelected()));           
            setCboProductId(CommonUtil.convertObjToStr(getCbmProductId().getDataForKey(CommonUtil.convertObjToStr(data.get(5)))));            
            System.out.println("setCboProductId ::" + getCboProductId() );
            setTxtAccountNo(CommonUtil.convertObjToStr(data.get(4)));
            setTxtSplitAmnt(CommonUtil.convertObjToStr(data.get(7)));
            
        }else{
            setCboStdInstruction(CommonUtil.convertObjToStr(getCbmStdInstruction().getDataForKey("OTHERS")));
            setTxtStdInstruction(CommonUtil.convertObjToStr(data.get(1)));
            setTxtAmount(CommonUtil.convertObjToStr(data.get(2)));
            setTxtServiceTax(CommonUtil.convertObjToStr(data.get(3)));
            setCboProductType(CommonUtil.convertObjToStr(getCbmProductType().getDataForKey(data.get(6))));
            getProductIdByType(CommonUtil.convertObjToStr(getCbmProductType().getKeyForSelected()));            
            setCboProductId(CommonUtil.convertObjToStr(getCbmProductId().getDataForKey(CommonUtil.convertObjToStr(data.get(5)))));
            System.out.println("setCboProductId ::" + getCboProductId() );
            setTxtAccountNo(CommonUtil.convertObjToStr(data.get(4)));
            setTxtSplitAmnt(CommonUtil.convertObjToStr(data.get(7)));
            if(deletedData!=null){
                deletedData.add(data.get(1));
            }
        }
        notifyObservers();
    }
    
    public void populateActTblSelectedRow(int row){
        ArrayList data = (ArrayList)tbmActNo.getDataArrayList().get(row);      
        System.out.println("data :: "+ data +"\n row ::"+ row);
        setCboProductId(CommonUtil.convertObjToStr(getCbmProductId().getDataForKey(data.get(0))));
        setCboProductType(CommonUtil.convertObjToStr(getCbmProductId().getDataForKey(data.get(1))));
        setTxtSplitAmnt(CommonUtil.convertObjToStr(data.get(4)));
        setTxtAccountNo(CommonUtil.convertObjToStr(data.get(2)));
        setLblCustName(CommonUtil.convertObjToStr(data.get(3)));
        notifyObservers();
    }
    public ArrayList populateSelectedRowAct(int row){
        ArrayList data = (ArrayList)tbmInstructions2.getDataArrayList().get(row);
        return data;
    }
    
     public void populateSelectedRowInst(int row){
        ArrayList dataInst = (ArrayList)tbmInstructions1.getDataArrayList().get(row);
//        if(getCbmInstruction().containsElement(getCbmStdInstruction().getDataForKey(data.get(1)))){
            setCboInstruction(CommonUtil.convertObjToStr(getCbmInstruction().getDataForKey(dataInst.get(1))));
            setTxtAreaParticular(CommonUtil.convertObjToStr(dataInst.get(2)));
//            setTxtServiceTax(CommonUtil.convertObjToStr(data.get(3)));
//        }else{
//            setCboStdInstruction(CommonUtil.convertObjToStr(getCbmStdInstruction().getDataForKey("OTHERS")));
//            setTxtStdInstruction(CommonUtil.convertObjToStr(data.get(1)));
//            setTxtAmount(CommonUtil.convertObjToStr(data.get(2)));
//            setTxtServiceTax(CommonUtil.convertObjToStr(data.get(3)));
            if(deletedDataInst!=null){
                deletedDataInst.add(dataInst.get(1));
//            }
        }
        notifyObservers();
    }
    
    /** This method is remove the selectedRow in the tblInstructions **/
    public void deleteSelectedRow(int row){
        final ArrayList data = tbmInstructions.getDataArrayList();
        if(removedRow!=null){
            removedRow.add(data.get(row));
        }
        tbmInstructions.removeRow(row);
        for(int i=0; i<tbmInstructions.getRowCount(); i++){
            tbmInstructions.setValueAt(new Integer(i+1),i,0);
        }
        resetInstructions();
        setTxtTotalAmt(calculateTotalAmount(data));
        setTxttotalServTax(calculateTotalServTaxAmount(data));
        notifyObservers();
    }
    
    
    
      public void deleteSelectedRowInstr(int row){
        final ArrayList dataInst = tbmInstructions1.getDataArrayList();
        if(removedRowInst!=null){
            removedRowInst.add(dataInst.get(row));
        }
        tbmInstructions1.removeRow(row);
        for(int i=0; i<tbmInstructions1.getRowCount(); i++){
            tbmInstructions1.setValueAt(new Integer(i+1),i,0);
        }
        resetInstr();
    }
    
    
    // This method removes the row from the tblInstruction table in UI
    public void removeTbmInstructionsRow(){
        tbmInstructions = new EnhancedTableModel(null,tblHeadings);
        setTbmInstructions(tbmInstructions);
    }
    
     public void removeTbmInstrRow(){
        tbmInstructions1 = new EnhancedTableModel(null,tblHeadings1);
        setTbmInstructions1(tbmInstructions1);
    }
      public void removeTbmActRow(){
        tbmInstructions2 = new EnhancedTableModel(null,tblHeadings2);
        setTbmInstructions2(tbmInstructions2);
    }
 
    /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        System.out.println("populateDatapopulateData"+whereMap);
        System.out.println("map========="+map);
        HashMap mapData=null;
        try {
            whereMap.put("MULTIPLE_LODGEMENT","MULTIPLE_LODGEMENT");
            mapData = proxy.executeQuery(whereMap, map);
            LodgementMasterTO objLodgementMasterTO =
            (LodgementMasterTO) ((List) mapData.get("LodgementMasterTO")).get(0);
            System.out.println("mapDatamapData"+mapData);
            System.out.println("objLodgementMasterTOobjLodgementMasterTO"+objLodgementMasterTO);
            setLodgementMasterTO(objLodgementMasterTO);
            if(mapData.containsKey("LodgementChequeTO")){
                LodgementChequeTO objLodgementChequeTO =
                (LodgementChequeTO) ((List) mapData.get("LodgementChequeTO")).get(0);
                setLodgementChequeTO(objLodgementChequeTO);
            }
            if(mapData.containsKey("LodgementHundiTO")){
                LodgementHundiTO objLodgementHundiTO =
                (LodgementHundiTO) ((List) mapData.get("LodgementHundiTO")).get(0);
                setLodgementHundiTO(objLodgementHundiTO);
            }
             if(mapData.containsKey("LodgementRemitTO")){
                LodgementRemitTO objLodgementRemitTO =
                (LodgementRemitTO) ((List) mapData.get("LodgementRemitTO")).get(0);
                setLodgementRemitTO(objLodgementRemitTO);
            }
//            if(mapData.containsKey("MultipleLodgementMasterTO")){
//                MultipleLodgementMasterTO objMultipleLodgementMasterTO=
//                (MultipleLodgementMasterTO) ((List) mapData.get("MultipleLodgementMasterTO")).get(0);
//                setLodgementRemitTO(objLodgementRemitTO);       
//            }
            populateTblInstruction(mapData);
            populateTblInstruction1(mapData);
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
    
     public ArrayList populateTransData(HashMap mapID, CTable tblData) {
         System.out.println("populateTransDatapopulateTransData");
        _tblData = tblData;
        
        // Adding Where Condition
        HashMap whereMap = null;
        if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
            if (mapID.get(CommonConstants.MAP_WHERE) instanceof HashMap)
                whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
            else
                System.out.println("Convert other data type to HashMap:" + mapID);
        } else {
            whereMap = new HashMap();
        }
        
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }
        
        mapID.put(CommonConstants.MAP_WHERE, whereMap);
        
        System.out.println("Screen   : " + getClass());
        System.out.println("Map Name : " + mapID.get(CommonConstants.MAP_NAME));
        System.out.println("Map      : " + mapID);
        
        dataHash = ClientUtil.executeTableQuery(mapID);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        tranData = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        
//        calcBalances();
        
        //        System.out.println("### Data after calcBalances() : "+data);
        removeTimeStampsInData();
        setProperDataType();
        //        System.out.println("### Data after removing Timestamp : "+data);
        populateTable();
        whereMap = null;
        return _heading;
    }
     
      private void removeTimeStampsInData() {
        int size = tranData.size();
        ArrayList arrList = new ArrayList();
        for (int i=0; i<size; i++) {
            arrList = (ArrayList)tranData.get(i);
            System.out.println("@@@@@arrList@@"+arrList);
            if (arrList!=null && arrList.size()>0) {
                String transDt = CommonUtil.convertObjToStr(arrList.get(0));
                System.out.println("@@@@@transDt@@"+transDt);
                if (transDt.length()>0) {
                    Date transDtWithoutTimeStamp = DateUtil.getDateMMDDYYYY(transDt);
                    transDtWithoutTimeStamp = new Date(transDtWithoutTimeStamp.getYear(),
                    transDtWithoutTimeStamp.getMonth(),
                    transDtWithoutTimeStamp.getDate());
                    //                    System.out.println("#$#$#$ transDtWithoutTimeStamp : "+transDtWithoutTimeStamp);
                    //                    System.out.println("#$#$#$ DateUtil.getStringDate(transDtWithoutTimeStamp) : "+DateUtil.getStringDate(transDtWithoutTimeStamp));
                    arrList.set(0, DateUtil.getStringDate(transDtWithoutTimeStamp));
                    //                    arrList.set(0, transDtWithoutTimeStamp);
                    tranData.set(i, arrList);
                }
            }
        }
        arrList = null;
          System.out.println("removeTimeStampsInData");
    }
      
       private void setProperDataType() {
           System.out.println("setProperDataType");
        int size = tranData.size();
        if (size>0) {
            ArrayList arrList;
            arrList = (ArrayList)tranData.get(0);
            for (int i=0; i<size; i++) {
                arrList = (ArrayList)tranData.get(i);
//                if ((getProdType().equals("TL")|| getProdType().equals("AD")) && arrList.size()>8) {
//                    arrList.set(3, ClientUtil.convertObjToCurrency(arrList.get(3)));
//                    arrList.set(4, ClientUtil.convertObjToCurrency(arrList.get(4)));
//                    arrList.set(5, ClientUtil.convertObjToCurrency(arrList.get(5)));
//                    arrList.set(6, ClientUtil.convertObjToCurrency(arrList.get(6)));
//                    arrList.set(7, ClientUtil.convertObjToCurrency(arrList.get(7)));
//                    arrList.set(8, ClientUtil.convertObjToCurrency(arrList.get(8)));
//                    arrList.set(9, ClientUtil.convertObjToCurrency(arrList.get(9)));
//                    arrList.set(10, ClientUtil.convertObjToCurrency(arrList.get(10)));
//                    if(arrList.size()>11){
//                        arrList.set(11, ClientUtil.convertObjToCurrency(arrList.get(11)));
//                        arrList.set(12, ClientUtil.convertObjToCurrency(arrList.get(12)));
//                    }
//                }
//                else {
                    if (arrList.size()<=6) {
//                        arrList.set(2, ClientUtil.convertObjToCurrency(arrList.get(2)));
//                        arrList.set(3, ClientUtil.convertObjToCurrency(arrList.get(3)));
//                        arrList.set(4, ClientUtil.convertObjToCurrency(arrList.get(4)));
//                        arrList.set(5, ClientUtil.convertObjToCurrency(arrList.get(5)));
                    } else {
//                        String transDt = CommonUtil.convertObjToStr(arrList.get(4));
//                        if (transDt.length()>0) {
//                            Date transDtWithoutTimeStamp = DateUtil.getDateMMDDYYYY(transDt);
//                            transDtWithoutTimeStamp = new Date(transDtWithoutTimeStamp.getYear(),
//                            transDtWithoutTimeStamp.getMonth(),
//                            transDtWithoutTimeStamp.getDate());
//                            arrList.set(4, DateUtil.getStringDate(transDtWithoutTimeStamp));
//                        }
//                        arrList.set(5, ClientUtil.convertObjToCurrency(arrList.get(5)));
//                        arrList.set(6, ClientUtil.convertObjToCurrency(arrList.get(6)));
                        arrList.set(8, ClientUtil.convertObjToCurrency(arrList.get(8)));
                    }
//                }
                tranData.set(i, arrList);
            }
        }
    }
       
       public void populateTable() {
           System.out.println("populateTablepopulateTable");
        //        ArrayList heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        boolean dataExist;
        if (_heading != null){
            System.out.println("jjjjjjjjj");
            _isAvailable = true;
            dataExist = true;
            //            TableSorter tableSorter = new TableSorter();
            //            tableSorter.addMouseListenerToHeaderInTable(_tblData);
            TableModel tableModel = new TableModel();
            tableModel.setHeading(_heading);
            tableModel.setData(tranData);
            tableModel.fireTableDataChanged();
            //            tableSorter.setModel(tableModel);
            //            tableSorter.fireTableDataChanged();
            _tblData.setAutoResizeMode(5);
            _tblData.doLayout();
            _tblData.setModel(tableModel);
            _tblData.revalidate();
//            if (getProdType().equals("TD") && ((ArrayList)data.get(0)).size()<=6) {
//                setRightAlignment(2);
//                setRightAlignment(3);
//                setRightAlignment(4);
//                setRightAlignment(5);
//            } else if (getProdType().equals("TL") && ((ArrayList)data.get(0)).size()>8) {
//                setRightAlignment(3);
//                setRightAlignment(4);
//                setRightAlignment(5);
//                setRightAlignment(6);
//                setRightAlignment(7);
//                setRightAlignment(8);
//                setRightAlignment(9);
//                setRightAlignment(10);
//                if(((ArrayList)data.get(0)).size()>11){
//                    setRightAlignment(11);
//                    setRightAlignment(12);
//                }
//            } else {
                setRightAlignment(5);
                setRightAlignment(6);
                setRightAlignment(7);
//            }
                System.out.println("lllllllllllll");
            
        }else{
            System.out.println("kkkkkkkkkkkkk");
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
            
//            ClientUtil.noDataAlert();
        }
        //            if (_tblData.getModel() instanceof TableSorter) {
        //            _tableModel = ((TableSorter) _tblData.getModel()).getModel();
        //        } else {
        //            _tableModel = (TableModel) _tblData.getModel();
        //        }
        //
        //        JTableHeader tblHeader = _tblData.getTableHeader();
        //        TableColumnModel tcm = tblHeader.getColumnModel();
        //        Enumeration enum = tcm.getColumns();
        //
        //        String str;
        //        _heading = new ArrayList();
        //        while (enum.hasMoreElements()) {
        //            str = (String) ((TableColumn) enum.nextElement()).getHeaderValue();
        //            _heading.add(str);
        //        }
    }
       private void setRightAlignment(int col) {
        javax.swing.table.DefaultTableCellRenderer r = new javax.swing.table.DefaultTableCellRenderer();
        r.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        _tblData.getColumnModel().getColumn(col).setCellRenderer(r);
        _tblData.getColumnModel().getColumn(col).sizeWidthToFit();
    }
       
    /**This method is used to populate the tbmInstructions when edit button
     *is clicked in the Ui to populate the existing data **/
    private void populateTblInstruction(HashMap mapData){
        
        System.out.println("test   mapData"+mapData);
//        if(mapData.containsKey("LodgementInstructionsTO")){
//            List instList = (List) mapData.get("LodgementInstructionsTO");
//            System.out.println("instListinstList"+instList);
//            double totalAmount = 0.0;
//            double totServTax = 0.0;
//            String returnTotalAmt = "";
//            String totalSerAmt = "";
//            if(instList != null && instList.size() !=0){
//                for(int i=0; i<instList.size();i++){
//                    HashMap dataMap = (HashMap) instList.get(i);
//                    existData = new ArrayList();
//                    existData.add(new Integer(i+1));
//                    existData.add(dataMap.get("INSTRUCTION"));
//                    existData.add(dataMap.get("AMOUNT"));
//                    existData.add(dataMap.get("SERVICE_TX"));
//                    totalAmount += Double.parseDouble(CommonUtil.convertObjToStr(dataMap.get("AMOUNT")));
//                    totServTax += Double.parseDouble(CommonUtil.convertObjToStr(dataMap.get("SERVICE_TX")));
//                    tbmInstructions.insertRow(tbmInstructions.getRowCount(), existData);
////                    existData = null;
//                }
//                returnTotalAmt =  CommonUtil.convertObjToStr(new Double(String.valueOf(totalAmount)));
//                totalSerAmt = CommonUtil.convertObjToStr(new Double(String.valueOf(totServTax)));
//                setTxtTotalAmt(returnTotalAmt);
//                setTxttotalServTax(totalSerAmt);
//                if(totalAmount>0 && TrueTransactMain.SERVICE_TAX_REQ.equals("Y")){
//                    calculateServiceTaxForCharges();
//                }
//                notifyObservers();
//            }
//        }
        
         if(mapData.containsKey("MultipleLodgementMasterTO")){
            List instList = (List) mapData.get("MultipleLodgementMasterTO");
            System.out.println("instListinstList"+instList);
            double totalAmount = 0.0;
            double totServTax = 0.0;
            String returnTotalAmt = "";
            String totalSerAmt = "";
            if(instList != null && instList.size() !=0){
                for(int i=0; i<instList.size();i++){
                    HashMap dataMap = (HashMap) instList.get(i);
                    existData = new ArrayList();
                    //LODGEMENT_ID ,INSTRUCTION, INST_AMT , SERVICE_TAX, BORROW_ACCT_NUM,PROD_ID, PROD_TYPE, INDIVIDUAL_AMOUNT
                    existData.add(new Integer(i+1));
                    existData.add(dataMap.get("INSTRUCTION"));
                    existData.add(dataMap.get("INST_AMT"));
                    existData.add(dataMap.get("SERVICE_TAX"));                    
                    existData.add(dataMap.get("BORROW_ACCT_NUM"));
                    existData.add(dataMap.get("PROD_ID"));
                    existData.add(dataMap.get("PROD_TYPE"));
                    existData.add(dataMap.get("INDIVIDUAL_AMOUNT")); 
                    existData.add("");
                    existData.add(""); 
                    if(CommonUtil.convertObjToStr(dataMap.get("INST_AMT")) != null){
                        totalAmount += Double.parseDouble(CommonUtil.convertObjToStr(dataMap.get("INST_AMT")));
                    }
                    if(CommonUtil.convertObjToStr(dataMap.get("SERVICE_TAX")) != null){
                        totServTax += Double.parseDouble(CommonUtil.convertObjToStr(dataMap.get("SERVICE_TAX")));
                    }                    
                    tbmInstructions.insertRow(tbmInstructions.getRowCount(), existData);
//                    existData = null;
                    setLodgementId(CommonUtil.convertObjToStr(dataMap.get("LODGEMENT_ID")));
                }
                returnTotalAmt =  CommonUtil.convertObjToStr(new Double(String.valueOf(totalAmount)));
                totalSerAmt = CommonUtil.convertObjToStr(new Double(String.valueOf(totServTax)));
                setTxtTotalAmt(returnTotalAmt);
                setTxttotalServTax(totalSerAmt);
                if(totalAmount>0 && TrueTransactMain.SERVICE_TAX_REQ.equals("Y")){
                    calculateServiceTaxForCharges();
                }
                notifyObservers();
            }
        }
        
    }
    
    // Added by nithya for multiple bills lodgement
    public void populateTblLodgementDetailsData(HashMap mapData, String subRegType) {
        System.out.println("mapData====++++" + mapData);  
//        HashMap existDataMap = new HashMap();
//        HashMap existDataTitleMap = new HashMap();
        existData = new ArrayList();        
        existData.add(mapData.get("PROD_ID"));
        existData.add(mapData.get("PROD_TYPE"));
        existData.add(mapData.get("ACT_NUM"));
        existData.add(mapData.get("ACT_NAME")); 
        existData.add(mapData.get("INDIVIDUAL_AMT")); 
        System.out.println("existData====++++" + existData);        
        tbmActNo.insertRow(0,existData);   
//        existDataMap.put("PROD_ID",mapData.get("PROD_ID"));
//        existDataMap.put("PROD_TYPE",mapData.get("PROD_TYPE"));
//        existDataMap.put("ACT_NUM",mapData.get("ACT_NUM"));
//        existDataMap.put("ACT_NAME",mapData.get("ACT_NAME"));
//        existDataMap.put("INDIVIDUAL_AMT",mapData.get("INDIVIDUAL_AMT"));
//        existDataTitleMap.put("MASTER_DATA",existDataMap);
//        lodgementRecords.put(mapData.get("ACT_NUM"), existDataTitleMap);     
        existData = null;
//        existDataMap = null;
//        existDataTitleMap = null;
    }
    
    public void setTableModel(CTable tblData){
        setTblModel(tblData, null, tblHeadingsForMultipleLodgement);
    }
    
    public void setTblModel(final CTable tbl, ArrayList tblData, ArrayList head) {
        TableSorter tableSorter = new TableSorter();
      //  tableSorter.addMouseListenerToHeaderInTable(tbl);
        // Modified mColIndex == 13 by nithya on 05-03-2016 for 0003914 
        TableModel tableModel = new TableModel(tblData, head) {
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if (mColIndex == 1 || mColIndex == 2 || mColIndex == 3 || mColIndex == 4) {
                    //tbl.setValueAt(map, rowIndex, mColIndex);
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
    // End
    
    public void populateTblActData(HashMap mapData, String subRegType){
        try
        {
        if(subRegType.equals("CPD")){
            System.out.println("kihyfffhjh======");
        if(mapData.get("PRODUCT_ID").equals("CPD")){
                    existData = new ArrayList();
                    existData.add(mapData.get("PRODUCT_ID"));
                    existData.add(mapData.get("ACT_NUM"));
                    existData.add(mapData.get("ACT_NAME"));
                    if(tbmInstructions2.getRowCount() == 0){
                    tbmInstructions2.insertRow(tbmInstructions2.getRowCount(), existData);
                    }else{
                        tbmInstructions2.removeRow(0);
                        tbmInstructions2.insertRow(0, existData);
                    }
        }else{
                    existData = new ArrayList();
                    existData.add(mapData.get("PRODUCT_ID"));
                    existData.add(mapData.get("ACT_NUM"));
                    existData.add(mapData.get("ACT_NAME"));
//                    tbmInstructions2.insertRow(tbmInstructions2.getRowCount(), existData);
                    if(tbmInstructions2.getRowCount() == 1){
                    tbmInstructions2.insertRow(tbmInstructions2.getRowCount(), existData);
                    }else{
                        tbmInstructions2.removeRow(1);
                        tbmInstructions2.insertRow(1, existData);
                    }
        }
        }else{
            
            existData = new ArrayList();
            existData.add(mapData.get("PRODUCT_ID"));
            existData.add(mapData.get("ACT_NUM"));
            existData.add(mapData.get("ACT_NAME"));
            if(tbmInstructions2.getRowCount() == 0){
                System.out.println("existData====++++"+existData);
                    tbmInstructions2.insertRow(tbmInstructions2.getRowCount(), existData);
                    }else{
                        tbmInstructions2.removeRow(0);
                        tbmInstructions2.insertRow(0, existData);
                    }
        }
        }
        catch(Exception e)
        {
            //e.printStackTrace();
        }
    }
    
     private void populateTblInstruction1(HashMap mapData){
        if(mapData.containsKey("LodgementInstrTO")){
            List instList = (List) mapData.get("LodgementInstrTO");
//            double totalAmount = 0.0;
//            double totServTax = 0.0;
//            String returnTotalAmt = "";
//            String totalSerAmt = "";
            if(instList != null && instList.size() !=0){
                for(int i=0; i<instList.size();i++){
                    HashMap dataMap = (HashMap) instList.get(i);
                    existData = new ArrayList();
                    existData.add(new Integer(i+1));
                    existData.add(dataMap.get("INSTRUCTION"));
                    existData.add(dataMap.get("PARTICULARS"));
//                    existData.add(dataMap.get("SERVICE_TX"));
//                    totalAmount += Double.parseDouble(CommonUtil.convertObjToStr(dataMap.get("AMOUNT")));
//                    totServTax += Double.parseDouble(CommonUtil.convertObjToStr(dataMap.get("SERVICE_TX")));
                    tbmInstructions1.insertRow(tbmInstructions1.getRowCount(), existData);
//                    existData = null;
                }
//                returnTotalAmt =  CommonUtil.convertObjToStr(new Double(String.valueOf(totalAmount)));
//                totalSerAmt = CommonUtil.convertObjToStr(new Double(String.valueOf(totServTax)));
//                setTxtTotalAmt(returnTotalAmt);
//                setTxttotalServTax(totalSerAmt);
                notifyObservers();
            }
        }
        
    }
    /** this method get the existing data from the Table **/
    public void existingData(){
        deletedData = new ArrayList();
        existingData = new ArrayList();
        removedRow = new ArrayList();
        int rowCount = tbmInstructions.getRowCount();
        for(int i=0;i<rowCount;i++){
            columnElement = new ArrayList();
            columnElement.add(tbmInstructions.getValueAt(i,1));
            rowData.add(columnElement);
        }
    }
     public void populateInstr(){
        tblInstr = new ArrayList();
        if(getActionType()!=ClientConstants.ACTIONTYPE_DELETE){
            if(getActionType() == ClientConstants.ACTIONTYPE_NEW)
                insertDataInstr();
            if(getActionType() == ClientConstants.ACTIONTYPE_EDIT)
                updateDataInstr();
            deletedDataInstr();
            deInitializeInstr();
        }else if(getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            deletedRowInstr();
        }
    }
     private  void insertDataInstr(){
        insertDataInst = new ArrayList();
        dataInst = tbmInstructions1.getDataArrayList();
      //   System.out.println("dataInstdataInst===="+dataInst);
        int rowData = dataInst.size();
        int rowNewData = newDataInstr.size();
        if(newDataInstr.size()!=0){
            for(int i=0;i<rowData;i++){
//                for(int j=0;j<rowNewData;j++){
//                    if(((ArrayList)newDataInstr.get(j)).get(1).equals(((ArrayList)dataInst.get(i)).get(1))){
                        insertDataInst.add(newDataInstr.get(i));
//                    }
                }
//            }
        }
        newDataInstr.clear();
        if(insertDataInst != null && insertDataInst.size()>0){
            for(int i=0; i<insertDataInst.size();i++){
                LodgementInstrTO objLodgementInstrTO;
                objLodgementInstrTO = new LodgementInstrTO();
                if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                    objLodgementInstrTO.setInstLodgementId(txtLodgementId);
                }
                objLodgementInstrTO.setInstSlNo(CommonUtil.convertObjToStr(((ArrayList)insertDataInst.get(i)).get(0)));
                objLodgementInstrTO.setInstInstruction(CommonUtil.convertObjToStr(((ArrayList)insertDataInst.get(i)).get(1)));
//                objLodgementInstr.setAmount(CommonUtil.convertObjToDouble(((ArrayList)insertData.get(i)).get(2)));
//                objLodgementInstr.setServiceTx(CommonUtil.convertObjToDouble(((ArrayList)insertData.get(i)).get(3)));
                objLodgementInstrTO.setInstParticulars(CommonUtil.convertObjToStr(((ArrayList)insertDataInst.get(i)).get(2)));
                objLodgementInstrTO.setInstStatus(CommonConstants.STATUS_CREATED);
                objLodgementInstrTO.setInstStatusBy(TrueTransactMain.USER_ID);
                objLodgementInstrTO.setInstStatusDt(currDt);
                tblInstr.add(objLodgementInstrTO);
            }
        }
        dataInst = null;
    }
    
    /** This populates the tblInstructionData with data to be updated **/
    private  void updateDataInstr(){
        System.out.println("existingDataInst...."+existingDataInst);
        if(existingDataInst!=null && existingDataInst.size()!=0){
            for(int i=0;i<existingDataInst.size();i++){
                LodgementInstrTO  objLodgementInstrTO;
                objLodgementInstrTO = new LodgementInstrTO();
                objLodgementInstrTO.setInstSlNo(CommonUtil.convertObjToStr(((ArrayList)insertDataInst.get(i)).get(0)));
                objLodgementInstrTO.setInstInstruction(CommonUtil.convertObjToStr(((ArrayList)insertDataInst.get(i)).get(1)));
//                objLodgementInstr.setAmount(CommonUtil.convertObjToDouble(((ArrayList)insertData.get(i)).get(2)));
//                objLodgementInstr.setServiceTx(CommonUtil.convertObjToDouble(((ArrayList)insertData.get(i)).get(3)));
                objLodgementInstrTO.setInstParticulars(CommonUtil.convertObjToStr(((ArrayList)insertDataInst.get(i)).get(2)));
                objLodgementInstrTO.setInstStatus(CommonConstants.STATUS_MODIFIED);
                objLodgementInstrTO.setInstStatusBy(TrueTransactMain.USER_ID);
                objLodgementInstrTO.setInstStatusDt(currDt);
                tblInstr.add(objLodgementInstrTO);
                System.out.println("in edit with old dataaa"+objLodgementInstrTO);
            }
        }else{
//            if(tbmInstructions.getRowCount()!=0)
//            {
//                for(int i=0; i<tbmInstructions.getRowCount();i++){
//                LodgementInstrTO objLodgementInstrTO;
////                objLodgementInstructionsTO = new LodgementInstructionsTO();
////                objLodgementInstructionsTO.setInstruction(CommonUtil.convertObjToStr(tbmInstructions.getValueAt(i,1)));
////                objLodgementInstructionsTO.setAmount(CommonUtil.convertObjToDouble(tbmInstructions.getValueAt(i,2)));
////                objLodgementInstructionsTO.setServiceTx(CommonUtil.convertObjToDouble(tbmInstructions.getValueAt(i,3))); 
////                objLodgementInstructionsTO.setLodgementId(txtLodgementId);
////                objLodgementInstructionsTO.setStatus(CommonConstants.STATUS_MODIFIED);
////                objLodgementInstructionsTO.setStatusBy(TrueTransactMain.USER_ID);
////                objLodgementInstructionsTO.setStatusDt(currDt);
////                tblInstructionData.add(objLodgementInstructionsTO);
//                   objLodgementInstrTO = new LodgementInstrTO();
////                objLodgementInstrTO.setInstInstruction(CommonUtil.convertObjToStr(((ArrayList)insertDataInst.get(i)).get(1)));
//////                objLodgementInstr.setAmount(CommonUtil.convertObjToDouble(((ArrayList)insertData.get(i)).get(2)));
//////                objLodgementInstr.setServiceTx(CommonUtil.convertObjToDouble(((ArrayList)insertData.get(i)).get(3)));
////                objLodgementInstrTO.setInstParticulars(CommonUtil.convertObjToStr(((ArrayList)insertDataInst.get(i)).get(2)));
//                 objLodgementInstrTO.setInstSlNo(CommonUtil.convertObjToStr(tbmInstructions.getValueAt(i,0)));
//                   objLodgementInstrTO.setInstInstruction(CommonUtil.convertObjToStr(tbmInstructions.getValueAt(i,1)));
//                objLodgementInstrTO.setInstParticulars(CommonUtil.convertObjToStr(tbmInstructions.getValueAt(i,2)));
//                objLodgementInstrTO.setInstLodgementId(txtLodgementId);
//                   objLodgementInstrTO.setInstStatus(CommonConstants.STATUS_MODIFIED);
//                objLodgementInstrTO.setInstStatusBy(TrueTransactMain.USER_ID);
//                objLodgementInstrTO.setInstStatusDt(currDt);
//                tblInstr.add(objLodgementInstrTO); 
//            }
//            }
            if(tbmInstructions1.getRowCount()!=0){
            for(int i=0; i<tbmInstructions1.getRowCount();i++){
                LodgementInstrTO objLodgementInstrTO;
//                objLodgementInstructionsTO = new LodgementInstructionsTO();
//                objLodgementInstructionsTO.setInstruction(CommonUtil.convertObjToStr(tbmInstructions.getValueAt(i,1)));
//                objLodgementInstructionsTO.setAmount(CommonUtil.convertObjToDouble(tbmInstructions.getValueAt(i,2)));
//                objLodgementInstructionsTO.setServiceTx(CommonUtil.convertObjToDouble(tbmInstructions.getValueAt(i,3))); 
//                objLodgementInstructionsTO.setLodgementId(txtLodgementId);
//                objLodgementInstructionsTO.setStatus(CommonConstants.STATUS_MODIFIED);
//                objLodgementInstructionsTO.setStatusBy(TrueTransactMain.USER_ID);
//                objLodgementInstructionsTO.setStatusDt(currDt);
//                tblInstructionData.add(objLodgementInstructionsTO);
                   objLodgementInstrTO = new LodgementInstrTO();
//                objLodgementInstrTO.setInstInstruction(CommonUtil.convertObjToStr(((ArrayList)insertDataInst.get(i)).get(1)));
////                objLodgementInstr.setAmount(CommonUtil.convertObjToDouble(((ArrayList)insertData.get(i)).get(2)));
////                objLodgementInstr.setServiceTx(CommonUtil.convertObjToDouble(((ArrayList)insertData.get(i)).get(3)));
//                objLodgementInstrTO.setInstParticulars(CommonUtil.convertObjToStr(((ArrayList)insertDataInst.get(i)).get(2)));
                 objLodgementInstrTO.setInstSlNo(CommonUtil.convertObjToStr(tbmInstructions1.getValueAt(i,0)));
                   objLodgementInstrTO.setInstInstruction(CommonUtil.convertObjToStr(tbmInstructions1.getValueAt(i,1)));
                objLodgementInstrTO.setInstParticulars(CommonUtil.convertObjToStr(tbmInstructions1.getValueAt(i,2)));
                objLodgementInstrTO.setInstLodgementId(txtLodgementId);
                   objLodgementInstrTO.setInstStatus(CommonConstants.STATUS_MODIFIED);
                objLodgementInstrTO.setInstStatusBy(TrueTransactMain.USER_ID);
                objLodgementInstrTO.setInstStatusDt(currDt);
                tblInstr.add(objLodgementInstrTO);
                System.out.println("here.... "+objLodgementInstrTO);
            }
        }
        }
        
        if(deletingExistsInstr==true && deletedDataInst!=null){
            if(deletedDataInst.size()!=0){
                for(int i=0;i<deletedDataInst.size();i++){
//                    LodgementInstructionsTO  objLodgementInstructionsTO;
//                    objLodgementInstructionsTO = new LodgementInstructionsTO();
//                    objLodgementInstructionsTO.setInstruction(CommonUtil.convertObjToStr(deletedData.get(i)));
//                    objLodgementInstructionsTO.setAmount(CommonUtil.convertObjToDouble(deletedData.get(i)));
//                    objLodgementInstructionsTO.setServiceTx(CommonUtil.convertObjToDouble(deletedData.get(i)));
//                    objLodgementInstructionsTO.setLodgementId(txtLodgementId);
//                    objLodgementInstructionsTO.setStatus(CommonConstants.STATUS_DELETED);
//                    objLodgementInstructionsTO.setStatusBy(TrueTransactMain.USER_ID);
//                    objLodgementInstructionsTO.setStatusDt(currDt);
//                    tblInstructionData.add(objLodgementInstructionsTO);
                     LodgementInstrTO objLodgementInstrTO;
//                objLodgementInstructionsTO = new LodgementInstructionsTO();
//                objLodgementInstructionsTO.setInstruction(CommonUtil.convertObjToStr(tbmInstructions.getValueAt(i,1)));
//                objLodgementInstructionsTO.setAmount(CommonUtil.convertObjToDouble(tbmInstructions.getValueAt(i,2)));
//                objLodgementInstructionsTO.setServiceTx(CommonUtil.convertObjToDouble(tbmInstructions.getValueAt(i,3))); 
//                objLodgementInstructionsTO.setLodgementId(txtLodgementId);
//                objLodgementInstructionsTO.setStatus(CommonConstants.STATUS_MODIFIED);
//                objLodgementInstructionsTO.setStatusBy(TrueTransactMain.USER_ID);
//                objLodgementInstructionsTO.setStatusDt(currDt);
//                tblInstructionData.add(objLodgementInstructionsTO);
                   objLodgementInstrTO = new LodgementInstrTO();
                   objLodgementInstrTO.setInstSlNo(CommonUtil.convertObjToStr(((ArrayList)insertDataInst.get(i)).get(0)));
                objLodgementInstrTO.setInstInstruction(CommonUtil.convertObjToStr(((ArrayList)insertDataInst.get(i)).get(1)));
//                objLodgementInstr.setAmount(CommonUtil.convertObjToDouble(((ArrayList)insertData.get(i)).get(2)));
//                objLodgementInstr.setServiceTx(CommonUtil.convertObjToDouble(((ArrayList)insertData.get(i)).get(3)));
                objLodgementInstrTO.setInstLodgementId(txtLodgementId);
                objLodgementInstrTO.setInstParticulars(CommonUtil.convertObjToStr(((ArrayList)insertDataInst.get(i)).get(2)));
                objLodgementInstrTO.setInstStatus(CommonConstants.STATUS_DELETED);
                objLodgementInstrTO.setInstStatusBy(TrueTransactMain.USER_ID);
                objLodgementInstrTO.setInstStatusDt(currDt);
                tblInstr.add(objLodgementInstrTO);
                }
            }
            deletingExistsInstr = false;
        }
//       if(tbmInstructions.getRowCount()!=0){
//            for(int i=0; i<tbmInstructions.getRowCount();i++){
//                LodgementInstructionsTO objLodgementInstructionsTO;
//                objLodgementInstructionsTO = new LodgementInstructionsTO();
//                objLodgementInstructionsTO.setInstruction(CommonUtil.convertObjToStr(tbmInstructions.getValueAt(i,1)));
//                objLodgementInstructionsTO.setAmount(CommonUtil.convertObjToDouble(tbmInstructions.getValueAt(i,2)));
//                objLodgementInstructionsTO.setServiceTx(CommonUtil.convertObjToDouble(tbmInstructions.getValueAt(i,3))); 
//                objLodgementInstructionsTO.setLodgementId(txtLodgementId);
//                objLodgementInstructionsTO.setStatus(CommonConstants.STATUS_MODIFIED);
//                objLodgementInstructionsTO.setStatusBy(TrueTransactMain.USER_ID);
//                objLodgementInstructionsTO.setStatusDt(currDt);
//                tblInstructionData.add(objLodgementInstructionsTO);
//            }
//        }
        
    }
    
    /** This populates the tblInstructionList with data to be deleted **/
    private void deletedDataInstr(){
//        System.out.println("removedRowInst === "+removedRowInst.size());
        if(removedRowInst != null && removedRowInst.size()>0){
            for(int i=0; i<removedRowInst.size();i++){
                LodgementInstrTO objLodgementInstrTO;
                objLodgementInstrTO = new LodgementInstrTO();
                objLodgementInstrTO.setInstLodgementId(txtLodgementId);
                objLodgementInstrTO.setInstSlNo(CommonUtil.convertObjToStr(((ArrayList)removedRowInst.get(i)).get(0)));
                objLodgementInstrTO.setInstInstruction(CommonUtil.convertObjToStr(((ArrayList)removedRowInst.get(i)).get(1)));
                objLodgementInstrTO.setInstParticulars(CommonUtil.convertObjToStr(((ArrayList)removedRowInst.get(i)).get(2)));
//                objLodgementInstructionsTO.setServiceTx(CommonUtil.convertObjToDouble(((ArrayList)removedRow.get(i)).get(3)));
                objLodgementInstrTO.setInstStatus(CommonConstants.STATUS_DELETED);
                objLodgementInstrTO.setInstStatusBy(TrueTransactMain.USER_ID);
                objLodgementInstrTO.setInstStatusDt(currDt);
                tblInstr.add(objLodgementInstrTO);
            }
        }
        
    }
    
    /** This populates the tblInstructionData with data to be deleted **/
    private void deletedRowInstr(){
        if(tbmInstructions.getRowCount()!=0){
            for(int i=0; i<tbmInstructions1.getRowCount();i++){
                 LodgementInstrTO objLodgementInstrTO;
                 objLodgementInstrTO = new LodgementInstrTO();
                 objLodgementInstrTO.setInstLodgementId(txtLodgementId);
                objLodgementInstrTO.setInstSlNo(CommonUtil.convertObjToStr(tbmInstructions1.getValueAt(i,0)));
                objLodgementInstrTO.setInstInstruction(CommonUtil.convertObjToStr(tbmInstructions1.getValueAt(i,1)));
                objLodgementInstrTO.setInstParticulars(CommonUtil.convertObjToStr(tbmInstructions1.getValueAt(i,2)));
                objLodgementInstrTO.setInstStatus(CommonConstants.STATUS_DELETED);
                objLodgementInstrTO.setInstStatusBy(TrueTransactMain.USER_ID);
                objLodgementInstrTO.setInstStatusDt(currDt);
                tblInstr.add(objLodgementInstrTO);
            }
        }
    }
    
    /** This freeup the memory used by the ArrayLists **/
    private void deInitializeInstr(){
        insertData = null;
        updateData = null;
        removedRow = null;
        existingData = null;
        deletedData = null;
    }
     public double calcOtherCharges(){
         double otherBnkCharge = 0.0;
  insertData = new ArrayList();
        data = tbmInstructions.getDataArrayList();
        int rowData = data.size();
        int rowNewData = newData.size();
        if(newData.size()!=0){
//            for(int i=0;i<rowData;i++){
            for(int i=0;i<rowData;i++){
//                for(int j=0;j<rowNewData;j++){
//                    if(((ArrayList)newData.get(j)).get(1).equals(((ArrayList)data.get(i)).get(1))){
//                        insertData.add(newData.get(j));
                insertData.add(newData.get(i));
//                    }
//                }
            }
        }
//        newData.clear();
        if(insertData != null && insertData.size()>0){
            for(int i=0; i<insertData.size();i++){
//                LodgementInstructionsTO objLodgementInstructionsTO;
//                objLodgementInstructionsTO = new LodgementInstructionsTO();
//                if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
//                    objLodgementInstructionsTO.setLodgementId(txtLodgementId);
//                }
                System.out.println("insertData***"+insertData);
                System.out.println("OTHER_BANK_CHARGES"+CommonUtil.convertObjToStr(((ArrayList)insertData.get(i)).get(1)).equals("OTHER_BANK_CHARGES"));
                 if(CommonUtil.convertObjToStr(((ArrayList)insertData.get(i)).get(1)).equals("OTHER_BANK_CHARGES")){
                     otherBnkCharge = CommonUtil.convertObjToDouble(((ArrayList)insertData.get(i)).get(2)).doubleValue();
                     otherBnkCharge = otherBnkCharge + CommonUtil.convertObjToDouble(((ArrayList)insertData.get(i)).get(3)).doubleValue();
                 }
//                objLodgementInstructionsTO.setInstruction(CommonUtil.convertObjToStr(((ArrayList)insertData.get(i)).get(1)));
//                objLodgementInstructionsTO.setAmount(CommonUtil.convertObjToDouble(((ArrayList)insertData.get(i)).get(2)));
//                objLodgementInstructionsTO.setServiceTx(CommonUtil.convertObjToDouble(((ArrayList)insertData.get(i)).get(3)));
//                objLodgementInstructionsTO.setStatus(CommonConstants.STATUS_CREATED);
//                objLodgementInstructionsTO.setStatusBy(TrueTransactMain.USER_ID);
//                objLodgementInstructionsTO.setStatusDt(currDt);
//                tblInstructionData.add(objLodgementInstructionsTO);
            }
        }
        data = null;
        return otherBnkCharge;
    }
    /**This method populates an ArrayList which contains the data related to
     *tblInstructions **/
    public void populateInstructionList(){        
        if(getActionType()!=ClientConstants.ACTIONTYPE_DELETE){
            if(getActionType() == ClientConstants.ACTIONTYPE_NEW)
                insertData();
            if(getActionType() == ClientConstants.ACTIONTYPE_EDIT)
                updateData();
            deletedData();
            deInitialize();
        }else if(getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            deletedRow();
        }
    }
    
    
    /** This populates the tblInstructionData ArrayList to hold the data to be inserted **/
    private  void insertData(){
        insertData = new ArrayList();
        data = tbmInstructions.getDataArrayList();
        System.out.println("data .... :: insertData() :: " + data);
        int rowData = data.size();
        int rowNewData = newData.size();
        if(newData.size()!=0){
//            for(int i=0;i<rowData;i++){
            for(int i=0;i<rowData;i++){
//                for(int j=0;j<rowNewData;j++){
//                    if(((ArrayList)newData.get(j)).get(1).equals(((ArrayList)data.get(i)).get(1))){
//                        insertData.add(newData.get(j));
                insertData.add(newData.get(i));
//                    }
//                }
            }
        }
        newData.clear();
        if (insertData != null && insertData.size() > 0) {
            String chrgActNo = null;
            String compareChrgActNo = null;
            for (int i = 0; i < insertData.size(); i++) {
                tblInstructionData = new ArrayList();
                for (int j = 0; j < insertData.size(); j++) {
                    chrgActNo = CommonUtil.convertObjToStr(((ArrayList) insertData.get(j)).get(4));
                    compareChrgActNo = CommonUtil.convertObjToStr(((ArrayList) insertData.get(i)).get(4));
                    if (chrgActNo.equalsIgnoreCase(compareChrgActNo)) {
                        LodgementInstructionsTO objLodgementInstructionsTO;
                        objLodgementInstructionsTO = new LodgementInstructionsTO();
                        if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                            objLodgementInstructionsTO.setLodgementId(txtLodgementId);
                        }
                        objLodgementInstructionsTO.setSlNo(CommonUtil.convertObjToStr(((ArrayList) insertData.get(j)).get(0)));
                        objLodgementInstructionsTO.setInstruction(CommonUtil.convertObjToStr(((ArrayList) insertData.get(j)).get(1)));
                        objLodgementInstructionsTO.setAmount(CommonUtil.convertObjToDouble(((ArrayList) insertData.get(j)).get(2)));
                        objLodgementInstructionsTO.setServiceTx(CommonUtil.convertObjToDouble(((ArrayList) insertData.get(j)).get(3)));
                        objLodgementInstructionsTO.setStatus(CommonConstants.STATUS_CREATED);
                        objLodgementInstructionsTO.setStatusBy(TrueTransactMain.USER_ID);
                        objLodgementInstructionsTO.setStatusDt((Date)currDt.clone());
                        tblInstructionData.add(objLodgementInstructionsTO);
                    }
                }
               multipleInstructionListMap.put(compareChrgActNo,tblInstructionData);
            }
        }
       
        data = null;
        System.out.println("updated tblInstructionData 555===="+tblInstructionData);
    }
    public void clearData(){
        newData.clear();
    }
    /** This populates the tblInstructionData with data to be updated **/
    private  void updateData(){
        System.out.println("updated existingData===="+existingData);
        if(existingData!=null && existingData.size()!=0){
            String chrgActNo = null;
            String compareChrgActNo = null;
            for (int i = 0; i < existingData.size(); i++) {
                tblInstructionData = new ArrayList();
                for (int j = 0; j < existingData.size(); j++) {
                    chrgActNo = CommonUtil.convertObjToStr(((ArrayList) insertData.get(j)).get(4));
                    compareChrgActNo = CommonUtil.convertObjToStr(((ArrayList) insertData.get(i)).get(4));
                    if (chrgActNo.equalsIgnoreCase(compareChrgActNo)) {
                        LodgementInstructionsTO objLodgementInstructionsTO;
                        objLodgementInstructionsTO = new LodgementInstructionsTO();
                        objLodgementInstructionsTO.setSlNo(CommonUtil.convertObjToStr(((ArrayList) existingData.get(j)).get(0)));
                        objLodgementInstructionsTO.setInstruction(CommonUtil.convertObjToStr(((ArrayList) existingData.get(j)).get(1)));
                        objLodgementInstructionsTO.setAmount(CommonUtil.convertObjToDouble(((ArrayList) existingData.get(j)).get(2)));
                        objLodgementInstructionsTO.setServiceTx(CommonUtil.convertObjToDouble(((ArrayList) existingData.get(j)).get(3)));
                        objLodgementInstructionsTO.setLodgementId(txtLodgementId);
                        objLodgementInstructionsTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        objLodgementInstructionsTO.setStatusBy(TrueTransactMain.USER_ID);
                        objLodgementInstructionsTO.setStatusDt(currDt);
                        tblInstructionData.add(objLodgementInstructionsTO);
                        System.out.println("updated existingData=111===" + tblInstructionData);
                    }
                }
                multipleInstructionListMap.put(compareChrgActNo, tblInstructionData);
            }

        }else{ //change here by nithya
            System.out.println("updated existingData=222===" + tbmInstructions.getRowCount());
            ArrayList chrgTblList = (ArrayList) tbmInstructions.getDataArrayList();
            if (tbmInstructions.getRowCount() != 0) {
                String chrgActNo = null;
                String compareChrgActNo = null;
                for (int i = 0; i < chrgTblList.size(); i++) {
                    tblInstructionData = new ArrayList();
                    for (int j = 0; j < chrgTblList.size(); j++) {
                        chrgActNo = CommonUtil.convertObjToStr(((ArrayList) chrgTblList.get(j)).get(4));
                        compareChrgActNo = CommonUtil.convertObjToStr(((ArrayList) chrgTblList.get(i)).get(4));
                        if (chrgActNo.equalsIgnoreCase(compareChrgActNo)) {
                            LodgementInstructionsTO objLodgementInstructionsTO;
                            objLodgementInstructionsTO = new LodgementInstructionsTO();
                            objLodgementInstructionsTO.setSlNo(CommonUtil.convertObjToStr(tbmInstructions.getValueAt(j, 0)));
                            objLodgementInstructionsTO.setInstruction(CommonUtil.convertObjToStr(tbmInstructions.getValueAt(j, 1)));
                            objLodgementInstructionsTO.setAmount(CommonUtil.convertObjToDouble(tbmInstructions.getValueAt(j, 2)));
                            objLodgementInstructionsTO.setServiceTx(CommonUtil.convertObjToDouble(tbmInstructions.getValueAt(j, 3)));
                            objLodgementInstructionsTO.setLodgementId(txtLodgementId);
                            objLodgementInstructionsTO.setStatus(CommonConstants.STATUS_MODIFIED);
                            objLodgementInstructionsTO.setStatusBy(TrueTransactMain.USER_ID);
                            objLodgementInstructionsTO.setStatusDt(currDt);
                            tblInstructionData.add(objLodgementInstructionsTO);
                        }
                    }
                    multipleInstructionListMap.put(compareChrgActNo, tblInstructionData);
                }
            }

        }
        
//        if(deletingExists==true && deletedData!=null){
//            if(deletedData.size()!=0){
//                String chrgActNo = null;
//                String compareChrgActNo = null;
//                for(int i=0;i<deletedData.size();i++){
//                    tblInstructionData = new ArrayList();
//                    for(int j=0;j<deletedData.size();j++){
//                        chrgActNo = CommonUtil.convertObjToStr(((ArrayList) deletedData.get(j)).get(4));
//                        compareChrgActNo = CommonUtil.convertObjToStr(((ArrayList) deletedData.get(i)).get(4));
//                        if (chrgActNo.equalsIgnoreCase(compareChrgActNo)) {
//                            LodgementInstructionsTO objLodgementInstructionsTO;
//                            objLodgementInstructionsTO = new LodgementInstructionsTO();
//                            objLodgementInstructionsTO.setSlNo(CommonUtil.convertObjToStr(deletedData.get(j)));
//                            objLodgementInstructionsTO.setInstruction(CommonUtil.convertObjToStr(deletedData.get(j)));
//                            objLodgementInstructionsTO.setAmount(CommonUtil.convertObjToDouble(deletedData.get(j)));
//                            objLodgementInstructionsTO.setServiceTx(CommonUtil.convertObjToDouble(deletedData.get(j)));
//                            objLodgementInstructionsTO.setLodgementId(txtLodgementId);
//                            objLodgementInstructionsTO.setStatus(CommonConstants.STATUS_DELETED);
//                            objLodgementInstructionsTO.setStatusBy(TrueTransactMain.USER_ID);
//                            objLodgementInstructionsTO.setStatusDt(currDt);
//                            tblInstructionData.add(objLodgementInstructionsTO);
//                        }
//                       
//                    }
//                   multipleInstructionListMap.put(compareChrgActNo,tblInstructionData); 
//                }
//            }
//            
//            deletingExists = false;
//        }
//       if(tbmInstructions.getRowCount()!=0){
//            for(int i=0; i<tbmInstructions.getRowCount();i++){
//                LodgementInstructionsTO objLodgementInstructionsTO;
//                objLodgementInstructionsTO = new LodgementInstructionsTO();
//                objLodgementInstructionsTO.setInstruction(CommonUtil.convertObjToStr(tbmInstructions.getValueAt(i,1)));
//                objLodgementInstructionsTO.setAmount(CommonUtil.convertObjToDouble(tbmInstructions.getValueAt(i,2)));
//                objLodgementInstructionsTO.setServiceTx(CommonUtil.convertObjToDouble(tbmInstructions.getValueAt(i,3))); 
//                objLodgementInstructionsTO.setLodgementId(txtLodgementId);
//                objLodgementInstructionsTO.setStatus(CommonConstants.STATUS_MODIFIED);
//                objLodgementInstructionsTO.setStatusBy(TrueTransactMain.USER_ID);
//                objLodgementInstructionsTO.setStatusDt(currDt);
//                tblInstructionData.add(objLodgementInstructionsTO);
//            }
//        }
        System.out.println("updated multipleInstructionListMap==== "+ multipleInstructionListMap);
    }
    
    /** This populates the tblInstructionList with data to be deleted **/
    private void deletedData(){
        if(removedRow != null && removedRow.size()>0){
            String chrgActNo = null;
            String compareChrgActNo = null;
            for(int i=0; i<removedRow.size();i++){
                tblInstructionData = new ArrayList();
                for(int j=0; j<removedRow.size();j++){
                   chrgActNo = CommonUtil.convertObjToStr(((ArrayList) removedRow.get(j)).get(4));
                   compareChrgActNo = CommonUtil.convertObjToStr(((ArrayList) removedRow.get(i)).get(4)); 
                    if (chrgActNo.equalsIgnoreCase(compareChrgActNo)) {
                        LodgementInstructionsTO objLodgementInstructionsTO;
                        objLodgementInstructionsTO = new LodgementInstructionsTO();
                        objLodgementInstructionsTO.setLodgementId(txtLodgementId);
                        objLodgementInstructionsTO.setSlNo(CommonUtil.convertObjToStr(((ArrayList) removedRow.get(j)).get(0)));
                        objLodgementInstructionsTO.setInstruction(CommonUtil.convertObjToStr(((ArrayList) removedRow.get(j)).get(1)));
                        objLodgementInstructionsTO.setAmount(CommonUtil.convertObjToDouble(((ArrayList) removedRow.get(j)).get(2)));
                        objLodgementInstructionsTO.setServiceTx(CommonUtil.convertObjToDouble(((ArrayList) removedRow.get(j)).get(3)));
                        objLodgementInstructionsTO.setStatus(CommonConstants.STATUS_DELETED);
                        objLodgementInstructionsTO.setStatusBy(TrueTransactMain.USER_ID);
                        objLodgementInstructionsTO.setStatusDt(currDt);
                        tblInstructionData.add(objLodgementInstructionsTO);
                    }
                }
              multipleInstructionListMap.put(compareChrgActNo,tblInstructionData);   
            }
        }
        
    }
    
    /** This populates the tblInstructionData with data to be deleted **/
    private void deletedRow(){
        if(tbmInstructions.getRowCount()!=0){
            for(int i=0; i<tbmInstructions.getRowCount();i++){
                LodgementInstructionsTO objLodgementInstructionsTO;
                objLodgementInstructionsTO = new LodgementInstructionsTO();
                objLodgementInstructionsTO.setSlNo(CommonUtil.convertObjToStr(tbmInstructions.getValueAt(i,0)));
                objLodgementInstructionsTO.setInstruction(CommonUtil.convertObjToStr(tbmInstructions.getValueAt(i,1)));
                objLodgementInstructionsTO.setAmount(CommonUtil.convertObjToDouble(tbmInstructions.getValueAt(i,2)));
                objLodgementInstructionsTO.setServiceTx(CommonUtil.convertObjToDouble(tbmInstructions.getValueAt(i,3)));
                objLodgementInstructionsTO.setLodgementId(txtLodgementId);
                objLodgementInstructionsTO.setStatus(CommonConstants.STATUS_DELETED);
                objLodgementInstructionsTO.setStatusBy(TrueTransactMain.USER_ID);
                objLodgementInstructionsTO.setStatusDt(currDt);
                tblInstructionData.add(objLodgementInstructionsTO);
            }
        }
    }
    
    /** This freeup the memory used by the ArrayLists **/
    private void deInitialize(){
        insertData = null;
        updateData = null;
        removedRow = null;
        existingData = null;
        deletedData = null;
    }
    
    /** This method is used to get the customer name according to the productType and the AccountNumber selected
     *in the ui**/
    public String getCustomerName(){
        final HashMap accountNameMap = new HashMap();
        String custName = "";
        accountNameMap.put("ACC_NUM",getTxtAccountNo());
        List resultList = null;
        resultList = ClientUtil.executeQuery("getAccountNumberName"+getCbmProductType().getKeyForSelected(),accountNameMap);
        if(resultList!=null && resultList.size()!=0){
            final HashMap resultMap = (HashMap)resultList.get(0);
            if(resultMap!=null){
                custName = resultMap.get("CUSTOMER_NAME").toString();
            }
        }
        resultList = null;
        return custName;
    }
   
     public String getCustomerNameCPD(){
        final HashMap accountNameMap = new HashMap();
        String custName = "";
        accountNameMap.put("ACC_NUM",getTxtAccountNum());
        final List resultList = ClientUtil.executeQuery("getAccountNumberNameBILLS",accountNameMap);
        if(resultList!=null){
            if(resultList.size()!=0){
                final HashMap resultMap = (HashMap)resultList.get(0);
                if(resultMap!=null){
                    custName = resultMap.get("CUSTOMER_NAME").toString();
                }
            }
        }
        return custName;
    }
     
/** This method is used to populate the label lblOperatesLike according to the BillsType selected in cboBillsType in the UI **/
    public String getOperatesLike(String prodId){
        HashMap whereMap = new HashMap();
        whereMap.put("PROD_ID",prodId);
        List resultList = ClientUtil.executeQuery("getOperatingType",whereMap);
        String operatesLike = "";
        if(resultList!=null){
            if(resultList.size()!=0){
                final HashMap resultMap = (HashMap)resultList.get(0);
                if(resultMap!=null){
                    operatesLike = resultMap.get("OPERATES_LIKE").toString();
                    setOperatesLikeValue(operatesLike);
                    setSubRegType(resultMap.get("SUB_REG_TYPE").toString());
                }
            }
        }
        return operatesLike;
    }
     public void populateCity(){
        try{
            if(lookUpHash==null)
                lookUpHash = new HashMap();
            
//            getPayableAtBranch();
            
//            if(getPayableAt().equals("ISSU_BRANCH")){
//                HashMap map = new HashMap();
//                map = getBankData(map) ;
//                lookUpHash.put(CommonConstants.PARAMFORQUERY, map);
//                lookUpHash.put(CommonConstants.MAP_NAME,"getCityForPayable");
//            }
//            else{
                HashMap map = new HashMap();
//                map.put("PROD_ID", getCbmRemitProdID().getKeyForSelected());
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                lookUpHash.put(CommonConstants.MAP_NAME,"getCityForBillsRemit");
//            }
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            cbmRemitCity = new ComboBoxModel(key,value);
            setCbmRemitCity(cbmRemitCity);
            makeComboBoxKeyValuesNull();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
     
     public void populateCityforInward(){
           try{
            if(lookUpHash==null)
                lookUpHash = new HashMap();
            
            getPayableAtBranch();
            
            if(getPayableAt().equals("ISSU_BRANCH")){
                HashMap map = new HashMap();
                map = getBankData(map) ;
                lookUpHash.put(CommonConstants.PARAMFORQUERY, map);
                lookUpHash.put(CommonConstants.MAP_NAME,"getCityForPayable");
            }
            else{
                HashMap map = new HashMap();
                map.put("PROD_ID", getCbmRemitProdID().getKeyForSelected());
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                lookUpHash.put(CommonConstants.MAP_NAME,"getCityRemittanceIssue1");
            }
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            cbmRemitCity = new ComboBoxModel(key,value);
            setCbmRemitCity(cbmRemitCity);
            makeComboBoxKeyValuesNull();
        }catch(Exception e){
            parseException.logException(e,true);
        }
     }
     public void populateDraweeBrank(String productId ,String city){
        try{
            if(productId.length()>0){
                System.out.println("getpayyyyy===="+getPayableAt());
                if(getPayableAt().equals("ISSU_BRANCH")){
                    ArrayList key = new ArrayList();
                    key.add("");
                    key.add(TrueTransactMain.BANK_ID);
                    
                    ArrayList value = new ArrayList();
                    value.add("");
                    value.add(TrueTransactMain.BANK_ID) ;
                    System.out.println("bbbbbbbbbbbbb===="+TrueTransactMain.BANK_ID);
                    cbmRemitDraweeBank = new ComboBoxModel(key,value);
                    setCbmRemitDraweeBank(cbmRemitDraweeBank);
                }
                else{
                    if(lookUpHash==null)
                        lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME,"getBankInward");//05-02-2014  getBank
                     System.out.println("productId===="+productId+"   =="+city);
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, getParametersForBank(productId,city));
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
                    cbmRemitDraweeBank = new ComboBoxModel(key,value);
                    setCbmRemitDraweeBank(cbmRemitDraweeBank);
                    makeComboBoxKeyValuesNull();
                }
            }
            productId = null;
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
      public void populateBranchCode(String city, String bankCode){
        try{
            
//            else{
                if(city.length()>0 && bankCode.length()>0){
                    final HashMap where = new HashMap();
                    where.put("CITY",city);
                    where.put("BANK_CODE",bankCode);
                    if(lookUpHash==null)
                        lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME,"getBillsRemitBranch");
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, where);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
                    cbmRemitBranchCode = new ComboBoxModel(key,value);
                    setCbmRemitBranchCode(cbmRemitBranchCode);
                    makeComboBoxKeyValuesNull();
                }
//            }
            city = null;
            bankCode = null;
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
       public void populateBranchCodeForInward(String productId, String bankCode){
        try{
            if(getPayableAt().equals("ISSU_BRANCH")){
                ArrayList key = new ArrayList();
                key.add("");
                key.add(TrueTransactMain.BRANCH_ID);
                
                ArrayList value = new ArrayList();
                value.add("");
                value.add(TrueTransactMain.BRANCH_ID) ;
                cbmRemitBranchCode = new ComboBoxModel(key,value);
                setCbmRemitBranchCode(cbmRemitBranchCode);
            }
            else{
                if(productId.length()>0 && bankCode.length()>0){
                    final HashMap where = new HashMap();
                    where.put("PROD_ID",productId);
                    where.put("BANK_CODE",bankCode);
                    if(lookUpHash==null)
                        lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME,"getBranchInward");//05-02-2014 getbranch
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, where);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
                    cbmRemitBranchCode = new ComboBoxModel(key,value);
                    setCbmRemitBranchCode(cbmRemitBranchCode);
                    makeComboBoxKeyValuesNull();
                }
            }
            productId = null;
            bankCode = null;
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
        public String executeQueryForCharge(String productId, String category, String amount, String chargeType){
        double inputAmt = CommonUtil.convertObjToDouble(amount).doubleValue() ;
        double calculatedCharge = 0.0;
        HashMap where = new HashMap();
        where.put("PROD_ID", productId);
        where.put("CUST_CATEGORY", category);
        where.put("AMOUNT", CommonUtil.convertObjToDouble(amount));
        where.put("CHARGE_TYPE", chargeType);
        where.put("AMOUNT_CHECKING", "AMOUNT_CHECKING");
//        getPayableAtBranch();
//        where.put("PAYABLE", getPayableAt());
//        where.put("BANK_CODE", bankCode);
//        where.put("BRANCH_CODE", branchCode);
        List outList = executeQuery("getBillsExchange", where);
        System.out.println("where111:" + where);
        System.out.println("outList11:" + outList);
      //  where = null ;
        if(outList.size() > 0){ //If a charge is configured. else return default zero
            for(int i = 0;i < outList.size(); i++){
            HashMap outputMap  = (HashMap)outList.get(i);
            
            if(outputMap != null){
                double toAmt = CommonUtil.convertObjToDouble(outputMap.get("TO_AMT")).doubleValue() ;
                double fixedRate = CommonUtil.convertObjToDouble(outputMap.get("CHARGE")).doubleValue() ;
                double percentage = CommonUtil.convertObjToDouble(outputMap.get("COMMISION")).doubleValue() ;

                double forEveryAmt = CommonUtil.convertObjToDouble(outputMap.get("FOR_EVERY_AMT")).doubleValue() ;
                double forEveryRate = CommonUtil.convertObjToDouble(outputMap.get("FOR_EVERY_RATE")).doubleValue() ;
                String forEveryType = CommonUtil.convertObjToStr(outputMap.get("FOR_EVERY_TYPE")) ;
                outputMap = null ;
                if(percentage != 0)
                    calculatedCharge += (inputAmt * percentage) / 100;
                if(fixedRate != 0)
                    calculatedCharge += fixedRate ;
                
                if (inputAmt >= toAmt) {
                    if(forEveryAmt != 0){
                        double remainder = inputAmt - toAmt ;
                        System.out.println("remainderremainderremainder"+remainder);
                        if(forEveryType.toUpperCase().equals("AMOUNT")){ //Value from Lookup Table
                            calculatedCharge = ((remainder / forEveryAmt) * forEveryRate + percentage) ;
                               System.out.println("calculatedCharge111"+calculatedCharge);
                        }
                     
                        else if(forEveryType.toUpperCase().equals("COMMISION")){//Value from Lookup Table
                            calculatedCharge = (((remainder / forEveryAmt) * percentage)/100  +percentage) ;
                               System.out.println("calculatedCharge2222"+calculatedCharge);
                        }
                    }
                }
                 else if (inputAmt < toAmt) {
                   calculatedCharge =percentage;
                }
                //ENDING
               // }
            }
        }
    }
        else{//Not checking amount slab -To calculate commision amount...
            where.remove("AMOUNT_CHECKING");
            List outListN = executeQuery("getBillsExchange", where);
             System.out.println("where2222:" + where);
        System.out.println("outList222:" + outListN);
                      for(int i = 0;i < outListN.size(); i++){
            HashMap outputMap  = (HashMap)outListN.get(0);
            
            if(outputMap != null){
                double toAmt = CommonUtil.convertObjToDouble(outputMap.get("TO_AMT")).doubleValue() ;
                double fixedRate = CommonUtil.convertObjToDouble(outputMap.get("CHARGE")).doubleValue() ;
                double percentage = CommonUtil.convertObjToDouble(outputMap.get("COMMISION")).doubleValue() ;

                double forEveryAmt = CommonUtil.convertObjToDouble(outputMap.get("FOR_EVERY_AMT")).doubleValue() ;
                double forEveryRate = CommonUtil.convertObjToDouble(outputMap.get("FOR_EVERY_RATE")).doubleValue() ;
                String forEveryType = CommonUtil.convertObjToStr(outputMap.get("FOR_EVERY_TYPE")) ;
                 outputMap = null ;
                if(percentage != 0)
                    calculatedCharge += (inputAmt * percentage) / 100;
                if(fixedRate != 0)
                    calculatedCharge += fixedRate ;
                
                if (inputAmt >= toAmt) {
                    if(forEveryAmt != 0){
                        double remainder = inputAmt - toAmt ;
                        System.out.println("remainderremainderremainder"+remainder);
                        if(forEveryType.toUpperCase().equals("AMOUNT")){ //Value from Lookup Table
                            calculatedCharge = ((remainder / forEveryAmt) * forEveryRate + percentage) ;
                               System.out.println("calculatedCharge111"+calculatedCharge);
                        }
                     
                        else if(forEveryType.toUpperCase().equals("COMMISION")){//Value from Lookup Table
                            calculatedCharge = (((remainder / forEveryAmt) * percentage)/100  +percentage) ;
                               System.out.println("calculatedCharge2222"+calculatedCharge);
                        }
                    }
                }
                 else if (inputAmt < toAmt) {
                   calculatedCharge =percentage;
                }
              
            }
        }
        }
        System.out.println("===========>>>>>>>>calculatedCharge : " + calculatedCharge);
        calculatedCharge = (double)getNearest((long)(calculatedCharge *100),100)/100;
        return String.valueOf(calculatedCharge) ;
        
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
     public String calServiceTax(String exchange,String productId,String category,String amount, String chargeType){
         HashMap outputMap = new HashMap();
        double inputAmt = CommonUtil.convertObjToDouble(amount).doubleValue() ;
        double exchangeAmt = CommonUtil.convertObjToDouble(exchange).doubleValue() ;
        double calculatedCharge = 0.0; 
//        if(!bankCode.equalsIgnoreCase("")){
            HashMap where = new HashMap();
            where.put("PROD_ID", productId);
            where.put("CUST_CATEGORY", category);
            where.put("AMOUNT", CommonUtil.convertObjToDouble(amount));
            where.put("CHARGE_TYPE", chargeType);
//            getPayableAtBranch();
//            where.put("PAYABLE", getPayableAt());
//            where.put("BANK_CODE", bankCode);
//            where.put("BRANCH_CODE", branchCode);
            List outList = executeQuery("getBillsServiceTax", where);
            System.out.println("where:" + where);
            System.out.println("outList:" + outList);
            where = null ;
           
            if(outList != null && outList.size()>0){ //If a charge is configured. else return default zero
                 outputMap  = (HashMap)outList.get(0);
                   if(outputMap != null){
                            double serviceTax = CommonUtil.convertObjToDouble(outputMap.get("SERVICE_TAX")).doubleValue() ;
                    if(serviceTax != 0){
                          System.out.println("outputMap222222"+outputMap);
                        calculatedCharge = (exchangeAmt * serviceTax) / 100;
                    }
                }
            }
            System.out.println("===========>>>>>>>>calculatedCharge : " + calculatedCharge);
            calculatedCharge = (double)getNearest((long)(calculatedCharge *100),100)/100;
//           }
     
        return String.valueOf(calculatedCharge) ; 
     
    }
     public double roundInterest(double intAmt) {
       double intamt = (double)getNearest((long)(intAmt *100),100)/100;
       return intamt;
    }
      public void makeComboBoxKeyValuesNull(){
        key = null;
        value = null;
        lookUpHash = null;
        keyValue = null;
    }
       public HashMap getParametersForBank(String productId,String city)throws Exception {
        final HashMap where = new HashMap();
        where.put("PROD_ID", productId);
        where.put("CITY",city);
        return where;
    }
       private void getPayableAtBranch(){
        String prodId = (String)cbmRemitProdID.getKeyForSelected();
        String payableAt = "" ;
        HashMap map = new HashMap();
        map.put("PRODUCT_ID", prodId) ;
        List tempList = ClientUtil.executeQuery("getPayableAt", map);
        System.out.println("map = "+map);
        System.out.println("in getPayableAtBranch() tempList.size() : "+tempList.size());
        if(tempList.size() > 0){
            setPayableAt(CommonUtil.convertObjToStr(tempList.get(0))) ;
        System.out.println("tempList.get(0) : "+tempList.get(0));}
    }
    public HashMap getInterestDetails(HashMap intDetails){
        HashMap intMap = new HashMap();
        List tempList = ClientUtil.executeQuery("getIntRateBills", intDetails);
        if(tempList != null && tempList.size() > 0){
            intMap = (HashMap)tempList.get(0);
            System.out.println("#####intMap : "+intMap);
        }
        return intMap;
    }
       
    private HashMap getBankData(HashMap map){
        map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        return map ;
    }
    /**
     * Getter for property cbmActivities.
     * @return Value of property cbmActivities.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmActivities() {
        return cbmActivities;
    }
    
    /**
     * Setter for property cbmActivities.
     * @param cbmActivities New value of property cbmActivities.
     */
    public void setCbmActivities(com.see.truetransact.clientutil.ComboBoxModel cbmActivities) {
        this.cbmActivities = cbmActivities;
    }
    
    /**
     * Getter for property cboActivities.
     * @return Value of property cboActivities.
     */
    public java.lang.String getCboActivities() {
        return cboActivities;
    }
    
    /**
     * Setter for property cboActivities.
     * @param cboActivities New value of property cboActivities.
     */
    public void setCboActivities(java.lang.String cboActivities) {
        this.cboActivities = cboActivities;
    }
    
    /**
     * Getter for property txtAmount.
     * @return Value of property txtAmount.
     */
    public java.lang.String getTxtAmount() {
        return txtAmount;
    }
    
    /**
     * Setter for property txtAmount.
     * @param txtAmount New value of property txtAmount.
     */
    public void setTxtAmount(java.lang.String txtAmount) {
        this.txtAmount = txtAmount;
    }
    
    /**
     * Getter for property txtTotalAmt.
     * @return Value of property txtTotalAmt.
     */
    public java.lang.String getTxtTotalAmt() {
        return txtTotalAmt;
    }
    
    /**
     * Setter for property txtTotalAmt.
     * @param txtTotalAmt New value of property txtTotalAmt.
     */
    public void setTxtTotalAmt(java.lang.String txtTotalAmt) {
        this.txtTotalAmt = txtTotalAmt;
    }
    
    /**
     * Getter for property _authorizeMap.
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }
    
    /**
     * Setter for property _authorizeMap.
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }
    
    /**
     * Getter for property cboRemitProdID.
     * @return Value of property cboRemitProdID.
     */
    public java.lang.String getCboRemitProdID() {
        return cboRemitProdID;
    }
    
    /**
     * Setter for property cboRemitProdID.
     * @param cboRemitProdID New value of property cboRemitProdID.
     */
    public void setCboRemitProdID(java.lang.String cboRemitProdID) {
        this.cboRemitProdID = cboRemitProdID;
    }
    
    /**
     * Getter for property cboRemitCity.
     * @return Value of property cboRemitCity.
     */
    public java.lang.String getCboRemitCity() {
        return cboRemitCity;
    }
    
    /**
     * Setter for property cboRemitCity.
     * @param cboRemitCity New value of property cboRemitCity.
     */
    public void setCboRemitCity(java.lang.String cboRemitCity) {
        this.cboRemitCity = cboRemitCity;
    }
    
    /**
     * Getter for property cboRemitDraweeBank.
     * @return Value of property cboRemitDraweeBank.
     */
    public java.lang.String getCboRemitDraweeBank() {
        return cboRemitDraweeBank;
    }
    
    /**
     * Setter for property cboRemitDraweeBank.
     * @param cboRemitDraweeBank New value of property cboRemitDraweeBank.
     */
    public void setCboRemitDraweeBank(java.lang.String cboRemitDraweeBank) {
        this.cboRemitDraweeBank = cboRemitDraweeBank;
    }
    
    /**
     * Getter for property cboRemitBranchCode.
     * @return Value of property cboRemitBranchCode.
     */
    public java.lang.String getCboRemitBranchCode() {
        return cboRemitBranchCode;
    }
    
    /**
     * Setter for property cboRemitBranchCode.
     * @param cboRemitBranchCode New value of property cboRemitBranchCode.
     */
    public void setCboRemitBranchCode(java.lang.String cboRemitBranchCode) {
        this.cboRemitBranchCode = cboRemitBranchCode;
    }
    
    /**
     * Getter for property txtRemitFavour.
     * @return Value of property txtRemitFavour.
     */
    public java.lang.String getTxtRemitFavour() {
        return txtRemitFavour;
    }
    
    /**
     * Setter for property txtRemitFavour.
     * @param txtRemitFavour New value of property txtRemitFavour.
     */
    public void setTxtRemitFavour(java.lang.String txtRemitFavour) {
        this.txtRemitFavour = txtRemitFavour;
    }
    
    /**
     * Getter for property txtInst1.
     * @return Value of property txtInst1.
     */
    public java.lang.String getTxtInst1() {
        return txtInst1;
    }
    
    /**
     * Setter for property txtInst1.
     * @param txtInst1 New value of property txtInst1.
     */
    public void setTxtInst1(java.lang.String txtInst1) {
        this.txtInst1 = txtInst1;
    }
    
    /**
     * Getter for property txtInst2.
     * @return Value of property txtInst2.
     */
    public java.lang.String getTxtInst2() {
        return txtInst2;
    }
    
    /**
     * Setter for property txtInst2.
     * @param txtInst2 New value of property txtInst2.
     */
    public void setTxtInst2(java.lang.String txtInst2) {
        this.txtInst2 = txtInst2;
    }
    
    /**
     * Getter for property cbmRemitProdID.
     * @return Value of property cbmRemitProdID.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRemitProdID() {
        return cbmRemitProdID;
    }
    
    /**
     * Setter for property cbmRemitProdID.
     * @param cbmRemitProdID New value of property cbmRemitProdID.
     */
    public void setCbmRemitProdID(com.see.truetransact.clientutil.ComboBoxModel cbmRemitProdID) {
        this.cbmRemitProdID = cbmRemitProdID;
        setChanged();
    }
    
    /**
     * Getter for property cbmRemitCity.
     * @return Value of property cbmRemitCity.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRemitCity() {
        return cbmRemitCity;
    }
    
    /**
     * Setter for property cbmRemitCity.
     * @param cbmRemitCity New value of property cbmRemitCity.
     */
    public void setCbmRemitCity(com.see.truetransact.clientutil.ComboBoxModel cbmRemitCity) {
        this.cbmRemitCity = cbmRemitCity;
    }
    
    /**
     * Getter for property cbmRemitDraweeBank.
     * @return Value of property cbmRemitDraweeBank.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRemitDraweeBank() {
        return cbmRemitDraweeBank;
    }
    
    /**
     * Setter for property cbmRemitDraweeBank.
     * @param cbmRemitDraweeBank New value of property cbmRemitDraweeBank.
     */
    public void setCbmRemitDraweeBank(com.see.truetransact.clientutil.ComboBoxModel cbmRemitDraweeBank) {
        this.cbmRemitDraweeBank = cbmRemitDraweeBank;
    }
    
    /**
     * Getter for property cbmRemitBranchCode.
     * @return Value of property cbmRemitBranchCode.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRemitBranchCode() {
        return cbmRemitBranchCode;
    }
    
    /**
     * Setter for property cbmRemitBranchCode.
     * @param cbmRemitBranchCode New value of property cbmRemitBranchCode.
     */
    public void setCbmRemitBranchCode(com.see.truetransact.clientutil.ComboBoxModel cbmRemitBranchCode) {
        this.cbmRemitBranchCode = cbmRemitBranchCode;
    }
    
    /**
     * Getter for property payableAt.
     * @return Value of property payableAt.
     */
    public java.lang.String getPayableAt() {
        return payableAt;
    }
    
    /**
     * Setter for property payableAt.
     * @param payableAt New value of property payableAt.
     */
    public void setPayableAt(java.lang.String payableAt) {
        this.payableAt = payableAt;
    }
    
    /**
     * Getter for property txtInstAmt.
     * @return Value of property txtInstAmt.
     */
    public java.lang.String getTxtInstAmt() {
        return txtInstAmt;
    }
    
    /**
     * Setter for property txtInstAmt.
     * @param txtInstAmt New value of property txtInstAmt.
     */
    public void setTxtInstAmt(java.lang.String txtInstAmt) {
        this.txtInstAmt = txtInstAmt;
    }
    
    /**
     * Getter for property cboCustCategory.
     * @return Value of property cboCustCategory.
     */
    public java.lang.String getCboCustCategory() {
        return cboCustCategory;
    }
    
    /**
     * Setter for property cboCustCategory.
     * @param cboCustCategory New value of property cboCustCategory.
     */
    public void setCboCustCategory(java.lang.String cboCustCategory) {
        this.cboCustCategory = cboCustCategory;
    }
    
    /**
     * Getter for property cbmCustCategory.
     * @return Value of property cbmCustCategory.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCustCategory() {
        return cbmCustCategory;
    }
    
    /**
     * Setter for property cbmCustCategory.
     * @param cbmCustCategory New value of property cbmCustCategory.
     */
    public void setCbmCustCategory(com.see.truetransact.clientutil.ComboBoxModel cbmCustCategory) {
        this.cbmCustCategory = cbmCustCategory;
    }
      /**
     * Getter for property cbmCustCategory.
     * @return Value of property cbmCustCategory.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmTrantype() {
        return cbmTranstype;
    }
    
    /**
     * Setter for property cbmCustCategory.
     * @param cbmCustCategory New value of property cbmCustCategory.
     */
    public void setCbmTrantype(com.see.truetransact.clientutil.ComboBoxModel cbmTranstype) {
        this.cbmTranstype = cbmTranstype;
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
     * Getter for property txttotalServTax.
     * @return Value of property txttotalServTax.
     */
    public java.lang.String getTxttotalServTax() {
        return txttotalServTax;
    }
    
    /**
     * Setter for property txttotalServTax.
     * @param txttotalServTax New value of property txttotalServTax.
     */
    public void setTxttotalServTax(java.lang.String txttotalServTax) {
        this.txttotalServTax = txttotalServTax;
    }
    
    /**
     * Getter for property remitStatus.
     * @return Value of property remitStatus.
     */
    public java.lang.String getRemitStatus() {
        return remitStatus;
    }
    
    /**
     * Setter for property remitStatus.
     * @param remitStatus New value of property remitStatus.
     */
    public void setRemitStatus(java.lang.String remitStatus) {
        this.remitStatus = remitStatus;
    }
    
    /**
     * Getter for property tdtRemitInstDate.
     * @return Value of property tdtRemitInstDate.
     */
    public java.lang.String getTdtRemitInstDate() {
        return tdtRemitInstDate;
    }
    
    /**
     * Setter for property tdtRemitInstDate.
     * @param tdtRemitInstDate New value of property tdtRemitInstDate.
     */
    public void setTdtRemitInstDate(java.lang.String tdtRemitInstDate) {
        this.tdtRemitInstDate = tdtRemitInstDate;
    }
    
    /**
     * Getter for property cboTranstype.
     * @return Value of property cboTranstype.
     */
    public java.lang.String getCboTranstype() {
        return cboTranstype;
    }
    
    /**
     * Setter for property cboTranstype.
     * @param cboTranstype New value of property cboTranstype.
     */
    public void setCboTranstype(java.lang.String cboTranstype) {
        this.cboTranstype = cboTranstype;
    }
    
    /**
     * Getter for property txtRemitFavour1.
     * @return Value of property txtRemitFavour1.
     */
    public java.lang.String getTxtRemitFavour1() {
        return txtRemitFavour1;
    }
    
    /**
     * Setter for property txtRemitFavour1.
     * @param txtRemitFavour1 New value of property txtRemitFavour1.
     */
    public void setTxtRemitFavour1(java.lang.String txtRemitFavour1) {
        this.txtRemitFavour1 = txtRemitFavour1;
    }
    
    /**
     * Getter for property txtRateForDelay.
     * @return Value of property txtRateForDelay.
     */
    public java.lang.String getTxtRateForDelay() {
        return txtRateForDelay;
    }
    
    /**
     * Setter for property txtRateForDelay.
     * @param txtRateForDelay New value of property txtRateForDelay.
     */
    public void setTxtRateForDelay(java.lang.String txtRateForDelay) {
        this.txtRateForDelay = txtRateForDelay;
    }
    
    /**
     * Getter for property cboInstruction.
     * @return Value of property cboInstruction.
     */
    public java.lang.String getCboInstruction() {
        return cboInstruction;
    }
    
    /**
     * Setter for property cboInstruction.
     * @param cboInstruction New value of property cboInstruction.
     */
    public void setCboInstruction(java.lang.String cboInstruction) {
        this.cboInstruction = cboInstruction;
    }
    
    /**
     * Getter for property cbmInstruction.
     * @return Value of property cbmInstruction.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmInstruction() {
        return cbmInstruction;
    }
    
    /**
     * Setter for property cbmInstruction.
     * @param cbmInstruction New value of property cbmInstruction.
     */
    public void setCbmInstruction(com.see.truetransact.clientutil.ComboBoxModel cbmInstruction) {
        this.cbmInstruction = cbmInstruction;
    }
    
    /**
     * Getter for property txtAreaParticular.
     * @return Value of property txtAreaParticular.
     */
    public java.lang.String getTxtAreaParticular() {
        return txtAreaParticular;
    }
    
    /**
     * Setter for property txtAreaParticular.
     * @param txtAreaParticular New value of property txtAreaParticular.
     */
    public void setTxtAreaParticular(java.lang.String txtAreaParticular) {
        this.txtAreaParticular = txtAreaParticular;
    }
    
    /**
     * Getter for property tbmInstructions1.
     * @return Value of property tbmInstructions1.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbmInstructions1() {
        return tbmInstructions1;
    }
    
    /**
     * Setter for property tbmInstructions1.
     * @param tbmInstructions1 New value of property tbmInstructions1.
     */
    public void setTbmInstructions1(com.see.truetransact.clientutil.EnhancedTableModel tbmInstructions1) {
        this.tbmInstructions1 = tbmInstructions1;
    }
    
    /**
     * Getter for property subRegType.
     * @return Value of property subRegType.
     */
    public java.lang.String getSubRegType() {
        return subRegType;
    }
    
    /**
     * Setter for property subRegType.
     * @param subRegType New value of property subRegType.
     */
    public void setSubRegType(java.lang.String subRegType) {
        this.subRegType = subRegType;
    }
    
     /**
     * Getter for property subRegType.
     * @return Value of property subRegType.
     */
    public java.lang.String getOperatesLikeValue() {
        return operatesLikeValue;
    }
    
    /**
     * Setter for property subRegType.
     * @param subRegType New value of property subRegType.
     */
    public void setOperatesLikeValue(java.lang.String operatesLikeValue) {
        this.operatesLikeValue = operatesLikeValue;
    }
    
    /**
     * Getter for property cboProductID.
     * @return Value of property cboProductID.
     */
    public java.lang.String getCboProductID() {
        return cboProductID;
    }
    
    /**
     * Setter for property cboProductID.
     * @param cboProductID New value of property cboProductID.
     */
    public void setCboProductID(java.lang.String cboProductID) {
        this.cboProductID = cboProductID;
    }
    
    /**
     * Getter for property txtAccountNum.
     * @return Value of property txtAccountNum.
     */
    public java.lang.String getTxtAccountNum() {
        return txtAccountNum;
    }
    
    /**
     * Setter for property txtAccountNum.
     * @param txtAccountNum New value of property txtAccountNum.
     */
    public void setTxtAccountNum(java.lang.String txtAccountNum) {
        this.txtAccountNum = txtAccountNum;
    }
    
    /**
     * Getter for property cbmProductID.
     * @return Value of property cbmProductID.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProductID() {
        return cbmProductID;
    }
    
    /**
     * Setter for property cbmProductID.
     * @param cbmProductID New value of property cbmProductID.
     */
    public void setCbmProductID(com.see.truetransact.clientutil.ComboBoxModel cbmProductID) {
        this.cbmProductID = cbmProductID;
    }
    
    /**
     * Getter for property txtRateForDelay1.
     * @return Value of property txtRateForDelay1.
     */
    public java.lang.String getTxtRateForDelay1() {
        return txtRateForDelay1;
    }
    
    /**
     * Setter for property txtRateForDelay1.
     * @param txtRateForDelay1 New value of property txtRateForDelay1.
     */
    public void setTxtRateForDelay1(java.lang.String txtRateForDelay1) {
        this.txtRateForDelay1 = txtRateForDelay1;
    }
    
    /**
     * Getter for property txtIntDays.
     * @return Value of property txtIntDays.
     */
    public java.lang.String getTxtIntDays() {
        return txtIntDays;
    }
    
    /**
     * Setter for property txtIntDays.
     * @param txtIntDays New value of property txtIntDays.
     */
    public void setTxtIntDays(java.lang.String txtIntDays) {
        this.txtIntDays = txtIntDays;
    }
    
    /**
     * Getter for property cboIntDays.
     * @return Value of property cboIntDays.
     */
    public java.lang.String getCboIntDays() {
        return cboIntDays;
    }
    
    /**
     * Setter for property cboIntDays.
     * @param cboIntDays New value of property cboIntDays.
     */
    public void setCboIntDays(java.lang.String cboIntDays) {
        this.cboIntDays = cboIntDays;
    }
    
    /**
     * Getter for property txtDiscountRateBills.
     * @return Value of property txtDiscountRateBills.
     */
    public java.lang.String getTxtDiscountRateBills() {
        return txtDiscountRateBills;
    }
    
    /**
     * Setter for property txtDiscountRateBills.
     * @param txtDiscountRateBills New value of property txtDiscountRateBills.
     */
    public void setTxtDiscountRateBills(java.lang.String txtDiscountRateBills) {
        this.txtDiscountRateBills = txtDiscountRateBills;
    }
    
    /**
     * Getter for property txtOverdueRateBills.
     * @return Value of property txtOverdueRateBills.
     */
    public java.lang.String getTxtOverdueRateBills() {
        return txtOverdueRateBills;
    }
    
    /**
     * Setter for property txtOverdueRateBills.
     * @param txtOverdueRateBills New value of property txtOverdueRateBills.
     */
    public void setTxtOverdueRateBills(java.lang.String txtOverdueRateBills) {
        this.txtOverdueRateBills = txtOverdueRateBills;
    }
    
    /**
     * Getter for property txtRateForCBP.
     * @return Value of property txtRateForCBP.
     */
    public java.lang.String getTxtRateForCBP() {
        return txtRateForCBP;
    }
    
    /**
     * Setter for property txtRateForCBP.
     * @param txtRateForCBP New value of property txtRateForCBP.
     */
    public void setTxtRateForCBP(java.lang.String txtRateForCBP) {
        this.txtRateForCBP = txtRateForCBP;
    }
    
    /**
     * Getter for property txtAtParLimit.
     * @return Value of property txtAtParLimit.
     */
    public java.lang.String getTxtAtParLimit() {
        return txtAtParLimit;
    }
    
    /**
     * Setter for property txtAtParLimit.
     * @param txtAtParLimit New value of property txtAtParLimit.
     */
    public void setTxtAtParLimit(java.lang.String txtAtParLimit) {
        this.txtAtParLimit = txtAtParLimit;
    }
    
    /**
     * Getter for property txtCleanBills.
     * @return Value of property txtCleanBills.
     */
    public java.lang.String getTxtCleanBills() {
        return txtCleanBills;
    }
    
    /**
     * Setter for property txtCleanBills.
     * @param txtCleanBills New value of property txtCleanBills.
     */
    public void setTxtCleanBills(java.lang.String txtCleanBills) {
        this.txtCleanBills = txtCleanBills;
    }
    
    /**
     * Getter for property txtTransitPeriod.
     * @return Value of property txtTransitPeriod.
     */
    public java.lang.String getTxtTransitPeriod() {
        return txtTransitPeriod;
    }
    
    /**
     * Setter for property txtTransitPeriod.
     * @param txtTransitPeriod New value of property txtTransitPeriod.
     */
    public void setTxtTransitPeriod(java.lang.String txtTransitPeriod) {
        this.txtTransitPeriod = txtTransitPeriod;
    }
    
    /**
     * Getter for property cboTransitPeriod.
     * @return Value of property cboTransitPeriod.
     */
    public java.lang.String getCboTransitPeriod() {
        return cboTransitPeriod;
    }
    
    /**
     * Setter for property cboTransitPeriod.
     * @param cboTransitPeriod New value of property cboTransitPeriod.
     */
    public void setCboTransitPeriod(java.lang.String cboTransitPeriod) {
        this.cboTransitPeriod = cboTransitPeriod;
    }
    
    /**
     * Getter for property txtDefaultPostage.
     * @return Value of property txtDefaultPostage.
     */
    public java.lang.String getTxtDefaultPostage() {
        return txtDefaultPostage;
    }
    
    /**
     * Setter for property txtDefaultPostage.
     * @param txtDefaultPostage New value of property txtDefaultPostage.
     */
    public void setTxtDefaultPostage(java.lang.String txtDefaultPostage) {
        this.txtDefaultPostage = txtDefaultPostage;
    }
    
    /**
     * Getter for property cbmIntDays.
     * @return Value of property cbmIntDays.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmIntDays() {
        return cbmIntDays;
    }
    
    /**
     * Setter for property cbmIntDays.
     * @param cbmIntDays New value of property cbmIntDays.
     */
    public void setCbmIntDays(com.see.truetransact.clientutil.ComboBoxModel cbmIntDays) {
        this.cbmIntDays = cbmIntDays;
    }
    
    /**
     * Getter for property cbmTransitPeriod.
     * @return Value of property cbmTransitPeriod.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmTransitPeriod() {
        return cbmTransitPeriod;
    }
    
    /**
     * Setter for property cbmTransitPeriod.
     * @param cbmTransitPeriod New value of property cbmTransitPeriod.
     */
    public void setCbmTransitPeriod(com.see.truetransact.clientutil.ComboBoxModel cbmTransitPeriod) {
        this.cbmTransitPeriod = cbmTransitPeriod;
    }
    
    /**
     * Getter for property cRadio_ICC_Yes.
     * @return Value of property cRadio_ICC_Yes.
     */
    public boolean isCRadio_ICC_Yes() {
        return cRadio_ICC_Yes;
    }
    
    /**
     * Setter for property cRadio_ICC_Yes.
     * @param cRadio_ICC_Yes New value of property cRadio_ICC_Yes.
     */
    public void setCRadio_ICC_Yes(boolean cRadio_ICC_Yes) {
        this.cRadio_ICC_Yes = cRadio_ICC_Yes;
    }
    
    /**
     * Getter for property cRadio_ICC_No.
     * @return Value of property cRadio_ICC_No.
     */
    public boolean isCRadio_ICC_No() {
        return cRadio_ICC_No;
    }
    
    /**
     * Setter for property cRadio_ICC_No.
     * @param cRadio_ICC_No New value of property cRadio_ICC_No.
     */
    public void setCRadio_ICC_No(boolean cRadio_ICC_No) {
        this.cRadio_ICC_No = cRadio_ICC_No;
    }
    
    /**
     * Getter for property tbmInstructions2.
     * @return Value of property tbmInstructions2.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbmInstructions2() {
        return tbmInstructions2;
    }
    
    /**
     * Setter for property tbmInstructions2.
     * @param tbmInstructions2 New value of property tbmInstructions2.
     */
    public void setTbmInstructions2(com.see.truetransact.clientutil.EnhancedTableModel tbmInstructions2) {
        this.tbmInstructions2 = tbmInstructions2;
    }
    
    public boolean checkAcNoWithoutProdType(String actNum) {
        HashMap mapData=new HashMap();
        boolean isExists = false;
        try{//dont delete chck selectalldao
            mapData.put("ACT_NUM", actNum);
            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData); 
            System.out.println("#### mapDataList :"+mapDataList);
            if (mapDataList!=null && mapDataList.size()>0) {
                mapData=(HashMap)mapDataList.get(0);
                setTxtAccountNo(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                cbmProductType.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));                    
                setCboProductType(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                getProductIdByType(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                cbmProductId.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                setCboProductId(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                setSelectedBranchID(CommonUtil.convertObjToStr(mapData.get("BRANCH_ID")));
                isExists = true;
            } else {
//                ArrayList key=new ArrayList();
//                ArrayList value=new ArrayList();
//                key.add("");
//                value.add("");   
                getProductIdByType("GL");
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
    
    /**
     * Getter for property txtAccountHeadValue.
     * @return Value of property txtAccountHeadValue.
     */
    public java.lang.String getTxtAccountHeadValue() {
        return txtAccountHeadValue;
    }
    
    /**
     * Setter for property txtAccountHeadValue.
     * @param txtAccountHeadValue New value of property txtAccountHeadValue.
     */
    public void setTxtAccountHeadValue(java.lang.String txtAccountHeadValue) {
        this.txtAccountHeadValue = txtAccountHeadValue;
    }
    
    /**
     * Getter for property txtDraweeBankNameVal.
     * @return Value of property txtDraweeBankNameVal.
     */
    public java.lang.String getTxtDraweeBankNameVal() {
        return txtDraweeBankNameVal;
    }
    
    /**
     * Setter for property txtDraweeBankNameVal.
     * @param txtDraweeBankNameVal New value of property txtDraweeBankNameVal.
     */
    public void setTxtDraweeBankNameVal(java.lang.String txtDraweeBankNameVal) {
        this.txtDraweeBankNameVal = txtDraweeBankNameVal;
    }
    
    /**
     * Getter for property otherBankProdType.
     * @return Value of property otherBankProdType.
     */
    public java.lang.String getOtherBankProdType() {
        return otherBankProdType;
    }
    
    /**
     * Setter for property otherBankProdType.
     * @param otherBankProdType New value of property otherBankProdType.
     */
    public void setOtherBankProdType(java.lang.String otherBankProdType) {
        this.otherBankProdType = otherBankProdType;
    }
       public com.see.truetransact.clientutil.EnhancedTableModel getTblOBDModel() {
        return tblOBDModel;
    }
    
    /**
     * Setter for property tblOBDModel.
     * @param tblOBDModel New value of property tblOBDModel.
     */
    public void setTblOBDModel(com.see.truetransact.clientutil.EnhancedTableModel tblOBDModel) {
        this.tblOBDModel = tblOBDModel;
    }
    
    /**
     * Getter for property tdtRemittedDt.
     * @return Value of property tdtRemittedDt.
     */
    public java.lang.String getTdtRemittedDt() {
        return tdtRemittedDt;
    }
    
    /**
     * Setter for property tdtRemittedDt.
     * @param tdtRemittedDt New value of property tdtRemittedDt.
     */
    public void setTdtRemittedDt(java.lang.String tdtRemittedDt) {
        this.tdtRemittedDt = tdtRemittedDt;
    }
    private String checkServiceTaxApplicable() {
        HashMap whereMap = new HashMap();
        whereMap.put("value", CommonUtil.convertObjToStr(getCbmBillsType().getKeyForSelected()));
        String checkFlag = "N";
        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
            String bankChareHead = "";
            List resultList = ClientUtil.executeQuery("getBillsAccountHeads", whereMap);
            if (resultList != null && resultList.size() > 0) {
                HashMap newMap = (HashMap) resultList.get(0);
                if (newMap != null && newMap.containsKey("BANK_CHARGE_HEAD")) {
                    bankChareHead = CommonUtil.convertObjToStr(newMap.get("BANK_CHARGE_HEAD"));
                    if (bankChareHead != null && bankChareHead.length() > 0) {
                        whereMap = new HashMap();
                        whereMap.put("AC_HD_ID", bankChareHead);
                        List accHeadList = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);
                        if (accHeadList != null && accHeadList.size() > 0) {
                            HashMap accHeadMap = (HashMap) accHeadList.get(0);
                            if (accHeadMap != null && accHeadMap.containsKey("SERVICE_TAX_APPLICABLE")) {
                                checkFlag = CommonUtil.convertObjToStr(accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                            }
                        }
                    }
                }
            }
        }
        return checkFlag;
    }
    public ServiceTaxDetailsTO setServiceTaxDetails(String command, String sbAcctNo) {
        final ServiceTaxDetailsTO objservicetaxDetTo = new ServiceTaxDetailsTO();
        try {
            HashMap tempService_TaxMap = (HashMap)serviceTax_Map.get(sbAcctNo);
            objservicetaxDetTo.setCommand(command);
            objservicetaxDetTo.setStatus(CommonConstants.STATUS_CREATED);
            objservicetaxDetTo.setStatusBy(TrueTransactMain.USER_ID);
            objservicetaxDetTo.setAcct_Num(billsNo);
            objservicetaxDetTo.setParticulars("Cash");
            objservicetaxDetTo.setBranchID(TrueTransactMain.BRANCH_ID);
            objservicetaxDetTo.setTrans_type("C");
            if (tempService_TaxMap != null && tempService_TaxMap.containsKey("SERVICE_TAX")) {
                objservicetaxDetTo.setServiceTaxAmt(CommonUtil.convertObjToDouble(tempService_TaxMap.get("SERVICE_TAX")));
            }
            if (tempService_TaxMap != null && tempService_TaxMap.containsKey("EDUCATION_CESS")) {
                objservicetaxDetTo.setEducationCess(CommonUtil.convertObjToDouble(tempService_TaxMap.get("EDUCATION_CESS")));
            }
            if (tempService_TaxMap != null && tempService_TaxMap.containsKey("HIGHER_EDU_CESS")) {
                objservicetaxDetTo.setHigherCess(CommonUtil.convertObjToDouble(tempService_TaxMap.get("HIGHER_EDU_CESS")));
            }
            if (tempService_TaxMap != null && tempService_TaxMap.containsKey("TOT_TAX_AMT")) {
                objservicetaxDetTo.setTotalTaxAmt(CommonUtil.convertObjToDouble(tempService_TaxMap.get("TOT_TAX_AMT")));
            }
            double roudVal = objservicetaxDetTo.getTotalTaxAmt() - (objservicetaxDetTo.getServiceTaxAmt() + objservicetaxDetTo.getEducationCess() + objservicetaxDetTo.getHigherCess());
            ServiceTaxCalculation serviceTaxObj= new ServiceTaxCalculation();
            objservicetaxDetTo.setRoundVal(CommonUtil.convertObjToStr(serviceTaxObj.roundOffAmtForRoundVal(roudVal)));
            objservicetaxDetTo.setStatusDt(currDt);

            if (command.equalsIgnoreCase("INSERT")) {
                objservicetaxDetTo.setCreatedBy(TrueTransactMain.USER_ID);
                objservicetaxDetTo.setCreatedDt(currDt);
            }
            
        } catch (Exception e) {
            log.info("Error In setLoanApplicationData()");
            e.printStackTrace();
        }
        return objservicetaxDetTo;

    }
    public boolean getBillsProduct(String billType){
        HashMap whereMap = new HashMap();
        boolean flag = false;
        if(billType != null && !billType.equals("")){
        whereMap.put("PROD_ID", billType);
        BillsTO billsTo = null;
        List list = (List) ClientUtil.executeQuery("getSelectBillsTO", whereMap);
        System.out.println("getBillsTO#####" + list);
        if(list != null && list.size() > 0){
            billsTo = (BillsTO)list.get(0);
            if((billsTo != null) && (billsTo.getContraAcHdYn() != null && !billsTo.getContraAcHdYn().equals("") && billsTo.getContraAcHdYn().equalsIgnoreCase("Y"))){
                flag = true;
            }else{
                flag =false;
            }
        }
        }
        return flag;
    } 
    
    private String getLodgement_BranchAcInfo() throws Exception {
        HashMap where = new HashMap();
        String billsType = CommonUtil.convertObjToStr(getCbmBillsType().getKeyForSelected());
        where.put("BILLS_TYPE", billsType);
        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        String billsNo = "";        
        List todList = ClientUtil.executeQuery("geUpdatedBillsId", where); 
        if(todList != null && todList.size() > 0){
            billsNo = (String) todList.get(0);
            
        }
        //CURR_VALUE
        System.out.println("billsNo$%#%#"+billsNo);
        return billsNo;
    }
    
    private void setChargeFields(){
        
    }
    
    
    
    public ArrayList listAfterDuplicateRempval(){        
         
         ArrayList selectedRow;
         ArrayList selectedRowIn;
         ArrayList tbmActNoArray = (ArrayList)(tbmInstructions.getDataArrayList()).clone();
         //ArrayList actArray = tbmActNoArray;
         if (tbmActNoArray.size() > 1) {
            for (int i = 1; i < tbmActNoArray.size(); i++) {
                selectedRow = (ArrayList) (tbmActNoArray.get(i));
                selectedRowIn = (ArrayList) (tbmActNoArray.get(i-1));
                String sbAcctNo = CommonUtil.convertObjToStr(selectedRow.get(4));
                String sbAcctNoIn = CommonUtil.convertObjToStr(selectedRowIn.get(4));
                if (sbAcctNo.equalsIgnoreCase(sbAcctNoIn)) {
                        tbmActNoArray.remove(i);
                }                
            }
        }
         System.out.println("nithya newList :: " + tbmActNoArray);
         return tbmActNoArray;
    }
    
    
    public void execute(String command) {
        TTException exception = null;
        try {
            HashMap term = new HashMap();
            ArrayList selectedRow;
            if(command.equalsIgnoreCase(CommonConstants.TOSTATUS_INSERT)){
               lodgementId = getLodgement_BranchAcInfo();
               setLodgementId(lodgementId);
            }else{
               lodgementId = getLodgementId(); 
            }
            ArrayList tbmActNoArray = tbmInstructions.getDataArrayList(); 
            //ArrayList actList =  getActNoArrayList();
            ArrayList newList = listAfterDuplicateRempval();
            //System.out.println("actList size :: " + actList);
            System.out.println("newList size :: " + newList);
            System.out.println("lodgementId :: " + lodgementId);
            System.out.println("data array list :: " + tbmInstructions.getDataArrayList());                      
            System.out.println("getServiceTax_Map() :: " + getServiceTax_Map());
            if (newList != null && newList.size() > 0) {
                double totalChequeAmount = 0;
                for (int i = 0; i < newList.size(); i++) {
                    selectedRow = (ArrayList) (newList.get(i));
                    String prodId = CommonUtil.convertObjToStr(selectedRow.get(5));
                    String productType = CommonUtil.convertObjToStr(selectedRow.get(6));
                    String sbAcctNo = CommonUtil.convertObjToStr(selectedRow.get(4));
                    Double amount = CommonUtil.convertObjToDouble(selectedRow.get(7)); 
                    String instr = CommonUtil.convertObjToStr(selectedRow.get(1));
                    Double instAmnt = CommonUtil.convertObjToDouble(selectedRow.get(2)); 
                    Double serviceTax = CommonUtil.convertObjToDouble(selectedRow.get(3)); 
                    String remitFavr = CommonUtil.convertObjToStr(selectedRow.get(8)); 
                    String balAmt = CommonUtil.convertObjToStr(selectedRow.get(9)); 
                    
                    totalChequeAmount = totalChequeAmount + amount;
                    term.put(CommonConstants.MODULE, getModule());
                    term.put(CommonConstants.SCREEN, getScreen());
                    term.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                    term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                    term.put("SAVE_BRANCH_CODE", ProxyParameters.BRANCH_ID);
                    term.put("SUB_REG_TYPE", getSubRegType());
                    checkAcNoWithoutProdType(getTxtAccountNo());
                    term.put("SELECTED_BRANCH_ID", getSelectedBranchID());
                    //get prod barnch id
                    String crBranchId = "";
                    HashMap interBranchCodeMap = new HashMap();
                    interBranchCodeMap.put("ACT_NUM", CommonUtil.convertObjToStr(getTxtAccountNo()));
                    List interBranchCodeList = ClientUtil.executeQuery("getSelectInterBranchCode", interBranchCodeMap);
                    if (interBranchCodeList != null && interBranchCodeList.size() > 0) {
                        interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                        System.out.println("interBranchCodeMap : " + interBranchCodeMap);
                        crBranchId = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
                    }
                    term.put("INTER_BRANCH_ID", crBranchId);
                    term.put("LodgementMasterTO", getLodgementMasterTO(command,productType,sbAcctNo));                    
//                    objMultipleLodgementMasterTO = getMultipleLodgementMasterTO(command, sbAcctNo, prodId, productType, amount,instr, instAmnt, serviceTax);
//                    term.put("MultipleLodgementMasterTO", objMultipleLodgementMasterTO);
                    term.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
                    System.out.println("############" + getCbmInstrumentType().getKeyForSelected());
                    if ((getCbmInstrumentType().getKeyForSelected().equals("CHEQUE")) || (getCbmInstrumentType().getKeyForSelected().equals("PAYORDER"))
                            || (getCbmInstrumentType().getKeyForSelected().equals("DEMAND DRAFT"))) {
                        term.put("LodgementChequeTO", getLodgementChequeTO(command,amount));
                    } else if (getCbmInstrumentType().getKeyForSelected().equals("HUNDI")) {
                        term.put("LodgementHundiTO", getLodgementHundiTO(command));
                    }
                    System.out.println("tblInstructionData ============== " + tblInstructionData);
                    //term.put("LodgementInstructionsTO", tblInstructionData);
                    System.out.println("multipleInstructionListMap :: " + multipleInstructionListMap);
                    if(multipleInstructionListMap != null && multipleInstructionListMap.size() > 0){
                       term.put("LodgementInstructionsTO", multipleInstructionListMap.get(sbAcctNo)); 
                    }                    
                    if (lblServiceTaxval != null && CommonUtil.convertObjToDouble(lblServiceTaxval) > 0 && serviceTax_Map != null) {
                        term.put("serviceTaxDetails", getServiceTax_Map().get(sbAcctNo));
                        term.put("serviceTaxDetailsTO", setServiceTaxDetails(command,sbAcctNo));
                    }
                    term.put("LodgementInstrTO", tblInstr);
                    if ((getCbmActivities().getKeyForSelected().equals("REALIZE"))
                            || (getCbmActivities().getKeyForSelected().equals("PROCEEDS_RECEIVED"))
                            || (getCbmActivities().getKeyForSelected().equals("DISHONOUR"))
                            || (getCbmActivities().getKeyForSelected().equals("CLOSURE"))) {
                        //term.put("LodgementRemitTO", getLodgementRemitTO(command,remitFavr,balAmt));
                        term.put("LodgementRemitTO", getLodgementRemitTO(command,remitFavr,balAmt));
                    }
                    if ((getOperatesLikeValue().equals("OUTWARD"))) {
//                 if((CommonUtil.convertObjToStr(term.get("SUB_REG_TYPE")).equals("CPD")) && (getOperatesLikeValue().equals("OUTWARD"))){
                        term.put("LodgementBillRatesTO", getLodgementBillRatesTO(command));
                    }
                    if (getTxtAccountHeadValue().length() > 0) {
                        term.put("OTHER_BANK_HEAD", getTxtAccountHeadValue());
                        term.put("OTHER_BANK_PROD_TYPE", getOtherBankProdType());
                    }
                    System.out.println("GGRTTT===" + getTransaction());
                    System.out.println("hhhhh===" + (getCbmActivities().getKeyForSelected().equals("REALIZE")));
                    String prodType = CommonUtil.convertObjToStr(getCbmProductType().getKeyForSelected());
                    System.out.println("prodTypeprodType===" + prodType);
                    if ((getCbmActivities().getKeyForSelected().equals("REALIZE")) && getTransaction() != null
                            && getTransaction().size() > 0 && prodType.equals("TL")) {
                        System.out.println("GGRTTT55555555555555555555===" + getTransaction());
                        term.put("LODGE_LOAN_DET", getTransaction());
                    }
                    term.put("LODGEMENT_ID",getLodgementId());
                    term.put("MULTIPLE_LODGEMENT","MULTIPLE_LODGEMENT");                    
                    
                    if( i == newList.size() - 1){
                       ArrayList multipleLodgementMasterList = getMultipleLodgementMasterTOList(command,tbmActNoArray);
                       term.put("FINAL_RECORD","FINAL_RECORD"); 
                       term.put("TOTAL_CHEQUE_AMT",totalChequeAmount);
                       term.put("MULTIPLE_LODGEMENT_MASTER",multipleLodgementMasterList);
                    }
                    System.out.println("TERM :: " + term);
                    proxyResultMap = proxy.execute(term, map);                    
                    
                    System.out.println("proxyResultMap :: " + proxyResultMap);
                }
            }
            
            setProxyReturnMap(proxyResultMap);
            setResult(getActionType());
            System.out.println("proxyResultMap   333===" + proxyResultMap);
            if (proxyResultMap != null && proxyResultMap.containsKey("ERRORLIST")) {
                String errMessage = "";
                String msg = CommonUtil.convertObjToStr(proxyResultMap.get("ERRORLIST"));
                if (msg.length() > 0) {
                    errMessage = errMessage + msg + "\n";
                }
                if (errMessage.length() > 0) {
                    ClientUtil.showMessageWindow(errMessage);
                    return;
                }
            }
            if (getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                if (proxyResultMap != null && proxyResultMap.containsKey("LODGEMENT_ID")) {
                    ClientUtil.showMessageWindow("Lodgement ID: " + CommonUtil.convertObjToStr(proxyResultMap.get("LODGEMENT_ID")));
                    System.out.println("");
                }
            }
        } catch (Exception e) {
//            setResult(ClientConstants.ACTIONTYPE_FAILED);
//            parseException.logException(e,true);
            System.out.println(exception + "exceptionmap###");
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            if (e instanceof TTException) {
                exception = (TTException) e;
                System.out.println(exception + "exceptionmap###" + exception);
            }
        }
        // If TT Exception
        if (exception != null) {
            HashMap exceptionHashMap = exception.getExceptionHashMap();
            System.out.println(exception + "exceptionmap###" + exceptionHashMap);
            if (exceptionHashMap != null) {
                ArrayList list = (ArrayList) exceptionHashMap.get(CommonConstants.EXCEPTION_LIST);
                //(list.get(0) instanceof String && "IB".equalsIgnoreCase((String)list.get(0))) ||
                if (list.size() == 1 && list.get(0) instanceof String && ((String) list.get(0)).startsWith("SUSPECIOUS") || CommonUtil.convertObjToStr(list.get(0)).equals("AED") || CommonUtil.convertObjToStr(list.get(0)).equals("AEL")) {
                    Object[] dialogOption = {"Exception", "Cancel"};
                    parseException.setDialogOptions(dialogOption);
                    if (parseException.logException(exception, true) == 0) {
                        try {
                            setResult(ClientConstants.ACTIONTYPE_FAILED);
//                            doActionPerform("EXCEPTION");
                        } catch (Exception e1) {
                            log.info("Error In doAction()");
                            e1.printStackTrace();
                            if (e1 instanceof TTException) {
                                Object[] dialogOption1 = {"OK"};
                                parseException.setDialogOptions(dialogOption1);
                                exception = (TTException) e1;
                                parseException.logException(exception, true);
                            }
                        }
                    }
                    Object[] dialogOption1 = {"OK"};
                    parseException.setDialogOptions(dialogOption1);
                } else {
                    parseException.logException(exception, true);
                }
            } else { // To Display Transaction No showing String message
                parseException.logException(exception, true);
                setResult(ClientConstants.ACTIONTYPE_FAILED);
            }
        }

    }
    
    private ArrayList getMultipleLodgementMasterTOList(String command, ArrayList tbmActNoArray){  
         ArrayList multipleLodgementMasterTOList = new ArrayList();
         ArrayList selectedRow;
         //ArrayList tbmActNoArray = tbmInstructions.getDataArrayList(); 
        if (tbmActNoArray != null && tbmActNoArray.size() > 0) {                
                for (int i = 0; i < tbmActNoArray.size(); i++) {
                    selectedRow = (ArrayList) (tbmActNoArray.get(i));
                    String prodId = CommonUtil.convertObjToStr(selectedRow.get(5));
                    String productType = CommonUtil.convertObjToStr(selectedRow.get(6));
                    String sbAcctNo = CommonUtil.convertObjToStr(selectedRow.get(4));
                    Double amount = CommonUtil.convertObjToDouble(selectedRow.get(7)); 
                    String instr = CommonUtil.convertObjToStr(selectedRow.get(1));
                    Double instAmnt = CommonUtil.convertObjToDouble(selectedRow.get(2)); 
                    Double serviceTax = CommonUtil.convertObjToDouble(selectedRow.get(3)); 
//                    String remitFavr = CommonUtil.convertObjToStr(selectedRow.get(8)); 
//                    String balAmt = CommonUtil.convertObjToStr(selectedRow.get(9)); 
                    objMultipleLodgementMasterTO = getMultipleLodgementMasterTO(command, sbAcctNo, prodId, productType, amount,instr, instAmnt, serviceTax);
                    multipleLodgementMasterTOList.add(objMultipleLodgementMasterTO);   
                }
        }  
        return multipleLodgementMasterTOList;
    }
    
  
}





