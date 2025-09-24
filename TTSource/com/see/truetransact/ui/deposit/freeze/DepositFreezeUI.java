/*
 * DepositFreezeUI.java
 *
 * Created on June 2, 2004, 4:33 PM
 */

package com.see.truetransact.ui.deposit.freeze;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JComponent;
import javax.swing.JFrame;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientproxy.ProxyParameters;
import java.util.List;
import java.util.Date;
/**
 *
 * @author  Pinky
 *
 */

public class DepositFreezeUI extends CInternalFrame implements Observer, UIMandatoryField {
    
    private HashMap mandatoryMap;
    private DepositFreezeOB observable;
    private DepositFreezeRB resourceBundle;
    private DepositFreezeMRB objMandatoryRB;
    
    private int viewType=-1;
    private final int DEPOSIT_ACCT=100;
    private boolean _intFreezeNew;
    private final String freezeType = "COMPLETE";
     int rowSelected = -1;
    private Date currDt = null;
    /** Creates new form BeanForm */
    public DepositFreezeUI() {
        initComponents();
        initSetUp();
    }
    private void initSetUp(){
        currDt = ClientUtil.getCurrentDate();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
        setMaxLength();
       new MandatoryCheck().putMandatoryMarks(getClass().getName(),panDepositDetails);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panFreezeInfo);   
        setObservable();
        observable.resetForm();
        setUp(ClientConstants.ACTIONTYPE_CANCEL,false);
        setButtonEnableDisable();
        enableDisableButtons(false);
        initComponentData();
        panFreezeEnableDisable(false);
        btnDelete.setEnabled(false);
    }
    private void setObservable() {
        observable = DepositFreezeOB.getInstance();
        observable.addObserver(this);
    }
    private void initComponentData(){
        this.cboProductID.setModel(observable.getCbmProductId());
        this.cboFreezeType.setModel(observable.getCbmFreezeType());
        this.tblFreeze.setModel(observable.getTbmFreeze());
    }
    public void setUp(int actionType,boolean isEnable) {
        ClientUtil.enableDisable(this,isEnable);
        
        observable.setActionType(actionType);
        observable.setStatus();
    }
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(btnNew.isEnabled());
//        btnDelete.setEnabled(btnNew.isEnabled());
        
        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());
        
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
//        mitDelete.setEnabled(btnDelete.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }
    private void enableDisableButtons(boolean enableDisable) {
        this.btnDepositNo.setEnabled(enableDisable);
        this.btnFreezeNew.setEnabled(enableDisable);
    }
    private void setMaxLength(){
        txtDepositNo.setAllowAll(true);
        this.txtRemark.setMaxLength(128);
    }
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnDepositNo.setName("btnDepositNo");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnFreezeDelete.setName("btnFreezeDelete");
        btnFreezeNew.setName("btnFreezeNew");
        btnFreezeSave.setName("btnFreezeSave");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnUnFreeze.setName("btnUnFreeze");
        cboFreezeType.setName("cboFreezeType");
        cboProductID.setName("cboProductID");
        cboSubDepositNo.setName("cboSubDepositNo");
        lblAccountHD.setName("lblAccountHD");
        lblAccountHDValue.setName("lblAccountHDValue");
        lblClearBalance.setName("lblClearBalance");
        lblClearBalanceValue.setName("lblClearBalanceValue");
        lblCustomerID.setName("lblCustomerID");
        lblCustomerIDValue.setName("lblCustomerIDValue");
