/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * PurchaseEntryOB.java
 */
package com.see.truetransact.ui.trading.purchaseentry;

import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.transferobject.trading.purchaseentry.PurchaseEntryTO;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.trading.tradingpurchase.TradingPurchaseTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CTable;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 *
 * @author  Revathi L
 */
public class PurchaseEntryOB extends CObservable {

    
    private static SqlMap sqlMap = null;
    Date currDt = null;
    private final static Logger log = Logger.getLogger(PurchaseEntryOB.class);
    private ProxyFactory proxy;
    private int _actionType;
    private int _result;
    private static PurchaseEntryOB objOB;
    private HashMap map;
    private int operation;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private HashMap authMap = new HashMap();
    private HashMap lookUpHash;
    private ArrayList key, value;
    private HashMap keyValue;
    private String cboTransType = "";
    private String cboTransMode = "";
    private ComboBoxModel cbmTransMode;
    private ComboBoxModel cbmTransType;
    private String prodID = "";
    private String supplierID = "";
    private String purchaseEntryID = "";
    private String creditFrom = "";
    private String prodType = "";
    private String  acHD= "";
    private String  cash= "";
    private String  purchComm= "";
    private String  purchaseRet= "";
    private String  sundryAmt= "";
    private String  chequeAmt= "";
    private String  totalAmt= "";
    private String txtChequeNo = "";
    private String txtNarration = "";
    private HashMap authorizeMap;
    private PurchaseEntryTO objPurchaseEntryTO;
    private String cashAmt = "";
    private String purchaseRetId = "";
   

    

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public String getCboTransType() {
        return cboTransType;
    }

    public void setCboTransType(String cboTransType) {
        this.cboTransType = cboTransType;
    }

    public String getTxtChequeNo() {
        return txtChequeNo;
    }

    public void setTxtChequeNo(String txtChequeNo) {
        this.txtChequeNo = txtChequeNo;
    }

    public String getTxtNarration() {
        return txtNarration;
    }

    public void setTxtNarration(String txtNarration) {
        this.txtNarration = txtNarration;
    }


    public String getCboTransMode() {
        return cboTransMode;
    }

    public void setCboTransMode(String cboTransMode) {
        this.cboTransMode = cboTransMode;
    }

    

    public PurchaseEntryOB() {

        try {
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            fillDropdown();
        } catch (Exception e) {
        }
    }

    static {
        try {
            log.info("Creating ParameterOB...");
            objOB = new PurchaseEntryOB();
        } catch (Exception e) {
            System.out.println("Error in static():" + e);
        }

    }

    

    private void fillDropdown() throws Exception {
        HashMap param = new HashMap();
        param.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookupKey = new ArrayList();
        HashMap where = new HashMap();
        key = new ArrayList();
        value = new ArrayList();
        
        param.put(CommonConstants.MAP_NAME, null);
        lookupKey.add("PURCHASE_TRANS_MODE");
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        HashMap lookupValues = ClientUtil.populateLookupData(param);
        fillData((HashMap) lookupValues.get("PURCHASE_TRANS_MODE"));
        cbmTransMode = new ComboBoxModel(key, value);

        param.put(CommonConstants.MAP_NAME, null);
        lookupKey.add("PURCHASE_TYPE");
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        lookupValues = ClientUtil.populateLookupData(param);
        fillData((HashMap) lookupValues.get("PURCHASE_TYPE"));
        cbmTransType = new ComboBoxModel(key, value);
        
        param.put(CommonConstants.MAP_NAME, null);
        lookupKey.add("PURCHASE_TYPE");
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        lookupValues = ClientUtil.populateLookupData(param);
        fillData((HashMap) lookupValues.get("PURCHASE_TYPE"));
        cbmTransType = new ComboBoxModel(key, value);
        
        key = null;
        value = null;
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    private void makeNull() {
        key = null;
        value = null;
    }

//    public void setCbmProdId(String prodType) {
//        if (CommonUtil.convertObjToStr(prodType).length() > 1) {
//            if (prodType.equals("GL")) {
//                key = new ArrayList();
//                value = new ArrayList();
//            } else {
//                try {
//                    lookUpHash = new HashMap();
//                    lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + prodType);
//                    lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
//                    keyValue = ClientUtil.populateLookupData(lookUpHash);
//                    getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//        } else {
//            key = new ArrayList();
//            value = new ArrayList();
//            key.add("");
//            value.add("");
//        }
//        cbmProdId = new ComboBoxModel(key, value);
//        this.cbmProdId = cbmProdId;
//        setChanged();
//    }

    private void setOperationMap() throws Exception {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "PurchaseEntryJNDI");
        map.put(CommonConstants.HOME, "purcahseentry.PurchaseEntryHome");
        map.put(CommonConstants.REMOTE, "purcahseentry.PurchaseEntry");
    }

    public static PurchaseEntryOB getInstance() throws Exception {
        return objOB;
    }

    public int getActionType() {
        return _actionType;
    }

