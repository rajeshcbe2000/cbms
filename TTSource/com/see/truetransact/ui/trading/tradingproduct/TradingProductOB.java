/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * TradingProductOB.java
 */
package com.see.truetransact.ui.trading.tradingproduct;

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
import com.see.truetransact.transferobject.trading.tradingproduct.TradingProductTO;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 *
 * @author Revathi.L
 */
public class TradingProductOB extends CObservable {

    Date curDate = null;
    private String cboUnitType;
    private String cboGroupName;
    private String cboItemAcHd;
    private String cboSubGroupName;
    private ComboBoxModel cbmUnitType;
    private ComboBoxModel cbmGroupName;
    private ComboBoxModel cbmItemAcHd;
    private ComboBoxModel cbmSubGroupName;
    private String txtProductID = "";
    private String txtProductName = "";
    private String txtProductDesc = "";
    private String txtTaxPer = "";
    private String txtReOrderLevel = "";
    private String tax = "";
    private String tdtDate;
    private HashMap authorizeMap;
    private TradingProductTO objTradingProductTO;
    private ArrayList key, value;
    private ProxyFactory proxy;
//    private static TradingProductOB objTradingProductOB;
    private HashMap map, keyValue, lookUpHash;
    private final static Logger log = Logger.getLogger(TradingProductOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int _result, _actionType;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    boolean newProd = false;
    

    public TradingProductOB() {
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

//    static {
//        try {
//            log.info("Creating ParameterOB...");
//            objTradingProductOB = new TradingProductOB();
//        } catch (Exception e) {
//            parseException.logException(e, true);
//        }
//    }

    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "TradingProductJNDI");
        map.put(CommonConstants.HOME, "trading.tradingproduct.TradingProductHome");
        map.put(CommonConstants.REMOTE, "trading.tradingproduct.TradingProduct");
    }

    /**
     * Creating instance for ComboboxModel cbmTokenType
     */
    private void initUIComboBoxModel() {
        cbmUnitType = new ComboBoxModel();
        cbmGroupName = new ComboBoxModel();
        cbmItemAcHd = new ComboBoxModel();
        cbmSubGroupName = new ComboBoxModel();
    }

