/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TradingAcHeadOB.java
 *
 * Created on Mon Mar 09 16:05:07 IST 2015
 */
package com.see.truetransact.ui.trading.tradingachead;

import java.util.Observable;
import org.apache.log4j.Logger;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.trading.tradingachead.TradingacheadTO;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 *
 * @author Revathi L
 */
public class TradingAcHeadOB extends CObservable {

    Date curDate = null;
    private String txtCashOnHand = "";
    private String txtPurchase = "";
    private String txtSales = "";
    private String txtPurchaseReturn = "";
    private String txtSalesReturn = "";
    private String txtDamages = "";
    private String txtPurchaseVAT = "";
    private String txtSalesVAT = "";
    private String txtSAReceivable = "";
    private String txtSLPayable = "";
    private String txtValue = "";
    private String txtStock = "";
    private String cboPeriod = "";
    private ComboBoxModel cbmPeriod;
    private ArrayList key, value;
    private ProxyFactory proxy;
    private static TradingAcHeadOB objTradingAcHeadOB;
    private HashMap map, keyValue, lookUpHash;
    private final static Logger log = Logger.getLogger(TradingAcHeadOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int _result, _actionType;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final String YES = "Y";
    private final String NO = "N";
    private HashMap authorizeMap;
    private TradingacheadTO objTradingacheadTO;
    private String acHdPid = "";
    private int viewType = 0;
    private HashMap operationMap;
     private List acHdLst = null;
     private boolean isAuth = false;

    /**
     * Consturctor Declaration for TDSConfigOB
     */
    private TradingAcHeadOB() {
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            initUIComboBoxModel();
            fillDropdown();
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    static {
        try {
            log.info("Creating ParameterOB...");
            objTradingAcHeadOB = new TradingAcHeadOB();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "TradingAcHeadJNDI");
        map.put(CommonConstants.HOME, "trading.tradingachead.TradingAcHeadHome");
        map.put(CommonConstants.REMOTE, "trading.tradingachead.TradingAcHead");
    }

    /**
     * Creating instance for ComboboxModel cbmTokenType
     */
    private void initUIComboBoxModel() {
    }

    /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception {
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("SALE_DAYS");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("SALE_DAYS"));
        cbmPeriod = new ComboBoxModel(key,value);
    }
    
    private void getMap(List list) throws Exception {
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        for (int i = 0, j = list.size(); i < j; i++) {
            key.add(((HashMap) list.get(i)).get("KEY"));
            value.add(((HashMap) list.get(i)).get("VALUE"));
        }
    }
    /**
     * Populates two ArrayList key,value
     */
    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    /**
     * Returns an instance of TokenConfigOB.
     *
     * @return TokenConfigOB
     */
    public static TradingAcHeadOB getInstance() throws Exception {
        return objTradingAcHeadOB;
    }

    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }

    public int getActionType() {
        return _actionType;
    }

    public void setActionType(int actionType) {
        _actionType = actionType;
        setStatus();
        setChanged();
    }

