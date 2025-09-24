/*
 * Pass Book.java
 * Swaroop 
 * Created on December 07, 2009, 1:46 PM
 */
package com.see.truetransact.ui.generalledger;

import com.see.truetransact.ui.common.viewall.*;
import com.ibatis.db.sqlmap.SqlMap;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.apache.log4j.Logger;

import com.see.truetransact.uicomponent.CDialog;
import com.see.truetransact.uicomponent.CLabel;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.*;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.salaryrecovery.MDSDetailsUI;
import com.see.truetransact.uicomponent.*;
import java.util.LinkedHashMap;

import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.ToDateValidation;
import java.awt.event.*;
import java.util.Date;
import javax.swing.KeyStroke;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * @author Swaroop
 */
public class AccountHeadOrderingUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer {

    private AccountHeadOrderingOB observable;
    HashMap passMap = new HashMap();
    HashMap paramMap = null;
    int amtColumnNo = 0;
    double tot = 0;
    int act=0;
    String amtColName = "";
    String behavesLike = "";
    private LinkedHashMap Selected = new LinkedHashMap();
    private LinkedHashMap NotSelected = new LinkedHashMap();
    public static final boolean isFrameClosed = false;
    private final static Logger log = Logger.getLogger(AccountHeadOrderingUI.class);
    private static SqlMap sqlMap = null;
    private TransDetailsUI transDetails = null;
    private ComboBoxModel cbmbranch;
    private ComboBoxModel cbmMjrHead;
    private int checkValue=0;
    private boolean btnCancel=false;
    HashMap actMap = null;
    double changeValue = 0.0;
    HashMap map = new HashMap();
    private TableModelListener tableModelListener;
    String mjrHeadId = "";
    String mjrHeadDesc = "";

    public int getCheckValue() {
        return checkValue;
    }

    public void setCheckValue(int checkValue) {
        this.checkValue = checkValue;
    }
    
    
     public ComboBoxModel getCbmbranch() {
        return cbmbranch;
    }
    
    public void setCbmbranch(ComboBoxModel cbmbranch) {
        this.cbmbranch = cbmbranch;
    }
    /**
     * Creates new form PassBook
     */
    public AccountHeadOrderingUI() {

        setupInit();
        setupScreen();
    }

    public AccountHeadOrderingUI(String mjrHead, String desc) {
        setObservable();
        initComponents();
        mjrHeadId = mjrHead;
        mjrHeadDesc = desc;
        setupInit();
    }

    private void setupInit() {
        initComponents();
        setObservable();
        toFront();
        fillMjrHead();
        observable.resetForm();
        update(observable, null);
        populateData();        
        setModified(true);
    }

