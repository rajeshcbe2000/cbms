/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ScreenModuleTreeNode.java
 *
 * Created on April 7, 2004, 5:52 PM
 */

package com.see.truetransact.ui.sysadmin.group;

/**
 *
 * @author  pinky
 */

import java.util.HashMap;

public class ScreenModuleTreeNode extends javax.swing.tree.DefaultMutableTreeNode {
    private boolean isModule;
    
    private String chkNewAllowed = "N";
    private String chkEditAllowed = "N";
    private String chkDeleteAllowed = "N";
    private String chkAuthRejAllowed = "N";
    private String chkExceptionAllowed = "N";
    private String chkPrintAllowed = "N";
    
    /** Creates a new instance of ScreenModuleTreeNode */
    public ScreenModuleTreeNode() {
    }
    public ScreenModuleTreeNode(Object object,boolean isModule) {
        super(object);
        this.isModule=isModule;
    }

    void setChkNewAllowed(boolean chkNewAllowed){
        if (chkNewAllowed) this.chkNewAllowed = "Y";
        else {this.chkNewAllowed = "N";
//                System.out.println("!!!!!!!!!!!!!!!!!!!!!THIS SHOULD GET PRINTED");
//        System.out.println("ooooooooooooooooooooooops");
        }
    }
    String getChkNewAllowed(){
        return this.chkNewAllowed;
    }
    
    void setChkEditAllowed(boolean chkEditAllowed){
        
        if (chkEditAllowed) this.chkEditAllowed = "Y";
        else this.chkEditAllowed = "N";
    }
    String getChkEditAllowed(){
        return this.chkEditAllowed;
    }
    
    void setChkDeleteAllowed(boolean chkDeleteAllowed){
        if (chkDeleteAllowed) this.chkDeleteAllowed = "Y";
        else this.chkDeleteAllowed = "N";
//        setChanged();
    }
    String getChkDeleteAllowed(){
        return this.chkDeleteAllowed;
    }
    
    void setChkAuthRejAllowed(boolean chkAuthRejAllowed){
        if (chkAuthRejAllowed) this.chkAuthRejAllowed = "Y";
        else this.chkAuthRejAllowed = "N";
//        setChanged();
    }
    String getChkAuthRejAllowed(){
        return this.chkAuthRejAllowed;
    }
    
    void setChkExceptionAllowed(boolean chkExceptionAllowed){
        if (chkExceptionAllowed) this.chkExceptionAllowed = "Y";
        else this.chkExceptionAllowed = "N";
//        setChanged();
    }
    String getChkExceptionAllowed(){
        return this.chkExceptionAllowed;
    }
    
    void setChkPrintAllowed(boolean chkPrintAllowed){
        if (chkPrintAllowed) this.chkPrintAllowed = "Y";
        else this.chkPrintAllowed = "N";
//        setChanged();
    }
    String getChkPrintAllowed(){
        return this.chkPrintAllowed;
    }
    
    public String toString(){
        if(this.isModule)
            return (String)(((HashMap)getUserObject()).get("moduleName"));
        else
            return (String)(((HashMap)getUserObject()).get("screenName"));
    }
    
    /**
     * Getter for property isModule.
     * @return Value of property isModule.
     */
    public boolean isModule() {
        return isModule;
    }
    
    /**
     * Setter for property isModule.
     * @param isModule New value of property isModule.
     */
    public void setModule(boolean isModule) {
        this.isModule = isModule;
    }
    
}