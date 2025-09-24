/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *includeExclude
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GroupOB.java
 *
 * Created on February 25, 2004, 4:50 PM
 */

package com.see.truetransact.ui.sysadmin.group;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.CommonRB;
import com.see.truetransact.transferobject.sysadmin.group.GroupMasterTO;
import com.see.truetransact.transferobject.sysadmin.group.GroupScreensTO;
//import com.see.truetransact.transferobject.sysadmin.group.ScreenMasterTO;

import com.see.truetransact.ui.TrueTransactMain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.util.Date;
/**
 *
 * @author  Pinky
 */

public class GroupOB extends Observable {    
    private String txtGrpID;
    private String txtGrpDesc;
    private String txtBranchGroup;
    private String lblStatus;    
    private boolean branchGroup;    
    
    private boolean chkNewAllowed = false;
    private boolean chkEditAllowed = false;
    private boolean chkDeleteAllowed = false;
    private boolean chkAuthRejAllowed = false;
    private boolean chkExceptionAllowed = false;
    private boolean chkPrintAllowed = false;
    
    private ArrayList availableScreen;
    private ArrayList grantScreen;
    
    private int actionType;
    private int result;
    
    private HashMap map,objHashMap;
    private ProxyFactory proxy;
    
    //ScreenMasterTO obj=new ScreenMasterTO();
    
    //private ScreenTOComparator screenComparator = new ScreenTOComparator();
    
    private final static ClientParseException parseException
    = ClientParseException.getInstance();
    
