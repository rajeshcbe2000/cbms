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
package com.see.truetransact.ui.trading.tradingsuppliermaster;

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
import com.see.truetransact.transferobject.trading.tradingsuppliermaster.TradingSuplierMasterTO;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 *
 * @author Revathi L
 */
public class TradingSupplierMasterOB extends CObservable {

    private String curDate = "";
    Date tdtDate = null;
    private String txtSupplierID = "";
    private String txtName = "";
    private String txtAddress = "";
    private String txtSundryCreditors = "";
    private String txtPurchase = "";
    private String txtPhone = "";
    private String txtCSTNO = "";
    private String txtKGSTNO = "";
    private String txtTinNo = "";
    private ArrayList key, value;
    private ProxyFactory proxy;
    private static TradingSupplierMasterOB objTradingSupplierMasterOB;
    private TradingSuplierMasterTO objTradingSuplierMasterTO;
    private HashMap map, keyValue, lookUpHash;
    private final static Logger log = Logger.getLogger(TradingSupplierMasterOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int _result, _actionType;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final String YES = "Y";
    private final String NO = "N";
    private String supplierPID = "";
    private String supplierPlace = "";
    private String supplierActnum = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = "";
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String supplierPost = "";
    private HashMap authorizeMap;
    private String custID = "";
    private String sundryCreditorsName = "";
    private String purchaseName = "";
    private String chkActive = "";

//    private String tdsCeAchdId="";
    /**
     * Consturctor Declaration for TDSConfigOB
     */
    private TradingSupplierMasterOB() {
        try {
            tdtDate = ClientUtil.getCurrentDate();
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
            objTradingSupplierMasterOB = new TradingSupplierMasterOB();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "TradingSupplierMasterJNDI");
        map.put(CommonConstants.HOME, "trading.tradingsuppliermaster.TradingSupplierMasterHome");
        map.put(CommonConstants.REMOTE, "trading.tradingsuppliermaster.TradingSupplierMaster");
    }

    /**
     * Creating instance for ComboboxModel cbmTokenType
     */
    private void initUIComboBoxModel() {
    }

    /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception {
        try {
            log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("TDS_CONFIG_SCOPE");
            lookup_keys.add("CUSTOMER.TYPE");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        } catch (NullPointerException e) {
            parseException.logException(e, true);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public String getTxtAddress() {
        return txtAddress;
    }

    public Date getTdtDate() {
        return tdtDate;
    }

    public void setTdtDate(Date tdtDate) {
        this.tdtDate = tdtDate;
    }

    public String getSupplierPost() {
        return supplierPost;
    }

    public void setSupplierPost(String supplierPost) {
        this.supplierPost = supplierPost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HashMap getAuthorizeMap() {
        return authorizeMap;
    }

    public void setAuthorizeMap(HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }

    public void setTxtAddress(String txtAddress) {
        this.txtAddress = txtAddress;
    }

    public String getTxtCSTNO() {
        return txtCSTNO;
    }

    public void setTxtCSTNO(String txtCSTNO) {
        this.txtCSTNO = txtCSTNO;
    }

    public String getTxtKGSTNO() {
        return txtKGSTNO;
    }

    public void setTxtKGSTNO(String txtKGSTNO) {
        this.txtKGSTNO = txtKGSTNO;
    }

    public String getTxtName() {
        return txtName;
    }

    public void setTxtName(String txtName) {
        this.txtName = txtName;
    }

    public String getTxtPhone() {
        return txtPhone;
    }

    public void setTxtPhone(String txtPhone) {
        this.txtPhone = txtPhone;
    }

    public String getTxtPurchase() {
        return txtPurchase;
    }

    public void setTxtPurchase(String txtPurchase) {
        this.txtPurchase = txtPurchase;
    }

    public String getTxtSundryCreditors() {
        return txtSundryCreditors;
    }

    public void setTxtSundryCreditors(String txtSundryCreditors) {
        this.txtSundryCreditors = txtSundryCreditors;
    }

    public String getTxtSupplierID() {
        return txtSupplierID;
    }

    public void setTxtSupplierID(String txtSupplierID) {
        this.txtSupplierID = txtSupplierID;
    }

    public String getTxtTinNo() {
        return txtTinNo;
    }

    public void setTxtTinNo(String txtTinNo) {
        this.txtTinNo = txtTinNo;
    }

    public String getSupplierPID() {
        return supplierPID;
    }

    public void setSupplierPID(String supplierPID) {
        this.supplierPID = supplierPID;
    }

    public String getSupplierPlace() {
        return supplierPlace;
    }

    public void setSupplierPlace(String supplierPlace) {
        this.supplierPlace = supplierPlace;
    }

    public String getSupplierActnum() {
        return supplierActnum;
    }

    public void setSupplierActnum(String supplierActnum) {
        this.supplierActnum = supplierActnum;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
    }

    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    /**
     * Populates two ArrayList key,value
     */
    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    public String getCustID() {
        return custID;
    }

    public void setCustID(String custID) {
        this.custID = custID;
    }

    public String getSundryCreditorsName() {
        return sundryCreditorsName;
    }

    public void setSundryCreditorsName(String sundryCreditorsName) {
        this.sundryCreditorsName = sundryCreditorsName;
    }

    public String getPurchaseName() {
        return purchaseName;
    }

    public void setPurchaseName(String purchaseName) {
        this.purchaseName = purchaseName;
    }

    public String getChkActive() {
        return chkActive;
    }

    public void setChkActive(String chkActive) {
        this.chkActive = chkActive;
    }

    /**
     * Returns an instance of TokenConfigOB.
     *
     * @return TokenConfigOB
     */
    public static TradingSupplierMasterOB getInstance() throws Exception {
        return objTradingSupplierMasterOB;
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
            data.put("objTradingSuplierMasterTO", setTradingSupplierMasterTO());
        } else {
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

    private TradingSuplierMasterTO setTradingSupplierMasterTO() {
        TradingSuplierMasterTO objTradingSuplierMasterTO = new TradingSuplierMasterTO();
        objTradingSuplierMasterTO.setSupplierID(CommonUtil.convertObjToStr(getTxtSupplierID()));
        objTradingSuplierMasterTO.setBranchCode(TrueTransactMain.BRANCH_ID);
        objTradingSuplierMasterTO.setCustomerID(CommonUtil.convertObjToStr(getTxtName()));
        objTradingSuplierMasterTO.setTxtAddress(CommonUtil.convertObjToStr(getTxtAddress()));
        objTradingSuplierMasterTO.setTxtSundryCreditors(CommonUtil.convertObjToStr(getTxtSundryCreditors()));
        objTradingSuplierMasterTO.setTxtPurchase(CommonUtil.convertObjToStr(getTxtPurchase()));
        objTradingSuplierMasterTO.setTdtDate(tdtDate);
        objTradingSuplierMasterTO.setTxtPhone(CommonUtil.convertObjToStr(getTxtPhone()));
        objTradingSuplierMasterTO.setTxtCSTNO(CommonUtil.convertObjToStr(getTxtCSTNO()));
        objTradingSuplierMasterTO.setTxtKGSTNO(CommonUtil.convertObjToStr(getTxtKGSTNO()));
        objTradingSuplierMasterTO.setTxtTinNo(CommonUtil.convertObjToStr(getTxtTinNo()));
        if (getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            objTradingSuplierMasterTO.setActive("Y");
        } else {
            objTradingSuplierMasterTO.setActive(CommonUtil.convertObjToStr(getChkActive()));
        }
        if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            objTradingSuplierMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
        } else {
            objTradingSuplierMasterTO.setStatus(CommonConstants.STATUS_CREATED);
        }
        objTradingSuplierMasterTO.setAuthorizeStatus("");
        objTradingSuplierMasterTO.setAuthorizeBy("");
        objTradingSuplierMasterTO.setStatusBy(TrueTransactMain.USER_ID);
        return objTradingSuplierMasterTO;
    }

    public void getData(HashMap whereMap) {
        try {
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
            System.out.println("#@@%@@#%@#data" + data);
            if (data != null && data.containsKey("objTradingSupplierMasterTO")) {
                objTradingSuplierMasterTO = (TradingSuplierMasterTO) ((List) data.get("objTradingSupplierMasterTO")).get(0);
                populateSupplierData(objTradingSuplierMasterTO);
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void populateSupplierData(TradingSuplierMasterTO objTradingSuplierMasterTO) {
        HashMap mapData = null;
        try {
            setTxtSupplierID(CommonUtil.convertObjToStr(objTradingSuplierMasterTO.getSupplierID()));
            setCustID(CommonUtil.convertObjToStr(objTradingSuplierMasterTO.getCustomerID()));
            HashMap queryMap = new HashMap();
            queryMap.put("CUST_ID", getCustID());
            getcustDetails(queryMap);
            setTxtSundryCreditors(CommonUtil.convertObjToStr(objTradingSuplierMasterTO.getTxtSundryCreditors()));
            setTxtPurchase(CommonUtil.convertObjToStr(objTradingSuplierMasterTO.getTxtPurchase()));
            setTdtDate(objTradingSuplierMasterTO.getTdtDate());
            setTxtPhone(CommonUtil.convertObjToStr(objTradingSuplierMasterTO.getTxtPhone()));
            setTxtCSTNO(CommonUtil.convertObjToStr(objTradingSuplierMasterTO.getTxtCSTNO()));
            setTxtKGSTNO(CommonUtil.convertObjToStr(objTradingSuplierMasterTO.getTxtKGSTNO()));
            setTxtTinNo(CommonUtil.convertObjToStr(objTradingSuplierMasterTO.getTxtTinNo()));
            setChkActive(CommonUtil.convertObjToStr(objTradingSuplierMasterTO.getActive()));
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);

        }
    }

    public void getcustDetails(HashMap queryWhereMap) {
        List custListData = ClientUtil.executeQuery("getSelectCustDetails", queryWhereMap);
        if (custListData != null && custListData.size() > 0) {
            queryWhereMap = (HashMap) custListData.get(0);
            setTxtName(CommonUtil.convertObjToStr(queryWhereMap.get("NAME")));
            setTxtAddress(CommonUtil.convertObjToStr(queryWhereMap.get("ADDRESS")));
            setSundryCreditorsName(CommonUtil.convertObjToStr(queryWhereMap.get("NAME")));
            setPurchaseName(CommonUtil.convertObjToStr(queryWhereMap.get("NAME")));
        }
    }

    private TradingSuplierMasterTO getTradingSuplierMasterTO(String command) {
        TradingSuplierMasterTO objTradingSuplierMasterTO = new TradingSuplierMasterTO();
        objTradingSuplierMasterTO.setCommand(command);
        objTradingSuplierMasterTO.setSupplierID(getTxtSupplierID());
        objTradingSuplierMasterTO.setSupplierPID(getSupplierPID());
        objTradingSuplierMasterTO.setSupplierActnum(getSupplierActnum());
        objTradingSuplierMasterTO.setSupplierPost(getSupplierPost());
        objTradingSuplierMasterTO.setSupplierPlace(getSupplierPlace());
        objTradingSuplierMasterTO.setTdtDate(getTdtDate());
        objTradingSuplierMasterTO.setTin(getTxtTinNo());
        objTradingSuplierMasterTO.setTxtAddress(getTxtAddress());
        objTradingSuplierMasterTO.setTxtCSTNO(getTxtCSTNO());
        objTradingSuplierMasterTO.setTxtKGSTNO(getTxtKGSTNO());
        objTradingSuplierMasterTO.setTxtName(getTxtName());
        objTradingSuplierMasterTO.setTxtPhone(getTxtPhone());
        objTradingSuplierMasterTO.setTxtPurchase(getTxtPurchase());
        objTradingSuplierMasterTO.setTxtSundryCreditors(getTxtSundryCreditors());
        objTradingSuplierMasterTO.setStatus(getStatus());
        objTradingSuplierMasterTO.setAuthorizeStatus(getAuthorizeStatus());
        objTradingSuplierMasterTO.setAuthorizeDt(getAuthorizeDt());
        objTradingSuplierMasterTO.setAuthorizeBy(getAuthorizeBy());
        return objTradingSuplierMasterTO;


    }

    private void setTradingSuppliermasterTO(TradingSuplierMasterTO objTradingSuplierMasterTO) {
        setTxtSupplierID(objTradingSuplierMasterTO.getSupplierID());
        setTxtName(objTradingSuplierMasterTO.getTxtName());
        setTxtAddress(objTradingSuplierMasterTO.getTxtAddress());
        setTxtPurchase(objTradingSuplierMasterTO.getTxtPurchase());
        setTxtSundryCreditors(objTradingSuplierMasterTO.getTxtSundryCreditors());
        setTdtDate(objTradingSuplierMasterTO.getTdtDate());
        setTxtPhone(objTradingSuplierMasterTO.getTxtPhone());
        setTxtCSTNO(objTradingSuplierMasterTO.getTxtCSTNO());
        setTxtKGSTNO(objTradingSuplierMasterTO.getTxtKGSTNO());
        setTxtTinNo(objTradingSuplierMasterTO.getTxtTinNo());

    }

    /**
     * Clear up all the Fields of UI thru OB *
     */
    public void resetForm() {
        setTxtSupplierID("");
        setTxtName("");
        setTxtAddress("");
        setTxtSundryCreditors("");
        setTxtPurchase("");
        setTdtDate(null);
        setTxtPhone("");
        setTxtCSTNO("");
        setTxtKGSTNO("");
        setTxtTinNo("");
        notifyObservers();

    }

    /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        HashMap mapData = null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            TradingSuplierMasterTO TradingSuplierMasterTO =
                    (TradingSuplierMasterTO) ((List) mapData.get("TradingSuplierMasterTO")).get(0);
            setTradingSuppliermasterTO(objTradingSuplierMasterTO);
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);

        }
    }

    /**
     * Return an ArrayList by executing Query *
     */
    public ArrayList getResultList() {
        ArrayList list = (ArrayList) ClientUtil.executeQuery("getSelectTDSDates", null);
        return list;
    }
}