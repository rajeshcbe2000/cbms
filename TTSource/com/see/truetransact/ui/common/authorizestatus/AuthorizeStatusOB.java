/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 **
 *
 * AuthorizeOB.java
 *
 * Created on March 3, 2004, 1:46 PM
 */

package com.see.truetransact.ui.common.authorizestatus;

import java.util.Observable;
import java.util.List;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uicomponent.CTable;

import com.see.truetransact.clientproxy.ProxyParameters;

/** Authorize Observable is the supporting class for Authorize UI
 *
 * @author bala
 */
public class AuthorizeStatusOB extends Observable {
    private boolean _isAvailable = true;
    private TableModel _tableModel;
    private CTable _tblData;
    private ArrayList _heading;
    private String userID = ProxyParameters.USER_ID;
    private String branchID = ProxyParameters.BRANCH_ID;
    private int hierarchyID = ProxyParameters.HIERARCHY_ID;
    
    int resultStatus = 0;
 
    
    /** Creates a new instance of AuthorizeOB */
    public AuthorizeStatusOB() {
    }
    
    /** updateStatus method used to update the database field based on the UI button
     * pressed
     *
     * @param map       HashMap from UI which is passed as a argument to Authorize UI constructor
     * @param status    Passed by UI. (Authorize, Reject, Exception - statuses)
     */    
    public void updateStatus(HashMap map, String status) {
        boolean isExecuted = true;
        ArrayList selectedList = getSelected();
        HashMap updateMap;
        String strUpdateMap = (String) map.get(CommonConstants.UPDATE_MAP_NAME);
        if(map.containsKey(CommonConstants.UPDATE_MAP_WHERE))
            updateMap = (HashMap) map.get(CommonConstants.UPDATE_MAP_WHERE);
        else
            updateMap = (HashMap) map.get(CommonConstants.MAP_WHERE);
        HashMap dataMap;
        for (int i=0, j=selectedList.size(); i < j; i++) {
            dataMap = (HashMap) selectedList.get(i);
            dataMap.put("STATUS", status);
            dataMap.put("USER_ID", userID);
            dataMap.put("BRANCH_ID", branchID);
            dataMap.put(CommonConstants.BRANCH_ID, branchID);
            dataMap.put(CommonConstants.HIERARCHY_ID, hierarchyID);
            dataMap.put(CommonConstants.AUTHORIZEDT, ClientUtil.getCurrentDate());
            System.out.println("dataMap&&**"+dataMap);
            if(strUpdateMap.equals("authorizeStandingInstruction")) {
                if (status.equals("REJECTED")) {
                    
                    List list= ClientUtil.executeQuery("getDeletedStatus",dataMap);
                    System.out.println("list@@@@@@@"+list);
                    if(list.size()>0) {
                        HashMap map1 = new HashMap();
                        map1=(HashMap)list.get(0);
                        if(map1.containsValue("DELETED"))
                            ClientUtil.execute("setDeletedStatus",dataMap);
                    }
                }
            }
           

            if (updateMap != null) {
                dataMap = appendMap(dataMap, updateMap);
            }
            
            isExecuted = ClientUtil.executeWithResult(strUpdateMap, dataMap);
        }
        
        if(!isExecuted){
            setResultStatus(ClientConstants.ACTIONTYPE_FAILED);
        }
        
        populateData(map, _tblData);
    }
    
    private HashMap appendMap(HashMap appendTo, HashMap fromMap) {
        Iterator fromIterator = fromMap.keySet().iterator();
        Object key = null;
        while (fromIterator.hasNext()) {
            key = fromIterator.next();
            appendTo.put(key, fromMap.get(key));
        }
        return appendTo;
    }

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
        System.out.println ("selectedList ... " + selectedList);
        return selectedList;
    }
    
    /** Retrives data and populates the CTable using TableModel
     *
     * @param mapID     HashMap used to retrive data from DB
     * @param tblData   CTable object used to update the table with TableModel
     * @return          Returns ArrayList for populating Search Combobox
     */    
    public ArrayList populateData(HashMap mapID, CTable tblData) {
        HashMap whereMap = null;
        
        _tblData = tblData;
        
        if (mapID.containsKey(CommonConstants.MAP_WHERE))
            whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
        else 
            whereMap = new HashMap();
        
        whereMap.put (CommonConstants.HIERARCHY_ID, hierarchyID);
        
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put (CommonConstants.BRANCH_ID, branchID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put (CommonConstants.USER_ID, userID);
        }
        if (!whereMap.containsKey(CommonConstants.AUTHORIZESTATUS) && mapID.containsKey(CommonConstants.AUTHORIZESTATUS)) {
            whereMap.put (CommonConstants.AUTHORIZESTATUS, mapID.get(CommonConstants.AUTHORIZESTATUS));
        }
        

        System.out.println ("Screen   : " + getClass());
        System.out.println ("Map Name : " + mapID.get(CommonConstants.MAP_NAME));
        System.out.println ("Map      : " + mapID + ":" + whereMap);
        
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
                setTblModel (tblData, null, _heading);
            }
            return null;
        }
        
        if (_heading == null) {
            _heading = new ArrayList();
//            _heading.add ("Select");
            while (iterator.hasNext()) {
                _heading.add((String) iterator.next());
            }
        }

        Object cellData="", keyData="";
        for (int i=0, j=list.size(); i < j; i++) {
            map = (HashMap) list.get(i);
            colData = new ArrayList();
            iterator = map.values().iterator();
//            colData.add(new Boolean(false));
            while (iterator.hasNext()) {
                cellData = iterator.next();
              
                if (cellData != null)
                    colData.add(cellData);
                else 
                    colData.add(null);
            }
            data.add(colData);
        }
        
        setTblModel(tblData, data, _heading);
        
        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else {
            _tableModel = (TableModel) tblData.getModel();
        }
        
        return _heading;
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
        
        hashdata.put (CommonConstants.HIERARCHY_ID, hierarchyID);
        // Adding Authorization Date
        hashdata.put(CommonConstants.AUTHORIZEDT, ClientUtil.getCurrentDate());

        return hashdata;
    }

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
    
    //__ To get the result Status...
    public int getResultStatus() {
        return resultStatus;
    }
    //__ To set the Type of Task to be performed...
    public void setResultStatus(int rst) {
        resultStatus = rst;
    }
    
    
}
