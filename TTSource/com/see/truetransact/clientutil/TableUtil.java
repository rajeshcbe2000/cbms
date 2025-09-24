/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * TableUtil.java
 *
 * Created on February 27, 2004, 4:22 PM
 */


package com.see.truetransact.clientutil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.uicomponent.COptionPane;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Set;
/** TableUtil Class is used to insert a record, delete a record and update a record
 * in CTable
 * @author Shanmugavel
 */
public class TableUtil {
    
    private final static Logger log = Logger.getLogger(TableUtil.class);
    
    private ArrayList tableValues;
    private ArrayList removedValues;
    private LinkedHashMap allValues;
    private String strAttributeKey;
    private final String YES = new String("Yes");
    private final String NO = new String("No");
    private final String WARNING_MESSAGE = new String("This Data Already exist. Do you want to replace it?");
    private final String TABLE_VALUES = "TABLE_VALUES";
    private final String ALL_VALUES = "ALL_VALUES";
    private final String TABLE_RECORD = "TABLE_RECORD";
    private final String ALL_RECORD = "ALL_RECORD";
    private final String OPTION = "OPTION";
    private final String RECORD = "RECORD";
    private final String COMMAND = "COMMAND";
    private final String INSERT = "INSERT";
    private final String UPDATE = "UPDATE";
    private final String DELETE = "DELETE";
    private final int SLNO = 0;
    private String str_MAX_DEL_SL_NO;
    
    /* Creates a new instance of TableUtil */
    public TableUtil() {
        tableValues = new ArrayList();
        removedValues = new ArrayList();
        allValues = new LinkedHashMap();
        strAttributeKey = "";
        str_MAX_DEL_SL_NO = "";
    }
    
    /** To set the CTable value at the time of retrieving from Database
     * @param tableValues To set the CTable Values in the TableUtil
     */
    public void setTableValues(ArrayList tableValues){
        this.tableValues = tableValues;
    }
    
    public ArrayList getTableValues(){
        return this.tableValues;
    }
    
    /** To set the values which are removed in CTable but existing in DataBase
     * @param removedValues contains values which are removed in CTable but existing in DataBase
     */
    public void setRemovedValues(ArrayList removedValues){
        this.removedValues = removedValues;
    }
    
    /** To get the values which are removed in CTable but existing in DataBase
     * @return Returns the ArrayList with values which are removed in CTable but existing in DataBase
     */
    public ArrayList getRemovedValues(){
        return this.removedValues;
    }
    
    /** To set the CTable Values(Both Displayed and not displayed) while retrieving the
     * values from the DataBase
     * @param allValues To set the CTable Values(Both Displayed and not displayed)
     */
    public void setAllValues(LinkedHashMap allValues){
        this.allValues = allValues;
    }
    
    public LinkedHashMap getAllValues(){
        return this.allValues;
    }
    
    /** To set the Attribute key whenever the TableUtil object is created
     * @param strAttributeKey Attribute Key which is used in HashMap
     */
    public void setAttributeKey(String strAttributeKey){
        this.strAttributeKey = strAttributeKey;
    }
    
    public String getAttributeKey(){
        return this.strAttributeKey;
    }
    
    /**
     * To set the maximum serial number from the Deleted records
     * @param str_MAX_DEL_SL_NO The maximum serial number from the Deleted records
     */    
    public void setStr_MAX_DEL_SL_NO(String str_MAX_DEL_SL_NO){
        this.str_MAX_DEL_SL_NO = str_MAX_DEL_SL_NO;
    }
    
    /**
     * To return the maximum serial number from the Deleted records
     * @return the maximum serial number from the Deleted records
     */    
    public String getStr_MAX_DEL_SL_NO(){
        return this.str_MAX_DEL_SL_NO;
    }
    
