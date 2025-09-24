
/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MDSCommencementOB.java
 *
* Created on January 7, 2004, 5:14 PM
 */

package com.see.truetransact.ui.mdsapplication.mdsconmmencement;

/**
 *
 * @author Sathiya
 *
 **/

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
//import java.lang.Double;
import java.util.Date;
import com.see.truetransact.ui.common.transaction.TransactionOB ;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.transferobject.mdsapplication.MDSApplicationTO;

public class MDSCommencementOB extends CObservable {
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
//    private static RemittanceProductRB objRemittanceProductRB = new RemittanceProductRB();
    
    java.util.ResourceBundle objRemittanceProductRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.mdsapplication.mdsconmmencement.MDSCommencementRB", ProxyParameters.LANGUAGE);


    private TransactionOB transactionOB;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";

//    private HashMap tableHashData;
    private HashMap hash;
    private HashMap operationMap;
    private ProxyFactory proxy;
//    private HashMap lookUpHash;
//    private HashMap keyValue;
//    private ArrayList key;
//    private ArrayList value;
    private int actionType;
    private int result;
    
    private Double txtInstAmt = 0.0;    //AJITH
    private Double txtTotAmt = 0.0;    //AJITH
    private String tdtStartDt = "";
    private String txtSchemeName = "";
    private String commencementTransId = "";
    private Date tdtCommencementDate = null;
    private EnhancedTableModel tblCommencement;
    private ArrayList commencementList = new ArrayList();
    private Date curDt =null;
    private Date commenceDate =null;
    
    private String txtSchemeNameClosure="";
    private HashMap MDSClosureMap = new HashMap();

    private HashMap authorizeMap = new HashMap();
    private static MDSCommencementOB MDSCommencementOB;
    static {
        try {
            MDSCommencementOB = new MDSCommencementOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    private String predefinedInstallment="";
    private String discountFirstInstScheme = "";
    private double discountAmount = 0.0;
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    public MDSCommencementOB() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "MDSCommencementJNDI");
        operationMap.put(CommonConstants.HOME, "mdsapplication.mdsconmmencement.MDSCommencementHome");
        operationMap.put(CommonConstants.REMOTE, "mdsapplication.mdsconmmencement.MDSCommencement");
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        setCommencementList();
        tblCommencement = new EnhancedTableModel(null, commencementList);
        notifyObservers();
        fillDropdown();
        curDt = ClientUtil.getCurrentDate();
    }
    
    public static MDSCommencementOB getInstance() {
        return MDSCommencementOB;
    }
    
    /* Sets the Remittance Product Branch CTable Tittle to ArrayList*/
    private void setCommencementList() throws Exception{
        commencementList.add("Application No");
        commencementList.add("Chittal No");
        commencementList.add("Sub No");
        commencementList.add("Application Date");
        commencementList.add("MemberName");
        commencementList.add("Thalayal");
        commencementList.add("Munnal");
    }
    
    public void resetCommencementTbl(){
        tblCommencement.setDataArrayList(null, commencementList);
    }

    /** Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     *
     */
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
    public int getResult(){
        return result;
    }
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    
    public void setActionType(int action) {
        actionType = action;
        setChanged();
    }
    public int getActionType(){
        return actionType;
    }
    
