/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * chargesServiceTaxOB.java
 *
 * Created on August 13, 2003, 4:30 PM
 */
package com.see.truetransact.ui.salaryrecovery;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import com.see.truetransact.clientproxy.ProxyParameters;
import java.util.List;
import java.util.Iterator;
import java.util.Date;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.transall.TransAllTO;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.TTException;
import org.apache.log4j.Logger;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.CommonRB;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.EnhancedTableModel;

/**
 *
 * @author Administrator Modified by Karthik
 */
public class TransAllOB extends CObservable {
    private final static Logger log = Logger.getLogger(TransAllOB.class);//Creating Instace of Log
    private ProxyFactory proxy;
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private static TransAllOB objOB;//Singleton Object Reference
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private ArrayList key, value;//ArrayList for populating the ComboBoxModel key-Storing keys value-for Storing Values
    private HashMap keyValue;//Which holds the resultset values as key,value pairs for populatine ComboBoxModel
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private List finalList = null;
    private TransactionOB transactionOB;
    private HashMap finalMap = new HashMap();
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private HashMap authMap = new HashMap();
    private String transallNo = "", txtClockNo = "", txtMemberNo = "", txtName = "", prodId = "" , schemeName = "",
            prodType = "", prodAccNo = "", chkRetired = "" , acNo = "";
    Double payingAmt, principal = null, penel, interest, totprincipal = null, totPenel, totInterest, totOthers = null, grandTotal;
    Date currDt = null;
    final ArrayList tableTitle = new ArrayList();
    private EnhancedTableModel tblSalaryRecoveryList;

    /**
     * Creates a new instance of chargesServiceTaxOB
     */
    public static TransAllOB getInstance() throws Exception {
        return objOB;
    }

    /**
     * Creates a new instance of NewBorrowingOB
     */
    public TransAllOB() {

        try {
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            currDt = ClientUtil.getCurrentDate();
            setTransAllTableTitle();
            tblSalaryRecoveryList = new EnhancedTableModel(null, tableTitle);
        } catch (Exception e) {
            //parseException.logException(e,true);
            //system.out.println("Error in NewBorrowingOB():" + e);
        }
    }

    static {
        try {
            log.info("Creating ParameterOB...");
            objOB = new TransAllOB();
        } catch (Exception e) {
            // parseException.logException(e,true);
            //system.out.println("Error in static():" + e);
        }
    }

//    public void insertTableData() {
//        try {
//            ArrayList rowList = new ArrayList();
//            ArrayList tableList = new ArrayList();
//            HashMap dataMap = new HashMap();
//            HashMap whereMap = new HashMap();
//            whereMap.put("MEMBER_NO", getTxtMemberNo());
//            //system.out.println("whereMap UNIIIIIIII===============" + whereMap);
//            HashMap proxyResultMap = proxy.executeQuery(whereMap, map);
//            //system.out.println("!@!@ proxyResultMap : " + proxyResultMap);
//            if (proxyResultMap != null && proxyResultMap.size() > 0) {
//                //system.out.println("!@!@ RECOVERY_LIST_TABLE_DATA : " + proxyResultMap.get("RECOVERY_LIST_TABLE_DATA"));
//                tableList = (ArrayList) proxyResultMap.get("RECOVERY_LIST_TABLE_DATA");
//                setFinalList(tableList);
//            }
//            System.out.println("final list"+finalList);
//            //system.out.println("#$# tableList:" + tableList);
//            tblSalaryRecoveryList = new EnhancedTableModel((ArrayList) tableList, tableTitle);
//        } catch (Exception e) {
//            e.printStackTrace();
//            parseException.logException(e, true);
//        }
//    }

    public java.util.HashMap getFinalMap() {
        return finalMap;
    }

    /**
     * Setter for property finalMap.
     *
     * @param finalMap New value of property finalMap.
     */
    public void setFinalMap(java.util.HashMap finalMap) {
        this.finalMap = finalMap;
    }

