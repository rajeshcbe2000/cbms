/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TableManipulation.java
 *
 * Created on June 29, 2004, 11:36 AM
 */

package com.see.truetransact.ui.deposit;

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
import com.see.truetransact.uicomponent.COptionPane;
import java.util.Observable;

/**
 * Class used for doing CTable Operations like displaying it in CTable,
 * checking for duplication, Deleteing the data depending on certain conditions,
 * setting the final data in the HashMap etc.
 *
 */
public class TableManipulation extends Observable {
    
    /** Creates a new instance of TableManipulation */
    public TableManipulation() {
    }
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final int YES = 0;
    private final int NO = 1;
    private final int CANCEL = 2;
    private final String FLD_SL_NO = "SlNo";
    private final String FLD_STATUS = "Status";
    private final String FLD_FOR_DB_YES_NO = "DBYesOrNo";
    private final String YES_FULL_STR = "Yes";
    private final String NO_FULL_STR = "No";
    private final String DIALOG_YES = "Yes";
    private final String DIALOG_NO = "No";
    private final String WARN_MESSAGE = "This Record Already exist. Do you want to replace it?";
    private HashMap allValues;
    private EnhancedTableModel tblModel;
    private ArrayList rowValues;
    private ArrayList tblColumn;
    private int rowCount;
    private int retIntInChkForDuplication;
    private int getSelectedRowCount;
    private int rowDel;
    private int modifiedSlNo;
    private int mode;
    private int slNo;
    private int count;
    
    /** Used to display the Datas in the CTable   
     * @param count ---> int data type, the count
     * @param mode ---> int data type, the mode of operation, either New or Modification
     * if 0, it is New else if 1, it is in Modification mode.
     * @param rowDel ---> int data type, the rowDel
     * @param getSelectedRowCount ---> int data type, the Selected record's Row count
     * @param rowCount ---> int data type, the rowCount
     * @param allValues ---> HashMap Object, the overall data in it's key-value pair
     * @param newValues ---> HashMap Object, the new data in it's key-value pair
     * @param tblModel ---> EnhancedTableModel object, the Table Model of the CTable
     * @param tblColumn ---> ArrayList Object, the column headers of the CTable
     * @param changedRowValues ---> ArrayList Object, the Arraylist containing the data for CTable
     * @param status ---> String Object, the status of the record
     * @return retHash ---> HashMap object, HashMap containing count, allValues, tblModel, rowDel
     *  etc after Manipulations
     */
    public HashMap populateTbl(int count, int mode, int rowDel, int getSelectedRowCount, int rowCount, HashMap allValues, HashMap newValues, EnhancedTableModel tblModel, ArrayList tblColumn, ArrayList changedRowValues, String status){
        HashMap retHash = new HashMap();
        setTbl(tblModel);
        setAllValues(allValues);
        setRowDel(rowDel);
        setRowCount(rowCount);
        setCount(count);
        setTblColumn(tblColumn);
        setGetSelectedRowCount(getSelectedRowCount);
        try{
            if(mode == 0){
                //--- Enters if a New Record is to be saved
                retIntInChkForDuplication=checkForDuplication(mode, getRowDel(), newValues);
                if(retIntInChkForDuplication == CANCEL){
                    newModePopulateTbl(false, changedRowValues, count, newValues, getAllValues());
                } else if(retIntInChkForDuplication==YES){
                    delTab(getRowCount(), false, status, getAllValues(), getCount(), getRowDel(), getGetSelectedRowCount(), getTbl());
                    newModePopulateTbl(true,changedRowValues, getCount(), newValues, getAllValues());
                }
            } else if(mode == 1){
                //--- Enters if Modification of existing record is taking place
                retIntInChkForDuplication=checkForDuplication(mode, getRowDel(), newValues);
                if(retIntInChkForDuplication==CANCEL){
                    editModePopulateTbl(changedRowValues, newValues, getAllValues());
                } else if(retIntInChkForDuplication==YES){
                    test(getGetSelectedRowCount(), getRowCount(),getAllValues(), changedRowValues, newValues, getRowDel(), status);
                }
            }
            retHash.put("COUNT" , String.valueOf(getCount()));
            retHash.put("INT_CHECK", String.valueOf(retIntInChkForDuplication));
            retHash.put("ROW_DEL" , String.valueOf(getRowDel()));
            retHash.put("GET_SELECTED_ROW_COUNT" , String.valueOf(getGetSelectedRowCount()));
            retHash.put("ROW_COUNT" , String.valueOf(getRowCount()));
            retHash.put("ALL_VALUES", getAllValues());
            retHash.put("TABLE_MODEL", getTbl());
        } catch(Exception e){
            parseException.logException(e,true);
        }
        return retHash;
    }
    
