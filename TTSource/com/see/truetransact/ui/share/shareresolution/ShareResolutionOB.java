/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ShareResolutionOB.java
 *
 * Created on April 23, 2005, 4:09 PM
 */

package com.see.truetransact.ui.share.shareresolution;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.ui.deposit.TableManipulation;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.JointAcctHolderManipulation;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.regex.Pattern;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import com.see.truetransact.transferobject.share.shareresolution.ShareResolutionTO;
import com.see.truetransact.transferobject.share.ShareAcctDetailsTO;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;

/**
 *
 * @author  JK
 */
public class ShareResolutionOB extends CObservable {
    Date curDate = null;
    private boolean chkSelectAll = false;
    private String txtResolutionNo = "";
    private String tdtResolutionDt = "";
    private boolean chkApplicationFee=false;
    private boolean chkMemberShipFee=false;
    private boolean chkShareFee=false;
    //--- Declarations for operationMap
    private final String SHARE_RES_JNDI = "ShareResolutionJNDI";
    private final String SHARE_RES_HOME = "share.ShareResolutionHome";
    private final String SHARE_RES_REMOTE = "share.ShareResolution";
    private int actionType;
    private ProxyFactory proxy;
    private HashMap newTransactionMap;
    private HashMap operationMap;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private static ShareResolutionOB shareResolutionOB; // singleton object
    
