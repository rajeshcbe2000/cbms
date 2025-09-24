/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 **
 *
 * Authorize.java
 *
 * Created on March 3, 2004, 1:46 PM
 */

package com.see.truetransact.ui.common.viewall;

import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.util.List;
import javax.swing.table.*; 
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.Color;
import java.awt.Component;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.event.ListSelectionListener;
//import javax.swing.DefaultListModel;

import org.apache.log4j.Logger;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.EnhancedComboBoxModel;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.viewall.ViewAll;

import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.ui.TrueTransactMain;

/**
 * @author  bala
 */
public class EditTableUI extends com.see.truetransact.uicomponent.CDialog  {
//    private final LoanSubsidyRB resourceBundle = new LoanSubsidyRB();
    private TableModelListener tableModelListener;
//    private LoanSubsidyOB observable;
    HashMap paramMap = null;
    HashMap dataMap =null;
    CInternalFrame parent = null;
    javax.swing.JList lstSearch;
    java.util.ArrayList arrLst=new java.util.ArrayList();
    Date currDt = null;
    TTIntegration ttIntegration = null;
    int previousRow = -1;
    HashMap accountNumberMap = null;
    HashMap guarantorMemberMap = null;
    HashMap accountChargeMap = null;
    HashMap guarantorChargeMap = null;
    String bankName = "";
    boolean generateNotice = false;
    final int TO=0, FROM=1;
    int viewType=-1;
    private String subsidyId="";
    boolean isFilled=false;
    boolean transAmtEdit=false;
    private StringBuffer acccountList =new StringBuffer();
    private final static Logger log = Logger.getLogger(EditTableUI.class);
    private String sourceScreen ="";
    private ArrayList recoveryList = new ArrayList();
    
    /** Creates new form AuthorizeUI */
    public EditTableUI() {
        setupInit();
        setupScreen();
    }
    
    /** Creates new form AuthorizeUI */
    public EditTableUI(CInternalFrame parent, HashMap paramMap) {
        this.parent = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
    }
    
     /** Creates new form AuthorizeUI */
    public EditTableUI(String sourceScreen, HashMap dataMap) {
        this.sourceScreen = sourceScreen;
        this.dataMap = dataMap;
        if(dataMap !=null ){
            recoveryList=(ArrayList)dataMap.get("DATA_LIST");
        }
        setupInit();
        setupScreen();
    }
    
    private void setupInit() {
        currDt = ClientUtil.getCurrentDate();
       
        initComponents();
        internationalize();
        setObservable();
      
        setMaxLength();
       
        initSubsidyTableData();
        enableDisableSearchDetails(false);
     

    }

    private void enableDisableSearchDetails(boolean flag){
      
    }
    private void setMaxLength(){
    
        
    }
    private void setupScreen() {
        //        setModal(true);
        
        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        /* Center frame on the screen */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }
    