    /**
     * To set the status based on ActionType, either New, Edit, etc.,
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
    }

    /**
     * To update the Status based on result performed by doAction() method
     */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }

    /**
     * Getter for property lblStatus.
     *
     * @return Value of property lblStatus.
     *
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }

    /**
     * Setter for property lblStatus.
     *
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }

    public void resetStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }

    public int getResult() {
        return _result;
    }

    public String getAcHdPid() {
        return acHdPid;
    }

    public void setAcHdPid(String acHdPid) {
        this.acHdPid = acHdPid;
    }

    public String getTxtCashOnHand() {
        return txtCashOnHand;
    }

    public void setTxtCashOnHand(String txtCashOnHand) {
        this.txtCashOnHand = txtCashOnHand;
    }

    public String getTxtPurchase() {
        return txtPurchase;
    }

    public void setTxtPurchase(String txtPurchase) {
        this.txtPurchase = txtPurchase;
    }

    public String getTxtSales() {
        return txtSales;
    }

    public void setTxtSales(String txtSales) {
        this.txtSales = txtSales;
    }

    public String getTxtPurchaseReturn() {
        return txtPurchaseReturn;
    }

    public void setTxtPurchaseReturn(String txtPurchaseReturn) {
        this.txtPurchaseReturn = txtPurchaseReturn;
    }

    public String getTxtSalesReturn() {
        return txtSalesReturn;
    }

    public void setTxtSalesReturn(String txtSalesReturn) {
        this.txtSalesReturn = txtSalesReturn;
    }

    public String getTxtDamages() {
        return txtDamages;
    }

    public void setTxtDamages(String txtDamages) {
        this.txtDamages = txtDamages;
    }

    public String getTxtPurchaseVAT() {
        return txtPurchaseVAT;
    }

    public void setTxtPurchaseVAT(String txtPurchaseVAT) {
        this.txtPurchaseVAT = txtPurchaseVAT;
    }

    public String getTxtSalesVAT() {
        return txtSalesVAT;
    }

    public void setTxtSalesVAT(String txtSalesVAT) {
        this.txtSalesVAT = txtSalesVAT;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public boolean isIsAuth() {
        return isAuth;
    }

    public void setIsAuth(boolean isAuth) {
        this.isAuth = isAuth;
    }
    
    
    
    
    /**
     * Returns an instance of TradingacheadTO by setting all the varibales of it *
     */
    private TradingacheadTO getTradingacheadTO(String command) {
        TradingacheadTO objTradingacheadTO = new TradingacheadTO();
        return objTradingacheadTO;
    }

    /**
     * sets the values of TDSConfigTo variables to TDSConfigOB Variables *
     */
    private void setTradingacheadTO(TradingacheadTO objTradingacheadTO) {
        notifyObservers();

    }

    /**
     * Clear up all the Fields of UI thru OB *
     */
    public void resetForm() {
        setTxtCashOnHand("");
        setTxtPurchase("");
        setTxtSales("");
        setTxtPurchaseReturn("");
        setTxtSalesReturn("");
        setTxtDamages("");
        setTxtPurchaseVAT("");
        setTxtSalesVAT("");
        setTxtSAReceivable("");
        setTxtSLPayable("");
        setTxtStock("");
        notifyObservers();
    }

    /* Populates the TO object by executing a Query */
    
    public void getData() {
        try {
            HashMap whereMap = new HashMap();
            HashMap mapName = new HashMap();
            
            mapName.put(CommonConstants.MAP_NAME,"getSelectTradingAcHd");
            
            if(getViewType() != 0){
                whereMap.put(CommonConstants.USER_ID,TrueTransactMain.USER_ID);
            }
            
            mapName.put(CommonConstants.MAP_WHERE,whereMap);
            
            HashMap mapData = proxy.executeQuery(mapName, map);
            mapName = null;
            
            populateOB(mapData);
            ttNotifyObservers();
            mapData = null;
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    private void populateOB(HashMap mapData) throws Exception {
        log.info("In populateOB...");
        if (mapData != null && mapData.size() > 0) {
            acHdLst = (List) mapData.get("objTradingAcHdTO");
            if (acHdLst != null && acHdLst.size() > 0) {
                setLblStatus(ClientConstants.ACTION_STATUS[2]);
                objTradingacheadTO = (TradingacheadTO) acHdLst.get(0);
                populateTradingAcHdData(objTradingacheadTO);
            }
        } else {
            setIsAuth(true);
            setLblStatus(ClientConstants.ACTION_STATUS[1]);
            resetForm();
        }
    }
    

    public void doAction() {
        try {
//            if( getActionType() != ClientConstants.ACTIONTYPE_CANCEL || getAuthorizeMap() != null){
//                doActionPerform();
//            }
//        } catch (Exception e) {
//            setResult(ClientConstants.ACTIONTYPE_FAILED);
//            parseException.logException(e,true);
//        }
            final HashMap data = new HashMap();
            data.put("COMMAND", getCommand());
            if (getAuthorizeMap() == null) {
                if (isPresent()) {
                    data.put("TOSTATUS", CommonConstants.TOSTATUS_UPDATE);
                    setLblStatus(ClientConstants.RESULT_STATUS[2]);
                } else {
                    data.put("TOSTATUS", CommonConstants.TOSTATUS_INSERT);
                    setLblStatus(ClientConstants.RESULT_STATUS[1]);
                }
                data.put("objTradingAcHdTO", setTradingAcHeadTO());
            } else {
                data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            }
            HashMap proxyResultMap = proxy.execute(data, map);
            setProxyReturnMap(proxyResultMap);
            authorizeMap = null;
            setResult(getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }
    
    private void doActionPerform() throws Exception{
        final HashMap data = new HashMap();
        data.put("COMMAND",getCommand());
        if(getAuthorizeMap() == null){
            if (isPresent()) {
                data.put("TOSTATUS", CommonConstants.TOSTATUS_UPDATE);
                setLblStatus(ClientConstants.RESULT_STATUS[2]);
            } else {
                data.put("TOSTATUS", CommonConstants.TOSTATUS_INSERT);
                setLblStatus(ClientConstants.RESULT_STATUS[1]);
            }
            data.put("objTradingAcHdTO", setTradingAcHeadTO());
        }
        else{
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
        }
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        authorizeMap = null;
        setResult(getActionType());
    }
    
    private boolean isPresent() {
        boolean recordExist = false;
        if (acHdLst != null && acHdLst.size()>0) {
            recordExist = true;
        } else {
            recordExist = false;
        }
        return recordExist;
    }
    
    public void verifyTradingAcctHead(com.see.truetransact.uicomponent.CTextField accountHead, String mapName) { 
        try {
            final HashMap data = new HashMap();
            data.put("ACCT_HD", accountHead.getText());
            data.put(CommonConstants.MAP_NAME, mapName);
            HashMap proxyResultMap = proxy.execute(data, operationMap);
        } catch (Exception e) {
            accountHead.setText("");
            parseException.logException(e, true);
        }
    }
    
    private TradingacheadTO setTradingAcHeadTO(){
        TradingacheadTO objTradingacheadTO = new TradingacheadTO();
        objTradingacheadTO.setAccountHeadPid(CommonUtil.convertObjToStr(getAcHdPid()));
        objTradingacheadTO.setCashOnHand(CommonUtil.convertObjToStr(getTxtCashOnHand()));
        objTradingacheadTO.setPurchase(CommonUtil.convertObjToStr(getTxtPurchase()));
        objTradingacheadTO.setSales(CommonUtil.convertObjToStr(getTxtSales()));
        objTradingacheadTO.setPurchaseReturn(CommonUtil.convertObjToStr(getTxtPurchaseReturn()));
        objTradingacheadTO.setSalesReturn(CommonUtil.convertObjToStr(getTxtSalesReturn()));
        objTradingacheadTO.setDamages(CommonUtil.convertObjToStr(getTxtDamages()));
        objTradingacheadTO.setPurchaseVAT(CommonUtil.convertObjToStr(getTxtPurchaseVAT()));
        objTradingacheadTO.setSalesVAT(CommonUtil.convertObjToStr(getTxtSalesVAT()));
        objTradingacheadTO.setSaReceivable(CommonUtil.convertObjToStr(getTxtSAReceivable()));
        objTradingacheadTO.setSlPayable(CommonUtil.convertObjToStr(getTxtSLPayable()));
        objTradingacheadTO.setValue(CommonUtil.convertObjToStr(getTxtValue()));
        objTradingacheadTO.setStock(CommonUtil.convertObjToStr(getTxtStock()));
        objTradingacheadTO.setPeriod(CommonUtil.convertObjToStr(cbmPeriod.getKeyForSelected()));
        objTradingacheadTO.setStatus(getAction());
        objTradingacheadTO.setAuthorizeStatus("");
        objTradingacheadTO.setStatusBy(TrueTransactMain.USER_ID);
        objTradingacheadTO.setStatusDt(CommonUtil.convertObjToStr(curDate));
        return objTradingacheadTO;
    }
    
    public void populateTradingAcHdData(TradingacheadTO objTradingacheadTO) {
        HashMap mapData=null;
        try {
            setAcHdPid(CommonUtil.convertObjToStr(objTradingacheadTO.getAccountHeadPid()));
            setTxtCashOnHand(CommonUtil.convertObjToStr(objTradingacheadTO.getCashOnHand()));
            setTxtPurchase(CommonUtil.convertObjToStr(objTradingacheadTO.getPurchase()));
            setTxtSales(CommonUtil.convertObjToStr(objTradingacheadTO.getSales()));
            setTxtPurchaseReturn(CommonUtil.convertObjToStr(objTradingacheadTO.getPurchaseReturn()));
            setTxtSalesReturn(CommonUtil.convertObjToStr(objTradingacheadTO.getSalesReturn()));
            setTxtDamages(CommonUtil.convertObjToStr(objTradingacheadTO.getDamages()));
            setTxtPurchaseVAT(CommonUtil.convertObjToStr(objTradingacheadTO.getPurchaseVAT()));
            setTxtSalesVAT(CommonUtil.convertObjToStr(objTradingacheadTO.getSalesVAT()));
            setTxtSAReceivable(CommonUtil.convertObjToStr(objTradingacheadTO.getSaReceivable()));
            setTxtSLPayable(CommonUtil.convertObjToStr(objTradingacheadTO.getSlPayable()));
            setTxtValue(CommonUtil.convertObjToStr(objTradingacheadTO.getValue()));
            setCboPeriod((String) getCbmPeriod().getDataForKey(CommonUtil.convertObjToStr(objTradingacheadTO.getPeriod())));
            setTxtStock(CommonUtil.convertObjToStr(objTradingacheadTO.getStock()));
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
    
    private String getAction(){
        String action = null;
        switch (_actionType) {
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
        return action;
    }
    
    private String getCommand(){
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
    
     public void ttNotifyObservers(){
        notifyObservers();
    }

    /**
     * Return an ArrayList by executing Query *
     */
    public ArrayList getResultList() {
        ArrayList list = (ArrayList) ClientUtil.executeQuery("getSelectTDSDates", null);
        return list;
    }
    
    public HashMap getAuthorizeMap() {
        return authorizeMap;
    }

    public void setAuthorizeMap(HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }

    public String getCboPeriod() {
        return cboPeriod;
    }

    public void setCboPeriod(String cboPeriod) {
        this.cboPeriod = cboPeriod;
    }

    public ComboBoxModel getCbmPeriod() {
        return cbmPeriod;
    }

    public void setCbmPeriod(ComboBoxModel cbmPeriod) {
        this.cbmPeriod = cbmPeriod;
    }

    public String getTxtValue() {
        return txtValue;
    }

    public void setTxtValue(String txtValue) {
        this.txtValue = txtValue;
    }

    public String getTxtStock() {
        return txtStock;
    }

    public void setTxtStock(String txtStock) {
        this.txtStock = txtStock;
    }

    public String getTxtSAReceivable() {
        return txtSAReceivable;
    }

    public void setTxtSAReceivable(String txtSAReceivable) {
        this.txtSAReceivable = txtSAReceivable;
    }

    public String getTxtSLPayable() {
        return txtSLPayable;
    }

    public void setTxtSLPayable(String txtSLPayable) {
        this.txtSLPayable = txtSLPayable;
    }
    
    
}
