/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MalayalamDetailsUI.java
 *
 * Created on January 20, 2005, 3:03 PM
 */
package com.see.truetransact.ui.common.customer.malayalam;

/**
 * Author   : Chithra
 * Location : Thrissur
 * Date of Completion : 14-05-2015
 */
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CTextField;

import java.util.Observable;
import java.util.Observer;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.EngToMalTransliterator;
import com.see.truetransact.ui.common.viewall.MalayalamDicSearchUI;
import com.see.truetransact.ui.common.viewall.MalayalamKeyboardUI;
import com.see.truetransact.uicomponent.CInternalFrame;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MalayalamDetailsUI extends CInternalFrame implements Observer, UIMandatoryField {

    /**
     * Vairable Declarations
     */
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.common.customer.malayalam.MalayalamDetailsRB", ProxyParameters.LANGUAGE);
    //Creating Instance For ResourceBundle-TokenConfigRB
    private MalayalamDetailsOB observable; //Reference for the Observable Class TokenConfigOB
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final String AUTHORIZE = "Authorize";//Variable used when btnAuthorize is clicked
    final int FIXEDDEPOSIT = 2;
    private HashMap ismCodeMap;
    private String shareType = "";
    private String txtEngValue = "";
    private int count = 0;
    private ArrayList mal_List = new ArrayList();

    /**
     * Creates new form TokenConfigUI
     */
    public MalayalamDetailsUI() {
        initForm();
    }

    /**
     * Method which is used to initialize the form TokenConfig
     */
    private void initForm() {
        initComponents();
        setFieldNames();
        setObservable();
        observable.resetForm();
        initComponentData();
        setMandatoryHashMap();
        setMaxLengths();
        setButtonEnableDisable();
        ismCodeMap = (HashMap) getIsmCode();
        setEnableDisableFileds(false);
    }

    /*
     * Auto Generated Method - setFieldNames() This method assigns name for all
     * the components. Other functions are working based on this name.
     */
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
        lbSpace2.setName("lbSpace2");
        lblMsg.setName("lblMsg");
        lblSpace1.setName("lblSpace1");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        mbrTokenConfig.setName("mbrTokenConfig");
        panStatus.setName("panStatus");

    }

    /*
     * Auto Generated Method - internationalize() This method used to assign
     * display texts from the Resource Bundle File.
     */
    private void internationalize() {
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lbSpace2.setText(resourceBundle.getString("lbSpace2"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
    }

    /**
     * Adds up the Observable to this form
     */
    private void setObservable() {
        try {
            observable = MalayalamDetailsOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /*
     * Setting model to the combobox cboTokenType
     */
    private void initComponentData() {
        try {
        } catch (ClassCastException e) {
            parseException.logException(e, true);
        }
    }

    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {

        lblStatus.setText(observable.getLblStatus());
        txtMemNum.setText(observable.getTxtMemNo());
        txtMemType.setText("A Class");
        txtRegName.setText(observable.getTxtRegName());
        txtRegHname.setText(observable.getTxtRegHname());
        txtRegPlace.setText(observable.getTxtRegPlace());
        txtRegvillage.setText(observable.getTxtRegvillage());
        txtRegGardName.setText(observable.getTxtRegGardName());
        txtRegAmsam.setText(observable.getTxtRegAmsam());
        txtRegDesam.setText(observable.getTxtRegDesam());
        if (observable.getTxtRegMName() != null && observable.getTxtRegMName().length() > 0) {
            txtRegMName.setText(observable.getTxtRegMName());
        } else {
            txtRegMName.setText(getwordFromDict(txtRegName.getText()));
        }
        if (observable.getTxtRegMaHname() != null && observable.getTxtRegMaHname().length() > 0) {
            txtRegMaHname.setText(observable.getTxtRegMaHname());
        } else {
            txtRegMaHname.setText(getwordFromDict(txtRegHname.getText()));
        }
        if (observable.getTxtRegMaPlace() != null && observable.getTxtRegMaPlace().length() > 0) {
            txtRegMaPlace.setText(observable.getTxtRegMaPlace());
        } else {
            txtRegMaPlace.setText(getwordFromDict(txtRegPlace.getText()));
        }
        if (observable.getTxtRegMavillage() != null && observable.getTxtRegMavillage().length() > 0) {
            txtRegMaVillage.setText(observable.getTxtRegMavillage());
        } else {
            txtRegMaVillage.setText(getwordFromDict(txtRegvillage.getText()));
        }
        if (observable.getTxtRegMaAmsam() != null && observable.getTxtRegMaAmsam().length() > 0) {
            txtRegMaAmsam.setText(observable.getTxtRegMaAmsam());
        } else {
            txtRegMaAmsam.setText(getwordFromDict(txtRegAmsam.getText()));
        }
        if (observable.getTxtRegMaDesam() != null && observable.getTxtRegMaDesam().length() > 0) {
            txtRegMaDesam.setText(observable.getTxtRegMaDesam());
        } else {
            txtRegMaDesam.setText(getwordFromDict(txtRegDesam.getText()));
        }
        if (observable.getTxtRegMaGardName() != null && observable.getTxtRegMaGardName().length() > 0) {
            txtRegMaGardName.setText(observable.getTxtRegMaGardName());
        } else {
            txtRegMaGardName.setText(getwordFromDict(txtRegGardName.getText()));
        }

    }

    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    public void updateOBFields() {
        observable.setScreen(getScreen());
        observable.setTxtRegMName(txtRegMName.getText());
        observable.setTxtMemNo(txtMemNum.getText());
        observable.setTxtRegMaHname(txtRegMaHname.getText());
        observable.setTxtRegMaPlace(txtRegMaPlace.getText());
        observable.setTxtRegMavillage(txtRegMaVillage.getText());
        observable.setTxtRegMaAmsam(txtRegMaAmsam.getText());
        observable.setTxtRegMaDesam(txtRegMaDesam.getText());
        observable.setTxtRegMaGardName(txtRegMaGardName.getText());
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
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtwefDate", new Boolean(true));
        mandatoryMap.put("txtTaxRate", new Boolean(false));
        mandatoryMap.put("txtEduCess", new Boolean(true));
        mandatoryMap.put("txthigherCess", new Boolean(true));
        mandatoryMap.put("txttaxHeadId", new Boolean(true));
    }

    /*
     * Auto Generated Method - getMandatoryHashMap() Getter method for setMandatoryHashMap().
     */
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /*
     * Auto Generated Method - setHelpMessage() This method shows tooltip help
     * for all the input fields available in the UI. It needs the Mandatory
     * Resource Bundle object. Help display Label name should be lblMsg.
     */
    public void setHelpMessage() {
    }

    /**
     * Used to set Maximum possible lenghts for TextFields
     */
    private void setMaxLengths() {
    }

    /*
     * Makes the button Enable or Disable accordingly when usier clicks new,edit
     * or delete buttons
     */
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
        lblStatus.setText(observable.getLblStatus());
        btnView.setEnabled(!btnView.isEnabled());
    }

    private void setEnableDisableFileds(boolean flag) {
        txtMemType.setEnabled(flag);
        txtMemNum.setEnabled(flag);
        txtMemType.setEnabled(flag);
        txtRegName.setEnabled(flag);
        txtRegHname.setEnabled(flag);
        txtRegPlace.setEnabled(flag);
        txtRegvillage.setEnabled(flag);
        txtRegGardName.setEnabled(flag);
        txtRegAmsam.setEnabled(flag);
        txtRegDesam.setEnabled(flag);
        txtRegMName.setEnabled(flag);
        txtRegMaHname.setEnabled(flag);
        txtRegMaPlace.setEnabled(flag);
        txtRegMaVillage.setEnabled(flag);
        txtRegMaGardName.setEnabled(flag);
        txtRegMaAmsam.setEnabled(flag);
        txtRegMaDesam.setEnabled(flag);
    }

    /**
     * Method used to check whether the Mandatory Fields in the Form are Filled
     * or not
     */
    private String checkMandatory(javax.swing.JComponent component) {
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }

    /**
     * Method used to Give a Alert when any Mandatory Field is not filled by the
     * user
     */
    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

    /*
     * Calls the execute method of TerminalOB to do insertion or updation or
     * deletion
     */
    private void saveAction(String status) {
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed


        System.out.println("status============" + status);
        observable.execute(status);
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
            lst.add("MEM_NUM");
            lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
            if (observable.getProxyReturnMap() != null) {
                if (observable.getProxyReturnMap().containsKey("MEM_NUM")) {
                    lockMap.put("MEM_NUM", observable.getProxyReturnMap().get("MEM_NUM"));
                }
            }
            if (status == CommonConstants.TOSTATUS_UPDATE) {
                //          lockMap.put("CONFIG_ID", observable.getTxtServiceTaxId());
            }
            setEditLockMap(lockMap);
            setEditLock();
        }

    }

    /*
     * set the screen after the updation,insertion, deletion
     */
    private void settings() {
        observable.resetForm();
        ClientUtil.clearAll(this);
        setButtonEnableDisable();
        observable.setResultStatus();
    }

    /*
     * Does necessary operaion when user clicks the save button
     */
    private void savePerformed() {
        updateOBFields();
        String action;
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            action = CommonConstants.TOSTATUS_INSERT;
            saveAction(action);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            action = CommonConstants.TOSTATUS_UPDATE;
            saveAction(action);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            action = CommonConstants.TOSTATUS_DELETE;
            saveAction(action);
        }
    }

    /*
     * Method used to showPopup ViewAll by Executing a Query
     */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        ArrayList lst = new ArrayList();
        viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
        viewMap.put(CommonConstants.MAP_NAME, "getCustomerDetails");
        HashMap where = new HashMap();
        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        viewMap.put(CommonConstants.MAP_WHERE, where);
        where = null;
        new ViewAll(this, viewMap).show();
    }

    /*
     * Fills up the HashMap with data when user selects the row in ViewAll
     * screen
     */
    public void fillData(Object map) {
        setModified(true);
        HashMap hash = (HashMap) map;
        System.out.println("fillData  : " + hash);
        final String ACCOUNTHEAD = (String) hash.get("ACCOUNT HEAD");
        final String ACCOUNTHEADDESC = (String) hash.get("ACCOUNT HEAD DESCRIPTION");
        if (viewType != null) {
            if (viewType.equals("New")
                    || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                HashMap where = new HashMap();
                where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                where.put("MEM_NUM", txtMemNum.getText());
                hash.put(CommonConstants.MAP_WHERE, where);
                observable.getData(hash);
                update(observable, map);
                if (viewType.equals(AUTHORIZE)) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
                setButtonEnableDisable();
                if (viewType.equals("New")) {
                    btnSave.setEnabled(true);
                    btnCancel.setEnabled(true);
                }
            }

        }

    }

    /**
     * Method used to fill up the TextFiled txtNoofTokens
     */
    /**
     * Method called when txtSeriesNo focus is lost to check for duplication of
     * SeriesNo
     */
    private void seriesCheck(String tokenType, String seriesNo) {
    }

    /**
     * This will show the alertwindow when the user enters the already existing
     * ShareType *
     */
    private int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {resourceBundle.getString("cDialogOK")};
        optionSelected = COptionPane.showOptionDialog(null, resourceBundle.getString(alertMsg), CommonConstants.INFORMATIONTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        return optionSelected;
    }

    private void checkTokenNo(CTextField tokenNo) {
        int number = Integer.parseInt(tokenNo.getText());
        if (number <= 0) {
            ClientUtil.showAlertWindow(resourceBundle.getString("tokenNoMsg"));
            tokenNo.setText("");
        }
    }

    private void popUp(int field) {
        final ViewAll objViewAll;
        final HashMap viewMap = new HashMap();
        //  viewType = field;

        //     updateOBFields();
        HashMap whereMap = new HashMap();

        if (field == FIXEDDEPOSIT) {
        }
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        viewMap.put(CommonConstants.MAP_NAME, "product.deposits.getAcctHeadList");
        objViewAll = new ViewAll(this, viewMap);
        //  objViewAll.setTitle(resourceBundle.getString("acHdTitle"));
        objViewAll.show();
    }

    /**
     * Method used to do Required operation when user clicks
     * btnAuthorize,btnReject or btnReject *
     */
    public void authorizeStatus(String authorizeStatus) {
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrTokenConfig = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lbSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace11 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace12 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace13 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace14 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace15 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace16 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panDetails = new com.see.truetransact.uicomponent.CPanel();
        txtRegName = new com.see.truetransact.uicomponent.CTextField();
        lblRegName = new com.see.truetransact.uicomponent.CLabel();
        txtRegHname = new com.see.truetransact.uicomponent.CTextField();
        lblRegHname = new com.see.truetransact.uicomponent.CLabel();
        lblRegPlace = new com.see.truetransact.uicomponent.CLabel();
        txtRegPlace = new com.see.truetransact.uicomponent.CTextField();
        lblRegGardName = new com.see.truetransact.uicomponent.CLabel();
        txtRegGardName = new com.see.truetransact.uicomponent.CTextField();
        txtRegDesam = new com.see.truetransact.uicomponent.CTextField();
        txtRegAmsam = new com.see.truetransact.uicomponent.CTextField();
        lblRegAmsam = new com.see.truetransact.uicomponent.CLabel();
        lblRegDesam = new com.see.truetransact.uicomponent.CLabel();
        lblRegVillage = new com.see.truetransact.uicomponent.CLabel();
        txtRegvillage = new com.see.truetransact.uicomponent.CTextField();
        txtRegMaGardName = new com.see.truetransact.uicomponent.CTextField();
        txtRegMaDesam = new com.see.truetransact.uicomponent.CTextField();
        txtRegMaAmsam = new com.see.truetransact.uicomponent.CTextField();
        txtRegMaVillage = new com.see.truetransact.uicomponent.CTextField();
        txtRegMaPlace = new com.see.truetransact.uicomponent.CTextField();
        txtRegMName = new com.see.truetransact.uicomponent.CTextField();
        txtRegMaHname = new com.see.truetransact.uicomponent.CTextField();
        txtMemType = new com.see.truetransact.uicomponent.CTextField();
        lblMemNo = new com.see.truetransact.uicomponent.CLabel();
        txtMemNum = new com.see.truetransact.uicomponent.CTextField();
        btnNext = new com.see.truetransact.uicomponent.CButton();
        chktxt = new com.see.truetransact.uicomponent.CTextField();
        mbrTokenConfig = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptView = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(529, 251));

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
        tbrTokenConfig.add(btnView);

        lbSpace3.setText("     ");
        tbrTokenConfig.add(lbSpace3);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnNew);

        lblSpace11.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace11.setText("     ");
        lblSpace11.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace11);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnEdit);

        lblSpace12.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace12.setText("     ");
        lblSpace12.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace12);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnDelete);

        lbSpace2.setText("     ");
        tbrTokenConfig.add(lbSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnSave);

        lblSpace13.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace13.setText("     ");
        lblSpace13.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace13);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnCancel);

        lblSpace3.setText("     ");
        tbrTokenConfig.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        tbrTokenConfig.add(btnAuthorize);

        lblSpace14.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace14.setText("     ");
        lblSpace14.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace14);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnException);

        lblSpace15.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace15.setText("     ");
        lblSpace15.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace15);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        tbrTokenConfig.add(btnReject);

        lblSpace5.setText("     ");
        tbrTokenConfig.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrTokenConfig.add(btnPrint);

        lblSpace16.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace16.setText("     ");
        lblSpace16.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace16.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace16.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace16);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnClose);

        getContentPane().add(tbrTokenConfig, java.awt.BorderLayout.NORTH);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

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

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        panDetails.setLayout(new java.awt.GridBagLayout());

        txtRegName.setAllowAll(true);
        txtRegName.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        txtRegName.setMinimumSize(new java.awt.Dimension(300, 21));
        txtRegName.setPreferredSize(new java.awt.Dimension(300, 21));
        txtRegName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRegNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panDetails.add(txtRegName, gridBagConstraints);

        lblRegName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panDetails.add(lblRegName, gridBagConstraints);

        txtRegHname.setAllowAll(true);
        txtRegHname.setMinimumSize(new java.awt.Dimension(300, 21));
        txtRegHname.setPreferredSize(new java.awt.Dimension(300, 21));
        txtRegHname.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRegHnameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panDetails.add(txtRegHname, gridBagConstraints);

        lblRegHname.setText("House Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panDetails.add(lblRegHname, gridBagConstraints);

        lblRegPlace.setText("Place");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panDetails.add(lblRegPlace, gridBagConstraints);

        txtRegPlace.setAllowAll(true);
        txtRegPlace.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRegPlace.setPreferredSize(new java.awt.Dimension(200, 21));
        txtRegPlace.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRegPlaceFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panDetails.add(txtRegPlace, gridBagConstraints);

        lblRegGardName.setText("Guardian Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panDetails.add(lblRegGardName, gridBagConstraints);

        txtRegGardName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRegGardName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtRegGardName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRegGardNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panDetails.add(txtRegGardName, gridBagConstraints);

        txtRegDesam.setAllowAll(true);
        txtRegDesam.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRegDesam.setPreferredSize(new java.awt.Dimension(200, 21));
        txtRegDesam.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRegDesamFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panDetails.add(txtRegDesam, gridBagConstraints);

        txtRegAmsam.setAllowAll(true);
        txtRegAmsam.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRegAmsam.setPreferredSize(new java.awt.Dimension(200, 21));
        txtRegAmsam.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRegAmsamFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panDetails.add(txtRegAmsam, gridBagConstraints);

        lblRegAmsam.setText("Amsam");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panDetails.add(lblRegAmsam, gridBagConstraints);

        lblRegDesam.setText("Desam");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panDetails.add(lblRegDesam, gridBagConstraints);

        lblRegVillage.setText("Village");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panDetails.add(lblRegVillage, gridBagConstraints);

        txtRegvillage.setAllowAll(true);
        txtRegvillage.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRegvillage.setPreferredSize(new java.awt.Dimension(200, 21));
        txtRegvillage.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRegvillageFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        panDetails.add(txtRegvillage, gridBagConstraints);

        txtRegMaGardName.setAllowAll(true);
        txtRegMaGardName.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        txtRegMaGardName.setMinimumSize(new java.awt.Dimension(450, 25));
        txtRegMaGardName.setPreferredSize(new java.awt.Dimension(450, 25));
        txtRegMaGardName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRegMaGardNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRegMaGardNameKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        panDetails.add(txtRegMaGardName, gridBagConstraints);

        txtRegMaDesam.setAllowAll(true);
        txtRegMaDesam.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        txtRegMaDesam.setMinimumSize(new java.awt.Dimension(450, 25));
        txtRegMaDesam.setPreferredSize(new java.awt.Dimension(450, 25));
        txtRegMaDesam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRegMaDesamKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRegMaDesamKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        panDetails.add(txtRegMaDesam, gridBagConstraints);

        txtRegMaAmsam.setAllowAll(true);
        txtRegMaAmsam.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        txtRegMaAmsam.setMinimumSize(new java.awt.Dimension(450, 25));
        txtRegMaAmsam.setPreferredSize(new java.awt.Dimension(450, 25));
        txtRegMaAmsam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRegMaAmsamKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRegMaAmsamKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        panDetails.add(txtRegMaAmsam, gridBagConstraints);

        txtRegMaVillage.setAllowAll(true);
        txtRegMaVillage.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        txtRegMaVillage.setMinimumSize(new java.awt.Dimension(450, 25));
        txtRegMaVillage.setPreferredSize(new java.awt.Dimension(450, 25));
        txtRegMaVillage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRegMaVillageKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRegMaVillageKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        panDetails.add(txtRegMaVillage, gridBagConstraints);

        txtRegMaPlace.setAllowAll(true);
        txtRegMaPlace.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        txtRegMaPlace.setMinimumSize(new java.awt.Dimension(450, 25));
        txtRegMaPlace.setPreferredSize(new java.awt.Dimension(450, 25));
        txtRegMaPlace.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRegMaPlaceKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRegMaPlaceKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        panDetails.add(txtRegMaPlace, gridBagConstraints);

        txtRegMName.setAllowAll(true);
        txtRegMName.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        txtRegMName.setMinimumSize(new java.awt.Dimension(450, 25));
        txtRegMName.setPreferredSize(new java.awt.Dimension(450, 25));
        txtRegMName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRegMNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRegMNameKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        panDetails.add(txtRegMName, gridBagConstraints);

        txtRegMaHname.setAllowAll(true);
        txtRegMaHname.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        txtRegMaHname.setMinimumSize(new java.awt.Dimension(450, 25));
        txtRegMaHname.setPreferredSize(new java.awt.Dimension(450, 25));
        txtRegMaHname.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRegMaHnameFocusLost(evt);
            }
        });
        txtRegMaHname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRegMaHnameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRegMaHnameKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        panDetails.add(txtRegMaHname, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        panDetails.add(txtMemType, gridBagConstraints);

        lblMemNo.setText("Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panDetails.add(lblMemNo, gridBagConstraints);

        txtMemNum.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMemNumFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        panDetails.add(txtMemNum, gridBagConstraints);

        btnNext.setText("Next");
        btnNext.setMaximumSize(new java.awt.Dimension(70, 27));
        btnNext.setMinimumSize(new java.awt.Dimension(70, 27));
        btnNext.setPreferredSize(new java.awt.Dimension(70, 27));
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panDetails.add(btnNext, gridBagConstraints);

        chktxt.setAllowAll(true);
        chktxt.setFont(new java.awt.Font("ML-TTIndulekha", 0, 18)); // NOI18N
        chktxt.setMinimumSize(new java.awt.Dimension(400, 24));
        chktxt.setPreferredSize(new java.awt.Dimension(400, 24));
        chktxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                chktxtKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panDetails.add(chktxt, gridBagConstraints);

        getContentPane().add(panDetails, java.awt.BorderLayout.CENTER);

        mnuProcess.setText("Process");

        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptView);

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

        mbrTokenConfig.add(mnuProcess);

        setJMenuBar(mbrTokenConfig);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView(ClientConstants.ACTION_STATUS[17]);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        setModified(false);
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        ///   cifClosingAlert();
    }//GEN-LAST:event_mitCloseActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        setModified(false);
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        // if(observable.getAuthorizeStatus()!=null)
        setModified(false);
        observable.resetForm();
        ClientUtil.clearAll(this);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        setButtonEnableDisable();
        viewType = "";
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        panDetails.setEnabled(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView(ClientConstants.ACTION_STATUS[3]);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        setModified(false);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
        }
        if (observable.getTxtRegCustId() == null || observable.getTxtRegCustId().length() <= 0) {
            ClientUtil.displayAlert("Invalid Member No:");
            return;
        }
        savePerformed();

        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnCancelActionPerformed(null);
        btnNewActionPerformed(null);
    }//GEN-LAST:event_btnSaveActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.resetForm();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        panDetails.setEnabled(true);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        setEnableDisableFileds(true);
        setValues();
    }//GEN-LAST:event_btnNewActionPerformed
    private void setValues() {
        HashMap where = new HashMap();
        List countList = ClientUtil.executeQuery("getLastMemNoFromCustRegional", new HashMap());
        if (countList != null && countList.size() > 0) {
            HashMap hmap = (HashMap) countList.get(0);
            if (hmap != null && hmap.size() > 0) {
                String shareNo = CommonUtil.convertObjToStr(hmap.get("SHARE_TYPE")) + (CommonUtil.convertObjToInt(hmap.get("SHARE_NO")) + 1);
                shareType = CommonUtil.convertObjToStr(hmap.get("SHARE_TYPE"));
                txtMemNum.setText(shareNo);
            }
        } else {
            final List objList = ClientUtil.executeQuery("getFirstMemNoForRegional", new HashMap());
            if (objList != null && objList.size() > 0) {
                HashMap hmap = (HashMap) objList.get(0);
                if (hmap != null && hmap.size() > 0) {
                    String shareNo = CommonUtil.convertObjToStr(hmap.get("SHARE_TYPE")) + CommonUtil.convertObjToStr(hmap.get("SHARE_NO"));
                    shareType = CommonUtil.convertObjToStr(hmap.get("SHARE_TYPE"));
                    txtMemNum.setText(shareNo);
                }
            }
        }
        observable.setTxtMemNo(txtMemNum.getText());
        if (txtMemNum.getText() != null && CommonUtil.convertObjToStr(txtMemNum.getText()).length() > 0) {
            viewType = "New";
            HashMap hMap = new HashMap();
            hMap.put("MEM_NUM", txtMemNum.getText());
            fillData(hMap);
        }
    }
    private void txtRegNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRegNameFocusLost
        txtRegMName.setText(getwordFromDict(txtRegName.getText()));

 }//GEN-LAST:event_txtRegNameFocusLost

    private void txtRegHnameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRegHnameFocusLost
        txtRegMaHname.setText(getwordFromDict(txtRegHname.getText()));

 }//GEN-LAST:event_txtRegHnameFocusLost

    private void txtRegPlaceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRegPlaceFocusLost
        txtRegMaPlace.setText(getwordFromDict(txtRegPlace.getText()));

}//GEN-LAST:event_txtRegPlaceFocusLost

    private void txtRegGardNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRegGardNameFocusLost
        txtRegMaGardName.setText(getwordFromDict(txtRegGardName.getText()));
    }//GEN-LAST:event_txtRegGardNameFocusLost

    private void txtRegDesamFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRegDesamFocusLost
        txtRegMaDesam.setText(getwordFromDict(txtRegDesam.getText()));
    }//GEN-LAST:event_txtRegDesamFocusLost

    private void txtRegAmsamFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRegAmsamFocusLost
        txtRegMaAmsam.setText(getwordFromDict(txtRegAmsam.getText()));
    }//GEN-LAST:event_txtRegAmsamFocusLost

    private void txtRegvillageFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRegvillageFocusLost
        txtRegMaVillage.setText(getwordFromDict(txtRegvillage.getText()));
    }//GEN-LAST:event_txtRegvillageFocusLost
    public HashMap getIsmCode() {
        HashMap ismMap = new HashMap();
        try {
            String hostDir = "D:\\TTCBS\\jboss-3.2.7\\bin\\template\\" + "ismCode.txt";
            File file = new File(hostDir);
            FileInputStream fis = new FileInputStream(file);
            //  customerList = new ArrayList();
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = "";
            int x = 0;
            String strLine;
            String numberParameter[] = new String[150];
            String val = "";
            while ((strLine = br.readLine()) != null) {
                numberParameter = strLine.split(",");
                if (numberParameter.length == 2) {
                    ismMap.put(CommonUtil.convertObjToInt(numberParameter[0].trim()), numberParameter[1]);
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(MalayalamDetailsUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ismMap;
    }
    private void txtRegMaHnameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMaHnameKeyReleased
        if (evt.getKeyCode() != 115 && evt.getKeyCode() != 113 && evt.getKeyCode() != 117) {
            String tempVal = txtRegMaHname.getText().trim();
            String txtVal = txtRegMaHname.getText().trim();
            char sub = evt.getKeyChar();
            String value = CommonUtil.convertObjToStr(ismCodeMap.get((int) sub));
            int p = txtRegMaHname.getCaretPosition();
            int m = 0;

            String chr = "";

            if (value.length() > 0) {
                String arr[] = value.split("&");
                if (arr.length > 0) {
                    m = CommonUtil.convertObjToInt(arr[0].trim());
                    char ch = (char) m;
                    chr = chr + ch;
                }
                if (arr.length > 1) {
                    m = CommonUtil.convertObjToInt(arr[1].trim());
                    char ch = (char) m;
                    chr = chr + ch;
                }
            } else {
                chr = "";
            }
            if (chr != null && chr.length() > 0) {
                if (p > 0 && p < txtVal.length()) {

                    String curval = txtVal.substring(0, p - 1) + chr + txtVal.substring(p, txtVal.length());
                    txtRegMaHname.setText(curval);
                    txtRegMaHname.setCaretPosition(p + 1);
                } else if (p == txtVal.length()) {
                    String cVal = "";
                    if (txtVal.length() > 0) {
                        if (p == 1) {
                            if (p == 1) {
                                if (txtVal.length() == 1) {
                                    cVal = "";
                                } else {
                                    cVal = txtVal.substring(p, txtVal.length() - 1);
                                }
                                cVal = chr + cVal;
                            }
                        } else if (p > 1) {
                            if ((p + 1) >= txtVal.length()) {
                                cVal += txtVal.substring(0, p - 1) + chr;
                            } else {
                                cVal += txtVal.substring(0, p - 1) + chr + txtVal.substring(p + 1, txtVal.length() - 1);
                            }
                        }
                    }

                    txtRegMaHname.setText(cVal);
                    txtRegMaHname.setCaretPosition(p + chr.length() - 1);
                } else if (p == 1) {
                    if (txtVal.length() > 0) {
                        txtVal = txtVal.substring(p - 1, txtVal.length());
                    } else {
                        txtVal = "";
                    }
                    txtRegMaHname.setText(chr + txtVal);
                    txtRegMaHname.setCaretPosition(p + chr.length());
                }
                if (txtVal.length() <= 0) {
                    txtRegMaHname.setText(txtVal);
                    txtRegMaHname.setCaretPosition(1);
                }
            } else {
                //     tempVal = tempVal.
            }

        }
    }//GEN-LAST:event_txtRegMaHnameKeyReleased

    private void txtRegMaHnameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMaHnameKeyPressed
        System.out.println(" key code  ::" + evt.getKeyCode());
        String wrd = "";
        if (evt.getKeyCode() == 115) {
            System.out.println("gggg  :" + txtRegMaHname.getCaretPosition());
            MalayalamKeyboardUI showObj = new MalayalamKeyboardUI("Customer", txtRegMaHname.getText(), txtRegMaHname.getCaretPosition(), txtRegHname.getText());
            showObj.show();
            if (showObj.getFinalTxt() != null && showObj.getFinalTxt().length() > 0) {
                txtRegMaHname.setText(showObj.getFinalTxt());
            }
        }
        if (evt.getKeyCode() == 113) {
            MalayalamDicSearchUI showObj = new MalayalamDicSearchUI("Customer", txtRegMaHname.getText(), txtRegMaHname.getCaretPosition(), txtRegHname.getText());
            showObj.show();
        }
        if (evt.getKeyCode() == 117) {
            if (count == 0) {
                int tt = txtRegMaHname.getCaretPosition();
                System.out.println("tt :" + tt);
                wrd = txtRegMaHname.getText().substring(tt);
            }
            int p =txtRegMaHname.getCaretPosition();
            String wordMal = txtRegMaHname.getText();
            String str1  = wordMal.substring(0, p);
             String str2  = wordMal.substring(p, wordMal.length());
             System.out.println("str1 :"+str1);
             System.out.println("str2 :"+str2);
             System.out.println("wordMal :"+wordMal);
            System.out.println("wrd ======== :"+wrd);
            System.out.println("mal_List ::"+mal_List);
            if (!txtEngValue.equals(wrd)) {
                mal_List = getMalWord(wrd);
                count = 0;
            }
            if (count == 0 && mal_List.size()>count && mal_List!=null) {
                txtRegMaHname.setText(CommonUtil.convertObjToStr(mal_List.get(count)));
            }
            if (txtEngValue.equals(wrd)) {
                count++;
                if (count <= mal_List.size()) {
                    txtRegMaHname.setText(CommonUtil.convertObjToStr(mal_List.get(count)));
                }
                txtRegMaHname.requestFocus();
                txtRegMaHname.setCaretPosition(txtRegMaHname.getText().length());
            }

txtRegMaHname.grabFocus();
        }
        txtRegMaHname.grabFocus();
        txtRegMaHname.requestFocus();
    }//GEN-LAST:event_txtRegMaHnameKeyPressed

    private void txtRegMaHnameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRegMaHnameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRegMaHnameFocusLost

    private void txtRegMNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMNameKeyReleased
        if (evt.getKeyCode() != 115 && evt.getKeyCode() != 113) {
            String tempVal = txtRegMName.getText().trim();
            String txtVal = txtRegMName.getText().trim();
            char sub = evt.getKeyChar();
            String value = CommonUtil.convertObjToStr(ismCodeMap.get((int) sub));
            int p = txtRegMName.getCaretPosition();
            int m = 0;

            String chr = "";

            if (value.length() > 0) {
                String arr[] = value.split("&");
                if (arr.length > 0) {
                    m = CommonUtil.convertObjToInt(arr[0].trim());
                    char ch = (char) m;
                    chr = chr + ch;
                }
                if (arr.length > 1) {
                    m = CommonUtil.convertObjToInt(arr[1].trim());
                    char ch = (char) m;
                    chr = chr + ch;
                }
            } else {
                chr = "";
            }
            if (chr != null && chr.length() > 0) {
                if (p > 0 && p < txtVal.length()) {

                    String curval = txtVal.substring(0, p - 1) + chr + txtVal.substring(p, txtVal.length());
                    txtRegMName.setText(curval);
                    txtRegMName.setCaretPosition(p + 1);
                } else if (p == txtVal.length()) {
                    String cVal = "";
                    if (txtVal.length() > 0) {
                        if (p == 1) {
                            if (p == 1) {
                                if (txtVal.length() == 1) {
                                    cVal = "";
                                } else {
                                    cVal = txtVal.substring(p, txtVal.length() - 1);
                                }
                                cVal = chr + cVal;
                            }
                        } else if (p > 1) {
                            if ((p + 1) >= txtVal.length()) {
                                cVal += txtVal.substring(0, p - 1) + chr;
                            } else {
                                cVal += txtVal.substring(0, p - 1) + chr + txtVal.substring(p + 1, txtVal.length() - 1);
                            }
                        }
                    }

                    txtRegMName.setText(cVal);
                    txtRegMName.setCaretPosition(p + chr.length() - 1);
                } else if (p == 1) {
                    if (txtVal.length() > 0) {
                        txtVal = txtVal.substring(p - 1, txtVal.length());
                    } else {
                        txtVal = "";
                    }
                    txtRegMName.setText(chr + txtVal);
                    txtRegMName.setCaretPosition(p + chr.length());
                }
                if (txtVal.length() <= 0) {
                    txtRegMName.setText(txtVal);
                    txtRegMName.setCaretPosition(1);
                }
            } else {
            }

        }
    }//GEN-LAST:event_txtRegMNameKeyReleased

    private void txtRegMaPlaceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMaPlaceKeyReleased
        if (evt.getKeyCode() != 115 && evt.getKeyCode() != 113) {
            String tempVal = txtRegMaPlace.getText().trim();
            String txtVal = txtRegMaPlace.getText().trim();
            char sub = evt.getKeyChar();
            String value = CommonUtil.convertObjToStr(ismCodeMap.get((int) sub));
            int p = txtRegMaPlace.getCaretPosition();
            int m = 0;

            String chr = "";

            if (value.length() > 0) {
                String arr[] = value.split("&");
                if (arr.length > 0) {
                    m = CommonUtil.convertObjToInt(arr[0].trim());
                    char ch = (char) m;
                    chr = chr + ch;
                }
                if (arr.length > 1) {
                    m = CommonUtil.convertObjToInt(arr[1].trim());
                    char ch = (char) m;
                    chr = chr + ch;
                }
            } else {
                chr = "";
            }
            if (chr != null && chr.length() > 0) {
                if (p > 0 && p < txtVal.length()) {

                    String curval = txtVal.substring(0, p - 1) + chr + txtVal.substring(p, txtVal.length());
                    txtRegMaPlace.setText(curval);
                    txtRegMaPlace.setCaretPosition(p + 1);
                } else if (p == txtVal.length()) {
                    String cVal = "";
                    if (txtVal.length() > 0) {
                        if (p == 1) {
                            if (p == 1) {
                                if (txtVal.length() == 1) {
                                    cVal = "";
                                } else {
                                    cVal = txtVal.substring(p, txtVal.length() - 1);
                                }
                                cVal = chr + cVal;
                            }
                        } else if (p > 1) {
                            if ((p + 1) >= txtVal.length()) {
                                cVal += txtVal.substring(0, p - 1) + chr;
                            } else {
                                cVal += txtVal.substring(0, p - 1) + chr + txtVal.substring(p + 1, txtVal.length() - 1);
                            }
                        }
                    }

                    txtRegMaPlace.setText(cVal);
                    txtRegMaPlace.setCaretPosition(p + chr.length() - 1);
                } else if (p == 1) {
                    if (txtVal.length() > 0) {
                        txtVal = txtVal.substring(p - 1, txtVal.length());
                    } else {
                        txtVal = "";
                    }
                    txtRegMaPlace.setText(chr + txtVal);
                    txtRegMaPlace.setCaretPosition(p + chr.length());
                }
                if (txtVal.length() <= 0) {
                    txtRegMaPlace.setText(txtVal);
                    txtRegMaPlace.setCaretPosition(1);
                }
            } else {
            }

        }
    }//GEN-LAST:event_txtRegMaPlaceKeyReleased

    private void txtRegMaVillageKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMaVillageKeyReleased
        if (evt.getKeyCode() != 115 && evt.getKeyCode() != 113) {
            String tempVal = txtRegMaVillage.getText().trim();
            String txtVal = txtRegMaVillage.getText().trim();
            char sub = evt.getKeyChar();
            String value = CommonUtil.convertObjToStr(ismCodeMap.get((int) sub));
            int p = txtRegMaVillage.getCaretPosition();
            int m = 0;

            String chr = "";

            if (value.length() > 0) {
                String arr[] = value.split("&");
                if (arr.length > 0) {
                    m = CommonUtil.convertObjToInt(arr[0].trim());
                    char ch = (char) m;
                    chr = chr + ch;
                }
                if (arr.length > 1) {
                    m = CommonUtil.convertObjToInt(arr[1].trim());
                    char ch = (char) m;
                    chr = chr + ch;
                }
            } else {
                chr = "";
            }
            if (chr != null && chr.length() > 0) {
                if (p > 0 && p < txtVal.length()) {

                    String curval = txtVal.substring(0, p - 1) + chr + txtVal.substring(p, txtVal.length());
                    txtRegMaVillage.setText(curval);
                    txtRegMaVillage.setCaretPosition(p + 1);
                } else if (p == txtVal.length()) {
                    String cVal = "";
                    if (txtVal.length() > 0) {
                        if (p == 1) {
                            if (p == 1) {
                                if (txtVal.length() == 1) {
                                    cVal = "";
                                } else {
                                    cVal = txtVal.substring(p, txtVal.length() - 1);
                                }
                                cVal = chr + cVal;
                            }
                        } else if (p > 1) {
                            if ((p + 1) >= txtVal.length()) {
                                cVal += txtVal.substring(0, p - 1) + chr;
                            } else {
                                cVal += txtVal.substring(0, p - 1) + chr + txtVal.substring(p + 1, txtVal.length() - 1);
                            }
                        }
                    }

                    txtRegMaVillage.setText(cVal);
                    txtRegMaVillage.setCaretPosition(p + chr.length() - 1);
                } else if (p == 1) {
                    if (txtVal.length() > 0) {
                        txtVal = txtVal.substring(p - 1, txtVal.length());
                    } else {
                        txtVal = "";
                    }
                    txtRegMaVillage.setText(chr + txtVal);
                    txtRegMaVillage.setCaretPosition(p + chr.length());
                }
                if (txtVal.length() <= 0) {
                    txtRegMaVillage.setText(txtVal);
                    txtRegMaVillage.setCaretPosition(1);
                }
            } else {
            }

        }
    }//GEN-LAST:event_txtRegMaVillageKeyReleased

    private void txtRegMaAmsamKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMaAmsamKeyReleased
        if (evt.getKeyCode() != 115 && evt.getKeyCode() != 113) {
            String tempVal = txtRegMaAmsam.getText().trim();
            String txtVal = txtRegMaAmsam.getText().trim();
            char sub = evt.getKeyChar();
            String value = CommonUtil.convertObjToStr(ismCodeMap.get((int) sub));
            int p = txtRegMaAmsam.getCaretPosition();
            int m = 0;

            String chr = "";

            if (value.length() > 0) {
                String arr[] = value.split("&");
                if (arr.length > 0) {
                    m = CommonUtil.convertObjToInt(arr[0].trim());
                    char ch = (char) m;
                    chr = chr + ch;
                }
                if (arr.length > 1) {
                    m = CommonUtil.convertObjToInt(arr[1].trim());
                    char ch = (char) m;
                    chr = chr + ch;
                }
            } else {
                chr = "";
            }
            if (chr != null && chr.length() > 0) {
                if (p > 0 && p < txtVal.length()) {

                    String curval = txtVal.substring(0, p - 1) + chr + txtVal.substring(p, txtVal.length());
                    txtRegMaAmsam.setText(curval);
                    txtRegMaAmsam.setCaretPosition(p + 1);
                } else if (p == txtVal.length()) {
                    String cVal = "";
                    if (txtVal.length() > 0) {
                        if (p == 1) {
                            if (p == 1) {
                                if (txtVal.length() == 1) {
                                    cVal = "";
                                } else {
                                    cVal = txtVal.substring(p, txtVal.length() - 1);
                                }
                                cVal = chr + cVal;
                            }
                        } else if (p > 1) {
                            if ((p + 1) >= txtVal.length()) {
                                cVal += txtVal.substring(0, p - 1) + chr;
                            } else {
                                cVal += txtVal.substring(0, p - 1) + chr + txtVal.substring(p + 1, txtVal.length() - 1);
                            }
                        }
                    }

                    txtRegMaAmsam.setText(cVal);
                    txtRegMaAmsam.setCaretPosition(p + chr.length() - 1);
                } else if (p == 1) {
                    if (txtVal.length() > 0) {
                        txtVal = txtVal.substring(p - 1, txtVal.length());
                    } else {
                        txtVal = "";
                    }
                    txtRegMaAmsam.setText(chr + txtVal);
                    txtRegMaAmsam.setCaretPosition(p + chr.length());
                }
                if (txtVal.length() <= 0) {
                    txtRegMaAmsam.setText(txtVal);
                    txtRegMaAmsam.setCaretPosition(1);
                }
            } else {
            }

        }
    }//GEN-LAST:event_txtRegMaAmsamKeyReleased

    private void txtRegMaDesamKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMaDesamKeyReleased
        if (evt.getKeyCode() != 115 && evt.getKeyCode() != 113) {
            String tempVal = txtRegMaDesam.getText().trim();
            String txtVal = txtRegMaDesam.getText().trim();
            char sub = evt.getKeyChar();
            String value = CommonUtil.convertObjToStr(ismCodeMap.get((int) sub));
            int p = txtRegMaDesam.getCaretPosition();
            int m = 0;

            String chr = "";

            if (value.length() > 0) {
                String arr[] = value.split("&");
                if (arr.length > 0) {
                    m = CommonUtil.convertObjToInt(arr[0].trim());
                    char ch = (char) m;
                    chr = chr + ch;
                }
                if (arr.length > 1) {
                    m = CommonUtil.convertObjToInt(arr[1].trim());
                    char ch = (char) m;
                    chr = chr + ch;
                }
            } else {
                chr = "";
            }
            if (chr != null && chr.length() > 0) {
                if (p > 0 && p < txtVal.length()) {

                    String curval = txtVal.substring(0, p - 1) + chr + txtVal.substring(p, txtVal.length());
                    txtRegMaDesam.setText(curval);
                    txtRegMaDesam.setCaretPosition(p + 1);
                } else if (p == txtVal.length()) {
                    String cVal = "";
                    if (txtVal.length() > 0) {
                        if (p == 1) {
                            if (p == 1) {
                                if (txtVal.length() == 1) {
                                    cVal = "";
                                } else {
                                    cVal = txtVal.substring(p, txtVal.length() - 1);
                                }
                                cVal = chr + cVal;
                            }
                        } else if (p > 1) {
                            if ((p + 1) >= txtVal.length()) {
                                cVal += txtVal.substring(0, p - 1) + chr;
                            } else {
                                cVal += txtVal.substring(0, p - 1) + chr + txtVal.substring(p + 1, txtVal.length() - 1);
                            }
                        }
                    }

                    txtRegMaDesam.setText(cVal);
                    txtRegMaDesam.setCaretPosition(p + chr.length() - 1);
                } else if (p == 1) {
                    if (txtVal.length() > 0) {
                        txtVal = txtVal.substring(p - 1, txtVal.length());
                    } else {
                        txtVal = "";
                    }
                    txtRegMaDesam.setText(chr + txtVal);
                    txtRegMaDesam.setCaretPosition(p + chr.length());
                }
                if (txtVal.length() <= 0) {
                    txtRegMaDesam.setText(txtVal);
                    txtRegMaDesam.setCaretPosition(1);
                }
            } else {
            }

        }
    }//GEN-LAST:event_txtRegMaDesamKeyReleased

    private void txtRegMaGardNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMaGardNameKeyReleased
        if (evt.getKeyCode() != 115 && evt.getKeyCode() != 113) {
            String tempVal = txtRegMaGardName.getText().trim();
            String txtVal = txtRegMaGardName.getText().trim();
            char sub = evt.getKeyChar();
            String value = CommonUtil.convertObjToStr(ismCodeMap.get((int) sub));
            int p = txtRegMaGardName.getCaretPosition();
            int m = 0;

            String chr = "";

            if (value.length() > 0) {
                String arr[] = value.split("&");
                if (arr.length > 0) {
                    m = CommonUtil.convertObjToInt(arr[0].trim());
                    char ch = (char) m;
                    chr = chr + ch;
                }
                if (arr.length > 1) {
                    m = CommonUtil.convertObjToInt(arr[1].trim());
                    char ch = (char) m;
                    chr = chr + ch;
                }
            } else {
                chr = "";
            }
            if (chr != null && chr.length() > 0) {
                if (p > 0 && p < txtVal.length()) {

                    String curval = txtVal.substring(0, p - 1) + chr + txtVal.substring(p, txtVal.length());
                    txtRegMaGardName.setText(curval);
                    txtRegMaGardName.setCaretPosition(p + 1);
                } else if (p == txtVal.length()) {
                    String cVal = "";
                    if (txtVal.length() > 0) {
                        if (p == 1) {
                            if (p == 1) {
                                if (txtVal.length() == 1) {
                                    cVal = "";
                                } else {
                                    cVal = txtVal.substring(p, txtVal.length() - 1);
                                }
                                cVal = chr + cVal;
                            }
                        } else if (p > 1) {
                            if ((p + 1) >= txtVal.length()) {
                                cVal += txtVal.substring(0, p - 1) + chr;
                            } else {
                                cVal += txtVal.substring(0, p - 1) + chr + txtVal.substring(p + 1, txtVal.length() - 1);
                            }
                        }
                    }

                    txtRegMaGardName.setText(cVal);
                    txtRegMaGardName.setCaretPosition(p + chr.length() - 1);
                } else if (p == 1) {
                    if (txtVal.length() > 0) {
                        txtVal = txtVal.substring(p - 1, txtVal.length());
                    } else {
                        txtVal = "";
                    }
                    txtRegMaGardName.setText(chr + txtVal);
                    txtRegMaGardName.setCaretPosition(p + chr.length());
                }
                if (txtVal.length() <= 0) {
                    txtRegMaGardName.setText(txtVal);
                    txtRegMaGardName.setCaretPosition(1);
                }
            } else {
            }

        }
    }//GEN-LAST:event_txtRegMaGardNameKeyReleased

    private void txtRegMNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMNameKeyPressed

        if (evt.getKeyCode() == 115) {
            MalayalamKeyboardUI showObj = new MalayalamKeyboardUI("Customer", txtRegMName.getText(), txtRegMName.getCaretPosition(), txtRegName.getText());
            showObj.show();
            if (showObj.getFinalTxt() != null && showObj.getFinalTxt().length() > 0) {
                txtRegMName.setText(showObj.getFinalTxt());
            }
        }
        if (evt.getKeyCode() == 113) {
            MalayalamDicSearchUI showObj = new MalayalamDicSearchUI("Customer", txtRegMName.getText(), txtRegMName.getCaretPosition(), txtRegName.getText());
            showObj.show();
        }
    }//GEN-LAST:event_txtRegMNameKeyPressed

    private void txtRegMaPlaceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMaPlaceKeyPressed
        if (evt.getKeyCode() == 115) {
            System.out.println("gggg 2 :" + txtRegMaPlace.getCaretPosition());
            MalayalamKeyboardUI showObj = new MalayalamKeyboardUI("Customer", txtRegMaPlace.getText(), txtRegMaPlace.getCaretPosition(), txtRegPlace.getText());
            showObj.show();
            if (showObj.getFinalTxt() != null && showObj.getFinalTxt().length() > 0) {
                txtRegMaPlace.setText(showObj.getFinalTxt());
            }
        }
        if (evt.getKeyCode() == 113) {
            MalayalamDicSearchUI showObj = new MalayalamDicSearchUI("Customer", txtRegMaPlace.getText(), txtRegMaPlace.getCaretPosition(), txtRegPlace.getText());
            showObj.show();
        }
    }//GEN-LAST:event_txtRegMaPlaceKeyPressed

    private void txtRegMaVillageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMaVillageKeyPressed
        if (evt.getKeyCode() == 115) {
            System.out.println("gggg 2 :" + txtRegMaVillage.getCaretPosition());
            MalayalamKeyboardUI showObj = new MalayalamKeyboardUI("Customer", txtRegMaVillage.getText(), txtRegMaVillage.getCaretPosition(), txtRegvillage.getText());
            showObj.show();
            if (showObj.getFinalTxt() != null && showObj.getFinalTxt().length() > 0) {
                txtRegMaVillage.setText(showObj.getFinalTxt());
            }
        }
        if (evt.getKeyCode() == 113) {
            MalayalamDicSearchUI showObj = new MalayalamDicSearchUI("Customer", txtRegMaVillage.getText(), txtRegMaVillage.getCaretPosition(), txtRegvillage.getText());
            showObj.show();
        }
    }//GEN-LAST:event_txtRegMaVillageKeyPressed

    private void txtRegMaAmsamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMaAmsamKeyPressed
        if (evt.getKeyCode() == 115) {
            System.out.println("gggg 2 :" + txtRegMaAmsam.getCaretPosition());
            MalayalamKeyboardUI showObj = new MalayalamKeyboardUI("Customer", txtRegMaAmsam.getText(), txtRegMaAmsam.getCaretPosition(), txtRegAmsam.getText());
            showObj.show();
            if (showObj.getFinalTxt() != null && showObj.getFinalTxt().length() > 0) {
                txtRegMaAmsam.setText(showObj.getFinalTxt());
            }
        }
        if (evt.getKeyCode() == 113) {
            MalayalamDicSearchUI showObj = new MalayalamDicSearchUI("Customer", txtRegMaAmsam.getText(), txtRegMaAmsam.getCaretPosition(), txtRegAmsam.getText());
            showObj.show();
        }
    }//GEN-LAST:event_txtRegMaAmsamKeyPressed

    private void txtRegMaDesamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMaDesamKeyPressed
        if (evt.getKeyCode() == 115) {
            System.out.println("gggg 2 :" + txtRegMaDesam.getCaretPosition());
            MalayalamKeyboardUI showObj = new MalayalamKeyboardUI("Customer", txtRegMaDesam.getText(), txtRegMaDesam.getCaretPosition(), txtRegDesam.getText());
            showObj.show();
            if (showObj.getFinalTxt() != null && showObj.getFinalTxt().length() > 0) {
                txtRegMaDesam.setText(showObj.getFinalTxt());
            }
        }
        if (evt.getKeyCode() == 113) {
            MalayalamDicSearchUI showObj = new MalayalamDicSearchUI("Customer", txtRegMaDesam.getText(), txtRegMaDesam.getCaretPosition(), txtRegDesam.getText());
            showObj.show();
        }
    }//GEN-LAST:event_txtRegMaDesamKeyPressed

    private void txtRegMaGardNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegMaGardNameKeyPressed
        if (evt.getKeyCode() == 115) {
            System.out.println("gggg 2 :" + txtRegMaGardName.getCaretPosition());
            MalayalamKeyboardUI showObj = new MalayalamKeyboardUI("Customer", txtRegMaGardName.getText(), txtRegMaGardName.getCaretPosition(), txtRegGardName.getText());
            showObj.show();
            if (showObj.getFinalTxt() != null && showObj.getFinalTxt().length() > 0) {
                txtRegMaGardName.setText(showObj.getFinalTxt());
            }
        }
        if (evt.getKeyCode() == 113) {
            MalayalamDicSearchUI showObj = new MalayalamDicSearchUI("Customer", txtRegMaGardName.getText(), txtRegMaGardName.getCaretPosition(), txtRegGardName.getText());
            showObj.show();
        }
    }//GEN-LAST:event_txtRegMaGardNameKeyPressed

    private void txtMemNumFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMemNumFocusLost
        observable.setTxtMemNo(txtMemNum.getText());
        if (txtMemNum.getText() != null && CommonUtil.convertObjToStr(txtMemNum.getText()).length() > 0) {
            viewType = "New";
            HashMap hMap = new HashMap();
            hMap.put("MEM_NUM", txtMemNum.getText());
            fillData(hMap);
        }
    }//GEN-LAST:event_txtMemNumFocusLost

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        HashMap where = new HashMap();
        HashMap hash = new HashMap();
        String shNo = txtMemNum.getText().substring(shareType.length(), txtMemNum.getText().length());
        int num = CommonUtil.convertObjToInt(shNo) + 1;
        txtMemNum.setText(shareType + num);
        observable.setTxtMemNo(txtMemNum.getText());
        where.put("MEM_NUM", txtMemNum.getText());
        hash.put(CommonConstants.MAP_WHERE, where);
        observable.getData(hash);
        update(observable, new HashMap());
    }//GEN-LAST:event_btnNextActionPerformed

    private void chktxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chktxtKeyPressed
    String out ="";
        if (evt.getKeyCode() != 115 && evt.getKeyCode() != 113) {
            String tempVal = chktxt.getText().trim();
            String txtVal = chktxt.getText().trim();
            char sub = evt.getKeyChar();
            System.out.println("sub: :"+sub);
            String value = CommonUtil.convertObjToStr(ismCodeMap.get((int) sub));
            System.out.println("value :"+value);
            int p = chktxt.getCaretPosition();
            int m = 0;
         System.out.println("pos :"+p);
            String chr = "";
            System.out.println("txtValtxtValtxtVal :"+txtVal);
            if (value.length() > 0) {
                String arr[] = value.split("&");
                if (arr.length > 0) {
                    m = CommonUtil.convertObjToInt(arr[0].trim());
                    char ch = (char) m;
                    chr = chr + ch;
                }
                if (arr.length > 1) {
                    m = CommonUtil.convertObjToInt(arr[1].trim());
                    char ch = (char) m;
                    chr = chr + ch;
                }
            } else {
                chr = "";
            }
            
            System.out.println("chr : +chr"+chr);
            String mm = txtVal.substring(p);
            System.out.println("mm :"+mm);
            String bb = txtVal.substring(p+1, txtVal.length());
            
            System.out.println("bb :"+bb);
            System.out.println(" chk val :"+mm+chr+bb);
             out =mm+chr+bb;
            
           
     }
          chktxt.setText(out);
    }//GEN-LAST:event_chktxtKeyPressed
    public String getwordFromDict(String word) {
        String malWord = "";
        ///  getMalWord(word);
        if (word != null && word.length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("ENG_WORD", word.toUpperCase().replace(",", "").trim());
            List lst = (List) ClientUtil.executeQuery("getMalayalamWord", whereMap);
            if (lst != null && lst.size() > 0) {
                HashMap one = (HashMap) lst.get(0);
                if (one != null && one.containsKey("M")) {
                    String mWord = CommonUtil.convertObjToStr(one.get("M"));
                    malWord += " " + mWord;
                } else {
                    malWord += " " + EngToMalTransliterator.get_ml(word);
                }

            } else {
                String arr[] = word.split(" ");
                for (int i = 0; i < arr.length; i++) {
                    whereMap = new HashMap();
                    whereMap.put("ENG_WORD", arr[i].toUpperCase().replace(",", "").trim());
                    lst = (List) ClientUtil.executeQuery("getMalayalamWord", whereMap);
                    if (lst != null && lst.size() > 0) {
                        HashMap one = (HashMap) lst.get(0);
                        if (one != null && one.containsKey("M")) {
                            String mWord = CommonUtil.convertObjToStr(one.get("M"));
                            malWord += " " + mWord;
                        } else {
                            malWord += " " + EngToMalTransliterator.get_ml(arr[i]);
                        }

                    } else {
                        malWord += " " + EngToMalTransliterator.get_ml(arr[i]);
                    }
                }
            }
        }
        return malWord.trim();
    }

    private ArrayList getMalWord(String wrd) {
        String malWord = "";
        txtEngValue = wrd;
        ArrayList mal = new ArrayList();
        int size = wrd.length();
        char chr[] = new char[size + 1];
        //  for(int i=size;i>0;i--){
        System.out.println("wrd kk:" + wrd);
        //   wrd = wrd.substring(0,i);
        System.out.println("wrd    ::" + wrd);
        HashMap whereMap = new HashMap();
        whereMap.put("ENG_WORD", wrd.replace(",", "").trim());
        List lst = (List) ClientUtil.executeQuery("getMalayalamFromWord", whereMap);
        for (int k = 0; k < lst.size(); k++) {
            HashMap one = (HashMap) lst.get(k);
            if (one != null && one.containsKey("M")) {
                String mWord = CommonUtil.convertObjToStr(one.get("M"));
                mal.add(mWord);
            }

        }
        System.out.println("malWord  :" + malWord);
        //  }
        mal_List = new ArrayList();
        return mal;

    }

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

    public static void main(String args[]) {
        System.out.println("hgjhasdgs gh");
        // CInternalFrame frm = new MalayalamDetailsUI(); 


    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNext;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CTextField chktxt;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblMemNo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblRegAmsam;
    private com.see.truetransact.uicomponent.CLabel lblRegDesam;
    private com.see.truetransact.uicomponent.CLabel lblRegGardName;
    private com.see.truetransact.uicomponent.CLabel lblRegHname;
    private com.see.truetransact.uicomponent.CLabel lblRegName;
    private com.see.truetransact.uicomponent.CLabel lblRegPlace;
    private com.see.truetransact.uicomponent.CLabel lblRegVillage;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace11;
    private com.see.truetransact.uicomponent.CLabel lblSpace12;
    private com.see.truetransact.uicomponent.CLabel lblSpace13;
    private com.see.truetransact.uicomponent.CLabel lblSpace14;
    private com.see.truetransact.uicomponent.CLabel lblSpace15;
    private com.see.truetransact.uicomponent.CLabel lblSpace16;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrTokenConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CToolBar tbrTokenConfig;
    private com.see.truetransact.uicomponent.CTextField txtMemNum;
    private com.see.truetransact.uicomponent.CTextField txtMemType;
    private com.see.truetransact.uicomponent.CTextField txtRegAmsam;
    private com.see.truetransact.uicomponent.CTextField txtRegDesam;
    private com.see.truetransact.uicomponent.CTextField txtRegGardName;
    private com.see.truetransact.uicomponent.CTextField txtRegHname;
    private com.see.truetransact.uicomponent.CTextField txtRegMName;
    private com.see.truetransact.uicomponent.CTextField txtRegMaAmsam;
    private com.see.truetransact.uicomponent.CTextField txtRegMaDesam;
    private com.see.truetransact.uicomponent.CTextField txtRegMaGardName;
    private com.see.truetransact.uicomponent.CTextField txtRegMaHname;
    private com.see.truetransact.uicomponent.CTextField txtRegMaPlace;
    private com.see.truetransact.uicomponent.CTextField txtRegMaVillage;
    private com.see.truetransact.uicomponent.CTextField txtRegName;
    private com.see.truetransact.uicomponent.CTextField txtRegPlace;
    private com.see.truetransact.uicomponent.CTextField txtRegvillage;
    // End of variables declaration//GEN-END:variables
}
