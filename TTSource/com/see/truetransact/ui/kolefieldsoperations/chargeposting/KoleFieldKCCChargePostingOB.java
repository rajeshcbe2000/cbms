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

package com.see.truetransact.ui.kolefieldsoperations.chargeposting;

//import java.util.Observable;
import com.see.truetransact.ui.termloan.riskfund.*;
import com.see.truetransact.ui.termloan.riskfund.*;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
//import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.Date;
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
//import com.see.truetransact.transferobject.termloan.chargesTo.TermLoanChargesTO;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.DateUtil;

/** Authorize Observable is the supporting class for Authorize UI
 *
 * @author bala
 */
public class KoleFieldKCCChargePostingOB extends CObservable {
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
    private String cboProdName;
    private String cboProdId;
    private String cboProdTypCr;
    private String cboProdIdCr;
    private String tdtFromDate;
    private String tdtToDate;
    private String cboChargeType;
    private String txtAccHd;
    private double riskFundTot;
    private String lblActHeadDesc;
    private String debitProdType;
    private ComboBoxModel cbmbranch;
    private String chkDebitLoanType = "";
    private String chkPostCharges = "";
    private String behavesLike = "";

    public ComboBoxModel getCbmbranch() {
        return cbmbranch;
    }

    public void setCbmbranch(ComboBoxModel cbmbranch) {
        this.cbmbranch = cbmbranch;
    }

    public String getDebitProdType() {
        return debitProdType;
    }

    public void setDebitProdType(String debitProdType) {
        this.debitProdType = debitProdType;
    }
    
    public String getLblActHeadDesc() {
        return lblActHeadDesc;
    }

    public void setLblActHeadDesc(String lblActHeadDesc) {
        this.lblActHeadDesc = lblActHeadDesc;
    }

    public double getRiskFundTot() {
        return riskFundTot;
    }

    public void setRiskFundTot(double riskFundTot) {
        this.riskFundTot = riskFundTot;
    }

    public String getTxtAccHd() {
        return txtAccHd;
    }

    public void setTxtAccHd(String txtAccHd) {
        this.txtAccHd = txtAccHd;
    }

    public String getCboChargeType() {
        return cboChargeType;
    }

    public void setCboChargeType(String cboChargeType) {
        this.cboChargeType = cboChargeType;
    }

    public String getTdtToDate() {
        return tdtToDate;
    }

    public void setTdtToDate(String tdtToDate) {
        this.tdtToDate = tdtToDate;
    }

    public String getTdtFromDate() {
        return tdtFromDate;
    }

    public void setTdtFromDate(String tdtFromDate) {
        this.tdtFromDate = tdtFromDate;
    }
    
    public String getCboProdName() {
        return cboProdName;
    }

    public void setCboProdName(String cboProdName) {
        this.cboProdName = cboProdName;
    }
    
    public String getCboProdId() {
        return cboProdId;
    }

    public void setCboProdId(String cboProdId) {
        this.cboProdId = cboProdId;
    }
    
    public String getCboProdIdCr() {
        return cboProdIdCr;
    }

    public void setCboProdIdCr(String cboProdIdCr) {
        this.cboProdIdCr = cboProdIdCr;
    }

    public String getCboProdTypCr() {
        return cboProdTypCr;
    }

    public void setCboProdTypCr(String cboProdTypCr) {
        this.cboProdTypCr = cboProdTypCr;
    }
    private ComboBoxModel cbmChargeType;

    public ComboBoxModel getCbmChargeType() {
        return cbmChargeType;
    }

    public void setCbmChargeType(ComboBoxModel cbmChargeType) {
        this.cbmChargeType = cbmChargeType;
    }
    private ComboBoxModel cbmProdId;
    private ComboBoxModel cbmProdTypCr;
    private String prodTypCr;
    private ComboBoxModel cbmProdIdCr;
    private String txtActNo;

    public String getTxtActNo() {
        return txtActNo;
    }

    public void setTxtActNo(String txtActNo) {
        this.txtActNo = txtActNo;
    }

