/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * QRCodeUI.java
 *
 * Created on August 24, 2003, 1:46 PM
 */
package com.see.truetransact.ui.common.viewall;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;
import java.util.Date;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;

import org.apache.log4j.Logger;

import com.see.truetransact.clientutil.EnhancedComboBoxModel;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.CPanel;
import com.see.truetransact.uicomponent.CDialog;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CComboBox;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import java.util.logging.Level;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.commonutil.StringEncrypter;
import com.see.truetransact.commonutil.StringEncrypter.EncryptionException;
import com.see.truetransact.uicomponent.*;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import java.awt.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.swing.*;

/**
 * @author balachandar
 */
public class QRCodeUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer {

    private final ViewAllRB resourceBundle = new ViewAllRB();
    private EnquiryOB observable;
    HashMap paramMap = null;
    int amtColumnNo = 0;
    double tot = 0;
    String amtColName = "";
    String behavesLike = "";
    boolean collDet = false;
    boolean StopPaymentView = false;
    private Date currDt = null;
    private boolean btnDividendPressed = false;
    private final static Logger log = Logger.getLogger(ViewAllTransactions.class);
    private TransDetailsUI transDetails = null;
    private StringEncrypter encrypt = null;
    private ProxyFactory proxy;
    private HashMap dataMap = null;
    HashMap qrCodeMap = new HashMap();
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ComboBoxModel cbmCategory;
    private String filePath = "";
    private byte[] QRBytes = null;

    public ComboBoxModel getCbmCategory() {
        return cbmCategory;
    }

    public void setCbmCategory(ComboBoxModel cbmCategory) {
        this.cbmCategory = cbmCategory;
    }

    public CComboBox getCboCategory() {
        return cboCategory;
    }

    public void setCboCategory(CComboBox cboCategory) {
        this.cboCategory = cboCategory;
    }

    /**
     * Creates new form ViewAll
     */
    public QRCodeUI() {
        setupInit();
        setupScreen();
        currDt = ClientUtil.getCurrentDate();
        try {
            encrypt = new StringEncrypter();
        } catch (EncryptionException ex) {
            java.util.logging.Logger.getLogger(QRCodeUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        qrCodeMap = new HashMap();
        qrCodeMap.put(CommonConstants.JNDI, "SelectAllJNDI");
        qrCodeMap.put(CommonConstants.HOME, "common.viewall.SelectAllHome");
        qrCodeMap.put(CommonConstants.REMOTE, "common.viewall.SelectAll");

        try {
            proxy = ProxyFactory.createProxy();
//            fillDropdown();
        } catch (Exception e) {
            System.err.println("Exception " + e + "Caught");
            e.printStackTrace();
        }
        txtLimit.setValidation(new CurrencyValidation(13, 0));
    }

    private void setupInit() {
        initComponents();
        internationalize();
        setObservable();
        toFront();
        setCombos();
        qrCodeImage.setVisible(true);
        observable.resetForm();
        update(observable, null);
        txtAccNo.setAllowAll(true);
        txtAccNo.setEnabled(false);
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
            observable = EnquiryOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateOBFields() {
        observable.setProdType(CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected()));
        observable.setTxtAccNo(txtAccNo.getText());
    }

    private void setCombos() {
        cboProdType.setModel(observable.getCbmProdType());
        cboCategory.setModel(observable.getCbmCategory());
    }

    private void addComboValues(List lst, CComboBox cbo) {
        HashMap hash = new HashMap();
        ArrayList arr = new ArrayList();
        String str = "";
        arr.add("");
        for (int i = 0; i < lst.size(); i++) {
            hash = (HashMap) lst.get(i);
            arr.add(hash.get("HEAD"));
        }
        EnhancedComboBoxModel cboModel = new EnhancedComboBoxModel(arr);
        cbo.setModel(cboModel);
        hash = null;
        arr = null;
        cboModel = null;
    }

    public void populateData() {
        updateOBFields();
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        if (!CommonUtil.convertObjToStr(observable.getProdType()).equals("")) {
            if (observable.getTdtFromDate() != null || observable.StopPaymentView == true) {
                if (btnIntDetPressed) {
                    viewMap.put(CommonConstants.MAP_NAME, "getAllIntTransactions" + observable.getProdType());
                    if (observable.getLinkMap() != null && observable.getLinkMap().containsKey("AS_CUSTOMER_COMES")
                            && observable.getLinkMap().get("AS_CUSTOMER_COMES").equals("Y")) {
                        if (observable.getProdType().equals("TL")) {
                            viewMap.put(CommonConstants.MAP_NAME, "getAllIntTransactionsAsAnWhenTL");
                        } else {
                            viewMap.put(CommonConstants.MAP_NAME, "getAllIntTransactionsAsAnWhenAD");
                        }
                    }
                } else if (observable.btnRDTransPressed == true) {
                    whereMap.put("CURR_DT", currDt);
                    viewMap.put(CommonConstants.MAP_NAME, "getAllRDTransactionsTD");
                } else if (btnDividendPressed == true) {
                    viewMap.put(CommonConstants.MAP_NAME, "getAllDiovidendSH");
                } else if (observable.StopPaymentView == true) {
                    viewMap.put(CommonConstants.MAP_NAME, "getSelectStopPaymentDetails");
                } else {
                    viewMap.put(CommonConstants.MAP_NAME, "getAllTransactions" + observable.getProdType());
                }
                if (observable.getProdType().equals("TD")) {
                    String actNum = "";
                    if (observable.getTxtAccNo().lastIndexOf("_") != -1) {
                        actNum = observable.getTxtAccNo().substring(0, observable.getTxtAccNo().lastIndexOf("_"));
                    }
                    whereMap.put("DEPOSIT_NO", actNum);
                }
                whereMap.put("ACT_NUM", observable.getTxtAccNo());
                if (observable.getTdtFromDate() != null) {
                    whereMap.put("FROM_DT", getProperDate(observable.getTdtFromDate()));
                }
                if (observable.getTdtToDate() != null) {
                    whereMap.put("TO_DT", getProperDate(observable.getTdtToDate()));
                } else {
                    whereMap.put("TO_DT", currDt);
                }
                if (collDet) {
                    whereMap.put("BEHAVES_LIKE", "DAILY");
                } else {
                    whereMap.put("BEHAVES_LIKE", null);
                }
                viewMap.put(CommonConstants.MAP_WHERE, whereMap);
                try {
                    log.info("populateData...");
//                    ArrayList heading = observable.populateData(viewMap, tblData);
//                    heading = null;
                } catch (Exception e) {
                    System.err.println("Exception " + e.toString() + "Caught");
                    e.printStackTrace();
                }
            } else {
                ClientUtil.displayAlert("Enter From Date...");
            }
        } else {
            ClientUtil.displayAlert("Select Product Type...");
        }
        viewMap = null;
        whereMap = null;
    }

    private Date getProperDate(Date sourceDate) {
        Date targetDate = (Date) currDt.clone();
        targetDate.setDate(sourceDate.getDate());
        targetDate.setMonth(sourceDate.getMonth());
        targetDate.setYear(sourceDate.getYear());
        return targetDate;
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

//    public void setVisible(boolean visible) {
//        if (observable.isAvailable()) {
//            super.setVisible(visible);
//        }
//    }
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
        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        lblAccNo = new com.see.truetransact.uicomponent.CLabel();
        lblAccNameValue = new com.see.truetransact.uicomponent.CLabel();
        panAcctNo = new com.see.truetransact.uicomponent.CPanel();
        txtAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccNo = new com.see.truetransact.uicomponent.CButton();
        lblProdType1 = new com.see.truetransact.uicomponent.CLabel();
        cboCategory = new com.see.truetransact.uicomponent.CComboBox();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        txtLimit = new com.see.truetransact.uicomponent.CTextField();
        panSearch1 = new com.see.truetransact.uicomponent.CPanel();
        btnGenerateQRImage = new com.see.truetransact.uicomponent.CButton();
        qrCodeImage = new com.see.truetransact.uicomponent.CPanel();
        lblQRImage = new com.see.truetransact.uicomponent.CLabel();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        btnLoadImage = new com.see.truetransact.uicomponent.CButton();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        btnRegeneragte = new com.see.truetransact.uicomponent.CButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(750, 675));
        setPreferredSize(new java.awt.Dimension(850, 675));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panSearchCondition.setMinimumSize(new java.awt.Dimension(700, 170));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(700, 170));
        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 6, 4, 6);
        panSearchCondition.add(lblProdType, gridBagConstraints);

