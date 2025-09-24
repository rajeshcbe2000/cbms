/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CalenderHolidays.java
 *
 * Created on January 23, 2004, 10:50 AM
 */

package com.see.truetransact.ui.sysadmin.calender;

import com.see.truetransact.ui.sysadmin.calender.CalenderHolidaysRB;
import com.see.truetransact.uicomponent.CButtonGroup;// To add and Remove the Radio Buttons...
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import javax.swing.table.DefaultTableModel;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.clientproxy.ProxyParameters;

import java.util.ArrayList;
import java.util.Observable;
import java.util.HashMap;
import java.util.Observer;
//import java.util.GregorianCalendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author   Lohith R.
 */

public class CalenderHolidaysUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer,UIMandatoryField{
    private String holidayId = "" ;
    private HashMap mandatoryMap;
    private CalenderHolidaysOB observable;
    //    private CalenderHolidaysRB objCalenderHolidaysRB;
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.calender.CalenderHolidaysRB", ProxyParameters.LANGUAGE);
    
    private final int EDIT=0,DELETE=1;
    final int AUTHORIZE=3, VIEW =4;
    int viewType=-1;
    private int intYear;
    private int leapYear;
    private int ACTION=-1;
    private int month = 0;
    private int tableRow = 0;
    private int enableButton = 0;
    private int getEditperformed = 0;
    private int dateValue;
    private Integer integerYear;
    private Integer integerSplit;
    private ArrayList tableData;
    private ArrayList arrayListTableDate;
    private String columnDate;
    private StringBuffer compareDate;
    private final String dateSeparator = "/";
    private boolean dataExist;
    private Date currentYear;
    public String stringMonthData;
    public String stringYearData;
    private boolean uniqueCombo = false;
    boolean isFilled = false;
    Date curDate = null;
    /** Creates new form AdminCalenderHolidays */
    public CalenderHolidaysUI() {
        curDate = ClientUtil.getCurrentDate();
        initComponents();
        initStartUp();
    }
    