    private void setupScreen() {
//        setModal(true);

        /*
         * Calculate the screen size
         */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        /*
         * Center frame on the screen
         */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    private void setObservable() {
        try {
            observable = AccountHeadOrderingOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateOBFields() {
      
    }

  
    
    public void fillMjrHead() {
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        HashMap mapShare = new HashMap();
        List keyValue = null;
        keyValue = ClientUtil.executeQuery("getMjrAcHdList", mapShare);
        key.add("");
        value.add("");
        if (keyValue != null && keyValue.size() > 0) {
            for (int i = 0; i < keyValue.size(); i++) {
                mapShare = (HashMap) keyValue.get(i);
                value.add(mapShare.get("Description"));
                key.add(mapShare.get("Major A/C HEAD Code"));
            }
        }
        cbmMjrHead = new ComboBoxModel(key, value);
        key = null;
        value = null;
        keyValue.clear();
        keyValue = null;
        mapShare.clear();
        mapShare = null;
    }
    public void populateData() {
        lblAccountHeadDetails.setText(mjrHeadId + "- " + mjrHeadDesc);
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();      
        System.out.println("MJR_AC_HD_ID :: " + mjrHeadId);
        whereMap.put("MJR_AC_HD_ID",mjrHeadId);
        viewMap.put(CommonConstants.MAP_NAME, "getAllAccountHeads" );        
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {
            log.info("populateData...");
            ArrayList heading = observable.populateData(viewMap, tblData);
            heading = null;
            javax.swing.table.TableColumn col = tblData.getColumn(tblData.getColumnName(1));
            col.setMaxWidth(300);
            col.setMinWidth(300);
            col.setWidth(300);
            col.setPreferredWidth(300);
            col = tblData.getColumn(tblData.getColumnName(2));
            col.setMaxWidth(120);
            col.setMinWidth(120);
            col.setWidth(120);
            col.setPreferredWidth(120);
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }        
        viewMap = null;
        whereMap = null;
    }

    public void show() {
        /*
         * The following if condition commented by Rajesh Because observable is
         * not making null after closing the UI So, if no data found in
         * previously opened EnquiryUI instance the observable.isAvailable() is
         * false, so EnquiryUI won't open.
         */
//        if (observable.isAvailable()) {
        super.show();
//        }
    }
    
    public void setVisible(boolean visible) {
            super.setVisible(visible);
    }

    /**
     * Bring up and populate the temporary project detail screen.
     */
    private void whenTableRowSelected() {
        this.dispose();

    }

    private String getLockDetails(String lockedBy, String screenId) {
        HashMap map = new HashMap();
        StringBuffer data = new StringBuffer();
        map.put("LOCKED_BY", lockedBy);
        map.put("SCREEN_ID", screenId);
        List lstLock = ClientUtil.executeQuery("getLockedDetails", map);
        map.clear();
        if (lstLock.size() > 0) {
            map = (HashMap) (lstLock.get(0));
            data.append("\nLog in Time : ").append(map.get("LOCKED_TIME"));
            data.append("\nIP Address : ").append(map.get("IP_ADDR"));
            data.append("\nBranch : ").append(map.get("BRANCH_ID"));
        }
        lstLock = null;
        map = null;
        return data.toString();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rdgAndOr = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoPassBookType = new com.see.truetransact.uicomponent.CButtonGroup();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        panArrow = new com.see.truetransact.uicomponent.CPanel();
        btnUpArrow = new com.see.truetransact.uicomponent.CButton();
        btnDownArrow = new com.see.truetransact.uicomponent.CButton();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnReset = new com.see.truetransact.uicomponent.CButton();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHeadDetails = new com.see.truetransact.uicomponent.CLabel();

        setTitle("Account Head Ordering");
        setMinimumSize(new java.awt.Dimension(800, 500));
        setPreferredSize(new java.awt.Dimension(800, 500));

        srcTable.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        srcTable.setPreferredSize(new java.awt.Dimension(452, 380));

        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblData.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 13)); // NOI18N
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDataMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDataMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblDataMouseReleased(evt);
            }
        });
        tblData.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tblDataMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblDataMouseMoved(evt);
            }
        });
        tblData.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblDataFocusLost(evt);
            }
        });
        tblData.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblDataPropertyChange(evt);
            }
        });
        tblData.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblDataKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblDataKeyReleased(evt);
            }
        });
        srcTable.setViewportView(tblData);

        javax.swing.GroupLayout panTableLayout = new javax.swing.GroupLayout(panTable);
        panTable.setLayout(panTableLayout);
        panTableLayout.setHorizontalGroup(
            panTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panTableLayout.createSequentialGroup()
                .addGap(0, 27, Short.MAX_VALUE)
                .addComponent(srcTable, javax.swing.GroupLayout.PREFERRED_SIZE, 615, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panTableLayout.setVerticalGroup(
            panTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(srcTable, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        btnUpArrow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/Rs_in.gif"))); // NOI18N
        btnUpArrow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpArrowActionPerformed(evt);
            }
        });

        btnDownArrow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/Rs_out.gif"))); // NOI18N
        btnDownArrow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownArrowActionPerformed(evt);
            }
        });

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panArrowLayout = new javax.swing.GroupLayout(panArrow);
        panArrow.setLayout(panArrowLayout);
        panArrowLayout.setHorizontalGroup(
            panArrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panArrowLayout.createSequentialGroup()
                .addGroup(panArrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panArrowLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(btnUpArrow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panArrowLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(btnDownArrow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panArrowLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panArrowLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panArrowLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panArrowLayout.setVerticalGroup(
            panArrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panArrowLayout.createSequentialGroup()
                .addGap(115, 115, 115)
                .addComponent(btnUpArrow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDownArrow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(95, Short.MAX_VALUE))
        );

        cLabel1.setText("Account Head Details");

        lblAccountHeadDetails.setForeground(new java.awt.Color(0, 51, 204));
        lblAccountHeadDetails.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblAccountHeadDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8)
                .addComponent(panArrow, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panArrow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 105, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAccountHeadDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblDataMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseReleased
        // TODO add your handling code here:
//        Object ob= new Boolean(true);
//        tblData.setValueAt(ob, tblData.getSelectedRow(), 0);
    }//GEN-LAST:event_tblDataMouseReleased

    private void tblDataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblDataKeyPressed
        // TODO add your handling code here:
        System.out.println("key code :: " + evt.getKeyCode());
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
//         if(tblData.getSelectedRowCount() > 0 && CommonUtil.convertObjToInt(tblData.getValueAt(tblData.getSelectedRow(), 4)) > 0 && evt.getKeyCode()==KeyEvent.VK_ENTER) {
//            System.out.println("Enter pressed");
//            int selRow = tblData.getSelectedRow();
//            int enteredOrder = CommonUtil.convertObjToInt(tblData.getValueAt(tblData.getSelectedRow(), 4));
//            int newOrderRow = 0;
//            System.out.println("enteredOrder :: " + enteredOrder);
//            for(int i=0; i<tblData.getRowCount();i++){
//                if(CommonUtil.convertObjToStr(tblData.getValueAt(i, 0)).equals(String.valueOf(enteredOrder))){
//                    newOrderRow = i;
//                    break;
//                }
//            }
//            System.out.println("newOrderRow :: " + newOrderRow);
//            if(selRow > newOrderRow){
//                String mjrHdDesc = "";
//                String mjrHdId = "";
//                String mjrHdType = "";
//                String storeMajrHdType = CommonUtil.convertObjToStr(tblData.getValueAt(selRow, 1));
//                String storeDesc = CommonUtil.convertObjToStr(tblData.getValueAt(selRow, 2));
//                String storeHdId = CommonUtil.convertObjToStr(tblData.getValueAt(selRow, 3));
//                for(int j= selRow-1; j>= newOrderRow; j--){
//                    mjrHdType =  CommonUtil.convertObjToStr(tblData.getValueAt(j, 1));
//                    mjrHdDesc =  CommonUtil.convertObjToStr(tblData.getValueAt(j, 2));
//                    mjrHdId =  CommonUtil.convertObjToStr(tblData.getValueAt(j, 3));
//                    tblData.setValueAt(mjrHdType, j+1, 1);
//                    tblData.setValueAt(mjrHdDesc, j+1, 2);
//                    tblData.setValueAt(mjrHdId, j+1, 3);
//                }
//                tblData.setValueAt(storeMajrHdType, newOrderRow, 1);
//                tblData.setValueAt(storeDesc, newOrderRow, 2);
//                tblData.setValueAt(storeHdId, newOrderRow, 3);
//            }  
//            if(selRow < newOrderRow){
//               String mjrHdDesc = "";
//               String mjrHdId = "";
//               String mjrHdType = "";
//               String storeMajrHdType = CommonUtil.convertObjToStr(tblData.getValueAt(selRow, 1));
//               String storeDesc = CommonUtil.convertObjToStr(tblData.getValueAt(selRow, 2));
//               String storeHdId = CommonUtil.convertObjToStr(tblData.getValueAt(selRow, 3)); 
//               for(int k=selRow+1; k<=newOrderRow ; k++){
//                    mjrHdType =  CommonUtil.convertObjToStr(tblData.getValueAt(k, 1));
//                    mjrHdDesc =  CommonUtil.convertObjToStr(tblData.getValueAt(k, 2));
//                    mjrHdId =  CommonUtil.convertObjToStr(tblData.getValueAt(k, 3));
//                    tblData.setValueAt(mjrHdType, k-1, 1);
//                    tblData.setValueAt(mjrHdDesc, k-1, 2);
//                    tblData.setValueAt(mjrHdId, k-1, 3);  
//               }
//               tblData.setValueAt(storeMajrHdType, newOrderRow, 1);
//               tblData.setValueAt(storeDesc, newOrderRow, 2);
//               tblData.setValueAt(storeHdId, newOrderRow, 3);
//            }
//        }
    }//GEN-LAST:event_tblDataKeyPressed

    private void tblDataKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblDataKeyReleased
    }//GEN-LAST:event_tblDataKeyReleased

    private void tblDataFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblDataFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDataFocusLost
    boolean btnIntDetPressed = false;

    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        System.out.println("#$#$ Hash : " + hash); 
        observable.refreshTable(); 
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    private void tblDataMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseDragged
        // TODO add your handling code here:          
    }//GEN-LAST:event_tblDataMouseDragged

    private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed
        // Add your handling code here:
