/*
 * Pass Book.java
 * Swaroop 
 * Created on December 07, 2009, 1:46 PM
 */
package com.see.truetransact.ui.common.viewall;

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
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.LinkedHashMap;

import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.see.truetransact.uicomponent.CButton;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uivalidation.NumericValidation;
import java.util.Date;

/**
 * @author Swaroop
 */
public class PassBookNewUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer {

    private PassBookNewOB observable;
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
    private final static Logger log = Logger.getLogger(PassBookNewUI.class);
    private static SqlMap sqlMap = null;
    private TransDetailsUI transDetails = null;

    /**
     * Creates new form PassBook
     */
    public PassBookNewUI() {

        setupInit();
        setupScreen();
    }

    public PassBookNewUI(String print) {
        setObservable();
        initComponents();
        txtAccNo.setAllowAll(true);
        txtLineNo.setValidation(new NumericValidation());

    }

    private void setupInit() {
        initComponents();
        internationalize();
        setObservable();
        toFront();
        setCombos();
        //transDetails = new TransDetailsUI(panMultiSearch);
        //transDetails.setRefreshActDetails(true);
        panMultiSearch.setVisible(true);
        btnClear.setVisible(true);
        observable.resetForm();
        update(observable, null);
        txtAccNo.setAllowAll(true);
        txtLineNo.setValidation(new NumericValidation());
        btnFrontPgPrint.setEnabled(false);
        btnprintTrans.setEnabled(false);
        btnBarCodePrint.setVisible(false);
        btnBarCodePrint.setEnabled(false);
        btnInitialise.setEnabled(false);
        tdtFromDate.setEnabled(true);
        btnAccNo.setEnabled(false);
       // txtAccNo.setEnabled(false);
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
            observable = PassBookNewOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateOBFields() {
        observable.setProdId(CommonUtil.convertObjToStr(((ComboBoxModel) cboAccountType.getModel()).getKeyForSelected()));
        observable.setProdType(CommonUtil.convertObjToStr(((ComboBoxModel) cboModuleType.getModel()).getKeyForSelected()));
        observable.setTxtAccNo(txtAccNo.getText());
        observable.setTdtFromDate(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
        //observable.setTdtToDate(DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()));
    }

    private void setCombos() {
        cboModuleType.setModel(observable.getCbmProdType());
    }

    public void populateData() {
       // updateOBFields();
        System.out.println("0001 " + observable.getTxtAccNo());
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        HashMap passbk = new HashMap();
//        String prod1 = CommonUtil.convertObjToStr(observable.checkProdType());
//        if (prod1.length() > 0) {
//            prod1 = prod1;
//        } else {
//            prod1 = CommonUtil.convertObjToStr(observable.getCbmProdType());
//        }
        //String prod=observable.getProdType();
        // System.out.println("resultList 222dkdkffk ");
        //System.out.println("observable.isBtnInitialise() "+observable.isBtnInitialise());
        if (observable.isBtnInitialise() == true) {
            passbk.put("ACCT_NUM", observable.getTxtAccNo());
            String prod1 = "";
            prod1 = CommonUtil.convertObjToStr(observable.getCbmProdType().getKeyForSelected());
           // passbk.put("MODULE_TYPE", prod1);
             passbk.put("MODULE_TYPE", prod1);
            passbk.put("INITSLNO", CommonUtil.convertObjToInt(txtLineNo.getText()));

            passbk.put("INITDT", DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
            passbk.put("INIT_TAG", "1");

            java.util.List resultList;
            resultList = ClientUtil.executeQuery("getPassBookProcess", passbk);
            //System.out.println("resultList 222 "+resultList);
        }
        // System.out.println("resultList 222333 ");
//            if (resultList != null && resultList.size() > 0) {
//                HashMap resultPass = (HashMap) resultList.get(0);                              
//                String st=CommonUtil.convertObjToStr(resultPass.get("PASS"));
        viewMap.put(CommonConstants.MAP_NAME, "getAllTransactionsGNPassBook" );
        whereMap.put("FROM_SLNO", CommonUtil.convertObjToInt(txtLineNo.getText()));
        whereMap.put("ACT_NUM", observable.getTxtAccNo());
        viewMap.put("ACT_NUM", observable.getTxtAccNo());
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {
            log.info("populateData...");
            ArrayList heading = observable.populateData(viewMap, tblData);
            setTableColumnWidth();
            heading = null;
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
        viewMap = null;
        whereMap = null;
    }

    private void setTableColumnWidth(){      
        javax.swing.table.TableColumn col = tblData.getColumn(tblData.getColumnName(0));           
        col.setPreferredWidth(120);
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
        java.awt.GridBagConstraints gridBagConstraints;

        rdgAndOr = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoPassBookType = new com.see.truetransact.uicomponent.CButtonGroup();
        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        panMultiSearch = new com.see.truetransact.uicomponent.CPanel();
        lblLineNo = new com.see.truetransact.uicomponent.CLabel();
        txtLineNo = new com.see.truetransact.uicomponent.CTextField();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        btnInitialise = new com.see.truetransact.uicomponent.CButton();
        panSelection = new com.see.truetransact.uicomponent.CPanel();
        lblModuleType = new com.see.truetransact.uicomponent.CLabel();
        cboModuleType = new com.see.truetransact.uicomponent.CComboBox();
        lblAccountType = new com.see.truetransact.uicomponent.CLabel();
        cboAccountType = new com.see.truetransact.uicomponent.CComboBox();
        lblAccNo = new com.see.truetransact.uicomponent.CLabel();
        panAcctNo = new com.see.truetransact.uicomponent.CPanel();
        txtAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccNo = new com.see.truetransact.uicomponent.CButton();
        lblAccNameValue = new com.see.truetransact.uicomponent.CLabel();
        lblAddressValue = new com.see.truetransact.uicomponent.CLabel();
        lblAddress = new com.see.truetransact.uicomponent.CLabel();
        lblAccName = new com.see.truetransact.uicomponent.CLabel();
        panButtons = new com.see.truetransact.uicomponent.CPanel();
        btnFrontPgPrint = new com.see.truetransact.uicomponent.CButton();
        btnBarCodePrint = new com.see.truetransact.uicomponent.CButton();
        btnExit = new com.see.truetransact.uicomponent.CButton();
        btnprintTrans = new com.see.truetransact.uicomponent.CButton();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnClear = new com.see.truetransact.uicomponent.CButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(750, 630));
        setPreferredSize(new java.awt.Dimension(750, 630));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panSearchCondition.setMinimumSize(new java.awt.Dimension(700, 250));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(700, 250));
        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        panMultiSearch.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panMultiSearch.setMinimumSize(new java.awt.Dimension(500, 100));
        panMultiSearch.setPreferredSize(new java.awt.Dimension(600, 50));
        panMultiSearch.setLayout(new java.awt.GridBagLayout());

        lblLineNo.setText("Print start Sl No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        panMultiSearch.add(lblLineNo, gridBagConstraints);

        txtLineNo.setAllowAll(true);
        txtLineNo.setAllowNumber(true);
        txtLineNo.setMinimumSize(new java.awt.Dimension(50, 21));
        txtLineNo.setPreferredSize(new java.awt.Dimension(50, 21));
        txtLineNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLineNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMultiSearch.add(txtLineNo, gridBagConstraints);

        lblFromDate.setText("Print Initialise From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 4, 6);
        panMultiSearch.add(lblFromDate, gridBagConstraints);

        tdtFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 4, 6);
        panMultiSearch.add(tdtFromDate, gridBagConstraints);

        btnInitialise.setText("Re-Initialize");
        btnInitialise.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInitialiseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 4, 4);
        panMultiSearch.add(btnInitialise, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(panMultiSearch, gridBagConstraints);

        panSelection.setMinimumSize(new java.awt.Dimension(400, 180));
        panSelection.setPreferredSize(new java.awt.Dimension(400, 180));
        panSelection.setLayout(new java.awt.GridBagLayout());

        lblModuleType.setText("Module Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSelection.add(lblModuleType, gridBagConstraints);

        cboModuleType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboModuleType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboModuleType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboModuleTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSelection.add(cboModuleType, gridBagConstraints);

        lblAccountType.setText("Account Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSelection.add(lblAccountType, gridBagConstraints);

        cboAccountType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAccountType.setPopupWidth(150);
        cboAccountType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAccountTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSelection.add(cboAccountType, gridBagConstraints);

        lblAccNo.setText("Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSelection.add(lblAccNo, gridBagConstraints);

        panAcctNo.setMinimumSize(new java.awt.Dimension(121, 21));
        panAcctNo.setPreferredSize(new java.awt.Dimension(21, 200));
        panAcctNo.setLayout(new java.awt.GridBagLayout());

        txtAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccNoActionPerformed(evt);
            }
        });
        txtAccNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccNoFocusLost(evt);
            }
        });
        panAcctNo.add(txtAccNo, new java.awt.GridBagConstraints());

        btnAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccNo.setToolTipText("Account No.");
        btnAccNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccNoActionPerformed(evt);
            }
        });
        panAcctNo.add(btnAccNo, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSelection.add(panAcctNo, gridBagConstraints);

        lblAccNameValue.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblAccNameValue.setForeground(new java.awt.Color(0, 51, 204));
        lblAccNameValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAccNameValue.setMaximumSize(new java.awt.Dimension(250, 21));
        lblAccNameValue.setMinimumSize(new java.awt.Dimension(250, 21));
        lblAccNameValue.setPreferredSize(new java.awt.Dimension(250, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSelection.add(lblAccNameValue, gridBagConstraints);

        lblAddressValue.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblAddressValue.setForeground(new java.awt.Color(0, 51, 204));
        lblAddressValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAddressValue.setMaximumSize(new java.awt.Dimension(300, 21));
        lblAddressValue.setMinimumSize(new java.awt.Dimension(300, 21));
        lblAddressValue.setPreferredSize(new java.awt.Dimension(300, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSelection.add(lblAddressValue, gridBagConstraints);

        lblAddress.setText("Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSelection.add(lblAddress, gridBagConstraints);

        lblAccName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSelection.add(lblAccName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panSearchCondition.add(panSelection, gridBagConstraints);

        panButtons.setMinimumSize(new java.awt.Dimension(200, 180));
        panButtons.setPreferredSize(new java.awt.Dimension(200, 180));
        panButtons.setLayout(new java.awt.GridBagLayout());

        btnFrontPgPrint.setText("Front Page Print");
        btnFrontPgPrint.setMaximumSize(new java.awt.Dimension(137, 27));
        btnFrontPgPrint.setMinimumSize(new java.awt.Dimension(137, 27));
        btnFrontPgPrint.setPreferredSize(new java.awt.Dimension(137, 27));
        btnFrontPgPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFrontPgPrintActionPerformed(evt);
            }
        });
        panButtons.add(btnFrontPgPrint, new java.awt.GridBagConstraints());

        btnBarCodePrint.setText("Barcode Print");
        btnBarCodePrint.setMaximumSize(new java.awt.Dimension(137, 27));
        btnBarCodePrint.setMinimumSize(new java.awt.Dimension(137, 27));
        btnBarCodePrint.setPreferredSize(new java.awt.Dimension(137, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 4, 4);
        panButtons.add(btnBarCodePrint, gridBagConstraints);

        btnExit.setText("Exit");
        btnExit.setMaximumSize(new java.awt.Dimension(137, 27));
        btnExit.setMinimumSize(new java.awt.Dimension(137, 27));
        btnExit.setPreferredSize(new java.awt.Dimension(137, 27));
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnExit, gridBagConstraints);

        btnprintTrans.setText("Print Transactions");
        btnprintTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnprintTransActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 4, 4);
        panButtons.add(btnprintTrans, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(panButtons, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(panSearchCondition, gridBagConstraints);

        sptLine.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(sptLine, gridBagConstraints);

        panTable.setLayout(new java.awt.GridBagLayout());

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
        tblData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblData.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 13)); // NOI18N
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
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
        tblData.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblDataKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblDataKeyReleased(evt);
            }
        });
        srcTable.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTable.add(srcTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panTable, gridBagConstraints);

        panSearch.setMinimumSize(new java.awt.Dimension(750, 40));
        panSearch.setPreferredSize(new java.awt.Dimension(750, 40));
        panSearch.setLayout(new java.awt.GridBagLayout());

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnClear, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panSearch, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblDataMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseReleased
        // TODO add your handling code here:
