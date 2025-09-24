/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositInterestApplicationOB.java
 *
 * Created on Mon Jun 13 18:24:58 IST 2011
 */

package com.see.truetransact.ui.locker.lockerSI;


import java.util.List;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.*;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CTable;
import java.util.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author
 */

public class LockerRentSIApplicationOB extends CObservable{
   
    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();  
    private ArrayList key;
    private ArrayList value;
    private HashMap keyValue;
    private HashMap lookupMap;
    
    private EnhancedTableModel tblLockerRentSIApplication;
    private ComboBoxModel cboLocType;
    private ProxyFactory proxy;
    private HashMap map;
    // private final static Logger log = Logger.getLogger(DepositInterestApplicationOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int result;
    private int actionType;
    private boolean rdoPrizedMember_Yes = false;
    private boolean rdoPrizedMember_No = false;
    private List finalList = null;
    private List finalTableList = null;
    private String cboLockerType;
    private ComboBoxModel cboLockType;
    private String txtProductID = "";
    private String txtTokenNo = "";
    private String lblRentDueAsOn="";
    private String txtRentDueAsOnMM="";
    private String txtRentDueAsOnyyyy="";
    private HashMap _authorizeMap;
    //Added by Rishad 27/03/2019 
    private boolean _isAvailable = true;
    private TableModel _tableModel;
    private CTable _tblData;
    private ArrayList _heading;
    private ArrayList sizeList =null;
    private TableModel searchTableModel;
    private ArrayList tempArrayList;
    private boolean isMultiSelect = false;
    HashMap processMap = new HashMap();
         
    public LockerRentSIApplicationOB() {
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "LockerRentSIApplicationJNDI");
            map.put(CommonConstants.HOME, "Locker.LockerSI.LockerRentSIApplicationHome");
            map.put(CommonConstants.REMOTE, "Locker.LockerSI.LockerRentSIApplication");
            setLockerRentSIApplicationTableTitle();
            tblLockerRentSIApplication = new EnhancedTableModel(null, tableTitle);
            fillDropdown();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public void setLockerRentSIApplicationTableTitle(){
        tableTitle.add("Select");
        tableTitle.add("LocNo");
        tableTitle.add("Name");
        tableTitle.add("Exp Date");
        tableTitle.add("Commission");
        tableTitle.add("ServiceTax");
        tableTitle.add("Fine");
        tableTitle.add("ProdId");
        tableTitle.add("ProdType");
        tableTitle.add("AvailableBalance");
        tableTitle.add("SI A/c No");
        tableTitle.add("GST");
        IncVal = new ArrayList();
    }
  
