/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MDSMemberReceiptEntryOB.java
 *
 * Created on January 7, 2004, 5:14 PM
 */

package com.see.truetransact.ui.mdsapplication.mdsmemberreceiptentry;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.mdsapplication.mdsreceiptentry.MDSReceiptEntryTO;
import java.util.Date;
import java.util.Iterator;

public class MDSMemberReceiptEntryOB extends CObservable {
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    java.util.ResourceBundle objRemittanceProductRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.mdsapplication.mdsmemberreceiptentry.MDSMemberReceiptEntryRB", ProxyParameters.LANGUAGE);
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private HashMap map;
    private int result;
    private int actionType;
    private ProxyFactory proxy;
    private Date currDate = null;
    private String txtMembershipNo = "";
    private String lblMemberName = "";
    private String lblMemberType = "";
    private String txtTotInstAmt = "";
    private String txtTotPenalAmt = "";
    private String txtTotBonusAmt = "";
    private String txtTotDiscountAmt = "";
    private String txtTotInterest = "";
    private String txtNoticeAmt = "";
    private String txtAribitrationAmt = "";
    private String txtTotalNetAmount = "";
    private String memberTransId = "";
    List bufferList=new ArrayList();
    private HashMap MdsSplitMap = new HashMap();
    private Iterator splitIterator;
    private MDSReceiptEntryTO splitMDSReceiptEntryTO = null;
    ArrayList splitMDSReceiptEntryLst = new ArrayList();
    HashMap sliptMap =  new HashMap();
    private String isSplitMDSTransaction = "";
    
    public HashMap getMdsSplitMap() {
        return MdsSplitMap;
    }

    public void setMdsSplitMap(HashMap MdsSplitMap) {
        this.MdsSplitMap = MdsSplitMap;
    }

    public List getBufferList() {
        return bufferList;
    }

    public void setBufferList(List bufferList) {
        this.bufferList = bufferList;
    }
    
    private EnhancedTableModel tblMemberRecord;
    private HashMap oldTransDetMap = new HashMap();
    private TransactionOB transactionOB = null;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    HashMap finalMap = new HashMap();
    private HashMap _authorizeMap;
    
    private static MDSMemberReceiptEntryOB MDSMemberReceiptEntryOB;
    static {
        try {
            MDSMemberReceiptEntryOB = new MDSMemberReceiptEntryOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    public MDSMemberReceiptEntryOB() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "MDSMemberReceiptEntryJNDI");
        map.put(CommonConstants.HOME, "mdsapplication.mdsmemberreceiptentry.MDSMemberReceiptEntryHome");
        map.put(CommonConstants.REMOTE, "mdsapplication.mdsmemberreceiptentry.MDSMemberReceiptEntry");
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        notifyObservers();
        currDate = ClientUtil.getCurrentDate();
    }
    
    public static MDSMemberReceiptEntryOB getInstance() {
        return MDSMemberReceiptEntryOB;
    }
    