//        Object ob= new Boolean(true);
//        tblData.setValueAt(ob, tblData.getSelectedRow(), 0);
    }//GEN-LAST:event_tblDataMouseReleased

    private void tblDataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblDataKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDataKeyPressed

    private void tblDataKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblDataKeyReleased
    }//GEN-LAST:event_tblDataKeyReleased

    private void tblDataFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblDataFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDataFocusLost

    private void btnInitialiseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInitialiseActionPerformed
        // TODO add your handling code here:
        ClientUtil.showMessageWindow("Print starts from fresh page !!!");
        observable.refreshTable();
        observable.setBtnInitialise(true);
        populateData();
        btnprintTransActionPerformed(null);
        observable.setBtnInitialise(false);
    }//GEN-LAST:event_btnInitialiseActionPerformed
    boolean btnIntDetPressed = false;

    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        System.out.println("#$#$ Hash : " + hash);
        
        String cbtype = CommonUtil.convertObjToStr(cboModuleType.getSelectedItem());
        if(cbtype.equals(CommonUtil.convertObjToStr("MDS"))){
        	txtAccNo.setText(CommonUtil.convertObjToStr(hash.get("CHITTAL_NO")));
        }else{
            txtAccNo.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
        }
        cboAccountType.setModel(observable.getCbmProdId());
        cboModuleType.setModel(observable.getCbmProdType());
        lblAddressValue.setText(CommonUtil.convertObjToStr(hash.get("HOUSENAME")));
        lblAccNameValue.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")));
        observable.refreshTable();
        txtAccNoFocusLost(null);
        txtLineNo.setEnabled(false);
        //transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, txtAccNo.getText());
        //populateData();
    }

    private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoActionPerformed
        // TODO add your handling code here:
        String module = CommonUtil.convertObjToStr(cboAccountType.getSelectedItem());
        String type = CommonUtil.convertObjToStr(cboModuleType.getSelectedItem());
        if (module.equals("") && type.equals("")) {
            ClientUtil.showAlertWindow("Please select Module Type and Account Type...");
            return;
        }
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
//        if (CommonUtil.convertObjToStr(observable.getProdType()).equals("D")) {
//            whereMap.put(CommonConstants.PRODUCT_TYPE, "OA");
//        } if (CommonUtil.convertObjToStr(observable.getProdType()).equals("T")) {
//            whereMap.put(CommonConstants.PRODUCT_TYPE, "TD");
//        } if (CommonUtil.convertObjToStr(observable.getProdType()).equals("L")) {
//            whereMap.put(CommonConstants.PRODUCT_TYPE, "TL");
//        } if (CommonUtil.convertObjToStr(observable.getProdType()).equals("A")) {
//            whereMap.put(CommonConstants.PRODUCT_TYPE, "AD");
//        } if (CommonUtil.convertObjToStr(observable.getProdType()).equals("M")) {
//            whereMap.put(CommonConstants.PRODUCT_TYPE, "MDS");
//        }
        //String prod = CommonUtil.convertObjToStr(observable.checkProdType());
