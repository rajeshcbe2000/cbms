/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CustomerIdChangeOB.java
 *
 * Created on Jul 24, 2019, 10:53 AM
 */
package com.see.truetransact.ui.indend.reservedepreciationachd;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uicomponent.CObservable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Suresh R
 *
 */
public class ReserveDepreciationAcHdOB extends CObservable {

    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap map;
    private ProxyFactory proxy;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    private EnhancedTableModel tblIndendCloseDetails;
    final ArrayList tblIndendCloseDetailsTitle = new ArrayList();
    private List finalCloseList = null;
    List incList = null;
    List expList = null;

    public ReserveDepreciationAcHdOB() {
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "ReserveDepreciationAcHdJNDI");
            map.put(CommonConstants.HOME, "ReserveDepreciationAcHdHome");
            map.put(CommonConstants.REMOTE, "ReserveDepreciationAcHd");
            setTblIndendCloseDetails();
            tblIndendCloseDetails = new EnhancedTableModel(null, tblIndendCloseDetailsTitle);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void setTblIndendCloseDetails() {
        tblIndendCloseDetailsTitle.add("AC_HD_ID");
        tblIndendCloseDetailsTitle.add("INC_AC_HD");
        tblIndendCloseDetailsTitle.add("EXP_AC_HD");
    }

    public void set1IncList(List list) {
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        for (int i = 0, j = list.size(); i < j; i++) {
            key.add(((HashMap) list.get(i)).get("KEY"));
            value.add(((HashMap) list.get(i)).get("VALUE"));
        }
        setIncList(value);
    }

    public void set2ExpList(List list) {
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        for (int i = 0, j = list.size(); i < j; i++) {
            key.add(((HashMap) list.get(i)).get("KEY"));
            value.add(((HashMap) list.get(i)).get("VALUE"));
        }
        setExpList(value);
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    public void doAction() {
        try {
            doActionPerform();

        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    private void doActionPerform() throws Exception {
        final HashMap data = new HashMap();
        data.put("RESERVE_DEP_ACHD", getFinalCloseList());
        HashMap proxyResultMap = proxy.execute(data, map);
        if (proxyResultMap.containsKey("SUCCESS")) {
            com.see.truetransact.clientutil.ClientUtil.showAlertWindow("Records Inserted/Updated Successfully...");
        }
        setProxyReturnMap(proxyResultMap);
        setResult(getActionType());
    }

    public void getData(HashMap whereMap) {
        try {
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void resetTableValues() {
        tblIndendCloseDetails.setDataArrayList(null, tblIndendCloseDetailsTitle);
    }

    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }

    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public java.lang.String getLblStatus() {
        return lblStatus;
    }

    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public EnhancedTableModel getTblIndendCloseDetails() {
        return tblIndendCloseDetails;
    }

    public void setTblIndendCloseDetails(EnhancedTableModel tblIndendCloseDetails) {
        this.tblIndendCloseDetails = tblIndendCloseDetails;
    }

    public List getFinalCloseList() {
        return finalCloseList;
    }

    public void setFinalCloseList(List finalCloseList) {
        this.finalCloseList = finalCloseList;
    }

    public List getExpList() {
        return expList;
    }

    public void setExpList(List expList) {
        this.expList = expList;
    }

    public List getIncList() {
        return incList;
    }

    public void setIncList(List incList) {
        this.incList = incList;
    }
}
