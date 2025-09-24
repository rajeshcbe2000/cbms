/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * AuthorizeOB.java
 *
 * Created on March 3, 2004, 1:46 PM
 */

package com.see.truetransact.ui.termloan.loansubsidy;

//import java.util.Observable;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
//import java.util.Enumeration;
import com.see.truetransact.clientutil.EnhancedTableModel;
import java.util.regex.Pattern;

//import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
//import javax.swing.table.TableColumnModel;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
//import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.transferobject.termloan.loansubsidy.TermLoanSubsidyTO;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientexception.ClientParseException;

/** Authorize Observable is the supporting class for Authorize UI
 *
 * @author bala
 */
public class LoanSubsidyOB extends CObservable {
    private boolean _isAvailable = true;
    private TableModel _tableModel;
    private CTable _tblData;
    private ArrayList _heading;
    private ArrayList sizeList =null;
    private TableModel searchTableModel;
    private ArrayList dataArrayList;
    private String relationalOperator="";
    private ArrayList tempArrayList;
    private boolean isMultiSelect = false;
    private ComboBoxModel cbmProdId;
//    private ComboBoxModel cbmNoticeType;
    private Map guarantorMap = null;
    private ArrayList guarantorList = null;
    private String txtNoticeCharge = "";
    private String txtPostageCharge = "";
    private ProxyFactory proxy;
    private HashMap map;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String prodType="";
//    private String result;
    private Date currDt=null;
    private HashMap totListMap=new HashMap();
    private ArrayList  deleteList =new ArrayList();
    private String lblTotalTransAmt="";
    private int actionType=-1;
    private String subsidyId="";
    private HashMap authorizeMap=new HashMap();
    private boolean activateNewRecord=false;
    private String lblStatus="";
    private int result=0;
    