//        whereMap.put(CommonConstants.PRODUCT_TYPE, prod);
        observable.setBtnInitialise(false);
        String prod = CommonUtil.convertObjToStr(((ComboBoxModel) cboModuleType.getModel()).getKeyForSelected());
        whereMap.put(CommonConstants.PRODUCT_ID, ((ComboBoxModel) cboModuleType.getModel()).getKeyForSelected());
        
        //whereMap.put("PROD_ID", observable.getProdId());
        whereMap.put("PROD_ID", ((ComboBoxModel) cboAccountType.getModel()).getKeyForSelected());
         System.out.println("prod@@@@@@@ " + prod);
        System.out.println("whereMap@@@@@@@ " + whereMap);
        whereMap.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
        String mapName ="Genpassbook"+prod;
        System.out.println("mapName " + mapName);
//        if(prod.equals(CommonUtil.convertObjToStr("MDS"))){
//            mapName = "Cash.getAccountListFor" + prod;
//        }else{
//           mapName = "Cash.getAccountList" + prod;
//        }
        viewMap.put(CommonConstants.MAP_NAME, mapName);
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        new ViewAll(this, viewMap).show();
    }//GEN-LAST:event_btnAccNoActionPerformed

    private void txtAccNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccNoFocusLost
        // TODO add your handling code here:
        System.out.println("0002" + observable.getTxtAccNo());
        act=1;
