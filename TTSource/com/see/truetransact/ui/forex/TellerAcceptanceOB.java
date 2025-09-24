/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TellerAcceptanceOB.java
 *
 * Created on February 20, 2004, 7:18 PM
 */

package com.see.truetransact.ui.forex;

/**
 *
 * @author  Hemant
 */

import java.util.HashMap;
import java.util.ArrayList;

import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;

public class TellerAcceptanceOB extends java.util.Observable {
    
    private TableModel tbmDetails;
    
    private final static com.see.truetransact.clientexception.ClientParseException parseException = com.see.truetransact.clientexception.ClientParseException.getInstance();
        
        private static TellerAcceptanceOB tellerAcceptanceOB;
        static {
            try {
                //log.info("Creating AccountCreationOB...");
                tellerAcceptanceOB = new TellerAcceptanceOB();
            } catch(Exception e) {
                parseException.logException(e,true);
            }
        }
    /** Creates a new instance of TellerAcceptanceOB */
    public TellerAcceptanceOB() {
        
        setTableModel(); 
    }
    
    public static TellerAcceptanceOB getInstance() {
            return tellerAcceptanceOB;
    }
    
    private void setTableModel(){
        tbmDetails = new com.see.truetransact.clientutil.TableModel(
            
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
    } 
    
    /** Getter for property tbmDetails.
     * @return Value of property tbmDetails.
     *
     */
    public com.see.truetransact.clientutil.TableModel getTbmDetails() {
        return tbmDetails;
    }
    
    /** Setter for property tbmDetails.
     * @param tbmDetails New value of property tbmDetails.
     *
     */
    public void setTbmDetails(com.see.truetransact.clientutil.TableModel tbmDetails) {
        this.tbmDetails = tbmDetails;
    }
    
    public void doAction(){
        
        if (tbmDetails != null) {
            HashMap whereMap;
            String strCnt = "";
            int rows = tbmDetails.getRowCount();
            
            for (int i=0; i < rows; i++) {            
                //strCnt = txtCount[i].getText().trim();
                if (((Boolean)tbmDetails.getValueAt(i,0)).booleanValue()) {
                    whereMap = new HashMap();
                    whereMap.put ("BRANCH_CODE", tbmDetails.getValueAt(i,1));
                    whereMap.put ("USER_ID", tbmDetails.getValueAt(i,2));
                    whereMap.put ("TO_BRANCH_CODE", tbmDetails.getValueAt(i,3));
                    whereMap.put ("TO_USER_ID", tbmDetails.getValueAt(i,4));
                    whereMap.put ("TRANS_DT", tbmDetails.getValueAt(i,5));
                    whereMap.put ("CURRENCY", tbmDetails.getValueAt(i,6));
                    whereMap.put ("DENOMINATION_VALUE", tbmDetails.getValueAt(i,7));
                    whereMap.put ("DENOMINATION_COUNT", tbmDetails.getValueAt(i,8));
                    whereMap.put ("STATUS", "RECEIVED");
                    
                    System.out.println (whereMap);
                    ClientUtil.execute("updateBranchCashTrans", whereMap);
                    
                }
            }
            getData();
        }
    }
    
    public void getData(){
        try{
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.MAP_NAME,"getPendingCashTransfer");
        
            HashMap result = ClientUtil.executeTableQuery(whereMap);
            if(result !=null ){
                addColumn(result);
            }    
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void addColumn(HashMap data) {
        try{
            ArrayList dataList = (ArrayList)data.get(CommonConstants.TABLEDATA);
            ArrayList headList = (ArrayList)data.get(CommonConstants.TABLEHEAD);

            if(dataList!=null){
                System.out.println("Inside");
                final int rows = dataList.size(); 

                com.see.truetransact.clientutil.TableModel tbm = new com.see.truetransact.clientutil.TableModel(){
                     boolean[] canEdit = new boolean [] {
                        true, false, false, false,false,false,false,false,false, false
                     };
                     
                     public boolean isCellEditable(int rowIndex, int columnIndex) {
                            return canEdit [columnIndex];
                     }
                };

                for(int i=0 ;i<rows;i++)
                    ((ArrayList)dataList.get(i)).set(0,Boolean.valueOf(false));
                    
                tbm.setCellEditable(true);
                tbm.setHeading(headList);    
                tbm.setData(dataList);
                tbmDetails = tbm;
            }    


            setChanged();
            notifyObservers();
        }catch(Exception e){
            e.printStackTrace();
        }
    }    
}
