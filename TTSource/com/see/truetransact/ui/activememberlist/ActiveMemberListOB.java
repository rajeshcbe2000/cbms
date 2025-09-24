/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * ActiveMemberListOB.java
 */

package com.see.truetransact.ui.activememberlist;

import com.see.truetransact.ui.TrueTransactMain;

import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.TableModel;

import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.activememberlist.ActiveMemberListTo;
import com.see.truetransact.transferobject.generalledger.gllimit.GLLimitTO;
import com.see.truetransact.uicomponent.CTable;
import com.sun.tools.javac.v8.util.Convert;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 *
 * @author
 */
public class ActiveMemberListOB extends CObservable {

    final ArrayList tableTitle = new ArrayList();
    final ArrayList tableTitle1 = new ArrayList();
    private HashMap operationMap;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private CTable _tblData;
    private ArrayList _heading;
    private ArrayList data;
    private EnhancedTableModel tblShare;
    private int result;
    private int actionType;
    private HashMap dataHash;
    private int dataSize;
    private HashMap map;
    private String tdtMeetingDate = "";
    private String cboShareType;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorize = "";
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private Date createdDt = null;
    private String createdBy = "";
    private String Branchcode = "";
    private ArrayList tableHeader = new ArrayList();
    private Date meetingDate = null;
    private String meetingId = "";
    private String memberName = "";
    private String table = "";
    private int tableSize = 0;
    private final String EMPTY = "";
    private Date currDt = null;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private ProxyFactory proxy = null;
    private int noOfShares = 0;
    private String shareType = "";
    private double availableBalance = 0;
    private String shareAcctNum = " ";
    private ArrayList selectedArrayList;
    private HashMap collectedMap;
    private HashMap deSelectedMap;
    private ArrayList deletedArrayList;
    private String tdtFromDate = ""; // Added by nithya on 26-09-2016 for 2775

    public String getTdtFromDate() {
        return tdtFromDate;
    }

    public void setTdtFromDate(String tdtFromDate) {
        this.tdtFromDate = tdtFromDate;
    }    
   
    public HashMap getDeSelectedMap() {
        return deSelectedMap;
    }

    public void setDeSelectedMap(HashMap deSelectedMap) {
        this.deSelectedMap = deSelectedMap;
    }

    public HashMap getCollectedMap() {
        return collectedMap;
    }

    public void setCollectedMap(HashMap collectedMap) {
        this.collectedMap = collectedMap;
    }

    public String getLblStatus() {
        return lblStatus;
    }

    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public int getTableSize() {
        return tableSize;
    }

