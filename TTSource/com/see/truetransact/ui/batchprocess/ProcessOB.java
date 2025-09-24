/*Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ProcessOB.java
 *
 * Created on September 15, 2004, 12:20 PM
 */

package com.see.truetransact.ui.batchprocess;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.uicomponent.CCheckBox;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientproxy.ProxyFactory;

import com.see.truetransact.ui.TrueTransactMain;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 *
 * @author shanmuga
 */
public class ProcessOB  extends CObservable{
    private TaskStatus tskStatus;
    private TaskHeader tskHeader;
    
    private java.util.Random random = new java.util.Random();
    private int exeTaskCount;
    private int exeTaskCountUI;
    private ProxyFactory proxy;
    private HashMap map;
    private ArrayList lst;
    private HashMap taskMap;
    private HashMap dataMap = null;
        
    private String transactionType = "";
    private String prodType = "";
    
    private EnhancedTableModel dayTableModel;
    
    private final String START = "STARTED";
    private final String ACCEPTED = "ACCEPTED";
    private final String REJECTED = "REJECTED";
    private final String STARTED = "STARTED";
    private final String COMPLETED = "COMPLETED";
    private final String ERROR = "ERROR";
    private final String NO_DATA = "NO_DATA";
    private final ArrayList dayTabTitle = new ArrayList();
    private final static Logger log = Logger.getLogger(ProcessOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
     private CTable _tblData;
     private ArrayList _heading;
    private HashMap dataHash;
    private ArrayList data;
       private boolean _isAvailable = true;
    /** Creates a new instance of ProcessOB */
    public ProcessOB() {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "SelectAllJNDI");
        map.put(CommonConstants.HOME, "common.viewall.SelectAllHome");
        map.put(CommonConstants.REMOTE, "common.viewall.SelectAll");
        /**/
        
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
        tskStatus = new TaskStatus();
        tskHeader = new TaskHeader();
        exeTaskCount = 0;
        exeTaskCountUI = 0;
        lst = new ArrayList();
        taskMap = new HashMap();
        setDayTabTitle();
        dayTableModel = new EnhancedTableModel(null, getDayTabTitle());
        
    }
    
    private void setDayTabTitle(){
        try{
            dayTabTitle.add("Task");
//            dayTabTitle.add("Actual count");
//            dayTabTitle.add("Execution count");
            dayTabTitle.add("Status");
        }catch(Exception e){
            log.info("Exception caught in setDayBeginTabTitle: "+e);
            parseException.logException(e,true);
        }
    }
    /**
     *
     * @return dayTabTitle which is the Table Title for log table
     */
    public ArrayList getDayTabTitle(){
        return this.dayTabTitle;
    }
    
    /**
     *
     * @param tskStatus set the TaskStatus(used in ThreadPool's updatePool() method)
     */
    public void setTaskStatus(TaskStatus tskStatus){
        this.tskStatus = tskStatus;
        setChanged();
    }
    /**
     *
     * @return the tskStatus
     */
    public TaskStatus getTaskStatus(){
        return this.tskStatus;
    }
    
    /**
     * It will reset the TableModel, taskList(Must be called before setEachTask() method call), exeTaskCountUI
     * to 0
     */
    public void resetAll(){
        dayTableModel = new EnhancedTableModel(null, getDayTabTitle());
        lst = new ArrayList();
        setTaskHeader(null);
        setTaskStatus(null);
        setExeTaskCountUI(0);
        setExeTaskCount(0);
    }
    
    /**
     *
     * @return the tableModel
     */
    public EnhancedTableModel getDayBeginTableModel(){
        return this.dayTableModel;
    }
    
    /**
     *
     * @param tskHeader set the TaskHeader(Used in ThreadPool's updatePool() method)
     */
    public void setTaskHeader(TaskHeader tskHeader){
        this.tskHeader = tskHeader;
        setChanged();
    }
    /**
     *
     * @return the tskHeader
     */
    public TaskHeader getTaskHeader(){
        return this.tskHeader;
    }
    
    public void setExeTaskCount(int exeTaskCount){
        this.exeTaskCount = exeTaskCount;
        setChanged();
    }
    public int getExeTaskCount(){
        return this.exeTaskCount;
    }
    
    /**
     *
     * @param exeTaskCountUI is the number of tasks selected in UI
     */
    public void setExeTaskCountUI(int exeTaskCountUI){
        this.exeTaskCountUI = exeTaskCountUI;
        setChanged();
    }
    /**
     *
     * @return exeTaskCountUI which is the number of tasks selected in UI
     */
    public int getExeTaskCountUI(){
        return this.exeTaskCountUI;
    }
    