//         String module=CommonUtil.convertObjToStr(((ComboBoxModel) cboAccountType.getModel()).getKeyForSelected());
//        String module = CommonUtil.convertObjToStr(cboAccountType.getSelectedItem());
//        String type = CommonUtil.convertObjToStr(cboModuleType.getSelectedItem());
//        if (module.equals("") && type.equals("")) {
//            ClientUtil.showAlertWindow("Please select Module Type and Account Type...");
//            return;
//        }
        if (txtAccNo.getText().length() > 0) {
            observable.refreshTable();
            String actNum = txtAccNo.getText();
            observable.setTxtAccNo(txtAccNo.getText());

            java.util.List resultList;

            String prodType = "";
            HashMap passMap1 = new HashMap();
            passMap1.put("ACCT_NUM", observable.getTxtAccNo());

            resultList = ClientUtil.executeQuery("getProdIdTypeForSelectedAct", passMap1);
            if (resultList != null && resultList.size() > 0) {
                HashMap resultMap = (HashMap) resultList.get(0);
//                cboModuleType.setSelectedItem(CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE")));
//                cboAccountType.setSelectedItem(CommonUtil.convertObjToStr(resultMap.get("PROD_ID")));
//                if (resultMap.get("ACCT_STATUS").equals("CLOSED")) {
//                    ClientUtil.displayAlert("Invalid Account No..");
//                    return;
//                } else {
                    prodType = CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE"));
                //}
            }

            //transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, observable.getTxtAccNo());
            observable.setAccountName(prodType);
            if (observable.getLblAccNameValue().length() <= 0) {
                System.out.println("0003");
                ClientUtil.displayAlert("Invalid Account No...");
                lblAccNameValue.setText("");
                txtAccNo.setText("");
                lblAddressValue.setText("");
            } else {
                lblAccNameValue.setText(observable.getLblAccNameValue());
                lblAddressValue.setText(observable.getLblAddressValue());
                cboModuleType.setSelectedItem(CommonUtil.convertObjToStr(observable.getProdType()));
                //cboAccountType.setSelectedItem(CommonUtil.convertObjToStr(observable.getProdId() ));
                // if(act==1){
             cboAccountType.getModel().setSelectedItem(observable.getProdId());
        //cboAccountType.setSelectedItem(CommonUtil.convertObjToStr(observable.getProdId() ));
       // }
                int lineNo = 0;
                Date tdt = null;
                HashMap passMap = new HashMap();
                passMap.put("ACT_NUM", observable.getTxtAccNo());

                HashMap dateMap = new HashMap();
                dateMap.put("ACT_NUM", observable.getTxtAccNo());
                dateMap.put("MODULE_TYPE", cboModuleType.getSelectedItem());
                resultList = ClientUtil.executeQuery("getInitialiseFromDate", dateMap);
                if (resultList != null && resultList.size() > 0) {
                    HashMap resultMap2 = (HashMap) resultList.get(0);
                    tdt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(resultMap2.get("INITIALIZE_DATE")));
                    if (tdt != null) {
                        tdtFromDate.setDateValue(DateUtil.getStringDate(tdt));
                    } else {
                        try {
                            HashMap newMap = new HashMap();
                            //starting of sb calc
                            //  List lst = (List) sqlMap.executeQueryForList("getLastFinYear", newMap);
                            List lst = (List) ClientUtil.executeQuery("getLastFinYear", newMap);
                            newMap = (HashMap) lst.get(0);
                            tdt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(newMap.get("LAST_FINANCIAL_YEAR_END")));
                            tdtFromDate.setDateValue(DateUtil.getStringDate(tdt));
                        } catch (Exception e) {
                        }
                    }
                }
                resultList = ClientUtil.executeQuery("getNextLineNoForPassBook" + prodType, passMap);
                if (resultList != null && resultList.size() > 0) {
                    HashMap resultMap = (HashMap) resultList.get(0);
                    lineNo = CommonUtil.convertObjToInt(resultMap.get("LINE_NO"));
                    txtLineNo.setText(String.valueOf(lineNo));
                    if (observable.getProdType().equals("TD")) {
                        txtLineNoFocusLost(null);
                    }
                    resultMap.clear();
                    resultMap = null;
                }


                HashMap passbk = new HashMap();
                passbk.put("ACCT_NUM", observable.getTxtAccNo());
                passbk.put("MODULE_TYPE", prodType);
                //passbk.put("INITSLNO", lineNo);
                //passbk.put("INITDT", tdt);
                passbk.put("INITSLNO", null);
                passbk.put("INITDT", null);
                passbk.put("INIT_TAG", "0");
                resultList = ClientUtil.executeQuery("getPassBookProcess", passbk);
                System.out.println("resultList 44 " + resultList);
                if (resultList != null && resultList.size() > 0) {
                    HashMap resultPass = (HashMap) resultList.get(0);
                    String st = CommonUtil.convertObjToStr(resultPass.get("PASS"));
                    if (CommonUtil.convertObjToStr(st.charAt(0)).equals(CommonUtil.convertObjToStr("0"))) {
                        //ClientUtil.showAlertWindow("Balance Mismatch...");
                        ClientUtil.showAlertWindow(st.substring(1));
                        txtAccNo.setText("");
                        lblAccNameValue.setText("");
                        lblAddressValue.setText("");
                        tdtFromDate.setDateValue(null);
                        txtLineNo.setText("");
                        btnFrontPgPrint.setEnabled(false);
                        btnprintTrans.setEnabled(false);
                       // btnBarCodePrint.setEnabled(false);
                        btnInitialise.setEnabled(false);
                        observable.clearTable();
                        // btnClearActionPerformed(null);
                        return;
                    }else if (CommonUtil.convertObjToStr(st.charAt(0)).equals(CommonUtil.convertObjToStr("2"))) {                       
                        /* //KD-4112
                        1  continues  enable  both reintialise ,  print transaction
                        2  continues  enable  reintialise and disable print transaction 
                        */
                        ClientUtil.showMessageWindow(st.substring(1));
                        btnFrontPgPrint.setEnabled(true);
                        btnInitialise.setEnabled(true);
                        btnprintTrans.setEnabled(false);
                        populateData();                        
                    } else {
                        btnFrontPgPrint.setEnabled(true);
                        btnprintTrans.setEnabled(true);
                       // btnBarCodePrint.setEnabled(true);
                        btnInitialise.setEnabled(true);
                        populateData();
                    }

                }
                resultList = null;
                passMap = null;


            }
        }
