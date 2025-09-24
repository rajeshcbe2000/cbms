

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * NbChequeBookMaintenanceOB.java
 */

package com.see.truetransact.ui.netbankingrequest.nbchequebookmaintenance;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.netbankingrequest.nbchequebookmaintenance.NbChequeBookMaintenanceTO;
import com.see.truetransact.ui.TrueTransactMain;
import static com.sun.tools.corba.se.idl.InterfaceState.Private;
import java.util.*;

/**
 *
 * @author Abhishek
 */
public class NbChequeBookMaintenanceOB extends CObservable {

    private HashMap map;
    private ProxyFactory proxy;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private EnhancedTableModel tblCbRequest;
    private EnhancedTableModel tblPanCardDetails;
    private HashMap checkMap;
    ArrayList tableTitle = new ArrayList();
    ArrayList tableTitle2 = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private ArrayList IncVal2 = new ArrayList();
    private String lblCustid2 = "";
    private String lblAccNo2 = "";
    private String lblCbRequest2 = "";
    private String lblNoOfCbLeave2 = "";
    private String lblUsageType2 = "";
    private int _actionType;
    private HashMap _authorizeMap;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    //private LinkedHashMap netbankingmap;
    private int actionType;
    private int _result;
    String Cid;
    private LinkedHashMap tblTOMap = new LinkedHashMap();
    public List selectedList = new ArrayList();
    public List deselectedList = new ArrayList();

    public LinkedHashMap getTblTOMap() {
        return tblTOMap;
    }

    public void setTblTOMap(LinkedHashMap tblTOMap) {
        this.tblTOMap = tblTOMap;
    }

    public int getActionType() {
        return _actionType;
    }

    public void setActionType(int actionType) {
        this._actionType = actionType;
    }

    public HashMap getAuthorizeMap() {
        return _authorizeMap;
    }

    public void setAuthorizeMap(HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }

    public String getLblAccNo2() {
        return lblAccNo2;
    }

    public void setLblAccNo2(String lblAccNo2) {
        this.lblAccNo2 = lblAccNo2;
    }

    public String getLblCbRequest2() {
        return lblCbRequest2;
    }

    public void setLblCbRequest2(String lblCbRequest2) {
        this.lblCbRequest2 = lblCbRequest2;
    }

    public String getLblCustid2() {
        return lblCustid2;
    }

    public void setLblCustid2(String lblCustid2) {
        this.lblCustid2 = lblCustid2;
    }

    public String getLblNoOfCbLeave2() {
        return lblNoOfCbLeave2;
    }

    public void setLblNoOfCbLeave2(String lblNoOfCbLeave2) {
        this.lblNoOfCbLeave2 = lblNoOfCbLeave2;
    }

    public String getLblUsageType2() {
        return lblUsageType2;
    }

    public void setLblUsageType2(String lblUsageType2) {
        this.lblUsageType2 = lblUsageType2;
    }