    /** Initialzation of UI */
    private void initStartUp(){
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        initComponentData();
        setMaximumLength();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panHoliday);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panWeeklyOff);
        setHelpMessage();
        observable.resetStatus();
        observable.resetForm();
        observable.removeHolidayRow();
        resetYear();
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
    }
    
    private void setObservable() {
        /* Implementing Singleton pattern */
        observable = CalenderHolidaysOB.getInstance();
        observable.addObserver(this);
    }
    
    /** Auto Generated Method - setFieldNames()
     * This method assigns name for all the components.
     * Other functions are working based on this name. */
    private void setFieldNames() {
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        btnYearDec.setName("btnYearDec");
        btnYearInc.setName("btnYearInc");
        cboDate.setName("cboDate");
        cboHalfDay1.setName("cboHalfDay1");
        cboHalfDay2.setName("cboHalfDay2");
        cboMonth.setName("cboMonth");
        cboWeeklyOff1.setName("cboWeeklyOff1");
        cboWeeklyOff2.setName("cboWeeklyOff2");
        lblDate.setName("lblDate");
        lblHalfDay1.setName("lblHalfDay1");
        lblHalfDay2.setName("lblHalfDay2");
        lblHoliday.setName("lblHoliday");
        lblMonth.setName("lblMonth");
        lblMsg.setName("lblMsg");
        lblRemarks.setName("lblRemarks");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblStatus.setName("lblStatus");
        lblWeeklyOff.setName("lblWeeklyOff");
        lblWeeklyOff1.setName("lblWeeklyOff1");
        lblWeeklyOff2.setName("lblWeeklyOff2");
        lblYear.setName("lblYear");
        mbrMain.setName("mbrMain");
        panHoliday.setName("panHoliday");
        panLeft.setName("panLeft");
        panMain.setName("panMain");
        panStatus.setName("panStatus");
        panWeek.setName("panWeek");
        panWeeklyOff.setName("panWeeklyOff");
        panYear.setName("panYear");
        panYearIncDec.setName("panYearIncDec");
        rdoWeeklyOff_No.setName("rdoWeeklyOff_No");
        rdoWeeklyOff_Yes.setName("rdoWeeklyOff_Yes");
        sptMonth.setName("sptMonth");
        srpTable.setName("srpTable");
        tblHolidayNames.setName("tblHolidayNames");
        txtHolidayName.setName("txtHolidayName");
        txtRemarks.setName("txtRemarks");
        txtYear.setName("txtYear");
    }
    
    
    /** Auto Generated Method - internationalize()
     * This method used to assign display texts from
     * the Resource Bundle File. */
    private void internationalize() {
        //        CalenderHolidaysRB resourceBundle = new CalenderHolidaysRB();
        btnClose.setText(resourceBundle.getString("btnClose"));
        ((javax.swing.border.TitledBorder)panLeft.getBorder()).setTitle(resourceBundle.getString("panLeft"));
        rdoWeeklyOff_No.setText(resourceBundle.getString("rdoWeeklyOff_No"));
        btnYearDec.setText(resourceBundle.getString("btnYearDec"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblWeeklyOff1.setText(resourceBundle.getString("lblWeeklyOff1"));
        btnYearInc.setText(resourceBundle.getString("btnYearInc"));
        lblHalfDay1.setText(resourceBundle.getString("lblHalfDay1"));
        rdoWeeklyOff_Yes.setText(resourceBundle.getString("rdoWeeklyOff_Yes"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblYear.setText(resourceBundle.getString("lblYear"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblWeeklyOff.setText(resourceBundle.getString("lblWeeklyOff"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
        lblMonth.setText(resourceBundle.getString("lblMonth"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        ((javax.swing.border.TitledBorder)panHoliday.getBorder()).setTitle(resourceBundle.getString("panHoliday"));
        lblWeeklyOff2.setText(resourceBundle.getString("lblWeeklyOff2"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblHalfDay2.setText(resourceBundle.getString("lblHalfDay2"));
        lblDate.setText(resourceBundle.getString("lblDate"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
    }
    
    /** Auto Generated Method - setMandatoryHashMap()
     * This method list out all the Input Fields available in the UI.
     * It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("rdoWeeklyOff_Yes", new Boolean(true));
        mandatoryMap.put("cboWeeklyOff1", new Boolean(false));
        mandatoryMap.put("cboWeeklyOff2", new Boolean(false));
        mandatoryMap.put("cboHalfDay1", new Boolean(false));
        mandatoryMap.put("cboHalfDay2", new Boolean(false));
        mandatoryMap.put("cboMonth", new Boolean(true));
        mandatoryMap.put("cboDate", new Boolean(true));
        mandatoryMap.put("txtHolidayName", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(true));
        mandatoryMap.put("txtYear", new Boolean(true));
    }
    
    /** Auto Generated Method - getMandatoryHashMap()
     * Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    /** Auto Generated Method - update()
     * This method called by Observable. It updates the UI with
     * Observable's data. If needed add/Remove RadioButtons
     * method need to be added.*/
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        rdoWeeklyOff_Yes.setSelected(observable.getRdoWeeklyOff_Yes());
        rdoWeeklyOff_No.setSelected(observable.getRdoWeeklyOff_No());
        cboWeeklyOff1.setSelectedItem(observable.getCboWeeklyOff1());
        cboWeeklyOff2.setSelectedItem(observable.getCboWeeklyOff2());
        cboHalfDay1.setSelectedItem(observable.getCboHalfDay1());
        cboHalfDay2.setSelectedItem(observable.getCboHalfDay2());
        cboMonth.setSelectedItem(observable.getCboMonth());
        cboDate.setSelectedItem(observable.getCboDate());
        txtHolidayName.setText(observable.getTxtHolidayName());
        txtRemarks.setText(observable.getTxtRemarks());
        txtYear.setText(observable.getTxtYear());
        tblHolidayNames.setModel(observable.getTblHolidays());
        lblStatus.setText(observable.getLblStatus());
        addRadioButtons();
    }
    
    
    private void initComponentData() {
        cboMonth.setModel(observable.getCbmMonth());
        cboWeeklyOff1.setModel(observable.getCbmWeeklyOff1());
        cboWeeklyOff2.setModel(observable.getCbmWeeklyOff2());
        cboHalfDay1.setModel(observable.getCbmHalfDay1());
        cboHalfDay2.setModel(observable.getCbmHalfDay2());
    }
    
    private void setMaximumLength(){
        txtYear.setMaxLength(32);
        txtHolidayName.setMaxLength(16);
        txtRemarks.setMaxLength(64);
    }
    
    /** Auto Generated Method - setHelpMessage()
     * This method shows tooltip help for all the input fields
     * available in the UI. It needs the Mandatory Resource Bundle
     * object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        CalenderHolidaysMRB objMandatoryRB = new CalenderHolidaysMRB();
        rdoWeeklyOff_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoWeeklyOff_Yes"));
        cboWeeklyOff1.setHelpMessage(lblMsg, objMandatoryRB.getString("cboWeeklyOff1"));
        cboWeeklyOff2.setHelpMessage(lblMsg, objMandatoryRB.getString("cboWeeklyOff2"));
        cboHalfDay1.setHelpMessage(lblMsg, objMandatoryRB.getString("cboHalfDay1"));
        cboHalfDay2.setHelpMessage(lblMsg, objMandatoryRB.getString("cboHalfDay2"));
        cboMonth.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMonth"));
        cboDate.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDate"));
        txtHolidayName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtHolidayName"));
        txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));
        txtYear.setHelpMessage(lblMsg, objMandatoryRB.getString("txtYear"));
    }
    
    // To Reset the Radio Buttons in the UI after any operation, We've to
    //1. Remove the Radio Buttons fron the Radio Groups...
    //2. Add the Radio Buttons Back in The Radio Groups...
    //a.) To Remove the Radio buttons...
    private void removeRadioButtons() {
        rdgActivity.remove(rdoWeeklyOff_Yes);
        rdgActivity.remove(rdoWeeklyOff_No);
    }
    
    // b.) To Add the Radio buttons...
    private void addRadioButtons() {
        rdgActivity = new CButtonGroup();
        rdgActivity.add(rdoWeeklyOff_Yes);
        rdgActivity.add(rdoWeeklyOff_No);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgActivity = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrMain = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace18 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace19 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace20 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace21 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panMain = new com.see.truetransact.uicomponent.CPanel();
        panLeft = new com.see.truetransact.uicomponent.CPanel();
        panWeek = new com.see.truetransact.uicomponent.CPanel();
        lblWeeklyOff = new com.see.truetransact.uicomponent.CLabel();
        lblWeeklyOff1 = new com.see.truetransact.uicomponent.CLabel();
        lblWeeklyOff2 = new com.see.truetransact.uicomponent.CLabel();
        lblHalfDay1 = new com.see.truetransact.uicomponent.CLabel();
        lblHalfDay2 = new com.see.truetransact.uicomponent.CLabel();
        panWeeklyOff = new com.see.truetransact.uicomponent.CPanel();
        rdoWeeklyOff_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoWeeklyOff_No = new com.see.truetransact.uicomponent.CRadioButton();
        cboWeeklyOff1 = new com.see.truetransact.uicomponent.CComboBox();
        cboWeeklyOff2 = new com.see.truetransact.uicomponent.CComboBox();
        cboHalfDay1 = new com.see.truetransact.uicomponent.CComboBox();
        cboHalfDay2 = new com.see.truetransact.uicomponent.CComboBox();
        panHoliday = new com.see.truetransact.uicomponent.CPanel();
        lblYear = new com.see.truetransact.uicomponent.CLabel();
        lblMonth = new com.see.truetransact.uicomponent.CLabel();
        lblDate = new com.see.truetransact.uicomponent.CLabel();
        lblHoliday = new com.see.truetransact.uicomponent.CLabel();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        sptMonth = new com.see.truetransact.uicomponent.CSeparator();
        cboMonth = new com.see.truetransact.uicomponent.CComboBox();
        cboDate = new com.see.truetransact.uicomponent.CComboBox();
        txtHolidayName = new com.see.truetransact.uicomponent.CTextField();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        panYear = new com.see.truetransact.uicomponent.CPanel();
        panYearIncDec = new com.see.truetransact.uicomponent.CPanel();
        btnYearInc = new com.see.truetransact.uicomponent.CButton();
        btnYearDec = new com.see.truetransact.uicomponent.CButton();
        txtYear = new com.see.truetransact.uicomponent.CTextField();
        srpTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblHolidayNames = new com.see.truetransact.uicomponent.CTable();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Holidays");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        tbrMain.setEnabled(false);
        tbrMain.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11)); // NOI18N
        tbrMain.setMinimumSize(new java.awt.Dimension(28, 28));
        tbrMain.setPreferredSize(new java.awt.Dimension(28, 28));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.setEnabled(false);
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrMain.add(btnView);

        lblSpace4.setText("     ");
        tbrMain.add(lblSpace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrMain.add(btnNew);

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace17);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrMain.add(btnEdit);

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace18);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrMain.add(btnDelete);

        lblSpace1.setText("     ");
        tbrMain.add(lblSpace1);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrMain.add(btnSave);

        lblSpace19.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace19.setText("     ");
        lblSpace19.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace19);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrMain.add(btnCancel);

        lblSpace2.setText("     ");
        tbrMain.add(lblSpace2);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrMain.add(btnAuthorize);

        lblSpace20.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace20.setText("     ");
        lblSpace20.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace20);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrMain.add(btnException);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace21);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrMain.add(btnReject);

        lblSpace5.setText("     ");
        tbrMain.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrMain.add(btnPrint);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace22);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrMain.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tbrMain, gridBagConstraints);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace3.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace3, gridBagConstraints);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(panStatus, gridBagConstraints);

        panMain.setLayout(new java.awt.GridBagLayout());

        panLeft.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panLeft.setLayout(new java.awt.GridBagLayout());

        panWeek.setBorder(javax.swing.BorderFactory.createTitledBorder("Weekly Off"));
        panWeek.setLayout(new java.awt.GridBagLayout());

        lblWeeklyOff.setText("Weekly Off");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panWeek.add(lblWeeklyOff, gridBagConstraints);

        lblWeeklyOff1.setText("Weekly Off 1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panWeek.add(lblWeeklyOff1, gridBagConstraints);

        lblWeeklyOff2.setText("Weekly Off 2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panWeek.add(lblWeeklyOff2, gridBagConstraints);

        lblHalfDay1.setText("Half Day 1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panWeek.add(lblHalfDay1, gridBagConstraints);

        lblHalfDay2.setText("Half Day 2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panWeek.add(lblHalfDay2, gridBagConstraints);

        panWeeklyOff.setMinimumSize(new java.awt.Dimension(100, 27));
        panWeeklyOff.setPreferredSize(new java.awt.Dimension(100, 27));
        panWeeklyOff.setLayout(new java.awt.GridBagLayout());

        rdoWeeklyOff_Yes.setSelected(true);
        rdoWeeklyOff_Yes.setText("Yes");
        rdoWeeklyOff_Yes.setMinimumSize(new java.awt.Dimension(50, 21));
        rdoWeeklyOff_Yes.setPreferredSize(new java.awt.Dimension(50, 21));
        rdoWeeklyOff_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoWeeklyOff_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panWeeklyOff.add(rdoWeeklyOff_Yes, gridBagConstraints);

        rdoWeeklyOff_No.setText("No");
        rdoWeeklyOff_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoWeeklyOff_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panWeeklyOff.add(rdoWeeklyOff_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panWeek.add(panWeeklyOff, gridBagConstraints);

        cboWeeklyOff1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboWeeklyOff1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panWeek.add(cboWeeklyOff1, gridBagConstraints);

        cboWeeklyOff2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboWeeklyOff2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panWeek.add(cboWeeklyOff2, gridBagConstraints);

        cboHalfDay1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboHalfDay1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panWeek.add(cboHalfDay1, gridBagConstraints);

        cboHalfDay2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboHalfDay2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panWeek.add(cboHalfDay2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeft.add(panWeek, gridBagConstraints);

        panHoliday.setBorder(javax.swing.BorderFactory.createTitledBorder("Holiday List"));
        panHoliday.setLayout(new java.awt.GridBagLayout());

        lblYear.setText("Year");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panHoliday.add(lblYear, gridBagConstraints);

        lblMonth.setText("Month");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panHoliday.add(lblMonth, gridBagConstraints);

        lblDate.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panHoliday.add(lblDate, gridBagConstraints);

        lblHoliday.setText("Holiday Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panHoliday.add(lblHoliday, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panHoliday.add(lblRemarks, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panHoliday.add(sptMonth, gridBagConstraints);

        cboMonth.setMinimumSize(new java.awt.Dimension(101, 21));
        cboMonth.setPreferredSize(new java.awt.Dimension(101, 21));
        cboMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMonthActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panHoliday.add(cboMonth, gridBagConstraints);

        cboDate.setMinimumSize(new java.awt.Dimension(101, 21));
        cboDate.setPreferredSize(new java.awt.Dimension(101, 21));
        cboDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panHoliday.add(cboDate, gridBagConstraints);

        txtHolidayName.setMinimumSize(new java.awt.Dimension(101, 21));
        txtHolidayName.setPreferredSize(new java.awt.Dimension(101, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panHoliday.add(txtHolidayName, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(101, 21));
        txtRemarks.setPreferredSize(new java.awt.Dimension(101, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panHoliday.add(txtRemarks, gridBagConstraints);

        panYear.setLayout(new java.awt.GridBagLayout());

        panYearIncDec.setLayout(new java.awt.GridBagLayout());

        btnYearInc.setText("cButton1");
        btnYearInc.setPreferredSize(new java.awt.Dimension(10, 10));
        btnYearInc.setEnabled(false);
        btnYearInc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnYearIncActionPerformed(evt);
            }
        });
        panYearIncDec.add(btnYearInc, new java.awt.GridBagConstraints());

        btnYearDec.setText("cButton2");
        btnYearDec.setPreferredSize(new java.awt.Dimension(10, 10));
        btnYearDec.setEnabled(false);
        btnYearDec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnYearDecActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panYearIncDec.add(btnYearDec, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panYear.add(panYearIncDec, gridBagConstraints);

        txtYear.setEditable(false);
        txtYear.setPreferredSize(new java.awt.Dimension(91, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panYear.add(txtYear, gridBagConstraints);

        panHoliday.add(panYear, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 30.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeft.add(panHoliday, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(panLeft, gridBagConstraints);

        srpTable.setMinimumSize(new java.awt.Dimension(250, 365));
        srpTable.setPreferredSize(new java.awt.Dimension(250, 365));

        tblHolidayNames.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        srpTable.setViewportView(tblHolidayNames);

        panMain.add(srpTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        getContentPane().add(panMain, gridBagConstraints);

        mnuProcess.setText("Process");
        mnuProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuProcessActionPerformed(evt);
            }
        });

        mitNew.setText("New");
        mitNew.setEnabled(false);
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setEnabled(false);
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setEnabled(false);
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptDelete);

        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUpItems(VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
         HashMap reportParamMap = new HashMap();
 com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed

    private void mnuProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProcessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuProcessActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled){
            viewType = -1;
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            singleAuthorizeMap.put("HOLIDAY ID", observable.getHolidayID());
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, curDate);
            singleAuthorizeMap.put("BRANCH_ID", ProxyParameters.USER_ID);
            ClientUtil.execute("authorizeCalendar", singleAuthorizeMap);
            btnCancelActionPerformed(null);
        } else{
            viewType = AUTHORIZE;
            final HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getSelectCalendarAuthorizeTOList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeCalendar");
            mapParam.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            isFilled = false;
            setModified(true);
            final AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        }
    }
    private void cboHalfDay2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboHalfDay2ActionPerformed
        // Add your handling code here:
        if ( cboHalfDay2.getSelectedItem().toString().length() > 0){
            observable.weeklyOff1Combo = cboWeeklyOff1.getSelectedItem().toString();
            observable.weeklyOff2Combo = cboWeeklyOff2.getSelectedItem().toString();
            observable.halfDay1Combo = cboHalfDay1.getSelectedItem().toString();
            observable.halfDay2Combo = cboHalfDay2.getSelectedItem().toString();
            observable.selectedCombo = 4;
            uniqueCombo = observable.checkComboUnique();
            if(!uniqueCombo){
                cboHalfDay2.setSelectedItem("");
            }
        }
    }//GEN-LAST:event_cboHalfDay2ActionPerformed
    
    private void cboHalfDay1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboHalfDay1ActionPerformed
        // Add your handling code here:
        if ( cboHalfDay1.getSelectedItem().toString().length() > 0){
            cboHalfDay2.setEnabled(true);
            observable.weeklyOff1Combo = cboWeeklyOff1.getSelectedItem().toString();
            observable.weeklyOff2Combo = cboWeeklyOff2.getSelectedItem().toString();
            observable.halfDay1Combo = cboHalfDay1.getSelectedItem().toString();
            observable.halfDay2Combo = cboHalfDay2.getSelectedItem().toString();
            observable.selectedCombo = 3;
            uniqueCombo = observable.checkComboUnique();
            if(!uniqueCombo){
                cboHalfDay1.setSelectedItem("");
                cboHalfDay2.setSelectedItem("");
                cboHalfDay2.setEnabled(false);
            }
        }else{
            cboHalfDay2.setSelectedItem("");
            cboHalfDay2.setEnabled(false);
        }
    }//GEN-LAST:event_cboHalfDay1ActionPerformed
    
    private void cboWeeklyOff2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboWeeklyOff2ActionPerformed
        // Add your handling code here:
        if ( cboWeeklyOff2.getSelectedItem().toString().length() > 0){
            observable.weeklyOff1Combo = cboWeeklyOff1.getSelectedItem().toString();
            observable.weeklyOff2Combo = cboWeeklyOff2.getSelectedItem().toString();
            observable.halfDay1Combo = cboHalfDay1.getSelectedItem().toString();
            observable.halfDay2Combo = cboHalfDay2.getSelectedItem().toString();
            observable.selectedCombo = 2;
            uniqueCombo = observable.checkComboUnique();
            if(!uniqueCombo){
                cboWeeklyOff2.setSelectedItem("");
            }
        }
    }//GEN-LAST:event_cboWeeklyOff2ActionPerformed
    
    private void cboWeeklyOff1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboWeeklyOff1ActionPerformed
        // Add your handling code here:
        if ( cboWeeklyOff1.getSelectedItem().toString().length() > 0){
            cboWeeklyOff2.setEnabled(true);
            observable.weeklyOff1Combo = cboWeeklyOff1.getSelectedItem().toString();
            observable.weeklyOff2Combo = cboWeeklyOff2.getSelectedItem().toString();
            observable.halfDay1Combo = cboHalfDay1.getSelectedItem().toString();
            observable.halfDay2Combo = cboHalfDay2.getSelectedItem().toString();
            observable.selectedCombo = 1;
            uniqueCombo = observable.checkComboUnique();
            if(!uniqueCombo){
                cboWeeklyOff1.setSelectedItem("");
                cboWeeklyOff2.setSelectedItem("");
                cboWeeklyOff2.setEnabled(false);
            }
        }else{
            cboWeeklyOff2.setSelectedItem("");
            cboWeeklyOff2.setEnabled(false);
        }
    }//GEN-LAST:event_cboWeeklyOff1ActionPerformed
    
    private void cboDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDateActionPerformed
        // Add your handling code here:
        observable.setCboDate(cboDate.getSelectedItem().toString());
    }//GEN-LAST:event_cboDateActionPerformed
    
    private void cboMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMonthActionPerformed
        // Add your handling code here:
        if ( cboMonth.getSelectedItem().toString().length() > 0){
            if(tblHolidayNames.getRowCount() > 0){
                observable.removeHolidayRow();
            }
            cboDate.setModel(new ComboBoxModel());
            getLeapYear();
            cboDate.setModel(observable.getCbmDate());
            tblHolidayNames.setModel(observable.getTblHolidays());
        }
    }//GEN-LAST:event_cboMonthActionPerformed
    
    private void btnYearIncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnYearIncActionPerformed
        // Add your handling code here:
        yearInc();
    }//GEN-LAST:event_btnYearIncActionPerformed
    
    private void rdoWeeklyOff_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoWeeklyOff_NoActionPerformed
        // Add your handling code here:
        if (rdoWeeklyOff_No.isSelected()){
            setComboEnableDisable(false);
        }
    }//GEN-LAST:event_rdoWeeklyOff_NoActionPerformed
    
    private void rdoWeeklyOff_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoWeeklyOff_YesActionPerformed
        // Add your handling code here:
        if (rdoWeeklyOff_Yes.isSelected()){
            updateOBFields();
            setComboEnableDisable(true);
        }
    }//GEN-LAST:event_rdoWeeklyOff_YesActionPerformed
    
    private void btnYearDecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnYearDecActionPerformed
        // Add your handling code here:
        yearDec();
    }//GEN-LAST:event_btnYearDecActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        observable.resetStatus();
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        observable.setTxtYear("");
        observable.resetStatus();
        super.removeEditLock(holidayId);
        observable.resetForm();
        observable.removeHolidayRow();
        setButtonEnableDisable();
        
        setYearEnableDisable();
        ClientUtil.enableDisable(this, false);
        viewType = -1;
        //__ Make the Screen Closable..
        setModified(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        //        objCalenderHolidaysRB = new CalenderHolidaysRB();
        String mandatoryHolidayListMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panHoliday);
        final String mandatoryWeklyOffMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panWeeklyOff);
        //        final String comboEmptyWarning = objCalenderHolidaysRB.getString("comboEmptyWarning");
        final String comboEmptyWarning = resourceBundle.getString("comboEmptyWarning");
        
        Date todaysDt = (Date)curDate.clone();
        int year = CommonUtil.convertObjToInt(txtYear.getText());
        int month = CommonUtil.convertObjToInt(((ComboBoxModel) cboMonth.getModel()).getKeyForSelected());
        int date = CommonUtil.convertObjToInt(cboDate.getSelectedItem());
        if (year > 0 && month > 0 && date > 0) {
            Date holidayDt = DateUtil.getDate(date, month, year);
            System.out.println("holidayDt :" + holidayDt);
            System.out.println("todaysDt :" + todaysDt);
            //--- check for "Selected date should not be Past & Today"
            if(DateUtil.dateDiff(curDate,holidayDt)<0){ //|| holidayDt.compareTo(todaysDt)==0){
                //---Alert the user
                mandatoryHolidayListMessage = mandatoryHolidayListMessage + resourceBundle.getString("todayOrPriorDateWarning");
            }
        }
        
        if (mandatoryHolidayListMessage.length() > 0 || mandatoryWeklyOffMessage.length() > 0){
            if(mandatoryHolidayListMessage.length() > 0 ){
                /** Check maditory fields for Holiday List **/
                displayAlert(mandatoryHolidayListMessage);
            }else if(mandatoryWeklyOffMessage.length() > 0){
                /** Check maditory fields for Weekly Off **/
                displayAlert(mandatoryWeklyOffMessage);
            }
        }else{
            if(rdoWeeklyOff_Yes.isSelected()){
                /** If YES is Selected in Weekly Off panel **/
                if((cboWeeklyOff1.getSelectedItem().toString().length() > 0) || (cboWeeklyOff2.getSelectedItem().toString().length() > 0) || (cboHalfDay1.getSelectedItem().toString().length() > 0) || (cboHalfDay2.getSelectedItem().toString().length() > 0)){
                    /** Atleast one of the combo is selected DO SAVE **/
                    saveAction();
                }else{
                    /** Check maditory combo fields for Weekly Off **/
                    displayAlert(comboEmptyWarning);
                }
            }else if(rdoWeeklyOff_No.isSelected()){
                /** If NO is selected DO SAVE **/
                saveAction();
            }
        }
        super.removeEditLock(holidayId);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        //        objCalenderHolidaysRB = new CalenderHolidaysRB();
        final String[] options = {resourceBundle.getString("cDialogOk"),resourceBundle.getString("cDialogCancel")};
        final int option = COptionPane.showOptionDialog(null, resourceBundle.getString("deleteWarning"), CommonConstants.WARNINGTITLE,
        COptionPane.OK_CANCEL_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        if (option == 0){
            observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
            observable.doAction();
            setButtonEnableDisable();
            
            setYearEnableDisable();
            observable.setTxtYear("");
            observable.resetForm();
            observable.removeHolidayRow();
            observable.setResultStatus();
            ClientUtil.enableDisable(panLeft, false);
    
        }
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        viewType = EDIT;
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        popUpItems(EDIT);
        comboDateSetModel();
        tblHolidayNames.setModel(observable.getTblHolidays());
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        boolean dataExists = false;
        dataExists = observable.checkWeeklyOff();
        if(dataExists){
            observable.populateWeeklyOff();
            if(observable.getRdoWeeklyOff_Yes() == true){
                ClientUtil.enableDisable(panWeek, true);
            }
        }
        setObservableYear();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        ClientUtil.enableDisable(panHoliday, true);
        ClientUtil.enableDisable(panWeeklyOff, true);
        setButtonEnableDisable();
  
        setYearEnableDisable();
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    private void btnCheck(){
         btnCancel.setEnabled(true);
         btnSave.setEnabled(false);
         btnNew.setEnabled(false);
         btnDelete.setEnabled(false);
         btnAuthorize.setEnabled(false);
         btnReject.setEnabled(false);
         btnException.setEnabled(false);
         btnEdit.setEnabled(false);
     }
    /* This method helps in popoualting the data from the data base  */
    private void popUpItems(int Action) {
        final HashMap viewMap = new HashMap();
        if (Action == EDIT || Action == DELETE || Action == VIEW){
            ArrayList lst = new ArrayList();
            lst.add("HOLIDAY_ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
        }
        ACTION=Action;
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        viewMap.put(CommonConstants.MAP_NAME, "ViewAllCalenderHolidaysTO");
        
        whereMap = null;
        
        new ViewAll(this, viewMap).show();
    }
    
    /* This method gets the CommonConstants.MAP_WHERE condition from the row clicked on the View All UI */
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        holidayId = "" ;
        boolean WeeklyDataExists = false;
        WeeklyDataExists = observable.checkWeeklyOff();
        if(WeeklyDataExists){
            observable.populateWeeklyOff();
            if(observable.getRdoWeeklyOff_Yes() == true){
                ClientUtil.enableDisable(panWeek, true);
            }
        }
        ClientUtil.enableDisable(panHoliday, true);
        ClientUtil.enableDisable(panWeeklyOff, true);
        //        hash.put(CommonConstants.MAP_WHERE, hash.get("HOLIDAY_ID"));
        observable.setHolidayID(CommonUtil.convertObjToStr(hash.get("HOLIDAY_ID")));
        holidayId = CommonUtil.convertObjToStr(hash.get("HOLIDAY_ID"));
        isFilled = true;
        observable.setStatus();
        observable.populateData(hash);
        setButtonEnableDisable();
        setDelBtnEnableDisable(true);
        setYearEnableDisable();
        
    	Date currDt = (Date)curDate.clone();;
        Date preDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hash.get("HOLIDAY DATE")));
        long dateDiff= 0;
        if (currDt != null && preDt != null) {
            dateDiff = DateUtil.dateDiff(preDt, currDt);
        }
        if(dateDiff>0)
            ClientUtil.enableDisable(panLeft,false);
        txtYear.setEnabled(false);
        cboMonth.setEnabled(true);
        rdoWeeklyOff_No.setSelected(true);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
        
        //--- disable the fields if it is Authorize
        if(viewType == AUTHORIZE || viewType == VIEW){
            ClientUtil.enableDisable(panLeft, false);
            btnDelete.setEnabled(false);
            btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                 btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                 btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            
        } else if(dateDiff>0){
            ClientUtil.enableDisable(panLeft, false);
            btnYearDec.setEnabled(false);
            btnYearInc.setEnabled(false);
            btnDelete.setEnabled(true);
        }
        else{
        ClientUtil.enableDisable(panLeft, true);
            btnDelete.setEnabled(true);
        }
    }
    
    /**  Checks the Leap Year and accordingly set the date **/
    private void getLeapYear(){
        if ( cboMonth.getSelectedItem().toString().length() > 0){
            month = Integer.parseInt(observable.getCbmMonth().getKeyForSelected().toString());
        }
        if( month == 1  || month == 3  || month == 5  || month == 7  || month == 8  || month == 10  || month == 12  ){
            observable.setComboDateSetModel(31);
        }else{
            if(month == 4  || month == 6  || month == 9  || month == 11  ){
                observable.setComboDateSetModel(30);
            }else{
                /** Check for the Leap Year **/
                
                checkLeapYear();
            }
        }
        resetText();
        final HashMap viewMap = new HashMap();
        HashMap hash = new HashMap();
        boolean dataExist = false;
        if(cboMonth.getSelectedItem().toString().length() > 0){
            dataExist = observable.setHolidayTableModel();
            observable.setCboMonth((String) cboMonth.getSelectedItem());
            if( dataExist == false){
                observable.setTableModel();
                cboDate.setSelectedItem("");
            }
        }else{
            observable.setCboMonth((String) cboMonth.getSelectedItem());
        }
    }
    
    /** Increment the Year by 1 **/
    private void yearInc(){
        txtYear.setText(String.valueOf(Integer.parseInt(txtYear.getText()) + 1));
        observable.setTxtYear(txtYear.getText());
        observable.resetFields();
        observable.removeHolidayRow();
        cboDate.setModel(new ComboBoxModel());
    }
    
    /** Decrement the Year by 1 **/
    private void yearDec(){
        txtYear.setText(String.valueOf(Integer.parseInt(txtYear.getText()) - 1));
        observable.setTxtYear(txtYear.getText());
        observable.resetFields();
        observable.removeHolidayRow();
        cboDate.setModel(new ComboBoxModel());
    }
    
    /** Sets the Year to the obersevable object **/
    private void setObservableYear(){
        currentYear = (Date)curDate.clone(); //new GregorianCalendar();
        txtYear.setText(String.valueOf(currentYear.getYear() + 1900));
        observable.setTxtYear(txtYear.getText().toString());
    }
    
    /** Gets the date from the CTable to comaper with the date entered  **/
    private void getDateValue(){
        String stringSplit[] = columnDate.split(dateSeparator);
        compareDate = new StringBuffer();
        //integerSplit = new Integer(Integer.parseInt(stringSplit[0]));
        integerSplit = CommonUtil.convertObjToInt(stringSplit[0]);
        dateValue = integerSplit.intValue();
        if (dateValue > 0) {
            compareDate.append(dateValue);
        }
        compareDate.append(dateSeparator);
        //integerSplit = new Integer(Integer.parseInt(stringSplit[1]));
        integerSplit = CommonUtil.convertObjToInt(stringSplit[1]);
        dateValue = integerSplit.intValue();
        compareDate.append(dateValue);
        compareDate.append(dateSeparator);
        //integerSplit = new Integer(Integer.parseInt(stringSplit[2]));
        integerSplit = CommonUtil.convertObjToInt(stringSplit[2]);
        dateValue= integerSplit.intValue();
        compareDate.append(dateValue);
        tableData.add(compareDate.toString());
    }
    
    //    private void getDateValue(){
    //        String stringSplit[] = columnDate.split(dateSeparator);
    //        compareDate = new StringBuffer();
    //
    //        integerSplit = new Integer(Integer.parseInt(stringSplit[1]));
    //        dateValue = integerSplit.intValue();
    //        compareDate.append(dateValue);
    //
    //        compareDate.append(dateSeparator);
    //
    //
    //        integerSplit = new Integer(Integer.parseInt(stringSplit[0]));
    //        dateValue = integerSplit.intValue();
    //        compareDate.append(dateValue);
    //
    //        compareDate.append(dateSeparator);
    //
    //        integerSplit = new Integer(Integer.parseInt(stringSplit[2]));
    //        dateValue= integerSplit.intValue();
    //        compareDate.append(dateValue);
    //
    //
    //        tableData.add(compareDate.toString());
    //    }
    
    
    public void saveAction(){
        tableRow = tblHolidayNames.getRowCount();
        arrayListTableDate = new ArrayList();
        updateOBFields();
        if(tableRow != 0 && (ACTION == EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_NEW )){
            /** If there are datas in Table i.e table row != null **/
            enableButton = 1;
            for(int i=0;i<tableRow;i++){
                tableData = new ArrayList();
                columnDate =  new String();
                columnDate = (String)tblHolidayNames.getValueAt(i,0);
                getDateValue();
                arrayListTableDate.add(tableData);
                tableData = null;
            }
            observable.tableInsertUpdate(arrayListTableDate);
            getEditperformed = observable.doCancel();
            if(getEditperformed != 1){
                setButtonEnableDisable();
                setYearEnableDisable();
            }
        }else{
            /** If there are no datas in Table i.e table row == null **/
            enableButton = 2;
            updateOBFields();
            observable.doAction();
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                observable.resetForm();
                observable.removeHolidayRow();
                observable.setResultStatus();
                setButtonEnableDisable();
                setYearEnableDisable();
                ClientUtil.enableDisable(this, false);
            }
        }
        getEditperformed = observable.doCancel();
        if(enableButton == 1 && getEditperformed == 1){
            /** Enable Disable for Cancel option  **/
            setYearEnableDisable();
            ClientUtil.enableDisable(panWeek, false);
            ClientUtil.enableDisable(panHoliday, true);
            ClientUtil.enableDisable(panWeeklyOff, true);
            if(observable.getRdoWeeklyOff_Yes() == true){
                ClientUtil.enableDisable(panWeek, true);
            }
        }else{
            /** Enable Disable for No option  **/
            ClientUtil.enableDisable(this, false);
        }
        if(getEditperformed != 1){
            observable.setTxtYear("");
            txtYear.setText(observable.getTxtYear());
        }
        lblStatus.setText(observable.getLblStatus());
        
        //__ Make the Screen Closable..
        setModified(false);
    }
    
    /** Set feb month accordingly to the Leap Year **/
    public void checkLeapYear(){
        if ( txtYear.getText() != null && txtYear.getText().length() != 0){
            leapYear = Integer.parseInt(txtYear.getText());
        }
        if( ( leapYear%100 != 0 && leapYear%4 == 0) || ( leapYear%400 == 0 )){
            if( month == 2){
                /** Leap Year **/
                observable.setComboDateSetModel(29);
            }
        }else{
            if( month == 2){
                /** Non Leap Year **/
                observable.setComboDateSetModel(28);
            }
        }
    }
    
    /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setRdoWeeklyOff_Yes(rdoWeeklyOff_Yes.isSelected());
        observable.setRdoWeeklyOff_No(rdoWeeklyOff_No.isSelected());
        observable.setCboWeeklyOff1((String) cboWeeklyOff1.getSelectedItem());
        observable.setCboWeeklyOff2((String) cboWeeklyOff2.getSelectedItem());
        observable.setCboHalfDay1((String) cboHalfDay1.getSelectedItem());
        observable.setCboHalfDay2((String) cboHalfDay2.getSelectedItem());
        observable.setCboMonth((String) cboMonth.getSelectedItem());
        observable.setCboDate((String) cboDate.getSelectedItem());
        observable.setTxtHolidayName(txtHolidayName.getText());
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setTxtYear(txtYear.getText());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
    }
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    /** Method to set the model of Combo Date */
    private void comboDateSetModel(){
        cboDate.setModel(observable.getCbmDate());
        cboDate.setSelectedItem(observable.getCboDate());
    }
    
    /** Method to reset the text field of Year */
    public void resetYear(){
        txtYear.setText("");
    }
    
    /** Method to reset the text fields of Holiday List */
    public void resetText(){
        txtHolidayName.setText("");
        txtRemarks.setText("");
    }
    
    /** Method to reset the Holiday List Tab to null (Remove all the rows) */
    private void resetTable() {
        observable.removeHolidayRow();
        tblHolidayNames.setModel(new javax.swing.table.DefaultTableModel(0,0));
    }
    
    /** Method to Enable / Disable Year Button */
    private void setYearEnableDisable(){
        btnYearInc.setEnabled(!btnNew.isEnabled());
        btnYearDec.setEnabled(!btnNew.isEnabled());
        txtYear.setEnabled(btnNew.isEnabled());
        txtYear.setEditable(btnNew.isEnabled());
    }
    
    /** Method to Enable / Disable combo fields of Weekly Off */
    private void setComboEnableDisable(boolean value){
        /** If YES / NO is selected in the WeeklyOff ( Enable / Disable )*/
        cboWeeklyOff1.setEnabled(value);
        cboWeeklyOff2.setEnabled(value);
        cboHalfDay1.setEnabled(value);
        cboHalfDay2.setEnabled(value);
        if (rdoWeeklyOff_Yes.isSelected()){
            cboWeeklyOff2.setEnabled(!value);
            cboHalfDay2.setEnabled(!value);
        }
        if(!value){
            /** If NO is selected in the WeeklyOff (Reset to null) */
            cboWeeklyOff1.setSelectedItem("");
            cboWeeklyOff2.setSelectedItem("");
            cboHalfDay1.setSelectedItem("");
            cboHalfDay2.setSelectedItem("");
        }
        
    }
    
    /** To Enable or Disable New, Edit, Save and Cancel Button */
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        btnSave.setEnabled(btnCancel.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
        setDelBtnEnableDisable(false);
    }
    
    /** To Enable or Disable Delete Button */
    private void setDelBtnEnableDisable(boolean enableDisable){
        btnDelete.setEnabled(enableDisable );
        mitDelete.setEnabled(enableDisable);
    }
    
    
    /**
     * @param args the command line arguments
     */
   /* public static void main(String args[]) {
        new CalenderHolidaysUI().show();
    }
    */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnYearDec;
    private com.see.truetransact.uicomponent.CButton btnYearInc;
    private com.see.truetransact.uicomponent.CComboBox cboDate;
    private com.see.truetransact.uicomponent.CComboBox cboHalfDay1;
    private com.see.truetransact.uicomponent.CComboBox cboHalfDay2;
    private com.see.truetransact.uicomponent.CComboBox cboMonth;
    private com.see.truetransact.uicomponent.CComboBox cboWeeklyOff1;
    private com.see.truetransact.uicomponent.CComboBox cboWeeklyOff2;
    private com.see.truetransact.uicomponent.CLabel lblDate;
    private com.see.truetransact.uicomponent.CLabel lblHalfDay1;
    private com.see.truetransact.uicomponent.CLabel lblHalfDay2;
    private com.see.truetransact.uicomponent.CLabel lblHoliday;
    private com.see.truetransact.uicomponent.CLabel lblMonth;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace18;
    private com.see.truetransact.uicomponent.CLabel lblSpace19;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace20;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblWeeklyOff;
    private com.see.truetransact.uicomponent.CLabel lblWeeklyOff1;
    private com.see.truetransact.uicomponent.CLabel lblWeeklyOff2;
    private com.see.truetransact.uicomponent.CLabel lblYear;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panHoliday;
    private com.see.truetransact.uicomponent.CPanel panLeft;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panWeek;
    private com.see.truetransact.uicomponent.CPanel panWeeklyOff;
    private com.see.truetransact.uicomponent.CPanel panYear;
    private com.see.truetransact.uicomponent.CPanel panYearIncDec;
    private com.see.truetransact.uicomponent.CButtonGroup rdgActivity;
    private com.see.truetransact.uicomponent.CRadioButton rdoWeeklyOff_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoWeeklyOff_Yes;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CSeparator sptMonth;
    private com.see.truetransact.uicomponent.CScrollPane srpTable;
    private com.see.truetransact.uicomponent.CTable tblHolidayNames;
    private javax.swing.JToolBar tbrMain;
    private com.see.truetransact.uicomponent.CTextField txtHolidayName;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtYear;
    // End of variables declaration//GEN-END:variables
}