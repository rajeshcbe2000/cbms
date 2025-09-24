/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MalayalamKeyboardUI.java
 *
 * Created on January 20, 2004, 10:45 AM
 */

package com.see.truetransact.ui.common.viewall;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
//import com.see.truetransact.clientutil.TableColorRenderer;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.ui.transaction.cash.CashTransactionUI;
import com.see.truetransact.clientutil.EnhancedTableModel;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.Dimension;
import java.util.Observable;
/**
 * Author   : Chithra
 * Location : Thrissur
 * Date of Completion : 14-05-2015
 */
public class MalayalamKeyboardUI extends com.see.truetransact.uicomponent.CDialog {
    boolean isShow = true;
    CashTransactionUI cashUI=null;
    EnhancedTableModel enhancedTable=new EnhancedTableModel();
    //Added By Suresh
    private String finalTxt = "";

    public String getFinalTxt() {
        return finalTxt;
    }

    public void setFinalTxt(String finalTxt) {
        this.finalTxt = finalTxt.trim();
    }

    
    /** Creates new form TableDataUI */
    public MalayalamKeyboardUI(String mapName,String txtVal,int position,String txtEnglish) {
        initComponents();
        setLabalForall();
        initSetup(mapName, null);
        txtValue.setText(txtVal);
        if(position>0)
        txtValue.setCaretPosition(position);
    }
    
    /** Creates new form TableDataUI */
    public MalayalamKeyboardUI(String mapName, HashMap whereConditionMap) {
        initComponents();
        setLabalForall();
        initSetup(mapName, whereConditionMap);
       
    }
     /** Creates new form TableDataUI */
    public MalayalamKeyboardUI(String mapName, HashMap whereConditionMap ,String string) {
        initComponents();
        initSetup(mapName, whereConditionMap);
        setTotalAmt();
    }
    /** Creates new form TableDataUI */
    public MalayalamKeyboardUI(String mapName, HashMap whereConditionMap,CashTransactionUI cashTransactionUI) {
        initComponents();
        initSetup(mapName, whereConditionMap);
        setTotalAmt();
        cashUI=new CashTransactionUI();
        cashUI=cashTransactionUI;
    }
    public void setLabalForall() {
        String val = "\u00A1";
        btnI66.setLabel(val);
        val = "\u00AB";
        btnI64.setLabel(val);
        val = "\u00AE";
        btnI63.setLabel(val);
        val = "\u00B6";
        btnI62.setLabel(val);
        val = "\u00AF";
        btnI61.setLabel(val);
        val = "\u00AA";
        btnI60.setLabel(val);
        val = "\u00A5";
        btnI59.setLabel(val);
        val = "\u00A8";
        btnI58.setLabel(val);
        val = "\u00D6";
        btnI89.setLabel(val);
        val = "\u00B1";
        btnI85.setLabel(val);
        val = "\u00B8";
        btnI84.setLabel(val);
        val = "\u00BA";
        btnI83.setLabel(val);
        val = "\u00BD";
        btnI81.setLabel(val);
        val = "\u00BF";
        btnI79.setLabel(val);
        val = "\u00C3";
        btnI77.setLabel(val);
        val = "\u00C6";
        btnI76.setLabel(val);
        val = "\u00C7";
        btnI74.setLabel(val);
        val = "\u00CA";
        btnI73.setLabel(val);
        val = "\u00C5";
        btnI71.setLabel(val);
        val = "\u00C1";
        btnI70.setLabel(val);
        val = "\u00C2";
        btnI69.setLabel(val);
        val = "\u00AC";
        btnI65.setLabel(val);
        val = "\u00B3";
        btnI68.setLabel(val);
        val = "\u00C0";
        btnI86.setLabel(val);
        val = "\u00C4";
        btnI99.setLabel(val);
        val = "\u00E2";
        btnI97.setLabel(val);
        val = "\u00D8";
        btnI98.setLabel(val);
        val = "\u00A3";
        btnI96.setLabel(val);
        val = "\u00DA";
        btnI94.setLabel(val);
        val = "\u00B4";
        btnI95.setLabel(val);
        val = "\u00BC";
        btnI93.setLabel(val);
        val = "\u00A7";
        btnI92.setLabel(val);
        val = "\u00AD";
        btnI91.setLabel(val);
        val = "\u00B5";
        btnI90.setLabel(val);
        val = "\u00A6";
        btnI88.setLabel(val);
        val = "\u00A9";
        btnI87.setLabel(val);
        val = "\u00A2";
        btnI100.setLabel(val);
        
    }
    private void setTotalAmt() {
        double totAmt = 0;
        double totAmtWithWave = 0;
        
    }
    
    private void initSetup(String mapName, HashMap whereConditionMap) {
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.MAP_NAME, mapName);
        
        if (whereConditionMap != null) {
            whereMap.put(CommonConstants.MAP_WHERE, whereConditionMap);
        }
        
            isShow = true;
        