//        lblCustomerName.setName("lblCustomerName");
        lblCustomerNameValue.setName("lblCustomerNameValue");
        lblDepositNo.setName("lblDepositNo");
        lblFreezeAmount.setName("lblFreezeAmount");
        lblFreezeDate.setName("lblFreezeDate");
        lblFreezeSum.setName("lblFreezeSum");
        lblFreezeSumValue.setName("lblFreezeSumValue");
        lblFreezeType.setName("lblFreezeType");
        lblMsg.setName("lblMsg");
        lblProductID.setName("lblProductID");
        lblRemark.setName("lblRemark");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus.setName("lblStatus");
        lblSubDepositNo.setName("lblSubDepositNo");
        mbrMain.setName("mbrMain");
        panCustomerDetail.setName("panCustomerDetail");
        panDepositDetails.setName("panDepositDetails");
        panDepositInfo.setName("panDepositInfo");
        panDepositNo.setName("panDepositNo");
        panFreeze.setName("panFreeze");
        panFreezeButton.setName("panFreezeButton");
        panFreezeDetails.setName("panFreezeDetails");
        panFreezeInfo.setName("panFreezeInfo");
        panFreezeTable.setName("panFreezeTable");
        panStatus.setName("panStatus");
        sptDeposit.setName("sptDeposit");
        srpFreeze.setName("srpFreeze");
        tblFreeze.setName("tblFreeze");
        tdtFreezeDate.setName("tdtFreezeDate");
        txtDepositNo.setName("txtDepositNo");
        txtFreezeAmount.setName("txtFreezeAmount");
        txtRemark.setName("txtRemark");
        
        lblDepositAmt.setName("lblDepositAmt");
        lblDepositAmtValue.setName("lblDepositAmtValue");
        lblLienSum.setName("lblLienSum");
        lblLienSumValue.setName("lblLienSumValue");
        lblShadowFreeze.setName("lblShadowFreeze");
        lblShadowFreezeValue.setName("lblShadowFreezeValue");
        lblDepositFreezeNo.setName("lblDepositFreezeNo");
        lblDepositFreezeNoDesc.setName("lblDepositFreezeNoDesc");
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        resourceBundle = new DepositFreezeRB();
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblFreezeDate.setText(resourceBundle.getString("lblFreezeDate"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSubDepositNo.setText(resourceBundle.getString("lblSubDepositNo"));
        lblProductID.setText(resourceBundle.getString("lblProductID"));
        lblFreezeSumValue.setText(resourceBundle.getString("lblFreezeSumValue"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        btnFreezeSave.setText(resourceBundle.getString("btnFreezeSave"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblDepositNo.setText(resourceBundle.getString("lblDepositNo"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblFreezeAmount.setText(resourceBundle.getString("lblFreezeAmount"));
        lblClearBalance.setText(resourceBundle.getString("lblClearBalance"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblCustomerIDValue.setText(resourceBundle.getString("lblCustomerIDValue"));
//        lblCustomerName.setText(resourceBundle.getString("lblCustomerName"));
        btnFreezeDelete.setText(resourceBundle.getString("btnFreezeDelete"));
        lblAccountHDValue.setText(resourceBundle.getString("lblAccountHDValue"));
        lblFreezeSum.setText(resourceBundle.getString("lblFreezeSum"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        lblRemark.setText(resourceBundle.getString("lblRemark"));
        lblAccountHD.setText(resourceBundle.getString("lblAccountHD"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblCustomerNameValue.setText(resourceBundle.getString("lblCustomerNameValue"));
        lblCustomerID.setText(resourceBundle.getString("lblCustomerID"));
        btnFreezeNew.setText(resourceBundle.getString("btnFreezeNew"));
        btnUnFreeze.setText(resourceBundle.getString("btnUnFreeze"));
        lblClearBalanceValue.setText(resourceBundle.getString("lblClearBalanceValue"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblFreezeType.setText(resourceBundle.getString("lblFreezeType"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        btnDepositNo.setText(resourceBundle.getString("btnDepositNo"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        
        lblDepositAmt.setText(resourceBundle.getString("lblDepositAmt"));
        lblDepositAmtValue.setText(resourceBundle.getString("lblDepositAmtValue"));
        lblLienSum.setText(resourceBundle.getString("lblLienSum"));
        lblLienSumValue.setText(resourceBundle.getString("lblLienSumValue"));
        lblShadowFreeze.setText(resourceBundle.getString("lblShadowFreeze"));
        lblShadowFreezeValue.setText(resourceBundle.getString("lblShadowFreezeValue"));
        lblDepositFreezeNo.setText(resourceBundle.getString("lblDepositFreezeNo"));
        lblDepositFreezeNoDesc.setText(resourceBundle.getString("lblDepositFreezeNoDesc"));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace25 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panFreeze = new com.see.truetransact.uicomponent.CPanel();
        panFreezeDetails = new com.see.truetransact.uicomponent.CPanel();
        panFreezeInfo = new com.see.truetransact.uicomponent.CPanel();
        tdtFreezeDate = new com.see.truetransact.uicomponent.CDateField();
        lblFreezeDate = new com.see.truetransact.uicomponent.CLabel();
        txtFreezeAmount = new com.see.truetransact.uicomponent.CTextField();
        cboFreezeType = new com.see.truetransact.uicomponent.CComboBox();
        lblFreezeAmount = new com.see.truetransact.uicomponent.CLabel();
        lblFreezeType = new com.see.truetransact.uicomponent.CLabel();
        lblRemark = new com.see.truetransact.uicomponent.CLabel();
        txtRemark = new com.see.truetransact.uicomponent.CTextField();
        panFreezeButton = new com.see.truetransact.uicomponent.CPanel();
        btnFreezeNew = new com.see.truetransact.uicomponent.CButton();
        btnFreezeSave = new com.see.truetransact.uicomponent.CButton();
        btnFreezeDelete = new com.see.truetransact.uicomponent.CButton();
        btnUnFreeze = new com.see.truetransact.uicomponent.CButton();
        lblDepositFreezeNo = new com.see.truetransact.uicomponent.CLabel();
        lblDepositFreezeNoDesc = new com.see.truetransact.uicomponent.CLabel();
        panFreezeTable = new com.see.truetransact.uicomponent.CPanel();
        srpFreeze = new com.see.truetransact.uicomponent.CScrollPane();
        tblFreeze = new com.see.truetransact.uicomponent.CTable();
        panDepositDetails = new com.see.truetransact.uicomponent.CPanel();
        panDepositInfo = new com.see.truetransact.uicomponent.CPanel();
        lblAccountHD = new com.see.truetransact.uicomponent.CLabel();
        cboProductID = new com.see.truetransact.uicomponent.CComboBox();
        lblDepositNo = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHDValue = new com.see.truetransact.uicomponent.CLabel();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        panDepositNo = new com.see.truetransact.uicomponent.CPanel();
        btnDepositNo = new com.see.truetransact.uicomponent.CButton();
        txtDepositNo = new com.see.truetransact.uicomponent.CTextField();
        lblCustomerID = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerIDValue = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerNameValue = new com.see.truetransact.uicomponent.CLabel();
        panCustomerDetail = new com.see.truetransact.uicomponent.CPanel();
        lblClearBalance = new com.see.truetransact.uicomponent.CLabel();
        lblClearBalanceValue = new com.see.truetransact.uicomponent.CLabel();
        lblFreezeSum = new com.see.truetransact.uicomponent.CLabel();
        lblFreezeSumValue = new com.see.truetransact.uicomponent.CLabel();
        cboSubDepositNo = new com.see.truetransact.uicomponent.CComboBox();
        lblSubDepositNo = new com.see.truetransact.uicomponent.CLabel();
        lblLienSumValue = new com.see.truetransact.uicomponent.CLabel();
        lblLienSum = new com.see.truetransact.uicomponent.CLabel();
        lblDepositAmt = new com.see.truetransact.uicomponent.CLabel();
        lblDepositAmtValue = new com.see.truetransact.uicomponent.CLabel();
        lblShadowFreeze = new com.see.truetransact.uicomponent.CLabel();
        lblShadowFreezeValue = new com.see.truetransact.uicomponent.CLabel();
        sptDeposit = new com.see.truetransact.uicomponent.CSeparator();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptNew = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptSave = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setPreferredSize(new java.awt.Dimension(620, 500));

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
        tbrOperativeAcctProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnNew);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace24);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace25);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnSave);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace26);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Close");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnAuthorize);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace27);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Print");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace28);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Print");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnPrint);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace29);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        getContentPane().add(tbrOperativeAcctProduct, java.awt.BorderLayout.NORTH);

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

        panFreeze.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panFreeze.setMinimumSize(new java.awt.Dimension(500, 250));
        panFreeze.setPreferredSize(new java.awt.Dimension(502, 250));
        panFreeze.setLayout(new java.awt.GridBagLayout());

        panFreezeDetails.setMinimumSize(new java.awt.Dimension(350, 150));
        panFreezeDetails.setPreferredSize(new java.awt.Dimension(350, 150));
        panFreezeDetails.setLayout(new java.awt.GridBagLayout());

        panFreezeInfo.setMinimumSize(new java.awt.Dimension(200, 156));
        panFreezeInfo.setPreferredSize(new java.awt.Dimension(200, 156));
        panFreezeInfo.setLayout(new java.awt.GridBagLayout());

        tdtFreezeDate.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeInfo.add(tdtFreezeDate, gridBagConstraints);

        lblFreezeDate.setText("Freeze Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeInfo.add(lblFreezeDate, gridBagConstraints);

        txtFreezeAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFreezeAmount.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeInfo.add(txtFreezeAmount, gridBagConstraints);

        cboFreezeType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFreezeTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeInfo.add(cboFreezeType, gridBagConstraints);

        lblFreezeAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeInfo.add(lblFreezeAmount, gridBagConstraints);

        lblFreezeType.setText("Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeInfo.add(lblFreezeType, gridBagConstraints);

        lblRemark.setText("Remark");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeInfo.add(lblRemark, gridBagConstraints);

        txtRemark.setMinimumSize(new java.awt.Dimension(150, 21));
        txtRemark.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeInfo.add(txtRemark, gridBagConstraints);

        btnFreezeNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnFreezeNew.setPreferredSize(new java.awt.Dimension(30, 30));
        btnFreezeNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFreezeNewActionPerformed(evt);
            }
        });
        panFreezeButton.add(btnFreezeNew);

        btnFreezeSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnFreezeSave.setPreferredSize(new java.awt.Dimension(30, 30));
        btnFreezeSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFreezeSaveActionPerformed(evt);
            }
        });
        panFreezeButton.add(btnFreezeSave);

        btnFreezeDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnFreezeDelete.setPreferredSize(new java.awt.Dimension(30, 30));
        btnFreezeDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFreezeDeleteActionPerformed(evt);
            }
        });
        panFreezeButton.add(btnFreezeDelete);

        btnUnFreeze.setText("UnFreeze");
        btnUnFreeze.setMaximumSize(new java.awt.Dimension(95, 30));
        btnUnFreeze.setMinimumSize(new java.awt.Dimension(95, 30));
        btnUnFreeze.setPreferredSize(new java.awt.Dimension(95, 30));
        btnUnFreeze.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnFreezeActionPerformed(evt);
            }
        });
        panFreezeButton.add(btnUnFreeze);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panFreezeInfo.add(panFreezeButton, gridBagConstraints);

        lblDepositFreezeNo.setText("FslNo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeInfo.add(lblDepositFreezeNo, gridBagConstraints);

        lblDepositFreezeNoDesc.setMaximumSize(new java.awt.Dimension(150, 16));
        lblDepositFreezeNoDesc.setMinimumSize(new java.awt.Dimension(150, 16));
        lblDepositFreezeNoDesc.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeInfo.add(lblDepositFreezeNoDesc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.weighty = 1.0;
        panFreezeDetails.add(panFreezeInfo, gridBagConstraints);

        panFreezeTable.setMinimumSize(new java.awt.Dimension(150, 22));
        panFreezeTable.setPreferredSize(new java.awt.Dimension(150, 403));
        panFreezeTable.setLayout(new java.awt.GridBagLayout());

        tblFreeze.setModel(new javax.swing.table.DefaultTableModel(
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
        tblFreeze.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFreezeMouseClicked(evt);
            }
        });
        srpFreeze.setViewportView(tblFreeze);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 10.0;
        panFreezeTable.add(srpFreeze, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.6;
        gridBagConstraints.weighty = 1.0;
        panFreezeDetails.add(panFreezeTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panFreeze.add(panFreezeDetails, gridBagConstraints);

        panDepositDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panDepositDetails.setMinimumSize(new java.awt.Dimension(500, 100));
        panDepositDetails.setPreferredSize(new java.awt.Dimension(502, 100));
        panDepositDetails.setLayout(new java.awt.GridBagLayout());

        panDepositInfo.setMinimumSize(new java.awt.Dimension(250, 150));
        panDepositInfo.setPreferredSize(new java.awt.Dimension(250, 150));
        panDepositInfo.setLayout(new java.awt.GridBagLayout());

        lblAccountHD.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositInfo.add(lblAccountHD, gridBagConstraints);

        cboProductID.setPopupWidth(200);
        cboProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositInfo.add(cboProductID, gridBagConstraints);

        lblDepositNo.setText("Deposit Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositInfo.add(lblDepositNo, gridBagConstraints);

        lblAccountHDValue.setForeground(new java.awt.Color(0, 51, 204));
        lblAccountHDValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAccountHDValue.setMaximumSize(new java.awt.Dimension(150, 16));
        lblAccountHDValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblAccountHDValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositInfo.add(lblAccountHDValue, gridBagConstraints);

        lblProductID.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositInfo.add(lblProductID, gridBagConstraints);

        panDepositNo.setLayout(new java.awt.GridBagLayout());

        btnDepositNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDepositNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnDepositNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDepositNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDepositNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panDepositNo.add(btnDepositNo, gridBagConstraints);

        txtDepositNo.setEnabled(false);
        txtDepositNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDepositNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panDepositNo.add(txtDepositNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDepositInfo.add(panDepositNo, gridBagConstraints);

        lblCustomerID.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositInfo.add(lblCustomerID, gridBagConstraints);

        lblCustomerIDValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblCustomerIDValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblCustomerIDValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositInfo.add(lblCustomerIDValue, gridBagConstraints);

        lblCustomerNameValue.setForeground(new java.awt.Color(0, 51, 204));
        lblCustomerNameValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCustomerNameValue.setMaximumSize(new java.awt.Dimension(150, 16));
        lblCustomerNameValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblCustomerNameValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 35, 4, 4);
        panDepositInfo.add(lblCustomerNameValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.4;
        panDepositDetails.add(panDepositInfo, gridBagConstraints);

        panCustomerDetail.setMinimumSize(new java.awt.Dimension(250, 150));
        panCustomerDetail.setPreferredSize(new java.awt.Dimension(250, 150));
        panCustomerDetail.setLayout(new java.awt.GridBagLayout());

        lblClearBalance.setText("Clear Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetail.add(lblClearBalance, gridBagConstraints);

        lblClearBalanceValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetail.add(lblClearBalanceValue, gridBagConstraints);

        lblFreezeSum.setText("Sum of Authorized Freezes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetail.add(lblFreezeSum, gridBagConstraints);

        lblFreezeSumValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetail.add(lblFreezeSumValue, gridBagConstraints);

        cboSubDepositNo.setMinimumSize(new java.awt.Dimension(45, 22));
        cboSubDepositNo.setPopupWidth(50);
        cboSubDepositNo.setPreferredSize(new java.awt.Dimension(150, 21));
        cboSubDepositNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSubDepositNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetail.add(cboSubDepositNo, gridBagConstraints);

        lblSubDepositNo.setText("Deposit Sub Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetail.add(lblSubDepositNo, gridBagConstraints);

        lblLienSumValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblLienSumValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetail.add(lblLienSumValue, gridBagConstraints);

        lblLienSum.setText("Sum of Authorized Liens");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetail.add(lblLienSum, gridBagConstraints);

        lblDepositAmt.setText("Deposit Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetail.add(lblDepositAmt, gridBagConstraints);

        lblDepositAmtValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblDepositAmtValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetail.add(lblDepositAmtValue, gridBagConstraints);

        lblShadowFreeze.setText("Shadow Freeze");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetail.add(lblShadowFreeze, gridBagConstraints);

        lblShadowFreezeValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCustomerDetail.add(lblShadowFreezeValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.4;
        panDepositDetails.add(panCustomerDetail, gridBagConstraints);

        sptDeposit.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        panDepositDetails.add(sptDeposit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panFreeze.add(panDepositDetails, gridBagConstraints);

        getContentPane().add(panFreeze, java.awt.BorderLayout.CENTER);

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess");

        mitNew.setText("New");
        mitNew.setName("mitNew");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);

        sptNew.setName("sptNew");
        mnuProcess.add(sptNew);

        mitSave.setText("Save");
        mitSave.setName("mitSave");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave");
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint");
        mitPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPrintActionPerformed(evt);
            }
        });
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);
    }// </editor-fold>//GEN-END:initComponents

    private void txtDepositNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDepositNoFocusLost
        // TODO add your handling code here:
        String ACCOUNTNO = (String) txtDepositNo.getText();
        if(ACCOUNTNO.length()>0){
            if (observable.checkAcNoWithoutProdType(ACCOUNTNO)) {
                cboProductID.setModel(observable.getCbmProductId());
                cboProductIDActionPerformed(null);
                txtDepositNo.setText(observable.getTxtDepositNo());
//                txtDepositNo.setText(ACCOUNTNO);
                if(lblAccountHDValue.getText().length()>0){
                    viewType = DEPOSIT_ACCT;
                    HashMap fillMap = new HashMap();
                    fillMap.put("PROD_ID",observable.getCbmProductId().getKeyForSelected());
                    fillMap.put("DEPOSIT_ACT_NUM",txtDepositNo.getText());
                    fillMap.put("DEFAULT", "DEFAULT");
                    fillData(fillMap);
                }else{
                    ClientUtil.showAlertWindow("Invalid Account No.");
                    txtDepositNo.setText("");
                    return;
                }
            } else {
                ClientUtil.showAlertWindow("Invalid Account No.");
                txtDepositNo.setText("");
                return;
            }
        }
        
    }//GEN-LAST:event_txtDepositNoFocusLost

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView(ClientConstants.ACTIONTYPE_VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void cboFreezeTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFreezeTypeActionPerformed
        // Add your handling code here:
        if(observable.getActionType()!=ClientConstants.ACTIONTYPE_AUTHORIZE &&
            observable.getActionType()!=ClientConstants.ACTIONTYPE_EXCEPTION &&
            observable.getActionType()!=ClientConstants.ACTIONTYPE_REJECT ){
            String freezeType = (String)((ComboBoxModel)this.cboFreezeType.getModel()).getKeyForSelected();
            if(freezeType!=null && freezeType.length()>0){
                if(freezeType.compareToIgnoreCase(this.freezeType)==0 && observable.getFreezeStatus()==CommonConstants.STATUS_CREATED){
                    Double bal=CommonUtil.convertObjToDouble(lblClearBalanceValue.getText());
                    double balance=0;
                    if(bal!=null)
                        balance = bal.doubleValue();
                    
                    bal=CommonUtil.convertObjToDouble(lblShadowFreezeValue.getText());
                    if(bal!=null)
                        balance -= bal.doubleValue();
                    this.txtFreezeAmount.setText(String.valueOf(balance));
                    this.txtFreezeAmount.setEnabled(false);
                }else if(observable.getActionType()!=ClientConstants.ACTIONTYPE_DELETE){
                    if(observable.getFreezeStatus()==CommonConstants.STATUS_CREATED)
                        this.txtFreezeAmount.setText("");
                    this.txtFreezeAmount.setEnabled(true);
                }
            }
        }
    }//GEN-LAST:event_cboFreezeTypeActionPerformed
    
    private void tblFreezeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFreezeMouseClicked
        // Add your handling code here:
         rowSelected = tblFreeze.getSelectedRow();
        
        if(observable.getActionType()!=ClientConstants.ACTIONTYPE_AUTHORIZE &&
        observable.getActionType()!=ClientConstants.ACTIONTYPE_EXCEPTION &&
        observable.getActionType()!=ClientConstants.ACTIONTYPE_REJECT ) {
            if(observable.getActionType()!=ClientConstants.ACTIONTYPE_DELETE)
                panFreezeEnableDisable(true);
            this.tdtFreezeDate.setEnabled(false);
            updationFreeze();
            this.btnUnFreeze.setEnabled(true);
            HashMap viewMap = new HashMap();
            this.viewType=viewType;
            if (viewType==ClientConstants.ACTIONTYPE_EDIT) {
                String fslNo= lblDepositFreezeNoDesc.getText();
                viewMap.put("FREEZE_NO",fslNo);
                whenTableRowSelected(viewMap);
            }
        }
        this.tdtFreezeDate.setEnabled(false);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE || observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW ||observable.getActionType()==ClientConstants.ACTIONTYPE_NEW)
        {
            this.btnUnFreeze.setEnabled(false);
        }
          for(int i=0; i<tblFreeze.getRowCount();i++) {
       String status=CommonUtil.convertObjToStr((tblFreeze.getValueAt(i,4)));
            if(status.length()==0)
            {
               this.btnUnFreeze.setEnabled(false);  
            }
          }
        
         String status =    CommonUtil.convertObjToStr(tblFreeze.getValueAt(rowSelected, 3));
        String status1 =    CommonUtil.convertObjToStr(tblFreeze.getValueAt(rowSelected, 4));
         if(status.equalsIgnoreCase("UNFREEZED"))
         {
              this.btnFreezeDelete.setEnabled(false);
             this.btnFreezeSave.setEnabled(false);
         }
        if(status.equalsIgnoreCase("UNFREEZED") && (status1.equalsIgnoreCase("REJECTED"))) {
           ClientUtil.enableDisable(this.panFreezeInfo,false);
             this.btnFreezeDelete.setEnabled(false);
             this.btnFreezeSave.setEnabled(false);
        }
    }//GEN-LAST:event_tblFreezeMouseClicked
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

    private void whenTableRowSelected(HashMap paramMap) {
                String lockedBy = "";
                  HashMap map = new HashMap();
                 map.put("SCREEN_ID", getScreenID());
                 map.put("RECORD_KEY", paramMap.get("FREEZE_NO"));
                 map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
                 map.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
                 map.put("CUR_DATE", currDt.clone());
                 System.out.println("Record Key Map : " + map);
                 List lstLock = ClientUtil.executeQuery("selectEditLock", map);
                 if (lstLock.size() > 0) {
                        lockedBy = CommonUtil.convertObjToStr(lstLock.get(0));
                        if (!lockedBy.equals(ProxyParameters.USER_ID)) {
//                            setMode(ClientConstants.ACTIONTYPE_VIEW_MODE);
                            btnSave.setEnabled(false);
                        } else {
//                            setMode(ClientConstants.ACTIONTYPE_EDIT);
                              btnSave.setEnabled(true);
                        }
                    } else {
//                        setMode(ClientConstants.ACTIONTYPE_EDIT);
                          btnSave.setEnabled(true);
                    }
                    setOpenForEditBy(lockedBy);
                    if (lockedBy.equals(""))
                        ClientUtil.execute("insertEditLock", map);
                     if (lockedBy.length() > 0 && !lockedBy.equals(ProxyParameters.USER_ID)) {
                    String data = getLockDetails(lockedBy, getScreenID()) ;
                    ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
//                    setMode(ClientConstants.ACTIONTYPE_VIEW_MODE);
                      btnSave.setEnabled(false);
                }
                
            }
            
     
      private String getLockDetails(String lockedBy, String screenId){
        HashMap map = new HashMap();
        StringBuffer data = new StringBuffer() ;
        map.put("LOCKED_BY", lockedBy) ;
        map.put("SCREEN_ID", screenId) ;
        java.util.List lstLock = ClientUtil.executeQuery("getLockedDetails", map);
        map.clear();
        if(lstLock.size() > 0){
            map = (HashMap)(lstLock.get(0));
            data.append("\nLog in Time : ").append(map.get("LOCKED_TIME")) ;
            data.append("\nIP Address : ").append(map.get("IP_ADDR")) ;
            data.append("\nBranch : ").append(map.get("BRANCH_ID"));
        }
        lstLock = null ;
        map = null ;
        return data.toString();
    }
         
        
   

    private void panFreezeEnableDisable(boolean value){
        ClientUtil.clearAll(this.panFreezeInfo);
        ClientUtil.enableDisable(this.panFreezeInfo,value);
        this.btnFreezeDelete.setEnabled(value);
        this.btnFreezeSave.setEnabled(value);
        this.btnUnFreeze.setEnabled(value);
    }
    private void updationFreeze() {
        observable.setFreezeStatus(CommonConstants.STATUS_MODIFIED);
        observable.populateFreeze(tblFreeze.getSelectedRow());
        populateFreezeDetail();
        enableDisableFreezeInfo();
        _intFreezeNew = false;
    }
    private void enableDisableFreezeInfo(){
        String authorizeStatus = observable.getFreezeAuthorizeStatus();
        if(authorizeStatus!=null && authorizeStatus.length()>0){
            if((authorizeStatus.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED))||(authorizeStatus.equalsIgnoreCase(CommonConstants.STATUS_REJECTED))){
                ClientUtil.enableDisable(this.panFreezeInfo,false);
                this.btnFreezeDelete.setEnabled(false);
                this.btnFreezeSave.setEnabled(false);
                return;
            }
        }
        this.btnUnFreeze.setEnabled(false);
    }
    private void populateFreezeDetail(){
        this.txtFreezeAmount.setText(observable.getTxtFreezeAmount());
        this.txtRemark.setText(observable.getTxtFreezeRemark());
        this.tdtFreezeDate.setDateValue(observable.getTdtFreezeDate());
        ((ComboBoxModel)this.cboFreezeType.getModel()).setKeyForSelected(observable.getCboFreezeType());
         lblDepositFreezeNoDesc.setText(observable.getFreezeNo());
    }
    private void cboSubDepositNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSubDepositNoActionPerformed
        // Add your handling code here:
        String subDepositNo=(String)((ComboBoxModel)this.cboSubDepositNo.getModel()).getKeyForSelected();
        ClientUtil.clearAll(this.panFreezeInfo);
         this.tdtFreezeDate.setDateValue(DateUtil.getStringDate((Date) currDt.clone()));
        if(subDepositNo!=null && subDepositNo.length()>0){
            observable.getAmountDetails(subDepositNo);
            updateAmountDetails();
            observable.getFreezeData(subDepositNo);
        }else{
            observable.resetTabel();
            resetAmountDetails();
        }
        updateTable();
    }//GEN-LAST:event_cboSubDepositNoActionPerformed
    private void resetAmountDetails(){
        this.lblClearBalanceValue.setText("");
        this.lblLienSumValue.setText("");
        this.lblDepositAmtValue.setText("");
        this.lblFreezeSumValue.setText("");
        this.lblShadowFreezeValue.setText("");
    }
    private void updateTable(){
        this.tblFreeze.setModel(observable.getTbmFreeze());
        this.tblFreeze.revalidate();
    }
    private void updateAmountDetails(){
        this.lblClearBalanceValue.setText(observable.getLblClearBalance());
        this.lblFreezeSumValue.setText(observable.getLblExistingFreezeAmt());
        this.lblLienSumValue.setText(observable.getLblExistingLienAmt());
        this.lblDepositAmtValue.setText(observable.getLblDepositAmt());
        this.lblShadowFreezeValue.setText(observable.getLblShadowFreeze());
    }
    private void btnDepositNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositNoActionPerformed
        // Add your handling code here:
        callView(DEPOSIT_ACCT);
        this.txtDepositNo.setEnabled(false);
         this.tdtFreezeDate.setDateValue(DateUtil.getStringDate((Date) currDt.clone()));
    }//GEN-LAST:event_btnDepositNoActionPerformed
    
    private void btnUnFreezeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnFreezeActionPerformed
        // Add your handling code here:
        String unFreezeRemark = COptionPane.showInputDialog(this,resourceBundle.getString("UNFREEZE_REMARK"),"");
        updateFreezeOBFields();
        observable.setUnFreezeRemark(unFreezeRemark);
        observable.setFreezeStatus(CommonConstants.STATUS_UNFREEZE);
        observable.deleteFreezeData(this.tblFreeze.getSelectedRow());
        this.updateTable();
        panFreezeEnableDisable(false);
        this.btnFreezeNew.setEnabled(true);
    }//GEN-LAST:event_btnUnFreezeActionPerformed
    
    private void btnFreezeDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFreezeDeleteActionPerformed
        // Add your handling code here:
        observable.setFreezeStatus(CommonConstants.STATUS_DELETED);
        observable.deleteFreezeData(this.tblFreeze.getSelectedRow());
        this.updateTable();
        this.updateBalance(this.txtFreezeAmount.getText(),false);
        panFreezeEnableDisable(false);
        this.btnFreezeNew.setEnabled(true);
    }//GEN-LAST:event_btnFreezeDeleteActionPerformed
    private boolean checkZeroAmount(){
        Double amt = CommonUtil.convertObjToDouble(this.txtFreezeAmount.getText());
        if(amt!=null){
            if(amt.doubleValue()<=0)
                return true;
        }
        return false;
    }
    private void btnFreezeSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFreezeSaveActionPerformed
        // Add your handling code here:
        String fslNo= observable.getFreezeNo();
        String mandatoryMessage;
        mandatoryMessage = checkMandatory(this.panDepositDetails);
        if(mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
            return;
        }
        mandatoryMessage = checkMandatory(this.panFreezeInfo);
        if(mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
            return;
        }
        String freezeType = (String)((ComboBoxModel)this.cboFreezeType.getModel()).getKeyForSelected();
        double clearBalance = CommonUtil.convertObjToDouble(lblClearBalanceValue.getText()).doubleValue();
        double FreezeAmt = CommonUtil.convertObjToDouble(txtFreezeAmount.getText()).doubleValue();
         if(freezeType.equalsIgnoreCase("PARTIAL"))
        {
            if(clearBalance == FreezeAmt){
           COptionPane.showMessageDialog(this,resourceBundle.getString("PARTWARNING"));
            return; 
            }
        }
          if(freezeType.equalsIgnoreCase("COMPLETE"))
        {
            if(clearBalance != FreezeAmt){
           COptionPane.showMessageDialog(this,resourceBundle.getString("COMPWARNING"));
            return; 
            }
        }
        if(checkZeroAmount()){
            COptionPane.showMessageDialog(this,resourceBundle.getString("ZEROAMOUNT_WARNING"));
            return;
        }
        if(!this._intFreezeNew){
            if(this.tblFreeze.getSelectedRow()!=-1) {
                ArrayList arr =((TableModel)tblFreeze.getModel()).getRow(this.tblFreeze.getSelectedRow());
                this.updateBalance(CommonUtil.convertObjToStr(arr.get(1)),false);
            }
        }
        if(!checkValidFreezeAmount()){
            COptionPane.showMessageDialog(this,resourceBundle.getString("FREEZESUM_WARNING"));
            if(!this._intFreezeNew){
                if(this.tblFreeze.getSelectedRow()!=-1) {
                    ArrayList arr =((TableModel)tblFreeze.getModel()).getRow(this.tblFreeze.getSelectedRow());
                    this.updateBalance(CommonUtil.convertObjToStr(arr.get(1)),true);
                }
            }
            return;
        }
        updateFreezeOBFields();
        if(!this._intFreezeNew)
            observable.insertFreezeData(this.tblFreeze.getSelectedRow());
        else
         observable.insertFreezeData(-1);
         HashMap lockMap = new HashMap();
         ArrayList lst = new ArrayList();
         lst.add("FREEZE_NO");
         lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
          if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT) {
                    lockMap.put("FREEZE_NO", fslNo);
                
                setEditLockMap(lockMap);
                setEditLock();
          }
        this.updateTable();
        this.updateBalance(this.txtFreezeAmount.getText(),true);
        this.btnFreezeNew.setEnabled(true);
        panFreezeEnableDisable(false);
    }//GEN-LAST:event_btnFreezeSaveActionPerformed
    
    private void btnFreezeNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFreezeNewActionPerformed
        // Add your handling code here:
        panFreezeEnableDisable(true);
        this.btnUnFreeze.setEnabled(false);
        this.btnFreezeDelete.setEnabled(false);
        this._intFreezeNew=true;
        this.tdtFreezeDate.setDateValue(DateUtil.getStringDate((Date) currDt.clone()));
        this.tdtFreezeDate.setEnabled(false);
        observable.setFreezeStatus(CommonConstants.STATUS_CREATED);
    }//GEN-LAST:event_btnFreezeNewActionPerformed
    private String checkMandatory(JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(),component);
    }
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    private void updateFreezeOBFields(){
        observable.setTxtFreezeAmount(this.txtFreezeAmount.getText());
        observable.setTdtFreezeDate(this.tdtFreezeDate.getDateValue());
        observable.setTxtFreezeRemark(this.txtRemark.getText());
        observable.setCboFreezeType((String)((ComboBoxModel)this.cboFreezeType.getModel()).getKeyForSelected());
        observable.setTxtDepositNo(this.txtDepositNo.getText());
        observable.setCboSubDepositNo((String)((ComboBoxModel)this.cboSubDepositNo.getModel()).getKeyForSelected());
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
//        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        updateAuthorizeStatus(ClientConstants.ACTIONTYPE_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        updateAuthorizeStatus(ClientConstants.ACTIONTYPE_REJECT);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        updateAuthorizeStatus(ClientConstants.ACTIONTYPE_AUTHORIZE);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void updateAuthorizeStatus(int actionType){
        if( !this.btnNew.isEnabled() && (viewType==ClientConstants.ACTIONTYPE_AUTHORIZE ||
        viewType==ClientConstants.ACTIONTYPE_EXCEPTION ||
        viewType==ClientConstants.ACTIONTYPE_REJECT )){
            observable.setActionType(actionType);
            this.updateFreezeOBFields();
            if(!observable.authorizeStatus()){
                COptionPane.showMessageDialog(this,resourceBundle.getString("AUTHORIZE_WARNING"));
                return;
            }
             super.setOpenForEditBy(observable.getStatusBy1());
            super.removeEditLock(lblDepositFreezeNoDesc.getText());
            this.btnCancelActionPerformed(null);
            observable.setResultStatus();
            this.setButtonEnableDisable();
            btnSave.setEnabled(false);
            viewType=-1;
         
            return;
        }
        observable.setActionType(actionType);
        observable.setStatus();
        viewType=actionType;
        
        HashMap mapParam = new HashMap();
        mapParam.put(CommonConstants.MAP_NAME, "getAuthorizeFreezeEntries");
        mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeFreezeTO");
        AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
        authorizeUI.show();
    }
    public void authorize(HashMap screenMap){
        ArrayList selectedList = (ArrayList)screenMap.get(CommonConstants.AUTHORIZEDATA);
        String authorizeStatus = (String)screenMap.get(CommonConstants.AUTHORIZESTATUS);
        HashMap dataMap;
        String status;
        double amount=0,shadowFreezeAmt=0,bal=0;
        for (int i=0, j=selectedList.size(); i < j; i++) {
            dataMap = (HashMap) selectedList.get(i);
            dataMap.put("SELECTED_AUTHORIZE_STATUS", authorizeStatus);
            observable.setAuthorizeMap(dataMap);
            if(!observable.authorizeStatus()){
                COptionPane.showMessageDialog(this,resourceBundle.getString("AUTHORIZE_WARNING"));
                return;
            }
        }
        dataMap = null;
    }
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here
        setModified(false);
        for(int i=0; i<tblFreeze.getRowCount();i++) {
            String data= CommonUtil.convertObjToStr(tblFreeze.getValueAt(i,0));
            if(observable.getActionType() ==ClientConstants.ACTIONTYPE_EDIT)
                setMode(ClientConstants.ACTIONTYPE_EDIT);
            if(tblFreeze.getValueAt(i,4)!=null)
//            if(observable.getAuthorizeStatus1()!=null)
                super.removeEditLock(data);
        }
        observable.resetForm();
        setUp(ClientConstants.ACTIONTYPE_CANCEL,false);
        setButtonEnableDisable();
        enableDisableButtons(false);
        resetCustomerDetail();
        this.panFreezeEnableDisable(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        updateOBAmountFields();
        setModified(false);
        observable.doAction();
        HashMap lockMap = new HashMap();
        ArrayList lst = new ArrayList();
        lst.add("FREEZE_NO");
        lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW) {
            if (observable.getProxyReturnMap()!=null) {
                if (observable.getProxyReturnMap().containsKey("FREEZE_NO")) {
                    lst=(ArrayList)observable.getProxyReturnMap().get("FREEZE_NO");
                    for(int i=0;i<lst.size();i++) {
                        lockMap.put("FREEZE_NO",lst.get(i));
                        setEditLockMap(lockMap);
                        setEditLock();
                    }
                }
            }
        }
        
        btnCancelActionPerformed(null);
        observable.setResultStatus();
    }//GEN-LAST:event_btnSaveActionPerformed
    private void updateOBAmountFields(){
        observable.setLblShadowFreeze(this.lblShadowFreezeValue.getText());
    }
    private boolean checkValidFreezeAmount(){
        double amount=0,freezeAmount=0;
        Double bal = CommonUtil.convertObjToDouble(this.lblClearBalanceValue.getText());
        if(bal!=null)
            amount=bal.doubleValue();
        
        bal = CommonUtil.convertObjToDouble(this.txtFreezeAmount.getText());
        if(bal!=null)
            freezeAmount=bal.doubleValue();
        
        bal = CommonUtil.convertObjToDouble(this.lblShadowFreezeValue.getText());
        if(bal!=null)
            freezeAmount+=bal.doubleValue();
        
        if(freezeAmount>amount)
            return false;
        return true;
    }
    private void resetCustomerDetail(){
        this.lblCustomerIDValue.setText("");
        this.lblCustomerNameValue.setText("");
        this.lblAccountHDValue.setText("");
        this.lblAccountHDValue.setToolTipText(lblAccountHDValue.getText());
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        setUp(ClientConstants.ACTIONTYPE_DELETE, false);
        callView(ClientConstants.ACTIONTYPE_DELETE);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        btnFreezeDelete.setEnabled(false);
        callView(ClientConstants.ACTIONTYPE_EDIT);
        
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        setModified(true);
        setUp(ClientConstants.ACTIONTYPE_NEW, true);
        setButtonEnableDisable();
        enableDisableButtons(true);
        ClientUtil.enableDisable(this.panFreezeInfo,false);
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        this.btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // Add your handling code here:
        this.btnPrintActionPerformed(evt);
    }//GEN-LAST:event_mitPrintActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        this.btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        this.btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        this.btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        this.btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        this.btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void cboProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIDActionPerformed
        // Add your handling code here:
        String prodId=(String)((ComboBoxModel)this.cboProductID.getModel()).getKeyForSelected();
        if(prodId!=null && prodId.length()>0){
            String actHead = observable.getAccountHead(prodId);
            this.lblAccountHDValue.setText(actHead);
            this.lblAccountHDValue.setToolTipText(lblAccountHDValue.getText());
        }else
            observable.resetForm();
    }//GEN-LAST:event_cboProductIDActionPerformed
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("tdtFreezeDate", new Boolean(true));
        mandatoryMap.put("txtFreezeAmount", new Boolean(true));
        mandatoryMap.put("cboFreezeType", new Boolean(true));
        mandatoryMap.put("txtRemark", new Boolean(true));
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("cboSubDepositNo", new Boolean(true));
        mandatoryMap.put("txtDepositNo", new Boolean(true));
    }
    
    /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new DepositFreezeMRB();
        tdtFreezeDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtFreezeDate"));
        txtFreezeAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFreezeAmount"));
        cboFreezeType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboFreezeType"));
        txtRemark.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemark"));
        cboProductID.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductID"));
        cboSubDepositNo.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSubDepositNo"));
        txtDepositNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDepositNo"));
        
    }
    public void update(Observable o, Object arg) {
        this.lblStatus.setText(observable.getLblStatus());
        this.cboFreezeType.setModel(observable.getCbmFreezeType());
        this.cboProductID.setModel(observable.getCbmProductId());
        this.cboSubDepositNo.setModel(observable.getCbmSubDepositNos());
        this.txtDepositNo.setText(observable.getTxtDepositNo());
       
        this.updateTable();
        
        this.populateFreezeDetail();
        this.updateAmountDetails();
        resetCustomerDetail();
    }
    private void callView(int viewType){
        HashMap viewMap = new HashMap();
        HashMap where = new HashMap();
        this.viewType=viewType;
        if (viewType==DEPOSIT_ACCT){
            viewMap.put(CommonConstants.MAP_NAME,"getDepositAccounts");
            where.put("PRODID",((ComboBoxModel)this.cboProductID.getModel()).getKeyForSelected());
//            where.put("DEPFREEZECHECK","");
            where.put("DATE",currDt.clone());
            viewMap.put(CommonConstants.MAP_WHERE, where);
        }
        else if (viewType==ClientConstants.ACTIONTYPE_EDIT || viewType==ClientConstants.ACTIONTYPE_VIEW) {
            viewMap.put(CommonConstants.MAP_NAME,"getEditFreezeEntries");
            where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);        
        }else if (viewType==ClientConstants.ACTIONTYPE_DELETE) {
            viewMap.put(CommonConstants.MAP_NAME,"getDeleteFreezeEntries");
            where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
        }
        new ViewAll(this,viewMap).show();
    }
    public void fillData(Object obj){
        try{
            HashMap hashMap=(HashMap)obj;
            System.out.println("#$#$#$ obj : "+obj);
            
            HashMap returnMap=null;
            
            if(viewType==ClientConstants.ACTIONTYPE_EDIT|| viewType==ClientConstants.ACTIONTYPE_VIEW){
                ClientUtil.enableDisable(this,true);
                this.setButtonEnableDisable();
                this.enableDisableButtons(true);
                ClientUtil.enableDisable(this.panDepositDetails, false);
                this.btnDepositNo.setEnabled(false);
                ClientUtil.enableDisable(this.panFreezeInfo,false);
            }
            if(viewType==ClientConstants.ACTIONTYPE_DELETE){
                this.setButtonEnableDisable();
                this.enableDisableButtons(false);
            }
            
            
            
            if(viewType==DEPOSIT_ACCT || viewType==ClientConstants.ACTIONTYPE_EDIT ||
                viewType==ClientConstants.ACTIONTYPE_DELETE ||
                viewType==ClientConstants.ACTIONTYPE_AUTHORIZE ||
                viewType==ClientConstants.ACTIONTYPE_EXCEPTION ||
                viewType==ClientConstants.ACTIONTYPE_REJECT ||
                viewType==ClientConstants.ACTIONTYPE_VIEW){
                    
                this.txtDepositNo.setText((String)hashMap.get("DEPOSIT_ACT_NUM"));
                
                
                observable.setTxtDepositNo(txtDepositNo.getText());
                returnMap = observable.getDepositActIfno();
                
                
                this.lblCustomerIDValue.setText((String)returnMap.get("CUST_ID"));
                this.lblCustomerNameValue.setText((String)returnMap.get("CUSTOMER_NAME"));
                this.lblCustomerNameValue.setToolTipText(lblCustomerNameValue.getText());
                observable.getSubDepositNos(this.txtDepositNo.getText());
                this.cboSubDepositNo.setModel(observable.getCbmSubDepositNos());
                this.cboSubDepositNoActionPerformed(null);
            }
            if(viewType==ClientConstants.ACTIONTYPE_EDIT ||
                viewType==ClientConstants.ACTIONTYPE_DELETE ||
                viewType==ClientConstants.ACTIONTYPE_AUTHORIZE ||
                viewType==ClientConstants.ACTIONTYPE_EXCEPTION ||
                viewType==ClientConstants.ACTIONTYPE_VIEW ||
                viewType==ClientConstants.ACTIONTYPE_REJECT) {
                
                ((ComboBoxModel)this.cboProductID.getModel()).setKeyForSelected((String)returnMap.get("PROD_ID"));
                this.lblAccountHDValue.setText((String)returnMap.get("HD_DESC"));
                //((ComboBoxModel)this.cboSubDepositNo.getModel()).setKeyForSelected((String)hashMap.get("SUBNO"));
                ((ComboBoxModel) this.cboSubDepositNo.getModel()).setKeyForSelected(
                        String.valueOf(hashMap.get("SUBNO"))
                );
                observable.setCboSubDepositNo(String.valueOf(hashMap.get("SUBNO")));
                observable.setLblDepositFreezeNoDesc((String)hashMap.get("FSLNO"));

                this.cboSubDepositNoActionPerformed(null);
            }
            if(viewType==ClientConstants.ACTIONTYPE_AUTHORIZE ||
                viewType==ClientConstants.ACTIONTYPE_EXCEPTION ||
                viewType==ClientConstants.ACTIONTYPE_REJECT ||
                viewType==ClientConstants.ACTIONTYPE_VIEW){
                
                observable.populateFreeze((String)hashMap.get("FSLNO"));
                observable.setAuthorizeMap(hashMap);
                populateFreezeDetail();
                setAuthorizeButtons();
            }
            
               if( viewType==ClientConstants.ACTIONTYPE_VIEW)
               {
                   ClientUtil.enableDisable(panDepositDetails, false);
                   ClientUtil.enableDisable(panFreezeInfo, false);
                   
               }
            hashMap=null;
            returnMap=null;
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void setAuthorizeButtons(){
        this.btnDelete.setEnabled(false);
        this.btnNew.setEnabled(false);
        this.btnEdit.setEnabled(false);
        this.btnSave.setEnabled(false);
        this.btnCancel.setEnabled(true);
    }
    private void updateBalance(String amount,boolean add){
        double shadowFreezeValue=0,freezeAmt=0;
        if(amount!=null && amount.length()>0){
            Double amt = CommonUtil.convertObjToDouble(amount);
            if(amt!=null){
                freezeAmt = amt.doubleValue();
                
                amt = CommonUtil.convertObjToDouble(this.lblShadowFreezeValue.getText());
                if(amt!=null)
                    shadowFreezeValue = amt.doubleValue();
                
                if(add){
                    shadowFreezeValue+=freezeAmt;
                }
                if(!add){
                    shadowFreezeValue-=freezeAmt;
                }
                this.lblShadowFreezeValue.setText(String.valueOf(shadowFreezeValue));
            }
            
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDepositNo;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnFreezeDelete;
    private com.see.truetransact.uicomponent.CButton btnFreezeNew;
    private com.see.truetransact.uicomponent.CButton btnFreezeSave;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnUnFreeze;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboFreezeType;
    private com.see.truetransact.uicomponent.CComboBox cboProductID;
    private com.see.truetransact.uicomponent.CComboBox cboSubDepositNo;
    private com.see.truetransact.uicomponent.CLabel lblAccountHD;
    private com.see.truetransact.uicomponent.CLabel lblAccountHDValue;
    private com.see.truetransact.uicomponent.CLabel lblClearBalance;
    private com.see.truetransact.uicomponent.CLabel lblClearBalanceValue;
    private com.see.truetransact.uicomponent.CLabel lblCustomerID;
    private com.see.truetransact.uicomponent.CLabel lblCustomerIDValue;
    private com.see.truetransact.uicomponent.CLabel lblCustomerNameValue;
    private com.see.truetransact.uicomponent.CLabel lblDepositAmt;
    private com.see.truetransact.uicomponent.CLabel lblDepositAmtValue;
    private com.see.truetransact.uicomponent.CLabel lblDepositFreezeNo;
    private com.see.truetransact.uicomponent.CLabel lblDepositFreezeNoDesc;
    private com.see.truetransact.uicomponent.CLabel lblDepositNo;
    private com.see.truetransact.uicomponent.CLabel lblFreezeAmount;
    private com.see.truetransact.uicomponent.CLabel lblFreezeDate;
    private com.see.truetransact.uicomponent.CLabel lblFreezeSum;
    private com.see.truetransact.uicomponent.CLabel lblFreezeSumValue;
    private com.see.truetransact.uicomponent.CLabel lblFreezeType;
    private com.see.truetransact.uicomponent.CLabel lblLienSum;
    private com.see.truetransact.uicomponent.CLabel lblLienSumValue;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblRemark;
    private com.see.truetransact.uicomponent.CLabel lblShadowFreeze;
    private com.see.truetransact.uicomponent.CLabel lblShadowFreezeValue;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace25;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblSubDepositNo;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panCustomerDetail;
    private com.see.truetransact.uicomponent.CPanel panDepositDetails;
    private com.see.truetransact.uicomponent.CPanel panDepositInfo;
    private com.see.truetransact.uicomponent.CPanel panDepositNo;
    private com.see.truetransact.uicomponent.CPanel panFreeze;
    private com.see.truetransact.uicomponent.CPanel panFreezeButton;
    private com.see.truetransact.uicomponent.CPanel panFreezeDetails;
    private com.see.truetransact.uicomponent.CPanel panFreezeInfo;
    private com.see.truetransact.uicomponent.CPanel panFreezeTable;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CSeparator sptDeposit;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpFreeze;
    private com.see.truetransact.uicomponent.CTable tblFreeze;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtFreezeDate;
    private com.see.truetransact.uicomponent.CTextField txtDepositNo;
    private com.see.truetransact.uicomponent.CTextField txtFreezeAmount;
    private com.see.truetransact.uicomponent.CTextField txtRemark;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] args) {
        DepositFreezeUI lui = new DepositFreezeUI();
        JFrame j = new JFrame();
        j.getContentPane().add(lui);
        j.setSize(620,500);
        j.show();
        lui.show();
    }
}
