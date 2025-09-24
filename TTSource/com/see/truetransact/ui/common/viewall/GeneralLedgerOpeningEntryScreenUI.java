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
import java.util.Date;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * @author Swaroop
 */
public class GeneralLedgerOpeningEntryScreenUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer {

    private GeneralLedgerOpeningEntryScreenOB observable;
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
    private final static Logger log = Logger.getLogger(GeneralLedgerOpeningEntryScreenUI.class);
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

    public int getCheckValue() {
        return checkValue;
    }

    public CComboBox getCboMjrHead() {
        return cboMjrHead;
    }

    public void setCboMjrHead(CComboBox cboMjrHead) {
        this.cboMjrHead = cboMjrHead;
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
    public GeneralLedgerOpeningEntryScreenUI() {

        setupInit();
        setupScreen();
    }

    public GeneralLedgerOpeningEntryScreenUI(String print) {
        setObservable();
        initComponents();
//        Date tdt=   (Date)  txtClosingYear.getText();
//        txtClosingYear.setValidation(new ToDateValidation());
        //txtLineNo.setValidation(new NumericValidation());

    }

    private void setupInit() {
        initComponents();
        //internationalize();
        setObservable();
        toFront();
        setCombos();
        fillClosingYear();
        fillBranch();
        fillMjrHead();
        //transDetails = new TransDetailsUI(panMultiSearch);
        //transDetails.setRefreshActDetails(true);
        panSearchCondition.setVisible(true);
        btnClear.setVisible(true);
        observable.resetForm();
        update(observable, null);
        
       // tdtClosingYeara.setd;
//        txtLineNo.setValidation(new NumericValidation());
//        btnFrontPgPrint.setEnabled(false);
//        btnprintTrans.setEnabled(false);
//        //btnBarCodePrint.setEnabled(false);
//        btnInitialise.setEnabled(false);
//        tdtFromDate.setEnabled(true);
//        btnAccNo.setEnabled(false);
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
            observable = GeneralLedgerOpeningEntryScreenOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateOBFields() {
       // observable.setProdId(CommonUtil.convertObjToStr(((ComboBoxModel) cboAccountType.getModel()).getKeyForSelected()));
        observable.setProdType(CommonUtil.convertObjToStr(((ComboBoxModel) cboBranch.getModel()).getKeyForSelected()));
        //observable.setTxtAccNo(txtClosingYear.getText());
       // observable.setTdtFromDate(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
        //observable.setTdtToDate(DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()));
    }

    private void setCombos() {
        cboBranch.setModel(observable.getCbmProdType());
    }
    public void fillClosingYear() { 
        HashMap mapShare = new HashMap();
        List keyValue = null;
        keyValue = ClientUtil.executeQuery("getClosingYear", mapShare);
        mapShare = (HashMap) keyValue.get(0); 
        tdtClosingYeara.setDateValue(CommonUtil.convertObjToStr(mapShare.get("CLOSING_YEAR")));
        keyValue.clear();
        keyValue = null;
        mapShare.clear();
        mapShare = null;
    }

    public void fillBranch() {
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        HashMap mapShare = new HashMap();
        List keyValue = null;
        keyValue = ClientUtil.executeQuery("getbranchesForBalanceSheet", mapShare);
        key.add("");
        value.add("");
        if (keyValue != null && keyValue.size() > 0) {
            for (int i = 0; i < keyValue.size(); i++) {
                mapShare = (HashMap) keyValue.get(i);
                value.add(mapShare.get("BRANCH_CODE"));
                key.add(mapShare.get("BRANCH_NAME"));
            }
        }
        cbmbranch = new ComboBoxModel(key, value);
        cboBranch.setModel(cbmbranch);
        key = null;
        value = null;
        keyValue.clear();
        keyValue = null;
        mapShare.clear();
        mapShare = null;
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
                value.add(mapShare.get("DESCRIPTION"));
                key.add(mapShare.get("MAJOR A/C HEAD CODE"));
            }
        }
        cbmMjrHead = new ComboBoxModel(key, value);
        cboMjrHead.setModel(cbmMjrHead);
        key = null;
        value = null;
        keyValue.clear();
        keyValue = null;
        mapShare.clear();
        mapShare = null;
    }
    public void populateData() {
       // updateOBFields();
//        System.out.println("check value 1 : "+getCheckValue());
//        System.out.println("check value 2 : "+checkValue);
//        System.out.println("txtClosingYear.getText().equals: "+tdtClosingYeara.getDateValue().equals(""));
//        System.out.println("txtClosingYear.getText() " + tdtClosingYeara.getDateValue());
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        HashMap passbk = new HashMap(); 
        viewMap.put(CommonConstants.MAP_NAME, "getGLOpeningEntryScreenDetails" );
       // whereMap.put("FROM_SLNO", CommonUtil.convertObjToInt(txtLineNo.getText()));
        whereMap.put("CLSYR", tdtClosingYeara.getDateValue());
        whereMap.put("BARCHID",cboBranch.getSelectedItem() );
        whereMap.put("MJRID", cboMjrHead.getSelectedItem());
        whereMap.put("CHKVAL",getCheckValue() );
        if(tdtClosingYeara.getDateValue().equals("")){
            ClientUtil.displayAlert("Closing Year should not be empty!!!");
            return;
        }
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {
            log.info("populateData...");
            ArrayList heading = observable.populateData(viewMap, tblData);
            heading = null;
            javax.swing.table.TableColumn col = tblData.getColumn(tblData.getColumnName(5));
            col.setMaxWidth(0);
            col.setMinWidth(0);
            col.setMaxWidth(0);
            col.setWidth(0);
            col.setPreferredWidth(0);
            col = tblData.getColumn(tblData.getColumnName(6));
            col.setMaxWidth(0);
            col.setMinWidth(0);
            col.setMaxWidth(0);
            col.setWidth(0);
            col.setPreferredWidth(0);
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
        observable.getGrandTotalAmount(whereMap);
        double assetBal=CommonUtil.convertObjToDouble(observable.getAssetBal());
        double liableBal=CommonUtil.convertObjToDouble(observable.getLiableBal());
        
        double diff=(assetBal-liableBal);
        txtAssetsBal.setText(CommonUtil.convertObjToStr(assetBal));
        txtLiabilityBal.setText(CommonUtil.convertObjToStr(liableBal));
        txtDifference.setText(CommonUtil.convertObjToStr(diff));
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
        cboBranch = new com.see.truetransact.uicomponent.CComboBox();
        cboMjrHead = new com.see.truetransact.uicomponent.CComboBox();
        lblModuleType = new com.see.truetransact.uicomponent.CLabel();
        lblModuleType1 = new com.see.truetransact.uicomponent.CLabel();
        lblModuleType3 = new com.see.truetransact.uicomponent.CLabel();
        chkViewBalance = new com.see.truetransact.uicomponent.CCheckBox();
        tdtClosingYeara = new com.see.truetransact.uicomponent.CDateField();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        txtLiabilityBal = new com.see.truetransact.uicomponent.CTextField();
        txtAssetsBal = new com.see.truetransact.uicomponent.CTextField();
        txtDifference = new com.see.truetransact.uicomponent.CTextField();
        lblModuleType2 = new com.see.truetransact.uicomponent.CLabel();
        lblModuleType4 = new com.see.truetransact.uicomponent.CLabel();
        btnIntDet = new com.see.truetransact.uicomponent.CButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(750, 630));
        setPreferredSize(new java.awt.Dimension(750, 630));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panSearchCondition.setMinimumSize(new java.awt.Dimension(700, 100));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(700, 100));
        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        cboBranch.setMaximumSize(new java.awt.Dimension(200, 21));
        cboBranch.setMinimumSize(new java.awt.Dimension(200, 21));
        cboBranch.setPreferredSize(new java.awt.Dimension(200, 21));
        cboBranch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboBranchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(cboBranch, gridBagConstraints);