    /** A method to set the combo box values */
    private void fillDropdown() throws Exception{
        try{
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void doSave(){
//        initialise();
        if(getActionType() != ClientConstants.ACTIONTYPE_DELETE ){
//            updateRemitProdBrchData();
//            //            updateRemitProdChrgData();
//            deleteRemitProdBrchData();
//            //            deleteRemitProdChrgData();
//            insertRemitProdBrchData();
            //            insertRemitProdChrgData();
        }
        doAction();
//        deinitialise();
    }
    
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            doActionPerform();
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }
    
    public void closureDoActionPerform() throws Exception{
        final HashMap data = new HashMap();
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        data.put("COMMAND",getCommand());
        data.put("SCHEME_NAME",getTxtSchemeNameClosure());
        data.put("MDS_CLOSURE",getMDSClosureMap());
        if(getAuthorizeMap()!=null && getAuthorizeMap().size()>0){
            data.put("AUTHORIZEMAP",getAuthorizeMap());
        }
        System.out.println("proxy:data:" + data + " : " + operationMap);
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setProxyReturnMap(proxyResultMap);
        System.out.println(" ###### proxyResultMap:" +proxyResultMap);
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final HashMap data = new HashMap();
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        data.put("COMMAND",getCommand());
        if(getAuthorizeMap()!=null && getAuthorizeMap().size()>0){
            data.put("AUTHORIZEMAP",getAuthorizeMap());
            data.put("PREDEFINITION_INSTALLMENT",getPredefinedInstallment());
            data.put("DISCOUNT_FIRST_INSTALLMENT",getDiscountFirstInstScheme()); // Added by nithya on 23-12-2020 for KD-2521
            data.put("DISCOUNT_AMOUNT",getDiscountAmount());
        }
        if (transactionDetailsTO == null)
            transactionDetailsTO = new LinkedHashMap();
        if (deletedTransactionDetailsTO != null) {
            transactionDetailsTO.put(DELETED_TRANS_TOs,deletedTransactionDetailsTO);
            deletedTransactionDetailsTO = null;
        }
        transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
        allowedTransactionDetailsTO = null;
        data.put("SCHEME_NAME",getTxtSchemeName());
        data.put("TransactionTO",transactionDetailsTO);
        Date commenceDt = (Date) getTdtCommencementDate();
        System.out.println("COMMENCEMENT_DATE" + getTdtCommencementDate());
        if(commenceDt!=null){
            commenceDate = (Date)curDt.clone();
            commenceDate.setDate(commenceDt.getDate());
            commenceDate.setMonth(commenceDt.getMonth());
            commenceDate.setYear(commenceDt.getYear());
        }
        data.put("COMMENCEMENT_DATE",commenceDate);
        System.out.println("proxy:data:" + data + " : " + operationMap);
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setProxyReturnMap(proxyResultMap);
    }

    /** Gets the command issued Insert , Upadate or Delete **/
    private String getCommand() throws Exception{
        String command = null;
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                command = CommonConstants.TOSTATUS_INSERT;
                break;
            case ClientConstants.ACTIONTYPE_COPY:
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
    
    public void populateData(HashMap whereMap) {
//        boolean aliasBranchTableFlag = false;
        HashMap mapData = new HashMap() ;
        try {
            System.out.println("$##### whereMap"+whereMap);
            mapData = proxy.executeQuery(whereMap, operationMap);
            populateOB(mapData);
        } catch( Exception e ) {
            parseException.logException(e,true);
        }
//        return aliasBranchTableFlag;
    }
    
    private void populateOB(HashMap mapData) {
        try{
        MDSApplicationTO mdsApplicationTO = new MDSApplicationTO() ;
        java.util.List lst = null;
        long i = 0;
        double totalAmt =0.0;
        ArrayList totalList = (ArrayList) ((List) mapData.get("mdsApplicationTO"));
        if(totalList!=null && totalList.size()>0){
            for(i = 0;i<totalList.size();i++){
                mdsApplicationTO = new MDSApplicationTO();
                mdsApplicationTO = (MDSApplicationTO)totalList.get((int)i);
                ArrayList CommencementList = new ArrayList();
                mdsApplicationTO = (MDSApplicationTO)totalList.get((int)i);
                CommencementList.add(mdsApplicationTO.getApplnNo());
                CommencementList.add(mdsApplicationTO.getChittalNo());
                CommencementList.add(mdsApplicationTO.getSubNo());
                CommencementList.add(mdsApplicationTO.getApplnDate());
                CommencementList.add(mdsApplicationTO.getMembershipName());
                CommencementList.add(mdsApplicationTO.getThalayal());
                CommencementList.add(mdsApplicationTO.getMunnal());
                tblCommencement.insertRow((int)i,CommencementList);
                totalAmt += mdsApplicationTO.getInstAmt();  //AJITH
            }
            setTxtSchemeName(mdsApplicationTO.getSchemeName());
            setTxtInstAmt(mdsApplicationTO.getInstAmt());   //AJITH
            setTdtStartDt(DateUtil.getStringDate(mdsApplicationTO.getChitStartDt()));
            setTdtCommencementDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(mdsApplicationTO.getCommencementDate())));
//            double totalAmount = (double)i * CommonUtil.convertObjToDouble(mdsApplicationTO.getInstAmt()).doubleValue();
            setTxtTotAmt(totalAmt); //AJITH
            List list = (List) mapData.get("TransactionTO");
            transactionOB.setDetails(list);
        }
        notifyObservers();
//        return aliasBranchTableFlag;
        }catch (Exception e){
            
        }
    }
    
