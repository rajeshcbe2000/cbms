/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ifrRenewal.java
 *
 * Created on February 16, 2012, 5:17 PM
 */

package com.see.truetransact.ui.borrowings;
import com.see.truetransact.uicomponent.CDialog;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.*;
import javax.swing.event.*;
import com.see.truetransact.clientutil.CMandatoryDialog;
import java.util.EventObject;
import java.util.Hashtable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.CellEditorListener;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientConstants;
import java.util.Observable;
import java.text.SimpleDateFormat;
import com.see.truetransact.clientutil.ClientUtil;
import java.util.Observer;
import java.util.*;
/**
 *
 * @author  user
 */
public class ifrRenewal extends CDialog {
    private TableModelListener tableModelListener;
    DefaultTableModel model = null;
    public String strBorrowingNo=null;
    public String strNewExpDate=null;
    public String strCurrExpdate=null;
    private Date currDt = null;
    /** Creates new form ifrRenewal */
    public ifrRenewal() {
        try {
            currDt = ClientUtil.getCurrentDate();
            initComponents();
            initTableData();
            this.setBounds(0,0, 500, 400);
            setVisible(true);
            tblData.getModel().addTableModelListener(tableModelListener);
            setTableModelListener();
        }catch (Exception e){
            e.printStackTrace();
        }
        //          this.setBounds(20,20, 500, 400);
    }
    
    private void setTableModelListener() {
        tableModelListener = new TableModelListener() {
            
            public void tableChanged(TableModelEvent e) {
                System.out.println("Cell 0000000000000000000000000000000000000000000000============" + e.getFirstRow() );
                if (e.getType() == TableModelEvent.UPDATE) {
                    System.out.println("Cell " + e.getFirstRow() + ", "
                    + e.getColumn() + " changed. The new value: "
                    + tblData.getModel().getValueAt(e.getFirstRow(),
                    e.getColumn()));
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    
                    if (column == 0 || column == 1) {
                        
                        TableModel model = tblData.getModel();
                        
                        String bid = model.getValueAt(row, 0).toString();
                        System.out.println("BORROWING ID INNN =========================="+bid);
                        
                        
                    }
                    
                    
                    
                    
                }
            }
        };
        System.out.println("Cell 53654654654654654"+tableModelListener);
        tblData.getModel().addTableModelListener(tableModelListener);
    }
    
//    public void initTableData() {
//        String data[][] ={{}};
//        String col[] = {"Borrowing No","Expiry Date"};
//        DefaultTableModel dataModel = new DefaultTableModel();
//        // dataModel.setDataVector(dataVector
//        model = new DefaultTableModel(data,col);
//        //  tblData.getCellEditor().stopCellEditing();
//        tblData.setModel(model);
//        
//        
//        HashMap singleAuthorizeMap = new HashMap();
//        singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
//        singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
//        
//        singleAuthorizeMap.put(CommonConstants.BRANCH_ID,  ProxyParameters.BRANCH_ID);
//        List aList= ClientUtil.executeQuery("getRenBorowingData", singleAuthorizeMap);
//        model.setRowCount(0);
//        // System.out.println("aList 0000===="+aList);
//        for (int t = 0;t<aList.size();t++) {
//            HashMap mapRecCo=(HashMap)aList.get(t);
//            //    System.out.println("mapRecCo 0000===="+mapRecCo);
//            String borrowingNo=mapRecCo.get("BORROWING_NO").toString();
//            String expDate=mapRecCo.get("SANCTION_EXP_DATE").toString();
//            model.addRow(new String[]{borrowingNo,getDtPrintValue(expDate)});
//        }
//        
//    }
    
      public void initTableData() {
          String data[][] ={{}};
          String col[] = {"Borrowing No","Expiry Date"};
          DefaultTableModel dataModel = new DefaultTableModel();
          // dataModel.setDataVector(dataVector
          model =new DefaultTableModel(data,col) {
              
              
              public boolean isCellEditable(int row, int column) {
                  //all cells false
                  return false;
              }
          };
          //  tblData.getCellEditor().stopCellEditing();
          tblData.setModel(model);
          
          
          HashMap singleAuthorizeMap = new HashMap();
          singleAuthorizeMap.put(CommonConstants.USER_ID, "app");
          singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
          
          singleAuthorizeMap.put(CommonConstants.BRANCH_ID,  "0001");
          List aList= ClientUtil.executeQuery("getRenBorowingData", singleAuthorizeMap);
          model.setRowCount(0);
          // System.out.println("aList 0000===="+aList);
          for (int t = 0;t<aList.size();t++) {
              HashMap mapRecCo=(HashMap)aList.get(t);
              //    System.out.println("mapRecCo 0000===="+mapRecCo);
              String borrowingNo=mapRecCo.get("BORROWING_NO").toString();
              String expDate=mapRecCo.get("SANCTION_EXP_DATE").toString();
              model.addRow(new String[]{borrowingNo,getDtPrintValue(expDate)});
          }
      }
      