    /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception {
        try {
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.JNDI, "LookUpJNDI");
            lookUpHash.put(CommonConstants.HOME, "common.lookup.LookUpHome");
            lookUpHash.put(CommonConstants.REMOTE, "common.lookup.LookUp");
            HashMap param = new HashMap();
            param.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookupKey = new ArrayList();
            lookupKey.add("UNIT_TYPE");
            param.put(CommonConstants.PARAMFORQUERY, lookupKey);
            HashMap lookupValues = ClientUtil.populateLookupData(param);
            getKeyValue((HashMap) lookupValues.get("UNIT_TYPE"));
            cbmUnitType = new ComboBoxModel(key, value);
            key = new ArrayList();
            value = new ArrayList();
            List groupLst = (List) ClientUtil.executeQuery("getTradingGroupName", null);
            getMap(groupLst);
            setCbmGroupName(new ComboBoxModel(key, value));
//            key = new ArrayList();
//            value = new ArrayList();
//            List itemLst = (List) ClientUtil.executeQuery("getTradingItemAcHd", null);
//            getMap(itemLst);
//            setCbmItemAcHd(new ComboBoxModel(key, value));
        } catch (NullPointerException e) {
            parseException.logException(e, true);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void setCbmSubGroupName(String groupName) {
        try {
            if (!groupName.equals("")) {
                HashMap queryWhereMap = new HashMap();
                queryWhereMap.put("GROUP_NAME", groupName);
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME, null);
                final ArrayList lookup_keys = new ArrayList();
                lookUpHash.put(CommonConstants.MAP_NAME, "getTradingSubGroupName");
                lookUpHash.put(CommonConstants.PARAMFORQUERY, queryWhereMap);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
                cbmSubGroupName = new ComboBoxModel(key, value);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        setChanged();
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
        if (getAuthorizeMap() == null) {
            data.put("objTradingProductTO", setTradingProductTO());
        } else {
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
        }
        System.out.println("#### Data in Sales OB : " + data);
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

    private TradingProductTO setTradingProductTO() {
        TradingProductTO objTradingProductTO = new TradingProductTO();
        objTradingProductTO.setProductID(CommonUtil.convertObjToStr(getTxtProductID()));
        objTradingProductTO.setProductName(CommonUtil.convertObjToStr(getTxtProductName()));
        objTradingProductTO.setProductDesc(CommonUtil.convertObjToStr(getTxtProductDesc()));
        objTradingProductTO.setUnitType(CommonUtil.convertObjToStr(getCbmUnitType().getKeyForSelected()));
        objTradingProductTO.setGroupName(CommonUtil.convertObjToStr(getCbmGroupName().getKeyForSelected()));
        objTradingProductTO.setSubGroupName(CommonUtil.convertObjToStr(getCbmSubGroupName().getKeyForSelected()));
        objTradingProductTO.setDate(curDate);
        objTradingProductTO.setTax(CommonUtil.convertObjToStr(getTax()));
        objTradingProductTO.setTaxPer(CommonUtil.convertObjToStr(getTxtTaxPer()));
        objTradingProductTO.setReOrderLevel(CommonUtil.convertObjToStr(getTxtReOrderLevel()));
        if (newProd) {
            objTradingProductTO.setNewProd(CommonUtil.convertObjToStr("Y"));
        } else {
            objTradingProductTO.setNewProd(CommonUtil.convertObjToStr("N"));
        }
        if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            objTradingProductTO.setStatus(CommonConstants.STATUS_MODIFIED);
        } else {
            objTradingProductTO.setStatus(CommonConstants.STATUS_CREATED);
        }
        objTradingProductTO.setAuthorizeStatus("");
        objTradingProductTO.setAuthorizeBy("");
        objTradingProductTO.setStatusBy(TrueTransactMain.USER_ID);
        objTradingProductTO.setStatusDt(curDate);
        return objTradingProductTO;
    }

    public void getData(HashMap whereMap) {
        try {
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
            if (data != null && data.containsKey("objTradingProductTO")) {
                objTradingProductTO = (TradingProductTO) ((List) data.get("objTradingProductTO")).get(0);
                HashMap mapData = null;
                setTxtProductID(CommonUtil.convertObjToStr(objTradingProductTO.getProductID()));
                setTxtProductName(CommonUtil.convertObjToStr(objTradingProductTO.getProductName()));
                setTxtProductDesc(CommonUtil.convertObjToStr(objTradingProductTO.getProductDesc()));
                setCboUnitType(CommonUtil.convertObjToStr(objTradingProductTO.getUnitType()));
                setTdtDate(CommonUtil.convertObjToStr(objTradingProductTO.getDate()));
                setCbmSubGroupName((String) getCbmGroupName().getDataForKey(CommonUtil.convertObjToStr(objTradingProductTO.getGroupName())));
                setCboSubGroupName((String) getCbmSubGroupName().getDataForKey(CommonUtil.convertObjToStr(objTradingProductTO.getSubGroupName())));
                setCboGroupName((String) getCbmGroupName().getDataForKey(CommonUtil.convertObjToStr(objTradingProductTO.getGroupName())));
                setTax(CommonUtil.convertObjToStr(objTradingProductTO.getTax()));
                setTxtTaxPer(CommonUtil.convertObjToStr(objTradingProductTO.getTaxPer()));
                setTxtReOrderLevel(CommonUtil.convertObjToStr(objTradingProductTO.getReOrderLevel()));
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /**
     * Returns an instance of TokenConfigOB.
     *
     * @return TokenConfigOB
     */
//    public static TradingProductOB getInstance() throws Exception {
//        return objTradingProductOB;
//    }

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

    public ArrayList getResultList() {
        ArrayList list = (ArrayList) ClientUtil.executeQuery("getSelectTDSDates", null);
        return list;
    }

    public String getCboUnitType() {
        return cboUnitType;
    }

    public void setCboUnitType(String cboUnitType) {
        this.cboUnitType = cboUnitType;
    }

    public String getCboGroupName() {
        return cboGroupName;
    }

    public void setCboGroupName(String cboGroupName) {
        this.cboGroupName = cboGroupName;
    }

    public String getCboItemAcHd() {
        return cboItemAcHd;
    }

    public void setCboItemAcHd(String cboItemAcHd) {
        this.cboItemAcHd = cboItemAcHd;
    }

    public String getCboSubGroupName() {
        return cboSubGroupName;
    }

    public void setCboSubGroupName(String cboSubGroupName) {
        this.cboSubGroupName = cboSubGroupName;
    }

    public ComboBoxModel getCbmUnitType() {
        return cbmUnitType;
    }

    public void setCbmUnitType(ComboBoxModel cbmUnitType) {
        this.cbmUnitType = cbmUnitType;
    }

    public ComboBoxModel getCbmGroupName() {
        return cbmGroupName;
    }

    public void setCbmGroupName(ComboBoxModel cbmGroupName) {
        this.cbmGroupName = cbmGroupName;
    }

    public ComboBoxModel getCbmItemAcHd() {
        return cbmItemAcHd;
    }

    public void setCbmItemAcHd(ComboBoxModel cbmItemAcHd) {
        this.cbmItemAcHd = cbmItemAcHd;
    }

    public ComboBoxModel getCbmSubGroupName() {
        return cbmSubGroupName;
    }

    public void setCbmSubGroupName(ComboBoxModel cbmSubGroupName) {
        this.cbmSubGroupName = cbmSubGroupName;
    }

    public String getTxtProductID() {
        return txtProductID;
    }

    public void setTxtProductID(String txtProductID) {
        this.txtProductID = txtProductID;
    }

    public String getTxtProductName() {
        return txtProductName;
    }

    public void setTxtProductName(String txtProductName) {
        this.txtProductName = txtProductName;
    }

    public String getTxtProductDesc() {
        return txtProductDesc;
    }

    public void setTxtProductDesc(String txtProductDesc) {
        this.txtProductDesc = txtProductDesc;
    }

    public String getTxtTaxPer() {
        return txtTaxPer;
    }

    public void setTxtTaxPer(String txtTaxPer) {
        this.txtTaxPer = txtTaxPer;
    }

    public String getTxtReOrderLevel() {
        return txtReOrderLevel;
    }

    public void setTxtReOrderLevel(String txtReOrderLevel) {
        this.txtReOrderLevel = txtReOrderLevel;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTdtDate() {
        return tdtDate;
    }

    public void setTdtDate(String tdtDate) {
        this.tdtDate = tdtDate;
    }

    public HashMap getAuthorizeMap() {
        return authorizeMap;
    }

    public void setAuthorizeMap(HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }
}