//       // int rowcnt = tblData.getRowCount();
//        int row = tblData.getSelectedRow();
//        //double changeValue = 0.0;
//       // Object ob = new Boolean(false);
//        //for (int i = 0; i < rowcnt; i++) {
//        tblData.setEditingColumn(2);
//        tblData.setEditingRow(row);
//        tblData.setCellEditor(null);
//        //tblData.setCellSelectionEnabled(true);
//        tblData.isCellEditable(row, 2);
//         
            //tblData.setValueAt(changeValue, row, 2);
            
           // System.out.println("row count "+rowcnt);
           // System.out.println("row slct "+row);
           // System.out.println("row changeValue "+changeValue);
        //}
//        ob = new Boolean(true);
//        for (int i = row; i < rowcnt; i++) {
//            tblData.setValueAt(changeValue, i, 2);
//            tblData.isC
        //}
        
        //tblData.setValueAt(ob, tblData.getSelectedRow(), 0);

    }//GEN-LAST:event_tblDataMousePressed

    private void tblDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseMoved
        // Add your handling code here:
    }//GEN-LAST:event_tblDataMouseMoved

    
    private void setTableModelListener() {
       // flag = false;
        try {
            tableModelListener = new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                   // if (e.getType() == TableModelEvent.UPDATE  ) {
                        System.out.println("Cell " + e.getFirstRow() + ", " + e.getColumn() + " changed. The new value: " + 
                                tblData.getModel().getValueAt(e.getFirstRow(), e.getColumn()));
                        int row = e.getFirstRow();
                        int column = e.getColumn();
                        int selectedRow = tblData.getSelectedRow();
                        boolean chk = ((Boolean) tblData.getValueAt(tblData.getSelectedRow(), 0)).booleanValue();
                        String scheme = CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 1));