    public String getLblStatus() {
        return lblStatus;
    }

    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
    }

    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    public EnhancedTableModel getTblCbRequest() {
        return tblCbRequest;
    }

    public void setTblCbRequest(EnhancedTableModel tblCbRequest) {
        this.tblCbRequest = tblCbRequest;
    }

    public NbChequeBookMaintenanceOB() {
        try {
            proxy = ProxyFactory.createProxy();

            map = new HashMap();
            map.put(CommonConstants.JNDI, "NbChequeBookMaintenanceJNDI");
            map.put(CommonConstants.HOME, "NbChequeBookMaintenanceHome");
            map.put(CommonConstants.REMOTE, "NbChequeBookMaintenance");
            //fillDropdown();
            setCbRequestTableTitle();
            tblCbRequest = new EnhancedTableModel(null, tableTitle);


        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void setCbRequestTableTitle() {
        tableTitle.add("Select");
        tableTitle.add("Customer Id");
        tableTitle.add("Account Number");
        tableTitle.add("CB Request");
        tableTitle.add("No Of CB Leave");
        tableTitle.add("Usage Type");
        IncVal = new ArrayList();
    }

    public void resetForm() {
        setLblCustid2("");
        setLblAccNo2("");
        setLblCbRequest2("");
        setLblNoOfCbLeave2("");
        setLblUsageType2("");
    }

    public void populateData(HashMap whereMap) throws Exception {
        HashMap mapData = new HashMap();
        ArrayList incTabRow = new ArrayList();

        mapData = proxy.executeQuery(whereMap, map);
        if (mapData.containsKey("chequeBookMaintenanceList"));
        if (checkMap == null) {
            checkMap = new HashMap();
        }
        checkMap = (HashMap) mapData.get("chequeBookMaintenanceList");
        if (checkMap != null && !checkMap.isEmpty()) {
            ArrayList addList = new ArrayList(checkMap.keySet());
            Map selOutput = new HashMap();
            Map deselOutput = new HashMap();
            for (int i = 0; i < addList.size(); i++) {
                NbChequeBookMaintenanceTO objNbChequeBookMaintenanceTO = (NbChequeBookMaintenanceTO) checkMap.get(addList.get(i));
                incTabRow = new ArrayList();
                if (getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    incTabRow.add(new Boolean(false));
                    deselOutput.put("CUST_ID", CommonUtil.convertObjToStr(objNbChequeBookMaintenanceTO.getCustId()));
                    deselOutput.put("ACCOUNT_NUM", CommonUtil.convertObjToStr(objNbChequeBookMaintenanceTO.getActNum()));
                    deselectedList.add(deselOutput);
                } else if (_actionType == ClientConstants.ACTIONTYPE_EDIT || _actionType == ClientConstants.ACTIONTYPE_DELETE
                        || _actionType == ClientConstants.ACTIONTYPE_AUTHORIZE || _actionType == ClientConstants.ACTIONTYPE_REJECT) {
                    incTabRow.add(new Boolean(true));
                    selOutput.put("CUST_ID", CommonUtil.convertObjToStr(objNbChequeBookMaintenanceTO.getCustId()));
                    selOutput.put("ACCOUNT_NUM", CommonUtil.convertObjToStr(objNbChequeBookMaintenanceTO.getActNum()));
                    selectedList.add(selOutput);
                }
                incTabRow.add(CommonUtil.convertObjToStr(objNbChequeBookMaintenanceTO.getCustId()));
                incTabRow.add(CommonUtil.convertObjToStr(objNbChequeBookMaintenanceTO.getActNum()));
                incTabRow.add(CommonUtil.convertObjToStr(objNbChequeBookMaintenanceTO.getCbRequest()));
                incTabRow.add(CommonUtil.convertObjToStr(objNbChequeBookMaintenanceTO.getNoOfCbLeave()));
                incTabRow.add(CommonUtil.convertObjToStr(objNbChequeBookMaintenanceTO.getUsageType()));
                tblCbRequest.addRow(incTabRow);
            }
        }
    }

    public void resetCbRequestTableValues() {
        tblCbRequest.setDataArrayList(null, tableTitle);
    }

    public void doAction() {
        try {
            if (_actionType != ClientConstants.ACTIONTYPE_CANCEL || getAuthorizeMap() != null) {
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
            if (selectedList != null && selectedList.size() > 0) {
                data.put("SelChequeBookDetails", selectedList);
            }
            if (deselectedList != null && deselectedList.size() > 0) {
                data.put("DeselChequeBookDetails", deselectedList);
            }
        } else {
            if (selectedList != null && selectedList.size() > 0) {
                data.put("SelAuthChequeBookDetails", selectedList);
            }
            if (deselectedList != null && deselectedList.size() > 0) {
                data.put("DeselAuthChequeBookDetails", deselectedList);
            }
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
        }
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        data.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        HashMap proxyResultMap = proxy.execute(data, map);

        setProxyReturnMap(proxyResultMap);
        _authorizeMap = null;
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

    public void setResult(int result) {
        _result = result;
    }
}
