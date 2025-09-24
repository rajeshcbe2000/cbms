/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TableColorRenderer.java
 *
 * Created on February 16, 2004, 12:17 PM
 */

package com.see.truetransact.clientutil;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JTable;

import java.awt.Component;
import java.awt.Color;
import java.util.HashMap;
/**
 *
 * @author  bala
 */
public class TableColorRenderer extends DefaultTableCellRenderer {
    private HashMap _colorMap = new HashMap();
    
    private Color foreColor;
    
    private Color backColor;
    
    /** Creates a new instance of TableColorRenderer */
    public TableColorRenderer(HashMap colorMap) {
        _colorMap = colorMap;
    }
    
    
    
    public Component getTableCellRendererComponent( 
        JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
            
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, 
            row, column);
        
        column = table.convertColumnIndexToModel(column);
        Integer col = new Integer(column);
        if (_colorMap.containsKey(col)) {
            Object val = table.getModel().getValueAt(row, column);
            if (column== col.intValue() &&  
                    (val != null && !"".equalsIgnoreCase(val.toString()))){
                setBackground((Color) _colorMap.get(new Integer(column)));
                setForeground(getForeground());
            }
        }
        
        if (isSelected)  {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        }
        /* Set oquae */
        this.setOpaque(true);
        
        return this;
    }
    
    /** Getter for property foreColor.
     * @return Value of property foreColor.
     *
     */
    public java.awt.Color getForeColor() {
        return foreColor;
    }
    
    /** Setter for property foreColor.
     * @param foreColor New value of property foreColor.
     *
     */
    public void setForeColor(java.awt.Color foreColor) {
        this.foreColor = foreColor;
    }
    
    /** Getter for property backColor.
     * @return Value of property backColor.
     *
     */
    public java.awt.Color getBackColor() {
        return backColor;
    }
    
    /** Setter for property backColor.
     * @param backColor New value of property backColor.
     *
     */
    public void setBackColor(java.awt.Color backColor) {
        this.backColor = backColor;
    }
    
}