    /** To insert one Record in Table
     *
     * <CODE>
     *
     *
     * ArrayList oneRecordList = new ArrayList();
     * HashMap oneRecordMap = new HashMap();
     *
     * oneRecordList.add(0, txtSerialNo);
     * oneRecordList.add(1, txtName);
     * oneRecordList.add(2, txtID);
     *
     * oneRecordMap.put("SLNO", txtSerialNO);
     * oneRecordMap.put("NAME", txtName);
     * oneRecordMap.put("ID", txtID);
     *
     * HashMap resultMap = TableUtil.insertTableValues(oneRecordList, oneRecordMap, "ID");
     *
     * ArrayList allRecordList = (ArrayList) resultMap.get(TABLE_VALUES);
     * LinkedHashMap allRecordMap = (LinkedHashMap) resultMap.get(ALL_VALUES);
     *
     * </CODE>
     * @param recToInsertNTable is the Record to insert in Table Values
     * @param recToInsertNAllValues is the Record to insert in All Values
     * @param strPrimaryKey is the Primary Key for the Table
     * @return returnValue Returns HashMap containing Values displayed in CTable, Values (both displayed and not displayed in CTable)
     * and an integer to state whether the record already existing or not
     */
    public HashMap insertTableValues(ArrayList recToInsertNTable, HashMap recToInsertNAllValues, String strPrimaryKey){
        log.info("In insertTableValues(ArrayList, HashMap, String)");
        HashMap returnValue = new HashMap();
        try{
            int option   = -1;
            
            // slNumber is the key for the Record
            int slNumber = 0;
            int x        = -1;
            boolean exist = false;
            HashMap setNullResult;
            HashMap removedRecord;
            setNullResult = setNullValues(recToInsertNAllValues);
            recToInsertNAllValues = (HashMap)setNullResult.get(RECORD);
            String[] options = {YES,NO};
            
            // To have a copy of Attribute value from each record
            String   strAttributeValue = "";
            
            // KeySet of all values
            Set keySet =  getAllValues().keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            
            if (objKeySet.length > 0){
                slNumber = CommonUtil.convertObjToInt(objKeySet[allValues.size() - 1]);
            }
            
            slNumber++;
            
            // To generate the Key by finding the greatest number(Key) from the deleted values as well as in CTable
            for (int i = getRemovedValues().size() - 1,j = 0;i >= 0;--i,++j){
                removedRecord = (HashMap) getRemovedValues().get(j);
                x = CommonUtil.convertObjToInt(removedRecord.get(getAttributeKey()));
                if (x >= slNumber){
                    slNumber = x + 1;
                }
            }
            
            // To generate the Key by finding the greatest number(Key) from the deleted values as well as in CTable
            if (getStr_MAX_DEL_SL_NO().length() != 0){
                x = CommonUtil.convertObjToInt(getStr_MAX_DEL_SL_NO());
                if (x >= slNumber){
                    slNumber = x + 1;
                }
            }
            
            // If the condition is satisfied then there is primary key
            if (strPrimaryKey != null){
                String strPrimaryKeyValue = CommonUtil.convertObjToStr( recToInsertNAllValues.get(strPrimaryKey));
                
                // To have a copy of Command value from each record
                String strCommandValue = "";
                HashMap oneRecord = new HashMap();
                
                // To check whether the strPrimaryKeyValue already exist or not
                for (int i = allValues.size() - 1,j = 0;i >= 0;--i,++j){
                    oneRecord = (HashMap) allValues.get(objKeySet[j]);
                    
                    // If the record already contains the PrimaryKeyValue then it will display the COptionPane
                    if (strPrimaryKeyValue.equals(CommonUtil.convertObjToStr(oneRecord.get(strPrimaryKey)))){
                        option = COptionPane.showOptionDialog(null, WARNING_MESSAGE, CommonConstants.WARNINGTITLE,
                        COptionPane.YES_NO_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                        
                        // To replace the old record
                        if (option == COptionPane.YES_OPTION){
                            recToInsertNAllValues.put(strAttributeKey, objKeySet[j]);
                            allValues.put(objKeySet[j], recToInsertNAllValues);
                            recToInsertNTable.set(SLNO, objKeySet[j]);
                            tableValues.set(j, recToInsertNTable);
                        }
                        exist = true;
                    }
                }
                
                // If the record does not contain that PrimaryKeyValue then insert into TableValues and AllValues
                if (!exist){
                    boolean existInDB = false;
                    
                    // To check whether the Inserted Record is existing in the DataBase and it is deleted in the CTable
                    for (int i = removedValues.size() - 1;i >= 0;--i){
                        oneRecord = (HashMap) removedValues.get(i);
                        strCommandValue   = CommonUtil.convertObjToStr( oneRecord.get(COMMAND));
                        strAttributeValue = CommonUtil.convertObjToStr( oneRecord.get(strAttributeKey));
                        oneRecord.put(COMMAND, null);
                        oneRecord.put(strAttributeKey, CommonUtil.convertObjToStr( recToInsertNAllValues.get(strAttributeKey)));
                        
                        // To find the Record is in the Deleted one or not
                        if (strPrimaryKeyValue.equals(oneRecord.get(strPrimaryKey))){
                            recToInsertNAllValues.put(strAttributeKey, String.valueOf(slNumber));
                            recToInsertNAllValues.put(COMMAND, INSERT);
                            allValues.put(String.valueOf(slNumber), recToInsertNAllValues);
                            existInDB = true;
                            oneRecord.put(COMMAND, strCommandValue);
                            oneRecord.put(strAttributeKey, strAttributeValue);
                            break;
                        }
                        oneRecord.put(COMMAND, strCommandValue);
                        oneRecord.put(strAttributeKey, strAttributeValue);
                    }
                    
                    // If the Record doesnot exist in the DataBase
                    if (!existInDB){
                        recToInsertNAllValues.put(strAttributeKey, String.valueOf(slNumber));
                        recToInsertNAllValues.put(COMMAND, INSERT);
                        allValues.put(String.valueOf(slNumber), recToInsertNAllValues);
                    }
                    recToInsertNTable.set(SLNO, String.valueOf(slNumber));
                    tableValues.add(recToInsertNTable);
                }
            }else{
                // There is no primary key
                HashMap oneRecord = new HashMap();
                
                // To have a copy of Command value from each record
                String strCommandValue = "";
                for (int i = allValues.size() - 1,j = 0;i >= 0;--i,++j){
                    recToInsertNAllValues.put(strAttributeKey, CommonUtil.convertObjToStr((((HashMap)allValues.get(objKeySet[j])).get(String.valueOf(strAttributeKey)))));
                    oneRecord = (HashMap) allValues.get(objKeySet[j]);
                    strCommandValue = CommonUtil.convertObjToStr( oneRecord.get(COMMAND));
                    oneRecord.put(COMMAND, null);
                    
                    // If the record already exists then it will display the COptionPane
                    if (recToInsertNAllValues.equals(oneRecord)){
                        option = COptionPane.showOptionDialog(null, WARNING_MESSAGE, CommonConstants.WARNINGTITLE,
                        COptionPane.YES_NO_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                        exist = true;
                        oneRecord.put(COMMAND, strCommandValue);
                        break;
                    }
                    oneRecord.put(COMMAND, strCommandValue);
                }
                
                // If the record does not exist then insert into TableValues and AllValues
                if (!exist){
                    boolean existInDB = false;
                    
                    // To check whether the Inserted Record is existing in the DataBase and it is deleted in the CTable
                    for (int i = removedValues.size() - 1;i >= 0;--i){
                        oneRecord = (HashMap) removedValues.get(i);
                        strCommandValue   = CommonUtil.convertObjToStr( oneRecord.get(COMMAND));
                        strAttributeValue = CommonUtil.convertObjToStr( oneRecord.get(strAttributeKey));
                        oneRecord.put(COMMAND, null);
                        oneRecord.put(strAttributeKey, CommonUtil.convertObjToStr( recToInsertNAllValues.get(strAttributeKey)));
                        // To find the Record is in the Deleted one or not
                        if (oneRecord.equals(recToInsertNAllValues)){
                            recToInsertNAllValues.put(strAttributeKey, String.valueOf(slNumber));
                            recToInsertNAllValues.put(COMMAND, INSERT);
                            allValues.put(String.valueOf(slNumber), recToInsertNAllValues);
                            oneRecord.put(COMMAND, strCommandValue);
                            oneRecord.put(strAttributeKey, strAttributeValue);
                            existInDB = true;
                            break;
                        }
                        oneRecord.put(COMMAND, strCommandValue);
                        oneRecord.put(strAttributeKey, strAttributeValue);
                    }
                    
                    // If the Record doesnot exist in the DataBase
                    if (!existInDB){
                        recToInsertNAllValues.put(strAttributeKey, String.valueOf(slNumber));
                        recToInsertNAllValues.put(COMMAND, INSERT);
                        allValues.put(String.valueOf(slNumber),recToInsertNAllValues);
                    }
                    
                    recToInsertNTable.set(SLNO, String.valueOf(slNumber));
                    tableValues.add(recToInsertNTable);
                }
            }
            returnValue.put(TABLE_VALUES, tableValues);
            returnValue.put(ALL_VALUES, allValues);
            returnValue.put(OPTION, String.valueOf(option));
            
            setNullResult = null;
            removedRecord = null;
        }catch(Exception e){
            log.info("The error in insertTableValues: "+e);
        }
        return returnValue;
    }
    
    
    /** To insert values in Table(No Primary Key)
     *
     * <CODE>
     *
     *
     * ArrayList oneRecordList = new ArrayList();
     * HashMap oneRecordMap = new HashMap();
     *
     * oneRecordList.add(0, txtSerialNo);
     * oneRecordList.add(1, txtName);
     *
     * oneRecordMap.put("SLNO", txtSerialNO);
     * oneRecordMap.put("NAME", txtName);
     *
     * HashMap resultMap = TableUtil.insertTableValues(oneRecordList, oneRecordMap);
     *
     * ArrayList allRecordList = (ArrayList) resultMap.get(TABLE_VALUES);
     * LinkedHashMap allRecordMap = (LinkedHashMap) resultMap.get(ALL_VALUES);
     *
     * </CODE>
     * @param recToInsertNTable is the Record to insert in Table Values
     * @param recToInsertNAllValues is the Record to insert in All Values
     * @return returnValue Returns HashMap containing Values displayed in CTable, Values (both displayed and not displayed in CTable)
     * and an integer to state whether the record already existing or not
     */
    public HashMap insertTableValues(ArrayList recToInsertNTable, HashMap recToInsertNAllValues){
        log.info("In insertTableValues(ArrayList, HashMap)");
        return insertTableValues(recToInsertNTable, recToInsertNAllValues, null);
    }
    
    
    /** To set null values in the HashMap(Useful at the time of Main Edit)
     * @param obj is the HashMap which is one Record
     * @return result Returns HashMap with one Record
     */
    private HashMap setNullValues(HashMap obj){
        log.info("In setNullValues(HashMap)");
        HashMap result = new HashMap();
        try{
            Set keySet =  obj.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            for (int i = obj.size() - 1;i >= 0;--i){
                if (CommonUtil.convertObjToStr(obj.get(objKeySet[i])).equals("")){
                    obj.put(objKeySet[i], null);
                }
            }
            result.put(RECORD,  obj);
            keySet = null;
            objKeySet = null;
        }catch(Exception e){
            log.info("The error in setNullValues: "+e);
        }
        return result;
    }
    
    
    /** To delete the Table values based on recordPosition
     *
     * <CODE>
     *
     *
     *
     * HashMap result = TableUtil.deleteTableValues(String.valueOf(rowSelected));
     *
     * ArrayList allRecordList = (ArrayList) resultMap.get(TABLE_VALUES);
     * LinkedHashMap allRecordMap = (LinkedHashMap) resultMap.get(ALL_VALUES);
     *
     * </CODE>
     * @return returnValue Returns HashMap containing Values displayed in CTable, Values (both displayed and not displayed in CTable)
     * @param recordPosition indicates the Position of the record in the tableValues ArrayList
     */
    public HashMap deleteTableValues(int recordPosition){
        log.info("In deleteTableValues(String)");
        HashMap returnValue = new HashMap();
        HashMap removedRecord;
        Set keySet =  getAllValues().keySet();
        Object[] objKeySet = (Object[]) keySet.toArray();
        String strRecordKey = "";
        try{
            strRecordKey = CommonUtil.convertObjToStr( ((ArrayList) getTableValues().get(recordPosition)).get(SLNO));
            getTableValues().remove(recordPosition);
            
            for (int i = allValues.size() - 1,j = 0;i >= 0;--i,++j){
                // To check which record is equal with strRecordKey
                if (strRecordKey.equals(CommonUtil.convertObjToStr( ((HashMap) allValues.get(objKeySet[j])).get(strAttributeKey)))){
                    removedRecord = (HashMap) allValues.remove(objKeySet[j]);
                    if (removedRecord.get(COMMAND).equals(UPDATE)){
                        removedRecord.put(COMMAND, DELETE);
                        removedValues.add(removedRecord);
                    }
                    break;
                }
            }
            
            returnValue.put(TABLE_VALUES, tableValues);
            returnValue.put(ALL_VALUES, allValues);
            
            removedRecord = null;
            keySet = null;
            objKeySet = null;
        }catch(Exception e){
            log.info("The error in deleteTableValues: "+e);
        }
        return returnValue;
    }
    
    
    /** To rearrange the records in the CTable(ArrayList)(Not Used Now)
     * @param records records are sent for rearranging the Keys and Values(Serial No)
     * @return Returns ArrayList
     */
    private ArrayList rearrangeRecords(ArrayList records){
        log.info("In rearrangeRecords(ArrayList)");
        try{
            ArrayList eachTabRecs;
            
            for (int i = records.size() - 1,j = 0;i >= 0;--i,++j){
                eachTabRecs = (ArrayList) records.get(j);
                eachTabRecs.set(SLNO, String.valueOf(j+1));
                records.set(j, eachTabRecs);
                eachTabRecs = null;
            }
            eachTabRecs = null;
        }catch(Exception e){
            log.info("The error in rearrangeRecords: "+e);
        }
        return records;
    }
    
    
    /** To rearrange the records in the Table(LinkedHashMap)(Not Used Now)
     * @param records are sent for rearranging the Keys and Values(Serial No)
     * @return Returns LinkedHashMap
     */
    private LinkedHashMap rearrangeRecords(LinkedHashMap records){
        log.info("In rearrangeRecords(LinkedHashMap)");
        try{
            HashMap eachRec;
            Set keySet =  records.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            LinkedHashMap allLocalRecords = new LinkedHashMap();
            
            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
                eachRec = (HashMap) records.get(objKeySet[j]);
                eachRec.put(strAttributeKey, String.valueOf(j+1));
                allLocalRecords.put(String.valueOf(j+1), eachRec);
                eachRec = null;
            }
            
            records.clear();
            records = allLocalRecords;
            
            allLocalRecords = null;
            eachRec = null;
            keySet = null;
        }catch(Exception e){
            log.info("The error in rearrangeRecords: "+e);
        }
        return records;
    }
    
    /** To update the Table values
     *
     * <CODE>
     *
     *
     *
     * ArrayList oneRecordList = new ArrayList();
     * HashMap oneRecordMap = new HashMap();
     *
     * oneRecordList.add(0, txtSerialNo);
     * oneRecordList.add(1, txtName);
     *
     * oneRecordMap.put("SLNO", txtSerialNO);
     * oneRecordMap.put("NAME", txtName);
     *
     * HashMap resultMap = TableUtil.updateTableValues(oneRecordList, oneRecordMap, String.valueOf(rowSelected));
     *
     * ArrayList allRecordList = (ArrayList) resultMap.get(TABLE_VALUES);
     * LinkedHashMap allRecordMap = (LinkedHashMap) resultMap.get(ALL_VALUES);
     *
     * </CODE>
     * @return returnValue Returns HashMap containing Values displayed in CTable, Values (both displayed and not displayed in CTable)
     * and an integer to state whether the record already existing or not
     * @param recordPosition ndicates the Position of the record in the tableValues ArrayList
     * @param recToUpdateNTable is the Record to update in Table Values(ArrayList)
     * @param recToUpdateNAllValues is the Record to update in All Values(HashMap)
     */
    public HashMap updateTableValues(ArrayList recToUpdateNTable, HashMap recToUpdateNAllValues, int recordPosition){
        log.info("In updateTableValues(ArrayList, HashMap, int)");
        HashMap returnValue = new HashMap();
        
        try{
            HashMap setDuplicateResult;
            HashMap setNullResult;
            HashMap oneRecord;
            Set keySet =  getAllValues().keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            String strRecordKey = CommonUtil.convertObjToStr( ((ArrayList) getTableValues().get(recordPosition)).get(SLNO));
            String strRecordKeyCommand = CommonUtil.convertObjToStr((((HashMap) getAllValues().get(strRecordKey)).get(COMMAND)));
            String strDublicateRecordKeyCommand = "";
            String  strCommand;
            String  strFirstRecordSlNo = "";
            String  strSecondRecordSlNo = "";
            ArrayList dublicatetableValues = new ArrayList();
            ArrayList dublicateRecToUpdateNTable = new ArrayList();
            LinkedHashMap dublicateallValues = new LinkedHashMap();
            HashMap dublicateRecToUpdateNAllValues = new HashMap();
            
            int count = 0, option = -1;
            setNullResult = setNullValues(recToUpdateNAllValues);
            
            recToUpdateNAllValues = (HashMap) setNullResult.get(RECORD);
            
            // To have a copy of the record
            setDuplicateResult = setDuplicateRecords(recToUpdateNTable, recToUpdateNAllValues);
            
            dublicateRecToUpdateNTable = (ArrayList) setDuplicateResult.get(TABLE_RECORD);
            dublicateRecToUpdateNAllValues = (HashMap) setDuplicateResult.get(ALL_RECORD);
            
            // To have a copy of Table Values and All Values
            for (int i = tableValues.size() - 1,j = 0;i >= 0;--i,++j){
                dublicatetableValues.add(j, (ArrayList) tableValues.get(j));
                dublicateallValues.put(objKeySet[j], (HashMap)allValues.get(objKeySet[j]));
            }
            
            // To insert the dummy record in the Table Values
            for (int i = dublicatetableValues.size() - 1,j = 0;i >= 0;--i,++j){
                // To check strRcordKey is equal with Key in Table Values(ArrayList)
                if (strRecordKey.equals(CommonUtil.convertObjToStr( ((ArrayList) dublicatetableValues.get(j)).get(SLNO)))){
                    dublicateRecToUpdateNTable.set(SLNO, strRecordKey);
                    dublicatetableValues.set(j, dublicateRecToUpdateNTable);
                }
                
                // TO CHECK WHICH RECORD IN ALLVALUES IS EQUAL WITH strRecordKey
                if (strRecordKey.equals(CommonUtil.convertObjToStr( ((HashMap) dublicateallValues.get(objKeySet[j])).get(strAttributeKey)))){
                    dublicateRecToUpdateNAllValues.put(strAttributeKey, strRecordKey);
                    dublicateallValues.put(strRecordKey, dublicateRecToUpdateNAllValues);
                }
            }
            
            // To check for dublication after update
            for (int i = dublicateallValues.size() - 1,j = 0;i >= 0;--i,++j){
                dublicateRecToUpdateNAllValues.put(strAttributeKey, objKeySet[j]);
                oneRecord = (HashMap) dublicateallValues.get(objKeySet[j]);
                strCommand = CommonUtil.convertObjToStr( oneRecord.get(COMMAND));
                oneRecord.put(COMMAND, null);
                if (dublicateRecToUpdateNAllValues.equals(oneRecord)){
                    count++;
                    if (count == 1){
                        strFirstRecordSlNo = CommonUtil.convertObjToStr(objKeySet[j]);
                    }else if (count == 2){
                        strSecondRecordSlNo = CommonUtil.convertObjToStr(objKeySet[j]);
                        oneRecord.put(COMMAND, strCommand);
                        break;
                    }
                }
                oneRecord.put(COMMAND, strCommand);
            }
            
            // There is a dublicate record
            if (count > 1){
                HashMap deleteResult = new HashMap();
                String[] options = {YES,NO};
                option = COptionPane.showOptionDialog(null, WARNING_MESSAGE, CommonConstants.WARNINGTITLE,
                COptionPane.YES_NO_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                // Record with strRecordKey will be deleted
                if (option == COptionPane.YES_OPTION){
                    strDublicateRecordKeyCommand = CommonUtil.convertObjToStr( (((HashMap) allValues.get(strSecondRecordSlNo)).get(COMMAND)));
                    deleteResult = deleteTableValues(recordPosition);
                    
                    setAllValues((LinkedHashMap) deleteResult.get(ALL_VALUES));
                    if (strRecordKey.equals(strFirstRecordSlNo)){
                        oneRecord = (HashMap) allValues.get(strSecondRecordSlNo);
                        oneRecord.put(COMMAND, strDublicateRecordKeyCommand);
                        allValues.put(strSecondRecordSlNo, oneRecord);
                    }
                    setTableValues((ArrayList) deleteResult.get(TABLE_VALUES));
                }
            }else{
                // No dublicate records.  Record will be updated
                dublicateRecToUpdateNAllValues.put(getAttributeKey(), strRecordKey);
                dublicateRecToUpdateNAllValues.put(COMMAND, strRecordKeyCommand);
                dublicateallValues.put(strRecordKey, dublicateRecToUpdateNAllValues);
                setAllValues(dublicateallValues);
                setTableValues(dublicatetableValues);
            }
            
            returnValue.put(TABLE_VALUES, tableValues);
            returnValue.put(ALL_VALUES, allValues);
            returnValue.put(OPTION, String.valueOf(option));
            
            setDuplicateResult = null;
            setNullResult = null;
            oneRecord = null;
            keySet = null;
            objKeySet = null;
        }catch(Exception e){
            log.info("The error in updateTableValues: "+e);
        }
        return returnValue;
    }
    
    
    /** To have a dublicate copy of the record(Used in updateTableValues)
     * @param arrayListRecord is the ArrayList to have a copy of the Record
     * @param hashMapRecord is HashMap to have a copy to have a copy of the Record
     * @return Returns HashMap containing Values displayed in CTable, Values (both displayed and not displayed in CTable)
     */
    private HashMap setDuplicateRecords(ArrayList arrayListRecord, HashMap hashMapRecord){
        log.info("In setDuplicateRecords(ArrayList, HashMap)");
        HashMap returnValue = new HashMap();
        try{
            ArrayList returnArrayList = new ArrayList();
            HashMap   returnHashMap   = new HashMap();
            Object objObject = new Object();
            Set keySet =  hashMapRecord.keySet();
            Object[] objKeySet = (Object[]) keySet.toArray();
            
            // To set the value for ArrayList
            for (int i = arrayListRecord.size() - 1,j = 0;i >= 0;--i,++j){
                objObject = arrayListRecord.get(j);
                returnArrayList.add(j, objObject);
                objObject = null;
            }
            
            // To set the value for HashMap
            for (int i = hashMapRecord.size() - 1,j = 0;i >= 0;--i,++j){
                objObject = hashMapRecord.get(objKeySet[j]);
                returnHashMap.put(objKeySet[j], objObject);
                objObject = null;
            }
            
            returnValue.put(TABLE_RECORD, returnArrayList);
            returnValue.put(ALL_RECORD, returnHashMap);
            
            keySet = null;
            objKeySet = null;
        }catch(Exception e){
            log.info("The error in setDuplicateRecords: "+e);
        }
        return returnValue;
    }
}
