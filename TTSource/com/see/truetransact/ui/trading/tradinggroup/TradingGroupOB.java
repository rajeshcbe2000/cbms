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
package com.see.truetransact.ui.trading.tradinggroup;

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
import com.see.truetransact.transferobject.tds.tdsconfig.TDSConfigTO;
import com.see.truetransact.transferobject.trading.tradinggroup.TradingGroupTO;
import com.see.truetransact.transferobject.trading.tradinggroup.TradingSubGroupTO;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.LinkedHashMap;
import com.see.truetransact.clientutil.EnhancedTableModel;

/**
 *
 * @author Revathi L
 */
public class TradingGroupOB extends CObservable {

    private String txtGroupID = "";
    private String txtGroupName = "";
    private String txtSubGroupID = "";
    private String txtSubGroupName = "";
    private TradingGroupTO objTradingGroupTO;
    private TradingSubGroupTO objTradingSubGroupTO;
    private static TradingGroupOB objTradingGroupOB;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int _result, _actionType;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap authorizeMap;
    private ProxyFactory proxy;
    private HashMap map, keyValue, lookUpHash;
    Date curDate = null;
    private LinkedHashMap subGroupMap = null;
    private ArrayList rowData;
    private static int serialNo = 1;
    private static int count = 1;
    private EnhancedTableModel tblGroup;
    final ArrayList tableTitle = new ArrayList();
    private LinkedHashMap totalSubGroupMap = null;// Contains Both Other Bank Details and Other Bank Branch Details
    private final String TO_DELETED_AT_UPDATE_MODE = "TO_DELETED_AT_UPDATE_MODE";
    private final String TO_NOT_DELETED_AT_UPDATE_MODE = "TO_NOT_DELETED_AT_UPDATE_MODE";
    private LinkedHashMap deletedSubGroupMap = null;
    private boolean newData = false;
    private String chkActive = "";

