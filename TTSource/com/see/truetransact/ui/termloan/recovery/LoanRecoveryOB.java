/*
 * Copyright 2012 Fincuro Solutions (P) Ltd. All rights reserved.
 *
 * This software is the proprietary information of Fincuro Solutions (P) Ltd..  
 * 
 *
 * LoanRecoveryOB.java
 *
 * Created on Jan 3, 2019, 1:46 PM
 */
package com.see.truetransact.ui.termloan.recovery;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.CTable;
import java.util.*;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 * LoanRecoveryOB Observable is the supporting class for LoanRecoveryUI
 *
 * @author Rishad
 */
public class LoanRecoveryOB extends CObservable {

    final ArrayList tableTitle = new ArrayList();
    final ArrayList processTableTitle = new ArrayList();
    private TableModel tblSalaryRecoveryList;
    private TableModel tblSalaryMDSRecoveryList;
    private TableModel tblSalaryRDRecoveryList;
    private TableModel tblProcessListSalaryRecoveryList;
    private TableModel tblLoanSalaryRecoveryList;
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(LoanRecoveryOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int result;
    private int actionType;
    private List finalList = null;
    private HashMap finalMap = new HashMap();
    private String tdtCalcIntUpto = "";
    private ComboBoxModel cbmProdType;
    private String cboProdType = "";
    private ComboBoxModel cbmProdId;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private String txtAccountNo = "";
    private Date currDate = null;
    private boolean chkDE = false;
    private HashMap loanDetailsMap = new HashMap();
    private String instName = "";
    private String txtrecoveryId = "";
    private CTable _tblData;
    private ArrayList _heading;
    private ArrayList sizeList = null;
    private ArrayList tempArrayList;
    private boolean isMultiSelect = false;
    private HashMap newTransactionMap;
    private String recoveryProdType = "";
    List colourList = new ArrayList();

    // End
    /**
     * Creates a new instance of LoanRecoveryOB
     */
    public LoanRecoveryOB() {
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "LoanRecoveryJNDI");
            map.put(CommonConstants.HOME, "LoanRecoveryHome");
            map.put(CommonConstants.REMOTE, "LoanRecovery");
            currDate = ClientUtil.getCurrentDate();
            setLoanRecoveryTableTitle();
            setProcessInterestTableTitle();
            tblSalaryRecoveryList = new TableModel(null, tableTitle);
            tblSalaryMDSRecoveryList = new TableModel(null, tableTitle);
            tblSalaryRDRecoveryList = new TableModel(null, tableTitle);
            tblLoanSalaryRecoveryList = new TableModel(null, tableTitle);
            tblProcessListSalaryRecoveryList = new TableModel(null, tableTitle);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void setLoanRecoveryTableTitle() {
        tableTitle.add("SINO.");
        tableTitle.add("CUSTOMER_NO");
        tableTitle.add("MEMBER_NO");
        tableTitle.add("NAME");
        tableTitle.add("SCHEME NAME");
        tableTitle.add("A/C NO.");
        tableTitle.add("TOTAL DEMAND");
        tableTitle.add("PRINCIPAL");
        tableTitle.add("INTEREST");
        tableTitle.add("PENAL INT");
        tableTitle.add("CHARGES");
        tableTitle.add("BONUS");
        tableTitle.add("FORFEIT_BONUS");
        tableTitle.add("CLEAR BALANCE");
        tableTitle.add("INST_BRANCH");
    }

    public void setProcessInterestTableTitle() {
        processTableTitle.add("SELECT");
        processTableTitle.add("RECOVER_ID");
        processTableTitle.add("PROD_ID");
        processTableTitle.add("PROD_TYPE");
        processTableTitle.add("ACT_NO");
        processTableTitle.add("SCHEME_NAME");
        processTableTitle.add("TOTAL DEMAND");
        processTableTitle.add("PRINCIPAL");
        processTableTitle.add("INTEREST");
        processTableTitle.add("PENAL INT");
        processTableTitle.add("CHARGES");
        processTableTitle.add("BONUS");
        processTableTitle.add("RECOVERY_AMOUNT");
        processTableTitle.add("TOTAL_RECOVERY_AMT");
    }

    public ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }

    public void setCbmProdType(ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
    }

    public String getCboProdType() {
        return cboProdType;
    }

