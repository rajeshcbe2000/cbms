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

package com.see.truetransact.ui.common.authorize;

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
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.uicomponent.CTable;

import com.see.truetransact.clientproxy.ProxyParameters;

/** Authorize Observable is the supporting class for Authorize UI
 *
 * @author bala
 */
public class AuthorizeOB extends Observable {
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
    
    /** Creates a new instance of AuthorizeOB */
    public AuthorizeOB() {
    }
    
    /** updateStatus method used to update the database field based on the UI button
     * pressed
     *
     * @param map       HashMap from UI which is passed as a argument to Authorize UI constructor
     * @param status    Passed by UI. (Authorize, Reject, Exception - statuses)
      */   
    public void updateStatus(HashMap map) {
        /*ArrayList selectedList = getSelected();
        String strUpdateMap = (String) map.get(CommonConstants.UPDATE_MAP_NAME);
        HashMap dataMap;
        for (int i=0, j=selectedList.size(); i < j; i++) {
            dataMap = (HashMap) selectedList.get(i);
            dataMap.put ("STATUS", status);
            dataMap.put ("USER_ID", userID);
            ClientUtil.execute(strUpdateMap, dataMap);
        }*/
        populateData(map, _tblData);
    }

    public ArrayList getSelected() {
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
                        CommonUtil.convertObjToStr(arrRow.get(k)));
                }
                
                selectedMap.put (CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
                // added for authorization date
                selectedMap.put (CommonConstants.AUTHORIZEDT, ClientUtil.getCurrentDate());
                if (!selectedMap.containsKey(CommonConstants.BRANCH_ID)) {
                        selectedMap.put (CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                }
                if (!selectedMap.containsKey(CommonConstants.USER_ID)) {
                    selectedMap.put (CommonConstants.USER_ID, ProxyParameters.USER_ID);
                }  
                
                //
                selectedList.add(selectedMap);
            }
        }
        return selectedList;
    }
    
    public void setSelectAll(Boolean selected) {
        for (int i=0, j=_tableModel.getRowCount(); i < j; i++) {
            _tableModel.setValueAt(selected, i, 0);
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
        _tblData = tblData;
        
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

        //System.out.println ("Screen   : AuthorizeOB");
        //System.out.println ("Map Name : " + mapID.get(CommonConstants.MAP_NAME));
        //System.out.println ("Map      : " + mapID + ":" + whereMap);
        
        List list = null;
        
        if(mapID.containsKey("viewAllShareAcctCashierAuthorizeTOList")){
            list = (List) mapID.get("viewAllShareAcctCashierAuthorizeTOList");
        }else if(mapID.containsKey("viewAllShareAcctAuthorizeTOList11")){
            list = (List) mapID.get("viewAllShareAcctAuthorizeTOList11");
        }else{
            list = ClientUtil.executeQuery((String) mapID.get(CommonConstants.MAP_NAME), 
                whereMap);
        }
        
        isMultiSelect = mapID.containsKey("MULTISELECT") ? true : false;
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
        

        String cellData="", keyData="";
        Object obj = null;
        int cellLen = 0;
        for (int i=0, j=list.size(), k=0; i < j; i++, k=0) {
            map = (HashMap) list.get(i);
            colData = new ArrayList();
            iterator = map.values().iterator();
            if (isMultiSelect)
                colData.add(new Boolean(false));
            
            while (iterator.hasNext()) {
                obj = iterator.next();
                cellData = CommonUtil.convertObjToStr(obj);
                cellLen = cellData.length();
                if (cellLen > ((Integer) sizeList.get(k)).intValue()) {
                    sizeList.remove(k);
                    sizeList.add(k, new Integer(cellLen));
                }

//                if (obj != null) {
                    colData.add(cellData);
//                } else {
//                    colData.add("");
//                }
                k++;
            }
            data.add(colData);
        }

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
            //col.setMaxWidth(k);
        }
        
        tblData.revalidate();
        
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
    
}