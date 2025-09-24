/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * QRCodeUI.java
 *
 * Created on August 24, 2003, 1:46 PM
 */
package com.see.truetransact.ui.remittance.rtgs.upi;

import com.see.truetransact.ui.common.viewall.*;
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
import com.see.truetransact.ui.transaction.multipleStanding.MultipleStandingUI;
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
public class UPIQRCodeUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer {

    private final ViewAllRB resourceBundle = new ViewAllRB();
    private UPIQRCodeOB observable;
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

    /**
     * Creates new form ViewAll
     */
    public UPIQRCodeUI() {
        setupInit();
        setupScreen();
        currDt = ClientUtil.getCurrentDate();
        try {
            encrypt = new StringEncrypter();
        } catch (EncryptionException ex) {
            java.util.logging.Logger.getLogger(QRCodeUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        qrCodeMap = new HashMap();
        qrCodeMap.put(CommonConstants.JNDI, "UPIQRCodeJNDI");
        qrCodeMap.put(CommonConstants.HOME, "rtgsneftfiletrans.upi.UPIQRCodeHome");
        qrCodeMap.put(CommonConstants.REMOTE, "rtgsneftfiletrans.upi.UPIQRCode");

        try {
            proxy = ProxyFactory.createProxy();
//            fillDropdown();
        } catch (Exception e) {
            System.err.println("Exception " + e + "Caught");
            e.printStackTrace();
        }
       
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
            observable = new UPIQRCodeOB();
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
        cboBank.setModel(observable.getCbmUpiBank());
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

        rdgAndOr = new com.see.truetransact.uicomponent.CButtonGroup();
        cComboBox1 = new com.see.truetransact.uicomponent.CComboBox();
        cTabbedPane1 = new com.see.truetransact.uicomponent.CTabbedPane();
        panUPI = new com.see.truetransact.uicomponent.CPanel();
        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        lblAccNo = new com.see.truetransact.uicomponent.CLabel();
        lblAccNameValue = new com.see.truetransact.uicomponent.CLabel();
        txtAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccNo = new com.see.truetransact.uicomponent.CButton();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        cboBank = new com.see.truetransact.uicomponent.CComboBox();
        btnGenerateQRImage = new com.see.truetransact.uicomponent.CButton();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        lblUpiId = new com.see.truetransact.uicomponent.CLabel();
        panUPIGeneration = new com.see.truetransact.uicomponent.CPanel();
        qrCodeImage = new com.see.truetransact.uicomponent.CPanel();
        lblQRImage = new com.see.truetransact.uicomponent.CLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(750, 600));
        setPreferredSize(new java.awt.Dimension(780, 580));

        panSearchCondition.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panSearchCondition.setMinimumSize(new java.awt.Dimension(700, 170));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(700, 170));

        lblProdType.setText("Product Type");

        cboProdType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.setPopupWidth(130);
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });

        lblAccNo.setText("Account No.");

        lblAccNameValue.setForeground(new java.awt.Color(0, 51, 204));
        lblAccNameValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAccNameValue.setMaximumSize(new java.awt.Dimension(175, 21));
        lblAccNameValue.setMinimumSize(new java.awt.Dimension(125, 21));
        lblAccNameValue.setPreferredSize(new java.awt.Dimension(175, 21));

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

        btnAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccNo.setToolTipText("Account No.");
        btnAccNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccNoActionPerformed(evt);
            }
        });

        cLabel2.setText("Bank");

        btnGenerateQRImage.setText("Generate UPI & QR Image");
        btnGenerateQRImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateQRImageActionPerformed(evt);
            }
        });

        btnPrint.setText("Print QR");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        cLabel1.setForeground(new java.awt.Color(0, 0, 153));
        cLabel1.setText("UPI ID ");
        cLabel1.setFont(new java.awt.Font("MS Sans Serif", 1, 12)); // NOI18N

        lblUpiId.setForeground(new java.awt.Color(51, 0, 153));
        lblUpiId.setFont(new java.awt.Font("MS Sans Serif", 1, 12)); // NOI18N

        javax.swing.GroupLayout panSearchConditionLayout = new javax.swing.GroupLayout(panSearchCondition);
        panSearchCondition.setLayout(panSearchConditionLayout);
        panSearchConditionLayout.setHorizontalGroup(
            panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSearchConditionLayout.createSequentialGroup()
                .addGroup(panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panSearchConditionLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panSearchConditionLayout.createSequentialGroup()
                                    .addGroup(panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblProdType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblAccNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addGroup(panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(cboBank, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(panSearchConditionLayout.createSequentialGroup()
                                            .addGroup(panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(txtAccNo, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                                                .addComponent(cboProdType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(btnAccNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(lblAccNameValue, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(panSearchConditionLayout.createSequentialGroup()
                                    .addGap(40, 40, 40)
                                    .addComponent(btnGenerateQRImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panSearchConditionLayout.createSequentialGroup()
                                .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblUpiId, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panSearchConditionLayout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panSearchConditionLayout.setVerticalGroup(
            panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSearchConditionLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProdType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboProdType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblAccNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtAccNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnAccNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblAccNameValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboBank, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(btnGenerateQRImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblUpiId, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panUPIGeneration.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panUPIGeneration.setMinimumSize(new java.awt.Dimension(750, 350));
        panUPIGeneration.setPreferredSize(new java.awt.Dimension(750, 350));

        qrCodeImage.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        qrCodeImage.setMinimumSize(new java.awt.Dimension(230, 230));
        qrCodeImage.setPreferredSize(new java.awt.Dimension(230, 230));

        lblQRImage.setMinimumSize(new java.awt.Dimension(225, 225));
        lblQRImage.setPreferredSize(new java.awt.Dimension(225, 225));
        lblQRImage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblQRImageMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout qrCodeImageLayout = new javax.swing.GroupLayout(qrCodeImage);
        qrCodeImage.setLayout(qrCodeImageLayout);
        qrCodeImageLayout.setHorizontalGroup(
            qrCodeImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(qrCodeImageLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblQRImage, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        qrCodeImageLayout.setVerticalGroup(
            qrCodeImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, qrCodeImageLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblQRImage, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panUPIGenerationLayout = new javax.swing.GroupLayout(panUPIGeneration);
        panUPIGeneration.setLayout(panUPIGenerationLayout);
        panUPIGenerationLayout.setHorizontalGroup(
            panUPIGenerationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panUPIGenerationLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(qrCodeImage, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(362, 362, 362))
        );
        panUPIGenerationLayout.setVerticalGroup(
            panUPIGenerationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panUPIGenerationLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(qrCodeImage, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panUPILayout = new javax.swing.GroupLayout(panUPI);
        panUPI.setLayout(panUPILayout);
        panUPILayout.setHorizontalGroup(
            panUPILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panUPILayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panSearchCondition, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(panUPIGeneration, javax.swing.GroupLayout.PREFERRED_SIZE, 394, Short.MAX_VALUE)
                .addContainerGap())
        );
        panUPILayout.setVerticalGroup(
            panUPILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panUPILayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(panUPILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panSearchCondition, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                    .addComponent(panUPIGeneration, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE))
                .addContainerGap())
        );

        cTabbedPane1.addTab("UPI Generation", panUPI);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    boolean btnIntDetPressed = false;

    public void fillData(Object obj) {
        try {
            HashMap hash = (HashMap) obj;
            System.out.println("#$#$ Hash : " + hash);
            txtAccNo.setText(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
            lblAccNameValue.setText(CommonUtil.convertObjToStr(hash.get("CUST_NAME")));
//            hash.put("QR_ACT_NUM", hash.get("ACT_NUM"));
//            hash.put("QR_BANK", getActualQRBankCode());
//            List lst = ClientUtil.executeQuery("getRecordExistorNotQRMaster", hash);
//            if (lst != null && lst.size() > 0) {
//                ClientUtil.showAlertWindow("QR Code already generated for this account");
//                HashMap existMap = new HashMap();
//                existMap = (HashMap) lst.get(0);
//                btnLoadImageActionPerformed();
//                btnGenerateQRImage.setEnabled(false);
//                txtAccNo.setEnabled(false);
//                cboProdType.setEnabled(false);
//                btnAccNo.setEnabled(false);
//            } else {
//                btnGenerateQRImage.setEnabled(true);
//                btnAccNo.setEnabled(false);
//            }
//            txtAccNoFocusLost(null);
        } catch (Exception e) {
        }
    }

    
    private boolean checkQRCreatedForBank(){
        boolean qrCreated = false;
        try {
            HashMap hash = new HashMap();
            System.out.println("#$#$ Hash : " + hash);
            hash.put("QR_ACT_NUM", txtAccNo.getText());
            hash.put("QR_BANK", CommonUtil.convertObjToStr(((ComboBoxModel) cboBank.getModel()).getKeyForSelected()));
            List lst = ClientUtil.executeQuery("getUPIRecordExistorNotForAccount", hash);
            if (lst != null && lst.size() > 0) {
                ClientUtil.showAlertWindow("QR Code already generated for this account with bank : " + CommonUtil.convertObjToStr(cboBank.getSelectedItem()));
                btnLoadImageActionPerformed();
                HashMap upiMap = (HashMap)lst.get(0);
                lblUpiId.setText(CommonUtil.convertObjToStr(upiMap.get("QR_UPI_ID")));
                btnGenerateQRImage.setEnabled(false);
                txtAccNo.setEnabled(false);
                cboProdType.setEnabled(false);
                btnAccNo.setEnabled(false);
                qrCreated = true;
            } else {
                btnGenerateQRImage.setEnabled(true);
                btnAccNo.setEnabled(false);
                qrCreated = false;
            }
            txtAccNoFocusLost(null);
        } catch (Exception e) {
        }
        return qrCreated;
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
    
    
    private boolean createUPIId(){
        String upiFormat = "";
        String upiId = "";
        boolean upiFormatSet = true;
        if (CommonUtil.convertObjToStr(((ComboBoxModel) cboBank.getModel()).getKeyForSelected()).equals("SBI")) {
            if(TrueTransactMain.CBMSPARAMETERS.containsKey("SBI_UPI_FORMAT") && TrueTransactMain.CBMSPARAMETERS.get("SBI_UPI_FORMAT")!= null){
                upiFormat = CommonUtil.convertObjToStr(TrueTransactMain.CBMSPARAMETERS.get("SBI_UPI_FORMAT"));
                upiId = upiFormat.replace("####", txtAccNo.getText());
            }else{
                ClientUtil.showMessageWindow("UPI format not set for " + CommonUtil.convertObjToStr(cboBank.getSelectedItem()));
                upiFormatSet = false;
            }
            
        }else if (CommonUtil.convertObjToStr(((ComboBoxModel) cboBank.getModel()).getKeyForSelected()).equals("ICICI")) {
            if(TrueTransactMain.CBMSPARAMETERS.containsKey("ICICI_UPI_FORMAT") && TrueTransactMain.CBMSPARAMETERS.get("ICICI_UPI_FORMAT")!= null){
                upiFormat = CommonUtil.convertObjToStr(TrueTransactMain.CBMSPARAMETERS.get("ICICI_UPI_FORMAT"));
                upiId = upiFormat.replace("####", txtAccNo.getText());
            }else{
                ClientUtil.showMessageWindow("UPI format not set for " + CommonUtil.convertObjToStr(cboBank.getSelectedItem()));
                upiFormatSet = false;
            }
        }
       lblUpiId.setText(upiId);
       return upiFormatSet;
    }
    
//    public static void generateQRCodeImage(String text, int width, int height, String filePath)
//            throws WriterException, IOException {
//
//        // Generate the QR code matrix
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
//        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
//
//        // Text to add below QR code
//        String footerText = "bank - Kooroppada";
//        Font font = new Font("Arial", Font.PLAIN, 16);
//        FontMetrics metrics = new Canvas().getFontMetrics(font);
//        int textHeight = metrics.getHeight();
//        int textWidth = metrics.stringWidth(footerText);
//
//        // Create final image with space for text
//        int finalImageWidth = Math.max(width, textWidth + 20);
//        int finalImageHeight = height + textHeight + 10;
//        BufferedImage finalImage = new BufferedImage(finalImageWidth, finalImageHeight, BufferedImage.TYPE_INT_RGB);
//        
//        ImageIcon icon = new ImageIcon(image);
//        
//        lblQRImage.setIcon(icon);
//
//// Create or update a JLabel to display the image
//
//
////        // Draw on the final image
////        Graphics2D g = finalImage.createGraphics();
////        g.setColor(Color.WHITE);
////        g.fillRect(0, 0, finalImageWidth, finalImageHeight);
////        g.drawImage(qrImage, (finalImageWidth - width) / 2, 0, null);
////
////        g.setColor(Color.BLACK);
////        g.setFont(font);
////        g.drawString(footerText, (finalImageWidth - textWidth) / 2, height + textHeight); // draw below QR
////
////        g.dispose();
////
////        // Save the final image
////        ImageIO.write(finalImage, "PNG", new File(filePath));
//    }


    private void btnGenerateQRImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateQRImageActionPerformed
        try {
            String upiBankCode = "";
            String ifscCode = "";
            if (cboProdType.getSelectedIndex() == 0) {
                ClientUtil.showAlertWindow("Product Type should not be empty");
                return;
            } else if (txtAccNo.getText().length() == 0) {
                ClientUtil.showAlertWindow("Account No should not be empty");
                return;
            } else if (cboBank.getSelectedIndex() == 0) {
                ClientUtil.showAlertWindow("Bank should not be empty");
                return;
            } else {
                cboProdType.setEnabled(false);
                btnAccNo.setEnabled(false);
                txtAccNo.setEnabled(false);
                btnGenerateQRImage.setEnabled(false);
                try {
                    if (!checkQRCreatedForBank()) {
                        if (createUPIId()) {
                            dataMap = new HashMap();
                            dataMap.put("QR_ACT_NUM", txtAccNo.getText());
                            dataMap.put("QR_ACT_NAME", lblAccNameValue.getText());
                            dataMap.put("QR_BANK", (String) ((ComboBoxModel) cboBank.getModel()).getKeyForSelected());
                            dataMap.put("QR_FILE", "");
                            dataMap.put("PROD_TYPE", (String) ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
                            dataMap.put("UPI_ID", lblUpiId.getText());
                            dataMap.put("UPI_NAME", lblAccNameValue.getText());
                            if (CommonUtil.convertObjToStr(((ComboBoxModel) cboBank.getModel()).getKeyForSelected()).equals("SBI")) {
                                System.out.println("Execute hereee :: " + CommonUtil.convertObjToStr(TrueTransactMain.CBMSPARAMETERS.get("SBI_UPI_BANK_CODE")));
                                upiBankCode = CommonUtil.convertObjToStr(TrueTransactMain.CBMSPARAMETERS.get("SBI_UPI_BANK_CODE"));
                            }else if (CommonUtil.convertObjToStr(((ComboBoxModel) cboBank.getModel()).getKeyForSelected()).equals("ICICI")) {
                                upiBankCode = CommonUtil.convertObjToStr(TrueTransactMain.CBMSPARAMETERS.get("ICICI_UPI_BANK_CODE"));
                            }
                            dataMap.put("UPI_BANK_CODE", upiBankCode);
                            HashMap ifscMap = new HashMap();
                            ifscMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                            List ifscList = ClientUtil.executeQuery("getBranchIFSCodeDetails", ifscMap);
                            if (ifscList != null && ifscList.size() > 0) {
                                ifscMap = (HashMap) ifscList.get(0);
                                ifscCode = CommonUtil.convertObjToStr(ifscMap.get("IFSC_CODE"));
                            }
                            dataMap.put("QR_IFSC", ifscCode);
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
                                ClientUtil.showAlertWindow("Selected Account QR Image already exist");
                                btnLoadImageActionPerformed();
                                btnGenerateQRImage.setEnabled(true);
                                cboProdType.setEnabled(true);
                                btnAccNo.setEnabled(true);
                            }
                        System.out.println("QR Code image created successfully! : " + proxyResultMap);
                       }
                    }
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
        btnGenerateQRImage.setEnabled(true);
        lblQRImage.setIcon(null);
        lblAccNameValue.setText("");
        btnAccNo.setEnabled(true);
        txtAccNo.setEnabled(false);
        lblUpiId.setText("");
        cboBank.setSelectedIndex(0);
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnLoadImageActionPerformed(){
         try {
           if (cboProdType.getSelectedIndex() == 0) {
                ClientUtil.showAlertWindow("Product Type should not be empty");
                return;
            } else if (txtAccNo.getText().length() == 0) {
                ClientUtil.showAlertWindow("Account No should not be empty");
                return;
            }else if (cboBank.getSelectedIndex() == 0) {
                ClientUtil.showAlertWindow("Bank should not be empty");
                return;
            }   else {
                cboProdType.setEnabled(false);
                btnAccNo.setEnabled(false);
                txtAccNo.setEnabled(false);
                btnGenerateQRImage.setEnabled(false);
                dataMap = new HashMap();
                dataMap.put("QR_ACT_NUM", txtAccNo.getText());
                dataMap.put("QR_BANK", (String) ((ComboBoxModel) cboBank.getModel()).getKeyForSelected());               
                dataMap.put("QR_FILE", lblQRImage.getIcon());
                dataMap.put("PROD_TYPE", (String) ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
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
    }
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        if (lblUpiId.getText().length() > 0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("actNum", CommonUtil.convertObjToStr(txtAccNo.getText()));
            paramMap.put("upiId", CommonUtil.convertObjToStr(lblUpiId.getText()));
            paramMap.put("actName", CommonUtil.convertObjToStr(lblAccNameValue.getText()));
            paramMap.put("bankName", CommonUtil.convertObjToStr(cboBank.getSelectedItem()));
            System.out.println("####### paramMap" + paramMap);
            ttIntgration.setParam(paramMap);
            ttIntgration.integrationForPrint("UPIQRCodePrint", true);

        }
    }//GEN-LAST:event_btnPrintActionPerformed

    private void print(String path) {

        TTIntegration ttIntgration = null;
        HashMap paramMap = new HashMap();
        //paramMap.put("PATH", path); // Commented by nithya on 16-01-2017
        paramMap.put("act_num", txtAccNo.getText());
        ttIntgration.setParam(paramMap);
        ttIntgration.integrationForPrint("qrcode", true);

    }

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
             try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        MultipleStandingUI gui = new MultipleStandingUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();   
        
    }

    public void update(Observable observed, Object arg) {
        ((ComboBoxModel) cboProdType.getModel()).setKeyForSelected(observable.getProdType());
        txtAccNo.setText(observable.getTxtAccNo());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccNo;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnGenerateQRImage;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CComboBox cComboBox1;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CTabbedPane cTabbedPane1;
    private com.see.truetransact.uicomponent.CComboBox cboBank;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CLabel lblAccNameValue;
    private com.see.truetransact.uicomponent.CLabel lblAccNo;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblQRImage;
    private com.see.truetransact.uicomponent.CLabel lblUpiId;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panUPI;
    private com.see.truetransact.uicomponent.CPanel panUPIGeneration;
    private com.see.truetransact.uicomponent.CPanel qrCodeImage;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAndOr;
    private com.see.truetransact.uicomponent.CTextField txtAccNo;
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