    ArrayList goldLoanProdIDs=null;
    private Date currDt = null;
    public GroupOB(boolean branchConfig) {
        try {
            currDt = ClientUtil.getCurrentDate();
            this.branchGroup = branchConfig;
            map = new HashMap();
            map.put(CommonConstants.JNDI, "GroupScreenJNDI");
            map.put(CommonConstants.HOME, "sysadmin.group.GroupScreenHome");
            map.put(CommonConstants.REMOTE,"sysadmin.group.GroupScreen");
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
        availableScreen = new ArrayList();
        grantScreen = new ArrayList();
    }
    
    /** R/W methods */
    
    void setChkNewAllowed(boolean chkNewAllowed){
        this.chkNewAllowed = chkNewAllowed;
        setChanged();
    }
    boolean getChkNewAllowed(){
        return this.chkNewAllowed;
    }
    
    void setChkEditAllowed(boolean chkEditAllowed){
        this.chkEditAllowed = chkEditAllowed;
        setChanged();
    }
    boolean getChkEditAllowed(){
        return this.chkEditAllowed;
    }
    
    void setChkDeleteAllowed(boolean chkDeleteAllowed){
        this.chkDeleteAllowed = chkDeleteAllowed;
        setChanged();
    }
    boolean getChkDeleteAllowed(){
        return this.chkDeleteAllowed;
    }
    
    void setChkAuthRejAllowed(boolean chkAuthRejAllowed){
        this.chkAuthRejAllowed = chkAuthRejAllowed;
        setChanged();
    }
    boolean getChkAuthRejAllowed(){
        return this.chkAuthRejAllowed;
    }
    
    void setChkExceptionAllowed(boolean chkExceptionAllowed){
        this.chkExceptionAllowed = chkExceptionAllowed;
        setChanged();
    }
    boolean getChkExceptionAllowed(){
        return this.chkExceptionAllowed;
    }
    
    void setChkPrintAllowed(boolean chkPrintAllowed){
        this.chkPrintAllowed = chkPrintAllowed;
        setChanged();
    }
    boolean getChkPrintAllowed(){
        return this.chkPrintAllowed;
    }
    
    void setTxtGrpID(String txtGrpID){
        this.txtGrpID = txtGrpID;
        setChanged();
    }    
    String getTxtGrpID(){
        return this.txtGrpID;
    }
    
    void setTxtBranchGroup(String txtBranchGroup){
        this.txtBranchGroup = txtBranchGroup;
        setChanged();
    }
    String getTxtBranchGroup(){
        return this.txtBranchGroup;
    }
    void setTxtGrpDesc(String txtGrpDesc){
        this.txtGrpDesc = txtGrpDesc;
        setChanged();
    }
    String getTxtGrpDesc(){
        return this.txtGrpDesc;
    }
    public int getActionType(){
        return actionType;
    }
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    public ArrayList getAvailableScreen() {
        return availableScreen;
    }
    public void setAvailableScreen(ArrayList availableScreen) {
        this.availableScreen = availableScreen;
        setChanged();
    }
    public ArrayList getGrantScreen() {
        return grantScreen;
    }
    public void setGrantScreen(ArrayList grantScreen) {
        this.grantScreen = grantScreen;
        setChanged();
    }
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        notifyObservers();
    }
    public int getResult() {
        return this.result;
    }
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        setChanged();
        notifyObservers();
    }
    public DefaultTreeModel getAvailableScreenModel() {
        return createTreeModel(availableScreen);
    }
    public DefaultTreeModel getGrantScreenModel() {
        return createTreeModel(grantScreen);
    }
    
    /** Reset all values */
    public void resetForm(){
        availableScreen.clear();
        grantScreen.clear();
        setTxtGrpID("");
        setTxtGrpDesc("");
        setAvailableScreen(this.availableScreen);
        setGrantScreen(this.grantScreen);
        setLblStatus("");
        
        setTxtBranchGroup("");        
        setChkNewAllowed(false);     
        setChkEditAllowed(false);     
        setChkDeleteAllowed(false);     
        setChkAuthRejAllowed(false);     
        setChkExceptionAllowed(false);     
        setChkPrintAllowed(false);
    }   
    
    /** Get list of available screens from ScreeMaster */
    public ArrayList getAllAvailableScreen() {
        objHashMap = new HashMap();
        ArrayList availableScreens=null;
        
        HashMap whereMap = new HashMap();
//        if (branchGroup) { // For User Group add Branch ID as a condition
//            whereMap.put("BRANCH_ID", null);
//        } else {
//            whereMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
//        }
        
        whereMap.put("BRANCH_GROUP_ID", getTxtBranchGroup());
        objHashMap.put(CommonConstants.MAP_WHERE, whereMap);
        
        objHashMap.put(CommonConstants.MAP_NAME, "getScreenModuleTO");
        
        try {
            objHashMap = proxy.executeQuery(objHashMap,map);
            availableScreens =(ArrayList)objHashMap.get(CommonConstants.MAP_NAME);
//            System.out.println("availableScreens:"+availableScreens);
        } catch( Throwable e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            setResultStatus();
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
        return availableScreens;
    }
    
    /** Perform action depending on the command */
    public void doAction() {
        objHashMap = new HashMap();
        GroupMasterTO groupMasterObj = new GroupMasterTO();
        groupMasterObj.setBranchGroupId(txtBranchGroup);
        
        groupMasterObj.setGroupName(txtGrpDesc);
        groupMasterObj.setGroupId(txtGrpID);
        
        CommonRB objCommonRB = new CommonRB();
        
        if ( getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            if (branchGroup)
                groupMasterObj.setBranchGroup(objCommonRB.getString("YES"));
            else
                groupMasterObj.setBranchGroup(objCommonRB.getString("NO"));
            
            groupMasterObj.setBranchCode(TrueTransactMain.BRANCH_ID);
            groupMasterObj.setCommand(CommonConstants.TOSTATUS_INSERT);
            groupMasterObj.setStatus(CommonConstants.STATUS_CREATED);
            groupMasterObj.setCreatedBy(TrueTransactMain.USER_ID);
            groupMasterObj.setCreatedDt(currDt);
        } else if ( getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            groupMasterObj.setCommand(CommonConstants.TOSTATUS_UPDATE);
            groupMasterObj.setStatus(CommonConstants.STATUS_MODIFIED);
        } else if ( getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            groupMasterObj.setCommand(CommonConstants.TOSTATUS_DELETE);
            groupMasterObj.setStatus(CommonConstants.STATUS_DELETED);
        }
        groupMasterObj.setStatusBy(TrueTransactMain.USER_ID);
        groupMasterObj.setStatusDt(currDt);
        try {
            objHashMap.put("GroupMasterTO", groupMasterObj);            
            objHashMap.put("ScreenTO", getGrantScreen());
            HashMap proxyResultMap = proxy.execute(objHashMap,map);
            setResult(getActionType());
        }catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
    }
    
    /** Populate data for Edit/delete */
    public void populateData(HashMap hashMap) {
        //HashMap mapData;
        hashMap.put(CommonConstants.MAP_NAME,"getSelectGroupMaster");
        try {
            objHashMap= proxy.executeQuery(hashMap, map);
            List list = (List)objHashMap.get(CommonConstants.MAP_NAME);
            setGroupMasterTO((GroupMasterTO)list.get(0));
            hashMap.put(CommonConstants.MAP_NAME,"getGroupScreenData");
            objHashMap = proxy.executeQuery(hashMap, map);
//            System.out.println("****************************objHashMap:"+objHashMap);
            grantScreen = (ArrayList)objHashMap.get(CommonConstants.MAP_NAME);
//            System.out.println("############################grantScreen:"+grantScreen);
            hashMap.put(CommonConstants.MAP_NAME,"getRemainingScreenMasterTO");
            objHashMap = proxy.executeQuery(hashMap,map);
            availableScreen = (ArrayList)objHashMap.get(CommonConstants.MAP_NAME);
            notifyObservers();
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            setResultStatus();
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
    }
    
    /** Set groupMasterTO object which represent a group */
    public void setGroupMasterTO(GroupMasterTO obj) {
        setTxtBranchGroup(obj.getBranchGroupId());
        setTxtGrpID(obj.getGroupId());
        setTxtGrpDesc(obj.getGroupName());
    }
    
    /** Takes a arryalist as argument and create TreeModel in which all leaf node
     * represent the corresponding element it arrayList */
    
    private DefaultTreeModel createTreeModel(ArrayList arr){
        HashMap screenModuleObject;
        ScreenModuleTreeNode next,childNode,dn;
        int position;
        objHashMap=new HashMap();
        objHashMap.put("moduleName", "CBMS++");
        objHashMap.put("screenClass", "CBMS++");
        ScreenModuleTreeNode root=new ScreenModuleTreeNode(objHashMap,true);
        DefaultTreeModel tre=new DefaultTreeModel(root);
        int size=arr.size();
        HashMap objGoldLoanScreenModuleObject = new HashMap();
//        System.out.println("before for loop and size is:"+size);
        for(int i=0;i<size;i++){
            screenModuleObject=(HashMap)arr.get(i);
//            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!screenModuleObject "+i+":"+screenModuleObject);
            if (((String)screenModuleObject.get("screenName")).equals("Loan Closing")) {
                objGoldLoanScreenModuleObject.putAll(screenModuleObject);
            }
            
            if (((String) screenModuleObject.get("screenName")).equals("Data Correction")) {
                //System.out.println("user id :: " + TrueTransactMain.USER_ID);
                //System.out.println("TrueTransactMain.USERINFO :: " + TrueTransactMain.USERINFO);
                if (!TrueTransactMain.USERINFO.containsKey("CORRECTION_ALLOWED")) {
                    continue;
                } else if (TrueTransactMain.USERINFO.containsKey("CORRECTION_ALLOWED") && ((TrueTransactMain.USERINFO.get("CORRECTION_ALLOWED") == null || (TrueTransactMain.USERINFO.get("CORRECTION_ALLOWED") != null && !TrueTransactMain.USERINFO.get("CORRECTION_ALLOWED").equals("Y"))))) {
                    continue;
                }
            }
            
            
            childNode=new ScreenModuleTreeNode(screenModuleObject,false);
            root=(ScreenModuleTreeNode)tre.getRoot();
            position=nodeExist(root,childNode);
            if(position<0){
                objHashMap=new HashMap();
                objHashMap.put("moduleSlNo",screenModuleObject.get("moduleSlNo"));
                objHashMap.put("moduleId",screenModuleObject.get("moduleId"));
                objHashMap.put("moduleName",screenModuleObject.get("moduleName"));
                objHashMap.put("newAllowed",screenModuleObject.get("newAllowed"));
                objHashMap.put("editAllowed",screenModuleObject.get("editAllowed"));
                objHashMap.put("deleteAllowed",screenModuleObject.get("deleteAllowed"));
                objHashMap.put("authRejAllowed",screenModuleObject.get("authRejAllowed"));
                objHashMap.put("exceptionAllowed",screenModuleObject.get("exceptionAllowed"));
                objHashMap.put("printAllowed",screenModuleObject.get("printAllowed"));
//                System.out.println("^^^^^^^^^^^^^"+i+"^^newAllowed:"+objHashMap.get("newAllowed"));
//                System.out.println("^^^^^^^^^^^^^"+i+"^^editAllowed:"+objHashMap.get("editAllowed"));
                dn=new ScreenModuleTreeNode(objHashMap,true);
                dn.insert(childNode,getInsertPosition(dn,childNode,false));
                root.insert(dn,getInsertPosition(root,dn,true));
                tre.setRoot(root);
            }else{
                
//                objHashMap=new HashMap();
//                objHashMap.put("moduleSlNo",screenModuleObject.get("moduleSlNo"));
//                objHashMap.put("moduleId",screenModuleObject.get("moduleId"));
//                objHashMap.put("moduleName",screenModuleObject.get("moduleName"));
//                objHashMap.put("newAllowed",screenModuleObject.get("newAllowed"));
//                objHashMap.put("editAllowed",screenModuleObject.get("editAllowed"));
//                objHashMap.put("deleteAllowed",screenModuleObject.get("deleteAllowed"));
//                objHashMap.put("authRejAllowed",screenModuleObject.get("authRejAllowed"));
//                objHashMap.put("exceptionAllowed",screenModuleObject.get("exceptionAllowed"));
//                objHashMap.put("printAllowed",screenModuleObject.get("printAllowed"));
//                System.out.println("^^^^^^^^^^^^^"+i+"^^newAllowed:"+objHashMap.get("newAllowed"));
//                System.out.println("^^^^^^^^^^^^^"+i+"^^editAllowed:"+objHashMap.get("editAllowed"));
//                next=new ScreenModuleTreeNode(objHashMap,false);
                
                next=(ScreenModuleTreeNode)root.getChildAt(position);
                next.insert(childNode,getInsertPosition(next,childNode,false));
            }
        }
//        HashMap resultMap = new HashMap();
//        
//        resultMap.put(CommonConstants.MAP_WHERE, new HashMap());
//      //  resultMap.put(CommonConstants.MAP_NAME, "TermLoan.getGoldLoanProdIDs");
//        try {
//            resultMap = proxy.executeQuery(resultMap,map);
//            goldLoanProdIDs =(ArrayList)resultMap.get(CommonConstants.MAP_NAME);
//            if (goldLoanProdIDs!=null && goldLoanProdIDs.size()>0) {
//                if (objGoldLoanScreenModuleObject!=null && objGoldLoanScreenModuleObject.size()>0) {
//                    for (int i=0; i<goldLoanProdIDs.size(); i++) {
//                        resultMap = (HashMap)goldLoanProdIDs.get(i);
//                        objGoldLoanScreenModuleObject.put("moduleName","Term Loan");
//                        objGoldLoanScreenModuleObject.put("screenName",resultMap.get("PROD_DESC"));
//                        screenModuleObject = new HashMap();
//                        screenModuleObject.putAll(objGoldLoanScreenModuleObject);
//                        childNode=new ScreenModuleTreeNode(screenModuleObject,false);
//                        root=(ScreenModuleTreeNode)tre.getRoot();
//                        position=nodeExist(root,childNode);
//                        if(position>=0) {
//                            next=(ScreenModuleTreeNode)root.getChildAt(position);
//                            next.insert(childNode,getInsertPosition(next,childNode,false));
//                        }
//                    }
//                }
//            }
//        } catch( Throwable e ) {
//            setResult(ClientConstants.ACTIONTYPE_FAILED);
//            setResultStatus();
//            System.err.println( "Exception " + e.toString() + "Caught" );
//            e.printStackTrace();
//        }
   //  resultMap = null;
        return tre;
    }
    
    /** Find the insert position for a node depending on its serial No  */
    
    public int getInsertPosition(ScreenModuleTreeNode node,ScreenModuleTreeNode nodeTo,boolean isModule){
        if(node.getChildCount()==0)
            return node.getChildCount();
        int levelNumber=0;
        int slNo=0;
        ScreenModuleTreeNode lastNode;
        levelNumber=getSlNo(nodeTo, isModule);
        Enumeration e=node.children();
        while(e.hasMoreElements()){
            lastNode=(ScreenModuleTreeNode)e.nextElement();
            slNo=getSlNo(lastNode,isModule);
            if(slNo>levelNumber)
                return node.getIndex(lastNode);
        }
        return node.getChildCount();
    }
    
    /** Get Serial No depending on whether its module or screen */
    
    private int getSlNo(ScreenModuleTreeNode node,boolean isModule){
        objHashMap=(HashMap)node.getUserObject();
        if(isModule)
            return ((BigDecimal)objHashMap.get("moduleSlNo")).intValue();
        else
            return ((BigDecimal)objHashMap.get("screenSlNo")).intValue();
    }
    
    /** Take TreeModel and form a arraylist of leaf nodes */
    
//    public ArrayList createArrayList(DefaultTreeModel treeModel){
//        ScreenMasterTO obj;
//        ScreenModuleTreeNode moduleNode,childNode;
//        ArrayList arr=new ArrayList();
//        ScreenModuleTreeNode root=(ScreenModuleTreeNode)treeModel.getRoot();
//        Enumeration e=root.children();
//        int childCount=0;
//        while(e.hasMoreElements()){
//            moduleNode=(ScreenModuleTreeNode)e.nextElement();
//            childCount=moduleNode.getChildCount();
//            for(int i=0;i<childCount;i++){
//                obj=new ScreenMasterTO();
//                childNode=(ScreenModuleTreeNode)moduleNode.getChildAt(i);
//                obj.setScreenId((String)((HashMap)childNode.getUserObject()).get("screenId"));
//                arr.add(obj);
//            }
//        }
        
     public ArrayList createArrayList(DefaultTreeModel treeModel){
        GroupScreensTO obj;
        ScreenModuleTreeNode moduleNode,childNode;
        ArrayList arr=new ArrayList();
        ScreenModuleTreeNode root=(ScreenModuleTreeNode)treeModel.getRoot();
        Enumeration e=root.children();
        int childCount=0;
        HashMap childMap;
        while(e.hasMoreElements()){
            moduleNode=(ScreenModuleTreeNode)e.nextElement();
            childCount=moduleNode.getChildCount();
            for(int i=0;i<childCount;i++){
                obj=new GroupScreensTO();
                childNode=(ScreenModuleTreeNode)moduleNode.getChildAt(i);
                childMap = (HashMap)childNode.getUserObject();
//                System.out.println("childMap : " + childMap);
                obj.setScreenId((String)childMap.get("screenId"));
//                System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$i:"+i);
                System.out.println("childNode.getChkNewAllowed():"+childMap.get("newAllowed"));
                obj.setNewAllowed((String)childMap.get("newAllowed"));
                obj.setEditAllowed((String)childMap.get("editAllowed"));
                obj.setDeleteAllowed((String)childMap.get("deleteAllowed"));
                obj.setAuthRejAllowed((String)childMap.get("authRejAllowed"));
                obj.setExceptionAllowed((String)childMap.get("exceptionAllowed"));
                obj.setPrintAllowed((String)childMap.get("printAllowed"));
                obj.setInterbranchAllowed((String)childMap.get("interbranchAllowed"));
//                obj.setEditAllowed(childNode.getChkEditAllowed());
//                obj.setDeleteAllowed(childNode.getChkDeleteAllowed());
//                obj.setAuthRejAllowed(childNode.getChkAuthRejAllowed());
//                obj.setExceptionAllowed(childNode.getChkExceptionAllowed());
//                obj.setPrintAllowed(childNode.getChkPrintAllowed());
                arr.add(obj);
            }
        }
        return arr;
    }
    
    /** Include/Exculde method called when nodes are included/excluded from one tree
     * to other */
    
    public void includeExclude(JTree excludeTree,JTree includeTree){
        TreePath[] parentPaths = excludeTree.getSelectionPaths();
        if(parentPaths!=null){
            TreePath parentPath;
            for(int i=0;i<parentPaths.length;i++) {
                parentPath=parentPaths[i];
                if (parentPath!= null)
                    addSinglePath(parentPath,includeTree);
                ((DefaultTreeModel)excludeTree.getModel()).reload();
            }
        }
    }
    
    /** Check whether child exist in parent,if exist return childIndex in parent
     * node */
    
    private int nodeExist(DefaultMutableTreeNode parent,DefaultMutableTreeNode child){
        DefaultMutableTreeNode next;
        //HashMap nextObj;
        Enumeration e=parent.children();
        HashMap screenModuleObject=(HashMap)child.getUserObject();
        while(e.hasMoreElements()){
            next = (DefaultMutableTreeNode)e.nextElement();
            objHashMap=(HashMap)next.getUserObject();
            if(((String)screenModuleObject.get("moduleId")).equals((String)objHashMap.get("moduleId")))
                return parent.getIndex(next);
        }
        return -1;
    }
    
    /** To set values for rights allowed fields like new,edit,etc. **/
    private ScreenModuleTreeNode setAllowedParams(ScreenModuleTreeNode objNode){        
//            if (branchGroup)
//                groupMasterObj.setBranchGroup(objCommonRB.getString("YES"));
//            else
//                groupMasterObj.setBranchGroup(objCommonRB.getString("NO"));
        objNode.setChkNewAllowed(getChkNewAllowed());         
        objNode.setChkEditAllowed(getChkEditAllowed());         
        objNode.setChkDeleteAllowed(getChkDeleteAllowed());         
        objNode.setChkAuthRejAllowed(getChkAuthRejAllowed());
        objNode.setChkExceptionAllowed(getChkExceptionAllowed());          
        objNode.setChkPrintAllowed(getChkPrintAllowed());
        return objNode;
    }
    
    /** Add childnode of selected tree path*/
    
    private void addSinglePath(TreePath parentPath,JTree includeTree){
        ScreenModuleTreeNode parentNode,childNode,next;
        Enumeration e;
        int  position=0;
        DefaultTreeModel t1= (DefaultTreeModel)includeTree.getModel();
        ScreenModuleTreeNode root=(ScreenModuleTreeNode)t1.getRoot();
        childNode = (ScreenModuleTreeNode)(parentPath.getLastPathComponent());
        parentNode = (ScreenModuleTreeNode)childNode.getParent();
        
   /*Check if childnode is module,if so next check whether that module is already
            added or not */
        if(root.toString().equals(parentNode.toString())){
            position=nodeExist(root,childNode);
            if(position>=0){
                parentNode=(ScreenModuleTreeNode)root.getChildAt(position);
                while(childNode.getChildCount()!=0){
                    next = (ScreenModuleTreeNode)childNode.getFirstChild();                    
                    next = setAllowedParams(next);                    
                    parentNode.insert(next,getInsertPosition(parentNode,next,false));
                }
                childNode.removeFromParent();
            }else{
                parentNode=childNode;
                int childCount = childNode.getChildCount();
                for (int i =0;i<childCount;i++){
                    next = (ScreenModuleTreeNode)childNode.getChildAt(i);                     
                    next = setAllowedParams(next);                     
                    parentNode.insert(next,i);                    
                }
//                childNode.removeFromParent();
                root.insert(parentNode,getInsertPosition(root,childNode,true));   
            }
        }
        
   /*If childnode is leaf then check its parent exist or not,if not add parent
         module and then add childnode */
        else {
            position=nodeExist(root,childNode);
            if(position<0){
                next=new ScreenModuleTreeNode((HashMap)parentNode.getUserObject(),true);
                next.removeAllChildren();  
                    
                childNode = setAllowedParams(childNode);
                    
                next.insert(childNode,getInsertPosition(next,childNode,false));
                root.insert(next,getInsertPosition(root,next,true));
                t1.setRoot(root);
            }else {
                next=(ScreenModuleTreeNode)root.getChildAt(position);                    
                childNode = setAllowedParams(childNode);                    
                next.insert(childNode,getInsertPosition(next,childNode,false));
            }
            if(parentNode.getChildCount()==0)
                parentNode.removeFromParent();
        }
        t1.reload();
    }
    
    public void xp(){
//        System.out.println("objHashMap:"+objHashMap);
//        setChkNewAllowed(objHashMap.get("newAllowed"));
//        setChkEditAllowed(objHashMap.getChkEditAllowed());
//        setChkDeleteAllowed(objHashMap.getChkDeleteAllowed());
//        setChkAuthRejAllowed(objHashMap.getChkAuthRejAllowed());
//        setChkExceptionAllowed(objHashMap.getChkExceptionAllowed());
//        setChkPrintAllowed(objHashMap.getChkPrintAllowed());
        
//        updateRights();
    }
    
    /**
     * Getter for property goldLoanProdIDs.
     * @return Value of property goldLoanProdIDs.
     */
    public java.util.ArrayList getGoldLoanProdIDs() {
        return goldLoanProdIDs;
    }
    
    /**
     * Setter for property goldLoanProdIDs.
     * @param goldLoanProdIDs New value of property goldLoanProdIDs.
     */
    public void setGoldLoanProdIDs(java.util.ArrayList goldLoanProdIDs) {
        this.goldLoanProdIDs = goldLoanProdIDs;
    }
    
}