    static {
        try {
            
            shareResolutionOB = new ShareResolutionOB();
            
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public static ShareResolutionOB getInstance() {
        return shareResolutionOB;
    }
    
    /** Creates a new instance of ShareResolutionOB */
    private ShareResolutionOB() throws Exception{
        curDate = ClientUtil.getCurrentDate();
        proxy = ProxyFactory.createProxy();
        setOperationMap();
    }
    
    //--- TO Object Declarations and HashMap Declarations for doActionPerform
    HashMap data;
    ShareResolutionTO shareResolutionTO;
    LinkedHashMap shareResolutionTOMap;
    private final String SHARE_ACC_DET_FOR_DAO = "ShareAccDet";
    private final String SHARE_RES_FOR_DAO = "ShareResolution";
    
    /** To perform the appropriate operation */
    public boolean doAction() throws Exception {
        boolean result = true;
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if(getCommand() != null){
//                    System.out.println("doAction getSelected():" + getSelected());
                    doActionPerform();
                }
                else{
                    throw new TTException("TTEXCEPTION");
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            result = false;
            System.out.println("e in ShareResolutionOB : " + e);
            parseException.logException(e,true);
        }
        return result;
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        data = new HashMap();
       if(getChkShareFee()==true){
           data.put("SHARE_FEE","SHARE_FEE");
       }
        if(getChkMemberShipFee()==true){
            data.put("MEMBERSHIP_FEE","MEMBERSHIP_FEE");
        }
        if(getChkApplicationFee()==true){
            data.put("APPLICATION_FEE","APPLICATION_FEE");
        }
         if(getNewTransactionMap() != null ){
            data.put("Transaction Details Data", getNewTransactionMap());
        }
        data.put(SHARE_ACC_DET_FOR_DAO, getSelected());
       
        data.put(SHARE_RES_FOR_DAO, setResolution());
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        System.out.println("data:" + data);
        List list=(List)data.get("ShareAccDet");
      int n= list.size();
      System.out.println("the size iz"+n);
     int action= getActionType();
     int no=15;
     System.out.println("action type is"+action);
     if(action==no){
         System.out.println("reject1111>>>>");
           if(n>1){
               ClientUtil.displayAlert("select one record at a time for rejection");
           }
           else
           {
                System.out.println("reject2222>>>>");
        HashMap proxyResultMap = proxy.execute(data,operationMap);
        System.out.println("proxyResultMap 1111 is>>>"+proxyResultMap);
          if (proxyResultMap != null && (proxyResultMap.containsKey(CommonConstants.TRANS_ID) || proxyResultMap.containsKey("CASH_TRANS_LIST") || proxyResultMap.containsKey("TRANSFER_TRANS_LIST"))) {
            setProxyReturnMap(proxyResultMap);
            if(proxyResultMap.containsKey(CommonConstants.TRANS_ID)){
                 ClientUtil.showMessageWindow("Share Account No: " + CommonUtil.convertObjToStr(proxyResultMap.get(CommonConstants.TRANS_ID)));
                 displayShareResolution(proxyResultMap);
                 setProxyReturnMap(null);
            }
          }
           }
     }
     else
     {
          System.out.println("accept1111>>>>");
         HashMap proxyResultMap = proxy.execute(data,operationMap);
         System.out.println("proxyResultMap 2222>>>"+proxyResultMap);
          if (proxyResultMap != null && (proxyResultMap.containsKey(CommonConstants.TRANS_ID) || proxyResultMap.containsKey("CASH_TRANS_LIST") || proxyResultMap.containsKey("TRANSFER_TRANS_LIST"))) {
            setProxyReturnMap(proxyResultMap);
            if(proxyResultMap.containsKey(CommonConstants.TRANS_ID)){
                 ClientUtil.showMessageWindow("Share Account No: " + CommonUtil.convertObjToStr(proxyResultMap.get(CommonConstants.TRANS_ID)));
                 displayShareResolution(proxyResultMap);
                 setProxyReturnMap(null);
            }
          } 
     }
     
        setResult(actionType);
        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        resetForm();
        data=null;
    
    }
    
        private void displayShareResolution(HashMap proxyResultMap) {
        try{
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Do you want to print Share Application?", CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        System.out.println("YES -> 0 / NO -> 1" + yesNo);
            if (yesNo == 0) {
                String reportname = "";
                if (proxyResultMap != null && proxyResultMap.containsKey("SingleTransID")) {
                    String applNum = CommonUtil.convertObjToStr(proxyResultMap.get("SingleTransID"));
                    TTIntegration ttIntgration = null;
                    HashMap paramMap = new HashMap();
                    if(proxyResultMap.containsKey("SingleTransID") && proxyResultMap.get("SingleTransID") != null)
                    paramMap.put("TransId", CommonUtil.convertObjToStr(proxyResultMap.get("SingleTransID")));
                    paramMap.put("TransDt", curDate);
                    paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                    reportname = "ReceiptPayment";
                    ttIntgration.setParam(paramMap);
                    ttIntgration.integrationForPrint(reportname, false);
            }
        }
        }catch(Exception e){
            ClientUtil.showMessageWindow("File not Found....!");
        }
    }
    
    private ShareResolutionTO setResolution() throws Exception {
        HashMap singleRecordShareRes;
        shareResolutionTO = new ShareResolutionTO();
        shareResolutionTO.setCommand(getCommand());
        System.out.println("shareResolutionTO.getCommand()" + shareResolutionTO.getCommand());
        shareResolutionTO.setResolutionNo(getTxtResolutionNo());
        Date IsDt = DateUtil.getDateMMDDYYYY(getTdtResolutionDt());
        if(IsDt != null){
        Date isDate = (Date)curDate.clone();
        isDate.setDate(IsDt.getDate());
        isDate.setMonth(IsDt.getMonth());
        isDate.setYear(IsDt.getYear());
        shareResolutionTO.setResolutionDt(isDate);
        }else{
            shareResolutionTO.setResolutionDt(DateUtil.getDateMMDDYYYY(getTdtResolutionDt()));
        }
//        shareResolutionTO.setResolutionDt(DateUtil.getDateMMDDYYYY(getTdtResolutionDt()));
        shareResolutionTO.setStatus(CommonConstants.STATUS_CREATED);
        shareResolutionTO.setStatusDt(curDate);
        shareResolutionTO.setStatusBy(TrueTransactMain.USER_ID);
        return shareResolutionTO;
    }
    
    
    /* To get the command type according to the Action */
    private String getCommand() throws Exception{
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
            case ClientConstants.ACTIONTYPE_RENEW:
                command = CommonConstants.TOSTATUS_RENEW;
                break;
            case ClientConstants.ACTIONTYPE_RESOLUTION_ACCEPT:
                command = CommonConstants.TOSTATUS_RESOLUTION_ACCEPT;
                break;
            case ClientConstants.ACTIONTYPE_RESOLUTION_REJECT:
                command = CommonConstants.TOSTATUS_RESOLUTION_REJECT;
                break;
            case ClientConstants.ACTIONTYPE_RESOLUTION_DEFFERED:
                command = CommonConstants.TOSTATUS_RESOLUTION_DEFFERED;
                break;
            default:
        }
        
        return command;
    }
    
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    
    public int getResult(){
        return result;
    }
    
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, SHARE_RES_JNDI);
        operationMap.put(CommonConstants.HOME, SHARE_RES_HOME);
        operationMap.put(CommonConstants.REMOTE, SHARE_RES_REMOTE);
    }
    
    public String getLblStatus(){
        return (this.lblStatus);
    }
    public void setLblStatus(String lblStatus){
        this.lblStatus=lblStatus;
        setChanged();
    }
    public void setStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    
    /* To get the Action Type */
    public int getActionType(){
        return this.actionType;
    }
    
    /* To set the Action Type */
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    
    private boolean _isAvailable = true;
    private TableModel _tableModel;
    private CTable _tblData;
    private ArrayList _heading;
    private String userID = ProxyParameters.USER_ID;
    private String branchID = ProxyParameters.BRANCH_ID;
    
    int resultStatus = 0;
    
    //--- To put the data for ShareAccountDetails
    private ArrayList getSelected() {
        Boolean bln;
        ArrayList arrRow;
        HashMap selectedMap;
        ArrayList selectedList = new ArrayList();
        for (int i=0, j=_tableModel.getRowCount(); i < j; i++) {
            bln = (Boolean) _tableModel.getValueAt(i, 0);
            if (bln.booleanValue() == true) {
                arrRow = _tableModel.getRow(i);
                selectedMap = new HashMap();
                
                for (int k=0, cols=arrRow.size(); k < cols; k++) {
                    selectedMap.put(_tableModel.getColumnName(k).toUpperCase(),
                    arrRow.get(k));
                }
                selectedList.add(selectedMap);
            }
        }
        System.out.println("selectedList ... " + selectedList);
//        //--- If Resolution Accepted, Generate the  Share no.From and To.
//        if(paramActionType == ClientConstants.ACTIONTYPE_RESOLUTION_ACCEPT){
//            generateShareNo(selectedList);
//        }
        
        return selectedList;
    }
    
