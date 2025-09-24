    /*
     * SecurityInsuranceUI.java
     *
     * Created on January 12, 2005, 5:20 PM
     */

package com.see.truetransact.ui.customer.goldsecurity;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.*;
import com.see.truetransact.ui.customer.security.*;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.GoldLoanItemView;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
/**
 *
 * @author  152713
 */
public class CustomerGoldSecurityUI extends CInternalFrame  implements java.util.Observer,UIMandatoryField {
    HashMap mandatoryMap;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    SecurityInsuranceRB resourceBundle = new SecurityInsuranceRB();    
    CustomerGoldSecurityOB observableSecurity;
    InsuranceOB observableInsurance;
    private boolean updateSecurity = false;
    private boolean updateInsurance = false;
    private boolean isFilled = false;    
    private final static Logger log = Logger.getLogger(CustomerGoldSecurityUI.class);
    private final        String AUTHORIZE = "AUTHORIZE";
    private final        String GOLD_SILVER_ORNAMENTS = "GOLD_SILVER_ORNAMENTS";    
    int result;
    int rowSecurity  = -1;
    int rowInsurance = -1;    
    private String viewType = "";
    private Date currDt = null;
    
    
    /** Creates new form SecurityInsuranceUI */
    public CustomerGoldSecurityUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
      //  setFieldNames();
        //internationalize();
        setMaxLength();
        setObservable();
        ClientUtil.enableDisable(this, false);
        allEnableDisable();
        initComponentData();
        setMandatoryHashMap();       
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panSecDetails);        
        setHelpMessage();
        observableSecurity.resetForm();
        observableSecurity.resetStatus();
    }
    private void allEnableDisable(){
        setButtonEnableDisable();
//        txtTotalSecurity_Value.setEnabled(true);
        //txtTotalSecurity_Value.setEditable(false);
        setAllSecurityDetailsEnableDisable(false);
        setAllSecurityBtnsEnableDisable(true);
       
    }
    
    private void setObservable(){
        observableSecurity = CustomerGoldSecurityOB.getInstance();
        observableSecurity.addObserver(this);
        observableInsurance = InsuranceOB.getInstance();
        observableInsurance.addObserver(this);        
    }
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {       
        panSecurityDetails_security.setName("panSecurityDetails_security");
        panLoanAvailed.setName("panLoanAvailed");
        panLoanTable.setName("panLoanTable");
        srpLoanTable.setName("srpLoanTable");
        tblLoanTable.setName("tblLoanTable");
        tabSecurityDetails.setName("tabSecurityDetails");
        lblCustID.setName("lblCustID");       
        lblCustName.setName("lblCustName");
        lblCustName_Disp.setName("lblCustName_Disp");       
        PanSecurityDetails.setName("PanSecurityDetails");
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
//        btnSecurityDelete.setName("btnSecurityDelete");
      //  btnSecurityNew.setName("btnSecurityNew");
        btnSecuritySave.setName("btnSecuritySave");        
        cboPurity.setName("cboSecurityCate");       
        lblAson.setName("lblAson");        
        lblParticulars.setName("lblParticulars");        
        lblPurity.setName("lblSecurityCate");
        //lblSecurityNo.setName("lblSecurityNo");        
        lblGrossWeight.setName("lblSecurityValue");       
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");       
//        lblTotalSecurity_Value.setName("lblTotalSecurity_Value");
        lblspace3.setName("lblspace3");
        mbrSecInsur.setName("mbrSecInsur");       
        panSecDetails.setName("panSecDetails");
        panSecNature.setName("panSecNature");
        panSecurity.setName("panSecurity");
        panSecurityDetails.setName("panSecurityDetails");
        panSecurityDetails_Cust.setName("panSecurityDetails_Cust");
        panSecurityDetails_security.setName("panSecurityDetails_security");        
        panSecurityTable.setName("panSecurityTable");
        panSecurityTableMain.setName("panSecurityTableMain");
        panSecurityTools.setName("panSecurityTools");
        panSecurityType.setName("panSecurityType");
        panTotalSecurity_Value.setName("panTotalSecurity_Value");        
        sptSecurityDetails_Vert.setName("sptSecurityDetails_Vert");        
        srpSecurityTable.setName("srpSecurityTable");        
        tblSecurityTable.setName("tblSecurityTable");
        tdtAson.setName("tdtAson");       
        txtAreaParticular.setName("txtAreaParticular");        
        //txtGrossWeight.setName("txtSecurityValue");
//        txtTotalSecurity_Value.setName("txtTotalSecurity_Value");
    }
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
       // btnSecurityDelete.setText(resourceBundle.getString("btnSecurityDelete"));       
        btnSave.setText(resourceBundle.getString("btnSave"));       
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblCustID.setText(resourceBundle.getString("lblCustID"));       
        lblCustName.setText(resourceBundle.getString("lblCustName"));
        lblCustName_Disp.setText(resourceBundle.getString("lblCustName_Disp"));       
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        //btnSecurityNew.setText(resourceBundle.getString("btnSecurityNew"));       
        ((javax.swing.border.TitledBorder)panSecurityDetails_Cust.getBorder()).setTitle(resourceBundle.getString("panSecurityDetails_Cust"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        //lblSecurityNo.setText(resourceBundle.getString("lblSecurityNo"));        
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblGrossWeight.setText(resourceBundle.getString("lblSecurityValue"));        
        btnPrint.setText(resourceBundle.getString("btnPrint"));        
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnSecuritySave.setText(resourceBundle.getString("btnSecuritySave"));
        lblAson.setText(resourceBundle.getString("lblAson"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));        
        lblPurity.setText(resourceBundle.getString("lblSecurityCate"));        
        lblParticulars.setText(resourceBundle.getString("lblParticulars"));
//        lblTotalSecurity_Value.setText(resourceBundle.getString("lblTotalSecurity_Value"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));       
    }
    
    
   
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {           
        tblLoanTable.setModel(observableSecurity.getTblSecurityLoanTab());        
        lblStatus.setText(observableSecurity.getLblStatus());       
        lblCustName_Disp.setText(observableSecurity.getLblCustName_Disp());
        tblSecurityTable.setModel(observableSecurity.getTblSecurityTab());        
        txtSecurityNo.setText(observableSecurity.getTxtSecurityNo());
        cboPurity.setSelectedItem(observableSecurity.getCboPurity());    
        cboAppraiserId.setSelectedItem(observableSecurity.getCboAppraiserId());    
        txtSecurityValue.setText(observableSecurity.getTxtSecurityValue());
        tdtAson.setDateValue(observableSecurity.getTdtAsOnDt());
        txtAreaParticular.setText(observableSecurity.getTxtParticulars());       
        //txtTotalSecurity_Value.setText(observableSecurity.getTxtTotalSecurity_Value());
        txtNetWeight.setText(observableSecurity.getTxtNetWeight());
        txtGrossWeight.setText(observableSecurity.getTxtGrossWeight());           
        lblStatus.setText(observableSecurity.getLblStatus());
        lblCustName_Disp.setText(observableSecurity.getLblCustName_Disp());      
        txtMarketRate.setText(observableSecurity.getTxtMarketRate());
        txtPledgeAmount.setText(observableSecurity.getTxtPledgeAmount());
        txtCustId.setText(observableSecurity.getTxtCustId());
        lblGoldSecurityId.setText(observableSecurity.getTxtGoldSecurityId());
        txtMemberNo.setText(observableSecurity.getTxtMemberNo());
        if (observableSecurity.getPhotoByteArray() != null) {
            lblPhoto.setIcon(new javax.swing.ImageIcon(observableSecurity.getPhotoByteArray()));
        }
    }
    
 
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {        
        observableSecurity.setTxtSecurityNo(txtSecurityNo.getText());
        observableSecurity.setSelectedBranchID(getSelectedBranchID());         
        observableSecurity.setLblCustName_Disp(lblCustName_Disp.getText());        
        if (tblLoanTable != null && tblLoanTable.getRowCount() > 0){
            observableSecurity.setTblSecurityLoanTab((com.see.truetransact.clientutil.EnhancedTableModel) tblLoanTable.getModel());
        }    
        observableSecurity.setCboPurity(CommonUtil.convertObjToStr(((ComboBoxModel)cboPurity.getModel()).getKeyForSelected()));
        observableSecurity.setCboAppraiserId(CommonUtil.convertObjToStr(((ComboBoxModel)cboAppraiserId.getModel()).getKeyForSelected()));
        observableSecurity.setTxtGrossWeight(txtGrossWeight.getText());
        observableSecurity.setTxtNetWeight(txtNetWeight.getText());
        observableSecurity.setTxtMarketRate(txtMarketRate.getText());
        observableSecurity.setTdtAsOnDt(tdtAson.getDateValue());
        observableSecurity.setTxtSecurityValue(txtSecurityValue.getText());
        observableSecurity.setTxtPledgeAmount(txtPledgeAmount.getText());
        observableSecurity.setTxtParticulars(txtAreaParticular.getText());             
        observableSecurity.setTxtCustId(txtCustId.getText());
        observableSecurity.setTxtGoldSecurityId(lblGoldSecurityId.getText());
        observableSecurity.setTxtMemberNo(txtMemberNo.getText());
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();       
        mandatoryMap.put("cboPurity", new Boolean(true));
        mandatoryMap.put("txtGrossWeight", new Boolean(true));
        mandatoryMap.put("txtNetWeight", new Boolean(true));
        mandatoryMap.put("tdtAson", new Boolean(true));
        mandatoryMap.put("txtParticulars", new Boolean(true));
        mandatoryMap.put("txtMarketRate", new Boolean(true));
        mandatoryMap.put("tdtToDate", new Boolean(true));
        mandatoryMap.put("txtSecurityValue", new Boolean(true));
        mandatoryMap.put("txtPledgeAmount", new Boolean(true));        
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        SecurityInsuranceMRB objMandatoryRB = new SecurityInsuranceMRB();        
        cboPurity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSecurityCate"));       
        txtGrossWeight.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSecurityValue"));
        tdtAson.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtAson"));
        txtAreaParticular.setHelpMessage(lblMsg, objMandatoryRB.getString("txtParticulars"));       
//        txtTotalSecurity_Value.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalSecurity_Value"));        
    }
    
    private void setMaxLength(){        
        txtGrossWeight.setMaxLength(16);        
        txtSecurityValue.setValidation(new CurrencyValidation(14,2));
        txtPledgeAmount.setValidation(new CurrencyValidation(14,2));
        txtAreaParticular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {               
                int length = txtAreaParticular.getText().length();
                if( length >= 250 ){
                    if( checkInValidCharacter(evt.getKeyChar())){
                        evt.consume();
                    }
                }
            }
        }
        );            
    }
    
    private boolean checkInValidCharacter(char keyChar){
        if (!((keyChar == java.awt.event.KeyEvent.VK_BACK_SPACE) ||
        (keyChar == java.awt.event.KeyEvent.VK_DELETE))) {
            return true;
        }
        return false;
    }
    
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
        setAuthBtnEnableDisable();
    }
    
    /** To Enable or Disable Authorize, Rejection and Exception Button */
    private void setAuthBtnEnableDisable(){
        final boolean enableDisable = !btnSave.isEnabled();
        btnAuthorize.setEnabled(enableDisable);
        btnException.setEnabled(enableDisable);
        btnReject.setEnabled(enableDisable);
        mitAuthorize.setEnabled(enableDisable);
        mitException.setEnabled(enableDisable);
        mitReject.setEnabled(enableDisable);
    }
    
    private void initComponentData() {
       cboPurity.setModel(observableSecurity.getCbmPurity());
       cboAppraiserId.setModel(observableSecurity.getCbmAppraiserId());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoSecurityType = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrSecInsur = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblspace4 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblspace3 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        PanSecurityDetails = new com.see.truetransact.uicomponent.CPanel();
        panSecurityDetails = new com.see.truetransact.uicomponent.CPanel();
        tabSecurityDetails = new com.see.truetransact.uicomponent.CTabbedPane();
        panSecurityDetails_security = new com.see.truetransact.uicomponent.CPanel();
        panSecDetails = new com.see.truetransact.uicomponent.CPanel();
        lblPurity = new com.see.truetransact.uicomponent.CLabel();
        cboPurity = new com.see.truetransact.uicomponent.CComboBox();
        lblGrossWeight = new com.see.truetransact.uicomponent.CLabel();
        txtGrossWeight = new com.see.truetransact.uicomponent.CTextField();
        lblAson = new com.see.truetransact.uicomponent.CLabel();
        tdtAson = new com.see.truetransact.uicomponent.CDateField();
        panSecurityType = new com.see.truetransact.uicomponent.CPanel();
        lblNetWeight = new com.see.truetransact.uicomponent.CLabel();
        txtNetWeight = new com.see.truetransact.uicomponent.CTextField();
        lblMarketRate = new com.see.truetransact.uicomponent.CLabel();
        txtMarketRate = new com.see.truetransact.uicomponent.CTextField();
        btnSecurityAdd = new com.see.truetransact.uicomponent.CButton();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        txtSecurityValue = new com.see.truetransact.uicomponent.CTextField();
        txtPledgeAmount = new com.see.truetransact.uicomponent.CTextField();
        cLabel3 = new com.see.truetransact.uicomponent.CLabel();
        txtSecurityNo = new com.see.truetransact.uicomponent.CTextField();
        panSecNature = new com.see.truetransact.uicomponent.CPanel();
        srpPhoto = new com.see.truetransact.uicomponent.CScrollPane();
        lblPhoto = new com.see.truetransact.uicomponent.CLabel();
        btnLoadPhoto = new com.see.truetransact.uicomponent.CButton();
        btnPhotoRemove = new com.see.truetransact.uicomponent.CButton();
        sptSecurityDetails_Vert = new com.see.truetransact.uicomponent.CSeparator();
        panSecurityTools = new com.see.truetransact.uicomponent.CPanel();
        btnSecuritySave = new com.see.truetransact.uicomponent.CButton();
        panparticulars = new com.see.truetransact.uicomponent.CPanel();
        lblParticulars = new com.see.truetransact.uicomponent.CLabel();
        srpTxtAreaParticulars = new com.see.truetransact.uicomponent.CScrollPane();
        txtAreaParticular = new com.see.truetransact.uicomponent.CTextArea();
        lblAppraiserId = new com.see.truetransact.uicomponent.CLabel();
        cboAppraiserId = new com.see.truetransact.uicomponent.CComboBox();
        panLoanAvailed = new com.see.truetransact.uicomponent.CPanel();
        panLoanTable = new com.see.truetransact.uicomponent.CPanel();
        srpLoanTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblLoanTable = new com.see.truetransact.uicomponent.CTable();
        panSecurityTableMain = new com.see.truetransact.uicomponent.CPanel();
        panSecurityTable = new com.see.truetransact.uicomponent.CPanel();
        srpSecurityTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblSecurityTable = new com.see.truetransact.uicomponent.CTable();
        panTotalSecurity_Value = new com.see.truetransact.uicomponent.CPanel();
        panSecurityDetails_Cust = new com.see.truetransact.uicomponent.CPanel();
        panSecurity = new com.see.truetransact.uicomponent.CPanel();
        lblCustID = new com.see.truetransact.uicomponent.CLabel();
        lblCustName = new com.see.truetransact.uicomponent.CLabel();
        lblMemberNo = new com.see.truetransact.uicomponent.CLabel();
        txtMemberNo = new com.see.truetransact.uicomponent.CTextField();
        txtCustId = new com.see.truetransact.uicomponent.CTextField();
        btnMemberNo = new com.see.truetransact.uicomponent.CButton();
        btnCustId = new com.see.truetransact.uicomponent.CButton();
        lblCustName_Disp = new com.see.truetransact.uicomponent.CLabel();
        cLabel4 = new com.see.truetransact.uicomponent.CLabel();
        lblGoldSecurityId = new com.see.truetransact.uicomponent.CLabel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrSecInsur = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptProcess = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitAuthorize = new javax.swing.JMenuItem();
        mitException = new javax.swing.JMenuItem();
        mitReject = new javax.swing.JMenuItem();
        sptException = new javax.swing.JSeparator();
        sptPrint = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(850, 675));
        setMinimumSize(new java.awt.Dimension(850, 670));
        setPreferredSize(new java.awt.Dimension(850, 670));

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
        tbrSecInsur.add(btnView);

        lblspace4.setMaximumSize(new java.awt.Dimension(15, 15));
        lblspace4.setMinimumSize(new java.awt.Dimension(15, 15));
        lblspace4.setPreferredSize(new java.awt.Dimension(15, 15));
        tbrSecInsur.add(lblspace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrSecInsur.add(btnNew);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSecInsur.add(lblSpace26);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrSecInsur.add(btnEdit);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSecInsur.add(lblSpace27);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrSecInsur.add(btnDelete);

        lblSpace2.setText("     ");
        tbrSecInsur.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrSecInsur.add(btnSave);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSecInsur.add(lblSpace28);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrSecInsur.add(btnCancel);

        lblSpace3.setText("     ");
        tbrSecInsur.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setEnabled(false);
        btnAuthorize.setMaximumSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setMinimumSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setPreferredSize(new java.awt.Dimension(29, 27));
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrSecInsur.add(btnAuthorize);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSecInsur.add(lblSpace29);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setEnabled(false);
        btnException.setMaximumSize(new java.awt.Dimension(29, 27));
        btnException.setMinimumSize(new java.awt.Dimension(29, 27));
        btnException.setPreferredSize(new java.awt.Dimension(29, 27));
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrSecInsur.add(btnException);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSecInsur.add(lblSpace30);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setEnabled(false);
        btnReject.setMaximumSize(new java.awt.Dimension(29, 27));
        btnReject.setMinimumSize(new java.awt.Dimension(29, 27));
        btnReject.setPreferredSize(new java.awt.Dimension(29, 27));
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrSecInsur.add(btnReject);

        lblspace3.setMaximumSize(new java.awt.Dimension(15, 15));
        lblspace3.setMinimumSize(new java.awt.Dimension(15, 15));
        lblspace3.setPreferredSize(new java.awt.Dimension(15, 15));
        tbrSecInsur.add(lblspace3);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrSecInsur.add(btnPrint);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSecInsur.add(lblSpace31);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrSecInsur.add(btnClose);

        getContentPane().add(tbrSecInsur, java.awt.BorderLayout.NORTH);

        PanSecurityDetails.setMinimumSize(new java.awt.Dimension(830, 650));
        PanSecurityDetails.setPreferredSize(new java.awt.Dimension(830, 650));

        panSecurityDetails.setMinimumSize(new java.awt.Dimension(825, 335));
        panSecurityDetails.setPreferredSize(new java.awt.Dimension(825, 335));

        tabSecurityDetails.setMinimumSize(new java.awt.Dimension(513, 325));
        tabSecurityDetails.setPreferredSize(new java.awt.Dimension(513, 325));

        panSecurityDetails_security.setMinimumSize(new java.awt.Dimension(508, 185));
        panSecurityDetails_security.setPreferredSize(new java.awt.Dimension(508, 185));

        panSecDetails.setMinimumSize(new java.awt.Dimension(240, 190));
        panSecDetails.setPreferredSize(new java.awt.Dimension(240, 190));
        panSecDetails.setLayout(new java.awt.GridBagLayout());

        lblPurity.setText("Purity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSecDetails.add(lblPurity, gridBagConstraints);

        cboPurity.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPurity.setPopupWidth(250);
        cboPurity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPurityActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
        panSecDetails.add(cboPurity, gridBagConstraints);

        lblGrossWeight.setText("Gross Weight");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSecDetails.add(lblGrossWeight, gridBagConstraints);

        txtGrossWeight.setAllowNumber(true);
        txtGrossWeight.setMinimumSize(new java.awt.Dimension(100, 21));
        txtGrossWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGrossWeightFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
        panSecDetails.add(txtGrossWeight, gridBagConstraints);

        lblAson.setText("As On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panSecDetails.add(lblAson, gridBagConstraints);

        tdtAson.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtAson.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtAson.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtAsonFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
        panSecDetails.add(tdtAson, gridBagConstraints);

        panSecurityType.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panSecDetails.add(panSecurityType, gridBagConstraints);

        lblNetWeight.setText("Net Weight");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panSecDetails.add(lblNetWeight, gridBagConstraints);

        txtNetWeight.setAllowNumber(true);
        txtNetWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNetWeightFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panSecDetails.add(txtNetWeight, gridBagConstraints);

        lblMarketRate.setText("Market Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        panSecDetails.add(lblMarketRate, gridBagConstraints);

        txtMarketRate.setAllowNumber(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panSecDetails.add(txtMarketRate, gridBagConstraints);

        btnSecurityAdd.setText("Gold Items");
        btnSecurityAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSecurityAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        panSecDetails.add(btnSecurityAdd, gridBagConstraints);

        cLabel1.setText("Security Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        panSecDetails.add(cLabel1, gridBagConstraints);

        cLabel2.setText("Pledge Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        panSecDetails.add(cLabel2, gridBagConstraints);

        txtSecurityValue.setAllowNumber(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSecDetails.add(txtSecurityValue, gridBagConstraints);

        txtPledgeAmount.setAllowNumber(true);
        txtPledgeAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPledgeAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSecDetails.add(txtPledgeAmount, gridBagConstraints);

        cLabel3.setText("Security No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panSecDetails.add(cLabel3, gridBagConstraints);

        txtSecurityNo.setAllowNumber(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panSecDetails.add(txtSecurityNo, gridBagConstraints);

        panSecNature.setMinimumSize(new java.awt.Dimension(250, 200));
        panSecNature.setPreferredSize(new java.awt.Dimension(250, 200));

        srpPhoto.setPreferredSize(new java.awt.Dimension(200, 200));
        srpPhoto.setViewportView(lblPhoto);

        btnLoadPhoto.setText("Load Photo");
        btnLoadPhoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadPhotoActionPerformed(evt);
            }
        });

        btnPhotoRemove.setText("Remove Photo");
        btnPhotoRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPhotoRemoveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panSecNatureLayout = new javax.swing.GroupLayout(panSecNature);
        panSecNature.setLayout(panSecNatureLayout);
        panSecNatureLayout.setHorizontalGroup(
            panSecNatureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(srpPhoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panSecNatureLayout.createSequentialGroup()
                .addGap(0, 14, Short.MAX_VALUE)
                .addComponent(btnLoadPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPhotoRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panSecNatureLayout.setVerticalGroup(
            panSecNatureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSecNatureLayout.createSequentialGroup()
                .addComponent(srpPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panSecNatureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLoadPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPhotoRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        sptSecurityDetails_Vert.setOrientation(javax.swing.SwingConstants.VERTICAL);

        panSecurityTools.setMinimumSize(new java.awt.Dimension(217, 30));
        panSecurityTools.setPreferredSize(new java.awt.Dimension(217, 30));
        panSecurityTools.setLayout(new java.awt.GridBagLayout());

        btnSecuritySave.setText("Save");
        btnSecuritySave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSecuritySaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurityTools.add(btnSecuritySave, gridBagConstraints);

        panparticulars.setMinimumSize(new java.awt.Dimension(500, 60));
        panparticulars.setPreferredSize(new java.awt.Dimension(500, 60));
        panparticulars.setLayout(new java.awt.GridBagLayout());

        lblParticulars.setText("Particulars");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panparticulars.add(lblParticulars, gridBagConstraints);

        srpTxtAreaParticulars.setMinimumSize(new java.awt.Dimension(400, 60));
        srpTxtAreaParticulars.setPreferredSize(new java.awt.Dimension(400, 60));

        txtAreaParticular.setBorder(javax.swing.BorderFactory.createBevelBorder(1));
        txtAreaParticular.setLineWrap(true);
        srpTxtAreaParticulars.setViewportView(txtAreaParticular);

        panparticulars.add(srpTxtAreaParticulars, new java.awt.GridBagConstraints());

        lblAppraiserId.setText("Appraise Id");

        javax.swing.GroupLayout panSecurityDetails_securityLayout = new javax.swing.GroupLayout(panSecurityDetails_security);
        panSecurityDetails_security.setLayout(panSecurityDetails_securityLayout);
        panSecurityDetails_securityLayout.setHorizontalGroup(
            panSecurityDetails_securityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panSecurityDetails_securityLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblAppraiserId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboAppraiserId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panSecurityTools, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panSecurityDetails_securityLayout.createSequentialGroup()
                .addGroup(panSecurityDetails_securityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panSecurityDetails_securityLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(panSecDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(sptSecurityDetails_Vert, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(panSecNature, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panparticulars, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panSecurityDetails_securityLayout.setVerticalGroup(
            panSecurityDetails_securityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSecurityDetails_securityLayout.createSequentialGroup()
                .addGroup(panSecurityDetails_securityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panSecDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sptSecurityDetails_Vert, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panSecurityDetails_securityLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(panSecNature, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(3, 3, 3)
                .addComponent(panparticulars, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panSecurityDetails_securityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panSecurityTools, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panSecurityDetails_securityLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panSecurityDetails_securityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAppraiserId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboAppraiserId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        tabSecurityDetails.addTab("Gold Security Details", panSecurityDetails_security);

        panLoanAvailed.setMinimumSize(new java.awt.Dimension(508, 200));
        panLoanAvailed.setPreferredSize(new java.awt.Dimension(508, 200));
        panLoanAvailed.setLayout(new java.awt.GridBagLayout());

        panLoanTable.setMinimumSize(new java.awt.Dimension(508, 220));
        panLoanTable.setPreferredSize(new java.awt.Dimension(508, 220));
        panLoanTable.setLayout(new java.awt.GridBagLayout());

        srpLoanTable.setMinimumSize(new java.awt.Dimension(508, 220));
        srpLoanTable.setPreferredSize(new java.awt.Dimension(508, 220));

        tblLoanTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblLoanTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblLoanTable.setMinimumSize(new java.awt.Dimension(490, 220));
        tblLoanTable.setPreferredSize(new java.awt.Dimension(490, 220));
        srpLoanTable.setViewportView(tblLoanTable);

        panLoanTable.add(srpLoanTable, new java.awt.GridBagConstraints());

        panLoanAvailed.add(panLoanTable, new java.awt.GridBagConstraints());

        tabSecurityDetails.addTab("Loans Availed Against This Security.", panLoanAvailed);

        panSecurityTableMain.setMinimumSize(new java.awt.Dimension(278, 155));
        panSecurityTableMain.setPreferredSize(new java.awt.Dimension(278, 155));
        panSecurityTableMain.setLayout(new java.awt.GridBagLayout());

        panSecurityTable.setMinimumSize(new java.awt.Dimension(270, 205));
        panSecurityTable.setPreferredSize(new java.awt.Dimension(270, 225));
        panSecurityTable.setLayout(new java.awt.GridBagLayout());

        srpSecurityTable.setMinimumSize(new java.awt.Dimension(270, 185));
        srpSecurityTable.setPreferredSize(new java.awt.Dimension(270, 185));

        tblSecurityTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Security No.", "Value", "Category", "As on"
            }
        ));
        tblSecurityTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblSecurityTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSecurityTableMousePressed(evt);
            }
        });
        srpSecurityTable.setViewportView(tblSecurityTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        panSecurityTable.add(srpSecurityTable, gridBagConstraints);

        panTotalSecurity_Value.setMinimumSize(new java.awt.Dimension(253, 55));
        panTotalSecurity_Value.setPreferredSize(new java.awt.Dimension(253, 55));
        panTotalSecurity_Value.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 15, 4);
        panSecurityTable.add(panTotalSecurity_Value, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSecurityTableMain.add(panSecurityTable, gridBagConstraints);

        javax.swing.GroupLayout panSecurityDetailsLayout = new javax.swing.GroupLayout(panSecurityDetails);
        panSecurityDetails.setLayout(panSecurityDetailsLayout);
        panSecurityDetailsLayout.setHorizontalGroup(
            panSecurityDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSecurityDetailsLayout.createSequentialGroup()
                .addComponent(tabSecurityDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(panSecurityTableMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panSecurityDetailsLayout.setVerticalGroup(
            panSecurityDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSecurityDetailsLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(panSecurityDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabSecurityDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panSecurityTableMain, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(65, Short.MAX_VALUE))
        );

        panSecurityDetails_Cust.setBorder(javax.swing.BorderFactory.createTitledBorder("Customer Details"));
        panSecurityDetails_Cust.setMinimumSize(new java.awt.Dimension(825, 80));
        panSecurityDetails_Cust.setPreferredSize(new java.awt.Dimension(825, 80));

        panSecurity.setMaximumSize(new java.awt.Dimension(350, 55));
        panSecurity.setMinimumSize(new java.awt.Dimension(350, 55));
        panSecurity.setPreferredSize(new java.awt.Dimension(350, 55));
        panSecurity.setLayout(new java.awt.GridBagLayout());

        lblCustID.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panSecurity.add(lblCustID, gridBagConstraints);

        lblCustName.setText("Customer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panSecurity.add(lblCustName, gridBagConstraints);

        lblMemberNo.setText("Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panSecurity.add(lblMemberNo, gridBagConstraints);

        txtMemberNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panSecurity.add(txtMemberNo, gridBagConstraints);

        txtCustId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panSecurity.add(txtCustId, gridBagConstraints);

        btnMemberNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMemberNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemberNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panSecurity.add(btnMemberNo, gridBagConstraints);

        btnCustId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        panSecurity.add(btnCustId, gridBagConstraints);

        lblCustName_Disp.setMaximumSize(new java.awt.Dimension(150, 21));
        lblCustName_Disp.setMinimumSize(new java.awt.Dimension(150, 21));
        lblCustName_Disp.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        panSecurity.add(lblCustName_Disp, gridBagConstraints);

        javax.swing.GroupLayout panSecurityDetails_CustLayout = new javax.swing.GroupLayout(panSecurityDetails_Cust);
        panSecurityDetails_Cust.setLayout(panSecurityDetails_CustLayout);
        panSecurityDetails_CustLayout.setHorizontalGroup(
            panSecurityDetails_CustLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSecurityDetails_CustLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(panSecurity, javax.swing.GroupLayout.PREFERRED_SIZE, 602, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panSecurityDetails_CustLayout.setVerticalGroup(
            panSecurityDetails_CustLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panSecurityDetails_CustLayout.createSequentialGroup()
                .addComponent(panSecurity, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                .addContainerGap())
        );

        cLabel4.setText("Gold Security Id");

        javax.swing.GroupLayout PanSecurityDetailsLayout = new javax.swing.GroupLayout(PanSecurityDetails);
        PanSecurityDetails.setLayout(PanSecurityDetailsLayout);
        PanSecurityDetailsLayout.setHorizontalGroup(
            PanSecurityDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanSecurityDetailsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(lblGoldSecurityId, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70))
            .addGroup(PanSecurityDetailsLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(panSecurityDetails_Cust, javax.swing.GroupLayout.PREFERRED_SIZE, 771, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(PanSecurityDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panSecurityDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 821, Short.MAX_VALUE)
                .addContainerGap())
        );
        PanSecurityDetailsLayout.setVerticalGroup(
            PanSecurityDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanSecurityDetailsLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(PanSecurityDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGoldSecurityId, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(panSecurityDetails_Cust, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panSecurityDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(PanSecurityDetails, java.awt.BorderLayout.CENTER);

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

        mnuProcess.setText("Process");
        mnuProcess.setMinimumSize(new java.awt.Dimension(73, 19));

        mitNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mitNew.setMnemonic('N');
        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        mitEdit.setMnemonic('E');
        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        mitDelete.setMnemonic('D');
        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptProcess);

        mitSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mitSave.setMnemonic('S');
        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setMnemonic('C');
        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitAuthorize.setText("Authorize");
        mitAuthorize.setEnabled(false);
        mitAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitAuthorizeActionPerformed(evt);
            }
        });
        mnuProcess.add(mitAuthorize);

        mitException.setText("Exception");
        mitException.setEnabled(false);
        mitException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitExceptionActionPerformed(evt);
            }
        });
        mnuProcess.add(mitException);

        mitReject.setText("Rejection");
        mitReject.setEnabled(false);
        mitReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitRejectActionPerformed(evt);
            }
        });
        mnuProcess.add(mitReject);
        mnuProcess.add(sptException);
        mnuProcess.add(sptPrint);

        mitPrint.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        mitPrint.setMnemonic('P');
        mitPrint.setText("Print");
        mitPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPrintActionPerformed(evt);
            }
        });
        mnuProcess.add(mitPrint);

        mitClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        mitClose.setMnemonic('l');
        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrSecInsur.add(mnuProcess);

        setJMenuBar(mbrSecInsur);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observableSecurity.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observableSecurity.setStatus();
        lblStatus.setText(observableSecurity.getLblStatus());
        popUp("Enquiry");
        btnCheck();
        
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void mitRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitRejectActionPerformed
        // TODO add your handling code here:
        btnRejectActionPerformed(evt);
    }//GEN-LAST:event_mitRejectActionPerformed
    
    private void mitExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitExceptionActionPerformed
        // TODO add your handling code here:
        btnExceptionActionPerformed(evt);
    }//GEN-LAST:event_mitExceptionActionPerformed
    
    private void mitAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAuthorizeActionPerformed
        // TODO add your handling code here:
        btnAuthorizeActionPerformed(evt);
    }//GEN-LAST:event_mitAuthorizeActionPerformed
        private void txtPremiumAmtFocusLost() {
        // TODO add your handling code here:
       
        
    }    private void txtPolicyAmtFocusLost() {
        // TODO add your handling code here:
        
        
    }
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        mitCloseActionPerformed();
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // TODO add your handling code here:
        mitPrintActionPerformed();
    }//GEN-LAST:event_mitPrintActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        mitCancelActionPerformed();
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        mitSaveActionPerformed();
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        mitDeleteActionPerformed();
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        mitEditActionPerformed();
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        mitNewActionPerformed();
    }//GEN-LAST:event_mitNewActionPerformed
    private void mitCloseActionPerformed() {
        btnCloseActionPerformed(null);
    }    private void mitPrintActionPerformed() {
        // TODO add your handling code here:
        //        btnPrintActionPerformed();
    }    private void mitCancelActionPerformed() {
        // TODO add your handling code here:
        btnCancelActionPerformed(null);
    }    private void mitSaveActionPerformed() {
        // TODO add your handling code here:
        btnSaveActionPerformed(null);
    }    private void mitDeleteActionPerformed() {
        // TODO add your handling code here:
        btnDeleteActionPerformed(null);
    }    private void mitEditActionPerformed() {
        // TODO add your handling code here:
        btnEditActionPerformed(null);
    }    private void mitNewActionPerformed() {
        // TODO add your handling code here:
        btnNewActionPerformed(null);
    }
    private void cboPurityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPurityActionPerformed
        // TODO add your handling code here:
        double perGramAmt = 0.0;
         if (cboPurity.getSelectedItem() !=null && !cboPurity.getSelectedItem().equals("")) {
            HashMap purityMap = new HashMap();
            String purity = CommonUtil.convertObjToStr(cboPurity.getSelectedItem());
            purityMap.put("PURITY", purity);
            purityMap.put("TODAY_DATE",ClientUtil.getCurrentDate());
            List lst = ClientUtil.executeQuery("getSelectTodaysMarketRate", purityMap);
            if (lst != null && lst.size() > 0) {
                purityMap = (HashMap) lst.get(0);
                perGramAmt = CommonUtil.convertObjToDouble(purityMap.get("PER_GRAM_RATE")).doubleValue();
                if (cboPurity.getSelectedItem() !=null && !cboPurity.getSelectedItem().equals("")) {
                   calcEligibleLoanAmount(perGramAmt);
                }
                //                setTotalSecValue(String.valueOf(totSecurityValue));
                //                setTotalMarginValue(String.valueOf(totMarginAmt));
                //                setTotalEligibleValue(String.valueOf(totEligibleAmt));
            }
        }
    }//GEN-LAST:event_cboPurityActionPerformed
   
    
    private void calcEligibleLoanAmount(double perGramAmt) {
        double totEligibleAmt = 0;
        txtMarketRate.setText(String.valueOf(perGramAmt));
        double totSecurityValue = perGramAmt * CommonUtil.convertObjToDouble(txtNetWeight.getText()).doubleValue();
        txtSecurityValue.setText(String.valueOf(totSecurityValue));             
    }

    
    
    
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
    private void cboSecurityCateActionPerformed() {
        if (!((observableSecurity.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE)))){
            if (chkForGoldSilverOrnaments()){                
                
                
            }else{
                
                observableSecurity.setTxtWeight("");                
                observableSecurity.setTxtGrossWeight("");
                
                
            }
        }else if(chkForGoldSilverOrnaments()){
            
            
        }else if(!chkForGoldSilverOrnaments()){
            
            observableSecurity.setTxtWeight("");            
            observableSecurity.setTxtGrossWeight("");
            
            
        }
    }
    private boolean chkForGoldSilverOrnaments(){
        boolean isWeightEnable = false;
        String strSecCateKey = CommonUtil.convertObjToStr(((ComboBoxModel)cboPurity.getModel()).getKeyForSelected());
        if (strSecCateKey.equals(GOLD_SILVER_ORNAMENTS)){
            isWeightEnable = true;
        }
        strSecCateKey = null;
        return isWeightEnable;
    }
    private void txtGrossWeightFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGrossWeightFocusLost
        // TODO add your handling code here:
       // txtSecurityValueFocusLost();
    }//GEN-LAST:event_txtGrossWeightFocusLost
    
    private void tdtAsonFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtAsonFocusLost
        // TODO add your handling code here:
        tdtAsonFocusLost();
    }//GEN-LAST:event_tdtAsonFocusLost
    private void tdtAsonFocusLost(){        
            Date stDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtAson.getDateValue()));
            if (DateUtil.dateDiff(stDt, currDt) > 0) {
                ClientUtil.showAlertWindow("Start date should be greater than or equal to current date");
                tdtAson.setDateValue(CommonUtil.convertObjToStr(currDt.clone()));
            }        
    }    
  
    
    private void btnSave_InsuranceActionPerformed() {
        
        String mandatoryMessage3 = "";
        
    
            updateOBFields();
            if (observableInsurance.addInsuranceDetails(rowInsurance, updateInsurance) == 1){
                // Donot reset the fields when return value is 1(Option is No for replacing the existing record)
            }else{
               
                observableInsurance.resetInsuranceDetails();
                
                updateInsurance = false;
            }
            observableSecurity.ttNotifyObservers();
        
    }    private void btnNew_InsuranceActionPerformed() {
        updateOBFields();
        observableInsurance.resetInsuranceDetails();
       
        
        rowInsurance = -1;
        updateInsurance = false;
        observableSecurity.ttNotifyObservers();
        setModified(true);
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        authEnableDisable();
        observableSecurity.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeActionPerformed(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        authEnableDisable();
        observableSecurity.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeActionPerformed(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        authEnableDisable();
        observableSecurity.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeActionPerformed(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void authEnableDisable(){
        setAllSecurityDetailsEnableDisable(false);
       
    }
    
    // Actions have to be taken when Authorize button is pressed
    private void authorizeActionPerformed(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            HashMap singleAuthorizeMap = new HashMap();
            java.util.ArrayList arrList = new java.util.ArrayList();
            HashMap authDataMap = new HashMap();
            
            authDataMap.put("CUSTOMER ID", txtCustId.getText());
            
            arrList.add(authDataMap);
            
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            
            authorize(singleAuthorizeMap);
            observableSecurity.setAuthorizeMap(null);
        } else {
            HashMap mapParam = new HashMap();
            
            HashMap authorizeMapCondition = new HashMap();
            authorizeMapCondition.put("STATUS_BY", TrueTransactMain.USER_ID);
            authorizeMapCondition.put("BRANCH_CODE", getSelectedBranchID());
            authorizeMapCondition.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, authorizeMapCondition);
            mapParam.put(CommonConstants.MAP_NAME, "getSelectCust_Gold_Security_AuthorizeTOList");
            viewType = AUTHORIZE;
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            //            btnSaveDisable();
            //            setAuthBtnEnableDisable();
            btnNew.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            btnCancel.setEnabled(true);
            
            authorizeMapCondition = null;
            //__ If there's no data to be Authorized, call Cancel action...
            //            if(!isModified()){
            //                setButtonEnableDisable();
            //                btnCancelActionPerformed(null);
            //            }
        }
    }
    
    private void btnSaveDisable(){
        btnSave.setEnabled(false);
        mitSave.setEnabled(false);
    }
    
    public void authorize(HashMap map) {
        observableSecurity.setAuthorizeMap(map);
        observableSecurity.doAction();
        if(observableSecurity.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
            super.setOpenForEditBy(observableSecurity.getStatusBy());
            super.removeEditLock(txtCustId.getText());
            isFilled = false;
            btnCancelActionPerformed(null);
            observableSecurity.setResultStatus();
        }
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        btncancelActionPerformed();
    }//GEN-LAST:event_btnCancelActionPerformed
    private void btncancelActionPerformed(){
        
        if(observableSecurity.getAuthorizeStatus1()!=null){
            super.removeEditLock(txtCustId.getText());
        }
        observableSecurity.resetForm();
        ClientUtil.enableDisable(this, false);// Disables the panel...
        observableSecurity.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        observableSecurity.setStatus();
        setButtonEnableDisable();        
        setAllSecurityBtnsEnableDisable(false);
        setModified(false);
        isFilled = false;
        viewType = "";
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnNew.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnCancel.setEnabled(false);
        btnSave.setEnabled(false);
        btnView.setEnabled(true);
        txtMemberNo.setText("");
        lblPhoto.setIcon(null);
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        btnsaveActionPerformed();
    }//GEN-LAST:event_btnSaveActionPerformed
    private void btnsaveActionPerformed(){
        //observable.setAuthorizeNo();
        if (checkFieldsWhenMainSavePressed()){
            updateOBFields();
            observableSecurity.doAction();
            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
            lst.add("CUSTOMER ID");
            lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lockMap.put("CUSTOMER ID",txtCustId.getText());
            setEditLockMap(lockMap);
            setEditLock();
            observableSecurity.resetForm();
            ClientUtil.enableDisable(this, false);
            observableSecurity.setResultStatus();
            observableSecurity.destroyObjects();
            observableSecurity.createObject();
            observableSecurity.ttNotifyObservers();
            setButtonEnableDisable();           
            setAllSecurityBtnsEnableDisable(false);
            if(observableSecurity.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
                setModified(false);
            }
            txtMemberNo.setText("");
            lblPhoto.setIcon(null);
        }
    }
    
    private boolean checkFieldsWhenMainSavePressed(){
        if (!((observableSecurity.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) || (viewType.equals(AUTHORIZE)))){
            StringBuffer mandatoryMessage = new StringBuffer("");
            if (!(tblSecurityTable.getRowCount() > 0)){
                mandatoryMessage.append(resourceBundle.getString("securityValueWarning"));
            }
            StringBuffer stbWarnMsg = new StringBuffer("");
            stbWarnMsg.append(observableSecurity.chkAllInsuranceHavingSecurity());
            stbWarnMsg.append(observableSecurity.chkSecValueGTInsuAmt());
            if (stbWarnMsg.length() > 0){
                mandatoryMessage.append(stbWarnMsg.toString());
            }
            stbWarnMsg = null;
            if (updateSecurity){
                mandatoryMessage.append("\n");
                mandatoryMessage.append(resourceBundle.getString("securityTableWarning"));
            }
            if (updateInsurance){
                mandatoryMessage.append("\n");
                mandatoryMessage.append(resourceBundle.getString("insuranceTableWarning"));
            }
            if (mandatoryMessage.length() > 0){
                displayAlert(mandatoryMessage.toString());
                return false;
            }
        }
        return true;
    }
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        btndeleteActionPerformed();
    }//GEN-LAST:event_btnDeleteActionPerformed
    private void btndeleteActionPerformed(){
        observableSecurity.setActionType(ClientConstants.ACTIONTYPE_DELETE);       
        setAllSecurityBtnsEnableDisable(false);
        popUp("Delete");
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
    }
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        btneditActionPerformed();
    }//GEN-LAST:event_btnEditActionPerformed
    private void btneditActionPerformed(){
        observableSecurity.createObject();
        observableSecurity.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp("Edit");
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        updateSecurity = false;
        updateInsurance = false;
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        btnnewActionPerformed();
        setAllSecurityDetailsEnableDisable(true);
        txtSecurityNo.setEnabled(false);
        cboAppraiserId.setEnabled(true);
    }//GEN-LAST:event_btnNewActionPerformed
    private void btnnewActionPerformed(){
        observableSecurity.resetForm();
        ClientUtil.enableDisable(this, false);// Enables the panel...
        observableSecurity.setActionType(ClientConstants.ACTIONTYPE_NEW);
//        popUp("New");
        viewType = "New";
//        HashMap sourceMap = new HashMap();
//        sourceMap.put("SECURITY","SECURITY");
//        new CheckCustomerIdUI(this,sourceMap);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        updateSecurity = false;
        updateInsurance = false;
        setModified(true);
    }    // Actions have to be taken when Security Details Delete button pressed
    private void btnsecurityDeleteActionPerformed(){
       
    }
    private void btnSecuritySaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSecuritySaveActionPerformed
        // TODO add your handling code here:
        btnsecuritySaveActionPerformed();
    }//GEN-LAST:event_btnSecuritySaveActionPerformed
    
    
    private StringBuffer validateSecurityFields(){
        StringBuffer mandatoryMessage = new StringBuffer();
        if(txtCustId.getText().length() <= 0){
           mandatoryMessage.append("Select Customer Id\n"); 
        }if(cboPurity.getSelectedIndex() == 0){
            mandatoryMessage.append("Purity Should not be empty\n");
        }if(txtGrossWeight.getText().trim().length() <= 0){
            mandatoryMessage.append("Grossweight Should not be empty\n");
        }if(txtNetWeight.getText().trim().length() <= 0){
            mandatoryMessage.append("Netweight Should not be empty\n");
        }if(txtMarketRate.getText().trim().length() <= 0){
            mandatoryMessage.append("Marketrate Should not be empty\n");
        }if(tdtAson.getDateValue().length() <= 0){
            mandatoryMessage.append("As on Date Should not be empty\n");
        }if(txtSecurityValue.getText().trim().length() <= 0 || CommonUtil.convertObjToDouble(txtSecurityValue.getText()) <= 0){
            mandatoryMessage.append("Security Value Should not be empty\n");
        }if(txtPledgeAmount.getText().trim().length() <= 0){
            mandatoryMessage.append("PledgeAmount Should not be empty\n");
        }if(txtAreaParticular.getText().trim().length() <= 0){
            mandatoryMessage.append("Gold Items Should not be empty");
        }        
        return mandatoryMessage;
    }
    
    // Actions have to be taken when Security Details Save button pressed
    private void btnsecuritySaveActionPerformed(){          
        StringBuffer mandatoryMessage = validateSecurityFields();
        Date stDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtAson.getDateValue()));
        if (mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage.toString());
        } else if (DateUtil.dateDiff(stDt, currDt) > 0) {
            ClientUtil.showAlertWindow("Start date should be greater than or equal to current date");
            tdtAson.setDateValue(CommonUtil.convertObjToStr(currDt.clone()));
        } else {
            updateOBFields();
            if (observableSecurity.addSecurityDetails(rowSecurity, updateSecurity) == 1) {
                // Donot reset the fields when return value is 1(Option is No for replacing the existing record)
            } else {
                // To reset the Fields
                setAllSecurityDetailsEnableDisable(false);
                setSecurityBtnsOnlyNewEnable();
                observableSecurity.resetSecurityDetails();
                updateSecurity = false;
            }
            observableSecurity.ttNotifyObservers();
        }
    }    // Actions have to be taken when Security Details New button is pressed
    private void btnsecurityNewActionPerformed(){
        updateOBFields();
        updateSecurity = false;
        observableSecurity.resetSecurityDetails();
        setAllSecurityDetailsEnableDisable(true);
        setSecurityBtnsOnlyNewSaveEnable();
        setDefaultValWhenSecurityNewBtnActionPerformed();
        rowSecurity = -1;
        observableSecurity.ttNotifyObservers();
        setModified(true);
    }
    private void setDefaultValWhenSecurityNewBtnActionPerformed(){
        observableSecurity.setDefaultValWhenSecurityNewBtnActionPerformed();
    }    // Actions have to be taken when a record from Insurance Details have been chosen
    
    private void tblSecurityTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSecurityTableMousePressed
        // TODO add your handling code here:
        tblsecurityTableMousePressed();
    }//GEN-LAST:event_tblSecurityTableMousePressed
    // Actions have to be taken when a record from Security Details have been chosen
    private void tblsecurityTableMousePressed(){
        updateOBFields();
        if (tblSecurityTable.getSelectedRow() >= 0){
            // If the table is in editable mode
            setAllSecurityDetailsEnableDisable(true);
            setAllSecurityBtnsEnableDisable(true);
            int insuranceTabRowToPopulate = observableSecurity.populateSecurityDetails(tblSecurityTable.getSelectedRow());
            
            if ((observableSecurity.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) ||viewType.equals("Enquiry") || (viewType.equals(AUTHORIZE))){
                // If the record is populated for Delete or Authorization
                setAllSecurityDetailsEnableDisable(false);
                setAllSecurityBtnsEnableDisable(false);
            }else{
                setAllSecurityDetailsEnableDisable(true);
                setAllSecurityBtnsEnableDisable(true);
                updateSecurity = true;
            }
            HashMap whereMap = new HashMap();
            HashMap keyMap = new HashMap();
            
            keyMap.put("CUST_ID", observableSecurity.getLblCustID_Disp());
            keyMap.put("GOLD_STOCK_ID",lblGoldSecurityId.getText());
            
            whereMap.put(CommonConstants.MAP_NAME, "getSelectLoanAvailedWithGoldStock");
            whereMap.put(CommonConstants.MAP_WHERE, keyMap);
            
            boolean haveData = ClientUtil.setTableModel(whereMap, tblLoanTable, false);
            
            if (!haveData){
                observableSecurity.resetSecurityLoanTab();
                tblLoanTable.setModel(observableSecurity.getTblSecurityLoanTab());
            }
            
            whereMap = null;
            keyMap = null;
            rowSecurity = tblSecurityTable.getSelectedRow();
        }
        observableSecurity.ttNotifyObservers();
    }
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

    private void btnSecurityAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSecurityAddActionPerformed
  
        String particulars = CommonUtil.convertObjToStr(txtAreaParticular.getText());        
        HashMap selItemList = new HashMap();
        if (particulars.length() != 0) {
            //    buffer.append("\n");
            String[] strarr = particulars.split("\n");
            String regex = "(\\d+)";
            Pattern p = Pattern.compile(regex);
            for (String test1 : strarr) {             
                 String[] strarr1 = test1.split(",");           
                for (String test : strarr1) {
                    Matcher m = p.matcher(test);
                    if (m.find()) {
                        String qtyy = m.group();
                        String key = test.substring(0, test.indexOf(qtyy)).trim();
                        key = key.toUpperCase();
                        int numberPosition = m.start();
                        int end = m.end();
                        String rem1 = test.substring(end, test.length()).trim();
                        char numberChar = test.charAt(numberPosition);
                        selItemList.put(key.replaceAll("-", ""), qtyy.replaceAll("-", "") + "#" + rem1.replaceAll("-", ""));
                    }
                }
            }

        }
        GoldLoanItemView surtyTab = new GoldLoanItemView("GOLDLOAN", observableSecurity.getGoldItemMap(),selItemList);
        surtyTab.show();
        if (surtyTab.getSelDataBuff() != null && surtyTab.getSelDataBuff().length() > 0) {
        txtAreaParticular.setText(CommonUtil.convertObjToStr(surtyTab.getSelDataBuff()));
        } 
    }//GEN-LAST:event_btnSecurityAddActionPerformed

    private void btnMemberNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMemberNoActionPerformed
        // TODO add your handling code here:      
      popUp("SHARE_ACT_NUM");
      viewType = "";
    }//GEN-LAST:event_btnMemberNoActionPerformed

    private void btnCustIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustIdActionPerformed
        // TODO add your handling code here:
       new CheckCustomerIdUI(this);
       setAllSecurityDetailsEnableDisable(true);
       txtSecurityNo.setEnabled(false);
       cboAppraiserId.setEnabled(true);
    }//GEN-LAST:event_btnCustIdActionPerformed

    private void txtNetWeightFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNetWeightFocusLost
        // TODO add your handling code here:
        double grossWeight = CommonUtil.convertObjToDouble(txtGrossWeight.getText()).doubleValue();
        double netWeight = CommonUtil.convertObjToDouble(txtNetWeight.getText()).doubleValue();
        if (grossWeight < netWeight) {
            ClientUtil.showAlertWindow("NetWeight should be less than grossweight");
            txtNetWeight.setText("");
            return;
        }
    }//GEN-LAST:event_txtNetWeightFocusLost

    private void txtPledgeAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPledgeAmountFocusLost
        // TODO add your handling code here:
        double securityValue = CommonUtil.convertObjToDouble(txtSecurityValue.getText()).doubleValue();
        double pledgeAmt = CommonUtil.convertObjToDouble(txtPledgeAmount.getText()).doubleValue();
        if (securityValue < pledgeAmt) {
            ClientUtil.showAlertWindow("Pledge amount should not be more than security value");
            txtPledgeAmount.setText("");
            return;
        }         
        double liabilityAmount = calculateLiabilityAmount();
        if(pledgeAmt < liabilityAmount){
            ClientUtil.showAlertWindow("Cannot pledge amount below current liability " + liabilityAmount +"/-"); 
            txtPledgeAmount.setText("");
            return;
        }
    }//GEN-LAST:event_txtPledgeAmountFocusLost

    
    private double calculateLiabilityAmount(){
        double liabilityAmount = 0.0;
        if(tblLoanTable.getRowCount() > 0){
            for(int i=0; i<tblLoanTable.getRowCount(); i++){                
                double liability = CommonUtil.convertObjToDouble(tblLoanTable.getValueAt(i, 5));
                liabilityAmount += liability;  
            }
        }
        return liabilityAmount;
    }
    
    
    private void btnLoadPhotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadPhotoActionPerformed
        // TODO add your handling code here:
          final JFileChooser objJFileChooser = new JFileChooser();
        objJFileChooser.setAccessory(new com.see.truetransact.clientutil.ImagePreview(objJFileChooser));
        final ImageFileFilter objImageFileFilter = new ImageFileFilter();
        final File selFile;
        byte[] byteArray;
        StringBuffer filePath;
        String fileName;
        
        objJFileChooser.setFileFilter(objImageFileFilter);
        objJFileChooser.removeChoosableFileFilter(objJFileChooser.getAcceptAllFileFilter());
        if (objJFileChooser.showOpenDialog(null) == objJFileChooser.APPROVE_OPTION){
            selFile = objJFileChooser.getSelectedFile();
            filePath = new StringBuffer(selFile.getAbsolutePath());
            try{
                lblPhoto.setIcon(new ImageIcon(CommonUtil.convertObjToStr(filePath)));
                fileName = filePath.substring(filePath.lastIndexOf("."));                
                final FileInputStream reader = new FileInputStream(selFile);
                final int size = reader.available();
                byteArray = new byte[size];
                reader.read(byteArray);
                reader.close();  
                observableSecurity.setPhotoFile(fileName);
                observableSecurity.setPhotoByteArray(byteArray);    
                btnPhotoRemove.setEnabled(true);
                byteArray = null;
            } catch (Exception e) {
                parseException.logException(e,true);
            }
        }
    }//GEN-LAST:event_btnLoadPhotoActionPerformed

    private void btnPhotoRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhotoRemoveActionPerformed
        // TODO add your handling code here:
        lblPhoto.setIcon(null);
        btnPhotoRemove.setEnabled(false);
        observableSecurity.setPhotoFile(null);
        observableSecurity.setPhotoByteArray(null);
    }//GEN-LAST:event_btnPhotoRemoveActionPerformed
    
    public void insertCustTableRecords(HashMap hash) {          
          if(hash.containsKey("MEMBER NO")){
            txtMemberNo.setText(hash.get("MEMBER NO").toString());     
          }   
          txtCustId.setText(hash.get("CUSTOMER ID").toString());
          lblCustName_Disp.setText(hash.get("FNAME").toString());
     }   
    
    private void popUp(String field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
        HashMap whereConditionMap = new HashMap();
        whereConditionMap.put("BRANCH_CODE", getSelectedBranchID());
        viewMap.put(CommonConstants.MAP_WHERE, whereConditionMap);
        if(field.equals("Edit") || field.equals("Delete")) {
            
            //            super.removeEditLock(lblCustID_Disp.getText());
            
            ArrayList lst = new ArrayList();
            lst.add("CUSTOMER ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            
            viewMap.put(CommonConstants.MAP_NAME, "popUpEditCustomerGoldSecurity");
        }else if(field.equals("New")) {
            viewMap.put(CommonConstants.MAP_NAME, "popUpNewSecurityInsurance");
        }
        else if(field.equals("Enquiry")) {
            viewMap.put(CommonConstants.MAP_NAME, "popUpEditCustomerGoldSecurity");
        }else if (field.equals("SHARE_ACT_NUM")) {            
            viewMap.put("MAPNAME", "viewAllShareAcct");
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);            
        }
        new ViewAll(this, viewMap).show();
    }
    
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        //System.out.println("hash ::" + hash);
        //System.out.println("viewType ::  "+ viewType);
        if (viewType != null) {
            if (viewType.equals("New") || viewType.equals("Edit") || viewType.equals("Delete") || viewType.equals(AUTHORIZE)|| viewType.equals("Enquiry")) {
                isFilled = true;
                if (viewType.equals(AUTHORIZE)){
                    hash.put(CommonConstants.MAP_WHERE, hash.get("CUSTOMER ID"));
                    observableSecurity.populateData(hash);
                }else if (viewType.equals("New")){
                    if(hash.containsKey("CUST_ID") ||hash.containsKey("CUSTOMER")){
                        hash.put("CUSTOMER ID",hash.get("CUST_ID"));
                        hash.put("CUSTOMER NAME",hash.get("NAME"));
                        hash.put("SHARE ACCOUNT NO",hash.get("MEMBER_NO"));
                        txtMemberNo.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
                        txtCustId.setText(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
                        lblCustName_Disp.setText(CommonUtil.convertObjToStr(hash.get("NAME")));                        
                    } 
                    observableSecurity.populateCustDetails(hash);
                }else{
                    hash.put(CommonConstants.MAP_WHERE, hash.get("CUSTOMER ID"));
                    observableSecurity.populateData(hash);
                }
                //                observableSecurity.ttNotifyObservers();
                if ((observableSecurity.getActionType() == ClientConstants.ACTIONTYPE_DELETE) || viewType.equals("Enquiry")|| (viewType.equals(AUTHORIZE))){
                    ClientUtil.enableDisable(this, false);
                    if(viewType==AUTHORIZE) {
                        btnAuthorize.setEnabled(observableSecurity.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                        btnReject.setEnabled(observableSecurity.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                        btnException.setEnabled(observableSecurity.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    }
                }else{
                    setSecurityBtnsOnlyNewEnable();                  
                    ClientUtil.enableDisable(this, true);                    
                    setAllSecurityDetailsEnableDisable(false);
                }
                
                observableSecurity.setStatus();
                //                setButtonEnableDisable();
                observableSecurity.ttNotifyObservers();
            }
             if (viewType.equals("SHARE_ACT_NUM")){
                    if(hash.containsKey("CUST_ID") ||hash.containsKey("CUSTOMER")){
                        hash.put("CUSTOMER ID",hash.get("CUST_ID"));
                        hash.put("CUSTOMER NAME",hash.get("CUSTOMER"));
                        hash.put("SHARE ACCOUNT NO",hash.get("SHARE ACCOUNT NO"));
                    } if(hash.containsKey("SHARE ACCOUNT NO")){
                       txtMemberNo.setText(CommonUtil.convertObjToStr(hash.get("SHARE ACCOUNT NO")));
                       txtCustId.setText(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
                       observableSecurity.setTxtCustId(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
                    }
                    observableSecurity.populateCustDetails(hash);
                    observableSecurity.setStatus();
                //                setButtonEnableDisable();
                observableSecurity.ttNotifyObservers();
                }
             if(viewType.equals("Edit")){
                 txtCustId.setEnabled(false);
                 txtMemberNo.setEnabled(false);
                 btnCustId.setEnabled(false);
                 btnMemberNo.setEnabled(false);
             }
        }
        setModified(true);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        javax.swing.JFrame frm = new javax.swing.JFrame();
        CustomerGoldSecurityUI ui = new CustomerGoldSecurityUI();
        frm.getContentPane().add(ui);
        ui.show();
        frm.setSize(600, 500);
        frm.show();
    }
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    private void setAllSecurityDetailsEnableDisable(boolean val){
        ClientUtil.enableDisable(panSecurityDetails_security, val);
       
    }
    private void setSecurityBtnsOnlyNewEnable(){       
        btnSecuritySave.setEnabled(false);
    }
    private void setSecurityBtnsOnlyNewSaveEnable(){       
        btnSecuritySave.setEnabled(true);
    }
    private void setAllSecurityBtnsEnableDisable(boolean val){       
        btnSecuritySave.setEnabled(val);
    }
    
   
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CPanel PanSecurityDetails;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustId;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnLoadPhoto;
    private com.see.truetransact.uicomponent.CButton btnMemberNo;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPhotoRemove;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSecurityAdd;
    private com.see.truetransact.uicomponent.CButton btnSecuritySave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CLabel cLabel3;
    private com.see.truetransact.uicomponent.CLabel cLabel4;
    private com.see.truetransact.uicomponent.CComboBox cboAppraiserId;
    private com.see.truetransact.uicomponent.CComboBox cboPurity;
    private com.see.truetransact.uicomponent.CLabel lblAppraiserId;
    private com.see.truetransact.uicomponent.CLabel lblAson;
    private com.see.truetransact.uicomponent.CLabel lblCustID;
    private com.see.truetransact.uicomponent.CLabel lblCustName;
    private com.see.truetransact.uicomponent.CLabel lblCustName_Disp;
    private com.see.truetransact.uicomponent.CLabel lblGoldSecurityId;
    private com.see.truetransact.uicomponent.CLabel lblGrossWeight;
    private com.see.truetransact.uicomponent.CLabel lblMarketRate;
    private com.see.truetransact.uicomponent.CLabel lblMemberNo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNetWeight;
    private com.see.truetransact.uicomponent.CLabel lblParticulars;
    private com.see.truetransact.uicomponent.CLabel lblPhoto;
    private com.see.truetransact.uicomponent.CLabel lblPurity;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblspace3;
    private com.see.truetransact.uicomponent.CLabel lblspace4;
    private com.see.truetransact.uicomponent.CMenuBar mbrSecInsur;
    private javax.swing.JMenuItem mitAuthorize;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitException;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitReject;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panLoanAvailed;
    private com.see.truetransact.uicomponent.CPanel panLoanTable;
    private com.see.truetransact.uicomponent.CPanel panSecDetails;
    private com.see.truetransact.uicomponent.CPanel panSecNature;
    private com.see.truetransact.uicomponent.CPanel panSecurity;
    private com.see.truetransact.uicomponent.CPanel panSecurityDetails;
    private com.see.truetransact.uicomponent.CPanel panSecurityDetails_Cust;
    private com.see.truetransact.uicomponent.CPanel panSecurityDetails_security;
    private com.see.truetransact.uicomponent.CPanel panSecurityTable;
    private com.see.truetransact.uicomponent.CPanel panSecurityTableMain;
    private com.see.truetransact.uicomponent.CPanel panSecurityTools;
    private com.see.truetransact.uicomponent.CPanel panSecurityType;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTotalSecurity_Value;
    private com.see.truetransact.uicomponent.CPanel panparticulars;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSecurityType;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptException;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CSeparator sptSecurityDetails_Vert;
    private com.see.truetransact.uicomponent.CScrollPane srpLoanTable;
    private com.see.truetransact.uicomponent.CScrollPane srpPhoto;
    private com.see.truetransact.uicomponent.CScrollPane srpSecurityTable;
    private com.see.truetransact.uicomponent.CScrollPane srpTxtAreaParticulars;
    private com.see.truetransact.uicomponent.CTabbedPane tabSecurityDetails;
    private com.see.truetransact.uicomponent.CTable tblLoanTable;
    private com.see.truetransact.uicomponent.CTable tblSecurityTable;
    private javax.swing.JToolBar tbrSecInsur;
    private com.see.truetransact.uicomponent.CDateField tdtAson;
    private com.see.truetransact.uicomponent.CTextArea txtAreaParticular;
    private com.see.truetransact.uicomponent.CTextField txtCustId;
    private com.see.truetransact.uicomponent.CTextField txtGrossWeight;
    private com.see.truetransact.uicomponent.CTextField txtMarketRate;
    private com.see.truetransact.uicomponent.CTextField txtMemberNo;
    private com.see.truetransact.uicomponent.CTextField txtNetWeight;
    private com.see.truetransact.uicomponent.CTextField txtPledgeAmount;
    private com.see.truetransact.uicomponent.CTextField txtSecurityNo;
    private com.see.truetransact.uicomponent.CTextField txtSecurityValue;
    // End of variables declaration//GEN-END:variables
    
}