    public void setCboProdType(String cboProdType) {
        this.cboProdType = cboProdType;
    }

    public com.see.truetransact.clientutil.EnhancedTableModel getTblSalaryRecoveryList() {
        return tblSalaryRecoveryList;
    }

     public com.see.truetransact.clientutil.EnhancedTableModel getTblSalaryMDSRecoveryList() {
        return tblSalaryMDSRecoveryList;
    }
     
     public com.see.truetransact.clientutil.EnhancedTableModel getTblProcessListSalaryRecoveryList() {
        return tblProcessListSalaryRecoveryList;
    }
      
     
      public com.see.truetransact.clientutil.EnhancedTableModel getTblSalaryRDRecoveryList() {
        return tblSalaryRDRecoveryList;
    }
      
    public com.see.truetransact.clientutil.EnhancedTableModel getTblLoanSalaryRecoveryList() {
        return tblLoanSalaryRecoveryList;
    }  

    public String getTdtCalcIntUpto() {
        return tdtCalcIntUpto;
    }

    public void setTdtCalcIntUpto(String tdtCalcIntUpto) {
        this.tdtCalcIntUpto = tdtCalcIntUpto;
    }

    public String getTxtAccountNo() {
        return txtAccountNo;
    }

    public void setTxtAccountNo(String txtAccountNo) {
        this.txtAccountNo = txtAccountNo;
    }

    public List getFinalList() {
        return finalList;
    }