    //---------------------------------------------------------------------------
    private int checkForDuplication(int mode, int getSelectedRow, HashMap newValues){
        allValues = getAllValues();
        if(allValues == null){
            allValues= new HashMap();
        }
        String newValueDbYesOrNo = CommonUtil.convertObjToStr(newValues.get(FLD_FOR_DB_YES_NO));
        String newValueStatus = CommonUtil.convertObjToStr(newValues.get(FLD_STATUS));
        int j=CANCEL;
        COptionPane duplicationCheck;
        HashMap calcMap;
        int allSize = allValues.size();
        for(int i=0;i<allSize;i++){
            newValues.put(FLD_SL_NO, String.valueOf(i+1));
            //--- For checking the duplication add the Status and DbYesOrNo field
            calcMap = (HashMap)allValues.get(String.valueOf(i));
            newValues.put(FLD_STATUS , CommonUtil.convertObjToStr(calcMap.get(FLD_STATUS)));
            newValues.put(FLD_FOR_DB_YES_NO , CommonUtil.convertObjToStr(calcMap.get(FLD_FOR_DB_YES_NO)));
            if(((i != getSelectedRow) && (mode==1)) || (mode==0)){
                if((allValues.get(String.valueOf(i)).equals(newValues))){
                    duplicationCheck = new COptionPane();
                    String[] options = {DIALOG_YES, DIALOG_NO};
                    j=duplicationCheck.showOptionDialog(null,WARN_MESSAGE,CommonConstants.WARNINGTITLE,COptionPane.YES_NO_OPTION,COptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if(j==YES){
                        setRowCount(i);
                        setGetSelectedRowCount(i);
                    }
                    break;
                } else {
                    j=CANCEL;
                }
            }
        }
        duplicationCheck = null;
        calcMap = null;
        newValues.put(FLD_FOR_DB_YES_NO,newValueDbYesOrNo);
        newValues.put(FLD_STATUS,newValueStatus);
        return j;
    }
    
    //---------------------------------------------------------------------------
    private void editModePopulateTbl(ArrayList changedRowValues, HashMap newValues, HashMap paramAllValues){
        allValues = paramAllValues ;
        HashMap hmap = (HashMap)allValues.get(String.valueOf(allValues.size()-1));
        int n=CommonUtil.convertObjToInt(hmap.get("SlNo"));
        String stat =CommonUtil.convertObjToStr(hmap.get("Status"));
        rowValues = tblModel.getDataArrayList();
        modifiedSlNo = rowDel + 1;
        if(rowValues!=null && rowValues.size()>0) {
            String s= CommonUtil.convertObjToStr(rowValues.get(rowValues.size()-1));

        }
        if(stat.equals("CREATED") || stat.equals("MODIFIED")){
            modifiedSlNo=n;
        }
        changedRowValues.add(0, String.valueOf(modifiedSlNo));
        rowValues.set(getSelectedRowCount, changedRowValues);
        tblModel.setDataArrayList(rowValues, tblColumn);
        allValues.remove(String.valueOf(rowDel));
        newValues.put(FLD_SL_NO, String.valueOf(modifiedSlNo));
        allValues.put(String.valueOf(allValues.size()), newValues);
        setAllValues(allValues);
        setTbl(tblModel);
        rowValues = null;
        newValues = null;
        changedRowValues = null;
    }
    
    //---------------------------------------------------------------------------
    private void newModePopulateTbl(boolean afterReseting, ArrayList changedRowValues, int count, HashMap newValues, HashMap paramAllValues){
        allValues = paramAllValues ;
        if(allValues == null){
            allValues = new HashMap();
        }
        int size=paramAllValues.size();
//        String stat=CommonUtil.convertObjToStr(newValues.get("Status"));
        slNo=count+1;
        changedRowValues.add(0, String.valueOf(slNo));
        tblModel.insertRow(tblModel.getRowCount(),changedRowValues);
        newValues.put(FLD_SL_NO, String.valueOf(slNo));
        allValues.put(String.valueOf(size), newValues);
        count=count+1;
        setCount(count);
        setAllValues(allValues);
        setTbl(tblModel);
        newValues = null;
        changedRowValues = null;
    }
    
    //---------------------------------------------------------------------------
    /** Deletes the Selected Record from the CTable
     * @param delRowCount ---> int data type, the Selected Row that has to be deleted
     * @param deleteKeyPressed ---> boolean data type, Enter whether Delete Button is clicked or not
     * @param status ---> String Object, the status of the record
     * @param paramAllValues ---> HashMap Object, contains the final Data in Key Pair Value
     * @param count ---> int data type, the count
     * @param rowDel ---> int data type, the rowDel
     * @param getSelectedRowCount ---> int data type, the getSelectedRowCount
     * @param tblModel ---> EnhancedTableModel, the tblModel
     */
    public HashMap delTab(int delRowCount , boolean deleteKeyPressed, String status, HashMap paramAllValues, int paramCount, int paramRowDel, int paramGetSelectedRowCount, EnhancedTableModel paramTblModel){
        setCount(paramCount);
        setTbl(paramTblModel);
        setAllValues(paramAllValues);
        setRowDel(paramRowDel);
        setRowCount(delRowCount);
        setGetSelectedRowCount(paramGetSelectedRowCount);
        //--- If the method is called by Pressing the Delete Button,
        //--- then get the RowCount as Row Selected.
        if(deleteKeyPressed==true){
            delRowCount = getRowDel();
        }
        //--- Checks whether data is from Database or not and Does Deletion according to it.
        HashMap calcMap = (HashMap)allValues.get(String.valueOf(delRowCount));
        if(CommonUtil.convertObjToStr(calcMap.get(FLD_FOR_DB_YES_NO)).equals(YES_FULL_STR)){
            calcMap.put(FLD_STATUS, "DELETED");
            allValues.put(String.valueOf(delRowCount),calcMap);
        } else if(CommonUtil.convertObjToStr(calcMap.get(FLD_FOR_DB_YES_NO)).equals(NO_FULL_STR)){
            allValues.remove(String.valueOf(delRowCount));
            count = count - 1;
        }
        //--- Resets the Table after Deleting the required row if it is from DB
        //--- else it removes the data
        if(CommonUtil.convertObjToStr(calcMap.get(FLD_FOR_DB_YES_NO)).equals(NO_FULL_STR)){
            tblModel.removeRow(getSelectedRowCount);
            int j =  getSelectedRowCount;
            HashMap mapCalc;
            int allSize = allValues.size();
            for(int i=delRowCount;i<allSize;i++){
                tblModel.setValueAt(new Integer(i+1), j, 0);
                //--- Resets the HashMap after deleting the required row
                mapCalc =  (HashMap)allValues.get(String.valueOf(i+1));
                mapCalc.put(FLD_SL_NO, String.valueOf(i+1));
                allValues.remove(String.valueOf(i+1));
                allValues.put(String.valueOf(i), mapCalc);
                j=j+1;
            }
            mapCalc = null;
        }else if(CommonUtil.convertObjToStr(calcMap.get(FLD_FOR_DB_YES_NO)).equals(YES_FULL_STR)){
            tblModel.removeRow(getSelectedRowCount);
        }
        setTbl(tblModel);
        setAllValues(allValues);
        setCount(count);
        calcMap = null;
        calcMap = new HashMap();
        calcMap.put("COUNT" , String.valueOf(getCount()));
        calcMap.put("ALL_VALUES", getAllValues());
        calcMap.put("TABLE_MODEL", getTbl());
        return calcMap;
    }
    
    //---------------------------------------------------------------------------
    private void setTbl(EnhancedTableModel tblModel){
        this.tblModel = tblModel;
        setChanged();
    }
    
    private EnhancedTableModel getTbl(){
        return this.tblModel;
    }
    
    private void setTblColumn(ArrayList tblColumn){
        this.tblColumn = tblColumn;
        setChanged();
    }
    
    private ArrayList getTblColumn(){
        return this.tblColumn;
    }
    
    private void setAllValues(HashMap allValues){
        this.allValues = allValues;
        setChanged();
    }
    
    private HashMap getAllValues(){
        return this.allValues;
    }
    
    private void setCount(int count){
        this.count = count;
        setChanged();
    }
    
    private int getCount(){
        return this.count;
    }
    
    private void setRowDel(int rowDel){
        this.rowDel = rowDel;
        setChanged();
    }
    
    private int getRowDel(){
        return this.rowDel;
    }
    
    private void setRowCount(int rowCount){
        this.rowCount = rowCount;
        setChanged();
    }
    
    private int getRowCount(){
        return this.rowCount;
    }
    
    private void setGetSelectedRowCount(int getSelectedRowCount){
        this.getSelectedRowCount = getSelectedRowCount;
        setChanged();
    }
    
    private int getGetSelectedRowCount(){
        return this.getSelectedRowCount;
    }
    
    private void test(int paramGetSelectedRowCount, int delRowCount, HashMap paramAllValues, ArrayList changedRowValues, HashMap newValues, int paramRowDel, String status){
        try{
            setGetSelectedRowCount(paramGetSelectedRowCount);
            setRowDel(paramRowDel);
            setAllValues(paramAllValues);
            HashMap calcMap = (HashMap)allValues.get(String.valueOf(delRowCount));
            if(CommonUtil.convertObjToStr(calcMap.get(FLD_FOR_DB_YES_NO)).equals(NO_FULL_STR)){
                allValues.remove(String.valueOf(delRowCount));
                count = getCount()-1;
                setCount(count);
                tblModel.removeRow(delRowCount);
                int sizeAllVal = allValues.size();
                for(int i=delRowCount;i<sizeAllVal;i++){
                    tblModel.setValueAt(String.valueOf(i+1), i, 0);
                }
                HashMap calcNom = new HashMap();
                int tblModSize = tblModel.getRowCount();
                for(int i=delRowCount;i<tblModSize;i++){
                    calcNom = (HashMap)allValues.get(String.valueOf(i+1));
                    calcNom.put(FLD_SL_NO, String.valueOf(i+1));
                    allValues.remove(String.valueOf(i+1));
                    allValues.put(String.valueOf(i), calcNom);
                }
                newValues.put(FLD_SL_NO, String.valueOf(paramRowDel));
                allValues.put(String.valueOf(paramRowDel-1), newValues);
                changedRowValues.add(0, String.valueOf(paramRowDel));
                rowValues = tblModel.getDataArrayList();
                rowValues.set((paramRowDel-1), changedRowValues);
                tblModel.setDataArrayList(rowValues, tblColumn);
            } else {
                calcMap.put(FLD_STATUS, status);
                allValues.put(String.valueOf(delRowCount),calcMap);
                allValues.put(String.valueOf(paramRowDel),newValues);
            }
            setTbl(tblModel);
            setAllValues(allValues);
            rowValues = null;
            newValues = null;
            changedRowValues = null;
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
