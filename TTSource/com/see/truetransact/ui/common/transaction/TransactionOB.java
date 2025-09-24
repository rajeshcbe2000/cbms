/*Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * TransactionOB.java
 *
 * Created on January 6, 2004, 12:10 PM
 */

package com.see.truetransact.ui.common.transaction;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientexception.ClientParseException;
//import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientproxy.ProxyFactory;
//import java.util.Observable;
import com.see.truetransact.uicomponent.CObservable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import org.apache.log4j.Logger;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.uivalidation.CurrencyValidation;

/**
 *
 * @author Sunil
 */

public class TransactionOB extends CObservable {
    private final static Logger log = Logger.getLogger(TransactionOB.class);
    private final int UPDATE = 2,DELETE = 3;
    private final String DELETED_ISSUE_TOs = "DELETED_ISSUE_TOs";
    //    private final String NOT_DELETED_ISSUE_TOs = "NOT_DELETED_ISSUE_TOs";
    private String batchId = "" ;
    private String batchDt = "" ;
    private String transId = "" ;
    private String status = "" ;
    private String txtApplicantsName = "";
    private String cboTransType = "";
    private String txtTransactionAmt = "";
    private String txtDebitAccNo = "";
    private String txtChequeNo = "";
    private String txtChequeNo2 = "";
    private String cboInstrumentType = "" ;
    private ComboBoxModel cbmInstrumentType ;
    private String tdtChequeDate = "";
    private String lblTotalTransactionAmtVal = "";
    private String lblCustomerNameVal = "";
    private String txtTransProductId = "" ;
    private String cboProductType = "";
    private String tokenNo = "";
    private ComboBoxModel cbmProductType ;
    
    private int selectedRowValue ;
    
    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;
    private ArrayList key;
    private ArrayList value;
    private LinkedHashMap transactiondetailMap=new LinkedHashMap();
    private LinkedHashMap deletedTransTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    private LinkedHashMap allowedTransactionDetailsTO = null;
//    private static LinkedHashMap dupTransactionDetailsTO = null;
    private ArrayList rowDataForTransDetails = null;
    private String LimitAmount;
    private String lasttxtAmount;
    private ProxyFactory proxy;
    private HashMap procChargeMap=new HashMap();
    private int result;
    private int actionType;
    private static int transactionSerialNo = 1;
    private static int noOfDeletedTransTOs = 1;
    private String LoneActNum;
    final TransactionRB objTransactionRB = new TransactionRB();
    final ArrayList transDetailsTitle = new ArrayList();
    private ComboBoxModel cbmTransactionType;
    private static TransactionOB transactionOB;
    private EnhancedTableModel tblTransDetails ;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private boolean depFlag;
    Date curDate = null;
    private String sourceScreen = "";
    private String particulars=""; //Added By Sathiya on 11-03-2014
    private String selectedTxnBranchId = "";
    private String selectedTxnType = "";

    public String getSelectedTxnType() {
        return selectedTxnType;
    }

    public void setSelectedTxnType(String selectedTxnType) {
        this.selectedTxnType = selectedTxnType;
    }
    public String getSelectedTxnBranchId() {
        return selectedTxnBranchId;
    }

    public void setSelectedTxnBranchId(String selectedTxnBranchId) {
        this.selectedTxnBranchId = selectedTxnBranchId;
    }
    /*static {
        try {
            transactionOB = new TransactionOB();
        } catch(Exception e) {
            e.printStackTrace();
            log.info("Error in transactionOB Declaration");
        }
    }
     **/
    
    /** Creates a new instance of TransactionOB */
    public TransactionOB(){
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            cbmProductType =  new ComboBoxModel();
            cbmInstrumentType =  new ComboBoxModel();
            fillDropdown();
            setTransDetailsTitle();
            tblTransDetails = new EnhancedTableModel(null, transDetailsTitle);
            makeComboBoxKeyValuesNull();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static TransactionOB getInstance() {
        return transactionOB;
    }
    
    public void setCboProductType(java.lang.String cboProductType) {
        this.cboProductType = cboProductType;
    }
    
    
    /**
     * Getter for property cbmProductType.
     * @return Value of property cbmProductType.
     */
    public ComboBoxModel getCbmProductType() {
        return cbmProductType;
    }
    
    /**
     * Setter for property cbmProductType.
     * @param cbmProductType New value of property cbmProductType.
     */
    public void setCbmProductType(ComboBoxModel cbmProductType) {
        this.cbmProductType = cbmProductType;
    }
    
    /**
     * Getter for property txtApplicantsName.
     * @return Value of property txtApplicantsName.
     */
    public java.lang.String getTxtApplicantsName() {
        return txtApplicantsName;
    }
    
    /**
     * Setter for property txtApplicantsName.
     * @param txtApplicantsName New value of property txtApplicantsName.
     */
    public void setTxtApplicantsName(java.lang.String txtApplicantsName) {
        this.txtApplicantsName = txtApplicantsName;
    }
    
    /**
     * Getter for property cboTransType.
     * @return Value of property cboTransType.
     */
    public java.lang.String getCboTransType() {
        return cboTransType;
    }
    
    /**
     * Setter for property cboTransType.
     * @param cboTransType New value of property cboTransType.
     */
    public void setCboTransType(java.lang.String cboTransType) {
        this.cboTransType = cboTransType;
    }
    
    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }
    
