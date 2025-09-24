/*
 * ViewAllOB.java
 *
 * Created on December 17, 2003, 11:41 AM
 */
package com.see.truetransact.ui.generalledger;

import com.see.truetransact.ui.common.viewall.*;
import java.util.Observable;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.Date;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.CObservable;
import java.util.List;
import java.util.LinkedHashMap;
import net.sf.jasperreports.engine.design.JasperDesign;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;

/**
 * @author bala
 */
public class AccountHeadOrderingOB extends CObservable {

    private boolean _isAvailable = true;
    private TableModel _tableModel;
    private CTable _tblData;
    private ArrayList _heading;
    private HashMap dataHash;
    private ArrayList data;
    private int dataSize;
    public final int MAXDATA = 1000;
    private ComboBoxModel cbmProdType;
    private ComboBoxModel cbmProdId;
    private String prodType;
    private String prodId;
    private String txtAccNo;
    private String lblAccNameValue;
    private String lblAddressValue;
    private Date tdtFromDate;
    private Date tdtToDate;
    private ArrayList key;
    private ArrayList value;
    private HashMap linkMap = new HashMap();
    private ProxyFactory proxy;
    public boolean btnRDTransPressed = false;
    private HashMap printMap = new HashMap();
    private static Date currDt = null;
    private static AccountHeadOrderingOB genLedgerOB;
    private HashMap paramMap;
    private double assetBal;
    private double liableBal;

    static {
        try {
            currDt = ClientUtil.getCurrentDate();
            genLedgerOB = new AccountHeadOrderingOB();
        } catch (Exception e) {
        }
    }

    public static AccountHeadOrderingOB getInstance() {
        return genLedgerOB;
    }

    /**
     * Creates a new instance of ViewAllOB
     */
    public AccountHeadOrderingOB() throws Exception {
        initiate();
    }

