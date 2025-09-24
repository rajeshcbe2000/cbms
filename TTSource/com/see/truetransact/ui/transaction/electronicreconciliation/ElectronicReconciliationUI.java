/*
 * ElectronicReconciliationUI.java
 *
 * Created on Sep 30th, 2019, 11:03 PM
 */
package com.see.truetransact.ui.transaction.electronicreconciliation;

/**
 *
 * @author Sathiya
 *
 */
import javax.swing.*;
import java.util.Date;
import java.util.List;
import java.awt.Color;
import java.util.HashMap;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.table.*;
import java.util.Observable;
import java.util.LinkedHashMap;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
//import com.see.truetransact.ui.transaction.rejectreprocessrequest.RejectReProcessRequestOB;
import com.see.truetransact.uicomponent.COptionPane;

public class ElectronicReconciliationUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    /**
     * Variable Declarations
     */
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.transaction.electronicreconciliation.ElectronicReconciliationRB", ProxyParameters.LANGUAGE);
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    String key = "";
    boolean isFilled = false;
    private String viewType = "";
    private Date currDate = null;
    private HashMap mandatoryMap;
    ElectronicReconciliationOB observable = null;
    java.io.File selectedFile = null;
    private Date maturityDate = null;
    HashMap whereMap = new HashMap();
    ArrayList colorList = new ArrayList();
    java.util.ResourceBundle objMandatoryRB;
    private ArrayList totFileDataList = null;
    HashMap depositDetailsMap = new HashMap();

    /**
     * Creates new form TokenConfigUI
     */
    public ElectronicReconciliationUI() {
        currDate = ClientUtil.getCurrentDate();
        initForm();
    }

    /**
     * Method which is used to initialize the form TokenConfig
     */
    private void initForm() {
        initComponents();
        internationalize();
        setObservable();
//        observable = new ElectronicReconciliationOB();
        initComponentData();
        initTableData();
        setMaxLengths();
        ClientUtil.enableDisable(panLoadFromFile, false);
        setButtonEnableDisable();
        objMandatoryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.transaction.electronicreconciliation.ElectronicReconciliationMRB", ProxyParameters.LANGUAGE);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panManualReconciliation);
        ClientUtil.enableDisable(this, false);
        btnBrowse.setEnabled(false);
//        btnDelete.setVisible(false);
        setSizeTableData();
        tdtMatchStatusFromDt.setEnabled(true);
        cboMatchStatusType.setEnabled(true);
        tdtFromDateManualRecon.setEnabled(true);
        tdtToDateManualRecon.setEnabled(true);
        cboUnMatchStatusType.setEnabled(true);
        cboReconTypeMatch.setEnabled(true);
        cboReconTypeManual.setEnabled(true);
        tdtMatchStatusToDt.setEnabled(true);
        cboReconSourceType.setEnabled(true);
        tdtReconFromDt.setEnabled(true);
        tdtReconRejectFromDt.setEnabled(true);
        cboSourceRejectType.setEnabled(true);
        tdtReconRejectToDt.setEnabled(true);
        cboReconRejectType.setEnabled(true);
        tdtReconToDt.setEnabled(true);
        cboReconClassifiedCategory.setEnabled(true);
        tdtReconFromDt.setDateValue(CommonUtil.convertObjToStr(DateUtil.addDays(currDate, -2)));
        tdtReconToDt.setDateValue(CommonUtil.convertObjToStr(currDate));
        tdtReconRejectFromDt.setDateValue(CommonUtil.convertObjToStr(DateUtil.addDays(currDate, -2)));
        tdtReconRejectToDt.setDateValue(CommonUtil.convertObjToStr(currDate));
        tdtMatchStatusFromDt.setDateValue(CommonUtil.convertObjToStr(DateUtil.addDays(currDate, -2)));
        tdtMatchStatusToDt.setDateValue(CommonUtil.convertObjToStr(currDate));
        tdtFromDateManualRecon.setDateValue(CommonUtil.convertObjToStr(DateUtil.addDays(currDate, -2)));
        tdtToDateManualRecon.setDateValue(CommonUtil.convertObjToStr(currDate));
        lblReconTypeManual.setVisible(false);
        cboUnMatchStatusType.setVisible(false);
        lblRejectAmt.setEnabled(true);
        txtCustName.setEnabled(true);
        txtActNum.setEnabled(true);
        cboSourceRejectType.setSelectedItem("Processing Bank Entries");
        cboReconRejectType.setSelectedItem("NEFT");
        cboReconSourceType.setSelectedItem("Processing Bank Entries");
        cboReconClassifiedCategory.setSelectedItem("NEFT");
        cboMatchStatusType.setSelectedItem("Processing Bank Entries");
        cboReconTypeMatch.setSelectedItem("NEFT");
        txtPBTActNum.setEnabled(true);
        txtPPTAmt.setEnabled(true);
        txtPPTCustName.setEnabled(true);
        txtMatchAmt.setEnabled(true);
        txtMatchCustName.setEnabled(true);
        txtMatchActNum.setEnabled(true);
        cboReconTransType.setEnabled(true);
        cboMatchTransType.setEnabled(true);
        btnBrowse.setEnabled(true);
        cboFileUploadType.setEnabled(true);
    }

    private void initComponentData() {
        tblOursTransactionList.setModel(observable.getElectronicManualMatchOursTable());
        tblTheirsTransactionList.setModel(observable.getElectronicManualMatchTheirsTable());
        cboMatchStatusType.setModel(observable.getCbmMatchStatusType());
        cboUnMatchStatusType.setModel(observable.getCbmUnMatchStatusType());
        cboReconTypeManual.setModel(observable.getCbmReconTypeManual());
        cboReconTypeMatch.setModel(observable.getCbmReconTypeMatch());
        cboSourceRejectType.setModel(observable.getCbmSourceRejectType());
        cboReconRejectType.setModel(observable.getCbmReconRejectType());
        cboReconSourceType.setModel(observable.getCbmReconSourceType());
        cboReconClassifiedCategory.setModel(observable.getCbmReconClassifiedCategory());
        cboMatchTransType.setModel(observable.getCbmMatchTransType());
        cboReconTransType.setModel(observable.getCbmReconTransType());
        cboFileUploadType.setModel(observable.getCbmFileUploadType());
    }

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("txtDebitGL", new Boolean(true));
        mandatoryMap.put("txtTotalAmount", new Boolean(true));
        mandatoryMap.put("tdtEffectiveDt", new Boolean(true));
        mandatoryMap.put("txtBeneficiaryName", new Boolean(true));
        mandatoryMap.put("txtNarration", new Boolean(true));
    }

    private void setObservable() {
        try {
            observable = new ElectronicReconciliationOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMaxLengths() {
        lblRejectAmt.setAllowAll(true);
        txtCustName.setAllowAll(true);
        txtActNum.setAllowAll(true);
        txtPBTActNum.setAllowAll(true);
        txtPPTCustName.setAllowAll(true);
        txtPPTAmt.setAllowAll(true);
        txtMatchActNum.setAllowAll(true);
        txtMatchCustName.setAllowAll(true);
        txtMatchAmt.setAllowAll(true);
    }

    private void internationalize() {
        resourceBundle = new ElectronicReconciliationRB();
        btnBrowse.setText(resourceBundle.getString("btnBrowse"));
        lblTotalTransactionAmt.setText(resourceBundle.getString("lblTotalTransactionAmt"));
    }

    private void initTableData() {
        tblFileDataDetails.setModel(observable.getTblFileDataDetails());
    }

    private void setButtonEnableDisable() {
//        btnNew.setEnabled(!btnNew.isEnabled());
//        btnEdit.setEnabled(!btnEdit.isEnabled());
//        btnDelete.setEnabled(!btnDelete.isEnabled());
//
//        btnSave.setEnabled(!btnNew.isEnabled());
//        btnCancel.setEnabled(!btnNew.isEnabled());
//
//        btnAuthorize.setEnabled(btnNew.isEnabled());
//        btnReject.setEnabled(btnNew.isEnabled());
//        btnException.setEnabled(btnNew.isEnabled());
//        btnView.setEnabled(!btnView.isEnabled());
    }

    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
    }

    public void updateOBFields() {
        observable.setScreen(getScreen());
        observable.setModule(getModule());
    }

    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
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

    /**
     * To display a popUp window for viewing existing data
     */
    private void callView(String currAction) {
        viewType = currAction;
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        if (currAction.equalsIgnoreCase("DEBIT_GL")) {
            viewMap.put(CommonConstants.MAP_NAME, "product.deposits.getAcctHeadList");
        } else if (currAction.equalsIgnoreCase("Enquiry")) {
            viewMap.put(CommonConstants.MAP_NAME, "getDepositBulkEnquiry");
        } else if (currAction.equalsIgnoreCase("Edit")) {
            viewMap.put(CommonConstants.MAP_NAME, "getDepositBulkEdit");
        }
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        new ViewAll(this, viewMap).show();
    }

    /*
     * Fills up the HashMap with data when user selects the row in ViewAll
     * screen
     */
    public void fillData(Object obj) {
        try {
            final HashMap hashMap = (HashMap) obj;
            isFilled = true;
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                observable.resetTableValues();
                this.setButtonEnableDisable();
                CommonUtil comm = new CommonUtil();
                final JDialog loading = comm.addProgressBar();
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                    @Override
                    protected Void doInBackground() throws InterruptedException {
                        try {
                            observable.getData(hashMap);
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
                totFileDataList = new ArrayList();
                totFileDataList = observable.getEditFileDataList();
                tableDataAlignmentEdit(totFileDataList);
                setSizeTableDataEdit();
                //setColorList();
                //setColour();
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
//                    btnSave.setEnabled(false);
                }
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                observable.resetTableValues();
                CommonUtil comm = new CommonUtil();
                final JDialog loading = comm.addProgressBar();
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                    @Override
                    protected Void doInBackground() throws InterruptedException {
                        try {
                            observable.getData(hashMap);
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
                totFileDataList = new ArrayList();
                totFileDataList = observable.getEditFileDataList();
                tableDataAlignmentEdit(totFileDataList);
                setSizeTableDataEdit();
                //setColorList();
                //setColour();
            }
//            btnCancel.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tabReconciliationDetails = new com.see.truetransact.uicomponent.CTabbedPane();
        panReconciliationUploadDetails = new com.see.truetransact.uicomponent.CPanel();
        panFileTableData = new com.see.truetransact.uicomponent.CPanel();
        srpFileDataDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblFileDataDetails = new com.see.truetransact.uicomponent.CTable();
        panProcess = new com.see.truetransact.uicomponent.CPanel();
        lblTotalTransactionAmt = new com.see.truetransact.uicomponent.CLabel();
        lblTotalTransactionAmtVal = new com.see.truetransact.uicomponent.CLabel();
        lblTotalRecordVal = new com.see.truetransact.uicomponent.CLabel();
        lblTotalRecord = new com.see.truetransact.uicomponent.CLabel();
        panLoadFromFile = new com.see.truetransact.uicomponent.CPanel();
        txtFileName = new com.see.truetransact.uicomponent.CTextField();
        lblFileName = new com.see.truetransact.uicomponent.CLabel();
        btnBrowse = new com.see.truetransact.uicomponent.CButton();
        lblReconTypeManual1 = new com.see.truetransact.uicomponent.CLabel();
        cboFileUploadType = new com.see.truetransact.uicomponent.CComboBox();
        btnFileUploadInsert = new com.see.truetransact.uicomponent.CButton();
        btnFileUploadClear = new com.see.truetransact.uicomponent.CButton();
        panReconInquiry = new com.see.truetransact.uicomponent.CPanel();
        panReconDetails = new com.see.truetransact.uicomponent.CPanel();
        cboReconSourceType = new com.see.truetransact.uicomponent.CComboBox();
        lblReconFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtReconFromDt = new com.see.truetransact.uicomponent.CDateField();
        lblReconSourceType = new com.see.truetransact.uicomponent.CLabel();
        tdtReconToDt = new com.see.truetransact.uicomponent.CDateField();
        lblReconToDate = new com.see.truetransact.uicomponent.CLabel();
        cboReconClassifiedCategory = new com.see.truetransact.uicomponent.CComboBox();
        lblReconType = new com.see.truetransact.uicomponent.CLabel();
        lblActNum2 = new com.see.truetransact.uicomponent.CLabel();
        txtPBTActNum = new com.see.truetransact.uicomponent.CTextField();
        lblCustName2 = new com.see.truetransact.uicomponent.CLabel();
        txtPPTCustName = new com.see.truetransact.uicomponent.CTextField();
        lblAmt2 = new com.see.truetransact.uicomponent.CLabel();
        txtPPTAmt = new com.see.truetransact.uicomponent.CTextField();
        cboReconTransType = new com.see.truetransact.uicomponent.CComboBox();
        lblReconTransType = new com.see.truetransact.uicomponent.CLabel();
        panReconInquiryLst = new com.see.truetransact.uicomponent.CPanel();
        srpTableReconInquiryLst = new com.see.truetransact.uicomponent.CScrollPane();
        tblReconInquirytLst = new com.see.truetransact.uicomponent.CTable();
        panAccountNo7 = new com.see.truetransact.uicomponent.CPanel();
        lblBankTxnCountValue = new com.see.truetransact.uicomponent.CLabel();
        lblBankTxnAmountValue = new com.see.truetransact.uicomponent.CLabel();
        lblBankTxnCount = new com.see.truetransact.uicomponent.CLabel();
        lblBankTxnAmount = new com.see.truetransact.uicomponent.CLabel();
        panReconDetails1 = new com.see.truetransact.uicomponent.CPanel();
        btnReconClear = new com.see.truetransact.uicomponent.CButton();
        btnReconInquiryProcess = new com.see.truetransact.uicomponent.CButton();
        panReconRejectInquiry = new com.see.truetransact.uicomponent.CPanel();
        panReconRejectDetails = new com.see.truetransact.uicomponent.CPanel();
        cboSourceRejectType = new com.see.truetransact.uicomponent.CComboBox();
        lblReconRejectFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtReconRejectFromDt = new com.see.truetransact.uicomponent.CDateField();
        lblReconRejectReconType = new com.see.truetransact.uicomponent.CLabel();
        lblReconRejectToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtReconRejectToDt = new com.see.truetransact.uicomponent.CDateField();
        lblReconRejectReconType1 = new com.see.truetransact.uicomponent.CLabel();
        cboReconRejectType = new com.see.truetransact.uicomponent.CComboBox();
        lblActNum = new com.see.truetransact.uicomponent.CLabel();
        txtActNum = new com.see.truetransact.uicomponent.CTextField();
        lblAmt = new com.see.truetransact.uicomponent.CLabel();
        lblRejectAmt = new com.see.truetransact.uicomponent.CTextField();
        lblCustName = new com.see.truetransact.uicomponent.CLabel();
        txtCustName = new com.see.truetransact.uicomponent.CTextField();
        panReconRejectInquiryLst = new com.see.truetransact.uicomponent.CPanel();
        srpTableReconRejectInquiryLst = new com.see.truetransact.uicomponent.CScrollPane();
        tblReconRejectInquirytLst = new com.see.truetransact.uicomponent.CTable();
        panAccountNo8 = new com.see.truetransact.uicomponent.CPanel();
        lblRejectCountValue = new com.see.truetransact.uicomponent.CLabel();
        lblRejectAmountValue = new com.see.truetransact.uicomponent.CLabel();
        lblRejectCount = new com.see.truetransact.uicomponent.CLabel();
        lblRejectAmount = new com.see.truetransact.uicomponent.CLabel();
        panReconRejectDetails1 = new com.see.truetransact.uicomponent.CPanel();
        btnReconRejectClear = new com.see.truetransact.uicomponent.CButton();
        btnReconRejectInquiryProcess = new com.see.truetransact.uicomponent.CButton();
        panMatchStatusInquiry = new com.see.truetransact.uicomponent.CPanel();
        panMatchStatusDetails = new com.see.truetransact.uicomponent.CPanel();
        cboMatchStatusType = new com.see.truetransact.uicomponent.CComboBox();
        lblMatchStatusFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtMatchStatusFromDt = new com.see.truetransact.uicomponent.CDateField();
        lblMatchStatusReconType = new com.see.truetransact.uicomponent.CLabel();
        lblMatchStatusReconType1 = new com.see.truetransact.uicomponent.CLabel();
        cboReconTypeMatch = new com.see.truetransact.uicomponent.CComboBox();
        lblMatchStatusToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtMatchStatusToDt = new com.see.truetransact.uicomponent.CDateField();
        lblActNum1 = new com.see.truetransact.uicomponent.CLabel();
        txtMatchActNum = new com.see.truetransact.uicomponent.CTextField();
        lblCustName1 = new com.see.truetransact.uicomponent.CLabel();
        txtMatchCustName = new com.see.truetransact.uicomponent.CTextField();
        lblAmt1 = new com.see.truetransact.uicomponent.CLabel();
        txtMatchAmt = new com.see.truetransact.uicomponent.CTextField();
        lblMatchTransType = new com.see.truetransact.uicomponent.CLabel();
        cboMatchTransType = new com.see.truetransact.uicomponent.CComboBox();
        panMatchStatusInquiryLst = new com.see.truetransact.uicomponent.CPanel();
        srpTableMatchStatusInquiryLst = new com.see.truetransact.uicomponent.CScrollPane();
        tblMatchStatusInquirytLst = new com.see.truetransact.uicomponent.CTable();
        panAccountNo6 = new com.see.truetransact.uicomponent.CPanel();
        lblMatchCountValue = new com.see.truetransact.uicomponent.CLabel();
        lblMatchAmountValue = new com.see.truetransact.uicomponent.CLabel();
        lblMatchCount = new com.see.truetransact.uicomponent.CLabel();
        lblMatchAmount = new com.see.truetransact.uicomponent.CLabel();
        panMatchStatusDetails1 = new com.see.truetransact.uicomponent.CPanel();
        btnMatchStatusClear = new com.see.truetransact.uicomponent.CButton();
        btnMatchStatusInquiryProcess = new com.see.truetransact.uicomponent.CButton();
        panManualReconciliation = new com.see.truetransact.uicomponent.CPanel();
        panTheirsTransactionTable = new com.see.truetransact.uicomponent.CPanel();
        panOurTransactionList = new com.see.truetransact.uicomponent.CPanel();
        srpOurTransactionList = new com.see.truetransact.uicomponent.CScrollPane();
        tblTheirsTransactionList = new com.see.truetransact.uicomponent.CTable();
        panAccountNo4 = new com.see.truetransact.uicomponent.CPanel();
        lblUnMatchTheirsCountValue = new com.see.truetransact.uicomponent.CLabel();
        lblUnMatchTheirsAmountValue = new com.see.truetransact.uicomponent.CLabel();
        lblUnMatchTheirsCount = new com.see.truetransact.uicomponent.CLabel();
        lblUnMatchTheirsAmount = new com.see.truetransact.uicomponent.CLabel();
        panOursTransactionTable = new com.see.truetransact.uicomponent.CPanel();
        panStatementTxnList = new com.see.truetransact.uicomponent.CPanel();
        srpStatementTxnList = new com.see.truetransact.uicomponent.CScrollPane();
        tblOursTransactionList = new com.see.truetransact.uicomponent.CTable();
        panAccountNo5 = new com.see.truetransact.uicomponent.CPanel();
        lblUnMatchOursCountValue = new com.see.truetransact.uicomponent.CLabel();
        lblUnMatchOursAmountValue = new com.see.truetransact.uicomponent.CLabel();
        lblUnMatchOursCount = new com.see.truetransact.uicomponent.CLabel();
        lblUnMatchOursAmount = new com.see.truetransact.uicomponent.CLabel();
        panReconSelectionDetails = new com.see.truetransact.uicomponent.CPanel();
        tdtFromDateManualRecon = new com.see.truetransact.uicomponent.CDateField();
        lblFromDateManualRecon = new com.see.truetransact.uicomponent.CLabel();
        lblReconTypeManual = new com.see.truetransact.uicomponent.CLabel();
        cboUnMatchStatusType = new com.see.truetransact.uicomponent.CComboBox();
        lblToDateAutoRecon = new com.see.truetransact.uicomponent.CLabel();
        tdtToDateManualRecon = new com.see.truetransact.uicomponent.CDateField();
        cboReconTypeManual = new com.see.truetransact.uicomponent.CComboBox();
        lblReconTypeManual2 = new com.see.truetransact.uicomponent.CLabel();
        btnDisplay = new com.see.truetransact.uicomponent.CButton();
        btnManualMatchClear = new com.see.truetransact.uicomponent.CButton();
        panFromBranchDetails4 = new com.see.truetransact.uicomponent.CPanel();
        panAutoCount = new com.see.truetransact.uicomponent.CPanel();
        lblAutoReconAmount = new com.see.truetransact.uicomponent.CLabel();
        lblAutoAmount = new com.see.truetransact.uicomponent.CLabel();
        lblAutoReconCount = new com.see.truetransact.uicomponent.CLabel();
        lblAutoCount = new com.see.truetransact.uicomponent.CLabel();
        lblAutoReconCount1 = new com.see.truetransact.uicomponent.CLabel();
        lblAutoCount1 = new com.see.truetransact.uicomponent.CLabel();
        lblAutoReconAmount1 = new com.see.truetransact.uicomponent.CLabel();
        lblAutoAmount1 = new com.see.truetransact.uicomponent.CLabel();
        btnReconciliation = new com.see.truetransact.uicomponent.CButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(990, 600));
        setMinimumSize(new java.awt.Dimension(990, 600));
        setPreferredSize(new java.awt.Dimension(990, 600));

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

        tabReconciliationDetails.setMinimumSize(new java.awt.Dimension(830, 690));
        tabReconciliationDetails.setName(""); // NOI18N
        tabReconciliationDetails.setPreferredSize(new java.awt.Dimension(830, 690));

        panReconciliationUploadDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panReconciliationUploadDetails.setMaximumSize(new java.awt.Dimension(800, 550));
        panReconciliationUploadDetails.setMinimumSize(new java.awt.Dimension(800, 550));
        panReconciliationUploadDetails.setPreferredSize(new java.awt.Dimension(800, 550));
        panReconciliationUploadDetails.setLayout(new java.awt.GridBagLayout());

        panFileTableData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panFileTableData.setMinimumSize(new java.awt.Dimension(950, 400));
        panFileTableData.setPreferredSize(new java.awt.Dimension(950, 400));
        panFileTableData.setLayout(new java.awt.GridBagLayout());

        srpFileDataDetails.setMinimumSize(new java.awt.Dimension(950, 400));
        srpFileDataDetails.setPreferredSize(new java.awt.Dimension(950, 400));
        srpFileDataDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                srpFileDataDetailsMouseClicked(evt);
            }
        });

        tblFileDataDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Trans Dt", "Branch", "Description", "Reference No", "Value Dt", "Debits", "Credits", "Balance"
            }
        ));
        tblFileDataDetails.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblFileDataDetails.setMaximumSize(new java.awt.Dimension(2147483647, 64));
        tblFileDataDetails.setMinimumSize(new java.awt.Dimension(975, 0));
        tblFileDataDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(806, 331));
        tblFileDataDetails.setSelectionBackground(new java.awt.Color(153, 255, 153));
        tblFileDataDetails.setSelectionForeground(new java.awt.Color(204, 0, 0));
        tblFileDataDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFileDataDetailsMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblFileDataDetailsMouseReleased(evt);
            }
        });
        tblFileDataDetails.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblFileDataDetailsFocusLost(evt);
            }
        });
        srpFileDataDetails.setViewportView(tblFileDataDetails);

        panFileTableData.add(srpFileDataDetails, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconciliationUploadDetails.add(panFileTableData, gridBagConstraints);

        panProcess.setMinimumSize(new java.awt.Dimension(800, 25));
        panProcess.setPreferredSize(new java.awt.Dimension(800, 22));
        panProcess.setLayout(new java.awt.GridBagLayout());

        lblTotalTransactionAmt.setForeground(new java.awt.Color(0, 153, 0));
        lblTotalTransactionAmt.setText("Total Amout Rs.");
        lblTotalTransactionAmt.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 30, 4, 4);
        panProcess.add(lblTotalTransactionAmt, gridBagConstraints);

        lblTotalTransactionAmtVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblTotalTransactionAmtVal.setForeground(new java.awt.Color(0, 153, 0));
        lblTotalTransactionAmtVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotalTransactionAmtVal.setMinimumSize(new java.awt.Dimension(140, 21));
        lblTotalTransactionAmtVal.setPreferredSize(new java.awt.Dimension(140, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProcess.add(lblTotalTransactionAmtVal, gridBagConstraints);

        lblTotalRecordVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblTotalRecordVal.setForeground(new java.awt.Color(0, 153, 0));
        lblTotalRecordVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotalRecordVal.setMinimumSize(new java.awt.Dimension(140, 21));
        lblTotalRecordVal.setPreferredSize(new java.awt.Dimension(140, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProcess.add(lblTotalRecordVal, gridBagConstraints);

        lblTotalRecord.setForeground(new java.awt.Color(0, 153, 0));
        lblTotalRecord.setText("Total Record :");
        lblTotalRecord.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 4);
        panProcess.add(lblTotalRecord, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panReconciliationUploadDetails.add(panProcess, gridBagConstraints);

        panLoadFromFile.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panLoadFromFile.setMinimumSize(new java.awt.Dimension(900, 30));
        panLoadFromFile.setPreferredSize(new java.awt.Dimension(900, 30));
        panLoadFromFile.setLayout(new java.awt.GridBagLayout());

        txtFileName.setMinimumSize(new java.awt.Dimension(380, 23));
        txtFileName.setPreferredSize(new java.awt.Dimension(380, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLoadFromFile.add(txtFileName, gridBagConstraints);

        lblFileName.setText("File");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLoadFromFile.add(lblFileName, gridBagConstraints);

        btnBrowse.setText("Browse");
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLoadFromFile.add(btnBrowse, gridBagConstraints);

        lblReconTypeManual1.setText("Recon Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoadFromFile.add(lblReconTypeManual1, gridBagConstraints);

        cboFileUploadType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboFileUploadType.setPopupWidth(250);
        cboFileUploadType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFileUploadTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoadFromFile.add(cboFileUploadType, gridBagConstraints);

        btnFileUploadInsert.setForeground(new java.awt.Color(204, 0, 0));
        btnFileUploadInsert.setText("INSERT");
        btnFileUploadInsert.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnFileUploadInsert.setMaximumSize(new java.awt.Dimension(95, 30));
        btnFileUploadInsert.setMinimumSize(new java.awt.Dimension(95, 30));
        btnFileUploadInsert.setPreferredSize(new java.awt.Dimension(95, 30));
        btnFileUploadInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFileUploadInsertActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panLoadFromFile.add(btnFileUploadInsert, gridBagConstraints);

        btnFileUploadClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnFileUploadClear.setText("Clear");
        btnFileUploadClear.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnFileUploadClear.setMaximumSize(new java.awt.Dimension(93, 30));
        btnFileUploadClear.setMinimumSize(new java.awt.Dimension(93, 30));
        btnFileUploadClear.setPreferredSize(new java.awt.Dimension(93, 30));
        btnFileUploadClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFileUploadClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panLoadFromFile.add(btnFileUploadClear, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        panReconciliationUploadDetails.add(panLoadFromFile, gridBagConstraints);

        tabReconciliationDetails.addTab("Reconciliation File Upload", panReconciliationUploadDetails);

        panReconInquiry.setMinimumSize(new java.awt.Dimension(975, 500));
        panReconInquiry.setPreferredSize(new java.awt.Dimension(975, 500));
        panReconInquiry.setLayout(new java.awt.GridBagLayout());

        panReconDetails.setMinimumSize(new java.awt.Dimension(800, 60));
        panReconDetails.setPreferredSize(new java.awt.Dimension(800, 60));
        panReconDetails.setLayout(new java.awt.GridBagLayout());

        cboReconSourceType.setMinimumSize(new java.awt.Dimension(125, 22));
        cboReconSourceType.setPopupWidth(150);
        cboReconSourceType.setPreferredSize(new java.awt.Dimension(125, 22));
        cboReconSourceType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboReconSourceTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconDetails.add(cboReconSourceType, gridBagConstraints);

        lblReconFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconDetails.add(lblReconFromDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconDetails.add(tdtReconFromDt, gridBagConstraints);

        lblReconSourceType.setText("Source");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconDetails.add(lblReconSourceType, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconDetails.add(tdtReconToDt, gridBagConstraints);

        lblReconToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconDetails.add(lblReconToDate, gridBagConstraints);

        cboReconClassifiedCategory.setMinimumSize(new java.awt.Dimension(125, 22));
        cboReconClassifiedCategory.setPopupWidth(150);
        cboReconClassifiedCategory.setPreferredSize(new java.awt.Dimension(125, 22));
        cboReconClassifiedCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboReconClassifiedCategoryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconDetails.add(cboReconClassifiedCategory, gridBagConstraints);

        lblReconType.setText("Recon Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconDetails.add(lblReconType, gridBagConstraints);

        lblActNum2.setText("Deposit No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconDetails.add(lblActNum2, gridBagConstraints);

        txtPBTActNum.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPBTActNum.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPBTActNumFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconDetails.add(txtPBTActNum, gridBagConstraints);

        lblCustName2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCustName2.setText("Customer Name");
        lblCustName2.setMaximumSize(new java.awt.Dimension(80, 18));
        lblCustName2.setMinimumSize(new java.awt.Dimension(100, 18));
        lblCustName2.setPreferredSize(new java.awt.Dimension(120, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconDetails.add(lblCustName2, gridBagConstraints);

        txtPPTCustName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPPTCustName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPPTCustNameFocusLost(evt);
            }
        });
        txtPPTCustName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPPTCustNameKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconDetails.add(txtPPTCustName, gridBagConstraints);

        lblAmt2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAmt2.setText("Amount");
        lblAmt2.setMaximumSize(new java.awt.Dimension(70, 18));
        lblAmt2.setMinimumSize(new java.awt.Dimension(50, 18));
        lblAmt2.setPreferredSize(new java.awt.Dimension(70, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconDetails.add(lblAmt2, gridBagConstraints);

        txtPPTAmt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPPTAmt.setMinimumSize(new java.awt.Dimension(125, 22));
        txtPPTAmt.setPreferredSize(new java.awt.Dimension(125, 22));
        txtPPTAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPPTAmtFocusLost(evt);
            }
        });
        txtPPTAmt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPPTAmtKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconDetails.add(txtPPTAmt, gridBagConstraints);

        cboReconTransType.setMinimumSize(new java.awt.Dimension(125, 22));
        cboReconTransType.setPopupWidth(150);
        cboReconTransType.setPreferredSize(new java.awt.Dimension(125, 22));
        cboReconTransType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboReconTransTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconDetails.add(cboReconTransType, gridBagConstraints);

        lblReconTransType.setText("Trans Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconDetails.add(lblReconTransType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReconInquiry.add(panReconDetails, gridBagConstraints);

        panReconInquiryLst.setBorder(javax.swing.BorderFactory.createTitledBorder("Transaction List"));
        panReconInquiryLst.setMinimumSize(new java.awt.Dimension(975, 425));
        panReconInquiryLst.setPreferredSize(new java.awt.Dimension(975, 425));
        panReconInquiryLst.setLayout(new java.awt.GridBagLayout());

        tblReconInquirytLst.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl. No", "Account No", "Trans Dt", "Name", "Product Id", "Amount", "Trans Id", "Particulars", "Verified"
            }
        ));
        tblReconInquirytLst.setPreferredScrollableViewportSize(new java.awt.Dimension(450, 350));
        tblReconInquirytLst.setSelectionBackground(new java.awt.Color(255, 255, 51));
        tblReconInquirytLst.setSelectionForeground(new java.awt.Color(153, 0, 0));
        tblReconInquirytLst.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblReconInquirytLstMouseClicked(evt);
            }
        });
        srpTableReconInquiryLst.setViewportView(tblReconInquirytLst);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panReconInquiryLst.add(srpTableReconInquiryLst, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReconInquiry.add(panReconInquiryLst, gridBagConstraints);

        panAccountNo7.setMinimumSize(new java.awt.Dimension(950, 25));
        panAccountNo7.setPreferredSize(new java.awt.Dimension(950, 25));
        panAccountNo7.setLayout(new java.awt.GridBagLayout());

        lblBankTxnCountValue.setForeground(new java.awt.Color(0, 0, 255));
        lblBankTxnCountValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblBankTxnCountValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblBankTxnCountValue.setMaximumSize(new java.awt.Dimension(78, 18));
        lblBankTxnCountValue.setMinimumSize(new java.awt.Dimension(200, 18));
        lblBankTxnCountValue.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountNo7.add(lblBankTxnCountValue, gridBagConstraints);

        lblBankTxnAmountValue.setForeground(new java.awt.Color(0, 0, 255));
        lblBankTxnAmountValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblBankTxnAmountValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblBankTxnAmountValue.setMaximumSize(new java.awt.Dimension(78, 18));
        lblBankTxnAmountValue.setMinimumSize(new java.awt.Dimension(200, 18));
        lblBankTxnAmountValue.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountNo7.add(lblBankTxnAmountValue, gridBagConstraints);

        lblBankTxnCount.setForeground(new java.awt.Color(0, 0, 255));
        lblBankTxnCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBankTxnCount.setText("Count");
        lblBankTxnCount.setMaximumSize(new java.awt.Dimension(150, 18));
        lblBankTxnCount.setMinimumSize(new java.awt.Dimension(150, 18));
        lblBankTxnCount.setPreferredSize(new java.awt.Dimension(150, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountNo7.add(lblBankTxnCount, gridBagConstraints);

        lblBankTxnAmount.setForeground(new java.awt.Color(0, 0, 255));
        lblBankTxnAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBankTxnAmount.setText("Total Amount Rs.");
        lblBankTxnAmount.setMaximumSize(new java.awt.Dimension(78, 18));
        lblBankTxnAmount.setMinimumSize(new java.awt.Dimension(250, 18));
        lblBankTxnAmount.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountNo7.add(lblBankTxnAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panReconInquiry.add(panAccountNo7, gridBagConstraints);

        panReconDetails1.setMinimumSize(new java.awt.Dimension(120, 60));
        panReconDetails1.setPreferredSize(new java.awt.Dimension(120, 60));
        panReconDetails1.setLayout(new java.awt.GridBagLayout());

        btnReconClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnReconClear.setText("Clear");
        btnReconClear.setMaximumSize(new java.awt.Dimension(113, 29));
        btnReconClear.setMinimumSize(new java.awt.Dimension(113, 29));
        btnReconClear.setPreferredSize(new java.awt.Dimension(113, 29));
        btnReconClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReconClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        panReconDetails1.add(btnReconClear, gridBagConstraints);

        btnReconInquiryProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnReconInquiryProcess.setText("DISPLAY");
        btnReconInquiryProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReconInquiryProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        panReconDetails1.add(btnReconInquiryProcess, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReconInquiry.add(panReconDetails1, gridBagConstraints);

        tabReconciliationDetails.addTab("Processing Bank Transactions", panReconInquiry);

        panReconRejectInquiry.setMinimumSize(new java.awt.Dimension(975, 500));
        panReconRejectInquiry.setPreferredSize(new java.awt.Dimension(975, 500));
        panReconRejectInquiry.setLayout(new java.awt.GridBagLayout());

        panReconRejectDetails.setMinimumSize(new java.awt.Dimension(800, 60));
        panReconRejectDetails.setPreferredSize(new java.awt.Dimension(800, 60));
        panReconRejectDetails.setLayout(new java.awt.GridBagLayout());

        cboSourceRejectType.setMinimumSize(new java.awt.Dimension(125, 21));
        cboSourceRejectType.setPopupWidth(250);
        cboSourceRejectType.setPreferredSize(new java.awt.Dimension(125, 21));
        cboSourceRejectType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSourceRejectTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconRejectDetails.add(cboSourceRejectType, gridBagConstraints);

        lblReconRejectFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconRejectDetails.add(lblReconRejectFromDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconRejectDetails.add(tdtReconRejectFromDt, gridBagConstraints);

        lblReconRejectReconType.setText("Source");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconRejectDetails.add(lblReconRejectReconType, gridBagConstraints);

        lblReconRejectToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconRejectDetails.add(lblReconRejectToDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconRejectDetails.add(tdtReconRejectToDt, gridBagConstraints);

        lblReconRejectReconType1.setText("Recon Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconRejectDetails.add(lblReconRejectReconType1, gridBagConstraints);

        cboReconRejectType.setMinimumSize(new java.awt.Dimension(125, 21));
        cboReconRejectType.setPopupWidth(150);
        cboReconRejectType.setPreferredSize(new java.awt.Dimension(125, 21));
        cboReconRejectType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboReconRejectTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconRejectDetails.add(cboReconRejectType, gridBagConstraints);

        lblActNum.setText("Deposit No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconRejectDetails.add(lblActNum, gridBagConstraints);

        txtActNum.setMinimumSize(new java.awt.Dimension(100, 21));
        txtActNum.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtActNumFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconRejectDetails.add(txtActNum, gridBagConstraints);

        lblAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAmt.setText("Amount");
        lblAmt.setMaximumSize(new java.awt.Dimension(70, 18));
        lblAmt.setMinimumSize(new java.awt.Dimension(70, 18));
        lblAmt.setPreferredSize(new java.awt.Dimension(70, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconRejectDetails.add(lblAmt, gridBagConstraints);

        lblRejectAmt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lblRejectAmt.setMinimumSize(new java.awt.Dimension(125, 21));
        lblRejectAmt.setPreferredSize(new java.awt.Dimension(125, 21));
        lblRejectAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                lblRejectAmtFocusLost(evt);
            }
        });
        lblRejectAmt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lblRejectAmtKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconRejectDetails.add(lblRejectAmt, gridBagConstraints);

        lblCustName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCustName.setText("Customer Name");
        lblCustName.setMaximumSize(new java.awt.Dimension(100, 18));
        lblCustName.setMinimumSize(new java.awt.Dimension(100, 18));
        lblCustName.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconRejectDetails.add(lblCustName, gridBagConstraints);

        txtCustName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustNameFocusLost(evt);
            }
        });
        txtCustName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCustNameKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconRejectDetails.add(txtCustName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReconRejectInquiry.add(panReconRejectDetails, gridBagConstraints);

        panReconRejectInquiryLst.setBorder(javax.swing.BorderFactory.createTitledBorder("Transaction List"));
        panReconRejectInquiryLst.setMinimumSize(new java.awt.Dimension(975, 425));
        panReconRejectInquiryLst.setPreferredSize(new java.awt.Dimension(975, 425));
        panReconRejectInquiryLst.setLayout(new java.awt.GridBagLayout());

        tblReconRejectInquirytLst.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl. No", "Account No", "Trans Dt", "Name", "Product Id", "Amount", "Trans Id", "Particulars", "Verified"
            }
        ));
        tblReconRejectInquirytLst.setPreferredScrollableViewportSize(new java.awt.Dimension(450, 350));
        tblReconRejectInquirytLst.setSelectionBackground(new java.awt.Color(255, 255, 51));
        tblReconRejectInquirytLst.setSelectionForeground(new java.awt.Color(153, 0, 0));
        tblReconRejectInquirytLst.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblReconRejectInquirytLstMouseClicked(evt);
            }
        });
        srpTableReconRejectInquiryLst.setViewportView(tblReconRejectInquirytLst);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panReconRejectInquiryLst.add(srpTableReconRejectInquiryLst, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReconRejectInquiry.add(panReconRejectInquiryLst, gridBagConstraints);

        panAccountNo8.setMinimumSize(new java.awt.Dimension(950, 25));
        panAccountNo8.setPreferredSize(new java.awt.Dimension(950, 25));
        panAccountNo8.setLayout(new java.awt.GridBagLayout());

        lblRejectCountValue.setForeground(new java.awt.Color(0, 0, 255));
        lblRejectCountValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblRejectCountValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblRejectCountValue.setMaximumSize(new java.awt.Dimension(78, 18));
        lblRejectCountValue.setMinimumSize(new java.awt.Dimension(200, 18));
        lblRejectCountValue.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountNo8.add(lblRejectCountValue, gridBagConstraints);

        lblRejectAmountValue.setForeground(new java.awt.Color(0, 0, 255));
        lblRejectAmountValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblRejectAmountValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblRejectAmountValue.setMaximumSize(new java.awt.Dimension(78, 18));
        lblRejectAmountValue.setMinimumSize(new java.awt.Dimension(200, 18));
        lblRejectAmountValue.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountNo8.add(lblRejectAmountValue, gridBagConstraints);

        lblRejectCount.setForeground(new java.awt.Color(0, 0, 255));
        lblRejectCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRejectCount.setText("Rejected Count");
        lblRejectCount.setMaximumSize(new java.awt.Dimension(150, 18));
        lblRejectCount.setMinimumSize(new java.awt.Dimension(150, 18));
        lblRejectCount.setPreferredSize(new java.awt.Dimension(150, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountNo8.add(lblRejectCount, gridBagConstraints);

        lblRejectAmount.setForeground(new java.awt.Color(0, 0, 255));
        lblRejectAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRejectAmount.setText("Total Amount Rs.");
        lblRejectAmount.setMaximumSize(new java.awt.Dimension(78, 18));
        lblRejectAmount.setMinimumSize(new java.awt.Dimension(250, 18));
        lblRejectAmount.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountNo8.add(lblRejectAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panReconRejectInquiry.add(panAccountNo8, gridBagConstraints);

        panReconRejectDetails1.setMinimumSize(new java.awt.Dimension(120, 60));
        panReconRejectDetails1.setPreferredSize(new java.awt.Dimension(120, 60));
        panReconRejectDetails1.setLayout(new java.awt.GridBagLayout());

        btnReconRejectClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnReconRejectClear.setText("Clear");
        btnReconRejectClear.setMaximumSize(new java.awt.Dimension(113, 29));
        btnReconRejectClear.setMinimumSize(new java.awt.Dimension(113, 29));
        btnReconRejectClear.setPreferredSize(new java.awt.Dimension(113, 29));
        btnReconRejectClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReconRejectClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panReconRejectDetails1.add(btnReconRejectClear, gridBagConstraints);

        btnReconRejectInquiryProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnReconRejectInquiryProcess.setText("DISPLAY");
        btnReconRejectInquiryProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReconRejectInquiryProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panReconRejectDetails1.add(btnReconRejectInquiryProcess, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReconRejectInquiry.add(panReconRejectDetails1, gridBagConstraints);

        tabReconciliationDetails.addTab("Rejected Transactions", panReconRejectInquiry);

        panMatchStatusInquiry.setMinimumSize(new java.awt.Dimension(975, 500));
        panMatchStatusInquiry.setPreferredSize(new java.awt.Dimension(975, 500));
        panMatchStatusInquiry.setLayout(new java.awt.GridBagLayout());

        panMatchStatusDetails.setMinimumSize(new java.awt.Dimension(800, 60));
        panMatchStatusDetails.setPreferredSize(new java.awt.Dimension(800, 60));
        panMatchStatusDetails.setLayout(new java.awt.GridBagLayout());

        cboMatchStatusType.setMinimumSize(new java.awt.Dimension(125, 22));
        cboMatchStatusType.setPopupWidth(250);
        cboMatchStatusType.setPreferredSize(new java.awt.Dimension(125, 22));
        cboMatchStatusType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMatchStatusTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMatchStatusDetails.add(cboMatchStatusType, gridBagConstraints);

        lblMatchStatusFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMatchStatusDetails.add(lblMatchStatusFromDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMatchStatusDetails.add(tdtMatchStatusFromDt, gridBagConstraints);

        lblMatchStatusReconType.setText("Source");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMatchStatusDetails.add(lblMatchStatusReconType, gridBagConstraints);

        lblMatchStatusReconType1.setText("Recon Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMatchStatusDetails.add(lblMatchStatusReconType1, gridBagConstraints);

        cboReconTypeMatch.setMinimumSize(new java.awt.Dimension(100, 21));
        cboReconTypeMatch.setPopupWidth(150);
        cboReconTypeMatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboReconTypeMatchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMatchStatusDetails.add(cboReconTypeMatch, gridBagConstraints);

        lblMatchStatusToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMatchStatusDetails.add(lblMatchStatusToDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMatchStatusDetails.add(tdtMatchStatusToDt, gridBagConstraints);

        lblActNum1.setText("Deposit No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMatchStatusDetails.add(lblActNum1, gridBagConstraints);

        txtMatchActNum.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMatchActNum.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMatchActNumFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMatchStatusDetails.add(txtMatchActNum, gridBagConstraints);

        lblCustName1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCustName1.setText("Customer Name");
        lblCustName1.setMaximumSize(new java.awt.Dimension(80, 18));
        lblCustName1.setMinimumSize(new java.awt.Dimension(80, 18));
        lblCustName1.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMatchStatusDetails.add(lblCustName1, gridBagConstraints);

        txtMatchCustName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMatchCustName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMatchCustNameFocusLost(evt);
            }
        });
        txtMatchCustName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMatchCustNameKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMatchStatusDetails.add(txtMatchCustName, gridBagConstraints);

        lblAmt1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAmt1.setText("Amount");
        lblAmt1.setMaximumSize(new java.awt.Dimension(70, 18));
        lblAmt1.setMinimumSize(new java.awt.Dimension(70, 18));
        lblAmt1.setPreferredSize(new java.awt.Dimension(70, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMatchStatusDetails.add(lblAmt1, gridBagConstraints);

        txtMatchAmt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMatchAmt.setMinimumSize(new java.awt.Dimension(125, 22));
        txtMatchAmt.setPreferredSize(new java.awt.Dimension(125, 22));
        txtMatchAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMatchAmtFocusLost(evt);
            }
        });
        txtMatchAmt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMatchAmtKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMatchStatusDetails.add(txtMatchAmt, gridBagConstraints);

        lblMatchTransType.setText("Trans Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMatchStatusDetails.add(lblMatchTransType, gridBagConstraints);

        cboMatchTransType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboMatchTransType.setPopupWidth(150);
        cboMatchTransType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMatchTransTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMatchStatusDetails.add(cboMatchTransType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMatchStatusInquiry.add(panMatchStatusDetails, gridBagConstraints);

        panMatchStatusInquiryLst.setBorder(javax.swing.BorderFactory.createTitledBorder("Transaction List"));
        panMatchStatusInquiryLst.setMinimumSize(new java.awt.Dimension(975, 425));
        panMatchStatusInquiryLst.setPreferredSize(new java.awt.Dimension(975, 425));
        panMatchStatusInquiryLst.setLayout(new java.awt.GridBagLayout());

        tblMatchStatusInquirytLst.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl. No", "Account No", "Trans Dt", "Name", "Product Id", "Amount", "Trans Id", "Particulars", "Verified"
            }
        ));
        tblMatchStatusInquirytLst.setPreferredScrollableViewportSize(new java.awt.Dimension(450, 350));
        tblMatchStatusInquirytLst.setSelectionBackground(new java.awt.Color(255, 255, 51));
        tblMatchStatusInquirytLst.setSelectionForeground(new java.awt.Color(153, 0, 0));
        tblMatchStatusInquirytLst.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMatchStatusInquirytLstMouseClicked(evt);
            }
        });
        srpTableMatchStatusInquiryLst.setViewportView(tblMatchStatusInquirytLst);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMatchStatusInquiryLst.add(srpTableMatchStatusInquiryLst, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMatchStatusInquiry.add(panMatchStatusInquiryLst, gridBagConstraints);

        panAccountNo6.setMinimumSize(new java.awt.Dimension(950, 25));
        panAccountNo6.setPreferredSize(new java.awt.Dimension(950, 25));
        panAccountNo6.setLayout(new java.awt.GridBagLayout());

        lblMatchCountValue.setForeground(new java.awt.Color(0, 0, 204));
        lblMatchCountValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblMatchCountValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblMatchCountValue.setMaximumSize(new java.awt.Dimension(78, 18));
        lblMatchCountValue.setMinimumSize(new java.awt.Dimension(200, 18));
        lblMatchCountValue.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountNo6.add(lblMatchCountValue, gridBagConstraints);

        lblMatchAmountValue.setForeground(new java.awt.Color(0, 0, 204));
        lblMatchAmountValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblMatchAmountValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblMatchAmountValue.setMaximumSize(new java.awt.Dimension(78, 18));
        lblMatchAmountValue.setMinimumSize(new java.awt.Dimension(300, 18));
        lblMatchAmountValue.setPreferredSize(new java.awt.Dimension(300, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountNo6.add(lblMatchAmountValue, gridBagConstraints);

        lblMatchCount.setForeground(new java.awt.Color(0, 0, 255));
        lblMatchCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMatchCount.setText("Match Count");
        lblMatchCount.setMaximumSize(new java.awt.Dimension(78, 18));
        lblMatchCount.setMinimumSize(new java.awt.Dimension(200, 18));
        lblMatchCount.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountNo6.add(lblMatchCount, gridBagConstraints);

        lblMatchAmount.setForeground(new java.awt.Color(0, 0, 204));
        lblMatchAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMatchAmount.setText("Match Amount");
        lblMatchAmount.setMaximumSize(new java.awt.Dimension(78, 18));
        lblMatchAmount.setMinimumSize(new java.awt.Dimension(100, 18));
        lblMatchAmount.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountNo6.add(lblMatchAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMatchStatusInquiry.add(panAccountNo6, gridBagConstraints);

        panMatchStatusDetails1.setMinimumSize(new java.awt.Dimension(120, 60));
        panMatchStatusDetails1.setPreferredSize(new java.awt.Dimension(120, 60));
        panMatchStatusDetails1.setLayout(new java.awt.GridBagLayout());

        btnMatchStatusClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnMatchStatusClear.setText("Clear");
        btnMatchStatusClear.setMaximumSize(new java.awt.Dimension(113, 29));
        btnMatchStatusClear.setMinimumSize(new java.awt.Dimension(113, 29));
        btnMatchStatusClear.setPreferredSize(new java.awt.Dimension(113, 29));
        btnMatchStatusClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMatchStatusClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panMatchStatusDetails1.add(btnMatchStatusClear, gridBagConstraints);

        btnMatchStatusInquiryProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnMatchStatusInquiryProcess.setText("DISPLAY");
        btnMatchStatusInquiryProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMatchStatusInquiryProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panMatchStatusDetails1.add(btnMatchStatusInquiryProcess, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMatchStatusInquiry.add(panMatchStatusDetails1, gridBagConstraints);

        tabReconciliationDetails.addTab("Match Status Inquiry", panMatchStatusInquiry);

        panManualReconciliation.setMinimumSize(new java.awt.Dimension(800, 508));
        panManualReconciliation.setPreferredSize(new java.awt.Dimension(800, 508));
        panManualReconciliation.setLayout(new java.awt.GridBagLayout());

        panTheirsTransactionTable.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTheirsTransactionTable.setMinimumSize(new java.awt.Dimension(950, 210));
        panTheirsTransactionTable.setPreferredSize(new java.awt.Dimension(950, 210));
        panTheirsTransactionTable.setLayout(new java.awt.GridBagLayout());

        panOurTransactionList.setBorder(javax.swing.BorderFactory.createTitledBorder("Processing Bank Entries"));
        panOurTransactionList.setMinimumSize(new java.awt.Dimension(900, 170));
        panOurTransactionList.setPreferredSize(new java.awt.Dimension(900, 170));
        panOurTransactionList.setLayout(new java.awt.GridBagLayout());

        srpOurTransactionList.setMinimumSize(new java.awt.Dimension(452, 110));
        srpOurTransactionList.setPreferredSize(new java.awt.Dimension(452, 110));

        tblTheirsTransactionList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SI. No.", "Trans Dt", "Bank Inquiry Ref No", "Description", "Amount", "Branch"
            }
        ));
        tblTheirsTransactionList.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblTheirsTransactionList.setPreferredScrollableViewportSize(new java.awt.Dimension(450, 225));
        tblTheirsTransactionList.setSelectionBackground(new java.awt.Color(204, 204, 255));
        tblTheirsTransactionList.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblTheirsTransactionList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTheirsTransactionListMouseClicked(evt);
            }
        });
        srpOurTransactionList.setViewportView(tblTheirsTransactionList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panOurTransactionList.add(srpOurTransactionList, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTheirsTransactionTable.add(panOurTransactionList, gridBagConstraints);
        panOurTransactionList.getAccessibleContext().setAccessibleName("Correspondent Bank Entries");

        panAccountNo4.setMinimumSize(new java.awt.Dimension(950, 25));
        panAccountNo4.setPreferredSize(new java.awt.Dimension(950, 25));
        panAccountNo4.setLayout(new java.awt.GridBagLayout());

        lblUnMatchTheirsCountValue.setForeground(new java.awt.Color(0, 0, 255));
        lblUnMatchTheirsCountValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblUnMatchTheirsCountValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblUnMatchTheirsCountValue.setMaximumSize(new java.awt.Dimension(78, 18));
        lblUnMatchTheirsCountValue.setMinimumSize(new java.awt.Dimension(200, 18));
        lblUnMatchTheirsCountValue.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountNo4.add(lblUnMatchTheirsCountValue, gridBagConstraints);

        lblUnMatchTheirsAmountValue.setForeground(new java.awt.Color(0, 0, 255));
        lblUnMatchTheirsAmountValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblUnMatchTheirsAmountValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblUnMatchTheirsAmountValue.setMaximumSize(new java.awt.Dimension(78, 18));
        lblUnMatchTheirsAmountValue.setMinimumSize(new java.awt.Dimension(200, 18));
        lblUnMatchTheirsAmountValue.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountNo4.add(lblUnMatchTheirsAmountValue, gridBagConstraints);

        lblUnMatchTheirsCount.setForeground(new java.awt.Color(0, 0, 255));
        lblUnMatchTheirsCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUnMatchTheirsCount.setText("UnMatch Count");
        lblUnMatchTheirsCount.setMaximumSize(new java.awt.Dimension(78, 18));
        lblUnMatchTheirsCount.setMinimumSize(new java.awt.Dimension(150, 18));
        lblUnMatchTheirsCount.setPreferredSize(new java.awt.Dimension(75, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountNo4.add(lblUnMatchTheirsCount, gridBagConstraints);

        lblUnMatchTheirsAmount.setForeground(new java.awt.Color(0, 0, 255));
        lblUnMatchTheirsAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUnMatchTheirsAmount.setText("UnMatch Amount");
        lblUnMatchTheirsAmount.setMaximumSize(new java.awt.Dimension(78, 18));
        lblUnMatchTheirsAmount.setMinimumSize(new java.awt.Dimension(200, 18));
        lblUnMatchTheirsAmount.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountNo4.add(lblUnMatchTheirsAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTheirsTransactionTable.add(panAccountNo4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panManualReconciliation.add(panTheirsTransactionTable, gridBagConstraints);

        panOursTransactionTable.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panOursTransactionTable.setMinimumSize(new java.awt.Dimension(950, 210));
        panOursTransactionTable.setPreferredSize(new java.awt.Dimension(950, 210));
        panOursTransactionTable.setLayout(new java.awt.GridBagLayout());

        panStatementTxnList.setBorder(javax.swing.BorderFactory.createTitledBorder("System Entries"));
        panStatementTxnList.setMinimumSize(new java.awt.Dimension(900, 170));
        panStatementTxnList.setPreferredSize(new java.awt.Dimension(900, 170));
        panStatementTxnList.setLayout(new java.awt.GridBagLayout());

        srpStatementTxnList.setMinimumSize(new java.awt.Dimension(746, 110));
        srpStatementTxnList.setPreferredSize(new java.awt.Dimension(746, 110));

        tblOursTransactionList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SI. No.", "Trans Dt", "Reference No", "Description", "Amount", "Our UTR Number", "Branch"
            }
        ));
        tblOursTransactionList.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblOursTransactionList.setPreferredScrollableViewportSize(new java.awt.Dimension(450, 350));
        tblOursTransactionList.setSelectionBackground(new java.awt.Color(204, 204, 255));
        tblOursTransactionList.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblOursTransactionList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblOursTransactionListMouseClicked(evt);
            }
        });
        srpStatementTxnList.setViewportView(tblOursTransactionList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatementTxnList.add(srpStatementTxnList, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOursTransactionTable.add(panStatementTxnList, gridBagConstraints);

        panAccountNo5.setMinimumSize(new java.awt.Dimension(950, 25));
        panAccountNo5.setPreferredSize(new java.awt.Dimension(950, 25));
        panAccountNo5.setLayout(new java.awt.GridBagLayout());

        lblUnMatchOursCountValue.setForeground(new java.awt.Color(0, 0, 255));
        lblUnMatchOursCountValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblUnMatchOursCountValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblUnMatchOursCountValue.setMaximumSize(new java.awt.Dimension(78, 18));
        lblUnMatchOursCountValue.setMinimumSize(new java.awt.Dimension(200, 18));
        lblUnMatchOursCountValue.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountNo5.add(lblUnMatchOursCountValue, gridBagConstraints);

        lblUnMatchOursAmountValue.setForeground(new java.awt.Color(0, 0, 255));
        lblUnMatchOursAmountValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblUnMatchOursAmountValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblUnMatchOursAmountValue.setMaximumSize(new java.awt.Dimension(78, 18));
        lblUnMatchOursAmountValue.setMinimumSize(new java.awt.Dimension(200, 18));
        lblUnMatchOursAmountValue.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountNo5.add(lblUnMatchOursAmountValue, gridBagConstraints);

        lblUnMatchOursCount.setForeground(new java.awt.Color(0, 0, 255));
        lblUnMatchOursCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUnMatchOursCount.setText("UnMatch count");
        lblUnMatchOursCount.setMaximumSize(new java.awt.Dimension(150, 18));
        lblUnMatchOursCount.setMinimumSize(new java.awt.Dimension(150, 18));
        lblUnMatchOursCount.setPreferredSize(new java.awt.Dimension(150, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountNo5.add(lblUnMatchOursCount, gridBagConstraints);

        lblUnMatchOursAmount.setForeground(new java.awt.Color(0, 0, 255));
        lblUnMatchOursAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUnMatchOursAmount.setText("UnMatch Amount");
        lblUnMatchOursAmount.setMaximumSize(new java.awt.Dimension(78, 18));
        lblUnMatchOursAmount.setMinimumSize(new java.awt.Dimension(250, 18));
        lblUnMatchOursAmount.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountNo5.add(lblUnMatchOursAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panOursTransactionTable.add(panAccountNo5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panManualReconciliation.add(panOursTransactionTable, gridBagConstraints);

        panReconSelectionDetails.setMinimumSize(new java.awt.Dimension(950, 30));
        panReconSelectionDetails.setPreferredSize(new java.awt.Dimension(950, 30));
        panReconSelectionDetails.setLayout(new java.awt.GridBagLayout());

        tdtFromDateManualRecon.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtFromDateManualRecon.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtFromDateManualRecon.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDateManualReconFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconSelectionDetails.add(tdtFromDateManualRecon, gridBagConstraints);

        lblFromDateManualRecon.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFromDateManualRecon.setText("From Date");
        lblFromDateManualRecon.setMaximumSize(new java.awt.Dimension(78, 18));
        lblFromDateManualRecon.setMinimumSize(new java.awt.Dimension(62, 18));
        lblFromDateManualRecon.setPreferredSize(new java.awt.Dimension(62, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconSelectionDetails.add(lblFromDateManualRecon, gridBagConstraints);

        lblReconTypeManual.setText("Source");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconSelectionDetails.add(lblReconTypeManual, gridBagConstraints);

        cboUnMatchStatusType.setMinimumSize(new java.awt.Dimension(125, 22));
        cboUnMatchStatusType.setPopupWidth(250);
        cboUnMatchStatusType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboUnMatchStatusTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconSelectionDetails.add(cboUnMatchStatusType, gridBagConstraints);

        lblToDateAutoRecon.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblToDateAutoRecon.setText("To Date");
        lblToDateAutoRecon.setMaximumSize(new java.awt.Dimension(78, 18));
        lblToDateAutoRecon.setMinimumSize(new java.awt.Dimension(50, 18));
        lblToDateAutoRecon.setPreferredSize(new java.awt.Dimension(50, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconSelectionDetails.add(lblToDateAutoRecon, gridBagConstraints);

        tdtToDateManualRecon.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtToDateManualRecon.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtToDateManualRecon.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToDateManualReconFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconSelectionDetails.add(tdtToDateManualRecon, gridBagConstraints);

        cboReconTypeManual.setMinimumSize(new java.awt.Dimension(125, 22));
        cboReconTypeManual.setPopupWidth(150);
        cboReconTypeManual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboReconTypeManualActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconSelectionDetails.add(cboReconTypeManual, gridBagConstraints);

        lblReconTypeManual2.setText("Recon Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconSelectionDetails.add(lblReconTypeManual2, gridBagConstraints);

        btnDisplay.setForeground(new java.awt.Color(204, 0, 0));
        btnDisplay.setText("DISPLAY");
        btnDisplay.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnDisplay.setMaximumSize(new java.awt.Dimension(95, 30));
        btnDisplay.setMinimumSize(new java.awt.Dimension(95, 30));
        btnDisplay.setPreferredSize(new java.awt.Dimension(95, 30));
        btnDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisplayActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconSelectionDetails.add(btnDisplay, gridBagConstraints);

        btnManualMatchClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnManualMatchClear.setText("Clear");
        btnManualMatchClear.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnManualMatchClear.setMaximumSize(new java.awt.Dimension(93, 30));
        btnManualMatchClear.setMinimumSize(new java.awt.Dimension(93, 30));
        btnManualMatchClear.setPreferredSize(new java.awt.Dimension(93, 30));
        btnManualMatchClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManualMatchClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panReconSelectionDetails.add(btnManualMatchClear, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panManualReconciliation.add(panReconSelectionDetails, gridBagConstraints);

        panFromBranchDetails4.setMinimumSize(new java.awt.Dimension(950, 40));
        panFromBranchDetails4.setPreferredSize(new java.awt.Dimension(950, 40));
        panFromBranchDetails4.setLayout(new java.awt.GridBagLayout());

        panAutoCount.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panAutoCount.setMinimumSize(new java.awt.Dimension(775, 50));
        panAutoCount.setPreferredSize(new java.awt.Dimension(775, 50));
        panAutoCount.setLayout(new java.awt.GridBagLayout());

        lblAutoReconAmount.setForeground(new java.awt.Color(0, 0, 255));
        lblAutoReconAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAutoReconAmount.setText("SYSTEM ENTRIES MATCHED AMOUNT:");
        lblAutoReconAmount.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAutoReconAmount.setMaximumSize(new java.awt.Dimension(63, 15));
        lblAutoReconAmount.setMinimumSize(new java.awt.Dimension(250, 15));
        lblAutoReconAmount.setPreferredSize(new java.awt.Dimension(250, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAutoCount.add(lblAutoReconAmount, gridBagConstraints);

        lblAutoAmount.setForeground(new java.awt.Color(0, 0, 255));
        lblAutoAmount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblAutoAmount.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAutoAmount.setMaximumSize(new java.awt.Dimension(150, 15));
        lblAutoAmount.setMinimumSize(new java.awt.Dimension(150, 15));
        lblAutoAmount.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAutoCount.add(lblAutoAmount, gridBagConstraints);

        lblAutoReconCount.setForeground(new java.awt.Color(0, 0, 255));
        lblAutoReconCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAutoReconCount.setText("SYSTEM ENTRIES COUNT:");
        lblAutoReconCount.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAutoReconCount.setMaximumSize(new java.awt.Dimension(225, 15));
        lblAutoReconCount.setMinimumSize(new java.awt.Dimension(225, 15));
        lblAutoReconCount.setPreferredSize(new java.awt.Dimension(225, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAutoCount.add(lblAutoReconCount, gridBagConstraints);

        lblAutoCount.setForeground(new java.awt.Color(0, 0, 255));
        lblAutoCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblAutoCount.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAutoCount.setMaximumSize(new java.awt.Dimension(75, 15));
        lblAutoCount.setMinimumSize(new java.awt.Dimension(75, 15));
        lblAutoCount.setPreferredSize(new java.awt.Dimension(75, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAutoCount.add(lblAutoCount, gridBagConstraints);

        lblAutoReconCount1.setForeground(new java.awt.Color(0, 0, 255));
        lblAutoReconCount1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAutoReconCount1.setText("BANK ENTRIES MATCHED COUNT:");
        lblAutoReconCount1.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAutoReconCount1.setMaximumSize(new java.awt.Dimension(225, 15));
        lblAutoReconCount1.setMinimumSize(new java.awt.Dimension(225, 15));
        lblAutoReconCount1.setPreferredSize(new java.awt.Dimension(225, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAutoCount.add(lblAutoReconCount1, gridBagConstraints);

        lblAutoCount1.setForeground(new java.awt.Color(0, 0, 255));
        lblAutoCount1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblAutoCount1.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAutoCount1.setMaximumSize(new java.awt.Dimension(75, 15));
        lblAutoCount1.setMinimumSize(new java.awt.Dimension(75, 15));
        lblAutoCount1.setPreferredSize(new java.awt.Dimension(75, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAutoCount.add(lblAutoCount1, gridBagConstraints);

        lblAutoReconAmount1.setForeground(new java.awt.Color(0, 0, 255));
        lblAutoReconAmount1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAutoReconAmount1.setText("BANK ENTRIES MATCHED AMOUNT:");
        lblAutoReconAmount1.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAutoReconAmount1.setMaximumSize(new java.awt.Dimension(230, 15));
        lblAutoReconAmount1.setMinimumSize(new java.awt.Dimension(250, 15));
        lblAutoReconAmount1.setPreferredSize(new java.awt.Dimension(230, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAutoCount.add(lblAutoReconAmount1, gridBagConstraints);

        lblAutoAmount1.setForeground(new java.awt.Color(0, 0, 255));
        lblAutoAmount1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblAutoAmount1.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAutoAmount1.setMaximumSize(new java.awt.Dimension(150, 15));
        lblAutoAmount1.setMinimumSize(new java.awt.Dimension(150, 15));
        lblAutoAmount1.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAutoCount.add(lblAutoAmount1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panFromBranchDetails4.add(panAutoCount, gridBagConstraints);

        btnReconciliation.setForeground(new java.awt.Color(0, 0, 255));
        btnReconciliation.setText("MATCH");
        btnReconciliation.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnReconciliation.setMaximumSize(new java.awt.Dimension(160, 27));
        btnReconciliation.setMinimumSize(new java.awt.Dimension(100, 27));
        btnReconciliation.setPreferredSize(new java.awt.Dimension(160, 27));
        btnReconciliation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReconciliationActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panFromBranchDetails4.add(btnReconciliation, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        panManualReconciliation.add(panFromBranchDetails4, gridBagConstraints);

        tabReconciliationDetails.addTab("Manual Recon", panManualReconciliation);

        getContentPane().add(tabReconciliationDetails, java.awt.BorderLayout.CENTER);
        tabReconciliationDetails.getAccessibleContext().setAccessibleName("Reconciliation Matched Records");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblFileDataDetailsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblFileDataDetailsFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblFileDataDetailsFocusLost

    private void srpFileDataDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_srpFileDataDetailsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_srpFileDataDetailsMouseClicked

    private void tblFileDataDetailsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFileDataDetailsMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblFileDataDetailsMouseReleased

    private void setColorList() {
        if (tblFileDataDetails.getRowCount() > 0) {
            colorList = new ArrayList();
            for (int i = 0; i < tblFileDataDetails.getRowCount(); i++) {
                if (CommonUtil.convertObjToStr(tblFileDataDetails.getValueAt(i, 6)).length() <= 0) {
                    colorList.add(String.valueOf(i));
                }
            }
        }
    }

    private void setColour() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (colorList.contains(String.valueOf(row))) {
                    setForeground(Color.RED);
                } else {
                    setForeground(Color.BLACK);
                }
                // Set oquae
                this.setOpaque(true);
                return this;
            }
        };
        tblFileDataDetails.setDefaultRenderer(Object.class, renderer);
    }

    private void tblFileDataDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFileDataDetailsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblFileDataDetailsMouseClicked

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
        // TODO add your handling code here:
        try {
//            observable.doProcess(null);
            String uploadType = CommonUtil.convertObjToStr(cboFileUploadType.getSelectedItem());
            if (uploadType.equals("")) {
                displayAlert("Please select File Upload Type...");
                return;
            }
            String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panReconciliationUploadDetails);
            if (mandatoryMessage.length() > 0) {
                displayAlert(mandatoryMessage);
            } else {
                maturityDate = null;
                javax.swing.JFileChooser fc = new javax.swing.JFileChooser();;
                int returnVal = fc.showOpenDialog(null);
                if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                    final java.io.File file = fc.getSelectedFile();
                    String fname = file.getName();
                    fname = fname.toUpperCase();
                    if (uploadType.equals("MT940") && (fname != null || !fname.equalsIgnoreCase(""))) {
                        if (fname.substring(fname.indexOf('.') + 1, fname.length()).equals("TXT")) {
                            txtFileName.setText(fc.getSelectedFile().toString());
                            txtFileName.setEnabled(false);
                        } else {
                            displayAlert("File should be .txt format");
                            return;
                        }
                    } else if (uploadType.equals("1PAY") && (fname != null || !fname.equalsIgnoreCase(""))) {
                        if (fname.substring(fname.indexOf('.') + 1, fname.length()).equals("XLS")) {
                            txtFileName.setText(fc.getSelectedFile().toString());
                            txtFileName.setEnabled(false);
                        } else {
                            displayAlert("File should be .xls format");
                            return;
                        }
                    }
                    CommonUtil comm = new CommonUtil();
                    final JDialog loading = comm.addProgressBar();
                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                        @Override
                        protected Void doInBackground() throws InterruptedException {
                            try {
                                selectedFile = file;
//                                getFileData(file);
//                                setSizeTableData();
                                btnBrowse.setEnabled(false);
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
                }
            }
        } catch (Exception e) {
            displayAlert("File should be .xls format");
        }
    }//GEN-LAST:event_btnBrowseActionPerformed

    public void getFileData(java.io.File file) {
        try {
            totFileDataList = new ArrayList();
            HashMap singleMap = new HashMap();
            HashMap singleDepositMap = new HashMap();
            depositDetailsMap = new HashMap();
            StringBuffer wrongRow = new StringBuffer();
            LinkedHashMap wrongCellDetailsMap = new LinkedHashMap();
            String HEADING_0 = "ROW NUMBER";
            String HEADING_1 = "ERROR_COLUMNS";
            int x = 0;
            String custName = "";
            String dob = "";
            String motherName = "";
            double depAmount = 0;
            String depositDt = "";
            String maturityDt = "";
            String addr1 = "";
            String addr2 = "";
            String addr3 = "";
            String district = "";
            String pincode = "";
            String refNum = "";
            ArrayList rowList = new ArrayList();
            //Need to Check File Already Processed or not
            singleMap.put("FILE_NAME", file.getName());
            List fileAlreadyDoneLst = ClientUtil.executeQuery("getToCheckDepositBulkFileAlreadyDone", singleMap);
            if (fileAlreadyDoneLst != null && fileAlreadyDoneLst.size() > 0) {
                ClientUtil.showMessageWindow("This File Already Uploaded, Can't Upload Again !!! ");
//                btnCancelActionPerformed(null);
                return;
            }
            java.io.FileInputStream inpuStream = new java.io.FileInputStream(file);
            HSSFWorkbook workbook = new HSSFWorkbook(inpuStream);
            HSSFSheet sheet = workbook.getSheetAt(0);
            observable.setTxtFileName(file.getName());
            int rows = sheet.getPhysicalNumberOfRows();
            x = 0;
            HSSFRow row = null;
            HSSFCell cell = null;
            rows = sheet.getPhysicalNumberOfRows();
            Date dobDate = null;
            Date depDate = null;
            Date matDate = null;
            int rownumber = 0;
            key = "";
            for (int i = 1; i < rows; i++) {
                row = sheet.getRow(i);
                rownumber = row.getRowNum() + 1;
                cell = null;
                custName = "";
                dob = "";
                motherName = "";
                depAmount = 0;
                depositDt = "";
                maturityDt = "";
                addr1 = "";
                addr2 = "";
                addr3 = "";
                district = "";
                pincode = "";
                refNum = "";
                singleMap = new HashMap();
                wrongRow = new StringBuffer();

                cell = row.getCell((short) 0);
                custName = CommonUtil.convertObjToStr(getCellValue(cell)).toUpperCase();  //CUST_NAME

                dobDate = row.getCell((short) 1).getDateCellValue();
                dob = CommonUtil.convertObjToStr(dobDate);                      //DOB

                cell = row.getCell((short) 2);
                motherName = CommonUtil.convertObjToStr(getCellValue(cell)).toUpperCase(); //MOTHER_NAME

                cell = row.getCell((short) 3);
                addr1 = CommonUtil.convertObjToStr(getCellValue(cell));         //ADDRESS1

                cell = row.getCell((short) 4);
                addr2 = CommonUtil.convertObjToStr(getCellValue(cell));         //ADDRESS2

                cell = row.getCell((short) 5);
                addr3 = CommonUtil.convertObjToStr(getCellValue(cell));         //ADDRESS3 

                cell = row.getCell((short) 6);
                district = CommonUtil.convertObjToStr(getCellValue(cell)).toUpperCase(); //DISTRICT

                cell = row.getCell((short) 7);
                pincode = CommonUtil.convertObjToStr(getCellValue(cell));       //PINCODE

                cell = row.getCell((short) 8);
                depAmount = CommonUtil.convertObjToDouble(getCellValue(cell));  //AMOUNT

                depDate = row.getCell((short) 9).getDateCellValue();
                depositDt = CommonUtil.convertObjToStr(depDate);                //DEPOSIT_DT

                matDate = row.getCell((short) 10).getDateCellValue();
                maturityDt = CommonUtil.convertObjToStr(matDate);               //MATURITY_DT

                cell = row.getCell((short) 11);
                refNum = CommonUtil.convertObjToStr(getCellValue(cell));        //REF_NUM

                key = String.valueOf(depAmount) + depositDt;
                maturityDate = matDate;
                if (custName.length() <= 0) {
                    if (wrongRow.length() > 0) {
                        wrongRow.append(" - ");
                    }
                    wrongRow.append("Customer Name is Empty ");
                }
                if (dob.length() <= 0) {
                    if (wrongRow.length() > 0) {
                        wrongRow.append(" - ");
                    }
                    wrongRow.append("DOB is Empty ");
                }
                if (motherName.length() <= 0) {
                    if (wrongRow.length() > 0) {
                        wrongRow.append(" - ");
                    }
                    wrongRow.append("Mother Name is Empty ");
                }
                if (CommonUtil.convertObjToDouble(depAmount) <= 0) {
                    if (wrongRow.length() > 0) {
                        wrongRow.append(" - ");
                    }
                    wrongRow.append("Deposit Amount is Empty ");
                }
                if (depositDt.length() <= 0) {
                    if (wrongRow.length() > 0) {
                        wrongRow.append(" - ");
                    }
                    wrongRow.append("Deposit Date is Empty ");
                }
                if ((maturityDt.length() <= 0) || (i > 1 && DateUtil.dateDiff(maturityDate, matDate) != 0)) {
                    if (wrongRow.length() > 0) {
                        wrongRow.append(" - ");
                    }
                    wrongRow.append("Maturity Date is Empty ");
                }
                if (wrongRow.length() > 0) {
                    singleMap.put(HEADING_0, CommonUtil.convertObjToStr(rownumber));
                    singleMap.put(HEADING_1, CommonUtil.convertObjToStr(wrongRow));
                    wrongCellDetailsMap.put(CommonUtil.convertObjToStr(rownumber), singleMap);
                }
                rowList = new ArrayList();
                rowList.add(custName);
                rowList.add(dob);
                rowList.add(motherName);
                rowList.add(String.valueOf(ClientUtil.convertObjToCurrency(String.valueOf(depAmount))));
                rowList.add(depositDt);
                rowList.add(maturityDt);
                rowList.add(addr1);
                rowList.add(addr2);
                rowList.add(addr3);
                rowList.add(district);
                rowList.add(pincode);
                rowList.add(refNum);
                if (depositDetailsMap.containsKey(key)) {
                    singleDepositMap = (HashMap) depositDetailsMap.get(key);
                    if (singleDepositMap != null && singleDepositMap.size() > 0) {
                        rowList.add(CommonUtil.convertObjToDouble(singleDepositMap.get("ROI")));
                        rowList.add(CommonUtil.convertObjToStr(ClientUtil.convertObjToCurrency(singleDepositMap.get("INTEREST"))));
                        rowList.add(CommonUtil.convertObjToStr(ClientUtil.convertObjToCurrency(singleDepositMap.get("MATURITY_AMOUNT"))));
                    }
                } else {
                    singleDepositMap = (HashMap) depositDetailsMap.get(key);
                    if (singleDepositMap != null && singleDepositMap.size() > 0) {
                        rowList.add(CommonUtil.convertObjToDouble(singleDepositMap.get("ROI")));
                        rowList.add(CommonUtil.convertObjToStr(ClientUtil.convertObjToCurrency(singleDepositMap.get("INTEREST"))));
                        rowList.add(CommonUtil.convertObjToStr(ClientUtil.convertObjToCurrency(singleDepositMap.get("MATURITY_AMOUNT"))));
                    }
                }
                totFileDataList.add(rowList);
            }
            if (inpuStream != null) {
                inpuStream.close();
            }
            if (wrongCellDetailsMap != null && wrongCellDetailsMap.size() > 0) {
                HashMap headerMap = new HashMap();
                HashMap ALLMap = new HashMap();
                headerMap.put("HEADING_0", HEADING_0);
                headerMap.put("HEADING_1", HEADING_1);
                ALLMap.put("HEADER_MAP", headerMap);
                ALLMap.put("DATA", wrongCellDetailsMap);
                TableDialogUI tableData = new TableDialogUI(ALLMap, "WRONG_CELL_DETAILS");
                tableData.setTitle("Details of Excel Sheet Data Errors ");
                tableData.show();
//                btnCancelActionPerformed(null);
                return;
            }
            tableDataAlignment(totFileDataList);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getDataFromAccessDB error : " + e);
        }
    }

    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod <= (roundingFactor / 2)) || (mod <= (roundingFactorOdd / 2))) {
            return lower(number, roundingFactor);
        } else {
            return higher(number, roundingFactor);
        }
    }

    public long lower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public long higher(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        if (mod == 0) {
            return number;
        }
        return (number - mod) + roundingFactor;
    }

    public long roundOffLower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public String getCellValue(HSSFCell cell) {
        String rowFields = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case 0:
                    rowFields += cell.getNumericCellValue();
                    break;
                case 1:
                    rowFields += cell.getStringCellValue();
                    break;
            }
        }
        return rowFields;
    }

    private void tableDataAlignment(List totFileDataList) {
        ArrayList _heading = new ArrayList();
        if (totFileDataList != null && totFileDataList.size() > 0) {
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(tblFileDataDetails);
            TableModel tableModel = new TableModel();
            _heading.add("CUST_NAME");
            _heading.add("DOB");
            _heading.add("MOTHER_NAME");
            _heading.add("AMOUNT");
            _heading.add("DEPOSIT_DT");
            _heading.add("MATURITY_DT");
            _heading.add("ADDR1");
            _heading.add("ADDR2");
            _heading.add("ADDR3");
            _heading.add("DISTRICT");
            _heading.add("PINCODE");
            _heading.add("REF_NO");
            _heading.add("ROI");
            _heading.add("INTEREST");
            _heading.add("MATURITY_AMT");
            tableModel.setHeading(_heading);
            tableModel.setData((ArrayList) totFileDataList);
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            tblFileDataDetails.setAutoResizeMode(0);
            tblFileDataDetails.doLayout();
            tblFileDataDetails.setModel(tableSorter);
            tblFileDataDetails.revalidate();
        }
    }

    private void setSizeTableData() {
        tblFileDataDetails.getColumnModel().getColumn(0).setPreferredWidth(220);
        tblFileDataDetails.getColumnModel().getColumn(1).setPreferredWidth(80);
        tblFileDataDetails.getColumnModel().getColumn(2).setPreferredWidth(180);
        tblFileDataDetails.getColumnModel().getColumn(3).setPreferredWidth(110);
        tblFileDataDetails.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblFileDataDetails.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblFileDataDetails.getColumnModel().getColumn(6).setPreferredWidth(250);
        tblFileDataDetails.getColumnModel().getColumn(7).setPreferredWidth(200);
    }

    private void tableDataAlignmentEdit(List totFileDataList) {
        ArrayList _heading = new ArrayList();
        if (totFileDataList != null && totFileDataList.size() > 0) {
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(tblFileDataDetails);
            TableModel tableModel = new TableModel();
            _heading.add("CUST_ID");
            _heading.add("DEPOSIT_NO");
            _heading.add("CUST_NAME");
            _heading.add("DEPOSIT_DT");
            _heading.add("MATURITY_DT");
            _heading.add("AMOUNT");
            _heading.add("ROI");
            _heading.add("INTEREST");
            _heading.add("MATURITY_AMT");
            _heading.add("DOB");
            _heading.add("MOTHER_NAME");
            _heading.add("ADDR1");
            _heading.add("ADDR2");
            _heading.add("ADDR3");
            _heading.add("DISTRICT");
            _heading.add("PINCODE");
            _heading.add("REF_NO");
            tableModel.setHeading(_heading);
            tableModel.setData((ArrayList) totFileDataList);
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            tblFileDataDetails.setAutoResizeMode(0);
            tblFileDataDetails.doLayout();
            tblFileDataDetails.setModel(tableSorter);
            tblFileDataDetails.revalidate();
        }
    }

    private void setSizeTableDataEdit() {
        tblFileDataDetails.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblFileDataDetails.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblFileDataDetails.getColumnModel().getColumn(2).setPreferredWidth(220);
        tblFileDataDetails.getColumnModel().getColumn(3).setPreferredWidth(90);
        tblFileDataDetails.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblFileDataDetails.getColumnModel().getColumn(5).setPreferredWidth(80);
        tblFileDataDetails.getColumnModel().getColumn(6).setPreferredWidth(80);
        tblFileDataDetails.getColumnModel().getColumn(7).setPreferredWidth(120);
    }

    private void savePerformed() {
        //Progrees bar added by Suresh R
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws InterruptedException {
                try {
                    observable.doAction(totFileDataList);
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
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    if (observable.getProxyReturnMap().containsKey("BULK_ID")) {
                        ClientUtil.showMessageWindow("Bulk ID : " + CommonUtil.convertObjToStr(observable.getProxyReturnMap().get("BULK_ID")));
                        callTemplate(CommonUtil.convertObjToStr(observable.getProxyReturnMap().get("BULK_ID")));
                    }
                }
            }
//            btnCancelActionPerformed(null);
//            btnCancel.setEnabled(true);
            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
        }
//        btnCancel.setEnabled(true);
//        btnAuthorize.setEnabled(true);
//        btnReject.setEnabled(true);
//        btnException.setEnabled(true);
        setModified(false);
        ClientUtil.clearAll(this);
    }

    private void callTemplate(String batchID) {
        TTIntegration ttIntgration = null;
        HashMap paramMap = new HashMap();
        paramMap.put("USER_ID", TrueTransactMain.USER_ID);
        paramMap.put("BranchId", TrueTransactMain.BRANCH_ID);
        paramMap.put("BatchId", batchID);
        paramMap.put("TransDt", currDate);
        ttIntgration.setParam(paramMap);
        ttIntgration.integrationForPrint("BatchPrint", true); //Changed By Suresh R
    }
    private void btnReconciliationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReconciliationActionPerformed
        // TODO add your handling code here:
        double unMatchHDFCAmount = CommonUtil.convertObjToDouble(lblAutoAmount1.getText());
        double unMatchNEFTAmount = CommonUtil.convertObjToDouble(lblAutoAmount.getText());
        if (unMatchHDFCAmount != unMatchNEFTAmount) {
            ClientUtil.showAlertWindow("Please select the exact match amount");
            return;
        } else {
            Date initiatedTransDt = null;
            String ourUTRNumber = "";
            if (observable.getElectronicManualMatchOursList().size() > 0) {
                for (int i = 0; i < tblOursTransactionList.getRowCount(); i++) {
                    if (CommonUtil.convertObjToStr(tblOursTransactionList.getValueAt(i, 0)).equals("true")) {
                        ourUTRNumber = CommonUtil.convertObjToStr(tblOursTransactionList.getValueAt(tblOursTransactionList.getSelectedRow(), 2));
                        initiatedTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblOursTransactionList.getValueAt(tblOursTransactionList.getSelectedRow(), 1)));
                    }
                }
            }
            String hdfcUTRNumber = "";
            Date paymentTransDt = null;
//            tblTheirsTransactionList.setValueAt(new Boolean(true), tblTheirsTransactionList.getSelectedRow(), 0);
            if (observable.getElectronicManualMatchTheirsList().size() > 0) {
                for (int i = 0; i < tblTheirsTransactionList.getRowCount(); i++) {
                    if (CommonUtil.convertObjToStr(tblTheirsTransactionList.getValueAt(i, 0)).equals("true")) {
                        hdfcUTRNumber = CommonUtil.convertObjToStr(tblTheirsTransactionList.getValueAt(tblTheirsTransactionList.getSelectedRow(), 2));
                        paymentTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblTheirsTransactionList.getValueAt(tblTheirsTransactionList.getSelectedRow(), 1)));
                    }
                }
            }
            if (paymentTransDt != null && initiatedTransDt != null && ourUTRNumber != null && hdfcUTRNumber != null) {
                observable.updateMatausStausRecord(hdfcUTRNumber, ourUTRNumber, paymentTransDt, initiatedTransDt);
                btnManualMatchClearActionPerformed(null);
                lblAutoAmount1.setText("");
                lblAutoAmount.setText("");
                lblAutoCount1.setText("");
                lblAutoCount.setText("");
            } else {
                ClientUtil.showAlertWindow("Please select the proper data ");
                return;
            }
            System.out.println("hdfcUTRNumber : " + hdfcUTRNumber + " ourUTRNumber : " + ourUTRNumber);
        }
    }//GEN-LAST:event_btnReconciliationActionPerformed
    public Date getProperDateFormat(Object obj) {
        Date currDt = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDt = (Date) currDate.clone();
            currDt.setDate(tempDt.getDate());
            currDt.setMonth(tempDt.getMonth());
            currDt.setYear(tempDt.getYear());
        }
        return currDt;
    }
    private void tblOursTransactionListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOursTransactionListMouseClicked
        // TODO add your handling code here:
        if (tblOursTransactionList.getSelectedColumn() == 0 && evt.getClickCount() == 1) {
            String st = CommonUtil.convertObjToStr(tblOursTransactionList.getValueAt(tblOursTransactionList.getSelectedRow(), 0));
            if (st.equals("true")) {
                tblOursTransactionList.setValueAt(new Boolean(false), tblOursTransactionList.getSelectedRow(), 0);
            } else {
                tblOursTransactionList.setValueAt(new Boolean(true), tblOursTransactionList.getSelectedRow(), 0);
                int count = 0;
                double autoAmt = 0;
                for (int i = 0; i < tblOursTransactionList.getRowCount(); i++) {
                    if (CommonUtil.convertObjToStr(tblOursTransactionList.getValueAt(i, 0)).equals("true")) {
                        count++;
                        autoAmt = autoAmt + CommonUtil.convertObjToDouble(tblOursTransactionList.getValueAt(tblOursTransactionList.getSelectedRow(), 4));
                    }
                }
                lblAutoCount.setText(CommonUtil.convertObjToStr(count));
                lblAutoAmount.setText(CommonUtil.convertObjToStr(autoAmt));
                if (count > 1) {
                    ClientUtil.showAlertWindow("Multiple Selection Not Allowed!!!");
                    tblOursTransactionList.setValueAt(new Boolean(false), tblOursTransactionList.getSelectedRow(), 0);
                    return;
                }
                if (observable.ourManualReconList != null && observable.ourManualReconList.size() > 0) {
                }
            }
        }
    }//GEN-LAST:event_tblOursTransactionListMouseClicked

    private void tblTheirsTransactionListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTheirsTransactionListMouseClicked
        // TODO add your handling code here:
        if (tblTheirsTransactionList.getRowCount() > 0 && evt.getClickCount() == 1) {
            String st = CommonUtil.convertObjToStr(tblTheirsTransactionList.getValueAt(tblTheirsTransactionList.getSelectedRow(), 0));
            int count = 0;
            if (st.equals("true")) {
                tblTheirsTransactionList.setValueAt(new Boolean(false), tblTheirsTransactionList.getSelectedRow(), 0);
            } else {
                lblAutoCount1.setText("");
                lblAutoAmount1.setText("");
                double autoAmt = 0;
                tblTheirsTransactionList.setValueAt(new Boolean(true), tblTheirsTransactionList.getSelectedRow(), 0);
                if (observable.getElectronicManualMatchTheirsList().size() > 0) {
                    for (int i = 0; i < tblTheirsTransactionList.getRowCount(); i++) {
                        if (CommonUtil.convertObjToStr(tblTheirsTransactionList.getValueAt(i, 0)).equals("true")) {
                            count++;
                            autoAmt = autoAmt + CommonUtil.convertObjToDouble(tblTheirsTransactionList.getValueAt(tblTheirsTransactionList.getSelectedRow(), 4));
                        }
                    }
                    lblAutoCount1.setText(CommonUtil.convertObjToStr(count));
                    lblAutoAmount1.setText(CommonUtil.convertObjToStr(autoAmt));
                    if (count > 1) {
                        ClientUtil.showAlertWindow("Multiple Selection Not Allowed!!!");
                        tblTheirsTransactionList.setValueAt(new Boolean(false), tblTheirsTransactionList.getSelectedRow(), 0);
                        return;
                    }
                }
            }
        }
    }//GEN-LAST:event_tblTheirsTransactionListMouseClicked

    private void tdtFromDateManualReconFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDateManualReconFocusLost
        // TODO add your handling code here:
//        if ((tdtFromDateManualRecon != null && tdtFromDateManualRecon.getDateValue().length() > 0)) {
//            if (DateUtil.getDateMMDDYYYY(tdtFromDateManualRecon.getDateValue()).compareTo(((Date) currDate.clone())) > 0) {
//                ClientUtil.showAlertWindow("FromDate Should Not Be Greater than Current Date !!!");
//                tdtFromDateManualRecon.setDateValue("");
//            }
//        }
    }//GEN-LAST:event_tdtFromDateManualReconFocusLost

    private void btnDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisplayActionPerformed
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws InterruptedException {
                try {

                    String reconCategory = CommonUtil.convertObjToStr(cboReconTypeManual.getSelectedItem());
                    if (DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(tdtFromDateManualRecon.getDateValue()), DateUtil.getDateMMDDYYYY(tdtToDateManualRecon.getDateValue())) < 0) {
                        ClientUtil.showAlertWindow("To Date Should Not Be Greater than From Date !!!");
//                        tdtToDateManualRecon.setDateValue("");
                    } else if (CommonUtil.convertObjToStr(reconCategory).equals("")) {
                        ClientUtil.showAlertWindow("Please select the Recon Type");
                    } else {
                        lblUnMatchTheirsCountValue.setText("");
                        lblUnMatchTheirsAmountValue.setText("");
                        lblUnMatchOursCountValue.setText("");
                        lblUnMatchOursAmountValue.setText("");
                        lblAutoAmount1.setText("");
                        lblAutoAmount.setText("");
                        lblAutoCount1.setText("");
                        lblAutoCount.setText("");
                        observable.resetManualMatchTableValues();
                        tblOursTransactionList.setModel(observable.getElectronicManualMatchOursTable());
                        tableDataAlignmentOurs();
                        setSizeTableDataOurs();
                        tblTheirsTransactionList.setModel(observable.getElectronicManualMatchTheirsTable());
                        tableDataAlignmentTheirs();
                        setSizeTableDataTheirs();
                        String electronicType = CommonUtil.convertObjToStr(cboUnMatchStatusType.getSelectedItem());
                        HashMap hash = new HashMap();
                        hash.put("FROM_DATE", getProperDateFormat(DateUtil.getDateMMDDYYYY(tdtFromDateManualRecon.getDateValue())));
                        hash.put("TO_DATE", getProperDateFormat(DateUtil.getDateMMDDYYYY(tdtToDateManualRecon.getDateValue())));
                        if (electronicType.length() > 0) {
                            hash.put("TYPE_OF_RECON", CommonUtil.convertObjToStr(electronicType));
                        }
                        hash.put("CLASIFIED_CAT", CommonUtil.convertObjToStr(reconCategory));
                        boolean recordExist = observable.populateManualMatchTableData(hash);
                        if (recordExist) {
                            tblOursTransactionList.setModel(observable.getElectronicManualMatchOursTable());
                            tableDataAlignmentOurs();
                            setSizeTableDataOurs();
                            tblTheirsTransactionList.setModel(observable.getElectronicManualMatchTheirsTable());
                            tableDataAlignmentTheirs();
                            setSizeTableDataTheirs();
                            javax.swing.table.DefaultTableCellRenderer r = new javax.swing.table.DefaultTableCellRenderer();
                            r.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                            tblOursTransactionList.getColumnModel().getColumn(4).setCellRenderer(r);
                            tblOursTransactionList.getColumnModel().getColumn(4).sizeWidthToFit();
                            tblTheirsTransactionList.getColumnModel().getColumn(4).setCellRenderer(r);
                            tblTheirsTransactionList.getColumnModel().getColumn(4).sizeWidthToFit();
                            lblUnMatchTheirsCountValue.setText(CommonUtil.convertObjToStr(observable.unMatchTheirsCount));
                            lblUnMatchTheirsAmountValue.setText(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(observable.unMatchTheirsAmount)));
                            lblUnMatchOursCountValue.setText(CommonUtil.convertObjToStr(observable.unMatchOursCount));
                            lblUnMatchOursAmountValue.setText(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(observable.unMatchOursAmount)));
                        } else {
                            ClientUtil.showAlertWindow("Record does not exist for seleted dates");
                            btnManualMatchClearActionPerformed(null);
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
    }//GEN-LAST:event_btnDisplayActionPerformed

    private void btnManualMatchClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManualMatchClearActionPerformed
        // TODO add your handling code here:
        observable.resetManualMatchTableValues();
        tblOursTransactionList.setModel(observable.getElectronicManualMatchOursTable());
        tableDataAlignmentOurs();
        setSizeTableDataOurs();
        tblTheirsTransactionList.setModel(observable.getElectronicManualMatchTheirsTable());
        tableDataAlignmentTheirs();
        setSizeTableDataTheirs();
        lblUnMatchTheirsCountValue.setText("");
        lblUnMatchTheirsAmountValue.setText("");
        lblUnMatchOursCountValue.setText("");
        lblUnMatchOursAmountValue.setText("");
//        tdtToDateManualRecon.setDateValue("");
//        cboUnMatchStatusType.setSelectedItem("");
//        cboReconTypeManual.setSelectedItem("");
        tdtFromDateManualRecon.setEnabled(true);
        tdtToDateManualRecon.setEnabled(true);
        cboUnMatchStatusType.setEnabled(true);
//        tdtFromDateManualRecon.setDateValue(CommonUtil.convertObjToStr(DateUtil.addDays(currDate, -2)));
//        tdtToDateManualRecon.setDateValue(CommonUtil.convertObjToStr(currDate));

    }//GEN-LAST:event_btnManualMatchClearActionPerformed
    private void tableDataAlignmentOurs() {
        ArrayList _heading = new ArrayList();
        if (observable.getElectronicManualMatchOursList() != null && observable.getElectronicManualMatchOursList().size() > 0) {
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(tblOursTransactionList);
            com.see.truetransact.clientutil.TableModel tableModel = new com.see.truetransact.clientutil.TableModel();
            _heading.add("Select");
            _heading.add("Payment Dt");
            _heading.add("Reference No");
            _heading.add("Description");
            _heading.add("Amount");
            _heading.add("Recon ID");
            _heading.add("Branch");
            tableModel.setHeading(_heading);
            tableModel.setData((ArrayList) observable.getElectronicManualMatchOursList());
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            tblOursTransactionList.setAutoResizeMode(0);
            tblOursTransactionList.doLayout();
            tblOursTransactionList.setModel(tableSorter);
            tblOursTransactionList.revalidate();
        }
    }

    private void setSizeTableDataOurs() {
        tblOursTransactionList.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblOursTransactionList.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblOursTransactionList.getColumnModel().getColumn(2).setPreferredWidth(140);
        tblOursTransactionList.getColumnModel().getColumn(3).setPreferredWidth(350);
        tblOursTransactionList.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblOursTransactionList.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblOursTransactionList.getColumnModel().getColumn(6).setPreferredWidth(100);
    }
    private void btnMatchStatusClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMatchStatusClearActionPerformed
        // TODO add your handling code here:
        observable.resetMatchStatusTableValues();
        tblMatchStatusInquirytLst.setModel(observable.getElectronicMatchStatusTable());
        tableDataAlignmentMatchStatusRecon();
        setSizeTableDataMatchStatusRecon();
        lblMatchCountValue.setText("");
        lblMatchAmountValue.setText("");
//        cboReconTypeMatch.setSelectedItem("");
//        cboMatchStatusType.setSelectedItem("");
//        tdtMatchStatusToDt.setDateValue("");
//        tdtMatchStatusFromDt.setDateValue("");
//        tdtMatchStatusFromDt.setDateValue(CommonUtil.convertObjToStr(DateUtil.addDays(currDate, -2)));
//        tdtMatchStatusToDt.setDateValue(CommonUtil.convertObjToStr(currDate));

    }//GEN-LAST:event_btnMatchStatusClearActionPerformed
    private void tableDataAlignmentTheirs() {
        ArrayList _heading = new ArrayList();
        if (observable.getElectronicManualMatchTheirsList() != null && observable.getElectronicManualMatchTheirsList().size() > 0) {
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(tblTheirsTransactionList);
            com.see.truetransact.clientutil.TableModel tableModel = new com.see.truetransact.clientutil.TableModel();
            _heading.add("Select");
            _heading.add("Payment Dt");
            _heading.add("Bank Reference NO");
            _heading.add("Description");
            _heading.add("Amount");
            _heading.add("Recon ID");
            _heading.add("Branch");
            tableModel.setHeading(_heading);
            tableModel.setData((ArrayList) observable.getElectronicManualMatchTheirsList());
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            tblTheirsTransactionList.setAutoResizeMode(0);
            tblTheirsTransactionList.doLayout();
            tblTheirsTransactionList.setModel(tableSorter);
            tblTheirsTransactionList.revalidate();
        }
    }

    private void setSizeTableDataTheirs() {
        tblTheirsTransactionList.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblTheirsTransactionList.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblTheirsTransactionList.getColumnModel().getColumn(2).setPreferredWidth(140);
        tblTheirsTransactionList.getColumnModel().getColumn(3).setPreferredWidth(350);
        tblTheirsTransactionList.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblTheirsTransactionList.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblTheirsTransactionList.getColumnModel().getColumn(5).setPreferredWidth(100);
    }
    private void cboMatchStatusTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMatchStatusTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboMatchStatusTypeActionPerformed
    private void resetMatchStatusUI() {
        observable.resetMatchStatusTableValues();
        lblMatchCountValue.setText("");
        lblMatchAmountValue.setText("");
        tdtMatchStatusFromDt.setDateValue("");
        tdtMatchStatusToDt.setDateValue("");
        cboMatchStatusType.setSelectedItem("");
        cboReconTypeMatch.setSelectedItem("");
        tblMatchStatusInquirytLst.setModel(observable.getElectronicMatchStatusTable());
        tableDataAlignmentMatchStatusRecon();
        setSizeTableDataMatchStatusRecon();
        tdtMatchStatusFromDt.setDateValue(CommonUtil.convertObjToStr(DateUtil.addDays(currDate, -2)));
        tdtMatchStatusToDt.setDateValue(CommonUtil.convertObjToStr(currDate));
    }
    private void btnMatchStatusInquiryProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMatchStatusInquiryProcessActionPerformed
        // TODO add your handling code here:
//        resetReconUI();
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws InterruptedException {
                try {
                    String electronicSourceType = CommonUtil.convertObjToStr(cboMatchStatusType.getSelectedItem());
                    String reconCategory = CommonUtil.convertObjToStr(cboReconTypeMatch.getSelectedItem());
                    String matchTransType = CommonUtil.convertObjToStr(cboMatchTransType.getSelectedItem());
                    System.out.println("electronicSourceType : " + electronicSourceType);
                    if (tdtMatchStatusFromDt.getDateValue().equals("") || tdtMatchStatusFromDt.getDateValue().equals("")) {
                        tdtMatchStatusFromDt.setDateValue(CommonUtil.convertObjToStr(currDate));
                        tdtMatchStatusToDt.setDateValue(CommonUtil.convertObjToStr(currDate));
                    }
                    if (DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(tdtMatchStatusFromDt.getDateValue()), DateUtil.getDateMMDDYYYY(tdtMatchStatusToDt.getDateValue())) < 0) {
                        ClientUtil.showAlertWindow("To Date Should Not Be Greater than From Date !!!");
//                        tdtMatchStatusFromDt.setDateValue("");
//                        tdtMatchStatusToDt.setDateValue("");
                    } else if (CommonUtil.convertObjToStr(electronicSourceType).equals("")) {
                        ClientUtil.showAlertWindow("Please Select Type of the Transaction");
                    } else if (CommonUtil.convertObjToStr(electronicSourceType).equals("Processing Bank Entries")
                            && CommonUtil.convertObjToStr(reconCategory).equals("")) {
                        ClientUtil.showAlertWindow("Please Select The Recon Type");
                    } else {
                        lblMatchCountValue.setText("");
                        lblMatchAmountValue.setText("");
                        observable.resetMatchStatusTableValues();
                        tblMatchStatusInquirytLst.setModel(observable.getElectronicMatchStatusTable());
                        tableDataAlignmentMatchStatusRecon();
                        setSizeTableDataMatchStatusRecon();
                        HashMap hash = new HashMap();
                        hash.put("FROM_DATE", getProperDateFormat(DateUtil.getDateMMDDYYYY(tdtMatchStatusFromDt.getDateValue())));
                        hash.put("TO_DATE", getProperDateFormat(DateUtil.getDateMMDDYYYY(tdtMatchStatusToDt.getDateValue())));
                        hash.put("TYPE_OF_RECON", CommonUtil.convertObjToStr(electronicSourceType));
                        hash.put("CLASIFIED_CAT", CommonUtil.convertObjToStr(reconCategory));
                        if (CommonUtil.convertObjToStr(txtMatchActNum.getText()).length() > 0) {
                            hash.put("ACCT_NUM", CommonUtil.convertObjToStr(txtMatchActNum.getText()));
                        }
                        if (CommonUtil.convertObjToStr(txtMatchAmt.getText()).length() > 0) {
                            hash.put("REJECT_AMOUNT", CommonUtil.convertObjToStr(txtMatchAmt.getText()));
                        }
                        if (CommonUtil.convertObjToStr(txtMatchCustName.getText()).length() > 0) {
                            hash.put("CUST_NAME", CommonUtil.convertObjToStr(txtMatchCustName.getText()));
                        }
                        if (CommonUtil.convertObjToStr(matchTransType).length() > 0 && CommonUtil.convertObjToStr(matchTransType).equals("Debit")) {
                            hash.put("REJECT_AMOUNT_DEBIT", "REJECT_AMOUNT_DEBIT");
                        }
                        if (CommonUtil.convertObjToStr(matchTransType).length() > 0 && CommonUtil.convertObjToStr(matchTransType).equals("Credit")) {
                            hash.put("REJECT_AMOUNT_CREDIT", "REJECT_AMOUNT_CREDIT");
                        }
                        boolean recordExist = observable.populateMatchStatusTableData(hash);
                        if (recordExist) {
                            lblMatchCountValue.setText("");
                            lblMatchAmountValue.setText("");
                            tblMatchStatusInquirytLst.setModel(observable.getElectronicMatchStatusTable());
                            tableDataAlignmentMatchStatusRecon();
                            setSizeTableDataMatchStatusRecon();
                            javax.swing.table.DefaultTableCellRenderer r = new javax.swing.table.DefaultTableCellRenderer();
                            r.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                            tblMatchStatusInquirytLst.getColumnModel().getColumn(3).setCellRenderer(r);
                            tblMatchStatusInquirytLst.getColumnModel().getColumn(3).sizeWidthToFit();
                            tblMatchStatusInquirytLst.getColumnModel().getColumn(4).setCellRenderer(r);
                            tblMatchStatusInquirytLst.getColumnModel().getColumn(4).sizeWidthToFit();
                            lblMatchCountValue.setText(CommonUtil.convertObjToStr(observable.matchCount));
                            lblMatchAmountValue.setText(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(observable.matchAmount)));
                        } else {
                            ClientUtil.showAlertWindow("Record does not exist for seleted dates");
                            btnMatchStatusClearActionPerformed(null);
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
    }//GEN-LAST:event_btnMatchStatusInquiryProcessActionPerformed
    private void tableDataAlignmentMatchStatusRecon() {
        ArrayList _heading = new ArrayList();
        if (observable.getElectronicMatchStatusList() != null && observable.getElectronicMatchStatusList().size() > 0) {
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(tblMatchStatusInquirytLst);
            com.see.truetransact.clientutil.TableModel tableModel = new com.see.truetransact.clientutil.TableModel();
            _heading.add("Payment Dt");
            _heading.add("Description");
            _heading.add("Reference No");
            _heading.add("Debits");
            _heading.add("Credits");
            _heading.add("IFS Code");
            _heading.add("Bank Act Num");
            _heading.add("Cust Name");
            _heading.add("Recon ID");
            _heading.add("Branch");
            tableModel.setHeading(_heading);
            tableModel.setData((ArrayList) observable.getElectronicMatchStatusList());
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            tblMatchStatusInquirytLst.setAutoResizeMode(0);
            tblMatchStatusInquirytLst.doLayout();
            tblMatchStatusInquirytLst.setModel(tableSorter);
            tblMatchStatusInquirytLst.revalidate();
        }
    }

    private void setSizeTableDataMatchStatusRecon() {
        tblMatchStatusInquirytLst.getColumnModel().getColumn(0).setPreferredWidth(85);
        tblMatchStatusInquirytLst.getColumnModel().getColumn(1).setPreferredWidth(350);
        tblMatchStatusInquirytLst.getColumnModel().getColumn(2).setPreferredWidth(140);
        tblMatchStatusInquirytLst.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblMatchStatusInquirytLst.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblMatchStatusInquirytLst.getColumnModel().getColumn(5).setPreferredWidth(125);
        tblMatchStatusInquirytLst.getColumnModel().getColumn(6).setPreferredWidth(125);
        tblMatchStatusInquirytLst.getColumnModel().getColumn(7).setPreferredWidth(250);
        tblMatchStatusInquirytLst.getColumnModel().getColumn(8).setPreferredWidth(125);
        tblMatchStatusInquirytLst.getColumnModel().getColumn(9).setPreferredWidth(65);
    }
    private void tblMatchStatusInquirytLstMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMatchStatusInquirytLstMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblMatchStatusInquirytLstMouseClicked

    private void cboUnMatchStatusTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboUnMatchStatusTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboUnMatchStatusTypeActionPerformed

    private void tdtToDateManualReconFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToDateManualReconFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtToDateManualReconFocusLost

    private void cboFileUploadTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFileUploadTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboFileUploadTypeActionPerformed

    private void cboReconTypeMatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboReconTypeMatchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboReconTypeMatchActionPerformed
    private void tableDataAlignmentProcessedRecon() {
        ArrayList _heading = new ArrayList();
        if (observable.getTableReconList() != null && observable.getTableReconList().size() > 0) {
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(tblReconInquirytLst);
            com.see.truetransact.clientutil.TableModel tableModel = new com.see.truetransact.clientutil.TableModel();
            _heading.add("Payment Dt");
            _heading.add("Description");
            _heading.add("Reference No");
            _heading.add("Debits");
            _heading.add("Credits");
            _heading.add("Recon ID");
            _heading.add("Branch");
            tableModel.setHeading(_heading);
            tableModel.setData((ArrayList) observable.getTableReconList());
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            tblReconInquirytLst.setAutoResizeMode(0);
            tblReconInquirytLst.doLayout();
            tblReconInquirytLst.setModel(tableSorter);
            tblReconInquirytLst.revalidate();
        }
    }

    private void setSizeTableDataProcessedRecon() {
        tblReconInquirytLst.getColumnModel().getColumn(0).setPreferredWidth(85);
        tblReconInquirytLst.getColumnModel().getColumn(1).setPreferredWidth(350);
        tblReconInquirytLst.getColumnModel().getColumn(2).setPreferredWidth(140);
        tblReconInquirytLst.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblReconInquirytLst.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblReconInquirytLst.getColumnModel().getColumn(5).setPreferredWidth(65);
        tblReconInquirytLst.getColumnModel().getColumn(6).setPreferredWidth(125);
    }
    private void cboReconTypeManualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboReconTypeManualActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboReconTypeManualActionPerformed
//    private void resetMatchStatusReconUI() {
//        observable.resetReconTableValues();
//        tblReconInquirytLst.setModel(observable.getElectrionicReconTable());
//        tableDataAlignmentMatchStatusRecon();
//        setSizeTableDataMatchStatusRecon();
//        lblBankTxnCountValue.setText("");
//        lblBankTxnAmountValue.setText("");
//    }
    private void btnReconClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReconClearActionPerformed
        // TODO add your handling code here:
        observable.resetReconTableValues();
        tblReconInquirytLst.setModel(observable.getElectrionicReconTable());
        tableDataAlignmentProcessedRecon();
        setSizeTableDataProcessedRecon();
        tdtReconToDt.setEnabled(true);
        cboReconClassifiedCategory.setEnabled(true);
        lblBankTxnAmountValue.setText("");
        lblBankTxnCountValue.setText("");
//        tdtReconFromDt.setDateValue("");
//        tdtReconToDt.setDateValue("");
//        cboReconSourceType.setSelectedItem("");
//        cboReconClassifiedCategory.setSelectedItem("");
//        tdtReconFromDt.setDateValue(CommonUtil.convertObjToStr(DateUtil.addDays(currDate, -2)));
//        tdtReconToDt.setDateValue(CommonUtil.convertObjToStr(currDate));
    }//GEN-LAST:event_btnReconClearActionPerformed

    private void cboReconSourceTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboReconSourceTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboReconSourceTypeActionPerformed

    private void btnReconInquiryProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReconInquiryProcessActionPerformed
        // TODO add your handling code here:
//        resetMatchStatusReconUI();
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws InterruptedException {
                try {
                    String electronicSourceType = CommonUtil.convertObjToStr(cboReconSourceType.getSelectedItem());
                    String electronicClassifiedCategory = CommonUtil.convertObjToStr(cboReconClassifiedCategory.getSelectedItem());
                    String reconTransType = CommonUtil.convertObjToStr(cboReconTransType.getSelectedItem());
                    System.out.println("electronicType : " + electronicClassifiedCategory);
                    if (tdtReconFromDt.getDateValue().equals("") || tdtReconFromDt.getDateValue().equals("")) {
                        tdtReconFromDt.setDateValue(CommonUtil.convertObjToStr(currDate));
                        tdtReconFromDt.setDateValue(CommonUtil.convertObjToStr(currDate));
                    }
                    if (DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(tdtReconFromDt.getDateValue()), DateUtil.getDateMMDDYYYY(tdtReconFromDt.getDateValue())) < 0) {
                        ClientUtil.showAlertWindow("To Date Should Not Be Greater than From Date !!!");
//                        tdtReconFromDt.setDateValue("");
//                        tdtReconFromDt.setDateValue("");
                    } else if (CommonUtil.convertObjToStr(electronicSourceType).equals("")) {
                        ClientUtil.showAlertWindow("Please Select Type of the Transaction");
                    } else if (CommonUtil.convertObjToStr(electronicSourceType).equals("Processing Bank Entries")
                            && CommonUtil.convertObjToStr(electronicClassifiedCategory).equals("")) {
                        ClientUtil.showAlertWindow("Please Select The Recon Type");
                    } else {
                        observable.resetReconTableValues();
                        tblReconInquirytLst.setModel(observable.getElectrionicReconTable());
                        tableDataAlignmentProcessedRecon();
                        setSizeTableDataProcessedRecon();
                        lblBankTxnAmountValue.setText("");
                        lblBankTxnCountValue.setText("");
                        HashMap hash = new HashMap();
                        hash.put("FROM_DATE", getProperDateFormat(DateUtil.getDateMMDDYYYY(tdtReconFromDt.getDateValue())));
                        hash.put("TO_DATE", getProperDateFormat(DateUtil.getDateMMDDYYYY(tdtReconToDt.getDateValue())));
                        hash.put("CLASIFIED_CAT", CommonUtil.convertObjToStr(electronicClassifiedCategory));
                        hash.put("SOURCE", CommonUtil.convertObjToStr(electronicSourceType));
                        if (CommonUtil.convertObjToStr(txtPBTActNum.getText()).length() > 0) {
                            hash.put("ACCT_NUM", CommonUtil.convertObjToStr(txtPBTActNum.getText()));
                        }
                        if (CommonUtil.convertObjToStr(txtPPTCustName.getText()).length() > 0) {
                            hash.put("CUST_NAME", CommonUtil.convertObjToStr(txtPPTCustName.getText()));
                        }
                        if (CommonUtil.convertObjToStr(txtPPTAmt.getText()).length() > 0) {
                            hash.put("REJECT_AMOUNT", CommonUtil.convertObjToStr(txtPPTAmt.getText()));
                        }
                        if (CommonUtil.convertObjToStr(reconTransType).length() > 0 && CommonUtil.convertObjToStr(reconTransType).equals("Debit")) {
                            hash.put("REJECT_AMOUNT_DEBIT", "REJECT_AMOUNT_DEBIT");
                        }
                        if (CommonUtil.convertObjToStr(reconTransType).length() > 0 && CommonUtil.convertObjToStr(reconTransType).equals("Credit")) {
                            hash.put("REJECT_AMOUNT_CREDIT", "REJECT_AMOUNT_CREDIT");
                        }
                        boolean recordExist = observable.populateReconTableData(hash);
                        if (recordExist) {
                            tblReconInquirytLst.setModel(observable.getElectrionicReconTable());
                            tableDataAlignmentProcessedRecon();
                            setSizeTableDataProcessedRecon();
                            javax.swing.table.DefaultTableCellRenderer r = new javax.swing.table.DefaultTableCellRenderer();
                            r.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                            tblReconInquirytLst.getColumnModel().getColumn(3).setCellRenderer(r);
                            tblReconInquirytLst.getColumnModel().getColumn(3).sizeWidthToFit();
                            tblReconInquirytLst.getColumnModel().getColumn(4).setCellRenderer(r);
                            tblReconInquirytLst.getColumnModel().getColumn(4).sizeWidthToFit();
                            lblBankTxnCountValue.setText(CommonUtil.convertObjToStr(observable.lblBankTxnCountValue));
                            lblBankTxnAmountValue.setText(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(observable.lblBankTxnAmountValue)));
                        } else {
                            ClientUtil.showAlertWindow("Record does not exist for seleted dates");
//                            tblReconInquirytLst.setModel(observable.getElectrionicReconTable());
                            btnReconClearActionPerformed(null);
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
    }//GEN-LAST:event_btnReconInquiryProcessActionPerformed

    private void cboReconClassifiedCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboReconClassifiedCategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboReconClassifiedCategoryActionPerformed

    private void tblReconInquirytLstMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblReconInquirytLstMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblReconInquirytLstMouseClicked

    private void btnReconRejectClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReconRejectClearActionPerformed
        // TODO add your handling code here:
        observable.resetReconRejectTableValues();
        tblReconRejectInquirytLst.setModel(observable.getElectrionicReconRejectTable());
        tableDataAlignmentReconReject();
        setSizeTableDataReconReject();
        lblRejectAmountValue.setText("");
        lblRejectCountValue.setText("");
//        tdtReconRejectFromDt.setDateValue("");
//        tdtReconRejectToDt.setDateValue("");
//        cboSourceRejectType.setSelectedItem("");
//        cboReconRejectType.setSelectedItem("");
        cboReconRejectType.setEnabled(true);
        tdtReconRejectToDt.setEnabled(true);
//        tdtReconRejectFromDt.setDateValue(CommonUtil.convertObjToStr(DateUtil.addDays(currDate, -2)));
//        tdtReconRejectToDt.setDateValue(CommonUtil.convertObjToStr(currDate));
//        cboSourceRejectType.setSelectedItem("Processing Bank Entries");
//        cboReconRejectType.setSelectedItem("NEFT");

    }//GEN-LAST:event_btnReconRejectClearActionPerformed

    private void cboSourceRejectTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSourceRejectTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboSourceRejectTypeActionPerformed

    private void btnReconRejectInquiryProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReconRejectInquiryProcessActionPerformed
        // TODO add your handling code here:
        resetReconRejectUI();
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws InterruptedException {
                try {
                    String electronicSourceType = CommonUtil.convertObjToStr(cboSourceRejectType.getSelectedItem());
                    String rejectReconType = CommonUtil.convertObjToStr(cboReconRejectType.getSelectedItem());
                    System.out.println("electronicType : " + electronicSourceType);
                    if (tdtReconRejectFromDt.getDateValue().equals("") || tdtReconRejectToDt.getDateValue().equals("")) {
                        tdtReconRejectFromDt.setDateValue(CommonUtil.convertObjToStr(currDate));
                        tdtReconRejectToDt.setDateValue(CommonUtil.convertObjToStr(currDate));
                    }
                    if (DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(tdtReconRejectFromDt.getDateValue()), DateUtil.getDateMMDDYYYY(tdtReconRejectToDt.getDateValue())) < 0) {
                        ClientUtil.showAlertWindow("To Date Should Not Be Greater than From Date !!!");
//                        tdtReconRejectFromDt.setDateValue("");
//                        tdtReconRejectToDt.setDateValue("");
                    } else if (CommonUtil.convertObjToStr(electronicSourceType).equals("")) {
                        ClientUtil.showAlertWindow("Please Select Type of the Transaction");
                    } else if (CommonUtil.convertObjToStr(electronicSourceType).equals("Processing Bank Entries")
                            && CommonUtil.convertObjToStr(rejectReconType).equals("")) {
                        ClientUtil.showAlertWindow("Please Select The Recon Type");
                    } else {
                        observable.resetReconRejectTableValues();
                        tableDataAlignmentReconReject();
                        setSizeTableDataReconReject();
                        lblRejectAmountValue.setText("");
                        lblRejectCountValue.setText("");
                        HashMap hash = new HashMap();
                        hash.put("FROM_DATE", getProperDateFormat(DateUtil.getDateMMDDYYYY(tdtReconRejectFromDt.getDateValue())));
                        hash.put("TO_DATE", getProperDateFormat(DateUtil.getDateMMDDYYYY(tdtReconRejectToDt.getDateValue())));
                        hash.put("SOURCE", CommonUtil.convertObjToStr(electronicSourceType));
                        hash.put("CLASIFIED_CAT", CommonUtil.convertObjToStr(rejectReconType));
                        if (CommonUtil.convertObjToStr(txtActNum.getText()).length() > 0) {
                            hash.put("ACCT_NUM", CommonUtil.convertObjToStr(txtActNum.getText()));
                        }
                        if (CommonUtil.convertObjToStr(txtCustName.getText()).length() > 0) {
                            hash.put("CUST_NAME", CommonUtil.convertObjToStr(txtCustName.getText()));
                        }
                        if (CommonUtil.convertObjToStr(lblRejectAmt.getText()).length() > 0) {
                            hash.put("REJECT_AMOUNT", CommonUtil.convertObjToStr(lblRejectAmt.getText()));
                        }
                        boolean recordExist = observable.populateReconRejectTableData(hash);
                        if (recordExist) {
                            tblReconRejectInquirytLst.setModel(observable.getElectrionicReconRejectTable());
                            tableDataAlignmentReconReject();
                            setSizeTableDataReconReject();
                            javax.swing.table.DefaultTableCellRenderer r = new javax.swing.table.DefaultTableCellRenderer();
                            r.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                            tblReconRejectInquirytLst.getColumnModel().getColumn(6).setCellRenderer(r);
                            tblReconRejectInquirytLst.getColumnModel().getColumn(6).sizeWidthToFit();
                            lblRejectCountValue.setText(CommonUtil.convertObjToStr(observable.lblRejectCountValue));
                            lblRejectAmountValue.setText(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(observable.lblRejectAmountValue)));
                        } else {
                            ClientUtil.showAlertWindow("Record does not exist for seleted dates");
                            btnReconRejectClearActionPerformed(null);
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
    }//GEN-LAST:event_btnReconRejectInquiryProcessActionPerformed
    private void resetReconRejectUI() {
        observable.resetReconRejectTableValues();
        tblReconRejectInquirytLst.setModel(observable.getElectrionicReconRejectTable());
        tableDataAlignmentReconReject();
        setSizeTableDataReconReject();
    }
    private void cboReconRejectTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboReconRejectTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboReconRejectTypeActionPerformed
    private void tableDataAlignmentReconReject() {
        ArrayList _heading = new ArrayList();
        if (observable.getElectronicReconRejectList() != null && observable.getElectronicReconRejectList().size() > 0) {
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(tblReconRejectInquirytLst);
            com.see.truetransact.clientutil.TableModel tableModel = new com.see.truetransact.clientutil.TableModel();
            _heading.add("Payment Dt");
            _heading.add("Description");
            _heading.add("Reference NO");
            _heading.add("Cust Name");
            _heading.add("Acct No");
            _heading.add("Mobile No");
            _heading.add("Amount");
            _heading.add("Recon ID");
            _heading.add("Branch");
            tableModel.setHeading(_heading);
            tableModel.setData((ArrayList) observable.getElectronicReconRejectList());
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            tblReconRejectInquirytLst.setAutoResizeMode(0);
            tblReconRejectInquirytLst.doLayout();
            tblReconRejectInquirytLst.setModel(tableSorter);
            tblReconRejectInquirytLst.revalidate();
        }
    }

    private void setSizeTableDataReconReject() {
        tblReconRejectInquirytLst.getColumnModel().getColumn(0).setPreferredWidth(85);
        tblReconRejectInquirytLst.getColumnModel().getColumn(1).setPreferredWidth(400);
        tblReconRejectInquirytLst.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblReconRejectInquirytLst.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblReconRejectInquirytLst.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblReconRejectInquirytLst.getColumnModel().getColumn(5).setPreferredWidth(65);
        tblReconRejectInquirytLst.getColumnModel().getColumn(6).setPreferredWidth(100);
        tblReconRejectInquirytLst.getColumnModel().getColumn(7).setPreferredWidth(100);
        tblReconRejectInquirytLst.getColumnModel().getColumn(8).setPreferredWidth(100);
//        setRightAlignment(6);
    }
    private void tblReconRejectInquirytLstMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblReconRejectInquirytLstMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblReconRejectInquirytLstMouseClicked

    private void txtActNumFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtActNumFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtActNumFocusLost

    private void lblRejectAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lblRejectAmtFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_lblRejectAmtFocusLost

    private void lblRejectAmtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lblRejectAmtKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblRejectAmtKeyPressed

    private void txtCustNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustNameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustNameFocusLost

    private void txtCustNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustNameKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustNameKeyPressed

    private void txtMatchActNumFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMatchActNumFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMatchActNumFocusLost

    private void txtMatchCustNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMatchCustNameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMatchCustNameFocusLost

    private void txtMatchCustNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMatchCustNameKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMatchCustNameKeyPressed

    private void txtMatchAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMatchAmtFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMatchAmtFocusLost

    private void txtMatchAmtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMatchAmtKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMatchAmtKeyPressed

    private void txtPBTActNumFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPBTActNumFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPBTActNumFocusLost

    private void txtPPTCustNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPPTCustNameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPPTCustNameFocusLost

    private void txtPPTCustNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPPTCustNameKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPPTCustNameKeyPressed

    private void txtPPTAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPPTAmtFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPPTAmtFocusLost

    private void txtPPTAmtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPPTAmtKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPPTAmtKeyPressed

    private void cboReconTransTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboReconTransTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboReconTransTypeActionPerformed

    private void cboMatchTransTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMatchTransTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboMatchTransTypeActionPerformed

    private void btnFileUploadInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFileUploadInsertActionPerformed
        // TODO add your handling code here:
        String filePath = CommonUtil.convertObjToStr(txtFileName.getText());
        if (filePath != null && filePath.length() > 0) {
            String fileUploadType = CommonUtil.convertObjToStr(cboFileUploadType.getSelectedItem());
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null, "Do you want to Really Insert, Please verify the selected file name : "+txtFileName.getText(), CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            if (yesNo == 0) {
                btnFileUploadInsert.setEnabled(false);
                observable.doProcess(null, filePath,fileUploadType);
            } else {
//                btnCancelActionPerformed(null);
                return;
            }
        } else {
            ClientUtil.showAlertWindow("Please select the proper file path");
            return;
        }
    }//GEN-LAST:event_btnFileUploadInsertActionPerformed

    private void btnFileUploadClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFileUploadClearActionPerformed
        // TODO add your handling code here:
        btnBrowse.setEnabled(true);
        txtFileName.setText("");
    }//GEN-LAST:event_btnFileUploadClearActionPerformed

    public String getAccHdDesc(String accHdId) {
        HashMap map1 = new HashMap();
        map1.put("ACCHD_ID", accHdId);
        List acList = ClientUtil.executeQuery("getSelectAcchdDesc", map1);
        if (acList != null && acList.size() > 0) {
            HashMap map2 = new HashMap();
            map2 = (HashMap) acList.get(0);
            String accHdDesc = map2.get("AC_HD_DESC").toString();
            return accHdDesc;
        } else {
            ClientUtil.showMessageWindow("In-Valid Account Head !!!");
            return "";
        }
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDate.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnBrowse;
    private com.see.truetransact.uicomponent.CButton btnDisplay;
    private com.see.truetransact.uicomponent.CButton btnFileUploadClear;
    private com.see.truetransact.uicomponent.CButton btnFileUploadInsert;
    private com.see.truetransact.uicomponent.CButton btnManualMatchClear;
    private com.see.truetransact.uicomponent.CButton btnMatchStatusClear;
    private com.see.truetransact.uicomponent.CButton btnMatchStatusInquiryProcess;
    private com.see.truetransact.uicomponent.CButton btnReconClear;
    private com.see.truetransact.uicomponent.CButton btnReconInquiryProcess;
    private com.see.truetransact.uicomponent.CButton btnReconRejectClear;
    private com.see.truetransact.uicomponent.CButton btnReconRejectInquiryProcess;
    private com.see.truetransact.uicomponent.CButton btnReconciliation;
    private com.see.truetransact.uicomponent.CComboBox cboFileUploadType;
    private com.see.truetransact.uicomponent.CComboBox cboMatchStatusType;
    private com.see.truetransact.uicomponent.CComboBox cboMatchTransType;
    private com.see.truetransact.uicomponent.CComboBox cboReconClassifiedCategory;
    private com.see.truetransact.uicomponent.CComboBox cboReconRejectType;
    private com.see.truetransact.uicomponent.CComboBox cboReconSourceType;
    private com.see.truetransact.uicomponent.CComboBox cboReconTransType;
    private com.see.truetransact.uicomponent.CComboBox cboReconTypeManual;
    private com.see.truetransact.uicomponent.CComboBox cboReconTypeMatch;
    private com.see.truetransact.uicomponent.CComboBox cboSourceRejectType;
    private com.see.truetransact.uicomponent.CComboBox cboUnMatchStatusType;
    private com.see.truetransact.uicomponent.CLabel lblActNum;
    private com.see.truetransact.uicomponent.CLabel lblActNum1;
    private com.see.truetransact.uicomponent.CLabel lblActNum2;
    private com.see.truetransact.uicomponent.CLabel lblAmt;
    private com.see.truetransact.uicomponent.CLabel lblAmt1;
    private com.see.truetransact.uicomponent.CLabel lblAmt2;
    private com.see.truetransact.uicomponent.CLabel lblAutoAmount;
    private com.see.truetransact.uicomponent.CLabel lblAutoAmount1;
    private com.see.truetransact.uicomponent.CLabel lblAutoCount;
    private com.see.truetransact.uicomponent.CLabel lblAutoCount1;
    private com.see.truetransact.uicomponent.CLabel lblAutoReconAmount;
    private com.see.truetransact.uicomponent.CLabel lblAutoReconAmount1;
    private com.see.truetransact.uicomponent.CLabel lblAutoReconCount;
    private com.see.truetransact.uicomponent.CLabel lblAutoReconCount1;
    private com.see.truetransact.uicomponent.CLabel lblBankTxnAmount;
    private com.see.truetransact.uicomponent.CLabel lblBankTxnAmountValue;
    private com.see.truetransact.uicomponent.CLabel lblBankTxnCount;
    private com.see.truetransact.uicomponent.CLabel lblBankTxnCountValue;
    private com.see.truetransact.uicomponent.CLabel lblCustName;
    private com.see.truetransact.uicomponent.CLabel lblCustName1;
    private com.see.truetransact.uicomponent.CLabel lblCustName2;
    private com.see.truetransact.uicomponent.CLabel lblFileName;
    private com.see.truetransact.uicomponent.CLabel lblFromDateManualRecon;
    private com.see.truetransact.uicomponent.CLabel lblMatchAmount;
    private com.see.truetransact.uicomponent.CLabel lblMatchAmountValue;
    private com.see.truetransact.uicomponent.CLabel lblMatchCount;
    private com.see.truetransact.uicomponent.CLabel lblMatchCountValue;
    private com.see.truetransact.uicomponent.CLabel lblMatchStatusFromDate;
    private com.see.truetransact.uicomponent.CLabel lblMatchStatusReconType;
    private com.see.truetransact.uicomponent.CLabel lblMatchStatusReconType1;
    private com.see.truetransact.uicomponent.CLabel lblMatchStatusToDate;
    private com.see.truetransact.uicomponent.CLabel lblMatchTransType;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblReconFromDate;
    private com.see.truetransact.uicomponent.CLabel lblReconRejectFromDate;
    private com.see.truetransact.uicomponent.CLabel lblReconRejectReconType;
    private com.see.truetransact.uicomponent.CLabel lblReconRejectReconType1;
    private com.see.truetransact.uicomponent.CLabel lblReconRejectToDate;
    private com.see.truetransact.uicomponent.CLabel lblReconSourceType;
    private com.see.truetransact.uicomponent.CLabel lblReconToDate;
    private com.see.truetransact.uicomponent.CLabel lblReconTransType;
    private com.see.truetransact.uicomponent.CLabel lblReconType;
    private com.see.truetransact.uicomponent.CLabel lblReconTypeManual;
    private com.see.truetransact.uicomponent.CLabel lblReconTypeManual1;
    private com.see.truetransact.uicomponent.CLabel lblReconTypeManual2;
    private com.see.truetransact.uicomponent.CLabel lblRejectAmount;
    private com.see.truetransact.uicomponent.CLabel lblRejectAmountValue;
    private com.see.truetransact.uicomponent.CTextField lblRejectAmt;
    private com.see.truetransact.uicomponent.CLabel lblRejectCount;
    private com.see.truetransact.uicomponent.CLabel lblRejectCountValue;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToDateAutoRecon;
    private com.see.truetransact.uicomponent.CLabel lblTotalRecord;
    private com.see.truetransact.uicomponent.CLabel lblTotalRecordVal;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransactionAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransactionAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblUnMatchOursAmount;
    private com.see.truetransact.uicomponent.CLabel lblUnMatchOursAmountValue;
    private com.see.truetransact.uicomponent.CLabel lblUnMatchOursCount;
    private com.see.truetransact.uicomponent.CLabel lblUnMatchOursCountValue;
    private com.see.truetransact.uicomponent.CLabel lblUnMatchTheirsAmount;
    private com.see.truetransact.uicomponent.CLabel lblUnMatchTheirsAmountValue;
    private com.see.truetransact.uicomponent.CLabel lblUnMatchTheirsCount;
    private com.see.truetransact.uicomponent.CLabel lblUnMatchTheirsCountValue;
    private com.see.truetransact.uicomponent.CPanel panAccountNo4;
    private com.see.truetransact.uicomponent.CPanel panAccountNo5;
    private com.see.truetransact.uicomponent.CPanel panAccountNo6;
    private com.see.truetransact.uicomponent.CPanel panAccountNo7;
    private com.see.truetransact.uicomponent.CPanel panAccountNo8;
    private com.see.truetransact.uicomponent.CPanel panAutoCount;
    private com.see.truetransact.uicomponent.CPanel panFileTableData;
    private com.see.truetransact.uicomponent.CPanel panFromBranchDetails4;
    private com.see.truetransact.uicomponent.CPanel panLoadFromFile;
    private com.see.truetransact.uicomponent.CPanel panManualReconciliation;
    private com.see.truetransact.uicomponent.CPanel panMatchStatusDetails;
    private com.see.truetransact.uicomponent.CPanel panMatchStatusDetails1;
    private com.see.truetransact.uicomponent.CPanel panMatchStatusInquiry;
    private com.see.truetransact.uicomponent.CPanel panMatchStatusInquiryLst;
    private com.see.truetransact.uicomponent.CPanel panOurTransactionList;
    private com.see.truetransact.uicomponent.CPanel panOursTransactionTable;
    private com.see.truetransact.uicomponent.CPanel panProcess;
    private com.see.truetransact.uicomponent.CPanel panReconDetails;
    private com.see.truetransact.uicomponent.CPanel panReconDetails1;
    private com.see.truetransact.uicomponent.CPanel panReconInquiry;
    private com.see.truetransact.uicomponent.CPanel panReconInquiryLst;
    private com.see.truetransact.uicomponent.CPanel panReconRejectDetails;
    private com.see.truetransact.uicomponent.CPanel panReconRejectDetails1;
    private com.see.truetransact.uicomponent.CPanel panReconRejectInquiry;
    private com.see.truetransact.uicomponent.CPanel panReconRejectInquiryLst;
    private com.see.truetransact.uicomponent.CPanel panReconSelectionDetails;
    private com.see.truetransact.uicomponent.CPanel panReconciliationUploadDetails;
    private com.see.truetransact.uicomponent.CPanel panStatementTxnList;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTheirsTransactionTable;
    private com.see.truetransact.uicomponent.CScrollPane srpFileDataDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpOurTransactionList;
    private com.see.truetransact.uicomponent.CScrollPane srpStatementTxnList;
    private com.see.truetransact.uicomponent.CScrollPane srpTableMatchStatusInquiryLst;
    private com.see.truetransact.uicomponent.CScrollPane srpTableReconInquiryLst;
    private com.see.truetransact.uicomponent.CScrollPane srpTableReconRejectInquiryLst;
    private com.see.truetransact.uicomponent.CTabbedPane tabReconciliationDetails;
    private com.see.truetransact.uicomponent.CTable tblFileDataDetails;
    private com.see.truetransact.uicomponent.CTable tblMatchStatusInquirytLst;
    private com.see.truetransact.uicomponent.CTable tblOursTransactionList;
    private com.see.truetransact.uicomponent.CTable tblReconInquirytLst;
    private com.see.truetransact.uicomponent.CTable tblReconRejectInquirytLst;
    private com.see.truetransact.uicomponent.CTable tblTheirsTransactionList;
    private com.see.truetransact.uicomponent.CDateField tdtFromDateManualRecon;
    private com.see.truetransact.uicomponent.CDateField tdtMatchStatusFromDt;
    private com.see.truetransact.uicomponent.CDateField tdtMatchStatusToDt;
    private com.see.truetransact.uicomponent.CDateField tdtReconFromDt;
    private com.see.truetransact.uicomponent.CDateField tdtReconRejectFromDt;
    private com.see.truetransact.uicomponent.CDateField tdtReconRejectToDt;
    private com.see.truetransact.uicomponent.CDateField tdtReconToDt;
    private com.see.truetransact.uicomponent.CDateField tdtToDateManualRecon;
    private com.see.truetransact.uicomponent.CTextField txtActNum;
    private com.see.truetransact.uicomponent.CTextField txtCustName;
    private com.see.truetransact.uicomponent.CTextField txtFileName;
    private com.see.truetransact.uicomponent.CTextField txtMatchActNum;
    private com.see.truetransact.uicomponent.CTextField txtMatchAmt;
    private com.see.truetransact.uicomponent.CTextField txtMatchCustName;
    private com.see.truetransact.uicomponent.CTextField txtPBTActNum;
    private com.see.truetransact.uicomponent.CTextField txtPPTAmt;
    private com.see.truetransact.uicomponent.CTextField txtPPTCustName;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        ElectronicReconciliationUI fad = new ElectronicReconciliationUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(fad);
        j.show();
        fad.show();
    }
}