    public TradingGroupOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "TradingGroupJNDI");
        map.put(CommonConstants.HOME, "trading.tradinggroup.TradingGroupHome");
        map.put(CommonConstants.REMOTE, "trading.tradinggroup.TradingGroup");
        setTableTile();
        serialNo = 1;
        count = 1;
        tblGroup = new EnhancedTableModel(null, tableTitle);
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            System.err.println("Exception " + e + "Caught");
            e.printStackTrace();
        }
    }

    private void setTableTile() throws Exception {
        tableTitle.add("Sl No");
        tableTitle.add("Created Dt");
        tableTitle.add("Sub Group ID");
        tableTitle.add("Name");
        tableTitle.add("Auth Status");
    }

    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception {
    }

    /**
     * Creating instance for ComboboxModel cbmTokenType
     */
    private void initUIComboBoxModel() {
    }

    /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception {
    }

    /**
     * Populates two ArrayList key,value
     */
    private void getKeyValue(HashMap keyValue) throws Exception {
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
        if (totalSubGroupMap == null) {
            totalSubGroupMap = new LinkedHashMap();
        }
        totalSubGroupMap.put(TO_NOT_DELETED_AT_UPDATE_MODE, subGroupMap);
        final HashMap data = new HashMap();
        data.put("COMMAND", getCommand());
        if (getAuthorizeMap() == null) {
            data.put("objTradingGroupTO", setTradinggroupTO());
            if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                if (deletedSubGroupMap != null && deletedSubGroupMap.size() > 0) {
                    data.put("DELETE_DATA", deletedSubGroupMap);
                }
            }
        } else {
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
        }
        data.put("objSubGroupTO", totalSubGroupMap);
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        System.out.println("##### Trading Group Data OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        authorizeMap = null;
        setResult(getActionType());
    }

    public void getData(HashMap whereMap) {
        try {
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
            System.out.println("##### getData " + data);
            if (data != null && data.containsKey("objTradingGroupTO")) {
                objTradingGroupTO = (TradingGroupTO) ((List) data.get("objTradingGroupTO")).get(0);
                populateTradingGroupData(objTradingGroupTO);
                subGroupMap = new LinkedHashMap();
                if (data.containsKey("SUB_GROUP_DATA")) {
                    subGroupMap = (LinkedHashMap) data.get("SUB_GROUP_DATA");
                    ArrayList addList = new ArrayList(subGroupMap.keySet());
                    for (int i = 0; i < addList.size(); i++) {
                        TradingSubGroupTO objTradingSubGroupTO = (TradingSubGroupTO) subGroupMap.get(addList.get(i));
                        setChkActive(CommonUtil.convertObjToStr(objTradingSubGroupTO.getActive()));
                        objTradingSubGroupTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        ArrayList SubGroupRow = new ArrayList();
                        SubGroupRow.add(String.valueOf(i + 1));
                        SubGroupRow.add(CommonUtil.convertObjToStr(objTradingSubGroupTO.getCr_Dt()));
                        SubGroupRow.add(CommonUtil.convertObjToStr(objTradingSubGroupTO.getSubGroupID()));
                        SubGroupRow.add(CommonUtil.convertObjToStr(objTradingSubGroupTO.getSubGroupName()));
                        SubGroupRow.add(CommonUtil.convertObjToStr(objTradingSubGroupTO.getAuthorize_Status()));
                        tblGroup.addRow(SubGroupRow);
                        SubGroupRow = null;
                    }
                }
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void populateTradingGroupTable() throws Exception {
        ArrayList addList = new ArrayList(subGroupMap.keySet());
        for (int i = 0; i < addList.size(); i++) {
            TradingSubGroupTO objTradingSubGroupTO = (TradingSubGroupTO) subGroupMap.get(addList.get(i));
            ArrayList SubGroupRow = new ArrayList();
            SubGroupRow.add(objTradingSubGroupTO.getSlNo());
            SubGroupRow.add(CommonUtil.convertObjToStr(objTradingSubGroupTO.getCr_Dt()));
            SubGroupRow.add(CommonUtil.convertObjToStr(objTradingSubGroupTO.getSubGroupID()));
            SubGroupRow.add(CommonUtil.convertObjToStr(objTradingSubGroupTO.getSubGroupName()));
            SubGroupRow.add(CommonUtil.convertObjToStr(objTradingSubGroupTO.getAuthorize_Status()));
            tblGroup.addRow(SubGroupRow);
            SubGroupRow = null;
        }
        notifyObservers();
    }

    public void populateTradingGroupData(TradingGroupTO objTradingGroupTO) {
        HashMap mapData = null;
        try {
            setTxtGroupID(CommonUtil.convertObjToStr(objTradingGroupTO.getGroupID()));
            setTxtGroupName(CommonUtil.convertObjToStr(objTradingGroupTO.getGroupName()));
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);

        }
    }

    public void populateMemberTableDetails(int row) {
        try {
            resetSubGroup();
            final TradingSubGroupTO objTradingSubGroupTO = (TradingSubGroupTO) subGroupMap.get(row);
            populateMemberTableData(objTradingSubGroupTO);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void populateMemberTableData(TradingSubGroupTO objTradingSubGroupTO) throws Exception {
        setTxtSubGroupID(CommonUtil.convertObjToStr(objTradingSubGroupTO.getSubGroupID()));
        setTxtSubGroupName(CommonUtil.convertObjToStr(objTradingSubGroupTO.getSubGroupName()));
        setChkActive(CommonUtil.convertObjToStr(objTradingSubGroupTO.getActive()));
        setChanged();
        notifyObservers();
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

    private TradingGroupTO setTradinggroupTO() {
        TradingGroupTO objTradingGroupTO = new TradingGroupTO();
        objTradingGroupTO.setGroupID(CommonUtil.convertObjToStr(txtGroupID));
        objTradingGroupTO.setGroupName(CommonUtil.convertObjToStr(txtGroupName));
        objTradingGroupTO.setBranchID(TrueTransactMain.BRANCH_ID);
        if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            objTradingGroupTO.setStatus(CommonConstants.STATUS_MODIFIED);
        } else {
            objTradingGroupTO.setStatus(CommonConstants.STATUS_CREATED);
        }
        objTradingGroupTO.setStatusBy(TrueTransactMain.USER_ID);
        objTradingGroupTO.setAuthorizeStatus("");
        objTradingGroupTO.setAuthorizeBy("");
        objTradingGroupTO.setStatusDt(curDate);
        return objTradingGroupTO;
    }

    public static TradingGroupOB getInstance() throws Exception {
        return objTradingGroupOB;
    }

    public void saveSubGroup(int rowSelected, boolean updateMode) {
        try {
            int rowSel = rowSelected;
            final TradingSubGroupTO objTradingSubGroupTO = new TradingSubGroupTO();
            if (subGroupMap == null) {
                subGroupMap = new LinkedHashMap();
            }
            int slno = 0;
            int nums[] = new int[50];
            int max = nums[0];
            if (!updateMode) {
                ArrayList data = tblGroup.getDataArrayList();
                slno = serialNo(data);
            } else {
                if (isNewData()) {
                    ArrayList data = tblGroup.getDataArrayList();
                    slno = serialNo(data);
                } else {
                    int b = CommonUtil.convertObjToInt(tblGroup.getValueAt(rowSelected, 0));
                    slno = b;
                }
            }
            if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                if (isNewData()) {
                    objTradingSubGroupTO.setStatus(CommonConstants.STATUS_CREATED);
                    objTradingSubGroupTO.setActive("Y");
                } else {
                    objTradingSubGroupTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objTradingSubGroupTO.setActive(CommonUtil.convertObjToStr(getChkActive()));
                }
            } else {
                objTradingSubGroupTO.setStatus(CommonConstants.STATUS_CREATED);
                objTradingSubGroupTO.setActive("Y");
            }
            objTradingSubGroupTO.setGroupID(CommonUtil.convertObjToStr(getTxtGroupID()));
            objTradingSubGroupTO.setSubGroupID(CommonUtil.convertObjToStr(getTxtSubGroupID()));
            objTradingSubGroupTO.setSubGroupName(CommonUtil.convertObjToStr(getTxtSubGroupName()));
            objTradingSubGroupTO.setCr_Dt(curDate);
            objTradingSubGroupTO.setSlNo(String.valueOf(slno));
            String sno = String.valueOf(slno);
            subGroupMap.put(slno, objTradingSubGroupTO);
            updateMemberTable(rowSel, sno, objTradingSubGroupTO);
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void updateMemberTable(int rowSel, String sno, TradingSubGroupTO objTradingSubGroupTO) throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append        
        for (int i = tblGroup.getRowCount(), j = 0; i > 0; i--, j++) {
            selectedRow = ((ArrayList) tblGroup.getDataArrayList().get(j)).get(0);
            if (sno.equals(CommonUtil.convertObjToStr(selectedRow))) {
                rowExists = true;
                ArrayList row = new ArrayList();
                ArrayList data = tblGroup.getDataArrayList();
                data.remove(rowSel);
                row.add(sno);
                row.add(CommonUtil.convertObjToStr(objTradingSubGroupTO.getCr_Dt()));
                row.add(CommonUtil.convertObjToStr(objTradingSubGroupTO.getSubGroupID()));
                row.add(CommonUtil.convertObjToStr(objTradingSubGroupTO.getSubGroupName()));
                row.add(CommonUtil.convertObjToStr(objTradingSubGroupTO.getAuthorize_Status()));
                tblGroup.insertRow(rowSel, row);
                row = null;
            }
        }
        if (!rowExists) {
            ArrayList row = new ArrayList();
            row.add(String.valueOf(sno));
            row.add(CommonUtil.convertObjToStr(objTradingSubGroupTO.getCr_Dt()));
            row.add(CommonUtil.convertObjToStr(objTradingSubGroupTO.getSubGroupID()));
            row.add(CommonUtil.convertObjToStr(objTradingSubGroupTO.getSubGroupName()));
            row.add(CommonUtil.convertObjToStr(objTradingSubGroupTO.getAuthorize_Status()));
            tblGroup.insertRow(tblGroup.getRowCount(), row);
            row = null;
        }
    }

    public int serialNo(ArrayList data) {
        final int dataSize = data.size();
        int nums[] = new int[50];
        int max = nums[0];
        int slno = 0;
        int a = 0;
        slno = dataSize + 1;
        for (int i = 0; i < data.size(); i++) {
            a = CommonUtil.convertObjToInt(tblGroup.getValueAt(i, 0));
            nums[i] = a;
            if (nums[i] > max) {
                max = nums[i];
            }
            slno = max + 1;
        }
        return slno;
    }

    public void resetSubGroup() {
        setTxtSubGroupID("");
        setTxtSubGroupName("");
        setChkActive("N");
    }

    public void resetGroup() {
        setTxtGroupID("");
        setTxtGroupName("");
    }

    public void resetForm() {
        rowData = null;
        subGroupMap = null;
        tblGroup.setDataArrayList(null, tableTitle);
        deletedSubGroupMap = null;
        serialNo = 1;
        totalSubGroupMap = null;
        resetSubGroup();
        resetGroup();
        count = 1;
        rowData = null;
        ttNotifyObservers();
    }

    public TradingSubGroupTO setTradingSubGroupTO() {
        return objTradingSubGroupTO;
    }

    private ArrayList setColumnValues(int rowClicked, TradingSubGroupTO objTradingSubGroupTO) {
        ArrayList row = new ArrayList();
        row.add(String.valueOf(rowClicked));
        //row.add(CommonUtil.convertObjToStr(objTradingSubGroupTO.getSubGroupID()));
        row.add(CommonUtil.convertObjToStr(objTradingSubGroupTO.getSubGroupName()));
        return row;

    }

    public void deleteSubGroup(int val, int row) {
        if (deletedSubGroupMap == null) {
            deletedSubGroupMap = new LinkedHashMap();
        }
        TradingSubGroupTO objTradingSubGroupTO = (TradingSubGroupTO) subGroupMap.get(val);
        objTradingSubGroupTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedSubGroupMap.put(CommonUtil.convertObjToStr(tblGroup.getValueAt(row, 0)), subGroupMap.get(val));
        Object obj;
        obj = val;
        subGroupMap.remove(val);
        resetGrouptableValues();
        try {
            populateTradingGroupTable();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
    }

    private void setTradingSubGroupOB(TradingSubGroupTO objTradingSubGroupTO) throws Exception {
        setTxtSubGroupID(CommonUtil.convertObjToStr(objTradingSubGroupTO.getSubGroupID()));
        setTxtSubGroupName(CommonUtil.convertObjToStr(objTradingSubGroupTO.getSubGroupName()));
    }

    public void resetGrouptableValues() {
        tblGroup.setDataArrayList(null, tableTitle);
    }

    public String getTxtGroupID() {
        return txtGroupID;
    }

    public void setTxtGroupID(String txtGroupID) {
        this.txtGroupID = txtGroupID;
    }

    public String getTxtGroupName() {
        return txtGroupName;
    }

    public void setTxtGroupName(String txtGroupName) {
        this.txtGroupName = txtGroupName;
    }

    public String getTxtSubGroupID() {
        return txtSubGroupID;
    }

    public void setTxtSubGroupID(String txtSubGroupID) {
        this.txtSubGroupID = txtSubGroupID;
    }

    public String getTxtSubGroupName() {
        return txtSubGroupName;
    }

    public void setTxtSubGroupName(String txtSubGroupName) {
        this.txtSubGroupName = txtSubGroupName;
    }

    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }

    public String getLblStatus() {
        return lblStatus;
    }

    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
    }

    public int getResult() {
        return _result;
    }

    public void setResult(int _result) {
        this._result = _result;
    }

    public int getActionType() {
        return _actionType;
    }

    public void setActionType(int _actionType) {
        this._actionType = _actionType;
    }

    public HashMap getAuthorizeMap() {
        return authorizeMap;
    }

    public void setAuthorizeMap(HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }

    public void resetStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }

    public EnhancedTableModel getTblGroup() {
        return tblGroup;
    }

    public void setTblGroup(EnhancedTableModel tblGroup) {
        this.tblGroup = tblGroup;
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    public boolean isNewData() {
        return newData;
    }

    public void setNewData(boolean newData) {
        this.newData = newData;
    }

    public String getChkActive() {
        return chkActive;
    }

    public void setChkActive(String chkActive) {
        this.chkActive = chkActive;
    }
}