    public void setFinalList(List finalList) {
        this.finalList = finalList;
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    public String getInstName() {
        return instName;
    }

    public void setInstName(String instName) {
        this.instName = instName;
    }

    public String getTxtrecoveryId() {
        return txtrecoveryId;
    }

    public void setTxtrecoveryId(String txtrecoveryId) {
        this.txtrecoveryId = txtrecoveryId;
    }

    public HashMap getNewTransactionMap() {
        return newTransactionMap;
    }

    public void setNewTransactionMap(HashMap newTransactionMap) {
        this.newTransactionMap = newTransactionMap;
    }

    public void insertTableData() {
        try {
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("CALC_INT_UPTO_DT", DateUtil.getDateMMDDYYYY(getTdtCalcIntUpto()));
            whereMap.put("LOAN_RECOVERY", "LOAN_RECOVERY");
            whereMap.put("INST_ID", getInstName());
            HashMap proxyResultMap = proxy.executeQuery(whereMap, map);
            System.out.println("!@!@ proxyResultMap : " + proxyResultMap);
            if (proxyResultMap != null && proxyResultMap.size() > 0) {
                tableList = (ArrayList) proxyResultMap.get("RECOVERY_LIST_TABLE_DATA");
                setFinalList(tableList);
            }
            tblSalaryRecoveryList = new TableModel((ArrayList) tableList, tableTitle);
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    public String getSelected() {
        Boolean bln;
        String selected = "";
        for (int i = 0, j = tblSalaryRecoveryList.getRowCount(); i < j; i++) {
            bln = (Boolean) tblSalaryRecoveryList.getValueAt(i, 0);
            if (bln.booleanValue() == true) {
                selected += "'" + tblSalaryRecoveryList.getValueAt(i, 1);
                selected += "',";
            }
        }
        selected = selected.length() > 0 ? selected.substring(0, selected.length() - 1) : "";
        return selected;
    }

    public void setSelectAll(CTable table, Boolean selected) {
        for (int i = 0, j = table.getRowCount(); i < j; i++) {
            table.setValueAt(selected, i, 0);
        }
    }

    public ArrayList insertTableData(HashMap paraMap, CTable _table) {
        _heading = null;
        _tblData = _table;
        try {
            javax.swing.table.DefaultTableModel tblModel = null;
            if (_table.getModel() instanceof TableSorter) {
                tblSalaryRecoveryList = ((TableSorter) _table.getModel()).getModel();
            } else if (_table.getModel() instanceof TableModel) {
                tblSalaryRecoveryList = (TableModel) _table.getModel();
            } else {
                tblModel = (javax.swing.table.DefaultTableModel) _table.getModel();
            }

            while (tblSalaryRecoveryList != null && _table.getRowCount() > 0) {
                tblSalaryRecoveryList.removeRow(0);
            }
            while (tblModel != null && _table.getRowCount() > 0) {
                tblModel.removeRow(0);
            }
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("CALC_INT_UPTO_DT", DateUtil.getDateMMDDYYYY(getTdtCalcIntUpto()));
            whereMap.put("LOAN_RECOVERY", "LOAN_RECOVERY");
            whereMap.put("INST_ID", getInstName());
            HashMap proxyResultMap = proxy.executeQuery(whereMap, map);
            System.out.println("!@!@ proxyResultMap : " + proxyResultMap);
            if (proxyResultMap != null && proxyResultMap.size() > 0) {
                tableList = (ArrayList) proxyResultMap.get("RECOVERY_LIST_TABLE_DATA");
                setFinalList(tableList);
            }
            _heading = null;
            setTblModel(_table, tableList, tableTitle);
            _table.revalidate();

            if (_table.getModel() instanceof TableSorter) {
                tblSalaryRecoveryList = ((TableSorter) _table.getModel()).getModel();
            } else {
                tblSalaryRecoveryList = (TableModel) _table.getModel();
            }
            return tableTitle;
        } catch (Exception e) {

            e.printStackTrace();
            parseException.logException(e, true);
        }
        return tableTitle;
    }
    public ArrayList insertProcessTableData(HashMap paraMap, CTable _table) {
        _heading = null;
        _tblData = _table;
        try {
            javax.swing.table.DefaultTableModel tblModel = null;
            if (_table.getModel() instanceof TableSorter) {
                tblLoanSalaryRecoveryList = ((TableSorter) _table.getModel()).getModel();
            } else if (_table.getModel() instanceof TableModel) {
                tblLoanSalaryRecoveryList = (TableModel) _table.getModel();
            } else {
                tblModel = (javax.swing.table.DefaultTableModel) _table.getModel();
            }

            while (tblLoanSalaryRecoveryList != null && tblLoanSalaryRecoveryList.getRowCount() > 0&& _table.getRowCount() > 0) {
                tblLoanSalaryRecoveryList.removeRow(0);
            }
            while (tblModel != null && _table.getRowCount() > 0) {
                tblModel.removeRow(0);
            }
            List recoveryList = null;
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            HashMap whereMap = new HashMap();
            HashMap resultMap = new HashMap();
            whereMap.put("RECOVERY_ID", paraMap.get("RECOVERY_ID"));
            whereMap.put("PROD_TYPE", paraMap.get("PROD_TYPE"));
            recoveryList = ClientUtil.executeQuery(CommonUtil.convertObjToStr(paraMap.get(CommonConstants.MAP_NAME)), whereMap);
            Iterator iterator = null;
            if (recoveryList != null && recoveryList.size() > 0) {
                resultMap = (HashMap) recoveryList.get(0);
                iterator = resultMap.keySet().iterator();
            } else {
                ClientUtil.showMessageWindow("No Data..");
                return null;
            }
            _heading = null;
            //   setTblModel(_table, null, _heading);
            if (_heading == null) {
                _heading = new ArrayList();
                _heading.add("Select");
                sizeList = new ArrayList();
                if (isMultiSelect) {
                    sizeList.add(new Integer(6));
                }
                String head = "";
                while (iterator.hasNext()) {
                    head = (String) iterator.next();
                    sizeList.add(new Integer(head.length()));
                    _heading.add(head);
                }
            }
            List creditBalAcctList = new ArrayList();
            if (recoveryList != null && recoveryList.size() > 0) {
                for (int i = 0; i < recoveryList.size(); i++) {
                    ArrayList newList = new ArrayList();
                    resultMap = (HashMap) recoveryList.get(i);
                    newList.add(new Boolean(false));
                    double closeAmt = 0.0;                    
                    String acctNo = CommonUtil.convertObjToStr(resultMap.get("ACT_NUM"));
                    double recoveredAmount = CommonUtil.convertObjToDouble(resultMap.get("RECOVERED_AMOUNT"));
                    HashMap statusMap = new HashMap();
                    statusMap.put("ACT_NUM",acctNo);
                    statusMap.put("ASON",currDate.clone());
                    List statusList = ClientUtil.executeQuery("getLoanRecoveryCurrentStatus", statusMap);
                    if(statusList != null && statusList.size() > 0){
                        statusMap = (HashMap)statusList.get(0);
                        closeAmt = CommonUtil.convertObjToDouble(statusMap.get("TOTAL_RECOVERED_AMOUNT"));
                         if(closeAmt < recoveredAmount){
                           resultMap.put("RECOVERED_AMOUNT",closeAmt);
                           resultMap.put("PRINCIPAL",statusMap.get("PRINCIPAL"));
                           resultMap.put("INTEREST",statusMap.get("INTEREST"));
                           resultMap.put("PENAL",statusMap.get("PENAL"));
                           resultMap.put("CHARGES",statusMap.get("CHARGES"));  
                           creditBalAcctList.add(acctNo);
                         }
                    }
                    
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("RECOVERY_ID")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("PROD_ID")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("ACT_NUM")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("MEMBER_NAME")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("SCHEME_NAME")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("TOTAL_DEMAND")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("PRINCIPAL")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("INTEREST")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("PENAL")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("CHARGES")));
                    //newList.add(CommonUtil.convertObjToStr(resultMap.get("TOTAL_DEMAND")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("RECOVERED_AMOUNT")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("DIVISION")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("TOTAL_RECOVERY_AMT")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
                    tableList.add(newList);
                }
            }
            if(creditBalAcctList != null && creditBalAcctList.size() > 0){
                setColourList(creditBalAcctList);
            }
            setTblModel(_table, tableList, _heading);
            _table.revalidate();

            if (_table.getModel() instanceof TableSorter) {
                tblLoanSalaryRecoveryList = ((TableSorter) _table.getModel()).getModel();
            } else {
                tblLoanSalaryRecoveryList = (TableModel) _table.getModel();
            }
            return _heading;
        } catch (Exception e) {

            e.printStackTrace();
            parseException.logException(e, true);
        }
        return _heading;
    }

    public void setTblModel(final CTable tbl, ArrayList tblData, ArrayList head) {
        TableSorter tableSorter = new TableSorter();
        TableModel tableModel = new TableModel(tblData, head) {

            public boolean isCellEditable(int rowIndex, int mColIndex) {

                if (mColIndex == 0 || mColIndex == 12) {
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

     public void setMDSTblModel(final CTable tbl, ArrayList tblData, ArrayList head) {
        TableSorter tableSorter = new TableSorter();
        TableModel tableModel = new TableModel(tblData, head) {

            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if(mColIndex == 0){
                    return true;
                }
                
                if ( mColIndex == 11) {
                    if(tbl.getValueAt(rowIndex, 0).equals(true)){
                        return true;
                    }else{
                        return false;
                    }
                    
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
     
     public void setProcessListTblModel(final CTable tbl, ArrayList tblData, ArrayList head) {
        TableSorter tableSorter = new TableSorter();
        TableModel tableModel = new TableModel(tblData, head) {

            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if(mColIndex == 0){
                    return true;
                }
                
                if ( mColIndex == 4) {
                    if(tbl.getValueAt(rowIndex, 0).equals(true)){
                        return true;
                    }else{
                        return false;
                    }
                    
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
     
     

     public void setRDTblModel(final CTable tbl, ArrayList tblData, ArrayList head) {
        TableSorter tableSorter = new TableSorter();
        TableModel tableModel = new TableModel(tblData, head) {

            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if(mColIndex == 0){
                    return true;
                }
                
                if ( mColIndex == 10) {
                    if(tbl.getValueAt(rowIndex, 0).equals(true)){
                        return true;
                    }else{
                        return false;
                    }
                    
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
    
    public void resetForm() {
        resetTableValues();
        setColourList(null);
        setChanged();
    }

    public void resetTableValues() {
        if (tblSalaryRecoveryList.getDataArrayList().size() > 0) {
            tblSalaryRecoveryList.setDataArrayList(null, tableTitle);
        }
    }

    public void viewTblData(ArrayList viewtblList) {
        tblSalaryRecoveryList = new TableModel((ArrayList) viewtblList, tableTitle);
    }

    public HashMap getFinalMap() {
        return finalMap;
    }

    public void setFinalMap(HashMap finalMap) {
        this.finalMap = finalMap;
    }

    /**
     * Retrives data and populates the CTable using TableModel
     *
     * @param mapID HashMap used to retrive data from DB
     * @param tblData CTable object used to update the table with TableModel
     * @return Returns ArrayList for populating Search Combobox
     */
    public Date getProperDateFormat(Object obj) {
        Date currDate = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate = (Date) currDate.clone();
            currDate.setDate(tempDt.getDate());
            currDate.setMonth(tempDt.getMonth());
            currDate.setYear(tempDt.getYear());
        }
        return currDate;
    }
    private Date getProperDate(Date sourceDate) {
        Date targetDate = (Date) currDate.clone();
        targetDate.setDate(sourceDate.getDate());
        targetDate.setMonth(sourceDate.getMonth());
        targetDate.setYear(sourceDate.getYear());
        return targetDate;
    }
    public String getRoundVal(double val) {
        String retVal = "";
        try {
            Float val1 = new Float(Math.round(val));
            retVal = CommonUtil.convertObjToStr(val1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public void doActionPerform() {
        try {
            final HashMap data = new HashMap();
            if (getFinalMap() != null && getFinalMap().size() > 0) {
                if(getRecoveryProdType().equals("TL")){
                data.put("RECOVERY_PROCESS_LIST", getFinalMap());
                }else if(getRecoveryProdType().equals("RD")){
                  data.put("RECOVERY_RD_PROCESS_LIST", getFinalMap());
                }if(getRecoveryProdType().equals("MDS")){
                  data.put("RECOVERY_MDS_PROCESS_LIST", getFinalMap());
                }if(getRecoveryProdType().equals("REC_AMT")){
                  data.put("RECOVERY_AMT_UPDATE_LIST", getFinalMap());
                }
            }
            data.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            data.put("CALC_INT_UPTO_DT", DateUtil.getDateMMDDYYYY(getTdtCalcIntUpto()));
            if (getNewTransactionMap() != null) {
                data.put("TRANSACTION_DETAILS_DATA", getNewTransactionMap());
            }
            data.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            data.put("RECOVERY_ID", getTxtrecoveryId());
            System.out.println("Data in RecoveryList Generation OB : " + data + "map is" + map);
            HashMap proxyResultMap = proxy.execute(data, map);
            finalMap = null;
            System.out.println("##################### proxyResultMap : " + proxyResultMap);
            setProxyReturnMap(proxyResultMap);
            // setResult(getActionType());
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(LoanRecoveryOB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Getter for property cbmProdId.
     *
     * @return Value of property cbmProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }
    
    
     public ArrayList insertMDSProcessTableData(HashMap paraMap, CTable _table) {
        _heading = null;
        _tblData = _table;
        try {
            javax.swing.table.DefaultTableModel tblModel = null;
            if (_table.getModel() instanceof TableSorter) {
                tblSalaryMDSRecoveryList = ((TableSorter) _table.getModel()).getModel();
            } else if (_table.getModel() instanceof TableModel) {
                tblSalaryMDSRecoveryList = (TableModel) _table.getModel();
            } else {
                tblModel = (javax.swing.table.DefaultTableModel) _table.getModel();
            }

            while (tblSalaryMDSRecoveryList != null && tblSalaryMDSRecoveryList.getRowCount() > 0 && _table.getRowCount() > 0) {
                tblSalaryMDSRecoveryList.removeRow(0);
            }
            while (tblModel != null && _table.getRowCount() > 0) {
                tblModel.removeRow(0);
            }
            List recoveryList = null;
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            HashMap whereMap = new HashMap();
            HashMap resultMap = new HashMap();
            whereMap.put("RECOVERY_ID", paraMap.get("RECOVERY_ID"));
            whereMap.put("PROD_TYPE", paraMap.get("PROD_TYPE"));
            recoveryList = ClientUtil.executeQuery(CommonUtil.convertObjToStr(paraMap.get(CommonConstants.MAP_NAME)), whereMap);
            Iterator iterator = null;
            if (recoveryList != null && recoveryList.size() > 0) {
                resultMap = (HashMap) recoveryList.get(0);
                iterator = resultMap.keySet().iterator();
            } else {
                ClientUtil.showMessageWindow("No Data..");
                return null;
            }
            _heading = null;
            //   setTblModel(_table, null, _heading);
            if (_heading == null) {
                _heading = new ArrayList();
                _heading.add("Select");
                sizeList = new ArrayList();
                if (isMultiSelect) {
                    sizeList.add(new Integer(6));
                }
                String head = "";
                while (iterator.hasNext()) {
                    head = (String) iterator.next();
                    sizeList.add(new Integer(head.length()));
                    _heading.add(head);
                }
            }

            if (recoveryList != null && recoveryList.size() > 0) {
                for (int i = 0; i < recoveryList.size(); i++) {
                    ArrayList newList = new ArrayList();
                    resultMap = (HashMap) recoveryList.get(i);
                    newList.add(new Boolean(false));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("RECOVERY_ID")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("PROD_ID")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("ACT_NUM")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("MEMBER_NAME")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("SCHEME_NAME")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("INST_NO")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("TOTAL_DEMAND")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("PRINCIPAL")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("INTEREST")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("PENAL")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("CHARGES")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("MDS_BONUS")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("FORFEIT_BONUS")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("TOTAL_DEMAND")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("DIVISION")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("TOTAL_RECOVERY_AMT")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
                    tableList.add(newList);
                }
            }
            setMDSTblModel(_table, tableList, _heading);
            _table.revalidate();

            if (_table.getModel() instanceof TableSorter) {
                tblSalaryMDSRecoveryList = ((TableSorter) _table.getModel()).getModel();
            } else {
                tblSalaryMDSRecoveryList = (TableModel) _table.getModel();
            }
            System.out.println("tblSalaryMDSRecoveryList count :: " + tblSalaryMDSRecoveryList.getRowCount());
            return _heading;
        } catch (Exception e) {

            e.printStackTrace();
            parseException.logException(e, true);
        }
        return _heading;
    }
    
     
     public ArrayList insertRDProcessTableData(HashMap paraMap, CTable _table) {
        _heading = null;
        _tblData = _table;
        try {
            javax.swing.table.DefaultTableModel tblModel = null;
            if (_table.getModel() instanceof TableSorter) {
                tblSalaryRDRecoveryList = ((TableSorter) _table.getModel()).getModel();
            } else if (_table.getModel() instanceof TableModel) {
                tblSalaryRDRecoveryList = (TableModel) _table.getModel();
            } else {
                tblModel = (javax.swing.table.DefaultTableModel) _table.getModel();
            }

            while (tblSalaryRDRecoveryList != null && tblSalaryRDRecoveryList.getRowCount() > 0 && _table.getRowCount() > 0) {
                tblSalaryRDRecoveryList.removeRow(0);
            }
            while (tblModel != null && _table.getRowCount() > 0) {
                tblModel.removeRow(0);
            }
            List recoveryList = null;
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            HashMap whereMap = new HashMap();
            HashMap resultMap = new HashMap();
            whereMap.put("RECOVERY_ID", paraMap.get("RECOVERY_ID"));
            whereMap.put("PROD_TYPE", paraMap.get("PROD_TYPE"));
            recoveryList = ClientUtil.executeQuery(CommonUtil.convertObjToStr(paraMap.get(CommonConstants.MAP_NAME)), whereMap);
            Iterator iterator = null;
            if (recoveryList != null && recoveryList.size() > 0) {
                resultMap = (HashMap) recoveryList.get(0);
                iterator = resultMap.keySet().iterator();
            } else {
                ClientUtil.showMessageWindow("No Data..");
                return null;
            }
            _heading = null;
            //   setTblModel(_table, null, _heading);
            if (_heading == null) {
                _heading = new ArrayList();
                _heading.add("Select");
                sizeList = new ArrayList();
                if (isMultiSelect) {
                    sizeList.add(new Integer(6));
                }
                String head = "";
                while (iterator.hasNext()) {
                    head = (String) iterator.next();
                    sizeList.add(new Integer(head.length()));
                    _heading.add(head);
                }
            }

            if (recoveryList != null && recoveryList.size() > 0) {
                for (int i = 0; i < recoveryList.size(); i++) {
                    ArrayList newList = new ArrayList();
                    resultMap = (HashMap) recoveryList.get(i);
                    newList.add(new Boolean(false));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("RECOVERY_ID")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("PROD_ID")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("ACT_NUM")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("MEMBER_NAME")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("INST_NO")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("TOTAL_DEMAND")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("PRINCIPAL")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("INTEREST")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("PENAL")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("CHARGES")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("TOTAL_DEMAND")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("DIVISION")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("TOTAL_RECOVERY_AMT")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
                    tableList.add(newList);
                }
            }
            setRDTblModel(_table, tableList, _heading);
            _table.revalidate();

            if (_table.getModel() instanceof TableSorter) {
                tblSalaryRDRecoveryList = ((TableSorter) _table.getModel()).getModel();
            } else {
                tblSalaryRDRecoveryList = (TableModel) _table.getModel();
            }
            return _heading;
        } catch (Exception e) {

            e.printStackTrace();
            parseException.logException(e, true);
        }
        return _heading;
    }

    public String getRecoveryProdType() {
        return recoveryProdType;
    }

    public void setRecoveryProdType(String recoveryProdType) {
        this.recoveryProdType = recoveryProdType;
    }
    
     public ArrayList insertAllProcessTableData(HashMap paraMap, CTable _table) {
        _heading = null;
        _tblData = _table;
        try {
            javax.swing.table.DefaultTableModel tblModel = null;
            if (_table.getModel() instanceof TableSorter) {
                 tblProcessListSalaryRecoveryList = ((TableSorter) _table.getModel()).getModel();
            } else if (_table.getModel() instanceof TableModel) {
                tblProcessListSalaryRecoveryList = (TableModel) _table.getModel();
            } else {
                tblModel = (javax.swing.table.DefaultTableModel) _table.getModel();
            }

            while (tblProcessListSalaryRecoveryList != null && tblProcessListSalaryRecoveryList.getRowCount() > 0 && _table.getRowCount() > 0) {
                tblProcessListSalaryRecoveryList.removeRow(0);
            }
            while (tblModel != null && _table.getRowCount() > 0) {
                tblModel.removeRow(0);
            }
            List recoveryList = null;
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            HashMap whereMap = new HashMap();
            HashMap resultMap = new HashMap();
            whereMap.put("RECOVERY_ID", paraMap.get("RECOVERY_ID"));
            whereMap.put("PROD_TYPE", paraMap.get("PROD_TYPE"));
            recoveryList = ClientUtil.executeQuery(CommonUtil.convertObjToStr(paraMap.get(CommonConstants.MAP_NAME)), whereMap);
            System.out.println("recoveryList here :: " + recoveryList);
            Iterator iterator = null;
            if (recoveryList != null && recoveryList.size() > 0) {
                resultMap = (HashMap) recoveryList.get(0);
                iterator = resultMap.keySet().iterator();
            } else {
                ClientUtil.showMessageWindow("No Data..");
                return null;
            }
            _heading = null;
            //   setTblModel(_table, null, _heading);
            if (_heading == null) {
                _heading = new ArrayList();
                _heading.add("Select");
                sizeList = new ArrayList();
                if (isMultiSelect) {
                    sizeList.add(new Integer(6));
                }
                String head = "";
                while (iterator.hasNext()) {
                    head = (String) iterator.next();
                    sizeList.add(new Integer(head.length()));
                    _heading.add(head);
                }
            }

            if (recoveryList != null && recoveryList.size() > 0) {
                for (int i = 0; i < recoveryList.size(); i++) {
                    ArrayList newList = new ArrayList();
                    resultMap = (HashMap) recoveryList.get(i);
                    newList.add(new Boolean(true));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("CUSTOMER ID")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("MEMBER NO")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("NAME")));
                    newList.add(CommonUtil.convertObjToStr(resultMap.get("RECOVERY AMOUNT")));
                    tableList.add(newList);
                }
            }
            System.out.println("tableList here :: " + tableList);
            setProcessListTblModel(_table, tableList, _heading);
            _table.revalidate();

            if (_table.getModel() instanceof TableSorter) {
                tblProcessListSalaryRecoveryList = ((TableSorter) _table.getModel()).getModel();
            } else {
                tblProcessListSalaryRecoveryList = (TableModel) _table.getModel();
            }
            System.out.println("tblProcessListSalaryRecoveryList count :: " + tblProcessListSalaryRecoveryList.getRowCount());
            return _heading;
        } catch (Exception e) {

            e.printStackTrace();
            parseException.logException(e, true);
        }
        return _heading;
    }

    public List getColourList() {
        return colourList;
    }

    public void setColourList(List colourList) {
        this.colourList = colourList;
    }

  
   }