//        if (observable.getProdType().length()>0) {
//            observable.refreshTable();
//            String actNum = txtAccNo.getText();
//            observable.setTxtAccNo(txtAccNo.getText());
//            transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, observable.getTxtAccNo());
//            observable.setAccountName();
//            lblAccNameValue.setText(observable.getLblAccNameValue());
//            if (observable.getLblAccNameValue().length()<=0) {
//                ClientUtil.displayAlert("Invalid Account No.");
//                lblAccNameValue.setText("");
//                txtAccNo.setText("");
//            }
//            populateData();
//        } else {
//            ClientUtil.displayAlert("Enter Product Type");
//            btnClearActionPerformed(null);
//            txtAccNo.setText("");
//        }

    }//GEN-LAST:event_txtAccNoFocusLost

    private void txtAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccNoActionPerformed
        // TODO add your handling code here:
//        observable.refreshTable();
//        String actNum = txtAccNo.getText();
//        observable.setTxtAccNo(txtAccNo.getText());
//        transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, observable.getTxtAccNo());
//        observable.setAccountName();
//        if (observable.getLblAccNameValue().length()<=0) {
//            ClientUtil.displayAlert("Invalid Account No.");
//            lblAccNameValue.setText("");
//            txtAccNo.setText("");
//        }else{
//            lblAccNameValue.setText(observable.getLblAccNameValue());
//            populateData();
//        }
    }//GEN-LAST:event_txtAccNoActionPerformed

    private void tdtFromDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDateFocusLost
        // TODO add your handling code here:
        observable.setTdtFromDate(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
//        tdtToDate.setDateValue("");
    }//GEN-LAST:event_tdtFromDateFocusLost

    private void cboModuleTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboModuleTypeActionPerformed
        // TODO add your handling code here:
        if(act!=1){
        System.out.println("0004");
        System.out.println("#$#$ prodType : " + ((ComboBoxModel) cboModuleType.getModel()).getKeyForSelected());
        observable.setProdType(CommonUtil.convertObjToStr(((ComboBoxModel) cboModuleType.getModel()).getKeyForSelected()));
        behavesLike = "";
        observable.setCbmProdId(CommonUtil.convertObjToStr(((ComboBoxModel) cboModuleType.getModel()).getKeyForSelected()));
        cboAccountType.setModel(observable.getCbmProdId());
        String prodId = (CommonUtil.convertObjToStr(((ComboBoxModel) cboAccountType.getModel()).getKeyForSelected()));
        if (prodId.length() > 0) {
            btnAccNo.setEnabled(true);
            observable.setProdId(prodId);
            txtAccNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID) + prodId);
        }
        lblAccNameValue.setText("");
        lblAddressValue.setText("");
        txtAccNo.setText("");
        txtAccNo.setEnabled(true);
        tdtFromDate.setDateValue(null);
        txtLineNo.setText("");
        }
    }//GEN-LAST:event_cboModuleTypeActionPerformed

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
//        int rowcnt = tblData.getRowCount();
//        int row = tblData.getSelectedRow();
//        Object ob = new Boolean(false);
//        for (int i = 0; i < rowcnt; i++) {
//            tblData.setValueAt(ob, i, 0);
//        }
//        ob = new Boolean(true);
//        for (int i = row; i < rowcnt; i++) {
//            tblData.setValueAt(ob, i, 0);
//        }
//        tblData.setValueAt(ob, tblData.getSelectedRow(), 0);
//
    }//GEN-LAST:event_tblDataMousePressed

    private void tblDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseMoved
        // Add your handling code here:
    }//GEN-LAST:event_tblDataMouseMoved

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        // Add your handling code here:
        cifClosingAlert();
        btnClearActionPerformed(null);

//        this.dispose();
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        observable.resetForm();
        update(observable, null);
        behavesLike = "";
        //transDetails.setTransDetails(null,null,null);
        lblAccNameValue.setText("");
        lblAddressValue.setText("");
        btnFrontPgPrint.setEnabled(false);
        btnprintTrans.setEnabled(false);
        tdtFromDate.setDateValue(null);
        //btnBarCodePrint.setEnabled(false);
        btnInitialise.setEnabled(false);
        txtLineNo.setText("");
        cboAccountType.setSelectedItem(null);
        btnAccNo.setEnabled(false);
        setModified(false);
        act=0;
    }//GEN-LAST:event_btnClearActionPerformed

private void cboAccountTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAccountTypeActionPerformed
// TODO add your handling code here:
    if(act!=1){
    System.out.println("#$#$ ProdId :00005 " + ((ComboBoxModel) cboAccountType.getModel()).getKeyForSelected());
    observable.setProdId(CommonUtil.convertObjToStr(((ComboBoxModel) cboAccountType.getModel()).getKeyForSelected()));
    //String prodType = (CommonUtil.convertObjToStr(((ComboBoxModel) cboModuleType.getModel()).getKeyForSelected()));
    String prodId = (CommonUtil.convertObjToStr(((ComboBoxModel) cboAccountType.getModel()).getKeyForSelected()));
    if (prodId.length() > 0) {
        txtAccNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID) + prodId);
    }
    }
}//GEN-LAST:event_cboAccountTypeActionPerformed