    private void setObservable() {
        try {
//            observable = new LoanSubsidyOB();
            // observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void fillData(Object obj){
        HashMap dataMap =(HashMap)obj;
      
//        if(viewType==ClientConstants.ACTIONTYPE_DELETE || viewType==ClientConstants.ACTIONTYPE_EDIT || viewType==ClientConstants.ACTIONTYPE_AUTHORIZE){
//            subsidyId="";
//            // observable.setSubsidyId("");
//            subsidyId=CommonUtil.convertObjToStr(dataMap.get("SUBSIDY_ID"));
//             if(viewType==ClientConstants.ACTIONTYPE_AUTHORIZE)
//                isFilled=true;
//            // observable.setSubsidyId(subsidyId);
//            editDeleteTableData(dataMap);
//            setButtonEnableDisable();
//        }
//        if(viewType==FROM){
//            if(dataMap.containsKey("ACCOUNTNO")){
//                txtFromAccountNo.setText(CommonUtil.convertObjToStr(dataMap.get("ACCOUNTNO")));
//            }else{
//                txtFromAccountNo.setText(CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));
//            }
//        }else if(viewType==TO){
////             txtToAccountNoFocusLost(null);
//            if(dataMap.containsKey("ACCOUNTNO")){
//                txtToAccountNo.setText(CommonUtil.convertObjToStr(dataMap.get("ACCOUNTNO")));
//            }else{
//                txtToAccountNo.setText(CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));
//            }
//            
//        }
        
    }
    private void editDeleteTableData(HashMap map){
//        if(// observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE){
            transAmtEdit=false;
//        }
//        
//        else{
            transAmtEdit=true;
//        }
        initSubsidyTableData();
    }
    
//    public void populateData(HashMap mapID) {
//        try {
//            log.info("populateData...");
//            ArrayList heading = // observable.populateData(mapID, tblData);
//            if (heading != null && heading.size() > 0) {
//                EnhancedComboBoxModel cboModel = new EnhancedComboBoxModel(heading);
//                //                cboSearchCol.setModel(cboModel);
//            }
//        } catch( Exception e ) {
//            System.err.println( "Exception " + e.toString() + "Caught" );
//            e.printStackTrace();
//        }
//    }
    
      public void show() {
          
//        if (isShow) {
            Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            pack();
            
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
       // }
  
          
//          super.show();
      }
      
      //    public void setVisible(boolean visible) {
      //        if (// observable.isAvailable()) {
      //            super.setVisible(visible);
      //        }
      //    }
      
      /**
       * Bring up and populate the temporary project detail screen.
       */
      private void whenTableRowSelected() {
          int rowIndexSelected = tblData.getSelectedRow();
          //        if (previousRow!=-1)
          //            if (!((Boolean) tblData.getValueAt(previousRow, 0)).booleanValue()) {
          //                int guarantorRowIndexSelected = tblGuarantorData.getSelectedRow();
          //                if (accountNumberMap==null) {
          //                    accountNumberMap = new HashMap();
          //                }
          //                if (guarantorMemberMap==null) {
          //                    guarantorMemberMap = new HashMap();
          //                }
          //                if (previousRow!=-1 && previousRow!=rowIndexSelected) {
          //        isSelectedRowTicked(tblGuarantorData);
          setColour();
          
          //                }
          //            } else {
          //                // observable.setSelectAll(tblGuarantorData, new Boolean(false));
          //            }
          
      }
      
      //    private void whenGuarantorTableRowSelected() {
      //        int rowIndexSelected = tblData.getSelectedRow();
      //        if (!((Boolean) tblData.getValueAt(rowIndexSelected, 0)).booleanValue()) {
      //            if (isSelectedRowTicked(tblGuarantorData)) {
      //                ClientUtil.displayAlert("Loanee Record not selected...");
      //                // observable.setSelectAll(tblGuarantorData, new Boolean(false));
      //            }
      //        }
      //    }
      
      private boolean isSelectedRowTicked(com.see.truetransact.uicomponent.CTable table) {
          boolean selected = false;
          for (int i=0, j=table.getRowCount(); i < j; i++) {
              selected = ((Boolean) table.getValueAt(i, 0)).booleanValue();
              if (!selected) {
                  //            table.setForeground(Colu
                  break;
              }
          }
          return selected;
      }
      
      
      /** This method is called from within the constructor to
       * initialize the form.
       * WARNING: Do NOT modify this code. The content of this method is
       * always regenerated by the Form Editor.
       */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        panTable = new com.see.truetransact.uicomponent.CPanel();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setResizable(true);
        setTitle("Edit Table UI");
        setMinimumSize(new java.awt.Dimension(600, 230));
        setPreferredSize(new java.awt.Dimension(600, 230));
        panTable.setLayout(new java.awt.GridBagLayout());

        panTable.setMinimumSize(new java.awt.Dimension(400, 250));
        panTable.setPreferredSize(new java.awt.Dimension(400, 250));
        srcTable.setViewport(srcTable.getRowHeader());
        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5"
            }
        ));
        tblData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblData.setEditingColumn(5);
        tblData.setEditingRow(0);
        tblData.setMinimumSize(new java.awt.Dimension(350, 80));
        tblData.setPreferredScrollableViewportSize(new java.awt.Dimension(450000, 400000));
        tblData.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblDataMouseMoved(evt);
            }
        });
        tblData.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblDataFocusLost(evt);
            }
        });
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDataMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblDataMouseReleased(evt);
            }
        });

        srcTable.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTable.add(srcTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panTable, gridBagConstraints);

        pack();
    }//GEN-END:initComponents

    private void tblDataFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblDataFocusLost
        // TODO add your handling code here:
//        setTableModelListenerUpdate();
    }//GEN-LAST:event_tblDataFocusLost
        

    
    private void tblDataMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseReleased
      
    }//GEN-LAST:event_tblDataMouseReleased
   
    private void tblDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseMoved
//	
    }//GEN-LAST:event_tblDataMouseMoved

    private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed
