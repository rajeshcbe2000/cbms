/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ChequeBookOB.java
 *
 * Created on January 20, 2004, 2:11 PM
 */

package com.see.truetransact.ui.supporting.chequebook;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;

import com.see.truetransact.transferobject.supporting.chequebook.ChequeBookTO;
import com.see.truetransact.transferobject.supporting.chequebook.ChequeBookStopPaymentTO;
import com.see.truetransact.transferobject.supporting.chequebook.ChequeBookLooseLeafTO;
import com.see.truetransact.transferobject.supporting.chequebook.ECSStopPaymentTO;

import com.see.truetransact.clientproxy.ProxyParameters;

import com.see.truetransact.clientutil.CMandatoryDialog;

import org.apache.log4j.Logger;

import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.ui.TrueTransactMain;
/**
 *
 * @author  rahul
 */
public class ChequeBookOB extends CObservable {
    
    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;
    private HashMap authorizeMap;
    
    private ArrayList key;
    private ArrayList value;
    final ArrayList chequeTabTitle = new ArrayList();
    private ArrayList chequeTabRow;
    
    private int actionType;
    private int result;
    final int CHEQUE=0;
    final int STOP=1;
    final int LEAF=2;
    final int REVOKE=3;
    final int ECS=6;
    
    private final int INFO_MESSAGE = 1;
    private final int ALERT_MESSAGE = 0;
    
    private ComboBoxModel cbmProdId;
    private ComboBoxModel cbmMethodOfDelivery;
    private ComboBoxModel cbmProductId;
    private ComboBoxModel cbmProductID;
    private ComboBoxModel cbmUsage;
    private ComboBoxModel cbmNoOfLeaves;
    private ComboBoxModel cbmProdType;
    private ComboBoxModel cbmProductType;
    private ComboBoxModel cbmProduct_Type;
    private ComboBoxModel cbmReasonStopPayment;
    private ComboBoxModel cbmEcsProdType;
    private ComboBoxModel cbmEcsProdId;
    private ComboBoxModel cbmEcsReasonForStopPayment;
    
    private ProxyFactory proxy = null;
    
    private String chqIssueId;
    private String chqStopId;
    private String chqLeafId;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String lblAccountHeadDesc;
    private String lblAccountHeadProdId;
    private String lblProductIdAccHdId;
    private String lblProductIdAccHdDesc;
    private String lblProductIDAccHdId;
    private String lblProductIDAccHdDesc;
    private String lblCustId;
    private String lblCustName;
    private String lblCustStopId;
    private String lblCustStopName;
    private String lblCustLooseId;
    private String lblCustLooseName;
    //    private String lblClearBalance;
    //    private String lblTotalBalance;
    private String lblUnpaidCheques;
    private String lblChequesReturned;
    private String lblChequesIssued;
    private String lblChqRevokeDtVal = "";
    private String lblChqStopDtVal = "";
     private String lblCustEcsStopName;
     private String lblCustEcsStopId;
     private String lblEcsRevokeDtVal = "";
    
    private String authStatus = "";
    
    private String tempLeafNo1 = "";
    private String tempLeafNo2 = "";
    
    private String tempStopStartNo1 = "";
    private String tempStopStartNo2 = "";
    private String tempStopEndNo1 = "";
    private String tempStopEndNo2 = "";
    
    private String tempEcsEndNo1="";
    private String tempEcsEndNo2="";
    
    final String SINGLE = "Single";
    final String BULK = "Bulk";
    final String YES = "YES";
    final String NO = "NO";
    
    String chequeCharges = null;
    String transId = "";
    String prodTypeTO = "";
    
    String status = "";
    Date curDate = null;
    private String authRemarks = "";
    private String stopStat = "";
    public boolean chequeBook = false;
    public boolean stopPayment = false;
    public boolean looseLeaf = false;
    public boolean ecsStopPayment = false;
    
    private EnhancedTableModel tblChequeTab;
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(ChequeBookUI.class);
    
    // To get the Value of Column Title and Dialogue Box...
    //    final ChequeBookRB objChequeBookRB = new ChequeBookRB();
    java.util.ResourceBundle objChequeBookRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.supporting.chequebook.ChequeBookRB", ProxyParameters.LANGUAGE);
    
    private static ChequeBookOB chequeBookOB;
    //    static {
    //        try {
    //            log.info("In ChequeBookOB Declaration");
    //            chequeBookOB = new ChequeBookOB();
    //        } catch(Exception e) {
    //            log.info("Error in ChequeBookOB Declaration");
    //        }
    //    }
    
    //    public static ChequeBookOB getInstance() {
    //        return chequeBookOB;
    //    }
    