    public ArrayList getTableData() {
        try {
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("MEMBER_NO", getTxtMemberNo());
            whereMap.put("RETIRED", getchkRetired());
            //system.out.println("whereMap UNIIIIIIII===============" + whereMap);
            HashMap proxyResultMap = proxy.executeQuery(whereMap, map);
            System.out.println("!@!@ proxyResultMap : " + proxyResultMap);
            if (proxyResultMap != null && proxyResultMap.size() > 0) {
                //system.out.println("!@!@ RECOVERY_LIST_TABLE_DATA : " + proxyResultMap.get("RECOVERY_LIST_TABLE_DATA"));
                tableList = (ArrayList) proxyResultMap.get("RECOVERY_LIST_TABLE_DATA");
            }
            return tableList;
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
        return null;
    }

    /**
     * Getter for property finalList.
     *
     * @return Value of property finalList.
     */
    public java.util.List getFinalList() {
        return finalList;
    }

    /**
     * Setter for property finalList.
     *
     * @param finalList New value of property finalList.
     */
    public void setFinalList(java.util.List finalList) {
        this.finalList = finalList;
    }

    public void setTransAllTableTitle() {
        tableTitle.add("Select");
        tableTitle.add("Descrition");
        tableTitle.add("A/C No");
        tableTitle.add("Paying");
        tableTitle.add("Principal");
        tableTitle.add("Penal");
        tableTitle.add("Interest");
        tableTitle.add("Bonus");
        tableTitle.add("Others");
        tableTitle.add("Notice");//Notice
        tableTitle.add("Arbitration");//Arbitration
        tableTitle.add("prodId");//prodId
    }

    public java.util.HashMap getAuthMap() {
        return authMap;
    }

    /**
     * Setter for property authMap.
     *
     * @param authMap New value of property authMap.
     */
    public void setAuthMap(java.util.HashMap authMap) {
        this.authMap = authMap;
    }

    public java.util.Date getCurrDt() {
        return currDt;
    }

    /**
     * Setter for property currDt.
     *
     * @param currDt New value of property currDt.
     */
    public void setCurrDt(java.util.Date currDt) {
        this.currDt = currDt;
    }

    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "TransAllJNDI");
        map.put(CommonConstants.HOME, "transall.TransAllHome");
        map.put(CommonConstants.REMOTE, "transall.TransAll");

    }

    public void setAllowedTransactionDetailsTO(java.util.LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }

    public void populateData(HashMap whereMap) {
        HashMap mapData = null;
        try {
            //system.out.println("whereMap==" + whereMap);
            //system.out.println("map==" + map);
            mapData = proxy.executeQuery(whereMap, map);
            //system.out.println("objmapData==" + mapData);
            //ArrayList detailTOList = (ArrayList) (((HashMap) proxy.executeQuery(mapData, map)).get("TransAllTO"));
            ArrayList detailTOList = (ArrayList) mapData.get("TransAllTO");
            System.out.println("objTOobjTOobjTOobjTO==" + detailTOList);
            TransAllTO objTO = new TransAllTO();
            ArrayList temp;
            ArrayList data = new ArrayList();
            double principal = 0.0;
            double interest = 0.0;
            double penal = 0.0;
            double others = 0.0;
            double grandTotal = 0.0;
            String clockNo = "";
            String memberNo = "";
            String memberName = "";
            String retired = "";
            for (int i = 0; i < detailTOList.size(); i++) {
                objTO = (TransAllTO) detailTOList.get(i);
                temp = new ArrayList();
                clockNo = objTO.getClockNo();
                memberNo = objTO.getMemberNo();
                memberName = objTO.getCustName();
                retired = objTO.getRetired();
                temp.add(new Boolean(true));
                temp.add(objTO.getDescription());
                temp.add(objTO.getAcNo());
                temp.add(objTO.getPayingAmt());
                temp.add(objTO.getPrincipal());
                temp.add(objTO.getPenel());
                temp.add(objTO.getInterest());
                temp.add(objTO.getBonus());
                temp.add(objTO.getOthers());
                temp.add(objTO.getNotice());
                temp.add(objTO.getArbitration());
                temp.add(objTO.getProdId());
                data.add(temp);
                principal += objTO.getPrincipal();
                interest += objTO.getInterest();
                penal += objTO.getPenel();
                others += objTO.getOthers();
                //system.out.println("data : " + data);
            }
            setTxtClockNo(clockNo);
            setTxtMemberNo(memberNo);
            setTxtName(memberName);
            grandTotal = principal + interest + penal + others;
            setPrincipal(principal);
            setSchemeName(objTO.getSchName());
            setAcNo(objTO.getAcNo());
            setInterest(interest);
            setPenel(penel);
            setTotOthers(others);
            setGrandTotal(grandTotal);
            tblSalaryRecoveryList = new EnhancedTableModel((ArrayList) data, tableTitle);
            setTransAllTO(objTO);
            if (mapData.containsKey("TRANSACTION_LIST")) {
                List list = (List) mapData.get("TRANSACTION_LIST");
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    private void setTransAllTO(TransAllTO objTO) {
        //system.out.println("objTO setBorrowingTO ===" + objTO.getTransallId());
        setTransallNo(objTO.getTransallId());
        setTxtClockNo(objTO.getClockNo());
        setTxtMemberNo(objTO.getMemberNo());
        //  setProdType(objTO.getProdType());
        setProdId(objTO.getProdId());
        //  setProdAccNo(objTO.getProdAccNo());
        setchkRetired(objTO.getRetired());
        setPayingAmt(objTO.getPayingAmt());
        setPrincipal(objTO.getPrincipal());
        setPenel(objTO.getPenel());
        setInterest(objTO.getInterest());
        setTotprincipal(objTO.getTotprincipal());
        setTotPenel(objTO.getTotPenel());
        setTotInterest(objTO.getTotInterest());
        setGrandTotal(objTO.getGrandTotal());
        notifyObservers();
    }

    private TransAllTO getTransAllTO(String command) {
        TransAllTO objTO = new TransAllTO();
        objTO.setCommand(command);
        objTO.setTransallId(getTransallNo());
        objTO.setClockNo(getTxtClockNo());
        objTO.setSchName(getSchemeName());
        objTO.setAcNo(getAcNo());
        objTO.setMemberNo(getTxtMemberNo());
        objTO.setProdId(getProdId());
        objTO.setRetired(getchkRetired());
        objTO.setPayingAmt(getPayingAmt());
        objTO.setPrincipal(getPrincipal());
        objTO.setPenel(getPenel());
        objTO.setInterest(getInterest());
        objTO.setTotprincipal(getTotprincipal());
        objTO.setTotPenel(getTotPenel());
        objTO.setTotInterest(getTotInterest());
        objTO.setGrandTotal(getGrandTotal());
        objTO.setTotOthers(getTotOthers());
        //objTO.setDescription();
        objTO.setStatusBy(ProxyParameters.USER_ID);
        objTO.setStatusDt(currDt);
        objTO.setBranchId(ProxyParameters.BRANCH_ID);
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            objTO.setAuthorizeStatus("");
            objTO.setAuthorizeBy("");
            objTO.setAuthorizeDte(null);
            objTO.setStatus(CommonConstants.STATUS_CREATED);
        }
        return objTO;
    }

    public void resetForm() {
        setTransallNo("");
        setTxtClockNo("");
        setTxtMemberNo("");
        setProdType("");
        setProdId("");
        setProdAccNo("");
        setchkRetired("");
        setPayingAmt(null);
        setPrincipal(null);
        setPenel(null);
        setInterest(null);
        setTotprincipal(null);
        setTotPenel(null);
        setTotInterest(null);
        setGrandTotal(null);
        resetTable();
        notifyObservers();
    }

    public void resetTable() {
        tblSalaryRecoveryList = new EnhancedTableModel(null, tableTitle);
    }

    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }

    /**
     * To update the Status based on result performed by doAction() method
     */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }

    public int getResult() {
        return _result;
    }

    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            term.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            term.put("MODE", command);
            if (!command.equals(CommonConstants.AUTHORIZESTATUS)) {
                term.put("TransAllTO", getTransAllTO(command));
                if (transactionDetailsTO == null) {
                    transactionDetailsTO = new LinkedHashMap();
                }
                if (deletedTransactionDetailsTO != null) {
                    transactionDetailsTO.put(DELETED_TRANS_TOs, deletedTransactionDetailsTO);
                    deletedTransactionDetailsTO = null;
                }
                transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
                allowedTransactionDetailsTO = null;
                term.put("TransactionTO", transactionDetailsTO);
            }
            //system.out.println("getFinalMap() =================" + getFinalMap());
            if (getFinalMap() != null && getFinalMap().size() > 0) {
                term.put("RECOVERY_PROCESS_LIST", getFinalMap());
            }
            term.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            //system.out.println("Data in RecoveryList Generation OB : " + term);
            finalMap = null;
            if (getAuthMap() != null && getAuthMap().size() > 0) {
                if (getAuthMap() != null) {
                    term.put(CommonConstants.AUTHORIZEMAP, getAuthMap());
                }
                if (allowedTransactionDetailsTO != null && allowedTransactionDetailsTO.size() > 0) {
                    if (transactionDetailsTO == null) {
                        transactionDetailsTO = new LinkedHashMap();
                    }
                    transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
                    term.put("TransactionTO", transactionDetailsTO);
                    allowedTransactionDetailsTO = null;
                }
                authMap = null;
            }
            HashMap proxyReturnMap = proxy.execute(term, map);
            //system.out.println("proxyReturnMap INN===================" + proxyReturnMap);
            setProxyReturnMap(proxyReturnMap);
            setResult(getActionType());
             if (proxyReturnMap != null && proxyReturnMap.containsKey("ERRORLIST")) {
                String errMessage = "";
                String msg = CommonUtil.convertObjToStr(proxyReturnMap.get("ERRORLIST"));
                if (msg.length() > 0) {
                    errMessage = errMessage + msg + "\n";
                }
                if (errMessage.length() > 0) {
                    ClientUtil.showAlertWindow(errMessage);
                    setResult(ClientConstants.ACTIONTYPE_FAILED);
                    return;
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            //system.out.println("Error in execute():" + e);
        }
    }

    public int getActionType() {
        return this._actionType;
    }

    public void setActionType(int actionType) {
        this._actionType = actionType;
        setChanged();
    }

    public String getLblStatus() {
        return this.lblStatus;
    }

    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }

    /**
     * To set the status based on ActionType, either New, Edit, etc.,
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        //  ttNotifyObservers();
    }

    public void setTransactionOB(com.see.truetransact.ui.common.transaction.TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }

    /**
     * Getter for property transallNo.
     *
     * @return Value of property transallNo.
     */
    public String getTransallNo() {
        return transallNo;
    }

    /**
     * Setter for property transallNo.
     *
     * @param transallNo New value of property transallNo.
     */
    public void setTransallNo(String transallNo) {
        this.transallNo = transallNo;
    }

    /**
     * Getter for property txtClockNo.
     *
     * @return Value of property txtClockNo.
     */
    public String getTxtClockNo() {
        return txtClockNo;
    }

    /**
     * Setter for property txtClockNo.
     *
     * @param txtClockNo New value of property txtClockNo.
     */
    public void setTxtClockNo(String txtClockNo) {
        this.txtClockNo = txtClockNo;
    }

    /**
     * Getter for property txtMemberNo.
     *
     * @return Value of property txtMemberNo.
     */
    public String getTxtMemberNo() {
        return txtMemberNo;
    }

    /**
     * Setter for property txtMemberNo.
     *
     * @param txtMemberNo New value of property txtMemberNo.
     */
    public void setTxtMemberNo(String txtMemberNo) {
        this.txtMemberNo = txtMemberNo;
    }

    /**
     * Getter for property prodId.
     *
     * @return Value of property prodId.
     */
    public java.lang.String getProdId() {
        return prodId;
    }

    /**
     * Setter for property prodId.
     *
     * @param prodId New value of property prodId.
     */
    public void setProdId(java.lang.String prodId) {
        this.prodId = prodId;
    }

    /**
     * Getter for property prodType.
     *
     * @return Value of property prodType.
     */
    public String getProdType() {
        return prodType;
    }

    /**
     * Setter for property prodType.
     *
     * @param prodType New value of property prodType.
     */
    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    /**
     * Getter for property prodAccNo.
     *
     * @return Value of property prodAccNo.
     */
    public java.lang.String getProdAccNo() {
        return prodAccNo;
    }

    /**
     * Setter for property prodAccNo.
     *
     * @param prodAccNo New value of property prodAccNo.
     */
    public void setProdAccNo(java.lang.String prodAccNo) {
        this.prodAccNo = prodAccNo;
    }

    public java.lang.String getchkRetired() {
        return chkRetired;
    }

    /**
     * Setter for property prodAccNo.
     *
     * @param prodAccNo New value of property prodAccNo.
     */
    public void setchkRetired(java.lang.String chkRetired) {
        this.chkRetired = chkRetired;
    }

    /**
     * Getter for property payingAmt.
     *
     * @return Value of property payingAmt.
     */
    public Double getPayingAmt() {
        return payingAmt;
    }

    /**
     * Setter for property payingAmt.
     *
     * @param payingAmt New value of property payingAmt.
     */
    public void setPayingAmt(Double payingAmt) {
        this.payingAmt = payingAmt;
    }

    /**
     * Getter for property penel.
     *
     * @return Value of property penel.
     */
    public java.lang.Double getPenel() {
        return penel;
    }

    /**
     * Setter for property penel.
     *
     * @param penel New value of property penel.
     */
    public void setPenel(Double penel) {
        this.penel = penel;
    }

    /**
     * Getter for property interest.
     *
     * @return Value of property interest.
     */
    public java.lang.Double getInterest() {
        return interest;
    }

    /**
     * Setter for property interest.
     *
     * @param interest New value of property interest.
     */
    public void setInterest(java.lang.Double interest) {
        this.interest = interest;
    }

    /**
     * Getter for property totPenel.
     *
     * @return Value of property totPenel.
     */
    public Double getTotPenel() {
        return totPenel;
    }

    /**
     * Setter for property totPenel.
     *
     * @param totPenel New value of property totPenel.
     */
    public void setTotPenel(Double totPenel) {
        this.totPenel = totPenel;
    }

    /**
     * Getter for property totInterest.
     *
     * @return Value of property totInterest.
     */
    public java.lang.Double getTotInterest() {
        return totInterest;
    }

    /**
     * Setter for property totInterest.
     *
     * @param totInterest New value of property totInterest.
     */
    public void setTotInterest(java.lang.Double totInterest) {
        this.totInterest = totInterest;
    }

    /**
     * Getter for property grandTotal.
     *
     * @return Value of property grandTotal.
     */
    public Double getGrandTotal() {
        return grandTotal;
    }

    /**
     * Setter for property grandTotal.
     *
     * @param grandTotal New value of property grandTotal.
     */
    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }

    /**
     * Getter for property principal.
     *
     * @return Value of property principal.
     */
    public Double getPrincipal() {
        return principal;
    }

    /**
     * Setter for property principal.
     *
     * @param principal New value of property principal.
     */
    public void setPrincipal(Double principal) {
        this.principal = principal;
    }

    /**
     * Getter for property totprincipal.
     *
     * @return Value of property totprincipal.
     */
    public java.lang.Double getTotprincipal() {
        return totprincipal;
    }

    /**
     * Setter for property totprincipal.
     *
     * @param totprincipal New value of property totprincipal.
     */
    public void setTotprincipal(java.lang.Double totprincipal) {
        this.totprincipal = totprincipal;
    }

    /**
     * Getter for property txtName.
     *
     * @return Value of property txtName.
     */
    public String getTxtName() {
        return txtName;
    }

    /**
     * Setter for property txtName.
     *
     * @param txtName New value of property txtName.
     */
    public void setTxtName(String txtName) {
        this.txtName = txtName;
    }

    /**
     * Getter for property totOthers.
     *
     * @return Value of property totOthers.
     */
    public Double getTotOthers() {
        return totOthers;
    }

    /**
     * Setter for property totOthers.
     *
     * @param totOthers New value of property totOthers.
     */
    public void setTotOthers(Double totOthers) {
        this.totOthers = totOthers;
    }

    /**
     * Setter for property totprincipal.
     *
     * @param totprincipal New value of property totprincipal.
     */
    /**
     * Getter for property tblSalaryRecoveryList.
     *
     * @return Value of property tblSalaryRecoveryList.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblSalaryRecoveryList() {
        return tblSalaryRecoveryList;
    }

    /**
     * Setter for property tblSalaryRecoveryList.
     *
     * @param tblSalaryRecoveryList New value of property tblSalaryRecoveryList.
     */
    public void setTblSalaryRecoveryList(com.see.truetransact.clientutil.EnhancedTableModel tblSalaryRecoveryList) {
        this.tblSalaryRecoveryList = tblSalaryRecoveryList;
    }
    
    /**
     * Getter for property schemeName.
     *
     * @return Value of property schemeName.
     */
    public String getSchemeName() {
        return schemeName;
    }

    /**
     * Setter for property schemeName.
     *
     * @param schemeName New value of property schemeName.
     */
    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }
    
    /**
     * Getter for property acNo.
     *
     * @return Value of property acNo.
     */
    public String getAcNo() {
        return acNo;
    }

    /**
     * Setter for property acNo.
     *
     * @param acNo New value of property acNo.
     */
    public void setAcNo(String acNo) {
        this.acNo = acNo;
    }

}