    public void setActionType(int actionType) {
        _actionType = actionType;
        setStatus();
        setChanged();
    }

    public void setStatus() {
    }

    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }

    public String getLblStatus() {
        return lblStatus;
    }

    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
    }

   
  
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }

    public int getResult() {
        return _result;
    }


   
    public void resetForm() {
       setPurchaseEntryID("");
       setSupplierID("");
       setCboTransMode("");
       setCboTransType("");
       setCreditFrom("");
       setProdType("");
       setProdID("");
       setAcHD("");
       setCash("");
       setPurchComm("");
       setPurchaseRet("");
       setSundryAmt("");
       setChequeAmt("");
       setTotalAmt("");
       setTxtChequeNo("");
       setTxtNarration("");
       setCashAmt("");
    }
    
    public void doAction() {
        try {
            if (getActionType() != ClientConstants.ACTIONTYPE_CANCEL || getAuthorizeMap() != null) {
                doActionPerform();
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    private void doActionPerform() throws Exception {
        final HashMap data = new HashMap();
        data.put("COMMAND", getCommand());
        data.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        if (getAuthorizeMap() == null) {
            data.put("objPurchaseEntryTO", setPurchaseEntryTO());
        } else {
            if(objPurchaseEntryTO!=null){
            data.put("objPurchaseEntryTO", objPurchaseEntryTO);
            }
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
        }
        System.out.println("data in Sales OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        authorizeMap = null;
        setResult(getActionType());
    }
    
     private String getCommand() {
        String command = null;
        switch (_actionType) {
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
        return command;
    }
     
     private PurchaseEntryTO setPurchaseEntryTO() {
        PurchaseEntryTO objPurchaseEntryTO = new PurchaseEntryTO();
        objPurchaseEntryTO.setPurchaseEntryID(CommonUtil.convertObjToStr(getPurchaseEntryID()));
        objPurchaseEntryTO.setSupplierID(CommonUtil.convertObjToStr(getSupplierID()));
        objPurchaseEntryTO.setTransMode(CommonUtil.convertObjToStr(getCbmTransMode().getKeyForSelected()));
        objPurchaseEntryTO.setTransType(CommonUtil.convertObjToStr(getCbmTransType().getKeyForSelected()));
        objPurchaseEntryTO.setCreditFrom(CommonUtil.convertObjToStr(getCreditFrom()));
        objPurchaseEntryTO.setProdType(CommonUtil.convertObjToStr(getProdType()));
        objPurchaseEntryTO.setProdID(CommonUtil.convertObjToStr(getProdID()));
        objPurchaseEntryTO.setAcHd(CommonUtil.convertObjToStr(getAcHD()));
        objPurchaseEntryTO.setCash(CommonUtil.convertObjToStr(getCash()));
        objPurchaseEntryTO.setPurchaseComm(CommonUtil.convertObjToStr(getPurchComm()));
        objPurchaseEntryTO.setPurchaseReturn(CommonUtil.convertObjToStr(getPurchaseRet()));
        objPurchaseEntryTO.setSundryAmt(CommonUtil.convertObjToStr(getSundryAmt()));
        objPurchaseEntryTO.setChequeAmt(CommonUtil.convertObjToStr(getChequeAmt()));
        objPurchaseEntryTO.setTotalAmt(CommonUtil.convertObjToStr(getTotalAmt()));
        objPurchaseEntryTO.setChequeNo(CommonUtil.convertObjToStr(getTxtChequeNo()));
        objPurchaseEntryTO.setNarration(CommonUtil.convertObjToStr(getTxtNarration()));
        objPurchaseEntryTO.setPurchaseRetID(CommonUtil.convertObjToStr(getPurchaseRetId()));
        if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            objPurchaseEntryTO.setStatus(CommonConstants.STATUS_MODIFIED);
        } else {
            objPurchaseEntryTO.setStatus(CommonConstants.STATUS_CREATED);
        }
        objPurchaseEntryTO.setStatusBy(TrueTransactMain.USER_ID);
        objPurchaseEntryTO.setBranch_code(TrueTransactMain.BRANCH_ID);
        return objPurchaseEntryTO;
    }
     
     public void getData(HashMap whereMap) {
        try {
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
            System.out.println("#@@%@@#%@#data" + data);
            if (data != null && data.containsKey("objPurchaseEntryTO")) {
                 objPurchaseEntryTO = (PurchaseEntryTO) ((List) data.get("objPurchaseEntryTO")).get(0);
                populatePurchaseEntryData(objPurchaseEntryTO);
            }
             HashMap resultMap = new HashMap();
            if (data.containsKey("TRANSFER_TRANS_LIST")) {
                List transList = (List) data.get("TRANSFER_TRANS_LIST");
                resultMap.put("TRANSFER_TRANS_LIST", transList);
                setProxyReturnMap(resultMap);
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
     
     public void populatePurchaseEntryData(PurchaseEntryTO objPurchaseEntryTO) {
        HashMap mapData = null;
        try {
            setPurchaseEntryID(CommonUtil.convertObjToStr(objPurchaseEntryTO.getPurchaseEntryID()));
            setSupplierID(CommonUtil.convertObjToStr(objPurchaseEntryTO.getSupplierID()));
            setCboTransMode(CommonUtil.convertObjToStr(objPurchaseEntryTO.getTransMode()));
            setCboTransType(CommonUtil.convertObjToStr(objPurchaseEntryTO.getTransType()));
            setProdType(CommonUtil.convertObjToStr(objPurchaseEntryTO.getProdType()));
            setProdID(CommonUtil.convertObjToStr(objPurchaseEntryTO.getProdID()));
            setAcHD(CommonUtil.convertObjToStr(objPurchaseEntryTO.getAcHd()));
            setCash(CommonUtil.convertObjToStr(objPurchaseEntryTO.getCash()));
            if(CommonUtil.convertObjToStr(getCash()).length()>0){
                setCashAmt(CommonUtil.convertObjToStr(getCash()));
            }
            setPurchComm(CommonUtil.convertObjToStr(objPurchaseEntryTO.getPurchaseComm()));
            setPurchaseRet(CommonUtil.convertObjToStr(objPurchaseEntryTO.getPurchaseReturn()));
             if(CommonUtil.convertObjToStr(getPurchaseRet()).length()>0){
                setCashAmt(CommonUtil.convertObjToStr(getPurchaseRet()));
            }
            setSundryAmt(CommonUtil.convertObjToStr(objPurchaseEntryTO.getSundryAmt()));
            if (CommonUtil.convertObjToStr(getCboTransType()).equals("Transfer")) {
                if (objPurchaseEntryTO.getCreditFrom().equals("O")) {
                    setCreditFrom("O");
                } else {
                    setCreditFrom("S");
                }
            }
            setChequeAmt(CommonUtil.convertObjToStr(objPurchaseEntryTO.getChequeAmt()));
            setTotalAmt(CommonUtil.convertObjToStr(objPurchaseEntryTO.getTotalAmt()));
            setTxtChequeNo(CommonUtil.convertObjToStr(objPurchaseEntryTO.getChequeNo()));
            setTxtNarration(CommonUtil.convertObjToStr(objPurchaseEntryTO.getNarration()));
            setPurchaseRetId(CommonUtil.convertObjToStr(objPurchaseEntryTO.getPurchaseRetID()));
            
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);

        }
    }

    public java.util.HashMap getAuthMap() {
        return authMap;
    }

    
    public void setAuthMap(java.util.HashMap authMap) {
        this.authMap = authMap;
    }

    public java.util.Date getCurrDt() {
        return currDt;
    }

   
    public void setCurrDt(java.util.Date currDt) {
        this.currDt = currDt;
    }

    public ComboBoxModel getCbmTransMode() {
        return cbmTransMode;
    }

    public void setCbmTransMode(ComboBoxModel cbmTransMode) {
        this.cbmTransMode = cbmTransMode;
    }

    public ComboBoxModel getCbmTransType() {
        return cbmTransType;
    }

    public void setCbmTransType(ComboBoxModel cbmTransType) {
        this.cbmTransType = cbmTransType;
    }

    public HashMap getAuthorizeMap() {
        return authorizeMap;
    }

    public void setAuthorizeMap(HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }

    public String getProdID() {
        return prodID;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getProdType() {
        return prodType;
    }

    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public String getAcHD() {
        return acHD;
    }

    public void setAcHD(String acHD) {
        this.acHD = acHD;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getPurchComm() {
        return purchComm;
    }

    public void setPurchComm(String purchComm) {
        this.purchComm = purchComm;
    }

    public String getPurchaseRet() {
        return purchaseRet;
    }

    public void setPurchaseRet(String purchaseRet) {
        this.purchaseRet = purchaseRet;
    }

    public String getSundryAmt() {
        return sundryAmt;
    }

    public void setSundryAmt(String sundryAmt) {
        this.sundryAmt = sundryAmt;
    }

    public String getChequeAmt() {
        return chequeAmt;
    }

    public void setChequeAmt(String chequeAmt) {
        this.chequeAmt = chequeAmt;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getCreditFrom() {
        return creditFrom;
    }

    public void setCreditFrom(String creditFrom) {
        this.creditFrom = creditFrom;
    }

    public String getPurchaseEntryID() {
        return purchaseEntryID;
    }

    public void setPurchaseEntryID(String purchaseEntryID) {
        this.purchaseEntryID = purchaseEntryID;
    }

    private void ttNotifyObservers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getCashAmt() {
        return cashAmt;
    }

    public void setCashAmt(String cashAmt) {
        this.cashAmt = cashAmt;
    }

    public String getPurchaseRetId() {
        return purchaseRetId;
    }

    public void setPurchaseRetId(String purchaseRetId) {
        this.purchaseRetId = purchaseRetId;
    }
    
    
}
