/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ListReports.java
 *
 * Created on November 23, 2005, 3:05 PM
 */

package com.see.truetransact.ui.common;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author  Bala
 */
public class ListReports {
    String optSelected = null;
    JDialog dialog;
    JLabel lblMsg;
    JList list;
    JButton btnOk;
    JButton btnCancel;
    /** Creates a new instance of ListReports */
    public ListReports() {
    }
    public String populateCombo(Object[] reports)
     {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            dialog = new JDialog();
            GridBagConstraints gridBagConstraints;
            dialog.setTitle("List Report");
            lblMsg = new JLabel("Please choose a report..:");
            list = new JList(reports);
            btnOk = new JButton("OK");
            btnCancel = new JButton("Cancel");
            btnOk.setMnemonic(KeyEvent.VK_O);
            btnCancel.setMnemonic(KeyEvent.VK_C);
            dialog.getContentPane().setLayout(null);
            lblMsg.setBounds(5,10,250,20);
            list.setSize(250,170);
            btnOk.setBounds(68,213,57,25);
            btnCancel.setBounds(140,213,70,25);
            JScrollPane scrollPane = new JScrollPane(list);
            scrollPane.setBounds(5,35,250,170);
            dialog.getContentPane().add(lblMsg);            
            dialog.getContentPane().add(scrollPane);
            dialog.getContentPane().add(btnOk);            
            dialog.getContentPane().add(btnCancel);
            dialog.setSize(268,272);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.setSelectedIndex(0);
            list.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent evt) {
                    if ((evt.getClickCount() == 2)) {
                        System.out.println("double clicked");
                        listMousePressed(evt);
                    }
                }
            });
            btnOk.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    System.out.println("Button Click : "+evt.getActionCommand());
                    if (list.getSelectedValue()!=null) {
                        listMousePressed(null);
                    }
                }
            });
            btnCancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    System.out.println("Button Click : "+evt.getActionCommand());
                    optSelected = null;
                    dialog.dispose();
                }
            });
            dialog.setModal(true);
            dialog.setLocation((screenSize.width-dialog.getWidth())/2, (screenSize.height-dialog.getHeight())/2);
            dialog.show();
            System.out.println("Chosen report is "+optSelected);
            return optSelected;
    }
    
    private void listMousePressed(MouseEvent evt) {
        String selected = (String)list.getSelectedValue();
        optSelected = selected;
        System.out.println("Chosen report is "+optSelected);
        dialog.dispose();
    }
}