    public List getDocumentDetailses(String mapName, String mapWhere) throws Exception {
        List lst=null;
        HashMap hash = new HashMap();
        hash.put(CommonConstants.MAP_NAME,mapName);
        hash.put(CommonConstants.MAP_WHERE,mapWhere);
        setOperationMap();
        try{
            lst = (List)proxy.executeQuery(hash,operationMap).get("DOCUMENT_LIST");
            //System.out.println("### lst : "+lst);
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return lst;
    }
    
    /**
     * Getter for property txtTransactionAmt.
     * @return Value of property txtTransactionAmt.
     */
    public java.lang.String getTxtTransactionAmt() {
        return txtTransactionAmt;
    }
    
    /**
     * Setter for property txtTransactionAmt.
     * @param txtTransactionAmt New value of property txtTransactionAmt.
     */
    public void setTxtTransactionAmt(java.lang.String txtTransactionAmt) {
        this.txtTransactionAmt = txtTransactionAmt;
    }
    
    /**
     * Getter for property txtDebitAccNo.
     * @return Value of property txtDebitAccNo.
     */
    public java.lang.String getTxtDebitAccNo() {
        return txtDebitAccNo;
    }
    
    /**
     * Setter for property txtDebitAccNo.
     * @param txtDebitAccNo New value of property txtDebitAccNo.
     */
    public void setTxtDebitAccNo(java.lang.String txtDebitAccNo) {
        this.txtDebitAccNo = txtDebitAccNo;
    }
    
    /**
     * Getter for property txtChequeNo.
     * @return Value of property txtChequeNo.
     */
    public java.lang.String getTxtChequeNo() {
        return txtChequeNo;
    }
    
    /**
     * Setter for property txtChequeNo.
     * @param txtChequeNo New value of property txtChequeNo.
     */
    public void setTxtChequeNo(java.lang.String txtChequeNo) {
        this.txtChequeNo = txtChequeNo;
    }
    
    /**
     * Setter for property tdtChequeDate.
     * @param tdtChequeDate New value of property tdtChequeDate.
     */
    public void setTdtChequeDate(java.lang.String tdtChequeDate) {
        this.tdtChequeDate = tdtChequeDate;
    }
    
    /**
     * Getter for property txtTotalTransactionAmt.
     * @return Value of property txtTotalTransactionAmt.
     */
    public java.lang.String getLblTotalTransactionAmtVal() {
        return lblTotalTransactionAmtVal;
    }
    
    /**
     * Setter for property txtTotalTransactionAmt.
     * @param txtTotalTransactionAmt New value of property txtTotalTransactionAmt.
     */
    public void setLblTotalTransactionAmtVal(java.lang.String lblTotalTransactionAmtVal) {
        this.lblTotalTransactionAmtVal = lblTotalTransactionAmtVal;
        setChanged();
    }
    
    
    
    /**
     * Getter for property cboProductType.
     * @return Value of property cboProductType.
     */
    public java.lang.String getCboProductType() {
        return cboProductType;
    }
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        //        operationMap.put(CommonConstants.JNDI, "TransactionJNDI");
        //        operationMap.put(CommonConstants.HOME, "transaction.TransactionHome");
        //        operationMap.put(CommonConstants.REMOTE, "transaction.Transaction");
        operationMap.put(CommonConstants.JNDI, "RemittanceIssueJNDI");
        operationMap.put(CommonConstants.HOME, "remittance.RemittanceIssueHome");
        operationMap.put(CommonConstants.REMOTE, "remittance.RemittanceIssue");
    }
    
    /* Sets  Trans Details  with table column headers */
    private void setTransDetailsTitle() throws Exception{
        transDetailsTitle.add(objTransactionRB.getString("tblTransDetailsColumn2"));
        transDetailsTitle.add(objTransactionRB.getString("tblTransDetailsColumn3"));
        transDetailsTitle.add(objTransactionRB.getString("tblTransDetailsColumn4"));
    }
    
    public void resetObjects(){
        resetForm();
        tblTransDetails = new EnhancedTableModel(null, transDetailsTitle);
        allowedTransactionDetailsTO = new LinkedHashMap();
        transactionSerialNo = 1;
        rowDataForTransDetails = new ArrayList();
        setLblTotalTransactionAmtVal("");
        lblCustomerNameVal = "" ;
        
    }
    
//    protected void removeProductElements() {
//        List keys = cbmProductType.getKeys();
//        if(keys.size() > 0 && keys != null){
//        for (int i = 0; i < keys.size(); i++) {
//            if (keys.get(i).equals("AD")) {
//                cbmProductType.removeKeyAndElement("AD");
//            }
//            if (keys.get(i).equals("MDS")) {
//                cbmProductType.removeKeyAndElement("MDS");
//            }
//            if (keys.get(i).equals("MDS")) {
//                cbmProductType.removeKeyAndElement("MDS");
//            }
//            if (keys.get(i).equals("OA")) {
//                cbmProductType.removeKeyAndElement("OA");
//            }
//            if (keys.get(i).equals("SA")) {
//                cbmProductType.removeKeyAndElement("SA");
//            }
//            if (keys.get(i).equals("TD")) {
//                cbmProductType.removeKeyAndElement("TD");
//            }
//            if (keys.get(i).equals("TL")) {
//                cbmProductType.removeKeyAndElement("TL");
//            }
//        }
//        }
//    }
    
    public void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("PRODUCTTYPE");
        lookup_keys.add("INSTRUMENTTYPE");
        lookup_keys.add("REMITTANCE_ISSUE.TRANSACTION_TYPE");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("REMITTANCE_ISSUE.TRANSACTION_TYPE"));
        cbmTransactionType = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
        cbmProductType = new ComboBoxModel(key,value);
        if(cbmProductType.getKeys().contains("MDS")){
          cbmProductType.removeKeyAndElement("MDS");
        }
        //System.out.println("KEEEE"+keyValue);
        //System.out.println("getsourcescree"+getSourceScreen());
        
        
