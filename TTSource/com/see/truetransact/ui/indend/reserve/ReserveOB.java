/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ReserveOB.java
 *
 * Created on Jul 24, 2019, 10:53 AM
 */
package com.see.truetransact.ui.indend.reserve;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.common.CommonRB;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;

/**
 *
 * @author Suresh R
 *
 */
public class ReserveOB extends CObservable {

    private String txtDepoID = "";
    private String tdtClosingDt = "";
    private String cboStockType = "";
    private String cboClosingStockType = "";
    private ComboBoxModel cbmStockType, cbmClosingStockType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static Logger log = Logger.getLogger(ReserveOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap map;
    private ProxyFactory proxy;
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    ReserveRB objReserveRB = new ReserveRB();
    private static final CommonRB objCommonRB = new CommonRB();
    private TableModel tblIndendCloseDetails;
    final ArrayList tblIndendCloseDetailsTitle = new ArrayList();
    final ArrayList tblIndendReserveDetailsTitle = new ArrayList();
    private List finalCloseList = null;

    /**
     * Creates a new instance of TDS MiantenanceOB
     */
    public ReserveOB() {
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "ReserveJNDI");
            map.put(CommonConstants.HOME, "ReserveHome");
            map.put(CommonConstants.REMOTE, "Reserve");
            fillDropdown();
            setTblIndendCloseDetails();
            tblIndendCloseDetails = new TableModel(null, tblIndendCloseDetailsTitle);
            setTblReserveDetails();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void setTblIndendCloseDetails() {
        tblIndendCloseDetailsTitle.add("BRANCH_ID");
        tblIndendCloseDetailsTitle.add("A/C DESCRIPTION");
        tblIndendCloseDetailsTitle.add("CLOSING_AMT");
    }

    public void setTblReserveDetails() {
        tblIndendReserveDetailsTitle.add("BRANCH_ID");
        tblIndendReserveDetailsTitle.add("A/C DESCRIPTION");
        tblIndendReserveDetailsTitle.add("CLOSING_AMT");
        tblIndendReserveDetailsTitle.add("PREV_CLOSING_AMT");
    }

    /**
     * To fill the comboboxes
     */
    private void fillDropdown() throws Exception {
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
        param = new java.util.HashMap();
        param.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookupKeys = new ArrayList(1);
        param.put(CommonConstants.PARAMFORQUERY, lookupKeys);
        HashMap lookupValues = populateData(param);
        param.put(CommonConstants.MAP_NAME, null);
        lookupKeys.add("INDEND_CLOSE_TYPE");
        lookupKeys.add("INDEND_CLOSE_STOCK");
        param.put(CommonConstants.PARAMFORQUERY, lookupKeys);
        lookupValues = populateData(param);

        fillData((HashMap) lookupValues.get("INDEND_CLOSE_TYPE"));
        cbmStockType = new ComboBoxModel(key, value);
        makeNull();

        fillData((HashMap) lookupValues.get("INDEND_CLOSE_STOCK"));
        cbmClosingStockType = new ComboBoxModel(key, value);

        lookupValues = null;
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get("KEY");
        value = (ArrayList) keyValue.get("VALUE");
    }

    private void makeNull() {
        key = null;
        value = null;
    }

    public void cellEditableColumnTrue() {
        tblIndendCloseDetails.setEditColoumnNo(2);
    }

    /**
     * To get data for comboboxes
     */
    private HashMap populateData(HashMap obj) throws Exception {
        keyValue = proxy.executeQuery(obj, lookupMap);
        log.info("Got HashMap");
        return keyValue;
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    /**
     * To perform the necessary operation
     */
    public void doAction() {
        try {
            doActionPerform();
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    /**
     * To perform the necessary action
     */
    private void doActionPerform() throws Exception {
        final HashMap data = new HashMap();
        data.put("CLOSE_TYPE", getCboStockType());
        data.put("CLOSE_DATE", getTdtClosingDt());
        data.put("RESERVE_DEP_LIST", getFinalCloseList());
        //System.out.println("#### data : "+data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        setResult(getActionType());
    }

    public void insertTableData(HashMap whereMap) throws Exception {
        try {
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            finalCloseList = null;
            String mapName = "";
            if (CommonUtil.convertObjToStr(whereMap.get("STOCK_TYPE")).equals("RESERVE")) {
                mapName = "getReserveHeadDetails";
            } else {
                mapName = "getReserveDepreciationHeadDetails";
            }
            List closeList = ClientUtil.executeQuery(mapName, whereMap);
            if (closeList != null && closeList.size() > 0) {
                for (int i = 0; i < closeList.size(); i++) {
                    dataMap = (HashMap) closeList.get(i);
                    rowList = new ArrayList();
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("AC_HD_DESC")));
                    rowList.add(CommonUtil.convertObjToDouble(String.valueOf("0")));
                    if (CommonUtil.convertObjToStr(whereMap.get("STOCK_TYPE")).equals("RESERVE")) {
                        rowList.add(CommonUtil.convertObjToStr(dataMap.get("PREV_CLOSING_AMT")));
                    }
                    tableList.add(rowList);
                }
                setFinalCloseList(closeList);
                if (CommonUtil.convertObjToStr(whereMap.get("STOCK_TYPE")).equals("RESERVE")) {
                    tblIndendCloseDetails = new TableModel((ArrayList) tableList, tblIndendReserveDetailsTitle);
                } else {
                    tblIndendCloseDetails = new TableModel((ArrayList) tableList, tblIndendCloseDetailsTitle);
                }
                cellEditableColumnTrue();
            } else {
                tblIndendCloseDetails.setDataArrayList(null, tblIndendCloseDetailsTitle);
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /**
     * To retrieve a particular customer's accountclosing record
     */
    public void getData(HashMap whereMap) {
        try {
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void resetForm() {
        txtDepoID = "";
        tdtClosingDt = "";
        cboStockType = "";
        cboClosingStockType = "";
        resetTableValues();
        finalCloseList = null;
        setChanged();
        ttNotifyObservers();
    }

    public void resetTableValues() {
        tblIndendCloseDetails = new TableModel((ArrayList) null, tblIndendCloseDetailsTitle);
    }

    /**
     * To set the status based on ActionType, either New, Edit, etc.,
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }

    /**
     * To update the Status based on result performed by doAction() method
     */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }

    /**
     * Getter for property actionType.
     *
     * @return Value of property actionType.
     */
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
     * Getter for property lblStatus.
     *
     * @return Value of property lblStatus.
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }

    /**
     * Setter for property lblStatus.
     *
     * @param lblStatus New value of property lblStatus.
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }

    /**
     * Getter for property result.
     *
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }

    /**
     * Setter for property result.
     *
     * @param result New value of property result.
     */
    public void setResult(int result) {
        this.result = result;
    }

    public String getTxtDepoID() {
        return txtDepoID;
    }

    public void setTxtDepoID(String txtDepoID) {
        this.txtDepoID = txtDepoID;
    }

    public String getTdtClosingDt() {
        return tdtClosingDt;
    }

    public void setTdtClosingDt(String tdtClosingDt) {
        this.tdtClosingDt = tdtClosingDt;
    }

    public String getCboStockType() {
        return cboStockType;
    }

    public void setCboStockType(String cboStockType) {
        this.cboStockType = cboStockType;
    }

    public String getCboClosingStockType() {
        return cboClosingStockType;
    }

    public void setCboClosingStockType(String cboClosingStockType) {
        this.cboClosingStockType = cboClosingStockType;
    }

    public ComboBoxModel getCbmStockType() {
        return cbmStockType;
    }

    public void setCbmStockType(ComboBoxModel cbmStockType) {
        this.cbmStockType = cbmStockType;
    }

    public ComboBoxModel getCbmClosingStockType() {
        return cbmClosingStockType;
    }

    public void setCbmClosingStockType(ComboBoxModel cbmClosingStockType) {
        this.cbmClosingStockType = cbmClosingStockType;
    }

    public List getFinalCloseList() {
        return finalCloseList;
    }

    public void setFinalCloseList(List finalCloseList) {
        this.finalCloseList = finalCloseList;
    }

    public TableModel getTblIndendCloseDetails() {
        return tblIndendCloseDetails;
    }

    public void setTblIndendCloseDetails(TableModel tblIndendCloseDetails) {
        this.tblIndendCloseDetails = tblIndendCloseDetails;
    }
}