        cboProdType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.setPopupWidth(130);
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 4, 4, 6);
        panSearchCondition.add(cboProdType, gridBagConstraints);

        lblAccNo.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        panSearchCondition.add(lblAccNo, gridBagConstraints);

        lblAccNameValue.setForeground(new java.awt.Color(0, 51, 204));
        lblAccNameValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAccNameValue.setMaximumSize(new java.awt.Dimension(175, 21));
        lblAccNameValue.setMinimumSize(new java.awt.Dimension(125, 21));
        lblAccNameValue.setPreferredSize(new java.awt.Dimension(175, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        panSearchCondition.add(lblAccNameValue, gridBagConstraints);

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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(panAcctNo, gridBagConstraints);

        lblProdType1.setText("Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 6, 4, 6);
        panSearchCondition.add(lblProdType1, gridBagConstraints);

        cboCategory.setMaximumSize(new java.awt.Dimension(100, 21));
        cboCategory.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCategory.setPopupWidth(130);
        cboCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCategoryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 4, 4, 6);
        panSearchCondition.add(cboCategory, gridBagConstraints);

        lblAmount.setText("Transaction Limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(lblAmount, gridBagConstraints);

        txtLimit.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLimit.setPreferredSize(new java.awt.Dimension(21, 200));
        txtLimit.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLimitFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(txtLimit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(panSearchCondition, gridBagConstraints);

        panSearch1.setMinimumSize(new java.awt.Dimension(750, 350));
        panSearch1.setPreferredSize(new java.awt.Dimension(750, 350));
        panSearch1.setLayout(new java.awt.GridBagLayout());

        btnGenerateQRImage.setText("Generate QR Image");
        btnGenerateQRImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateQRImageActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch1.add(btnGenerateQRImage, gridBagConstraints);

        qrCodeImage.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        qrCodeImage.setMinimumSize(new java.awt.Dimension(230, 230));
        qrCodeImage.setPreferredSize(new java.awt.Dimension(230, 230));
        qrCodeImage.setLayout(new java.awt.GridBagLayout());

        lblQRImage.setMinimumSize(new java.awt.Dimension(225, 225));
        lblQRImage.setPreferredSize(new java.awt.Dimension(225, 225));
        lblQRImage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblQRImageMouseClicked(evt);
            }
        });
        qrCodeImage.add(lblQRImage, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 5);
        panSearch1.add(qrCodeImage, gridBagConstraints);

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch1.add(btnClear, gridBagConstraints);

        btnLoadImage.setText("Load QR Image");
        btnLoadImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadImageActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch1.add(btnLoadImage, gridBagConstraints);

        btnPrint.setText("Print QR");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch1.add(btnPrint, gridBagConstraints);

        btnRegeneragte.setText("Regenerate OTP");
        btnRegeneragte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegeneragteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch1.add(btnRegeneragte, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panSearch1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    boolean btnIntDetPressed = false;

    public void fillData(Object obj) {
        try {
            HashMap hash = (HashMap) obj;
            System.out.println("#$#$ Hash : " + hash);
            txtAccNo.setText(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
            lblAccNameValue.setText(CommonUtil.convertObjToStr(hash.get("CUST_NAME")));
            hash.put("QR_ACT_NUM", hash.get("ACT_NUM"));
            hash.put("QR_BANK", getActualQRBankCode());
            List lst = ClientUtil.executeQuery("getRecordExistorNotQRMaster", hash);
            if (lst != null && lst.size() > 0) {
                ClientUtil.showAlertWindow("QR Code already generated for this account");
                HashMap existMap = new HashMap();
                existMap = (HashMap) lst.get(0);
                txtLimit.setText(CommonUtil.convertObjToStr(existMap.get("TXN_LIMIT")));
                btnLoadImageActionPerformed(null);
                btnGenerateQRImage.setEnabled(false);
                txtAccNo.setEnabled(false);
                cboProdType.setEnabled(false);
                cboCategory.setEnabled(false);
                btnLoadImage.setEnabled(false);
                btnRegeneragte.setEnabled(true);
                btnAccNo.setEnabled(false);
            } else {
                btnGenerateQRImage.setEnabled(true);
                btnLoadImage.setEnabled(false);
                btnRegeneragte.setEnabled(false);
                btnAccNo.setEnabled(false);
            }
            txtAccNoFocusLost(null);
        } catch (Exception e) {
        }
    }

    private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoActionPerformed
        // TODO add your handling code here:
        if (cboProdType.getSelectedIndex() > 0) {
            HashMap viewMap = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            whereMap.put(CommonConstants.PRODUCT_TYPE, observable.getProdType());
            viewMap.put(CommonConstants.MAP_NAME, "Enquiry.getAccountList");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        } else {
            ClientUtil.showAlertWindow("Product Type should not be empty");
            return;
        }
    }//GEN-LAST:event_btnAccNoActionPerformed

    private void txtAccNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccNoFocusLost
        // TODO add your handling code here:
//        if (observable.getProdType().length() > 0 && txtAccNo.getText().length() > 0) {
//            if (!txtAccNo.getText().equals(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID))) {
//                String actNum = txtAccNo.getText();
//                if (actNum != null && actNum.length() > 0) {
//                    observable.asAnWhenCustomerComesYesNO(actNum, null);
//                }
//                if ((observable.getProdType().equals("TL") || observable.getProdType().equals("AD")) && (actNum != null && actNum.length() > 0)
//                        && observable.getLinkMap() != null && observable.getLinkMap().containsKey("AS_CUSTOMER_COMES")
//                        && observable.getLinkMap().get("AS_CUSTOMER_COMES") != null && observable.getLinkMap().get("AS_CUSTOMER_COMES").equals("Y")) {
//
//                    HashMap asAndWhenMap = interestCalculationTLAD(actNum);
//                    if (asAndWhenMap != null && asAndWhenMap.size() > 0) {
//                        transDetails.setAsAndWhenMap(asAndWhenMap);
//                    }
//                } else {
//                    observable.setLinkMap(new HashMap());
//                }
//                if (observable.getProdType().equals("TD")) {
//                    if (txtAccNo.getText().length() > 0 && txtAccNo.getText().lastIndexOf("_") == -1) {
//                        actNum = txtAccNo.getText();
//                        txtAccNo.setText(txtAccNo.getText() + "_1");
//                    } else {
//                        actNum = txtAccNo.getText();
//                        actNum = txtAccNo.getText().substring(0, actNum.lastIndexOf("_"));
//                        System.out.println("#$#$#$#$ actNum " + actNum);
//                    }
//
//                    HashMap where = new HashMap();
//                    where.put("ACT_NUM", actNum);
//                    List lst = ClientUtil.executeQuery("getBehavesLikeForDepositNo", where);
//                    if (lst != null && lst.size() > 0) {
//                        behavesLike = CommonUtil.convertObjToStr(((HashMap) lst.get(0)).get("BEHAVES_LIKE"));
////                    if (behavesLike.equals("DAILY"))
////                        btnCollDt.setVisible(true);
////                    else
////                        btnCollDt.setVisible(false);
//
//                        HashMap behavesLikeMap = new HashMap();
//                        behavesLikeMap.put("DEPOSIT_NO", actNum);
//                        lst = ClientUtil.executeQuery("getStatusForDeposit", behavesLikeMap);
//                        if (lst != null && lst.size() > 0) {
//                            behavesLikeMap = (HashMap) lst.get(0);
//                            lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", behavesLikeMap);
//                            if (lst != null && lst.size() > 0) {
//                                behavesLikeMap = (HashMap) lst.get(0);
//                                if (behavesLikeMap.get("BEHAVES_LIKE").equals("RECURRING")) {
////                                btnrecurrView.setVisible(true);
////                                btnrecurrView.setEnabled(true);
//                                } else {
////                                btnrecurrView.setVisible(false);
////                                btnrecurrView.setEnabled(false);
//                                }
//                            }
//
//                        }
//                    }
//                } else {
////                btnrecurrView.setVisible(false);
////                btnrecurrView.setEnabled(false);
////                btnCollDt.setVisible(false);
//                }
//
//                if (observable.getProdType().equals("OA")) {
//                    HashMap chequeMap = new HashMap();
//                    chequeMap.put("ACT_NUM", txtAccNo.getText());
//                    List lst = ClientUtil.executeQuery("getSelectStopPaymentDetails", chequeMap);
//                    if (lst != null && lst.size() > 0) {
//                        chequeMap = (HashMap) lst.get(0);
////                    btnStopPaymentView.setVisible(true);
////                    btnStopPaymentView.setEnabled(true);
//                    } else {
////                    btnStopPaymentView.setVisible(false);
////                    btnStopPaymentView.setEnabled(false);
//                    }
//                } else {
////                btnStopPaymentView.setVisible(false);
////                btnStopPaymentView.setEnabled(false);
//                }
//                observable.setTxtAccNo(txtAccNo.getText());
//                transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, observable.getTxtAccNo());
//                observable.setAccountName();
//                lblAccNameValue.setText(observable.getLblAccNameValue());
//                if (observable.getLblAccNameValue().length() <= 0) {
//                    ClientUtil.displayAlert("Invalid Account No.");
//                    lblAccNameValue.setText("");
//                    txtAccNo.setText("");
//                    //Added By Suresh
//                    if (!observable.getProdType().equals("GL")) {
//                        txtAccNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
//                    }
//                }
//            }
//        } else {
//            ClientUtil.displayAlert("Enter Product Type");
////            btnClearActionPerformed(null);
//            txtAccNo.setText("");
//        }
    }//GEN-LAST:event_txtAccNoFocusLost

    private void txtAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccNoActionPerformed
        // TODO add your handling code here:
        String actNum = txtAccNo.getText();
        if (actNum != null && actNum.length() > 0) {
            observable.asAnWhenCustomerComesYesNO(actNum, null);
        }
        if ((observable.getProdType().equals("TL") || observable.getProdType().equals("AD")) && (actNum != null && actNum.length() > 0)
                && observable.getLinkMap() != null && observable.getLinkMap().containsKey("AS_CUSTOMER_COMES")
                && observable.getLinkMap().get("AS_CUSTOMER_COMES") != null && observable.getLinkMap().get("AS_CUSTOMER_COMES").equals("Y")) {

            HashMap asAndWhenMap = interestCalculationTLAD(actNum);
            if (asAndWhenMap != null && asAndWhenMap.size() > 0) {
                transDetails.setAsAndWhenMap(asAndWhenMap);
            }
        } else {
            observable.setLinkMap(new HashMap());
        }
        if (observable.getProdType().equals("TD")) {
            HashMap behavesLikeMap = new HashMap();
            behavesLikeMap.put("DEPOSIT_NO", txtAccNo.getText());
            List lst = ClientUtil.executeQuery("getStatusForDeposit", behavesLikeMap);
            if (lst != null && lst.size() > 0) {
                behavesLikeMap = (HashMap) lst.get(0);
                lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", behavesLikeMap);
                if (lst != null && lst.size() > 0) {
                    behavesLikeMap = (HashMap) lst.get(0);
                    if (behavesLikeMap.get("BEHAVES_LIKE").equals("RECURRING")) {
//                        btnrecurrView.setVisible(true);
//                        btnrecurrView.setEnabled(true);
                    } else {
//                        btnrecurrView.setVisible(false);
//                        btnrecurrView.setEnabled(false);
                    }
                }
            }
        } else {
//            btnrecurrView.setVisible(false);
//            btnrecurrView.setEnabled(false);
        }
        if (observable.getProdType().equals("TD")) {
            //            String actNum = txtAccNo.getText();
            if (txtAccNo.getText().length() > 0 && txtAccNo.getText().lastIndexOf("_") == -1) {
                actNum = txtAccNo.getText();
                txtAccNo.setText(txtAccNo.getText() + "_1");
            } else {
                actNum = txtAccNo.getText();
                actNum = txtAccNo.getText().substring(0, actNum.lastIndexOf("_"));
                System.out.println("#$#$#$#$ actNum " + actNum);
            }
            HashMap where = new HashMap();
            where.put("ACT_NUM", actNum);
            List lst = ClientUtil.executeQuery("getBehavesLikeForDepositNo", where);
            if (lst != null && lst.size() > 0) {
                behavesLike = CommonUtil.convertObjToStr(((HashMap) lst.get(0)).get("BEHAVES_LIKE"));
//                if (behavesLike.equals("DAILY"))
//                    btnIntDet.setVisible(false);
//                else
//                    btnIntDet.setVisible(true);
            }
            lst = null;
        }
        observable.setTxtAccNo(txtAccNo.getText());
        transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, observable.getTxtAccNo());
        observable.setAccountName();
        lblAccNameValue.setText(observable.getLblAccNameValue());
    }//GEN-LAST:event_txtAccNoActionPerformed
    private HashMap interestCalculationTLAD(String accountNo) {
        HashMap map = new HashMap();
        HashMap hash = null;
        try {

            map.put("ACT_NUM", accountNo);
            map.put("ACCT_NUM", accountNo);
            List lst = ClientUtil.executeQuery("TermLoan.getBehavesLike", map);
            String prod_id = null;
            if (lst != null && lst.size() > 0) {
                HashMap prodMap = (HashMap) lst.get(0);
                prod_id = CommonUtil.convertObjToStr(prodMap.get("PROD_ID"));
            }
//                String prod_id=((ComboBoxModel)cboProductID.getModel()).getKeyForSelected().toString();
            map.put("PROD_ID", prod_id);
            map.put("TRANS_DT", currDt);
            map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            lst = ClientUtil.executeQuery("IntCalculationDetail", map);
            if (lst == null || lst.isEmpty()) {
                lst = ClientUtil.executeQuery("IntCalculationDetailAD", map);
            }
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                    hash = new HashMap();
                    return hash;
                }
                map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);

                //                    InterestCalculationTask interestcalTask=new InterestCalculationTask(header);
                map.putAll(hash);
                map.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                map.put("CURR_DATE", ClientUtil.getCurrentDateProperFormat());
                System.out.println("map before intereest###" + map);
                //                    hash =interestcalTask.interestCalcTermLoanAD(map);
                hash = observable.loanInterestCalculationAsAndWhen(map);

                System.out.println("hashinterestoutput###" + hash);
                hash.put("AS_CUSTOMER_COMES", map.get("AS_CUSTOMER_COMES"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hash;
    }
    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
//        btnStopPaymentView.setVisible(false);
        System.out.println("#$#$ prodType : " + ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
        observable.setProdType(CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected()));
        if (!observable.getProdType().equals("") && !observable.getProdType().equals("OA")) {
            ClientUtil.showAlertWindow("Please select product type only for Operative Account ");
            cboProdType.setSelectedItem("");
            return;
        }
//        behavesLike = "";
//        if (observable.getProdType().equals("TD") || observable.getProdType().equals("TL") || observable.getProdType().equals("ATL") || observable.getProdType().equals("AD") || observable.getProdType().equals("AAD")) {
////            btnIntDet.setVisible(true);
//        } else {
////            btnIntDet.setVisible(false);
//        }
//        if(observable.getProdType().equals("TD"))
//            btnCollDt.setVisible(true);
//        else
//            btnCollDt.setVisible(false);
//        
//        if(observable.getProdType().equals("SH"))
//            btnDivDet.setVisible(true);
//        else
//            btnDivDet.setVisible(false);
//        if (!observable.getProdType().equals("GL")) {
//            txtAccNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
//        } else {
//            txtAccNo.setText("");
//        }

    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    private void btnGenerateQRImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateQRImageActionPerformed
        try {
            if (cboCategory.getSelectedIndex() == 0) {
                ClientUtil.showAlertWindow("Category should not be empty");
                return;
            } else if (cboProdType.getSelectedIndex() == 0) {
                ClientUtil.showAlertWindow("Product Type should not be empty");
                return;
            } else if (txtAccNo.getText().length() == 0) {
                ClientUtil.showAlertWindow("Account No should not be empty");
                return;
            } else if (txtLimit.getText().length() == 0) {
                ClientUtil.showAlertWindow("Transaction Limit amount should not be empty");
                return;
            } else {
                btnLoadImage.setEnabled(false);
                cboProdType.setEnabled(false);
                btnAccNo.setEnabled(false);
                txtAccNo.setEnabled(false);
                btnGenerateQRImage.setEnabled(false);
                try {
                    dataMap = new HashMap();
                    dataMap.put("QR_ACT_NUM", txtAccNo.getText());
                    dataMap.put("QR_BANK", getActualQRBankCode());
                    dataMap.put("QR_DETAILS", encrypt.encrypt("Fin" + "," + TrueTransactMain.BANK_ID + txtAccNo.getText() + "," + lblAccNameValue.getText() + "," + "Curo"));
                    dataMap.put("QR_FILE", "");
                    dataMap.put("TXN_LIMIT", txtLimit.getText());
                    dataMap.put("CATEGORY", cboCategory.getSelectedItem());
                    dataMap.put("PROD_TYPE", (String) ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
                    dataMap.put("insertQRMasterMap", "insertQRMasterMap");
                    qrCodeMap.put(CommonConstants.MODULE, getModule());
                    qrCodeMap.put(CommonConstants.SCREEN, getScreen());
                    qrCodeMap.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
                    qrCodeMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                    HashMap proxyResultMap = proxy.execute(dataMap, qrCodeMap);
                    if (proxyResultMap != null && proxyResultMap.containsKey("QR_FILE")) {
                        QRBytes = (byte[]) proxyResultMap.get("QR_FILE");
                        Icon icon = new ImageIcon(QRBytes);
                        lblQRImage.setIcon(icon);
                    } else if (proxyResultMap != null && proxyResultMap.containsKey("RE")) {
                        ClientUtil.showAlertWindow("Selected Account QR Image already exist,Please Load QR Image to View existing record.");
                        btnLoadImage.setEnabled(true);
                        btnGenerateQRImage.setEnabled(true);
                        cboProdType.setEnabled(true);
                        btnAccNo.setEnabled(true);
                    }
                    System.out.println("QR Code image created successfully! : " + proxyResultMap);
                    // Added by nithya on 09-02-2017 for Print OTP
                    if(proxyResultMap != null){
                    int yesNo = 0;
                    String[] options = {"Yes", "No"};
                    yesNo = COptionPane.showOptionDialog(null, "Do you want to print OTP?", CommonConstants.WARNINGTITLE,
                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                            null, options, options[0]);
                    System.out.println("#$#$$ yesNo : " + yesNo);
                    if (yesNo == 0) {
                         HashMap hash = new HashMap();
                         hash.put("QR_ACT_NUM", txtAccNo.getText());
                         hash.put("QR_BANK", getActualQRBankCode());
                         List lst = ClientUtil.executeQuery("getRecordExistorNotQRMaster", hash);
                         if (lst != null && lst.size() > 0) {
                            HashMap existMap = new HashMap();
                            existMap = (HashMap) lst.get(0);
                            System.out.println("existMap OTP :: " + existMap.get("OTP_NUM"));
                            StringEncrypter encrypt = new StringEncrypter();
                            String otpNum = "";
                            otpNum = CommonUtil.convertObjToStr(existMap.get("OTP_NUM"));                           
                            otpNum = encrypt.decrypt(otpNum);                           
                            printOTP(otpNum);
                        }
                    }
                    }  
                    // End
                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(QRCodeUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(QRCodeUI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnGenerateQRImageActionPerformed
   private void printOTP(String otpNum) {
        TTIntegration ttIntgration = null;
        HashMap paramMap = new HashMap();       
        paramMap.put("OTP", otpNum);
        paramMap.put("act_num", txtAccNo.getText());
        ttIntgration.setParam(paramMap);
        ttIntgration.integrationForPrint("qrcode_password", true);
    }
   
   private String getActualQRBankCode() throws Exception{//This query to get actual bank code from branch_master table, for that hardcoded this branch code
        String qrBankCode = "";
        HashMap qrBankCodeMap = new HashMap();
        List lst = ClientUtil.executeQuery("getBankCodeDetails", null);
        if (lst != null && lst.size() > 0) {
            qrBankCodeMap = (HashMap)lst.get(0);
            qrBankCode = CommonUtil.convertObjToStr(qrBankCodeMap.get("BANK_CODE"));
            System.out.println("qrBankCode : "+qrBankCode);
        }
        return qrBankCode;
    }
    
    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        cboProdType.setEnabled(true);
        txtAccNo.setEnabled(true);
        cboProdType.setSelectedItem("");
        btnAccNo.setEnabled(true);
        txtAccNo.setText("");
        btnLoadImage.setEnabled(true);
        btnGenerateQRImage.setEnabled(true);
        lblQRImage.setIcon(null);
        txtLimit.setText("");
        lblAccNameValue.setText("");
        cboCategory.setSelectedItem("");
        btnAccNo.setEnabled(true);
        btnRegeneragte.setEnabled(true);
        cboCategory.setEnabled(true);
        txtAccNo.setEnabled(false);
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnLoadImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadImageActionPerformed
        // TODO add your handling code here:
        try {
            if (cboCategory.getSelectedIndex() == 0) {
                ClientUtil.showAlertWindow("Category should not be empty");
                return;
            }else if (cboProdType.getSelectedIndex() == 0) {
                ClientUtil.showAlertWindow("Product Type should not be empty");
                return;
            } else if (txtAccNo.getText().length() == 0) {
                ClientUtil.showAlertWindow("Account No should not be empty");
                return;
            } else if (txtLimit.getText().length() == 0) {
                ClientUtil.showAlertWindow("Transaction Limit amount should not be empty");
                return;
            } else {
                btnLoadImage.setEnabled(false);
                cboProdType.setEnabled(false);
                btnAccNo.setEnabled(false);
                txtAccNo.setEnabled(false);
                btnGenerateQRImage.setEnabled(false);
                dataMap = new HashMap();
                dataMap.put("QR_ACT_NUM", txtAccNo.getText());
                dataMap.put("QR_BANK", getActualQRBankCode());
                dataMap.put("QR_DETAILS", encrypt.encrypt("Fin" + "," + TrueTransactMain.BANK_ID + txtAccNo.getText() + "," + lblAccNameValue.getText() + "," + "Curo"));
                dataMap.put("QR_FILE", lblQRImage.getIcon());
                dataMap.put("TXN_LIMIT", txtLimit.getText());
                dataMap.put("PROD_TYPE", (String) ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
                dataMap.put("getQRMasterMap", "getQRMasterMap");
                dataMap.put(CommonConstants.MAP_NAME, "getQRMasterMap");
                qrCodeMap.put(CommonConstants.MODULE, getModule());
                qrCodeMap.put(CommonConstants.SCREEN, getScreen());
                qrCodeMap.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
                qrCodeMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                HashMap proxyResultMap = proxy.executeQuery(dataMap, qrCodeMap);
                System.out.println("QR Code image created successfully! : " + proxyResultMap);
                if (proxyResultMap != null && proxyResultMap.containsKey("QR_FILE")) {
                    System.out.println("QR Code image loaded successfully! : " + proxyResultMap.get("QR_FILE"));
                    QRBytes = (byte[]) proxyResultMap.get("QR_FILE");
                    if (QRBytes != null && QRBytes.length > 0) {
                        Icon icon = new ImageIcon(QRBytes);
                        lblQRImage.setIcon(icon);
                    } else {
                        ClientUtil.showAlertWindow("Invalid...");
                    }
                }
                System.out.println("QR Code image loaded successfully! : " + proxyResultMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnLoadImageActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        try {
            if (cboCategory.getSelectedIndex() == 0) {
                ClientUtil.showAlertWindow("Category should not be empty");
                return;
            }else if (cboProdType.getSelectedIndex() == 0) {
                ClientUtil.showAlertWindow("Product Type should not be empty");
                return;
            } else if (txtAccNo.getText().length() == 0) {
                ClientUtil.showAlertWindow("Account No should not be empty");
                return;
            } else if (txtLimit.getText().length() == 0) {
                ClientUtil.showAlertWindow("Transaction Limit amount should not be empty");
                return;
            } else {
                Robot robot = null;
                try {
                    robot = new Robot();
                } catch (AWTException ex) {
                    java.util.logging.Logger.getLogger(QRCodeUI.class.getName()).log(Level.SEVERE, null, ex);
                }
//            PrinterJob pjob = PrinterJob.getPrinterJob();
//            PageFormat pf = pjob.defaultPage();
//            pjob.setPrintable(new BasicPrint(), pf);
//            try {
//            pjob.print();
//            } catch (PrinterException e) {
//            }
//            
//            JScrollPane pane = new JScrollPane(lblQRImage);
//    pane.setPreferredSize(new Dimension(250, 200));
//    f.add("Center", pane);
//    JButton printButton = new JButton("Print This Window");
////    printButton.addActionListener(new PrintUIWindow(f));
//    f.add("South", printButton);
//    f.pack();
//    f.setVisible(true);
//        String format = "png";
//        String fileName = filePath;
                //System.out.println("nithya :: filePath :: " + filePath +"\nAccount no : " + txtAccNo.getText());    
                PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
                pras.add(new Copies(1));
//        PrintService pss[] = PrintServiceLookup.lookupPrintServices(DocFlavor.INPUT_STREAM.GIF, pras);
                PrintService pss = PrintServiceLookup.lookupDefaultPrintService();
                PrintService ps = pss;
                System.out.println("Printing to " + ps);
                DocPrintJob job = ps.createPrintJob();
                //FileInputStream fin = new FileInputStream(filePath); // Commented by nithya on 16-01-2017
                //Doc doc = new SimpleDoc(fin, DocFlavor.INPUT_STREAM.GIF, null);
                //job.print(doc, pras);
                //fin.close(); // Commented by nithya on 16-01-2017
                print(filePath);
//        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
//        DocPrintJob job = service.createPrintJob();
//        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
//        SimpleDoc doc = new SimpleDoc(filePath, flavor, null);
//        job.print(doc, null);
//        lblQRImage.print(lblQRImage.getIcon());
//        printComponentToFile(lblQRImage.getIcon(), true);

//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         setTitle("Online First Person Shooter");

//         ImageIcon image = new ImageIcon(fileName);
//         JLabel label = new JLabel(image);
//         JScrollPane scrollPane = new JScrollPane(label);
//         scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//         scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//         add(scrollPane, BorderLayout.CENTER);
//         pack();

//        CLabel label = new CLabel("This is a test",new ImageIcon(fileName),CLabel.CENTER);
//        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
//        BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
//            ImageIO.write(screenFullImage, format, new File(fileName));
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(QRCodeUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("A full screenshot saved!");
    }//GEN-LAST:event_btnPrintActionPerformed

    private void print(String path) {

        TTIntegration ttIntgration = null;
        HashMap paramMap = new HashMap();
        //paramMap.put("PATH", path); // Commented by nithya on 16-01-2017
        paramMap.put("act_num", txtAccNo.getText());
        ttIntgration.setParam(paramMap);
        ttIntgration.integrationForPrint("qrcode", true);

    }

    private void cboCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboCategoryActionPerformed

    private void txtLimitFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLimitFocusLost
//        tokenChecking();
    }//GEN-LAST:event_txtLimitFocusLost

    private void lblQRImageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblQRImageMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
//            objCustomerUISupport.zoomImage(lblQRImage);
            //            javax.swing.JWindow photoFrame = new javax.swing.JWindow();
            //            javax.swing.JLabel photoLabel = new javax.swing.JLabel();
            //            javax.swing.JLayeredPane jlp = new javax.swing.JLayeredPane();
            //            photoLabel.setIcon(lblPhoto.getIcon());
            //            photoFrame.getContentPane().setLayout(new java.awt.BorderLayout()) ;
            //            jlp.add(photoLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
            //            photoFrame.getContentPane().add("Center", jlp);
            //            photoFrame.setSize(lblPhoto.getIcon().getIconWidth(), lblPhoto.getIcon().getIconHeight());
            //            photoFrame.addMouseListener(new MyMouseListener(photoFrame));
            //            TrueTransactMain.showWindow(photoFrame);
        }
    }//GEN-LAST:event_lblQRImageMouseClicked

    private void btnRegeneragteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegeneragteActionPerformed
        // TODO add your handling code here:
        if (cboCategory.getSelectedIndex() == 0) {
            ClientUtil.showAlertWindow("Category should not be empty");
            return;
        } else if (cboProdType.getSelectedIndex() == 0) {
            ClientUtil.showAlertWindow("Product Type should not be empty");
            return;
        } else if (txtAccNo.getText().length() == 0) {
            ClientUtil.showAlertWindow("Account No should not be empty");
            return;
        } else if (txtLimit.getText().length() == 0) {
            ClientUtil.showAlertWindow("Transaction Limit amount should not be empty");
            return;
        } else {
            try {
                dataMap = new HashMap();
                dataMap.put("QR_ACT_NUM", txtAccNo.getText());
                dataMap.put("QR_BANK", getActualQRBankCode());
                dataMap.put("TXN_LIMIT", txtLimit.getText());
                dataMap.put("PROD_TYPE", (String) ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
                dataMap.put("updateResetOTPQRMasterMap", "updateResetOTPQRMasterMap");
                dataMap.put(CommonConstants.MAP_NAME, "updateResetOTPQRMasterMap");
                qrCodeMap.put(CommonConstants.MODULE, getModule());
                qrCodeMap.put(CommonConstants.SCREEN, getScreen());
                qrCodeMap.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
                qrCodeMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                HashMap proxyResultMap = proxy.execute(dataMap, qrCodeMap);
                System.out.println("QR Code image created successfully! : " + proxyResultMap);
                if (proxyResultMap != null && proxyResultMap.containsKey("STATUS")) {
                    ClientUtil.showAlertWindow("" + proxyResultMap.get("STATUS"));
                }
                // Added by nithya on 09-02-2017 for Print OTP
                    if(proxyResultMap != null){
                    int yesNo = 0;
                    String[] options = {"Yes", "No"};
                    yesNo = COptionPane.showOptionDialog(null, "Do you want to print OTP?", CommonConstants.WARNINGTITLE,
                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                            null, options, options[0]);
                    System.out.println("#$#$$ yesNo : " + yesNo);
                    if (yesNo == 0) {
                         HashMap hash = new HashMap();
                         hash.put("QR_ACT_NUM", txtAccNo.getText());
                         hash.put("QR_BANK", getActualQRBankCode());
                         List lst = ClientUtil.executeQuery("getRecordExistorNotQRMaster", hash);
                         if (lst != null && lst.size() > 0) {
                            HashMap existMap = new HashMap();
                            existMap = (HashMap) lst.get(0);
                            System.out.println("existMap OTP :: " + existMap.get("OTP_NUM"));
                            StringEncrypter encrypt = new StringEncrypter();
                            String otpNum = "";
                            otpNum = CommonUtil.convertObjToStr(existMap.get("OTP_NUM"));                           
                            otpNum = encrypt.decrypt(otpNum);                           
                            printOTP(otpNum);
                        }
                    }
                    }  
                    // End
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(QRCodeUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnRegeneragteActionPerformed

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
        HashMap mapParam = new HashMap();

        HashMap where = new HashMap();
        where.put("beh", "CA");

        mapParam.put("MAPNAME", "getSelectInwardClearingAuthorizeTOList");
        new ViewAll(mapParam).show();
    }

    public void update(Observable observed, Object arg) {
        ((ComboBoxModel) cboProdType.getModel()).setKeyForSelected(observable.getProdType());
        txtAccNo.setText(observable.getTxtAccNo());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccNo;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnGenerateQRImage;
    private com.see.truetransact.uicomponent.CButton btnLoadImage;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnRegeneragte;
    private com.see.truetransact.uicomponent.CComboBox cboCategory;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CLabel lblAccNameValue;
    private com.see.truetransact.uicomponent.CLabel lblAccNo;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblProdType1;
    private com.see.truetransact.uicomponent.CLabel lblQRImage;
    private com.see.truetransact.uicomponent.CPanel panAcctNo;
    private com.see.truetransact.uicomponent.CPanel panSearch1;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel qrCodeImage;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAndOr;
    private com.see.truetransact.uicomponent.CTextField txtAccNo;
    private com.see.truetransact.uicomponent.CTextField txtLimit;
    // End of variables declaration//GEN-END:variables

    public void fillDropdown() throws Exception {
        log.info("In fillDropdown()");

        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookup_keys = new ArrayList();

        lookup_keys.add("APP_CATEGORY");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);

        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get("APP_CATEGORY"));
        cbmCategory = new ComboBoxModel(key, value);
    }

// Get the value from the Hash Map depending on the Value of Key...
    private void getKeyValue(HashMap keyValue) throws Exception {
        log.info("In getKeyValue()");

        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    public static void printComponentToFile(Component comp, boolean fill) throws PrinterException {
        Paper paper = new Paper();
        paper.setSize(8.3 * 72, 11.7 * 72);
        paper.setImageableArea(18, 18, 559, 783);

        PageFormat pf = new PageFormat();
        pf.setPaper(paper);
        pf.setOrientation(PageFormat.LANDSCAPE);

        BufferedImage img = new BufferedImage(
                (int) Math.round(pf.getWidth()),
                (int) Math.round(pf.getHeight()),
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = img.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fill(new Rectangle(0, 0, img.getWidth(), img.getHeight()));
        ComponentPrinter cp = new ComponentPrinter(comp, fill);
        try {
            cp.print(g2d, pf, 0);
        } finally {
            g2d.dispose();
        }

        try {
            ImageIO.write(img, "png", new File("Page-" + (fill ? "Filled" : "") + ".png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static class ComponentPrinter implements Printable {

        private Component comp;
        private boolean fill;

        public ComponentPrinter(Component comp, boolean fill) {
            this.comp = comp;
            this.fill = fill;
        }

        @Override
        public int print(Graphics g, PageFormat format, int page_index) throws PrinterException {

            if (page_index > 0) {
                return Printable.NO_SUCH_PAGE;
            }

            Graphics2D g2 = (Graphics2D) g;
            g2.translate(format.getImageableX(), format.getImageableY());

            double width = (int) Math.floor(format.getImageableWidth());
            double height = (int) Math.floor(format.getImageableHeight());

            if (!fill) {

                width = Math.min(width, comp.getPreferredSize().width);
                height = Math.min(height, comp.getPreferredSize().height);

            }

            comp.setBounds(0, 0, (int) Math.floor(width), (int) Math.floor(height));
            if (comp.getParent() == null) {
                comp.addNotify();
            }
            comp.validate();
            comp.doLayout();
            comp.printAll(g2);
            if (comp.getParent() != null) {
                comp.removeNotify();
            }

            return Printable.PAGE_EXISTS;
        }
    }

    public int print(Graphics g, PageFormat pf, int pageIndex) {
        ImageIcon printImage = new javax.swing.ImageIcon(filePath);
        Graphics2D g2d = (Graphics2D) g;
        g.translate((int) (pf.getImageableX()), (int) (pf.getImageableY()));
        if (pageIndex == 0) {
            double pageWidth = pf.getImageableWidth();
            double pageHeight = pf.getImageableHeight();
            double imageWidth = printImage.getIconWidth();
            double imageHeight = printImage.getIconHeight();
            double scaleX = pageWidth / imageWidth;
            double scaleY = pageHeight / imageHeight;
            double scaleFactor = Math.min(scaleX, scaleY);
            g2d.scale(scaleFactor, scaleFactor);
            g.drawImage(printImage.getImage(), 0, 0, null);
            return Printable.PAGE_EXISTS;
        }
        return Printable.NO_SUCH_PAGE;
    }

    public class BasicPrint extends JComponent implements Printable {

        public int print(Graphics g, PageFormat pf, int pageIndex) {

            double ix = pf.getImageableX();
            double iy = pf.getImageableY();
            double iw = pf.getImageableWidth();
            double ih = pf.getImageableHeight();

            return Printable.NO_SUCH_PAGE;
        }
    }
}
