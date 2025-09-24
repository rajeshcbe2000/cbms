/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RoleOB.java
 *
 * Created on April 12, 2004, 2:36 PM
 */

package com.see.truetransact.ui.sysadmin.role;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.sysadmin.role.GroupMasterTO;
import com.see.truetransact.transferobject.sysadmin.role.LevelMasterTO;
import com.see.truetransact.transferobject.sysadmin.role.RoleMasterTO;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.TrueTransactMain;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

/**
 *
 * @author  Pinky
 * @modified  Jayakrishnan.K.R.
 */

public class RoleOB extends Observable {
    
    private int actionType;
    private int result;
    
    private static RoleOB roleOB;
    private HashMap map;
    private ProxyFactory proxy;
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.role.RoleRB", ProxyParameters.LANGUAGE);
    
    private RoleMasterTO  roleMasterObj;
    private TableModel tmlRoles;
    private ArrayList groupRoleTOs;
    private ComboBoxModel cbmRoleHierarchy;
    private HashMap lookupMap;
    private ArrayList tableHeader=new ArrayList();
    private ArrayList key,value;
    public String YES = "Y";
    public String NO = "N";
    private int viewType=0;
    private final int LEVEL_MASTER=10, LEVEL_FOEIGN = 11;
    private HashMap keyValue;
    private HashMap param = new HashMap();
    private final static ClientParseException parseException
    = ClientParseException.getInstance();
    private ArrayList deletedRow;
    private ArrayList editDelRow;
     ArrayList selectedRow;
    
