/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TDSConfigOB.java
 *
 * Created on Mon Jan 31 16:05:07 IST 2005
 */

/**
 *
 * @author Revathi L
 */

package com.see.truetransact.ui.trading.shopmaster;

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
import com.see.truetransact.transferobject.mdsapplication.mdschangeofmember.MDSChangeofMemberTO;
import com.see.truetransact.uicomponent.CObservable;
//import com.see.truetransact.transferobject.tds.tdsconfig.TDSConfigTO;
import com.see.truetransact.transferobject.trading.shopmaster.ShopMasterTO;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import com.see.truetransact.clientproxy.ProxyParameters;

/**
 *
 * @author Revathi L
 */
public class ShopMasterOB extends CObservable {

    Date curDate = null;
    private ComboBoxModel cbmBranchID;
    private ComboBoxModel cbmPlace;
    private ArrayList key, value;
    private ProxyFactory proxy;
    private static ShopMasterOB objShopMasterOB;
    private HashMap map, keyValue, lookupMap, param;
    private final static Logger log = Logger.getLogger(ShopMasterOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int _result, actionType;
    private HashMap _authorizeMap;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final String YES = "Y";
    private final String NO = "N";
    private String txtStoreID = "";
    private String txtName = "";
    private String txtPlace = "";
    private String cboBranchID = "";
    private String txtCasAcHead = "";
    private String txtTransAcHead = "";
    private String txtKvatAcHead = "";
    private String cboPlace = "";
    private boolean chkActive = false;
    ShopMasterTO objShopMasterTO;
    private HashMap lookUpHash;

    private ShopMasterOB() {
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "ShopMasterJNDI");
            map.put(CommonConstants.HOME, "trading.shopmaster.ShopMasterHome");
            map.put(CommonConstants.REMOTE, "trading.shopmaster.ShopMaster");
            fillDropDown();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    static {
        try {
            log.info("Creating ParameterOB...");
            objShopMasterOB = new ShopMasterOB();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /**
     * Creating instance for ComboboxModel cbmTokenType
     */
    private void initUIComboBoxModel() {
    }

    private void fillDropDown() throws Exception {
        HashMap param = new HashMap();
        param.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("CUSTOMER.CITY");
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        HashMap lookupValues = ClientUtil.populateLookupData(param);
        getKeyValue((HashMap) lookupValues.get("CUSTOMER.CITY"));
        cbmPlace = new ComboBoxModel(key, value);
        //Branch
        key = new ArrayList();
        value = new ArrayList();
        param.put(CommonConstants.MAP_NAME, "getAllBran");
        HashMap where = new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        param.put(CommonConstants.PARAMFORQUERY, where);
        where = null;
        keyValue = ClientUtil.populateLookupData(param);
        fillData((HashMap) keyValue.get(CommonConstants.DATA));
        setCbmBranchID(new ComboBoxModel(key, value));
        key = new ArrayList();
        value = new ArrayList();
        key = null;
        value = null;
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    /* Filling up the the ComboBox in the UI*/
    /**
     * Populates two ArrayList key,value
     */
    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
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
            data.put("objShopMasterTO", setShopMasterTO());
        } else {
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
        }
        System.out.println("#### Data in Shop Master OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        _authorizeMap = null;
        setResult(getActionType());
    }

    /**
     * Returns an instance of TokenConfigOB.
     *
     * @return TokenConfigOB
     */
    public static ShopMasterOB getInstance() throws Exception {
        return objShopMasterOB;
    }

    // Setter method for tdtStartDate
    void setTdtStartDate(String tdtStartDate) {
        //this.tdtStartDate = tdtStartDate;
        setChanged();
    }

    private String getCommand() {
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

    private String getAction() {
        String action = null;
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
        return action;
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    public void getData(HashMap whereMap) {
        try {
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
            System.out.println("#@@%@@#%@#data" + data);
            objShopMasterTO = (ShopMasterTO) ((List) data.get("objShopMasterTO")).get(0);
            populateData(objShopMasterTO);
        } catch (Exception e) {
            parseException.logException(e, true);
            e.printStackTrace();
        }
    }

    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }

    public int getActionType() {
        return actionType;
    }

    /**
     * Setter for property actionType.
     *
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
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

    public ComboBoxModel getCbmBranchID() {
        return cbmBranchID;
    }

    public void setCbmBranchID(ComboBoxModel cbmBranchID) {
        this.cbmBranchID = cbmBranchID;
    }

    public boolean isChkActive() {
        return chkActive;
    }

    public void setChkActive(boolean chkActive) {
        this.chkActive = chkActive;
    }

    public HashMap getAuthorizeMap() {
        return _authorizeMap;
    }

    public void setAuthorizeMap(HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }

    /**
     * Returns an instance of TDSConfigTO by setting all the varibales of it *
     */
    private ShopMasterTO setShopMasterTO() {
        ShopMasterTO objShopMasterTO = new ShopMasterTO();
        objShopMasterTO.setTxtStoreID(getTxtStoreID());
        objShopMasterTO.setTxtName(getTxtName());
        objShopMasterTO.setTxtPlace(CommonUtil.convertObjToStr(getCbmPlace().getSelectedItem()));
        objShopMasterTO.setCboBranchID(CommonUtil.convertObjToStr(getCbmBranchID().getSelectedItem()));
        //objShopMasterTO.setCboBranchID(CommonUtil.convertObjToStr(getCbmBranchID().getKeyForSelected()));
        if (isChkActive()) {
            objShopMasterTO.setCboCashACHead(getTxtCasAcHead());
        }
        objShopMasterTO.setCboTransACHead(getTxtTransAcHead());
        objShopMasterTO.setCboKVATACHead(getTxtKvatAcHead());
        objShopMasterTO.setStatus(getAction());
        objShopMasterTO.setStatusBy(TrueTransactMain.USER_ID);
        objShopMasterTO.setStatusDt(curDate);
        objShopMasterTO.setAuthorizeStatus("");
        objShopMasterTO.setAuthorizeBy("");
        return objShopMasterTO;
    }

    /**
     * sets the values of TDSConfigTo variables to TDSConfigOB Variables *
     */
    private void populateData(ShopMasterTO objShopMasterTO) {
        setTxtStoreID(objShopMasterTO.getTxtStoreID());
        setTxtName(objShopMasterTO.getTxtName());
        setCboPlace(CommonUtil.convertObjToStr(objShopMasterTO.getTxtPlace()));
        setCboBranchID(CommonUtil.convertObjToStr(objShopMasterTO.getCboBranchID()));
       // setCboBranchID(CommonUtil.convertObjToStr(getCbmBranchID().getDataForKey(objShopMasterTO.getCboBranchID())));
        setTxtCasAcHead(objShopMasterTO.getCboCashACHead());
        setTxtTransAcHead(objShopMasterTO.getCboTransACHead());
        setTxtKvatAcHead(objShopMasterTO.getCboKVATACHead());
        notifyObservers();

    }

    /**
     * Clear up all the Fields of UI thru OB *
     */
    public void resetForm() {
        setTxtName("");
        setCboBranchID("");
        setCboPlace("");
        setChkActive(false);
        setTxtCasAcHead("");
        setTxtTransAcHead("");
        setTxtKvatAcHead("");
        notifyObservers();
    }

    /**
     * Return an ArrayList by executing Query *
     */
    public ArrayList getResultList() {
        ArrayList list = (ArrayList) ClientUtil.executeQuery("getSelectTDSDates", null);
        return list;
    }

    public String getCboBranchID() {
        return cboBranchID;
    }

    public void setCboBranchID(String cboBranchID) {
        this.cboBranchID = cboBranchID;
    }

    public String getTxtName() {
        return txtName;
    }

    public void setTxtName(String txtName) {
        this.txtName = txtName;
    }

    public String getTxtStoreID() {
        return txtStoreID;
    }

    public void setTxtStoreID(String txtStoreID) {
        this.txtStoreID = txtStoreID;
    }

    public String getTxtPlace() {
        return txtPlace;
    }

    public void setTxtPlace(String txtPlace) {
        this.txtPlace = txtPlace;
    }

    public ComboBoxModel getCbmPlace() {
        return cbmPlace;
    }

    public void setCbmPlace(ComboBoxModel cbmPlace) {
        this.cbmPlace = cbmPlace;
    }

    public String getCboPlace() {
        return cboPlace;
    }

    public void setCboPlace(String cboPlace) {
        this.cboPlace = cboPlace;
    }

    public String getTxtCasAcHead() {
        return txtCasAcHead;
    }

    public void setTxtCasAcHead(String txtCasAcHead) {
        this.txtCasAcHead = txtCasAcHead;
    }

    public String getTxtTransAcHead() {
        return txtTransAcHead;
    }

    public void setTxtTransAcHead(String txtTransAcHead) {
        this.txtTransAcHead = txtTransAcHead;
    }

    public String getTxtKvatAcHead() {
        return txtKvatAcHead;
    }

    public void setTxtKvatAcHead(String txtKvatAcHead) {
        this.txtKvatAcHead = txtKvatAcHead;
    }
}