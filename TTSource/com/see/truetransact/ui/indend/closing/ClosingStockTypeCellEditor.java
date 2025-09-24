/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * ClosingStockTypeCellEditor.java
 */

package com.see.truetransact.ui.indend.closing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * A custom editor for cells in the ClosingStockType column.
 * @author user
 */
public class ClosingStockTypeCellEditor extends AbstractCellEditor
        implements TableCellEditor, ActionListener {
 
    private ClosingStockType closingStockType;
    private List<ClosingStockType> listClosingStockType;
     
    public ClosingStockTypeCellEditor(List<ClosingStockType> listClosingStockType) {
        this.listClosingStockType = listClosingStockType;
    }
     
    @Override
    public Object getCellEditorValue() {
        return this.closingStockType;
    }
 
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        if (value instanceof ClosingStockType) {
            this.closingStockType = (ClosingStockType) value;
        }
         
        JComboBox<ClosingStockType> comboCountry = new JComboBox<ClosingStockType>();
         
        for (ClosingStockType aClosingStockType : listClosingStockType) {
            comboCountry.addItem(aClosingStockType);
        }
         
        comboCountry.setSelectedItem(closingStockType);
        comboCountry.addActionListener(this);
         
        if (isSelected) {
            comboCountry.setBackground(table.getSelectionBackground());
        } else {
            comboCountry.setBackground(table.getSelectionForeground());
        }
         
        return comboCountry;
    }
 
    @Override
    public void actionPerformed(ActionEvent event) {
        JComboBox<ClosingStockType> comboCountry = (JComboBox<ClosingStockType>) event.getSource();
        this.closingStockType = (ClosingStockType) comboCountry.getSelectedItem();
    }
 
}