/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 * 
 * GroupTreeCellRenderer.java
 *
 * Created on April 8, 2004, 5:46 PM
 */

package com.see.truetransact.ui.sysadmin.group;

import java.awt.Component;
import java.util.HashMap;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author  pinky
 */

public class GroupTreeCellRenderer extends DefaultTreeCellRenderer {
    
    /** Creates a new instance of GroupTreeCellRenderer */
    public GroupTreeCellRenderer() {
        
    }
    public Component getTreeCellRendererComponent(
                        JTree tree,
                        Object value,
                        boolean sel,
                        boolean expanded,
                        boolean leaf,
                        int row,
                        boolean hasFocus) {

        super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);
        HashMap obj= (HashMap)((ScreenModuleTreeNode)value).getUserObject();        
        if (leaf) {                        
            setToolTipText((String)obj.get("screenClass"));
        } else {            
            setToolTipText((String)obj.get("moduleName")); 
        } 
        return this;
    }       
}