//                        switch (column) {
//                            case 4: 
//                                TableModel model = tblData.getModel();
//                                String acc_no = CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 3));
//                                String noOfInsPay = CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 4));
//                                //System.out.println("AC NO=" + acc_no + " finalMap BEDFORE====" + finalMap);
//                                if (scheme.equals("TL")) {
//                                    System.out.println("11111111111111111");
//                                    getLoanDetails("3", column, chk, "empty", selectedRow);
//                                    //System.out.println("column =========== " + column);
//                                } else if (scheme.equals("TD")) {
//                                    System.out.println("222222222222222222222222");
//                                    getTDDetails(column, chk, "empty", selectedRow);
//                                } else if (scheme.equals("SA")) {
//                                    System.out.println("333333333333333333");
//                                    //if(columnNo==4){
//                                        getSADetails(column, chk, "empty", selectedRow);
//                                    //}
//                                } else if (scheme.equals("GL")) {
//                                    System.out.println("4444444444444444444444");
//                                    getSBDetails(column, chk, "empty", selectedRow);
//                                } else if (scheme.equals("MDS")) {
//                                    System.out.println("555555555555555555555");
//                                    if (acc_no.indexOf("_") != -1) {
//                                        acc_no = acc_no.substring(0, acc_no.indexOf("_"));
//                                    }
//                                    java.awt.event.MouseEvent evt = null;
//                                    calcEachChittal(column, chk, "empty", selectedRow);//e.
//                                    //mdsSplitUp();
//                                    if (column == 3) {
//                                        //System.out.println("in table mousclickkkkkkk " + column);
//                                        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
//
//                                            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                                                //     //System.out.println("in table mousclickkkkkkk "+column);
////                                                  tblTransactionMouseClicked(evt);
//                                            }
//                                        });
//                                    } 
//                                } else if (scheme.equals("AD")) {
//                                    getADDetails(column, chk, "empty", selectedRow);
//                                } else if (column == 3) {
//                                    getSBDetails(column, chk, "empty", selectedRow);
//                                }
//                                //System.out.println("calacToatal sel======" + selectedRow + " chk==" + chk + " column===" + column);
//                                calcTotal(chk, selectedRow, column);
//                                break;
//                            case 6:
//                                if(scheme.equals("TL") || scheme.equals("AD")){
//                                    TLGetDetails(column,scheme);
//                                }
//                                if(scheme.equals("TD")){
//                                     getTDDetails(column, chk, "empty", selectedRow);
//                                }
//                                calcTotal(chk, selectedRow, column);
//                                break;
//                            case 0:  calcTotal(chk, selectedRow, column);
//                                acc_no = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 3));
//                                if (scheme.equals("MDS")) {
//                                    if (acc_no.indexOf("_") != -1) {
//                                        acc_no = acc_no.substring(0, acc_no.indexOf("_"));
//                                    }
//                                } 
//                                break;
//                            case 7:  if (scheme.equals("SA")) {
//                                     //if(columnNo==7){
//                                        getSADetails(column, chk, "empty", selectedRow);
//                                     //}
//                                }  if(scheme.equals("TL") || scheme.equals("AD")){
//                                    TLGetDetails(column,scheme);
//                                }
//                                calcTotal(chk, selectedRow, column);
//                                break;
//                            default:
//                                calcTotal(chk, selectedRow, column);
//                                break;    }
                   // }
                }
            };
            tblData.getModel().addTableModelListener(tableModelListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
     
    public void setTblModel(final CTable tbl, ArrayList tblData, ArrayList head) {
        TableSorter tableSorter = new TableSorter();
        //  tableSorter.addMouseListenerToHeaderInTable(tbl);
        // Modified mColIndex == 13 by nithya on 05-03-2016 for 0003914 
        com.see.truetransact.clientutil.TableModel tableModel = new com.see.truetransact.clientutil.TableModel(tblData, head) {

            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if (mColIndex == 0 || mColIndex == 4 || mColIndex == 5 || mColIndex == 6 || mColIndex == 7 || mColIndex == 9 || mColIndex == 11
                        || mColIndex == 12 || mColIndex == 13) {
                    //tbl.setValueAt(map, rowIndex, mColIndex);
                    return true;
                } else {
                    return false;
                }
            }
        };

        tableModel.fireTableDataChanged();
        tableSorter.setModel(tableModel);
        tableSorter.fireTableDataChanged();

        tbl.setModel(tableSorter);
        tbl.revalidate();
    }
   
    private void tblDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseClicked
        // TODO add your handling code here:
        
      //  setTblModel(tblData, null, null);
