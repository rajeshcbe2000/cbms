/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DepositReprintOB.java
 *
 * Created on September 12, 2011, 12:27 PM
 */

package com.see.truetransact.ui.deposit.print;
import com.see.truetransact.ui.transaction.cash.*;
import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.borrowings.master.BorrowingsTO;
import com.see.truetransact.transferobject.borrowings.master.BorrowingsChequeTO;
import com.ibatis.db.sqlmap.SqlMap;
import java.util.Date;
import com.see.truetransact.clientutil.EnhancedTableModel;
import java.util.LinkedHashMap;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.transaction.TransactionOB ; //trans details
import com.see.truetransact.uicomponent.CTable;
/**
 *
 * @author  user
 */

public class DepositReprintOB extends CObservable{
    final ArrayList tableTitle = new ArrayList();
    private static SqlMap sqlMap = null;
     private int dataSize;

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }
  private HashMap dataHash;
     private Date currDt=null; //trans details
    private CTable _tblData;
    private ArrayList data;
     private ArrayList _heading;
    private final static Logger log = Logger.getLogger(DepositReprintOB.class);//Creating Instace of Log
    private ProxyFactory proxy;
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private static DepositReprintOB objCashierApprovalOB;//Singleton Object Reference
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private ArrayList key,value;//ArrayList for populating the ComboBoxModel key-Storing keys value-for Storing Values
    private HashMap keyValue;//Which holds the resultset values as key,value pairs for populatine ComboBoxModel
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private EnhancedTableModel tblReciept;
    private EnhancedTableModel tblPayment;
    public EnhancedTableModel getTblReciept() {
        return tblReciept;
    }

    public void setTblReciept(EnhancedTableModel tblReciept) {
        this.tblReciept = tblReciept;
    }
      
    
    /** Creates a new instance of NewBorrowingOB */
    public DepositReprintOB() {
         try {
             setTableTitle();
            currDt=ClientUtil.getCurrentDate(); //trans details
            
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            }catch(Exception e){
            //parseException.logException(e,true);
                 System.out.println("Error in NewBorrowingOB():"+e);
        }
    }
     static {
        try {
            log.info("Creating ParameterOB...");
            objCashierApprovalOB = new DepositReprintOB();
        } catch(Exception e) {
           // parseException.logException(e,true);
               System.out.println("Error in static():"+e);
        }
    }

    private void setTableTitle() {
        tableTitle.add("Select");
        tableTitle.add("Deposit No");
        tableTitle.add("Behaviour");
        tableTitle.add("Name");
        tableTitle.add("Amount");
        tableTitle.add("Deposit Date");
        tableTitle.add("Maturity Date");

    }
     // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "CashierApprovalJNDI");
        map.put(CommonConstants.HOME, "cashierApproval.CashierApprovalHome");
        map.put(CommonConstants.REMOTE, "cashierApproval.CashierApproval");
    }
      public void populateData(HashMap mapID, CTable tblData) {
        _tblData = tblData;
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
        dataHash = ClientUtil.executeTableQuery(mapID);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        System.out.println("_heading===" + _heading);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
          System.out.println("Datas :"+data.size());
        ArrayList tblDatanew = new ArrayList();
        for (int i = 0; i <=data.size()-1; i++) {
            List tmpList = (List) data.get(i);
            ArrayList newList = new ArrayList();
            newList.add(false);
            newList.add(tmpList.get(0));
            newList.add(tmpList.get(1));
            newList.add(tmpList.get(2));
            newList.add(tmpList.get(3));
            newList.add(tmpList.get(4));
            newList.add(tmpList.get(5));
            tblDatanew.add(newList);
        }
        tblReciept = new EnhancedTableModel((ArrayList) tblDatanew, tableTitle);
        setTblReciept(tblReciept);
        setDataSize(data.size()); 
    } 
 
     public static DepositReprintOB getInstance()throws Exception{
        return objCashierApprovalOB;
    }
      public int getActionType(){
        return _actionType;
    }
    
    public void setActionType(int actionType) {
        _actionType = actionType;
     }
     
    
    
     public void populateData(HashMap whereMap) {      
    }
      public void setResult(int result) {
        _result = result;
        setChanged();
    }
 
      public int getResult(){
        return _result;
    }
    
        /** Resets all the UI Fields */
    public void resetForm(){
       
    }
   
         /* Executes Query using the TO object */
    public void execute(HashMap term) {
        try {
              HashMap proxyReturnMap = proxy.execute(term, map);
              System.out.println("proxyy111>>>>>>>==="+proxyReturnMap);
            setProxyReturnMap(proxyReturnMap);
        //end..
            setResult(getActionType());
       } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
              System.out.println("Error in execute():"+e);
        }
    }
   
}
