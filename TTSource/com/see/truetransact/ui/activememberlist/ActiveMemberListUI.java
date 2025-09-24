/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ActiveMemberListUI.java
 *
 * Created on February 2, 2005, 12:20 PM
 */
package com.see.truetransact.ui.activememberlist;

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CComboBox;
import com.see.truetransact.uicomponent.CLabel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import com.see.truetransact.ui.common.viewall.ViewAll;
import java.awt.Color;
import java.awt.Component;
import java.util.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author 152721
 */
public class ActiveMemberListUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    HashMap mandatoryMap;
    ActiveMemberListOB observable;
//    AgentRB resourceBundle = new AgentRB();
    //java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.agent.AgentRB", ProxyParameters.LANGUAGE);
    final int EDIT = 0, DELETE = 1, AUTHORIZE = 2, AGENTID = 3, ACCNO = 4, DEPOSITNO = 5, VIEW = 10;
    final int SUSPPROD_TYPE = 6, SUSPPROD_ID = 7, SUSPPROD_ACCN = 8;
    int viewType = -1;
    Date currDt = null;
    HashMap selectedMap;
    HashMap activeMemberLstMap;
    HashMap editMap;
    private boolean selectMode = false;
    ArrayList colourList = new ArrayList();
    /**
     * Creates new form AgentUI
     */
    public ActiveMemberListUI() {
        initComponents();
        initSetup();
        initComponentData();
        currDt = ClientUtil.getCurrentDate();
    }

    private void initSetup() {
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setMaxLenths();
        observable = new ActiveMemberListOB();
        observable.addObserver(this);
        ClientUtil.enableDisable(this, false); //__ Disables all when the screen appears for the 1st time
        setButtonEnableDisable();              //__ Enables/Disables the necessary buttons and menu items...
        //__ to reset the status...
        observable.fillDropDown();
        cboShareType.setModel(observable.getCbmShare_Type());
        Process.setEnabled(false);
        btnDelete.setVisible(false);
    }

    private void setObservable() {
        try {
            observable = new ActiveMemberListOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Auto Generated Method - setFieldNames()
     This method assigns name for all the components.
     Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        ShareType.setName("Share_Type");
        cboShareType.setName("cboShareType");
        tdtMeetingDate.setName("tdtMeetingDate");
        MeetingDate.setName("MeetingDate");
    }

    private void internationalize() {
    }

    public void update(Observable observed, Object arg) {
    }

    public int mapContainsValues(HashMap map) {
        int max = Integer.parseInt(Collections.max(map.keySet()).toString());
        int mapKey = 0;
        for (int i = 0; i < max; i++) {
            if (map.containsKey(i)) {
                mapKey = i;
                return mapKey;
            }
        }
        return mapKey;
    }

    public void updateOBFields() {
        observable.setTdtMeetingDate(tdtMeetingDate.getDateValue());
        observable.setTdtFromDate(tdtFromDate.getDateValue()); // Added by nithya on 26-09-2016 for 2775
        System.out.println("meeting date" + tdtMeetingDate.getDateValue() + ">>>>>>>>" + "selectedMap.size() : " + selectedMap.size() + " selectedMap : " + selectedMap);
        observable.setCboShareType(cboShareType.getSelectedItem().toString());
        //observable.setCollectedMap(selectedMap);
        observable.setCollectedMap(activeMemberLstMap);// Added by nithya
        observable.setDeSelectedMap(editMap);
        observable.doAction();
    }

    public CLabel getMeeting_Date() {
        return MeetingDate;
    }

    public void setMeeting_Date(CLabel Meeting_Date) {
        this.MeetingDate = Meeting_Date;
    }

    public CComboBox getCboShare_Type() {
        return cboShareType;
    }

    public void setCboShare_Type(CComboBox cboShare_Type) {
        this.cboShareType = cboShare_Type;
    }

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();

    }

    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    private void setMaxLenths() {
    }

    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());

        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());

        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());

        btnView.setEnabled(!btnView.isEnabled());
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panShare = new com.see.truetransact.uicomponent.CPanel();
        panActiveMember = new com.see.truetransact.uicomponent.CPanel();
        cPntblShare = new com.see.truetransact.uicomponent.CPanel();
        cPanel5 = new com.see.truetransact.uicomponent.CPanel();
        MeetingDate = new com.see.truetransact.uicomponent.CLabel();
        tdtMeetingDate = new com.see.truetransact.uicomponent.CDateField();
        ShareType = new com.see.truetransact.uicomponent.CLabel();
        cboShareType = new com.see.truetransact.uicomponent.CComboBox();
        Process = new com.see.truetransact.uicomponent.CButton();
        ScrShare = new javax.swing.JScrollPane();
        tblShare = new com.see.truetransact.uicomponent.CTable();
        lblFromDt = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        btnMemberList = new com.see.truetransact.uicomponent.CButton();
        tbrLoantProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace11 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace12 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace13 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace14 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace15 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace16 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        mbrShare = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptProcess = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        sptPrint = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(650, 600));
        setMinimumSize(new java.awt.Dimension(650, 600));
        setPreferredSize(new java.awt.Dimension(650, 600));

        panShare.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panShare.setFocusCycleRoot(true);
        panShare.setMinimumSize(new java.awt.Dimension(450, 500));
        panShare.setPreferredSize(new java.awt.Dimension(450, 600));
        panShare.setLayout(new java.awt.BorderLayout());

        panActiveMember.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panActiveMember.setFocusCycleRoot(true);
        panActiveMember.setMaximumSize(new java.awt.Dimension(450, 400));
        panActiveMember.setMinimumSize(new java.awt.Dimension(450, 400));
        panActiveMember.setPreferredSize(new java.awt.Dimension(450, 400));

        cPntblShare.setMaximumSize(new java.awt.Dimension(450, 200));
        cPntblShare.setMinimumSize(new java.awt.Dimension(450, 200));
        cPntblShare.setPreferredSize(new java.awt.Dimension(450, 200));

        cPanel5.setMaximumSize(new java.awt.Dimension(450, 200));
        cPanel5.setMinimumSize(new java.awt.Dimension(450, 200));
        cPanel5.setPreferredSize(new java.awt.Dimension(450, 200));
        cPntblShare.add(cPanel5);

        MeetingDate.setText("Meeting Date");

        ShareType.setText("Share Type");

        cboShareType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboShareTypeActionPerformed(evt);
            }
        });

        Process.setText("Active Members");
        Process.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProcessActionPerformed(evt);
            }
        });

        ScrShare.setMaximumSize(new java.awt.Dimension(700, 900));
        ScrShare.setMinimumSize(new java.awt.Dimension(700, 900));
        ScrShare.setOpaque(false);
        ScrShare.setPreferredSize(new java.awt.Dimension(550, 380));

        tblShare.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Branch Code", "Share Acct Num", "Customer Name", "Available Balance"
            }
        ));
        tblShare.setMaximumSize(new java.awt.Dimension(770, 110000));
        tblShare.setMinimumSize(new java.awt.Dimension(770, 110000));
        tblShare.setPreferredScrollableViewportSize(new java.awt.Dimension(550, 110000));
        tblShare.setPreferredSize(new java.awt.Dimension(770, 110000));
        tblShare.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblShareMouseClicked(evt);
            }
        });
        ScrShare.setViewportView(tblShare);

        lblFromDt.setText("From Date");

        chkSelectAll.setText("Select All");
        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });

        btnMemberList.setText("Members");
        btnMemberList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemberListActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panActiveMemberLayout = new javax.swing.GroupLayout(panActiveMember);
        panActiveMember.setLayout(panActiveMemberLayout);
        panActiveMemberLayout.setHorizontalGroup(
            panActiveMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panActiveMemberLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(panActiveMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ShareType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFromDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(panActiveMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(panActiveMemberLayout.createSequentialGroup()
                        .addComponent(cboShareType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(379, 379, 379))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panActiveMemberLayout.createSequentialGroup()
                        .addGroup(panActiveMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panActiveMemberLayout.createSequentialGroup()
                                .addComponent(tdtFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(MeetingDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tdtMeetingDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panActiveMemberLayout.createSequentialGroup()
                                .addGap(130, 130, 130)
                                .addComponent(btnMemberList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Process, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
            .addGroup(panActiveMemberLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(panActiveMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkSelectAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panActiveMemberLayout.createSequentialGroup()
                        .addComponent(ScrShare, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cPntblShare, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        panActiveMemberLayout.setVerticalGroup(
            panActiveMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panActiveMemberLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panActiveMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ShareType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboShareType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panActiveMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panActiveMemberLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(panActiveMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tdtFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblFromDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panActiveMemberLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panActiveMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tdtMeetingDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(MeetingDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(panActiveMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panActiveMemberLayout.createSequentialGroup()
                        .addGap(0, 266, Short.MAX_VALUE)
                        .addComponent(cPntblShare, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panActiveMemberLayout.createSequentialGroup()
                        .addGroup(panActiveMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panActiveMemberLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(chkSelectAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panActiveMemberLayout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(panActiveMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Process, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnMemberList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 17, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ScrShare, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        panShare.add(panActiveMember, java.awt.BorderLayout.CENTER);
        panActiveMember.getAccessibleContext().setAccessibleName("");

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrLoantProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnNew);

        lblSpace11.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace11.setText("     ");
        lblSpace11.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace11);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnEdit);

        lblSpace12.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace12.setText("     ");
        lblSpace12.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace12);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrLoantProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnSave);

        lblSpace13.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace13.setText("     ");
        lblSpace13.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace13);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrLoantProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnAuthorize);

        lblSpace14.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace14.setText("     ");
        lblSpace14.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace14);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnException);

        lblSpace15.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace15.setText("     ");
        lblSpace15.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace15);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrLoantProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnPrint);

        lblSpace16.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace16.setText("     ");
        lblSpace16.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace16.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace16.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace16);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnClose);

        mnuProcess.setText("Process");

        mitNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mitNew.setMnemonic('N');
        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        mitEdit.setMnemonic('E');
        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        mitDelete.setMnemonic('D');
        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptProcess);

        mitSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mitSave.setMnemonic('S');
        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setMnemonic('C');
        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitPrint.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        mitPrint.setMnemonic('P');
        mitPrint.setText("Print");
        mitPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPrintActionPerformed(evt);
            }
        });
        mnuProcess.add(mitPrint);
        mnuProcess.add(sptPrint);

        mitClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        mitClose.setMnemonic('l');
        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrShare.add(mnuProcess);

        setJMenuBar(mbrShare);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tbrLoantProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 174, Short.MAX_VALUE))
                    .addComponent(panShare, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tbrLoantProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panShare, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed

        // TODO add your handling code here:
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
//        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);

    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void btnCheck() {
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
    }

    public void authorizeStatus(String authorizeStatus) {
    }

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        {
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            javax.swing.JMenu mnu = TrueTransactMain.getMenu();
            String tit = "";
            try {
                tit = this.title.substring(0, (this.title.indexOf("[")) - 1);
            } catch (Exception e) {
                tit = this.title.trim();
            }
            if (tit.length() > 0) {
                int avail = 0;
                for (int m = 4; m < mnu.getItemCount(); m++) {
                    if (mnu.getItem(m).getText().equals(tit)) {
                        avail++;
                        mnu.remove(mnu.getItem(m));
                    }
                }
            }
            this.dispose();
        }
        // TODO add your handling code here:       
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        ShareType.setEnabled(false);
        cboShareType.setEnabled(false);
        MeetingDate.setEnabled(false);
        tdtMeetingDate.setEnabled(false);
        clear();
        observable.resetTable();
        observable.resetForm();
        
    }//GEN-LAST:event_btnCancelActionPerformed
    public void clear() {
        // observable.resetForm();
        //txtNoOfTokens.setText("");
        ClientUtil.clearAll(this);
        setButtonEnableDisable();
        //viewType = "";
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnAuthorize.setEnabled(false);
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
//            if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && selectedMap.size() == 0){
//                ClientUtil.showAlertWindow("Please select atleast one record and then save");
//                return;
//            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                boolean isChecked = true;
                String st = "";
                int tblShareRowCnt = tblShare.getRowCount();
                for (int i = 0; i < tblShareRowCnt; i++) {
                    st = CommonUtil.convertObjToStr(tblShare.getValueAt(i, 0));
                    if (st.equals("false")) {
                        isChecked = false;
                    }else{
                        isChecked = true;
                        break;
                    }
                }
                if(!isChecked){
                    ClientUtil.showAlertWindow("Please select atleast one record and then save");
                    return;
                }else{
                    activeMemberLstMap = new HashMap();                    
                    for (int i = 0; i < tblShareRowCnt; i++) {
                        ArrayList list = new ArrayList();
                        if (((Boolean) tblShare.getValueAt(i, 0)).booleanValue()) {
                            list.add(CommonUtil.convertObjToStr(tblShare.getValueAt(i, 1)));
                            list.add(CommonUtil.convertObjToStr(tblShare.getValueAt(i, 2)));
                            list.add(CommonUtil.convertObjToStr(tblShare.getValueAt(i, 3)));
                            list.add(CommonUtil.convertObjToStr(tblShare.getValueAt(i, 4)));
                            activeMemberLstMap.put(i, list);
                        }
                    }                   
                }
            }            
            updateOBFields();
        }
        
        //observable.doAction();
        observable.resetForm();
        System.out.println("SelectedRow======" + tblShare.getSelectedRowCount());
        ClientUtil.enableDisable(this, false);
        setModified(false);
        ClientUtil.clearAll(this);
        selectedMap = null;
        observable.ttNotifyObservers();
        btnCancelActionPerformed(null);

    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT); //__ Sets the Action Type to be performed...
        popUp(EDIT);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        cboShareType.setEnabled(false);
        tdtMeetingDate.setEnabled(false);
        Process.setEnabled(false);
        btnSave.setEnabled(true);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnView.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
    
        setButtonEnableDisable();             // Enables/Disables the necessary buttons and menu items...
        ClientUtil.enableDisable(this, true); // Enables the panel...
        // enableDisable(true);
        setButtonEnableDisable();
        // btnEmp.setEnabled(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        // observable.setStatus();
        // lblStatus.setText(observable.getLblStatus());
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(true);
        Process.setEnabled(true);
        btnCancel.setEnabled(true);
        setModified(true);
        btnSave.setEnabled(true);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnView.setEnabled(false);
        btnCancel.setEnabled(true);
        btnPrint.setEnabled(true);
        selectedMap = new HashMap();
    }//GEN-LAST:event_btnNewActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(null);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // TODO add your handling code here:
        btnPrintActionPerformed(null);
    }//GEN-LAST:event_mitPrintActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(null);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(null);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(null);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(null);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(null);
    }//GEN-LAST:event_mitNewActionPerformed

    /**
     * Exit the Application
     */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

    private void cboShareTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboShareTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboShareTypeActionPerformed

    private void ProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProcessActionPerformed
        HashMap shareTypeMap = new HashMap();
        HashMap whereMap = new HashMap();
        String ShareType = ((ComboBoxModel) cboShareType.getModel()).getKeyForSelected().toString();
        if (CommonUtil.convertObjToStr(ShareType).equals("")) {
            ClientUtil.showMessageWindow("Share type should not be empty");
            return;
        }
        Date meetingDate = DateUtil.getDateMMDDYYYY(tdtMeetingDate.getDateValue());
        System.out.println("ShareType==========" + ShareType + " meetingDate : " + meetingDate);
        if (null == meetingDate || meetingDate.equals("")) {
            ClientUtil.showMessageWindow("Meeting date should not be empty");
            return;
        }
        Date fromDate = CommonUtil.getProperDate(currDt,DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
        whereMap.put("FROM_DATE", fromDate);
        whereMap.put("MEETING_DATE", CommonUtil.getProperDate(currDt,meetingDate));
        whereMap.put("SHARE_TYPE", ShareType);
        shareTypeMap.put(CommonConstants.MAP_WHERE, whereMap);
        shareTypeMap.put(CommonConstants.MAP_NAME, "getShareAvailbaleBalanceList");
        try {
            // log.info("populateData...");
            observable.populateData(shareTypeMap, tblShare);
            tblShare.setModel(observable.getTblShare());
            selectMode = true;
            checkForExistingActiveMember(whereMap);
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
        setColour();
        System.out.println(" shareTypeMap=======" + shareTypeMap);
    }//GEN-LAST:event_ProcessActionPerformed

    private void tblShareMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblShareMouseClicked
        //  if(selectMode == true){
        String st = "";
        String customerName = "";
        String balance = "";
        String shareAcctNum = "";
        String branchCode = "";
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            System.out.println("New Mode selectedMap : ");
            int tablRow = tblShare.getSelectedRow();
            ArrayList list = new ArrayList();
            for (int i = 0; i < tblShare.getSelectedColumnCount(); i++) {
                st = CommonUtil.convertObjToStr(tblShare.getValueAt(tblShare.getSelectedRow(), 0));
                if (st.equals("true")) {
                    tblShare.setValueAt(new Boolean(false), tblShare.getSelectedRow(), 0);
                } else {
                    // Checking whether the selected member is already existing in ACTIVE_MEMBER_LIST for the given from and meeting date. 
                    //If yes, it cant be selected for further processing 
                    HashMap checkMap = new HashMap();
                    Date meetingDate = DateUtil.getDateMMDDYYYY(tdtMeetingDate.getDateValue());
                    Date fromDate = CommonUtil.getProperDate(currDt,DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
                    checkMap.put("SHARE_ACC_NUM",CommonUtil.convertObjToStr(tblShare.getValueAt(tblShare.getSelectedRow(), 3)));
                    checkMap.put("FROM_DATE",fromDate);
                    checkMap.put("MEETING_DATE",CommonUtil.getProperDate(currDt,meetingDate));      
                    List isMemberExistsMap = (List)ClientUtil.executeQuery("checkMemberActive", checkMap);
                    if (isMemberExistsMap != null && isMemberExistsMap.size() > 0) {
                      tblShare.setValueAt(new Boolean(false), tblShare.getSelectedRow(), 0);
                    }else{
                        tblShare.setValueAt(new Boolean(true), tblShare.getSelectedRow(), 0);
                        list.add(CommonUtil.convertObjToStr(tblShare.getValueAt(tblShare.getSelectedRow(), 1)));
                        list.add(CommonUtil.convertObjToStr(tblShare.getValueAt(tblShare.getSelectedRow(), 2)));
                        list.add(CommonUtil.convertObjToStr(tblShare.getValueAt(tblShare.getSelectedRow(), 3)));
                        list.add(CommonUtil.convertObjToStr(tblShare.getValueAt(tblShare.getSelectedRow(), 4)));
                    }
                }
            }
            System.out.println("st : " + st + " customerName : " + customerName + " balance : " + balance + " shareAcctNum : " + shareAcctNum + " branchCode : " + branchCode + " list : " + list);
            if (st.equals("false")) {
                selectedMap.put(tablRow, list);
                st = "true";
            } else {
                selectedMap.remove(tablRow);
            }
        }else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            System.out.println("Edit Mode selectedMap : "+observable.getCollectedMap());
            selectedMap = observable.getCollectedMap();
            System.out.println("selectedMap : "+selectedMap);
            int tablRow = tblShare.getSelectedRow();
            ArrayList list = new ArrayList();
            for (int i = 0; i < tblShare.getSelectedColumnCount(); i++) {
                st = CommonUtil.convertObjToStr(tblShare.getValueAt(tblShare.getSelectedRow(), 0));
                System.out.println("st : "+st);
                if (!st.equals("true")) {
                    tblShare.setValueAt(new Boolean(true), tblShare.getSelectedRow(), 0);
                } else {
                    tblShare.setValueAt(new Boolean(false), tblShare.getSelectedRow(), 0);
                    list.add(CommonUtil.convertObjToStr(tblShare.getValueAt(tblShare.getSelectedRow(), 1)));
                    list.add(CommonUtil.convertObjToStr(tblShare.getValueAt(tblShare.getSelectedRow(), 2)));
                    list.add(CommonUtil.convertObjToStr(tblShare.getValueAt(tblShare.getSelectedRow(), 3)));
                    list.add(CommonUtil.convertObjToStr(tblShare.getValueAt(tblShare.getSelectedRow(), 4)));
                }
            }
            if (st.equals("false")) {
                editMap.remove(tablRow);
                st = "true";
            } else {
                editMap.put(tablRow,list);                
                observable.setCollectedMap(selectedMap);
            }
            System.out.println("st : " + st + " customerName : " + customerName + " balance : " + balance + " shareAcctNum : " + shareAcctNum + " branchCode : " 
            + branchCode + " list : " + list+" selectedMap : "+selectedMap+" editMap : "+editMap);            
        }

    }//GEN-LAST:event_tblShareMouseClicked

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // TODO add your handling code here:
        for (int i = 0; i < tblShare.getRowCount(); i++) {
            tblShare.setValueAt(new Boolean(chkSelectAll.isSelected()), i, 0);
        }
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void btnMemberListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMemberListActionPerformed
        // TODO add your handling code here:
        HashMap shareTypeMap = new HashMap();
        HashMap whereMap = new HashMap();
        colourList = new ArrayList();
        String ShareType = ((ComboBoxModel) cboShareType.getModel()).getKeyForSelected().toString();
        if (CommonUtil.convertObjToStr(ShareType).equals("")) {
            ClientUtil.showMessageWindow("Share type should not be empty");
            return;
        }
        Date meetingDate = DateUtil.getDateMMDDYYYY(tdtMeetingDate.getDateValue());
        System.out.println("ShareType==========" + ShareType + " meetingDate : " + meetingDate);
        if (null == meetingDate || meetingDate.equals("")) {
            ClientUtil.showMessageWindow("Meeting date should not be empty");
            return;
        }
        Date fromDate = CommonUtil.getProperDate(currDt,DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
        whereMap.put("FROM_DATE", fromDate);
        whereMap.put("MEETING_DATE", CommonUtil.getProperDate(currDt,meetingDate));
        whereMap.put("SHARE_TYPE", ShareType);
        shareTypeMap.put(CommonConstants.MAP_WHERE, whereMap);
        shareTypeMap.put(CommonConstants.MAP_NAME, "getActiveMemberList");
        try {
            // log.info("populateData...");
            observable.populateData(shareTypeMap, tblShare);            
            tblShare.setModel(observable.getTblShare());
            selectMode = true; 
            checkForExistingActiveMember(whereMap);
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }       
        setColour();
        System.out.println(" shareTypeMap=======" + shareTypeMap);
    }//GEN-LAST:event_btnMemberListActionPerformed
    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    private void checkForExistingActiveMember(HashMap whereMap) {
        List existingActiveMemberDetailsList = (List) ClientUtil.executeQuery("getExistingActiveMemberDetails", whereMap);
        if (existingActiveMemberDetailsList != null && existingActiveMemberDetailsList.size() > 0) {
            for (int i = 0; i < existingActiveMemberDetailsList.size(); i++) {
                HashMap shareNoMap = (HashMap) existingActiveMemberDetailsList.get(i);
                String shareNo = CommonUtil.convertObjToStr(shareNoMap.get("SHARE_ACC_NUM"));
                for (int j = 0; j < tblShare.getRowCount(); j++) {
                    System.out.println("tablevalue :: " + CommonUtil.convertObjToStr(tblShare.getValueAt(j, 3)));
                    if (shareNo.equalsIgnoreCase(CommonUtil.convertObjToStr(tblShare.getValueAt(j, 3)))) {
                        colourList.add(String.valueOf(j));
                        break;
                    }
                }
            }
        }
    }
    
    private void initComponentData() {
        selectedMap = new HashMap();
        editMap = new HashMap();
        System.out.println("hai........");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new ActiveMemberListUI().show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CLabel MeetingDate;
    private com.see.truetransact.uicomponent.CButton Process;
    private javax.swing.JScrollPane ScrShare;
    private com.see.truetransact.uicomponent.CLabel ShareType;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnMemberList;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CPanel cPanel5;
    private com.see.truetransact.uicomponent.CPanel cPntblShare;
    private com.see.truetransact.uicomponent.CComboBox cboShareType;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblFromDt;
    private com.see.truetransact.uicomponent.CLabel lblSpace11;
    private com.see.truetransact.uicomponent.CLabel lblSpace12;
    private com.see.truetransact.uicomponent.CLabel lblSpace13;
    private com.see.truetransact.uicomponent.CLabel lblSpace14;
    private com.see.truetransact.uicomponent.CLabel lblSpace15;
    private com.see.truetransact.uicomponent.CLabel lblSpace16;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CMenuBar mbrShare;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panActiveMember;
    private com.see.truetransact.uicomponent.CPanel panShare;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CTable tblShare;
    private javax.swing.JToolBar tbrLoantProduct;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtMeetingDate;
    // End of variables declaration//GEN-END:variables

    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        HashMap whereMap = new HashMap();
        HashMap operativeMap = new HashMap();
        HashMap depositMap = new HashMap();
        whereMap.put("BRANCH_CODE", getSelectedBranchID());
        System.out.println("whereMap filldata : "+whereMap);
        if (field == EDIT) { //Edit=0 and Delete=1            
            ArrayList lst = new ArrayList();
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectRecordCountDetails");
            new ViewAll(this, viewMap, false).show();
        }
    }

    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        HashMap OAprodDescMap = new HashMap();
        HashMap depProdDescMap = new HashMap();
        if (viewType == EDIT || viewType == VIEW) {
            hash.put("BRANCH_CODE", getSelectedBranchID());
            System.out.println("hash : "+hash);
            observable.populateDataInTable(hash,tblShare);     //__ Called to display the Data in the UI fields...     
            tblShare.setModel(observable.getTblShare());
            tdtFromDate.setDateValue((CommonUtil.convertObjToStr(hash.get("FROM_DT")))); // NITHYA
            tdtMeetingDate.setDateValue((CommonUtil.convertObjToStr(hash.get("MEETING_DT")))); // NITHYA
        } 
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    
   private void setColour() {
        /* Set a cellrenderer to this table in order format the date */
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (colourList.contains(String.valueOf(row))) {
                    setForeground(Color.RED);                                       
                } else {
                    setForeground(Color.BLACK);                    
                }
                // Set oquae
                this.setOpaque(true);
                return this;
            }
        };
        tblShare.setDefaultRenderer(Object.class, renderer);
    }
}
