/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LockOB.java
 *
 * Created on February 25, 2004, 2:48 PM
 */

package com.see.truetransact.ui.salaryrecovery;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.EnhancedTableModel;
//import com.see.truetransact.ui.TrueTransactMain;
//import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.transferobject.salaryrecovery.SalaryDeductionMappingTO;
import com.see.truetransact.transferobject.transaction.reconciliation.ReconciliationTO;
//import java.math.BigDecimal;
//import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

//import com.see.truetransact.serverside.tds.tdscalc;
/**
 *
 * @author  rahul
 */
public class LockOB extends CObservable{
    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;
    private ProxyFactory proxy;
    HashMap map =null;
    private int actionType;
    private int result;
    private HashMap _authorizeMap;
    private ArrayList key;
    private ArrayList value;
    private HashMap procChargeHash = new HashMap();
    private ComboBoxModel cbmProdType;
    private ComboBoxModel cbmProdId;
    private ComboBoxModel cbmInputCurrency;
    private ComboBoxModel cbmInstrumentType;
    private ArrayList lockTableTitle;
    //tdscalc
    private HashMap tdsCalc =new HashMap();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(SalaryDeductionMappingUI.class);
    final String DEBIT = "DEBIT";
    final String CREDIT = "CREDIT";
    final String INITIATORTYPE = "CASHIER";
    private String lblAccHdDesc;
    private String txtAccHdId;
    private String lblAccName;
    private String lblTransactionId;
    private String lblTransDate;
    private String lblInitiatorId;
    private String depTransId = null;
    private double interestAmt = 0.0;
    private String depInterestAmt = "";
    private static CInternalFrame frame;
    private  double LimitAmt;
    private double oldAmount = 0.0;
    private String  LimitAmount;
    private String prodType;
    private String cr_cash;
    private String dr_cash;
    private ArrayList denominationList;
    private boolean isExist;
    private int result1=1;
    private boolean odExpired;
    private boolean flag=false;
    private String depLinkBatchId;
    private HashMap oaMap=new HashMap();
    private HashMap linkMap=new HashMap();
    private String asAnWhenCustomer =new String();
    private  double procAmount;
    private HashMap ALL_LOAN_AMOUNT;
    private List linkBathList=null;
    private static LockOB lockOB;
    private Date curDate = null;
    public boolean depositRenewalFlag = false;
    public String depositPenalAmt = null;
    public String depositPenalMonth = null;
    private String creatingFlexi = "";
    private double flexiAmount = 0.0;
    private String reconcile = "";
    private String balanceType = "";
    public HashMap reconcileMap;
    ReconciliationTO reconciliationTO = new ReconciliationTO();
    private String  AuthorizeBy="";
    private String authorizeStatus="";
    private String lblHouseName = "";
    private String txtEmployerRefNo="";
    private String chkDeleteFlag="";
    private Map corpLoanMap = null; // For Corporate Loan purpose added by Rajesh
    private String salaryRecovery="";
    private String lockStatus="";
    static {
        try {
            
            lockOB = new LockOB();
        } catch(Exception e) {
            log.info("Error in SalaryDeductionMappingOB Declaration");
        }
    }
    
    public static LockOB getInstance(CInternalFrame frm) {
        frame = frm;
        return lockOB;
    }
    
