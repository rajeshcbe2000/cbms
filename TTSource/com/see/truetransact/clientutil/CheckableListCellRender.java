/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * CheckableListCellRender.java
 */
 
package com.see.truetransact.clientutil;

import javax.swing.*;

public class CheckableListCellRender extends JCheckBox implements ListCellRenderer {
    
    public CheckableListCellRender() {
        setBackground(UIManager.getColor("List.textBackground"));
        setForeground(UIManager.getColor("List.textForeground"));
    }
    /**
     * Return a component that has been configured to display the specified
     * value. That component's <code>paint</code> method is then called to
     * "render" the cell.  If it is necessary to compute the dimensions
     * of a list because the list cells do not have a fixed size, this method
     * is called to generate a component on which <code>getPreferredSize</code>
     * can be invoked.
     *
     * @param list The JList we're painting.
     * @param value The value returned by list.getModel().getElementAt(index).
     * @param index The cells index.
     * @param isSelected True if the specified cell was selected.
     * @param cellHasFocus True if the specified cell has the focus.
     * @return A component whose paint() method will render the specified value.
     *
     * @see JList
     * @see ListSelectionModel
     * @see ListModel
     */
    public java.awt.Component getListCellRendererComponent(
    JList list, Object value,  int index, boolean isSelected, boolean cellHasFocus) {
        setEnabled(list.isEnabled());
        setSelected(((CheckableItem) value).isSelected());
        setFont(list.getFont());
        setText(value.toString());
        return this;
        
    }
}