    public void insertTableData(ArrayList list) {
        try {
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            tblLockerRentSIApplication = new EnhancedTableModel((ArrayList) list, tableTitle) {

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    if (columnIndex == 5 || columnIndex == 6) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };

        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }
    
      public void insertTableData(ArrayList list,ArrayList title) {
        try {
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            tblLockerRentSIApplication = new EnhancedTableModel((ArrayList) list, title) {

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    if (columnIndex == 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };

        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }
     public void resetTableValues(){
        tblLockerRentSIApplication.setDataArrayList(null,tableTitle);
    }
       public void resetForm(){
        resetTableValues();
        setChanged();
    }
  
     private void fillDropdown() throws Exception{
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
        
        final HashMap param = new HashMap();
        
        param.put(CommonConstants.MAP_NAME, "getLockerProducts");
        param.put(CommonConstants.PARAMFORQUERY, null);
        HashMap lookupValues = ClientUtil.populateLookupData(param);
        fillData((HashMap)lookupValues.get(CommonConstants.DATA));
        int j=value.size();
        for(int i=1;i<j;i++){
            value.set(i, (String)value.get(i)+" ("+(String)key.get(i)+")");
        }
     cboLocType = new ComboBoxModel(key,value);
     
     
     }
     private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
     
      private HashMap populateDataLocal(HashMap obj)  throws Exception{
        keyValue = proxy.executeQuery(obj,lookupMap);
        
        return keyValue;
    }
     
    /** To perform the necessary action */
    public  void doActionPerform(HashMap ldata)throws Exception {
       
        System.out.println("Data in OBockerRentSIApplication OB : " + ldata);
        HashMap proxyResultMap = proxy.execute(ldata, map);
        setProxyReturnMap(proxyResultMap);
        setResult(getActionType());
    }
    
     public  void doActionPerformed()throws Exception {
//         final HashMap data = new HashMap();
//         LinkedHashMap transactionDetailsTO;
//         data.put("MODE", command);
//         
//         data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
//         data.put(CommonConstants.USER_ID, _authorizeMap.get(CommonConstants.USER_ID));
//         
//         data.put(CommonConstants.MODULE, getModule());
//         data.put(CommonConstants.SCREEN, getScreen());
//         LinkedHashMap transMap=new LinkedHashMap();
         HashMap proxyResultMap = proxy.execute(getProcessMap(), map);
         setProxyReturnMap(proxyResultMap);
         setResult(getActionType());
          
     }
    
    /**
     * Getter for property tblDepositInterestApplication.
     * @return Value of property tblDepositInterestApplication.
     */
    
      
    /**
     * Setter for property tblDepositInterestApplication.
     * @param tblDepositInterestApplication New value of property tblDepositInterestApplication.
     */
  
    /**
     * Getter for property rdoPrizedMember_Yes.
     * @return Value of property rdoPrizedMember_Yes.
     */
    public void setLblRentDueAsOn(String lblRentDueAsOn) {
        this.lblRentDueAsOn = lblRentDueAsOn;
    }

    public String getLblRentDueAsOn() {
        return lblRentDueAsOn;
    }

    public void setTxtRentDueAsOnMM(String txtRentDueAsOnMM) {
        this.txtRentDueAsOnMM = txtRentDueAsOnMM;
    }

    public String getTxtRentDueAsOnMM() {
        return txtRentDueAsOnMM;
    }

    public void setTxtRentDueAsOnyyyy(String txtRentDueAsOnyyyy) {
        this.txtRentDueAsOnyyyy = txtRentDueAsOnyyyy;
    }

    public String getTxtRentDueAsOnyyyy() {
        return txtRentDueAsOnyyyy;
    }

    /**
     *
     * Setter for property rdoPrizedMember_Yes.
     *
     * @param rdoPrizedMember_Yes New value of property rdoPrizedMember_Yes.
     */
    /**
     * Getter for property rdoPrizedMember_No.
     *
     * @return Value of property rdoPrizedMember_No.
     */
    /**
     * Setter for property rdoPrizedMember_No.
     *
     * @param rdoPrizedMember_No New value of property rdoPrizedMember_No.
     */
    /**
     * Getter for property finalList.
     *
     * @return Value of property finalList.
     */
    public java.util.List getFinalList() {
        return finalList;
    }

    /**
     * Setter for property finalList.
     *
     * @param finalList New value of property finalList.
     */
    public void setFinalList(java.util.List finalList) {
        this.finalList = finalList;
    }

    /**
     * Getter for property txtProductID.
     *
     * @return Value of property txtProductID.
     */
    /**
     * Setter for property txtProductID.
     *
     * @param txtProductID New value of property txtProductID.
     */
    /**
     * Getter for property result.
     *
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }

    /**
     * Setter for property result.
     *
     * @param result New value of property result.
     */
    public void setResult(int result) {
        this.result = result;
    }

    /**
     * Getter for property actionType.
     *
     * @return Value of property actionType.
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * Setter for property actionType.
     *
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * Getter for property tableTitle.
     *
     * @return Value of property tableTitle.
     */
    public java.util.ArrayList getTableTitle() {
        return tableTitle;
    }

    public String getCboLockerType() {
        return cboLockerType;
    }

    public void setCboLockerType(String cboLockerType) {
        this.cboLockerType = cboLockerType;
    }

    public com.see.truetransact.clientutil.ComboBoxModel getCboLocType() {
        return cboLocType;
    }

//    public void setTblLockerRentSIApplication(com.see.truetransact.clientutil.ComboBoxModel cboLocType) {
//        this.cboLocType = cboLocType;
//    }

    public java.util.HashMap getAuthorizeMap() {
        return _authorizeMap;
    }

    /**
     * Setter for property _authorizeMap.
     *
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void setAuthorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }

    public com.see.truetransact.clientutil.EnhancedTableModel getTblLockerRentSIApplication() {
        return tblLockerRentSIApplication;
    }

    public void setTblLockerRentSIApplication(com.see.truetransact.clientutil.EnhancedTableModel tblLockerRentSIApplication) {
        this.tblLockerRentSIApplication = tblLockerRentSIApplication;
    }

    public HashMap getProcessMap() {
        return processMap;
    }

    public void setProcessMap(HashMap processMap) {
        this.processMap = processMap;
    }
 
    
  public ArrayList populateData(HashMap mapID, CTable tblData) {
     HashMap whereMap = null;
      _heading = null;
      _tblData = tblData;
      HashMap tempMap = new HashMap();
      tblData.setModel(new DefaultTableModel());
      if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
          whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
      } else {
          whereMap = new HashMap();
      }

      whereMap.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
      if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
          whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
      }
      if (!whereMap.containsKey(CommonConstants.USER_ID)) {
          whereMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
      }
      javax.swing.table.DefaultTableModel tblModel = null;
      if (tblData.getModel() instanceof TableSorter) {
          _tableModel = ((TableSorter) tblData.getModel()).getModel();
      } else if (tblData.getModel() instanceof TableModel) {
          _tableModel = (TableModel) tblData.getModel();
      } else {
          tblModel = (javax.swing.table.DefaultTableModel) tblData.getModel();
      }

      while (_tableModel != null && tblData.getRowCount() > 0) {
          _tableModel.removeRow(0);
      }
      while (tblModel != null && tblData.getRowCount() > 0) {
          tblModel.removeRow(0);
      }
       
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
      String cellData = "", keyData = "";
      Object obj = null;
      int cellLen = 0;
      for (int i = 0, j = list.size(), k = 1; i < j; i++, k = 1) {
          map = (HashMap) list.get(i);
          colData = new ArrayList();
          iterator = map.values().iterator();
          if (isMultiSelect) {
              colData.add(new Boolean(false));
          }

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

      setTblModel(tblData, data, _heading);
        
//        TableColumn col = null;
//        for (int i=0, j=sizeList.size(), k=0; i < j; i++) {
//            k = ((Integer)sizeList.get(i)).intValue();
//            if (k > 50) {
//                k = 400;
//            } else {
//                k *= 8;
//            }
//            col = tblData.getColumn(_heading.get(i));
//            col.setPreferredWidth(k);
//            col.setMinWidth(k);
//        }
//        tblData.revalidate();
        
        if (tblData.getModel() instanceof TableSorter) {
            _tableModel = ((TableSorter) tblData.getModel()).getModel();
        } else {
            _tableModel = (TableModel) tblData.getModel();
        }
        tempMap.clear();
        tempMap = null;
        return _heading;
    }
        
    private void setTblModel(final CTable tbl, ArrayList tblData, ArrayList head) {
       TableSorter tableSorter = new TableSorter();
       // tableSorter.addMouseListenerToHeaderInTable(tbl);
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
    
    
    public HashMap checkServiceTaxApplicable(String accheadId) {
        String checkFlag = "N";
        HashMap checkForTaxMap = new HashMap();
        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
            HashMap whereMap = new HashMap();
            whereMap.put("AC_HD_ID", accheadId);
            List accHeadList = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);                  
            if (accHeadList != null && accHeadList.size() > 0) {
                HashMap accHeadMap = (HashMap) accHeadList.get(0);
                if (accHeadMap != null && accHeadMap.containsKey("SERVICE_TAX_APPLICABLE")&& accHeadMap.containsKey("SERVICE_TAX_ID")) {
                    checkFlag = CommonUtil.convertObjToStr(accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_APPLICABLE",accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_ID",accHeadMap.get("SERVICE_TAX_ID"));
                }
            }
        }
        return checkForTaxMap;
    }
    
    
    
}