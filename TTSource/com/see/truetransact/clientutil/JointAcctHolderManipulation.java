/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * JointAcctHolderManipulation.java
 *
 * Created on May 19, 2004, 4:29 PM
 */

package com.see.truetransact.clientutil;

/**
 *
 * @author  K.R.Jayakrishnan
 */

import java.util.HashMap;
import java.util.List;
import com.see.truetransact.clientutil.ClientUtil;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import java.util.Observable;

/** This class has different methods for manipulating
 *  the Joint Account Holder's data.
 */
public class JointAcctHolderManipulation extends Observable{
    //--- Declarations for Joint Account Table
    HashMap jntAcctSingleRec;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private EnhancedTableModel tblJointAccnt;
    private ArrayList jntAccntRow;
    private final String FLD_FOR_DB_YES_NO = "DBYesOrNo";
    private final String YES_FULL_STR = "Yes";
    private final String NO_FULL_STR = "No";
    LinkedHashMap jntAcctTOMap;
    //--- End of Declarations for Joint Account Table
    
    /** Creates a new instance of JointAcctHolderManipulation */
    public JointAcctHolderManipulation() {
        
    }
    
    /** This method is used to set the Joint Account Holder Table Model
     * and to add the data in the final data HashMap - "jntAcctAll"
     * @param queryWhereMap, jntAcctAll, tblJointAccnt
     * queryWhereMap --> Hashmap which has the Customer ID value with Key as
     * CUST_ID
     * For ex: HashMap queryWhereMap = new HashMap();
     *         queryWhereMap.put("CUST_ID",custId);
     * jntAcctAll --> Hashmap that has the final data
     * tblJointAccnt --> Enhanced Table Model that has the Joint Account Holder's Table Model
     * @return jntAcctAll ---> HashMap which has the final data after the modifications are done in this method
     */
    public LinkedHashMap populateJointAccntTable(HashMap queryWhereMap, LinkedHashMap jntAcctAll, EnhancedTableModel tblJointAccnt){
        try {
            if(jntAcctAll==null){ //--- If jointAcctAll Hashmap is null, initialize it.
                jntAcctAll = new LinkedHashMap();
            }
            List custListData = ClientUtil.executeQuery("getSelectAccInfoTblDisplay",queryWhereMap);
            HashMap custMapData;
            custMapData = (HashMap) custListData.get(0);
            setJntAcctTableData(custMapData,true,tblJointAccnt);
            String keyCustId = CommonUtil.convertObjToStr(custMapData.get("CUST_ID"));
            /* If there is No Customer Id, insert the all the data with dbYesOrNo having "No" value
             * else, insert the all the data with dbYesOrNo having "Yes" value */
            if(jntAcctAll.get(keyCustId) == null){
                custMapData = insertJntAcctSingleRec(custMapData, NO_FULL_STR);
            } else {
                custMapData = insertJntAcctSingleRec(custMapData, YES_FULL_STR);
            }
            int rowCnt = tblJointAccnt.getRowCount();
            jntAcctAll.put(keyCustId, custMapData);
            setTblJointAccnt(tblJointAccnt);
            custListData = null;
            custMapData=null;
            jntAccntRow = null;
        } catch( Exception e ) {
            parseException.logException(e,true);
        }
        return jntAcctAll;
    }
    