    /**
     *
     * @param taskMap has check boxes text as key and the corresponding TaskClass name as value
     */
    public void setTaskMap(HashMap taskMap){
        this.taskMap = taskMap;
        setChanged();
    }
    /**
     *
     * @return taskMap which contains check boxes text as key and corresponding TaskClass name
     * as value
     */
    public HashMap getTaskMap(){
        return this.taskMap;
    }
    
    public void setTaskList(ArrayList lst){
        this.lst = lst;
        setChanged();
    }
    public ArrayList getTaskList(){
        return this.lst;
    }
    
    public void ttNotifyObservers(){
        this.notifyObservers();
    }
    
    public void setTabTask(CCheckBox chkBox){
        ArrayList exeTask = new ArrayList();
        exeTask.add(chkBox.getText());  //Task
        String cnt = String.valueOf(random.nextInt(20));
//        exeTask.add(cnt);               //Actual count
//        exeTask.add(cnt);               //Execution count
        exeTask.add(COMPLETED);         //Status
        dayTableModel.addRow(exeTask);
    }
    
    public void setTabTask(CCheckBox chkBox, String status){
        ArrayList exeTask = new ArrayList();
        exeTask.add(chkBox.getText());  //Task
        String cnt = String.valueOf(random.nextInt(20));
//        exeTask.add(cnt);               //Actual count
//        exeTask.add(cnt);               //Execution count
        exeTask.add(status);         //Status
        dayTableModel.addRow(exeTask);
    }
    void doExecute(ArrayList exeList){
         try {
            HashMap dataMap = new HashMap();
            dataMap.put("EXECUTE_LIST", exeList);
            map.put(CommonConstants.MODULE, getModule());
            map.put(CommonConstants.SCREEN, getScreen());
            map.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            map.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            HashMap proxyResultMap = proxy.execute(dataMap, map);
//            setProxyReturnMap(proxyResultMap);
//            if (proxyResultMap != null && proxyResultMap.containsKey(CommonConstants.TRANS_ID)) {
//    		ClientUtil.showMessageWindow ("Standing Instruction No. : " + CommonUtil.convertObjToStr(proxyResultMap.get(CommonConstants.TRANS_ID)));
//        }
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
    }
    private TaskHeader getTaskFromMap(String value) {
        TaskHeader tskHeader = new TaskHeader();
        try{
        tskHeader.setTaskClass(CommonUtil.convertObjToStr(taskMap.get(value)));
        
        /** To pass the Parameter Map to the Task to be performed... */
        tskHeader.setTaskParam(dataMap);
        tskHeader.setTransactionType(getTransactionType());
        tskHeader.setProductType(getProdType());
        
        
        tskHeader.setBranchID(TrueTransactMain.BRANCH_ID);
        tskHeader.setBankID(TrueTransactMain.BANK_ID);
        tskHeader.setUserID(TrueTransactMain.USER_ID);
        tskHeader.setIpAddr(java.net.InetAddress.getLocalHost().getHostAddress());
        }catch(Exception E){
            System.out.println("Error in Setting the Task Header...");
            E.printStackTrace();
        }
        return tskHeader;
    }
    
    public void setEachTask(CCheckBox chkBox, TaskHeader objTaskHeader){
        ArrayList exeTask = new ArrayList();
        exeTask.add(chkBox.getText());  //Task
//        exeTask.add("0");               //Actual count
//        exeTask.add("0");               //Execution count
        exeTask.add(START);             //Status
        dayTableModel.addRow(exeTask);
        //lst.add(getTaskFromMap(chkBox.getText()));
        lst.add (objTaskHeader);
        ++exeTaskCountUI;
    }

    public void setEachTask(String str, TaskHeader objTaskHeader){
        ArrayList exeTask = new ArrayList();
        exeTask.add(str);  //Task
        exeTask.add(START);
        dayTableModel.addRow(exeTask);
        lst.add (objTaskHeader);
        ++exeTaskCountUI;
    }
        
    public void setEachTask(CCheckBox chkBox){
        ArrayList exeTask = new ArrayList();
        exeTask.add(chkBox.getText());  //Task
//        exeTask.add("0");               //Actual count
//        exeTask.add("0");               //Execution count
        exeTask.add(START);             //Status
        dayTableModel.addRow(exeTask);
        lst.add(getTaskFromMap(chkBox.getText()));
        ++exeTaskCountUI;
    }    
    
    /**
     * To update each row's actual count, executed task count and status
     * @param chkBox
     * @return true to enable all the check boxes when all the tasks are completed
     */
    public boolean updateTaskStatusInLogTable(CCheckBox chkBox){
        String status = "";
        if (tskStatus.getStatus() == 1){
            status = ACCEPTED;
        }else if (tskStatus.getStatus() == 2){
            status = REJECTED;
        }else if (tskStatus.getStatus() == 3){
            status = STARTED;
        }else if (tskStatus.getStatus() == 4){
            status = COMPLETED;
        }else if (tskStatus.getStatus() == 5){
            status = ERROR;
        }else if (tskStatus.getStatus() == 6){
            status = NO_DATA;
        }
        ArrayList exeAllTasks = dayTableModel.getDataArrayList();
        ArrayList task;
        for (int i = exeAllTasks.size() - 1,j = 0;i >= 0;--i,++j){
            task = (ArrayList) exeAllTasks.get(j);
            if (task.get(0).equals(chkBox.getText())){
                if (!((task.get(1).equals(COMPLETED)) || (status.equals(STARTED)) || (task.get(1).equals(REJECTED)) || (task.get(1).equals(ACCEPTED)) || (task.get(1).equals(ERROR)) || (task.get(1).equals(NO_DATA)))){
                    setExeTaskCount(++exeTaskCount);
                }
//                dayTableModel.setValueAt(String.valueOf(tskStatus.getActualCount()), j, 1);
//                dayTableModel.setValueAt(String.valueOf(tskStatus.getExecutionCount()), j, 2);
                dayTableModel.setValueAt(status, j, 1);
                // To check whether the number of tasks selected in UI is
                // equal to the number of finished tasks
                if (getExeTaskCount() == getExeTaskCountUI()){
                    // Flag to Enable all the check boxes in UI
                    return true;
                }
//                break;
            }
        }
        return false;
    }

    public boolean updateTaskStatusInLogTable(String str){
        String status = "";
        if (tskStatus.getStatus() == 1){
            status = ACCEPTED;
        }else if (tskStatus.getStatus() == 2){
            status = REJECTED;
        }else if (tskStatus.getStatus() == 3){
            status = STARTED;
        }else if (tskStatus.getStatus() == 4){
            status = COMPLETED;
        }else if (tskStatus.getStatus() == 5){
            status = ERROR;
        }else if (tskStatus.getStatus() == 6){
            status = NO_DATA;
        }
        ArrayList exeAllTasks = dayTableModel.getDataArrayList();
        ArrayList task;
        for (int i = exeAllTasks.size() - 1,j = 0;i >= 0;--i,++j){
            task = (ArrayList) exeAllTasks.get(j);
            if (task.get(0).equals(str)){
                if (!((task.get(1).equals(COMPLETED)) || (status.equals(STARTED)) || (task.get(1).equals(REJECTED)) || (task.get(1).equals(ACCEPTED)) || (task.get(1).equals(ERROR)) || (task.get(1).equals(NO_DATA)))){
                    setExeTaskCount(++exeTaskCount);
                }
                dayTableModel.setValueAt(status, j, 1);
                // To check whether the number of tasks selected in UI is
                // equal to the number of finished tasks
                if (getExeTaskCount() == getExeTaskCountUI()){
                    // Flag to Enable all the check boxes in UI
                    return true;
                }
            }
        }
        return false;
    }

       public ArrayList populateData(HashMap mapID, CTable tblData) {
        _tblData = tblData;
        
        // Adding Where Condition
        HashMap whereMap = null;
        if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
            if (mapID.get(CommonConstants.MAP_WHERE) instanceof HashMap) {
                whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
            } else {
                System.out.println ("Convert other data type to HashMap:" + mapID);
            }
        } else {
            whereMap = new HashMap();
        }
        
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put (CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put (CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }
        
        mapID.put (CommonConstants.MAP_WHERE, whereMap);
        
        System.out.println ("Screen   : " + getClass());
        System.out.println ("Map Name : " + mapID.get(CommonConstants.MAP_NAME));
        System.out.println ("Map      : " + mapID);
        
        dataHash = ClientUtil.executeTableQuery(mapID);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        if (_heading!=null && _heading.size()>0) {
            _heading.add(0, "Select");
        }
        ArrayList arrList = new ArrayList();
        if (data!=null && data.size()>0) {
            for (int i=0; i<data.size();i++) {
                arrList = (ArrayList)data.get(i);
                arrList.add(0, new Boolean(false));
                data.set(i, arrList);
            }
        }
        System.out.println("### Data : "+data);
        populateTable();
        whereMap = null;
        return _heading;
        
    }
public void setSelectAll(Boolean selected) {
        for (int i=0, j=_tblData.getRowCount(); i < j; i++) {
            _tblData.setValueAt(selected, i, 0);
        }
    }
    public void populateTable() {
//        ArrayList heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        boolean dataExist;
        if (_heading != null){
            _isAvailable = true;
            dataExist = true;         
            setTblModel(_tblData, data, _heading);     
        }else{
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
    public void setDataMap(HashMap dataMap){
        this.dataMap = dataMap;
    }
    
    public HashMap getDataMap(){
        return this.dataMap;
    }
    
    
    
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
    public String getTransactionType() {
        return transactionType;
    }
    
    
    public void setProdType(String prodType) {
        this.prodType = prodType;
    }
    public String getProdType() {
        return prodType;
    }
}