    public ComboBoxModel getCbmProdIdCr() {
        return cbmProdIdCr;
    }

    public void setCbmProdIdCr(String prodType) {
       if (CommonUtil.convertObjToStr(prodType).length() > 1) {
            if (prodType.equals("GL")) {
                key = new ArrayList();
                value = new ArrayList();
            } else {
                try {
                    lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + prodType);
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
        cbmProdIdCr = new ComboBoxModel(key, value);
        this.cbmProdIdCr = cbmProdIdCr;
        setChanged();
    }

   public void fillBranch() {
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        HashMap mapShare = new HashMap();
        List keyValue = null;
        keyValue = ClientUtil.executeQuery("getbranchesForBalanceSheet", mapShare);
        key.add("");
        value.add("");
        if (keyValue != null && keyValue.size() > 0) {
            for (int i = 0; i < keyValue.size(); i++) {
                mapShare = (HashMap) keyValue.get(i);
                key.add(mapShare.get("BRANCH_CODE"));
                value.add(mapShare.get("BRANCH_NAME"));
            }
        }
        cbmbranch = new ComboBoxModel(key, value);
        key = null;
        value = null;
        keyValue.clear();
        keyValue = null;
        mapShare.clear();
        mapShare = null;
    }

    public String getProdTypCr() {
        return prodTypCr;
    }

    public void setProdTypCr(String prodTypCr) {
        this.prodTypCr = prodTypCr;
    }

    public ComboBoxModel getCbmProdTypCr() {
        return cbmProdTypCr;
    }

    public void setCbmProdTypCr(ComboBoxModel cbmProdTypCr) {
        this.cbmProdTypCr = cbmProdTypCr;
    }
//    private ComboBoxModel cbmNoticeType;
    private Map guarantorMap = null;
    private ArrayList guarantorList = null;
    private String txtNoticeCharge = "";
    private String txtPostageCharge = "";
    private ProxyFactory proxy;
    private HashMap map;
    private HashMap operationMap;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String prodType="";
    private String result;
    private Date tdtAuctionDate=null;
    private HashMap dataHash;
    private ArrayList data;
     final ArrayList tableTitle = new ArrayList();
     final ArrayList ChargetableTitle = new ArrayList();
     private EnhancedTableModel   tblData;
     private int dataSize;
     private Date currDt;
     private HashMap lookUpHash;
     private HashMap keyValue;
      private ArrayList key;
    private ArrayList value;

    private final String RISK_FUND_JNDI = "RiskFundJNDI";
    private final String RISK_FUND_HOME = "termloan.riskfund.RiskFundHome";
    private final String RISK_FUND_REMOTE = "termloan.riskfund.RiskFund";

    private void getKeyValue(HashMap keyValue) throws Exception {
        //log.info("In getKeyValue()");

        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }


    private List finalList = null;
    

    public List getFinalList() {
        return finalList;
    }

    public void setFinalList(List finalList) {
        this.finalList = finalList;
    }
    
    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public EnhancedTableModel getTblData() {
        return tblData;
    }

    public void setTblData(EnhancedTableModel tblData) {
        this.tblData = tblData;
    }
    
    /** Creates a new instance of AuthorizeOB */
    public KoleFieldKCCChargePostingOB() {
        try {
            proxy = ProxyFactory.createProxy();
            //setOperationMap();
           map = new HashMap();
             map.put(CommonConstants.JNDI, "KccChargePostingJNDI");
            map.put(CommonConstants.HOME, "KoleFieldsKCCChargePostingHome");
            map.put(CommonConstants.REMOTE, "KoleFieldsKCCChargePosting");
            //setTableTitle();
            fillDropDown();
            fillBranch();
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    public void fillDropDown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("PRODUCTTYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        
        getKeyValue((HashMap) keyValue.get("PRODUCTTYPE"));
        cbmProdTypCr = new ComboBoxModel(key, value);
        
        lookup_keys.add("TERMLOAN.KOLE_FIELD_CHARGE_TYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);       
        getKeyValue((HashMap) keyValue.get("TERMLOAN.KOLE_FIELD_CHARGE_TYPE"));
        cbmChargeType = new ComboBoxModel(key, value);
    }
    public void setTableTitle()
    {
        tableTitle.add("Select");
        tableTitle.add("Account No");
        tableTitle.add("Cust Id");
        tableTitle.add("Name");
        tableTitle.add("Membership No");
        tableTitle.add("Loan No");
        tableTitle.add("Product ID");
        tableTitle.add("Risk Fund");
        tableTitle.add("Bank Title");
        tableTitle.add("Branch Name");
    }
    
    public void setChargetableTitle()
    {
        ChargetableTitle.add("Select");
        ChargetableTitle.add("Account No");
        ChargetableTitle.add("Loan Type");
        ChargetableTitle.add("Cust Id");        
        ChargetableTitle.add("Membership No");
        ChargetableTitle.add("Name");
        ChargetableTitle.add("Limit");
        ChargetableTitle.add("Balance");
        ChargetableTitle.add("Charge Amount");
        ChargetableTitle.add("Principle Due");
        ChargetableTitle.add("Due Date");
        ChargetableTitle.add("Interest Due");
        ChargetableTitle.add("Penal");
        ChargetableTitle.add("Charges");
    }
    
    public void setAccountHead() {
        try {
            final HashMap accountHeadMap = new HashMap();
            accountHeadMap.put("PROD_ID", (String) cbmProdIdCr.getKeyForSelected());
            if (!this.getProdType().equals("") && !this.getProdType().equals("GL") && !accountHeadMap.get("PROD_ID").equals("")) {
                final List resultList = ClientUtil.executeQuery("getAccountHeadProd" + this.getProdType(), accountHeadMap);
                final HashMap resultMap = (HashMap) resultList.get(0);
                setTxtAccHd(resultMap.get("AC_HEAD").toString());
                setLblActHeadDesc(resultMap.get("AC_HEAD_DESC").toString());
            }
        } catch (Exception e) {
        }
    }
    public void fillDropDown1(String productType) {
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        HashMap lookUpHash = new HashMap();
        lookUpHash.put("PROD_ID", productType);
        List keyValue = null;
        setProdType(productType);
        if (!productType.equals("ALL_PRODUCT")) {
            if (productType.equals("MDS")) {
                keyValue = ClientUtil.executeQuery("getProductsForMDSRiskFund", lookUpHash);
            } else {
                keyValue = ClientUtil.executeQuery("getProductsForLoanNotice", lookUpHash);
            }
            key.add("");
            value.add("");
            LinkedHashMap prodMap = new LinkedHashMap();
            if (keyValue != null && keyValue.size() > 0) {
                for (int i = 0; i < keyValue.size(); i++) {
                    prodMap = (LinkedHashMap) keyValue.get(i);
                    key.add(prodMap.get("PROD_ID"));
                    value.add(prodMap.get("PROD_DESC"));
                }
                prodMap.clear();
                prodMap = null;
            }
        }
        if (productType.equals("ALL_PRODUCT")) {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
        cbmProdId = new ComboBoxModel(key, value);
        key = null;
        value = null;
        //lookUpHash.clear();
        lookUpHash = null;
        // keyValue.clear();
        keyValue = null;
    }
    
       
    /** updateStatus method used to update the database field based on the UI button
     * pressed
     *
     * @param map       HashMap from UI which is passed as a argument to Authorize UI constructor
     * @param status    Passed by UI. (Authorize, Reject, Exception - statuses)
      */   
    
    
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
    
            
       
        
        
        
        
      
//        _isAvailable = ClientUtil.setTableModel(mapID, tblData);
        
        
         
        
    public void populateData(HashMap mapID, CTable tblDatan) {
        try{
        _tblData= tblDatan;
        HashMap whereMap=null;
         if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
            if (mapID.get(CommonConstants.MAP_WHERE) instanceof HashMap)
                whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
            else 
                System.out.println ("Convert other data type to HashMap:" + mapID);
        } else {
            whereMap = new HashMap();
        }
         if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put (CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put (CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }
         whereMap.put("FROM_DATE", mapID.get("FROM_DATE"));
         whereMap.put("TO_DATE"  , mapID.get("TO_DATE"));
         whereMap.put("PROD_ID", mapID.get("PROD_ID"));
         whereMap.putAll(mapID);   
        mapID.put (CommonConstants.MAP_WHERE, whereMap);
        dataHash = ClientUtil.executeTableQuery(mapID);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        ArrayList tblDatanew=new ArrayList();
        for(int i=0;i<data.size();i++){
            List tmpList=(List) data.get(i);
            ArrayList newList=new ArrayList();
            newList.add(new Boolean(false));
            newList.add(tmpList.get(3));
            newList.add(tmpList.get(14));
            newList.add(tmpList.get(2));
            newList.add(tmpList.get(13));
            newList.add(tmpList.get(12));
            newList.add(tmpList.get(15));
            newList.add(tmpList.get(1));
            newList.add(tmpList.get(6));
            newList.add(tmpList.get(7));
            tblDatanew.add(newList);
                    }
//        setRiskFundTot(riskFindTot);
        tblData= new EnhancedTableModel((ArrayList)tblDatanew, tableTitle);        
        setDataSize(data.size());
        dataHash.clear();
        data.clear();
        _heading.clear();
         }catch(Exception e){
            e.printStackTrace();
        }
    }
    
        public void populateChargeData(HashMap mapID, CTable tblDatan) {
        try{
            
        _tblData= tblDatan;
        HashMap whereMap=null;
         if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
            if (mapID.get(CommonConstants.MAP_WHERE) instanceof HashMap)
                whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
            else 
                System.out.println ("Convert other data type to HashMap:" + mapID);
        } else {
            whereMap = new HashMap();
        }
         if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put (CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put (CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }
         whereMap.put("FROM_DATE", mapID.get("FROM_DATE"));
         whereMap.put("TO_DATE"  , mapID.get("TO_DATE"));
         whereMap.put("PROD_ID", mapID.get("PROD_ID"));
         whereMap.putAll(mapID);   
        mapID.put (CommonConstants.MAP_WHERE, whereMap);
        dataHash = ClientUtil.executeTableQuery(mapID);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        ArrayList tblDatanew=new ArrayList();
        for(int i=0;i<data.size();i++){
            List tmpList=(List) data.get(i);
            ArrayList newList=new ArrayList();
            newList.add(new Boolean(false));
            newList.add(tmpList.get(1));
            newList.add(tmpList.get(5));
            newList.add(tmpList.get(2));
            newList.add(tmpList.get(3));
            newList.add(tmpList.get(4));
            newList.add(tmpList.get(7));
            newList.add(tmpList.get(8));
            newList.add(tmpList.get(9));
            newList.add(tmpList.get(10));
            newList.add(tmpList.get(11));
            newList.add(tmpList.get(12));
            newList.add(tmpList.get(13));
            newList.add(tmpList.get(13));
            tblDatanew.add(newList);           
        }
        tblData= new EnhancedTableModel((ArrayList)tblDatanew, ChargetableTitle);        
        setDataSize(data.size());
        dataHash.clear();
        data.clear();
        _heading.clear();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
   
    public void executeOB(ArrayList riskFund) throws Exception{
        try{      
        HashMap mapRiskFund= new HashMap();
        mapRiskFund.put("CHARGE_LIST",riskFund);
        mapRiskFund.put("FROM_DATE", getTdtFromDate());
        mapRiskFund.put("TO_DATE", getTdtToDate());
        mapRiskFund.put("PROD_NAME_DR",getCboProdName());
        mapRiskFund.put("PROD_ID_DR",getCboProdId());
        mapRiskFund.put("BEHAVES_LIKE",getBehavesLike());
        mapRiskFund.put("PROD_TYP_CR", getCboProdTypCr());
        mapRiskFund.put("PROD_ID_CR", getCboProdIdCr());
        mapRiskFund.put("CHRG_TYP", getCboChargeType());
        mapRiskFund.put("PROD_TYP_DR", getDebitProdType());
        mapRiskFund.put("AC_HD", getTxtAccHd());
        mapRiskFund.put("SCREEN",getScreen());
        mapRiskFund.put("AC_NO", getTxtActNo());
        mapRiskFund.put("RISK_FUND_TOT", getRiskFundTot());
        if(getChkPostCharges().equals("Y")){
            mapRiskFund.put("POST_CHARGES", "Y");
        }else if(getChkDebitLoanType().endsWith("Y")){
            mapRiskFund.put("CHARGE_DEBIT_FROM_LOAN", "Y");
        }
        mapRiskFund.put (CommonConstants.INITIATED_BRANCH, ProxyParameters.BRANCH_ID);
        if (!getCbmbranch().getKeyForSelected().equals("") && getCbmbranch().getKeyForSelected()!=null) {
            mapRiskFund.put("ACCOUNT_BRANCH", getCbmbranch().getKeyForSelected());
        }else{
           mapRiskFund.put ("ACCOUNT_BRANCH", ProxyParameters.BRANCH_ID); 
        }
        mapRiskFund.put ("BRANCH_CODE", ProxyParameters.BRANCH_ID); 
        mapRiskFund.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        mapRiskFund.put(CommonConstants.MODULE, getModule());
        mapRiskFund.put( CommonConstants.SCREEN, getScreen());  
        HashMap proxyResultMap=proxy.execute(mapRiskFund, map);
        setProxyReturnMap(proxyResultMap);
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e, true);
            ClientUtil.showMessageWindow(e.getMessage());
        }
    }
    
     public void resetForm(){
        setDataSize(0);
        setDebitProdType("");
        setFinalList(null);
        setProdTypCr("");
        setProdType("");
        setLblActHeadDesc("");
        setTdtFromDate("");
        setTdtToDate("");
        setTxtAccHd("");
        setTxtActNo("");
        setChkPostCharges("N");
        setChkDebitLoanType("N");
        setBehavesLike("");
        setChanged();
    }
     
     ///public void resetTableValues(){
     //   tblData.setDataArrayList(null,tableTitle);
    //}    
    
    
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
        TableModel tblModel = null;
        if (table.getModel() instanceof TableSorter) {
            tblModel = ((TableSorter) table.getModel()).getModel();
        } else if (table.getModel() instanceof TableModel) {
            tblModel = (TableModel) table.getModel();
        }
        System.out.println("@@## Data ArrayList : "+tblModel.getDataArrayList());
        return tblModel.getDataArrayList();
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
    
    /**
     * Getter for property result.
     * @return Value of property result.
     */
    public java.lang.String getResult() {
        return result;
    }
    
    /**
     * Setter for property result.
     * @param result New value of property result.
     */
    public void setResult(java.lang.String result) {
        this.result = result;
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
     * Getter for property tdtAuctionDate.
     * @return Value of property tdtAuctionDate.
     */
    public Date getTdtAuctionDate() {
        return tdtAuctionDate;
    }
    
    /**
     * Setter for property tdtAuctionDate.
     * @param tdtAuctionDate New value of property tdtAuctionDate.
     */
    public void setTdtAuctionDate(Date tdtAuctionDate) {
        this.tdtAuctionDate = tdtAuctionDate;
    }
 
    private void setOperationMap() throws Exception {
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, RISK_FUND_JNDI);
        operationMap.put(CommonConstants.HOME, RISK_FUND_HOME);
        operationMap.put(CommonConstants.REMOTE, RISK_FUND_REMOTE);
    }

    public String getChkDebitLoanType() {
        return chkDebitLoanType;
    }

    public void setChkDebitLoanType(String chkDebitLoanType) {
        this.chkDebitLoanType = chkDebitLoanType;
    }

    public String getChkPostCharges() {
        return chkPostCharges;
    }

    public void setChkPostCharges(String chkPostCharges) {
        this.chkPostCharges = chkPostCharges;
    }

    public String getBehavesLike() {
        return behavesLike;
    }

    public void setBehavesLike(String behavesLike) {
        this.behavesLike = behavesLike;
    }
    
}