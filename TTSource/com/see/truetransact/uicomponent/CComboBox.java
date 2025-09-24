/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * CComboBox.java
 *
 * Created on July 28, 2003, 10:37 AM
 */

package com.see.truetransact.uicomponent;

import java.awt.Dimension;

/**
 *
 * @author  annamalai_t1, bala
 */
public class CComboBox extends javax.swing.JComboBox {
    private String helpMessage;
    private javax.swing.JLabel lblDisplay;
    protected int popupWidth;
    
    /** Creates a new instance of CComboBox */
    public CComboBox() {
//        super.setRenderer(new MyCellRenderer());
        helpFocus();
        setFont(new java.awt.Font("MS Sans Serif",java.awt.Font.PLAIN,12));
        setUI(new SteppedComboBoxUI());
        popupWidth = 0;        
//        ListCellRenderer r = super.getRenderer();
////        ((JLabel) r).setHorizontalAlignment(SwingConstants.CENTER);
//        ((JLabel) r).setMinimumSize(new java.awt.Dimension(500, 20));
//        ((JLabel) r).setPreferredSize(new java.awt.Dimension(500, 20));
//        ((JLabel) r).setMaximumSize(new java.awt.Dimension(500, 20));
////        ((JLabel) r).setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED));
////        ((JLabel) r).setIcon (javax.swing.plaf.metal.MetalIconFactory.getFileChooserNewFolderIcon());

        // List
//        setRenderer(getRenderer());
    }
    
    private void helpFocus() {
//        setMinimumSize(new java.awt.Dimension(100, 21));
        setPreferredSize(new java.awt.Dimension(100, 21));
        addFocusListener(new HelpFocusAdapter());

        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                if (evt.getKeyChar() == '\n') {
                    nextFocus();
                }
            }
        });
    }

    public void setPopupWidth(int width) {
        popupWidth = width;
    }

    public Dimension getPopupSize() {
        Dimension size = getSize();
        if (popupWidth < 1)
            popupWidth = size.width;
        return new Dimension(popupWidth, size.height);
    }    
    
    public void setSelectedItem(Object obj) {
        super.setSelectedItem(obj);
        if (super.getSelectedItem() != null && super.getSelectedItem().toString().length() > 0) {
            super.setToolTipText(super.getSelectedItem().toString());
        }
    }
    
    private class HelpFocusAdapter extends java.awt.event.FocusAdapter {
        public void focusGained(java.awt.event.FocusEvent evt) {
            if (lblDisplay != null) lblDisplay.setText(helpMessage);
        }
        public void focusLost(java.awt.event.FocusEvent evt) {
            if (lblDisplay != null) lblDisplay.setText("");
        }
    }
    
    public void setHelpMessage(javax.swing.JLabel lblDisplay, String helpMessage) {
        this.helpMessage = helpMessage;
        this.lblDisplay = lblDisplay;
    }
    public String getHelpMessage() {
        return this.helpMessage;
    }
     
//    /*List*/
//    public ListCellRenderer getRenderer() {
//        DefaultListCellRenderer renderer = new DefaultListCellRenderer() {
//            public Component getListCellRendererComponent(
//            javax.swing.JList list,
//            Object value,
//            int index,
//            boolean isSelected,
//            boolean cellHasFocus) {
//                list.setMinimumSize(new java.awt.Dimension(400, 50));
//                list.setPreferredSize(new java.awt.Dimension(400, 50));
//                list.setMaximumSize(new java.awt.Dimension(400, 50));
////                javax.swing.JLabel cell =
//                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
////                list.setHorizontalTextPosition(SwingConstants.LEFT);
////                return cell;
//            }
//        };
//        
//        return renderer;
//    }/**/
}



//class MyCellRenderer extends DefaultListCellRenderer {
//    
//    public Component getListCellRendererComponent(JList list, 
//                                                  Object value, 
//                                                  int index, 
//                                                  boolean isSelected, 
//                                                  boolean cellHasFocus) {
//        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
//        setToolTipText((String)value);
//        return this;
//    }
//}