    static {
        try {
            roleOB = new RoleOB();
            
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    private RoleOB()throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "GroupRoleJNDI");
        map.put(CommonConstants.HOME, "sysadmin.role.GroupRoleHome");
        map.put(CommonConstants.REMOTE,"sysadmin.role.GroupRole");
        try {
            proxy = ProxyFactory.createProxy();
            fillDropDown();
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
        setCoulmnHeader();
        tmlRoles = new TableModel(new ArrayList(),tableHeader);
        groupRoleTOs=new ArrayList();
    }
    public static RoleOB getInstance() {
        return roleOB;
    }
    private void setCoulmnHeader() {
        tableHeader.add(resourceBundle.getString("tblColumn1"));
        tableHeader.add(resourceBundle.getString("tblColumn2"));
        tableHeader.add(resourceBundle.getString("tblColumn3"));
        tableHeader.add(resourceBundle.getString("tblColumn4"));
        tableHeader.add(resourceBundle.getString("tblColumn5"));
        //tableHeader.add(resourceBundle.getString("tblColumn6"));
        tableHeader.add(resourceBundle.getString("tblColumn7"));
        tableHeader.add(resourceBundle.getString("tblColumn8"));
    }
    
    private String txtGroupID = "";
    private String txtGroupDesc = "";
    private String txtRoleID = "";
    private String txtRoleDesc = "";
    private String txtCashDebit = "";
    private String txtCashCredit = "";
    private String txtClearingCredit = "";
    private String txtClearingDebit = "";
    private String txtTransCredit = "";
    private String txtTransDebit = "";
    private String txtLevelID = "";
    private String txtLevelName = "";
    private String txtLevelDesc = "";
    private String lblStatus = "";
    private boolean chkAccAllBran = false ;
    private String txtLevelNameForeign = "";
    private String txtLevelDescForeign = "";
    private String txtCashDebitForeign = "";
    private String txtCashCreditForeign = "";
    private String txtClearingCreditForeign = "";
    private String txtClearingDebitForeign = "";
    private String txtTransCreditForeign = "";
    private String txtTransDebitForeign = "";
    private String txtLevelIDForeign = "";
    private String cboRoleHierarchy = "";
    private boolean chkHierarchyAllowed = false;
    private String cboRoleID = "";
    private ComboBoxModel cbmRoleID;
    
    
    
    // Setter method for txtGroupID
    void setTxtGroupID(String txtGroupID){
        this.txtGroupID = txtGroupID;
        setChanged();
    }
    // Getter method for txtGroupID
    String getTxtGroupID(){
        return this.txtGroupID;
    }
    
    // Setter method for txtGroupDesc
    void setTxtGroupDesc(String txtGroupDesc){
        this.txtGroupDesc = txtGroupDesc;
        setChanged();
    }
    // Getter method for txtGroupDesc
    String getTxtGroupDesc(){
        return this.txtGroupDesc;
    }
    
    // Setter method for txtRoleID
    void setTxtRoleID(String txtRoleID){
        this.txtRoleID = txtRoleID;
        setChanged();
    }
    // Getter method for txtRoleID
    String getTxtRoleID(){
        return this.txtRoleID;
    }
    
    // Setter method for txtRoleDesc
    void setTxtRoleDesc(String txtRoleDesc){
        this.txtRoleDesc = txtRoleDesc;
        setChanged();
    }
    // Getter method for txtRoleDesc
    String getTxtRoleDesc(){
        return this.txtRoleDesc;
    }
    
    // Setter method for txtCashDebit
    void setTxtCashDebit(String txtCashDebit){
        this.txtCashDebit = txtCashDebit;
        setChanged();
    }
    // Getter method for txtCashDebit
    String getTxtCashDebit(){
        return this.txtCashDebit;
    }
    
    // Setter method for txtCashCredit
    void setTxtCashCredit(String txtCashCredit){
        this.txtCashCredit = txtCashCredit;
        setChanged();
    }
    // Getter method for txtCashCredit
    String getTxtCashCredit(){
        return this.txtCashCredit;
    }
    
    // Setter method for txtClearingCredit
    void setTxtClearingCredit(String txtClearingCredit){
        this.txtClearingCredit = txtClearingCredit;
        setChanged();
    }
    // Getter method for txtClearingCredit
    String getTxtClearingCredit(){
        return this.txtClearingCredit;
    }
    
    // Setter method for txtClearingDebit
    void setTxtClearingDebit(String txtClearingDebit){
        this.txtClearingDebit = txtClearingDebit;
        setChanged();
    }
    // Getter method for txtClearingDebit
    String getTxtClearingDebit(){
        return this.txtClearingDebit;
    }
    
    // Setter method for txtTransCredit
    void setTxtTransCredit(String txtTransCredit){
        this.txtTransCredit = txtTransCredit;
        setChanged();
    }
    // Getter method for txtTransCredit
    String getTxtTransCredit(){
        return this.txtTransCredit;
    }
    
    // Setter method for txtTransDebit
    void setTxtTransDebit(String txtTransDebit){
        this.txtTransDebit = txtTransDebit;
        setChanged();
    }
    // Getter method for txtTransDebit
    String getTxtTransDebit(){
        return this.txtTransDebit;
    }
    
    // Setter method for txtLevelID
    void setTxtLevelID(String txtLevelID){
        this.txtLevelID = txtLevelID;
        setChanged();
    }
    // Getter method for txtLevelID
    String getTxtLevelID(){
        return this.txtLevelID;
    }
    
    // Setter method for txtLevelName
    void setTxtLevelName(String txtLevelName){
        this.txtLevelName = txtLevelName;
        setChanged();
    }
    // Getter method for txtLevelName
    String getTxtLevelName(){
        return this.txtLevelName;
    }
    
    // Setter method for txtLevelDesc
    void setTxtLevelDesc(String txtLevelDesc){
        this.txtLevelDesc = txtLevelDesc;
        setChanged();
    }
    // Getter method for txtLevelDesc
    String getTxtLevelDesc(){
        return this.txtLevelDesc;
    }
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        setChanged();
        notifyObservers();
    }
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        setChanged();
        notifyObservers();
    }
    
    public void ttNotifyObservers() {
        notifyObservers();
    }
    
    /** Getter for property actionType.
     * @return Value of property actionType.
     *
     */
    public int getActionType() {
        return actionType;
    }
    
    /** Setter for property actionType.
     * @param actionType New value of property actionType.
     *
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
    
    /** Getter for property result.
     * @return Value of property result.
     *
     */
    public int getResult() {
        return result;
    }
    /** Setter for property result.
     * @param result New value of property result.
     *
     */
    public void setResult(int result) {
        this.result = result;
    }
    /** Getter for property lblStatus.
     * @return Value of property lblStatus.
     *
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    
    /** Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }
    
    private void fillDropDown(){
        try{
            lookupMap = new HashMap();
            lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
            lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
            lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
            final ArrayList lookupKey = new ArrayList();
            lookupKey.add("ROLE_HIERARCHY");
            param.put(CommonConstants.PARAMFORQUERY, lookupKey);
            final HashMap lookupValues = populatelookupData(param);
            fillData((HashMap)lookupValues.get("ROLE_HIERARCHY"));
            cbmRoleHierarchy = new ComboBoxModel(key,value);
            lookupMap.put(CommonConstants.MAP_NAME,"getDesignation");
            lookupMap.put(CommonConstants.PARAMFORQUERY, null);
            keyValue = ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            cbmRoleID = new ComboBoxModel(key,value);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
     private void getKeyValue(HashMap keyValue)  throws Exception{
        
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    
    /** To set the key & value for comboboxes */
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /** To get data for comboboxes */
    private HashMap populatelookupData(HashMap obj)  throws Exception{
        keyValue = proxy.executeQuery(obj,lookupMap);
        return keyValue;
    }
    
    /** This method is used to clear the Fields related to Level Details of Foreign Branch */
    public void clearForeignLevelDetails(){
        setTxtLevelIDForeign("");
        setTxtLevelNameForeign("");
        setTxtLevelDescForeign("");
        setTxtClearingCreditForeign("");
        setTxtClearingDebitForeign("");
        setTxtTransCreditForeign("");
        setTxtTransDebitForeign("");
        setTxtCashDebitForeign("");
        setTxtCashCreditForeign("");
    }
    
    public void resetForm(){
        txtGroupID = "";
        txtGroupDesc = "";
        txtRoleID = "";
        cboRoleHierarchy = "";
        txtRoleDesc = "";
        txtCashDebit = "";
        txtCashCredit = "";
        txtClearingCredit = "";
        txtClearingDebit = "";
        txtTransCredit = "";
        txtTransDebit = "";
        txtLevelID = "";
        txtLevelName = "";
        txtLevelDesc = "";
        
        txtLevelNameForeign = "";
        txtLevelDescForeign = "";
        txtCashDebitForeign = "";
        txtCashCreditForeign = "";
        txtClearingCreditForeign = "";
        txtClearingDebitForeign = "";
        txtTransCreditForeign = "";
        txtTransDebitForeign = "";
        txtLevelIDForeign = "";
        cboRoleID="";
        groupRoleTOs.clear();
        tmlRoles.setData(new ArrayList());
        tmlRoles.fireTableDataChanged();
        setChkAccAllBran(false);
    }
    public void doAction() {
        HashMap obj = new HashMap();
        GroupMasterTO groupMasterObj = new GroupMasterTO();
        groupMasterObj.setGroupName(txtGroupDesc);
        groupMasterObj.setGroupId(txtGroupID);
        //        System.out.println(getActionType());
        if ( getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            groupMasterObj.setCommand(CommonConstants.TOSTATUS_UPDATE);
        } else if ( getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            groupMasterObj.setCommand(CommonConstants.TOSTATUS_DELETE);
        }
        try {
            obj.put("GroupMasterTO", groupMasterObj);
            getGroupRolesTO();
            obj.put("RoleTOs",groupRoleTOs);
            
            if(deletedRow!=null){
                obj.put("RoleDeleted", deletedRow);
            }
           if(editDelRow!=null){
              obj.put("EditRoleDeleted", editDelRow); 
           }
            
            System.out.println("Data Map "+ obj);
            HashMap proxyResultMap = proxy.execute(obj,map);
            deletedRow = new ArrayList();
            editDelRow = new ArrayList();
            setResult(getActionType());
        }catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
        
    }
    public void populateData(HashMap hashMap) {
        HashMap mapData;
        try {
            System.out.println("viewType: " + viewType);
            
            if(viewType == ClientConstants.ACTIONTYPE_EDIT ||
            viewType == ClientConstants.ACTIONTYPE_DELETE ||
            viewType == ClientConstants.ACTIONTYPE_AUTHORIZE ||
            viewType == ClientConstants.ACTIONTYPE_VIEW){
                
                //__  to get the Group Related Data...
                hashMap.put(CommonConstants.MAP_NAME,"selectGroupMasterTO");
                System.out.println("hashMap: " + hashMap);
                
                
                mapData = proxy.executeQuery(hashMap, map);
                System.out.println("mapData: " + mapData);
                
                List list = (List)mapData.get(CommonConstants.MAP_NAME);
                setGroupMasterTO((HashMap)list.get(0));
                
                //__ To get the Role Related Data...
                if(viewType == ClientConstants.ACTIONTYPE_EDIT ||
                viewType == ClientConstants.ACTIONTYPE_DELETE ||
                viewType == ClientConstants.ACTIONTYPE_VIEW){
                    hashMap.put(CommonConstants.MAP_NAME,"getSelectRoleMasterTO");
                }else if(viewType==ClientConstants.ACTIONTYPE_AUTHORIZE){
                    hashMap.put(CommonConstants.MAP_NAME,"getSelectAuthorizeRoleMasterTO");
                }
                mapData = proxy.executeQuery(hashMap, map);
                
                System.out.println("Role data: " + mapData);
                
                groupRoleTOs = (ArrayList)mapData.get(CommonConstants.MAP_NAME);
                setGroupRolesTO();
                
            }else if(viewType == LEVEL_FOEIGN){
                getLevelForeign(hashMap);
                
            }else if(viewType==LEVEL_MASTER){
                getLevelHome(hashMap);
            }
            notifyObservers();
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            setResultStatus();
            System.err.println( "Exception " + e.toString() + " Caught" );
            e.printStackTrace();
        }
    }
    
    
    public void getLevelHome(HashMap hashMap) throws Exception{
        hashMap.put(CommonConstants.MAP_NAME,"getSelectLevelMasterTO");
        HashMap mapData = proxy.executeQuery(hashMap, map);
        List list = (List)mapData.get(CommonConstants.MAP_NAME);
        if(list!= null && list.size() > 0)
            setLevelMasterTO((LevelMasterTO)list.get(0));
        //        else
        //            resetLevelMasterTO();
    }
    
    public void getLevelForeign(HashMap hashMap) throws Exception{
        hashMap.put(CommonConstants.MAP_NAME,"getSelectLevelMasterTO");
        HashMap mapData = proxy.executeQuery(hashMap, map);
        List list = (List)mapData.get(CommonConstants.MAP_NAME);
        if(list!= null && list.size() > 0)
            setLevelMasterForeignTO((LevelMasterTO)list.get(0));
        //        else
        //            resetLevelMasterTO();
    }
    
    
    public void resetLevelMasterTO(){
        setTxtLevelID("");
        setTxtLevelName("");
        setTxtLevelDesc("");
        setTxtCashCredit("");
        setTxtCashDebit("");
        setTxtClearingCredit("");
        setTxtClearingDebit("");
        setTxtTransCredit("");
        setTxtTransDebit("");
        
        setTxtLevelIDForeign("");
        setTxtLevelNameForeign("");
        setTxtLevelDescForeign("");
        setTxtCashCreditForeign("");
        setTxtCashDebitForeign("");
        setTxtClearingCreditForeign("");
        setTxtClearingDebitForeign("");
        setTxtTransCreditForeign("");
        setTxtTransDebitForeign("");
    }
    private void setLevelMasterTO(LevelMasterTO obj){
        setTxtLevelID(CommonUtil.convertObjToStr(obj.getLevelId()));
        setTxtLevelName(CommonUtil.convertObjToStr(obj.getLevelName()));
        setTxtLevelDesc(CommonUtil.convertObjToStr(obj.getLevelDesc()));
        setTxtCashCredit(CommonUtil.convertObjToStr(obj.getCashCredit()));
        setTxtCashDebit(CommonUtil.convertObjToStr(obj.getCashDebit()));
        
        setTxtClearingCredit(CommonUtil.convertObjToStr(obj.getClearingCredit()));
        setTxtClearingDebit(CommonUtil.convertObjToStr(obj.getClearingDebit()));
        
        setTxtTransCredit(CommonUtil.convertObjToStr(obj.getTransCredit()));
        setTxtTransDebit(CommonUtil.convertObjToStr(obj.getTransDebit()));
    }
    
    private void setLevelMasterForeignTO(LevelMasterTO obj){
        setTxtLevelIDForeign(CommonUtil.convertObjToStr(obj.getLevelId()));
        setTxtLevelNameForeign(CommonUtil.convertObjToStr(obj.getLevelName()));
        setTxtLevelDescForeign(CommonUtil.convertObjToStr(obj.getLevelDesc()));
        setTxtCashCreditForeign(CommonUtil.convertObjToStr(obj.getCashCredit()));
        setTxtCashDebitForeign(CommonUtil.convertObjToStr(obj.getCashDebit()));
        
        setTxtClearingCreditForeign(CommonUtil.convertObjToStr(obj.getClearingCredit()));
        setTxtClearingDebitForeign(CommonUtil.convertObjToStr(obj.getClearingDebit()));
        
        setTxtTransCreditForeign(CommonUtil.convertObjToStr(obj.getTransCredit()));
        setTxtTransDebitForeign(CommonUtil.convertObjToStr(obj.getTransDebit()));
    }
    
    /** Set groupMasterTO object which represent a group */
    public void setGroupMasterTO(HashMap mapObj) {
        setTxtGroupID(CommonUtil.convertObjToStr(mapObj.get("groupId")));
        setTxtGroupDesc(CommonUtil.convertObjToStr(mapObj.get("groupName")));
    }
    
    private void setGroupRolesTO() {
        try{
            ArrayList arr;
            tmlRoles.setData(new ArrayList());
            int j=groupRoleTOs.size();
            HashMap whereMap = null, where= null;
            for ( int i=0; i<j;i++) {
                roleMasterObj = (RoleMasterTO)groupRoleTOs.get(i);
                if(viewType==ClientConstants.ACTIONTYPE_AUTHORIZE){
                    setCboRoleID(CommonUtil.convertObjToStr(roleMasterObj.getRoleId()));
                    setTxtRoleDesc(roleMasterObj.getRoleName());
                    setCboRoleHierarchy(CommonUtil.convertObjToStr(getCbmRoleHierarchy().getDataForKey(CommonUtil.convertObjToStr(roleMasterObj.getHierarchyId()))));
                    setTxtLevelID(roleMasterObj.getLevelId());
                    setTxtLevelIDForeign(roleMasterObj.getForeignLevelId());
                    whereMap = new HashMap();
                    whereMap.put("LEVEL_ID", roleMasterObj.getLevelId());
                    where = new HashMap();
                    where.put(CommonConstants.MAP_WHERE, whereMap);
                    getLevelHome(where);
                    if(roleMasterObj.getAccessAllBranch().equals("Y")){
                        setChkAccAllBran(true);
                        whereMap.put("LEVEL_ID", roleMasterObj.getForeignLevelId());
                        where.put(CommonConstants.MAP_WHERE, whereMap);
                        getLevelForeign(where);
                    }else if(roleMasterObj.getAccessAllBranch().equals("N")){
                        setChkAccAllBran(false);
                        clearForeignLevelDetails();
                    }
                }else{
                    arr= new ArrayList();
                    arr.add(CommonUtil.convertObjToStr(roleMasterObj.getRoleId()));
                    arr.add(CommonUtil.convertObjToStr(roleMasterObj.getRoleName()));
                    arr.add(CommonUtil.convertObjToStr(roleMasterObj.getHierarchyId()));
                    arr.add(CommonUtil.convertObjToStr(roleMasterObj.getLevelId()));
                    arr.add(CommonUtil.convertObjToStr(roleMasterObj.getForeignLevelId()));
                    //arr.add(CommonUtil.convertObjToStr(roleMasterObj.getAccessAllBranch()));
                    arr.add(CommonUtil.convertObjToStr(roleMasterObj.getSameHierarchyAllowed()));
                    arr.add(CommonUtil.convertObjToStr(roleMasterObj.getAuthorizeStatus()));
                    
                    tmlRoles.insertRow(tmlRoles.getRowCount(),arr);
                    tmlRoles.fireTableDataChanged();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void getGroupRolesTO(){
        ArrayList arr=tmlRoles.getDataArrayList();
        System.out.println("arr==========="+arr);
        ArrayList groupRoles=new ArrayList();
        ArrayList row;
        int j=arr.size();
        for(int i=0;i<j;i++){
            row=(ArrayList)arr.get(i);
            System.out.println("row==========="+row);
            roleMasterObj=new RoleMasterTO();
            roleMasterObj.setRoleId(CommonUtil.convertObjToStr((String)row.get(0)));
            roleMasterObj.setRoleName(CommonUtil.convertObjToStr(row.get(1)));
            roleMasterObj.setHierarchyId(CommonUtil.convertObjToDouble(row.get(2)));
            roleMasterObj.setLevelId(CommonUtil.convertObjToStr(row.get(3)));
            roleMasterObj.setForeignLevelId(CommonUtil.convertObjToStr(row.get(4)));
            roleMasterObj.setGroupId(txtGroupID);
            roleMasterObj.setAuthorizeStatus(CommonUtil.convertObjToStr(row.get(6)));
            //            if(getChkAccAllBran()==true){
            //roleMasterObj.setAccessAllBranch(CommonUtil.convertObjToStr(row.get(5)));
            roleMasterObj.setSameHierarchyAllowed(CommonUtil.convertObjToStr(row.get(5)));
            //            } else {
            //            roleMasterObj.setAccessAllBranch(NO);
            //            }
            if(getChkAccAllBran()==true){
                roleMasterObj.setAccessAllBranch("Y");
            }
            else
            {
                roleMasterObj.setAccessAllBranch("N");
            }
            roleMasterObj.setStatus(CommonConstants.STATUS_CREATED);
            groupRoles.add(roleMasterObj);
        }
        mergeTOs(groupRoles);
    }
    private void mergeTOs(ArrayList arr){
        int j=groupRoleTOs.size();
        int k=arr.size();
        RoleMasterTO toCompare;
        int l;
        for(int i=0;i<j;i++){
            toCompare=(RoleMasterTO)groupRoleTOs.get(i);
            for(l=0;l<k;l++){
                roleMasterObj=(RoleMasterTO)arr.get(l);
                if(roleMasterObj.getRoleId().equals(toCompare.getRoleId())){
                    groupRoleTOs.remove(i);
                    i=i-1;
                    break;
                }
            }
            j=groupRoleTOs.size();
        }
        j=groupRoleTOs.size();
        for(int i=0;i<j;i++){
            toCompare=(RoleMasterTO)groupRoleTOs.get(i);
            toCompare.setStatus(CommonConstants.STATUS_DELETED);
            groupRoleTOs.set(i,toCompare);
        }
        groupRoleTOs.addAll(arr);
    }
    /** Getter for property tmlRoles.
     * @return Value of property tmlRoles.
     *
     */
    public TableModel getTmlRoles() {
        return tmlRoles;
    }
    
    /** Setter for property tmlRoles.
     * @param tmlRoles New value of property tmlRoles.
     *
     */
    public void setTmlRoles(TableModel tmlRoles) {
        this.tmlRoles = tmlRoles;
    }
    
    public ArrayList populateRoles(int rowNum) {
        return tmlRoles.getRow(rowNum);
    }
    public void deleteRoleData(int rowNum) {
        System.out.println("jjjjjiiiiiiiiii");
        selectedRow = tmlRoles.getRow(rowNum);
        System.out.println("selectedRow::::::::::::;"+selectedRow);
        tmlRoles.removeRow(rowNum);
        RoleMasterTO roleMasterTO = new RoleMasterTO();
        if(deletedRow==null){
            deletedRow = new ArrayList();
        }
        roleMasterTO.setRoleId(CommonUtil.convertObjToStr((String)selectedRow.get(0)));
        roleMasterTO.setGroupId(txtGroupID);
        roleMasterTO.setStatusBy(TrueTransactMain.USER_ID);
        roleMasterTO.setStatusDt(ClientUtil.getCurrentDate());
        roleMasterTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedRow.add(roleMasterTO);
    }
    public void setselRowDat()  
    {
       selectedRow = new ArrayList();
    }

    public void editDelRoleData(int rowNum) {
        ArrayList selectedRow = tmlRoles.getRow(rowNum);
        tmlRoles.removeRow(rowNum);
        RoleMasterTO roleMasterTO = new RoleMasterTO();
        if(editDelRow==null){
            editDelRow = new ArrayList();
        }
        roleMasterTO.setRoleId(CommonUtil.convertObjToStr((String)selectedRow.get(0)));
        roleMasterTO.setGroupId(txtGroupID);
        roleMasterTO.setStatusBy(TrueTransactMain.USER_ID);
        roleMasterTO.setStatusDt(ClientUtil.getCurrentDate());
        roleMasterTO.setStatus(CommonConstants.STATUS_DELETED);
        editDelRow.add(roleMasterTO);
    }
    
    public int insertRoleData(ArrayList irRow) {
        System.out.println("insertRoleData");
      int m=0;
        System.out.println("selectedRowsize"+selectedRow.size());
        for(int i=0;i<selectedRow.size();i++)
        {
        System.out.println("((String)selectedRow.get(i))"+((String)selectedRow.get(i)));
        if ( ((String)selectedRow.get(i)).equalsIgnoreCase((String)(irRow.get(i))))
        {
            m = 1;
//          System.out.println("((String)selectedRow.get(i))lllllllllllll"+((String)selectedRow.get(i)));  
        }
        else
        {
            m=2;
            break;
         }
        }
        if(m==2)
        {
            int i=irRow.size();
            System.out.println("iiiiiiiiiiiiii"+i);
            irRow.set(i-1,"");
        }
        ArrayList arr = tmlRoles.getDataArrayList();
        System.out.println("arr:::::::"+arr);
        ArrayList arr1;
        int j=arr.size();
        System.out.println("j:::::::;;;;;;;;;"+j);
        System.out.println("irRow::::::::"+irRow);
        System.out.println("tmlRoles.getRowCount():::::"+tmlRoles.getRowCount());
        for (int i=0;i<j;i++) {
            arr1 = (ArrayList)arr.get(i);
            if ( ((String)arr1.get(0)).equalsIgnoreCase((String)(irRow.get(0))))
                return -1;
        }
        
        tmlRoles.insertRow(tmlRoles.getRowCount(), irRow);
        tmlRoles.fireTableDataChanged();
        return 0;
    }
    public int chkDupRoleDat(ArrayList irRow) 
    {
       ArrayList arr = tmlRoles.getDataArrayList();
        System.out.println("arr:::::::"+arr);
        ArrayList arr1;
        int j=arr.size();
        System.out.println("j:::::::;;;;;;;;;"+j);
        System.out.println("irRow::::::::"+irRow);
        System.out.println("tmlRoles.getRowCount():::::"+tmlRoles.getRowCount());
        for (int i=0;i<j;i++) {
            arr1 = (ArrayList)arr.get(i);
            if ( ((String)arr1.get(0)).equalsIgnoreCase((String)(irRow.get(0))))
                return -1;
        }
        return 0;
    }
    /** Getter for property viewType.
     * @return Value of property viewType.
     *
     */
    public int getViewType() {
        return viewType;
    }
    
    /** Setter for property viewType.
     * @param viewType New value of property viewType.
     *
     */
    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
    
    /**
     * Getter for property chkAccAllBran.
     * @return Value of property chkAccAllBran.
     */
    public boolean getChkAccAllBran() {
        return chkAccAllBran;
    }
    
    /**
     * Setter for property chkAccAllBran.
     * @param chkAccAllBran New value of property chkAccAllBran.
     */
    public void setChkAccAllBran(boolean chkAccAllBran) {
        this.chkAccAllBran = chkAccAllBran;
    }
    
    // Setter method for txtLevelNameForeign
    void setTxtLevelNameForeign(String txtLevelNameForeign){
        this.txtLevelNameForeign = txtLevelNameForeign;
        setChanged();
    }
    // Getter method for txtLevelNameForeign
    String getTxtLevelNameForeign(){
        return this.txtLevelNameForeign;
    }
    
    // Setter method for txtLevelDescForeign
    void setTxtLevelDescForeign(String txtLevelDescForeign){
        this.txtLevelDescForeign = txtLevelDescForeign;
        setChanged();
    }
    // Getter method for txtLevelDescForeign
    String getTxtLevelDescForeign(){
        return this.txtLevelDescForeign;
    }
    
    // Setter method for txtCashDebitForeign
    void setTxtCashDebitForeign(String txtCashDebitForeign){
        this.txtCashDebitForeign = txtCashDebitForeign;
        setChanged();
    }
    // Getter method for txtCashDebitForeign
    String getTxtCashDebitForeign(){
        return this.txtCashDebitForeign;
    }
    
    // Setter method for txtCashCreditForeign
    void setTxtCashCreditForeign(String txtCashCreditForeign){
        this.txtCashCreditForeign = txtCashCreditForeign;
        setChanged();
    }
    // Getter method for txtCashCreditForeign
    String getTxtCashCreditForeign(){
        return this.txtCashCreditForeign;
    }
    
    // Setter method for txtClearingDebitForeign
    void setTxtClearingDebitForeign(String txtClearingDebitForeign){
        this.txtClearingDebitForeign = txtClearingDebitForeign;
        setChanged();
    }
    // Getter method for txtClearingDebitForeign
    String getTxtClearingDebitForeign(){
        return this.txtClearingDebitForeign;
    }
    
    // Setter method for txtCashCreditForeign
    void setTxtClearingCreditForeign(String txtClearingCreditForeign){
        this.txtClearingCreditForeign = txtClearingCreditForeign;
        setChanged();
    }
    // Getter method for txtCashCreditForeign
    String getTxtClearingCreditForeign(){
        return this.txtClearingCreditForeign;
    }
    
    // Setter method for txtTransDebitForeign
    void setTxtTransDebitForeign(String txtTransDebitForeign){
        this.txtTransDebitForeign = txtTransDebitForeign;
        setChanged();
    }
    // Getter method for txtClearingDebitForeign
    String getTxtTransDebitForeign(){
        return this.txtTransDebitForeign;
    }
    
    // Setter method for txtCashCreditForeign
    void setTxtTransCreditForeign(String txtTransCreditForeign){
        this.txtTransCreditForeign = txtTransCreditForeign;
        setChanged();
    }
    // Getter method for txtCashCreditForeign
    String getTxtTransCreditForeign(){
        return this.txtTransCreditForeign;
    }
    
    // Setter method for txtLevelIDForeign
    void setTxtLevelIDForeign(String txtLevelIDForeign){
        this.txtLevelIDForeign = txtLevelIDForeign;
        setChanged();
    }
    // Getter method for txtCashCreditForeign
    String getTxtLevelIDForeign(){
        return this.txtLevelIDForeign;
    }
    
    /**
     * Getter for property cboRoleHierarchy.
     * @return Value of property cboRoleHierarchy.
     */
    public java.lang.String getCboRoleHierarchy() {
        return cboRoleHierarchy;
    }
    
    /**
     * Setter for property cboRoleHierarchy.
     * @param cboRoleHierarchy New value of property cboRoleHierarchy.
     */
    public void setCboRoleHierarchy(java.lang.String cboRoleHierarchy) {
        this.cboRoleHierarchy = cboRoleHierarchy;
    }
    
    /**
     * Getter for property cbmRoleHierarchy.
     * @return Value of property cbmRoleHierarchy.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRoleHierarchy() {
        return cbmRoleHierarchy;
    }
    
    /**
     * Setter for property cbmRoleHierarchy.
     * @param cbmRoleHierarchy New value of property cbmRoleHierarchy.
     */
    public void setCbmRoleHierarchy(com.see.truetransact.clientutil.ComboBoxModel cbmRoleHierarchy) {
        this.cbmRoleHierarchy = cbmRoleHierarchy;
    }
    
    /**
     * Getter for property chkHierarchyAllowed.
     * @return Value of property chkHierarchyAllowed.
     */
    public boolean getChkHierarchyAllowed() {
        return chkHierarchyAllowed;
    }
    
    /**
     * Setter for property chkHierarchyAllowed.
     * @param chkHierarchyAllowed New value of property chkHierarchyAllowed.
     */
    public void setChkHierarchyAllowed(boolean chkHierarchyAllowed) {
        this.chkHierarchyAllowed = chkHierarchyAllowed;
    }
    
    /**
     * Getter for property cboRoleID.
     * @return Value of property cboRoleID.
     */
    public java.lang.String getCboRoleID() {
        return cboRoleID;
    }
    
    /**
     * Setter for property cboRoleID.
     * @param cboRoleID New value of property cboRoleID.
     */
    public void setCboRoleID(java.lang.String cboRoleID) {
        this.cboRoleID = cboRoleID;
    }
    
    /**
     * Getter for property cbmRoleID.
     * @return Value of property cbmRoleID.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRoleID() {
        return cbmRoleID;
    }
    
    /**
     * Setter for property cbmRoleID.
     * @param cbmRoleID New value of property cbmRoleID.
     */
    public void setCbmRoleID(com.see.truetransact.clientutil.ComboBoxModel cbmRoleID) {
        this.cbmRoleID = cbmRoleID;
    }
    
}