//      
    }//GEN-LAST:event_tblDataMousePressed

     
    public void initSubsidyTableData() {
        tblData.setModel(new javax.swing.table.DefaultTableModel(
        setTableData(),
        new String [] {
            
            "Acct Num","Penal","Waive Off Penal","Interest","Waive Off Interest"
        }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
               
               
            };
            
            boolean[] canEdit = new boolean [] {
                false, false, false, false,true
            };
           
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex==4){
                    return true;
                }
               
                return canEdit [columnIndex];
            }
        });
        
        
        tblData.setCellSelectionEnabled(true);
        tblData.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblDataPropertyChange(evt);
               
            }
        });
        setTableModelListener();
        
        setSizeTallyTableData();
        
    }
    
    public void resetinitSubsidyTableData() {
        Object obj[][]=new Object[0][0];
        tblData.setModel(new javax.swing.table.DefaultTableModel(
        obj,
        new String [] {
            "Select","Acct Num","Name","Subsidy Adjust Achd","Subsidy Amt","Trans Amt","Subsidy Date"
        }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
                
            };
            
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false,transAmtEdit, false
            };
            
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex==5 && transAmtEdit){
                    return true;
                }
                
                return canEdit [columnIndex];
            }
        });
        
        
        tblData.setCellSelectionEnabled(true);
        tblData.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblDataPropertyChange(evt);
            }
        });
        
        tblData.addMouseListener(new MouseAdapter() {
        public void mousePressed(MouseEvent me) {
        int selRow = tblData.getSelectedRow();
        int selCol = tblData.getSelectedColumn();
        Object value = tblData.getValueAt(selRow, selCol);
        }
        });
        setTableModelListener();
//        setTableModelListenerUpdate();
        setSizeTallyTableData();
       
    }
    
    private void tblDataPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblRecoveryListTallyPropertyChange
      
    }//GEN-LAST:event_tblRecoveryListTallyPropertyChange
    
    
     private void setTableModelListenerUpdate() {
        tableModelListener = new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    System.out.println("Cell " + e.getFirstRow() + ", "
                    + e.getColumn() + " changed. The new value: "
                    + tblData.getModel().getValueAt(e.getFirstRow(),
                    e.getColumn()));
                    int row = e.getFirstRow();
                    int column  = e.getColumn();
                    if (column == 5) {
                        TableModel model = tblData.getModel();
//                        tblData.setValueAt(model.getValueAt(tblData.getSelectedRow(),5),tblData.getSelectedRow(),5);
                        System.out.println("tblData getvalueate"+tblData.getValueAt(tblData.getSelectedRow(),5)+" e.getColumn()"+ e.getColumn());
//                        calcEachChittal();
//                        calcTotal();
                    }
                }
            }
        };
        tblData.getModel().addTableModelListener(tableModelListener);
    }
    private Object[][] setTableData() {
        DefaultTableModel  tblModel = (DefaultTableModel) tblData.getModel();
            HashMap whereMap=new HashMap();
           
//            disableRowList = new ArrayList();
//            String emp_Ref_No = CommonUtil.convertObjToStr(tblSalaryRecoveryList.getValueAt(tblSalaryRecoveryList.getSelectedRow(),1));
          
            if(recoveryList !=null && recoveryList.size()>0 ){
//            System.out.println("####### Final DAO  Map Proxy Result map : "+finalMap);
//            System.out.println("####### // observable.getProxyReturnMap() : "+// observable.getProxyReturnMap());
//            recoveryList= (ArrayList)finalMap.get(emp_Ref_No);
//            System.out.println("####### recoveryList : "+recoveryList+"#### Size()"+recoveryList.size());
            Object totalList[][] = new Object[recoveryList.size()][5];
             Object totalListRow[] = new Object[8];
            
            
            whereMap=new HashMap();
            double total_Demand=0.0;
            double total_RecoveredAmt=0.0;
            for(int i=0;i<recoveryList.size();i++){
                whereMap=(HashMap) recoveryList.get(i);
//                System.out.println("####### whereMap : "+i+""+whereMap);
               // totalList[i][0] = new Boolean(true);
                totalList[i][0] = CommonUtil.convertObjToStr(whereMap.get("ACCT_NUM"));
                totalList[i][1] = CommonUtil.convertObjToStr(whereMap.get("PENAL"));
                totalList[i][2] = CommonUtil.convertObjToStr(whereMap.get("WAIVE_OFF_PENAL"));
                totalList[i][3] = CommonUtil.convertObjToStr(whereMap.get("INTEREST"));
                totalList[i][4] = CommonUtil.convertObjToStr(whereMap.get("WAIVE_OFF_INTEREST"));
                
                if(i==0){
                    acccountList.append("'"+CommonUtil.convertObjToStr(whereMap.get("ACCT_NUM"))+"'");
                }else{
                    acccountList.append("," + "'"+CommonUtil.convertObjToStr(whereMap.get("ACCT_NUM"))+"'");
                }
                
               
//                if(// observable.isActivateNewRecord()){
                    
               
                    
//                }
                //                totalList[i][7] = CommonUtil.convertObjToStr(whereMap.get("RECOVERED_AMOUNT"));
                //                total_Demand+=CommonUtil.convertObjToDouble(whereMap.get("TOTAL_DEMAND")).doubleValue();
                //                total_RecoveredAmt+=CommonUtil.convertObjToDouble(whereMap.get("RECOVERED_AMOUNT")).doubleValue();
                //                if(CommonUtil.convertObjToStr(whereMap.get("PROD_TYPE")).equals("TD") || CommonUtil.convertObjToStr(whereMap.get("PROD_TYPE")).equals("MDS")){
                ////                    disableRowList.add(String.valueOf(i));
                //                }
            }
            //            System.out.println("####### disableRowList : "+disableRowList);
            //            txtTotalDemandTally.setText(String.valueOf(total_Demand));
            //            txtTotalRecoveredTallyAmt.setText(String.valueOf(total_RecoveredAmt));
//            if(// observable.isActivateNewRecord()){
//                ((DefaultTableModel)tblData.getModel()).addRow(totalList);
//            }
            return totalList;
            }
            return null;
    }
    
    private void setTableModelListener() {
        tableModelListener = new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    //                    System.out.println("Cell " + e.getFirstRow() + ", "
                    //                    + e.getColumn() + " changed. The new value: "
                    //                    + tblRecoveryListTally.getModel().getValueAt(e.getFirstRow(),
                    //                    e.getColumn()));
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    if (column == 4) {
                        if(CommonUtil.convertObjToDouble(tblData.getModel().getValueAt(e.getFirstRow(),e.getColumn())).doubleValue()>0){
                            double demand_Amount =CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 3).toString()).doubleValue();
                            double recovered_Amount =CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 4).toString()).doubleValue();
                            if(demand_Amount<recovered_Amount && recovered_Amount>0){
                                ClientUtil.showMessageWindow("Transaction Amount should not Cross Interest Amount !!!");
                                tblData.setValueAt(tblData.getValueAt(tblData.getSelectedRow(), 3),  tblData.getSelectedRow(), 4);
                            }else{
                                String scheme_Name = CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(),4));
                            }
                        }
