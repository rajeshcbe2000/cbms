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

package com.see.truetransact.ui.termloan.loanstatus;

//import java.util.Observable;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import java.util.regex.Pattern;
import javax.swing.table.TableColumn;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
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
public class LoanStatusOB extends CObservable {
    private TableModel _tableModel;
    private CTable _tblData;
    private ArrayList _heading;
    private ArrayList sizeList =null;
    private TableModel searchTableModel;
    private ArrayList dataArrayList;
    private ArrayList tempArrayList;
    private boolean isMultiSelect = false;
    private ComboBoxModel cbmProdId;
    private ProxyFactory proxy;
    private HashMap map;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String prodType="";
    private Date currDt=null;
    private HashMap totListMap=new HashMap();
    private ArrayList  deleteList =new ArrayList();
    private String lblTotalTransAmt="";
    private int actionType=-1;
    private String lblStatus="";
    private int result=0;
    
    /** Creates a new instance of AuthorizeOB */
    public LoanStatusOB() {
        try {
            proxy = ProxyFactory.createProxy();
            currDt=ClientUtil.getCurrentDate();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "");
            map.put(CommonConstants.HOME, "");
            map.put(CommonConstants.REMOTE, "");
            
            
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
           cbmProdId = new ComboBoxModel(key,value);
           this.cbmProdId = cbmProdId;
           setChanged();
        
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
    
   public String getSelected(CTable tblData) {
        Boolean bln;
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
        tempMap.putAll(whereMap);
        
        List list = ClientUtil.executeQuery((String) mapID.get(CommonConstants.MAP_NAME), 
                whereMap);
        
        isMultiSelect = mapID.containsKey("MULTISELECT") ? true : false;
        isMultiSelect = true;
        ArrayList data = new ArrayList();
        ArrayList colData = new ArrayList();

        HashMap map;
        Iterator iterator = null;
        
      
        
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
       
       tempMap.putAll(whereMap);
        
        List list = ClientUtil.executeQuery((String) mapID.get(CommonConstants.MAP_NAME), 
                whereMap);
        return list;
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
   
    
    public void setDataArrayList() {
        dataArrayList = searchTableModel.getDataArrayList();
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