//        int rowSelect=tblData.getSelectedRow();
//        changeValue=CommonUtil.convertObjToDouble(tblData.getValueAt(rowSelect, 2));
//        tblData.setValueAt(changeValue, rowSelect, 2);
        
//    try {
//        System.out.println("evt.getClickCount() "+evt.getClickCount());
//        if (evt.getClickCount() > 0) {
//           // setTableModelListener();
//            tblData.isCellEditable(tblData.getSelectedRow(), 2);
//            String branchCode = CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 1)); 
//            String closeBal = CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 2)); 
//            String majorHead = CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 3)); 
//            String account = CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 4)); 
//              
//                    ArrayList newList = new ArrayList(); 
//                        System.out.println("IN tblTransactionMouseClicked");
//                       
//                        newList.add(CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 1)));
//                        newList.add(CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 2)));
//                        newList.add(CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 3)));
//                        newList.add(CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 4)));
//                        newList.add(tdtClosingYeara.getDateValue());
//                        
//                        System.out.println("newList "+newList);
//                        actMap.put("ENTRY_DETAILS", newList);
//                       // tblData.setValueAt(newList, tblData.getSelectedRow(), 1);
//                        //newList.add(CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 2)));
//                       // calcTotal(chk, tblTransaction.getSelectedRow(), 0);
//                       
//                       // termMdsMap = new HashMap();
////                        HashMap mdssplitMap = new HashMap();
////                        mdssplitMap.put("CLOCK_NO", txtClockNo.getText());
////                        mdssplitMap.put("MEMBER_NO", txtMemberNo.getText());
////                        mdssplitMap.put("CUST_NAME", txtName.getText());
////                        mdssplitMap.put("TOT_PRIN", mdsPrinctot);
////                        mdssplitMap.put("TOT_PENAL", mdsPenaltot);
////                        mdssplitMap.put("TOT_INT", mdsInttot);
////                        mdssplitMap.put("TOT_OTHERS", mdsOthrtot);
////                        mdssplitMap.put("TOT_GRAND", mdsgrandtot);
////                        mdssplitMap.put("CHARGES", new Double(0));
////                        mdssplitMap.put("ACT_NUM", chittalNo);
////                        mdssplitMap.put("PROD_TYPE", "MDS");
////                        mdssplitMap.put("SPLIT_DETAILS", mds.getNewTot());
////                        mdssplitMap.put("PROD_ID", prod_id);
////                        mdssplitMap.put("PROD_DESCRIPTION",description);
////                         
////                       actMap
//            //selectedScheme();
//          //  calcTotal(true, tblTransaction.getSelectedRow(), 0);
//        }
//        ////gl_opening update, check customer id
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
    
    
//    try {
//        
//        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && evt.getClickCount() > 0) {
//            setTableModelListener();
//            String scheme1 = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 0));
//            String noOfInsPay1 = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 4));
//            String principal1 = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 5));
//            String interest1 = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 7));
//            String description = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 2));
//            if (scheme1.equals("AD") && (!noOfInsPay1.equals(""))) {
//                double prin = CommonUtil.convertObjToDouble(principal1);
//                double paying = CommonUtil.convertObjToDouble(noOfInsPay1);
//                double intr = CommonUtil.convertObjToDouble(interest1);
//                if (intr > 0) {
//                    prin = paying - intr;
//                    tblTransaction.setValueAt(prin, tblTransaction.getSelectedRow(), 5);
//                }
//            } else {
//                if (evt.getClickCount() > 0 && evt.getClickCount() == 2) {
//                    String scheme = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 1));
//                    String noOfInsPay = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 4));
//                    String chittalNo = (CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 3)));
//                    String prod_id = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 14));
//                    String principal = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 5));
//                    String interest = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 7));
//                    ArrayList newList = new ArrayList();
//                    //System.out.println("clkno is " + txtClockNo.getText());
//                    if (scheme.equals("MDS") && (!noOfInsPay.equals(""))) {
//                        //System.out.println("IN tblTransactionMouseClicked");
//                        String acc_no1 = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 3));
//                        if (scheme.equals("MDS")) {
//                            if (acc_no1.indexOf("_") != -1) {
//                                acc_no1 = acc_no1.substring(0, acc_no1.indexOf("_"));
//                            }
//                        }
//                        if (finalMap != null && finalMap.containsKey(acc_no1)) {
//                            finalMap.remove(acc_no1);
//                        }
//                        newList.add(noOfInsPay);
//                        newList.add(CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 3)));
//                        newList.add(CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 12)));
//                        newList.add(CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 10)));
//                        newList.add(CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 11)));
//                        newList.add(CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 13)));
//                        newList.add(CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 2)));
//                        //System.out.println("newList newList" + newList);
//                        // MDSDetailsUI mdsUi=
//                        //new MDSDetailsUI().getMdsdetail(newList);
//                        //System.out.println("clkno is1 " + txtClockNo.getText());
//                        String clkno = txtClockNo.getText();
//                        MDSDetailsUI mds = new MDSDetailsUI(newList);
//                        chittalFlag = true;
//                        mds.show();
//                        ////System.out.println("clkno is 2"+txtClockNo.getText());
//                        //new  MDSDetailsUI(newList).show();
//                        // //System.out.println("intre2 intre2 "+mds.getInterest());
//                        ////System.out.println("getNewTot getNewTot "+mds.getNewTot());
//                        // //System.out.println("clkno is 3"+txtClockNo.getText());
//                        txtClockNo.setText(clkno);
//                        if (mds.getInterest() >= 0) {
//                            tblTransaction.setValueAt(mds.getInterest(), tblTransaction.getSelectedRow(), 7);
//                        } else {
//                            tblTransaction.setValueAt(interest, tblTransaction.getSelectedRow(), 7);
//                        }
//                        //txtGrandTotal.setText(String.valueOf((Double.parseDouble(txtGrandTotal.getText()))+mds.getInterest()-(Double.parseDouble(txtTotInterest.getText()))));
//                        //txtTotInterest.setText(String.valueOf(mds.getInterest()));
//                        boolean chk = ((Boolean) tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 0)).booleanValue();
//                        calcTotal(chk, tblTransaction.getSelectedRow(), 0);
//                        double mdsPrinctot = 0.0;
//                        double mdsPenaltot = 0.0;
//                        double mdsInttot = 0.0;
//                        double mdsOthrtot = 0.0;
//                        double mdsgrandtot = 0.0;
//                        mdsPrinctot = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 5));
//                        mdsPenaltot = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 6));
//                        mdsInttot = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 7));
//                        mdsOthrtot = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 9));
//                        mdsgrandtot = mdsPrinctot + mdsPenaltot + mdsInttot + mdsOthrtot;
//                        termMdsMap = new HashMap();
//                        HashMap mdssplitMap = new HashMap();
//                        mdssplitMap.put("CLOCK_NO", txtClockNo.getText());
//                        mdssplitMap.put("MEMBER_NO", txtMemberNo.getText());
//                        mdssplitMap.put("CUST_NAME", txtName.getText());
//                        mdssplitMap.put("TOT_PRIN", mdsPrinctot);
//                        mdssplitMap.put("TOT_PENAL", mdsPenaltot);
//                        mdssplitMap.put("TOT_INT", mdsInttot);
//                        mdssplitMap.put("TOT_OTHERS", mdsOthrtot);
//                        mdssplitMap.put("TOT_GRAND", mdsgrandtot);
//                        mdssplitMap.put("CHARGES", new Double(0));
//                        mdssplitMap.put("ACT_NUM", chittalNo);
//                        mdssplitMap.put("PROD_TYPE", "MDS");
//                        mdssplitMap.put("SPLIT_DETAILS", mds.getNewTot());
//                        mdssplitMap.put("PROD_ID", prod_id);
//                        mdssplitMap.put("PROD_DESCRIPTION",description);
//                        if (getRdoPrizedMember_Yes() == true) {
//                            mdssplitMap.put("PRIZED_MEMBER", "Y");
//                        } else {
//                            mdssplitMap.put("PRIZED_MEMBER", "N");
//                        }
//                        termMdsMap.put("MDS", mdssplitMap);
//                        finalMap.put(chittalNo, termMdsMap);
//                    }
//                }
//            }
//            //selectedScheme();
//            calcTotal(true, tblTransaction.getSelectedRow(), 0);
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
    }//GEN-LAST:event_tblDataMouseClicked

    private void tblDataPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblDataPropertyChange
     if(tblData.getSelectedRowCount() > 0 && CommonUtil.convertObjToInt(tblData.getValueAt(tblData.getSelectedRow(), 3)) > 0 ) {
            
            int selRow = tblData.getSelectedRow();
            int enteredOrder = CommonUtil.convertObjToInt(tblData.getValueAt(tblData.getSelectedRow(), 3));
            int newOrderRow = 0;
            for(int i=0; i<tblData.getRowCount();i++){
                if(CommonUtil.convertObjToStr(tblData.getValueAt(i, 0)).equals(String.valueOf(enteredOrder))){
                    newOrderRow = i;
                    break;
                }
            }
            if(selRow > newOrderRow){
                String mjrHdDesc = "";
                String mjrHdId = "";
                String storeDesc = CommonUtil.convertObjToStr(tblData.getValueAt(selRow, 1));
                String storeHdId = CommonUtil.convertObjToStr(tblData.getValueAt(selRow, 2));
                for(int j= selRow-1; j>= newOrderRow; j--){
                    mjrHdDesc =  CommonUtil.convertObjToStr(tblData.getValueAt(j, 1));
                    mjrHdId =  CommonUtil.convertObjToStr(tblData.getValueAt(j, 2));
                    tblData.setValueAt(mjrHdDesc, j+1, 1);
                    tblData.setValueAt(mjrHdId, j+1, 2);
                }
                tblData.setValueAt(storeDesc, newOrderRow, 1);
                tblData.setValueAt(storeHdId, newOrderRow, 2);
            }  
            if(selRow < newOrderRow){
               String mjrHdDesc = "";
               String mjrHdId = "";               
               String storeDesc = CommonUtil.convertObjToStr(tblData.getValueAt(selRow, 1));
               String storeHdId = CommonUtil.convertObjToStr(tblData.getValueAt(selRow, 2)); 
               for(int k=selRow+1; k<=newOrderRow ; k++){
                    mjrHdDesc =  CommonUtil.convertObjToStr(tblData.getValueAt(k, 1));
                    mjrHdId =  CommonUtil.convertObjToStr(tblData.getValueAt(k, 2));
                    tblData.setValueAt(mjrHdDesc, k-1, 1);
                    tblData.setValueAt(mjrHdId, k-1, 2);  
               }
               tblData.setValueAt(storeDesc, newOrderRow, 1);
               tblData.setValueAt(storeHdId, newOrderRow, 2);
            }
            tblData.setValueAt("",tblData.getSelectedRow(), 3);
        }
    }//GEN-LAST:event_tblDataPropertyChange

    private void btnUpArrowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpArrowActionPerformed
        // TODO add your handling code here:
         if(tblData.getSelectedRowCount() > 0) {
            int selRow = tblData.getSelectedRow();
            if(selRow != 0){ 
                int prevRow = selRow - 1;
                String selRowHdType = CommonUtil.convertObjToStr(tblData.getValueAt(selRow, 1));
                String selrowHdDesc = CommonUtil.convertObjToStr(tblData.getValueAt(selRow, 2));
                
                String prevHdType = CommonUtil.convertObjToStr(tblData.getValueAt(prevRow, 1));
                String prevDesc = CommonUtil.convertObjToStr(tblData.getValueAt(prevRow, 2));
                
                tblData.setValueAt(prevHdType, selRow, 1);
                tblData.setValueAt(prevDesc, selRow, 2);
                
                tblData.setValueAt(selRowHdType, prevRow, 1);
                tblData.setValueAt(selrowHdDesc, prevRow, 2);
                
                tblData.setRowSelectionInterval(prevRow, prevRow);
            }            
         } 
    }//GEN-LAST:event_btnUpArrowActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        mjrHeadId = "";
        mjrHeadDesc = "";
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        populateData();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnDownArrowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownArrowActionPerformed
        // TODO add your handling code here:
        if(tblData.getSelectedRowCount() > 0) {
            int selRow = tblData.getSelectedRow();
            int rowCount = tblData.getRowCount();
            if(selRow != rowCount-1){ 
                int nextRow = selRow + 1;
                String selrowHdType = CommonUtil.convertObjToStr(tblData.getValueAt(selRow, 1));
                String selrowHdDesc = CommonUtil.convertObjToStr(tblData.getValueAt(selRow, 2));
                
                String nextHdType = CommonUtil.convertObjToStr(tblData.getValueAt(nextRow, 1));
                String nextDesc = CommonUtil.convertObjToStr(tblData.getValueAt(nextRow, 2));
                
                tblData.setValueAt(nextHdType, selRow, 1);
                tblData.setValueAt(nextDesc, selRow, 2);
                
                tblData.setValueAt(selrowHdType, nextRow, 1);
                tblData.setValueAt(selrowHdDesc, nextRow, 2);
                
                tblData.setRowSelectionInterval(nextRow, nextRow);
            }            
         } 
    }//GEN-LAST:event_btnDownArrowActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        if(tblData.getRowCount() > 0){            
            for(int i=0; i<tblData.getRowCount(); i++){
                HashMap headMap =  new HashMap();
                headMap.put("AC_HD_ID",tblData.getValueAt(i, 2));
                headMap.put("AC_HD_ORDER",tblData.getValueAt(i, 0));
                ClientUtil.execute("udateAcctHeadOrder", headMap);
            }
            btnResetActionPerformed(null);
        }
    }//GEN-LAST:event_btnSaveActionPerformed



    private void internationalize() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
