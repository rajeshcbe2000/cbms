/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * CTable.java
 *
 * Created on July 28, 2003, 10:37 AM
 */

package com.see.truetransact.uicomponent;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JTable;
import java.awt.Component;
import java.awt.Color;
/**
 *
 * @author  bala
 */
public class CTable extends javax.swing.JTable {
    private Color alternateColor = new Color(225, 225, 225);
    javax.swing.border.Border selected = new javax.swing.border.LineBorder(Color.GREEN);

    /** Creates a new instance of CTable */
    public CTable() {
        getTableHeader().setReorderingAllowed(false);
    }
    
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        
        if (!isRowSelected(row)) {
            c.setBackground(row % 2 == 0 ? null : alternateColor );
        }
        
        if (c instanceof javax.swing.JComponent) {
            if (isRowSelected(row) && isColumnSelected(column)) ((javax.swing.JComponent)c).setBorder(selected);
        }
        
        return c;
    }
    
    public void setReorderingAllowed(boolean yesno) {
        getTableHeader().setReorderingAllowed(yesno);
    }
}
