/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * ClosingStockTypeRenderer.java
 */

package com.see.truetransact.ui.indend.closing;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * A custom renderer for cells in the ClosingStockTypeRenderer column.
 * @author user
 */

public class ClosingStockTypeRenderer extends DefaultTableCellRenderer {
     
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof ClosingStockType) {
            ClosingStockType country = (ClosingStockType) value;
            setText(country.getName());
        }
         
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getSelectionForeground());
        }
         
        return this;
    }
     
}