    public boolean populateTableRecord(HashMap hash){
        boolean flag = false;
        HashMap recordMap = new HashMap();
        String multipleMember = "";
        double totalAmt =0.0;
        recordMap.put("SCHEME_NAME",getTxtSchemeName());
        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
            recordMap.put("COMMENCEMENT_TRANS_ID",getCommencementTransId());
        }
        if(getActionType() == ClientConstants.ACTIONTYPE_NEW){
            List lst = ClientUtil.executeQuery("getSchemeDivisionetailsList", recordMap);
            if(lst!=null && lst.size()>0){
                recordMap = (HashMap)lst.get(0);
//                int noOfDiv = CommonUtil.convertObjToInt(recordMap.get("NO_OF_DIVISIONS"));
                int totMember = CommonUtil.convertObjToInt(recordMap.get("TOTAL_NO_OF_MEMBERS"));
                multipleMember = CommonUtil.convertObjToStr(recordMap.get("MULTIPLE_MEMBER"));
                totalAmt = CommonUtil.convertObjToDouble(recordMap.get("TOTAL_AMOUNT_SCHEME")).doubleValue();
//                int totChitNo = CommonUtil.convertObjToInt(recordMap.get("NEXT_CHITTAL_NO"));
                recordMap = new HashMap();
                recordMap.put("SCHEME_NAME",getTxtSchemeName());
                lst = ClientUtil.executeQuery("getSelectGLTransMDSApplicationTO", recordMap);
                if(lst!=null && lst.size()>0){
                    long i = 0;
                    if(multipleMember.equals("Y") && !multipleMember.equals("")){
                        totMember = lst.size();
                    }
                    MDSApplicationTO mdsApplicationTO = new MDSApplicationTO();
                    if(lst!=null && lst.size()>0 && totMember == lst.size()){
                        for(i = 0;i<lst.size();i++){
                            mdsApplicationTO = new MDSApplicationTO();
                            ArrayList CommencementList = new ArrayList();
                            mdsApplicationTO = (MDSApplicationTO)lst.get((int)i);
                            CommencementList.add(mdsApplicationTO.getApplnNo());
                            CommencementList.add(mdsApplicationTO.getChittalNo());
                            CommencementList.add(mdsApplicationTO.getSubNo());
                            CommencementList.add(mdsApplicationTO.getApplnDate());
                            CommencementList.add(mdsApplicationTO.getMembershipName());
                            CommencementList.add(mdsApplicationTO.getThalayal());
                            CommencementList.add(mdsApplicationTO.getMunnal());
                            tblCommencement.insertRow((int)i,CommencementList);
                        }
                        setTxtSchemeName(mdsApplicationTO.getSchemeName());
                        setTxtInstAmt(mdsApplicationTO.getInstAmt());   //AJITH
                        setTdtStartDt(DateUtil.getStringDate(mdsApplicationTO.getChitStartDt()));
                        double totalAmount =0.0;
                         if(multipleMember.equals("Y") && !multipleMember.equals("")){
                             
                             totalAmount = totalAmt;
                         }else{
                             totalAmount = (double)i * mdsApplicationTO.getInstAmt();   //AJITH
                         }
                        setTxtTotAmt(totalAmount);  //AJITH
                    }else{
                        ClientUtil.showAlertWindow("Total Chit should be equal to no of Division and total no of member");
                        flag = true;
                    }
                }else{
                    ClientUtil.showAlertWindow("Commencement transaction created, pending for authorization");
                    flag = true;
                }
            }else{
                ClientUtil.showAlertWindow("All the application should be authorized");
                flag = true;
            }
        }
        return flag;
    }    
    
    /** Resets the General Remittance Fields to Null  */
    public void resetOBFields(){
        setTxtSchemeName("");
        setTdtStartDt("");
        setTxtInstAmt(0.0); //AJITH
        setTxtTotAmt(0.0); //AJITH
        setAuthorizeMap(null);
        setDiscountFirstInstScheme("N");
        setDiscountAmount(0.0);
//        resetRadioButton();
    }
    
    /**
     * Getter for property tblCommencement.
     * @return Value of property tblCommencement.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblCommencement() {
        return tblCommencement;
    }
    
    /**
     * Setter for property tblCommencement.
     * @param tblCommencement New value of property tblCommencement.
     */
    public void setTblCommencement(com.see.truetransact.clientutil.EnhancedTableModel tblCommencement) {
        this.tblCommencement = tblCommencement;
    }

    public Double getTxtInstAmt() {
        return txtInstAmt;
    }

    public void setTxtInstAmt(Double txtInstAmt) {
        this.txtInstAmt = txtInstAmt;
    }

    public Double getTxtTotAmt() {
        return txtTotAmt;
    }

    public void setTxtTotAmt(Double txtTotAmt) {
        this.txtTotAmt = txtTotAmt;
    }

    /**
     * Getter for property tdtStartDt.
     * @return Value of property tdtStartDt.
     */
    public java.lang.String getTdtStartDt() {
        return tdtStartDt;
    }
    
    /**
     * Setter for property tdtStartDt.
     * @param tdtStartDt New value of property tdtStartDt.
     */
    public void setTdtStartDt(java.lang.String tdtStartDt) {
        this.tdtStartDt = tdtStartDt;
    }
    
    /**
     * Getter for property txtSchemeName.
     * @return Value of property txtSchemeName.
     */
    public java.lang.String getTxtSchemeName() {
        return txtSchemeName;
    }
    
    /**
     * Setter for property txtSchemeName.
     * @param txtSchemeName New value of property txtSchemeName.
     */
    public void setTxtSchemeName(java.lang.String txtSchemeName) {
        this.txtSchemeName = txtSchemeName;
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
     * Getter for property commencementTransId.
     * @return Value of property commencementTransId.
     */
    public java.lang.String getCommencementTransId() {
        return commencementTransId;
    }
    
    /**
     * Setter for property commencementTransId.
     * @param commencementTransId New value of property commencementTransId.
     */
    public void setCommencementTransId(java.lang.String commencementTransId) {
        this.commencementTransId = commencementTransId;
    }
    
    /**
     * Getter for property authorizeMap.
     * @return Value of property authorizeMap.
     */
    public java.util.HashMap getAuthorizeMap() {
        return authorizeMap;
    }
    
    /**
     * Setter for property authorizeMap.
     * @param authorizeMap New value of property authorizeMap.
     */
    public void setAuthorizeMap(java.util.HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }
    
    /**
     * Getter for property tdtCommencementDate.
     * @return Value of property tdtCommencementDate.
     */
    public java.util.Date getTdtCommencementDate() {
        return tdtCommencementDate;
    }    
    
    /**
     * Setter for property tdtCommencementDate.
     * @param tdtCommencementDate New value of property tdtCommencementDate.
     */
    public void setTdtCommencementDate(java.util.Date tdtCommencementDate) {
        this.tdtCommencementDate = tdtCommencementDate;
    }    
    
    /**
     * Getter for property txtSchemeNameClosure.
     * @return Value of property txtSchemeNameClosure.
     */
    public java.lang.String getTxtSchemeNameClosure() {
        return txtSchemeNameClosure;
    }
    
    /**
     * Setter for property txtSchemeNameClosure.
     * @param txtSchemeNameClosure New value of property txtSchemeNameClosure.
     */
    public void setTxtSchemeNameClosure(java.lang.String txtSchemeNameClosure) {
        this.txtSchemeNameClosure = txtSchemeNameClosure;
    }
    
    /**
     * Getter for property MDSClosureMap.
     * @return Value of property MDSClosureMap.
     */
    public java.util.HashMap getMDSClosureMap() {
        return MDSClosureMap;
    }
    
    /**
     * Setter for property MDSClosureMap.
     * @param MDSClosureMap New value of property MDSClosureMap.
     */
    public void setMDSClosureMap(java.util.HashMap MDSClosureMap) {
        this.MDSClosureMap = MDSClosureMap;
    }

    public String getPredefinedInstallment() {
        return predefinedInstallment;
    }

    public void setPredefinedInstallment(String predefinedInstallment) {
        this.predefinedInstallment = predefinedInstallment;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getDiscountFirstInstScheme() {
        return discountFirstInstScheme;
    }

    public void setDiscountFirstInstScheme(String discountFirstInstScheme) {
        this.discountFirstInstScheme = discountFirstInstScheme;
    }   
    
    
}