    /** Creates a new instance of ChequeBookOB */
    public ChequeBookOB() throws Exception {
        initianSetup();
    }
    public ChequeBookOB(String selectedBranch) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>selectedBranch : " + selectedBranch);
        setSelectedBranchID(selectedBranch);
        initianSetup();
    }
    
    private void initianSetup() throws Exception{
        log.info("initianSetup");
        curDate = ClientUtil.getCurrentDate();
        setOperationMap();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            log.info(" Error In initianSetup()");
            //e.printStackTrace();
        }
        fillDropdown();// To Fill all the Combo Boxes
        setChequeTabTitle();// To set the Title of Table in Charges Tab...
        tblChequeTab = new EnhancedTableModel(null, chequeTabTitle);
    }
    
    // To set the Column title in Table...
    private void setChequeTabTitle() throws Exception{
        log.info("In setChequeTabTitle...");
        
        chequeTabTitle.add(objChequeBookRB.getString("tblColumn1"));
        chequeTabTitle.add(objChequeBookRB.getString("tblColumn2"));
        chequeTabTitle.add(objChequeBookRB.getString("tblColumn3"));
        chequeTabTitle.add(objChequeBookRB.getString("tblColumn4"));
        chequeTabTitle.add(objChequeBookRB.getString("tblColumn5"));
        
    }
    
    
    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception{
        log.info("In setOperationMap()");
        
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "ChequeBookJNDI");
        operationMap.put(CommonConstants.HOME, "supporting.chequebook.ChequeBookHome");
        operationMap.put(CommonConstants.REMOTE, "supporting.chequebook.ChequeBook");
    }
    
    public void fillDropdown() throws Exception{
        log.info("fillDropdown");
        
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        /*lookup_keys.add("OP_AC_PRODUCT");*/
        lookup_keys.add("PROD_ID");
        lookup_keys.add("CHEQUE_PROD_TYPE");
        lookup_keys.add("CHEQUEBOOKISSUE");
        lookup_keys.add("CHEQUESTOP.REASON");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        
        getKeyValue((HashMap)keyValue.get("CHEQUEBOOKISSUE"));
        cbmMethodOfDelivery = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("CHEQUE_PROD_TYPE"));
        cbmProdType = new ComboBoxModel(key,value);
        cbmProductType = new ComboBoxModel(key,value);
        cbmProduct_Type = new ComboBoxModel(key,value);
        cbmEcsProdType= new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("CHEQUESTOP.REASON"));
        cbmReasonStopPayment = new ComboBoxModel(key,value);
        cbmEcsReasonForStopPayment = new ComboBoxModel(key,value);
        
        // To obtain the Product id from the Table "OP_AC_PRODUCT"...
        // here "getAccProduct" is the mapped Statement name, defined in InwardClearingMap...
        //        lookUpHash.put(CommonConstants.MAP_NAME,"getProductId");
        //        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        //        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        //        cbmProdId = new ComboBoxModel(key,value);
        //        cbmProductId = new ComboBoxModel(key,value);
        //        cbmProductID = new ComboBoxModel(key,value);
        System.out.println("getSelectedBranchID() : " + getSelectedBranchID());
        lookUpHash.put(CommonConstants.MAP_NAME,"ChequeIssue.getChequeSubType");
        //        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        lookUpHash.put(CommonConstants.PARAMFORQUERY, getSelectedBranchID());
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmUsage = new ComboBoxModel(key,value);
        
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        log.info("getKeyValue");
        
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
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
    
    // To enter the Data into the Database...Called from doActionPerform()...
    //=====Cheque Book Issue...=====
    public ChequeBookTO setChequeBook() {
        log.info("In setChequeBook()");
        
        final ChequeBookTO objChequeBookTO = new ChequeBookTO();
        try{
            objChequeBookTO.setChqIssueId(chqIssueId);
            
            objChequeBookTO.setProdType(CommonUtil.convertObjToStr(cbmProdType.getKeyForSelected()));
            objChequeBookTO.setProdId(CommonUtil.convertObjToStr(cbmProdId.getKeyForSelected()));
            
            System.out.println("Prod_Id: " + CommonUtil.convertObjToStr(cbmProdId.getKeyForSelected()));
            
            objChequeBookTO.setAcctNo(txtAccountNo);
            //objChequeBookTO.setUnpaidChq(new Double(getTxtUnpaidChq()));
            //objChequeBookTO.setChqReturned(new Double(getTxtChqReturned()));
            //            objChequeBookTO.setChqIssue(CommonUtil.convertObjToDouble(getLblChequesIssued()));
            objChequeBookTO.setAcctNames(txtNamesOfAccount);
            objChequeBookTO.setChqDelivery((String)cbmMethodOfDelivery.getKeyForSelected());
            objChequeBookTO.setNoLeaves(CommonUtil.convertObjToDouble(cbmNoOfLeaves.getKeyForSelected()));
            objChequeBookTO.setNoChqBooks(CommonUtil.convertObjToDouble(txtNoOfChequeBooks));
            objChequeBookTO.setStartChqNo1(txtStartingCheque);
            objChequeBookTO.setStartChqNo2(txtStartingChequeNo);
            objChequeBookTO.setEndChqNo1(txtEndingcheque);
            objChequeBookTO.setEndChqNo2(txtEndingchequeNo);
            objChequeBookTO.setChqBkSeriesFrom(CommonUtil.convertObjToDouble(txtSeriesFrom));
            objChequeBookTO.setChqBkSeriesTo(CommonUtil.convertObjToDouble(txtSeriesNoTo));
            objChequeBookTO.setChargesCollected(CommonUtil.convertObjToDouble(txtChargesCollected));
            objChequeBookTO.setRemarks(txtRemarks);
            objChequeBookTO.setChequeSubType((String)cbmUsage.getKeyForSelected());
            
            objChequeBookTO.setTransOutId(getTransId());
            objChequeBookTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
            objChequeBookTO.setBranchId(getSelectedBranchID());
            System.out.println("getTransOutId: " + objChequeBookTO.getTransOutId());
            
        }catch(Exception e){
            log.info("Error In setChequeBook()");
            e.printStackTrace();
        }
        return objChequeBookTO;
    }
    
    //=====Stop Payment Issue...=====
    public ChequeBookStopPaymentTO setChequeBookStopPayment() {
        log.info("In setChequeBookStopPayment()");
        
        final ChequeBookStopPaymentTO objChequeBookStopPaymentTO = new ChequeBookStopPaymentTO();
        try{
            objChequeBookStopPaymentTO.setChqStopId(chqStopId);
            
            objChequeBookStopPaymentTO.setProdType(CommonUtil.convertObjToStr(cbmProductType.getKeyForSelected()));
            objChequeBookStopPaymentTO.setProdId(CommonUtil.convertObjToStr(cbmProductId.getKeyForSelected()));
            objChequeBookStopPaymentTO.setAcctNo(txtAccNo);
            objChequeBookStopPaymentTO.setStartChqNo1(txtStartCheque);
            objChequeBookStopPaymentTO.setStartChqNo2(txtStartchequeNo);
            objChequeBookStopPaymentTO.setEndChqNo1(txtEndCheque);
            objChequeBookStopPaymentTO.setEndChqNo2(txtEndingChequeNo);
            
            Date ChDt = DateUtil.getDateMMDDYYYY(tdtChequeDate);
            if(ChDt != null){
            Date chDate = (Date)curDate.clone();
            chDate.setDate(ChDt.getDate());
            chDate.setMonth(ChDt.getMonth());
            chDate.setYear(ChDt.getYear());
//            objChequeBookStopPaymentTO.setChqDt(DateUtil.getDateMMDDYYYY(tdtChequeDate));
            objChequeBookStopPaymentTO.setChqDt(chDate);
            }else{
                objChequeBookStopPaymentTO.setChqDt(DateUtil.getDateMMDDYYYY(tdtChequeDate));
            }
            
            if (getRdoLeaf_single() == true) {
                objChequeBookStopPaymentTO.setLeaf(SINGLE);
            } else if (getRdoLeaf_Bulk() == true) {
                objChequeBookStopPaymentTO.setLeaf(BULK);
            }
            objChequeBookStopPaymentTO.setPayeeName(txtPayeeName);
            objChequeBookStopPaymentTO.setChqAmt(CommonUtil.convertObjToDouble(txtChequeAmt));
            objChequeBookStopPaymentTO.setStopPayChrg(CommonUtil.convertObjToDouble(txtStopPaymentCharges));
            objChequeBookStopPaymentTO.setStopPayReason(CommonUtil.convertObjToStr(cbmReasonStopPayment.getKeyForSelected()));
            objChequeBookStopPaymentTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
            objChequeBookStopPaymentTO.setBranchId(getSelectedBranchID());
            objChequeBookStopPaymentTO.setAuthorizeRemarks(authRemarks);
            objChequeBookStopPaymentTO.setStopStatus(stopStat);
        }catch(Exception e){
            log.info("Error In setChequeBookStopPayment()");
            e.printStackTrace();
        }
        return objChequeBookStopPaymentTO;
    }
    
    //=====Loose Leaf Issue...=====
    public ChequeBookLooseLeafTO setChequeBookLooseLeaf() {
        log.info("In setChequeBookStopPayment()");
        
        final ChequeBookLooseLeafTO objChequeBookLooseLeafTO = new ChequeBookLooseLeafTO();
        try{
            objChequeBookLooseLeafTO.setChqLeafId(chqLeafId);
            
            objChequeBookLooseLeafTO.setProdType(CommonUtil.convertObjToStr(cbmProduct_Type.getKeyForSelected()));
            objChequeBookLooseLeafTO.setProdId(CommonUtil.convertObjToStr(cbmProductID.getKeyForSelected()));
            objChequeBookLooseLeafTO.setAcctNo(txtAccounNumber);
            objChequeBookLooseLeafTO.setLeafNo1(txtLeafNo1);
            objChequeBookLooseLeafTO.setLeafNo2(txtLeafNo2);
            objChequeBookLooseLeafTO.setRemarks(txtRemark);
            objChequeBookLooseLeafTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
            objChequeBookLooseLeafTO.setBranchId(getSelectedBranchID());
        }catch(Exception e){
            log.info("Error In setChequeBookLooseLeaf()");
            e.printStackTrace();
        }
        return objChequeBookLooseLeafTO;
    }
    public ECSStopPaymentTO setEcsStopPayment() {
        log.info("In setEcsStopPayment()");
        
        final ECSStopPaymentTO objECSStopPaymentTO = new ECSStopPaymentTO();
        try{
            objECSStopPaymentTO.setEcsStopId(ecsStopId); 
            objECSStopPaymentTO.setEcsProdType(CommonUtil.convertObjToStr(cbmEcsProdType.getKeyForSelected()));
            objECSStopPaymentTO.setEcsProdId(CommonUtil.convertObjToStr(cbmEcsProdId.getKeyForSelected()));
            objECSStopPaymentTO.setEcsAcctNo(ecsAcctNo);
            objECSStopPaymentTO.setEcsAmt(CommonUtil.convertObjToDouble(ecsAmt));
            objECSStopPaymentTO.setEcsEndChqNo1(ecsEndChqNo1);
            objECSStopPaymentTO.setEcsEndChqNo2(ecsEndChqNo2);
            objECSStopPaymentTO.setEcsPayeeName(ecsPayeeName);
            objECSStopPaymentTO.setEcsStopPayChrg(CommonUtil.convertObjToDouble(ecsStopPayChrg));
            objECSStopPaymentTO.setEcsStopPayReason(CommonUtil.convertObjToStr(cbmEcsReasonForStopPayment.getKeyForSelected()));
            objECSStopPaymentTO.setEcsBranchId(ProxyParameters.BRANCH_ID);
            objECSStopPaymentTO.setEcsAuthorizeRemarks(authRemarks);
            objECSStopPaymentTO.setEcsStopStatus(stopStat);
            Date ChDt = DateUtil.getDateMMDDYYYY(ecsDt);
            if(ChDt != null){
            Date chDate = (Date)curDate.clone();
            chDate.setDate(ChDt.getDate());
            chDate.setMonth(ChDt.getMonth());
            chDate.setYear(ChDt.getYear());
            objECSStopPaymentTO.setEcsDt(chDate);
            }
            else{
                 objECSStopPaymentTO.setEcsDt(DateUtil.getDateMMDDYYYY(ecsDt));
            }
        }catch(Exception e){
            log.info("Error In setChequeBookStopPayment()");
            e.printStackTrace();
        }
        return objECSStopPaymentTO;
    }
    
    
    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap, int panEditDelete) {
        log.info("In populateData()");
        
        final HashMap mapData;
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            populateOB(mapData,panEditDelete);
        } catch( Exception e ) {
            log.info("Error In populateData()");
            e.printStackTrace();
        }
    }
    
    private void populateOB(HashMap mapData, int panEditDelete) throws Exception{
        log.info("In populateOB()");
        ChequeBookTO objChequeBookTO = null;
        ChequeBookStopPaymentTO objChequeBookStopPaymentTO = null;
        ChequeBookLooseLeafTO objChequeBookLooseLeafTO = null;
        ECSStopPaymentTO objECSStopPaymentTO =null;
        
        if(panEditDelete==CHEQUE){
            objChequeBookTO = (ChequeBookTO) ((List) mapData.get("DATA")).get(0);
            setChequeBookTO(objChequeBookTO);
        }else if(panEditDelete==STOP){
            objChequeBookStopPaymentTO = (ChequeBookStopPaymentTO) ((List) mapData.get("DATA")).get(0);
            setChequeBookStopPaymentTO(objChequeBookStopPaymentTO);
        }else if(panEditDelete==LEAF){
            objChequeBookLooseLeafTO = (ChequeBookLooseLeafTO) ((List) mapData.get("DATA")).get(0);
            setChequeBookLooseLeafTO(objChequeBookLooseLeafTO);
        }
        else if(panEditDelete==ECS){
            objECSStopPaymentTO = (ECSStopPaymentTO) ((List) mapData.get("DATA")).get(0);
            setEcsStopPaymentTO(objECSStopPaymentTO);
        }
//         notifyObservers();
    }
    
    // To Enter the values in the UI fields, from the database...
    //=====Cheque Book Issue...=====
    private void setChequeBookTO(ChequeBookTO objChequeBookTO) throws Exception{
        log.info("In setChequeBookTO()");
        
        setChequeIssueId(CommonUtil.convertObjToStr(objChequeBookTO.getChqIssueId()));
        
        setCboProdType((String) getCbmProdType().getDataForKey(CommonUtil.convertObjToStr(objChequeBookTO.getProdType())));
        getProductID(CommonUtil.convertObjToStr(objChequeBookTO.getProdType()));
        
        setCboProdId((String) getCbmProdId().getDataForKey(CommonUtil.convertObjToStr(objChequeBookTO.getProdId())));
        setTxtAccountNo(CommonUtil.convertObjToStr(objChequeBookTO.getAcctNo()));
        setCboMethodOfDelivery((String) getCbmMethodOfDelivery().getDataForKey(CommonUtil.convertObjToStr(objChequeBookTO.getChqDelivery())));
        setTxtNamesOfAccount(objChequeBookTO.getAcctNames());
        setTxtNoOfChequeBooks(CommonUtil.convertObjToStr(objChequeBookTO.getNoChqBooks()));
        setTxtStartingCheque(CommonUtil.convertObjToStr(objChequeBookTO.getStartChqNo1()));
        setTxtStartingChequeNo(CommonUtil.convertObjToStr(objChequeBookTO.getStartChqNo2()));
        setTxtEndingcheque(CommonUtil.convertObjToStr(objChequeBookTO.getEndChqNo1()));
        setTxtEndingchequeNo(CommonUtil.convertObjToStr(objChequeBookTO.getEndChqNo2()));
        setTxtSeriesFrom(CommonUtil.convertObjToStr(objChequeBookTO.getChqBkSeriesFrom()));
        setTxtSeriesNoTo(CommonUtil.convertObjToStr(objChequeBookTO.getChqBkSeriesTo()));
        setTxtChargesCollected(CommonUtil.convertObjToStr(objChequeBookTO.getChargesCollected()));
        setTxtRemarks(CommonUtil.convertObjToStr(objChequeBookTO.getRemarks()));
        setCboUsage((String) getCbmUsage().getDataForKey(CommonUtil.convertObjToStr(objChequeBookTO.getChequeSubType())));
        getLeavesPerBook(CommonUtil.convertObjToStr(objChequeBookTO.getChequeSubType()));
        setCboNoOfLeaves((String) getCbmNoOfLeaves().getDataForKey(CommonUtil.convertObjToStr(objChequeBookTO.getNoLeaves())));
        
        setTransId(CommonUtil.convertObjToStr(objChequeBookTO.getTransOutId()));
        
        setProdTypeTO(CommonUtil.convertObjToStr(objChequeBookTO.getProdId()));
        
        setStatus(CommonUtil.convertObjToStr(objChequeBookTO.getStatus()));
        setStatusBy(objChequeBookTO.getStatusBy());
        setAuthorizeStatus(objChequeBookTO.getAuthorizeStatus());
        
        //        if(objChequeBookTO.getUnpaidChq()!=null){
        //            setLblUnpaidCheques(CommonUtil.convertObjToStr(objChequeBookTO.getUnpaidChq()));
        //        }if(objChequeBookTO.getChqReturned()!=null){
        //            setLblChequesReturned(CommonUtil.convertObjToStr(objChequeBookTO.getChqReturned()));
        //        }if(objChequeBookTO.getChqIssue()!=null){
        //            setLblChequesIssued(CommonUtil.convertObjToStr(objChequeBookTO.getChqIssue()));
        //        }
    }
    
    //=====Stop Payment Issue...=====
    private void setChequeBookStopPaymentTO(ChequeBookStopPaymentTO objChequeBookStopPaymentTO) throws Exception{
        log.info("In setChequeBookStopPaymentTO()");
        setChequeStopId(CommonUtil.convertObjToStr(objChequeBookStopPaymentTO.getChqStopId()));
        
        setCboProductType((String) getCbmProductType().getDataForKey(CommonUtil.convertObjToStr(objChequeBookStopPaymentTO.getProdType())));
        getProductID(CommonUtil.convertObjToStr(objChequeBookStopPaymentTO.getProdType()));
        
        setCboProductId((String) getCbmProductId().getDataForKey(CommonUtil.convertObjToStr(objChequeBookStopPaymentTO.getProdId())));
        setTxtAccNo(CommonUtil.convertObjToStr(objChequeBookStopPaymentTO.getAcctNo()));
        setTxtStartCheque(CommonUtil.convertObjToStr(objChequeBookStopPaymentTO.getStartChqNo1()));
        setTxtStartchequeNo(CommonUtil.convertObjToStr(objChequeBookStopPaymentTO.getStartChqNo2()));
        setTxtEndCheque(CommonUtil.convertObjToStr(objChequeBookStopPaymentTO.getEndChqNo1()));
        setTxtEndingChequeNo(CommonUtil.convertObjToStr(objChequeBookStopPaymentTO.getEndChqNo2()));
        
        if (CommonUtil.convertObjToStr(objChequeBookStopPaymentTO.getLeaf()).equals(SINGLE)) setRdoLeaf_single(true);
        else setRdoLeaf_Bulk(true);
        
        setTdtChequeDate(DateUtil.getStringDate(objChequeBookStopPaymentTO.getChqDt()));
        setTxtChequeAmt(CommonUtil.convertObjToStr(objChequeBookStopPaymentTO.getChqAmt()));
        setTxtPayeeName(CommonUtil.convertObjToStr(objChequeBookStopPaymentTO.getPayeeName()));
        setTxtStopPaymentCharges(CommonUtil.convertObjToStr(objChequeBookStopPaymentTO.getStopPayChrg()));
        setCboReasonStopPayment((String) getCbmReasonStopPayment().getDataForKey(CommonUtil.convertObjToStr(objChequeBookStopPaymentTO.getStopPayReason())));
        setAuthStatus(CommonUtil.convertObjToStr(objChequeBookStopPaymentTO.getAuthorizeStatus()));
        
        tempStopStartNo1 = getTxtStartCheque();
        tempStopStartNo2 = getTxtStartchequeNo();
        tempStopEndNo1 = getTxtEndCheque();
        tempStopEndNo2 = getTxtEndingChequeNo();
        
        setStatus(CommonUtil.convertObjToStr(objChequeBookStopPaymentTO.getStatus()));
        setStatusBy(objChequeBookStopPaymentTO.getStatusBy());
        setAuthorizeStatus(objChequeBookStopPaymentTO.getAuthorizeStatus());
        setAuthRemarks(objChequeBookStopPaymentTO.getAuthorizeRemarks());
        setStopStat(objChequeBookStopPaymentTO.getStopStatus());
        setLblChqRevokeDtVal(DateUtil.getStringDate(objChequeBookStopPaymentTO.getChqRevokeDt()));
        setLblChqStopDtVal(DateUtil.getStringDate(objChequeBookStopPaymentTO.getChqStopDt()));
    }
    private void setEcsStopPaymentTO(ECSStopPaymentTO objECSStopPaymentTO) throws Exception{
        setEcsStopId(CommonUtil.convertObjToStr(objECSStopPaymentTO.getEcsStopId()));
        setCboEcsProdType((String) getCbmEcsProdType().getDataForKey(CommonUtil.convertObjToStr(objECSStopPaymentTO.getEcsProdType())));
        getProductID(CommonUtil.convertObjToStr(objECSStopPaymentTO.getEcsProdType()));
        setCboEcsProdId((String) getCbmEcsProdId().getDataForKey(CommonUtil.convertObjToStr(objECSStopPaymentTO.getEcsProdId())));
        setEcsAcctNo(CommonUtil.convertObjToStr(objECSStopPaymentTO.getEcsAcctNo()));
        setEcsAmt(CommonUtil.convertObjToStr(objECSStopPaymentTO.getEcsAmt()));
        setEcsEndChqNo1(CommonUtil.convertObjToStr(objECSStopPaymentTO.getEcsEndChqNo1()));
        setEcsEndChqNo2(CommonUtil.convertObjToStr(objECSStopPaymentTO.getEcsEndChqNo2()));
        setEcsDt(DateUtil.getStringDate(objECSStopPaymentTO.getEcsDt()));
        tempEcsEndNo1=getEcsEndChqNo1();
        tempEcsEndNo2=getEcsEndChqNo2();
        setEcsPayeeName(CommonUtil.convertObjToStr(objECSStopPaymentTO.getEcsPayeeName()));
        setEcsStopPayChrg(CommonUtil.convertObjToStr(objECSStopPaymentTO.getEcsStopPayChrg()));
        setEcsStopPayReason((String) getCbmEcsReasonForStopPayment().getDataForKey(CommonUtil.convertObjToStr(objECSStopPaymentTO.getEcsStopPayReason())));
        setAuthStatus(CommonUtil.convertObjToStr(objECSStopPaymentTO.getEcsAuthorizeStatus()));
        setStatus(CommonUtil.convertObjToStr(objECSStopPaymentTO.getEcsStatus()));
        setStatusBy(objECSStopPaymentTO.getEcsStatusBy());
        setAuthorizeStatus(objECSStopPaymentTO.getEcsAuthorizeStatus());
        setAuthRemarks(objECSStopPaymentTO.getEcsAuthorizeRemarks());
        setStopStat(objECSStopPaymentTO.getEcsStopStatus());
        setCboEcsReasonStopPayment((String) getCbmEcsReasonForStopPayment().getDataForKey(CommonUtil.convertObjToStr(objECSStopPaymentTO.getEcsStopPayReason())));
        setLblEcsRevokeDtVal(DateUtil.getStringDate(objECSStopPaymentTO.getEcsRevokeDt()));
        setEcsStopDt(DateUtil.getStringDate(objECSStopPaymentTO.getEcsStopDt()));
    }
    
    //=====Loose Leaf Issue...=====
    private void setChequeBookLooseLeafTO(ChequeBookLooseLeafTO objChequeBookLooseLeafTO) throws Exception{
        log.info("In setChequeBookLooseLeafTO()");
        
        setChequeLeafId(CommonUtil.convertObjToStr(objChequeBookLooseLeafTO.getChqLeafId()));
        
        setCboProduct_Type((String) getCbmProduct_Type().getDataForKey(CommonUtil.convertObjToStr(objChequeBookLooseLeafTO.getProdType())));
        getProductID(CommonUtil.convertObjToStr(objChequeBookLooseLeafTO.getProdType()));
        
        setCboProductID((String) getCbmProductID().getDataForKey(CommonUtil.convertObjToStr(objChequeBookLooseLeafTO.getProdId())));
        setTxtAccounNumber(CommonUtil.convertObjToStr(objChequeBookLooseLeafTO.getAcctNo()));
        setTxtLeafNo1(CommonUtil.convertObjToStr(objChequeBookLooseLeafTO.getLeafNo1()));
        setTxtLeafNo2(CommonUtil.convertObjToStr(objChequeBookLooseLeafTO.getLeafNo2()));
        setTxtRemark(CommonUtil.convertObjToStr(objChequeBookLooseLeafTO.getRemarks()));
        
        tempLeafNo1 = getTxtLeafNo1();
        tempLeafNo2 = getTxtLeafNo2();
        
        setStatus(CommonUtil.convertObjToStr(objChequeBookLooseLeafTO.getStatus()));
        setStatusBy(objChequeBookLooseLeafTO.getStatusBy()); 
        setAuthorizeStatus(objChequeBookLooseLeafTO.getAuthorizeStatus());
    }
    
    //To fill the Table at the Time of insert, update and delete...
    public void fillChequeTab(String ACCNUM){
        log.info("In fullChequeTab()");
        try{
            //__ To Clear the Data in the Table...
            resetTable();
            
            final HashMap acctNumMap = new HashMap();
            acctNumMap.put("ACCT_NO",ACCNUM);
            final ArrayList resultList = (ArrayList)ClientUtil.executeQuery("getTableValues", acctNumMap);
            if(resultList != null)
                setChequeTab(resultList);
        }catch(Exception e){
            log.info("Error in fillChequeTab...");
        }
    }
    
    private void setChequeTab(ArrayList chequeTabFill) throws Exception{
        log.info("In setChequeTab()");
        ChequeBookTO objChequeBookTO;
        tblChequeTab = new EnhancedTableModel(null, chequeTabTitle);
        if (chequeTabFill != null){
            log.info("In setChequeTab() After if before For...");
            for (int i=0, j= chequeTabFill.size();i<j;i++){
                chequeTabRow = new ArrayList();
                StringBuffer buff1 = new StringBuffer();
                StringBuffer buff2 = new StringBuffer();
                final HashMap resultMap = (HashMap)chequeTabFill.get(i);
                
                buff1.append(CommonUtil.convertObjToStr(resultMap.get("START_CHQ_NO1")));
                buff1.append(" ");
                buff1.append(CommonUtil.convertObjToStr(resultMap.get("START_CHQ_NO2")));
                
                buff2.append(CommonUtil.convertObjToStr(resultMap.get("END_CHQ_NO1")));
                buff2.append(" ");
                buff2.append(CommonUtil.convertObjToStr(resultMap.get("END_CHQ_NO2")));
                
                chequeTabRow.add(CommonUtil.convertObjToStr(resultMap.get("CHQ_ISSUE_DT")));
                chequeTabRow.add(CommonUtil.convertObjToStr(resultMap.get("CHQ_BK_SERIES_FROM")));
                chequeTabRow.add(CommonUtil.convertObjToStr(resultMap.get("CHQ_BK_SERIES_TO")));
                chequeTabRow.add(buff1);
                chequeTabRow.add(buff2);
                
                chequeCharges = CommonUtil.convertObjToStr(resultMap.get("CHARGES_COLLECTED"));
                
                tblChequeTab.insertRow(0,chequeTabRow);
            }
        }
        ttNotifyObservers();
    }
    
    // TESTING THE DOACTION...
    public void doAction(int pan) {
        log.info("In doAction()");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null || getAuthorizeMap() != null){
                    doActionPerform(pan);
                }
            }
            else
                log.info("Action Type Not Defined In setChequeBookTO()");
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            
            System.out.println("Error : " + e.toString());
            parseException.logException(e,true);
        }
    }
    
    //testing the doaction perform...
    private void doActionPerform(int pan) throws Exception{
        log.info("In doActionPerform()");
        
        System.out.println(" pan: " +  pan);
        
        final HashMap data = new HashMap();
        data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
        data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        if (pan==CHEQUE){
            if(getAuthorizeMap() == null){
                final ChequeBookTO objChequeBookTO = setChequeBook();
                objChequeBookTO.setCommand(getCommand());
                data.put("ChequeBookTO",objChequeBookTO);
                data.put("TRANS_ID",getTransId());
            }
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            resetChequeIssueForm();
            
        }else if(pan==STOP){
            if(getAuthorizeMap() == null){
                final ChequeBookStopPaymentTO objChequeBookStopPaymentTO = setChequeBookStopPayment();
                objChequeBookStopPaymentTO.setCommand(getCommand());
                data.put("ChequeBookStopPaymentTO",objChequeBookStopPaymentTO);
                
                if(tempStopStartNo1.equalsIgnoreCase(getTxtStartCheque())
                &&  tempStopStartNo2.equalsIgnoreCase(getTxtStartchequeNo())
                &&  tempStopEndNo1.equalsIgnoreCase(getTxtEndCheque())
                &&  tempStopEndNo2.equalsIgnoreCase(getTxtEndingChequeNo())
                && getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_UPDATE)){
                    data.put("StopPaymentRule",NO);
                }else{
                    data.put("StopPaymentRule",YES);
                }
            }
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            resetStopPaymentForm();
            
        }else if(pan==LEAF){
            final ChequeBookLooseLeafTO objChequeBookLooseLeafTO = setChequeBookLooseLeaf();
            objChequeBookLooseLeafTO.setCommand(getCommand());
            data.put("ChequeBookLooseLeafTO",objChequeBookLooseLeafTO);
            
            if(tempLeafNo1.equalsIgnoreCase(getTxtLeafNo1())
            &&  tempLeafNo2.equalsIgnoreCase(getTxtLeafNo2())
            && getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_UPDATE)){
                data.put("LooseLeafRule",NO);
            }else{
                data.put("LooseLeafRule",YES);
            }
            resetLooseLeafForm();
            
        }
        else if(pan==ECS){
            if(getAuthorizeMap() == null){
                final ECSStopPaymentTO objECSStopPaymentTO = setEcsStopPayment();
                objECSStopPaymentTO.setCommand(getCommand());
                data.put("ECSStopPaymentTO",objECSStopPaymentTO);
                
                if(tempEcsEndNo1.equalsIgnoreCase(getEcsEndChqNo1())
                &&  tempEcsEndNo2.equalsIgnoreCase(getEcsEndChqNo2())
                && getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_UPDATE)){
                    data.put("EcsStopPaymentRule",NO);
                }else{
                    data.put("EcsStopPaymentRule",YES);
                }
            }
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            resetEcsStopPaymentForm();
            
        }
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        HashMap proxyResultMap = proxy.execute(data,operationMap);
        setProxyReturnMap(proxyResultMap);
        setResult(actionType);
    }
    
    
    
    /* To decide which action Should be performed...*/
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
    
    //__ To format the data regarding the chequecook Generated...
    private void displayAuthData(HashMap proxyResultMap){
        
        System.out.println("displayAuthData Called ion OB");
        
        HashMap resultMap;
        
        final Object[] keyList = (proxyResultMap.keySet()).toArray();
        final int keyListLength = keyList.length;
        StringBuffer str = new StringBuffer();
        str.append(objChequeBookRB.getString("MESSAGEHEADING"));
        
        for(int i=0; i<keyListLength; i++){
            resultMap = (HashMap)proxyResultMap.get(CommonUtil.convertObjToStr(keyList[i]));
            if(resultMap.containsKey("BK_SERIES_FROM")){
                str.append("\n\n");
                str.append(objChequeBookRB.getString("lblAccounNumber") + ": " + CommonUtil.convertObjToStr(resultMap.get("ACCOUNT NUMBER")) + "\n");
                str.append(objChequeBookRB.getString("lblSeriesNoFrom") + ": " + CommonUtil.convertObjToStr(resultMap.get("BK_SERIES_FROM")) + "\n" );
                str.append(objChequeBookRB.getString("lblSeriesNoTo") + ": " + CommonUtil.convertObjToStr(resultMap.get("BK_SERIES_TO")) + "\n" );
                
                str.append(objChequeBookRB.getString("lblStartChequeNo") + ": " +  CommonUtil.convertObjToStr(resultMap.get("START_LEAF_NO1")) + " " +
                CommonUtil.convertObjToStr(resultMap.get("START_LEAF_NO2")) + "\n" );
                
                str.append(objChequeBookRB.getString("lblEndChequeNo") + ": " + CommonUtil.convertObjToStr(resultMap.get("END_LEAF_NO1")) + " " +
                CommonUtil.convertObjToStr(resultMap.get("END_LEAF_NO2")) + "\n" );
                
            }else{
                StringBuffer alert = new StringBuffer();
                alert.append(objChequeBookRB.getString("CHEQUEBOOKWARNING") + "\n");
                alert.append(objChequeBookRB.getString("AVAILBOOKS") + CommonUtil.convertObjToDouble(resultMap.get("AVAILABLE_BOOKS")) + "\n");
                alert.append(objChequeBookRB.getString("ORDEREDBOOKS") + CommonUtil.convertObjToDouble(resultMap.get("NO OF BOOKS")) + "\n\n");
                alert.append(objChequeBookRB.getString("REORDERBOOKS"));
                displayMessage(alert.toString(), ALERT_MESSAGE);
                
                break;
            }
        }
        
        if(str.toString().length() > 0){
            displayMessage(str.toString(), INFO_MESSAGE);
        }
    }
    
    //__ To display the formatted data...
    private void displayMessage(String message, int icon){
        CMandatoryDialog cmd = new CMandatoryDialog(icon);
        cmd.setTitle(objChequeBookRB.getString("MESSAGETITLE"));
        cmd.setMessage(message);
        cmd.show();
    }
    
    public void resetChequeIssueForm(){
        //        setCboProdId("");
        setTxtAccountNo("");
        setTxtNamesOfAccount("");
        setCboMethodOfDelivery("");
        setCboNoOfLeaves("");
        setTxtNoOfChequeBooks("");
        setTxtChargesCollected("");
        setTxtRemarks("");
        setTxtSeriesFrom("");
        setTxtSeriesNoTo("");
        setTxtStartingCheque("");
        setTxtStartingChequeNo("");
        setTxtEndingcheque("");
        setTxtEndingchequeNo("");
        setCboUsage("");
        tblChequeTab = new EnhancedTableModel(null, chequeTabTitle);
        
        //        setProdTypeTO("");
        
        setStatus("");
    }
    public void resetStopPaymentForm(){
        //        setCboProductId("");
        setTxtAccNo("");
        setTxtStartCheque("");
        setTxtStartchequeNo("");
        setTxtEndCheque("");
        setTxtEndingChequeNo("");
        setTxtEndingcheque("");
        setTxtEndingchequeNo("");
        setRdoLeaf_single(false);
        setRdoLeaf_Bulk(false);
        setTdtChequeDate("");
        setTxtChequeAmt("");
        setTxtPayeeName("");
        setTxtStopPaymentCharges("");
        setCboReasonStopPayment("");
        setLblChqRevokeDtVal("");
        setLblChqStopDtVal("");
        setAuthStatus("");
        setStatus("");
        setAuthRemarks("");
    }
     public void resetEcsStopPaymentForm(){
        //        setCboProductId("");
        setEcsAcctNo("");
        setEcsAmt("");
        setEcsEndChqNo1("");
        setEcsEndChqNo2("");
        setEcsPayeeName("");
        setEcsStopDt("");
        setEcsStopPayChrg("");
        setEcsStopPayReason("");
        setCboEcsProdId("");
        setCboEcsProdType("");
        setCboEcsReasonStopPayment("");
        setEcsDt("");
    }
    public void resetLooseLeafForm(){
        //        setCboProductID("");
        setTxtAccounNumber("");
        setTxtLeafNo1("");
        setTxtLeafNo2("");
        setTxtRemark("");
        setStatus("");
    }
    
    /** TO RESET THE TABLE...*/
    public void resetTable(){
        try{
            ArrayList data = tblChequeTab.getDataArrayList();
            for(int i=data.size(); i>0; i--)
                tblChequeTab.removeRow(i-1);
            //            setChanged();
            //            notifyObservers();
        }catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in resetTable():");
        }
    }
    
    /** To reset the Lables in UI...*/
    public void resetLable(){
        /* Cheque Book Issue...*/
        this.setLblAccountHeadProdId("");
        this.setLblAccountHeadDesc("");
        this.setLblCuctId("");
        this.setLblCustName("");
        //        this.setLblClearBalance("");
        //        this.setLblTotalBalance("");
        //        this.setLblUnpaidCheques("");
        //        this.setLblChequesReturned("");
        //        this.setLblChequesIssued("");
        /* Stop Payment Issue...*/
        this.setLblProductIdAccHdId("");
        this.setLblProductIdAccHdDesc("");
        this.setLblCustStopId("");
        this.setLblCustStopName("");
        /* Loose Leaf Issue...*/
        this.setLblProductIDAccHdId("");
        this.setLblProductIDAccHdDesc("");
        this.setLblCustLooseId("");
        this.setLblCustLooseName("");
        
        this.tempLeafNo1 = "";
        this.tempLeafNo2 = "";
        
        setTransId("");
    }
    
    /* To set and change the Status of the lable STATUS */
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    public int getResult(){
        return this.result;
    }
    
    /* To set the Value of the lblStatus...*/
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    public String getLblStatus(){
        return lblStatus;
    }
    
    /* To reset the Value of lblStatus after each save action...*/
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
    
    
    // Set and the get methods for ComboBox models...
    public void setCbmProdId(ComboBoxModel cbmProdId){
        this.cbmProdId = cbmProdId;
        setChanged();
    }
    
    ComboBoxModel getCbmProdId(){
        return cbmProdId;
    }
    
    public void setCbmMethodOfDelivery(ComboBoxModel cbmMethodOfDelivery){
        this.cbmMethodOfDelivery = cbmMethodOfDelivery;
        setChanged();
    }
    
    ComboBoxModel getCbmMethodOfDelivery(){
        return cbmMethodOfDelivery;
    }
    
    public void setCbmProductId(ComboBoxModel cbmProductId){
        this.cbmProductId = cbmProductId;
        setChanged();
    }
    
    ComboBoxModel getCbmProductId(){
        return cbmProductId;
    }
    
    public void setCbmProductID(ComboBoxModel cbmProductID){
        this.cbmProductID = cbmProductID;
        setChanged();
    }
    
    ComboBoxModel getCbmProductID(){
        return cbmProductID;
    }
    
    public void setCbmReasonStopPayment(ComboBoxModel cbmReasonStopPayment){
        this.cbmReasonStopPayment = cbmReasonStopPayment;
        setChanged();
    }
    
    ComboBoxModel getCbmReasonStopPayment(){
        return cbmReasonStopPayment;
    }
    
    private String cboProdId = "";
    private String txtAccountNo = "";
    private String txtNamesOfAccount = "";
    private String cboMethodOfDelivery = "";
    private String cboNoOfLeaves = "";
    private String txtNoOfChequeBooks = "";
    private String txtChargesCollected = "";
    private String txtRemarks = "";
    private String txtSeriesFrom = "";
    private String txtSeriesNoTo = "";
    private String txtStartingCheque = "";
    private String txtStartingChequeNo = "";
    private String txtEndingcheque = "";
    private String txtEndingchequeNo = "";
    private String cboProductId = "";
    private String txtAccNo = "";
    private String txtStartCheque = "";
    private String txtStartchequeNo = "";
    private String txtEndCheque = "";
    private String txtEndingChequeNo = "";
    private boolean rdoLeaf_single = false;
    private boolean rdoLeaf_Bulk = false;
    private String tdtChequeDate = "";
    private String txtChequeAmt = "";
    private String txtPayeeName = "";
    private String txtStopPaymentCharges = "";
    private String cboReasonStopPayment = "";
    private String cboProductID = "";
    private String txtAccounNumber = "";
    private String txtLeafNo1 = "";
    private String txtLeafNo2 = "";
    private String txtRemark = "";
    private String cboUsage = "";
    
    private String cboProdType = "";
    private String cboProductType = "";
    private String cboProduct_Type = "";
    
        private String ecsStopId = "";
	private String ecsStopDt = "";
	private String cboEcsProdId = "";
	private String ecsAcctNo = "";
	private String ecsEndChqNo1 = "";
	private String ecsEndChqNo2 = "";
	private String ecsPayeeName = "";
	private String ecsAmt = "";
	private String ecsStopPayChrg = "";
	private String ecsStopPayReason = "";
	private String ecsStopStatus = "";
        private String cboEcsProdType="";
        private String cboEcsReasonStopPayment = "";
        private String  ecsDt="";

    
    void setCboProdId(String cboProdId){
        this.cboProdId = cboProdId;
        setChanged();
    }
    String getCboProdId(){
        return this.cboProdId;
    }
    
    void setTxtAccountNo(String txtAccountNo){
        this.txtAccountNo = txtAccountNo;
        setChanged();
    }
    String getTxtAccountNo(){
        return this.txtAccountNo;
    }
    
    void setTxtNamesOfAccount(String txtNamesOfAccount){
        this.txtNamesOfAccount = txtNamesOfAccount;
        setChanged();
    }
    String getTxtNamesOfAccount(){
        return this.txtNamesOfAccount;
    }
    
    void setCboMethodOfDelivery(String cboMethodOfDelivery){
        this.cboMethodOfDelivery = cboMethodOfDelivery;
        setChanged();
    }
    String getCboMethodOfDelivery(){
        return this.cboMethodOfDelivery;
    }
    
    void setCboNoOfLeaves(String cboNoOfLeaves){
        this.cboNoOfLeaves = cboNoOfLeaves;
        setChanged();
    }
    String getCboNoOfLeaves(){
        return this.cboNoOfLeaves;
    }
    
    ComboBoxModel getCbmNoOfLeaves(){
        return cbmNoOfLeaves;
    }
    
    void setTxtNoOfChequeBooks(String txtNoOfChequeBooks){
        this.txtNoOfChequeBooks = txtNoOfChequeBooks;
        setChanged();
    }
    String getTxtNoOfChequeBooks(){
        return this.txtNoOfChequeBooks;
    }
    
    void setTxtChargesCollected(String txtChargesCollected){
        this.txtChargesCollected = txtChargesCollected;
        setChanged();
    }
    String getTxtChargesCollected(){
        return this.txtChargesCollected;
    }
    
    void setTxtRemarks(String txtRemarks){
        this.txtRemarks = txtRemarks;
        setChanged();
    }
    String getTxtRemarks(){
        return this.txtRemarks;
    }
    
    void setTxtSeriesFrom(String txtSeriesFrom){
        this.txtSeriesFrom = txtSeriesFrom;
        setChanged();
    }
    String getTxtSeriesFrom(){
        return this.txtSeriesFrom;
    }
    
    void setTxtSeriesNoTo(String txtSeriesNoTo){
        this.txtSeriesNoTo = txtSeriesNoTo;
        setChanged();
    }
    String getTxtSeriesNoTo(){
        return this.txtSeriesNoTo;
    }
    
    void setTxtStartingCheque(String txtStartingCheque){
        this.txtStartingCheque = txtStartingCheque;
        setChanged();
    }
    String getTxtStartingCheque(){
        return this.txtStartingCheque;
    }
    
    void setTxtStartingChequeNo(String txtStartingChequeNo){
        this.txtStartingChequeNo = txtStartingChequeNo;
        setChanged();
    }
    String getTxtStartingChequeNo(){
        return this.txtStartingChequeNo;
    }
    
    void setTxtEndingcheque(String txtEndingcheque){
        this.txtEndingcheque = txtEndingcheque;
        setChanged();
    }
    String getTxtEndingcheque(){
        return this.txtEndingcheque;
    }
    
    void setTxtEndingchequeNo(String txtEndingchequeNo){
        this.txtEndingchequeNo = txtEndingchequeNo;
        setChanged();
    }
    String getTxtEndingchequeNo(){
        return this.txtEndingchequeNo;
    }
    
    void setCboProductId(String cboProductId){
        this.cboProductId = cboProductId;
        setChanged();
    }
    String getCboProductId(){
        return this.cboProductId;
    }
    
    void setTxtAccNo(String txtAccNo){
        this.txtAccNo = txtAccNo;
        setChanged();
    }
    String getTxtAccNo(){
        return this.txtAccNo;
    }
    
    void setTxtStartCheque(String txtStartCheque){
        this.txtStartCheque = txtStartCheque;
        setChanged();
    }
    String getTxtStartCheque(){
        return this.txtStartCheque;
    }
    
    void setTxtStartchequeNo(String txtStartchequeNo){
        this.txtStartchequeNo = txtStartchequeNo;
        setChanged();
    }
    String getTxtStartchequeNo(){
        return this.txtStartchequeNo;
    }
    
    void setTxtEndCheque(String txtEndCheque){
        this.txtEndCheque = txtEndCheque;
        setChanged();
    }
    String getTxtEndCheque(){
        return this.txtEndCheque;
    }
    
    void setTxtEndingChequeNo(String txtEndingChequeNo){
        this.txtEndingChequeNo = txtEndingChequeNo;
        setChanged();
    }
    String getTxtEndingChequeNo(){
        return this.txtEndingChequeNo;
    }
    
    void setRdoLeaf_single(boolean rdoLeaf_single){
        this.rdoLeaf_single = rdoLeaf_single;
        setChanged();
    }
    boolean getRdoLeaf_single(){
        return this.rdoLeaf_single;
    }
    
    void setRdoLeaf_Bulk(boolean rdoLeaf_Bulk){
        this.rdoLeaf_Bulk = rdoLeaf_Bulk;
        setChanged();
    }
    boolean getRdoLeaf_Bulk(){
        return this.rdoLeaf_Bulk;
    }
    
    void setTdtChequeDate(String tdtChequeDate){
        this.tdtChequeDate = tdtChequeDate;
        setChanged();
    }
    String getTdtChequeDate(){
        return this.tdtChequeDate;
    }
    
    void setTxtChequeAmt(String txtChequeAmt){
        this.txtChequeAmt = txtChequeAmt;
        setChanged();
    }
    String getTxtChequeAmt(){
        return this.txtChequeAmt;
    }
    
    void setTxtPayeeName(String txtPayeeName){
        this.txtPayeeName = txtPayeeName;
        setChanged();
    }
    String getTxtPayeeName(){
        return this.txtPayeeName;
    }
    
    void setTxtStopPaymentCharges(String txtStopPaymentCharges){
        this.txtStopPaymentCharges = txtStopPaymentCharges;
        setChanged();
    }
    String getTxtStopPaymentCharges(){
        return this.txtStopPaymentCharges;
    }
    
    void setCboReasonStopPayment(String cboReasonStopPayment){
        this.cboReasonStopPayment = cboReasonStopPayment;
        setChanged();
    }
    String getCboReasonStopPayment(){
        return this.cboReasonStopPayment;
    }
    
    void setCboProductID(String cboProductID){
        this.cboProductID = cboProductID;
        setChanged();
    }
    String getCboProductID(){
        return this.cboProductID;
    }
    
    void setTxtAccounNumber(String txtAccounNumber){
        this.txtAccounNumber = txtAccounNumber;
        setChanged();
    }
    String getTxtAccounNumber(){
        return this.txtAccounNumber;
    }
    
    void setTxtLeafNo1(String txtLeafNo1){
        this.txtLeafNo1 = txtLeafNo1;
        setChanged();
    }
    String getTxtLeafNo1(){
        return this.txtLeafNo1;
    }
    
    void setTxtLeafNo2(String txtLeafNo2){
        this.txtLeafNo2 = txtLeafNo2;
        setChanged();
    }
    String getTxtLeafNo2(){
        return this.txtLeafNo2;
    }
    
    void setTxtRemark(String txtRemark){
        this.txtRemark = txtRemark;
        setChanged();
    }
    String getTxtRemark(){
        return this.txtRemark;
    }
    
    /* SET AND GET METHODS FOR CHEQUE-ISSUE ID...*/
    void setChequeIssueId(String chqIssueId){
        this.chqIssueId = chqIssueId;
        setChanged();
    }
    String getChequeIssueId(){
        return this.chqIssueId;
    }
    
    /* SET AND GET METHODS FOR CHEQUE-STOP ID...*/
    void setChequeStopId(String chqStopId){
        this.chqStopId = chqStopId;
        setChanged();
    }
    String getChequeStopId(){
        return this.chqStopId;
    }
    
    /* SET AND GET METHODS FOR CHEQUE-LEAF ID...*/
    void setChequeLeafId(String chqLeafId){
        this.chqLeafId = chqLeafId;
        setChanged();
    }
    String getChequeLeafId(){
        return this.chqLeafId;
    }
    
    /* Set and GET METHODS FOR THE tABLE...*/
    void setTblCheque(EnhancedTableModel tblChequeTab){
        this.tblChequeTab = tblChequeTab;
        setChanged();
    }
    
    EnhancedTableModel getTblCheque(){
        return this.tblChequeTab;
    }
    
    /* To  set the value of Account Head in UI...*/
    public void setLblAccountHeadProdId(String lblAccountHeadProdId){
        this.lblAccountHeadProdId = lblAccountHeadProdId;
        setChanged();
    }
    public String getLblAccountHeadProdId(){
        return this.lblAccountHeadProdId;
    }
    
    public void setLblAccountHeadDesc(String lblAccountHeadDesc){
        this.lblAccountHeadDesc = lblAccountHeadDesc;
        setChanged();
    }
    public String getLblAccountHeadDesc(){
        return this.lblAccountHeadDesc;
    }
    
    public void setLblProductIdAccHdId(String lblProductIdAccHdId){
        this.lblProductIdAccHdId = lblProductIdAccHdId;
        setChanged();
    }
    public String getLblProductIdAccHdId(){
        return this.lblProductIdAccHdId;
    }
    
    public void setLblProductIdAccHdDesc(String lblProductIdAccHdDesc){
        this.lblProductIdAccHdDesc = lblProductIdAccHdDesc;
        setChanged();
    }
    public String getLblProductIdAccHdDesc(){
        return this.lblProductIdAccHdDesc;
    }
    
    public void setLblProductIDAccHdId(String lblProductIDAccHdId){
        this.lblProductIDAccHdId = lblProductIDAccHdId;
        setChanged();
    }
    public String getLblProductIDAccHdId(){
        return this.lblProductIDAccHdId;
    }
    
    public void setLblProductIDAccHdDesc(String lblProductIDAccHdDesc){
        this.lblProductIDAccHdDesc = lblProductIDAccHdDesc;
        setChanged();
    }
    public String getLblProductIDAccHdDesc(){
        return this.lblProductIDAccHdDesc;
    }
    
    
    public HashMap setAccountHead(String PRODTYPE, String PRODID) {
        HashMap resultMap = new HashMap();
        try {
            final HashMap accountHeadMap = new HashMap();
            accountHeadMap.put("PROD_ID",PRODID);
            final List resultList = ClientUtil.executeQuery("getAccountHeadProd" + PRODTYPE, accountHeadMap);
            resultMap = (HashMap)resultList.get(0);
        }catch(Exception e){
        }
        return resultMap;
    }
    
    /* To set the value of the Customer-Id, Clear-Balance, Total-Balance...*/
    /*  CHEQUE BOOK ISSUE...*/
    public void setLblCuctId(String lblCustId){
        this.lblCustId = lblCustId;
        setChanged();
    }
    public String getLblCuctId(){
        return this.lblCustId;
    }
    
    /*  STOP PAYMENT ISSUE...*/
    public void setLblCustStopId(String lblCustStopId){
        this.lblCustStopId = lblCustStopId;
        setChanged();
    }
    public String getLblCustStopId(){
        return this.lblCustStopId;
    }
    
    /* LOOSE LEAF ISSUE...*/
    public void setLblCustLooseId(String lblCustLooseId){
        this.lblCustLooseId = lblCustLooseId;
        setChanged();
    }
    public String getLblCustLooseId(){
        return this.lblCustLooseId;
    }
    
    //    public void setLblClearBalance(String lblClearBalance){
    //        this.lblClearBalance = lblClearBalance;
    //        setChanged();
    //    }
    //    public String getLblClearBalance(){
    //        return this.lblClearBalance;
    //    }
    
    //    public void setLblTotalBalance(String lblTotalBalance){
    //        this.lblTotalBalance = lblTotalBalance;
    //        setChanged();
    //    }
    //    public String getLblTotalBalance(){
    //        return this.lblTotalBalance;
    //    }
    
    
    public HashMap setCustomer(String ACCOUNTNO) {
        HashMap resultMap = new HashMap();
        try {
            final HashMap customerMap = new HashMap();
            customerMap.put("ACT_NUM",ACCOUNTNO);
            final List resultList = ClientUtil.executeQuery("getCustomer", customerMap);
            resultMap = (HashMap)resultList.get(0);
        }catch(Exception e){
        }
        return resultMap;
        
    }
    
    /* For setting the Name of the Account Number Holder...*/
    
    public void setLblCustName(String lblCustName){
        this.lblCustName = lblCustName;
        setChanged();
    }
    public String getLblCustName(){
        return this.lblCustName;
    }
    
    /*  STOP PAYMENT ISSUE...*/
    public void setLblCustStopName(String lblCustStopName){
        this.lblCustStopName = lblCustStopName;
        setChanged();
    }
    public String getLblCustStopName(){
        return this.lblCustStopName;
    }
    
    /*  LOOSE LEAF ISSUE...*/
    public void setLblCustLooseName(String lblCustLooseName){
        this.lblCustLooseName = lblCustLooseName;
        setChanged();
    }
    public String getLblCustLooseName(){
        return this.lblCustLooseName;
    }
    
    public HashMap setCustName(String prodType, String AccountNo){
        HashMap resultMap = new HashMap();
        try {
            final HashMap custNameMap = new HashMap();
            custNameMap.put("ACC_NUM",AccountNo);
            final List resultList = ClientUtil.executeQuery("getAccountNumberName" + prodType, custNameMap);
            resultMap = (HashMap)resultList.get(0);
        }catch(Exception e){
        }
        return resultMap;
    }
    
    // To set the value of the Unpaid Cheques, Cheque Returned, Cheques Issued...
    //    public void setLblUnpaidCheques(String lblUnpaidCheques){
    //        this.lblUnpaidCheques = lblUnpaidCheques;
    //        setChanged();
    //    }
    //    public String getLblUnpaidCheques(){
    //        return this.lblUnpaidCheques;
    //    }
    //
    //    public void setLblChequesReturned(String lblChequesReturned){
    //        this.lblChequesReturned = lblChequesReturned;
    //        setChanged();
    //    }
    //    public String getLblChequesReturned(){
    //        return this.lblChequesReturned;
    //    }
    
    //    public void setLblChequesIssued(String lblChequesIssued){
    //        this.lblChequesIssued = lblChequesIssued;
    //        setChanged();
    //    }
    //    public String getLblChequesIssued(){
    //        return this.lblChequesIssued;
    //    }
    
    public void paymentRevoke(){
        HashMap resultMap = new HashMap();
        try {
            final HashMap custNameMap = new HashMap();
            custNameMap.put("CHQ_STOP_ID",chqStopId);
            custNameMap.put("CHQ_STOP_REM",authRemarks);
            Date curDt = (Date) curDate.clone();
            custNameMap.put("CHQ_REVOKE_DT",curDt);
            System.out.println("custNameMap: " +custNameMap);
            ClientUtil.execute("setPaymentRevoke", custNameMap);
        }catch(Exception e){
        }
    }
     public void EcspaymentRevoke(){
        HashMap resultMap = new HashMap();
        try {
            final HashMap custNameMap = new HashMap();
            custNameMap.put("ECS_STOP_ID",ecsStopId);
            custNameMap.put("ECS_STOP_REM",authRemarks);
            Date curDt = (Date) curDate.clone();
            custNameMap.put("ECS_REVOKE_DT",curDt);
            System.out.println("custNameMap: " +custNameMap);
            ClientUtil.execute("setEcsPaymentRevoke", custNameMap);
        }catch(Exception e){
        }
    }
    