    public void setTableSize(int tableSize) {
        this.tableSize = tableSize;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getShareAcctNum() {
        return shareAcctNum;
    }

    public void setShareAcctNum(String shareAcctNum) {
        this.shareAcctNum = shareAcctNum;
    }

    public Date getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Date meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public int getNoOfShares() {
        return noOfShares;
    }

    public void setNoNfShares(int noOfShares) {
        this.noOfShares = noOfShares;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public String getAuthorize() {
        return authorize;
    }

    public void setAuthorize(String authorize) {
        this.authorize = authorize;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
    }

    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ActiveMemberListOB() {
        try {
            //System.out.println("1111111111111====");
            proxy = ProxyFactory.createProxy();
            //System.out.println("222222222222====");
            map = new HashMap();
            map.put(CommonConstants.JNDI, "ActiveMemberListJNDI");
            map.put(CommonConstants.HOME, "ActiveMemberList.ActiveMemberListHome");
            map.put(CommonConstants.REMOTE, "ActiveMemberList.ActiveMemberList");
            //System.out.println("33333333333333====");
            //fillDropdown();
            setTableTitle();
            //System.out.println("44444444444444====");
            tmlShare = new TableModel(null, tableHeader);
            //System.out.println("5555555555555====");
        } catch (Exception e) {
            //System.out.println("bb====" + e);
            parseException.logException(e, true);
        }
    }

    public String getBranchcode() {
        return Branchcode;
    }

    public void setBranchcode(String Branchcode) {
        this.Branchcode = Branchcode;
    }
    private TableModel tmlShare;
    private ArrayList updatedList = null;

    public TableModel getTmlShare() {
        return tmlShare;
    }

    public void setTmlShare(TableModel tmlShare) {
        this.tmlShare = tmlShare;
    }

    public String getCboShareType() {
        return cboShareType;
    }

    public void setCboShareType(String cboShareType) {
        this.cboShareType = cboShareType;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public EnhancedTableModel getTblShare() {
        return tblShare;
    }

    public void setTblShare(EnhancedTableModel tblShare) {
        this.tblShare = tblShare;
    }
    //__ ArrayLists for the Agent Table...
    ArrayList ShareTabTitle = new ArrayList();
//    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(ActiveMemberListUI.class);
    private String dpacnum = "";
    private Date lastComPaidDt = null;
    private Date curDate = null;
    private ComboBoxModel cbmShare_Type;
//    private final String BRANCH = TrueTransactMain.BRANCH_ID;

    private void setTableTitle() {
        tableTitle.add("Select");
        tableTitle.add("Customer Name");
        tableTitle.add("Available Balance");
        tableTitle.add("Share Acct Num");
        tableTitle.add("Branch Code");
    }

    public ComboBoxModel getCbmShare_Type() {
        return cbmShare_Type;
    }

    public void setCbmShare_Type(ComboBoxModel cbmShare_Type) {
        this.cbmShare_Type = cbmShare_Type;
    }

    public static ActiveMemberListOB getActive_MemberListOB() {
        return Active_MemberListOB;
    }

    public static void setActive_MemberListOB(ActiveMemberListOB Active_MemberListOB) {
        Active_MemberListOB.Active_MemberListOB = Active_MemberListOB;
    }

    public void resetTable() {
        try {
            ArrayList data = tblShare.getDataArrayList();
            for (int i = data.size(); i > 0; i--) {
                tblShare.removeRow(i - 1);
            }
        } catch (Exception e) {
            //setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in resetTable():");
        }
    }

    public void populateData(HashMap mapID, CTable tblData) {
        _tblData = tblData;

        // Adding Where Condition
        HashMap whereMap = null;
        if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
            if (mapID.get(CommonConstants.MAP_WHERE) instanceof HashMap) {
                whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
            } else {
                System.out.println("Convert other data type to HashMap:" + mapID);
            }
        } else {
            whereMap = new HashMap();
        }

        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }

        mapID.put(CommonConstants.MAP_WHERE, whereMap);

        System.out.println("Screen   : " + getClass());
        System.out.println("Map Name : " + mapID.get(CommonConstants.MAP_NAME));
        System.out.println("Map      : " + mapID);

//        _isAvailable = ClientUtil.setTableModel(mapID, tblData);
        dataHash = ClientUtil.executeTableQuery(mapID);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        //System.out.println("_heading===" + _heading);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        ArrayList tblDatanew = new ArrayList();
        for (int i = 0; i < data.size(); i++) {
            List tmpList = (List) data.get(i);
            ArrayList newList = new ArrayList();
            String chkVal = CommonUtil.convertObjToStr(tmpList.get(4));
            if (chkVal != null && chkVal.equalsIgnoreCase("Y")) {
                newList.add(new Boolean(false));
            } else {
                newList.add(new Boolean(false));
            }
            newList.add(tmpList.get(0));
            newList.add(tmpList.get(1));
            newList.add(tmpList.get(3));
            newList.add(tmpList.get(2));
            tblDatanew.add(newList);
        }
        tblShare = new EnhancedTableModel((ArrayList) tblDatanew, tableTitle);
        setDataSize(data.size());

//        System.out.println("### Data : "+data);
        //  if (getDataSize()<=MAXDATA)
        //  populateTable();
        //  return _heading;
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    private void populatetmlShare(ArrayList resultList) {
        if (resultList != null) {
            if (resultList.size() != 0) {
                int size = resultList.size();
                //  setTableSize(size);
                for (int i = 0; i < size; i++) {
                    GLLimitTO objTO = (GLLimitTO) resultList.get(i);
                    ArrayList dataList = new ArrayList();

                    if (updatedList == null) {
                        updatedList = new ArrayList();
                        updatedList.add(objTO.getSlNo());
                    } else {
                        updatedList.add(objTO.getSlNo());
                    }
                    dataList.add(objTO.getSlNo());
                    dataList.add(objTO.getAcHdId());
                    dataList.add(objTO.getLimitAmt());
                    dataList.add(objTO.getAnnualLimitAmt());
                    dataList.add(objTO.getOverDrawPer());
                    dataList.add(objTO.getFrmPeriod());
                    dataList.add(objTO.getToPeriod());
                    dataList.add(objTO.getAuthorizeStatus());
                    dataList.add(objTO.getInterBranchAllowed());
                    tmlShare.insertRow(0, dataList);
                }
            }
        }
    }

    public void populateSelectedRow(int rowIndex) {
        ArrayList rowData = tmlShare.getRow(rowIndex);
        setBranchcode(CommonUtil.convertObjToStr(rowData.get(0)));
        setMemberName(CommonUtil.convertObjToStr(rowData.get(2)));
        setAvailableBalance(CommonUtil.convertObjToDouble(rowData.get(3)));
        setMeetingId(CommonUtil.convertObjToStr(rowData.get(1)));
        notifyObservers();
    }

    public ArrayList getActiveMemberListData() {
        ActiveMemberListTo objActiveMemberTO;
        try {
            selectedArrayList = new ArrayList();
            //System.out.println("collectedMap : " + collectedMap + " collectedMap : " + collectedMap.size());
            Iterator iterate = collectedMap.entrySet().iterator();
            int i = -1;
            if (null != collectedMap && collectedMap.size() > 0) {
                ArrayList list = new ArrayList();
                while (iterate.hasNext()) {
                    objActiveMemberTO = new ActiveMemberListTo();
                    Map.Entry entry = (Map.Entry) iterate.next();
                    Object key1 = (Object) entry.getKey();
                    Object value = (Object) entry.getValue();
                    //System.out.println("key : " + key1.toString() + " value : " + value.toString());
                    list = (ArrayList) value;
                    if (null != list && list.size() > 0) {
                        objActiveMemberTO.setMemberName(list.get(0).toString());
                        objActiveMemberTO.setAvailableBalance(Double.parseDouble(list.get(1).toString()));
                        objActiveMemberTO.setShareAcctNum(list.get(2).toString());
                        objActiveMemberTO.setBranchcode(list.get(3).toString());
                        objActiveMemberTO.setStatusBy(ProxyParameters.USER_ID);
                        objActiveMemberTO.setMeetingDate(DateUtil.getDateMMDDYYYY(getTdtMeetingDate()));
                        objActiveMemberTO.setFromDate(DateUtil.getDateMMDDYYYY(getTdtFromDate())); // Added by nithya on 26-09-2016 for 2775
                        objActiveMemberTO.setBranchcode(TrueTransactMain.BRANCH_ID);
                        objActiveMemberTO.setCommand(getCommand());
                        if (getCommand().equalsIgnoreCase("INSERT")) {
                            objActiveMemberTO.setCreatedBy(TrueTransactMain.USER_ID);
                            objActiveMemberTO.setCreatedDt(currDt);
                            objActiveMemberTO.setStatus(getAction());
                        }
                        //System.out.println("objActiveMemberTO : " + objActiveMemberTO);
                    }
                    selectedArrayList.add(i + 1, objActiveMemberTO);
                }
            }
        } catch (Exception e) {
            log.info("Error In setInwardData()");
            e.printStackTrace();
        }
        //System.out.println("selectedArrayList : " + selectedArrayList);
        return selectedArrayList;
    }

    private String getCommand() {
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

    private String getAction() {
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

    public void doAction() {
        try {
            //System.out.println("am anju...................");
//            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
            //System.out.println("do action perform");
            doActionperform();
            //}
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    public void doActionperform() {
        try {
            final ActiveMemberListTo objActiveMemberTO = new ActiveMemberListTo();
            final HashMap data = new HashMap();
            data.put("COMMAND", getCommand());
            HashMap dataMap = new HashMap();
            HashMap proxyReturnMap = new HashMap();
            dataMap.put("COMMAND", getCommand());
            if (getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                dataMap.put("insertActiveMemberList", getActiveMemberListData());
            } else if (getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                dataMap.put("updateActiveMemberList", getActiveMemberListUpdateData());
            }
            //System.out.println("dataMap== :  " + dataMap);
            proxyReturnMap = proxy.execute(dataMap, map);
            if (proxyReturnMap != null && proxyReturnMap.containsKey("TOTAL_COUNT") && getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                ClientUtil.showMessageWindow("Total inserted record. : " + proxyReturnMap.get("TOTAL_COUNT"));
            } else if (proxyReturnMap != null && proxyReturnMap.containsKey("TOTAL_COUNT") && getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                ClientUtil.showMessageWindow("Total deleted record. : " + proxyReturnMap.get("TOTAL_COUNT"));                
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
            e.printStackTrace();
        }

    }
    private static ActiveMemberListOB Active_MemberListOB;

    static {
        try {
            log.info("In AgentOB Declaration");
            Active_MemberListOB = new ActiveMemberListOB();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void resetForm() {
        tmlShare = new TableModel(null, tableHeader);
        notifyObservers();
    }

    public static ActiveMemberListOB getInstance() {
        return Active_MemberListOB;
    }

    /**
     * Creates a new instance of InwardClearingOB
     */
    // To set the Column title in Table...
    private void settblShareTabTitle() throws Exception {
        log.info("In setAgentTabTitle...");

    }

    /**
     * To perform the necessary action
     */
    // to decide which action Should be performed...
    public void fillDropDown() {
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        HashMap mapShare = new HashMap();
        List keyValue = null;
        //setCbmShare_Type(Share_Type);

        keyValue = ClientUtil.executeQuery("getsharetype", mapShare);
        //System.out.println("keyValue=======" + keyValue);
        key.add("");
        value.add("");

        if (keyValue != null && keyValue.size() > 0) {
            for (int i = 0; i < keyValue.size(); i++) {
                mapShare = (HashMap) keyValue.get(i);
                key.add(mapShare.get("SHARE_TYPE"));
                value.add(mapShare.get("SHARE_TYPE"));
            }
        }
        //System.out.println("key======" + key);
       //System.out.println("value======" + value);
        cbmShare_Type = new ComboBoxModel(key, value);
        key = null;
        value = null;

        keyValue.clear();
        keyValue = null;
        mapShare.clear();
        mapShare = null;
    }

    public void setShareTabData() {
    }

    public String getTdtMeetingDate() {
        return tdtMeetingDate;
    }

    public void setTdtMeetingDate(String tdtMeetingDate) {
        this.tdtMeetingDate = tdtMeetingDate;
    }
    
//edit mode displaying already existing records in table
    public void populateDataInTable(HashMap whereMap, CTable tblData) {
        _tblData = tblData;
        HashMap shareTypeMap = new HashMap();
        whereMap.put("MEETING_DATE", meetingDate);
        //whereMap.put("SHARE_TYPE", ShareType);
        shareTypeMap.put(CommonConstants.MAP_WHERE, whereMap);
        shareTypeMap.put(CommonConstants.MAP_NAME, "getSelectShareAccountTO");
        shareTypeMap.put(CommonConstants.MAP_WHERE, whereMap);
        System.out.println("Screen   : " + getClass());
        System.out.println("Map Name : " + shareTypeMap.get(CommonConstants.MAP_NAME));
        System.out.println("Map      : " + shareTypeMap);
        dataHash = ClientUtil.executeTableQuery(shareTypeMap);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        //System.out.println("_heading===" + _heading);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        //System.out.println("data === : " + data);
        ArrayList tblDatanew = new ArrayList();
        collectedMap = new HashMap();
        for (int i = 0; i < data.size(); i++) {
            List tmpList = (List) data.get(i);
            //System.out.println("### tmpList : " + tmpList);
            ArrayList newList = new ArrayList();
            newList.add(new Boolean(true));
            newList.add(tmpList.get(0));
            newList.add(tmpList.get(1));
            newList.add(tmpList.get(3));
            newList.add(tmpList.get(2));
            tblDatanew.add(newList);
            collectedMap.put(i, newList);
            //System.out.println("### tblDatanew : " + tblDatanew);
        }
        // setCollectedMap(i,newList);
        //System.out.println("### collectedMap : " + collectedMap);
        tblShare = new EnhancedTableModel((ArrayList) tblDatanew, tableTitle);
        setDataSize(data.size());        
    }
    
//edit mode updating status as deleted
    public ArrayList getActiveMemberListUpdateData() {
        ActiveMemberListTo objActiveMemberTO;
        try {
            deletedArrayList = new ArrayList();
            //System.out.println("deSelectedMap : " + deSelectedMap + " deSelectedMap : " + deSelectedMap.size());
            Iterator iterate = deSelectedMap.entrySet().iterator();
            int i = -1;
            if (null != deSelectedMap && deSelectedMap.size() > 0) {
                ArrayList list = new ArrayList();
                while (iterate.hasNext()) {
                    objActiveMemberTO = new ActiveMemberListTo();
                    Map.Entry entry = (Map.Entry) iterate.next();
                    Object key1 = (Object) entry.getKey();
                    Object value = (Object) entry.getValue();
                    //System.out.println("key : " + key1.toString() + " value : " + value.toString());
                    list = (ArrayList) value;
                    if (null != list && list.size() > 0) {
                        objActiveMemberTO.setMemberName(list.get(0).toString());
                        objActiveMemberTO.setAvailableBalance(Double.parseDouble(list.get(1).toString()));
                        objActiveMemberTO.setShareAcctNum(list.get(2).toString());
                        objActiveMemberTO.setBranchcode(list.get(3).toString());
                        objActiveMemberTO.setStatusBy(ProxyParameters.USER_ID);
                        objActiveMemberTO.setMeetingDate(DateUtil.getDateMMDDYYYY(getTdtMeetingDate()));
                        objActiveMemberTO.setFromDate(DateUtil.getDateMMDDYYYY(getTdtFromDate())); // nithya
                        objActiveMemberTO.setBranchcode(TrueTransactMain.BRANCH_ID);
                        objActiveMemberTO.setCommand(getCommand());
                        objActiveMemberTO.setCreatedBy(TrueTransactMain.USER_ID);
                        objActiveMemberTO.setCreatedDt(currDt);
                        objActiveMemberTO.setStatus(CommonConstants.STATUS_DELETED);
                    }
                    //System.out.println("objActiveMemberTO : " + objActiveMemberTO);
                    deletedArrayList.add(i + 1, objActiveMemberTO);
                }

            }
        } catch (Exception e) {
            log.info("Error In setInwardData()");
            e.printStackTrace();
        }
        System.out.println("deletedArrayList : " + deletedArrayList);
        return deletedArrayList;
    }
}