     private String getCommand(){
        String command = null;
        System.out.println("actionType : " + actionType);
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
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                command = CommonConstants.STATUS_AUTHORIZED;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                command = CommonConstants.STATUS_REJECTED;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                command = CommonConstants.STATUS_EXCEPTION;
            default:
        }
        // System.out.println("command : " + command);
        return command;
    }
    
    private String getAction(){
        String action = null;
        // System.out.println("actionType : " + actionType);
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
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                action = CommonConstants.STATUS_AUTHORIZED;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                action = CommonConstants.STATUS_REJECTED;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                action = CommonConstants.STATUS_EXCEPTION;
                break;
            default:
        }
        // System.out.println("command : " + command);
        return action;
    }
    
    
    public void getTransDetails(HashMap whereMap) {
        final HashMap mapData;
        try {
            mapData = proxy.executeQuery(whereMap,map);
            System.out.println("@#$@#$@#$mapData:"+mapData);
            if(mapData.containsKey("TRANSACTION_LIST")){
                List list = (List) mapData.get("TRANSACTION_LIST");
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
            }
        } catch( Exception e ) {
            System.out.println("Error In populateData()");
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            doActionPerform();
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final HashMap data = new HashMap();
        data.put("COMMAND",getCommand());
        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || getActionType() == ClientConstants.ACTIONTYPE_REJECT){
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
            data.put("MEMBER_RECEIPT_ID", getMemberTransId());
        }
        if(getFinalMap() != null && getFinalMap().size()>0){
            data.put("MDS_MEMBER_DATA",getFinalMap());
            data.put("TOTAL_NET_AMOUNT",getTxtTotalNetAmount());
           // data.put("MEMBER_NO",getTxtMembershipNo());
        }
        if(isSplitMDSTransaction != null && isSplitMDSTransaction.equals("Y")){
            data.put("mdsReceiptSplitEntryTOList", splitMDSReceiptEntryLst);
            data.put("IS_SPLIT_MDS_TRANSACTION",isSplitMDSTransaction);
        }
        if (transactionDetailsTO == null)
            transactionDetailsTO = new LinkedHashMap();
        if (deletedTransactionDetailsTO != null) {
            transactionDetailsTO.put(DELETED_TRANS_TOs,deletedTransactionDetailsTO);
            deletedTransactionDetailsTO = null;
        }
        transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
        allowedTransactionDetailsTO = null;
        data.put("TransactionTO",transactionDetailsTO);
        data.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID); 
        data.put(CommonConstants.SCREEN, getScreen());
        System.out.println("#$########## data : "+ data);
        HashMap proxyResultMap = proxy.execute(data, map);
        System.out.println("###### proxyResultMap : "+proxyResultMap);
        setProxyReturnMap(proxyResultMap);
        finalMap = null;
        _authorizeMap = null;
    }
    
    public void resetForm(){
        setTxtMembershipNo("");
        setTxtTotalNetAmount("");
        setTxtAribitrationAmt("");
        setTxtNoticeAmt("");
        setTxtTotBonusAmt("");
        setTxtTotBonusAmt("");
        setTxtTotDiscountAmt("");
        setTxtTotDiscountAmt("");
        setTxtTotInstAmt("");
        setTxtTotInterest("");
        setTxtTotPenalAmt("");
        setLblMemberName("");
        setLblMemberType("");
    }
    
     public void doSplitMDSTransaction(){
         splitMDSReceiptEntryLst.clear();//added by nithya on 28-11-2017 
        if (getMdsSplitMap() != null && getMdsSplitMap().size() > 0) {
            List splitList = null;
            List splitTransInstList = new ArrayList();
            List bonusAmountList = new ArrayList();
            List penalList = new ArrayList();
            List narrationList = new ArrayList();
            ArrayList sampleList = null;
            ArrayList sampleList2 = null;
            HashMap MDSsplitTrans = (HashMap) getMdsSplitMap();
            splitIterator = MDSsplitTrans.keySet().iterator();
            String splitKey = "";
            int curInst = 0;
            for (int i = 0; i < MDSsplitTrans.size(); i++) {
                    splitKey = (String) splitIterator.next();
                    //System.out.println("###### splitKey ###### : " + splitKey);
                    HashMap splitMap = (HashMap) MDSsplitTrans.get(splitKey);
                    sampleList = new ArrayList();
                    System.out.println("splitMap^$^$^#^#^#^"+splitMap);     
                    if (splitMap.containsKey("IS_SPLIT_MDS_TRANSACTION") && splitMap.get("IS_SPLIT_MDS_TRANSACTION") != null) {
                        isSplitMDSTransaction = CommonUtil.convertObjToStr(splitMap.get("IS_SPLIT_MDS_TRANSACTION"));
                    }
                    if (splitMap.containsKey("INST_AMT_LIST") && splitMap.get("INST_AMT_LIST") != null) {
                        splitTransInstList = (List) splitMap.get("INST_AMT_LIST");
                    }
                    if (splitMap.containsKey("BONUS_AMT_LIST") && splitMap.get("BONUS_AMT_LIST") != null) {
                        bonusAmountList = (List) splitMap.get("BONUS_AMT_LIST");
                    }
                    if (splitMap.containsKey("NARRATION_LIST") && splitMap.get("NARRATION_LIST") != null) {
                        narrationList = (List) splitMap.get("NARRATION_LIST");
                    }
                    if (splitMap.containsKey("PENAL_AMT_LIST") && splitMap.get("PENAL_AMT_LIST") != null) {
                        penalList = (List) splitMap.get("PENAL_AMT_LIST");
                    }
                    if (splitMap.containsKey("INSTALL_NO") && splitMap.get("INSTALL_NO") != null) {
                        curInst = CommonUtil.convertObjToInt(splitMap.get("INSTALL_NO"));
                    }
                    int size = splitTransInstList.size();
                    for (int k = 0; k < size; k++) {
                        splitMDSReceiptEntryTO = new MDSReceiptEntryTO(); 
                        //sampleList = new ArrayList();
                        splitMDSReceiptEntryTO.setSchemeName(CommonUtil.convertObjToStr(splitMap.get("SCHEME_NAME")));
                        splitMDSReceiptEntryTO.setDivisionNo(CommonUtil.convertObjToDouble(splitMap.get("DIVISION_NO")));
                        splitMDSReceiptEntryTO.setChittalNo(CommonUtil.convertObjToStr(splitMap.get("CHITTAL_NO")));
                        splitMDSReceiptEntryTO.setChitStartDt(getProperDateFormat(CommonUtil.convertObjToStr(splitMap.get("CHIT_START_DT"))));
                        splitMDSReceiptEntryTO.setChitEndDt(getProperDateFormat(CommonUtil.convertObjToStr(splitMap.get("SCHEME_END_DT"))));
                        splitMDSReceiptEntryTO.setNoOfInst(CommonUtil.convertObjToInt(splitMap.get("NO_OF_INSTALLMENTS")));
                        splitMDSReceiptEntryTO.setSubNo(CommonUtil.convertObjToDouble(splitMap.get("SUB_NO")));
                        splitMDSReceiptEntryTO.setInstAmt(CommonUtil.convertObjToDouble(splitMap.get("INST_AMT")));
                        splitMDSReceiptEntryTO.setPendingInst(CommonUtil.convertObjToDouble(splitMap.get("PENDING_INST")));                       
                        splitMDSReceiptEntryTO.setMemberName(CommonUtil.convertObjToStr(splitMap.get("MEMBER_NAME")));
                        splitMDSReceiptEntryTO.setPaidDate((Date)currDate.clone());  
                        splitMDSReceiptEntryTO.setBankPay("N");
                        splitMDSReceiptEntryTO.setStatusBy(TrueTransactMain.USER_ID);
                        splitMDSReceiptEntryTO.setBranchCode(getSelectedBranchID());
                        splitMDSReceiptEntryTO.setInitiatedBranch(TrueTransactMain.BRANCH_ID); 
                        splitMDSReceiptEntryTO.setBonusAmtPayable(CommonUtil.convertObjToDouble(bonusAmountList.get(k)));
                        splitMDSReceiptEntryTO.setBonusAmtAvail(CommonUtil.convertObjToDouble(bonusAmountList.get(k)));
                        splitMDSReceiptEntryTO.setCurrInst(new Double(curInst));
                        splitMDSReceiptEntryTO.setNoOfInstPay(1);
                        splitMDSReceiptEntryTO.setPaidInst(new Double(curInst));                        
                        splitMDSReceiptEntryTO.setNarration(setNarrationToSplitTransaction(k,curInst,CommonUtil.convertObjToStr(splitMap.get("CHIT_START_DT")),
                                                            CommonUtil.convertObjToStr(splitMap.get("SCHEME_END_DT"))));
                        splitMDSReceiptEntryTO.setPenalAmtPayable(CommonUtil.convertObjToDouble(penalList.get(k)));
                        splitMDSReceiptEntryTO.setInstAmtPayable(CommonUtil.convertObjToDouble(splitTransInstList.get(k)));
                        splitMDSReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(penalList.get(k)) + CommonUtil.convertObjToDouble(splitTransInstList.get(k)));                         
                        sampleList.add(splitMDSReceiptEntryTO);
                        //System.out.println("sampleList#%#%#%#%#%#"+sampleList);
                        //System.out.println("sampleList2#%#%#%#%#%#"+sampleList2);
                    }                    
                    splitMDSReceiptEntryLst.add(sampleList);                    
                    //System.out.println("splitMDSReceiptEntryLst#%#%#%#%#%#"+splitMDSReceiptEntryLst);
            }
        }
    }
     
    public Date getProperDateFormat(Object obj) {
        Date curDate = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            curDate = (Date) currDate.clone();
            curDate.setDate(tempDt.getDate());
            curDate.setMonth(tempDt.getMonth());
            curDate.setYear(tempDt.getYear());
        }
        return curDate;
    }
    
    private String setNarrationToSplitTransaction(int i, int paidInstallments, String strDt, String endDt) {
        String narration = "";
        ArrayList narrationList = new ArrayList();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM/yyyy");
        int paidInst = paidInstallments + 1;
        paidInstallments += i;
        paidInst += i;
        narration = "Inst#" + (paidInst);
        Date dt1 = DateUtil.addDays(DateUtil.getDateMMDDYYYY(strDt), 30 * paidInstallments);
        Date dt = DateUtil.addDays(DateUtil.getDateMMDDYYYY(endDt), 30 * (1));
        narration += " " + sdf.format(dt1);
        //narrationList.add(narration);
        return narration;
    }
    
    public void setProductMapDetails(String scheme_name) {
        HashMap schemeMap = new HashMap();
        schemeMap.put("SCHEME_NAME", scheme_name);
        List lst = ClientUtil.executeQuery("getSelectSchemeAcctHead", schemeMap);
        if (lst != null && lst.size() > 0) {
            schemeMap = (HashMap) lst.get(0);
            if (schemeMap.containsKey("IS_SPLIT_MDS_TRANSACTION") && schemeMap.get("IS_SPLIT_MDS_TRANSACTION") != null) {
                isSplitMDSTransaction = CommonUtil.convertObjToStr(schemeMap.get("IS_SPLIT_MDS_TRANSACTION"));
            }
        }
    }

    /**
     * Getter for property txtMembershipNo.
     * @return Value of property txtMembershipNo.
     */
    public java.lang.String getTxtMembershipNo() {
        return txtMembershipNo;
    }
    
    /**
     * Setter for property txtMembershipNo.
     * @param txtMembershipNo New value of property txtMembershipNo.
     */
    public void setTxtMembershipNo(java.lang.String txtMembershipNo) {
        this.txtMembershipNo = txtMembershipNo;
    }
    
    /**
     * Getter for property txtTotInstAmt.
     * @return Value of property txtTotInstAmt.
     */
    public java.lang.String getTxtTotInstAmt() {
        return txtTotInstAmt;
    }
    
    /**
     * Setter for property txtTotInstAmt.
     * @param txtTotInstAmt New value of property txtTotInstAmt.
     */
    public void setTxtTotInstAmt(java.lang.String txtTotInstAmt) {
        this.txtTotInstAmt = txtTotInstAmt;
    }
    
    /**
     * Getter for property txtTotPenalAmt.
     * @return Value of property txtTotPenalAmt.
     */
    public java.lang.String getTxtTotPenalAmt() {
        return txtTotPenalAmt;
    }
    
    /**
     * Setter for property txtTotPenalAmt.
     * @param txtTotPenalAmt New value of property txtTotPenalAmt.
     */
    public void setTxtTotPenalAmt(java.lang.String txtTotPenalAmt) {
        this.txtTotPenalAmt = txtTotPenalAmt;
    }
    
    /**
     * Getter for property txtTotBonusAmt.
     * @return Value of property txtTotBonusAmt.
     */
    public java.lang.String getTxtTotBonusAmt() {
        return txtTotBonusAmt;
    }
    
    /**
     * Setter for property txtTotBonusAmt.
     * @param txtTotBonusAmt New value of property txtTotBonusAmt.
     */
    public void setTxtTotBonusAmt(java.lang.String txtTotBonusAmt) {
        this.txtTotBonusAmt = txtTotBonusAmt;
    }
    
    /**
     * Getter for property txtTotDiscountAmt.
     * @return Value of property txtTotDiscountAmt.
     */
    public java.lang.String getTxtTotDiscountAmt() {
        return txtTotDiscountAmt;
    }
    
    /**
     * Setter for property txtTotDiscountAmt.
     * @param txtTotDiscountAmt New value of property txtTotDiscountAmt.
     */
    public void setTxtTotDiscountAmt(java.lang.String txtTotDiscountAmt) {
        this.txtTotDiscountAmt = txtTotDiscountAmt;
    }
    
    /**
     * Getter for property txtTotInterest.
     * @return Value of property txtTotInterest.
     */
    public java.lang.String getTxtTotInterest() {
        return txtTotInterest;
    }
    
    /**
     * Setter for property txtTotInterest.
     * @param txtTotInterest New value of property txtTotInterest.
     */
    public void setTxtTotInterest(java.lang.String txtTotInterest) {
        this.txtTotInterest = txtTotInterest;
    }
    
    /**
     * Getter for property txtNoticeAmt.
     * @return Value of property txtNoticeAmt.
     */
    public java.lang.String getTxtNoticeAmt() {
        return txtNoticeAmt;
    }
    
    /**
     * Setter for property txtNoticeAmt.
     * @param txtNoticeAmt New value of property txtNoticeAmt.
     */
    public void setTxtNoticeAmt(java.lang.String txtNoticeAmt) {
        this.txtNoticeAmt = txtNoticeAmt;
    }
    
    /**
     * Getter for property txtAribitrationAmt.
     * @return Value of property txtAribitrationAmt.
     */
    public java.lang.String getTxtAribitrationAmt() {
        return txtAribitrationAmt;
    }
    
    /**
     * Setter for property txtAribitrationAmt.
     * @param txtAribitrationAmt New value of property txtAribitrationAmt.
     */
    public void setTxtAribitrationAmt(java.lang.String txtAribitrationAmt) {
        this.txtAribitrationAmt = txtAribitrationAmt;
    }
    
    
    /**
     * Getter for property lblMemberName.
     * @return Value of property lblMemberName.
     */
    public java.lang.String getLblMemberName() {
        return lblMemberName;
    }
    
    /**
     * Setter for property lblMemberName.
     * @param lblMemberName New value of property lblMemberName.
     */
    public void setLblMemberName(java.lang.String lblMemberName) {
        this.lblMemberName = lblMemberName;
    }
    
    /**
     * Getter for property lblMemberType.
     * @return Value of property lblMemberType.
     */
    public java.lang.String getLblMemberType() {
        return lblMemberType;
    }
    
    /**
     * Setter for property lblMemberType.
     * @param lblMemberType New value of property lblMemberType.
     */
    public void setLblMemberType(java.lang.String lblMemberType) {
        this.lblMemberType = lblMemberType;
    }
    
    /**
     * Getter for property tblMemberRecord.
     * @return Value of property tblMemberRecord.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblMemberRecord() {
        return tblMemberRecord;
    }
    
    /**
     * Setter for property tblMemberRecord.
     * @param tblMemberRecord New value of property tblMemberRecord.
     */
    public void setTblMemberRecord(com.see.truetransact.clientutil.EnhancedTableModel tblMemberRecord) {
        this.tblMemberRecord = tblMemberRecord;
    }
    
    /**
     * Getter for property oldTransDetMap.
     * @return Value of property oldTransDetMap.
     */
    public java.util.HashMap getOldTransDetMap() {
        return oldTransDetMap;
    }
    
    /**
     * Setter for property oldTransDetMap.
     * @param oldTransDetMap New value of property oldTransDetMap.
     */
    public void setOldTransDetMap(java.util.HashMap oldTransDetMap) {
        this.oldTransDetMap = oldTransDetMap;
    }
    
    /**
     * Getter for property transactionOB.
     * @return Value of property transactionOB.
     */
    public com.see.truetransact.ui.common.transaction.TransactionOB getTransactionOB() {
        return transactionOB;
    }
    
    /**
     * Setter for property transactionOB.
     * @param transactionOB New value of property transactionOB.
     */
    public void setTransactionOB(com.see.truetransact.ui.common.transaction.TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }
    
    /**
     * Getter for property allowedTransactionDetailsTO.
     * @return Value of property allowedTransactionDetailsTO.
     */
    public java.util.LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }
    
    /**
     * Setter for property allowedTransactionDetailsTO.
     * @param allowedTransactionDetailsTO New value of property allowedTransactionDetailsTO.
     */
    public void setAllowedTransactionDetailsTO(java.util.LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }
    
    /**
     * Getter for property transactionDetailsTO.
     * @return Value of property transactionDetailsTO.
     */
    public java.util.LinkedHashMap getTransactionDetailsTO() {
        return transactionDetailsTO;
    }
    
    /**
     * Setter for property transactionDetailsTO.
     * @param transactionDetailsTO New value of property transactionDetailsTO.
     */
    public void setTransactionDetailsTO(java.util.LinkedHashMap transactionDetailsTO) {
        this.transactionDetailsTO = transactionDetailsTO;
    }
    
    /**
     * Getter for property finalMap.
     * @return Value of property finalMap.
     */
    public java.util.HashMap getFinalMap() {
        return finalMap;
    }
    
    /**
     * Setter for property finalMap.
     * @param finalMap New value of property finalMap.
     */
    public void setFinalMap(java.util.HashMap finalMap) {
        this.finalMap = finalMap;
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
     * Getter for property deletedTransactionDetailsTO.
     * @return Value of property deletedTransactionDetailsTO.
     */
    public java.util.LinkedHashMap getDeletedTransactionDetailsTO() {
        return deletedTransactionDetailsTO;
    }
    
    /**
     * Setter for property deletedTransactionDetailsTO.
     * @param deletedTransactionDetailsTO New value of property deletedTransactionDetailsTO.
     */
    public void setDeletedTransactionDetailsTO(java.util.LinkedHashMap deletedTransactionDetailsTO) {
        this.deletedTransactionDetailsTO = deletedTransactionDetailsTO;
    }
    
    /**
     * Getter for property txtTotalNetAmount.
     * @return Value of property txtTotalNetAmount.
     */
    public java.lang.String getTxtTotalNetAmount() {
        return txtTotalNetAmount;
    }
    
    /**
     * Setter for property txtTotalNetAmount.
     * @param txtTotalNetAmount New value of property txtTotalNetAmount.
     */
    public void setTxtTotalNetAmount(java.lang.String txtTotalNetAmount) {
        this.txtTotalNetAmount = txtTotalNetAmount;
    }
    
    /**
     * Getter for property memberTransId.
     * @return Value of property memberTransId.
     */
    public java.lang.String getMemberTransId() {
        return memberTransId;
    }
    
    /**
     * Setter for property memberTransId.
     * @param memberTransId New value of property memberTransId.
     */
    public void setMemberTransId(java.lang.String memberTransId) {
        this.memberTransId = memberTransId;
    }
    
    /**
     * Getter for property lblStatus.
     * @return Value of property lblStatus.
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    
    /**
     * Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }
    
    /**
     * Getter for property result.
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }
    
    /**
     * Setter for property result.
     * @param result New value of property result.
     */
    public void setResult(int result) {
        this.result = result;
    }
    
    /**
     * Getter for property actionType.
     * @return Value of property actionType.
     */
    public int getActionType() {
        return actionType;
    }
    
    /**
     * Setter for property actionType.
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
//        ttNotifyObservers();
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
//        ttNotifyObservers();
    }
    
}