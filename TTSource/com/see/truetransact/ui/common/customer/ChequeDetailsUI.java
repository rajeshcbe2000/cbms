/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 

 * ChequeDetailsUI.java
 *
 * Created on April 13, 2011, 10:10 PM
 */
package com.see.truetransact.ui.common.customer;

import java.util.Observer;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.Toolkit;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.TableDialogUI;

/**
 *
 * @author Suresh
 */
public class ChequeDetailsUI extends com.see.truetransact.uicomponent.CDialog implements Observer {

//    final CheckCustomerIdRB resourceBundle = new CheckCustomerIdRB();
    private CTable _tblData;
    private HashMap dataHash;
    private ArrayList data;
    private int dataSize;
    private ArrayList _heading;
    private boolean _isAvailable = true;
    Date currDt = null;
    public String branchID;
    public String prodType;
    public String acNum = null;
    ArrayList chequeTabRow = new ArrayList();

    public ChequeDetailsUI() {
        initComponents();
        initForm();
    }

    /**
     * Account Number Constructor
     */
    public ChequeDetailsUI(String actNum) {
        acNum = actNum;
        initComponents();
        setMaxLengths();
        addToTableUsingActNo(actNum);
        branchID = TrueTransactMain.BRANCH_ID;
        setupScreen();
    }

    /**
     * Method which is used to initialize the form TokenConfig
     */
    private void initForm() {
        setMaxLengths();
        setFieldNames();
        internationalize();
        currDt = ClientUtil.getCurrentDate();
    }