//    //--- Generate ShareNo.From and To.
//    public void generateShareNo(ArrayList selectedList){
//        
//        //--- retrive the data from selectedList and generate the shareNo and add it to the selectedList
//            int sizeSelectedList = selectedList.size();
//            for(int i =0;i<sizeSelectedList;i++){
//                HashMap individualData  = (HashMap)selectedList.get(i);
//                int noOfShares = CommonUtil.convertObjToInt(individualData.get("NO. OF SHARES"));
//                String shareType = CommonUtil.convertObjToStr(individualData.get("SHARE TYPE"));
//            }
//        
////        HashMap whereMap = new HashMap();
////        whereMap.put("ITEM_SUB_TYPE", "");
////        List transOutData = (List) ClientUtil.executeQuery("getInventoryDetails.TransOut", whereMap);
////        if(transOutData.size()>0){
////            
////        } else {
////            //--- retrive the data from selectedList and generate the shareNo and add it to the selectedList
////            int sizeSelectedList = selectedList.size();
////            for(int i =0;i<sizeSelectedList;i++){
////                HashMap individualData  = (HashMap)selectedList.get(i);
////                int noOfShares = CommonUtil.convertObjToInt(individualData.get("NO. OF SHARES"));
////                String shareType = CommonUtil.convertObjToStr(individualData.get("SHARE TYPE"));
////            }
////        }
//        
//    }
    
    /** Retrives data and populates the CTable using TableModel
     *
     * @param mapID     HashMap used to retrive data from DB
     * @param tblData   CTable object used to update the table with TableModel
     * @return          Returns ArrayList for populating Search Combobox
     */
    public ArrayList populateData(CTable tblData) {
        try {
          //  log.info("populateData...");
        HashMap mapID = new HashMap();
        mapID.put(CommonConstants.MAP_NAME,"getSelectResolutionTOList");
        HashMap whereMap = null;
        
        _tblData = tblData;
        
        if (mapID.containsKey(CommonConstants.MAP_WHERE))
            whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
        else
            whereMap = new HashMap();
        
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put(CommonConstants.BRANCH_ID, branchID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put(CommonConstants.USER_ID, userID);
        }
        if (!whereMap.containsKey(CommonConstants.SENDTORESOLUTIONSTATUS) && mapID.containsKey(CommonConstants.SENDTORESOLUTIONSTATUS)) {
            whereMap.put(CommonConstants.SENDTORESOLUTIONSTATUS, mapID.get(CommonConstants.SENDTORESOLUTIONSTATUS));
        }
        
        
        System.out.println("Screen   : " + getClass());
        System.out.println("Map Name : " + mapID.get(CommonConstants.MAP_NAME));
        System.out.println("Map      : " + mapID + ":" + whereMap);
        
        List list = ClientUtil.executeQuery((String) mapID.get(CommonConstants.MAP_NAME),
        whereMap);
        
        _isAvailable = list.size() > 0 ? true : false;
        
        ArrayList data = new ArrayList();
        ArrayList colData = new ArrayList();
        
        HashMap map;
        Iterator iterator = null;
        
        if (_isAvailable) {
            map = (HashMap) list.get(0);
            iterator = map.keySet().iterator();
        } else {
            if (_heading != null) {
                setTblModel(tblData, null, _heading);
            }
            return null;
        }
        
        if (_heading == null) {
            _heading = new ArrayList();
            _heading.add("Select");
           // _heading.add("status");
            while (iterator.hasNext()) {
                _heading.add((String) iterator.next());
            }
        }
        
        Object cellData="", keyData="";
        for (int i=0, j=list.size(); i < j; i++) {
            map = (HashMap) list.get(i);
            colData = new ArrayList();
            iterator = map.values().iterator();
            colData.add(new Boolean(false));
            while (iterator.hasNext()) {
                cellData = iterator.next();
                
                if (cellData != null)
                    colData.add(cellData);
                else
                    colData.add("");
            }
            data.add(colData);
        }
        
        setTblModel(tblData, data, _heading);
        
        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else {
            _tableModel = (TableModel) tblData.getModel();
        }
        
        
        } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
           // log.info("Exception in populateData..." + e.toString());
        }
        
        return _heading;
    }
    
    private void setTblModel(CTable tbl, ArrayList tblData, ArrayList head) {
        TableSorter tableSorter = new TableSorter();
      //  tableSorter.addMouseListenerToHeaderInTable(tbl);
        TableModel tableModel = new TableModel(tblData, head) {
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if (mColIndex == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        
        tableModel.fireTableDataChanged();
        tableSorter.setModel(tableModel);
        tableSorter.fireTableDataChanged();
        
        tbl.setModel(tableSorter);
        tbl.revalidate();
    }
    
    /** Table Object Setter method
     *
     * @param tbl   CTable Object
     */
    public void setTable(CTable tbl) {
        _tblData = tbl;
    }
    
    
    /** Is Data Available or not checking Method
     *
     * @return  Returns boolean
     */
    public boolean isAvailable() {
        return _isAvailable;
    }
    
    //    /** fillData populates the UI based on the table row selected
    //     *
    //     * @param rowIndexSelected  Selected Table Row index
    //     * @return                  Returns HashMap with Table Column &
    //                                Row values for the selected row.
    //     */
    //    public HashMap fillData(int rowIndexSelected) {
    //        _tableModel = (TableModel) _tblData.getModel();
    //        ArrayList rowdata = _tableModel.getRow(rowIndexSelected);
    //
    //        HashMap hashdata = new HashMap();
    //        String strColName = null;
    //        Object obj = null;
    //
    //        for (int i = 0, j = _tableModel.getColumnCount(); i < j; i++) {
    //            obj = rowdata.get(i);
    //
    //            strColName = _tableModel.getColumnName(i).toUpperCase().trim();
    ////            hashdata.put(strColName, CommonUtil.convertObjToStr(obj));
    //            if (obj != null) {
    //                hashdata.put(strColName, obj);
    //            } else {
    //                hashdata.put(strColName, "");
    //            }
    //        }
    //
    //        // Adding Authorization Date
    //        hashdata.put("AUTHORIZEDT", curDate);
    //
    //        return hashdata;
    //    }
    
    public void setSelectAll(Boolean selected) {
        for (int i=0, j=_tableModel.getRowCount(); i < j; i++) {
            _tableModel.setValueAt(selected, i, 0);
        }
    }
    
    
    /** Getter method for TableModel
     * @return Returns TableModel
     */
    public TableModel getTableModel() {
        return _tableModel;
    }
    
    //    /** Search used to update the table model based on the search criteria given by the
    //     * user.
    //     *
    //     * @param searchTxt     Search Text which is entered by the user
    //     * @param selCol        Colunm selected from the combobox
    //     * @param selColCri     Condition selected from the condition combobox
    //     * @param chkCase       Match case checking
    //     */
    //    public void searchData(String searchTxt, int selCol, int selColCri, boolean chkCase) {
    //        if (searchTxt.length() > 0) {
    //            ArrayList arrFilterRow = new ArrayList();
    //            ArrayList arrOriRow;
    //            String strArrData;
    //
    //            for (int i=0, j=_tblData.getRowCount(); i < j; i++) {
    //                arrOriRow = _tableModel.getRow(i);
    //                strArrData = arrOriRow.get(selCol).toString();
    //                if (strArrData != null) {
    //                    strArrData = strArrData.trim();
    //                    if (!chkCase) strArrData = strArrData.toUpperCase();
    //
    //                    if ((selColCri==2 && strArrData.equals(searchTxt)) ||
    //                        (selColCri==0 && strArrData.startsWith(searchTxt)) ||
    //                        (selColCri==1 && strArrData.endsWith(searchTxt))) {
    //                        arrFilterRow.add(arrOriRow);
    //                    } else if (selColCri==3) {
    //                        if (Pattern.matches(searchTxt + "\\w*", strArrData))
    //                            arrFilterRow.add(arrOriRow);
    //                    }
    //                }
    //            }
    //
    //            TableSorter tableSorter = new TableSorter();
    //            tableSorter.addMouseListenerToHeaderInTable(_tblData);
    //
    //            TableModel tmlNew = new TableModel(arrFilterRow, _heading) {
    //                public boolean isCellEditable(int rowIndex, int mColIndex) {
    //                    if (mColIndex == 0) {
    //                        return true;
    //                    } else {
    //                        return false;
    //                    }
    //                }
    //            };
    //
    //            tmlNew.fireTableDataChanged();
    //            tableSorter.setModel(tmlNew);
    //            tableSorter.fireTableDataChanged();
    //
    //            _tblData.setModel(tableSorter);
    //            _tblData.revalidate();
    //        }
    //    }
    
    //__ To get the result Status...
    public int getResultStatus() {
        return resultStatus;
    }
    //__ To set the Type of Task to be performed...
    public void setResultStatus(int rst) {
        resultStatus = rst;
    }
    
    //--- resets the UI form
    public void resetForm(){
        //        resetAccInfo();
        //        resetLables();
        //        resetTransferIn();
        //        resetDepSubNo();
        //        resetDepSubNoTbl();
        //        resetJntAccntHoldTbl();
        //        nomineeTOList = null;
    }
    
    /**
     * Getter for property chkSelectAll.
     * @return Value of property chkSelectAll.
     */
    public boolean getChkSelectAll() {
        return chkSelectAll;
    }
    
    /**
     * Setter for property chkSelectAll.
     * @param chkSelectAll New value of property chkSelectAll.
     */
    public void setChkSelectAll(boolean chkSelectAll) {
        this.chkSelectAll = chkSelectAll;
    }
    
    /**
     * Getter for property txtResolutionNo.
     * @return Value of property txtResolutionNo.
     */
    public java.lang.String getTxtResolutionNo() {
        return txtResolutionNo;
    }
    
    /**
     * Setter for property txtResolutionNo.
     * @param txtResolutionNo New value of property txtResolutionNo.
     */
    public void setTxtResolutionNo(java.lang.String txtResolutionNo) {
        this.txtResolutionNo = txtResolutionNo;
    }
    
    /**
     * Getter for property tdtResolutionDt.
     * @return Value of property tdtResolutionDt.
     */
    public java.lang.String getTdtResolutionDt() {
        return tdtResolutionDt;
    }
    
    
    public void setChkApplicationFee(boolean chkApplicationFee){
        this.chkApplicationFee=chkApplicationFee;
    }
    public boolean getChkApplicationFee()
    {
        return chkApplicationFee;
    }
    
     public void setChkMemberShipFee(boolean chkMemberShipFee){
        this.chkMemberShipFee=chkMemberShipFee;
    }
    public boolean getChkMemberShipFee()
    {
        return chkMemberShipFee;
    }
    
     public void setChkShareFee(boolean chkShareFee){
        this.chkShareFee=chkShareFee;
    }
    public boolean getChkShareFee()
    {
        return chkShareFee;
    }
    
     public java.util.HashMap getNewTransactionMap() {
        return newTransactionMap;
    }
    
    /**
     * Setter for property newTransactionMap.
     * @param newTransactionMap New value of property newTransactionMap.
     */
    public void setNewTransactionMap(java.util.HashMap newTransactionMap) {
        this.newTransactionMap = newTransactionMap;
    }
    
    /**
     * Setter for property tdtResolutionDt.
     * @param tdtResolutionDt New value of property tdtResolutionDt.
     */
    public void setTdtResolutionDt(java.lang.String tdtResolutionDt) {
        this.tdtResolutionDt = tdtResolutionDt;
    }
    
}