    /** Creates a new instance of AuthorizeOB */
    public LoanSubsidyOB() {
        try {
            proxy = ProxyFactory.createProxy();
            currDt=ClientUtil.getCurrentDate();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "TermLoanChargesJNDI");
            map.put(CommonConstants.HOME, "termloan.charges.TermLoanChargesHome");
            map.put(CommonConstants.REMOTE, "termloan.charges.TermLoanCharges");
            
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public void fillDropDown(String productType) {
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        HashMap lookUpHash = new HashMap();
        lookUpHash.put("PROD_ID", productType);
        List keyValue=null;
        setProdType(productType);
        if(productType.equals("Advances")){
            keyValue = ClientUtil.executeQuery("Cash.getAccProductAD", lookUpHash);
        }else{
            keyValue = ClientUtil.executeQuery("Cash.getAccProductTL", lookUpHash);
        }
        key.add("");
        value.add("");
        HashMap prodMap = new HashMap();
        if (keyValue!=null && keyValue.size()>0) {
            for (int i=0; i<keyValue.size(); i++) {
                prodMap = (HashMap)keyValue.get(i);
                key.add(prodMap.get("PROD_ID"));
                value.add(prodMap.get("PROD_DESC"));
            }
        }
        cbmProdId = new ComboBoxModel(key,value);
        key = null;
        value = null;
        lookUpHash.clear();
        lookUpHash = null;
        keyValue.clear();
        keyValue = null;
        prodMap.clear();
        prodMap = null;
    }
    
    
       public void setCbmProdId(String prodType) {
           ArrayList key = new ArrayList();
           ArrayList value = new ArrayList();
           key.add("");
           value.add("");
           if (CommonUtil.convertObjToStr(prodType).length()>1) {
               
               //            if (prodType.equals("GL")) {
               //                key = new ArrayList();
               //                value = new ArrayList();
               //            }
               //            else {
               try {
                   HashMap lookUpHash = new HashMap();
                   if(prodType.equals("Advances")){
                       lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + "AD");
                   }
                   else{
                       lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + "TL");
                   }
                   lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                   HashMap keyValue =ClientUtil.populateLookupData(lookUpHash);
                   
                   keyValue=(HashMap)keyValue.get("DATA");
                   key = (ArrayList)keyValue.get(CommonConstants.KEY);
                   value = (ArrayList)keyValue.get(CommonConstants.VALUE);
                   //                    getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
               } catch (Exception ex) {
                   ex.printStackTrace();
               }
               //            }
           }
           //else {
           //            key = new ArrayList();
           //            value = new ArrayList();
           //            key.add("");
           //            value.add("");
           //        }
           cbmProdId = new ComboBoxModel(key,value);
           this.cbmProdId = cbmProdId;
           setChanged();
        
    }
    /** updateStatus method used to update the database field based on the UI button
     * pressed
     *
     * @param map       HashMap from UI which is passed as a argument to Authorize UI constructor
     * @param status    Passed by UI. (Authorize, Reject, Exception - statuses)
      */   
    public void insertCharges(HashMap whereMap, boolean onlyChargeDetails) {
        HashMap obj = new HashMap();
        obj.put("NOTICE_CHARGES", whereMap);
        obj.put("ONLY_CHARGE_DETAILS", new Boolean(onlyChargeDetails));
        if(prodType.equals("MDS")){
            obj.put("PROD_TYPE",prodType);
        }
        System.out.println("map in LoanNoticeOB : " + obj);
        try {
            HashMap where = proxy.execute(obj, map) ;
//            setResult(ClientConstants.RESULT_STATUS[2]);
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e,true);
//            setResul t(ClientConstants.RESULT_STATUS[4]);
        }
    }

    public void doAction(String actionType){
        try
        {
            HashMap finalMap =new HashMap();
            finalMap.put("PRODID_LIST",getProdIdList());
            finalMap.put(CommonConstants.BRANCH_ID,ProxyParameters.BRANCH_ID);
            finalMap.put("TOTAL_SUBSIDY_TRANS_AMT",CommonUtil.convertObjToDouble(lblTotalTransAmt));
            
            finalMap.put("SOURCE_SCREEN","LOAN_SUBSIDY");
            finalMap.put("INSERT_SUBSIDY",getTotListMap());
            finalMap.put("COMMAND",getCommand());
            if(getActionType()==ClientConstants.ACTIONTYPE_REJECT || getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE){
                finalMap.put(CommonConstants.AUTHORIZEMAP,getAuthorizeMap());
            }
            System.out.println("finalMap#####"+finalMap);
            HashMap resultMap =proxy.execute(finalMap, map);
            
            setResult(getActionType());
          
        }catch (Exception e){
            e.printStackTrace();
            ClientUtil.showMessageWindow(e.getMessage());
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            setResultStatus();
        }
    }
    
    public ArrayList getProdIdList(){
        ArrayList prodList =new ArrayList();
        if(CommonUtil.convertObjToStr(cbmProdId.getKeyForSelected()).equals("")){
            prodList.add(cbmProdId.getKeys());
        }else{
             prodList.add(cbmProdId.getKeyForSelected());
        }
        return prodList;
    }
    
    private String getCommand(){
        if(getActionType()==ClientConstants.ACTIONTYPE_NEW){
            return CommonConstants.TOSTATUS_INSERT;
        }else if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
             return CommonConstants.TOSTATUS_UPDATE;
        }else if(getActionType()==ClientConstants.ACTIONTYPE_DELETE){
             return CommonConstants.TOSTATUS_DELETE;
        }else if(getActionType()==ClientConstants.ACTIONTYPE_REJECT){
             return CommonConstants.STATUS_REJECTED;
        }else if(getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE){
             return CommonConstants.STATUS_AUTHORIZED;
        }
        return "";
    }

    public void getTotalList(ArrayList totalList){
        TermLoanSubsidyTO obj=null;
        ArrayList singleList=null;
        if(totalList !=null && totalList.size()>0){
            for(int i=0;i<totalList.size();i++){
                 singleList=(ArrayList)totalList.get(i);
                 obj =new TermLoanSubsidyTO();
                if(singleList !=null && singleList.size()>0){
                    obj.setAcctNum(CommonUtil.convertObjToStr(singleList.get(1)));
                    obj.setAdjustAchd(CommonUtil.convertObjToStr(singleList.get(3)));
                    obj.setSubsidyAmt(CommonUtil.convertObjToDouble(singleList.get(4)));
                    obj.setTransAmt(CommonUtil.convertObjToDouble(singleList.get(5)));
                     obj.setSubsidyDt(currDt);
                    if(getCommand().equals(CommonConstants.TOSTATUS_INSERT)|| CommonUtil.convertObjToStr(singleList.get(6)).length()==0){
                        obj.setStatus(CommonConstants.STATUS_CREATED);
                        obj.setCommand(CommonConstants.TOSTATUS_INSERT);
                        
                    }else if(getCommand().equals(CommonConstants.TOSTATUS_UPDATE)){
                        obj.setStatus(CommonConstants.STATUS_MODIFIED);
                        obj.setCommand(CommonConstants.TOSTATUS_UPDATE);
                        
                    }else if(getCommand().equals(CommonConstants.TOSTATUS_DELETE)){
                        obj.setStatus(CommonConstants.STATUS_DELETED);
                        obj.setCommand(CommonConstants.TOSTATUS_DELETE);
                    }
                    obj.setStatusBy(ProxyParameters.USER_ID);
                    obj.setStatusDt(currDt);
                    obj.setBranchCode(ProxyParameters.BRANCH_ID);
                    obj.setSubsidyId(subsidyId);
                    getTotListMap().put(obj.getAcctNum(),obj);
        }
        }}
        
         if(deleteList !=null && deleteList.size()>0){
            for(int i=0;i<deleteList.size();i++){
                 singleList=(ArrayList)deleteList.get(i);
                 obj =new TermLoanSubsidyTO();
                if(singleList !=null && singleList.size()>0){
                    obj.setAcctNum(CommonUtil.convertObjToStr(singleList.get(1)));
                    obj.setAdjustAchd(CommonUtil.convertObjToStr(singleList.get(3)));
                    obj.setSubsidyAmt(CommonUtil.convertObjToDouble(singleList.get(4)));
                    obj.setTransAmt(CommonUtil.convertObjToDouble(singleList.get(5)));
                    obj.setSubsidyDt(currDt);
//                    if(getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
//                        obj.setStatus(CommonConstants.STATUS_CREATED);
//                        obj.setCommand(CommonConstants.TOSTATUS_INSERT);
//                    }else if(getCommand().equals(CommonConstants.TOSTATUS_UPDATE)){
//                        obj.setStatus(CommonConstants.STATUS_MODIFIED);
//                        obj.setCommand(CommonConstants.TOSTATUS_UPDATE);
//                    }else if(getCommand().equals(CommonConstants.TOSTATUS_DELETE)){
                        obj.setStatus(CommonConstants.STATUS_DELETED);
                        obj.setCommand(CommonConstants.TOSTATUS_DELETE);
//                    }
                    obj.setStatusBy(ProxyParameters.USER_ID);
                    obj.setStatusDt(currDt);
                    obj.setBranchCode(ProxyParameters.BRANCH_ID);
                    obj.setSubsidyId(subsidyId);
                    getTotListMap().put(obj.getAcctNum(),obj);
        }
        }}
        
    }
    public String getSelected(CTable tblData) {
        Boolean bln;
//        ArrayList arrRow;
//        HashMap selectedMap;
//        ArrayList selectedList = new ArrayList();
        String selected="";
        for (int i=0, j=tblData.getRowCount(); i < j; i++) {
            bln = (Boolean) tblData.getValueAt(i, 0);
            if (bln.booleanValue() == true) {
                if(prodType.equals("MDS")){
                    selected+="'"+tblData.getValueAt(i, 2);
                }else{
                    selected+="'"+tblData.getValueAt(i, 1);
                }
                selected+="',";
//                selectedList.add(_tableModel.getValueAt(i, 1));
            }
        }
        selected = selected.length()>0 ? selected.substring(0,selected.length()-1) : ""; 
        System.out.println("#$#$ selected : "+selected);
        return selected;
    }
    
    public void setSelectAll(CTable table, Boolean selected) {
        for (int i=0, j=table.getRowCount(); i < j; i++) {
            table.setValueAt(selected, i, 0);
        }
    }
    
    
    /** Retrives data and populates the CTable using TableModel
     *
     * @param mapID     HashMap used to retrive data from DB
     * @param tblData   CTable object used to update the table with TableModel
     * @return          Returns ArrayList for populating Search Combobox
     */    
    public ArrayList populateData(HashMap mapID, CTable tblData) {
        HashMap whereMap = null;
        //Added By Suresh
        _heading = null;    
        _tblData = tblData;
        HashMap tempMap = new HashMap();
        
        if (mapID.containsKey(CommonConstants.MAP_WHERE))
            whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
        else 
            whereMap = new HashMap();

        whereMap.put (CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put (CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put (CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }
        if (!whereMap.containsKey(CommonConstants.AUTHORIZESTATUS) && mapID.containsKey(CommonConstants.AUTHORIZESTATUS)) {
            whereMap.put (CommonConstants.AUTHORIZESTATUS, mapID.get(CommonConstants.AUTHORIZESTATUS));
        }

        System.out.println ("Screen   : LoanNoticeOB");
        System.out.println ("Map Name : " + mapID.get(CommonConstants.MAP_NAME));
        System.out.println ("Map      : " + mapID + ":" + whereMap);
        
        javax.swing.table.DefaultTableModel tblModel = null;
        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else if (tblData.getModel() instanceof TableModel) {
            _tableModel = (TableModel) tblData.getModel();
        } else {
            tblModel = (javax.swing.table.DefaultTableModel) tblData.getModel();
        }
        
        while (_tableModel!=null && tblData.getRowCount() > 0) {
            _tableModel.removeRow(0);
        }
         while (tblModel!=null && tblData.getRowCount() > 0) {
            tblModel.removeRow(0);
        }
        guarantorMap = null;
        
        tempMap.putAll(whereMap);
        
        List list = ClientUtil.executeQuery((String) mapID.get(CommonConstants.MAP_NAME), 
                whereMap);
        
        isMultiSelect = mapID.containsKey("MULTISELECT") ? true : false;
        isMultiSelect = true;
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
                setTblModel (tblData, null, _heading);
            }            
            return null;
        }
        
        if (_heading == null) {
            _heading = new ArrayList();
            if (isMultiSelect)
                _heading.add ("Select");
            sizeList = new ArrayList();
            if (isMultiSelect)
                sizeList.add (new Integer(6));
            String head = "";
            while (iterator.hasNext()) {
                head = (String) iterator.next();
                sizeList.add (new Integer(head.length()));
                _heading.add(head);
            }
        }
//        System.out.println("@@@$$$ sizeList : "+sizeList);
        
        seperateList(list);

        String cellData="", keyData="";
        Object obj = null;
        int cellLen = 0;
        for (int i=0, j=list.size(), k=1; i < j; i++, k=1) {
            map = (HashMap) list.get(i);
            colData = new ArrayList();
            iterator = map.values().iterator();
            if (isMultiSelect)
                colData.add(new Boolean(true));
            
            while (iterator.hasNext()) {
                obj = iterator.next();
                cellData = CommonUtil.convertObjToStr(obj);
                cellLen = cellData.length();
                if (cellLen > ((Integer) sizeList.get(k)).intValue()) {
                    sizeList.remove(k);
                    sizeList.add(k, new Integer(cellLen));
                }
                colData.add(cellData);
                k++;
            }
            data.add(colData);
        }
         

//        System.out.println("@@@$$$ sizeList : "+sizeList);
        
        setTblModel(tblData, data, _heading);
        
        TableColumn col = null;
        for (int i=0, j=sizeList.size(), k=0; i < j; i++) {
            k = ((Integer)sizeList.get(i)).intValue();
            if (k > 50) {
                k = 400;
            } else {
                k *= 8;
            }
            col = tblData.getColumn(_heading.get(i));
            col.setPreferredWidth(k);
            col.setMinWidth(k);
//            System.out.println("@@@$$$ col width : "+k);
            //col.setMaxWidth(k);
        }
        //Added By Suresh
        if(prodType.equals("MDS")){
            _tblData.getColumnModel().getColumn(3).setPreferredWidth(65);
            _tblData.getColumnModel().getColumn(6).setPreferredWidth(65);
        }
        tblData.revalidate();
        
        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else {
            _tableModel = (TableModel) tblData.getModel();
        }
        
//        System.out.println("@@@$$$ tempMap : "+tempMap);
//        populateCharges(tempMap);
        tempMap.clear();
        tempMap = null;
        
        return _heading;
    }
    
     /** Retrives data and populates the CTable using TableModel
     *
     * @param mapID     HashMap used to retrive data from DB
     * @param tblData   CTable object used to update the table with TableModel
     * @return          Returns ArrayList for populating Search Combobox
     */    
    public List populateDataNew(HashMap mapID, CTable tblData) {
        HashMap whereMap = null;
        //Added By Suresh
        _heading = null;    
        _tblData = tblData;
        HashMap tempMap = new HashMap();
        
        if (mapID.containsKey(CommonConstants.MAP_WHERE))
            whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
        else 
            whereMap = new HashMap();

        whereMap.put (CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put (CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put (CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }
        if (!whereMap.containsKey(CommonConstants.AUTHORIZESTATUS) && mapID.containsKey(CommonConstants.AUTHORIZESTATUS)) {
            whereMap.put (CommonConstants.AUTHORIZESTATUS, mapID.get(CommonConstants.AUTHORIZESTATUS));
        }

        System.out.println ("Screen   : LoanNoticeOB");
        System.out.println ("Map Name : " + mapID.get(CommonConstants.MAP_NAME));
        System.out.println ("Map      : " + mapID + ":" + whereMap);
        
        javax.swing.table.DefaultTableModel tblModel = null;
        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else if (tblData.getModel() instanceof TableModel) {
            _tableModel = (TableModel) tblData.getModel();
        } else {
            tblModel = (javax.swing.table.DefaultTableModel) tblData.getModel();
        }
        if(!isActivateNewRecord()){
        while (_tableModel!=null && tblData.getRowCount() > 0) {
            _tableModel.removeRow(0);
        }
         while (tblModel!=null && tblData.getRowCount() > 0) {
            tblModel.removeRow(0);
        }
        }
        guarantorMap = null;
        
        tempMap.putAll(whereMap);
        
        List list = ClientUtil.executeQuery((String) mapID.get(CommonConstants.MAP_NAME), 
                whereMap);
        
        
       
        
        return list;
    }
    
    
    public double totSubsidyAmt(DefaultTableModel tblData){
        DefaultTableModel tblModel = null;
        double subsidyTransAmt=0.0;
        Boolean isSelectedRecord=new Boolean(false);
//         if (tblData.getModel() instanceof TableSorter) {
////            tblModel = ((TableSorter) tblData.getModel()).getModel();
//        } else if( tblData.getModel() instanceof  DefaultTableModel){
//            tblModel = (DefaultTableModel) tblData.getModel();
//        }
       
        for(int i=0;i<tblData.getRowCount();i++)
        {
            isSelectedRecord =(Boolean)tblData.getValueAt(i,0);
            if(isSelectedRecord.booleanValue()){
                subsidyTransAmt+=CommonUtil.convertObjToDouble(tblData.getValueAt(i,5)).doubleValue(); 
            }
        }
        return subsidyTransAmt;
    }
    
    public void removeRowsFromGuarantorTable(CTable tblData) {
        javax.swing.table.DefaultTableModel tblModel = null;
        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else if (tblData.getModel() instanceof TableModel) {
            _tableModel = (TableModel) tblData.getModel();
        } else {
            tblModel = (javax.swing.table.DefaultTableModel) tblData.getModel();
        }
        
        while (_tableModel!=null && tblData.getRowCount() > 0) {
            _tableModel.removeRow(0);
        }
         while (tblModel!=null && tblData.getRowCount() > 0) {
            tblModel.removeRow(0);
        }
        guarantorMap = null;
    }
    
    public void populateGuarantorTable(String actNum, CTable tblData) {
        if (_heading!=null) {
            if (guarantorMap!=null && guarantorMap.containsKey(actNum)) {
                guarantorList = (ArrayList) guarantorMap.get(actNum);
                setTblModel(tblData, guarantorList, _heading);
                TableColumn col = null;
                for (int i=0, j=sizeList.size(), k=0; i < j; i++) {
                    k = ((Integer)sizeList.get(i)).intValue();
                    if (k > 50) {
                        k = 400;
                    } else {
                        k *= 8;
                    }
                    col = tblData.getColumn(_heading.get(i));
                    col.setPreferredWidth(k);
                    col.setMinWidth(k);
                }
                //Added By Suresh
                if(prodType.equals("MDS")){
                    _tblData.getColumnModel().getColumn(3).setPreferredWidth(65);
                    _tblData.getColumnModel().getColumn(6).setPreferredWidth(65);
                }
                tblData.revalidate();            
            } else {
                setTblModel(tblData, null, _heading);
            }
        }
    }
    
    private void seperateList(List list) {
        ArrayList tempList = new ArrayList();
//        System.out.println("@@@$$$ list : "+list);
        Map map = new HashMap();
        Iterator iterator = null;
        String cellData;
        for (int i=0; i<list.size(); i++) {
            map = (HashMap) list.get(i);
//            System.out.println("@@@$$$ map : "+map);
            if (String.valueOf(map.get("CUST_TYPE")).equals("GUARANTOR")) {
                iterator = map.values().iterator();
//                if (guarantorList==null) {
                guarantorList = new ArrayList();
//                }
                guarantorList.add(new Boolean(false));
                if (guarantorMap==null) {
                    guarantorMap = new HashMap();
                }
//                System.out.println("@@@$$$ guarantorMap : "+guarantorMap);
                while (iterator.hasNext()) {
                    cellData = CommonUtil.convertObjToStr(iterator.next());
                    guarantorList.add(cellData);
                }
//                System.out.println("@@@$$$ guarantorList : "+guarantorList);
                //Changed By Suresh
                String actNum="";
                if(prodType.equals("MDS")){
                    actNum = String.valueOf(map.get("CHITTAL_NO"));
                }else{
                    actNum = String.valueOf(map.get("ACT_NUM"));
                }
                if (guarantorMap.containsKey(actNum)) {
                    tempList = (ArrayList) guarantorMap.get(actNum);
                } else {
                    tempList = new ArrayList();
                }
                tempList.add(guarantorList);
                guarantorMap.put(actNum, tempList);
                list.remove(i--);
//                if (i<8) {
//                    System.out.println("@@@$$$ guarantorMap : "+guarantorMap);
//                }
            }
        }
//        System.out.println("@@@$$$ final list : "+list);
//        System.out.println("@@@$$$ final guarantorMap : "+guarantorMap);
    }
    
        
    private void setTblModel(CTable tbl, ArrayList tblData, ArrayList head) {
        TableSorter tableSorter = new TableSorter();
        tableSorter.addMouseListenerToHeaderInTable(tbl);
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
    
    /** fillData populates the UI based on the table row selected
     *
     * @param rowIndexSelected  Selected Table Row index
     * @return                  Returns HashMap with Table Column & 
                                Row values for the selected row.
     */    
    public HashMap fillData(int rowIndexSelected) {
        _tableModel = (TableModel) _tblData.getModel();
        ArrayList rowdata = _tableModel.getRow(rowIndexSelected);

        HashMap hashdata = new HashMap();
        String strColName = null;
        Object obj = null;

        for (int i = 0, j = _tableModel.getColumnCount(); i < j; i++) {
            obj = rowdata.get(i);

            strColName = _tableModel.getColumnName(i).toUpperCase().trim();
//            hashdata.put(strColName, CommonUtil.convertObjToStr(obj));

            if (obj != null) {
                hashdata.put(strColName, obj);
            } else {
                hashdata.put(strColName, "");
            }
        }
        
        hashdata.put (CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
        // Adding Authorization Date
        hashdata.put(CommonConstants.AUTHORIZEDT, ClientUtil.getCurrentDate());
        
        return hashdata;
    }
    
    /** Getter method for TableModel
     * @return Returns TableModel
     */    
    public TableModel getTableModel() {
        return _tableModel;
    }
    
    /** Search used to update the table model based on the search criteria given by the
     * user.
     *
     * @param searchTxt     Search Text which is entered by the user
     * @param selCol        Colunm selected from the combobox
     * @param selColCri     Condition selected from the condition combobox
     * @param chkCase       Match case checking
     */    
    public void searchData(String searchTxt, int selCol, int selColCri, boolean chkCase) {
        if (searchTxt.length() > 0) {
            ArrayList arrFilterRow = new ArrayList();
            ArrayList arrOriRow;
            String strArrData;
            for (int i=0, j=_tblData.getRowCount(); i < j; i++) {
                arrOriRow = _tableModel.getRow(i);
                strArrData = arrOriRow.get(selCol).toString();
                if (strArrData != null) {
                    strArrData = strArrData.trim();
                    if (!chkCase) strArrData = strArrData.toUpperCase();
                    
                    if ((selColCri==2 && strArrData.equals(searchTxt)) || 
                        (selColCri==0 && strArrData.startsWith(searchTxt)) ||
                        (selColCri==1 && strArrData.endsWith(searchTxt))) {
                        arrFilterRow.add(arrOriRow);
                    } else if (selColCri==3) {
                        if (Pattern.matches(searchTxt + "\\w*", strArrData)) 
                            arrFilterRow.add(arrOriRow);
                    }
                }
            }

            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(_tblData);
            
            TableModel tmlNew = new TableModel(arrFilterRow, _heading) {
                public boolean isCellEditable(int rowIndex, int mColIndex) {
                    if (mColIndex == 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            
            tmlNew.fireTableDataChanged();
            tableSorter.setModel(tmlNew);
            tableSorter.fireTableDataChanged();
            
            _tblData.setModel(tableSorter);
            _tblData.revalidate();
        }        
    }
    
    public void setDataArrayList() {
        dataArrayList = searchTableModel.getDataArrayList();
    }
    
    public void searchData(String searchTxt, int selCol, int selColCri, boolean chkCase, String operator) {
        if (searchTxt.length() > 0) {
            ArrayList arrFilterRow = new ArrayList();
            ArrayList arrOriRow;
            String strArrData;
            for (int i=0, j=dataArrayList.size(); i < j; i++) {
                arrOriRow = (ArrayList)dataArrayList.get(i);
                strArrData = arrOriRow.get(selCol).toString();
                if (strArrData != null) {
                    strArrData = strArrData.trim();
                    if (!chkCase) strArrData = strArrData.toUpperCase();
                    
                    if ((selColCri==2 && strArrData.equals(searchTxt)) || 
                        (selColCri==0 && strArrData.startsWith(searchTxt)) ||
                        (selColCri==1 && strArrData.endsWith(searchTxt))) {
                        arrFilterRow.add(arrOriRow);
                    } else if (selColCri==3) {
                        if (Pattern.matches(searchTxt + "\\w*", strArrData)) 
                            arrFilterRow.add(arrOriRow);
                    }
                }
            }

            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(_tblData);
            
            if (relationalOperator.equals("Or"))
                arrFilterRow.addAll(tempArrayList);
            TableModel tmlNew = new TableModel(arrFilterRow, _heading) {
                public boolean isCellEditable(int rowIndex, int mColIndex) {
                    if (mColIndex == 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            
            tmlNew.fireTableDataChanged();
            tableSorter.setModel(tmlNew);
            tableSorter.fireTableDataChanged();
            
            _tblData.setModel(tableSorter);
            _tblData.revalidate();
//            if (relationalOperator.equals("Or"))
            if (operator.equals("And"))
                dataArrayList = arrFilterRow;
            if (operator.equals("Or")) {
                if (tempArrayList==null) tempArrayList = new ArrayList();
                tempArrayList = arrFilterRow;
            }
            relationalOperator = operator;
        }        
    }
    
    /**
     * Getter for property searchTableModel.
     * @return Value of property searchTableModel.
     */
    public ArrayList getTableModel(CTable table) {
        DefaultTableModel tblModel=null;
        ArrayList list=new ArrayList();
        if(deleteList ==null){
            deleteList=new ArrayList();
        }
        ArrayList totList=new ArrayList();
        tblModel = (DefaultTableModel) table.getModel();
        Vector v= (Vector)tblModel.getDataVector();
        for (int i=0; i<v.size();i++){
            List lst=(List)v.get(i);
            if(new Boolean(CommonUtil.convertObjToStr(lst.get(0))).booleanValue()){
                list=new ArrayList();
                list.add(lst.get(0));
                list.add(lst.get(1));
                list.add(lst.get(2));
                list.add(lst.get(3));
                list.add(lst.get(4));
                list.add(lst.get(5));
                list.add(lst.get(6));
                totList.add(list);
            }else if(getActionType()==ClientConstants.ACTIONTYPE_EDIT && CommonUtil.convertObjToStr(lst.get(6)).length()>0){
                list=new ArrayList();
                list.add(lst.get(0));
                list.add(lst.get(1));
                list.add(lst.get(2));
                list.add(lst.get(3));
                list.add(lst.get(4));
                list.add(lst.get(5));
                list.add(lst.get(6));
                deleteList.add(list);
                
            }
        }
        
        System.out.println("totList"+totList);
        
        return totList;
    }
    
    /**
     * Getter for property searchTableModel.
     * @return Value of property searchTableModel.
     */
    public com.see.truetransact.clientutil.TableModel getSearchTableModel() {
        return searchTableModel;
    }
    
    /**
     * Setter for property searchTableModel.
     * @param searchTableModel New value of property searchTableModel.
     */
    public void setSearchTableModel(com.see.truetransact.clientutil.TableModel searchTableModel) {
        this.searchTableModel = searchTableModel;
        setDataArrayList();
    }
    
    /**
     * Getter for property cbmProdId.
     * @return Value of property cbmProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }
    
    /**
     * Setter for property cbmProdId.
     * @param cbmProdId New value of property cbmProdId.
     */
    public void setCbmProdId(com.see.truetransact.clientutil.ComboBoxModel cbmProdId) {
        this.cbmProdId = cbmProdId;
    }
    
    /**
     * Getter for property guarantorMap.
     * @return Value of property guarantorMap.
     */
    public java.util.Map getGuarantorMap() {
        return guarantorMap;
    }
    
    /**
     * Setter for property guarantorMap.
     * @param guarantorMap New value of property guarantorMap.
     */
    public void setGuarantorMap(java.util.Map guarantorMap) {
        this.guarantorMap = guarantorMap;
    }
    
    /**
     * Getter for property txtNoticeCharge.
     * @return Value of property txtNoticeCharge.
     */
    public java.lang.String getTxtNoticeCharge() {
        return txtNoticeCharge;
    }
    
    /**
     * Setter for property txtNoticeCharge.
     * @param txtNoticeCharge New value of property txtNoticeCharge.
     */
    public void setTxtNoticeCharge(java.lang.String txtNoticeCharge) {
        this.txtNoticeCharge = txtNoticeCharge;
    }
    
    /**
     * Getter for property txtPostageCharge.
     * @return Value of property txtPostageCharge.
     */
    public java.lang.String getTxtPostageCharge() {
        return txtPostageCharge;
    }
    
    /**
     * Setter for property txtPostageCharge.
     * @param txtPostageCharge New value of property txtPostageCharge.
     */
    public void setTxtPostageCharge(java.lang.String txtPostageCharge) {
        this.txtPostageCharge = txtPostageCharge;
    }
    
//    /**
//     * Getter for property result.
//     * @return Value of property result.
//     */
//    public java.lang.String getResult() {
//        return result;
//    }
//    
//    /**
//     * Setter for property result.
//     * @param result New value of property result.
//     */
//    public void setResult(java.lang.String result) {
//        this.result = result;
//    }
    
    /**
     * Getter for property prodType.
     * @return Value of property prodType.
     */
    public java.lang.String getProdType() {
        return prodType;
    }
    
    /**
     * Setter for property prodType.
     * @param prodType New value of property prodType.
     */
    public void setProdType(java.lang.String prodType) {
        this.prodType = prodType;
    }
    
    /**
     * Getter for property totListMap.
     * @return Value of property totListMap.
     */
    public java.util.HashMap getTotListMap() {
        return totListMap;
    }
    
    /**
     * Setter for property totListMap.
     * @param totListMap New value of property totListMap.
     */
    public void setTotListMap(java.util.HashMap totListMap) {
        this.totListMap = totListMap;
    }
    
    /**
     * Getter for property lblTotalTransAmt.
     * @return Value of property lblTotalTransAmt.
     */
    public java.lang.String getLblTotalTransAmt() {
        return lblTotalTransAmt;
    }
    
    /**
     * Setter for property lblTotalTransAmt.
     * @param lblTotalTransAmt New value of property lblTotalTransAmt.
     */
    public void setLblTotalTransAmt(java.lang.String lblTotalTransAmt) {
        this.lblTotalTransAmt = lblTotalTransAmt;
    }
    
    /**
     * Getter for property actionType.
     * @return Value of property actionType.
     */
    public int getActionType() {
        return actionType;
    }
    
    /**
     * Setter for property actionType.
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
    
    /**
     * Getter for property subsidyId.
     * @return Value of property subsidyId.
     */
    public java.lang.String getSubsidyId() {
        return subsidyId;
    }
    
    /**
     * Setter for property subsidyId.
     * @param subsidyId New value of property subsidyId.
     */
    public void setSubsidyId(java.lang.String subsidyId) {
        this.subsidyId = subsidyId;
    }
    
    /**
     * Getter for property authorizeMap.
     * @return Value of property authorizeMap.
     */
    public java.util.HashMap getAuthorizeMap() {
        return authorizeMap;
    }
    
    /**
     * Setter for property authorizeMap.
     * @param authorizeMap New value of property authorizeMap.
     */
    public void setAuthorizeMap(java.util.HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }
    
    /**
     * Getter for property activateNewRecord.
     * @return Value of property activateNewRecord.
     */
    public boolean isActivateNewRecord() {
        return activateNewRecord;
    }    
   
    /**
     * Setter for property activateNewRecord.
     * @param activateNewRecord New value of property activateNewRecord.
     */
    public void setActivateNewRecord(boolean activateNewRecord) {
        this.activateNewRecord = activateNewRecord;
    }    
    
    
      // To set and change the Status of the lable STATUS
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    public int getResult(){
        return this.result;
    }
    
    // To set the Value of the lblStatus...
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    public String getLblStatus(){
        return lblStatus;
    }
    
    //To reset the Value of lblStatus after each save action...
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        notifyObservers();
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        notifyObservers();
    }
    
}