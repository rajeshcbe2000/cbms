/*
 * ElectronicPaymentUI.java
 *
 * Created on Nov 12, 2012019, 10:54 AM.
 */
package com.see.truetransact.ui.transaction.electronicpayment;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uivalidation.CurrencyValidation;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.deposit.TermDepositUI;
import com.see.truetransact.ui.TrueTransactMain;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Date;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CFrame;
import com.see.truetransact.uicomponent.CLabel;
import com.see.truetransact.uicomponent.COptionPane;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.SwingWorker;

/**
 *
 * @author Sathiya
 */
public class ElectronicPaymentUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    private HashMap mandatoryMap;
    ElectronicPaymentOB observable;
    final String AUTHORISE = "Authorise";
    private String AuthType = new String();
    final int EDIT = 0, DELETE = 1, ACCNUMBER = 2, AUTHORIZE = 3, VIEW = 4, FROM_UTR = 5, TO_UTR = 6;
    int viewType = -1, updateTab = -1;
    boolean isFilled = false;
    private final static Logger log = Logger.getLogger(ElectronicPaymentUI.class);
    Date curDate = null;

    /**
     * Creates new form ElectronicPaymentUI
     */
    public ElectronicPaymentUI() {
        initComponents();
        initSetup();
    }

    private void initSetup() {
        setFieldNames();
        internationalize();
        setMaxLenths();
        setMandatoryHashMap();
        setObservable();
        initComponentData();
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        resetUI();
        setHelpMessage();
        tabElectrionicPayment.resetVisits();
        btnDelete.setVisible(false);
        curDate = ClientUtil.getCurrentDate();
        tbrHead.setVisible(false);
        removeButtons();
        chkSelectAll.setEnabled(true);
        tdtFromDt.setDateValue(CommonUtil.convertObjToStr(curDate));
        tdtToDt.setDateValue(CommonUtil.convertObjToStr(curDate));
        cboElectronicType.setEnabled(true);
        tdtFromDt.setEnabled(true);
        tdtToDt.setEnabled(true);
        btnAcctNum.setEnabled(false);
    }

    private void setObservable() {
        try {
            observable = new ElectronicPaymentOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    }

    /*
     * Auto Generated Method - internationalize() This method used to assign
     * display texts from the Resource Bundle File.
     */
    private void internationalize() {

    }

    /*
     * Auto Generated Method - setMandatoryHashMap() This method list out all
     * the Input Fields available in the UI. It needs a class level HashMap
     * variable mandatoryMap.
     */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
    }

    /*
     * Auto Generated Method - getMandatoryHashMap() Getter method for
     * setMandatoryHashMap().
     */
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {

    }

    public void updateOBFields() {
        observable.setModule(getModule());
        observable.setScreen(getScreen());
    }

    /*
     * Auto Generated Method - setHelpMessage() This method shows tooltip help
     * for all the input fields available in the UI. It needs the Mandatory
     * Resource Bundle object. Help display Label name should be lblMsg.
     */
    public void setHelpMessage() {

    }

    // To fill the Data into the Combo Boxes...
    // it invokes the Combo Box model defined in OB class...
    private void initComponentData() {
        tblElectrionicPaymentLst.setModel(observable.getElectrionicPaymentTable());
        cboElectronicType.setModel(observable.getCbmElectronicType());
    }

    private void setMaxLenths() {
        txtTotDepAmt.setValidation(new CurrencyValidation());
    }

    private void removeButtons() {
        btnNew.setVisible(false);
        btnEdit.setVisible(false);
        btnDelete.setVisible(false);
        btnSave.setVisible(false);
        btnException.setEnabled(false);

    }

    // To set The Status of the Buttons Depending on the Condition...
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
        btnView.setEnabled(!btnView.isEnabled());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgInterestType = new com.see.truetransact.uicomponent.CButtonGroup();
        tabElectrionicPayment = new com.see.truetransact.uicomponent.CTabbedPane();
        panElectrionicPayment = new com.see.truetransact.uicomponent.CPanel();
        panElectrionicPaymentDetails = new com.see.truetransact.uicomponent.CPanel();
        btnProcess1 = new com.see.truetransact.uicomponent.CButton();
        lblMultiDepDate1 = new com.see.truetransact.uicomponent.CLabel();
        lblElectronicPaymentDtVal1 = new com.see.truetransact.uicomponent.CLabel();
        lblMultiDepDate = new com.see.truetransact.uicomponent.CLabel();
        lblElectronicPaymentDtVal = new com.see.truetransact.uicomponent.CLabel();
        panCustIdDetails2 = new com.see.truetransact.uicomponent.CPanel();
        txtBatchId = new com.see.truetransact.uicomponent.CTextField();
        btnBatchId = new com.see.truetransact.uicomponent.CButton();
        lblBatchId = new com.see.truetransact.uicomponent.CLabel();
        panElectrionicPaymentLst = new com.see.truetransact.uicomponent.CPanel();
        srpTable_ElectrionicPaymentLst = new com.see.truetransact.uicomponent.CScrollPane();
        tblElectrionicPaymentLst = new com.see.truetransact.uicomponent.CTable();
        panTotalDeatils = new com.see.truetransact.uicomponent.CPanel();
        lblSelectedBills = new com.see.truetransact.uicomponent.CLabel();
        lblTotalDepAmt = new com.see.truetransact.uicomponent.CLabel();
        lblTotalDepAmount = new com.see.truetransact.uicomponent.CLabel();
        txtTotDepAmt = new com.see.truetransact.uicomponent.CTextField();
        txtTotAccounts = new com.see.truetransact.uicomponent.CTextField();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        btnClose1 = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        panSelectAll = new com.see.truetransact.uicomponent.CPanel();
        lblSelectAll = new com.see.truetransact.uicomponent.CLabel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        panElectrionicInquiry = new com.see.truetransact.uicomponent.CPanel();
        panInquiryDetails = new com.see.truetransact.uicomponent.CPanel();
        btnInquiryClear = new com.see.truetransact.uicomponent.CButton();
        panAcctNo = new com.see.truetransact.uicomponent.CPanel();
        lblAcctNum = new com.see.truetransact.uicomponent.CLabel();
        panAcctNumDetails = new com.see.truetransact.uicomponent.CPanel();
        txtAcctNum = new com.see.truetransact.uicomponent.CTextField();
        btnAcctNum = new com.see.truetransact.uicomponent.CButton();
        cboElectronicType = new com.see.truetransact.uicomponent.CComboBox();
        tdtToDt = new com.see.truetransact.uicomponent.CDateField();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDt = new com.see.truetransact.uicomponent.CDateField();
        btnInquiryProcess = new com.see.truetransact.uicomponent.CButton();
        panElectrionicInquiryLst = new com.see.truetransact.uicomponent.CPanel();
        srpTable_ElectrionicInquiryLst = new com.see.truetransact.uicomponent.CScrollPane();
        tblElectrionicInquirytLst = new com.see.truetransact.uicomponent.CTable();
        tbrHead = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
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
        setMinimumSize(new java.awt.Dimension(990, 600));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(990, 600));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        tabElectrionicPayment.setMinimumSize(new java.awt.Dimension(990, 550));
        tabElectrionicPayment.setPreferredSize(new java.awt.Dimension(990, 550));

        panElectrionicPayment.setMinimumSize(new java.awt.Dimension(975, 500));
        panElectrionicPayment.setPreferredSize(new java.awt.Dimension(975, 500));
        panElectrionicPayment.setLayout(new java.awt.GridBagLayout());

        panElectrionicPaymentDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panElectrionicPaymentDetails.setMinimumSize(new java.awt.Dimension(975, 40));
        panElectrionicPaymentDetails.setPreferredSize(new java.awt.Dimension(975, 40));
        panElectrionicPaymentDetails.setLayout(new java.awt.GridBagLayout());

        btnProcess1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnProcess1.setText("DISPLAY");
        btnProcess1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcess1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panElectrionicPaymentDetails.add(btnProcess1, gridBagConstraints);

        lblMultiDepDate1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMultiDepDate1.setText("Screen Name");
        lblMultiDepDate1.setMaximumSize(new java.awt.Dimension(115, 18));
        lblMultiDepDate1.setMinimumSize(new java.awt.Dimension(115, 18));
        lblMultiDepDate1.setPreferredSize(new java.awt.Dimension(115, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panElectrionicPaymentDetails.add(lblMultiDepDate1, gridBagConstraints);

        lblElectronicPaymentDtVal1.setForeground(new java.awt.Color(153, 0, 0));
        lblElectronicPaymentDtVal1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblElectronicPaymentDtVal1.setText("Screen Name");
        lblElectronicPaymentDtVal1.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblElectronicPaymentDtVal1.setMaximumSize(new java.awt.Dimension(100, 20));
        lblElectronicPaymentDtVal1.setMinimumSize(new java.awt.Dimension(200, 20));
        lblElectronicPaymentDtVal1.setPreferredSize(new java.awt.Dimension(250, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panElectrionicPaymentDetails.add(lblElectronicPaymentDtVal1, gridBagConstraints);

        lblMultiDepDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMultiDepDate.setText("Transaction Date ");
        lblMultiDepDate.setMaximumSize(new java.awt.Dimension(115, 18));
        lblMultiDepDate.setMinimumSize(new java.awt.Dimension(115, 18));
        lblMultiDepDate.setPreferredSize(new java.awt.Dimension(115, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panElectrionicPaymentDetails.add(lblMultiDepDate, gridBagConstraints);

        lblElectronicPaymentDtVal.setForeground(new java.awt.Color(153, 0, 0));
        lblElectronicPaymentDtVal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblElectronicPaymentDtVal.setText("Trans Date");
        lblElectronicPaymentDtVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblElectronicPaymentDtVal.setMaximumSize(new java.awt.Dimension(100, 20));
        lblElectronicPaymentDtVal.setMinimumSize(new java.awt.Dimension(100, 20));
        lblElectronicPaymentDtVal.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panElectrionicPaymentDetails.add(lblElectronicPaymentDtVal, gridBagConstraints);

        panCustIdDetails2.setFocusable(false);
        panCustIdDetails2.setMinimumSize(new java.awt.Dimension(125, 24));
        panCustIdDetails2.setPreferredSize(new java.awt.Dimension(125, 24));
        panCustIdDetails2.setLayout(new java.awt.GridBagLayout());

        txtBatchId.setAllowAll(true);
        txtBatchId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBatchId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBatchIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustIdDetails2.add(txtBatchId, gridBagConstraints);

        btnBatchId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBatchId.setToolTipText("Select Customer");
        btnBatchId.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBatchId.setMaximumSize(new java.awt.Dimension(21, 21));
        btnBatchId.setMinimumSize(new java.awt.Dimension(21, 21));
        btnBatchId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBatchId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatchIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panCustIdDetails2.add(btnBatchId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panElectrionicPaymentDetails.add(panCustIdDetails2, gridBagConstraints);

        lblBatchId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBatchId.setText("Batch Id");
        lblBatchId.setMaximumSize(new java.awt.Dimension(50, 18));
        lblBatchId.setMinimumSize(new java.awt.Dimension(50, 18));
        lblBatchId.setPreferredSize(new java.awt.Dimension(125, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panElectrionicPaymentDetails.add(lblBatchId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panElectrionicPayment.add(panElectrionicPaymentDetails, gridBagConstraints);

        panElectrionicPaymentLst.setBorder(javax.swing.BorderFactory.createTitledBorder("Electronic Payment List"));
        panElectrionicPaymentLst.setMinimumSize(new java.awt.Dimension(975, 375));
        panElectrionicPaymentLst.setPreferredSize(new java.awt.Dimension(975, 375));
        panElectrionicPaymentLst.setLayout(new java.awt.GridBagLayout());

        tblElectrionicPaymentLst.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl. No", "Account No", "Trans Dt", "Name", "Product Id", "Amount", "Trans Id", "Particulars", "Verified"
            }
        ));
        tblElectrionicPaymentLst.setPreferredScrollableViewportSize(new java.awt.Dimension(450, 350));
        tblElectrionicPaymentLst.setSelectionBackground(new java.awt.Color(255, 255, 51));
        tblElectrionicPaymentLst.setSelectionForeground(new java.awt.Color(153, 0, 0));
        tblElectrionicPaymentLst.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblElectrionicPaymentLstMouseClicked(evt);
            }
        });
        srpTable_ElectrionicPaymentLst.setViewportView(tblElectrionicPaymentLst);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panElectrionicPaymentLst.add(srpTable_ElectrionicPaymentLst, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panElectrionicPayment.add(panElectrionicPaymentLst, gridBagConstraints);
        panElectrionicPaymentLst.getAccessibleContext().setAccessibleName("");

        panTotalDeatils.setMinimumSize(new java.awt.Dimension(860, 40));
        panTotalDeatils.setPreferredSize(new java.awt.Dimension(860, 40));
        panTotalDeatils.setLayout(new java.awt.GridBagLayout());

        lblSelectedBills.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTotalDeatils.add(lblSelectedBills, gridBagConstraints);

        lblTotalDepAmt.setText("No. Of Accounts : ");
        lblTotalDepAmt.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalDeatils.add(lblTotalDepAmt, gridBagConstraints);

        lblTotalDepAmount.setText("Total Amount : ");
        lblTotalDepAmount.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalDeatils.add(lblTotalDepAmount, gridBagConstraints);

        txtTotDepAmt.setForeground(new java.awt.Color(153, 0, 0));
        txtTotDepAmt.setDisabledTextColor(new java.awt.Color(204, 0, 0));
        txtTotDepAmt.setEnabled(false);
        txtTotDepAmt.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        txtTotDepAmt.setMinimumSize(new java.awt.Dimension(180, 21));
        txtTotDepAmt.setPreferredSize(new java.awt.Dimension(180, 21));
        txtTotDepAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTotDepAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalDeatils.add(txtTotDepAmt, gridBagConstraints);

        txtTotAccounts.setForeground(new java.awt.Color(153, 0, 0));
        txtTotAccounts.setDisabledTextColor(new java.awt.Color(204, 0, 0));
        txtTotAccounts.setEnabled(false);
        txtTotAccounts.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        txtTotAccounts.setMinimumSize(new java.awt.Dimension(80, 21));
        txtTotAccounts.setPreferredSize(new java.awt.Dimension(80, 21));
        txtTotAccounts.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTotAccountsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalDeatils.add(txtTotAccounts, gridBagConstraints);

        btnProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnProcess.setText("PROCESS");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalDeatils.add(btnProcess, gridBagConstraints);

        btnClose1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose1.setText("Close");
        btnClose1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClose1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalDeatils.add(btnClose1, gridBagConstraints);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalDeatils.add(btnClear, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panElectrionicPayment.add(panTotalDeatils, gridBagConstraints);

        panSelectAll.setMinimumSize(new java.awt.Dimension(102, 27));
        panSelectAll.setPreferredSize(new java.awt.Dimension(102, 27));
        panSelectAll.setLayout(new java.awt.GridBagLayout());

        lblSelectAll.setText("Select All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 19, 5, 0);
        panSelectAll.add(lblSelectAll, gridBagConstraints);

        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 5, 1);
        panSelectAll.add(chkSelectAll, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panElectrionicPayment.add(panSelectAll, gridBagConstraints);

        tabElectrionicPayment.addTab("Pending Payment", panElectrionicPayment);

        panElectrionicInquiry.setMinimumSize(new java.awt.Dimension(975, 500));
        panElectrionicInquiry.setPreferredSize(new java.awt.Dimension(975, 500));
        panElectrionicInquiry.setLayout(new java.awt.GridBagLayout());

        panInquiryDetails.setMinimumSize(new java.awt.Dimension(990, 32));
        panInquiryDetails.setPreferredSize(new java.awt.Dimension(990, 32));
        panInquiryDetails.setLayout(new java.awt.GridBagLayout());

        btnInquiryClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnInquiryClear.setText("Clear");
        btnInquiryClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInquiryClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInquiryDetails.add(btnInquiryClear, gridBagConstraints);

        panAcctNo.setMinimumSize(new java.awt.Dimension(200, 25));
        panAcctNo.setPreferredSize(new java.awt.Dimension(200, 25));
        panAcctNo.setLayout(new java.awt.GridBagLayout());

        lblAcctNum.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAcctNum.setText("Account No");
        lblAcctNum.setMaximumSize(new java.awt.Dimension(50, 18));
        lblAcctNum.setMinimumSize(new java.awt.Dimension(70, 18));
        lblAcctNum.setPreferredSize(new java.awt.Dimension(70, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panAcctNo.add(lblAcctNum, gridBagConstraints);

        panAcctNumDetails.setFocusable(false);
        panAcctNumDetails.setMinimumSize(new java.awt.Dimension(125, 24));
        panAcctNumDetails.setPreferredSize(new java.awt.Dimension(125, 24));
        panAcctNumDetails.setLayout(new java.awt.GridBagLayout());

        txtAcctNum.setAllowAll(true);
        txtAcctNum.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAcctNum.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAcctNumFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcctNumDetails.add(txtAcctNum, gridBagConstraints);

        btnAcctNum.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAcctNum.setToolTipText("Select Customer");
        btnAcctNum.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAcctNum.setMaximumSize(new java.awt.Dimension(21, 21));
        btnAcctNum.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAcctNum.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAcctNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcctNumActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panAcctNumDetails.add(btnAcctNum, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panAcctNo.add(panAcctNumDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInquiryDetails.add(panAcctNo, gridBagConstraints);

        cboElectronicType.setMinimumSize(new java.awt.Dimension(97, 22));
        cboElectronicType.setPopupWidth(250);
        cboElectronicType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboElectronicTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInquiryDetails.add(cboElectronicType, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInquiryDetails.add(tdtToDt, gridBagConstraints);

        lblToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInquiryDetails.add(lblToDate, gridBagConstraints);

        lblFromDate.setText("Accepted Payment From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInquiryDetails.add(lblFromDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInquiryDetails.add(tdtFromDt, gridBagConstraints);

        btnInquiryProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnInquiryProcess.setText("DISPLAY");
        btnInquiryProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInquiryProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInquiryDetails.add(btnInquiryProcess, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panElectrionicInquiry.add(panInquiryDetails, gridBagConstraints);

        panElectrionicInquiryLst.setBorder(javax.swing.BorderFactory.createTitledBorder("Electronic Payment List"));
        panElectrionicInquiryLst.setMinimumSize(new java.awt.Dimension(975, 425));
        panElectrionicInquiryLst.setPreferredSize(new java.awt.Dimension(975, 425));
        panElectrionicInquiryLst.setLayout(new java.awt.GridBagLayout());

        tblElectrionicInquirytLst.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl. No", "Account No", "Trans Dt", "Name", "Product Id", "Amount", "Trans Id", "Particulars", "Verified"
            }
        ));
        tblElectrionicInquirytLst.setPreferredScrollableViewportSize(new java.awt.Dimension(450, 350));
        tblElectrionicInquirytLst.setSelectionBackground(new java.awt.Color(255, 255, 51));
        tblElectrionicInquirytLst.setSelectionForeground(new java.awt.Color(153, 0, 0));
        tblElectrionicInquirytLst.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblElectrionicInquirytLstMouseClicked(evt);
            }
        });
        srpTable_ElectrionicInquiryLst.setViewportView(tblElectrionicInquirytLst);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panElectrionicInquiryLst.add(srpTable_ElectrionicInquiryLst, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panElectrionicInquiry.add(panElectrionicInquiryLst, gridBagConstraints);

        tabElectrionicPayment.addTab("Status Inquiry", panElectrionicInquiry);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        getContentPane().add(tabElectrionicPayment, gridBagConstraints);
        tabElectrionicPayment.getAccessibleContext().setAccessibleName("InterestMaintenance");

        tbrHead.setMinimumSize(new java.awt.Dimension(980, 29));
        tbrHead.setPreferredSize(new java.awt.Dimension(980, 29));

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
        tbrHead.add(btnView);

        lblSpace5.setText("     ");
        tbrHead.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrHead.add(btnNew);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrHead.add(btnEdit);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrHead.add(btnDelete);

        lblSpace2.setText("     ");
        tbrHead.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrHead.add(btnSave);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrHead.add(btnCancel);

        lblSpace3.setText("     ");
        tbrHead.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrHead.add(btnAuthorize);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrHead.add(btnReject);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrHead.add(btnException);

        lblSpace4.setText("     ");
        tbrHead.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrHead.add(btnPrint);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrHead.add(btnClose);

        getContentPane().add(tbrHead, new java.awt.GridBagConstraints());

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

    private void resetUI() {
        observable.resetForm();
    }

    private void resetInquiryUI() {
        observable.resetInquiryTableValues();
        cboElectronicType.setSelectedItem("");
        tdtFromDt.setDateValue("");
        tdtToDt.setDateValue("");
        txtAcctNum.setText("");
        cboElectronicType.setEnabled(true);
        tblElectrionicInquirytLst.setModel(observable.getElectrionicInquiryTable());
        tableDataAlignmentInquiry();
        setSizeTableDataInquiry();
        tdtFromDt.setEnabled(true);
        tdtToDt.setEnabled(true);
        tdtFromDt.setDateValue(CommonUtil.convertObjToStr(curDate));
        tdtToDt.setDateValue(CommonUtil.convertObjToStr(curDate));
    }

    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        if (field == VIEW) {
            viewType = field;
            viewMap.put(CommonConstants.MAP_NAME, "getElectronicPaymentList");
        } else if (field == FROM_UTR) {
            whereMap.put("FILTERED_LIST", "");
            viewType = field;
            viewMap.put(CommonConstants.MAP_NAME, "getElectronicPaymentDetails");
        } else if (field == TO_UTR) {
            viewType = field;
            whereMap.put("FILTERED_LIST", "");
            viewMap.put(CommonConstants.MAP_NAME, "getElectronicPaymentDetails");
        } else if (field == EDIT) {
            viewType = field;
            whereMap.put("FILTERED_LIST", "");
            viewMap.put(CommonConstants.MAP_NAME, "getElectronicInquiryList");
        }
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        new ViewAll(this, viewMap, false).show();
    }

    public void fillData(Object param) {
        setModified(true);
        isFilled = true;
        final HashMap hash = (HashMap) param;
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                || viewType == EDIT || viewType == VIEW || viewType == FROM_UTR || viewType == TO_UTR) {
            System.out.println("in Filldata.... " + hash);
            if (viewType == VIEW) {
                txtBatchId.setText(CommonUtil.convertObjToStr(hash.get("TRANS_ID")));
                lblElectronicPaymentDtVal1.setText(CommonUtil.convertObjToStr(hash.get("SOURCE_SCREEN")));
                lblElectronicPaymentDtVal.setText(CommonUtil.convertObjToStr(hash.get("TRANS_DT")));
            } else if (viewType == EDIT) {
                txtAcctNum.setText(CommonUtil.convertObjToStr(hash.get("ACCT_NUM")));
            }
            btnCancel.setEnabled(true);
        }
    }

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void setRightAlignment(int col) {
        javax.swing.table.DefaultTableCellRenderer r = new javax.swing.table.DefaultTableCellRenderer();
        r.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tblElectrionicPaymentLst.getColumnModel().getColumn(col).setCellRenderer(r);
        tblElectrionicPaymentLst.getColumnModel().getColumn(col).sizeWidthToFit();
    }

    /**
     * Exit the Application
     */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        //System.out.printlnexit(0);
    }//GEN-LAST:event_exitForm
    public boolean realizeOrNotValidation() {
        boolean Realize = false;
        for (int i = 0; i < tblElectrionicPaymentLst.getRowCount(); i++) {
            if (CommonUtil.convertObjToStr(tblElectrionicPaymentLst.getValueAt(i, 0)).equals("true") && CommonUtil.convertObjToStr(tblElectrionicPaymentLst.getValueAt(i, 7)).equals("No")) {
                Realize = true;
            }
        }
        return Realize;
    }

    private void tblElectrionicPaymentLstMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblElectrionicPaymentLstMouseClicked
        // TODO add your handling code here:         
        if (tblElectrionicPaymentLst.getRowCount() > 0 && tblElectrionicPaymentLst.getSelectedRow() >= 0) {   //Added By  R
            if (tblElectrionicPaymentLst.getSelectedColumn() == 0) {
                String st = CommonUtil.convertObjToStr(tblElectrionicPaymentLst.getValueAt(tblElectrionicPaymentLst.getSelectedRow(), 0));
                if (st.equals("true")) {
                    tblElectrionicPaymentLst.setValueAt(new Boolean(false), tblElectrionicPaymentLst.getSelectedRow(), 0);
                } else {
                    tblElectrionicPaymentLst.setValueAt(new Boolean(true), tblElectrionicPaymentLst.getSelectedRow(), 0);
                }
            }
            double totAmount = 0;
            int totCount = 0;
            for (int i = 0; i < tblElectrionicPaymentLst.getRowCount(); i++) {
                String st = CommonUtil.convertObjToStr(tblElectrionicPaymentLst.getValueAt(i, 0));
                if (st.equals("true")) {
                    totAmount = totAmount + CommonUtil.convertObjToDouble(tblElectrionicPaymentLst.getValueAt(i, 6)).doubleValue();
                    totCount = totCount + 1;
                }
            }
            txtTotDepAmt.setText(CurrencyValidation.formatCrore(String.valueOf(totAmount)));
            txtTotAccounts.setText(String.valueOf(totCount));
        }
    }//GEN-LAST:event_tblElectrionicPaymentLstMouseClicked


    private void txtTotDepAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTotDepAmtFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotDepAmtFocusLost
    
    private void tableDataAlignmentPayment() {
        ArrayList _heading = new ArrayList();
        if (observable.getElectronicPaymentList() != null && observable.getElectronicPaymentList().size() > 0) {
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(tblElectrionicPaymentLst);

            com.see.truetransact.clientutil.TableModel tableModel = new com.see.truetransact.clientutil.TableModel();
            _heading.add("Select");//0
            _heading.add("Sl. No");//0
            _heading.add("Account No");
            _heading.add("Trans Dt");
            _heading.add("Name");
            _heading.add("Product Id");
            _heading.add("Amount");
            _heading.add("UTR Number");
            _heading.add("Particulars");
            _heading.add("IFS Code");
            _heading.add("Sender Act Num");
            _heading.add("Bank Name");
            _heading.add("Branch Name");
            tableModel.setHeading(_heading);
            tableModel.setData((ArrayList) observable.getElectronicPaymentList());
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            tblElectrionicPaymentLst.setAutoResizeMode(0);
            tblElectrionicPaymentLst.doLayout();
            tblElectrionicPaymentLst.setModel(tableSorter);
            tblElectrionicPaymentLst.revalidate();
        }
    }

    private void setSizeTableDataPayment() {
        tblElectrionicPaymentLst.getColumnModel().getColumn(0).setPreferredWidth(100);
        tblElectrionicPaymentLst.getColumnModel().getColumn(1).setPreferredWidth(50);
        tblElectrionicPaymentLst.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblElectrionicPaymentLst.getColumnModel().getColumn(3).setPreferredWidth(75);
        tblElectrionicPaymentLst.getColumnModel().getColumn(4).setPreferredWidth(250);
        tblElectrionicPaymentLst.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblElectrionicPaymentLst.getColumnModel().getColumn(6).setPreferredWidth(100);
        tblElectrionicPaymentLst.getColumnModel().getColumn(7).setPreferredWidth(100);
        tblElectrionicPaymentLst.getColumnModel().getColumn(8).setPreferredWidth(200);
        tblElectrionicPaymentLst.getColumnModel().getColumn(9).setPreferredWidth(100);
        tblElectrionicPaymentLst.getColumnModel().getColumn(10).setPreferredWidth(100);
        tblElectrionicPaymentLst.getColumnModel().getColumn(11).setPreferredWidth(200);
        tblElectrionicPaymentLst.getColumnModel().getColumn(12).setPreferredWidth(200);
        setRightAlignment(6);
    }
    private void txtTotAccountsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTotAccountsFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotAccountsFocusLost
    private void tableDataAlignmentInquiry() {
        ArrayList _heading = new ArrayList();
        if (observable.getElectronicPaymentInquiryList() != null && observable.getElectronicPaymentInquiryList().size() > 0) {
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(tblElectrionicInquirytLst);
            com.see.truetransact.clientutil.TableModel tableModel = new com.see.truetransact.clientutil.TableModel();
            _heading.add("Account No");
            _heading.add("Trans Dt");
            _heading.add("Name");
            _heading.add("Amount");
            _heading.add("Our UTR Number");
            _heading.add("Payment Accepted Dt");
            _heading.add("API Response Status");
            _heading.add("Bank UTR Number");
            _heading.add("Bank Description");
            _heading.add("Particulars");
            _heading.add("Payment Status");
            _heading.add("Inquiry Status");
            _heading.add("Screen Name");
            tableModel.setHeading(_heading);
            tableModel.setData((ArrayList) observable.getElectronicPaymentInquiryList());
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            tblElectrionicInquirytLst.setAutoResizeMode(0);
            tblElectrionicInquirytLst.doLayout();
            tblElectrionicInquirytLst.setModel(tableSorter);
            tblElectrionicInquirytLst.revalidate();
        }
    }

    private void setSizeTableDataInquiry() {
        tblElectrionicInquirytLst.getColumnModel().getColumn(0).setPreferredWidth(85);
        tblElectrionicInquirytLst.getColumnModel().getColumn(1).setPreferredWidth(65);
        tblElectrionicInquirytLst.getColumnModel().getColumn(2).setPreferredWidth(250);
        tblElectrionicInquirytLst.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblElectrionicInquirytLst.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblElectrionicInquirytLst.getColumnModel().getColumn(5).setPreferredWidth(65);
        tblElectrionicInquirytLst.getColumnModel().getColumn(6).setPreferredWidth(75);
        tblElectrionicInquirytLst.getColumnModel().getColumn(7).setPreferredWidth(150);
        tblElectrionicInquirytLst.getColumnModel().getColumn(8).setPreferredWidth(350);
        tblElectrionicInquirytLst.getColumnModel().getColumn(9).setPreferredWidth(150);
        tblElectrionicInquirytLst.getColumnModel().getColumn(10).setPreferredWidth(100);
        tblElectrionicInquirytLst.getColumnModel().getColumn(11).setPreferredWidth(100);
        tblElectrionicInquirytLst.getColumnModel().getColumn(12).setPreferredWidth(125);
    }
    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:                
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws InterruptedException {
                try {
                    if (tblElectrionicPaymentLst.getRowCount() > 0) {
                        HashMap paramMap = new HashMap();
//                    List observable.getFinalList() = new ArrayList();
                        ArrayList rowList = new ArrayList();
                        for (int i = 0; i < tblElectrionicPaymentLst.getRowCount(); i++) {
                            String st = CommonUtil.convertObjToStr(tblElectrionicPaymentLst.getValueAt(i, 0));
                            if (st.equals("true")) {
                                ArrayList selectedTableList = new ArrayList();
//                            rowList.add(false);
                                selectedTableList.add(CommonUtil.convertObjToStr(tblElectrionicPaymentLst.getValueAt(i, 1)));
                                selectedTableList.add(CommonUtil.convertObjToStr(tblElectrionicPaymentLst.getValueAt(i, 2)));
                                selectedTableList.add(CommonUtil.convertObjToStr(tblElectrionicPaymentLst.getValueAt(i, 3)));
                                selectedTableList.add(CommonUtil.convertObjToStr(tblElectrionicPaymentLst.getValueAt(i, 4)));
                                selectedTableList.add(CommonUtil.convertObjToStr(tblElectrionicPaymentLst.getValueAt(i, 5)));
                                selectedTableList.add(CommonUtil.convertObjToStr(tblElectrionicPaymentLst.getValueAt(i, 6)));
                                selectedTableList.add(CommonUtil.convertObjToStr(tblElectrionicPaymentLst.getValueAt(i, 7)));
                                selectedTableList.add(CommonUtil.convertObjToStr(tblElectrionicPaymentLst.getValueAt(i, 8)));
                                selectedTableList.add(CommonUtil.convertObjToStr(tblElectrionicPaymentLst.getValueAt(i, 9)));
                                selectedTableList.add(CommonUtil.convertObjToStr(tblElectrionicPaymentLst.getValueAt(i, 10)));
                                selectedTableList.add(CommonUtil.convertObjToStr(tblElectrionicPaymentLst.getValueAt(i, 11)));
                                selectedTableList.add(CommonUtil.convertObjToStr(tblElectrionicPaymentLst.getValueAt(i, 12)));
                                System.out.println("selectedTableList : "+selectedTableList);
                                rowList.add(selectedTableList);
                                observable.setFinalList(rowList);
                            }
                        }
                        if (observable.getFinalList() != null && observable.getFinalList().size() > 0) {
                            observable.doAction(paramMap);
                        } else {
                            ClientUtil.showAlertWindow("Please select the record to proceed...");
                        }
                    } else {
                        ClientUtil.showAlertWindow("Please select the record to proceed...");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                loading.dispose();
            }
        };
        worker.execute();
        loading.show();
        try {
            worker.get();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        btnCancelActionPerformed(null);
    }//GEN-LAST:event_btnProcessActionPerformed

    private void btnClose1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose1ActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnClose1ActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(null);
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        resetUI();
        resetInquiryUI();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        isFilled = false;
        viewType = -1;
        setModified(false);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        //btnException.setEnabled(true);
        btnNew.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnView.setEnabled(true);
        txtTotAccounts.setText("");
        txtTotDepAmt.setText("");
        lblElectronicPaymentDtVal.setText("");
        observable.resetForm();
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        observable.setStatus();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:

    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:

    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:

    }//GEN-LAST:event_btnEditActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.resetForm();
        ClientUtil.enableDisable(this, true);
        ClientUtil.enableDisable(panElectrionicPaymentLst, true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        observable.setStatus();
        setModified(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        observable.ttNotifyObservers();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        resetUI();
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        popUp(VIEW);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        observable.setStatus();
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnView.setEnabled(false);
        observable.ttNotifyObservers();
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnProcess1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcess1ActionPerformed
        // TODO add your handling code here:
        resetUI();
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws InterruptedException {
                try {
                    if (CommonUtil.convertObjToStr(txtBatchId.getText()).length() > 0) {
                        HashMap hash = new HashMap();
                        hash.put("TRANS_ID", CommonUtil.convertObjToStr(txtBatchId.getText()));
                        hash.put("SOURCE_SCREEN", CommonUtil.convertObjToStr(lblElectronicPaymentDtVal1.getText()));
                        hash.put("BATCH_DT", CommonUtil.convertObjToStr(lblElectronicPaymentDtVal.getText()));
                        observable.populateTableData(hash);
                        tblElectrionicPaymentLst.setModel(observable.getElectrionicPaymentTable());
//                        setSizeTableData();
                        chkSelectAll.setEnabled(true);
                        tableDataAlignmentPayment();
                        setSizeTableDataPayment();
                        javax.swing.table.DefaultTableCellRenderer r = new javax.swing.table.DefaultTableCellRenderer();
                        r.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                        tblElectrionicPaymentLst.getColumnModel().getColumn(3).setCellRenderer(r);
                        tblElectrionicPaymentLst.getColumnModel().getColumn(3).sizeWidthToFit();
//                        txtToUTR.setText(observable.getLblElectronicPaymentIDVal());
                        lblElectronicPaymentDtVal.setText(observable.getLblElectronicPaymentDtVal());
                    } else {
                        ClientUtil.showAlertWindow("Please select Processed Id...");
//                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                loading.dispose();
            }
        };
        worker.execute();
        loading.show();
        try {
            worker.get();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }//GEN-LAST:event_btnProcess1ActionPerformed

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // TODO add your handling code here:
        boolean flag;
        if (chkSelectAll.isSelected() == true) {
            flag = true;
        } else {
            flag = false;
        }
        double totAmount = 0;
        int totCount = 0;
        for (int i = 0; i < tblElectrionicPaymentLst.getRowCount(); i++) {
            tblElectrionicPaymentLst.setValueAt(new Boolean(flag), i, 0);
            if (CommonUtil.convertObjToStr(tblElectrionicPaymentLst.getValueAt(i, 0)).equals("true")) {
                totAmount = totAmount + CommonUtil.convertObjToDouble(tblElectrionicPaymentLst.getValueAt(i, 6)).doubleValue();
                totCount = totCount + 1;
            }
        }
        txtTotAccounts.setText(String.valueOf(totCount));
        txtTotDepAmt.setText(CurrencyValidation.formatCrore(String.valueOf(totAmount)));
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void txtBatchIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBatchIdFocusLost
        // TODO add your handling code here:

    }//GEN-LAST:event_txtBatchIdFocusLost

    private void btnBatchIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatchIdActionPerformed
        // TODO add your handling code here:
        txtBatchId.setEnabled(false);
        popUp(VIEW);
    }//GEN-LAST:event_btnBatchIdActionPerformed

    private void txtAcctNumFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAcctNumFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAcctNumFocusLost

    private void btnAcctNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcctNumActionPerformed
        // TODO add your handling code here:
        txtBatchId.setEnabled(false);
        popUp(EDIT);
    }//GEN-LAST:event_btnAcctNumActionPerformed

    private void tblElectrionicInquirytLstMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblElectrionicInquirytLstMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblElectrionicInquirytLstMouseClicked

    private void btnInquiryClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInquiryClearActionPerformed
        // TODO add your handling code here:
//        btnCancelActionPerformed(null);
        resetInquiryUI();        
    }//GEN-LAST:event_btnInquiryClearActionPerformed

    private void cboElectronicTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboElectronicTypeActionPerformed
        // Add your handling code here:        
    }//GEN-LAST:event_cboElectronicTypeActionPerformed

    private void btnInquiryProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInquiryProcessActionPerformed
        // TODO add your handling code here:
//        resetInquiryUI();
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws InterruptedException {
                try {
                    String electronicType = CommonUtil.convertObjToStr(cboElectronicType.getSelectedItem());
                    if (electronicType.equals("")) {
                        ClientUtil.showAlertWindow("Please select type of the transaction");
                    } else {
                        if (tdtFromDt.getDateValue().equals("")) {
                            tdtFromDt.setDateValue(CommonUtil.convertObjToStr(curDate));
                        }
                        if (tdtToDt.getDateValue().equals("")) {
                            tdtToDt.setDateValue(CommonUtil.convertObjToStr(curDate));
                        }
                        if (DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()), DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue())) < 0) {
                            ClientUtil.showAlertWindow("To Date Should Not Be Greater than From Date !!!");
                            tdtFromDt.setDateValue("");
                            tdtToDt.setDateValue("");
                        }else{
                            lblElectronicPaymentDtVal.setText("");
                            HashMap hash = new HashMap();
                            String paymentStatus = "ACCEPTED";
                            if (electronicType.equals("Not Processed")) {
                                electronicType = "";
                                paymentStatus = "";
                            } else if (electronicType.equals("ACCEPTED")) {
                                hash.put("PAYMENT_STATUS", paymentStatus);
                            } else {
                                hash.put("PAYMENT_STATUS", paymentStatus);
                                hash.put("INQUIRY_STATUS", electronicType);
                            }
                            hash.put("FROM_DT", getProperDateFormat(DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue())));
                            hash.put("TO_DT", getProperDateFormat(DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue())));
                            if (txtAcctNum.getText().length() > 0) {
                                hash.put("TRANS_ID", CommonUtil.convertObjToStr(txtAcctNum.getText()));
                            }
                            hash.put("SOURCE_SCREEN", CommonUtil.convertObjToStr(lblElectronicPaymentDtVal1.getText()));
                            observable.populateInquiryTableData(hash);
                            tblElectrionicInquirytLst.setModel(observable.getElectrionicInquiryTable());
                            tableDataAlignmentInquiry();
                            setSizeTableDataInquiry();
                            javax.swing.table.DefaultTableCellRenderer r = new javax.swing.table.DefaultTableCellRenderer();
                            r.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                            tblElectrionicInquirytLst.getColumnModel().getColumn(3).setCellRenderer(r);
                            tblElectrionicInquirytLst.getColumnModel().getColumn(3).sizeWidthToFit();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                loading.dispose();
            }
        };
        worker.execute();
        loading.show();
        try {
            worker.get();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }//GEN-LAST:event_btnInquiryProcessActionPerformed

    private boolean isVerifiedAll() {
        boolean verifyAll = true;
        String flag = "";
        for (int i = 0; i < tblElectrionicPaymentLst.getRowCount(); i++) {
            flag = CommonUtil.convertObjToStr(tblElectrionicPaymentLst.getValueAt(i, 8));
            if (flag.equals("No")) {
                verifyAll = false;
                break;
            }
        }
        return verifyAll;
    }

    public void enableDisableForMutiple(boolean flag) {
        btnSave.setEnabled(flag);
        btnCancel.setEnabled(flag);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            btnSave.setEnabled(false);
        }
    }

    private void setSizeTableData() {
        javax.swing.table.TableColumn col = tblElectrionicPaymentLst.getColumn(tblElectrionicPaymentLst.getColumnName(0));
        col.setMaxWidth(45);
        col = tblElectrionicPaymentLst.getColumn(tblElectrionicPaymentLst.getColumnName(1));
        col.setMaxWidth(100);
        col = tblElectrionicPaymentLst.getColumn(tblElectrionicPaymentLst.getColumnName(2));
        col.setMaxWidth(90);
        col = tblElectrionicPaymentLst.getColumn(tblElectrionicPaymentLst.getColumnName(3));
        col.setMaxWidth(160);
        col = tblElectrionicPaymentLst.getColumn(tblElectrionicPaymentLst.getColumnName(4));
        col.setMaxWidth(80);
        col = tblElectrionicPaymentLst.getColumn(tblElectrionicPaymentLst.getColumnName(5));
        col.setMaxWidth(100);
        col = tblElectrionicPaymentLst.getColumn(tblElectrionicPaymentLst.getColumnName(6));
        col.setMaxWidth(150);
        col = tblElectrionicPaymentLst.getColumn(tblElectrionicPaymentLst.getColumnName(7));
        col.setMaxWidth(75);
        col = tblElectrionicPaymentLst.getColumn(tblElectrionicPaymentLst.getColumnName(8));
        col.setMaxWidth(55);
        col = tblElectrionicPaymentLst.getColumn(tblElectrionicPaymentLst.getColumnName(9));
        col.setMaxWidth(100);
        col = tblElectrionicPaymentLst.getColumn(tblElectrionicPaymentLst.getColumnName(10));
        col.setMaxWidth(100);
//        col = tblElectrionicPaymentLst.getColumn(tblElectrionicPaymentLst.getColumnName(11));
//        col.setMaxWidth(100);
//        col = tblElectrionicPaymentLst.getColumn(tblElectrionicPaymentLst.getColumnName(12));
//        col.setMaxWidth(100);

    }

    private void setSizeTableInquiryData() {
        javax.swing.table.TableColumn col = tblElectrionicInquirytLst.getColumn(tblElectrionicInquirytLst.getColumnName(0));
        col.setMaxWidth(100);
        col = tblElectrionicInquirytLst.getColumn(tblElectrionicInquirytLst.getColumnName(1));
        col.setMaxWidth(100);
        col = tblElectrionicInquirytLst.getColumn(tblElectrionicInquirytLst.getColumnName(2));
        col.setMaxWidth(150);
        col = tblElectrionicInquirytLst.getColumn(tblElectrionicInquirytLst.getColumnName(3));
        col.setMaxWidth(160);
        col = tblElectrionicInquirytLst.getColumn(tblElectrionicInquirytLst.getColumnName(4));
        col.setMaxWidth(150);
        col = tblElectrionicInquirytLst.getColumn(tblElectrionicInquirytLst.getColumnName(5));
        col.setMaxWidth(100);
        col = tblElectrionicInquirytLst.getColumn(tblElectrionicInquirytLst.getColumnName(6));
        col.setMaxWidth(100);
        col = tblElectrionicInquirytLst.getColumn(tblElectrionicInquirytLst.getColumnName(7));
        col.setMaxWidth(100);
        col = tblElectrionicInquirytLst.getColumn(tblElectrionicInquirytLst.getColumnName(8));
        col.setMaxWidth(150);
        col = tblElectrionicInquirytLst.getColumn(tblElectrionicInquirytLst.getColumnName(9));
        col.setMaxWidth(150);
        col = tblElectrionicInquirytLst.getColumn(tblElectrionicInquirytLst.getColumnName(10));
        col.setMaxWidth(150);
        col = tblElectrionicInquirytLst.getColumn(tblElectrionicInquirytLst.getColumnName(10));
        col.setMaxWidth(250);
    }

    public Date getProperDateFormat(Object obj) {
        Date currDt = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDt = (Date) curDate.clone();
            currDt.setDate(tempDt.getDate());
            currDt.setMonth(tempDt.getMonth());
            currDt.setYear(tempDt.getYear());
        }
        return currDt;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new ElectronicPaymentUI().show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAcctNum;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBatchId;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnClose1;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnInquiryClear;
    private com.see.truetransact.uicomponent.CButton btnInquiryProcess;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnProcess1;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboElectronicType;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblAcctNum;
    private com.see.truetransact.uicomponent.CLabel lblBatchId;
    private com.see.truetransact.uicomponent.CLabel lblElectronicPaymentDtVal;
    private com.see.truetransact.uicomponent.CLabel lblElectronicPaymentDtVal1;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblMultiDepDate;
    private com.see.truetransact.uicomponent.CLabel lblMultiDepDate1;
    private com.see.truetransact.uicomponent.CLabel lblSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblSelectedBills;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CLabel lblTotalDepAmount;
    private com.see.truetransact.uicomponent.CLabel lblTotalDepAmt;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAcctNo;
    private com.see.truetransact.uicomponent.CPanel panAcctNumDetails;
    private com.see.truetransact.uicomponent.CPanel panCustIdDetails2;
    private com.see.truetransact.uicomponent.CPanel panElectrionicInquiry;
    private com.see.truetransact.uicomponent.CPanel panElectrionicInquiryLst;
    private com.see.truetransact.uicomponent.CPanel panElectrionicPayment;
    private com.see.truetransact.uicomponent.CPanel panElectrionicPaymentDetails;
    private com.see.truetransact.uicomponent.CPanel panElectrionicPaymentLst;
    private com.see.truetransact.uicomponent.CPanel panInquiryDetails;
    private com.see.truetransact.uicomponent.CPanel panSelectAll;
    private com.see.truetransact.uicomponent.CPanel panTotalDeatils;
    private com.see.truetransact.uicomponent.CButtonGroup rdgInterestType;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_ElectrionicInquiryLst;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_ElectrionicPaymentLst;
    private com.see.truetransact.uicomponent.CTabbedPane tabElectrionicPayment;
    private com.see.truetransact.uicomponent.CTable tblElectrionicInquirytLst;
    private com.see.truetransact.uicomponent.CTable tblElectrionicPaymentLst;
    private javax.swing.JToolBar tbrHead;
    private com.see.truetransact.uicomponent.CDateField tdtFromDt;
    private com.see.truetransact.uicomponent.CDateField tdtToDt;
    private com.see.truetransact.uicomponent.CTextField txtAcctNum;
    private com.see.truetransact.uicomponent.CTextField txtBatchId;
    private com.see.truetransact.uicomponent.CTextField txtTotAccounts;
    private com.see.truetransact.uicomponent.CTextField txtTotDepAmt;
    // End of variables declaration//GEN-END:variables
}