private void txtLineNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLineNoFocusLost
// TODO add your handling code here:
    if (txtLineNo.getText() != null && txtLineNo.getText().length() > 0) {
        populateData();
        int count = CommonUtil.convertObjToInt(txtLineNo.getText());
//        if(tblData.getRowCount()>0){
//            for(int i=0;i<count;i++){
//                tblData.setValueAt(new Boolean(true), i, 0);
//            }
//        }
    }
}//GEN-LAST:event_txtLineNoFocusLost

    private void btnFrontPgPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFrontPgPrintActionPerformed
         HashMap report = new HashMap();
        // List result=null;
         java.util.List resultList1;
        report.put("PROD_DESC", cboAccountType.getSelectedItem());
        //ClientUtil.execute("getReportName", report);
        resultList1 = ClientUtil.executeQuery("getReportName", report);
        
        if (resultList1 != null && resultList1.size() > 0) {
                HashMap resultPass = (HashMap) resultList1.get(0);                              
                String repName=CommonUtil.convertObjToStr(resultPass.get("FIRSTPAGE"));
            
       // String repName = CommonUtil.convertObjToStr(report.get("FIRSTPAGE"));
       // System.out.println("repName front "+repName);
        passMap.put("ACT_NUM", observable.getTxtAccNo());
        callTTIntergration(repName, passMap);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_btnFrontPgPrintActionPerformed

    private void btnprintTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnprintTransActionPerformed
         HashMap reportp = new HashMap();
         java.util.List resultList1;
        reportp.put("PROD_DESC", cboAccountType.getSelectedItem());
        //ClientUtil.execute("getReportName", report);
        resultList1 = ClientUtil.executeQuery("getReportName", reportp);
        
        if (resultList1 != null && resultList1.size() > 0) {
                HashMap resultPass = (HashMap) resultList1.get(0);                              
                String repName=CommonUtil.convertObjToStr(resultPass.get("PRINTTRANS"));
            
       // String repName = CommonUtil.convertObjToStr(reportp.get("PRINTTRANS"));
       // String repName = "GN_OA_PASSBK";
        //System.out.println("repName print "+repName);
        passMap.put("ACT_NUM", observable.getTxtAccNo());
        callTTIntergration(repName, passMap);
        HashMap postMap = new HashMap();
        // List result=null;
        postMap.put("ACT_NUM", observable.getTxtAccNo());
        ClientUtil.execute("updateGnPassbook", postMap);
        // TODO add your handling code here:
        }
    }//GEN-LAST:event_btnprintTransActionPerformed

    private ComboBoxModel getListModel() {
        ComboBoxModel listData = new ComboBoxModel();
        return listData;
    }

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
        new PassBookNewUI().show();
    }

    public void update(Observable observed, Object arg) {
        ((ComboBoxModel) cboModuleType.getModel()).setKeyForSelected(observable.getProdType());
        
        //System.out.println("0006" + observable.getTxtAccNo());
        txtAccNo.setText(observable.getTxtAccNo());
        tdtFromDate.setDateValue(DateUtil.getStringDate(observable.getTdtFromDate()));
        //tdtToDate.setDateValue(DateUtil.getStringDate(observable.getTdtToDate()));
    }

    public void callTTIntergration(String repName, HashMap parMap) {
        System.out.println("Here is the param map :: " + parMap);
        TTIntegration ttIntgration = null;
        ttIntgration.setParam(parMap);
        // ttIntgration.integration(repName);
        ttIntgration.integrationForPrint(repName, true);
    }

    public void alertMsg() {
        int lastLineNo = 0;
        if (observable.getPrintMap() != null) {
            System.out.println("getPrintMap :" + observable.getPrintMap());
            passMap = observable.getPrintMap();
        }
        System.out.println("passMap" + passMap);
        if (passMap.containsKey("LAST_SLNO")) {
            lastLineNo = CommonUtil.convertObjToInt(passMap.get("LAST_SLNO"));
        }
        final HashMap nextLineNo = new HashMap();
        nextLineNo.put("NEXT_LINE_NO", new Integer(lastLineNo + 1));
        StringBuffer sb = new StringBuffer().append("Last printed line no is ").append(String.valueOf(lastLineNo)
                + ". So, next line no will be " + String.valueOf(lastLineNo + 1)
                + "\nPress Yes to continue updating this next line no."
                + "\nPress No to enter new next line no");
        int a = ClientUtil.confirmationAlert(sb.toString());
        if (a == 0) {
            // do nothing
        } else {
            final CDialog passBookFrame = new CDialog(this, true);
            passBookFrame.setTitle("Passbook print Confirmation");
            passBookFrame.setSize(400, 200);
            passBookFrame.setLayout(null);
            passBookFrame.setDefaultCloseOperation(CDialog.DISPOSE_ON_CLOSE);

            StringBuffer sbMsg = new StringBuffer().append("<html><font color = red>").
                    append("!!!Kindly clear the printer memory and switch off the printer.\n").
                    append("Switch on the printer after 10 secs.</font></html>");
            CLabel lblMessage = new CLabel();
            lblMessage.setText(sbMsg.toString());
            lblMessage.setBounds(30, 15, 320, 40);
            passBookFrame.getContentPane().add(lblMessage);

            CLabel lblNextLineNo = new CLabel();
            lblNextLineNo.setText("Enter next line no");
            lblNextLineNo.setBounds(40, 70, 150, 20);
            passBookFrame.getContentPane().add(lblNextLineNo);

            final CTextField txtNextLineNo = new CTextField();
            txtNextLineNo.setText(String.valueOf(lastLineNo + 1));
            txtNextLineNo.setBounds(200, 70, 100, 20);
            txtNextLineNo.setValidation(new NumericValidation());
            passBookFrame.getContentPane().add(txtNextLineNo);

            CButton btnOk = new CButton();
            btnOk.setText("OK");
            btnOk.setBounds(170, 100, 70, 30);
            btnOk.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    nextLineNo.put("NEXT_LINE_NO", new Integer(txtNextLineNo.getText()));
                    passBookFrame.dispose();
                }
            });
            passBookFrame.getContentPane().add(btnOk);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            /*
             * Center frame on the screen
             */
            Dimension frameSize = passBookFrame.getSize();
            passBookFrame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
            frameSize = null;
            screenSize = null;
            //Display the window.
            passBookFrame.setVisible(true);
            //            passBookFrame.addWindowListener(new WindowListener(){
            //                public void windowOpened(WindowEvent e) {
            //
            //                }
            //
            //                public void windowClosing(WindowEvent e) {
            //                    passBookFrame.setVisible(false);
            //                    passBookFrame.dispose();
            //                }
            //
            //                public void windowClosed(WindowEvent e) {
            //                    passBookFrame.setVisible(false);
            //                    passBookFrame.dispose();
            //                }
            //
            //                public void windowIconified(WindowEvent e) {
            //
            //                }
            //
            //                public void windowDeiconified(WindowEvent e) {
            //
            //                }
            //
            //                public void windowActivated(WindowEvent e) {
            //
            //                }
            //
            //                public void windowDeactivated(WindowEvent e) {
            //
            //                }
            //
            //            });
            //                System.out.println("@@@###PAssMAp"+passMap);
            ////                for(int i=0;i<tblData.getRowCount();i++){
            //                    ClientUtil.execute("deleteTempPassBook",passMap);
            ////                }
        }
        System.out.println("NEXT_LINE_NO " + nextLineNo.get("NEXT_LINE_NO"));
        txtLineNo.setText(CommonUtil.convertObjToStr(nextLineNo.get("NEXT_LINE_NO")));