    /** This method is used to set the Joint Account Holder Table Model
     * @param custMapData, jntAcctNewClicked, tblJointAccnt
     * custMapData --> Hashmap which is the result of executing the query "getSelectAccInfoTblDisplay"
     * For ex: List custListData = ClientUtil.executeQuery("getSelectAccInfoTblDisplay",queryWhereMap);
     *         HashMap custMapData;
     *         custMapData = (HashMap) custListData.get(0);
     * jntAcctNewClicked --> Boolean to say whether New Button for the Joint Account Holder is slected or
     * not.
     * tblJointAccnt --> Enhanced Table Model that has the Joint Account Holder's Table Model
     */
    public void setJntAcctTableData(HashMap custMapData, boolean jntAcctNewClicked, EnhancedTableModel tblJointAccnt){
        jntAccntRow = new ArrayList();
        jntAccntRow.add(custMapData.get("NAME"));
        jntAccntRow.add(custMapData.get("CUST_ID"));
        jntAccntRow.add(custMapData.get("CUST_TYPE"));
       if(jntAcctNewClicked==true){ //--- If it is Joint Acct Holder, display as Joint
            jntAccntRow.add("Joint");
            
            //--- To add the Status as Major/Minor
//            if(CommonUtil.convertObjToStr(custMapData.get("MINOR")).equalsIgnoreCase("Y")){
//                jntAccntRow.add("MINOR");
//            }else{
//                 if(CommonUtil.convertObjToStr(custMapData.get("MINOR")).equalsIgnoreCase("N")){
//                    jntAccntRow.add("MAJOR");
//                 } else {
//                     jntAccntRow.add(CommonUtil.convertObjToStr(custMapData.get("MINOR")));
//                 }
//            }
//             ADDED BY NIKHIL
             jntAccntRow.add(custMapData.get("CASTE"));
             jntAccntRow.add(custMapData.get("CUSTOMER_STATUS"));

            tblJointAccnt.addRow(jntAccntRow);
        } else if(jntAcctNewClicked==false){ //--- If it is Main Acct Holder, display as Main
            jntAccntRow.add("Main");
            
            //--- To add the Status as Major/Minor
//            if(CommonUtil.convertObjToStr(custMapData.get("MINOR")).equalsIgnoreCase("Y")){
//                jntAccntRow.add("MINOR");
//            }else{
//                 if(CommonUtil.convertObjToStr(custMapData.get("MINOR")).equalsIgnoreCase("N")){
//                    jntAccntRow.add("MAJOR");
//                 } else {
//                     jntAccntRow.add(CommonUtil.convertObjToStr(custMapData.get("MINOR")));
//                 }
//            }
//             ADDED BY NIKHIL
             jntAccntRow.add(custMapData.get("CASTE"));
             jntAccntRow.add(custMapData.get("CUSTOMER_STATUS"));
             
            if(tblJointAccnt.getRowCount()!=0){ //--- If it has data, remove the first row
                tblJointAccnt.removeRow(0);     //--- to enter the new CustomerId data in it.
            }
         
            tblJointAccnt.insertRow(0,jntAccntRow);
        }
        setTblJointAccnt(tblJointAccnt);
    }
    
    /** This method is used to set the Joint Account Holder Table Model
     * @param tblJointAccnt
     * tblJointAccnt --> Enhanced Table Model that has the Joint Account Holder's Table Model
     */
    void setTblJointAccnt(EnhancedTableModel tblJointAccnt){
        this.tblJointAccnt = tblJointAccnt;
        setChanged();
    }
    
    /** This method is used to get the Joint Account Holder Table Model
     * @return tblJointAccnt
     * tblJointAccnt --> Enhanced Table Model that has the Joint Account Holder's Table Model
     */
    public EnhancedTableModel getTblJointAccnt(){
        return this.tblJointAccnt;
    }
    
    private HashMap insertJntAcctSingleRec(HashMap custMapData, String dbYesOrNo){
        jntAcctSingleRec = new HashMap();
        jntAcctSingleRec.put("CUST_ID",CommonUtil.convertObjToStr(custMapData.get("CUST_ID")));
        jntAcctSingleRec.put(FLD_FOR_DB_YES_NO,dbYesOrNo);
        jntAcctSingleRec.put("STATUS", "CREATED");
        return jntAcctSingleRec;
    }
    
