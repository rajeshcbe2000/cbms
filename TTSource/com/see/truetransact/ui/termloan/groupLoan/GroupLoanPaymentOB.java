/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SHGTransactionOB.java
 *
 * Created on Tue Oct 18 12:40:45 IST 2011
 */

package com.see.truetransact.ui.termloan.groupLoan;

import com.see.truetransact.ui.termloan.SHG.*;
import java.util.Observable;
import java.util.HashMap;


import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
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
import com.see.truetransact.transferobject.mdsapplication.MDSApplicationTO;
import com.see.truetransact.clientutil.EnhancedTableModel;

import com.see.truetransact.transferobject.termloan.groupLoan.GroupLoanTO;
import com.see.truetransact.ui.common.transaction.TransactionOB ;
/**
 *
 * @author  Suresh
 */

public class GroupLoanPaymentOB extends CObservable{
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int actionType;
    private int result;
    private HashMap oldTransDetMap = new HashMap();
    private TransactionOB transactionOB;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    ArrayList tableTitle = new ArrayList();
    ArrayList tableList = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private EnhancedTableModel tblSHGDetails;
    private HashMap _authorizeMap;
    private ProxyFactory proxy;
    private HashMap map;
    private List finalList = null;
    private final static Logger log = Logger.getLogger(GroupLoanPaymentOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private String callingTransAcctNo ="";
    private String txtGroupId = "";
    private String txtTotalPayment = "";
    private String shgTransId = "";
    private Date currDt = null;
    private String txtAcctNum = "";
    private String narration = "";
    private Date purchaseDt = null;

    public Date getPurchaseDt() {
        return purchaseDt;
    }

    public void setPurchaseDt(Date purchaseDt) {
        this.purchaseDt = purchaseDt;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getTxtAcctNum() {
        return txtAcctNum;
    }

    public void setTxtAcctNum(String txtAcctNum) {
        this.txtAcctNum = txtAcctNum;
    }
    
    /** Creates a new instance of TDS MiantenanceOB */
    public GroupLoanPaymentOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "GroupLoanJNDI");
            map.put(CommonConstants.HOME, "GroupLoanJNDIHome");
            map.put(CommonConstants.REMOTE, "GroupLoan");
            setTableTile();
//            tblSHGDetails= new EnhancedTableModel(null, tableTitle);
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    private void setTableTile() throws Exception{
        tableTitle.add("Credit Card No");
        tableTitle.add("Customer Id");
        tableTitle.add("Customer Name");
        tableTitle.add("Limit");
        tableTitle.add("Avail Balance");
        //tableTitle.add("Principal Due");
        //tableTitle.add("Interest");
        //tableTitle.add("Penal");
        //tableTitle.add("Charge");
        tableTitle.add("Total Due");
        tableTitle.add("Payment Amt");
        IncVal = new ArrayList();
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
    
    /** To retrieve a particular customer's accountclosing record */
    public void getData(HashMap whereMap) {
        try{
            System.out.println("whereMap.whereMap>>>>>"+whereMap);
            //getNarration(whereMap);
            getTransDetails(whereMap);
        }catch(Exception e){
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    public boolean duePayYN(String actNum,String custID){
        boolean dueFlag = false ;
        HashMap prodMap = new HashMap();
        HashMap dueMap = new HashMap();
        prodMap.put("ACT_NUM", actNum);
        List prodList = ClientUtil.executeQuery("getProdIdForGroupLoan", prodMap);
        if (prodList != null && prodList.size() > 0) {
            prodMap = (HashMap) prodList.get(0);
        }
        dueMap.put("PROD_ID", CommonUtil.convertObjToStr(prodMap.get("PROD_ID")));
        List dueList = ClientUtil.executeQuery("getRepayDetails", prodMap);
        if (dueList != null && dueList.size() > 0) {
            dueMap = (HashMap) dueList.get(0);
            if(dueMap.get("DEBIT_ALLOW_FOR_DUE").equals("Y")){
                HashMap checkMap = new HashMap();
                checkMap.put("ACT_NUM", actNum);
                checkMap.put("CUST_ID", custID);
                checkMap.put("TRANS_DT", currDt);
                List checkList = ClientUtil.executeQuery("getOverDueAmount", checkMap);
                if (checkList != null && checkList.size() > 0) {
                    checkMap = (HashMap) checkList.get(0);
                    System.out.println("checkMap#$#$"+checkMap);
                    System.out.println("!@$$$!@@#@$#%$%#%#$#$"+CommonUtil.convertObjToDouble(checkMap.get("DUE_AMOUNT")));
                    if(CommonUtil.convertObjToDouble(checkMap.get("DUE_AMOUNT"))>0)
                        dueFlag = true ; 
                    else
                        dueFlag = false ;  
                }
            }
        }
        System.out.println("dueFlag#####"+dueFlag);
        return dueFlag;
    }
    public void getEditTableDetails(HashMap whereMap){            
        try{
            ArrayList rowList = new ArrayList();
            HashMap dataMap = new HashMap();
            whereMap.put("TRANS_DT",currDt);
            List editList = ClientUtil.executeQuery("getGroupPaymentTableAuthorize", whereMap);
            System.out.println("#$@$@$#@$@#$@ editList : "+editList);
            if(editList!=null && editList.size()>0){
                for (int i = 0;i<editList.size();i++){
                    dataMap = (HashMap) editList.get(i);
                    rowList = new ArrayList();
                    rowList.add(dataMap.get("CREDIT_CARD_NO"));
                    rowList.add(dataMap.get("CUST_ID"));
                    rowList.add(dataMap.get("CUSTOMER"));
                    rowList.add(dataMap.get("LIMIT"));
                    rowList.add(dataMap.get("AVAIL_BAL"));
                    rowList.add(dataMap.get("TOTAL_DUE"));
                    rowList.add(dataMap.get("PAYMENT"));
                    rowList.add(dataMap.get("NARRATION"));
                    tableList.add(rowList);
                }
                setFinalList(editList);
//                tblSHGDetails= new EnhancedTableModel((ArrayList)tableList, tableTitle);
            }else{
                ClientUtil.displayAlert("No Record !!! ");
            }
        }catch(Exception e){
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    public void getTransDetails(HashMap whereMap) {
        final HashMap mapData;
        try {
            System.out.println("whereMap.getTransDetails>>>>>"+whereMap);
            whereMap.put("TRANS_DT",currDt.clone());
            whereMap.put("PURCHASE_DT","PURCHASE_DT");
            mapData = proxy.executeQuery(whereMap,map);
            System.out.println("@#$@#$@#$mapData:"+mapData);
            if(mapData.containsKey("TRANSACTION_LIST")){
                List list = (List) mapData.get("TRANSACTION_LIST");
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
            }
            if(mapData.containsKey("PURCHASE_DT") && mapData.get("PURCHASE_DT")!=null){               
                setPurchaseDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(mapData.get("PURCHASE_DT"))));               
            }
        } catch( Exception e ) {
            System.out.println("Error In populateData()");
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    public void getNarration(HashMap whereMap) {
        final HashMap mapData;
        try {
            System.out.println("whereMap.getTransDetails>>>>>"+whereMap);
            whereMap.put("PURCHASE_DT","PURCHASE_DT");
            mapData = proxy.executeQuery(whereMap,map);
            System.out.println("@#$@#$@#$mapData:"+mapData);
            if(mapData.containsKey("PURCHASE_DT") && mapData.get("PURCHASE_DT")!=null){               
                setPurchaseDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(mapData.get("PURCHASE_DT"))));               
            }
        } catch( Exception e ) {
            System.out.println("Error In populateData()");
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    public GroupLoanTO getGroupLoanTO() {
        GroupLoanTO groupLoanTO = new GroupLoanTO(); 
        groupLoanTO.setNarration(getNarration());
        return groupLoanTO;
    }
    
    public void setGroupLoanTO(GroupLoanTO groupLoanTO) {       
       setNarration(groupLoanTO.getNarration());
    }
            
    public void getTableDetails(HashMap whereMap){              // NEW ACTION
        try{
            ArrayList rowList = new ArrayList();
            HashMap dataMap = new HashMap();
            whereMap.put("BRANCH_CODE",getSelectedBranchID());
            whereMap.put("ACT_NUM",CommonUtil.convertObjToStr(whereMap.get("ACT_NUM")));   
            whereMap.put("TRANS_DT",currDt);
            List loanList = ClientUtil.executeQuery("getGroupLoanTableDetailsPayment", whereMap);
            System.out.println("#$@$@$#@$@#$@ List : "+loanList);
            if(loanList!=null && loanList.size()>0){
                for (int i = 0;i<loanList.size();i++){
                    double princ_Due = 0.0;
                    double int_Due = 0.0;
                    double penal = 0.0;
                    double charge = 0.0;
                    double total_Due = 0.0;
                    dataMap = (HashMap) loanList.get(i);
                    rowList = new ArrayList();
                    rowList.add(dataMap.get("CREDIT_CARD_NO"));
                    rowList.add(dataMap.get("CUST_ID"));
                    rowList.add(dataMap.get("CUSTOMER"));
                    rowList.add(dataMap.get("LIMIT"));
                    rowList.add(dataMap.get("AVIL_BAL"));
                    rowList.add(dataMap.get("TOTAL_DUE"));
                    rowList.add(String.valueOf(0));
                    rowList.add(String.valueOf(""));
                    tableList.add(rowList);
                }
                System.out.println("#$# standInsList:"+loanList);
                setFinalList(loanList);
//                tblSHGDetails= new EnhancedTableModel((ArrayList)tableList, tableTitle);
            }else{
                ClientUtil.displayAlert("No Record !!! ");
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private HashMap interestCalculationTLAD(Object accountNo, Object prod_id){
        HashMap map=new HashMap();
        HashMap hash=null;
        try{
            map.put("ACT_NUM",accountNo);
            //		if((ComboBoxModel)cboProdId.getModel()!=null)
            //                if((((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString())!=null)
            //                    prod_id=((ComboBoxModel)cboProdId.getModel()).getKeyForSelected().toString();
            map.put("PROD_ID",prod_id);
            map.put("TRANS_DT", currDt);
            map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
            String mapNameForCalcInt = "IntCalculationDetail";
            List lst=ClientUtil.executeQuery(mapNameForCalcInt, map);
            if(lst !=null && lst.size()>0){
                hash=(HashMap)lst.get(0);
                if(hash.get("AS_CUSTOMER_COMES")!=null  && hash.get("AS_CUSTOMER_COMES").equals("N")){
                    hash=new HashMap();
                    return hash;
                }
                map.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
                map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                map.putAll(hash);
                map.put("LOAN_ACCOUNT_CLOSING","LOAN_ACCOUNT_CLOSING");
                map.put("CURR_DATE", currDt);
                System.out.println("map before intereest###"+map);
                //                    hash =interestcalTask.interestCalcTermLoanAD(map);
                lst = ClientUtil.executeQuery("",map);
                if (lst!=null && lst.size()>0) {
                    hash=(HashMap)lst.get(0);
                    if(hash==null) {
                        hash=new HashMap();
                    }
                    System.out.println("hashinterestoutput###"+hash);
                }
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }

        return hash;
    }     
    
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            doActionPerform();
            setResult(getActionType());
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
            data.put("ACT_NUM", getTxtAcctNum());
        }
        if(getFinalList() != null && getFinalList().size()>0){
            data.put("SHG_TABLE_DATA_PAYMENT",getFinalList());
            //data.put("GROUP_ID",getTxtGroupId());
        }
        GroupLoanTO groupLoanTO = getGroupLoanTO();
        if(getFinalList() != null && getFinalList().size()>0){
            data.put("GroupLoanTO",groupLoanTO);
            //data.put("GROUP_ID",getTxtGroupId());
        }
        if (transactionDetailsTO == null)
            transactionDetailsTO = new LinkedHashMap();
        if (deletedTransactionDetailsTO != null) {
            transactionDetailsTO.put(DELETED_TRANS_TOs,deletedTransactionDetailsTO);
            deletedTransactionDetailsTO = null;
        }
        transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
        allowedTransactionDetailsTO = null;
        if(getTxtAcctNum()!=null && getTxtAcctNum().length()>0){
            data.put("ACT_NUM",(String)getTxtAcctNum());
        }
        if(getPurchaseDt()!=null && !getPurchaseDt().equals("")){
            data.put("PURCHASE_DT",getPurchaseDt());
        }
        data.put("TransactionTO",transactionDetailsTO);
        data.put("BRANCH_ID",getSelectedBranchID());
        data.put("INITIATED_BRANCH",TrueTransactMain.BRANCH_ID);; 
        System.out.println("#$########## data : "+ data);
        HashMap proxyReturnMap = proxy.execute(data, map);
        System.out.println("######proxyReturnMap : "+proxyReturnMap);
        setProxyReturnMap(proxyReturnMap);
        setResult(actionType);
        _authorizeMap = null;
    }
    
    public void resetForm(){
        ArrayList tableList = new ArrayList();
        setTxtGroupId("");
        setTxtTotalPayment("");
        setCallingTransAcctNo("");
        setChanged();
        setTxtAcctNum("");
    }
    
    
    public void resetTableValues(){
        tblSHGDetails.setDataArrayList(null,tableTitle);
    }
    
    
    // Setter method for txtGroupId
    void setTxtGroupId(String txtGroupId){
        this.txtGroupId = txtGroupId;
        setChanged();
    }
    // Getter method for txtGroupId
    String getTxtGroupId(){
        return this.txtGroupId;
    }
    
    // Setter method for txtTotalPayment
    void setTxtTotalPayment(String txtTotalPayment){
        this.txtTotalPayment = txtTotalPayment;
        setChanged();
    }
    // Getter method for txtTotalPayment
    String getTxtTotalPayment(){
        return this.txtTotalPayment;
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
     * Getter for property tblSHGDetails.
     * @return Value of property tblSHGDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblSHGDetails() {
        return tblSHGDetails;
    }
    
    /**
     * Setter for property tblSHGDetails.
     * @param tblSHGDetails New value of property tblSHGDetails.
     */
    public void setTblSHGDetails(com.see.truetransact.clientutil.EnhancedTableModel tblSHGDetails) {
        this.tblSHGDetails = tblSHGDetails;
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }
    
    /**
     * Getter for property tableTitle.
     * @return Value of property tableTitle.
     */
    public java.util.ArrayList getTableTitle() {
        return tableTitle;
    }
    
    /**
     * Setter for property tableTitle.
     * @param tableTitle New value of property tableTitle.
     */
    public void setTableTitle(java.util.ArrayList tableTitle) {
        this.tableTitle = tableTitle;
    }
    
    /**
     * Getter for property tableList.
     * @return Value of property tableList.
     */
    public java.util.ArrayList getTableList() {
        return tableList;
    }
    
    /**
     * Setter for property tableList.
     * @param tableList New value of property tableList.
     */
    public void setTableList(java.util.ArrayList tableList) {
        this.tableList = tableList;
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
     * Getter for property finalList.
     * @return Value of property finalList.
     */
    public java.util.List getFinalList() {
        return finalList;
    }
    
    /**
     * Setter for property finalList.
     * @param finalList New value of property finalList.
     */
    public void setFinalList(java.util.List finalList) {
        this.finalList = finalList;
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
     * Getter for property shgTransId.
     * @return Value of property shgTransId.
     */
    public java.lang.String getShgTransId() {
        return shgTransId;
    }
    
    /**
     * Setter for property shgTransId.
     * @param shgTransId New value of property shgTransId.
     */
    public void setShgTransId(java.lang.String shgTransId) {
        this.shgTransId = shgTransId;
    }
    
    /**
     * Getter for property callingTransAcctNo.
     * @return Value of property callingTransAcctNo.
     */
    public java.lang.String getCallingTransAcctNo() {
        return callingTransAcctNo;
    }
    
    /**
     * Setter for property callingTransAcctNo.
     * @param callingTransAcctNo New value of property callingTransAcctNo.
     */
    public void setCallingTransAcctNo(java.lang.String callingTransAcctNo) {
        this.callingTransAcctNo = callingTransAcctNo;
    }
    
}