//    public String setCharges(String PRODTYPE, String prodId, String actNum){
//        double charges = 0;
//        String chargesApplied = "";
//        try {
//            final HashMap chargesMap = new HashMap();
//            //            double charges = 0;
//            chargesMap.put("PRODID",prodId);
//            chargesMap.put("ACCOUNTNO",actNum);
//            chargesMap.put("CURRENT_DT",curDate.clone());
//            /*
//             * To Deduct the Free Cheques from the Issued Cheque
//             */
//            final List validList = ClientUtil.executeQuery("getFreeChequeValidity" + PRODTYPE, chargesMap);
//            String LEAVES = "";
//            if(validList.size() > 0){
//                final HashMap validMap = (HashMap)validList.get(0);
//                System.out.println("validMap: " +validMap);
//                LEAVES = CommonUtil.convertObjToStr(validMap.get("LEAVES"));
//            }else{
//                LEAVES = String.valueOf(0);
//            }
//            
//            double freeLeaves = CommonUtil.convertObjToDouble(LEAVES).doubleValue();
//            
//            final List resultList = ClientUtil.executeQuery("getChequeIssueCharges" + PRODTYPE, chargesMap);
//            final HashMap resultMap = (HashMap)resultList.get(0);
//            System.out.println("resultMap: " +resultMap);
//            String leaveCharges  = CommonUtil.convertObjToStr(resultMap.get("CHGPERLEAF"));
//            
//            double noOfLeaves =  CommonUtil.convertObjToDouble(txtNoOfChequeBooks).doubleValue() * CommonUtil.convertObjToDouble(cbmNoOfLeaves.getKeyForSelected()).doubleValue();
//            //Double.parseDouble(CommonUtil.convertObjToStr(cbmNoOfLeaves.getKeyForSelected()));
//            
//            if(freeLeaves > 0){
//                System.out.println("Free Leaves  > 0");
//                final List totalList = ClientUtil.executeQuery("getTotalChequeIssue", chargesMap);
//                final HashMap totalMap = (HashMap)totalList.get(0);
//                double chargesCollected = CommonUtil.convertObjToDouble(totalMap.get("CHARGES")).doubleValue();
////                double leavesIssued = CommonUtil.convertObjToDouble(totalMap.get("TOTALLEAVES")).doubleValue();
//                System.out.println("chargesCollected : " + chargesCollected);
//                //__ if Charge Already Applied...
//                if(chargesCollected > 0){
//                    System.out.println("Charges applied...");
//                    charges = CommonUtil.convertObjToDouble(leaveCharges).doubleValue() * noOfLeaves;
//                    
//                }else{
//                    System.out.println("Charges Not applied...");
//                    
//                    String totalLeaves = "";
//                    totalLeaves = CommonUtil.convertObjToStr(totalMap.get("TOTALLEAVES"));
//                    
//                    /* Leaves issued for the particular Account...*/
//                    double leavesIssued = CommonUtil.convertObjToDouble(totalLeaves).doubleValue();//__ Double.parseDouble(totalLeaves);
//                    /* Leaves to be issued...*/
//                    //                double noOfLeaves =  Double.parseDouble(txtNoOfChequeBooks) * Double.parseDouble(CommonUtil.convertObjToStr(cbmNoOfLeaves.getKeyForSelected()));
//                    /* chequeCharges...*/
//                    if((chequeCharges !=  null) && (CommonUtil.convertObjToDouble(chequeCharges).doubleValue() > 0)){
//                        charges = CommonUtil.convertObjToDouble(leaveCharges).doubleValue() * noOfLeaves;
//                        
//                    }else if(((leavesIssued + noOfLeaves) - freeLeaves) > 0 ){
////                        charges = (leavesIssued + noOfLeaves - freeLeaves) * CommonUtil.convertObjToDouble(leaveCharges).doubleValue();
//                        charges = (noOfLeaves - freeLeaves) * CommonUtil.convertObjToDouble(leaveCharges).doubleValue();
//                    }
//                }
//            }else{
//                charges = CommonUtil.convertObjToDouble(leaveCharges).doubleValue() * noOfLeaves;
//            }
//        }catch(Exception e){
//            System.out.println("Error in setCharges()");
//            parseException.logException(e,true);
//        }
//        chargesApplied = String.valueOf(charges);
//        System.out.println("chargesApplied: " + chargesApplied);
//        return chargesApplied;
//    }
    public String setCharges(String PRODTYPE, String prodId, String actNum){
        double charges = 0;
        String chargesApplied = "";
        try {
            final HashMap chargesMap = new HashMap();
            //            double charges = 0;
            chargesMap.put("PRODID",prodId);
            chargesMap.put("ACCOUNTNO",actNum);
            chargesMap.put("CURRENT_DT",curDate);
            /*
             * To Deduct the Free Cheques from the Issued Cheque
             */
            final List validList = ClientUtil.executeQuery("getFreeChequeValidity" + PRODTYPE, chargesMap);
            String LEAVES = "";
            if(validList.size() > 0){
                final HashMap validMap = (HashMap)validList.get(0);
                System.out.println("validMap: " +validMap);
                LEAVES = CommonUtil.convertObjToStr(validMap.get("LEAVES"));
            }else{
                LEAVES = String.valueOf(0);
            }
            
            double freeLeaves = CommonUtil.convertObjToDouble(LEAVES).doubleValue();
            
            final List resultList = ClientUtil.executeQuery("getChequeIssueCharges" + PRODTYPE, chargesMap);
            final HashMap resultMap = (HashMap)resultList.get(0);
            System.out.println("resultMap: " +resultMap);
            String leaveCharges  = CommonUtil.convertObjToStr(resultMap.get("CHGPERLEAF"));
            
            double noOfLeaves =  CommonUtil.convertObjToDouble(txtNoOfChequeBooks).doubleValue() * CommonUtil.convertObjToDouble(cbmNoOfLeaves.getKeyForSelected()).doubleValue();
            //Double.parseDouble(CommonUtil.convertObjToStr(cbmNoOfLeaves.getKeyForSelected()));
            
            if(freeLeaves > 0){
                System.out.println("Free Leaves  > 0");
                final List totalList = ClientUtil.executeQuery("getTotalChequeIssue", chargesMap);
                final HashMap totalMap = (HashMap)totalList.get(0);
                double chargesCollected = CommonUtil.convertObjToDouble(totalMap.get("CHARGES")).doubleValue();
                double leavesIssued = CommonUtil.convertObjToDouble(totalMap.get("TOTALLEAVES")).doubleValue();
                System.out.println("chargesCollected : " + chargesCollected);
                //__ if Charge Already Applied...
                if(leavesIssued  >= freeLeaves){
                    //                        charges = (leavesIssued + noOfLeaves - freeLeaves) * CommonUtil.convertObjToDouble(leaveCharges).doubleValue();
                    charges = CommonUtil.convertObjToDouble(leaveCharges).doubleValue() * noOfLeaves;
                    
                }
                else{
                    double leftFreeLeaves = freeLeaves - leavesIssued;
                    charges = (noOfLeaves - leftFreeLeaves) * CommonUtil.convertObjToDouble(leaveCharges).doubleValue();
                }
                
            }else{
                charges = CommonUtil.convertObjToDouble(leaveCharges).doubleValue() * noOfLeaves;
            }
        }catch(Exception e){
            System.out.println("Error in setCharges()");
            parseException.logException(e,true);
        }
        chargesApplied = String.valueOf(charges);
        System.out.println("chargesApplied: " + chargesApplied);
        return chargesApplied;
    }
    
    public void setStopPaymentCharges(String prodType, String prodId, String actNum){
        try {
            final HashMap stopChargesMap = new HashMap();
            //            String prodType = CommonUtil.convertObjToStr(cbmProductType.getKeyForSelected());
            System.out.println("Stop prodType: " +prodType);
            //            stopChargesMap.put("PRODID",CommonUtil.convertObjToStr(cbmProductId.getKeyForSelected()));
            
            stopChargesMap.put("PRODID",prodId);
            stopChargesMap.put("ACCOUNTNO",actNum);
            
            final List resultList = ClientUtil.executeQuery("getChequeStopCharges" + prodType, stopChargesMap);
            final HashMap resultMap = (HashMap)resultList.get(0);
            String stopCharges = null;
            stopCharges = CommonUtil.convertObjToStr(resultMap.get("STOP_CHRG"));
            double charges = CommonUtil.convertObjToDouble(stopCharges).doubleValue();
            /* Stop Payment charges is per instance basis... (not per leaf base)*/
//            if(getRdoLeaf_Bulk()){
//                charges = (CommonUtil.convertObjToDouble(txtEndingChequeNo).doubleValue()
//                - CommonUtil.convertObjToDouble(txtStartchequeNo).doubleValue() + 1)
//                * CommonUtil.convertObjToDouble(stopCharges).doubleValue();
//            }
            setTxtStopPaymentCharges(String.valueOf(charges));
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /*
     * To Know the no of Cheques issued by the Particular Account No(Holder)
     * in the given financial Year...
     */
    public void chequeIssued(){
        int limitYear;
        int rows = 0;
        Date startDate;
        Date endDate;
        String stringDate = DateUtil.getStringDate(curDate);
        int givenYear = Integer.parseInt(stringDate.substring(stringDate.lastIndexOf("/")+1));
        long diff = DateUtil.getDateMMDDYYYY(stringDate).getTime() - DateUtil.getDateMMDDYYYY("3/31/"+givenYear).getTime();
        if(diff > 0){
            //            startDate = DateUtil.getDateMMDDYYYY("4/01/" + givenYear);
            //            endDate = DateUtil.getDateMMDDYYYY("3/31/" + (givenYear + 1));
            
            startDate = DateUtil.getDate(1,4,givenYear);
            endDate = DateUtil.getDate(31,3, (givenYear + 1));
        }else{
            //            startDate = DateUtil.getDateMMDDYYYY("4/01/" + (givenYear-1));
            //            endDate = DateUtil.getDateMMDDYYYY("3/31/" + givenYear);
            
            startDate = DateUtil.getDate(1,4,(givenYear-1));
            endDate = DateUtil.getDate(31,3,givenYear);
        }
        final HashMap dateMap = new HashMap();
        dateMap.put("ACCTNO",getTxtAccountNo());
        dateMap.put("STARTDATE",startDate);
        dateMap.put("ENDDATE",endDate);
        final List dateList = ClientUtil.executeQuery("getNoOfChequeIssued", dateMap);
        final HashMap countMap = (HashMap)dateList.get(0);//
        rows = CommonUtil.convertObjToInt(countMap.get("COUNT"));
        //        setLblChequesIssued(String.valueOf(rows));
        
    }
    public boolean chkMinBalance(){
        if(getCbmProdType().getKeyForSelected().equals("OA")){
        HashMap dataMap = new HashMap();
        dataMap.put("PROD_ID", CommonUtil.convertObjToStr(cbmProdId.getKeyForSelected()));
        dataMap.put("ACT_NUM", getTxtAccountNo());
        List lst = (List) ClientUtil.executeQuery("chkMinBal", dataMap);
        dataMap = null;
        if(lst != null)
            if(lst.size() > 0){
                return true;
            }
            return false;
        }else{
            return true;
        }
    }
    
    void setCboUsage(String cboUsage){
        this.cboUsage = cboUsage;
        setChanged();
    }
    String getCboUsage(){
        return this.cboUsage;
    }
    
    ComboBoxModel getCbmUsage(){
        return cbmUsage;
    }
    //__ cboProdType, cboProductType, cboProduct_Type
    
    void setCboProdType(String cboProdType){
        this.cboProdType = cboProdType;
        setChanged();
    }
    String getCboProdType(){
        return this.cboProdType;
    }
    
    ComboBoxModel getCbmProdType(){
        return cbmProdType;
    }
    
    void setCboProductType(String cboProductType){
        this.cboProductType = cboProductType;
        setChanged();
    }
    String getCboProductType(){
        return this.cboProductType;
    }
    
    ComboBoxModel getCbmProductType(){
        return cbmProductType;
    }
    
    void setCboProduct_Type(String cboProduct_Type){
        this.cboProduct_Type = cboProduct_Type;
        setChanged();
    }
    String getCboProduct_Type(){
        return this.cboProduct_Type;
    }
    
    ComboBoxModel getCbmProduct_Type(){
        return cbmProduct_Type;
    }
    
    
    /**
     * To get the Value of No Of Leaves, Depending on the Usage
     */
    
    public void getLeavesPerBook(String subItem){
        try {
            lookUpHash = new HashMap();
            
            HashMap whereMap = new HashMap();
            
            whereMap.put("ITEM_SUB_TYPE", subItem);
            whereMap.put(CommonConstants.BRANCH_ID,getSelectedBranchID());
            
            lookUpHash.put(CommonConstants.MAP_NAME,"ChequeIssue.getChequeLeavesPerBook");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, whereMap);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            cbmNoOfLeaves = new ComboBoxModel(key,value);
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e,true);
        }
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
                if(chequeBook == true)
                 cbmProdType.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                if(stopPayment == true)
                    cbmProductType.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                if(looseLeaf == true)
                    cbmProduct_Type.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                if(ecsStopPayment == true)
                    cbmEcsProdType.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                setCboProdId(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                setCboProductID(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                setCboProductId(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                setCboEcsProdId(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                getProductID(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE"))); 
                cbmProdId.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                cbmProductID.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                cbmProductId.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                cbmEcsProdId.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                setAccountHead(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")),CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                isExists = true;
            } else {
//                ArrayList key=new ArrayList();
//                ArrayList value=new ArrayList();
//                key.add("");
//                value.add("");   
                setCboProdId("");
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
    
    //    //__ To set The Values for the Serial Numbers and the Cheque Leaves No...
    //    public HashMap getChequeBookData(int chequeBooks){
    //        HashMap result = new HashMap();
    //        try{
    //            final HashMap chequeDataMap = new HashMap();
    //            chequeDataMap.put("ITEM_SUB_TYPE", (String)cbmUsage.getKeyForSelected());
    //            chequeDataMap.put("LEAVES", (String)cbmNoOfLeaves.getKeyForSelected());
    //            final List transOut = ClientUtil.executeQuery("ChequeIssue.getSerialNoTransOut", chequeDataMap);
    //            if(transOut.size() > 0){
    //                final List transBoth = ClientUtil.executeQuery("ChequeIssue.getSerialNo", chequeDataMap);
    //                if(transBoth.size() > 0){
    //                    HashMap transBothMap = (HashMap)transBoth.get(0);
    //                    HashMap transOutMap = (HashMap)transOut.get(0);
    //
    //                    int bothBookFrom = CommonUtil.convertObjToInt(transBothMap.get("BOOK_SLNO_FROM"));
    //                    int bothBookTo = CommonUtil.convertObjToInt(transBothMap.get("BOOK_SLNO_TO"));
    //
    //                    int outBookTo = CommonUtil.convertObjToInt(transOutMap.get("BOOK_SLNO_TO"));
    //                    setTransId(CommonUtil.convertObjToStr(transBothMap.get("TRANS_ID")));
    //
    //                    /*
    //                     * If the Current Series, still has some Books to be issued...
    //                     */
    //                    if( (outBookTo >= bothBookFrom) && (outBookTo < bothBookTo)){
    //                        result.put("BOOK_SLNO_FROM",String.valueOf(outBookTo + 1));
    //                        result.put("BOOK_SLNO_TO",CommonUtil.convertObjToStr(transBothMap.get("BOOK_SLNO_TO")));
    //                        int leavesFrom = CommonUtil.convertObjToInt(transOutMap.get("LEAVES_SLNO_FROM"));
    //                        result.put("LEAVES_SLNO_FROM",String.valueOf(leavesFrom+1));
    //                    }else{
    //                        result.put("BOOK_SLNO_FROM",CommonUtil.convertObjToStr(transBothMap.get("BOOK_SLNO_FROM")));
    //                        result.put("BOOK_SLNO_TO",CommonUtil.convertObjToStr(transBothMap.get("BOOK_SLNO_TO")));
    //                        result.put("LEAVES_SLNO_FROM",CommonUtil.convertObjToStr(transBothMap.get("LEAVES_SLNO_FROM")));
    //                    }
    //                    result.put("INSTRUMENT_PREFIX",CommonUtil.convertObjToStr(transBothMap.get("INSTRUMENT_PREFIX")));
    //                }
    //            }else{
    //                final List transIn = ClientUtil.executeQuery("ChequeIssue.getSerialNoTransIn", chequeDataMap);
    //                if(transIn.size() > 0){
    //                    result = (HashMap)transIn.get(0);
    //                    System.out.println("Trans Id: "+ CommonUtil.convertObjToStr(result.get("TRANS_ID")));
    //                    setTransId(CommonUtil.convertObjToStr(result.get("TRANS_ID")));
    //                }
    //            }
    //        }catch(Exception e){
    //            e.printStackTrace();
    //            parseException.logException(e,true);
    //        }
    //
    //        return result;
    //    }
    
    
    //__ To set The Values for the Serial Numbers and the Cheque Leaves No...
    //    public HashMap getChequeBookData(String usage, String leaves){
    
    //        HashMap result = new HashMap();
    //        //        int iterator = 0;
    //        try{
    //            final HashMap chequeDataMap = new HashMap();
    //            chequeDataMap.put("ITEM_SUB_TYPE", usage);
    //            chequeDataMap.put("LEAVES", leaves);
    //            chequeDataMap.put(CommonConstants.BRANCH_ID,getSelectedBranchID());
    //
    //            final List transOut = ClientUtil.executeQuery("ChequeIssue.getSerialNoTransOut", chequeDataMap);
    //            if(transOut.size() > 0){
    //                final List transBoth = ClientUtil.executeQuery("ChequeIssue.getSerialNo", chequeDataMap);
    //                int size = transBoth.size();
    //                if(size > 0){
    //                    //                    for(int i=0; i< size; i++){
    //                    HashMap transBothMap = (HashMap)transBoth.get(0);
    //                    System.out.println("Data Map : " + transBothMap);
    //
    //                    HashMap transOutMap = (HashMap)transOut.get(0);
    //
    //                    long bothBookFrom = CommonUtil.convertObjToLong(transBothMap.get("BOOK_SLNO_FROM"));
    //                    long bothBookTo = CommonUtil.convertObjToLong(transBothMap.get("BOOK_SLNO_TO"));
    //
    //                    long outBookTo = CommonUtil.convertObjToLong(transOutMap.get("BOOK_SLNO_TO"));
    //                    long outLeavesFrom = CommonUtil.convertObjToLong(transOutMap.get("LEAVES_SLNO_FROM"));
    //                    setTransId(CommonUtil.convertObjToStr(transBothMap.get("TRANS_ID")));
    //
    //                    System.out.println("bothBookFrom: "+ bothBookFrom);
    //                    System.out.println("bothBookTo: "+ bothBookTo);
    //                    System.out.println("outBookTo: "+ outBookTo);
    //
    //                    //__ Checking is already done in the Query...
    //                    //__ If the Current Series, still has some Books to be issued...
    //
    //                    //__ if the current Series is already in use...
    //                    if( (outBookTo >= bothBookFrom) && (outBookTo <= bothBookTo)){
    //                        result.put("BOOK_SLNO_FROM",String.valueOf(outBookTo + 1));
    //                        result.put("LEAVES_SLNO_FROM",String.valueOf(outLeavesFrom+1));
    //
    //                    }else{
    //                        result.put("BOOK_SLNO_FROM",String.valueOf(bothBookFrom));
    //                        result.put("LEAVES_SLNO_FROM",String.valueOf(transBothMap.get("LEAVES_SLNO_FROM")));
    //                    }
    //
    //                    result.put("BOOK_SLNO_TO",CommonUtil.convertObjToStr(transBothMap.get("BOOK_SLNO_TO")));
    //                    result.put("INSTRUMENT_PREFIX",CommonUtil.convertObjToStr(transBothMap.get("INSTRUMENT_PREFIX")));
    //                    result.put("TRANS_ID", CommonUtil.convertObjToStr(transBothMap.get("TRANS_ID")));
    //                    //                            break;
    //                    //                        }
    //                    //                    }
    //
    //
    //                    //                    else{
    //                    //                        System.out.println("True Case...");
    //                    //                        iterator++;
    //                    //                        if(iterator <= size){
    //                    //                            transBothMap = (HashMap)transBoth.get(iterator);
    //                    //
    //                    //                            result.put("BOOK_SLNO_FROM",CommonUtil.convertObjToStr(transBothMap.get("BOOK_SLNO_FROM")));
    //                    //                            result.put("BOOK_SLNO_TO",CommonUtil.convertObjToStr(transBothMap.get("BOOK_SLNO_TO")));
    //                    //                            result.put("LEAVES_SLNO_FROM",CommonUtil.convertObjToStr(transBothMap.get("LEAVES_SLNO_FROM")));
    //                    //                        }
    //                    //                    }
    //                    //                    result.put("INSTRUMENT_PREFIX",CommonUtil.convertObjToStr(transBothMap.get("INSTRUMENT_PREFIX")));
    //                }
    //            }else{
    //                final List transIn = ClientUtil.executeQuery("ChequeIssue.getSerialNoTransIn", chequeDataMap);
    //                if(transIn.size() > 0){
    //                    result = (HashMap)transIn.get(0);
    //                    System.out.println("Trans Id: "+ CommonUtil.convertObjToStr(result.get("TRANS_ID")));
    //                    setTransId(CommonUtil.convertObjToStr(result.get("TRANS_ID")));
    //                }
    //            }
    //        }catch(Exception e){
    //            e.printStackTrace();
    //            parseException.logException(e,true);
    //        }
    //
    //        System.out.println("Result in OB: " + result);
    //
    //        return result;
    //    }
    
    
    void setTransId(String transId){
        this.transId = transId;
        setChanged();
    }
    String getTransId(){
        return this.transId;
    }
    
    void setAuthStatus(String authStatus){
        this.authStatus = authStatus;
        setChanged();
    }
    String getAuthStatus(){
        return this.authStatus;
    }
    
    public void setProdTypeTO(String prodTypeTO) {
        this.prodTypeTO = prodTypeTO;
        setChanged();
    }
    public String getProdTypeTO() {
        return this.prodTypeTO;
    }
    
    /*
     * Setter and Getter methods for the Authorization.
     */
    public void setAuthorizeMap(HashMap authMap) {
        authorizeMap = authMap;
    }
    
    public HashMap getAuthorizeMap() {
        return authorizeMap;
    }
    
    
    
    /*public HashMap getAuthData(HashMap dataMap){
        HashMap resultMap = new HashMap();
        final List resultList = ClientUtil.executeQuery("ChequeIssue.getInventoryDetailsData", dataMap);
        if(resultList.size() > 0){
            resultMap = (HashMap)resultList.get(0);
        }
        return resultMap;
    }*/
    
    public void getProductID(String prodType){
        try {
            log.info("getProductID()");
            lookUpHash.put(CommonConstants.MAP_NAME, "InwardClearing.getProductData"+prodType);
            lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            cbmProdId = new ComboBoxModel(key,value);
            cbmProductId = new ComboBoxModel(key,value);
            cbmProductID = new ComboBoxModel(key,value);
            cbmEcsProdId =  new ComboBoxModel(key,value);
        }catch(Exception e){
            e.printStackTrace();
            log.info("Error in getProductData()");
        }
    }
    
    
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Getter for property authRemarks.
     * @return Value of property authRemarks.
     */
    public java.lang.String getAuthRemarks() {
        return authRemarks;
    }    
   
    /**
     * Setter for property authRemarks.
     * @param authRemarks New value of property authRemarks.
     */
    public void setAuthRemarks(java.lang.String authRemarks) {
        this.authRemarks = authRemarks;
    }    
    
    /**
     * Getter for property stopStat.
     * @return Value of property stopStat.
     */
    public java.lang.String getStopStat() {
        return stopStat;
    }
    
    /**
     * Setter for property stopStat.
     * @param stopStat New value of property stopStat.
     */
    public void setStopStat(java.lang.String stopStat) {
        this.stopStat = stopStat;
    }
    
    /**
     * Getter for property lblChqRevokeDtVal.
     * @return Value of property lblChqRevokeDtVal.
     */
    public java.lang.String getLblChqRevokeDtVal() {
        return lblChqRevokeDtVal;
    }
    
    /**
     * Setter for property lblChqRevokeDtVal.
     * @param lblChqRevokeDtVal New value of property lblChqRevokeDtVal.
     */
    public void setLblChqRevokeDtVal(java.lang.String lblChqRevokeDtVal) {
        this.lblChqRevokeDtVal = lblChqRevokeDtVal;
    }
    
    /**
     * Getter for property lblChqStopDtVal.
     * @return Value of property lblChqStopDtVal.
     */
    public java.lang.String getLblChqStopDtVal() {
        return lblChqStopDtVal;
    }
    
    /**
     * Setter for property lblChqStopDtVal.
     * @param lblChqStopDtVal New value of property lblChqStopDtVal.
     */
    public void setLblChqStopDtVal(java.lang.String lblChqStopDtVal) {
        this.lblChqStopDtVal = lblChqStopDtVal;
    }
    
    /**
     * Getter for property cbmEcsProdType.
     * @return Value of property cbmEcsProdType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmEcsProdType() {
        return cbmEcsProdType;
    }
    
    /**
     * Setter for property cbmEcsProdType.
     * @param cbmEcsProdType New value of property cbmEcsProdType.
     */
    public void setCbmEcsProdType(com.see.truetransact.clientutil.ComboBoxModel cbmEcsProdType) {
        this.cbmEcsProdType = cbmEcsProdType;
    }
    
    /**
     * Getter for property cbmEcsProdId.
     * @return Value of property cbmEcsProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmEcsProdId() {
        return cbmEcsProdId;
    }
    
    /**
     * Setter for property cbmEcsProdId.
     * @param cbmEcsProdId New value of property cbmEcsProdId.
     */
    public void setCbmEcsProdId(com.see.truetransact.clientutil.ComboBoxModel cbmEcsProdId) {
        this.cbmEcsProdId = cbmEcsProdId;
    }
    
    /**
     * Getter for property cbmEcsReasonForStopPayment.
     * @return Value of property cbmEcsReasonForStopPayment.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmEcsReasonForStopPayment() {
        return cbmEcsReasonForStopPayment;
    }
    
    /**
     * Setter for property cbmEcsReasonForStopPayment.
     * @param cbmEcsReasonForStopPayment New value of property cbmEcsReasonForStopPayment.
     */
    public void setCbmEcsReasonForStopPayment(com.see.truetransact.clientutil.ComboBoxModel cbmEcsReasonForStopPayment) {
        this.cbmEcsReasonForStopPayment = cbmEcsReasonForStopPayment;
    }
    
    /**
     * Getter for property ecsStopId.
     * @return Value of property ecsStopId.
     */
    public java.lang.String getEcsStopId() {
        return ecsStopId;
    }
    
    /**
     * Setter for property ecsStopId.
     * @param ecsStopId New value of property ecsStopId.
     */
    public void setEcsStopId(java.lang.String ecsStopId) {
        this.ecsStopId = ecsStopId;
    }
    
    /**
     * Getter for property ecsStopDt.
     * @return Value of property ecsStopDt.
     */
    public java.lang.String getEcsStopDt() {
        return ecsStopDt;
    }
    
    /**
     * Setter for property ecsStopDt.
     * @param ecsStopDt New value of property ecsStopDt.
     */
    public void setEcsStopDt(java.lang.String ecsStopDt) {
        this.ecsStopDt = ecsStopDt;
    }
    
    /**
     * Getter for property cboEcsProdId.
     * @return Value of property cboEcsProdId.
     */
    public java.lang.String getCboEcsProdId() {
        return cboEcsProdId;
    }
    
    /**
     * Setter for property cboEcsProdId.
     * @param cboEcsProdId New value of property cboEcsProdId.
     */
    public void setCboEcsProdId(java.lang.String cboEcsProdId) {
        this.cboEcsProdId = cboEcsProdId;
    }
    
    /**
     * Getter for property ecsAcctNo.
     * @return Value of property ecsAcctNo.
     */
    public java.lang.String getEcsAcctNo() {
        return ecsAcctNo;
    }
    
    /**
     * Setter for property ecsAcctNo.
     * @param ecsAcctNo New value of property ecsAcctNo.
     */
    public void setEcsAcctNo(java.lang.String ecsAcctNo) {
        this.ecsAcctNo = ecsAcctNo;
    }
    
    /**
     * Getter for property ecsEndChqNo1.
     * @return Value of property ecsEndChqNo1.
     */
    public java.lang.String getEcsEndChqNo1() {
        return ecsEndChqNo1;
    }
    
    /**
     * Setter for property ecsEndChqNo1.
     * @param ecsEndChqNo1 New value of property ecsEndChqNo1.
     */
    public void setEcsEndChqNo1(java.lang.String ecsEndChqNo1) {
        this.ecsEndChqNo1 = ecsEndChqNo1;
    }
    
    /**
     * Getter for property ecsEndChqNo2.
     * @return Value of property ecsEndChqNo2.
     */
    public java.lang.String getEcsEndChqNo2() {
        return ecsEndChqNo2;
    }
    
    /**
     * Setter for property ecsEndChqNo2.
     * @param ecsEndChqNo2 New value of property ecsEndChqNo2.
     */
    public void setEcsEndChqNo2(java.lang.String ecsEndChqNo2) {
        this.ecsEndChqNo2 = ecsEndChqNo2;
    }
    
    /**
     * Getter for property ecsPayeeName.
     * @return Value of property ecsPayeeName.
     */
    public java.lang.String getEcsPayeeName() {
        return ecsPayeeName;
    }
    
    /**
     * Setter for property ecsPayeeName.
     * @param ecsPayeeName New value of property ecsPayeeName.
     */
    public void setEcsPayeeName(java.lang.String ecsPayeeName) {
        this.ecsPayeeName = ecsPayeeName;
    }
    
    /**
     * Getter for property ecsAmt.
     * @return Value of property ecsAmt.
     */
    public java.lang.String getEcsAmt() {
        return ecsAmt;
    }
    
    /**
     * Setter for property ecsAmt.
     * @param ecsAmt New value of property ecsAmt.
     */
    public void setEcsAmt(java.lang.String ecsAmt) {
        this.ecsAmt = ecsAmt;
    }
    
    /**
     * Getter for property ecsStopPayChrg.
     * @return Value of property ecsStopPayChrg.
     */
    public java.lang.String getEcsStopPayChrg() {
        return ecsStopPayChrg;
    }
    
    /**
     * Setter for property ecsStopPayChrg.
     * @param ecsStopPayChrg New value of property ecsStopPayChrg.
     */
    public void setEcsStopPayChrg(java.lang.String ecsStopPayChrg) {
        this.ecsStopPayChrg = ecsStopPayChrg;
    }
    
    /**
     * Getter for property ecsStopPayReason.
     * @return Value of property ecsStopPayReason.
     */
    public java.lang.String getEcsStopPayReason() {
        return ecsStopPayReason;
    }
    
    /**
     * Setter for property ecsStopPayReason.
     * @param ecsStopPayReason New value of property ecsStopPayReason.
     */
    public void setEcsStopPayReason(java.lang.String ecsStopPayReason) {
        this.ecsStopPayReason = ecsStopPayReason;
    }
    
    /**
     * Getter for property ecsStopStatus.
     * @return Value of property ecsStopStatus.
     */
    public java.lang.String getEcsStopStatus() {
        return ecsStopStatus;
    }
    
    /**
     * Setter for property ecsStopStatus.
     * @param ecsStopStatus New value of property ecsStopStatus.
     */
    public void setEcsStopStatus(java.lang.String ecsStopStatus) {
        this.ecsStopStatus = ecsStopStatus;
    }
    
    /**
     * Getter for property cboEcsProdType.
     * @return Value of property cboEcsProdType.
     */
    public java.lang.String getCboEcsProdType() {
        return cboEcsProdType;
    }
    
    /**
     * Setter for property cboEcsProdType.
     * @param cboEcsProdType New value of property cboEcsProdType.
     */
    public void setCboEcsProdType(java.lang.String cboEcsProdType) {
        this.cboEcsProdType = cboEcsProdType;
    }
    
    /**
     * Getter for property cboEcsReasonStopPayment.
     * @return Value of property cboEcsReasonStopPayment.
     */
    public java.lang.String getCboEcsReasonStopPayment() {
        return cboEcsReasonStopPayment;
    }
    
    /**
     * Setter for property cboEcsReasonStopPayment.
     * @param cboEcsReasonStopPayment New value of property cboEcsReasonStopPayment.
     */
    public void setCboEcsReasonStopPayment(java.lang.String cboEcsReasonStopPayment) {
        this.cboEcsReasonStopPayment = cboEcsReasonStopPayment;
    }
    
    /**
     * Getter for property ecsDt.
     * @return Value of property ecsDt.
     */
    public java.lang.String getEcsDt() {
        return ecsDt;
    }
    
    /**
     * Setter for property ecsDt.
     * @param ecsDt New value of property ecsDt.
     */
    public void setEcsDt(java.lang.String ecsDt) {
        this.ecsDt = ecsDt;
    }
    
    /**
     * Getter for property lblCustEcsStopName.
     * @return Value of property lblCustEcsStopName.
     */
    public java.lang.String getLblCustEcsStopName() {
        return lblCustEcsStopName;
    }
    
    /**
     * Setter for property lblCustEcsStopName.
     * @param lblCustEcsStopName New value of property lblCustEcsStopName.
     */
    public void setLblCustEcsStopName(java.lang.String lblCustEcsStopName) {
        this.lblCustEcsStopName = lblCustEcsStopName;
    }
    
    /**
     * Getter for property lblCustEcsStopId.
     * @return Value of property lblCustEcsStopId.
     */
    public java.lang.String getLblCustEcsStopId() {
        return lblCustEcsStopId;
    }
    
    /**
     * Setter for property lblCustEcsStopId.
     * @param lblCustEcsStopId New value of property lblCustEcsStopId.
     */
    public void setLblCustEcsStopId(java.lang.String lblCustEcsStopId) {
        this.lblCustEcsStopId = lblCustEcsStopId;
    }
    
    /**
     * Getter for property lblEcsRevokeDtVal.
     * @return Value of property lblEcsRevokeDtVal.
     */
    public java.lang.String getLblEcsRevokeDtVal() {
        return lblEcsRevokeDtVal;
    }
    
    /**
     * Setter for property lblEcsRevokeDtVal.
     * @param lblEcsRevokeDtVal New value of property lblEcsRevokeDtVal.
     */
    public void setLblEcsRevokeDtVal(java.lang.String lblEcsRevokeDtVal) {
        this.lblEcsRevokeDtVal = lblEcsRevokeDtVal;
    }
    
}