    /** This method is used to move the Joint Account Holder to Main Account Holder
     * @param mainAccntRow, strRowSelected, intRowSelected, tblJointAccnt, jntAcctAll
     * mainAccntRow --> String that contains the Main Customer ID
     *    For Ex. mainAccntRow = CommonUtil.convertObjToStr(tblJointAcctHolder.getValueAt(0, 1))
     * strRowSelected --> String that contains the Joint Acccount Holder's Customer ID
     *    For Ex. strRowSelected = CommonUtil.convertObjToStr(tblJointAcctHolder.getValueAt(tblJointAcctHolder.getSelectedRow(), 1))
     * intRowSelected --> int that has the Selected row
     *    For Ex. intRowSelected = tblJointAcctHolder.getSelectedRow()
     * tblJointAccnt --> Enhanced Table Model that has the Joint Account Holder's Table Model
     * jntAcctAll --> Hashmap that has the final data
     * @return jntAcctAll --> Hashmap that has the final data after manipulation of data
     */
    public LinkedHashMap moveToMain(String mainAccntRow, String strRowSelected , int intRowSelected, EnhancedTableModel tblJointAccnt, LinkedHashMap jntAcctAll){
        HashMap singleRec = (HashMap)jntAcctAll.get(strRowSelected);
        String dbYesOrNo = CommonUtil.convertObjToStr(singleRec.get(FLD_FOR_DB_YES_NO));
        if(dbYesOrNo.equals(NO_FULL_STR)){ //--- If the Record is not from the Database, Delete it.
            jntAcctAll.remove(strRowSelected);
        } else if(dbYesOrNo.equals(YES_FULL_STR)){ //--- else, change the Status as "DELETED"
            singleRec.put("STATUS", "DELETED");
            jntAcctAll.put(strRowSelected,singleRec);
        }
        HashMap custMapData = new HashMap();
        custMapData.put("CUST_ID", mainAccntRow);
        HashMap custMapData1 = insertJntAcctSingleRec(custMapData, NO_FULL_STR);
        jntAcctAll.put(mainAccntRow, custMapData1);
        ArrayList OldMainList = (ArrayList)tblJointAccnt.getDataArrayList().get(0);
        ArrayList newMainList = (ArrayList)tblJointAccnt.getDataArrayList().get(intRowSelected);
        OldMainList.remove(3);
        OldMainList.add(3, "Joint");
        newMainList.remove(3);
        newMainList.add(3, "Main");
        tblJointAccnt.removeRow(0);
        tblJointAccnt.insertRow(0,newMainList);
        tblJointAccnt.removeRow(intRowSelected);
        tblJointAccnt.insertRow(intRowSelected,OldMainList);
        setTblJointAccnt(tblJointAccnt);
        return jntAcctAll;
    }
    
    /** This method is used to delete the Joint Account Holder
     * @param strDelRowCount, intDelRowCount, tblJointAccnt, jntAcctAll
     * strDelRowCount --> String that contains the Joint Account Holder Customer ID
     *    For Ex. strDelRowCount = CommonUtil.convertObjToStr(tblJointAcctHolder.getValueAt(tblJointAcctHolder.getSelectedRow(), 1))
     * intDelRowCount --> int that has the Selected row
     *    For Ex. intDelRowCount = tblJointAcctHolder.getSelectedRow()
     * tblJointAccnt --> Enhanced Table Model that has the Joint Account Holder's Table Model
     * jntAcctAll --> Hashmap that has the final data
     * @return jntAcctAll --> Hashmap that has the final data after manipulation of data
     */
    
    public LinkedHashMap delJointAccntHolder(String strDelRowCount, int intDelRowCount, EnhancedTableModel tblJointAccnt, LinkedHashMap jntAcctAll){
        HashMap singleRec = (HashMap)jntAcctAll.get(strDelRowCount);
        String dbYesOrNo = CommonUtil.convertObjToStr(singleRec.get(FLD_FOR_DB_YES_NO));
        if(dbYesOrNo.equals(NO_FULL_STR)){ //--- If the Record is not from Database, Delete it
            jntAcctAll.remove(strDelRowCount);
        } else if(dbYesOrNo.equals(YES_FULL_STR)){ //--- else, change the Status to "DELETED"
            singleRec.put("STATUS", "DELETED");
            jntAcctAll.put(strDelRowCount,singleRec);
        }
        tblJointAccnt.removeRow(intDelRowCount);
        setTblJointAccnt(tblJointAccnt);
        return jntAcctAll;
    }
    
    
}