    private void initiate() throws Exception {
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            System.err.println("Exception " + e + "Caught");
            e.printStackTrace();
        }
        fillDropDown();
    }

    private void fillDropDown() throws Exception {
        System.out.println("Inside  ob...");
        HashMap lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");

        final HashMap param = new HashMap();

        final ArrayList lookupKey = new ArrayList();
        //lookupKey.add("PRODUCTTYPE");
        lookupKey.add("GNPASSBOOK.MODULE");
        param.put(CommonConstants.MAP_NAME, null);
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);

        HashMap lookupValues = ClientUtil.populateLookupData(param);

        fillData((HashMap) lookupValues.get("GNPASSBOOK.MODULE"));

        cbmProdType = new ComboBoxModel(key, value);

    }

    public void setCbmProdId(String prodType) {
        if (CommonUtil.convertObjToStr(prodType).length() > 0) {
            if (prodType.equals("GL")) {
                key = new ArrayList();
                value = new ArrayList();
            } else {
                try {
                    //lookUpHash = new HashMap();
                    paramMap = new HashMap();
                    String key1 = "";
                    String val1 = "";
                    paramMap.put("PROD_TYPE", prodType);
                    cbmProdId = new ComboBoxModel();
                    List prdLst = ClientUtil.executeQuery("getPassBookProductId", paramMap);
                    if (prdLst != null && prdLst.size() > 0) {
                        for (int i = 0; i < prdLst.size(); i++) {
                            paramMap = (HashMap) prdLst.get(i);
                            key1 = CommonUtil.convertObjToStr(paramMap.get("PROD_ID"));
                            val1 = CommonUtil.convertObjToStr(paramMap.get("PROD_DESC"));
                            cbmProdId.addKeyAndElement(key1, val1);
                        }
                        setChanged();
                    } else {
                        cbmProdId.addKeyAndElement(key1, val1);
                        setChanged();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            cbmProdId = new ComboBoxModel();
            setChanged();
        }
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    /**
     * To get data for comboboxes
     */
    private HashMap populateDataLocal(HashMap obj, HashMap lookupMap) throws Exception {
        HashMap keyValue = proxy.executeQuery(obj, lookupMap);
        return keyValue;
    }
    ///as an when customer comes

    public HashMap loanInterestCalculationAsAndWhen(HashMap whereMap) {
        HashMap mapData = new HashMap();
        try {
            java.util.List mapDataList = ClientUtil.executeQuery("", whereMap); //, frame);
            mapData = (HashMap) mapDataList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("#### MapData :" + mapData);
        return mapData;
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

   
    
//    public ArrayList populateData(HashMap mapID, CTable tblData) {
//        _tblData = tblData;
//        //getGrandTotalAmount();
//        // Adding Where Condition
//        HashMap whereMap = null;
//        if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
//            if (mapID.get(CommonConstants.MAP_WHERE) instanceof HashMap) {
//                whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
//            } else {
//                System.out.println("Convert other data type to HashMap:" + mapID);
//            }
//        } else {
//            whereMap = new HashMap();
//        }
//
//        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
//            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
//        }
//        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
//            whereMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
//        }
//
//        mapID.put(CommonConstants.MAP_WHERE, whereMap);
//
//        System.out.println("Screen   : " + getClass());
//        System.out.println("Map Name : " + mapID.get(CommonConstants.MAP_NAME));
//        System.out.println("Map      : " + mapID);
//
//        dataHash = ClientUtil.executeTableQuery(mapID);
//        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
//        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
//        ArrayList arrList = new ArrayList();
//        if (data != null && data.size() > 0) {
//            for (int i = 0; i < data.size(); i++) {
//                arrList = (ArrayList) data.get(i);
////                if (!getProdType().equals("TD")) {
////                    arrList.add(0, new Boolean(false));
////                }
//                data.set(i, arrList);
//            }
//        }
//        System.out.println("### Data : " + data);
//            populateTable();
//       // }
//        whereMap = null;
//        return _heading;
//    }
    
    
    public ArrayList populateData(HashMap mapID, CTable tblData) {
        _tblData = tblData;
        //getGrandTotalAmount();
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
        List glList = ClientUtil.executeQuery(CommonUtil.convertObjToStr(mapID.get(CommonConstants.MAP_NAME)),whereMap);
        _heading = new ArrayList();
        _heading.add("SL NO");
        _heading.add("DESCRIPTION");
        _heading.add("HEAD ID");
        _heading.add("NEW ORDER");
        data = new ArrayList();
        if (glList != null && glList.size() > 0) {
            for (int i = 0; i < glList.size(); i++) {
                //SLNO,AC_HD_DESC,AC_HD_ID
                ArrayList arrList = new ArrayList();
                HashMap map = (HashMap)glList.get(i);
                arrList.add(CommonUtil.convertObjToStr(map.get("SLNO")));
                arrList.add(CommonUtil.convertObjToStr(map.get("AC_HD_DESC")));
                arrList.add(CommonUtil.convertObjToStr(map.get("AC_HD_ID")));
                arrList.add(CommonUtil.convertObjToStr(map.get("")));
                data.add(arrList);
            }
        }
        System.out.println("### Data : " + data);
        populateTable();
        whereMap = null;
        return _heading;
    }
   
    public void populateTable() {
        boolean dataExist;
        if (_heading != null) {
            _isAvailable = true;
            dataExist = true;
            TableModel tableModel = new TableModel();
            tableModel.setHeading(_heading);
            tableModel.setData(data);
            tableModel.fireTableDataChanged();
            _tblData.setAutoResizeMode(5);
            _tblData.doLayout();
            _tblData.setModel(tableModel);
            _tblData.revalidate();
            setTblModel(_tblData, data, _heading);  
        } else {
            _isAvailable = false;
            dataExist = false;
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(_tblData);
            TableModel tableModel = new TableModel();
            tableModel.setHeading(new ArrayList());
            tableModel.setData(new ArrayList());
            tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            _tblData.setModel(tableSorter);
            _tblData.revalidate();
            ClientUtil.noDataAlert();
        }
    }

    private void setTblModel(CTable tbl, ArrayList tblData, ArrayList head) {
        TableSorter tableSorter = new TableSorter();
        tableSorter.addMouseListenerToHeaderInTable(tbl);
        TableModel tableModel = new TableModel(tblData, head) {

            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if (mColIndex == 3) {
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

    private void setRightAlignment(int col) {
        javax.swing.table.DefaultTableCellRenderer r = new javax.swing.table.DefaultTableCellRenderer();
        r.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        _tblData.getColumnModel().getColumn(col).setCellRenderer(r);
        _tblData.getColumnModel().getColumn(col).sizeWidthToFit();
    }

    public void setTable(CTable tbl) {
        _tblData = tbl;
    }

    public boolean isAvailable() {
        return _isAvailable;
    }

    public HashMap fillData(int rowIndexSelected) {
        _tableModel = (TableModel) _tblData.getModel();
        ArrayList rowdata = null;

        if (rowIndexSelected > -1) {
            rowdata = _tableModel.getRow(rowIndexSelected);
        }

        HashMap hashdata = new HashMap();
        String strColName = null;
        Object obj = null;

        for (int i = 0, j = _tableModel.getColumnCount(); i < j; i++) {
            if (rowdata != null) {
                obj = rowdata.get(i);
            }

            strColName = _tableModel.getColumnName(i).toUpperCase().trim();
            if (obj != null) {
                hashdata.put(strColName, obj);
            } else {
                hashdata.put(strColName, "");
            }
        }
        rowdata = null;
        hashdata = null;
        obj = null;
        return hashdata;
    }

    public void refreshTable() {
        //        TableSorter tableSorter = new TableSorter();
        //        tableSorter.addMouseListenerToHeaderInTable(_tblData);
        //
        //        tableSorter.setModel(_tableModel);
        //        tableSorter.fireTableDataChanged();
        if (_tblData != null && _tableModel != null) {
            _tblData.setModel(_tableModel);
            _tblData.revalidate();
        }

    }

    public void searchData(String searchTxt, int selCol, int selColCri, boolean chkCase) {
        ArrayList arrFilterRow = new ArrayList();
        ArrayList arrOriRow = new ArrayList();
        String strArrData;
        int j = data.size();
        if (searchTxt.length() > 0) {
            //            if (j<=MAXDATA) {
            //                arrFilterRow = data;
            //                j=0;
            //            }
            for (int i = 0; i < j; i++) {
                arrOriRow = (ArrayList) data.get(i);
                System.out.println("### Data Row : " + arrOriRow);
                strArrData = arrOriRow.get(selCol) != null ? arrOriRow.get(selCol).toString() : "";
                if (strArrData != null) {
                    strArrData = strArrData.trim();
                    if (!chkCase) {
                        strArrData = strArrData.toUpperCase();
                    }

                    if ((selColCri == 2 && strArrData.equals(searchTxt))
                            || (selColCri == 0 && strArrData.startsWith(searchTxt))
                            || (selColCri == 1 && strArrData.endsWith(searchTxt))) {
                        arrFilterRow.add(arrOriRow);
                    } else if (selColCri == 3) {
                        if (Pattern.matches(searchTxt + "\\w*", strArrData)) {
                            arrFilterRow.add(arrOriRow);
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < j; i++) {
                arrOriRow = (ArrayList) data.get(i);
                System.out.println("### Data Row : " + arrOriRow);
                strArrData = arrOriRow.get(selCol) != null ? arrOriRow.get(selCol).toString() : "";
                if (strArrData != null) {
                    strArrData = strArrData.trim();
                    if (strArrData.length() == 0) {
                        arrFilterRow.add(arrOriRow);
                    }
                }
            }
        }
        System.out.println("### Selected Rows : " + arrFilterRow);
        TableSorter tableSorter = new TableSorter();
        tableSorter.addMouseListenerToHeaderInTable(_tblData);

        TableModel tmlNew = new TableModel();
        tmlNew.setHeading(_heading);
        tmlNew.setData(arrFilterRow);
        tmlNew.fireTableDataChanged();
        tableSorter.setModel(tmlNew);
        tableSorter.fireTableDataChanged();

        _tblData.setModel(tableSorter);
        _tblData.revalidate();
        arrFilterRow = null;
        arrOriRow = null;
    }

    public void clearTable(){
         _tableModel = new TableModel();
    }
    public void resetForm() {       
        _tableModel = new TableModel();
        refreshTable();
        ttNotifyObservers();
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    /**
     * Getter for property dataSize.
     *
     * @return Value of property dataSize.
     */
    public int getDataSize() {
        return dataSize;
    }

    /**
     * Setter for property dataSize.
     *
     * @param dataSize New value of property dataSize.
     */
    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    /**
     * Getter for property cbmProdType.
     *
     * @return Value of property cbmProdType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }

    public String getLblAddressValue() {
        return lblAddressValue;
    }

    public void setLblAddressValue(String lblAddressValue) {
        this.lblAddressValue = lblAddressValue;
    }

    /**
     * Setter for property cbmProdType.
     *
     * @param cbmProdType New value of property cbmProdType.
     */
    public void setCbmProdType(com.see.truetransact.clientutil.ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
    }

    /**
     * Getter for property prodType.
     *
     * @return Value of property prodType.
     */
    public java.lang.String getProdType() {
        return prodType;
    }

    /**
     * Setter for property prodType.
     *
     * @param prodType New value of property prodType.
     */
    public void setProdType(java.lang.String prodType) {
        this.prodType = prodType;
    }

    /**
     * Getter for property txtAccountNo.
     *
     * @return Value of property txtAccountNo.
     */
    public java.lang.String getTxtAccNo() {
        return txtAccNo;
    }

    /**
     * Setter for property txtAccountNo.
     *
     * @param txtAccountNo New value of property txtAccountNo.
     */
    public void setTxtAccNo(java.lang.String txtAccNo) {
        this.txtAccNo = txtAccNo;
    }

    /**
     * Getter for property tdtFromDate.
     *
     * @return Value of property tdtFromDate.
     */
    public java.util.Date getTdtFromDate() {
        return tdtFromDate;
    }

    /**
     * Setter for property tdtFromDate.
     *
     * @param tdtFromDate New value of property tdtFromDate.
     */
    public void setTdtFromDate(java.util.Date tdtFromDate) {
        this.tdtFromDate = tdtFromDate;
    }

    /**
     * Getter for property tdtToDate.
     *
     * @return Value of property tdtToDate.
     */
    public java.util.Date getTdtToDate() {
        return tdtToDate;
    }

    /**
     * Setter for property tdtToDate.
     *
     * @param tdtToDate New value of property tdtToDate.
     */
    public void setTdtToDate(java.util.Date tdtToDate) {
        this.tdtToDate = tdtToDate;
    }

    /**
     * Getter for property lblAccNameValue.
     *
     * @return Value of property lblAccNameValue.
     */
    public java.lang.String getLblAccNameValue() {
        return lblAccNameValue;
    }

    /**
     * Setter for property lblAccNameValue.
     *
     * @param lblAccNameValue New value of property lblAccNameValue.
     */
    public void setLblAccNameValue(java.lang.String lblAccNameValue) {
        this.lblAccNameValue = lblAccNameValue;
    }

    /**
     * Getter for property linkMap.
     *
     * @return Value of property linkMap.
     */
    public java.util.HashMap getLinkMap() {
        return linkMap;
    }

    /**
     * Setter for property linkMap.
     *
     * @param linkMap New value of property linkMap.
     */
    public void setLinkMap(java.util.HashMap linkMap) {
        this.linkMap = linkMap;
    }

    public HashMap getPrintMap() {
        return printMap;
    }

    public void setPrintMap(HashMap printMap) {
        this.printMap = printMap;
    }

    protected void clearPrintMap() {
        printMap.clear();
    }

    public ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }

    public void setCbmProdId(ComboBoxModel cbmProdId) {
        this.cbmProdId = cbmProdId;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public double getAssetBal() {
        return assetBal;
    }

    public void setAssetBal(double assetBal) {
        this.assetBal = assetBal;
    }

    public double getLiableBal() {
        return liableBal;
    }

    public void setLiableBal(double liableBal) {
        this.liableBal = liableBal;
    }
    
}