//            passMap= new HashMap();
//            passMap.put("ACT_NUM",observable.getTxtAccNo());
        System.out.println("OBser" + observable.getSelectedBranchID());
        passMap.put("LINE_NO", nextLineNo.get("NEXT_LINE_NO"));
        if (observable.getTdtToDate() != null) {
            passMap.put("TODT", observable.getTdtToDate());
        } else {
            java.util.Date dt = ClientUtil.getCurrentDateWithTime();
            passMap.put("TODT", dt);
        }
        ClientUtil.execute("updatePassBookFlag", passMap);
        //    ClientUtil.execute("deleteTempPassBook",passMap);
        System.out.println("observable.getProdType()" + observable.getProdType());
        if (observable.getProdType().equals("OA")) {
            ClientUtil.execute("updatePassBookLineNo", passMap);
        } else if (observable.getProdType().equals("AD")) {
            ClientUtil.execute("updatePassBookLineNoAD", passMap);
        }
        observable.refreshTable();
        observable.clearPrintMap();

    }

    public void RdPrintActionPerformed() {
        /*
         * if(rdoPassBookType_RePrint.isSelected()){
         *
         * }else if(rdoPassBookType_Dup.isSelected()){
         *
         * }else{ String repName="RDPassBook"; HashMap reportMap = new
         * HashMap();
         * reportMap.put("Acct_Num",CommonUtil.convertObjToStr(txtAccNo.getText().substring(0,
         * 13)));
         * reportMap.put("Sl_No",CommonUtil.convertObjToInt(txtLineNo.getText()));
         * reportMap.put("Prod_Id",CommonUtil.convertObjToStr(observable.getCbmProdId().getKeyForSelected()));
         * callTTIntergration(repName,reportMap);
         * if(observable.getProdType().equals("TD")){
         * reportMap.put("Sl_No",CommonUtil.convertObjToInt(txtLineNo.getText())+(tblData.getRowCount()-1));
         * ClientUtil.execute("updatePassBookLineNoRD",reportMap); } }
         */
        btnClearActionPerformed(null);

    }

    public void btnPrintActionPerformed() {
        Selected = new LinkedHashMap();
        NotSelected = new LinkedHashMap();
        boolean selected = false;
        int lineNo = 0;
        ClientUtil.execute("deleteTempPassBook1", passMap);
//            passMap.put("ACT_NUM",observable.getTxtAccNo());
//            final java.util.List resultList;
//            resultList = ClientUtil.executeQuery("getNextLineNoForPassBook"+observable.getProdType(),passMap);
//            if(resultList != null && resultList.size()>0){
//                final HashMap resultMap = (HashMap)resultList.get(0);
//                lineNo = CommonUtil.convertObjToInt(resultMap.get("LINE_NO"));
//            }
        lineNo = CommonUtil.convertObjToInt(txtLineNo.getText());
        for (int i = 1; i < lineNo; i++) {
            passMap = new HashMap();
            passMap.put("ACT_NUM", observable.getTxtAccNo());
            passMap.put("Acct_Num", observable.getTxtAccNo());
            passMap.put("TRANS_DT", null);
            passMap.put("PARTICULARS", null);
            passMap.put("INST_TYPE", null);
            passMap.put("INST_NO", null);
            passMap.put("INST_DT", null);
            passMap.put("DEBIT", null);
            passMap.put("CREDIT", null);
            passMap.put("BALANCE", null);
            passMap.put("SLNO", null);
            passMap.put("PAGENO", null);
            Object obj = new Integer(i);
            //NotSelected.put(obj, passMap);
            Selected.put(obj, passMap);
            passMap = null;
        }
        System.out.println("@@@### Selected 1 : " + Selected);
        int slNo = 0;
        String prevBal = "0";
        for (int i = 0; i < tblData.getRowCount(); i++) {
            passMap = new HashMap();
           // boolean column = new Boolean(CommonUtil.convertObjToStr(tblData.getValueAt(i, 0))).booleanValue();
            passMap.put("ACT_NUM", observable.getTxtAccNo());
            passMap.put("Acct_Num", observable.getTxtAccNo());
            passMap.put("TRANS_DT", CommonUtil.convertObjToStr(tblData.getValueAt(i, 1)));
            passMap.put("PARTICULARS", CommonUtil.convertObjToStr(tblData.getValueAt(i, 2)));
            passMap.put("INST_TYPE", CommonUtil.convertObjToStr(tblData.getValueAt(i, 3)));
            passMap.put("INST_NO", CommonUtil.convertObjToStr(tblData.getValueAt(i, 4)));
            passMap.put("INST_DT", CommonUtil.convertObjToStr(tblData.getValueAt(i, 5)));
            passMap.put("DEBIT", CommonUtil.convertObjToStr(tblData.getValueAt(i, 6)));
            passMap.put("CREDIT", CommonUtil.convertObjToStr(tblData.getValueAt(i, 7)));
            passMap.put("BALANCE", CommonUtil.convertObjToStr(tblData.getValueAt(i, 8)));
//                if (i==0) {
//                    passMap.put("SLNO",CommonUtil.convertObjToStr(new Integer(lineNo)));
//                    passMap.put("SLNO",CommonUtil.convertObjToStr(new Integer(0)));  // for the first record i should be zero
            // based on this only generating slnos in report
//                } else {
            passMap.put("SLNO", CommonUtil.convertObjToStr(tblData.getValueAt(i, 9)));
//                }
            lineNo = CommonUtil.convertObjToInt(tblData.getValueAt(i, 9));
            passMap.put("PAGENO", CommonUtil.convertObjToStr(tblData.getValueAt(i, 10)));
            passMap.put("TRANS_ID", CommonUtil.convertObjToStr(tblData.getValueAt(i, 11)));
            passMap.put("BATCH_ID", CommonUtil.convertObjToStr(tblData.getValueAt(i, 12)));

//                HashMap prevBalMap= new HashMap();
//                prevBalMap.put("ACT_NUM",observable.getTxtAccNo());
//                prevBalMap.put("SLNO",tblData.getValueAt(i,9));
//                prevBalMap.put("PAGENO",tblData.getValueAt(i,10));
//                
//                if (i==0) {
//                    java.util.List resultList;
//                    resultList = ClientUtil.executeQuery("getPassBookPrevBalance",passMap);
//                    if(resultList != null && resultList.size()>0){
//                        HashMap resultMap = (HashMap)resultList.get(0);
//                        prevBal = CommonUtil.convertObjToStr(resultMap.get("BALANCE"));
//                        resultMap.clear();
//                        resultMap = null;
//                    }
//                    resultList.clear();
//                    resultList = null;
//                }
            passMap.put("PREV_BAL", prevBal);

            Object obj = tblData.getValueAt(i, 10) + "." + tblData.getValueAt(i, 9);
//            if (!column) {
//                NotSelected.put(obj, passMap);
//            } else {
                selected = true;
                Selected.put(obj, passMap);
            //}
            prevBal = CommonUtil.convertObjToStr(tblData.getValueAt(i, 8));
            passMap = null;
        }
        if (!selected) {
            Selected.clear();
        }

        System.out.println("@@@### Selected 2 : " + Selected);
        String accNo = CommonUtil.convertObjToStr(txtAccNo.getText());
        System.out.println("accNNN " + accNo);
        ArrayList addList = new ArrayList();
//        if (Selected.isEmpty()) {
//            addList = new ArrayList(NotSelected.keySet());
//        } else {
            addList = new ArrayList(Selected.keySet());
        //}
        System.out.println("@@@### addList : " + addList);
        for (int i = 0; i < addList.size(); i++) {
//            if (Selected.isEmpty()) {
//                passMap = (HashMap) NotSelected.get(addList.get(i));
//            } else {
                passMap = (HashMap) Selected.get(addList.get(i));
            //}
            ClientUtil.execute("inserIntoTmpPassBook", passMap);
        }
        if (CommonUtil.convertObjToInt(txtLineNo.getText()) >= 1 && CommonUtil.convertObjToInt(txtLineNo.getText()) <= addList.size()) {
            HashMap map = new HashMap();
            System.out.println("inside if.!");
            if (passMap.get("ACT_NUM") != null && passMap.containsKey("ACT_NUM")) {
                map.put("ACT_NUM", CommonUtil.convertObjToStr(passMap.get("ACT_NUM")));
                ClientUtil.execute("inserIntoTmpPassBook", map);
            }
        }
        String repName = "PassBook";
        callTTIntergration(repName, passMap);
        observable.setPrintMap(passMap);
        //if(b==1)
        // {

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccNo;
    private com.see.truetransact.uicomponent.CButton btnBarCodePrint;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnExit;
    private com.see.truetransact.uicomponent.CButton btnFrontPgPrint;
    private com.see.truetransact.uicomponent.CButton btnInitialise;
    private com.see.truetransact.uicomponent.CButton btnprintTrans;
    private com.see.truetransact.uicomponent.CComboBox cboAccountType;
    private com.see.truetransact.uicomponent.CComboBox cboModuleType;
    private com.see.truetransact.uicomponent.CLabel lblAccName;
    private com.see.truetransact.uicomponent.CLabel lblAccNameValue;
    private com.see.truetransact.uicomponent.CLabel lblAccNo;
    private com.see.truetransact.uicomponent.CLabel lblAccountType;
    private com.see.truetransact.uicomponent.CLabel lblAddress;
    private com.see.truetransact.uicomponent.CLabel lblAddressValue;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblLineNo;
    private com.see.truetransact.uicomponent.CLabel lblModuleType;
    private com.see.truetransact.uicomponent.CPanel panAcctNo;
    private com.see.truetransact.uicomponent.CPanel panButtons;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panSelection;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAndOr;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPassBookType;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CTextField txtAccNo;
    private com.see.truetransact.uicomponent.CTextField txtLineNo;
    // End of variables declaration//GEN-END:variables
}