        /*HashMap map = new HashMap();
        map.put (new Integer("1"), java.awt.Color.yellow);
        TableColorRenderer renderer = new TableColorRenderer(map);
        tblDenomination.setDefaultRenderer(Object.class, renderer);*/
    }
    
    public void show() {
        if (isShow) {
            Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            pack();
            System.out.println("hereeeeeeeeeeeeeeeeeeee");
            /* Center frame on the screen */
            Dimension frameSize = getSize();
            if (frameSize.height > screenSize.height)
                frameSize.height = screenSize.height;
            if (frameSize.width > screenSize.width)
                frameSize.width = screenSize.width;
            setLocation((screenSize.width - frameSize.width) / 2,
            (screenSize.height - frameSize.height) / 2);
            setModal(true);
            super.show();
        }
    }
    private void setValueonText(String ch){
        int p = txtValue.getCaretPosition();
        String val = txtValue.getText();
        System.out.println("ppppppppp :" + p);
        System.out.println("val.length() :" + val.length());
        ch = ch.trim();
        if (p > 0 && p < val.length()) {
            String curval = val.substring(0, p) + ch + val.substring(p, val.length());
            txtValue.setText(curval);
            txtValue.setCaretPosition(p + ch.length());
        } else if (p == val.length()) {
            txtValue.setText(val + ch);
            txtValue.setCaretPosition(p + ch.length());
        } else if (p == 0) {
            txtValue.setText(ch + val);
            txtValue.setCaretPosition(p + ch.length());
        }
        if (val.length() <= 0) {
            txtValue.setText(ch);
            txtValue.setCaretPosition(1); 
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        panDenomination = new com.see.truetransact.uicomponent.CPanel();
        scrDenomination = new com.see.truetransact.uicomponent.CScrollPane();
        panKeyboard = new com.see.truetransact.uicomponent.CPanel();
        btnA = new com.see.truetransact.uicomponent.CButton();
        btnB = new com.see.truetransact.uicomponent.CButton();
        btnC = new com.see.truetransact.uicomponent.CButton();
        btnD = new com.see.truetransact.uicomponent.CButton();
        btnE = new com.see.truetransact.uicomponent.CButton();
        btnF = new com.see.truetransact.uicomponent.CButton();
        btnA6 = new com.see.truetransact.uicomponent.CButton();
        btnA7 = new com.see.truetransact.uicomponent.CButton();
        btnA8 = new com.see.truetransact.uicomponent.CButton();
        btnA9 = new com.see.truetransact.uicomponent.CButton();
        btnA10 = new com.see.truetransact.uicomponent.CButton();
        btnA11 = new com.see.truetransact.uicomponent.CButton();
        btnA12 = new com.see.truetransact.uicomponent.CButton();
        btnA13 = new com.see.truetransact.uicomponent.CButton();
        btnI = new com.see.truetransact.uicomponent.CButton();
        btnI1 = new com.see.truetransact.uicomponent.CButton();
        btnI2 = new com.see.truetransact.uicomponent.CButton();
        btnI3 = new com.see.truetransact.uicomponent.CButton();
        btnI4 = new com.see.truetransact.uicomponent.CButton();
        btnI5 = new com.see.truetransact.uicomponent.CButton();
        btnI6 = new com.see.truetransact.uicomponent.CButton();
        btnI7 = new com.see.truetransact.uicomponent.CButton();
        btnI8 = new com.see.truetransact.uicomponent.CButton();
        btnI9 = new com.see.truetransact.uicomponent.CButton();
        btnI10 = new com.see.truetransact.uicomponent.CButton();
        btnI11 = new com.see.truetransact.uicomponent.CButton();
        btnI12 = new com.see.truetransact.uicomponent.CButton();
        btnI13 = new com.see.truetransact.uicomponent.CButton();
        btnI14 = new com.see.truetransact.uicomponent.CButton();
        btnI15 = new com.see.truetransact.uicomponent.CButton();
        btnI16 = new com.see.truetransact.uicomponent.CButton();
        btnI17 = new com.see.truetransact.uicomponent.CButton();
        btnI18 = new com.see.truetransact.uicomponent.CButton();
        btnI19 = new com.see.truetransact.uicomponent.CButton();
        btnI20 = new com.see.truetransact.uicomponent.CButton();
        btnI21 = new com.see.truetransact.uicomponent.CButton();
        btnI22 = new com.see.truetransact.uicomponent.CButton();
        btnI24 = new com.see.truetransact.uicomponent.CButton();
        btnI25 = new com.see.truetransact.uicomponent.CButton();
        btnI28 = new com.see.truetransact.uicomponent.CButton();
        btnI29 = new com.see.truetransact.uicomponent.CButton();
        btnI30 = new com.see.truetransact.uicomponent.CButton();
        btnI33 = new com.see.truetransact.uicomponent.CButton();
        btnI34 = new com.see.truetransact.uicomponent.CButton();
        btnI35 = new com.see.truetransact.uicomponent.CButton();
        btnI36 = new com.see.truetransact.uicomponent.CButton();
        btnI39 = new com.see.truetransact.uicomponent.CButton();
        btnI40 = new com.see.truetransact.uicomponent.CButton();
        btnI41 = new com.see.truetransact.uicomponent.CButton();
        btnI42 = new com.see.truetransact.uicomponent.CButton();
        btnI43 = new com.see.truetransact.uicomponent.CButton();
        btnI44 = new com.see.truetransact.uicomponent.CButton();
        btnI45 = new com.see.truetransact.uicomponent.CButton();
        btnI46 = new com.see.truetransact.uicomponent.CButton();
        btnI47 = new com.see.truetransact.uicomponent.CButton();
        btnI48 = new com.see.truetransact.uicomponent.CButton();
        btnI49 = new com.see.truetransact.uicomponent.CButton();
        btnI50 = new com.see.truetransact.uicomponent.CButton();
        btnI51 = new com.see.truetransact.uicomponent.CButton();
        btnI52 = new com.see.truetransact.uicomponent.CButton();
        btnI53 = new com.see.truetransact.uicomponent.CButton();
        btnI55 = new com.see.truetransact.uicomponent.CButton();
        btnI56 = new com.see.truetransact.uicomponent.CButton();
        btnI57 = new com.see.truetransact.uicomponent.CButton();
        btnI58 = new com.see.truetransact.uicomponent.CButton();
        btnI59 = new com.see.truetransact.uicomponent.CButton();
        btnI60 = new com.see.truetransact.uicomponent.CButton();
        btnI61 = new com.see.truetransact.uicomponent.CButton();
        btnI62 = new com.see.truetransact.uicomponent.CButton();
        btnI63 = new com.see.truetransact.uicomponent.CButton();
        btnI64 = new com.see.truetransact.uicomponent.CButton();
        btnI66 = new com.see.truetransact.uicomponent.CButton();
        btnI65 = new com.see.truetransact.uicomponent.CButton();
        btnI68 = new com.see.truetransact.uicomponent.CButton();
        btnI69 = new com.see.truetransact.uicomponent.CButton();
        btnI70 = new com.see.truetransact.uicomponent.CButton();
        btnI71 = new com.see.truetransact.uicomponent.CButton();
        btnI73 = new com.see.truetransact.uicomponent.CButton();
        btnI74 = new com.see.truetransact.uicomponent.CButton();
        btnI76 = new com.see.truetransact.uicomponent.CButton();
        btnI77 = new com.see.truetransact.uicomponent.CButton();
        btnI79 = new com.see.truetransact.uicomponent.CButton();
        btnI81 = new com.see.truetransact.uicomponent.CButton();
        btnI83 = new com.see.truetransact.uicomponent.CButton();
        btnI84 = new com.see.truetransact.uicomponent.CButton();
        btnI85 = new com.see.truetransact.uicomponent.CButton();
        btnI89 = new com.see.truetransact.uicomponent.CButton();
        btnI86 = new com.see.truetransact.uicomponent.CButton();
        btnI87 = new com.see.truetransact.uicomponent.CButton();
        btnI88 = new com.see.truetransact.uicomponent.CButton();
        btnI90 = new com.see.truetransact.uicomponent.CButton();
        btnI91 = new com.see.truetransact.uicomponent.CButton();
        btnI92 = new com.see.truetransact.uicomponent.CButton();
        btnI93 = new com.see.truetransact.uicomponent.CButton();
        btnI94 = new com.see.truetransact.uicomponent.CButton();
        btnI95 = new com.see.truetransact.uicomponent.CButton();
        btnI96 = new com.see.truetransact.uicomponent.CButton();
        btnI97 = new com.see.truetransact.uicomponent.CButton();
        btnI98 = new com.see.truetransact.uicomponent.CButton();
        btnI99 = new com.see.truetransact.uicomponent.CButton();
        txtValue = new com.see.truetransact.uicomponent.CTextField();
        btnCopy = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnI100 = new com.see.truetransact.uicomponent.CButton();

        setMinimumSize(new java.awt.Dimension(670, 470));
        getContentPane().setLayout(new java.awt.BorderLayout(5, 5));

        panDenomination.setMaximumSize(new java.awt.Dimension(650, 450));
        panDenomination.setMinimumSize(new java.awt.Dimension(650, 450));
        panDenomination.setPreferredSize(new java.awt.Dimension(650, 450));
        panDenomination.setLayout(new java.awt.GridBagLayout());

        scrDenomination.setFocusable(false);
        scrDenomination.setMaximumSize(new java.awt.Dimension(670, 470));
        scrDenomination.setMinimumSize(new java.awt.Dimension(670, 470));
        scrDenomination.setPreferredSize(new java.awt.Dimension(670, 470));

        panKeyboard.setFont(new java.awt.Font("ML-TTIndulekha", 0, 11)); // NOI18N
        panKeyboard.setMaximumSize(new java.awt.Dimension(650, 450));
        panKeyboard.setMinimumSize(new java.awt.Dimension(650, 450));
        panKeyboard.setPreferredSize(new java.awt.Dimension(650, 450));
        panKeyboard.setRequestFocusEnabled(false);
        panKeyboard.setLayout(new java.awt.GridBagLayout());

        btnA.setText("A");
        btnA.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnA.setMaximumSize(new java.awt.Dimension(60, 30));
        btnA.setMinimumSize(new java.awt.Dimension(60, 30));
        btnA.setPreferredSize(new java.awt.Dimension(60, 30));
        btnA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        panKeyboard.add(btnA, gridBagConstraints);

        btnB.setText("B");
        btnB.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnB.setMaximumSize(new java.awt.Dimension(60, 30));
        btnB.setMinimumSize(new java.awt.Dimension(60, 30));
        btnB.setPreferredSize(new java.awt.Dimension(60, 30));
        btnB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        panKeyboard.add(btnB, gridBagConstraints);

        btnC.setText("C");
        btnC.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnC.setMaximumSize(new java.awt.Dimension(60, 30));
        btnC.setMinimumSize(new java.awt.Dimension(60, 30));
        btnC.setPreferredSize(new java.awt.Dimension(60, 30));
        btnC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        panKeyboard.add(btnC, gridBagConstraints);

        btnD.setText("Cu");
        btnD.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnD.setMaximumSize(new java.awt.Dimension(60, 30));
        btnD.setMinimumSize(new java.awt.Dimension(60, 30));
        btnD.setPreferredSize(new java.awt.Dimension(60, 30));
        btnD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        panKeyboard.add(btnD, gridBagConstraints);

        btnE.setText("D");
        btnE.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnE.setMaximumSize(new java.awt.Dimension(60, 30));
        btnE.setMinimumSize(new java.awt.Dimension(60, 30));
        btnE.setPreferredSize(new java.awt.Dimension(60, 30));
        btnE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        panKeyboard.add(btnE, gridBagConstraints);

        btnF.setText("Du");
        btnF.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnF.setMaximumSize(new java.awt.Dimension(60, 30));
        btnF.setMinimumSize(new java.awt.Dimension(60, 30));
        btnF.setPreferredSize(new java.awt.Dimension(60, 30));
        btnF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        panKeyboard.add(btnF, gridBagConstraints);

        btnA6.setText("E");
        btnA6.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnA6.setMaximumSize(new java.awt.Dimension(60, 30));
        btnA6.setMinimumSize(new java.awt.Dimension(60, 30));
        btnA6.setPreferredSize(new java.awt.Dimension(60, 30));
        btnA6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnA6ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        panKeyboard.add(btnA6, gridBagConstraints);

        btnA7.setText("F");
        btnA7.setFont(new java.awt.Font("ML-TTIndulekha", 1, 18)); // NOI18N
        btnA7.setMaximumSize(new java.awt.Dimension(60, 30));
        btnA7.setMinimumSize(new java.awt.Dimension(60, 30));
        btnA7.setPreferredSize(new java.awt.Dimension(60, 30));
        btnA7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnA7ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panKeyboard.add(btnA7, gridBagConstraints);

        btnA8.setText("G");
        btnA8.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnA8.setMaximumSize(new java.awt.Dimension(60, 30));
        btnA8.setMinimumSize(new java.awt.Dimension(60, 30));
        btnA8.setPreferredSize(new java.awt.Dimension(60, 30));
        btnA8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnA8ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        panKeyboard.add(btnA8, gridBagConstraints);

        btnA9.setText("sF");
        btnA9.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnA9.setMaximumSize(new java.awt.Dimension(60, 30));
        btnA9.setMinimumSize(new java.awt.Dimension(60, 30));
        btnA9.setPreferredSize(new java.awt.Dimension(60, 30));
        btnA9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnA9ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        panKeyboard.add(btnA9, gridBagConstraints);

        btnA10.setText("H");
        btnA10.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnA10.setMaximumSize(new java.awt.Dimension(60, 30));
        btnA10.setMinimumSize(new java.awt.Dimension(60, 30));
        btnA10.setPreferredSize(new java.awt.Dimension(60, 30));
        btnA10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnA10ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        panKeyboard.add(btnA10, gridBagConstraints);

        btnA11.setText("Hm");
        btnA11.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnA11.setMaximumSize(new java.awt.Dimension(60, 30));
        btnA11.setMinimumSize(new java.awt.Dimension(60, 30));
        btnA11.setPreferredSize(new java.awt.Dimension(60, 30));
        btnA11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnA11ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        panKeyboard.add(btnA11, gridBagConstraints);

        btnA12.setText("Hu");
        btnA12.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnA12.setMaximumSize(new java.awt.Dimension(60, 30));
        btnA12.setMinimumSize(new java.awt.Dimension(60, 30));
        btnA12.setPreferredSize(new java.awt.Dimension(60, 30));
        btnA12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnA12ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        panKeyboard.add(btnA12, gridBagConstraints);

        btnA13.setText("Aw");
        btnA13.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnA13.setMaximumSize(new java.awt.Dimension(60, 30));
        btnA13.setMinimumSize(new java.awt.Dimension(60, 30));
        btnA13.setPreferredSize(new java.awt.Dimension(60, 30));
        btnA13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnA13ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        panKeyboard.add(btnA13, gridBagConstraints);

        btnI.setText("T");
        btnI.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnI.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        panKeyboard.add(btnI, gridBagConstraints);

        btnI1.setText("[");
        btnI1.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnI1.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI1.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI1.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 5;
        panKeyboard.add(btnI1, gridBagConstraints);

        btnI2.setText("U");
        btnI2.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnI2.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI2.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI2.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        panKeyboard.add(btnI2, gridBagConstraints);

        btnI3.setText("S");
        btnI3.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnI3.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI3.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI3.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        panKeyboard.add(btnI3, gridBagConstraints);

        btnI4.setText("R");
        btnI4.setFocusable(false);
        btnI4.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnI4.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI4.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI4.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 4;
        panKeyboard.add(btnI4, gridBagConstraints);

        btnI5.setText("Q");
        btnI5.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnI5.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI5.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI5.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI5ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 4;
        panKeyboard.add(btnI5, gridBagConstraints);

        btnI6.setText("P");
        btnI6.setFont(new java.awt.Font("ML-TTIndulekha", 1, 18)); // NOI18N
        btnI6.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI6.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI6.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI6ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 4;
        panKeyboard.add(btnI6, gridBagConstraints);

        btnI7.setText("O");
        btnI7.setFocusable(false);
        btnI7.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnI7.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI7.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI7.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI7ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        panKeyboard.add(btnI7, gridBagConstraints);

        btnI8.setText("N");
        btnI8.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnI8.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI8.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI8.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI8ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        panKeyboard.add(btnI8, gridBagConstraints);

        btnI9.setText("I");
        btnI9.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnI9.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI9.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI9.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI9ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        panKeyboard.add(btnI9, gridBagConstraints);

        btnI10.setText("J");
        btnI10.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnI10.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI10.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI10.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI10ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        panKeyboard.add(btnI10, gridBagConstraints);

        btnI11.setText("K");
        btnI11.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnI11.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI11.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI11.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI11ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        panKeyboard.add(btnI11, gridBagConstraints);

        btnI12.setText("L");
        btnI12.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnI12.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI12.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI12.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI12ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        panKeyboard.add(btnI12, gridBagConstraints);

        btnI13.setText("M");
        btnI13.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnI13.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI13.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI13.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI13ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        panKeyboard.add(btnI13, gridBagConstraints);

        btnI14.setText("X");
        btnI14.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnI14.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI14.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI14.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI14ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        panKeyboard.add(btnI14, gridBagConstraints);

        btnI15.setText("W");
        btnI15.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnI15.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI15.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI15.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI15ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        panKeyboard.add(btnI15, gridBagConstraints);

        btnI16.setText("V");
        btnI16.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnI16.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI16.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI16.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI16ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        panKeyboard.add(btnI16, gridBagConstraints);

        btnI17.setText("Z");
        btnI17.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnI17.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI17.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI17.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI17ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 5;
        panKeyboard.add(btnI17, gridBagConstraints);

        btnI18.setText("e");
        btnI18.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnI18.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI18.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI18.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI18ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 6;
        panKeyboard.add(btnI18, gridBagConstraints);

        btnI19.setText("Y");
        btnI19.setFont(new java.awt.Font("ML-TTIndulekha", 0, 15)); // NOI18N
        btnI19.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI19.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI19.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI19ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 5;
        panKeyboard.add(btnI19, gridBagConstraints);

        btnI20.setText("c");
        btnI20.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnI20.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI20.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI20.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI20ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        panKeyboard.add(btnI20, gridBagConstraints);

        btnI21.setText("b");
        btnI21.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnI21.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI21.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI21.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI21ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        panKeyboard.add(btnI21, gridBagConstraints);

        btnI22.setText("a");
        btnI22.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnI22.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI22.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI22.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI22ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        panKeyboard.add(btnI22, gridBagConstraints);

        btnI24.setText("`");
        btnI24.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnI24.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI24.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI24.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI24ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        panKeyboard.add(btnI24, gridBagConstraints);

        btnI25.setText("_");
        btnI25.setFont(new java.awt.Font("ML-TTIndulekha", 1, 18)); // NOI18N
        btnI25.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI25.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI25.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI25ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        panKeyboard.add(btnI25, gridBagConstraints);

        btnI28.setText("^");
        btnI28.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnI28.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI28.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI28.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI28ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        panKeyboard.add(btnI28, gridBagConstraints);

        btnI29.setText("]");
        btnI29.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        btnI29.setMaximumSize(new java.awt.Dimension(60, 30));
        btnI29.setMinimumSize(new java.awt.Dimension(60, 30));
        btnI29.setPreferredSize(new java.awt.Dimension(60, 30));
        btnI29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnI29ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        panKeyboard.add(btnI29, gridBagConstraints);

        btnI30.setText("\\");
            btnI30.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI30.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI30.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI30.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI30.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI30ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 9;
            gridBagConstraints.gridy = 5;
            panKeyboard.add(btnI30, gridBagConstraints);

            btnI33.setText("h");
            btnI33.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI33.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI33.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI33.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI33.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI33ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 8;
            gridBagConstraints.gridy = 6;
            panKeyboard.add(btnI33, gridBagConstraints);

            btnI34.setText("i");
            btnI34.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI34.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI34.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI34.setPreferredSize(new java.awt.Dimension(45, 23));
            btnI34.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI34ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 9;
            gridBagConstraints.gridy = 6;
            panKeyboard.add(btnI34, gridBagConstraints);

            btnI35.setText("j");
            btnI35.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI35.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI35.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI35.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI35.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI35ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 7;
            panKeyboard.add(btnI35, gridBagConstraints);

            btnI36.setText("k");
            btnI36.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI36.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI36.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI36.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI36.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI36ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 7;
            panKeyboard.add(btnI36, gridBagConstraints);

            btnI39.setText("n");
            btnI39.setFont(new java.awt.Font("ML-TTIndulekha", 1, 18)); // NOI18N
            btnI39.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI39.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI39.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI39.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI39ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 7;
            gridBagConstraints.gridy = 7;
            panKeyboard.add(btnI39, gridBagConstraints);

            btnI40.setText("l");
            btnI40.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI40.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI40.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI40.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI40.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI40ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 2;
            gridBagConstraints.gridy = 7;
            panKeyboard.add(btnI40, gridBagConstraints);

            btnI41.setText("f");
            btnI41.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI41.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI41.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI41.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI41.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI41ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 3;
            gridBagConstraints.gridy = 7;
            panKeyboard.add(btnI41, gridBagConstraints);

            btnI42.setText("g");
            btnI42.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI42.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI42.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI42.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI42.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI42ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 4;
            gridBagConstraints.gridy = 7;
            panKeyboard.add(btnI42, gridBagConstraints);

            btnI43.setText("d");
            btnI43.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI43.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI43.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI43.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI43.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI43ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 5;
            gridBagConstraints.gridy = 7;
            panKeyboard.add(btnI43, gridBagConstraints);

            btnI44.setText("p");
            btnI44.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI44.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI44.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI44.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI44.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI44ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 9;
            gridBagConstraints.gridy = 7;
            panKeyboard.add(btnI44, gridBagConstraints);

            btnI45.setText("o");
            btnI45.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI45.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI45.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI45.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI45.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI45ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 8;
            gridBagConstraints.gridy = 7;
            panKeyboard.add(btnI45, gridBagConstraints);

            btnI46.setText("r");
            btnI46.setFocusable(false);
            btnI46.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI46.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI46.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI46.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI46.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI46ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 8;
            panKeyboard.add(btnI46, gridBagConstraints);

            btnI47.setText("q");
            btnI47.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI47.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI47.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI47.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI47.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI47ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 8;
            panKeyboard.add(btnI47, gridBagConstraints);

            btnI48.setText("v");
            btnI48.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI48.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI48.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI48.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI48.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI48ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 2;
            gridBagConstraints.gridy = 8;
            panKeyboard.add(btnI48, gridBagConstraints);

            btnI49.setText("m");
            btnI49.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI49.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI49.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI49.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI49.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI49ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 6;
            gridBagConstraints.gridy = 7;
            panKeyboard.add(btnI49, gridBagConstraints);

            btnI50.setText("z");
            btnI50.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI50.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI50.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI50.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI50.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI50ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 3;
            gridBagConstraints.gridy = 8;
            panKeyboard.add(btnI50, gridBagConstraints);

            btnI51.setText("y");
            btnI51.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI51.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI51.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI51.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI51.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI51ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 4;
            gridBagConstraints.gridy = 8;
            panKeyboard.add(btnI51, gridBagConstraints);

            btnI52.setText("u");
            btnI52.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI52.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI52.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI52.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI52.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI52ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 5;
            gridBagConstraints.gridy = 8;
            panKeyboard.add(btnI52, gridBagConstraints);

            btnI53.setText("w");
            btnI53.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI53.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI53.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI53.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI53.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI53ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 6;
            gridBagConstraints.gridy = 8;
            panKeyboard.add(btnI53, gridBagConstraints);

            btnI55.setText("{");
            btnI55.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI55.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI55.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI55.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI55.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI55ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 7;
            gridBagConstraints.gridy = 8;
            panKeyboard.add(btnI55, gridBagConstraints);

            btnI56.setText("s");
            btnI56.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI56.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI56.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI56.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI56.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI56ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 8;
            gridBagConstraints.gridy = 8;
            panKeyboard.add(btnI56, gridBagConstraints);

            btnI57.setText("t");
            btnI57.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI57.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI57.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI57.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI57.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI57ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 9;
            gridBagConstraints.gridy = 8;
            panKeyboard.add(btnI57, gridBagConstraints);

            btnI58.setText("¨");
            btnI58.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI58.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI58.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI58.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI58.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI58ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 7;
            gridBagConstraints.gridy = 9;
            panKeyboard.add(btnI58, gridBagConstraints);

            btnI59.setText("¤");
            btnI59.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI59.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI59.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI59.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI59.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI59ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 6;
            gridBagConstraints.gridy = 9;
            panKeyboard.add(btnI59, gridBagConstraints);

            btnI60.setText("ª");
            btnI60.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI60.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI60.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI60.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI60.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI60ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 5;
            gridBagConstraints.gridy = 9;
            panKeyboard.add(btnI60, gridBagConstraints);

            btnI61.setText("¯");
            btnI61.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI61.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI61.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI61.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI61.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI61ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 4;
            gridBagConstraints.gridy = 9;
            panKeyboard.add(btnI61, gridBagConstraints);

            btnI62.setText("¶");
            btnI62.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI62.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI62.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI62.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI62.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI62ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 3;
            gridBagConstraints.gridy = 9;
            panKeyboard.add(btnI62, gridBagConstraints);

            btnI63.setText("®");
            btnI63.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI63.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI63.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI63.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI63.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI63ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 2;
            gridBagConstraints.gridy = 9;
            panKeyboard.add(btnI63, gridBagConstraints);

            btnI64.setText("«");
            btnI64.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI64.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI64.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI64.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI64.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI64ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 9;
            panKeyboard.add(btnI64, gridBagConstraints);

            btnI66.setText("¡");
            btnI66.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI66.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI66.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI66.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI66.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI66ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 9;
            panKeyboard.add(btnI66, gridBagConstraints);

            btnI65.setText("¬");
            btnI65.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI65.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI65.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI65.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI65.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI65ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 11;
            panKeyboard.add(btnI65, gridBagConstraints);

            btnI68.setText("³");
            btnI68.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI68.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI68.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI68.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI68.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI68ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 2;
            gridBagConstraints.gridy = 11;
            panKeyboard.add(btnI68, gridBagConstraints);

            btnI69.setText("Â");
            btnI69.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI69.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI69.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI69.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI69.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI69ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 11;
            panKeyboard.add(btnI69, gridBagConstraints);

            btnI70.setText(" ");
            btnI70.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI70.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI70.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI70.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI70.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI70ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 9;
            gridBagConstraints.gridy = 10;
            panKeyboard.add(btnI70, gridBagConstraints);

            btnI71.setText("Å");
            btnI71.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI71.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI71.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI71.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI71.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI71ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 8;
            gridBagConstraints.gridy = 10;
            panKeyboard.add(btnI71, gridBagConstraints);

            btnI73.setText("Ê");
            btnI73.setFont(new java.awt.Font("ML-TTIndulekha", 1, 18)); // NOI18N
            btnI73.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI73.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI73.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI73.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI73ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 7;
            gridBagConstraints.gridy = 10;
            panKeyboard.add(btnI73, gridBagConstraints);

            btnI74.setText("È");
            btnI74.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI74.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI74.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI74.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI74.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI74ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 6;
            gridBagConstraints.gridy = 10;
            panKeyboard.add(btnI74, gridBagConstraints);

            btnI76.setText("Æ");
            btnI76.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI76.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI76.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI76.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI76.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI76ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 5;
            gridBagConstraints.gridy = 10;
            panKeyboard.add(btnI76, gridBagConstraints);

            btnI77.setText("Ã");
            btnI77.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI77.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI77.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI77.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI77.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI77ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 4;
            gridBagConstraints.gridy = 10;
            panKeyboard.add(btnI77, gridBagConstraints);

            btnI79.setText("¿");
            btnI79.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI79.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI79.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI79.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI79.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI79ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 3;
            gridBagConstraints.gridy = 10;
            panKeyboard.add(btnI79, gridBagConstraints);

            btnI81.setText("½");
            btnI81.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI81.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI81.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI81.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI81.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI81ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 2;
            gridBagConstraints.gridy = 10;
            panKeyboard.add(btnI81, gridBagConstraints);

            btnI83.setText("º");
            btnI83.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI83.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI83.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI83.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI83.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI83ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 10;
            panKeyboard.add(btnI83, gridBagConstraints);

            btnI84.setText("¸");
            btnI84.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI84.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI84.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI84.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI84.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI84ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 10;
            panKeyboard.add(btnI84, gridBagConstraints);

            btnI85.setText("±");
            btnI85.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI85.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI85.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI85.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI85.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI85ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 9;
            gridBagConstraints.gridy = 9;
            panKeyboard.add(btnI85, gridBagConstraints);

            btnI89.setText("Ö");
            btnI89.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI89.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI89.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI89.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI89.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI89ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 8;
            gridBagConstraints.gridy = 9;
            panKeyboard.add(btnI89, gridBagConstraints);

            btnI86.setText("À");
            btnI86.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI86.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI86.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI86.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI86.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI86ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 3;
            gridBagConstraints.gridy = 11;
            panKeyboard.add(btnI86, gridBagConstraints);

            btnI87.setText("©");
            btnI87.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI87.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI87.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI87.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI87.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI87ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 5;
            gridBagConstraints.gridy = 12;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
            panKeyboard.add(btnI87, gridBagConstraints);

            btnI88.setText("¦");
            btnI88.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI88.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI88.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI88.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI88.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI88ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 4;
            gridBagConstraints.gridy = 12;
            panKeyboard.add(btnI88, gridBagConstraints);

            btnI90.setText("µ");
            btnI90.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI90.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI90.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI90.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI90.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI90ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 3;
            gridBagConstraints.gridy = 12;
            panKeyboard.add(btnI90, gridBagConstraints);

            btnI91.setText("­");
            btnI91.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI91.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI91.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI91.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI91.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI91ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 2;
            gridBagConstraints.gridy = 12;
            panKeyboard.add(btnI91, gridBagConstraints);

            btnI92.setText("§");
            btnI92.setFont(new java.awt.Font("ML-TTIndulekha", 1, 18)); // NOI18N
            btnI92.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI92.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI92.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI92.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI92ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 12;
            panKeyboard.add(btnI92, gridBagConstraints);

            btnI93.setText("¼");
            btnI93.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI93.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI93.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI93.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI93.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI93ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 12;
            panKeyboard.add(btnI93, gridBagConstraints);

            btnI94.setText("Ú");
            btnI94.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI94.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI94.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI94.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI94.setRequestFocusEnabled(false);
            btnI94.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI94ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 8;
            gridBagConstraints.gridy = 11;
            panKeyboard.add(btnI94, gridBagConstraints);

            btnI95.setText("´");
            btnI95.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI95.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI95.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI95.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI95.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI95ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 9;
            gridBagConstraints.gridy = 11;
            panKeyboard.add(btnI95, gridBagConstraints);

            btnI96.setText("£");
            btnI96.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI96.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI96.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI96.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI96.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI96ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 7;
            gridBagConstraints.gridy = 11;
            panKeyboard.add(btnI96, gridBagConstraints);

            btnI97.setText("â");
            btnI97.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI97.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI97.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI97.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI97.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI97ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 5;
            gridBagConstraints.gridy = 11;
            panKeyboard.add(btnI97, gridBagConstraints);

            btnI98.setText("Ø");
            btnI98.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI98.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI98.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI98.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI98.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI98ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 6;
            gridBagConstraints.gridy = 11;
            panKeyboard.add(btnI98, gridBagConstraints);

            btnI99.setText("Ä");
            btnI99.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI99.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI99.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI99.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI99.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI99ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 4;
            gridBagConstraints.gridy = 11;
            panKeyboard.add(btnI99, gridBagConstraints);

            txtValue.setAllowAll(true);
            txtValue.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            txtValue.setMaximumSize(new java.awt.Dimension(300, 24));
            txtValue.setMinimumSize(new java.awt.Dimension(300, 24));
            txtValue.setPreferredSize(new java.awt.Dimension(300, 24));
            txtValue.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    txtValueFocusLost(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridwidth = 20;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_START;
            gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
            panKeyboard.add(txtValue, gridBagConstraints);

            btnCopy.setText("Insert");
            btnCopy.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnCopyActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 5;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
            panKeyboard.add(btnCopy, gridBagConstraints);

            btnClear.setText("Clear");
            btnClear.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnClearActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 7;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridwidth = 2;
            panKeyboard.add(btnClear, gridBagConstraints);

            btnClose.setText("Close");
            btnClose.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnCloseActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 8;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
            panKeyboard.add(btnClose, gridBagConstraints);

            btnI100.setText("¢");
            btnI100.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
            btnI100.setMaximumSize(new java.awt.Dimension(60, 30));
            btnI100.setMinimumSize(new java.awt.Dimension(60, 30));
            btnI100.setPreferredSize(new java.awt.Dimension(60, 30));
            btnI100.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnI100ActionPerformed(evt);
                }
            });
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 6;
            gridBagConstraints.gridy = 12;
            panKeyboard.add(btnI100, gridBagConstraints);

            scrDenomination.setViewportView(panKeyboard);

            panDenomination.add(scrDenomination, new java.awt.GridBagConstraints());

            getContentPane().add(panDenomination, java.awt.BorderLayout.CENTER);

            pack();
        }// </editor-fold>//GEN-END:initComponents
            /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        this.dispose();
    }//GEN-LAST:event_exitForm

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        ClientUtil.clearAll(this);
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        txtValue.setText("");
        setFinalTxt("");
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopyActionPerformed
        setFinalTxt(txtValue.getText());
        this.dispose();
    }//GEN-LAST:event_btnCopyActionPerformed

    private void txtValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValueFocusLost

   }//GEN-LAST:event_txtValueFocusLost

    private void btnI99ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI99ActionPerformed
        setValueonText(btnI99.getText());
    }//GEN-LAST:event_btnI99ActionPerformed

    private void btnI98ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI98ActionPerformed
        setValueonText(btnI98.getText());
    }//GEN-LAST:event_btnI98ActionPerformed

    private void btnI97ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI97ActionPerformed
        setValueonText(btnI97.getText());
    }//GEN-LAST:event_btnI97ActionPerformed

    private void btnI96ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI96ActionPerformed
        setValueonText(btnI96.getText());
    }//GEN-LAST:event_btnI96ActionPerformed

    private void btnI95ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI95ActionPerformed
        setValueonText(btnI95.getText());
    }//GEN-LAST:event_btnI95ActionPerformed

    private void btnI94ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI94ActionPerformed
        setValueonText(btnI94.getText());
    }//GEN-LAST:event_btnI94ActionPerformed

    private void btnI93ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI93ActionPerformed
        setValueonText(btnI93.getText());
    }//GEN-LAST:event_btnI93ActionPerformed

    private void btnI92ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI92ActionPerformed
        setValueonText(btnI92.getText());
    }//GEN-LAST:event_btnI92ActionPerformed

    private void btnI91ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI91ActionPerformed
        setValueonText(btnI91.getText());
    }//GEN-LAST:event_btnI91ActionPerformed

    private void btnI90ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI90ActionPerformed
        setValueonText(btnI90.getText());
    }//GEN-LAST:event_btnI90ActionPerformed

    private void btnI88ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI88ActionPerformed
        setValueonText(btnI88.getText());
    }//GEN-LAST:event_btnI88ActionPerformed

    private void btnI87ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI87ActionPerformed
        setValueonText(btnI87.getText());
    }//GEN-LAST:event_btnI87ActionPerformed

    private void btnI86ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI86ActionPerformed
        setValueonText(btnI86.getText());
    }//GEN-LAST:event_btnI86ActionPerformed

    private void btnI89ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI89ActionPerformed
        setValueonText(btnI89.getText());
    }//GEN-LAST:event_btnI89ActionPerformed

    private void btnI85ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI85ActionPerformed
        setValueonText(btnI85.getText());
    }//GEN-LAST:event_btnI85ActionPerformed

    private void btnI84ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI84ActionPerformed
        setValueonText(btnI84.getText());
    }//GEN-LAST:event_btnI84ActionPerformed

    private void btnI83ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI83ActionPerformed
        setValueonText(btnI83.getText());
    }//GEN-LAST:event_btnI83ActionPerformed

    private void btnI81ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI81ActionPerformed
        setValueonText(btnI81.getText());
    }//GEN-LAST:event_btnI81ActionPerformed

    private void btnI79ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI79ActionPerformed
        setValueonText(btnI79.getText());
    }//GEN-LAST:event_btnI79ActionPerformed

    private void btnI77ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI77ActionPerformed
        setValueonText(btnI77.getText());
    }//GEN-LAST:event_btnI77ActionPerformed

    private void btnI76ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI76ActionPerformed
        setValueonText(btnI76.getText());
    }//GEN-LAST:event_btnI76ActionPerformed

    private void btnI74ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI74ActionPerformed
        setValueonText(btnI74.getText());
    }//GEN-LAST:event_btnI74ActionPerformed

    private void btnI73ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI73ActionPerformed
        setValueonText(btnI73.getText());
    }//GEN-LAST:event_btnI73ActionPerformed

    private void btnI71ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI71ActionPerformed
        setValueonText(btnI71.getText());
    }//GEN-LAST:event_btnI71ActionPerformed

    private void btnI70ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI70ActionPerformed
        setValueonText(btnI70.getText());
    }//GEN-LAST:event_btnI70ActionPerformed

    private void btnI69ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI69ActionPerformed
        setValueonText(btnI69.getText());
    }//GEN-LAST:event_btnI69ActionPerformed

    private void btnI68ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI68ActionPerformed
        setValueonText(btnI68.getText());
    }//GEN-LAST:event_btnI68ActionPerformed

    private void btnI65ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI65ActionPerformed
        setValueonText(btnI65.getText());
    }//GEN-LAST:event_btnI65ActionPerformed

    private void btnI66ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI66ActionPerformed
        setValueonText(btnI66.getText());
    }//GEN-LAST:event_btnI66ActionPerformed

    private void btnI64ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI64ActionPerformed
        setValueonText(btnI64.getText());
    }//GEN-LAST:event_btnI64ActionPerformed

    private void btnI63ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI63ActionPerformed
        setValueonText(btnI63.getText());
    }//GEN-LAST:event_btnI63ActionPerformed

    private void btnI62ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI62ActionPerformed
        setValueonText(btnI62.getText());
    }//GEN-LAST:event_btnI62ActionPerformed

    private void btnI61ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI61ActionPerformed
        setValueonText(btnI61.getText());
    }//GEN-LAST:event_btnI61ActionPerformed

    private void btnI60ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI60ActionPerformed
        setValueonText(btnI60.getText());
    }//GEN-LAST:event_btnI60ActionPerformed

    private void btnI59ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI59ActionPerformed
        setValueonText(btnI59.getText());
    }//GEN-LAST:event_btnI59ActionPerformed

    private void btnI58ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI58ActionPerformed
        setValueonText(btnI58.getText());
    }//GEN-LAST:event_btnI58ActionPerformed

    private void btnI57ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI57ActionPerformed
        setValueonText(btnI57.getText());
    }//GEN-LAST:event_btnI57ActionPerformed

    private void btnI56ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI56ActionPerformed
        setValueonText(btnI56.getText());
    }//GEN-LAST:event_btnI56ActionPerformed

    private void btnI55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI55ActionPerformed
        setValueonText(btnI55.getText());
    }//GEN-LAST:event_btnI55ActionPerformed

    private void btnI53ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI53ActionPerformed
        setValueonText(btnI53.getText());
    }//GEN-LAST:event_btnI53ActionPerformed

    private void btnI52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI52ActionPerformed
        setValueonText(btnI52.getText());
    }//GEN-LAST:event_btnI52ActionPerformed

    private void btnI51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI51ActionPerformed
        setValueonText(btnI51.getText());
    }//GEN-LAST:event_btnI51ActionPerformed

    private void btnI50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI50ActionPerformed
        setValueonText(btnI50.getText());
    }//GEN-LAST:event_btnI50ActionPerformed

    private void btnI49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI49ActionPerformed
        setValueonText(btnI49.getText());
    }//GEN-LAST:event_btnI49ActionPerformed

    private void btnI48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI48ActionPerformed
        setValueonText(btnI48.getText());
    }//GEN-LAST:event_btnI48ActionPerformed

    private void btnI47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI47ActionPerformed
        setValueonText(btnI47.getText());
    }//GEN-LAST:event_btnI47ActionPerformed

    private void btnI46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI46ActionPerformed
        setValueonText(btnI46.getText());
    }//GEN-LAST:event_btnI46ActionPerformed

    private void btnI45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI45ActionPerformed
        setValueonText(btnI45.getText());
    }//GEN-LAST:event_btnI45ActionPerformed

    private void btnI44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI44ActionPerformed
        setValueonText(btnI44.getText());
    }//GEN-LAST:event_btnI44ActionPerformed

    private void btnI43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI43ActionPerformed
        setValueonText(btnI43.getText());
    }//GEN-LAST:event_btnI43ActionPerformed

    private void btnI42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI42ActionPerformed
        setValueonText(btnI42.getText());
    }//GEN-LAST:event_btnI42ActionPerformed

    private void btnI41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI41ActionPerformed
        setValueonText(btnI41.getText());
    }//GEN-LAST:event_btnI41ActionPerformed

    private void btnI40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI40ActionPerformed
        setValueonText(btnI40.getText());
    }//GEN-LAST:event_btnI40ActionPerformed

    private void btnI39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI39ActionPerformed
        setValueonText(btnI39.getText());
    }//GEN-LAST:event_btnI39ActionPerformed

    private void btnI36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI36ActionPerformed
        setValueonText(btnI36.getText());
    }//GEN-LAST:event_btnI36ActionPerformed

    private void btnI35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI35ActionPerformed
        setValueonText(btnI35.getText());
    }//GEN-LAST:event_btnI35ActionPerformed

    private void btnI34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI34ActionPerformed
        setValueonText(btnI34.getText());
    }//GEN-LAST:event_btnI34ActionPerformed

    private void btnI33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI33ActionPerformed
        setValueonText(btnI33.getText());
    }//GEN-LAST:event_btnI33ActionPerformed

    private void btnI30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI30ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnI30.getText());
    }//GEN-LAST:event_btnI30ActionPerformed

    private void btnI29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI29ActionPerformed
        setValueonText(btnI29.getText());        // TODO add your handling code here:
    }//GEN-LAST:event_btnI29ActionPerformed

    private void btnI28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI28ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnI28.getText());
    }//GEN-LAST:event_btnI28ActionPerformed

    private void btnI25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI25ActionPerformed
        setValueonText(btnI25.getText());
    }//GEN-LAST:event_btnI25ActionPerformed

    private void btnI24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI24ActionPerformed
        setValueonText(btnI24.getText());
    }//GEN-LAST:event_btnI24ActionPerformed

    private void btnI22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI22ActionPerformed
        setValueonText(btnI22.getText());
    }//GEN-LAST:event_btnI22ActionPerformed

    private void btnI21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI21ActionPerformed
        setValueonText(btnI21.getText());
    }//GEN-LAST:event_btnI21ActionPerformed

    private void btnI20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI20ActionPerformed
        setValueonText(btnI20.getText());
    }//GEN-LAST:event_btnI20ActionPerformed

    private void btnI19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI19ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnI19.getText());
    }//GEN-LAST:event_btnI19ActionPerformed

    private void btnI18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI18ActionPerformed
        setValueonText(btnI18.getText());
    }//GEN-LAST:event_btnI18ActionPerformed

    private void btnI17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI17ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnI17.getText());
    }//GEN-LAST:event_btnI17ActionPerformed

    private void btnI16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI16ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnI16.getText());
    }//GEN-LAST:event_btnI16ActionPerformed

    private void btnI15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI15ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnI15.getText());
    }//GEN-LAST:event_btnI15ActionPerformed

    private void btnI14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI14ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnI14.getText());
    }//GEN-LAST:event_btnI14ActionPerformed

    private void btnI13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI13ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnI13.getText());
    }//GEN-LAST:event_btnI13ActionPerformed

    private void btnI12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI12ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnI12.getText());
    }//GEN-LAST:event_btnI12ActionPerformed

    private void btnI11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI11ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnI11.getText());
    }//GEN-LAST:event_btnI11ActionPerformed

    private void btnI10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI10ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnI10.getText());
    }//GEN-LAST:event_btnI10ActionPerformed

    private void btnI9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI9ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnI9.getText());
    }//GEN-LAST:event_btnI9ActionPerformed

    private void btnI8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI8ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnI8.getText());
    }//GEN-LAST:event_btnI8ActionPerformed

    private void btnI7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI7ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnI7.getText());
    }//GEN-LAST:event_btnI7ActionPerformed

    private void btnI6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI6ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnI6.getText());
    }//GEN-LAST:event_btnI6ActionPerformed

    private void btnI5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI5ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnI5.getText());
    }//GEN-LAST:event_btnI5ActionPerformed

    private void btnI4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI4ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnI4.getText());
    }//GEN-LAST:event_btnI4ActionPerformed

    private void btnI3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI3ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnI3.getText());
    }//GEN-LAST:event_btnI3ActionPerformed

    private void btnI2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI2ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnI2.getText());
    }//GEN-LAST:event_btnI2ActionPerformed

    private void btnI1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI1ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnI1.getText());
    }//GEN-LAST:event_btnI1ActionPerformed

    private void btnIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIActionPerformed
        // TODO add your handling code here:
        setValueonText(btnI.getText());
    }//GEN-LAST:event_btnIActionPerformed

    private void btnA13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnA13ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnA13.getText());
    }//GEN-LAST:event_btnA13ActionPerformed

    private void btnA12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnA12ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnA12.getText());
    }//GEN-LAST:event_btnA12ActionPerformed

    private void btnA11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnA11ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnA11.getText());
    }//GEN-LAST:event_btnA11ActionPerformed

    private void btnA10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnA10ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnA10.getText());
    }//GEN-LAST:event_btnA10ActionPerformed

    private void btnA9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnA9ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnA9.getText());
    }//GEN-LAST:event_btnA9ActionPerformed

    private void btnA8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnA8ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnA8.getText());
    }//GEN-LAST:event_btnA8ActionPerformed

    private void btnA7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnA7ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnA7.getText());
    }//GEN-LAST:event_btnA7ActionPerformed

    private void btnA6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnA6ActionPerformed
        // TODO add your handling code here:
        setValueonText(btnA6.getText());
    }//GEN-LAST:event_btnA6ActionPerformed

    private void btnFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFActionPerformed
        // TODO add your handling code here:
        setValueonText(btnF.getText());
    }//GEN-LAST:event_btnFActionPerformed

    private void btnEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEActionPerformed
        // TODO add your handling code here:
        setValueonText(btnE.getText());
    }//GEN-LAST:event_btnEActionPerformed

    private void btnDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDActionPerformed
        // TODO add your handling code here:
        setValueonText(btnD.getText());
    }//GEN-LAST:event_btnDActionPerformed

    private void btnCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCActionPerformed
        // TODO add your handling code here:
        setValueonText(btnC.getText());
    }//GEN-LAST:event_btnCActionPerformed

    private void btnBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBActionPerformed
        // TODO add your handling code here:
        setValueonText(btnB.getText());
    }//GEN-LAST:event_btnBActionPerformed

    private void btnAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAActionPerformed
        // TODO add your handling code here:
        setValueonText(btnA.getText());
    }//GEN-LAST:event_btnAActionPerformed

    private void btnI100ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnI100ActionPerformed
        setValueonText(btnI100.getText());
    }//GEN-LAST:event_btnI100ActionPerformed
     private void internationalize() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    }

    public void update(Observable o, Object arg) {
    }

    public void updateOBFields() {
    }
    /**
     * Getter for property enhancedTable.
     * @return Value of property enhancedTable.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getEnhancedTable() {
        return enhancedTable;
    }
    
    /**
     * Setter for property enhancedTable.
     * @param enhancedTable New value of property enhancedTable.
     */
    public void setEnhancedTable(com.see.truetransact.clientutil.EnhancedTableModel enhancedTable) {
        this.enhancedTable = enhancedTable;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnA;
    private com.see.truetransact.uicomponent.CButton btnA10;
    private com.see.truetransact.uicomponent.CButton btnA11;
    private com.see.truetransact.uicomponent.CButton btnA12;
    private com.see.truetransact.uicomponent.CButton btnA13;
    private com.see.truetransact.uicomponent.CButton btnA6;
    private com.see.truetransact.uicomponent.CButton btnA7;
    private com.see.truetransact.uicomponent.CButton btnA8;
    private com.see.truetransact.uicomponent.CButton btnA9;
    private com.see.truetransact.uicomponent.CButton btnB;
    private com.see.truetransact.uicomponent.CButton btnC;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCopy;
    private com.see.truetransact.uicomponent.CButton btnD;
    private com.see.truetransact.uicomponent.CButton btnE;
    private com.see.truetransact.uicomponent.CButton btnF;
    private com.see.truetransact.uicomponent.CButton btnI;
    private com.see.truetransact.uicomponent.CButton btnI1;
    private com.see.truetransact.uicomponent.CButton btnI10;
    private com.see.truetransact.uicomponent.CButton btnI100;
    private com.see.truetransact.uicomponent.CButton btnI11;
    private com.see.truetransact.uicomponent.CButton btnI12;
    private com.see.truetransact.uicomponent.CButton btnI13;
    private com.see.truetransact.uicomponent.CButton btnI14;
    private com.see.truetransact.uicomponent.CButton btnI15;
    private com.see.truetransact.uicomponent.CButton btnI16;
    private com.see.truetransact.uicomponent.CButton btnI17;
    private com.see.truetransact.uicomponent.CButton btnI18;
    private com.see.truetransact.uicomponent.CButton btnI19;
    private com.see.truetransact.uicomponent.CButton btnI2;
    private com.see.truetransact.uicomponent.CButton btnI20;
    private com.see.truetransact.uicomponent.CButton btnI21;
    private com.see.truetransact.uicomponent.CButton btnI22;
    private com.see.truetransact.uicomponent.CButton btnI24;
    private com.see.truetransact.uicomponent.CButton btnI25;
    private com.see.truetransact.uicomponent.CButton btnI28;
    private com.see.truetransact.uicomponent.CButton btnI29;
    private com.see.truetransact.uicomponent.CButton btnI3;
    private com.see.truetransact.uicomponent.CButton btnI30;
    private com.see.truetransact.uicomponent.CButton btnI33;
    private com.see.truetransact.uicomponent.CButton btnI34;
    private com.see.truetransact.uicomponent.CButton btnI35;
    private com.see.truetransact.uicomponent.CButton btnI36;
    private com.see.truetransact.uicomponent.CButton btnI39;
    private com.see.truetransact.uicomponent.CButton btnI4;
    private com.see.truetransact.uicomponent.CButton btnI40;
    private com.see.truetransact.uicomponent.CButton btnI41;
    private com.see.truetransact.uicomponent.CButton btnI42;
    private com.see.truetransact.uicomponent.CButton btnI43;
    private com.see.truetransact.uicomponent.CButton btnI44;
    private com.see.truetransact.uicomponent.CButton btnI45;
    private com.see.truetransact.uicomponent.CButton btnI46;
    private com.see.truetransact.uicomponent.CButton btnI47;
    private com.see.truetransact.uicomponent.CButton btnI48;
    private com.see.truetransact.uicomponent.CButton btnI49;
    private com.see.truetransact.uicomponent.CButton btnI5;
    private com.see.truetransact.uicomponent.CButton btnI50;
    private com.see.truetransact.uicomponent.CButton btnI51;
    private com.see.truetransact.uicomponent.CButton btnI52;
    private com.see.truetransact.uicomponent.CButton btnI53;
    private com.see.truetransact.uicomponent.CButton btnI55;
    private com.see.truetransact.uicomponent.CButton btnI56;
    private com.see.truetransact.uicomponent.CButton btnI57;
    private com.see.truetransact.uicomponent.CButton btnI58;
    private com.see.truetransact.uicomponent.CButton btnI59;
    private com.see.truetransact.uicomponent.CButton btnI6;
    private com.see.truetransact.uicomponent.CButton btnI60;
    private com.see.truetransact.uicomponent.CButton btnI61;
    private com.see.truetransact.uicomponent.CButton btnI62;
    private com.see.truetransact.uicomponent.CButton btnI63;
    private com.see.truetransact.uicomponent.CButton btnI64;
    private com.see.truetransact.uicomponent.CButton btnI65;
    private com.see.truetransact.uicomponent.CButton btnI66;
    private com.see.truetransact.uicomponent.CButton btnI68;
    private com.see.truetransact.uicomponent.CButton btnI69;
    private com.see.truetransact.uicomponent.CButton btnI7;
    private com.see.truetransact.uicomponent.CButton btnI70;
    private com.see.truetransact.uicomponent.CButton btnI71;
    private com.see.truetransact.uicomponent.CButton btnI73;
    private com.see.truetransact.uicomponent.CButton btnI74;
    private com.see.truetransact.uicomponent.CButton btnI76;
    private com.see.truetransact.uicomponent.CButton btnI77;
    private com.see.truetransact.uicomponent.CButton btnI79;
    private com.see.truetransact.uicomponent.CButton btnI8;
    private com.see.truetransact.uicomponent.CButton btnI81;
    private com.see.truetransact.uicomponent.CButton btnI83;
    private com.see.truetransact.uicomponent.CButton btnI84;
    private com.see.truetransact.uicomponent.CButton btnI85;
    private com.see.truetransact.uicomponent.CButton btnI86;
    private com.see.truetransact.uicomponent.CButton btnI87;
    private com.see.truetransact.uicomponent.CButton btnI88;
    private com.see.truetransact.uicomponent.CButton btnI89;
    private com.see.truetransact.uicomponent.CButton btnI9;
    private com.see.truetransact.uicomponent.CButton btnI90;
    private com.see.truetransact.uicomponent.CButton btnI91;
    private com.see.truetransact.uicomponent.CButton btnI92;
    private com.see.truetransact.uicomponent.CButton btnI93;
    private com.see.truetransact.uicomponent.CButton btnI94;
    private com.see.truetransact.uicomponent.CButton btnI95;
    private com.see.truetransact.uicomponent.CButton btnI96;
    private com.see.truetransact.uicomponent.CButton btnI97;
    private com.see.truetransact.uicomponent.CButton btnI98;
    private com.see.truetransact.uicomponent.CButton btnI99;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CPanel panDenomination;
    private com.see.truetransact.uicomponent.CPanel panKeyboard;
    private com.see.truetransact.uicomponent.CScrollPane scrDenomination;
    private com.see.truetransact.uicomponent.CTextField txtValue;
    // End of variables declaration//GEN-END:variables
    
}