        cboMjrHead.setMaximumSize(new java.awt.Dimension(200, 21));
        cboMjrHead.setMinimumSize(new java.awt.Dimension(200, 21));
        cboMjrHead.setPreferredSize(new java.awt.Dimension(200, 21));
        cboMjrHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMjrHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(cboMjrHead, gridBagConstraints);

        lblModuleType.setText("Major Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(lblModuleType, gridBagConstraints);

        lblModuleType1.setText("Ho / Branch");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 2);
        panSearchCondition.add(lblModuleType1, gridBagConstraints);

        lblModuleType3.setText("Closing Year");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(lblModuleType3, gridBagConstraints);

        chkViewBalance.setText("View Balance <> 0");
        chkViewBalance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkViewBalanceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panSearchCondition.add(chkViewBalance, gridBagConstraints);

        tdtClosingYeara.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tdtClosingYearaMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tdtClosingYearaMousePressed(evt);
            }
        });
        tdtClosingYeara.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtClosingYearaFocusLost(evt);
            }
        });
        tdtClosingYeara.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tdtClosingYearaKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 4, 6);
        panSearchCondition.add(tdtClosingYeara, gridBagConstraints);

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

        panSearch.setMinimumSize(new java.awt.Dimension(750, 100));
        panSearch.setPreferredSize(new java.awt.Dimension(750, 100));
        panSearch.setLayout(new java.awt.GridBagLayout());

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipadx = 28;
        gridBagConstraints.ipady = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 14, 11, 0);
        panSearch.add(btnClear, gridBagConstraints);

        txtLiabilityBal.setEditable(false);
        txtLiabilityBal.setAllowNumber(true);
        txtLiabilityBal.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLiabilityBal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLiabilityBalActionPerformed(evt);
            }
        });
        txtLiabilityBal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLiabilityBalFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 89;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 6);
        panSearch.add(txtLiabilityBal, gridBagConstraints);

        txtAssetsBal.setEditable(false);
        txtAssetsBal.setAllowNumber(true);
        txtAssetsBal.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAssetsBal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAssetsBalActionPerformed(evt);
            }
        });
        txtAssetsBal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAssetsBalFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 77;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 0);
        panSearch.add(txtAssetsBal, gridBagConstraints);

        txtDifference.setEditable(false);
        txtDifference.setAllowNumber(true);
        txtDifference.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDifference.setPreferredSize(new java.awt.Dimension(200, 21));
        txtDifference.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDifferenceActionPerformed(evt);
            }
        });
        txtDifference.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDifferenceFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 276;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 6);
        panSearch.add(txtDifference, gridBagConstraints);

        lblModuleType2.setText("Grand Total");
        lblModuleType2.setMaximumSize(new java.awt.Dimension(100, 20));
        lblModuleType2.setMinimumSize(new java.awt.Dimension(100, 20));
        lblModuleType2.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 135, 0, 0);
        panSearch.add(lblModuleType2, gridBagConstraints);

        lblModuleType4.setText("Difference");
        lblModuleType4.setMaximumSize(new java.awt.Dimension(100, 20));
        lblModuleType4.setMinimumSize(new java.awt.Dimension(100, 20));
        lblModuleType4.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = -22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 135, 0, 0);
        panSearch.add(lblModuleType4, gridBagConstraints);

        btnIntDet.setText("Refresh Grid");
        btnIntDet.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnIntDet.setMaximumSize(new java.awt.Dimension(145, 27));
        btnIntDet.setMinimumSize(new java.awt.Dimension(145, 27));
        btnIntDet.setPreferredSize(new java.awt.Dimension(145, 27));
        btnIntDet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIntDetActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -30;
        gridBagConstraints.ipady = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 14, 0, 0);
        panSearch.add(btnIntDet, gridBagConstraints);

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
    boolean btnIntDetPressed = false;

    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        System.out.println("#$#$ Hash : " + hash); 
        cboBranch.setModel(observable.getCbmProdType());
        cboBranch.setModel(observable.getCbmProdType()); 
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

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        observable.resetForm();
        btnCancel=true;
        update(observable, null);
        //cboMjrHead.set;
        behavesLike = "";
        tdtClosingYeara.setDateValue(null);
        chkViewBalance.setSelected(false);
        setModified(false);
        txtAssetsBal.setText("");
        txtDifference.setText("");
        txtLiabilityBal.setText("");
        act=0;
    }//GEN-LAST:event_btnClearActionPerformed

    private void cboBranchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboBranchActionPerformed
     
        populateData();
    }//GEN-LAST:event_cboBranchActionPerformed

    private void cboMjrHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMjrHeadActionPerformed
     populateData();
    }//GEN-LAST:event_cboMjrHeadActionPerformed

    private void txtLiabilityBalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLiabilityBalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLiabilityBalActionPerformed

    private void txtLiabilityBalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLiabilityBalFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLiabilityBalFocusLost

    private void txtAssetsBalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAssetsBalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAssetsBalActionPerformed

    private void txtAssetsBalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAssetsBalFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAssetsBalFocusLost

    private void txtDifferenceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDifferenceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDifferenceActionPerformed

    private void txtDifferenceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDifferenceFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDifferenceFocusLost

    private void btnIntDetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIntDetActionPerformed
        HashMap mapShare = new HashMap();
        Date closingDt = DateUtil.getDateMMDDYYYY(tdtClosingYeara.getDateValue());
        mapShare.put("CLOSING_YEAR",closingDt);
         ClientUtil.execute("btnRefreshQuery", mapShare);
        
        mapShare.clear();
        mapShare = null;
    }//GEN-LAST:event_btnIntDetActionPerformed

