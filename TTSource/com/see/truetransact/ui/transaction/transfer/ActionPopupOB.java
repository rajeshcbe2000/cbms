/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ActionPopupOB.java
 *
 * Created on August 12, 2003, 3:43 PM
 */

package com.see.truetransact.ui.transaction.transfer;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.transferobject.transaction.transfer.DetailTransferTO;
import com.see.truetransact.transferobject.transaction.transfer.MasterTransferTO;
import com.see.truetransact.uicomponent.CObservable;
import java.util.ArrayList;
import java.util.HashMap;
import com.see.truetransact.clientproxy.ProxyParameters;
import java.util.Date;
import com.see.truetransact.clientutil.ClientUtil;

/**
 *
 * @author  Pranav
 *
 * @modified Pinky
 */

public class ActionPopupOB extends CObservable {
    
    private int operation;
    private String selectedBatch;
    private Date transaction_Dt=null;
    private String initiatedBranch;
    private boolean selectAllDetails;
    private boolean _isAvailable;
    private ProxyFactory proxy = null;
    
    private TableModel masterModel = new TableModel();
    private TableModel detailModel = new TableModel();
    
    ArrayList detailTOList = null;
    HashMap map;
    private Date currDt = null;
    /** Creates a new instance of ActionPopupOB */
    public ActionPopupOB(int operation) {
        this.operation = operation;
        map = new HashMap();
        // put the map settings in place
        map.put(CommonConstants.JNDI, "TransferJNDI");
        map.put(CommonConstants.HOME, "transaction.transfer.TransferHome");
        map.put(CommonConstants.REMOTE, "transaction.transfer.TransferRemote");
        currDt = ClientUtil.getCurrentDate();
        try {
            /* pass the null value, the action based dependency is taken care of
             * in side the getMasterData() method itself
             */
            getMasterData(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean getSelectAllDetails() {
        return selectAllDetails;
    }
    
    public void setSelectAllDetails(boolean selectAllDetails) {
        this.selectAllDetails = selectAllDetails;
        setChanged();
        notifyObservers();
    }
    
    public java.lang.String getSelectedBatch() {
        return selectedBatch;
    }
    
    public void setSelectedBatch(java.lang.String selectedBatch) {
        this.selectedBatch = selectedBatch;
    }
    
    public TableModel getDetailModel() {
        return detailModel;
    }
    
    public void setDetailModel(TableModel _detailModel) {
        detailModel = _detailModel;
        setChanged();
        notifyObservers();
    }
    
    public TableModel getMasterModel() {
        return masterModel;
    }
    
    public void setMasterModel(TableModel _masterModel) {
        masterModel = _masterModel;
        setChanged();
        notifyObservers();
    }
    
    private ArrayList getMasterHeader() {
        ArrayList header = new ArrayList();
        
        // Create the header part of the master table
        header.add("Batch ID");
        header.add("Instrument Count (CR)");
        header.add("Amount (CR)");
        header.add("Instrument Count (DR)");
        header.add("Amount (DR)");
        header.add("Created By");
        header.add("Trans Date");
        header.add("Initiated Branch");
        
        return header;
    }
    
    private ArrayList getDetailHeader() {
        ArrayList header = new ArrayList();
        
        /* Create the header part of the master table, it will also show a combo
         * box, in the scenario where more than one selection is possible.
         * currently, only "Edit" action is without any combobox
         */
        header.add("Transaction ID");
        header.add("Account Head");
        header.add("Account No.");
        header.add("Accound Holder");
        header.add("Transaction Type");
        header.add("Amount");
        header.add("Status");
        
        return header;
    }
    
    public int getMasterData(Object obj) {
        HashMap returnMap = null;
        ArrayList data = new ArrayList();
        ArrayList masterTOList = new ArrayList();
        HashMap hash = new HashMap();
        try {
            proxy = ProxyFactory.createProxy();
            
            HashMap whereMap = null;
            // get the customerdetails from database
            String mapName = "getMasterTransferTO";
            if( operation == ClientConstants.ACTIONTYPE_AUTHORIZE ||
            operation == ClientConstants.ACTIONTYPE_REJECT ||
            operation == ClientConstants.ACTIONTYPE_EXCEPTION ) {
                String authStatus = "";
                if (operation == ClientConstants.ACTIONTYPE_AUTHORIZE) {
                    authStatus = CommonConstants.STATUS_AUTHORIZED;
                } else if (operation == ClientConstants.ACTIONTYPE_REJECT) {
                    authStatus = CommonConstants.STATUS_REJECTED;
                } else if (operation == ClientConstants.ACTIONTYPE_EXCEPTION) {
                    authStatus = CommonConstants.STATUS_EXCEPTION;
                }
                mapName = "getAuthorizeMasterTransferTO";
                whereMap = new HashMap();
                whereMap.put("AUTHORIZESTATUS", authStatus);
                whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                whereMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
                whereMap.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
                hash.put(CommonConstants.MAP_WHERE, whereMap);
            }
            else 
            {
                hash.put(CommonConstants.MAP_WHERE, ProxyParameters.BRANCH_ID);
            }
                hash.put(CommonConstants.MAP_NAME, mapName);
                returnMap = (HashMap) proxy.executeQuery(hash, map);
                System.out.println("returnMap = " + returnMap);
            /* go throught entire list of data comming from database, feed it
             * in the arraylist and put that arraylist further into one more
             * arraylist
             */
             
           if(returnMap!=null) 
            {
            masterTOList = (ArrayList)returnMap.get(CommonConstants.DATA);
            //The following if code (only 1 line) added by Rajesh
            System.out.println("masterTOList.size() : " + masterTOList.size());
            if (masterTOList.size()!=0){
                _isAvailable=true;
            MasterTransferTO to;
            ArrayList temp;
            
            for (int i = 0; i < masterTOList.size(); i++) {
                to = (MasterTransferTO)masterTOList.get(i);
                temp = new ArrayList();
                temp.add(to.getBatchId());
                temp.add(to.getInstCr() == null ? "0" : to.getInstCr());
                temp.add(to.getAmountCr() == null ? new Double("0.0") : to.getAmountCr());
                temp.add(to.getInstDr() == null ? "0" : to.getInstDr());
                temp.add(to.getAmountDr() == null ? new Double("0.0") : to.getAmountDr());                
                temp.add(to.getCreatedBy());
                temp.add(to.getTransDt());
                temp.add(to.getInitBran());
                data.add(temp);
            }
            /* overriding the isCellEditable() method, because the default implementation
             * allows editing of all the cells, but in this scenariowe need to edit only
             * the first cell, that has the combo box
             */
            setMasterModel(new TableModel(data, getMasterHeader()) {
                public boolean isCellEditable(int rowIndex, int mColIndex) {
                    return false;
                }
            });}}
        } catch( Exception e ) {
            e.printStackTrace();
        }
        return masterTOList.size();
    }
    public int getDetailData() {
        ArrayList data = new ArrayList();
        HashMap toMap = new HashMap();
        HashMap whereMap=new HashMap();
        try {
            proxy = ProxyFactory.createProxy();
            String mapName = "getDetailTransferTO";
            if( operation == ClientConstants.ACTIONTYPE_AUTHORIZE ||
            operation == ClientConstants.ACTIONTYPE_REJECT ||
            operation == ClientConstants.ACTIONTYPE_EXCEPTION )
                mapName = "getAuthorizeDetailTransferTO";
            if(operation == ClientConstants.ACTIONTYPE_EXCEPTION){
                whereMap.put("EXCEPTION","EXCEPTION");
            }
                whereMap.put("SELECTED_BATCH",selectedBatch);
                whereMap.put("TRANS_DT", currDt.clone());
                whereMap.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);   
            toMap.put(CommonConstants.MAP_NAME,mapName);
            
            if (mapName.equals("getDetailTransferTO"))
                toMap.put(CommonConstants.MAP_WHERE, selectedBatch);
            else
                toMap.put(CommonConstants.MAP_WHERE, whereMap);
            
            detailTOList = (ArrayList)(((HashMap)proxy.executeQuery(toMap, map)).get(CommonConstants.DATA));
            
            
            /* go throught entire list of data comming from database, feed it
             * in the arraylist and put that arraylist further into one more
             * arraylist
             */
            DetailTransferTO to;
            ArrayList temp;
            for (int i = 0; i < detailTOList.size(); i++) {
                
                to = (DetailTransferTO)detailTOList.get(i);
                
                temp = new ArrayList();
                temp.add(to.getTransId());
                temp.add(to.getAcHdDesc());
                temp.add(to.getActNum());
                temp.add(new String((to.getActFName()==null ? "" : to.getActFName()) + " " + (to.getActMName() == null ? "" : (to.getActMName() + " ")) + (to.getActLName()==null ? "":to.getActLName())));
                temp.add(to.getTransType());
                temp.add(to.getAmount());
                temp.add(to.getStatus());
                data.add(temp);
            }
            /* overriding the isCellEditable() method, because the default implementation
             * allows editing of all the cells, but in this scenariowe need to edit only
             * the first cell, that has the combo box
             */
            setDetailModel(new TableModel(data, getDetailHeader()) {
                public boolean isCellEditable(int rowIndex, int mColIndex) {
                    return false;
                }
            });
        } catch( Exception e ) {
            System.err.println( "Exception " + e + " caught" );
        }
        return detailTOList.size();
    }
    
    public boolean isAvailable() {
        return _isAvailable;
    }
    
    /**
     * Getter for property transaction_Dt.
     * @return Value of property transaction_Dt.
     */
    public java.util.Date getTransaction_Dt() {
        return transaction_Dt;
    }
    
    /**
     * Setter for property transaction_Dt.
     * @param transaction_Dt New value of property transaction_Dt.
     */
    public void setTransaction_Dt(java.util.Date transaction_Dt) {
        this.transaction_Dt = transaction_Dt;
    }
    
    /**
     * Getter for property initiatedBranch.
     * @return Value of property initiatedBranch.
     */
    public java.lang.String getInitiatedBranch() {
        return initiatedBranch;
    }
    
    /**
     * Setter for property initiatedBranch.
     * @param initiatedBranch New value of property initiatedBranch.
     */
    public void setInitiatedBranch(java.lang.String initiatedBranch) {
        this.initiatedBranch = initiatedBranch;
    }
    
}