//            HashMap mapParam = new HashMap();
//
//            HashMap where = new HashMap();
//            where.put("beh", "CA");
//
//            mapParam.put("MAPNAME", "getSelectInwardClearingAuthorizeTOList");
        //mapParam.put("WHERE", where);
        // HashMap rMap = (HashMap) dao.getData(mapParam);

        // HashMap testMap = new HashMap();
        //testMap.put("MAPNAME", "getSelectOperativeAcctProductTOList");
        new AccountHeadOrderingUI().show();
    }

    public void update(Observable observed, Object arg) {
        //txtClosingYear.setText(observable.getTxtAccNo());
        //tdtFromDate.setDateValue(DateUtil.getStringDate(observable.getTdtFromDate()));
        //tdtToDate.setDateValue(DateUtil.getStringDate(observable.getTdtToDate()));
    }

    public void callTTIntergration(String repName, HashMap parMap) {
        System.out.println("Here is the param map :: " + parMap);
        TTIntegration ttIntgration = null;
        ttIntgration.setParam(parMap);
        // ttIntgration.integration(repName);
        ttIntgration.integrationForPrint(repName, true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDownArrow;
    private com.see.truetransact.uicomponent.CButton btnReset;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnUpArrow;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadDetails;
    private com.see.truetransact.uicomponent.CPanel panArrow;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAndOr;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPassBookType;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    // End of variables declaration//GEN-END:variables
}