private void chkViewBalanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkViewBalanceActionPerformed
    if (chkViewBalance.isSelected()) {
        setCheckValue(1);
    }else{
       setCheckValue(0); 
    }
    System.out.println("check value : " + getCheckValue());
    populateData();
}//GEN-LAST:event_chkViewBalanceActionPerformed

    private void tdtClosingYearaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtClosingYearaFocusLost
        // TODO add your handling code here:
        System.out.println("focus value : ");
        HashMap mapShare = new HashMap();
        mapShare.put("DT",tdtClosingYeara.getDateValue());
        List keyValue = null;
        keyValue = ClientUtil.executeQuery("getAuditFinancialYearDt", mapShare);
        mapShare = (HashMap) keyValue.get(0); 
        tdtClosingYeara.setDateValue(CommonUtil.convertObjToStr(mapShare.get("FIN_CLS_DT")));
        observable.setTdtFromDate(DateUtil.getDateMMDDYYYY(tdtClosingYeara.getDateValue()));
        populateData(); 
    }//GEN-LAST:event_tdtClosingYearaFocusLost

    private void tdtClosingYearaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tdtClosingYearaMouseClicked
//System.out.println("clicked value : ");        // TODO add your handling code here:
    }//GEN-LAST:event_tdtClosingYearaMouseClicked

    private void tdtClosingYearaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tdtClosingYearaMousePressed