//                        TableModel model = tblData.getModel();
                        System.out.println("tblData getvalueate"+tblData.getValueAt(tblData.getSelectedRow(),4)+" e.getColumn()"+ e.getColumn());
                        //                        calcTallyListTotal();
                    }
                }
            }
        };
        tblData.getModel().addTableModelListener(tableModelListener);
    }
    
    private void setSizeTallyTableData(){
        //        if(tblData.getRowCount()>0){
        //            tblData.getColumnModel().getColumn(0).setPreferredWidth(140);
        //            tblData.getColumnModel().getColumn(1).setPreferredWidth(70);
        //            tblData.getColumnModel().getColumn(2).setPreferredWidth(30);
        //            tblData.getColumnModel().getColumn(3).setPreferredWidth(30);
        //            tblData.getColumnModel().getColumn(4).setPreferredWidth(40);
        //            tblData.getColumnModel().getColumn(5).setPreferredWidth(30);
        //            tblData.getColumnModel().getColumn(6).setPreferredWidth(70);
        //            tblData.getColumnModel().getColumn(7).setPreferredWidth(90);
        //        }
    }
    
   public ArrayList getTableData(){
       ArrayList singleList=new ArrayList();
       ArrayList totList =new ArrayList();
       int count= tblData.getModel().getRowCount();
       int columnCount= tblData.getModel().getColumnCount();
       for(int i=0;i<count;i++){
           singleList=new ArrayList();
           for(int j=0;j<columnCount;j++){
               singleList.add(tblData.getValueAt(i,j));
           }
            totList.add(singleList);
       }
      return totList;
   }
   
   
   private void setColour() {
       /* Set a cellrenderer to this table in order format the date */
       DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
           public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {
               super.getTableCellRendererComponent(table, value, isSelected,hasFocus, row, column);
               System.out.println("row #####"+row);
               boolean selected = ((Boolean) table.getValueAt(row, 0)).booleanValue();
               if (!selected) {
                   setForeground(Color.RED);
               }
               else {
                   setForeground(Color.BLACK);
               }
               // Set oquae
               this.setOpaque(true);
               return this;
           }
       };
       tblData.setDefaultRenderer(Object.class, renderer);
   }
   
   
   
   
   
   
   
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
   
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    // End of variables declaration//GEN-END:variables
}