    /** Creates a new instance of CashTransactionOB */
    public LockOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        initianSetup();
        setOperationMap();
        setLockTitle();
        lockTable = new EnhancedTableModel(null, lockTableTitle);
    }
    
    public Date getCurrentDate() {
        return (Date)curDate.clone();
    }
    private void initianSetup() throws Exception{
        log.info("In initianSetup()");
        
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            log.info(" Error In initianSetup()");
            e.printStackTrace();
        }
        fillDropdown();// To Fill all the Combo Boxes
    }
    
    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception{
        log.info("In setOperationMap()");
        
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "SalaryDeductionMappingJNDI");
        operationMap.put(CommonConstants.HOME, "salaryrecovery.SalaryDeductionMappingHome");
        operationMap.put(CommonConstants.REMOTE, "salaryrecovery.SalaryDeductionMapping");
    }
    public void setTableReset(){
        lockTable.setDataArrayList(null,lockTableTitle);
    }
    private void setLockTitle() throws Exception{
        try {
            lockTableTitle=new ArrayList();
            lockTableTitle.add("ProductType");
            lockTableTitle.add("ProductId");
            lockTableTitle.add("AccountNo");
            lockTableTitle.add("Name");
            
        }catch(Exception e) {
            log.info("Exception in lockTableTitle: "+e);
            parseException.logException(e,true);
        }
    }
    public void insertTableData(ArrayList list){
        try{
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            lockTable= new EnhancedTableModel((ArrayList)list, lockTableTitle);
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    
    // To Fill the Combo boxes in the UI
    public void fillDropdown() throws Exception{
        log.info("In fillDropdown()");
        
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("FOREX.CURRENCY");
        lookup_keys.add("INSTRUMENTTYPE");
        lookup_keys.add("PRODUCTTYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("FOREX.CURRENCY"));
        cbmInputCurrency = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("INSTRUMENTTYPE"));
        int idx=key.indexOf("ONLINE_TRANSFER");
        key.remove(idx);
        value.remove(idx);
        
        cbmInstrumentType = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
        cbmProdType = new ComboBoxModel(key,value);
        cbmProdType.addKeyAndElement("MDS", "MDS");
        cbmProdType.removeKeyAndElement("OA");
        cbmProdType.removeKeyAndElement("GL");
        cbmProdType.removeKeyAndElement("SA");
        
    }
    
    // Get the value from the Hash Map depending on the Value of Key...
    private void getKeyValue(HashMap keyValue)  throws Exception{
        log.info("In getKeyValue()");
        
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    // To enter the Data into the Database...Called from doActionPerform()...
    public SalaryDeductionMappingTO setSalaryDeductionMapping() {
        
        
        final SalaryDeductionMappingTO objSalaryDeductionMappingTO = new SalaryDeductionMappingTO();
        try{
            if (!CommonUtil.convertObjToStr(getCboProdId()).equals(""))
                objSalaryDeductionMappingTO.setProdId(CommonUtil.convertObjToStr(getCbmProdId().getKeyForSelected()));
            objSalaryDeductionMappingTO.setProdType(CommonUtil.convertObjToStr(getCbmProdType().getKeyForSelected()));
            objSalaryDeductionMappingTO.setEmployerRefNo(getEmployerRefNo());
            objSalaryDeductionMappingTO.setRemarks(getTxtRemarks());
            objSalaryDeductionMappingTO.setActNum(getTxtAccNo());
            objSalaryDeductionMappingTO.setAmount(CommonUtil.convertObjToDouble(getTxtAmount()));
            objSalaryDeductionMappingTO.setBranchId(getSelectedBranchID());
            objSalaryDeductionMappingTO.setStatusBy(ProxyParameters.USER_ID);
            objSalaryDeductionMappingTO.setStatus(getCommand());
            objSalaryDeductionMappingTO.setStatusDt(curDate);
            objSalaryDeductionMappingTO.setDeleteFlag( getDeleteFlag());
            objSalaryDeductionMappingTO.setCreatedBy(ProxyParameters.USER_ID);
            objSalaryDeductionMappingTO.setCreatedDt(curDate);
            
        }catch(Exception e){
            
            e.printStackTrace();
        }
        return objSalaryDeductionMappingTO;
    }
    
    
    
    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) {
        try{
            log.info("In populateData()");
            populateOB(whereMap);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    private void populateOB(HashMap mapData) throws Exception{
        setEmployerRefNo(CommonUtil.convertObjToStr(mapData.get("EMP_REF_NO")));
        setTxtAccNo(CommonUtil.convertObjToStr(mapData.get("MAP_ACT_NUM")));
        setTxtAmount(CommonUtil.convertObjToStr(mapData.get("AMOUNT")));
        getCbmProdType().setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("MAP_PROD_TYPE")));
        getCbmProdId().setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("MAP_PROD_ID")));
        setTxtRemarks(CommonUtil.convertObjToStr(mapData.get("REMARKS")));
        setDeleteFlag(CommonUtil.convertObjToStr(mapData.get("DELETE_FLAG")));
        ttNotifyObservers();
    }
    public HashMap asAnWhenCustomerComesYesNO(String acct_no){
        HashMap map=new HashMap();
        map.put("ACT_NUM",acct_no);
        map.put("TRANS_DT", curDate.clone());
        map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
        List lst=null;
        if(!CommonUtil.convertObjToStr(getCbmProdType().getKeyForSelected()).equals("AD"))
            lst=ClientUtil.executeQuery("IntCalculationDetail", map);
        else
            lst=ClientUtil.executeQuery("IntCalculationDetailAD", map);
        
        if(lst !=null && lst.size()>0){
            map=(HashMap)lst.get(0);
            setLinkMap(map);
        }
        return map;
    }
    // To Enter the values in the UI fields, from the database...
    private void setSalaryDeductionMappingTO(SalaryDeductionMappingTO objSalaryDeductionMappingTO) throws Exception{
        
        // Set as a Lable
        setStatusBy(objSalaryDeductionMappingTO.getStatusBy());
        getCbmProdType().setKeyForSelected(CommonUtil.convertObjToStr(objSalaryDeductionMappingTO.getProdType()));
        setCboProdType((String) getCbmProdType().getDataForKey(CommonUtil.convertObjToStr(objSalaryDeductionMappingTO.getProdType())));
        if (!CommonUtil.convertObjToStr(objSalaryDeductionMappingTO.getProdId()).equals("")) {
            setCbmProdId(objSalaryDeductionMappingTO.getProdType());
            getCbmProdId().setKeyForSelected(CommonUtil.convertObjToStr(objSalaryDeductionMappingTO.getProdId()));
            setCboProdId((String) getCbmProdId().getDataForKey(CommonUtil.convertObjToStr(objSalaryDeductionMappingTO.getProdId())));
        }
        setTxtAccNo(CommonUtil.convertObjToStr(objSalaryDeductionMappingTO.getActNum()));
        oldAmount = objSalaryDeductionMappingTO.getAmount().doubleValue();
        setTxtAmount(CommonUtil.convertObjToStr(new Double(oldAmount)));
        setTxtRemarks(CommonUtil.convertObjToStr(objSalaryDeductionMappingTO.getRemarks()));
        setEmployerRefNo(CommonUtil.convertObjToStr(objSalaryDeductionMappingTO.getEmployerRefNo()));
        this.setProdType(objSalaryDeductionMappingTO.getProdType());
        setAuthorizeBy(CommonUtil.convertObjToStr(objSalaryDeductionMappingTO.getAuthorizeBy()));
        setAuthorizeStatus(CommonUtil.convertObjToStr(objSalaryDeductionMappingTO.getAuthorizeStatus()));
        setDeleteFlag(CommonUtil.convertObjToStr(objSalaryDeductionMappingTO. getDeleteFlag()));
    }
    
    
    // To perform Appropriate operation... Insert, Update, Delete...
    public void doAction() {
        TTException exception = null;
        
        log.info("In doAction()");
        
        try {
            
            
            if( getCommand() != null || getAuthorizeMap() != null){
                doActionPerform("");
            }
            
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            if(e instanceof TTException) {
                exception = (TTException) e;
            }
        }
        
        // If TT Exception
        
    }
    
    private HashMap interestAmtgiving(){
        String actNum = getTxtAccNo();
        HashMap interestMap = new HashMap();
        interestMap.put("ACT_NUM",getTxtAccNo());
        List lst = ClientUtil.executeQuery("getIntForDeptIntTable", interestMap);
        if(lst.size()>0 && lst != null){
            interestMap = (HashMap)lst.get(0);
            String paidInt = CommonUtil.convertObjToStr(interestMap.get("PAID_INT"));
            Date applDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(interestMap.get("APPL_DT")));
        }
        return interestMap;
    }
    
    /** To perform the necessary action */
    private void doActionPerform(String parameter) throws Exception{
        
        HashMap proxyResultMap = new HashMap();
        final SalaryDeductionMappingTO objSalaryDeductionMappingTO = setSalaryDeductionMapping();
        objSalaryDeductionMappingTO.setCommand(getCommand());
        HashMap data=new HashMap();
        data.put("SalaryDeductionMappingTO",objSalaryDeductionMappingTO);
        System.out.println("data#####"+data);
        proxyResultMap = proxy.execute(data, operationMap);
        //                custId = CommonUtil.convertObjToStr(proxyResultMap.get("CUST_ID"));
        setProxyReturnMap(proxyResultMap);
        
        //                storePhotoSign();
        //                resetForm();
        setResult(actionType);
        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        System.out.println("result is"+getResult());
        
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
                setSelectedBranchID(CommonUtil.convertObjToStr(mapData.get("BRANCH_ID")));
                cbmProdType.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                setProdType(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                setCbmProdId(getProdType());
                setSalaryRecovery(CommonUtil.convertObjToStr(mapData.get("SALARY_RECOVERY")));
                setLockStatus(CommonUtil.convertObjToStr(mapData.get("LOCK_STATUS")));
                //                getProducts();
                cbmProdId.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
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
    
    public void getProducts(){
        List list=null;
        ArrayList key=new ArrayList();
        ArrayList value=new ArrayList();
        key.add("");
        value.add("");
        
        HashMap data;
        list=ClientUtil.executeQuery("Transfer.getCreditProduct"+getProdType(),null);
        if(list!=null && list.size()>0){
            int size=list.size();
            for(int i=0;i<size;i++){
                data=(HashMap)list.get(i);
                key.add(data.get("PRODID"));
                value.add(data.get("PRODDESC"));
            }
        }
        data = null;
        cbmProdType = new ComboBoxModel(key,value);
        setChanged();
    }
    
    public void getLinkBatchId(){
        
        String accNum=getTxtAccNo();
        oaMap=new HashMap();
        if(accNum.lastIndexOf("_")!=-1)
            accNum=accNum.substring(0,accNum.lastIndexOf("_"));
        HashMap hash=new HashMap();
        hash.put("ACT_NUM",accNum);
        List lst=ClientUtil.executeQuery("getLinkBatchValues", hash);
        System.out.println(hash+"####lst"+lst);
        if(lst.size()>0)
            oaMap=(HashMap)lst.get(0);
    }
    
    // Setter for Authorization.
    public void setAuthorizeMap(HashMap authorizeMap) {
        _authorizeMap = authorizeMap;
    }
    
    // Getter for Authorization.
    public HashMap getAuthorizeMap() {
        return _authorizeMap;
    }
    
    // to decide which action Should be performed...
    private String getCommand() throws Exception{
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
    
    // Returns the Current Value of Action type...
    public int getActionType(){
        return actionType;
    }
    
    // Sets the value of the Action Type to the poperation we want to execute...
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    // To reset all the fields in the UI
    public void resetForm(){
        log.info("In resetForm()");
        oldAmount = 0.0;
        _authorizeMap = null;
        setEmployerRefNo("");
        setCboProdId("");
        setCboProdType("");
        setTxtAccNo("");
        setDeleteFlag("");
        setTxtAmount("");
        setTxtRemarks("");
        setProdType("");
        setCbmProdId("");
        getCbmProdType().setKeyForSelected("");
        getCbmProdId().setKeyForSelected("");
        setSelectedBranchID(ProxyParameters.BRANCH_ID);
        ttNotifyObservers();
    }
    
    //To reset all the Lables in the UI...
    public void resetLable(){
        this.setTxtAccHd("");
        this.setLblAccHdDesc("");
        this.setLblAccName("");
        this.setLblTransactionId("");
        this.setLblTransDate("");
        this.setLblInitiatorId("");
    }
    
    // To set and change the Status of the lable STATUS
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    public int getResult(){
        return this.result;
    }
    
    // To set the Value of the lblStatus...
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    public String getLblStatus(){
        return lblStatus;
    }
    
    //To reset the Value of lblStatus after each save action...
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    
    private String cboProdId = "";
    private String cboProdType = "";
    private String txtAccNo = "";
    private String txtInitiatorChannel = "";
    private boolean rdoTransactionType_Debit = false;
    private boolean rdoTransactionType_Credit = false;
    private String txtInputAmt = "";
    private String txtAmount = "";
    private String cboInputCurrency = "";
    private String cboInstrumentType = "";
    private String txtInstrumentNo1 = "";
    private String txtInstrumentNo2 = "";
    private String tdtInstrumentDate = "";
    private String txtTokenNo = "";
    private String txtRemarks="";
    private String txtParticulars = "";
    private String txtPanNo="";
    private String txtNarration = "";
    private EnhancedTableModel lockTable ;
    
    void setInitiatorChannelValue(){
        setTxtInitiatorChannel(INITIATORTYPE);
        ttNotifyObservers();
    }
    
    
    void setCboProdId(String cboProdId){
        this.cboProdId = cboProdId;
        setChanged();
    }
    String getCboProdId(){
        return this.cboProdId;
    }
    
    void setEmployerRefNo(String txtEmployerRefNo){
        this.txtEmployerRefNo= txtEmployerRefNo;
        
    }
    String getEmployerRefNo(){
        return this.txtEmployerRefNo;
    }
    
    void setDeleteFlag(String chkDeleteFlag){
        this.chkDeleteFlag= chkDeleteFlag;
        
    }
    String getDeleteFlag(){
        return this.chkDeleteFlag;
    }
    public void setCbmProdId(String prodType) {
        if (CommonUtil.convertObjToStr(prodType).length()>1) {
            if (prodType.equals("GL")) {
                key = new ArrayList();
                value = new ArrayList();
            } else {
                try {
                    lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME,"Lock.getAccProduct" + prodType);
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
        cbmProdId = new ComboBoxModel(key,value);
        this.cbmProdId = cbmProdId;
        setChanged();
    }
    
    
    
    ComboBoxModel getCbmProdId(){
        return cbmProdId;
    }
    
    void setCboProdType(String cboProdType){
        this.cboProdType = cboProdType;
        setChanged();
    }
    String getCboProdType(){
        return this.cboProdType;
    }
    
    public void setCbmProdType(ComboBoxModel cbmProdType){
        this.cbmProdType = cbmProdType;
        setChanged();
    }
    
    ComboBoxModel getCbmProdType(){
        return cbmProdType;
    }
    
    void setTxtAccNo(String txtAccNo){
        this.txtAccNo = txtAccNo;
        setChanged();
    }
    String getTxtAccNo(){
        return this.txtAccNo;
    }
    
    void setTxtInitiatorChannel(String txtInitiatorChannel){
        this.txtInitiatorChannel = txtInitiatorChannel;
        setChanged();
    }
    String getTxtInitiatorChannel(){
        return this.txtInitiatorChannel;
    }
    
    void setRdoTransactionType_Debit(boolean rdoTransactionType_Debit){
        this.rdoTransactionType_Debit = rdoTransactionType_Debit;
        setChanged();
    }
    boolean getRdoTransactionType_Debit(){
        return this.rdoTransactionType_Debit;
    }
    
    void setRdoTransactionType_Credit(boolean rdoTransactionType_Credit){
        this.rdoTransactionType_Credit = rdoTransactionType_Credit;
        setChanged();
    }
    boolean getRdoTransactionType_Credit(){
        return this.rdoTransactionType_Credit;
    }
    
    void setTxtInputAmt(String txtInputAmt){
        this.txtInputAmt = txtInputAmt;
        setChanged();
    }
    String getTxtInputAmt(){
        return this.txtInputAmt;
    }
    void setTxtAmount(String txtAmount){
        this.txtAmount = txtAmount;
        setChanged();
    }
    String getTxtAmount(){
        return this.txtAmount;
    }
    
    void setTxtRemarks(String txtRemarks){
        this.txtRemarks = txtRemarks;
        setChanged();
    }
    String getTxtRemarks(){
        return this.txtRemarks;
    }
    void setCboInputCurrency(String cboInputCurrency){
        this.cboInputCurrency = cboInputCurrency;
        setChanged();
    }
    String getCboInputCurrency(){
        return this.cboInputCurrency;
    }
    
    public void setCbmInputCurrency(ComboBoxModel cbmInputCurrency){
        this.cbmInputCurrency = cbmInputCurrency;
        setChanged();
    }
    
    ComboBoxModel getCbmInputCurrency(){
        return cbmInputCurrency;
    }
    
    void setCboInstrumentType(String cboInstrumentType){
        this.cboInstrumentType = cboInstrumentType;
        setChanged();
    }
    String getCboInstrumentType(){
        return this.cboInstrumentType;
    }
    
    public void setCbmInstrumentType(ComboBoxModel cbmInstrumentType){
        this.cbmInstrumentType = cbmInstrumentType;
        setChanged();
    }
    
    ComboBoxModel getCbmInstrumentType(){
        return cbmInstrumentType;
    }
    
    void setTxtInstrumentNo1(String txtInstrumentNo1){
        this.txtInstrumentNo1 = txtInstrumentNo1;
        setChanged();
    }
    String getTxtInstrumentNo1(){
        return this.txtInstrumentNo1;
    }
    
    void setTxtInstrumentNo2(String txtInstrumentNo2){
        this.txtInstrumentNo2 = txtInstrumentNo2;
        setChanged();
    }
    String getTxtInstrumentNo2(){
        return this.txtInstrumentNo2;
    }
    
    void setTdtInstrumentDate(String tdtInstrumentDate){
        this.tdtInstrumentDate = tdtInstrumentDate;
        setChanged();
    }
    String getTdtInstrumentDate(){
        return this.tdtInstrumentDate;
    }
    
    void setTxtTokenNo(String txtTokenNo){
        this.txtTokenNo = txtTokenNo;
        setChanged();
    }
    String getTxtTokenNo(){
        return this.txtTokenNo;
    }
    
    
    
    void setTxtParticulars(String txtParticulars){
        this.txtParticulars = txtParticulars;
        setChanged();
    }
    String getTxtParticulars(){
        return this.txtParticulars;
    }
    
    //To Set the Lables In the UI...
    //==========================================
    public void setTxtAccHd(String txtAccHdId){
        this.txtAccHdId = txtAccHdId;
        setChanged();
    }
    public String getTxtAccHd(){
        return this.txtAccHdId;
    }
    
    public void setLblAccHdDesc(String lblAccHdDesc){
        this.lblAccHdDesc = lblAccHdDesc;
        setChanged();
    }
    public String getLblAccHdDesc(){
        return this.lblAccHdDesc;
    }
    
    // To apply TDS
    private void calculateTDs() {
        if(prodType.equals("TD") && getTxtAccNo()!=null) {
            HashMap tdsCalc =new HashMap();
            String deposit_No = CommonUtil.convertObjToStr(tdsCalc.get("DEPOSIT_NO"));
            if (deposit_No.lastIndexOf("_")!=-1)
                deposit_No = deposit_No.substring(0,deposit_No.lastIndexOf("_"));
            System.out.println("depositNo "+deposit_No);
            tdsCalc.put("DEPOSIT_NO",deposit_No);
            List list = ClientUtil.executeQuery("getCustNoforTDS",tdsCalc);
            tdsCalc =new HashMap();
            if(list.size()>0)
                System.out.println("depositNoforcalculateTDs : "+deposit_No);
            tdsCalc = (HashMap)list.get(0);
            //                tdsCalc.put("CUST_ID", tdsCalc.get("CUST_ID"));
            tdsCalc.put("PROD_ID",getCboProdId());
            tdsCalc.put("PROD_TYPE",getProdType());
            System.out.println("TDSCALC CUSTOMER "+tdsCalc);
            //                TdsCalc tdsCalculator = new TdsCalc(_branchCode);
            //                HashMap tdsMap = tdsCalculator.calculateTds((String)((HashMap)list.get(0)).get("CUST_ID"), null, null, ClientUtil.getCurrentDate(_branchCode), tdsCalc);
        }
        
    }
    
    public void setAccountHead() {
        try {
            System.out.println("here for edit"+(String)cbmProdId.getKeyForSelected());
            final HashMap accountHeadMap = new HashMap();
            accountHeadMap.put("PROD_ID",(String)cbmProdId.getKeyForSelected());
            if(!this.getProdType().equals("") && !this.getProdType().equals("GL") && !accountHeadMap.get("PROD_ID").equals("")){
                final List resultList = ClientUtil.executeQuery("getAccountHeadProd"+this.getProdType(), accountHeadMap);
                final HashMap resultMap = (HashMap)resultList.get(0);
                setTxtAccHd(resultMap.get("AC_HEAD").toString());
                setLblAccHdDesc(resultMap.get("AC_HEAD_DESC").toString());
                this.setCr_cash(((String)resultMap.get("CR_CASH")==null)?" ":((String)resultMap.get("CR_CASH")));
                this.setDr_cash(((String)resultMap.get("DR_CASH")==null)?"":((String)resultMap.get("DR_CASH")));
            }
        }catch(Exception e){
        }
    }
    // For setting the Name of the Account Number Holder...
    public void setLblAccName(String lblAccName){
        this.lblAccName = lblAccName;
        setChanged();
    }
    public String getLblAccName(){
        return this.lblAccName;
    }
    public void setAccountName(String AccountNo,String prodType){
        HashMap resultMap = new HashMap();
        final HashMap accountNameMap = new HashMap();
        List resultList = new ArrayList();
        try {
            if (!prodType.equals("")) {
                accountNameMap.put("ACC_NUM",AccountNo);
                String pID = !prodType.equals("GL") ? getCbmProdId().getKeyForSelected().toString() : "";
                if(prodType.equals("GL") && getTxtAccNo().length()>0){
                    resultList = ClientUtil.executeQuery("getAccountNumberNameTL",accountNameMap);
                    
                } else {
                    if(prodType.equals("TD")){
                        if(AccountNo.indexOf("_")==-1){
                            AccountNo= AccountNo+"_1";
                            accountNameMap.put("ACC_NUM",AccountNo);
                        }
                    }
                }
                resultList = ClientUtil.executeQuery("getAccountNumberName"+this.getProdType(),accountNameMap);
                
                if(resultList != null && resultList.size() > 0){
                    if(!prodType.equals("GL")) {
                        HashMap dataMap = new HashMap();
                        accountNameMap.put("BRANCH_ID", getSelectedBranchID());
                        List lst = (List) ClientUtil.executeQuery("getProdIdForActNo"+prodType,accountNameMap);
                        if(lst != null && lst.size() > 0)
                            dataMap = (HashMap) lst.get(0);
                        if(dataMap.get("PROD_ID").equals(pID)){
                            resultMap = (HashMap)resultList.get(0);
                        }
                    } else {
                        resultMap = (HashMap)resultList.get(0);
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        if(resultMap.containsKey("CUSTOMER_NAME")){
            System.out.println("%^$%^$%^$%^inside here"+resultMap);
            setLblAccName(CommonUtil.convertObjToStr(resultMap.get("CUSTOMER_NAME")));
        }
        
        //            if(resultList != null){
        //                final HashMap resultMap = (HashMap)resultList.get(0);
        //                setLblAccName(resultMap.get("CUSTOMER_NAME").toString());
        //            } else {
        //                setLblAccName("");
        //            }
        //        }catch(Exception e){
        //
        //        }
    }
    public void setAccountName(String AccountNo){
        HashMap resultMap = new HashMap();
        final HashMap accountNameMap = new HashMap();
        List resultList = new ArrayList();
        try {
            if (!prodType.equals("")) {
                accountNameMap.put("ACC_NUM",AccountNo);
                String pID = !prodType.equals("GL") ? getCbmProdId().getKeyForSelected().toString() : "";
                if(prodType.equals("GL") && getTxtAccNo().length()>0){
                    resultList = ClientUtil.executeQuery("getAccountNumberNameTL",accountNameMap);
                } else {
                    resultList = ClientUtil.executeQuery("getAccountNumberName"+this.getProdType(),accountNameMap);
                    List custHouseNameList = ClientUtil.executeQuery("getCustomerHouseName",accountNameMap);
                    if(custHouseNameList!=null && custHouseNameList.size()>0){
                        HashMap dataMap = new HashMap();
                        dataMap = (HashMap) custHouseNameList.get(0);
                        setLblHouseName(CommonUtil.convertObjToStr(dataMap.get("HOUSE_NAME")));
                    }
                }
                if(resultList != null && resultList.size() > 0){
                    if(!prodType.equals("GL")) {
                        HashMap dataMap = new HashMap();
                        accountNameMap.put("BRANCH_ID", getSelectedBranchID());
                        List lst = (List) ClientUtil.executeQuery("getProdIdForActNo"+prodType,accountNameMap);
                        if(lst != null && lst.size() > 0)
                            dataMap = (HashMap) lst.get(0);
                        if(dataMap.get("PROD_ID").equals(pID)){
                            resultMap = (HashMap)resultList.get(0);
                        }
                    } else {
                        resultMap = (HashMap)resultList.get(0);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        if(resultMap.containsKey("CUSTOMER_NAME"))
            setLblAccName(resultMap.get("CUSTOMER_NAME").toString());
        else
            setLblAccName("");
        //            if(resultList != null){
        //                final HashMap resultMap = (HashMap)resultList.get(0);
        //                setLblAccName(resultMap.get("CUSTOMER_NAME").toString());
        //            } else {
        //                setLblAccName("");
        //            }
        //        }catch(Exception e){
        //
        //        }
    }
    
    // For setting the transaction Id in UI at the Time of Edit or Delete...
    public void setLblTransactionId(String lblTransactionId){
        this.lblTransactionId = lblTransactionId;
        setChanged();
    }
    public String getLblTransactionId(){
        return this.lblTransactionId;
    }
    
    // For setting the Name of the Clearing Date in Ui at the time of Edit and Delete...
    public void setLblTransDate(String lblTransDate){
        this.lblTransDate = lblTransDate;
        setChanged();
    }
    public String getLblTransDate(){
        return this.lblTransDate;
    }
    
    // For setting the Initiator Id in UI at the Time of Edit or Delete...
    public void setLblInitiatorId(String lblInitiatorId){
        this.lblInitiatorId = lblInitiatorId;
        setChanged();
    }
    public String getLblInitiatorId(){
        return this.lblInitiatorId;
    }
    
    /**
     * Getter for property prodType.
     * @return Value of property prodType.
     */
    public java.lang.String getProdType() {
        return prodType;
    }
    
    /**
     * Setter for property prodType.
     * @param prodType New value of property prodType.
     */
    public void setProdType(java.lang.String prodType) {
        this.prodType = prodType;
    }
    
    
    public java.lang.String getSalaryRecovery() {
        return salaryRecovery;
    }
    
    /**
     * Setter for property prodType.
     * @param prodType New value of property prodType.
     */
    public void setSalaryRecovery(java.lang.String salaryRecovery) {
        this.salaryRecovery = salaryRecovery;
    }
    public java.lang.String getLockStatus() {
        return lockStatus;
    }
    
    /**
     * Setter for property prodType.
     * @param prodType New value of property prodType.
     */
    public void setLockStatus(java.lang.String lockStatus) {
        this.lockStatus = lockStatus;
    }
    /**
     * Getter for property cr_cash.
     * @return Value of property cr_cash.
     */
    public java.lang.String getCr_cash() {
        return cr_cash;
    }
    
    /**
     * Setter for property cr_cash.
     * @param cr_cash New value of property cr_cash.
     */
    public void setCr_cash(java.lang.String cr_cash) {
        this.cr_cash = cr_cash;
    }
    
    /**
     * Getter for property dr_cash.
     * @return Value of property dr_cash.
     */
    public java.lang.String getDr_cash() {
        return dr_cash;
    }
    
    /**
     * Setter for property dr_cash.
     * @param dr_cash New value of property dr_cash.
     */
    public void setDr_cash(java.lang.String dr_cash) {
        this.dr_cash = dr_cash;
    }
    
    /**
     * Getter for property denominationList.
     * @return Value of property denominationList.
     */
    public java.util.ArrayList getDenominationList() {
        return denominationList;
    }
    
    /**
     * Setter for property denominationList.
     * @param denominationList New value of property denominationList.
     */
    public void setDenominationList(java.util.ArrayList denominationList) {
        this.denominationList = denominationList;
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
     * Getter for property flag.
     * @return Value of property flag.
     */
    public boolean isFlag() {
        return flag;
    }
    
    /**
     * Setter for property flag.
     * @param flag New value of property flag.
     */
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    
    /**
     * Getter for property ALL_LOAN_AMOUNT.
     * @return Value of property ALL_LOAN_AMOUNT.
     */
    public java.util.HashMap getALL_LOAN_AMOUNT() {
        return ALL_LOAN_AMOUNT;
    }
    
    /**
     * Setter for property ALL_LOAN_AMOUNT.
     * @param ALL_LOAN_AMOUNT New value of property ALL_LOAN_AMOUNT.
     */
    public void setALL_LOAN_AMOUNT(java.util.HashMap ALL_LOAN_AMOUNT) {
        this.ALL_LOAN_AMOUNT = ALL_LOAN_AMOUNT;
    }
    
    /**
     * Getter for property depTransId.
     * @return Value of property depTransId.
     */
    public java.lang.String getDepTransId() {
        return depTransId;
    }
    
    /**
     * Setter for property depTransId.
     * @param depTransId New value of property depTransId.
     */
    public void setDepTransId(java.lang.String depTransId) {
        this.depTransId = depTransId;
    }
    
    /**
     * Getter for property interestAmt.
     * @return Value of property interestAmt.
     */
    public double getInterestAmt() {
        return interestAmt;
    }
    
    /**
     * Setter for property interestAmt.
     * @param interestAmt New value of property interestAmt.
     */
    public void setInterestAmt(double interestAmt) {
        this.interestAmt = interestAmt;
    }
    
    /**
     * Getter for property odExpired.
     * @return Value of property odExpired.
     */
    public boolean isOdExpired() {
        return odExpired;
    }
    
    /**
     * Setter for property odExpired.
     * @param odExpired New value of property odExpired.
     */
    public void setOdExpired(boolean odExpired) {
        this.odExpired = odExpired;
    }
    
    /**
     * Getter for property depInterestAmt.
     * @return Value of property depInterestAmt.
     */
    public java.lang.String getDepInterestAmt() {
        return depInterestAmt;
    }
    
    /**
     * Setter for property depInterestAmt.
     * @param depInterestAmt New value of property depInterestAmt.
     */
    public void setDepInterestAmt(java.lang.String depInterestAmt) {
        this.depInterestAmt = depInterestAmt;
    }
    
    /**
     * Getter for property depLinkBatchId.
     * @return Value of property depLinkBatchId.
     */
    public java.lang.String getDepLinkBatchId() {
        return depLinkBatchId;
    }
    
    /**
     * Setter for property depLinkBatchId.
     * @param depLinkBatchId New value of property depLinkBatchId.
     */
    public void setDepLinkBatchId(java.lang.String depLinkBatchId) {
        this.depLinkBatchId = depLinkBatchId;
    }
    
    /**
     * Getter for property asAnWhenCustomer.
     * @return Value of property asAnWhenCustomer.
     */
    public java.lang.String getAsAnWhenCustomer() {
        return asAnWhenCustomer;
    }
    
    /**
     * Setter for property asAnWhenCustomer.
     * @param asAnWhenCustomer New value of property asAnWhenCustomer.
     */
    public void setAsAnWhenCustomer(java.lang.String asAnWhenCustomer) {
        this.asAnWhenCustomer = asAnWhenCustomer;
    }
    
    /**
     * Getter for property linkMap.
     * @return Value of property linkMap.
     */
    public java.util.HashMap getLinkMap() {
        return linkMap;
    }
    
    /**
     * Setter for property linkMap.
     * @param linkMap New value of property linkMap.
     */
    public void setLinkMap(java.util.HashMap linkMap) {
        this.linkMap = linkMap;
    }
    
    /**
     * Getter for property linkBathList.
     * @return Value of property linkBathList.
     */
    public java.util.List getLinkBathList() {
        return linkBathList;
    }
    
    /**
     * Setter for property linkBathList.
     * @param linkBathList New value of property linkBathList.
     */
    public void setLinkBathList(java.util.List linkBathList) {
        this.linkBathList = linkBathList;
    }
    
    /**
     * Getter for property depositPenalAmt.
     * @return Value of property depositPenalAmt.
     */
    public java.lang.String getDepositPenalAmt() {
        return depositPenalAmt;
    }
    
    /**
     * Setter for property depositPenalAmt.
     * @param depositPenalAmt New value of property depositPenalAmt.
     */
    public void setDepositPenalAmt(java.lang.String depositPenalAmt) {
        this.depositPenalAmt = depositPenalAmt;
    }
    
    /**
     * Getter for property depositPenalMonth.
     * @return Value of property depositPenalMonth.
     */
    public java.lang.String getDepositPenalMonth() {
        return depositPenalMonth;
    }
    
    /**
     * Setter for property depositPenalMonth.
     * @param depositPenalMonth New value of property depositPenalMonth.
     */
    public void setDepositPenalMonth(java.lang.String depositPenalMonth) {
        this.depositPenalMonth = depositPenalMonth;
    }
    
    /**
     * Getter for property txtPanNo.
     * @return Value of property txtPanNo.
     */
    public java.lang.String getTxtPanNo() {
        return txtPanNo;
    }
    
    /**
     * Setter for property txtPanNo.
     * @param txtPanNo New value of property txtPanNo.
     */
    public void setTxtPanNo(java.lang.String txtPanNo) {
        this.txtPanNo = txtPanNo;
    }
    
    /**
     * Getter for property creatingFlexi.
     * @return Value of property creatingFlexi.
     */
    public java.lang.String getCreatingFlexi() {
        return creatingFlexi;
    }
    
    /**
     * Setter for property creatingFlexi.
     * @param creatingFlexi New value of property creatingFlexi.
     */
    public void setCreatingFlexi(java.lang.String creatingFlexi) {
        this.creatingFlexi = creatingFlexi;
    }
    
    /**
     * Getter for property flexiAmount.
     * @return Value of property flexiAmount.
     */
    public double getFlexiAmount() {
        return flexiAmount;
    }
    
    /**
     * Setter for property flexiAmount.
     * @param flexiAmount New value of property flexiAmount.
     */
    public void setFlexiAmount(double flexiAmount) {
        this.flexiAmount = flexiAmount;
    }
    
    /**
     * Getter for property reconcile.
     * @return Value of property reconcile.
     */
    public java.lang.String getReconcile() {
        return reconcile;
    }
    
    /**
     * Setter for property reconcile.
     * @param reconcile New value of property reconcile.
     */
    public void setReconcile(java.lang.String reconcile) {
        this.reconcile = reconcile;
    }
    
    /**
     * Getter for property balanceType.
     * @return Value of property balanceType.
     */
    public java.lang.String getBalanceType() {
        return balanceType;
    }
    
    /**
     * Setter for property balanceType.
     * @param balanceType New value of property balanceType.
     */
    public void setBalanceType(java.lang.String balanceType) {
        this.balanceType = balanceType;
    }
    
    /**
     * Getter for property AuthorizeBy.
     * @return Value of property AuthorizeBy.
     */
    public java.lang.String getAuthorizeBy() {
        return AuthorizeBy;
    }
    
    /**
     * Setter for property AuthorizeBy.
     * @param AuthorizeBy New value of property AuthorizeBy.
     */
    public void setAuthorizeBy(java.lang.String AuthorizeBy) {
        this.AuthorizeBy = AuthorizeBy;
    }
    
    public java.lang.String getAuthorizeStatus() {
        return authorizeStatus;
    }
    
    /**
     * Setter for property AuthorizeBy.
     * @param AuthorizeBy New value of property AuthorizeBy.
     */
    public void setAuthorizeStatus(java.lang.String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }
    
    /**
     * Getter for property corpLoanMap.
     * @return Value of property corpLoanMap.
     */
    public java.util.Map getCorpLoanMap() {
        return corpLoanMap;
    }
    
    /**
     * Setter for property corpLoanMap.
     * @param corpLoanMap New value of property corpLoanMap.
     */
    public void setCorpLoanMap(java.util.Map corpLoanMap) {
        this.corpLoanMap = corpLoanMap;
    }
    
    /**
     * Getter for property lblHouseName.
     * @return Value of property lblHouseName.
     */
    public java.lang.String getLblHouseName() {
        return lblHouseName;
    }
    
    /**
     * Setter for property lblHouseName.
     * @param lblHouseName New value of property lblHouseName.
     */
    public void setLblHouseName(java.lang.String lblHouseName) {
        this.lblHouseName = lblHouseName;
    }
    
    /**
     * Getter for property txtNarration.
     * @return Value of property txtNarration.
     */
    public java.lang.String getTxtNarration() {
        return txtNarration;
    }
    
    /**
     * Setter for property txtNarration.
     * @param txtNarration New value of property txtNarration.
     */
    public void setTxtNarration(java.lang.String txtNarration) {
        this.txtNarration = txtNarration;
    }
    
    public com.see.truetransact.clientutil.EnhancedTableModel getLockTable() {
        return lockTable;
    }
    
    /**
     * Setter for property gahanTable.
     * @param gahanTable New value of property gahanTable.
     */
    public void setLockTable(com.see.truetransact.clientutil.EnhancedTableModel lockTable) {
        this.lockTable = lockTable;
    }
    
}