//System.out.println("pressed value : ");        // TODO add your handling code here:
    }//GEN-LAST:event_tdtClosingYearaMousePressed

    private void tdtClosingYearaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tdtClosingYearaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtClosingYearaKeyPressed

    
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
        // TODO add your handling code here:
        if (tblData.getSelectedRowCount() > 0) {
            double closeBal = CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 2));
            double closebalref = CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 6));
            System.out.println("closebal :: " + closeBal + " closebalref ::" + closebalref);
            if (closeBal != closebalref) {
                HashMap updateMap = new HashMap();
                updateMap.put("CLOSE_BAL", tblData.getValueAt(tblData.getSelectedRow(), 2));
                updateMap.put("BRANCH_CODE", tblData.getValueAt(tblData.getSelectedRow(), 4));
                updateMap.put("DT", tdtClosingYeara.getDateValue());
                updateMap.put("AC_HD_ID", tblData.getValueAt(tblData.getSelectedRow(), 5));
                ClientUtil.execute("updateAuditGLBalances", updateMap);
                populateData();
            }
        }
    }//GEN-LAST:event_tblDataPropertyChange



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
        new GeneralLedgerOpeningEntryScreenUI().show();
    }

    public void update(Observable observed, Object arg) {
        ((ComboBoxModel) cboBranch.getModel()).setKeyForSelected(observable.getProdType());
        
        //System.out.println("0006" + observable.getTxtAccNo());
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
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnIntDet;
    private com.see.truetransact.uicomponent.CComboBox cboBranch;
    private com.see.truetransact.uicomponent.CComboBox cboMjrHead;
    private com.see.truetransact.uicomponent.CCheckBox chkViewBalance;
    private com.see.truetransact.uicomponent.CLabel lblModuleType;
    private com.see.truetransact.uicomponent.CLabel lblModuleType1;
    private com.see.truetransact.uicomponent.CLabel lblModuleType2;
    private com.see.truetransact.uicomponent.CLabel lblModuleType3;
    private com.see.truetransact.uicomponent.CLabel lblModuleType4;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAndOr;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPassBookType;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CDateField tdtClosingYeara;
    private com.see.truetransact.uicomponent.CTextField txtAssetsBal;
    private com.see.truetransact.uicomponent.CTextField txtDifference;
    private com.see.truetransact.uicomponent.CTextField txtLiabilityBal;
    // End of variables declaration//GEN-END:variables
}