      public String getDtPrintValue(String strDate) {
          try {
              //create SimpleDateFormat object with source string date format
              SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
              //"yyyy-MM-dd HH:mm:ss "
              //parse the string into Date object
              Date date = sdfSource.parse(strDate);
              //create SimpleDateFormat object with desired date format
              SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/MM/yyyy");
              //parse the date into another format
              strDate = sdfDestination.format(date);
          }
          catch(Exception e) {
              // e.printStackTrace();
          }
          return strDate;
      }
      public static void main(String args[]) throws Exception {
          try {
              ifrRenewal objIfrRenewal=new ifrRenewal();
              objIfrRenewal.setVisible(true);
          }catch (Exception e){
              e.printStackTrace();
          }
      }
      /** This method is called from within the constructor to
       * initialize the form.
       * WARNING: Do NOT modify this code. The content of this method is
       * always regenerated by the Form Editor.
       */
    private void initComponents() {//GEN-BEGIN:initComponents
        panRenewal = new com.see.truetransact.uicomponent.CPanel();
        btnSave = new javax.swing.JButton();
        cdteRenDate = new com.see.truetransact.uicomponent.CDateField();
        cScrollPaneTable1 = new com.see.truetransact.uicomponent.CScrollPaneTable();
        tblData = new com.see.truetransact.uicomponent.CTable();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();

        panRenewal.setLayout(null);

        panRenewal.setMaximumSize(new java.awt.Dimension(10, 10));
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        panRenewal.add(btnSave);
        btnSave.setBounds(390, 90, 73, 23);

        panRenewal.add(cdteRenDate);
        cdteRenDate.setBounds(380, 50, 101, 21);

        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Borrowing No", "Expiry Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblData.setColumnSelectionAllowed(true);
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDataMouseClicked(evt);
            }
        });

        cScrollPaneTable1.setViewportView(tblData);

        panRenewal.add(cScrollPaneTable1);
        cScrollPaneTable1.setBounds(0, 0, 370, 420);

        cLabel1.setText("Date");
        panRenewal.add(cLabel1);
        cLabel1.setBounds(380, 20, 27, 18);

        getContentPane().add(panRenewal, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents
    
    private void tblDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseClicked
        // TODO add your handling code here:
        int row=tblData.rowAtPoint(evt.getPoint());
        
        int col= tblData.columnAtPoint(evt.getPoint());
        
        String BNo=tblData.getValueAt(row,0).toString();
        System.out.println("B NOINMMMmmmmmmmmmmmmmmmmmmmmm==================="+BNo);
        strBorrowingNo=BNo;
        strCurrExpdate=tblData.getValueAt(row,1).toString();
    }//GEN-LAST:event_tblDataMouseClicked
    
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        strNewExpDate=cdteRenDate.getDateValue();
        if(strBorrowingNo==null || strBorrowingNo.equalsIgnoreCase("")) {
            displayAlert("Select Borrowing No!!!");
            return;
        }
        if(strNewExpDate==null || strNewExpDate.equalsIgnoreCase("")) {
            displayAlert("Select New Expiry Date!!!");
            return;
        }
        //displayAlert("strCurrExpdate=="+strCurrExpdate+" strNewExpDate= "+strNewExpDate);
        if(new Date(strCurrExpdate).after(new Date(strNewExpDate))) {
            displayAlert("Renewal Date is grater than the current expiry date!!");
            return;
        }
        getSaveRenewalDate();
    }//GEN-LAST:event_btnSaveActionPerformed
    
    public void getSaveRenewalDate() {
        try {
            if(strBorrowingNo!=null && !strBorrowingNo.equalsIgnoreCase("")) {
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
                singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
                singleAuthorizeMap.put("BORROWING_NO",strBorrowingNo );
                singleAuthorizeMap.put("SANCTION_EXP_DATE",getDateValue(strNewExpDate) );
                singleAuthorizeMap.put(CommonConstants.BRANCH_ID,  ProxyParameters.BRANCH_ID);
                boolean flag=ClientUtil.executeWithResult("updateExpDate", singleAuthorizeMap);
                this.dispose();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public Date getDateValue(String date1) {
        Date date=null ;
        try {
            
            
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
            date=new Date(sdf2.format(sdf1.parse(date1)));
            
        }
        catch (Exception e) {
            System.out.println("Error in getDateValue():"+e);
        }
        return date;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSave;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CScrollPaneTable cScrollPaneTable1;
    private com.see.truetransact.uicomponent.CDateField cdteRenDate;
    private com.see.truetransact.uicomponent.CPanel panRenewal;
    private com.see.truetransact.uicomponent.CTable tblData;
    // End of variables declaration//GEN-END:variables
    
}