    private void setupScreen() {
        setModal(true);
        setTitle("Cheque Book Details " + "[" + branchID + "]");
        /*
         * Calculate the screen size
         */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("@$#@$#@# screenSize : " + screenSize);
        setSize(650, 450);
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

    private void addToTableUsingActNo(String actNum) {
        acNum = actNum;
        HashMap where = new HashMap();
        LinkedHashMap viewMap = new LinkedHashMap();
        where.put("ACCT_NO", actNum);
        viewMap.put(CommonConstants.MAP_NAME, "getTableValues");
        viewMap.put(CommonConstants.MAP_WHERE, where);
        try {
            populateData(viewMap);
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
        viewMap = null;
        where = null;
    }

    public ArrayList populateData(HashMap whereMap) {
        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        }
        dataHash = ClientUtil.executeTableQuery(whereMap);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        System.out.println("### Data : " + data);
        populateChequeTable();
        whereMap = null;
        return _heading;
    }

    public void populateChequeTable() {
        int temp=0;
        boolean dataExist;
        if (_heading != null) {
            final ArrayList chequeTabTitle = new ArrayList();
            chequeTabTitle.add("Issued Date");
            chequeTabTitle.add("Series From");
            chequeTabTitle.add("Series To");
            chequeTabTitle.add("Starting Cheque No");
            chequeTabTitle.add("Ending Cheque No");
            chequeTabTitle.add("Unused Count");

            //[7881, SB, 199, 2020-08-03 00:00:00.0, 7890, 199, SB, 0.00]
            
            TableModel model = new TableModel(new ArrayList(), chequeTabTitle);
            int rowCount = 0;
            for (int i = 0, j = data.size(); i < j; i++) {
                chequeTabRow = new ArrayList();
                StringBuffer buff1 = new StringBuffer();
                StringBuffer buff2 = new StringBuffer();
                final ArrayList resultMap = (ArrayList) data.get(i);

                chequeTabRow.add(CommonUtil.convertObjToStr(resultMap.get(0)));
                chequeTabRow.add(resultMap.get(2));
                chequeTabRow.add(resultMap.get(5));

                buff1.append(CommonUtil.convertObjToStr(resultMap.get(1)));
                buff1.append(" ");
                buff1.append(CommonUtil.convertObjToStr(resultMap.get(0)));

                buff2.append(CommonUtil.convertObjToStr(resultMap.get(6)));
                buff2.append(" ");
                buff2.append(CommonUtil.convertObjToStr(resultMap.get(4)));

                chequeTabRow.add(buff1);
                chequeTabRow.add(buff2);
                int usedCount = 0;
                /*
                [7881, SB, 199, 2020-08-03 00:00:00.0, 7890, 199, SB, 0.00]

	chqissuedt = 3
	chq_bk_series_from = 2
	chq_bk_series_to = 5
	start_chq_no1 = 1
    start_chq_no2 = 0
	end_chq_no1 = 6
	end_chq_no2 = 4 
	
                 */
                HashMap usedMap = new HashMap();
                usedMap.put("NO_1", CommonUtil.convertObjToStr(resultMap.get(1)));
                usedMap.put("NO_2", CommonUtil.convertObjToStr(resultMap.get(6)));
                usedMap.put("FROM_CHEQUE_NO",CommonUtil.convertObjToStr(resultMap.get(0)));
                usedMap.put("TO_CHEQUE_NO", CommonUtil.convertObjToStr(resultMap.get(4)));
                usedMap.put("ACCT_NO", acNum);
                System.out.println("usedMap : " + usedMap);
                List usedList = ClientUtil.executeQuery("getUnusedCheckCountsEachSeries", usedMap);
                if (usedList != null && usedList.size() > 0) {
                    usedMap = (HashMap) usedList.get(0);
                    usedCount = CommonUtil.convertObjToInt(usedMap.get("CHEQUE_COUNT"));
                    chequeTabRow.add(usedCount);                                     
                    if (usedCount >= 0) {
                        temp=temp+usedCount;   
                        if (rowCount == 0) {
                            model.insertRow(rowCount, (ArrayList) chequeTabRow);
                        } else {
                            model.insertRow(rowCount + 1, (ArrayList) chequeTabRow);
                        }
                    }
                }
            }   
            System.out.println("%%%%%%%%%Total USED COUNT %%%%"+temp);
            model.fireTableDataChanged();
            tblChequeDetails.setModel(model);
            tblChequeDetails.revalidate();
        }        
    }

    /**
     * Used to set Maximum possible lenghts for TextFields
     */
    private void setMaxLengths() {
    }

    /*
     * Auto Generated Method - setFieldNames() This method assigns name for all
     * the components. Other functions are working based on this name.
     */
    private void setFieldNames() {
        panTransDetails.setName("panMemberShipFacility");
    }
    /*
     * Auto Generated Method - internationalize() This method used to assign
     * display texts from the Resource Bundle File.
     */

    private void internationalize() {
    }

    public void update(java.util.Observable o, Object arg) {
    }
    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */

    public void updateOBFields() {
    }

    /*
     * Auto Generated Method - setMandatoryHashMap()
     *
     * ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     *
     * This method list out all the Input Fields available in the UI. It needs a
     * class level HashMap variable mandatoryMap.
     */
    public void setMandatoryHashMap() {
    }

    /*
     * Auto Generated Method - getMandatoryHashMap() Getter method for setMandatoryHashMap().
     */
    public HashMap getMandatoryHashMap() {
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panTransDetails = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panChequeDetailTable = new com.see.truetransact.uicomponent.CPanel();
        srpChequeDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblChequeDetails = new com.see.truetransact.uicomponent.CTable();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        panTransDetails.setMaximumSize(new java.awt.Dimension(825, 775));
        panTransDetails.setMinimumSize(new java.awt.Dimension(825, 775));
        panTransDetails.setPreferredSize(new java.awt.Dimension(825,775));
        panTransDetails.setLayout(new java.awt.GridBagLayout());

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 21, 0, 0);
        panTransDetails.add(btnClose, gridBagConstraints);

        panChequeDetailTable.setBorder(javax.swing.BorderFactory.createTitledBorder("Cheque Details"));
        panChequeDetailTable.setMaximumSize(new java.awt.Dimension(575, 400));
        panChequeDetailTable.setMinimumSize(new java.awt.Dimension(575,400));
        panChequeDetailTable.setName("panTransInfo"); // NOI18N
        panChequeDetailTable.setPreferredSize(new java.awt.Dimension(575,400));
        panChequeDetailTable.setLayout(new java.awt.GridBagLayout());

        srpChequeDetails.setMinimumSize(new java.awt.Dimension(380, 340));
        srpChequeDetails.setPreferredSize(new java.awt.Dimension(480, 340));

        tblChequeDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Issue Date", "Series No.", "Serial No.", "Starting Cheque No.", "Ending Cheque No.", "Unused Count"
            }
        ));
        tblChequeDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(415,2000));
        tblChequeDetails.setMinimumSize(new java.awt.Dimension(415,2000));
        tblChequeDetails.setPreferredSize(new java.awt.Dimension(415,2000));
        tblChequeDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblChequeDetailsMousePressed(evt);
            }
        });
        srpChequeDetails.setViewportView(tblChequeDetails);

        panChequeDetailTable.add(srpChequeDetails, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panTransDetails.add(panChequeDetailTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panTransDetails, gridBagConstraints);
        panTransDetails.getAccessibleContext().setAccessibleName("MembershipFacifility");

        pack();
    }// </editor-fold>//GEN-END:initComponents

                                    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
                                                                                                                                        }//GEN-LAST:event_formWindowClosed

                                                            private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
                                                                                                                                                                                                                                                                                            }//GEN-LAST:event_formWindowClosing

    /**
     * Exit the Application
     */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
                                                    }//GEN-LAST:event_exitForm

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void tblChequeDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblChequeDetailsMousePressed
        // TODO add your handling code here:
        HashMap whereMap = new HashMap();
        whereMap.put("ACCT_NO", acNum);
        String fromNo = CommonUtil.convertObjToStr(tblChequeDetails.getValueAt(tblChequeDetails.getSelectedRow(), 3));
        whereMap.put("FROM_CHQ_NO1", fromNo.substring(0,fromNo.indexOf(" ")));
        fromNo = fromNo.substring(fromNo.indexOf(" ")+1,fromNo.length());
        System.out.println("### FromNo :"+fromNo+":");
        String toNo = CommonUtil.convertObjToStr(tblChequeDetails.getValueAt(tblChequeDetails.getSelectedRow(), 4));
        whereMap.put("TO_CHQ_NO1", toNo.substring(0,toNo.indexOf(" ")));
        toNo = toNo.substring(toNo.indexOf(" ")+1,toNo.length());
        System.out.println("### ToNo :"+toNo+":");
        whereMap.put("FROM_CHQ_NO2", fromNo);
        whereMap.put("TO_CHQ_NO2", toNo);
        System.out.println("###$$% whereMap : "+whereMap);
        TableDialogUI tableData = new TableDialogUI("getIssuedChequeDetails", whereMap);
        tableData.setTitle("Cheque book leaves details");
        tableData.show();
    }//GEN-LAST:event_tblChequeDetailsMousePressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        //        new CheckCustomerIdUI().show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CPanel panChequeDetailTable;
    private com.see.truetransact.uicomponent.CPanel panTransDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpChequeDetails;
    private com.see.truetransact.uicomponent.CTable tblChequeDetails;
    // End of variables declaration//GEN-END:variables
}