//        key.add("RM");
//        value.add("Remittance");
//        cbmProductType = new ComboBoxModel(key,value);

        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("INSTRUMENTTYPE"));
        cbmInstrumentType = new ComboBoxModel(key,value);
        makeComboBoxKeyValuesNull();
        
    }
    
    //Added By Suresh
    public void setProductType() throws Exception{ // Recovery Tally Screen
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("PRODUCTTYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
        cbmProductType = new ComboBoxModel(key,value);
        cbmProductType.removeKeyAndElement("TD");
        cbmProductType.removeKeyAndElement("TL");
        //cbmProductType.removeKeyAndElement("GL");
        cbmProductType.removeKeyAndElement("SA");
        keyValue = null;
    }
    //added by Suresh
   public void addInvestmentProduct() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("PRODUCTTYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
        key.add("INV");
        value.add("Investment");
        cbmProductType = new ComboBoxModel(key,value);        
    }
    
    //Added By Sreekrishnan
    public void removeTermLoanForDividend() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("PRODUCTTYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));        
        cbmProductType = new ComboBoxModel(key,value);
        cbmProductType.removeKeyAndElement("TD");
        cbmProductType.removeKeyAndElement("TL");
   }
    
    //Added By Suresh
    public void addBorrowingProduct() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("PRODUCTTYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
        key.add("BRW");
        value.add("Borrowing");
        cbmProductType = new ComboBoxModel(key,value);        
    }
    
    
    //Added By Suresh
    public void addOparativeProduct() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("PRODUCTTYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));        
        cbmProductType = new ComboBoxModel(key,value);
        cbmProductType.removeKeyAndElement("AD");
        cbmProductType.removeKeyAndElement("GL");
        cbmProductType.removeKeyAndElement("TD");
        cbmProductType.removeKeyAndElement("TL");
        cbmProductType.removeKeyAndElement("SA");       
    }
    
    //Added By Suresh
    public void addRTGSNEFTProduct() throws Exception { //17-Nov-2017
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("PRODUCTTYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get("PRODUCTTYPE"));
        cbmProductType = new ComboBoxModel(key, value);
        cbmProductType.removeKeyAndElement("MDS");
        cbmProductType.removeKeyAndElement("TD");
        cbmProductType.removeKeyAndElement("TL");
        //cbmProductType.removeKeyAndElement("SA");
        cbmProductType.removeKeyAndElement("AB");
    }
    
    public void deleteTransDetails(int row) {
        //log.info("Inside deleteTransDetails()");
        int size = allowedTransactionDetailsTO.size();
        try {
            TransactionTO objTransactionTO;
            if (size == 1 || row+1 == size) {
                objTransactionTO = (TransactionTO) allowedTransactionDetailsTO.remove( String.valueOf( (row+1) ) );
            } else {
                objTransactionTO = (TransactionTO) allowedTransactionDetailsTO.get( String.valueOf( (row+1) ) );
                /* Remove the selected TO Object from the HashMap(issueDetailsMap) */
                for(int i = row+1;i<size;i++) {
                    allowedTransactionDetailsTO.put(String.valueOf(i),(TransactionTO)allowedTransactionDetailsTO.remove(String.valueOf((i+1))));
                }
            }
            ////System.out.println("objTransactionTO" + objTransactionTO) ;
            if( ( CommonUtil.convertObjToStr(objTransactionTO.getStatus()).length()>0 )
            && (objTransactionTO.getStatus() != null )
            && !(CommonUtil.convertObjToStr(objTransactionTO.getStatus()).equals(""))) {
                if (deletedTransactionDetailsTO == null)
                    deletedTransactionDetailsTO = new LinkedHashMap();
                deletedTransactionDetailsTO.put(String.valueOf(noOfDeletedTransTOs++), objTransactionTO);
                setTransactiondetailMap(deletedTransactionDetailsTO);
            }
            objTransactionTO = null;
            
            rowDataForTransDetails.remove(row);
            /* Orders the serial no in the arraylist (tableData) after the removal
               of selected Row in the table */
            for(int i=0,j = rowDataForTransDetails.size();i<j;i++){
                ( (ArrayList) rowDataForTransDetails.get(i)).set(0,String.valueOf(i+1));
            }
            
            setLblTotalTransactionAmtVal(String.valueOf(calculateTotalAmount(rowDataForTransDetails)));
            tblTransDetails.setDataArrayList(rowDataForTransDetails,transDetailsTitle);
            transactionSerialNo--;
            
        } catch( Exception e ) {
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    
    public void whenCashIsSelectedClear() {
        setTxtChequeNo("");
        setTxtChequeNo2("");
        setTdtChequeDate("");
        setCboProductType("");
        setCboInstrumentType("");
    }
    
    public void resetTransactionDetails() {
        setTransId("");
        setTxtApplicantsName("");
        setCboTransType("");
        setCboInstrumentType("");
        setTxtTransactionAmt("");
        setTxtTransProductId("");
        setCboProductType("");
        setTxtDebitAccNo("");
        setTxtChequeNo("");
        setTxtChequeNo2("");
        setTdtChequeDate("");
        setBatchDt("");
        setTokenNo("");
        setStatus(null);
        setParticulars("");
        ttNotifyObservers();
    }
    
    private ArrayList setTableValuesForTransactionDetails(int serial_No_OR_selectedRow,TransactionTO objTransactionTO) {
        ArrayList singleRow = new ArrayList();
        singleRow.add(CommonUtil.convertObjToStr(objTransactionTO.getTransId()));
        singleRow.add((String) getCbmTransactionType().getDataForKey(CommonUtil.convertObjToStr(objTransactionTO.getTransType())));
        singleRow.add(CommonUtil.convertObjToStr(objTransactionTO.getTransAmt()));
        return singleRow;
        
    }
    
    // Save the Transaction Details when the button in Transaction is pressed
    public void saveTransactionDetails(boolean tableTransactionMousePressed,int selectedRow, boolean isRemitDup, int rowCnt) {
        //log.info("Inside saveTransactionDetails()");
        try {
            TransactionTO objTransactionTO = setTransactionTO();
            if (allowedTransactionDetailsTO == null)
                allowedTransactionDetailsTO = new LinkedHashMap();
            if (rowDataForTransDetails == null)
                rowDataForTransDetails =  new ArrayList();
            if (tableTransactionMousePressed) {
                rowDataForTransDetails.set(selectedRow, setTableValuesForTransactionDetails(selectedRow + 1, objTransactionTO));
                allowedTransactionDetailsTO.put(String.valueOf(selectedRow + 1), objTransactionTO);
                setTransactionOB(objTransactionTO); 
            } else {
                if(isDepFlag() == true)
                    transactionSerialNo = 1;
                if(isRemitDup){
                    transactionSerialNo = rowCnt+1;
                    isRemitDup = false;
                }
                rowDataForTransDetails.add(setTableValuesForTransactionDetails(transactionSerialNo, objTransactionTO));
//                dupTransactionDetailsTO = allowedTransactionDetailsTO;
//                dupMap.put("DupMAp", dupTransactionDetailsTO);
                allowedTransactionDetailsTO.put(String.valueOf(transactionSerialNo),objTransactionTO);
                setTransactiondetailMap(allowedTransactionDetailsTO);
//                 if(procChargeMap.size()>0)
//            allowedTransactionDetailsTO.put("PROCCHARGEMAP",procChargeMap);
                transactionSerialNo++;
            }
            
            objTransactionTO = null;
            setLblTotalTransactionAmtVal(String.valueOf(calculateTotalAmount(rowDataForTransDetails)));
            //////System.out.println("transDetailsTitle : " + transDetailsTitle);
            //////System.out.println("rowDataForTransDetails : " + rowDataForTransDetails);
            tblTransDetails.setDataArrayList(rowDataForTransDetails,transDetailsTitle);
        }catch( Exception e ) {
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    
    
    
    
    private double calculateTotalAmount(ArrayList tableData) {
        final int column = 2;
        //String returnTotalAmt = "";
        double crAmt = 0, dbAmt = 0, returnTotalAmt=0;
        int crInst = 0, dbInst = 0;
        try {
            double totalAmount = 0.0;
            ArrayList rowData = new ArrayList();
            for (int i=0,j=tableData.size();i<j;i++) {
                rowData = (ArrayList) tableData.get(i);
                totalAmount += CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(rowData.get(column)));                
                long dr = roundOff((long) (totalAmount * 1000));                
                totalAmount = dr / 100.0;                            
                rowData = null;
            }           
            returnTotalAmt = new Double(totalAmount).doubleValue();
            //returnTotalAmt =  CommonUtil.convertObjToStr(new Double(String.valueOf(totalAmount)));
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        
        return returnTotalAmt;
    }
    
    private long roundOff(long amt) {
        long amount = amt / 10;
//        int lastDigit = (int)amt%10;
        int lastDigit = (int) (amt % 10);  //() brackets added because sometimes returns 8 if 0 also.
        if (lastDigit > 5) {
            amount++;
        }
        return amount;
    }
    
    private void makeComboBoxKeyValuesNull(){
        key = null;
        value = null;
        lookUpHash = null;
        keyValue = null;
    }
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /**
     *
     * @return RemittanceIssueTransactionTO
     * ie Sets the Issue Details Transfer Object
     */
    public TransactionTO setTransactionTO() {
        //log.info("setTransactionTO()");
        ////System.out.println("(String)cbmInstrumentType.getKeyForSelected() : " + (String)cbmInstrumentType.getKeyForSelected());
        TransactionTO objTransactionTO = new TransactionTO();
        try{
            objTransactionTO.setBatchId(CommonUtil.convertObjToStr(getBatchId()));
            objTransactionTO.setApplName(CommonUtil.convertObjToStr(getTxtApplicantsName()));
            
            Date BchDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getBatchDt()));
            if(BchDt != null){
            Date bchDate = (Date)curDate.clone();
            bchDate.setDate(BchDt.getDate());
            bchDate.setMonth(BchDt.getMonth());
            bchDate.setYear(BchDt.getYear());
            objTransactionTO.setBatchDt(bchDate);
            }else{
                objTransactionTO.setBatchDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getBatchDt())));
            }
//            objTransactionTO.setBatchDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getBatchDt())));
            objTransactionTO.setChequeNo(CommonUtil.convertObjToStr(getTxtChequeNo()));
            objTransactionTO.setChequeNo2(CommonUtil.convertObjToStr(getTxtChequeNo2()));
            objTransactionTO.setBranchId(getSelectedBranchID());
            
            Date ChDt = DateUtil.getDateMMDDYYYY(getTdtChequeDate());
            if(ChDt != null){
            Date ChDate = (Date)curDate.clone();
            ChDate.setDate(ChDt.getDate());
            ChDate.setMonth(ChDt.getMonth());
            ChDate.setYear(ChDt.getYear());
            objTransactionTO.setChequeDt(ChDate);
            }else{
                objTransactionTO.setChequeDt(DateUtil.getDateMMDDYYYY(getTdtChequeDate()));
            }
//            objTransactionTO.setChequeDt(DateUtil.getDateMMDDYYYY(getTdtChequeDate()));
            //Added by sreekrishnan
            if(CommonUtil.convertObjToStr(cbmProductType.getKeyForSelected())!=null &&
                    !CommonUtil.convertObjToStr((String)cbmProductType.getKeyForSelected()).equals("") &&
                   CommonUtil.convertObjToStr((String)cbmProductType.getKeyForSelected()).equalsIgnoreCase("SH")){
                objTransactionTO.setProductType("GL");
            }else{
                objTransactionTO.setProductType(CommonUtil.convertObjToStr((String)cbmProductType.getKeyForSelected()));
            }
            if(CommonUtil.convertObjToStr(cbmProductType.getKeyForSelected())!=null &&
                    !CommonUtil.convertObjToStr((String)cbmProductType.getKeyForSelected()).equals("") &&
                   CommonUtil.convertObjToStr((String)cbmProductType.getKeyForSelected()).equalsIgnoreCase("SH")){
               objTransactionTO.setProductId("");
            }else{
                objTransactionTO.setProductId(CommonUtil.convertObjToStr(getTxtTransProductId()));
            }
            objTransactionTO.setInstType(getCboInstrumentType());            
            objTransactionTO.setDebitAcctNo(CommonUtil.convertObjToStr(getTxtDebitAccNo()));
            objTransactionTO.setTransAmt(CommonUtil.convertObjToDouble(getTxtTransactionAmt()));
            objTransactionTO.setTransId(CommonUtil.convertObjToStr(getTransId()));
            objTransactionTO.setTransType(CommonUtil.convertObjToStr((String)cbmTransactionType.getKeyForSelected()));
            objTransactionTO.setCommand(CommonUtil.convertObjToStr(getCommand()));
            objTransactionTO.setTokenNo(CommonUtil.convertObjToStr(getTokenNo()));
            objTransactionTO.setStatus(getStatus());
            objTransactionTO.setParticulars(getParticulars());
             ////System.out.println("objTransactionTO : " + objTransactionTO);
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e,true);
        }
        //System.out.println("objTransactionTO^$^$^$^$$"+objTransactionTO);
        return objTransactionTO;
    }
    
    
    
    public void doAction() {
        try {
            //log.info("Inside doAction()");
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null ){
                    doActionPerform();
                }
                else{
                    final TransactionRB objTransactionRB = new TransactionRB();
                    throw new TTException(objTransactionRB.getString("TOCommandError"));
                }
            }
            resetForm();
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
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
                setTxtDebitAccNo(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                setSelectedBranchID(CommonUtil.convertObjToStr(mapData.get("BRANCH_ID")));
                cbmProductType.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                setCboProductType(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                setTxtTransProductId(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
//                setCbmProdId(getProdType());
//                getProducts();
//                cbmProdId.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                isExists = true;
            } else {
//                ArrayList key=new ArrayList();
//                ArrayList value=new ArrayList();
//                key.add("");
//                value.add("");   
//                setCbmProdId("");
//                isExists = false;
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
    
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    public int getActionType(){
        return this.actionType;
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        int count = 0;
        //log.info("Inside doActionPerform()");
        final HashMap data = new HashMap();
        data.put("MODE", getCommand());
        if (allowedTransactionDetailsTO == null)
            allowedTransactionDetailsTO = new LinkedHashMap();
        
        if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            if (deletedTransTO != null) {
                allowedTransactionDetailsTO.put(DELETED_ISSUE_TOs,deletedTransTO);
                deletedTransTO = null;
            }
        }
//        if(procChargeMap.size()>0)
//            data.put("PROCCHARGEMAP",procChargeMap);
        data.put("TransactionTO",allowedTransactionDetailsTO);
        
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setResult(actionType);
        actionType = ClientConstants.ACTIONTYPE_CANCEL;
    }
    
    /* To get the type of command */
    private String getCommand() throws Exception{
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
    
    /* Reset the whole form remittance*/
    public void resetForm() {
        
        // reset Transaction Details
        setTransId("");
        setTxtApplicantsName("");
        setCboTransType("");
        setCboInstrumentType("");
        setTxtTransactionAmt("");
        setTxtTransProductId("");
        setCboProductType("");
        getCbmProductType().setKeyForSelected("");
        setTxtDebitAccNo("");
        setTxtChequeNo("");
        setTxtChequeNo2("");
        setTdtChequeDate("");
        setTokenNo("");
        setStatus(null);
        setBatchDt("");
//        resetTransactionDetails();
        setLblCustomerNameVal("", null);        
        if(cboProductType.contains("Share")){
            cbmProductType.removeKeyAndElement("SH");
        }
        cbmProductType.setKeyForSelected("");
        setParticulars("");
        setChanged();
        ttNotifyObservers();
        
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    private void createObjects() {
        if (allowedTransactionDetailsTO == null)
            allowedTransactionDetailsTO = new LinkedHashMap();
        
    }
    void checkOA(String operative_Accno){
        double procPercentage=0;
        double clearBalance=0;
        LinkedHashMap linkedMap=getTransactiondetailMap();
        //        HashMap firstRecord=(HashMap)linkedMap.get(String.valueOf(1));
        if (linkedMap.size()>0) {
            TransactionTO  transactionTO=(TransactionTO )linkedMap.get(String.valueOf(1));
            String prod_id=transactionTO.getProductId();
            if(transactionTO.getProductType().equals("TL")) {
                String actNum=transactionTO.getDebitAcctNo();

                HashMap actHash=new HashMap();
                actHash.put("ACT_NUM",actNum);

                List lst=(List)ClientUtil.executeQuery("getProcPercentageTL", actHash);

                if(lst.size()>0){
                    procChargeMap=(HashMap)lst.get(0);
                    procPercentage=CommonUtil.convertObjToDouble(procChargeMap.get("PROC_CHRG_PER")).doubleValue();
                    prod_id=CommonUtil.convertObjToStr(procChargeMap.get("PROD_ID"));
                    procChargeMap.put("TL_PROD_ID",prod_id);
                    procChargeMap.put("LINK_BATCH_ID",actNum);
                    procChargeMap.put("OA_ACT_NUM",operative_Accno);
                }
                String amount=getLimitAmount();
                amount=amount.replaceAll(",", "");
                double LimitAmt=CommonUtil.convertObjToDouble(amount);

                double procCharge=LimitAmt*(procPercentage/100);
                 //System.out.println("obLimitamount"+LimitAmt+"PROCPERCENTAGE"+procPercentage+"FINALCHARGE"+procCharge);
                HashMap oaHash=new HashMap();
                oaHash.put("ACT_NUM",operative_Accno);
                List ls=ClientUtil.executeQuery("getOAbalenceForCharges", oaHash);
                if(ls.size()>0){
                    oaHash=(HashMap)ls.get(0);
                    clearBalance=CommonUtil.convertObjToDouble(oaHash.get("CLEAR_BALANCE")).doubleValue();
                    procChargeMap.put("OA_PROD_ID",oaHash.get("PROD_ID"));
                    procChargeMap.put("PROC_CHRG",new Double(procCharge));
                    //System.out.println("checkoa#######"+procChargeMap);
                }
                String lastTxtAmt=getLasttxtAmount();
    //            lastTxtAmt=lastTxtAmt.replaceAll(",", "");
                double lastTextAmount=CommonUtil.convertObjToDouble(lastTxtAmt);
                //System.out.println("####lasttxtamt"+lastTxtAmt+"doublelasttextamt"+lastTxtAmt);
                double balance=clearBalance-lastTextAmount;
                if(balance>=procCharge)
                {}
                else{
                    ClientUtil.confirmationAlert("Operative Account Not Having Sufficient Balance Collect Manually");
                    procChargeMap=null;
                }
                //transactionTO.get
            }
        }
    }
    private int recordSize(List record) {
        return record.size();
    }
    
    public void setDetails(List data) throws Exception {
        //System.out.println(data+"^%%%%%%$Calling here...." + getActionType());
        if(data!=null){
            //System.out.println("in else of tob.........."+data.size());
            allowedTransactionDetailsTO = new LinkedHashMap();
            
            rowDataForTransDetails= new ArrayList();
            for (int i=0,transactionSerialNo = 1, j= data.size();i<j;i++) {
                TransactionTO objTransactionTO = (TransactionTO) data.get(i);
                if(objTransactionTO.getProductType()!=null)
                if(objTransactionTO.getProductType().equals("TL")) // ||objTransactionTO.getProductType().equals("OD"))
                   setLoneActNum(objTransactionTO.getDebitAcctNo());
                if (getActionType()==UPDATE){
                    objTransactionTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
                } else if (getActionType()==DELETE){
                    objTransactionTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                }
                //System.out.println("transactionSerialNo : " + transactionSerialNo);
                Date BchDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTransactionTO.getBatchDt()));
                if(BchDt != null){
                    Date bchDate = (Date)curDate.clone();
                    bchDate.setDate(BchDt.getDate());
                    bchDate.setMonth(BchDt.getMonth());
                    bchDate.setYear(BchDt.getYear());
                    objTransactionTO.setBatchDt(bchDate);
                }
                BchDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTransactionTO.getChequeDt()));
                if(BchDt != null){
                    Date bchDate = (Date)curDate.clone();
                    bchDate.setDate(BchDt.getDate());
                    bchDate.setMonth(BchDt.getMonth());
                    bchDate.setYear(BchDt.getYear());
                    objTransactionTO.setChequeDt(bchDate);
                }
                allowedTransactionDetailsTO.put(String.valueOf(transactionSerialNo),objTransactionTO);
                rowDataForTransDetails.add(setTableValuesForTransactionDetails(transactionSerialNo, objTransactionTO));
                ////System.out.println("objTransactionTO in setDetails of TransOB : " + objTransactionTO.toString());
                transactionSerialNo++;
                
                objTransactionTO = null;
            }
            setLblTotalTransactionAmtVal(String.valueOf(calculateTotalAmount(rowDataForTransDetails)));
            tblTransDetails.setDataArrayList(rowDataForTransDetails,transDetailsTitle);
            ttNotifyObservers();
        }
    }
    
    // To retrive the data from the database and populate it in the OB
    private boolean populateOB(HashMap mapData) throws Exception{
        boolean flag = false;
        //log.info("In PopulateOB method:");
        List list = (List) mapData.get("TransactionTO");
        //System.out.println("transaction OB"+list);
        createObjects();
        if (!list.isEmpty())
            setDetails(list);
        list = null;
        return flag;
    }
    
    /**
     * Executes the query
     */
    private List executeQuery(String mapName, HashMap where){
        List returnList = null;
        try{
            returnList = (List) ClientUtil.executeQuery(mapName, where);
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return returnList;
    }
    
    /**
     * Set the Transactions Details in OB
     */
    private void setTransactionOB(TransactionTO objTransactionTO) throws Exception {
        ////System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>objTransactionTO : " + objTransactionTO);
        //log.info("Inside setTransactionOB()");
        setBatchId(CommonUtil.convertObjToStr(objTransactionTO.getBatchId()));
        setTxtApplicantsName(CommonUtil.convertObjToStr(objTransactionTO.getApplName()));
        setBatchDt(DateUtil.getStringDate(objTransactionTO.getBatchDt()));
        setTdtChequeDate(DateUtil.getStringDate(objTransactionTO.getChequeDt()));
        setTransId(CommonUtil.convertObjToStr(objTransactionTO.getTransId()));
        setTxtChequeNo(CommonUtil.convertObjToStr(objTransactionTO.getChequeNo()));
        setTxtChequeNo2(CommonUtil.convertObjToStr(objTransactionTO.getChequeNo2()));
        setCboProductType((String) getCbmProductType().getDataForKey(CommonUtil.convertObjToStr(objTransactionTO.getProductType())));
        getCbmProductType().setKeyForSelected(CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
        setTxtTransProductId(CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
        setTxtDebitAccNo(CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
        setCboTransType((String) getCbmTransactionType().getDataForKey(CommonUtil.convertObjToStr(objTransactionTO.getTransType())));
        setCboInstrumentType(objTransactionTO.getInstType());
        setTxtTransactionAmt(CommonUtil.convertObjToStr(objTransactionTO.getTransAmt()));
        setTokenNo(CommonUtil.convertObjToStr(objTransactionTO.getTokenNo()));
        setStatus(CommonUtil.convertObjToStr(objTransactionTO.getStatus()));
        setLblTotalTransactionAmtVal(CommonUtil.convertObjToStr(getLblTotalTransactionAmtVal()));
        if(objTransactionTO.getProductType() != null && (objTransactionTO.getProductType().equals("RM")|| objTransactionTO.getProductType().equals("GL"))){
            objTransactionTO.setProductId("");
            setTxtTransProductId(CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
        }
            
        setLblCustomerNameVal(null, objTransactionTO.getProductType());
        setParticulars(CommonUtil.convertObjToStr(objTransactionTO.getParticulars()));        
        ////System.out.println("****getCboInstrumentType : " + getCboInstrumentType());
    }
    
    /**
     * Getter for property batchId.
     * @return Value of property batchId.
     */
    public java.lang.String getBatchId() {
        return batchId;
    }
    
    /**
     * Setter for property batchId.
     * @param batchId New value of property batchId.
     */
    public void setBatchId(java.lang.String batchId) {
        this.batchId = batchId;
    }
    
    /**
     * Getter for property batchDt.
     * @return Value of property batchDt.
     */
    public java.lang.String getBatchDt() {
        return batchDt;
    }
    
    /**
     * Setter for property batchDt.
     * @param batchDt New value of property batchDt.
     */
    public void setBatchDt(java.lang.String batchDt) {
        this.batchDt = batchDt;
    }
    
    
    /**
     * Getter for property transId.
     * @return Value of property transId.
     */
    public java.lang.String getTransId() {
        return transId;
    }
    
    /**
     * Setter for property transId.
     * @param transId New value of property transId.
     */
    public void setTransId(java.lang.String transId) {
        this.transId = transId;
    }
    
    /**
     * Getter for property status.
     * @return Value of property status.
     */
    public java.lang.String getStatus() {
        return status;
    }
    
    /**
     * Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }
    
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    
    /**
     * Getter for property cbmTransactionType.
     * @return Value of property cbmTransactionType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmTransactionType() {
        return cbmTransactionType;
    }
    
    /**
     * Setter for property cbmTransactionType.
     * @param cbmTransactionType New value of property cbmTransactionType.
     */
    public void setCbmTransactionType(com.see.truetransact.clientutil.ComboBoxModel cbmTransactionType) {
        this.cbmTransactionType = cbmTransactionType;
    }
    
    /**
     * Getter for property result.
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }
    
    /**
     * Getter for property txtTransProductId.
     * @return Value of property txtTransProductId.
     */
    public java.lang.String getTxtTransProductId() {
        return txtTransProductId;
    }
    
    /**
     * Setter for property txtTransProductId.
     * @param txtTransProductId New value of property txtTransProductId.
     */
    public void setTxtTransProductId(java.lang.String txtTransProductId) {
        this.txtTransProductId = txtTransProductId;
    }
    
    /**
     * Getter for property tblTransDetails.
     * @return Value of property tblTransDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblTransDetails() {
        return tblTransDetails;
    }
    
    /**
     * Setter for property tblTransDetails.
     * @param tblTransDetails New value of property tblTransDetails.
     */
    public void setTblTransDetails(com.see.truetransact.clientutil.EnhancedTableModel tblTransDetails) {
        this.tblTransDetails = tblTransDetails;
    }
    
    /**
     * Getter for property selectedRowValue.
     * @return Value of property selectedRowValue.
     */
    public int getSelectedRowValue() {
        return selectedRowValue;
    }
    
    /**
     * Setter for property selectedRowValue.
     * @param selectedRowValue New value of property selectedRowValue.
     */
    public void setSelectedRowValue(int selectedRowValue) {
        this.selectedRowValue = selectedRowValue;
    }
    
    public void populateSelectedTransDetails(String row) {
        try {
            //System.out.println("++++++++++++++++==== row : " + row);
            //System.out.println("******************** allowedTransactionDetailsTO : " + allowedTransactionDetailsTO);
            TransactionTO objTransactionTO = (TransactionTO)allowedTransactionDetailsTO.get(row);
            //System.out.println(">>>>>>>>>>>>>objTransactionTO in TOB popSelTransDetails : " + objTransactionTO);
            setTransactionOB(objTransactionTO);
        }catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    
    /**
     * Getter for property tdtChequeDate.
     * @return Value of property tdtChequeDate.
     */
    public java.lang.String getTdtChequeDate() {
        return tdtChequeDate;
    }
    
    /**
     * Getter for property allowedTransactionDetailsTO.
     * @return Value of property allowedTransactionDetailsTO.
     */
    public java.util.LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }
    
//     /**
//     * Getter for property dupTransactionDetailsTO.
//     * @return Value of property dupTransactionDetailsTO.
//     */
//    public java.util.LinkedHashMap getDupTransactionDetailsTO() {
//        return dupTransactionDetailsTO;
//    }
    /**
     * Getter for property txtChequeNo2.
     * @return Value of property txtChequeNo2.
     */
    public java.lang.String getTxtChequeNo2() {
        return txtChequeNo2;
    }
    
    /**
     * Setter for property txtChequeNo2.
     * @param txtChequeNo2 New value of property txtChequeNo2.
     */
    public void setTxtChequeNo2(java.lang.String txtChequeNo2) {
        this.txtChequeNo2 = txtChequeNo2;
    }
    
    /**
     * Getter for property cboInstrumentType.
     * @return Value of property cboInstrumentType.
     */
    public java.lang.String getCboInstrumentType() {
        return cboInstrumentType;
    }
    
    /**
     * Setter for property cboInstrumentType.
     * @param cboInstrumentType New value of property cboInstrumentType.
     */
    public void setCboInstrumentType(java.lang.String cboInstrumentType) {
        this.cboInstrumentType = cboInstrumentType;
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
    }
    
    public String toString(){
        StringBuffer strb = new StringBuffer();
        strb.append(batchId).append("\n");
        strb.append(batchDt).append("\n");
        strb.append(transId).append("\n");
        strb.append(status).append("\n");
        strb.append(txtApplicantsName).append("\n");
        strb.append(cboTransType).append("\n");
        strb.append(txtTransactionAmt).append("\n");
        strb.append(txtDebitAccNo).append("\n");
        strb.append(txtChequeNo).append("\n");
        strb.append(txtChequeNo2).append("\n");
        strb.append(cboInstrumentType).append("\n");
        strb.append(lblCustomerNameVal).append("\n");
        strb.append(tdtChequeDate).append("\n");
        strb.append(lblTotalTransactionAmtVal).append("\n");
        strb.append(txtTransProductId).append("\n");
        strb.append(cboProductType).append("\n");
        return strb.toString();
    }
    
    /**
     * Getter for property lblCustomerNameVal.
     * @return Value of property lblCustomerNameVal.
     */
    public java.lang.String getLblCustomerNameVal() {
        return lblCustomerNameVal;
    }
    
    /**
     * Setter for property lblCustomerNameVal.
     * @param lblCustomerNameVal New value of property lblCustomerNameVal.
     */
    public void setLblCustomerNameVal(String custName, String prodType) {
        if(custName == null && CommonUtil.convertObjToStr(prodType).length() > 0 ){
            if(!prodType.equals("RM") && !prodType.equals("INV") && !prodType.equals("BRW")){
                final HashMap accountNameMap = new HashMap();
                accountNameMap.put("ACC_NUM",getTxtDebitAccNo());
                //System.out.println("accountNameMap : " + accountNameMap);
                final List resultList = ClientUtil.executeQuery("getAccountNumberName"+prodType, accountNameMap);
                //System.out.println("resultList.size() : " + resultList.size() + "  /  resultList : "+resultList);
                if (resultList.size()>=1) {
                    final HashMap resultMap = (HashMap)resultList.get(0);
                    //System.out.println("resultMap : " + resultMap);
                    this.lblCustomerNameVal = CommonUtil.convertObjToStr(resultMap.get("CUSTOMER_NAME").toString());
                }
            }
                //  else
                //    ClientUtil.noDataAlert();
        }
        else{
            this.lblCustomerNameVal = custName ;
        }
        setChanged();
    }
    
    /**
     * Getter for property transactiondetailMap.
     * @return Value of property transactiondetailMap.
     */
    public java.util.LinkedHashMap getTransactiondetailMap() {
        return transactiondetailMap;
    }
    
    /**
     * Setter for property transactiondetailMap.
     * @param transactiondetailMap New value of property transactiondetailMap.
     */
    public void setTransactiondetailMap(java.util.LinkedHashMap transactiondetailMap) {
        this.transactiondetailMap = transactiondetailMap;
    }
    
    /**
     * Getter for property LimitAmount.
     * @return Value of property LimitAmount.
     */
    public java.lang.String getLimitAmount() {
        return LimitAmount;
    }
    
    /**
     * Setter for property LimitAmount.
     * @param LimitAmount New value of property LimitAmount.
     */
    public void setLimitAmount(java.lang.String LimitAmount) {
        this.LimitAmount = LimitAmount;
    }
    
    /**
     * Getter for property procChargeMap.
     * @return Value of property procChargeMap.
     */
    public java.util.HashMap getProcChargeMap() {
        return procChargeMap;
    }
    
    /**
     * Setter for property procChargeMap.
     * @param procChargeMap New value of property procChargeMap.
     */
    public void setProcChargeMap(java.util.HashMap procChargeMap) {
        this.procChargeMap = procChargeMap;
    }
    
    /**
     * Getter for property lasttxtAmount.
     * @return Value of property lasttxtAmount.
     */
    public java.lang.String getLasttxtAmount() {
        return lasttxtAmount;
    }
    
    /**
     * Setter for property lasttxtAmount.
     * @param lasttxtAmount New value of property lasttxtAmount.
     */
    public void setLasttxtAmount(java.lang.String lasttxtAmount) {
        this.lasttxtAmount = lasttxtAmount;
    }
    
    /**
     * Getter for property LoneActNum.
     * @return Value of property LoneActNum.
     */
    public java.lang.String getLoneActNum() {
        return LoneActNum;
    }
    
    /**
     * Setter for property LoneActNum.
     * @param LoneActNum New value of property LoneActNum.
     */
    public void setLoneActNum(java.lang.String LoneActNum) {
        this.LoneActNum = LoneActNum;
    }
    
    /**
     * Getter for property depFlag.
     * @return Value of property depFlag.
     */
    public boolean isDepFlag() {
        return depFlag;
    }
    
    /**
     * Setter for property depFlag.
     * @param depFlag New value of property depFlag.
     */
    public void setDepFlag(boolean depFlag) {
        this.depFlag = depFlag;
    }
    
    /**
     * Getter for property tokenNo.
     * @return Value of property tokenNo.
     */
    public java.lang.String getTokenNo() {
        return tokenNo;
    }
    
    /**
     * Setter for property tokenNo.
     * @param tokenNo New value of property tokenNo.
     */
    public void setTokenNo(java.lang.String tokenNo) {
        this.tokenNo = tokenNo;
    }
    
    /**
     * Getter for property sourceScreen.
     * @return Value of property sourceScreen.
     */
    public java.lang.String getSourceScreen() {
        return sourceScreen;
    }
    
    /**
     * Setter for property sourceScreen.
     * @param sourceScreen New value of property sourceScreen.
     */
    public void setSourceScreen(java.lang.String sourceScreen) {
        this.sourceScreen = sourceScreen;
    }
    
}


