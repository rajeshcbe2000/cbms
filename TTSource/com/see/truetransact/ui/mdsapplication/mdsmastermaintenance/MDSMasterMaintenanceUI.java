  /*
 * RemittanceProductUI.java
 *
 * Created on November 26, 2003, 11:27 AM
 IR*/
package com.see.truetransact.ui.mdsapplication.mdsmastermaintenance;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.transferobject.common.charges.LoanSlabChargesTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.common.nominee.NomineeUI;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import com.see.truetransact.ui.common.viewall.ViewAll;
import javax.swing.table.AbstractTableModel;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.ToDateValidation;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.*;

/**
 *
 * @author Hemant Modification Lohith R.
 * @modified : Sunil Added Edit Locking - 08-07-2005
 */
public class MDSMasterMaintenanceUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer {

    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.mdsapplication.mdsmastermaintenance.MDSMasterMaintenanceRB", ProxyParameters.LANGUAGE);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private MDSMasterMaintenanceMRB objMandatoryRB = new MDSMasterMaintenanceMRB();
    NomineeUI nomineeUi = new NomineeUI("SCREEN");
    MDSMasterMaintenanceOB observable = null;
    private String viewType = new String();
    final String AUTHORIZE = "Authorize";
    private boolean updateMode = false;
    private boolean isFilled = false;
    HashMap mandatoryMap = null;
    int updateTab = -1;
    String bondNoset = "N";
    String applicationNoset = "N";
    String PROD_ID = "";
    String bond = "N";
    String application_No = "N";
    boolean fromAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    //Loan Charges
    private boolean tableFlag = false;
    private JTable table = null;
    public String prodDesc = "";
    private boolean transNew = true;//charge details
    private List chargelst = null; //CHARGE DETAILS
    private boolean finalChecking = false; //CHARGE DETAILS
    Rounding rd = new Rounding();
    static int entryCountNO = 0;
    private javax.swing.JScrollPane srpChargeDetails;
    private boolean selectMode = false;//charge details, calculating total of selected amount
    private HashMap returnMap = null;//charge details, calculating total of selected amount
    private List bufferList = new ArrayList();
    private Object columnNames[] = {"Sl No", "Certificate No", "Member No", "Name", "Contact No", "Networth"};
    TransactionUI transactionUI = new TransactionUI(); //trans details
    double amtBorrow = 0.0; //trans details
    java.util.Date currDate = null;
    private int rejectFlag = 0;
    Date cdate = TrueTransactMain.getApplicationDate();
    String daate = DateUtil.getStringDate(cdate);
    private int rowCount=0;
    String prizdPend = "";
    String secPend = "";
    private boolean thalayal = false;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;

    /**
     * Creates new form BeanForm
     */
    public MDSMasterMaintenanceUI() {
        initComponents();
        initRunComponentsCaseDetails();
        initComponentSecurity();
        initForm();
    }

    private void initForm() {
        setFieldNames();
        internationalize();
        observable = new MDSMasterMaintenanceOB();
        setMaxLengths();
        initComponentData();
        setMandatoryHashMap();
        setHelpMessage();
        setButtonEnableDisable();
        ClientUtil.enableDisable(panMasterMaintenanceDetails, false);
        ClientUtil.enableDisable(panCaseTableFields, false);
        ClientUtil.enableDisable(panCollatetalDetails, false);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panInsideMasterMaintenanceDetails);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panMdsSchemeNameDetails);
        //system.out.println("daate" + daate);
        enableDisableButton(false);
        enableDisableButtons(false);
        btnNew.setVisible(false);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
        setSizeTableData();
        addGahanRadioBtns();
        btnSecurityCollateral(false);
        panChargeTransfer.add(transactionUI);
        transactionUI.setSourceScreen("MDS_MASTERMAINTENANCE");

        //   transactionUI.setTransactionMode(CommonConstants.DEBIT);
        observable.setTransactionOB(transactionUI.getTransactionOB());
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).length() > 0 && CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
            panSalaryRecovery.setVisible(true);
            lblLockStatus.setVisible(true);
            lblLockStatusVal.setVisible(true);
        } else {
            panSalaryRecovery.setVisible(false);
            lblLockStatus.setVisible(false);
            lblLockStatusVal.setVisible(false);
        }
        getTableData();
        txtApplicationNo.setEnabled(false);
        currDate = ClientUtil.getCurrentDate();
        tdtApplicationDate.setDateValue(CommonUtil.convertObjToStr(currDate));
        //system.out.println("tdtApplicationDate.." + tdtApplicationDate.getDateValue());
        tdtApplicationDate.setEnabled(false);
        
        rdoGoldSecurityExitsNo.setSelected(true); // Added by nithya on 07-03-2020 for KD-1379
        txtGoldSecurityId.setEnabled(false);
        btnGoldSecurityIdSearch.setEnabled(false);
        btnAuthorize.setEnabled(true);
        btnCancel.setEnabled(true);
    }

    private void getTableData() {

        Object rowData[][] = new Object[+bufferList.size()][6];
        //  Integer rowData[][] = new  Integer[buffer1.size()][3];
        int j = 0;
        String d1 = "";
        String d2 = "";
        int i = 0;
        //system.out.println("BuufferrList  " + bufferList.size());




        for (i = 0; i < bufferList.size(); i++) {
            HashMap m = new HashMap();
            m = (HashMap) bufferList.get(i);
            //system.out.println("iii m00001 : " + m.get("SL_NO"));
            rowData[i][0] = m.get("SL_NO").toString();
            //system.out.println("iii m000011 : " + m.get("SALARY_CERTIFICATE_NO"));
            rowData[i][1] = m.get("SALARY_CERTIFICATE_NO").toString();
            rowData[i][2] = m.get("EMP_MEMBER_NO").toString();
            //system.out.println("iii m0000 111: " + m.get("EMP_MEMBER_NO"));
            rowData[i][3] = m.get("EMP_NAME").toString();
            if(m.containsKey("CONTACT_NO") && m.get("CONTACT_NO")!=null){
            	rowData[i][4] = m.get("CONTACT_NO").toString();
            } else{
                rowData[i][4] ="";
            }
            rowData[i][5] = m.get("NETWORTH").toString();
        }
        //system.out.println("iii m0000 222: ");





        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        tblSalaryDetails.setModel(new javax.swing.table.DefaultTableModel(rowData, columnNames) {
            public boolean isCellEditable(int row, int column) {
                //Only the third column
                return false;
            }
        });
        tblSalaryDetails.setVisible(true);

    }

    private void setSizeTableData() {
        tblDepositDetails.getColumnModel().getColumn(0).setPreferredWidth(60);
        tblDepositDetails.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblDepositDetails.getColumnModel().getColumn(2).setPreferredWidth(70);
        tblDepositDetails.getColumnModel().getColumn(3).setPreferredWidth(70);
    }

    public class SimpleTableModel extends AbstractTableModel {

        private ArrayList dataVector;
        private ArrayList headingVector;

        public SimpleTableModel(ArrayList dataVector, ArrayList headingVector) {
            this.dataVector = dataVector;
            this.headingVector = headingVector;
        }

        public int getColumnCount() {
            return headingVector.size();
        }

        public int getRowCount() {
            return dataVector.size();
        }

        public Object getValueAt(int row, int col) {
            ArrayList rowVector = (ArrayList) dataVector.get(row);
            return rowVector.get(col);
        }

        public String getColumnName(int column) {
            return headingVector.get(column).toString();
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            if (col == 0 && (CommonUtil.convertObjToStr(getValueAt(row, col + 4)).equals("Y"))) {
                //tableMouseClicked();
                return false;
            } else {
                if (col != 0) {
//                    tableMouseClicked(null);
//                    return false;
                    
                    if (col == 3 && (CommonUtil.convertObjToStr(getValueAt(row, col + 2)).equals("Y"))) {
                        //tableMouseClicked(null);
                        return true;
                    } else {
                        //tableMouseClicked(null);
                        return false;
                    }
                } else {
                    //tableMouseClicked(null);

//                    
                    //system.out.println("nnn");
                    return true;
                }
            }
        }

        public void setValueAt(Object aValue, int row, int col) {
            ArrayList rowVector = (ArrayList) dataVector.get(row);
            rowVector.set(col, aValue);
        }
    }

    private void chrgTableEnableDisable() {
        lblTotalTransactionAmtVal.setText("");
        tableFlag = false;
        panChargeDetails.removeAll();
        panChargeDetails.setVisible(false);
    }

    private void createChargeTable(String prodDesc) {
        HashMap tableMap = buildData(prodDesc);
        ArrayList dataList = new ArrayList();
        dataList = (ArrayList) tableMap.get("DATA");
        if (dataList != null && dataList.size() > 0) {
            tableFlag = true;
            ArrayList headers;
            panChargeDetails.setVisible(true);
            SimpleTableModel stm = new SimpleTableModel((ArrayList) tableMap.get("DATA"), (ArrayList) tableMap.get("HEAD"));
            table = new JTable(stm);
            table.setSize(430, 110);            
            table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                   
                    if (tableFlag == true) {
                     tableMouseClicked();
                    }
                }
            });
            table.addKeyListener(new java.awt.event.KeyAdapter() {

                public void keyReleased(java.awt.event.KeyEvent e) {
                    if (tableFlag == true) {
                        tableMouseClicked();
                    }
                }
            });
            srpChargeDetails = new javax.swing.JScrollPane(table);
            srpChargeDetails.setMinimumSize(new java.awt.Dimension(430, 110));
            srpChargeDetails.setPreferredSize(new java.awt.Dimension(430, 110));
            panChargeDetails.add(srpChargeDetails, new java.awt.GridBagConstraints());
            table.revalidate();
        } else {
            tableFlag = false;
            chrgTableEnableDisable();
        }
    }
    private void tableMouseClicked() {
        double totAmount = 0.0;       
        for (int i = 0; i < table.getRowCount(); i++) {
            if (CommonUtil.convertObjToStr(table.getValueAt(i, 0)).equals("true")) {
                totAmount = totAmount + CommonUtil.convertObjToDouble(table.getValueAt(i, 3)).doubleValue();
            }
        }
        transactionUI.setCallingApplicantName(lblMembershipName.getText());
        transactionUI.setCallingAmount("" + totAmount);       
        lblTotalTransactionAmtVal.setText("" + totAmount);
        transactionUI.setCallingTransType("CASH");            
    }

    private HashMap buildData(String prodDesc) {
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_ID", prodDesc);
        whereMap.put("DEDUCTION_ACCU", "R");

        List list = ClientUtil.executeQuery("getChargeDetailsData", whereMap);
        System.out.println("list...>>>>>>>>" + list);
        //system.out.println("list size...>>>>>>>>" + list.size());
        if (list != null && list.size() > 0) {
            entryCountNO = 0;
        } else {
            entryCountNO = 1;
        }
        boolean _isAvailable = list.size() > 0 ? true : false;
        ArrayList _heading = null;
        ArrayList data = new ArrayList();
        ArrayList colData = new ArrayList();
        HashMap map;
        Iterator iterator = null;
        if (_isAvailable) {
            map = (HashMap) list.get(0);
            iterator = map.keySet().iterator();
        }
        if (_isAvailable && _heading == null) {
            _heading = new ArrayList();
            _heading.add("Select");
            while (iterator.hasNext()) {
                _heading.add((String) iterator.next());
            }
        }

        String cellData = "", keyData = "";
        Object obj = null;
        for (int i = 0, j = list.size(); i < j; i++) {
            map = (HashMap) list.get(i);
            colData = new ArrayList();
            iterator = map.values().iterator();
            System.out.println("Map###############"+map);
            if (CommonUtil.convertObjToStr(map.get("M")).equals("Y")) {
                colData.add(new Boolean(true));
            } else {
                colData.add(new Boolean(false));
            }
            while (iterator.hasNext()) {
                obj = iterator.next();
                //                if (obj != null) {
                colData.add(CommonUtil.convertObjToStr(obj));
                //                } else {
                //                    colData.add("");
                //                }
            }
            data.add(colData);
        }
        map = new HashMap();
        map.put("HEAD", _heading);
        map.put("DATA", data);
        //system.out.println("map in creating charges...." + map);
        return map;
    }

    private void chargeAmount() {
        HashMap appraiserMap = new HashMap();
        appraiserMap.put("SCHEME_ID", prodDesc);
        appraiserMap.put("DEDUCTION_ACCU", "R");
        chargelst = ClientUtil.executeQuery("getAllChargeDetailsData", appraiserMap);
        HashMap chargeMap = new HashMap();
        if (chargelst != null && chargelst.size() > 0) {
            for (int i = 0; i < chargelst.size(); i++) {
                String accHead = "";
                String editable = "";
                chargeMap = (HashMap) chargelst.get(i);
                accHead = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_ID"));
                editable = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_EDITABLE"));
                //System.out.println("$#@@$ accHead" + accHead);
                for (int j = 0; j < table.getRowCount(); j++) {
                    //System.out.println("$#@@$ accHead inside table " + table.getValueAt(j, 1));
                    if (CommonUtil.convertObjToStr(table.getValueAt(j, 1)).equals(accHead)) {
                        double chargeAmt = 0;
                        if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Sanction Amount")) {
                            chargeAmt = CommonUtil.convertObjToDouble(txtPrizedAmount.getText()).doubleValue()
                                    * CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("CHARGE_RATE"))).doubleValue() / 100;
                            long roundOffType = getRoundOffType(CommonUtil.convertObjToStr(chargeMap.get("ROUND_OFF_TYPE")));
                            if (roundOffType != 0) {
                                chargeAmt = rd.getNearest((long) (chargeAmt * roundOffType), roundOffType) / roundOffType;
                            }
                            double minAmt = CommonUtil.convertObjToDouble(chargeMap.get("MIN_CHARGE_AMOUNT")).doubleValue();
                            double maxAmt = CommonUtil.convertObjToDouble(chargeMap.get("MAX_CHARGE_AMOUNT")).doubleValue();
                            if (chargeAmt < minAmt) {
                                chargeAmt = minAmt;
                            }
                            if (chargeAmt > maxAmt) {
                                chargeAmt = maxAmt;
                            }
                            table.setValueAt(String.valueOf(chargeAmt), j, 3);
                        } else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Amount Range")) {

                            List chargeslabLst = ClientUtil.executeQuery("getSelectLoanSlabChargesTO", chargeMap);
                            double limit = CommonUtil.convertObjToDouble(txtPrizedAmount.getText()).doubleValue();
                            if (chargeslabLst != null && chargeslabLst.size() > 0) {
                                double minAmt = 0;
                                double maxAmt = 0;
                                for (int k = 0; k < chargeslabLst.size(); k++) {
                                    LoanSlabChargesTO objLoanSlabChargesTO = (LoanSlabChargesTO) chargeslabLst.get(k);

                                    double minAmtRange = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getFromSlabAmt()).doubleValue();
                                    double maxAmtRange = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getToSlabAmt()).doubleValue();
                                    if (limit >= minAmtRange && limit <= maxAmtRange) {
                                        double chargeRate = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getChargeRate()).doubleValue();
                                        minAmt = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getMinChargeAmount()).doubleValue();
                                        maxAmt = CommonUtil.convertObjToDouble(objLoanSlabChargesTO.getMaxChargeAmount()).doubleValue();

                                        chargeAmt = CommonUtil.convertObjToDouble(txtPrizedAmount.getText()).doubleValue() * chargeRate / 100;
                                        if (chargeAmt < minAmt) {
                                            chargeAmt = minAmt;
                                        }
                                        if (chargeAmt > maxAmt) {
                                            chargeAmt = maxAmt;
                                        }
                                        break;
                                    }
                                }

                            }
                        table.setValueAt(String.valueOf(chargeAmt), j, 3);
                        } else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Flat Charge")) {
                            chargeAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("FLAT_CHARGE"))).doubleValue();
                        }
                        chargeMap.put("CHARGE_AMOUNT", String.valueOf(chargeAmt));
                        System.out.println("#$#$$# chargeAmt111111111:" + chargeAmt);
                    }
                    if (editable.equals("Y")) {
                        double chargeAmt1 = CommonUtil.convertObjToDouble(table.getValueAt(j, 3));
                        chargeMap.put("CHARGE_AMOUNT", String.valueOf(chargeAmt1));
                        System.out.println("#$#$$# chargeAmt2222222222:" + chargeAmt1);
                    }
                }
            }
            System.out.println("#$#$$# chargeMap:" + chargeMap);
            System.out.println("#$#$$# chargelst:" + chargelst);
            table.revalidate();
            table.updateUI();

        }

    }

    private int getRoundOffType(String roundOff) {
        int returnVal = 0;
        if (roundOff.equals("Nearest Value")) {
            returnVal = 1 * 100;
        } else if (roundOff.equals("Nearest Hundreds")) {
            returnVal = 100 * 100;
        } else if (roundOff.equals("Nearest Tens")) {
            returnVal = 10 * 100;
        }
        return returnVal;
    }

    /* Auto Generated Method - setFieldNames()
     This method assigns name for all the components.
     Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnMemNo.setName("btnMemNo");
        btnMemberNo.setName("btnMemberNo");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnSchemeName.setName("btnSchemeName");
        btnView.setName("btnView");
        cboCity.setName("cboCity");
        cboSecurityType.setName("cboSecurityType");
        lblAddress.setName("lblAddress");
        lblBordDt.setName("lblBordDt");
        lblBordNo.setName("lblBordNo");
        lblCaseStatus.setName("lblCaseStatus");
        lblCaseNumber.setName("lblCaseNumber");
        lblFillingDt.setName("lblFillingDt");
        lblFillingFees.setName("lblFillingFees");
        lblMiscCharges.setName("lblMiscCharges");
        lblChitStartDt.setName("lblChitStartDt");
        lblChittalNo.setName("lblChittalNo");
        lblCity.setName("lblCity");
        lblContactNo.setName("lblContactNo");
        lblContactNum.setName("lblContactNum");
        lblDepAmount.setName("lblDepAmount");
        lblDepDt.setName("lblDepDt");
        lblDepNo.setName("lblDepNo");
        lblDesignation.setName("lblDesignation");
        lblDivisionNo.setName("lblDivisionNo");
        lblEmployerName.setName("lblEmployerName");
        lblGoldRemarks.setName("lblGoldRemarks");
        lblGrossWeight.setName("lblGrossWeight");
        lblJewelleryDetails.setName("lblJewelleryDetails");
        lblLastInstDate.setName("lblLastInstDate");
        lblLastInstNo.setName("lblLastInstNo");
        lblMaturityDt.setName("lblMaturityDt");
        lblMaturityValue.setName("lblMaturityValue");
        lblMemName.setName("lblMemName");
        lblMemNetworth.setName("lblMemNetworth");
        lblMemNo.setName("lblMemNo");
        lblMemType.setName("lblMemType");
//        lblMemberName.setName("lblMemberName");
        lblMemberNo.setName("lblMemberNo");
        lblMemberNum.setName("lblMemberNum");
        lblMemberType.setName("lblMemberType");
        lblMembershipName.setName("lblMembershipName");
        lblMembershipType.setName("lblMembershipType");
        lblSalaryRecovery.setName("lblSalaryRecovery");
        lblMsg.setName("lblMsg");
        lblNetWeight.setName("lblNetWeight");
        lblNetWorth.setName("lblNetWorth");
        lblNomineName.setName("lblNomineName");
        lblNomineeName.setName("lblNomineeName");
        lblOverDueAmount.setName("lblOverDueAmount");
        lblOverDueInstallments.setName("lblOverDueInstallments");
        lblPayDt.setName("lblPayDt");
        lblPinCode.setName("lblPinCode");
        lblPrizedAmount.setName("lblPrizedAmount");
        lblProductId.setName("lblProductId");
        lblProductTypeSecurity.setName("lblProductTypeSecurity");
        lblRateOfInterest.setName("lblRateOfInterest");
        lblRemarks.setName("lblRemarks");
        lblResolutionDt.setName("lblResolutionDt");
        lblResolutionNo.setName("lblResolutionNo");
        lblRetirementDt.setName("lblRetirementDt");
        lblSalaryCertificateNo.setName("lblSalaryCertificateNo");
        lblSalaryRemark.setName("lblSalaryRemark");
        lblSchemeName.setName("lblSchemeName");
        lblSecurityRemarks.setName("lblSecurityRemarks");
        lblSecurityType.setName("lblSecurityType");
        lblSecurityValue.setName("lblSecurityValue");
        lblSpace.setName("lblSpace");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblSpace6.setName("lblSpace6");
        lblStatus.setName("lblStatus");
        lblTotalAmountTillDate.setName("lblTotalAmountTillDate");
        lblTotalDeposit.setName("lblTotalDeposit");
        lblTotalDepositValue.setName("lblTotalDepositValue");
        lblTotalSalary.setName("lblTotalSalary");
        lblValueOfGold.setName("lblValueOfGold");
        mbrMain.setName("mbrMain");
        panBtnDeposit.setName("panBtnDeposit");
        panBtnMemberType.setName("panBtnMemberType");
        panCollateralTypeDetails.setName("panCollateralTypeDetails");
        panDepositDetails.setName("panDepositDetails");
        panDepositTable.setName("panDepositTable");
        panDepositType.setName("panDepositType");
        panGoldTypeDetails.setName("panGoldTypeDetails");
        panInsideMasterMaintenanceDetails.setName("panInsideMasterMaintenanceDetails");
        panInstallmentDetails.setName("panInstallmentDetails");
        panMasterMaintenanceDetails.setName("panMasterMaintenanceDetails");
        panMdsSchemeNameDetails.setName("panMdsSchemeNameDetails");
        panMemberDetails.setName("panMemberDetails");
        panMemberNo.setName("panMemberNo");
        panMemberNoDetails.setName("panMemberNoDetails");
        panMemberNumber.setName("panMemberNumber");
        panMemberTypeDetails.setName("panMemberTypeDetails");
        panMemberTypeTable.setName("panMemberTypeTable");
        panPrizeDetails.setName("panPrizeDetails");
        panPrizedDetails.setName("panPrizedDetails");
        panSalaryDetails.setName("panSalaryDetails");
        panSchemeName.setName("panSchemeName");
        panStatus.setName("panStatus");
        srpMemberType.setName("srpMemberType");
        srpTableDeposit.setName("srpTableDeposit");
        tabMasterMaintenance.setName("tabMasterMaintenance");
        tabMasterMaintenanceDetails.setName("tabMasterMaintenanceDetails");
        tblDepositDetails.setName("tblDepositDetails");
        tblMemberType.setName("tblMemberType");
        tdtBondDt.setName("tdtBondDt");
        tdtChitStartDt.setName("tdtChitStartDt");
        tdtDepDt.setName("tdtDepDt");
        tdtLastInstDate.setName("tdtLastInstDate");
        tdtPayDt.setName("tdtPayDt");
        tdtResolutionDt.setName("tdtResolutionDt");
        tdtRetirementDt.setName("tdtRetirementDt");
        txtAddress.setName("txtAddress");
        txtBondNo.setName("txtBondNo");
        cboCaseStatus.setName("cboCaseStatus");
        txtCaseNumber.setName("txtCaseNumber");
        tdtlFillingDt.setName("tdtlFillingDt");
        txtFillingFees.setName("txtFillingFees");
        txtMiscCharges.setName("txtMiscCharges");
        txtChittalNo.setName("txtChittalNo");
        txtContactNo.setName("txtContactNo");
        txtContactNum.setName("txtContactNum");
        txtDepAmount.setName("txtDepAmount");
        txtDepNo.setName("txtDepNo");
        txtDesignation.setName("txtDesignation");
        txtDivisionNo.setName("txtDivisionNo");
        txtSubNo.setName("txtSubNo");
        txtEmployerName.setName("txtEmployerName");
        txtGoldRemarks.setName("txtGoldRemarks");
        txtGrossWeight.setName("txtGrossWeight");
        txtJewelleryDetails.setName("txtJewelleryDetails");
        txtLastInstNo.setName("txtLastInstNo");
        txtMaturityDt.setName("txtMaturityDt");
        txtMaturityValue.setName("txtMaturityValue");
        txtMemName.setName("txtMemName");
        txtMemNetworth.setName("txtMemNetworth");
        txtMemNo.setName("txtMemNo");
        txtMemType.setName("txtMemType");
        txtMemberNo.setName("txtMemberNo");
        txtMemberNum.setName("txtMemberNum");
        txtNetWeight.setName("txtNetWeight");
        txtNetWorth.setName("txtNetWorth");
        txtOverDueAmount.setName("txtOverDueAmount");
        txtOverDueInstallments.setName("txtOverDueInstallments");
        txtPinCode.setName("txtPinCode");
        txtPrizedAmount.setName("txtPrizedAmount");
//        txtProductId.setName("txtProductId");
        txtRateOfInterest.setName("txtRateOfInterest");
        txtRemarks.setName("txtRemarks");
        txtResolutionNo.setName("txtResolutionNo");
        txtSalaryCertificateNo.setName("txtSalaryCertificateNo");
        txtSalaryRemark.setName("txtSalaryRemark");
        txtSchemeName.setName("txtSchemeName");
        txtSecurityRemarks.setName("txtSecurityRemarks");
        txtTotalAmountTillDate.setName("txtTotalAmountTillDate");
        txtTotalSalary.setName("txtTotalSalary");
        txtValueOfGold.setName("txtValueOfGold");
    }

    /* Auto Generated Method - internationalize()
     This method used to assign display texts from
     the Resource Bundle File. */
    private void internationalize() {
        resourceBundle = new MDSMasterMaintenanceRB();
        lblBordDt.setText(resourceBundle.getString("lblBordDt"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblRateOfInterest.setText(resourceBundle.getString("lblRateOfInterest"));
        lblContactNum.setText(resourceBundle.getString("lblContactNum"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMaturityValue.setText(resourceBundle.getString("lblMaturityValue"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblOverDueAmount.setText(resourceBundle.getString("lblOverDueAmount"));
        lblSalaryRecovery.setText(resourceBundle.getString("lblSalaryRecovery"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblMemberNum.setText(resourceBundle.getString("lblMemberNum"));
        lblOverDueInstallments.setText(resourceBundle.getString("lblOverDueInstallments"));
        lblJewelleryDetails.setText(resourceBundle.getString("lblJewelleryDetails"));
        lblValueOfGold.setText(resourceBundle.getString("lblValueOfGold"));
        btnMemberNo.setText(resourceBundle.getString("btnMemberNo"));
        lblChitStartDt.setText(resourceBundle.getString("lblChitStartDt"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblDivisionNo.setText(resourceBundle.getString("lblDivisionNo"));
        lblSecurityValue.setText(resourceBundle.getString("lblSecurityValue"));
        lblSalaryCertificateNo.setText(resourceBundle.getString("lblSalaryCertificateNo"));
        ((javax.swing.border.TitledBorder) panInstallmentDetails.getBorder()).setTitle(resourceBundle.getString("panInstallmentDetails"));
        lblMembershipType.setText(resourceBundle.getString("lblMembershipType"));
        ((javax.swing.border.TitledBorder) panPrizeDetails.getBorder()).setTitle(resourceBundle.getString("panPrizeDetails"));
        lblMemberType.setText(resourceBundle.getString("lblMemberType"));
        lblSecurityRemarks.setText(resourceBundle.getString("lblSecurityRemarks"));
        lblNetWorth.setText(resourceBundle.getString("lblNetWorth"));
        lblPayDt.setText(resourceBundle.getString("lblPayDt"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblResolutionDt.setText(resourceBundle.getString("lblResolutionDt"));
        lblLastInstDate.setText(resourceBundle.getString("lblLastInstDate"));
        lblTotalDepositValue.setText(resourceBundle.getString("lblTotalDepositValue"));
        lblMemType.setText(resourceBundle.getString("lblMemType"));
        lblCity.setText(resourceBundle.getString("lblCity"));
        lblBordNo.setText(resourceBundle.getString("lblBordNo"));
        lblCaseStatus.setText(resourceBundle.getString("lblCaseStatus"));
        lblCaseNumber.setText(resourceBundle.getString("lblCaseNumber"));
        lblFillingDt.setText(resourceBundle.getString("lblFillingDt"));
        lblFillingFees.setText(resourceBundle.getString("lblFillingFees"));
        lblMiscCharges.setText(resourceBundle.getString("lblMiscCharges"));
        lblMemNo.setText(resourceBundle.getString("lblMemNo"));
        lblGrossWeight.setText(resourceBundle.getString("lblGrossWeight"));
        lblDesignation.setText(resourceBundle.getString("lblDesignation"));
        lblRetirementDt.setText(resourceBundle.getString("lblRetirementDt"));
        lblMemNetworth.setText(resourceBundle.getString("lblMemNetworth"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMemberNo.setText(resourceBundle.getString("lblMemberNo"));
        lblSalaryRemark.setText(resourceBundle.getString("lblSalaryRemark"));
        lblProductId.setText(resourceBundle.getString("lblProductId"));
        lblProductTypeSecurity.setText(resourceBundle.getString("lblProductTypeSecurity"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnMemNo.setText(resourceBundle.getString("btnMemNo"));
        lblDepAmount.setText(resourceBundle.getString("lblDepAmount"));
        lblChittalNo.setText(resourceBundle.getString("lblChittalNo"));
        lblSecurityType.setText(resourceBundle.getString("lblSecurityType"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblContactNo.setText(resourceBundle.getString("lblContactNo"));
        btnView.setText(resourceBundle.getString("btnView"));
        lblTotalAmountTillDate.setText(resourceBundle.getString("lblTotalAmountTillDate"));
        lblSchemeName.setText(resourceBundle.getString("lblSchemeName"));
        lblPinCode.setText(resourceBundle.getString("lblPinCode"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        lblGoldRemarks.setText(resourceBundle.getString("lblGoldRemarks"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblResolutionNo.setText(resourceBundle.getString("lblResolutionNo"));
        lblNetWeight.setText(resourceBundle.getString("lblNetWeight"));
        lblTotalDeposit.setText(resourceBundle.getString("lblTotalDeposit"));
        lblSpace.setText(resourceBundle.getString("lblSpace"));
        lblEmployerName.setText(resourceBundle.getString("lblEmployerName"));
        lblLastInstNo.setText(resourceBundle.getString("lblLastInstNo"));
        lblTotalSalary.setText(resourceBundle.getString("lblTotalSalary"));
//        lblMemberName.setText(resourceBundle.getString("lblMemberName"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblDepNo.setText(resourceBundle.getString("lblDepNo"));
        ((javax.swing.border.TitledBorder) panPrizedDetails.getBorder()).setTitle(resourceBundle.getString("panPrizedDetails"));
        lblPrizedAmount.setText(resourceBundle.getString("lblPrizedAmount"));
        btnSchemeName.setText(resourceBundle.getString("btnSchemeName"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblMaturityDt.setText(resourceBundle.getString("lblMaturityDt"));
        lblNomineName.setText(resourceBundle.getString("lblNomineName"));
        lblAddress.setText(resourceBundle.getString("lblAddress"));
        lblSpace6.setText(resourceBundle.getString("lblSpace6"));
        lblNomineeName.setText(resourceBundle.getString("lblNomineeName"));
        lblDepDt.setText(resourceBundle.getString("lblDepDt"));
        lblMembershipName.setText(resourceBundle.getString("lblMembershipName"));
        lblMemName.setText(resourceBundle.getString("lblMemName"));
    }

    /* Auto Generated Method - setMandatoryHashMap()
     
     ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     
     This method list out all the Input Fields available in the UI.
     It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtRemarks", new Boolean(false));
        mandatoryMap.put("txtOverDueAmount", new Boolean(true));
        mandatoryMap.put("txtTotalAmountTillDate", new Boolean(true));
        mandatoryMap.put("txtLastInstNo", new Boolean(true));
        mandatoryMap.put("txtOverDueInstallments", new Boolean(true));
        mandatoryMap.put("tdtLastInstDate", new Boolean(true));
        mandatoryMap.put("txtChittalNo", new Boolean(true));
        mandatoryMap.put("tdtChitStartDt", new Boolean(true));
        mandatoryMap.put("txtDivisionNo", new Boolean(true));
        mandatoryMap.put("txtSchemeName", new Boolean(true));
        mandatoryMap.put("txtMemberNo", new Boolean(true));
        mandatoryMap.put("txtBondNo", new Boolean(true));
        mandatoryMap.put("cboCaseStatus", new Boolean(true));
        mandatoryMap.put("txtCaseNumber", new Boolean(true));
        mandatoryMap.put("tdtlFillingDt", new Boolean(true));
        mandatoryMap.put("txtFillingFees", new Boolean(true));
        mandatoryMap.put("txtMiscCharges", new Boolean(true));
        mandatoryMap.put("tdtBondDt", new Boolean(true));
        mandatoryMap.put("txtPrizedAmount", new Boolean(true));
        mandatoryMap.put("tdtResolutionDt", new Boolean(true));
        mandatoryMap.put("tdtPayDt", new Boolean(true));
        mandatoryMap.put("txtResolutionNo", new Boolean(true));
        mandatoryMap.put("txtContactNo", new Boolean(false));
        mandatoryMap.put("txtMemberNum", new Boolean(true));
        mandatoryMap.put("txtSalaryRemark", new Boolean(true));
        mandatoryMap.put("txtDesignation", new Boolean(true));
        mandatoryMap.put("txtAddress", new Boolean(true));
        mandatoryMap.put("txtEmployerName", new Boolean(true));
        mandatoryMap.put("txtSalaryCertificateNo", new Boolean(true));
        mandatoryMap.put("txtTotalSalary", new Boolean(true));
        mandatoryMap.put("txtNetWorth", new Boolean(true));
        mandatoryMap.put("txtPinCode", new Boolean(true));
        mandatoryMap.put("cboCity", new Boolean(true));
        mandatoryMap.put("tdtRetirementDt", new Boolean(true));
        mandatoryMap.put("txtMemNetworth", new Boolean(true));
        mandatoryMap.put("txtContactNum", new Boolean(true));
        mandatoryMap.put("txtMemType", new Boolean(true));
        mandatoryMap.put("txtMemName", new Boolean(true));
        mandatoryMap.put("txtMemNo", new Boolean(true));
        mandatoryMap.put("txtSecurityRemarks", new Boolean(true));
        mandatoryMap.put("cboSecurityType", new Boolean(true));
        mandatoryMap.put("txtGoldRemarks", new Boolean(true));
        mandatoryMap.put("txtValueOfGold", new Boolean(true));
        mandatoryMap.put("txtNetWeight", new Boolean(true));
        mandatoryMap.put("txtGrossWeight", new Boolean(true));
        mandatoryMap.put("txtJewelleryDetails", new Boolean(true));
        mandatoryMap.put("txtDepAmount", new Boolean(true));
        mandatoryMap.put("txtMaturityDt", new Boolean(true));
        mandatoryMap.put("txtMaturityValue", new Boolean(true));
//        mandatoryMap.put("txtProductId", new Boolean(true));
        mandatoryMap.put("txtRateOfInterest", new Boolean(true));
        mandatoryMap.put("txtDepNo", new Boolean(true));
        mandatoryMap.put("tdtDepDt", new Boolean(true));
    }

    /* Auto Generated Method - getMandatoryHashMap()
     Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    private void initComponentData() {
        try {
            cboCity.setModel(observable.getCbmCity());
            cboSecurityType.setModel(observable.getCbmSecurity());
            cboOtherInstituion.setModel(observable.getCbmOtherInstituion());
            cboSecurityTypeSociety.setModel(observable.getCbmSecurityTypeSociety());
            cboProdType.setModel(observable.getCbmProdType());
            cboProductTypeSecurity.setModel(observable.getCbmProdTypeSecurity());
            cboProdId.setModel(observable.getCbmProdId());
            cboDepProdType.setModel(observable.getCbmDepProdID());
            cboCaseStatus.setModel(observable.getCbmCaseStatus());
            cboNature.setModel(observable.getCbmNature());
            cboRight.setModel(observable.getCbmRight());
            cboPledge.setModel(observable.getCbmPledge());
            cboDocumentType.setModel(observable.getCbmDocumentType());
            tblCaseTable.setModel(observable.getTblCaseDetails());
            tblOtherSocietyDetails.setModel(observable.getTblOtherSocietyDetails());
            tblCollateral.setModel(observable.getTblCollateralDetails());
            tblJointCollateral.setModel(observable.getTblJointCollateral());
        } catch (ClassCastException e) {
            parseException.logException(e, true);
        }
    }

    //Added By Suresh
    private void initRunComponentsCaseDetails() {
        java.awt.GridBagConstraints gridBagConstraints;
        panAssetDescription = new com.see.truetransact.uicomponent.CPanel();
        panCaseTableFieldsDetails = new com.see.truetransact.uicomponent.CPanel();
        lblCaseStatus = new com.see.truetransact.uicomponent.CLabel();
        cboCaseStatus = new com.see.truetransact.uicomponent.CComboBox();
        lblCaseNumber = new com.see.truetransact.uicomponent.CLabel();
        txtCaseNumber = new com.see.truetransact.uicomponent.CTextField();
        lblFillingDt = new com.see.truetransact.uicomponent.CLabel();
        tdtlFillingDt = new com.see.truetransact.uicomponent.CDateField();
        lblFillingFees = new com.see.truetransact.uicomponent.CLabel();
        txtFillingFees = new com.see.truetransact.uicomponent.CTextField();
        lblMiscCharges = new com.see.truetransact.uicomponent.CLabel();
        txtMiscCharges = new com.see.truetransact.uicomponent.CTextField();
        panCaseDetailBtn = new com.see.truetransact.uicomponent.CPanel();
        btnCaseNew = new com.see.truetransact.uicomponent.CButton();
        btnCaseSave = new com.see.truetransact.uicomponent.CButton();
        btnCaseDelete = new com.see.truetransact.uicomponent.CButton();
        panScheduleTable = new com.see.truetransact.uicomponent.CPanel();
        srpCaseTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblCaseTable = new com.see.truetransact.uicomponent.CTable();
        tbrTokenConfig = new com.see.truetransact.uicomponent.CToolBar();
        panCaseTableFields.setLayout(new java.awt.GridBagLayout());

        panCaseTableFields.setBorder(new javax.swing.border.TitledBorder("Case Details"));
        panCaseTableFields.setMinimumSize(new java.awt.Dimension(800, 350));
        panCaseTableFields.setPreferredSize(new java.awt.Dimension(800, 350));
        panCaseTableFieldsDetails.setLayout(new java.awt.GridBagLayout());

        panCaseTableFieldsDetails.setMinimumSize(new java.awt.Dimension(300, 300));
        panCaseTableFieldsDetails.setPreferredSize(new java.awt.Dimension(300, 300));
        lblCaseStatus.setText("Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCaseTableFieldsDetails.add(lblCaseStatus, gridBagConstraints);

        cboCaseStatus.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCaseStatus.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;

        cboCaseStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCaseStatusActionPerformed(evt);
            }
        });

        panCaseTableFieldsDetails.add(cboCaseStatus, gridBagConstraints);

        lblCaseNumber.setText("Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCaseTableFieldsDetails.add(lblCaseNumber, gridBagConstraints);

        txtCaseNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseTableFieldsDetails.add(txtCaseNumber, gridBagConstraints);

        lblFillingDt.setText("Filling Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCaseTableFieldsDetails.add(lblFillingDt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseTableFieldsDetails.add(tdtlFillingDt, gridBagConstraints);

        lblFillingFees.setText("Filling Fees");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCaseTableFieldsDetails.add(lblFillingFees, gridBagConstraints);

        txtFillingFees.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseTableFieldsDetails.add(txtFillingFees, gridBagConstraints);

        lblMiscCharges.setText("Misc Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCaseTableFieldsDetails.add(lblMiscCharges, gridBagConstraints);

        txtMiscCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseTableFieldsDetails.add(txtMiscCharges, gridBagConstraints);

        panCaseDetailBtn.setLayout(new java.awt.GridBagLayout());

        panCaseDetailBtn.setMinimumSize(new java.awt.Dimension(95, 35));
        panCaseDetailBtn.setPreferredSize(new java.awt.Dimension(95, 35));
        btnCaseNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif")));
        btnCaseNew.setToolTipText("New");
        btnCaseNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnCaseNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnCaseNew.setPreferredSize(new java.awt.Dimension(29, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCaseDetailBtn.add(btnCaseNew, gridBagConstraints);

        btnCaseSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif")));
        btnCaseSave.setToolTipText("Save");
        btnCaseSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnCaseSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnCaseSave.setName("btnContactNoAdd");
        btnCaseSave.setPreferredSize(new java.awt.Dimension(29, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCaseDetailBtn.add(btnCaseSave, gridBagConstraints);

        btnCaseDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif")));
        btnCaseDelete.setToolTipText("Delete");
        btnCaseDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnCaseDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnCaseDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCaseDetailBtn.add(btnCaseDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(10, 7, 0, 0);
        panCaseTableFieldsDetails.add(panCaseDetailBtn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCaseTableFields.add(panCaseTableFieldsDetails, gridBagConstraints);

        panScheduleTable.setLayout(new java.awt.GridBagLayout());

        srpCaseTable.setMinimumSize(new java.awt.Dimension(450, 200));
        srpCaseTable.setPreferredSize(new java.awt.Dimension(450, 200));
        tblCaseTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
            "Status", "Number", "File Date"
        }));
        tblCaseTable.setPreferredScrollableViewportSize(new java.awt.Dimension(250, 400));
        srpCaseTable.setViewportView(tblCaseTable);

        panScheduleTable.add(srpCaseTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panCaseTableFields.add(panScheduleTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails_Table1.add(panCaseTableFields, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panCaseDetails.add(panSanctionDetails_Table1, gridBagConstraints);


        btnCaseNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCaseNewActionPerformed(evt);
            }
        });

        btnCaseSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCaseSaveActionPerformed(evt);
            }
        });

        btnCaseDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCaseDeleteActionPerformed(evt);
            }
        });


        tdtlFillingDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtlFillingDtFocusLost(evt);
            }
        });


        tblCaseTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblCaseTableMousePressed(evt);
            }
        });

        srpCaseTable.setViewportView(tblCaseTable);
    }

    private void btnCaseNewActionPerformed(java.awt.event.ActionEvent evt) {
        updateMode = false;
        btnCaseNew.setEnabled(false);
        btnCaseSave.setEnabled(true);
        btnCaseDelete.setEnabled(false);
        observable.setNewCaseData(true);
        ClientUtil.enableDisable(panCaseTableFields, true);
        txtFillingFees.setEnabled(false);
    }

    private void btnCaseSaveActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        try {
            updateCaseOBFields();
            String caseStatus = (String) cboCaseStatus.getSelectedItem();
            if (tblCaseTable.getRowCount() > 0) {
                for (int i = 0; i < tblCaseTable.getRowCount(); i++) {
                    String caseState = CommonUtil.convertObjToStr(tblCaseTable.getValueAt(i, 0));
                    if (caseStatus.equalsIgnoreCase(caseState) && !updateMode) {
                        ClientUtil.displayAlert("Case Status Already Exists in this Table");
                        cboCaseStatus.setSelectedItem("");
                        return;
                    }
                }
            }
            if (caseStatus.length() > 0) {
                observable.addToCaseTable(updateTab);
                tblCaseTable.setModel(observable.getTblCaseDetails());
                observable.resetCaseDetails();
                resetCaseDetails();
                ClientUtil.enableDisable(panCaseTableFields, false);
                btnCaseNew.setEnabled(true);
                btnCaseSave.setEnabled(false);
                btnCaseDelete.setEnabled(false);
            } else {
                ClientUtil.showAlertWindow("Case Status Should Not be Empty !!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetCaseDetails() {
        cboCaseStatus.setSelectedItem("");
        txtCaseNumber.setText("");
        tdtlFillingDt.setDateValue("");
        txtFillingFees.setText("");
        txtMiscCharges.setText("");
    }

    private void btnCaseDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        String caseStatus = CommonUtil.convertObjToStr(tblCaseTable.getValueAt(tblCaseTable.getSelectedRow(), 0));
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            HashMap costMap = new HashMap();
            HashMap expenseMap = new HashMap();
            String costType = "";
            String expType = "";
            if (caseStatus.equals("Arbitration Case")) {
                costType = "ARC Cost";
                expType = "ARC Expense";
            } else if (caseStatus.equals("Execution of Award")) {
                costType = "EA Cost";
                expType = "EA Expense";
            } else if (caseStatus.equals("Execution Process")) {
                costType = "EP Cost";
                expType = "EP Expense";
            }
            costMap.put("ACT_NUM", CommonUtil.convertObjToStr(txtChittalNo.getText()));
            costMap.put("CHARGE_TYPE", costType);
            List costLst = ClientUtil.executeQuery("getTermLoanChargePaidAmount", costMap);
            expenseMap.put("CHARGE_TYPE", expType);
            expenseMap.put("ACT_NUM", CommonUtil.convertObjToStr(txtChittalNo.getText()));
            List expenseLst = ClientUtil.executeQuery("getTermLoanChargePaidAmount", expenseMap);
            String message = "";
            if (costLst != null && costLst.size() > 0) {
                message += "Transaction already made for " + costType;
            }
            if (expenseLst != null && expenseLst.size() > 0) {
                message += "\nTransaction already made for " + expType;
            }
            if (message.length() > 0) {
                ClientUtil.displayAlert(message);
                return;
            }
        }
        observable.deleteCaseTableData(caseStatus, tblCaseTable.getSelectedRow());
        observable.resetCaseDetails();
        resetCaseDetails();
        btnCaseNew.setEnabled(true);
        btnCaseSave.setEnabled(false);
        btnCaseDelete.setEnabled(false);
        ClientUtil.enableDisable(panCaseTableFields, false);
    }

    private void cboCaseStatusActionPerformed(java.awt.event.ActionEvent evt) {
        String caseStatus = CommonUtil.convertObjToStr(((ComboBoxModel) cboCaseStatus.getModel()).getKeyForSelected()).toString();
        if (caseStatus.length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("CHARGE_TYPE", caseStatus);
            List lst = ClientUtil.executeQuery("getChargecalcDetails", whereMap);
            if (lst != null && lst.size() > 0) {
                double limit = 0.0;
                HashMap caseMap = new HashMap();
                caseMap.put("SCHEME_NAME", txtSchemeName.getText());
                List totalSchemeAmtLst = ClientUtil.executeQuery("getTotalAmountPerMember", caseMap);
                if (totalSchemeAmtLst != null && totalSchemeAmtLst.size() > 0) {
                    caseMap = (HashMap) totalSchemeAmtLst.get(0);
                    limit = CommonUtil.convertObjToDouble(caseMap.get("TOTAL__AMOUNT")).doubleValue();
                    whereMap = (HashMap) lst.get(0);
                    if (CommonUtil.convertObjToStr(whereMap.get("CHARGE_BASE")).equals("Amount Range")) {
                        for (int i = 0; i < lst.size(); i++) {
                            HashMap dataMap = (HashMap) lst.get(i);
                            if (CommonUtil.convertObjToDouble(dataMap.get("FROM_SLAB_AMT")).doubleValue() <= limit
                                    && CommonUtil.convertObjToDouble(dataMap.get("TO_SLAB_AMT")).doubleValue() >= limit) {
                                double chargeRate = CommonUtil.convertObjToDouble(whereMap.get("SLAB_CHARGE_RATE")).doubleValue();
                                double fillingFees = (double) limit * chargeRate / 100;
                                double minChargeAmt = CommonUtil.convertObjToDouble(whereMap.get("SLAB_MIN_CHARGE_AMOUNT")).doubleValue();
                                double maxChargeAmt = CommonUtil.convertObjToDouble(whereMap.get("SLAB_MAX_CHARGE_AMOUNT")).doubleValue();
                                txtFillingFees.setText(String.valueOf(fillingFees));
                                break;
                            }
                        }
                    } else if (CommonUtil.convertObjToStr(whereMap.get("CHARGE_BASE")).equals("Flat Charge")) {
                        double flatCharge = CommonUtil.convertObjToDouble(whereMap.get("FLAT_CHARGE")).doubleValue();
                        double minChargeAmt = CommonUtil.convertObjToDouble(whereMap.get("MIN_CHARGE_AMOUNT")).doubleValue();
                        double maxChargeAmt = CommonUtil.convertObjToDouble(whereMap.get("MAX_CHARGE_AMOUNT")).doubleValue();
                        txtFillingFees.setText(String.valueOf(flatCharge));
                    } else if (CommonUtil.convertObjToStr(whereMap.get("CHARGE_BASE")).equals("Sanction Amount")) {
                        double chargeRate = CommonUtil.convertObjToDouble(whereMap.get("CHARGE_RATE")).doubleValue();
                        double fillingFees = (double) limit * chargeRate / 100;
                        double minChargeAmt = CommonUtil.convertObjToDouble(whereMap.get("MIN_CHARGE_AMOUNT")).doubleValue();
                        double maxChargeAmt = CommonUtil.convertObjToDouble(whereMap.get("MAX_CHARGE_AMOUNT")).doubleValue();
                        txtFillingFees.setText(String.valueOf(fillingFees));
                    }
                }
            }
        }
    }

    private void tdtlFillingDtFocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        if (tdtlFillingDt.getDateValue() != null && tdtlFillingDt.getDateValue().length() > 0) {
            ToDateValidation toDate = new ToDateValidation((Date) currDate.clone(), true);
            toDate.setComponent(this.tdtlFillingDt);
            if (!toDate.validate()) {
                COptionPane.showMessageDialog(this, "Future Date Not Allowed");
                tdtlFillingDt.setDateValue("");
                tdtlFillingDt.requestFocus();
                return;
            }
        }
    }

    public void updateCaseOBFields() {
        observable.setCboCaseStatus((String) cboCaseStatus.getSelectedItem());
        observable.setTxtCaseNumber(txtCaseNumber.getText());
        observable.setTdtlFillingDt(tdtlFillingDt.getDateValue());
        observable.setTxtFillingFees(txtFillingFees.getText());
        observable.setTxtMiscCharges(txtMiscCharges.getText());
    }

    private void tblCaseTableMousePressed(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        updateCaseOBFields();
        updateMode = true;
        updateTab = tblCaseTable.getSelectedRow();
        observable.setNewCaseData(false);
        String st = CommonUtil.convertObjToStr(tblCaseTable.getValueAt(tblCaseTable.getSelectedRow(), 0));
        observable.populateCaseDetails(st);
        populateCaseDetails();
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            btnCaseNew.setEnabled(false);
            btnCaseSave.setEnabled(false);
            btnCaseDelete.setEnabled(false);
            ClientUtil.enableDisable(panCaseTableFields, false);
            cboCaseStatus.setEnabled(false);
        } else {
            btnCaseNew.setEnabled(false);
            btnCaseSave.setEnabled(true);
            btnCaseDelete.setEnabled(true);
            ClientUtil.enableDisable(panCaseTableFields, true);
            cboCaseStatus.setEnabled(false);
            txtFillingFees.setEnabled(false);
            cboCaseStatus.setEnabled(false);
        }
    }

    public void populateCaseDetails() {
        cboCaseStatus.setSelectedItem(observable.getCboCaseStatus());
        txtCaseNumber.setText(observable.getTxtCaseNumber());
        txtFillingFees.setText(observable.getTxtFillingFees());
        txtMiscCharges.setText(observable.getTxtMiscCharges());
        tdtlFillingDt.setDateValue(observable.getTdtlFillingDt());
    }

    /**
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
    }

    public void populateMasterMaintenanceFields() {
     
        //        txtRemarks.setText(observable.getTxtRemarks());
        txtOverDueAmount.setText(CommonUtil.convertObjToStr(observable.getTxtOverDueAmount()));   //AJITH
        txtTotalAmountTillDate.setText(CommonUtil.convertObjToStr(observable.getTxtTotalAmountTillDate()));   //AJITH
        txtLastInstNo.setText(observable.getTxtLastInstNo());
        txtOverDueInstallments.setText(CommonUtil.convertObjToStr(observable.getTxtOverDueInstallments()));   //AJITH
        tdtLastInstDate.setDateValue(observable.getTdtLastInstDate());
        txtChittalNo.setText(observable.getTxtChittalNo());
        tdtChitStartDt.setDateValue(observable.getTdtChitStartDt());
        tdtChitCloseDt.setDateValue(observable.getTdtChitCloseDt());
        txtDivisionNo.setText(CommonUtil.convertObjToStr(observable.getTxtDivisionNo()));   //AJITH
        txtSubNo.setText(CommonUtil.convertObjToStr(observable.getTxtSubNo())); //AJITH
        txtSchemeName.setText(observable.getTxtSchemeName());
        txtMemberNo.setText(observable.getTxtMemberNo());
        //system.out.println("    observable.getTxtBordNo()    " + observable.getTxtBordNo());
        txtBondNo.setText(observable.getTxtBordNo());
         System.out.println("BB33 -- "+txtBondNo.getText()); 
        tdtBondDt.setDateValue(observable.getTdtBordDt());
        if (tdtBondDt.getDateValue().equals("")) {
            tdtBondDt.setDateValue(CommonUtil.convertObjToStr(currDate));
        }
        txtPrizedAmount.setText(CommonUtil.convertObjToStr(observable.getTxtPrizedAmount()));   //AJITH
        tdtResolutionDt.setDateValue(observable.getTdtResolutionDt());
        tdtPayDt.setDateValue(observable.getTdtPayDt());
        txtResolutionNo.setText(observable.getTxtResolutionNo());
        txtContactNo.setText(observable.getTxtContactNo());
        txtApplicationNo.setText(observable.getApplicationNo());
        tdtApplicationDate.setDateValue(observable.getApplicationDate());
        ResolutionSearch.setEnabled(true);
        txaApplicationSecRemarks.setText(observable.getApplicationSecurityRemarks());
        if (observable.getCbxDefaulter() != null && !observable.getCbxDefaulter().equals("") && observable.getCbxDefaulter().equals("Y")) {
            cbxDefaulter.setSelected(true);
            cbxDefaulterActionPerformed(null);
        } else {
            cbxDefaulter.setSelected(false);
        }
        cboSecurityType.setSelectedItem(observable.getCboSecurityType());
        txtSecurityValue.setText(observable.getLblSecurityValue());
        txtGoldRemarks.setText(observable.getTxtGoldRemarks());
        chkStandingInstn.setSelected(observable.getChkStandingInstn());
        if (chkStandingInstn.isSelected() == true) {
            setVisibleStandingIns(true);
            txtCustomerIdCr.setText(observable.getTxtCustomerIdCr());
        } else {
            setVisibleStandingIns(false);
        }
        if (observable.getChkNominee() == true) {
            chkNominee.setSelected(true);
        } else {
            chkNominee.setSelected(false);
            tabMasterMaintenanceDetails.remove(nomineeUi);
            tabMasterMaintenanceDetails.resetVisits();
        }
        if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).length() > 0 && CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
            String salaryRecovery = CommonUtil.convertObjToStr(observable.getRdoSalaryRecovery());
            if (salaryRecovery.length() > 0 && salaryRecovery.equals("Y")) {
                rdoSalaryRecovery_Yes.setSelected(true);
            } else {
                rdoSalaryRecovery_No.setSelected(true);
            }
            String lockStatus = CommonUtil.convertObjToStr(observable.getLockStatus());
            if (lockStatus.length() > 0 && lockStatus.equals("Y")) {
                lblLockStatusVal.setText("LOCKED");
            } else {
                lblLockStatusVal.setText("UNLOCKED");
            }
        }
        System.out.println("observable.getTxtGoldSecurityId() :: " + observable.getTxtGoldSecurityId());
        System.out.println("observable.getRdoGoldSecurityStockExists() :: " + observable.getRdoGoldSecurityStockExists());
        txtGoldSecurityId.setText(observable.getTxtGoldSecurityId());
        if(observable.getRdoGoldSecurityStockExists().equalsIgnoreCase("Y")){ // Added by nithya on 07-03-2020 for KD-1379
            rdoGoldSecurityExitsYes.setSelected(true);
            rdoGoldSecurityExitsNo.setSelected(false);
            rdoGoldSecurityExitsYesActionPerformed(null);
        }else{
            rdoGoldSecurityExitsYes.setSelected(false);
            rdoGoldSecurityExitsNo.setSelected(true);
        }
        txtValueOfGold.setText(observable.getTxtValueOfGold());
        txtNetWeight.setText(CommonUtil.convertObjToStr(observable.getTxtNetWeight())); //AJITH
        txtGrossWeight.setText(CommonUtil.convertObjToStr(observable.getTxtGrossWeight())); //AJITH
        txtJewelleryDetails.setText(observable.getTxtJewelleryDetails());
        //lblMembershipName.setText(observable.getLblMemberName());
        lblMembershipType.setText(observable.getLblMemberType());
        lblNomineName.setText(observable.getLblNomineeName());
        txtDivisionNo.setEnabled(false);
        txtSubNo.setEnabled(false);
        tdtChitStartDt.setEnabled(false);
        tdtPayDt.setEnabled(false);
        txtPrizedAmount.setEnabled(false);
    }

    /* Auto Generated Method - updateOBFields()
     This method called by Save option of UI.
     It updates the OB with UI data.*/
    public void updateMasterMaintenanceFields() {
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setTxtOverDueAmount(CommonUtil.convertObjToDouble(txtOverDueAmount.getText()));  //AJITH
        observable.setTxtTotalAmountTillDate(CommonUtil.convertObjToDouble(txtTotalAmountTillDate.getText()));  //AJITH
        observable.setTxtLastInstNo(txtLastInstNo.getText());
        observable.setTxtOverDueInstallments(CommonUtil.convertObjToInt(txtOverDueInstallments.getText()));  //AJITH
        observable.setTdtLastInstDate(tdtLastInstDate.getDateValue());
        observable.setTxtChittalNo(txtChittalNo.getText());
        observable.setTdtChitStartDt(tdtChitStartDt.getDateValue());
        observable.setTdtChitCloseDt(tdtChitCloseDt.getDateValue());
        observable.setTxtDivisionNo(CommonUtil.convertObjToInt(txtDivisionNo.getText()));  //AJITH
        observable.setTxtSubNo(CommonUtil.convertObjToInt(txtSubNo.getText()));  //AJITH
        observable.setTxtSchemeName(txtSchemeName.getText());
        observable.setTxtMemberNo(txtMemberNo.getText());
        observable.setTxtBordNo(txtBondNo.getText());
        observable.setBondNoSet(bondNoset);
        observable.setTdtBordDt(tdtBondDt.getDateValue());
        observable.setTxtPrizedAmount(CommonUtil.convertObjToDouble(txtPrizedAmount.getText()));   //AJITH
        observable.setTdtResolutionDt(tdtResolutionDt.getDateValue());
        observable.setTdtPayDt(tdtPayDt.getDateValue());
        observable.setTxtResolutionNo(txtResolutionNo.getText());
        observable.setApplicationNo(txtApplicationNo.getText());
        observable.setApplicationDate(tdtApplicationDate.getDateValue());
        if (cbxDefaulter.isSelected()) {
            observable.setCbxDefaulter("Y");
        } else {
            observable.setCbxDefaulter("N");
        }
//        observable.setTxtContactNo(txtContactNo.getText());
//        observable.setTxtMemberNum(txtMemberNum.getText());
//        observable.setTxtSalaryRemark(txtSalaryRemark.getText());
//        observable.setTxtDesignation(txtDesignation.getText());
//        observable.setTxtAddress(txtAddress.getText());
//        observable.setTxtEmployerName(txtEmployerName.getText());
//        observable.setTxtSalaryCertificateNo(txtSalaryCertificateNo.getText());
//        observable.setTxtTotalSalary(txtTotalSalary.getText());
//        observable.setTxtNetWorth(txtNetWorth.getText());
//        observable.setTxtPinCode(txtPinCode.getText());
//        observable.setCboCity((String) cboCity.getSelectedItem());
//        observable.setTdtRetirementDt(tdtRetirementDt.getDateValue());
        observable.setTxtSecurityRemarks(txtSecurityRemarks.getText());
        observable.setCboSecurityType((String) cboSecurityType.getSelectedItem());
        observable.setLblSecurityValue(txtSecurityValue.getText());
        observable.setTxtGoldRemarks(txtGoldRemarks.getText());
        observable.setChkStandingInstn(chkStandingInstn.isSelected());
        if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).length() > 0 && CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
            if (rdoSalaryRecovery_Yes.isSelected() == true) {
                observable.setRdoSalaryRecovery("Y");
            } else {
                observable.setRdoSalaryRecovery("N");
            }
        } else {
            observable.setRdoSalaryRecovery("");
        }
        observable.setTxtCustomerIdCr(txtCustomerIdCr.getText());
        if (chkNominee.isSelected() == true) {
            observable.setChkNominee(true);
        } else {
            observable.setChkNominee(false);
        }
        if(rdoGoldSecurityExitsYes.isSelected()){  // Added by nithya on 07-03-2020 for KD-1379
            observable.setRdoGoldSecurityStockExists("Y");
        }else{
            observable.setRdoGoldSecurityStockExists("N"); 
        }if(rdoGoldSecurityExitsNo.isSelected()){
           observable.setRdoGoldSecurityStockExists("N"); 
        }        
        observable.setTxtGoldSecurityId(txtGoldSecurityId.getText());
        observable.setTxtValueOfGold(txtValueOfGold.getText());
        observable.setTxtNetWeight(CommonUtil.convertObjToDouble(txtNetWeight.getText()));  //AJITH
        observable.setTxtGrossWeight(CommonUtil.convertObjToDouble(txtGrossWeight.getText()));  //AJITH
        observable.setTxtJewelleryDetails(txtJewelleryDetails.getText());
        observable.setLblMemberName(lblMembershipName.getText());
        observable.setLblMemberType(lblMembershipType.getText());
        observable.setLblNomineeName(lblNomineName.getText());
        observable.setSalarySecDetails(bufferList);
        observable.setApplicationNo(txtApplicationNo.getText());
        observable.setApplicationDate(tdtApplicationDate.getDateValue());
        observable.setApplicationSet(applicationNoset);
        observable.setApplicationSecurityRemarks(txaApplicationSecRemarks.getText());
        if (cbxOnlyApplication.isSelected()) {
            observable.setCbxOnlyAppliction("Y");
            observable.setTxtBordNo("");
            observable.setBondNoSet("N");

        } else {
            observable.setCbxOnlyAppliction("N");
        }
        observable.setScreen(this.getScreen());
    }

    public void populateMemberTypeFields() {
        txtMemNo.setText(observable.getTxtMemNo());
        txtMemName.setText(observable.getTxtMemName());
        txtMemType.setText(observable.getTxtMemType());
        txtContactNum.setText(observable.getTxtContactNum());
        txtMemNetworth.setText(observable.getTxtMemNetworth());
        txtMemPriority.setText(observable.getTxtMemPriority());
    }

    public void updateMemberTypeFields() {
        observable.setTxtChittalNo(txtChittalNo.getText());
        observable.setTxtSubNo(CommonUtil.convertObjToInt(txtSubNo.getText())); //AJITH
        observable.setTxtMemNo(txtMemNo.getText());
        observable.setTxtMemName(txtMemName.getText());
        observable.setTxtMemType(txtMemType.getText());
        observable.setTxtContactNum(txtContactNum.getText());
        observable.setTxtMemNetworth(txtMemNetworth.getText());
        observable.setTxtMemPriority(txtMemPriority.getText());
    }

    public void populateDepositTypeFields() {
        txtDepNo.setText(observable.getTxtDepNo());
//        txtProductId.setText(observable.getTxtProductId());
        cboProductTypeSecurity.setSelectedItem(observable.getCboProductTypeSecurity());
        cboDepProdType.setSelectedItem(observable.getCboDepProdID());
        tdtDepDt.setDateValue(observable.getTdtDepDt());
        txtDepAmount.setText(observable.getTxtDepAmount());
        txtRateOfInterest.setText(observable.getTxtRateOfInterest());
        txtMaturityValue.setText(observable.getTxtMaturityValue());
        txtMaturityDt.setDateValue(observable.getTxtMaturityDt());
        if (observable.getCbxSamechittal() != null) {
            //system.out.println("CBXXXXXXXXXXXXX" + observable.getCbxSamechittal());
            if (observable.getCbxSamechittal().equals("Y")) {
                cbxSameChittal.setSelected(true);
            } else {
                cbxSameChittal.setSelected(false);
            }
        } else {
            cbxSameChittal.setSelected(false);
        }

    }

    public void populateSocietyTypeFields() {
        cboOtherInstituion.setSelectedItem(observable.getCboOtherInstituion());
        cboSecurityTypeSociety.setSelectedItem(observable.getCboSecurityTypeSociety());
        txtOtherInstituionName.setText(observable.getTxtOtherInstituionName());
        txtSecurityNoSociety.setText(observable.getTxtSecurityNoSociety());
        txtSecurityAmountSociety.setText(observable.getTxtSecurityAmountSociety());
        txtMaturityValueSociety.setText(observable.getTxtMaturityValueSociety());
        txtRemarksSociety.setText(observable.getTxtRemarksSociety());
        tdtIssueDtSoceity.setDateValue(observable.getTdtIssueDtSoceity());
        tdtMaturityDateSociety.setDateValue(observable.getTdtMaturityDateSociety());
    }

    public void updateDepositTypeFields() {
        observable.setTxtChittalNo(txtChittalNo.getText());
        observable.setTxtSubNo(CommonUtil.convertObjToInt(txtSubNo.getText())); //AJITH
        observable.setTxtDepNo(txtDepNo.getText());
        observable.setCboProductTypeSecurity((String) cboProductTypeSecurity.getSelectedItem());
        observable.setCboDepProdID((String) cboDepProdType.getSelectedItem());
        observable.setTdtDepDt(tdtDepDt.getDateValue());
        observable.setTxtDepAmount(txtDepAmount.getText());
        observable.setTxtMaturityDt(txtMaturityDt.getDateValue());
        observable.setTxtMaturityValue(txtMaturityValue.getText());
        observable.setTxtRateOfInterest(txtRateOfInterest.getText());
        if (cbxSameChittal.isSelected()) {
            observable.setCbxSamechittal("Y");
        } else {
            observable.setCbxSamechittal("N");
        }
    }

    public void updateSocietyTypeFields() {
        observable.setCboOtherInstituion((String) cboOtherInstituion.getSelectedItem());
        observable.setCboSecurityTypeSociety((String) cboSecurityTypeSociety.getSelectedItem());
        observable.setTxtOtherInstituionName(txtOtherInstituionName.getText());
        observable.setTxtSecurityNoSociety(txtSecurityNoSociety.getText());
        observable.setTxtSecurityAmountSociety(txtSecurityAmountSociety.getText());
        observable.setTxtMaturityValueSociety(txtMaturityValueSociety.getText());
        observable.setTxtRemarksSociety(txtRemarksSociety.getText());
        observable.setTdtIssueDtSoceity(tdtIssueDtSoceity.getDateValue());
        observable.setTdtMaturityDateSociety(tdtMaturityDateSociety.getDateValue());
    }

    /* Auto Generated Method - setHelpMessage()
     This method shows tooltip help for all the input fields
     available in the UI. It needs the Mandatory Resource Bundle
     object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        MDSMasterMaintenanceMRB objMandatoryRB = new MDSMasterMaintenanceMRB();
        txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));
        txtOverDueAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOverDueAmount"));
        txtTotalAmountTillDate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalAmountTillDate"));
        txtLastInstNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLastInstNo"));
        txtOverDueInstallments.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOverDueInstallments"));
        tdtLastInstDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLastInstDate"));
        txtChittalNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChittalNo"));
        tdtChitStartDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtChitStartDt"));
        txtDivisionNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDivisionNo"));
        txtSchemeName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSchemeName"));
        txtMemberNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMemberNo"));
        txtBondNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBondNo"));
        cboCaseStatus.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCaseStatus"));
        txtCaseNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCaseNumber"));
        tdtlFillingDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtlFillingDt"));
        txtFillingFees.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFillingFees"));
        txtMiscCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMiscCharges"));
        tdtBondDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtBondDt"));
        txtPrizedAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPrizedAmount"));
        tdtResolutionDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtResolutionDt"));
        tdtPayDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtPayDt"));
        txtResolutionNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtResolutionNo"));
        txtContactNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtContactNo"));
        txtMemberNum.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMemberNum"));
        txtSalaryRemark.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSalaryRemark"));
        txtDesignation.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDesignation"));
        txtAddress.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAddress"));
        txtEmployerName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEmployerName"));
        txtSalaryCertificateNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSalaryCertificateNo"));
        txtTotalSalary.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalSalary"));
        txtNetWorth.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNetWorth"));
        txtPinCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPinCode"));
        cboCity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCity"));
        tdtRetirementDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtRetirementDt"));
        txtMemNetworth.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMemNetworth"));
        txtContactNum.setHelpMessage(lblMsg, objMandatoryRB.getString("txtContactNum"));
        txtMemType.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMemType"));
        txtMemName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMemName"));
        txtMemNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMemNo"));
        txtSecurityRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSecurityRemarks"));
        cboSecurityType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSecurityType"));
        txtGoldRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGoldRemarks"));
        txtValueOfGold.setHelpMessage(lblMsg, objMandatoryRB.getString("txtValueOfGold"));
        txtNetWeight.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNetWeight"));
        txtGrossWeight.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGrossWeight"));
        txtJewelleryDetails.setHelpMessage(lblMsg, objMandatoryRB.getString("txtJewelleryDetails"));
        txtDepAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDepAmount"));
        txtMaturityDt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaturityDt"));
        txtMaturityValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaturityValue"));
//        txtProductId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProductId"));
        txtRateOfInterest.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRateOfInterest"));
        txtDepNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDepNo"));
        tdtDepDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDepDt"));
    }

    private void setMaxLengths() {
        txtTotalAmountTillDate.setValidation(new CurrencyValidation());
        txtOverDueAmount.setValidation(new CurrencyValidation());
        txtPrizedAmount.setValidation(new CurrencyValidation());
        txtTotalSalary.setValidation(new CurrencyValidation());
        txtValueOfGold.setValidation(new CurrencyValidation());
        txtDepAmount.setValidation(new CurrencyValidation());
        txtOverDueInstallments.setValidation(new NumericValidation());
        txtResolutionNo.setAllowAll(true);
        txtBondNo.setAllowAll(true);
        txtLastInstNo.setAllowAll(true);
        txtRemarks.setAllowAll(true);
        txtSalaryCertificateNo.setAllowAll(true);
        txtPinCode.setAllowNumber(true);
        txtContactNo.setAllowNumber(true);
        txtMemberNum.setAllowAll(true);
        txtNetWorth.setAllowAll(true);
        txtContactNum.setAllowAll(true);
        txtMemNetworth.setAllowAll(true);
        txtGrossWeight.setValidation(new NumericValidation(3, 2));
        txtNetWeight.setValidation(new NumericValidation(3, 2));
        txtValueOfGold.setAllowAll(true);
        txtGoldRemarks.setAllowAll(true);
        txtDepNo.setAllowAll(true);
        txtDivisionNo.setAllowAll(true);
        txtSubNo.setValidation(new NumericValidation());
        txtRateOfInterest.setValidation(new NumericValidation(3, 2));
        txtMaturityValue.setValidation(new CurrencyValidation());
        txtMemNo.setAllowAll(true);
        txtMemberNo.setAllowAll(true);
//        txtProductId.setAllowAll(true);
        txtSecurityValue.setAllowAll(true);
        txtCaseNumber.setAllowAll(true);
        txtFillingFees.setValidation(new CurrencyValidation());
        txtMiscCharges.setValidation(new CurrencyValidation());
        txtDocumentNo.setAllowAll(true);
        txtOwnerMemNo.setAllowAll(true);
        txtVillage.setAllowAll(true);
        txtPledgeNo.setAllowAll(true);
        txtSurveyNo.setAllowAll(true);
        txtTotalArea.setAllowAll(true);
        txtPledgeAmount.setValidation(new CurrencyValidation(14, 2));
        txtOtherInstituionName.setAllowAll(true);
        txtSecurityNoSociety.setAllowAll(true);
        txtSecurityAmountSociety.setValidation(new CurrencyValidation(14, 2));
        txtMaturityValueSociety.setValidation(new CurrencyValidation(14, 2));
        txtRemarksSociety.setAllowAll(true);
    }

    private void initComponentSecurity() {

        panCollateralTypeDetails = new com.see.truetransact.uicomponent.CPanel();
        panCollateralTable = new com.see.truetransact.uicomponent.CPanel();
        panCollateralJointTable = new com.see.truetransact.uicomponent.CPanel();
        srpCollateralTable = new com.see.truetransact.uicomponent.CScrollPane();
        srpCollateralJointTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblCollateral = new com.see.truetransact.uicomponent.CTable();
        tblJointCollateral = new com.see.truetransact.uicomponent.CTable();
        panCollatetalDetails = new com.see.truetransact.uicomponent.CPanel();
        lblOwnerMemberNo = new com.see.truetransact.uicomponent.CLabel();
        lblDocumentNo = new com.see.truetransact.uicomponent.CLabel();
        txtDocumentNo = new com.see.truetransact.uicomponent.CTextField();
        lblDocumentType = new com.see.truetransact.uicomponent.CLabel();
        cboDocumentType = new com.see.truetransact.uicomponent.CComboBox();
        lblDocumentDate = new com.see.truetransact.uicomponent.CLabel();
        lblRegisteredOffice = new com.see.truetransact.uicomponent.CLabel();
        txtRegisteredOffice = new com.see.truetransact.uicomponent.CTextField();
        lblOwnerMemberNname = new com.see.truetransact.uicomponent.CLabel();
        txtOwnerMemberNname = new com.see.truetransact.uicomponent.CTextField();
        tdtPledgeDate = new com.see.truetransact.uicomponent.CDateField();
        lblPledgeDate = new com.see.truetransact.uicomponent.CLabel();
        lblPledgeNo = new com.see.truetransact.uicomponent.CLabel();
        txtPledgeNo = new com.see.truetransact.uicomponent.CTextField();
        lblPledge = new com.see.truetransact.uicomponent.CLabel();
        lblVillage = new com.see.truetransact.uicomponent.CLabel();
        txtVillage = new com.see.truetransact.uicomponent.CTextField();
        lblSurveyNo = new com.see.truetransact.uicomponent.CLabel();
        txtSurveyNo = new com.see.truetransact.uicomponent.CTextField();
        lblRight = new com.see.truetransact.uicomponent.CLabel();
        lblPledgeType = new com.see.truetransact.uicomponent.CLabel();
        txtPledgeType = new com.see.truetransact.uicomponent.CTextField();
        lblPledgeAmount = new com.see.truetransact.uicomponent.CLabel();
        txtPledgeAmount = new com.see.truetransact.uicomponent.CTextField();
        tdtDocumentDate = new com.see.truetransact.uicomponent.CDateField();
        cboNature = new com.see.truetransact.uicomponent.CComboBox();
        lblTotalArea = new com.see.truetransact.uicomponent.CLabel();
        txtTotalArea = new com.see.truetransact.uicomponent.CTextField();
        panBtnCollateralType = new com.see.truetransact.uicomponent.CPanel();
        btnCollateralNew = new com.see.truetransact.uicomponent.CButton();
        btnCollateralSave = new com.see.truetransact.uicomponent.CButton();
        btnCollateralDelete = new com.see.truetransact.uicomponent.CButton();
        srpTxtAreaParticulars = new com.see.truetransact.uicomponent.CScrollPane();
        txtAreaParticular = new com.see.truetransact.uicomponent.CTextArea();
        panOwnerMemberNumber = new com.see.truetransact.uicomponent.CPanel();
        panDocumentNumber = new com.see.truetransact.uicomponent.CPanel();
        txtOwnerMemNo = new com.see.truetransact.uicomponent.CTextField();
        btnOwnerMemNo = new com.see.truetransact.uicomponent.CButton();
        btnDocumentNo = new com.see.truetransact.uicomponent.CButton();
        cboPledge = new com.see.truetransact.uicomponent.CComboBox();
        lblNature = new com.see.truetransact.uicomponent.CLabel();
        cboRight = new com.see.truetransact.uicomponent.CComboBox();
        lblGahanYesNo = new com.see.truetransact.uicomponent.CLabel();
        panGahanYesNo = new com.see.truetransact.uicomponent.CPanel();
        rdoGahanYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoGahanNo = new com.see.truetransact.uicomponent.CRadioButton();
        rdoGahanGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        panCollateralTypeDetails1 = new com.see.truetransact.uicomponent.CPanel();

        panCollateralTypeDetails1.setLayout(new java.awt.GridBagLayout());
        //-------------------------------------------------table started-----------------------------one more table started
        panCollateralJointTable.setLayout(new java.awt.GridBagLayout());

        panCollateralJointTable.setMinimumSize(new java.awt.Dimension(460, 220));
        panCollateralJointTable.setPreferredSize(new java.awt.Dimension(460, 220));
        srpCollateralJointTable.setMinimumSize(new java.awt.Dimension(450, 180));
        srpCollateralJointTable.setPreferredSize(new java.awt.Dimension(450, 180));
        tblJointCollateral.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
            "Cust Id", "Name", "Constitution"
        }));
        tblJointCollateral.setMinimumSize(new java.awt.Dimension(375, 220));
        tblJointCollateral.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblJointCollateral.setPreferredSize(new java.awt.Dimension(375, 220));
        tblJointCollateral.setOpaque(false);

        srpCollateralJointTable.setViewportView(tblJointCollateral);

        panCollateralJointTable.add(srpCollateralJointTable, new java.awt.GridBagConstraints());
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panSecurityDetails1.add(panCollateralJointTable, gridBagConstraints);
        panCollateralTable.setLayout(new java.awt.GridBagLayout());

        panCollateralTable.setMinimumSize(new java.awt.Dimension(460, 210));
        panCollateralTable.setPreferredSize(new java.awt.Dimension(460, 210));
        srpCollateralTable.setMinimumSize(new java.awt.Dimension(450, 200));
        srpCollateralTable.setPreferredSize(new java.awt.Dimension(450, 200));
        tblCollateral.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
            "Member No", "Name", "Doc No", "PledgeAmt", "SurveyNo", "TotalArea"
        }));
        tblCollateral.setMinimumSize(new java.awt.Dimension(375, 750));
        tblCollateral.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblCollateral.setPreferredSize(new java.awt.Dimension(375, 750));
        tblCollateral.setOpaque(false);
        tblCollateral.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblCollateralMousePressed(evt);
            }
        });

        srpCollateralTable.setViewportView(tblCollateral);

        panCollateralTable.add(srpCollateralTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panSecurityDetails1.add(panCollateralTable, gridBagConstraints);

        panCollatetalDetails.setLayout(new java.awt.GridBagLayout());

        /*  ------------------------------------------------------------- gahan --------------------------------------------------------------------------------------------------------------*/
        panCollatetalDetails.setMinimumSize(new java.awt.Dimension(300, 570));
        panCollatetalDetails.setPreferredSize(new java.awt.Dimension(300, 570));
        panCollatetalDetails.setRequestFocusEnabled(false);
        lblGahanYesNo.setText("Gahan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCollatetalDetails.add(lblGahanYesNo, gridBagConstraints);

        panGahanYesNo.setLayout(new java.awt.GridBagLayout());
        rdoGahanYes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        panGahanYesNo.add(rdoGahanYes, gridBagConstraints);
        rdoGahanYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoGahanYesActionPerformed(evt);
            }
        });
        rdoGahanNo.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panGahanYesNo.add(rdoGahanNo, gridBagConstraints);
        rdoGahanNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoGahanNoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(panGahanYesNo, gridBagConstraints);

        lblOwnerMemberNo.setText("Owner Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblOwnerMemberNo, gridBagConstraints);


        lblOwnerMemberNname.setText("Owner Member Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblOwnerMemberNname, gridBagConstraints);

        txtOwnerMemberNname.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(txtOwnerMemberNname, gridBagConstraints);

        lblDocumentNo.setText("Document No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblDocumentNo, gridBagConstraints);

        panDocumentNumber.setLayout(new java.awt.GridBagLayout());

        txtDocumentNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;//1
        gridBagConstraints.gridy = 0;//3
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDocumentNumber.add(txtDocumentNo, gridBagConstraints);
        txtDocumentNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDocumentNoFocusLost(evt);
            }
        });

        btnDocumentNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnDocumentNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnDocumentNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDocumentNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDocumentNo.setEnabled(false);
        btnDocumentNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDocumentNoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;

        panDocumentNumber.add(btnDocumentNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;

        panCollatetalDetails.add(panDocumentNumber, gridBagConstraints);

        lblDocumentType.setText("Document Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblDocumentType, gridBagConstraints);

        cboDocumentType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(cboDocumentType, gridBagConstraints);

        lblDocumentDate.setText("Document Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblDocumentDate, gridBagConstraints);


        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(tdtDocumentDate, gridBagConstraints);

        lblRegisteredOffice.setText("Registered Office");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblRegisteredOffice, gridBagConstraints);

        txtRegisteredOffice.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(txtRegisteredOffice, gridBagConstraints);


        lblPledgeNo.setText("Pledge No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblPledgeNo, gridBagConstraints);

        txtPledgeNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(txtPledgeNo, gridBagConstraints);

        lblPledgeDate.setText("Pledge Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblPledgeDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(tdtPledgeDate, gridBagConstraints);

        lblPledgeType.setText("Pledge Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblPledgeType, gridBagConstraints);

        cboPledge.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(cboPledge, gridBagConstraints);

        lblPledgeAmount.setText("Pledge Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblPledgeAmount, gridBagConstraints);

        txtPledgeAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPledgeAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPledgeAmountFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(txtPledgeAmount, gridBagConstraints);


        lblVillage.setText("Village");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblVillage, gridBagConstraints);

        txtVillage.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(txtVillage, gridBagConstraints);

        lblSurveyNo.setText("Survey No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblSurveyNo, gridBagConstraints);

        txtSurveyNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(txtSurveyNo, gridBagConstraints);


        lblTotalArea.setText("Total Area (In Cents)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(lblTotalArea, gridBagConstraints);

        txtTotalArea.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCollatetalDetails.add(txtTotalArea, gridBagConstraints);

        lblNature.setText("Nature");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCollatetalDetails.add(lblNature, gridBagConstraints);

        cboNature.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCollatetalDetails.add(cboNature, gridBagConstraints);

        lblRight.setText("Right");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCollatetalDetails.add(lblRight, gridBagConstraints);

        cboRight.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCollatetalDetails.add(cboRight, gridBagConstraints);

        //       ---------------------------------------------------------------------------------------------------
        panBtnCollateralType.setLayout(new java.awt.GridBagLayout());

        panBtnCollateralType.setMinimumSize(new java.awt.Dimension(95, 35));
        panBtnCollateralType.setPreferredSize(new java.awt.Dimension(95, 35));
        btnCollateralNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif")));
        btnCollateralNew.setToolTipText("New");
        btnCollateralNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnCollateralNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnCollateralNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnCollateralNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCollateralNewActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnCollateralType.add(btnCollateralNew, gridBagConstraints);

        btnCollateralSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif")));
        btnCollateralSave.setToolTipText("Save");
        btnCollateralSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnCollateralSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnCollateralSave.setName("btnContactNoAdd");
        btnCollateralSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnCollateralSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCollateralSaveActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnCollateralType.add(btnCollateralSave, gridBagConstraints);

        btnCollateralDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif")));
        btnCollateralDelete.setToolTipText("Delete");
        btnCollateralDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnCollateralDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnCollateralDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnCollateralDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCollateralDeleteActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnCollateralType.add(btnCollateralDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 18);
        panCollatetalDetails.add(panBtnCollateralType, gridBagConstraints);

        srpTxtAreaParticulars.setMinimumSize(new java.awt.Dimension(150, 45));
        srpTxtAreaParticulars.setPreferredSize(new java.awt.Dimension(150, 45));
        txtAreaParticular.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtAreaParticular.setLineWrap(true);
        txtAreaParticular.setMaximumSize(new java.awt.Dimension(2, 14));
        txtAreaParticular.setMinimumSize(new java.awt.Dimension(2, 14));
        txtAreaParticular.setPreferredSize(new java.awt.Dimension(2, 14));
        srpTxtAreaParticulars.setViewportView(txtAreaParticular);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 2;
        panCollatetalDetails.add(srpTxtAreaParticulars, gridBagConstraints);

        panOwnerMemberNumber.setLayout(new java.awt.GridBagLayout());

        txtOwnerMemNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOwnerMemNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtOwnerMemNoFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOwnerMemberNumber.add(txtOwnerMemNo, gridBagConstraints);

        btnOwnerMemNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnOwnerMemNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnOwnerMemNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnOwnerMemNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnOwnerMemNo.setEnabled(false);
        btnOwnerMemNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOwnerMemNoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOwnerMemberNumber.add(btnOwnerMemNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCollatetalDetails.add(panOwnerMemberNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panSecurityDetails1.add(panCollatetalDetails, gridBagConstraints);

        txtMemNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMemNoFocusLost(evt);
            }
        });

        txtOwnerMemNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtOwnerMemNoFocusLost(evt);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgIsLapsedGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgEFTProductGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPayableBranchGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPrintServicesGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgSeriesGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgSalaryRecovery = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrAdvances = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace40 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace41 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace42 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace43 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace44 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace45 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        tabMasterMaintenanceDetails = new com.see.truetransact.uicomponent.CTabbedPane();
        panMasterMaintenanceDetails = new com.see.truetransact.uicomponent.CPanel();
        panInsideMasterMaintenanceDetails = new com.see.truetransact.uicomponent.CPanel();
        panInstallmentDetails = new com.see.truetransact.uicomponent.CPanel();
        txtOverDueAmount = new com.see.truetransact.uicomponent.CTextField();
        txtTotalAmountTillDate = new com.see.truetransact.uicomponent.CTextField();
        txtLastInstNo = new com.see.truetransact.uicomponent.CTextField();
        lblOverDueAmount = new com.see.truetransact.uicomponent.CLabel();
        lblTotalAmountTillDate = new com.see.truetransact.uicomponent.CLabel();
        lblLastInstDate = new com.see.truetransact.uicomponent.CLabel();
        lblLastInstNo = new com.see.truetransact.uicomponent.CLabel();
        txtOverDueInstallments = new com.see.truetransact.uicomponent.CTextField();
        lblOverDueInstallments = new com.see.truetransact.uicomponent.CLabel();
        tdtLastInstDate = new com.see.truetransact.uicomponent.CDateField();
        panMdsSchemeNameDetails = new com.see.truetransact.uicomponent.CPanel();
        lblChittalNo = new com.see.truetransact.uicomponent.CLabel();
        lblChitStartDt = new com.see.truetransact.uicomponent.CLabel();
        tdtChitStartDt = new com.see.truetransact.uicomponent.CDateField();
        lblSchemeName = new com.see.truetransact.uicomponent.CLabel();
        lblDivisionNo = new com.see.truetransact.uicomponent.CLabel();
        panSchemeName = new com.see.truetransact.uicomponent.CPanel();
        txtSchemeName = new com.see.truetransact.uicomponent.CTextField();
        panChittalNo = new com.see.truetransact.uicomponent.CPanel();
        txtChittalNo = new com.see.truetransact.uicomponent.CTextField();
        txtSubNo = new com.see.truetransact.uicomponent.CTextField();
        panChittalNo1 = new com.see.truetransact.uicomponent.CPanel();
        txtDivisionNo = new com.see.truetransact.uicomponent.CTextField();
        lblCoChit = new com.see.truetransact.uicomponent.CLabel();
        lblProdDesc = new com.see.truetransact.uicomponent.CLabel();
        lblChitCloseDt = new com.see.truetransact.uicomponent.CLabel();
        tdtChitCloseDt = new com.see.truetransact.uicomponent.CDateField();
        btnChittalNo = new com.see.truetransact.uicomponent.CButton();
        btnSchemeName = new com.see.truetransact.uicomponent.CButton();
        panMemberNoDetails = new com.see.truetransact.uicomponent.CPanel();
        lblMemberNo = new com.see.truetransact.uicomponent.CLabel();
        panMemberNo = new com.see.truetransact.uicomponent.CPanel();
        txtMemberNo = new com.see.truetransact.uicomponent.CTextField();
        btnMemberNo = new com.see.truetransact.uicomponent.CButton();
        lblNomineeName = new com.see.truetransact.uicomponent.CLabel();
        lblNomineName = new com.see.truetransact.uicomponent.CLabel();
        lblMemberType = new com.see.truetransact.uicomponent.CLabel();
        lblMembershipType = new com.see.truetransact.uicomponent.CLabel();
        lblMembershipName = new com.see.truetransact.uicomponent.CLabel();
        panPrizedDetails = new com.see.truetransact.uicomponent.CPanel();
        lblBordDt = new com.see.truetransact.uicomponent.CLabel();
        txtBondNo = new com.see.truetransact.uicomponent.CTextField();
        tdtBondDt = new com.see.truetransact.uicomponent.CDateField();
        lblBordNo = new com.see.truetransact.uicomponent.CLabel();
        panPrizeDetails = new com.see.truetransact.uicomponent.CPanel();
        lblResolutionDt = new com.see.truetransact.uicomponent.CLabel();
        lblPrizedAmount = new com.see.truetransact.uicomponent.CLabel();
        lblPayDt = new com.see.truetransact.uicomponent.CLabel();
        txtPrizedAmount = new com.see.truetransact.uicomponent.CTextField();
        tdtResolutionDt = new com.see.truetransact.uicomponent.CDateField();
        lblResolutionNo = new com.see.truetransact.uicomponent.CLabel();
        tdtPayDt = new com.see.truetransact.uicomponent.CDateField();
        txtResolutionNo = new com.see.truetransact.uicomponent.CTextField();
        ResolutionSearch = new com.see.truetransact.uicomponent.CButton();
        panSecurityDetails = new com.see.truetransact.uicomponent.CPanel();
        tabMasterMaintenance = new com.see.truetransact.uicomponent.CTabbedPane();
        panSalaryDetails = new com.see.truetransact.uicomponent.CPanel();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        lblSalaryCertificateNo = new com.see.truetransact.uicomponent.CLabel();
        lblSalaryRemark = new com.see.truetransact.uicomponent.CLabel();
        lblEmployerName = new com.see.truetransact.uicomponent.CLabel();
        lblTotalSalary = new com.see.truetransact.uicomponent.CLabel();
        lblDesignation = new com.see.truetransact.uicomponent.CLabel();
        lblMemberNum = new com.see.truetransact.uicomponent.CLabel();
        lblAddress = new com.see.truetransact.uicomponent.CLabel();
        lblPinCode = new com.see.truetransact.uicomponent.CLabel();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        lblRetirementDt = new com.see.truetransact.uicomponent.CLabel();
        lblContactNo = new com.see.truetransact.uicomponent.CLabel();
        lblNetWorth = new com.see.truetransact.uicomponent.CLabel();
        txtContactNo = new com.see.truetransact.uicomponent.CTextField();
        txtMemberNum = new com.see.truetransact.uicomponent.CTextField();
        txtSalaryRemark = new com.see.truetransact.uicomponent.CTextField();
        txtDesignation = new com.see.truetransact.uicomponent.CTextField();
        txtAddress = new com.see.truetransact.uicomponent.CTextField();
        txtEmployerName = new com.see.truetransact.uicomponent.CTextField();
        txtSalaryCertificateNo = new com.see.truetransact.uicomponent.CTextField();
        txtTotalSalary = new com.see.truetransact.uicomponent.CTextField();
        txtNetWorth = new com.see.truetransact.uicomponent.CTextField();
        txtPinCode = new com.see.truetransact.uicomponent.CTextField();
        cboCity = new com.see.truetransact.uicomponent.CComboBox();
        tdtRetirementDt = new com.see.truetransact.uicomponent.CDateField();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblSalaryDetails = new com.see.truetransact.uicomponent.CTable();
        panProperty1 = new com.see.truetransact.uicomponent.CPanel();
        btnNew_Property1 = new com.see.truetransact.uicomponent.CButton();
        btnlSave_Property1 = new com.see.truetransact.uicomponent.CButton();
        btnDelete_Property1 = new com.see.truetransact.uicomponent.CButton();
        panMemberTypeDetails = new com.see.truetransact.uicomponent.CPanel();
        panMemberTypeTable = new com.see.truetransact.uicomponent.CPanel();
        srpMemberType = new com.see.truetransact.uicomponent.CScrollPane();
        tblMemberType = new com.see.truetransact.uicomponent.CTable();
        panMemberDetails = new com.see.truetransact.uicomponent.CPanel();
        lblMemNo = new com.see.truetransact.uicomponent.CLabel();
        lblMemName = new com.see.truetransact.uicomponent.CLabel();
        lblMemType = new com.see.truetransact.uicomponent.CLabel();
        lblMemNetworth = new com.see.truetransact.uicomponent.CLabel();
        txtMemNetworth = new com.see.truetransact.uicomponent.CTextField();
        txtContactNum = new com.see.truetransact.uicomponent.CTextField();
        lblContactNum = new com.see.truetransact.uicomponent.CLabel();
        txtMemType = new com.see.truetransact.uicomponent.CTextField();
        txtMemName = new com.see.truetransact.uicomponent.CTextField();
        panMemberNumber = new com.see.truetransact.uicomponent.CPanel();
        txtMemNo = new com.see.truetransact.uicomponent.CTextField();
        btnMemNo = new com.see.truetransact.uicomponent.CButton();
        panBtnMemberType = new com.see.truetransact.uicomponent.CPanel();
        btnMemberNew = new com.see.truetransact.uicomponent.CButton();
        btnMemberSave = new com.see.truetransact.uicomponent.CButton();
        btnMemberDelete = new com.see.truetransact.uicomponent.CButton();
        lblMemberPriority = new com.see.truetransact.uicomponent.CLabel();
        txtMemPriority = new com.see.truetransact.uicomponent.CTextField();
        panCollateralTypeDetails = new com.see.truetransact.uicomponent.CPanel();
        lblSecurityType = new com.see.truetransact.uicomponent.CLabel();
        lblSecurityValue = new com.see.truetransact.uicomponent.CLabel();
        txtSecurityValue = new com.see.truetransact.uicomponent.CTextField();
        lblSecurityRemarks = new com.see.truetransact.uicomponent.CLabel();
        cboSecurityType = new com.see.truetransact.uicomponent.CComboBox();
        txtSecurityRemarks = new com.see.truetransact.uicomponent.CTextField();
        panGoldTypeDetails = new com.see.truetransact.uicomponent.CPanel();
        lblJewelleryDetails = new com.see.truetransact.uicomponent.CLabel();
        lblGrossWeight = new com.see.truetransact.uicomponent.CLabel();
        lblNetWeight = new com.see.truetransact.uicomponent.CLabel();
        lblValueOfGold = new com.see.truetransact.uicomponent.CLabel();
        lblGoldRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtGoldRemarks = new com.see.truetransact.uicomponent.CTextField();
        txtValueOfGold = new com.see.truetransact.uicomponent.CTextField();
        txtNetWeight = new com.see.truetransact.uicomponent.CTextField();
        txtGrossWeight = new com.see.truetransact.uicomponent.CTextField();
        srpTxtAreaParticulars = new com.see.truetransact.uicomponent.CScrollPane();
        txtJewelleryDetails = new com.see.truetransact.uicomponent.CTextArea();
        lblGoldSecurityExists = new com.see.truetransact.uicomponent.CLabel();
        rdoGoldSecurityExitsYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoGoldSecurityExitsNo = new com.see.truetransact.uicomponent.CRadioButton();
        cLabel3 = new com.see.truetransact.uicomponent.CLabel();
        txtGoldSecurityId = new com.see.truetransact.uicomponent.CTextField();
        btnGoldSecurityIdSearch = new com.see.truetransact.uicomponent.CButton();
        panDepositDetails = new com.see.truetransact.uicomponent.CPanel();
        panDepositType = new com.see.truetransact.uicomponent.CPanel();
        lblProductId = new com.see.truetransact.uicomponent.CLabel();
        lblDepAmount = new com.see.truetransact.uicomponent.CLabel();
        lblRateOfInterest = new com.see.truetransact.uicomponent.CLabel();
        lblDepDt = new com.see.truetransact.uicomponent.CLabel();
        txtDepAmount = new com.see.truetransact.uicomponent.CTextField();
        txtMaturityValue = new com.see.truetransact.uicomponent.CTextField();
        txtRateOfInterest = new com.see.truetransact.uicomponent.CTextField();
        lblMaturityDt = new com.see.truetransact.uicomponent.CLabel();
        lblMaturityValue = new com.see.truetransact.uicomponent.CLabel();
        lblDepNo = new com.see.truetransact.uicomponent.CLabel();
        tdtDepDt = new com.see.truetransact.uicomponent.CDateField();
        panBtnDeposit = new com.see.truetransact.uicomponent.CPanel();
        btnDepositNew = new com.see.truetransact.uicomponent.CButton();
        btnDepositSave = new com.see.truetransact.uicomponent.CButton();
        btnDepositDelete = new com.see.truetransact.uicomponent.CButton();
        txtMaturityDt = new com.see.truetransact.uicomponent.CDateField();
        cboDepProdType = new com.see.truetransact.uicomponent.CComboBox();
        panDepNo = new com.see.truetransact.uicomponent.CPanel();
        txtDepNo = new com.see.truetransact.uicomponent.CTextField();
        btnDepNo = new com.see.truetransact.uicomponent.CButton();
        lblProductTypeSecurity = new com.see.truetransact.uicomponent.CLabel();
        cboProductTypeSecurity = new com.see.truetransact.uicomponent.CComboBox();
        lblSameChittal = new com.see.truetransact.uicomponent.CLabel();
        cbxSameChittal = new com.see.truetransact.uicomponent.CCheckBox();
        panDepositTable = new com.see.truetransact.uicomponent.CPanel();
        srpTableDeposit = new com.see.truetransact.uicomponent.CScrollPane();
        tblDepositDetails = new com.see.truetransact.uicomponent.CTable();
        lblTotalDeposit = new com.see.truetransact.uicomponent.CLabel();
        lblTotalDepositValue = new com.see.truetransact.uicomponent.CLabel();
        panOtherSocietyDetails = new com.see.truetransact.uicomponent.CPanel();
        panOtherSociety = new com.see.truetransact.uicomponent.CPanel();
        lblOtherInstituionName = new com.see.truetransact.uicomponent.CLabel();
        lblSecurityAmountSociety = new com.see.truetransact.uicomponent.CLabel();
        lblIssueDtSoceity = new com.see.truetransact.uicomponent.CLabel();
        lblSecurityNoSociety = new com.see.truetransact.uicomponent.CLabel();
        txtOtherInstituionName = new com.see.truetransact.uicomponent.CTextField();
        txtMaturityValueSociety = new com.see.truetransact.uicomponent.CTextField();
        txtRemarksSociety = new com.see.truetransact.uicomponent.CTextField();
        lblRemarksSociety = new com.see.truetransact.uicomponent.CLabel();
        lblMaturityDateSociety = new com.see.truetransact.uicomponent.CLabel();
        lblSecurityTypeSociety = new com.see.truetransact.uicomponent.CLabel();
        panBtnOtherSociety = new com.see.truetransact.uicomponent.CPanel();
        btnSocietyNew = new com.see.truetransact.uicomponent.CButton();
        btnSocietySave = new com.see.truetransact.uicomponent.CButton();
        btnSocietyDelete = new com.see.truetransact.uicomponent.CButton();
        tdtMaturityDateSociety = new com.see.truetransact.uicomponent.CDateField();
        cboSecurityTypeSociety = new com.see.truetransact.uicomponent.CComboBox();
        lblOtherInstituion = new com.see.truetransact.uicomponent.CLabel();
        cboOtherInstituion = new com.see.truetransact.uicomponent.CComboBox();
        lblMaturityValueSociety = new com.see.truetransact.uicomponent.CLabel();
        txtSecurityNoSociety = new com.see.truetransact.uicomponent.CTextField();
        txtSecurityAmountSociety = new com.see.truetransact.uicomponent.CTextField();
        tdtIssueDtSoceity = new com.see.truetransact.uicomponent.CDateField();
        panOtherSocietyTable = new com.see.truetransact.uicomponent.CPanel();
        srpOtherSociety = new com.see.truetransact.uicomponent.CScrollPane();
        tblOtherSocietyDetails = new com.see.truetransact.uicomponent.CTable();
        panStanding = new com.see.truetransact.uicomponent.CPanel();
        panStandingDetails = new com.see.truetransact.uicomponent.CPanel();
        panCustomerNO = new com.see.truetransact.uicomponent.CPanel();
        txtCustomerIdCr = new com.see.truetransact.uicomponent.CTextField();
        btnCustomerIdFileOpenCr = new com.see.truetransact.uicomponent.CButton();
        lblAccountNo = new com.see.truetransact.uicomponent.CLabel();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        lblProductType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        chkStandingInstn = new com.see.truetransact.uicomponent.CCheckBox();
        panNominee = new com.see.truetransact.uicomponent.CPanel();
        chkNominee = new com.see.truetransact.uicomponent.CCheckBox();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        panSalaryRecovery = new com.see.truetransact.uicomponent.CPanel();
        panSalaryRecoveryValue = new com.see.truetransact.uicomponent.CPanel();
        rdoSalaryRecovery_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSalaryRecovery_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblSalaryRecovery = new com.see.truetransact.uicomponent.CLabel();
        lblLockStatus = new com.see.truetransact.uicomponent.CLabel();
        lblLockStatusVal = new com.see.truetransact.uicomponent.CLabel();
        cPanel4 = new com.see.truetransact.uicomponent.CPanel();
        lblDefaulterMark = new com.see.truetransact.uicomponent.CLabel();
        cbxDefaulter = new com.see.truetransact.uicomponent.CCheckBox();
        panCaseDetails = new com.see.truetransact.uicomponent.CPanel();
        panSanctionDetails_Table1 = new com.see.truetransact.uicomponent.CPanel();
        panCaseTableFields = new com.see.truetransact.uicomponent.CPanel();
        panSecurityDetails1 = new com.see.truetransact.uicomponent.CPanel();
        panLoanChargeDef = new com.see.truetransact.uicomponent.CPanel();
        cPanel3 = new com.see.truetransact.uicomponent.CPanel();
        cPanel6 = new com.see.truetransact.uicomponent.CPanel();
        lblApplicationNo = new com.see.truetransact.uicomponent.CLabel();
        lblApplicationDate = new com.see.truetransact.uicomponent.CLabel();
        txtApplicationNo = new com.see.truetransact.uicomponent.CTextField();
        tdtApplicationDate = new com.see.truetransact.uicomponent.CDateField();
        lblChargeAmount = new com.see.truetransact.uicomponent.CLabel();
        lblTotalTransactionAmtVal = new com.see.truetransact.uicomponent.CLabel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        cbxOnlyApplication = new com.see.truetransact.uicomponent.CCheckBox();
        txaApplicationSec = new com.see.truetransact.uicomponent.CLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaApplicationSecRemarks = new com.see.truetransact.uicomponent.CTextArea();
        panChargeDetails = new com.see.truetransact.uicomponent.CPanel();
        panChargeTransfer = new com.see.truetransact.uicomponent.CPanel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitAuthorize = new javax.swing.JMenuItem();
        mitReject = new javax.swing.JMenuItem();
        mitException = new javax.swing.JMenuItem();
        sptException = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setMinimumSize(new java.awt.Dimension(850, 675));
        setPreferredSize(new java.awt.Dimension(850, 675));
        getContentPane().setLayout(new java.awt.GridBagLayout());

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
        tbrAdvances.add(btnView);

        lblSpace6.setText("     ");
        tbrAdvances.add(lblSpace6);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnNew);

        lblSpace40.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace40.setText("     ");
        lblSpace40.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace40.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace40.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace40);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnEdit);

        lblSpace41.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace41.setText("     ");
        lblSpace41.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace41.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace41.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace41);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnDelete);

        lblSpace3.setText("     ");
        tbrAdvances.add(lblSpace3);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.setEnabled(false);
        btnSave.setFocusable(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnSave);

        lblSpace42.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace42.setText("     ");
        lblSpace42.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace42.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace42.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace42);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setFocusable(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnCancel);

        lblSpace4.setText("     ");
        tbrAdvances.add(lblSpace4);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnAuthorize);

        lblSpace43.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace43.setText("     ");
        lblSpace43.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace43.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace43.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace43);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        lblSpace44.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace44.setText("     ");
        lblSpace44.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace44.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace44.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace44);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnReject);

        lblSpace5.setText("     ");
        tbrAdvances.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrAdvances.add(btnPrint);

        lblSpace45.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace45.setText("     ");
        lblSpace45.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace45.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace45.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace45);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(tbrAdvances, gridBagConstraints);

        tabMasterMaintenanceDetails.setMinimumSize(new java.awt.Dimension(850, 480));
        tabMasterMaintenanceDetails.setPreferredSize(new java.awt.Dimension(850, 480));

        panMasterMaintenanceDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panMasterMaintenanceDetails.setMinimumSize(new java.awt.Dimension(850, 450));
        panMasterMaintenanceDetails.setPreferredSize(new java.awt.Dimension(850, 450));
        panMasterMaintenanceDetails.setLayout(new java.awt.GridBagLayout());

        panInsideMasterMaintenanceDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInsideMasterMaintenanceDetails.setMinimumSize(new java.awt.Dimension(850, 600));
        panInsideMasterMaintenanceDetails.setPreferredSize(new java.awt.Dimension(850, 600));
        panInsideMasterMaintenanceDetails.setLayout(new java.awt.GridBagLayout());

        panInstallmentDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Installment Details"));
        panInstallmentDetails.setMinimumSize(new java.awt.Dimension(410, 155));
        panInstallmentDetails.setPreferredSize(new java.awt.Dimension(410, 155));
        panInstallmentDetails.setLayout(new java.awt.GridBagLayout());

        txtOverDueAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInstallmentDetails.add(txtOverDueAmount, gridBagConstraints);

        txtTotalAmountTillDate.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInstallmentDetails.add(txtTotalAmountTillDate, gridBagConstraints);

        txtLastInstNo.setMinimumSize(new java.awt.Dimension(50, 21));
        txtLastInstNo.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInstallmentDetails.add(txtLastInstNo, gridBagConstraints);

        lblOverDueAmount.setText("Over Due Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInstallmentDetails.add(lblOverDueAmount, gridBagConstraints);

        lblTotalAmountTillDate.setText("Installment Amount Paid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInstallmentDetails.add(lblTotalAmountTillDate, gridBagConstraints);

        lblLastInstDate.setText("Last Installment  Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInstallmentDetails.add(lblLastInstDate, gridBagConstraints);

        lblLastInstNo.setText("Last Installment No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInstallmentDetails.add(lblLastInstNo, gridBagConstraints);

        txtOverDueInstallments.setMinimumSize(new java.awt.Dimension(50, 21));
        txtOverDueInstallments.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInstallmentDetails.add(txtOverDueInstallments, gridBagConstraints);

        lblOverDueInstallments.setText("Over Due Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInstallmentDetails.add(lblOverDueInstallments, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInstallmentDetails.add(tdtLastInstDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideMasterMaintenanceDetails.add(panInstallmentDetails, gridBagConstraints);

        panMdsSchemeNameDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panMdsSchemeNameDetails.setMinimumSize(new java.awt.Dimension(400, 115));
        panMdsSchemeNameDetails.setPreferredSize(new java.awt.Dimension(400, 115));

        lblChittalNo.setText("Chittal No");

        lblChitStartDt.setText("Chit start Date");

        lblSchemeName.setText("MDS Scheme Name");
        lblSchemeName.setToolTipText("MDS Scheme Name");

        lblDivisionNo.setText("Division No");

        panSchemeName.setLayout(new java.awt.GridBagLayout());

        txtSchemeName.setAllowAll(true);
        txtSchemeName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSchemeName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSchemeNameActionPerformed(evt);
            }
        });
        txtSchemeName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSchemeNameFocusLost(evt);
            }
        });
        panSchemeName.add(txtSchemeName, new java.awt.GridBagConstraints());

        panChittalNo.setLayout(new java.awt.GridBagLayout());

        txtChittalNo.setAllowAll(true);
        txtChittalNo.setMinimumSize(new java.awt.Dimension(110, 21));
        txtChittalNo.setPreferredSize(new java.awt.Dimension(110, 21));
        txtChittalNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChittalNoFocusLost(evt);
            }
        });
        panChittalNo.add(txtChittalNo, new java.awt.GridBagConstraints());

        txtSubNo.setMinimumSize(new java.awt.Dimension(40, 21));
        txtSubNo.setPreferredSize(new java.awt.Dimension(40, 21));

        panChittalNo1.setLayout(new java.awt.GridBagLayout());

        txtDivisionNo.setMinimumSize(new java.awt.Dimension(50, 21));
        txtDivisionNo.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panChittalNo1.add(txtDivisionNo, gridBagConstraints);

        lblCoChit.setForeground(new java.awt.Color(0, 51, 204));
        lblCoChit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCoChit.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCoChit.setMaximumSize(new java.awt.Dimension(100, 18));
        lblCoChit.setMinimumSize(new java.awt.Dimension(100, 18));
        lblCoChit.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panChittalNo1.add(lblCoChit, gridBagConstraints);

        lblProdDesc.setMaximumSize(new java.awt.Dimension(80, 21));
        lblProdDesc.setMinimumSize(new java.awt.Dimension(80, 21));
        lblProdDesc.setPreferredSize(new java.awt.Dimension(80, 21));

        lblChitCloseDt.setText("Chit Close Date");

        tdtChitCloseDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtChitCloseDtFocusLost(evt);
            }
        });

        btnChittalNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnChittalNo.setEnabled(false);
        btnChittalNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnChittalNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnChittalNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnChittalNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChittalNoActionPerformed(evt);
            }
        });

        btnSchemeName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSchemeName.setEnabled(false);
        btnSchemeName.setMaximumSize(new java.awt.Dimension(21, 21));
        btnSchemeName.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSchemeName.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSchemeName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSchemeNameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panMdsSchemeNameDetailsLayout = new javax.swing.GroupLayout(panMdsSchemeNameDetails);
        panMdsSchemeNameDetails.setLayout(panMdsSchemeNameDetailsLayout);
        panMdsSchemeNameDetailsLayout.setHorizontalGroup(
            panMdsSchemeNameDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMdsSchemeNameDetailsLayout.createSequentialGroup()
                .addGroup(panMdsSchemeNameDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panMdsSchemeNameDetailsLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(lblSchemeName, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(panSchemeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(btnSchemeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(lblProdDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panMdsSchemeNameDetailsLayout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(lblChittalNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panChittalNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(btnChittalNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addComponent(txtSubNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panMdsSchemeNameDetailsLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(lblDivisionNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(panChittalNo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panMdsSchemeNameDetailsLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(lblChitStartDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(tdtChitStartDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(lblChitCloseDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tdtChitCloseDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(8, Short.MAX_VALUE))
        );
        panMdsSchemeNameDetailsLayout.setVerticalGroup(
            panMdsSchemeNameDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMdsSchemeNameDetailsLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(panMdsSchemeNameDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panMdsSchemeNameDetailsLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblSchemeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panSchemeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSchemeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblProdDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(panMdsSchemeNameDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panMdsSchemeNameDetailsLayout.createSequentialGroup()
                        .addGroup(panMdsSchemeNameDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panMdsSchemeNameDetailsLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(lblChittalNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(panChittalNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnChittalNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSubNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(panMdsSchemeNameDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panMdsSchemeNameDetailsLayout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(lblDivisionNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(panChittalNo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4)
                        .addGroup(panMdsSchemeNameDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tdtChitStartDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panMdsSchemeNameDetailsLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(lblChitStartDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblChitCloseDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(tdtChitCloseDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 37;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideMasterMaintenanceDetails.add(panMdsSchemeNameDetails, gridBagConstraints);

        panMemberNoDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panMemberNoDetails.setMinimumSize(new java.awt.Dimension(400, 100));
        panMemberNoDetails.setPreferredSize(new java.awt.Dimension(400, 100));
        panMemberNoDetails.setLayout(new java.awt.GridBagLayout());

        lblMemberNo.setText("Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberNoDetails.add(lblMemberNo, gridBagConstraints);

        panMemberNo.setLayout(new java.awt.GridBagLayout());

        txtMemberNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMemberNo.add(txtMemberNo, gridBagConstraints);

        btnMemberNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMemberNo.setEnabled(false);
        btnMemberNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnMemberNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnMemberNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMemberNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemberNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMemberNo.add(btnMemberNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMemberNoDetails.add(panMemberNo, gridBagConstraints);

        lblNomineeName.setText("Nominee Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberNoDetails.add(lblNomineeName, gridBagConstraints);

        lblNomineName.setForeground(new java.awt.Color(0, 51, 204));
        lblNomineName.setMaximumSize(new java.awt.Dimension(150, 18));
        lblNomineName.setMinimumSize(new java.awt.Dimension(150, 18));
        lblNomineName.setPreferredSize(new java.awt.Dimension(150, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMemberNoDetails.add(lblNomineName, gridBagConstraints);

        lblMemberType.setText("Member Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberNoDetails.add(lblMemberType, gridBagConstraints);

        lblMembershipType.setForeground(new java.awt.Color(0, 51, 204));
        lblMembershipType.setMaximumSize(new java.awt.Dimension(150, 18));
        lblMembershipType.setMinimumSize(new java.awt.Dimension(150, 18));
        lblMembershipType.setPreferredSize(new java.awt.Dimension(150, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMemberNoDetails.add(lblMembershipType, gridBagConstraints);

        lblMembershipName.setForeground(new java.awt.Color(0, 51, 204));
        lblMembershipName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblMembershipName.setMaximumSize(new java.awt.Dimension(240, 18));
        lblMembershipName.setMinimumSize(new java.awt.Dimension(240, 18));
        lblMembershipName.setPreferredSize(new java.awt.Dimension(240, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 1);
        panMemberNoDetails.add(lblMembershipName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideMasterMaintenanceDetails.add(panMemberNoDetails, gridBagConstraints);

        panPrizedDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Bond Details"));
        panPrizedDetails.setMinimumSize(new java.awt.Dimension(410, 45));
        panPrizedDetails.setPreferredSize(new java.awt.Dimension(410, 45));
        panPrizedDetails.setLayout(new java.awt.GridBagLayout());

        lblBordDt.setText("Bond Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 13, 2, 3);
        panPrizedDetails.add(lblBordDt, gridBagConstraints);

        txtBondNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBondNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBondNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPrizedDetails.add(txtBondNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPrizedDetails.add(tdtBondDt, gridBagConstraints);

        lblBordNo.setText("Bond No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPrizedDetails.add(lblBordNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideMasterMaintenanceDetails.add(panPrizedDetails, gridBagConstraints);

        panPrizeDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Prized  Details"));
        panPrizeDetails.setMinimumSize(new java.awt.Dimension(400, 75));
        panPrizeDetails.setPreferredSize(new java.awt.Dimension(400, 75));
        panPrizeDetails.setLayout(new java.awt.GridBagLayout());

        lblResolutionDt.setText("Resolution Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPrizeDetails.add(lblResolutionDt, gridBagConstraints);

        lblPrizedAmount.setText("Prized Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPrizeDetails.add(lblPrizedAmount, gridBagConstraints);

        lblPayDt.setText("Pay Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPrizeDetails.add(lblPayDt, gridBagConstraints);

        txtPrizedAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPrizeDetails.add(txtPrizedAmount, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPrizeDetails.add(tdtResolutionDt, gridBagConstraints);

        lblResolutionNo.setText("Resolution No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPrizeDetails.add(lblResolutionNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPrizeDetails.add(tdtPayDt, gridBagConstraints);

        txtResolutionNo.setMinimumSize(new java.awt.Dimension(80, 21));
        txtResolutionNo.setPreferredSize(new java.awt.Dimension(80, 21));
        txtResolutionNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtResolutionNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPrizeDetails.add(txtResolutionNo, gridBagConstraints);

        ResolutionSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        ResolutionSearch.setEnabled(false);
        ResolutionSearch.setMaximumSize(new java.awt.Dimension(21, 21));
        ResolutionSearch.setMinimumSize(new java.awt.Dimension(21, 21));
        ResolutionSearch.setPreferredSize(new java.awt.Dimension(21, 21));
        ResolutionSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResolutionSearchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPrizeDetails.add(ResolutionSearch, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideMasterMaintenanceDetails.add(panPrizeDetails, gridBagConstraints);

        panSecurityDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Security Details"));
        panSecurityDetails.setMinimumSize(new java.awt.Dimension(840, 250));
        panSecurityDetails.setPreferredSize(new java.awt.Dimension(840, 250));
        panSecurityDetails.setLayout(new java.awt.GridBagLayout());

        tabMasterMaintenance.setMinimumSize(new java.awt.Dimension(420, 300));
        tabMasterMaintenance.setPreferredSize(new java.awt.Dimension(420, 300));

        panSalaryDetails.setMinimumSize(new java.awt.Dimension(250, 300));
        panSalaryDetails.setPreferredSize(new java.awt.Dimension(300, 300));
        panSalaryDetails.setLayout(new java.awt.GridBagLayout());

        cPanel1.setLayout(new java.awt.GridBagLayout());

        lblSalaryCertificateNo.setText("Salary Certificate No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        cPanel1.add(lblSalaryCertificateNo, gridBagConstraints);

        lblSalaryRemark.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        cPanel1.add(lblSalaryRemark, gridBagConstraints);

        lblEmployerName.setText("Employer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        cPanel1.add(lblEmployerName, gridBagConstraints);

        lblTotalSalary.setText("Total Salary");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        cPanel1.add(lblTotalSalary, gridBagConstraints);

        lblDesignation.setText("Designation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        cPanel1.add(lblDesignation, gridBagConstraints);

        lblMemberNum.setText("Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        cPanel1.add(lblMemberNum, gridBagConstraints);

        lblAddress.setText("Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        cPanel1.add(lblAddress, gridBagConstraints);

        lblPinCode.setText("PinCode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        cPanel1.add(lblPinCode, gridBagConstraints);

        lblCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        cPanel1.add(lblCity, gridBagConstraints);

        lblRetirementDt.setText("Date Of Retirement");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        cPanel1.add(lblRetirementDt, gridBagConstraints);

        lblContactNo.setText("Contact No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        cPanel1.add(lblContactNo, gridBagConstraints);

        lblNetWorth.setText("Networth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        cPanel1.add(lblNetWorth, gridBagConstraints);

        txtContactNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        cPanel1.add(txtContactNo, gridBagConstraints);

        txtMemberNum.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        cPanel1.add(txtMemberNum, gridBagConstraints);

        txtSalaryRemark.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        cPanel1.add(txtSalaryRemark, gridBagConstraints);

        txtDesignation.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        cPanel1.add(txtDesignation, gridBagConstraints);

        txtAddress.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        cPanel1.add(txtAddress, gridBagConstraints);

        txtEmployerName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        cPanel1.add(txtEmployerName, gridBagConstraints);

        txtSalaryCertificateNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        cPanel1.add(txtSalaryCertificateNo, gridBagConstraints);

        txtTotalSalary.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        cPanel1.add(txtTotalSalary, gridBagConstraints);

        txtNetWorth.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        cPanel1.add(txtNetWorth, gridBagConstraints);

        txtPinCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        cPanel1.add(txtPinCode, gridBagConstraints);

        cboCity.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel1.add(cboCity, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        cPanel1.add(tdtRetirementDt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSalaryDetails.add(cPanel1, gridBagConstraints);

        cPanel2.setMinimumSize(new java.awt.Dimension(310, 100));
        cPanel2.setPreferredSize(new java.awt.Dimension(310, 100));
        cPanel2.setLayout(new java.awt.GridBagLayout());

        cScrollPane1.setMinimumSize(new java.awt.Dimension(300, 100));
        cScrollPane1.setPreferredSize(new java.awt.Dimension(300, 100));

        tblSalaryDetails.setModel(new javax.swing.table.DefaultTableModel(
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
        tblSalaryDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSalaryDetailsMouseClicked(evt);
            }
        });
        cScrollPane1.setViewportView(tblSalaryDetails);

        cPanel2.add(cScrollPane1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panSalaryDetails.add(cPanel2, gridBagConstraints);

        panProperty1.setMinimumSize(new java.awt.Dimension(200, 25));
        panProperty1.setPreferredSize(new java.awt.Dimension(200, 25));
        panProperty1.setLayout(new java.awt.GridBagLayout());

        btnNew_Property1.setText("New");
        btnNew_Property1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew_Property1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProperty1.add(btnNew_Property1, gridBagConstraints);

        btnlSave_Property1.setText("Save");
        btnlSave_Property1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlSave_Property1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProperty1.add(btnlSave_Property1, gridBagConstraints);

        btnDelete_Property1.setText("Delete");
        btnDelete_Property1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete_Property1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProperty1.add(btnDelete_Property1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        panSalaryDetails.add(panProperty1, gridBagConstraints);

        tabMasterMaintenance.addTab("Salary Details", panSalaryDetails);

        panMemberTypeDetails.setMinimumSize(new java.awt.Dimension(300, 300));
        panMemberTypeDetails.setPreferredSize(new java.awt.Dimension(300, 300));
        panMemberTypeDetails.setLayout(new java.awt.GridBagLayout());

        panMemberTypeTable.setMinimumSize(new java.awt.Dimension(460, 210));
        panMemberTypeTable.setPreferredSize(new java.awt.Dimension(460, 210));
        panMemberTypeTable.setLayout(new java.awt.GridBagLayout());

        srpMemberType.setMinimumSize(new java.awt.Dimension(450, 160));
        srpMemberType.setPreferredSize(new java.awt.Dimension(450, 160));

        tblMemberType.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Member No", "Name", "Type of Member", "Contact No", "Networth"
            }
        ));
        tblMemberType.setMinimumSize(new java.awt.Dimension(300, 700));
        tblMemberType.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblMemberType.setPreferredSize(new java.awt.Dimension(300, 700));
        tblMemberType.setOpaque(false);
        tblMemberType.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMemberTypeMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblMemberTypeMousePressed(evt);
            }
        });
        srpMemberType.setViewportView(tblMemberType);

        panMemberTypeTable.add(srpMemberType, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 8, 4);
        panMemberTypeDetails.add(panMemberTypeTable, gridBagConstraints);

        panMemberDetails.setMinimumSize(new java.awt.Dimension(275, 200));
        panMemberDetails.setPreferredSize(new java.awt.Dimension(275, 200));
        panMemberDetails.setRequestFocusEnabled(false);
        panMemberDetails.setLayout(new java.awt.GridBagLayout());

        lblMemNo.setText("Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemberDetails.add(lblMemNo, gridBagConstraints);

        lblMemName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemberDetails.add(lblMemName, gridBagConstraints);

        lblMemType.setText("Type of Member");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemberDetails.add(lblMemType, gridBagConstraints);

        lblMemNetworth.setText("Networth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemberDetails.add(lblMemNetworth, gridBagConstraints);

        txtMemNetworth.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(txtMemNetworth, gridBagConstraints);

        txtContactNum.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(txtContactNum, gridBagConstraints);

        lblContactNum.setText("Contact No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemberDetails.add(lblContactNum, gridBagConstraints);

        txtMemType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(txtMemType, gridBagConstraints);

        txtMemName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(txtMemName, gridBagConstraints);

        panMemberNumber.setLayout(new java.awt.GridBagLayout());

        txtMemNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMemNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMemNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMemberNumber.add(txtMemNo, gridBagConstraints);

        btnMemNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMemNo.setEnabled(false);
        btnMemNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnMemNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnMemNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMemNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMemberNumber.add(btnMemNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberDetails.add(panMemberNumber, gridBagConstraints);

        panBtnMemberType.setMinimumSize(new java.awt.Dimension(95, 35));
        panBtnMemberType.setPreferredSize(new java.awt.Dimension(95, 35));
        panBtnMemberType.setLayout(new java.awt.GridBagLayout());

        btnMemberNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnMemberNew.setToolTipText("New");
        btnMemberNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnMemberNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnMemberNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnMemberNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemberNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnMemberType.add(btnMemberNew, gridBagConstraints);

        btnMemberSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnMemberSave.setToolTipText("Save");
        btnMemberSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnMemberSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnMemberSave.setName("btnContactNoAdd"); // NOI18N
        btnMemberSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnMemberSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemberSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnMemberType.add(btnMemberSave, gridBagConstraints);

        btnMemberDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnMemberDelete.setToolTipText("Delete");
        btnMemberDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnMemberDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnMemberDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnMemberDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemberDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnMemberType.add(btnMemberDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 7;
        panMemberDetails.add(panBtnMemberType, gridBagConstraints);

        lblMemberPriority.setText("Priority");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        panMemberDetails.add(lblMemberPriority, gridBagConstraints);

        txtMemPriority.setAllowAll(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        panMemberDetails.add(txtMemPriority, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 50, 12, 4);
        panMemberTypeDetails.add(panMemberDetails, gridBagConstraints);

        tabMasterMaintenance.addTab("Member Type", panMemberTypeDetails);

        panCollateralTypeDetails.setLayout(new java.awt.GridBagLayout());

        lblSecurityType.setText("Security Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCollateralTypeDetails.add(lblSecurityType, gridBagConstraints);

        lblSecurityValue.setText("Security Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCollateralTypeDetails.add(lblSecurityValue, gridBagConstraints);

        txtSecurityValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCollateralTypeDetails.add(txtSecurityValue, gridBagConstraints);

        lblSecurityRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCollateralTypeDetails.add(lblSecurityRemarks, gridBagConstraints);

        cboSecurityType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCollateralTypeDetails.add(cboSecurityType, gridBagConstraints);

        txtSecurityRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCollateralTypeDetails.add(txtSecurityRemarks, gridBagConstraints);

        tabMasterMaintenance.addTab("Collateral Type", panCollateralTypeDetails);

        panGoldTypeDetails.setMinimumSize(new java.awt.Dimension(250, 300));
        panGoldTypeDetails.setPreferredSize(new java.awt.Dimension(250, 300));
        panGoldTypeDetails.setLayout(new java.awt.GridBagLayout());

        lblJewelleryDetails.setText("Jewellery Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 146, 0, 0);
        panGoldTypeDetails.add(lblJewelleryDetails, gridBagConstraints);

        lblGrossWeight.setText("Gross Weight");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 163, 0, 0);
        panGoldTypeDetails.add(lblGrossWeight, gridBagConstraints);

        lblNetWeight.setText("Net Weight");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 178, 0, 0);
        panGoldTypeDetails.add(lblNetWeight, gridBagConstraints);

        lblValueOfGold.setText("Value of the Gold");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 137, 0, 0);
        panGoldTypeDetails.add(lblValueOfGold, gridBagConstraints);

        lblGoldRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 10, 0, 0);
        panGoldTypeDetails.add(lblGoldRemarks, gridBagConstraints);

        txtGoldRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 27, 0);
        panGoldTypeDetails.add(txtGoldRemarks, gridBagConstraints);

        txtValueOfGold.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 27, 0);
        panGoldTypeDetails.add(txtValueOfGold, gridBagConstraints);

        txtNetWeight.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        panGoldTypeDetails.add(txtNetWeight, gridBagConstraints);

        txtGrossWeight.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 0, 0);
        panGoldTypeDetails.add(txtGrossWeight, gridBagConstraints);

        srpTxtAreaParticulars.setMinimumSize(new java.awt.Dimension(400, 60));
        srpTxtAreaParticulars.setPreferredSize(new java.awt.Dimension(400, 60));

        txtJewelleryDetails.setBorder(javax.swing.BorderFactory.createBevelBorder(1));
        txtJewelleryDetails.setLineWrap(true);
        srpTxtAreaParticulars.setViewportView(txtJewelleryDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 21;
        gridBagConstraints.ipady = -12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 0, 0);
        panGoldTypeDetails.add(srpTxtAreaParticulars, gridBagConstraints);

        lblGoldSecurityExists.setText("Gold Security Exists");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 124, 0, 0);
        panGoldTypeDetails.add(lblGoldSecurityExists, gridBagConstraints);

        rdoGoldSecurityExitsYes.setText("Yes");
        rdoGoldSecurityExitsYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoGoldSecurityExitsYesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 0, 0, 0);
        panGoldTypeDetails.add(rdoGoldSecurityExitsYes, gridBagConstraints);

        rdoGoldSecurityExitsNo.setText("No");
        rdoGoldSecurityExitsNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoGoldSecurityExitsNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 6, 0, 0);
        panGoldTypeDetails.add(rdoGoldSecurityExitsNo, gridBagConstraints);

        cLabel3.setText("Gold Security Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 11, 0, 0);
        panGoldTypeDetails.add(cLabel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 196;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 6, 0, 0);
        panGoldTypeDetails.add(txtGoldSecurityId, gridBagConstraints);

        btnGoldSecurityIdSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnGoldSecurityIdSearch.setMaximumSize(new java.awt.Dimension(21, 21));
        btnGoldSecurityIdSearch.setMinimumSize(new java.awt.Dimension(21, 21));
        btnGoldSecurityIdSearch.setPreferredSize(new java.awt.Dimension(21, 21));
        btnGoldSecurityIdSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoldSecurityIdSearchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 12;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 11;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 6, 0, 126);
        panGoldTypeDetails.add(btnGoldSecurityIdSearch, gridBagConstraints);

        tabMasterMaintenance.addTab("Gold Type", panGoldTypeDetails);

        panDepositDetails.setMinimumSize(new java.awt.Dimension(850, 225));
        panDepositDetails.setPreferredSize(new java.awt.Dimension(850, 225));
        panDepositDetails.setLayout(new java.awt.GridBagLayout());

        panDepositType.setMinimumSize(new java.awt.Dimension(440, 225));
        panDepositType.setPreferredSize(new java.awt.Dimension(440, 225));
        panDepositType.setRequestFocusEnabled(false);

        lblProductId.setText("Product Id");

        lblDepAmount.setText("Dep Amount");

        lblRateOfInterest.setText("Rate of Interest");

        lblDepDt.setText("Dep Date");

        txtDepAmount.setMinimumSize(new java.awt.Dimension(100, 21));

        txtMaturityValue.setMinimumSize(new java.awt.Dimension(100, 21));

        txtRateOfInterest.setMinimumSize(new java.awt.Dimension(100, 21));

        lblMaturityDt.setText("Maturity Date");

        lblMaturityValue.setText("Maturity Value");

        lblDepNo.setText("Deposit No");

        panBtnDeposit.setMinimumSize(new java.awt.Dimension(95, 35));
        panBtnDeposit.setPreferredSize(new java.awt.Dimension(95, 35));
        panBtnDeposit.setLayout(new java.awt.GridBagLayout());

        btnDepositNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnDepositNew.setToolTipText("New");
        btnDepositNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnDepositNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnDepositNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnDepositNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnDeposit.add(btnDepositNew, gridBagConstraints);

        btnDepositSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnDepositSave.setToolTipText("Save");
        btnDepositSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnDepositSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnDepositSave.setName("btnContactNoAdd"); // NOI18N
        btnDepositSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnDepositSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnDeposit.add(btnDepositSave, gridBagConstraints);

        btnDepositDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDepositDelete.setToolTipText("Delete");
        btnDepositDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnDepositDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnDepositDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnDepositDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnDeposit.add(btnDepositDelete, gridBagConstraints);

        txtMaturityDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMaturityDtFocusLost(evt);
            }
        });

        cboDepProdType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----Select----" }));
        cboDepProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDepProdType.setPopupWidth(165);

        panDepNo.setLayout(new java.awt.GridBagLayout());

        txtDepNo.setAllowAll(true);
        txtDepNo.setMinimumSize(new java.awt.Dimension(110, 21));
        txtDepNo.setPreferredSize(new java.awt.Dimension(110, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDepNo.add(txtDepNo, gridBagConstraints);

        btnDepNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDepNo.setEnabled(false);
        btnDepNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnDepNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDepNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDepNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panDepNo.add(btnDepNo, gridBagConstraints);

        lblProductTypeSecurity.setText("Product Type");

        cboProductTypeSecurity.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----Select----" }));
        cboProductTypeSecurity.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductTypeSecurity.setPopupWidth(120);
        cboProductTypeSecurity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductTypeSecurityActionPerformed(evt);
            }
        });

        lblSameChittal.setText("Same Chittal as Lien");
        lblSameChittal.setToolTipText("Same Chittal as Lien");
        lblSameChittal.setMaximumSize(new java.awt.Dimension(150, 18));
        lblSameChittal.setMinimumSize(new java.awt.Dimension(150, 18));
        lblSameChittal.setPreferredSize(new java.awt.Dimension(150, 18));

        cbxSameChittal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxSameChittalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panDepositTypeLayout = new javax.swing.GroupLayout(panDepositType);
        panDepositType.setLayout(panDepositTypeLayout);
        panDepositTypeLayout.setHorizontalGroup(
            panDepositTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panDepositTypeLayout.createSequentialGroup()
                .addGroup(panDepositTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panDepositTypeLayout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(lblProductId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(cboDepProdType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblMaturityDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMaturityDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panDepositTypeLayout.createSequentialGroup()
                        .addGroup(panDepositTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panDepositTypeLayout.createSequentialGroup()
                                .addGap(75, 75, 75)
                                .addComponent(lblDepDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panDepositTypeLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblDepNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(panDepositTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panDepNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tdtDepDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(58, 58, 58)
                        .addComponent(panBtnDeposit, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panDepositTypeLayout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(lblDepAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(txtDepAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panDepositTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(panDepositTypeLayout.createSequentialGroup()
                            .addGap(52, 52, 52)
                            .addComponent(lblProductTypeSecurity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(cboProductTypeSecurity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(lblMaturityValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtMaturityValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panDepositTypeLayout.createSequentialGroup()
                            .addGap(51, 51, 51)
                            .addComponent(lblSameChittal, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cbxSameChittal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(34, 34, 34)
                            .addComponent(lblRateOfInterest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtRateOfInterest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(21, 21, 21))
        );
        panDepositTypeLayout.setVerticalGroup(
            panDepositTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panDepositTypeLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(panDepositTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbxSameChittal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panDepositTypeLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(panDepositTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtRateOfInterest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRateOfInterest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lblSameChittal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(panDepositTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panDepositTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cboProductTypeSecurity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblMaturityValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtMaturityValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panDepositTypeLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblProductTypeSecurity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(4, 4, 4)
                .addGroup(panDepositTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panDepositTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cboDepProdType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblMaturityDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panDepositTypeLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblProductId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtMaturityDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panDepositTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panDepositTypeLayout.createSequentialGroup()
                        .addComponent(lblDepNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(lblDepDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panDepositTypeLayout.createSequentialGroup()
                        .addComponent(panDepNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(tdtDepDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panDepositTypeLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(panBtnDeposit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(4, 4, 4)
                .addGroup(panDepositTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panDepositTypeLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblDepAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtDepAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panDepositDetails.add(panDepositType, gridBagConstraints);

        panDepositTable.setMinimumSize(new java.awt.Dimension(380, 220));
        panDepositTable.setPreferredSize(new java.awt.Dimension(380, 220));
        panDepositTable.setLayout(new java.awt.GridBagLayout());

        srpTableDeposit.setMinimumSize(new java.awt.Dimension(375, 160));
        srpTableDeposit.setPreferredSize(new java.awt.Dimension(375, 160));

        tblDepositDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Prod Type", "Dep No", "Dep Amt", "Matur Val"
            }
        ));
        tblDepositDetails.setMinimumSize(new java.awt.Dimension(275, 750));
        tblDepositDetails.setOpaque(false);
        tblDepositDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblDepositDetails.setPreferredSize(new java.awt.Dimension(275, 750));
        tblDepositDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDepositDetailsMousePressed(evt);
            }
        });
        srpTableDeposit.setViewportView(tblDepositDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 0);
        panDepositTable.add(srpTableDeposit, gridBagConstraints);

        lblTotalDeposit.setText("Total Deposit Value ");
        lblTotalDeposit.setMinimumSize(new java.awt.Dimension(160, 18));
        lblTotalDeposit.setPreferredSize(new java.awt.Dimension(160, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 4, 6);
        panDepositTable.add(lblTotalDeposit, gridBagConstraints);

        lblTotalDepositValue.setText("                          ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 5);
        panDepositTable.add(lblTotalDepositValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panDepositDetails.add(panDepositTable, gridBagConstraints);

        tabMasterMaintenance.addTab("Deposit Type", panDepositDetails);

        panOtherSocietyDetails.setMinimumSize(new java.awt.Dimension(850, 225));
        panOtherSocietyDetails.setPreferredSize(new java.awt.Dimension(850, 225));
        panOtherSocietyDetails.setLayout(new java.awt.GridBagLayout());

        panOtherSociety.setMinimumSize(new java.awt.Dimension(440, 225));
        panOtherSociety.setPreferredSize(new java.awt.Dimension(440, 225));
        panOtherSociety.setRequestFocusEnabled(false);
        panOtherSociety.setLayout(new java.awt.GridBagLayout());

        lblOtherInstituionName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panOtherSociety.add(lblOtherInstituionName, gridBagConstraints);

        lblSecurityAmountSociety.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 25, 4);
        panOtherSociety.add(lblSecurityAmountSociety, gridBagConstraints);

        lblIssueDtSoceity.setText("Issue Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 4);
        panOtherSociety.add(lblIssueDtSoceity, gridBagConstraints);

        lblSecurityNoSociety.setText("Security No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panOtherSociety.add(lblSecurityNoSociety, gridBagConstraints);

        txtOtherInstituionName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panOtherSociety.add(txtOtherInstituionName, gridBagConstraints);

        txtMaturityValueSociety.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panOtherSociety.add(txtMaturityValueSociety, gridBagConstraints);

        txtRemarksSociety.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panOtherSociety.add(txtRemarksSociety, gridBagConstraints);

        lblRemarksSociety.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 4);
        panOtherSociety.add(lblRemarksSociety, gridBagConstraints);

        lblMaturityDateSociety.setText("Maturity Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 4);
        panOtherSociety.add(lblMaturityDateSociety, gridBagConstraints);

        lblSecurityTypeSociety.setText("Security Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panOtherSociety.add(lblSecurityTypeSociety, gridBagConstraints);

        panBtnOtherSociety.setMinimumSize(new java.awt.Dimension(95, 35));
        panBtnOtherSociety.setPreferredSize(new java.awt.Dimension(95, 35));
        panBtnOtherSociety.setLayout(new java.awt.GridBagLayout());

        btnSocietyNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnSocietyNew.setToolTipText("New");
        btnSocietyNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSocietyNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSocietyNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSocietyNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSocietyNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnOtherSociety.add(btnSocietyNew, gridBagConstraints);

        btnSocietySave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSocietySave.setToolTipText("Save");
        btnSocietySave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSocietySave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSocietySave.setName("btnContactNoAdd"); // NOI18N
        btnSocietySave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSocietySave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSocietySaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnOtherSociety.add(btnSocietySave, gridBagConstraints);

        btnSocietyDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnSocietyDelete.setToolTipText("Delete");
        btnSocietyDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnSocietyDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnSocietyDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnSocietyDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSocietyDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnOtherSociety.add(btnSocietyDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 2);
        panOtherSociety.add(panBtnOtherSociety, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panOtherSociety.add(tdtMaturityDateSociety, gridBagConstraints);

        cboSecurityTypeSociety.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----Select----" }));
        cboSecurityTypeSociety.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSecurityTypeSociety.setPopupWidth(165);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panOtherSociety.add(cboSecurityTypeSociety, gridBagConstraints);

        lblOtherInstituion.setText("Other Institution");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panOtherSociety.add(lblOtherInstituion, gridBagConstraints);

        cboOtherInstituion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----Select----" }));
        cboOtherInstituion.setMinimumSize(new java.awt.Dimension(100, 21));
        cboOtherInstituion.setPopupWidth(120);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panOtherSociety.add(cboOtherInstituion, gridBagConstraints);

        lblMaturityValueSociety.setText("Maturity Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 4);
        panOtherSociety.add(lblMaturityValueSociety, gridBagConstraints);

        txtSecurityNoSociety.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panOtherSociety.add(txtSecurityNoSociety, gridBagConstraints);

        txtSecurityAmountSociety.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 25, 2);
        panOtherSociety.add(txtSecurityAmountSociety, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panOtherSociety.add(tdtIssueDtSoceity, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panOtherSocietyDetails.add(panOtherSociety, gridBagConstraints);

        panOtherSocietyTable.setMinimumSize(new java.awt.Dimension(380, 220));
        panOtherSocietyTable.setPreferredSize(new java.awt.Dimension(380, 220));
        panOtherSocietyTable.setLayout(new java.awt.GridBagLayout());

        srpOtherSociety.setMinimumSize(new java.awt.Dimension(375, 160));
        srpOtherSociety.setPreferredSize(new java.awt.Dimension(375, 160));

        tblOtherSocietyDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Security No", "Security Type", "Instituion", "Name", "Amount"
            }
        ));
        tblOtherSocietyDetails.setMinimumSize(new java.awt.Dimension(275, 750));
        tblOtherSocietyDetails.setOpaque(false);
        tblOtherSocietyDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblOtherSocietyDetails.setPreferredSize(new java.awt.Dimension(275, 750));
        tblOtherSocietyDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblOtherSocietyDetailsMousePressed(evt);
            }
        });
        srpOtherSociety.setViewportView(tblOtherSocietyDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 18, 0);
        panOtherSocietyTable.add(srpOtherSociety, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panOtherSocietyDetails.add(panOtherSocietyTable, gridBagConstraints);

        tabMasterMaintenance.addTab("OtherSecurityDetails", panOtherSocietyDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panSecurityDetails.add(tabMasterMaintenance, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideMasterMaintenanceDetails.add(panSecurityDetails, gridBagConstraints);

        panStanding.setMinimumSize(new java.awt.Dimension(410, 95));
        panStanding.setPreferredSize(new java.awt.Dimension(410, 95));
        panStanding.setLayout(new java.awt.GridBagLayout());

        panStandingDetails.setMinimumSize(new java.awt.Dimension(230, 100));
        panStandingDetails.setPreferredSize(new java.awt.Dimension(230, 100));
        panStandingDetails.setLayout(new java.awt.GridBagLayout());

        panCustomerNO.setMinimumSize(new java.awt.Dimension(125, 21));
        panCustomerNO.setPreferredSize(new java.awt.Dimension(125, 21));
        panCustomerNO.setLayout(new java.awt.GridBagLayout());

        txtCustomerIdCr.setEditable(false);
        txtCustomerIdCr.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panCustomerNO.add(txtCustomerIdCr, gridBagConstraints);

        btnCustomerIdFileOpenCr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustomerIdFileOpenCr.setMaximumSize(new java.awt.Dimension(21, 21));
        btnCustomerIdFileOpenCr.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCustomerIdFileOpenCr.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCustomerIdFileOpenCr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerIdFileOpenCrActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustomerNO.add(btnCustomerIdFileOpenCr, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 4, 0);
        panStandingDetails.add(panCustomerNO, gridBagConstraints);

        lblAccountNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAccountNo.setText("Account No");
        lblAccountNo.setMaximumSize(new java.awt.Dimension(85, 18));
        lblAccountNo.setMinimumSize(new java.awt.Dimension(85, 18));
        lblAccountNo.setPreferredSize(new java.awt.Dimension(85, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 4, 2);
        panStandingDetails.add(lblAccountNo, gridBagConstraints);

        lblProdId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panStandingDetails.add(lblProdId, gridBagConstraints);

        lblProductType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panStandingDetails.add(lblProductType, gridBagConstraints);

        cboProdType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----Select----" }));
        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.setPopupWidth(125);
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panStandingDetails.add(cboProdType, gridBagConstraints);

        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panStandingDetails.add(cboProdId, gridBagConstraints);

        chkStandingInstn.setText("Standing Instruction");
        chkStandingInstn.setMaximumSize(new java.awt.Dimension(150, 20));
        chkStandingInstn.setMinimumSize(new java.awt.Dimension(150, 20));
        chkStandingInstn.setPreferredSize(new java.awt.Dimension(150, 20));
        chkStandingInstn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkStandingInstnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 8);
        panStandingDetails.add(chkStandingInstn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panStanding.add(panStandingDetails, gridBagConstraints);

        panNominee.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11)); // NOI18N
        panNominee.setMinimumSize(new java.awt.Dimension(210, 100));
        panNominee.setPreferredSize(new java.awt.Dimension(210, 100));
        panNominee.setLayout(new java.awt.GridBagLayout());

        chkNominee.setText("Nomination Details");
        chkNominee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkNomineeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 0, 1);
        panNominee.add(chkNominee, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panNominee.add(lblRemarks, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panNominee.add(txtRemarks, gridBagConstraints);

        panSalaryRecovery.setMinimumSize(new java.awt.Dimension(195, 30));
        panSalaryRecovery.setPreferredSize(new java.awt.Dimension(195, 30));
        panSalaryRecovery.setLayout(new java.awt.GridBagLayout());

        panSalaryRecoveryValue.setMinimumSize(new java.awt.Dimension(95, 27));
        panSalaryRecoveryValue.setPreferredSize(new java.awt.Dimension(95, 27));
        panSalaryRecoveryValue.setLayout(new java.awt.GridBagLayout());

        rdgSalaryRecovery.add(rdoSalaryRecovery_Yes);
        rdoSalaryRecovery_Yes.setText("Yes");
        rdoSalaryRecovery_Yes.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoSalaryRecovery_Yes.setPreferredSize(new java.awt.Dimension(50, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panSalaryRecoveryValue.add(rdoSalaryRecovery_Yes, gridBagConstraints);

        rdgSalaryRecovery.add(rdoSalaryRecovery_No);
        rdoSalaryRecovery_No.setText("No");
        rdoSalaryRecovery_No.setMaximumSize(new java.awt.Dimension(44, 18));
        rdoSalaryRecovery_No.setMinimumSize(new java.awt.Dimension(44, 18));
        rdoSalaryRecovery_No.setPreferredSize(new java.awt.Dimension(44, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panSalaryRecoveryValue.add(rdoSalaryRecovery_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 4);
        panSalaryRecovery.add(panSalaryRecoveryValue, gridBagConstraints);

        lblSalaryRecovery.setText("Salary Recovery");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panSalaryRecovery.add(lblSalaryRecovery, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panNominee.add(panSalaryRecovery, gridBagConstraints);

        lblLockStatus.setText("Lock Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panNominee.add(lblLockStatus, gridBagConstraints);

        lblLockStatusVal.setForeground(new java.awt.Color(0, 51, 204));
        lblLockStatusVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblLockStatusVal.setMaximumSize(new java.awt.Dimension(90, 18));
        lblLockStatusVal.setMinimumSize(new java.awt.Dimension(90, 18));
        lblLockStatusVal.setPreferredSize(new java.awt.Dimension(90, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panNominee.add(lblLockStatusVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panStanding.add(panNominee, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideMasterMaintenanceDetails.add(panStanding, gridBagConstraints);

        cPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cPanel4.setMinimumSize(new java.awt.Dimension(400, 28));
        cPanel4.setPreferredSize(new java.awt.Dimension(400, 28));
        cPanel4.setLayout(new java.awt.GridBagLayout());

        lblDefaulterMark.setText("Defaulter Mark");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        cPanel4.add(lblDefaulterMark, gridBagConstraints);

        cbxDefaulter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxDefaulterActionPerformed(evt);
            }
        });
        cPanel4.add(cbxDefaulter, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panInsideMasterMaintenanceDetails.add(cPanel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMasterMaintenanceDetails.add(panInsideMasterMaintenanceDetails, gridBagConstraints);

        tabMasterMaintenanceDetails.addTab("Master Maintenance Details", panMasterMaintenanceDetails);

        panCaseDetails.setMinimumSize(new java.awt.Dimension(820, 550));
        panCaseDetails.setPreferredSize(new java.awt.Dimension(820, 550));
        panCaseDetails.setLayout(new java.awt.GridBagLayout());

        panSanctionDetails_Table1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSanctionDetails_Table1.setMinimumSize(new java.awt.Dimension(775, 320));
        panSanctionDetails_Table1.setPreferredSize(new java.awt.Dimension(775, 320));
        panSanctionDetails_Table1.setLayout(new java.awt.GridBagLayout());

        panCaseTableFields.setBorder(javax.swing.BorderFactory.createTitledBorder("Case Details"));
        panCaseTableFields.setMinimumSize(new java.awt.Dimension(800, 350));
        panCaseTableFields.setPreferredSize(new java.awt.Dimension(800, 350));
        panCaseTableFields.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails_Table1.add(panCaseTableFields, gridBagConstraints);

        panCaseDetails.add(panSanctionDetails_Table1, new java.awt.GridBagConstraints());

        tabMasterMaintenanceDetails.addTab("Case Details", panCaseDetails);

        panSecurityDetails1.setMinimumSize(new java.awt.Dimension(814, 320));
        panSecurityDetails1.setPreferredSize(new java.awt.Dimension(814, 320));
        panSecurityDetails1.setLayout(new java.awt.GridBagLayout());
        tabMasterMaintenanceDetails.addTab("Collateral Type Details", panSecurityDetails1);

        panLoanChargeDef.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panLoanChargeDefMouseClicked(evt);
            }
        });
        panLoanChargeDef.setLayout(new java.awt.GridBagLayout());

        cPanel3.setMinimumSize(new java.awt.Dimension(850, 200));
        cPanel3.setPreferredSize(new java.awt.Dimension(850, 200));
        cPanel3.setLayout(new java.awt.GridBagLayout());

        cPanel6.setMinimumSize(new java.awt.Dimension(350, 200));
        cPanel6.setPreferredSize(new java.awt.Dimension(350, 200));
        cPanel6.setLayout(new java.awt.GridBagLayout());

        lblApplicationNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblApplicationNo.setText("Application No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel6.add(lblApplicationNo, gridBagConstraints);

        lblApplicationDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblApplicationDate.setText("Application Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel6.add(lblApplicationDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 2);
        cPanel6.add(txtApplicationNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 2);
        cPanel6.add(tdtApplicationDate, gridBagConstraints);

        lblChargeAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblChargeAmount.setText("Charge Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel6.add(lblChargeAmount, gridBagConstraints);

        lblTotalTransactionAmtVal.setMinimumSize(new java.awt.Dimension(100, 21));
        lblTotalTransactionAmtVal.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 2);
        cPanel6.add(lblTotalTransactionAmtVal, gridBagConstraints);

        cLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cLabel1.setText("Only Application");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel6.add(cLabel1, gridBagConstraints);

        cbxOnlyApplication.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxOnlyApplicationActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        cPanel6.add(cbxOnlyApplication, gridBagConstraints);

        txaApplicationSec.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txaApplicationSec.setText("Application Security Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel6.add(txaApplicationSec, gridBagConstraints);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(100, 25));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(120, 80));

        txaApplicationSecRemarks.setColumns(20);
        txaApplicationSecRemarks.setRows(5);
        jScrollPane1.setViewportView(txaApplicationSecRemarks);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 2);
        cPanel6.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        cPanel3.add(cPanel6, gridBagConstraints);

        panChargeDetails.setMinimumSize(new java.awt.Dimension(500, 200));
        panChargeDetails.setPreferredSize(new java.awt.Dimension(500, 200));
        panChargeDetails.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        cPanel3.add(panChargeDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panLoanChargeDef.add(cPanel3, gridBagConstraints);

        panChargeTransfer.setMinimumSize(new java.awt.Dimension(850, 300));
        panChargeTransfer.setPreferredSize(new java.awt.Dimension(850, 300));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panLoanChargeDef.add(panChargeTransfer, gridBagConstraints);

        tabMasterMaintenanceDetails.addTab("Charges to Recover", panLoanChargeDef);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tabMasterMaintenanceDetails, gridBagConstraints);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace, gridBagConstraints);

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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(panStatus, gridBagConstraints);

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

        mitAuthorize.setText("Authorize");
        mnuProcess.add(mitAuthorize);

        mitReject.setText("Rejection");
        mnuProcess.add(mitReject);

        mitException.setText("Exception");
        mnuProcess.add(mitException);
        mnuProcess.add(sptException);

        mitPrint.setText("Print");
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSocietyDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSocietyDeleteActionPerformed
        // TODO add your handling code here:
        String s = CommonUtil.convertObjToStr(tblOtherSocietyDetails.getValueAt(tblOtherSocietyDetails.getSelectedRow(), 0));
        observable.deleteSocietyTableData(s, tblOtherSocietyDetails.getSelectedRow());
        observable.resetSocietyTypeDetails();
        resetSocietyTypeDetails();
        ClientUtil.enableDisable(panOtherSociety, false);
        enableDisableSocietyPanButton(false);
        btnSocietyNew.setEnabled(true);
    }//GEN-LAST:event_btnSocietyDeleteActionPerformed

    private void tblOtherSocietyDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOtherSocietyDetailsMousePressed
        // TODO add your handling code here:
        updateSocietyTypeFields();
        updateMode = true;
        updateTab = tblOtherSocietyDetails.getSelectedRow();
        observable.setSocietyTypeData(false);
        String st = CommonUtil.convertObjToStr(tblOtherSocietyDetails.getValueAt(tblOtherSocietyDetails.getSelectedRow(), 0));
        observable.populateSocietyTypeDetails(st);
        populateSocietyTypeFields();
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            enableDisableSocietyPanButton(false);
            ClientUtil.enableDisable(panOtherSociety, false);
        } else {
            enableDisableSocietyPanButton(true);
            btnSocietyNew.setEnabled(false);
            ClientUtil.enableDisable(panOtherSociety, true);
        }
        txtSecurityNoSociety.setEnabled(false);
    }//GEN-LAST:event_tblOtherSocietyDetailsMousePressed

    private void btnSocietySaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSocietySaveActionPerformed
        // TODO add your handling code here:
        try {
            updateSocietyTypeFields();
            observable.addSocietyTypeTable(updateTab, updateMode);
            tblOtherSocietyDetails.setModel(observable.getTblOtherSocietyDetails());
            observable.resetSocietyTypeDetails();
            resetSocietyTypeDetails();
            ClientUtil.enableDisable(panOtherSociety, false);
            enableDisableSocietyPanButton(false);
            btnSocietyNew.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSocietySaveActionPerformed

    private void btnSocietyNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSocietyNewActionPerformed
        // TODO add your handling code here:
        updateMode = false;
        observable.setSocietyTypeData(true);
        enableDisableSocietyPanButton(false);
        btnSocietySave.setEnabled(true);
        ClientUtil.enableDisable(panOtherSociety, true);
    }//GEN-LAST:event_btnSocietyNewActionPerformed

    private void cboProductTypeSecurityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductTypeSecurityActionPerformed
        // TODO add your handling code here:
        if (cboProductTypeSecurity.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel) cboProductTypeSecurity.getModel()).getKeyForSelected().toString();
            observable.setCbmProdTypeSecurity(prodType);
            cboDepProdType.setModel(observable.getCbmDepProdID());
            if (prodType.equals("TD")) {
                lblProductId.setText("Product Id");
                lblDepNo.setText("Deposit No");
                lblDepDt.setText("Dep Date");
                lblDepAmount.setText("Dep Amount");
                lblMaturityValue.setText("Maturity Value");
                lblMaturityDt.setText("Maturity Date");
                txtRateOfInterest.setVisible(true);
                lblRateOfInterest.setVisible(true);
            } else {
                lblProductId.setText("Scheme Name");
                lblDepNo.setText("Chittal No");
                lblDepDt.setText("Scheme StartDt");
                lblDepAmount.setText("Inst Amount");
                lblMaturityValue.setText("Paid Amount");
                lblMaturityDt.setText("Scheme EndDt");
                txtRateOfInterest.setText("");
                txtRateOfInterest.setVisible(false);
                lblRateOfInterest.setVisible(false);
            }
        }
    }//GEN-LAST:event_cboProductTypeSecurityActionPerformed

    private void txtBondNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBondNoFocusLost
        // TODO add your handling code here:
        if (txtBondNo.getText().length() > 0) {
            tdtBondDt.setEnabled(true);
        } else {
            tdtBondDt.setDateValue("");
            tdtBondDt.setEnabled(false);
        }
    }//GEN-LAST:event_txtBondNoFocusLost

    private void btnDepNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepNoActionPerformed
        // TODO add your handling code here:
        popUp("DEPOSIT_ACC_NO");
    }//GEN-LAST:event_btnDepNoActionPerformed

    private void chkNomineeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkNomineeActionPerformed
        // TODO add your handling code here:
        if (chkNominee.isSelected() == true) {
            tabMasterMaintenanceDetails.add(nomineeUi, "Nominee");
            tabMasterMaintenanceDetails.resetVisits();
            ClientUtil.enableDisable(nomineeUi, true);
            nomineeUi.resetNomineeTab();
            nomineeUi.enableDisableNominee_SaveDelete();
            nomineeUi.setMainCustomerId(txtChittalNo.getText() + "_" + txtSubNo.getText());
        } else if (chkNominee.isSelected() == false) {
            tabMasterMaintenanceDetails.remove(nomineeUi);
            tabMasterMaintenanceDetails.resetVisits();
        } else if ((chkNominee.isSelected() == false) && (nomineeUi.getTblRowCount() == 0)) {
            resetNominee();
        }
    }//GEN-LAST:event_chkNomineeActionPerformed

    private void chkStandingInstnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkStandingInstnActionPerformed
        // TODO add your handling code here:
        boolean flag;
        if (chkStandingInstn.isSelected() == true) {
            flag = true;
            setVisibleStandingIns(true);
            observable.setChkStandingInstn(chkStandingInstn.isSelected());
        } else {
            flag = false;
            setVisibleStandingIns(false);
        }
    }//GEN-LAST:event_chkStandingInstnActionPerformed
    private void setVisibleStandingIns(boolean flag) {
        lblProductType.setVisible(flag);
        cboProdType.setVisible(flag);
        lblProdId.setVisible(flag);
        cboProdId.setVisible(flag);
        lblAccountNo.setVisible(flag);
        panCustomerNO.setVisible(flag);
        btnCustomerIdFileOpenCr.setEnabled(flag);
        txtCustomerIdCr.setText("");
    }
    private void btnCustomerIdFileOpenCrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerIdFileOpenCrActionPerformed
        // TODO add your handling code here:
        popUp("CREDIT_ACC_NO");
    }//GEN-LAST:event_btnCustomerIdFileOpenCrActionPerformed

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
        if (cboProdType.getSelectedIndex() > 0) {        // TODO add your handling code here:
            String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
            observable.setCbmProdId(prodType);
            if (prodType.equals("GL")) {
                cboProdId.setEnabled(false);
                txtCustomerIdCr.setText("");
                lblAccountNo.setText("Account Head Id");
                btnCustomerIdFileOpenCr.setEnabled(true);
            } else {
                cboProdId.setEnabled(true);
                lblAccountNo.setText("Account No");
                txtCustomerIdCr.setText("");
                btnCustomerIdFileOpenCr.setEnabled(true);
                txtCustomerIdCr.setEnabled(false);
            }
            if (!prodType.equals("GL")) {
                cboProdId.setModel(observable.getCbmProdId());
            }
        }
    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void txtChittalNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChittalNoFocusLost
        // TODO add your handling code here:
        if (txtChittalNo.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("CHITTAL_NUMBER", txtChittalNo.getText());
            List lst = ClientUtil.executeQuery("getSelectChittalNo", whereMap);
            if (lst != null && lst.size() > 0) {
                viewType = "CHITTAL_NO";
                whereMap = (HashMap) lst.get(0);
                List chitLst = ClientUtil.executeQuery("getSelectChitNoNotinMasterDetails", whereMap);
                if (chitLst != null && chitLst.size() > 0) {
                    whereMap = (HashMap) chitLst.get(0);
                    fillData(whereMap);
                    tdtPayDt.setEnabled(false);
                    txtPrizedAmount.setEnabled(false);
                    txtMemberNo.setEnabled(false);
                    btnMemNo.setEnabled(false);
                    ClientUtil.enableDisable(panMdsSchemeNameDetails, false);
                    ClientUtil.enableDisable(panMemberDetails, false);
                    ClientUtil.enableDisable(panDepositType, false);
                    ClientUtil.enableDisable(panOtherSociety, false);
                    ClientUtil.enableDisable(panStandingDetails, false);
                    chkNominee.setEnabled(false);
                    if (tdtBondDt.getDateValue() == null || tdtBondDt.getDateValue().equals("")) {
                        tdtBondDt.setDateValue(daate);
                    }
                    chitLst = null;
                    lst = null;
                    whereMap = null;
                }
            } else {
                ClientUtil.displayAlert("Invalid Chittal No !!! ");
                txtChittalNo.setText("");
            }
        }
        onlyApplnEnaDis(txtChittalNo.getText());
    }//GEN-LAST:event_txtChittalNoFocusLost

    private void onlyApplnEnaDis(String chittalNo)
    {
        HashMap mapOnllAppn= new HashMap();
        mapOnllAppn.put("CHITTAL_NO", chittalNo);
        List lstMapOnlyAppln=ClientUtil.executeQuery("getApplnNoSet", mapOnllAppn);
        if(lstMapOnlyAppln.size()>0 && lstMapOnlyAppln!=null)
        {
          mapOnllAppn=  (HashMap)lstMapOnlyAppln.get(0);
        }
        String applnNo=CommonUtil.convertObjToStr(mapOnllAppn.get("APPLICATION_NO"));
        String applnSet=CommonUtil.convertObjToStr(mapOnllAppn.get("APPLICATIONSET"));
        if(applnNo.equals("N") && applnSet.equals("N"))
        {
            cbxOnlyApplication.setEnabled(true);
        }
        else
        {
            cbxOnlyApplication.setEnabled(false);
        }
    }
    private void txtSchemeNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSchemeNameFocusLost
        // TODO add your handling code here:
        if (txtSchemeName.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("SCHEME_NAME", txtSchemeName.getText());
            List lst = ClientUtil.executeQuery("getSelectSchemeNameForApplication", whereMap);

            // getSelectEachSchemeDetails
            if (lst != null && lst.size() > 0) {
                observable.setTxtSchemeName(txtSchemeName.getText());
                viewType = "SCHEME_DETAILS";
                whereMap = (HashMap) lst.get(0);
                fillData(whereMap);
                lst = null;
                whereMap = null;
                btnSchemeName.setEnabled(false);
                btnChittalNo.setEnabled(true);
                btnMemberNo.setEnabled(true);
                txtMemberNo.setEnabled(false);
                txtChittalNo.setEnabled(true);
                chkNominee.setEnabled(false);
                tdtBondDt.setDateValue(daate);
            } else {
                ClientUtil.displayAlert("Invalid Scheme Name !!! ");
                txtSchemeName.setText("");
                txtChittalNo.setText("");
                ClientUtil.clearAll(this);
            }
        }
    }//GEN-LAST:event_txtSchemeNameFocusLost

    private void txtMaturityDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMaturityDtFocusLost
        // TODO add your handling code here:
        java.util.Date depositDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtDepDt.getDateValue()));
        java.util.Date maturietDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(txtMaturityDt.getDateValue()));
        if (DateUtil.dateDiff(depositDate, maturietDate) < 0) {
            ClientUtil.showAlertWindow("Maturity Date Should be on or After Deposit Date !!!");
            txtMaturityDt.setDateValue("");
            return;
        }
    }//GEN-LAST:event_txtMaturityDtFocusLost
    private void resetNominee() {
        nomineeUi.resetNomineeData();
        nomineeUi.resetTable();
        nomineeUi.resetNomineeTab();
        nomineeUi.setBtnEnableDisable(false);
        //        observable.setChkNomineeDetails(false);
        //        chkNominee.setSelected(observable.getChkNomineeDetails());
        ClientUtil.enableDisable(nomineeUi, false);
    }
    private void txtMemNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMemNoFocusLost
        // TODO add your handling code here:
        if (txtMemNo.getText().length() > 0) {
            HashMap listMap = new HashMap();
            listMap.put("MEMBERSHIP_NO", txtMemNo.getText());
            java.util.List lst = ClientUtil.executeQuery("getMemeberShipDetails", listMap);
            if (lst != null && lst.size() > 0) {
                listMap = (HashMap) lst.get(0);
                viewType = "SUB_MEMBER_NO";
                fillData(listMap);
            } else {
                ClientUtil.showAlertWindow("Invalid Member No");
                txtMemNo.setText("");
                observable.setTxtMemNo("");
            }
        }
    }//GEN-LAST:event_txtMemNoFocusLost

    private void btnMemNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMemNoActionPerformed
        // TODO add your handling code here:
        popUp("SUB_MEMBER_NO");
    }//GEN-LAST:event_btnMemNoActionPerformed

    private void btnDepositDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositDeleteActionPerformed
        // TODO add your handling code here:
        String s = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(tblDepositDetails.getSelectedRow(), 1));
        observable.deleteDepositTableData(s, tblDepositDetails.getSelectedRow());
        observable.resetDepositTypeDetails();
        resetDepositTypeDetails();
        ClientUtil.enableDisable(panDepositType, false);
        enableDisableDepositPanButton(false);
        btnDepositNew.setEnabled(true);
        btnDepNo.setEnabled(false);
        calculateTot();
        if (tblDepositDetails.getRowCount() == 0) {
            lblTotalDepositValue.setText(CurrencyValidation.formatCrore(String.valueOf("0")));
        }
    }//GEN-LAST:event_btnDepositDeleteActionPerformed

    private void btnMemberDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMemberDeleteActionPerformed
        // TODO add your handling code here:
        String s = CommonUtil.convertObjToStr(tblMemberType.getValueAt(tblMemberType.getSelectedRow(), 0));
        observable.deleteMemberTableData(s, tblMemberType.getSelectedRow());
        observable.resetMemberTypeDetails();
        resetMemberTypeDetails();
        ClientUtil.enableDisable(panMemberDetails, false);
        enableDisableMemberPanButton(false);
        btnMemberNew.setEnabled(true);
    }//GEN-LAST:event_btnMemberDeleteActionPerformed

    private void tblDepositDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDepositDetailsMousePressed
        // TODO add your handling code here:
        updateDepositTypeFields();
        updateMode = true;
        updateTab = tblDepositDetails.getSelectedRow();
        observable.setDepositTypeData(false);
        String depProdType = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(tblDepositDetails.getSelectedRow(), 0));
        observable.setCbmProdTypeSecurity(depProdType);
        cboDepProdType.setModel(observable.getCbmDepProdID());
        String st = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(tblDepositDetails.getSelectedRow(), 1));
        observable.populateDepositTypeDetails(st);
        populateDepositTypeFields();
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            enableDisableDepositPanButton(false);
        } else {
            enableDisableDepositPanButton(true);
            btnDepositNew.setEnabled(false);
        }
        ClientUtil.enableDisable(panDepositType, false);
        btnDepNo.setEnabled(false);
        txtDepNo.setEnabled(false);
    }//GEN-LAST:event_tblDepositDetailsMousePressed

    private void btnDepositSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositSaveActionPerformed
        // TODO add your handling code here:
        try {
            HashMap whereMap = new HashMap();
            whereMap.put("DEPOSIT_NO", txtDepNo.getText());
            String prodtype = CommonUtil.convertObjToStr(cboProductTypeSecurity.getSelectedItem());

            List recordList = null;
            List depList = null;
            double depAmount = 0.0;
            if ((prodtype.equals("TD") || prodtype.equals("Deposits"))&& (txtChittalNo.getText()!=(txtDepNo.getText(0,13)))) {
                //in case of same chittal as lien no need for checking aval bal
                System.out.println("same chittal ---->"+cbxSameChittal.isSelected());
                if(!cbxSameChittal.isSelected()){
                    depList = ClientUtil.executeQuery("getAvailableBalForDep", whereMap);
                    HashMap hmap = (HashMap) depList.get(0);
                    depAmount = CommonUtil.convertObjToDouble(hmap.get("AVAILABLE_BALANCE")).doubleValue();
                }
            } else {
                recordList = ClientUtil.executeQuery("checkDepositNoAlreadyinMaster", whereMap);
            }

            if (((recordList != null && recordList.size() > 0) || (depList != null && depList.size() > 0 && depAmount <= 0.0)) && !updateMode) {
                ClientUtil.showMessageWindow("This Deposit has Already been Used as Security !!!");
            } else {
                updateDepositTypeFields();
                observable.addDepositTypeTable(updateTab, updateMode);
                tblDepositDetails.setModel(observable.getTblDepositTypeDetails());
                observable.resetDepositTypeDetails();
            }
            resetDepositTypeDetails();
            ClientUtil.enableDisable(panDepositType, false);
            enableDisableDepositPanButton(false);
            btnDepositNew.setEnabled(true);
            btnDepNo.setEnabled(false);
            //  cbxSameChittal.setSelected(false);
            calculateTot();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnDepositSaveActionPerformed

    public void calculateTot() {
        double totDeposit = 0;
        for (int i = 0; i < tblDepositDetails.getRowCount(); i++) {
            totDeposit = totDeposit + CommonUtil.convertObjToDouble(tblDepositDetails.getValueAt(i, 2).toString()).doubleValue();
            lblTotalDepositValue.setText(CurrencyValidation.formatCrore(String.valueOf(totDeposit)));
        }
        setSizeTableData();
    }

    private void tblMemberTypeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMemberTypeMousePressed
        // TODO add your handling code here:
        updateMemberTypeFields();
        updateMode = true;
        updateTab = tblMemberType.getSelectedRow();
        observable.setMemberTypeData(false);
        String st = CommonUtil.convertObjToStr(tblMemberType.getValueAt(tblMemberType.getSelectedRow(), 0));
        observable.populateMemberTypeDetails(st);
        populateMemberTypeFields();
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            enableDisableMemberPanButton(false);
            ClientUtil.enableDisable(panMemberDetails, false);
        } else {
            enableDisableMemberPanButton(true);
            ClientUtil.enableDisable(panMemberDetails, true);
            btnMemberNew.setEnabled(false);
        }
        txtMemNo.setEnabled(false);
    }//GEN-LAST:event_tblMemberTypeMousePressed

    private void tblMemberTypeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMemberTypeMouseClicked
    }//GEN-LAST:event_tblMemberTypeMouseClicked

    private void btnMemberSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMemberSaveActionPerformed
        // TODO add your handling code here:
        try {
            if (txtMemNo.getText().length() == 0) {
                ClientUtil.showAlertWindow("Member number should not be empty");
            } else {
                //Added by nithya for KD-2966
                double maxSuretyAmount = 0.0;
                HashMap memMap = new HashMap();
                memMap.put("MEMBER_NO", txtMemNo.getText());
                List suretyList = ClientUtil.executeQuery("getMaxSurety", memMap);
                if (suretyList != null && suretyList.size() > 0) {
                    HashMap surMap = (HashMap) suretyList.get(0);                   
                    if (surMap.containsKey("MAXIMUM_SURETY_AMT") && surMap.get("MAXIMUM_SURETY_AMT") != null) {
                        maxSuretyAmount = CommonUtil.convertObjToInt(surMap.get("MAXIMUM_SURETY_AMT"));
                    }                 
                    List loanSuertyAmtLst = ClientUtil.executeQuery("getTotAmtSetAsSuretyForMember", memMap);
                    if(loanSuertyAmtLst != null && loanSuertyAmtLst.size() > 0){
                        HashMap suretyAmtMap = (HashMap)loanSuertyAmtLst.get(0);
                        double totSuretyAmt = CommonUtil.convertObjToDouble(suretyAmtMap.get("TOT_SURETY_AMT"));
                        double networthGiven = CommonUtil.convertObjToDouble(txtMemNetworth.getText());
                        if(maxSuretyAmount > 0 && (totSuretyAmt + networthGiven) > maxSuretyAmount){
                           ClientUtil.showMessageWindow("The surety amount exceeds maximum surety amount Rs."+maxSuretyAmount+"/-for this member !!!"
                                   + "\nThis Member Stand Surety of Amt Rs. " + totSuretyAmt + "/-");
                           txtMemNetworth.setText("");
                           return;
                        }
                    }                    
                }
                updateMemberTypeFields();
                observable.addMemberTypeTable(updateTab, updateMode);
                tblMemberType.setModel(observable.getTblMemberTypeDetails());
                observable.resetMemberTypeDetails();
                resetMemberTypeDetails();
                ClientUtil.enableDisable(panMemberDetails, false);
                //            enableDisablePanScheduleDetails(false);
                enableDisableMemberPanButton(false);
                btnMemberNew.setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnMemberSaveActionPerformed
    private void resetMemberTypeDetails() {
        txtMemNo.setText("");
        txtMemName.setText("");
        txtMemType.setText("");
        txtContactNum.setText("");
        txtMemNetworth.setText("");
        txtMemPriority.setText("");
    }

    private void resetDepositTypeDetails() {
        txtDepNo.setText("");
        cboProductTypeSecurity.setSelectedItem("");
        cboDepProdType.setSelectedItem("");
        tdtDepDt.setDateValue("");
        txtDepAmount.setText("");
        txtRateOfInterest.setText("");
        txtMaturityValue.setText("");
        txtMaturityDt.setDateValue("");
    }

    private void resetSocietyTypeDetails() {
        txtSecurityNoSociety.setText("");
        cboOtherInstituion.setSelectedItem("");
        cboSecurityTypeSociety.setSelectedItem("");
        tdtIssueDtSoceity.setDateValue("");
        tdtMaturityDateSociety.setDateValue("");
        txtOtherInstituionName.setText("");
        txtSecurityAmountSociety.setText("");
        txtMaturityValueSociety.setText("");
        txtRemarksSociety.setText("");
    }
    private void btnMemberNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMemberNoActionPerformed
        // TODO add your handling code here:
        popUp("MEMBER_NO");
        tdtPayDt.setEnabled(false);
        txtPrizedAmount.setEnabled(false);
        txtMemberNo.setEnabled(false);
        ClientUtil.enableDisable(panMdsSchemeNameDetails, false);
    }//GEN-LAST:event_btnMemberNoActionPerformed

    private void btnChittalNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChittalNoActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        nomineeUi.resetTable();
        nomineeUi.resetNomineeData();
        nomineeUi.resetNomineeTab();
        nomineeUi.disableNewButton(false);
        rdoGoldSecurityExitsNo.setSelected(false);
        rdoGoldSecurityExitsYes.setSelected(false);
        popUp("CHITTAL_NO");
        tdtPayDt.setEnabled(false);
        txtPrizedAmount.setEnabled(false);
        txtMemberNo.setEnabled(false);
        btnMemNo.setEnabled(false);
        ClientUtil.enableDisable(panMdsSchemeNameDetails, false);
        ClientUtil.enableDisable(panMemberDetails, false);
        ClientUtil.enableDisable(panDepositType, false);
        ClientUtil.enableDisable(panOtherSociety, false);
        ClientUtil.enableDisable(panStandingDetails, false);
        chkNominee.setEnabled(false);
        if (tdtBondDt.getDateValue() == null || tdtBondDt.getDateValue().equals("")) {
            tdtBondDt.setDateValue(daate);
        }
        onlyApplnEnaDis(txtChittalNo.getText());
//        ClientUtil.enableDisable(panStandingDetails, true);
//                ClientUtil.enableDisable(panMasterMaintenanceDetails, true);
                populateMasterMaintenanceFields();
                calculateTot();
                ClientUtil.enableDisable(panInstallmentDetails, false);
                txtRemarks.setEnabled(false);
                btnCustomerIdFileOpenCr.setEnabled(true);
                btnChittalNo.setEnabled(true);
                btnSchemeName.setEnabled(true);
                tdtChitCloseDt.setEnabled(true);
               
    }//GEN-LAST:event_btnChittalNoActionPerformed

    private void btnSchemeNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSchemeNameActionPerformed
        // TODO add your handling code here:
        rdoGoldSecurityExitsNo.setSelected(false);
        rdoGoldSecurityExitsYes.setSelected(false);
        popUp("SCHEME_DETAILS");
        btnSchemeName.setEnabled(false);
        btnChittalNo.setEnabled(true);
        btnMemberNo.setEnabled(true);
        txtMemberNo.setEnabled(false);
        txtChittalNo.setEnabled(true);
        chkNominee.setEnabled(false);
        tdtBondDt.setDateValue(daate);
//        if(txtSchemeName.getText().length()>0){
//            btnChittalNo.setEnabled(true);
//            btnSchemeName.setEnabled(false);
//        }else{
//            btnChittalNo.setEnabled(false);
//            btnSchemeName.setEnabled(true);
//        }
        
    }//GEN-LAST:event_btnSchemeNameActionPerformed

    private void txtResolutionNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtResolutionNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtResolutionNoActionPerformed
    private void resetUIFields() {
        txtRemarks.setText("");
        txtOverDueAmount.setText("");
        txtTotalAmountTillDate.setText("");
        txtLastInstNo.setText("");
        txtOverDueInstallments.setText("");
        tdtLastInstDate.setDateValue("");
        txtChittalNo.setText("");
        tdtChitStartDt.setDateValue("");
        txtDivisionNo.setText("");
//        txtSchemeName.setText("");
        txtMemberNo.setText("");
        tdtPayDt.setDateValue("");
        observable.setTdtPayDt("");
        observable.setTxtPrizedAmount(0.0);
        txtPrizedAmount.setText("");
        lblNomineName.setText("");
        lblMembershipType.setText("");
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        rdoGoldSecurityExitsNo.setSelected(false);
        rdoGoldSecurityExitsYes.setSelected(false);
        txtGoldSecurityId.setText("");
    }
    private void btnDepositNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositNewActionPerformed
        // TODO add your handling code here:
        updateMode = false;
        observable.setDepositTypeData(true);
        enableDisableDepositPanButton(false);
        btnDepositSave.setEnabled(true);

        ClientUtil.enableDisable(panDepositType, false);
        cbxSameChittal.setEnabled(true);
        btnDepNo.setEnabled(true);
        cboProductTypeSecurity.setEnabled(true);
        cboDepProdType.setEnabled(true);
        cbxSameChittal.setSelected(false);
    }//GEN-LAST:event_btnDepositNewActionPerformed

    private void btnMemberNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMemberNewActionPerformed
        // TODO add your handling code here:
        updateMode = false;
        observable.setMemberTypeData(true);
        enableDisableMemberPanButton(false);
        btnMemberSave.setEnabled(true);
        ClientUtil.enableDisable(panMemberDetails, true);
        btnMemNo.setEnabled(true);
        txtMemName.setEnabled(false);
        txtMemType.setEnabled(false);

    }//GEN-LAST:event_btnMemberNewActionPerformed

    private void enableDisableMemberPanButton(boolean flag) {
        btnMemberNew.setEnabled(flag);
        btnMemberSave.setEnabled(flag);
        btnMemberDelete.setEnabled(flag);
    }

    private void enableDisableDepositPanButton(boolean flag) {
        btnDepositNew.setEnabled(flag);
        btnDepositSave.setEnabled(flag);
        btnDepositDelete.setEnabled(flag);
    }

    private void enableDisableSocietyPanButton(boolean flag) {
        btnSocietyNew.setEnabled(flag);
        btnSocietySave.setEnabled(flag);
        btnSocietyDelete.setEnabled(flag);
    }
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        btnView.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        popUp("Enquiry");
        ClientUtil.enableDisable(panMasterMaintenanceDetails, false);
        btnSave.setEnabled(false);
        btnView.setEnabled(false);
    }            //    private void enableDisableAliasBranchTable(boolean flag) {//GEN-LAST:event_btnViewActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        updateAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);

    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void updateAuthorizeStatus(String authorizeStatus) {

        if (viewType == AUTHORIZE && isFilled) {
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("SCHEME_NAME", observable.getTxtSchemeName());
            singleAuthorizeMap.put("CHITTAL_NO", observable.getTxtChittalNo());
            singleAuthorizeMap.put("SUB_NO", CommonUtil.convertObjToInt(observable.getTxtSubNo())); //AJITH
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT", ClientUtil.getCurrentDateWithTime());
            if (tblDepositDetails != null && tblDepositDetails.getRowCount() > 0) {
                ArrayList lienList = new ArrayList();
                for (int i = 0; i < tblDepositDetails.getRowCount(); i++) {
                    HashMap hmap = new HashMap();
                    String depNo = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(i, 1));
                    hmap.put("DEPOSIT_NO", depNo);
                    hmap.put("CHITTAL_NO", txtChittalNo.getText());

                    List list = ClientUtil.executeQuery("getLientAmountForMDS", hmap);
                    if (list != null && list.size() > 0) {
                        hmap = (HashMap) list.get(0);
                        double lienAmt = CommonUtil.convertObjToDouble(hmap.get("LIEN_AMOUNT")).doubleValue();
                        hmap.put("STATUS", authorizeStatus);
                        if (authorizeStatus.equals("AUTHORIZED")) {
                            hmap.put("LIENAMOUNT", new Double(lienAmt));
                        } else {
                            hmap.put("LIENAMOUNT", new Double(0.0));
                        }
                        hmap.put("SHADOWLIEN", hmap.get("LIEN_AMOUNT"));
                        hmap.put("DEPOSIT_ACT_NUM", depNo);
                        hmap.put("SUBNO", CommonUtil.convertObjToInt("1"));
                        hmap.put("CHITTAL_NO", txtChittalNo.getText());
                        hmap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
                        hmap.put("AUTHORIZED_DT", ClientUtil.getCurrentDateWithTime());
                        hmap.put("USER_ID", TrueTransactMain.USER_ID);

                        lienList.add(hmap);
                    }
                }
                if (lienList != null && lienList.size() > 0) {
                    singleAuthorizeMap.put("LIEN_DETAILS", lienList);
                }
            }

            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap, observable.getTxtSchemeName());
            viewType = "";
            super.setOpenForEditBy(observable.getStatusBy());
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getMDSMasterMaintenanceAuthorize");
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    public void authorize(HashMap map, String id) {
        //system.out.println("Authorize Map : " + map);

        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            if (transactionUI.getOutputTO().size() > 0) {
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }
            observable.doAction(nomineeUi.getNomineeOB());
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(id);
                 if (fromNewAuthorizeUI) {
                    newauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    newauthorizeListUI.setFocusToTable();
                    newauthorizeListUI.displayDetails("MDS Master Maintenance");
                }
                if (fromAuthorizeUI) {
                    authorizeListUI.removeSelectedRow();
                    this.dispose();
                    authorizeListUI.setFocusToTable();
                    authorizeListUI.displayDetails("MDS Master Maintenance");
                }
            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        ClientUtil.enableDisable(panMasterMaintenanceDetails, true);
        ClientUtil.enableDisable(panMemberDetails, false);
        ClientUtil.enableDisable(panDepositType, false);
        ClientUtil.enableDisable(panOtherSociety, false);
        ClientUtil.enableDisable(panMdsSchemeNameDetails, false);
        ClientUtil.clearAll(this);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        btnMemberNew.setEnabled(true);
        btnDepositNew.setEnabled(true);
        btnSocietyNew.setEnabled(true);
        btnSchemeName.setEnabled(true);
        txtSchemeName.setEnabled(false);
        txtMemberNo.setEnabled(true);
        btnMemberNo.setEnabled(true);
        tdtPayDt.setEnabled(false);
        txtPrizedAmount.setEnabled(false);
        txtLastInstNo.setEnabled(false);
        tdtLastInstDate.setEnabled(false);
        txtTotalAmountTillDate.setEnabled(false);
        txtOverDueInstallments.setEnabled(false);
        txtOverDueAmount.setEnabled(false);
        txtRemarks.setEnabled(false);
        txtMemName.setEnabled(false);
        txtMemType.setEnabled(false);
        txtSubNo.setEnabled(false);
        ClientUtil.enableDisable(panStandingDetails, false);
        thalayal = false;
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        setModified(true);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
//        popUp("Edit");
        btnSchemeName.setEnabled(true);
        txtMemberNo.setEnabled(false);
        btnMemberNew.setEnabled(true);
        btnDepositNew.setEnabled(true);
        btnSave.setEnabled(true);
        ClientUtil.enableDisable(panMasterMaintenanceDetails, false);
        ClientUtil.enableDisable(panMemberDetails, false);
        ClientUtil.enableDisable(panDepositType, false);
        ClientUtil.enableDisable(panOtherSociety, false);
        ClientUtil.enableDisable(panMdsSchemeNameDetails, false);
        btnMemberNew.setEnabled(true);
        btnDepositNew.setEnabled(true);
        btnSocietyNew.setEnabled(true);
        btnSchemeName.setEnabled(true);
        btnMemberNo.setEnabled(false);
        btnDelete.setEnabled(false);
        btnView.setEnabled(false);
        btnEdit.setEnabled(false);
        btnCancel.setEnabled(true);
        btnChittalNo.setEnabled(true);
        txtSchemeName.setEnabled(true);
        txtChittalNo.setEnabled(true);
        btnCaseNew.setEnabled(true);
        txtSubNo.setEnabled(false);
        addGahanRadioBtns();
        btnCollateralNew.setEnabled(true);

        ClientUtil.enableDisable(panStandingDetails, false);
        chkNominee.setEnabled(false);
        tdtChitCloseDt.setEnabled(true);
        thalayal = false;    
        enableDisableGoldType(true);
        
        rdoGoldSecurityExitsNo.setSelected(true); // Added by nithya on 07-03-2020 for KD-1379
        txtGoldSecurityId.setEnabled(false);
        btnGoldSecurityIdSearch.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        transNew = false;
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(panMasterMaintenanceDetails, false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        //btnSave.setEnabled(false);
        //System.out.println("user defined :: " + observable.getUserDefinedAuction());
        updateMasterMaintenanceFields();
        setModified(false);
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panInsideMasterMaintenanceDetails);
        mandatoryMessage = mandatoryMessage + new MandatoryCheck().checkMandatory(getClass().getName(), panMdsSchemeNameDetails);
        //system.out.println("mandatoryMessage..." + mandatoryMessage);
        HashMap hashmap = new HashMap();
        //jiby
        HashMap mapProd= new HashMap();
          mapProd.put("PROD_ID", observable.getTxtProductId());
          List lstPrizdPend=ClientUtil.executeQuery("getSelPrizdPend", mapProd);
          if(lstPrizdPend.size()>0 && !lstPrizdPend.isEmpty())
          {
           mapProd= new HashMap();
           mapProd=(HashMap)lstPrizdPend.get(0);
          }  
          prizdPend =CommonUtil.convertObjToStr(mapProd.get("SECURITY_PRIZED"));
          secPend = CommonUtil.convertObjToStr(mapProd.get("SECURITY_PENDING"));
          if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
            HashMap paymentMap = new HashMap();
            paymentMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(txtSchemeName.getText()));
            paymentMap.put("CHITTAL_NO", CommonUtil.convertObjToStr(txtChittalNo.getText()));
            paymentMap.put("SUB_NO", CommonUtil.convertObjToInt(txtSubNo.getText()));   //AJITH
            List paymentList = ClientUtil.executeQuery("getSchemeNameDetailsNoOfMember",paymentMap);
            if(paymentList != null && paymentList.size()>0){
                paymentMap = (HashMap) paymentList.get(0);
                Date paymentDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paymentMap.get("PAYMENT_DATE")));
                if(DateUtil.dateDiff(paymentDate, (Date)currDate.clone())<0){
                    ClientUtil.showAlertWindow("Please make payment on or after "+DateUtil.getStringDate(paymentDate));
                    return;
                }
            }
        }
          System.out.println("prizdPend:::::"+prizdPend);
          ///prized
          if(prizdPend.equals("Y"))    
          {
            Double prizedAmount=CommonUtil.convertObjToDouble(txtPrizedAmount.getText().toString());
            System.out.println("prizedAmount==="+prizedAmount);
            double depAmount = 0.0;
            double netWorth = 0.0;
            double memNetWorth = 0.0;
            double pledgeAmount = 0.0;
            double losAmount = 0.0;
            double valuOfGold = CommonUtil.convertObjToDouble(txtValueOfGold.getText()).doubleValue();
            for (int i = 0; i < tblSalaryDetails.getRowCount(); i++) {
                netWorth = netWorth + CommonUtil.convertObjToDouble(tblSalaryDetails.getValueAt(i, 5)).doubleValue();
                String custid = CommonUtil.convertObjToStr(tblSalaryDetails.getValueAt(i, 2));
                hashmap.put("CUST_ID", custid);
                hashmap.put("MEMBER_NO", custid);
                List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                if (lst1 != null && lst1.size() > 0) {
                    ClientUtil.displayAlert("Customer is death marked please select another customerId");
                    return;
                }
            }
            for (int j = 0; j < tblMemberType.getRowCount(); j++) {
                memNetWorth = memNetWorth + CommonUtil.convertObjToDouble(tblMemberType.getValueAt(j, 4)).doubleValue();
                String custid = CommonUtil.convertObjToStr(tblMemberType.getValueAt(j, 0));
                hashmap.put("CUST_ID", custid);
                hashmap.put("MEMBER_NO", custid);
                List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                if (lst1 != null && lst1.size() > 0) {
                    ClientUtil.displayAlert("Customer is death marked please select another customerId");
                    return;
                }
            }
            for (int k = 0; k < tblCollateral.getRowCount(); k++) {
                pledgeAmount = pledgeAmount + CommonUtil.convertObjToDouble(tblCollateral.getValueAt(k, 3)).doubleValue();
                String custid = CommonUtil.convertObjToStr(tblCollateral.getValueAt(k, 0));
                hashmap.put("CUST_ID", custid);
                hashmap.put("MEMBER_NO", custid);
                List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                if (lst1 != null && lst1.size() > 0) {
                    ClientUtil.displayAlert("Customer is death marked please select another customerId");
                    return;
                }
            }

            for (int l = 0; l < tblDepositDetails.getRowCount(); l++) {
                String depNo = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(l, 1));
                String prodtype = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(l, 0));
//                String securityAmt = txtPrizedAmount.getText();
//                observable.setSecurityAmt(securityAmt);
                HashMap hmap = new HashMap();
                hmap.put("DEPOSIT_NO", depNo);
                if (prodtype.equals("TD") || prodtype.equals("Deposits")) {
                    List lst = ClientUtil.executeQuery("getAvailableBalForDep", hmap);
                    if (lst != null && lst.size() > 0) {
                        hmap = (HashMap) lst.get(0);
                       // depAmount = depAmount + CommonUtil.convertObjToDouble(hmap.get("AVAILABLE_BALANCE")).doubleValue();
                        depAmount = depAmount+CommonUtil.convertObjToDouble(tblDepositDetails.getValueAt(l, 3)).doubleValue();
                        
                        hmap.put("CUST_ID", hmap.get("CUST_ID"));        
                        hmap.put("MEMBER_NO", hmap.get("CUST_ID"));
                        List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hmap);
                        if (lst1 != null && lst1.size() > 0) {
                            ClientUtil.displayAlert("Customer is death marked please select another customerId");
                            return;
                        }
                    }
                } else {
                    String mdsNo = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(l, 1));
                    hashmap.put("CHITTAL_NO", mdsNo);

                    List lst1 = ClientUtil.executeQuery("getCustIdDeathChecking", hashmap);
                    if (lst1 != null && lst1.size() > 0) {
                        hashmap = (HashMap) lst1.get(0);
                        hashmap.put("MEMBER_NO", hashmap.get("CUST_ID"));
                        hashmap.put("CUST_ID", hashmap.get("CUST_ID"));
                        lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                        if (lst1 != null && lst1.size() > 0) {
                            ClientUtil.displayAlert("Customer is death marked please select another customerId");
                            return;
                        }
                    }
                    depAmount = depAmount+CommonUtil.convertObjToDouble(tblDepositDetails.getValueAt(l, 3)).doubleValue();
                }


            }
            for (int m = 0; m < tblOtherSocietyDetails.getRowCount(); m++) {
                losAmount = losAmount + CommonUtil.convertObjToDouble(tblOtherSocietyDetails.getValueAt(m, 4)).doubleValue();
            }
            //added by sreekrishnan for dianamicaly calculate the balance security amount 
//            if(tblCollateral.getRowCount()>0){
//                pledgeAmount = observable.getCollateralSecurityAmount();        
//            }
            System.out.println("pledgeAmount=="+pledgeAmount+"valuOfGold=="+valuOfGold+"netWorth=="+netWorth+"memNetWorth=="+memNetWorth+"depAmount=="+depAmount+"losAmount=="+losAmount);    
            double tot = valuOfGold + netWorth + memNetWorth + pledgeAmount + depAmount + losAmount;
            System.out.println("tot=="+tot);
           // double sanctionAmt = CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue();
            if ((tot < prizedAmount) && !cbxDefaulter.isSelected() && !cbxOnlyApplication.isSelected() && !thalayal && observable.getUserDefinedAuction().equalsIgnoreCase("N")) { //Added UserDefinedAuction checking by nithya on 23-12-2019 for KD-1074
                ClientUtil.displayAlert("Security Amount is lesser than the Sanctioned amount");
                return;
            }
          }
          ///pending
//added by nitdhin
          else if(secPend.equals("Y"))
          {      
        HashMap mdsMap = new HashMap();
        String chittalNo = txtChittalNo.getText();
        //String subNo = txtSubNo.getText();    //AJITH Blocked on 13/08/2021
        Integer subNo = CommonUtil.convertObjToInt(txtSubNo.getText()); //AJITH New Line on 13/08/2021
        mdsMap.put("CHITTAL_NO", chittalNo);
        mdsMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
        double noinset = 0.0;
        double instAmt = 0.0;
        double paidInst = 0.0;
        double totAmt = 0.0;
        List mdsList = ClientUtil.executeQuery("getTotalInstAmount", mdsMap);
        List mdsList1 = ClientUtil.executeQuery("getSelctApplnReceiptDetails", mdsMap);
        if (mdsList1 != null && mdsList1.size() > 0) {
            mdsMap = (HashMap) mdsList1.get(0);
            noinset = CommonUtil.convertObjToDouble(mdsMap.get("NO_OF_INSTALLMENTS")).doubleValue();
            instAmt = CommonUtil.convertObjToDouble(mdsMap.get("INST_AMT")).doubleValue();
            totAmt = instAmt * noinset;
        }
        if (mdsList != null && mdsList.size() > 0) {
            mdsMap = (HashMap) mdsList.get(0);
            paidInst = CommonUtil.convertObjToDouble(mdsMap.get("NO_INST_PAID")).doubleValue();
            paidInst = paidInst * instAmt;
        }
        double tobepaidInst = totAmt - paidInst; 
        
        System.out.println("tobepaidInst==="+tobepaidInst);
              double depAmount = 0.0;
            double netWorth = 0.0;
            double memNetWorth = 0.0;
            double pledgeAmount = 0.0;
            double losAmount = 0.0;
            double valuOfGold = CommonUtil.convertObjToDouble(txtValueOfGold.getText()).doubleValue();
            for (int i = 0; i < tblSalaryDetails.getRowCount(); i++) {
                netWorth = netWorth + CommonUtil.convertObjToDouble(tblSalaryDetails.getValueAt(i, 5)).doubleValue();
                String custid = CommonUtil.convertObjToStr(tblSalaryDetails.getValueAt(i, 2));
                hashmap.put("CUST_ID", custid);
                hashmap.put("MEMBER_NO", custid);
                List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                if (lst1 != null && lst1.size() > 0) {
                    ClientUtil.displayAlert("Customer is death marked please select another customerId");
                    return;
                }
            }
            for (int j = 0; j < tblMemberType.getRowCount(); j++) {
                memNetWorth = memNetWorth + CommonUtil.convertObjToDouble(tblMemberType.getValueAt(j, 4)).doubleValue();
                String custid = CommonUtil.convertObjToStr(tblMemberType.getValueAt(j, 0));
                hashmap.put("CUST_ID", custid);
                hashmap.put("MEMBER_NO", custid);
                List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                if (lst1 != null && lst1.size() > 0) {
                    ClientUtil.displayAlert("Customer is death marked please select another customerId");
                    return;
                }
            }
            for (int k = 0; k < tblCollateral.getRowCount(); k++) {
                pledgeAmount = pledgeAmount + CommonUtil.convertObjToDouble(tblCollateral.getValueAt(k, 3)).doubleValue();
                String custid = CommonUtil.convertObjToStr(tblCollateral.getValueAt(k, 0));
                hashmap.put("CUST_ID", custid);
                hashmap.put("MEMBER_NO", custid);
                List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                if (lst1 != null && lst1.size() > 0) {
                    ClientUtil.displayAlert("Customer is death marked please select another customerId");
                    return;
                }
            }

            for (int l = 0; l < tblDepositDetails.getRowCount(); l++) {
                String depNo = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(l, 1));
                String prodtype = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(l, 0));
//                String securityAmt = txtPrizedAmount.getText();
//                observable.setSecurityAmt(securityAmt);
                HashMap hmap = new HashMap();
                hmap.put("DEPOSIT_NO", depNo);
                if (prodtype.equals("TD") || prodtype.equals("Deposits")) {
                    List lst = ClientUtil.executeQuery("getAvailableBalForDep", hmap);
                    if (lst != null && lst.size() > 0) {
                        hmap = (HashMap) lst.get(0);
                        depAmount = depAmount + CommonUtil.convertObjToDouble(hmap.get("AVAILABLE_BALANCE")).doubleValue();
                        hmap.put("MEMBER_NO", hmap.get("CUST_ID"));
                        hmap.put("CUST_ID", hmap.get("CUST_ID"));//edit by Nidhin
                        List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hmap);
                        System.out.println("lst1"+lst1);
                        if (lst1 != null && lst1.size() > 0) {
                            ClientUtil.displayAlert("Customer is death marked please select another customerId");
                            return;
                        }
                    }
                } else {
                    String mdsNo = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(l, 1));
                    hashmap.put("CHITTAL_NO", mdsNo);

                    List lst1 = ClientUtil.executeQuery("getCustIdDeathChecking", hashmap);
                    if (lst1 != null && lst1.size() > 0) {
                        hashmap = (HashMap) lst1.get(0);
                        hashmap.put("MEMBER_NO", hashmap.get("CUST_ID"));
                        hashmap.put("CUST_ID", hashmap.get("CUST_ID"));
                        lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hmap);
                        if (lst1 != null && lst1.size() > 0) {
                            ClientUtil.displayAlert("Customer is death marked please select another customerId");
                            return;
                        }
                    }
                    depAmount =depAmount+ CommonUtil.convertObjToDouble(tblDepositDetails.getValueAt(l, 3)).doubleValue();
                }
            }
            for (int m = 0; m < tblOtherSocietyDetails.getRowCount(); m++) {
                losAmount = losAmount + CommonUtil.convertObjToDouble(tblOtherSocietyDetails.getValueAt(m, 4)).doubleValue();
            }
            
            //added by sreekrishnan for dianamicaly calculate the balance security amount 
//            if(tblCollateral.getRowCount()>0){
//                pledgeAmount = observable.getCollateralSecurityAmount();        
//            }
            
            System.out.println("pledgeAmount=="+pledgeAmount+"valuOfGold=="+valuOfGold+"netWorth=="+netWorth+"memNetWorth=="+memNetWorth+"depAmount=="+depAmount+"losAmount=="+losAmount);    
            double tot = valuOfGold + netWorth + memNetWorth + pledgeAmount + depAmount + losAmount;
            System.out.println("tot=="+tot);
           // double sanctionAmt = CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue();
            if ((tot < tobepaidInst) && !cbxDefaulter.isSelected() && !cbxOnlyApplication.isSelected()&& !thalayal && observable.getUserDefinedAuction().equalsIgnoreCase("N")) { //Added UserDefinedAuction checking by nithya on 23-12-2019 for KD-1074
                ClientUtil.displayAlert("Security Amount is lesser than the Sanctioned amount");
                return;
            }
        
          }
          else{
        //added by nidhin
        double whichEverIsLessPrzOrInst = 0.0;      
        Double prizedAmount=CommonUtil.convertObjToDouble(txtPrizedAmount.getText().toString());
        HashMap mdsMap = new HashMap();
        String chittalNo = CommonUtil.convertObjToStr(txtChittalNo.getText());
        //String subNo = CommonUtil.convertObjToStr(txtSubNo.getText());    //AJITH Blocked on 13/08/2021
        Integer subNo = CommonUtil.convertObjToInt(txtSubNo.getText()); //AJITH New Line on 13/08/2021
        mdsMap.put("CHITTAL_NO", chittalNo);
        mdsMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
        double noinset = 0.0;
        double instAmt = 0.0;
        double paidInst = 0.0;
        double totAmt = 0.0;
        List mdsList = ClientUtil.executeQuery("getTotalInstAmount", mdsMap);
        List mdsList1 = ClientUtil.executeQuery("getSelctApplnReceiptDetails", mdsMap);
        if (mdsList1 != null && mdsList1.size() > 0) {
            mdsMap = (HashMap) mdsList1.get(0);
            noinset = CommonUtil.convertObjToDouble(mdsMap.get("NO_OF_INSTALLMENTS"));
            instAmt = CommonUtil.convertObjToDouble(mdsMap.get("INST_AMT"));
            totAmt = instAmt * noinset;
        }
        if (mdsList != null && mdsList.size() > 0) {
            mdsMap = (HashMap) mdsList.get(0);
            paidInst = CommonUtil.convertObjToDouble(mdsMap.get("NO_INST_PAID"));
            paidInst = noinset - paidInst; 
            paidInst = paidInst * instAmt;
        }
        
        double tobepaidInst = totAmt - paidInst; 
        if(prizedAmount < paidInst){
            whichEverIsLessPrzOrInst = prizedAmount;
        }else{
            whichEverIsLessPrzOrInst = paidInst;
        }
              double depAmount = 0.0;
            double netWorth = 0.0;
            double memNetWorth = 0.0;
            double pledgeAmount = 0.0;
            double losAmount = 0.0;
            double valuOfGold = CommonUtil.convertObjToDouble(txtValueOfGold.getText());
            for (int i = 0; i < tblSalaryDetails.getRowCount(); i++) {
                netWorth = netWorth + CommonUtil.convertObjToDouble(tblSalaryDetails.getValueAt(i, 5));
                String custid = CommonUtil.convertObjToStr(tblSalaryDetails.getValueAt(i, 2));
                hashmap.put("CUST_ID", custid);
                hashmap.put("MEMBER_NO", custid);
                List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                if (lst1 != null && lst1.size() > 0) {
                    ClientUtil.displayAlert("Customer is death marked please select another customerId");
                    return;
                }
            }
            for (int j = 0; j < tblMemberType.getRowCount(); j++) {
                memNetWorth = memNetWorth + CommonUtil.convertObjToDouble(tblMemberType.getValueAt(j, 4));
                String custid = CommonUtil.convertObjToStr(tblMemberType.getValueAt(j, 0));
                hashmap.put("CUST_ID", custid);
                hashmap.put("MEMBER_NO", custid);
                List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                if (lst1 != null && lst1.size() > 0) {
                    ClientUtil.displayAlert("Customer is death marked please select another customerId");
                    return;
                }
            }
            for (int k = 0; k < tblCollateral.getRowCount(); k++) {
                pledgeAmount = pledgeAmount + CommonUtil.convertObjToDouble(tblCollateral.getValueAt(k, 3));
                String custid = CommonUtil.convertObjToStr(tblCollateral.getValueAt(k, 0));
                hashmap.put("CUST_ID", custid);
                hashmap.put("MEMBER_NO", custid);
                List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                if (lst1 != null && lst1.size() > 0) {
                    ClientUtil.displayAlert("Customer is death marked please select another customerId");
                    return;
                }
            }

            for (int l = 0; l < tblDepositDetails.getRowCount(); l++) {
                String depNo = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(l, 1));
                String prodtype = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(l, 0));
//                String securityAmt = txtPrizedAmount.getText();
//                observable.setSecurityAmt(securityAmt);
                HashMap hmap = new HashMap();
                hmap.put("DEPOSIT_NO", depNo);
                if (prodtype.equals("TD") || prodtype.equals("Deposits")) {
                    List lst = ClientUtil.executeQuery("getAvailableBalForDep", hmap);
                    if (lst != null && lst.size() > 0) {
                        hmap = (HashMap) lst.get(0);
                        depAmount = depAmount + CommonUtil.convertObjToDouble(hmap.get("AVAILABLE_BALANCE"));
                        hmap.put("MEMBER_NO", hmap.get("CUST_ID"));
                        hmap.put("CUST_ID", hmap.get("CUST_ID"));//edit by Nidhin
                        List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hmap);
                        if (lst1 != null && lst1.size() > 0) {
                            ClientUtil.displayAlert("Customer is death marked please select another customerId");
                            return;
                        }
                    }
                   depAmount =depAmount+ CommonUtil.convertObjToDouble(tblDepositDetails.getValueAt(l, 3)).doubleValue();
                } else {
                    String mdsNo = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(l, 1));
                    hashmap.put("CHITTAL_NO", mdsNo);

                    List lst1 = ClientUtil.executeQuery("getCustIdDeathChecking", hashmap);
                    if (lst1 != null && lst1.size() > 0) {
                        hashmap = (HashMap) lst1.get(0);
                        hashmap.put("MEMBER_NO", hashmap.get("CUST_ID"));
                        hashmap.put("CUST_ID", hashmap.get("CUST_ID"));
                        lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hmap);
                        if (lst1 != null && lst1.size() > 0) {
                            ClientUtil.displayAlert("Customer is death marked please select another customerId");
                            return;
                        }
                    }
                    depAmount =depAmount+ CommonUtil.convertObjToDouble(tblDepositDetails.getValueAt(l, 3)).doubleValue();
                }
            }
            for (int m = 0; m < tblOtherSocietyDetails.getRowCount(); m++) {
                losAmount = losAmount + CommonUtil.convertObjToDouble(tblOtherSocietyDetails.getValueAt(m, 4)).doubleValue();
            }
            
            //added by sreekrishnan for dianamicaly calculate the balance security amount 
//            if(tblCollateral.getRowCount()>0){
//                pledgeAmount = observable.getCollateralSecurityAmount();        
//            }
            
            System.out.println("pledgeAmount=="+pledgeAmount+"valuOfGold=="+valuOfGold+"netWorth=="+netWorth+"memNetWorth=="+memNetWorth+"depAmount=="+depAmount+"losAmount=="+losAmount);    
            double tot = valuOfGold + netWorth + memNetWorth + pledgeAmount + depAmount + losAmount;
            System.out.println("tot=="+tot);
           // double sanctionAmt = CommonUtil.convertObjToDouble(txtLimit_SD.getText()).doubleValue();
            if (tot < whichEverIsLessPrzOrInst && !cbxDefaulter.isSelected() && !cbxOnlyApplication.isSelected()&& !thalayal && observable.getUserDefinedAuction().equalsIgnoreCase("N")) { //Added UserDefinedAuction checking by nithya on 23-12-2019 for KD-1074
                ClientUtil.displayAlert("Security Amount is lesser than the Sanctioned amount..");
                return;
            }
          }
        
        ////
        
        
        
        
        
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
        } else {

            /*     if(!cbxSameChittal.isSelected())
                
             {
                    
             double securitySum=0.0;
             if(!bufferList.isEmpty())doAction(
             {
             for(int m=0;m<tblSalaryDetails. getRowCount();m++){
             securitySum=securitySum+CommonUtil.convertObjToDouble(((HashMap)bufferList.get(m)).get("NETWORTH")). doubleValue();
             }
             // securitySum=securitySum+CommonUtil.convertObjToDouble(txtNetWorth.getText());
             }
             if(tblMemberType.getRowCount()>0)
             {
                
             for(int j=0;j<tblMemberType.getRowCount();j++) {
             securitySum=securitySum+CommonUtil.convertObjToDouble(tblMemberType.getValueAt(j,4)).doubleValue();             
             }
             }
             if(!txtSecurityValue.getText().equals(""))
             {
             securitySum=securitySum+CommonUtil.convertObjToDouble(txtSecurityValue.getText()); 
             }
            
            
             if(!txtValueOfGold.getText().equals(""))
             {
             securitySum=securitySum+CommonUtil.convertObjToDouble(txtValueOfGold.getText()); 
             }
            
            
             if(tblDepositDetails.getRowCount()>0)
             {
                
             for(int j=0;j<tblDepositDetails.getRowCount();j++) {
             securitySum=securitySum+CommonUtil.convertObjToDouble(tblDepositDetails.getValueAt(j,3)).doubleValue();             
             }
             }
            
            
            
             if(tblOtherSocietyDetails.getRowCount()>0)
             {
                
             for(int j=0;j<tblOtherSocietyDetails.getRowCount();j++) {
             securitySum=securitySum+CommonUtil.convertObjToDouble(tblOtherSocietyDetails.getValueAt(j,4)).doubleValue();             
             }
             }
            
             
             if(txtPrizedAmount.getText().equals(""))
             {
             ClientUtil.showMessageWindow("The selected Chittal is not prized yet !!! ");
             btnCancelActionPerformed(null); 
             return;
             }
             
             //system.out.println("ccc"+cboCity.getSelectedIndex()+"ffff"+tdtRetirementDt.getDateValue()+"jjjj");
             //system.out.println("!txtSalaryCertificateNo.getText().equals || !txtEmployerName.getText().equals   "+( !txtSalaryCertificateNo.getText().equals("") || !txtEmployerName.getText().equals("") || !txtAddress.getText().equals("")  || !txtPinCode.getText().equals("") || !txtDesignation.getText().equals("") || !txtContactNo.getText().equals("") || !txtMemberNum.getText().equals("") || !txtTotalSalary.getText().equals("") || !txtNetWorth.getText().equals("") || !txtSalaryRemark.getText().equals("") || !tdtRetirementDt.getDateValue().equals("") || !tdtRetirementDt.getDateValue().equals("  ")  ));
             if( !txtSalaryCertificateNo.getText().equals("") || !txtEmployerName.getText().equals("") || !txtAddress.getText().equals("") ||  !txtPinCode.getText().equals("") || !txtDesignation.getText().equals("") || !txtContactNo.getText().equals("") || !txtMemberNum.getText().equals("") || !txtTotalSalary.getText().equals("") || !txtNetWorth.getText().equals("") || !txtSalaryRemark.getText().equals("") || cboCity.getSelectedIndex()>0 || !tdtRetirementDt.getDateValue().equals(""))
           
             {
             ClientUtil.showMessageWindow("Save the Salary Security Details to grid first!!! ");
             return;  
             }
             double prize= CommonUtil.convertObjToDouble(txtPrizedAmount.getText());
             if(securitySum<prize)
             {
             ClientUtil.showMessageWindow("Security must be greater than or equal to Prize amount !!! ");
             return;
             }
             }*/


            //akku
            //system.out.println("EntryCountttt" + entryCountNO);
            if (!lblTotalTransactionAmtVal.getText().equals("") && !lblTotalTransactionAmtVal.getText().equals("0.0")) {
                if (application_No.equals("N")) {
                    System.out.println("entryCountNO >>>>>>>>>>>>>///" + entryCountNO);
                    if (entryCountNO == 0) {
                        int transactionSize = 0;
                        System.out.println("transaction output111>>>>>>>>>>>>>>>>///" + transactionUI.getOutputTO());
                        if (transactionUI.getOutputTO().size() == 0 && CommonUtil.convertObjToDouble(lblTotalTransactionAmtVal.getText()).doubleValue() > 0) { //trans details
                            ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                            return;
                        } else {
                            if (CommonUtil.convertObjToDouble(lblTotalTransactionAmtVal.getText()).doubleValue() > 0) {
                                // amtBorrow=CommonUtil.convertObjToDouble(txtLoanAmt.getText()).doubleValue();
                                //     //system.out.println("txtAmtBorrowed.getText()0000000000====="+txtAmtBorrowed.getText());
                                //system.out.println("transaction output111aaa>>>>>>>>>>>>>>>>///" + transactionUI.getOutputTO());
                                transactionSize = (transactionUI.getOutputTO()).size();
                                //system.out.println("transaction size>>>>>>>>>>>>>>>>///" + transactionSize);
                                if (transactionSize != 1 && CommonUtil.convertObjToDouble(lblTotalTransactionAmtVal.getText()).doubleValue() > 0) {
                                    ClientUtil.showAlertWindow("Multiple Transactions are Not allowed, Make it one Transaction");
                                    return;
                                } else {
                                    //system.out.println("transaction output222>>>>>>>>>>>>>>>>///" + transactionUI.getOutputTO());
                                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                                }
                            } else if (transactionUI.getOutputTO().size() > 0) {
                                System.out.println("transaction output333>>>>>>>>>>>>>>>>///" + transactionUI.getOutputTO());
                                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                            }
                        }
                        //system.out.println("transactionSize.." + transactionSize);
                        if (transactionSize == 0 && CommonUtil.convertObjToDouble(lblTotalTransactionAmtVal.getText()).doubleValue() > 0) {
                            ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                            return;
                        } else if (transactionSize != 0) {
                            if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
                                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                                return;
                            }
                            if (transactionUI.getOutputTO().size() > 0) {
                                //system.out.println("transaction output444>>>>>>>>>>>>>>>>///" + transactionUI.getOutputTO());
                                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                                System.out.println("setAllowedTransactionDetailsTO%%%%%%"+observable.getAllowedTransactionDetailsTO());
                                savePerformed();
//                    transactionUI.setButtonEnableDisable(true);
//        transactionUI.cancelAction(false);
//        transactionUI.resetObjects();
//        transactionUI.setCallingApplicantName("");
//        transactionUI.setCallingAmount("");

                            }

                        }
                    } else {
                        //akku
                        // {
                        savePerformed();
                    }
                }
            } else {
                savePerformed();
            }
            //  }
            // savePerformed();
            btnCancel.setEnabled(true);
            lblProdDesc.setText("");
           
        }
    }//GEN-LAST:event_btnSaveActionPerformed


    private void chargeTransaction() {
        HashMap chargeMap ;   
        ArrayList charge = new ArrayList(); 
        for (int i = 0; i < table.getRowCount(); i++) {    
            if (CommonUtil.convertObjToStr(table.getValueAt(i, 0)).equals("true")) {
                chargeMap = new HashMap();                
                chargeMap.put("CHARGE_ID", CommonUtil.convertObjToStr(table.getValueAt(i, 1)));
                chargeMap.put("CHARGE_DESC", CommonUtil.convertObjToStr(table.getValueAt(i, 2)));
                chargeMap.put("CHARGE_AMOUNT", CommonUtil.convertObjToDouble(table.getValueAt(i, 3)));                
                charge.add(chargeMap);
                System.out.println("charge##########"+charge);
            }       
        }
        observable.setChargelst(charge);
    }
                      
        
    
    private void finalizeCharges() {
       HashMap chargeMap = new HashMap();
        System.out.println("chargelst##########"+chargelst);
        if (chargelst != null && chargelst.size() > 0) {
               for (int i = 0; i < chargelst.size(); i++) {
                String accHead = "";
                chargeMap = (HashMap) chargelst.get(i);
                accHead = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_ID"));
                //system.out.println("$#@@$ accHead>>>>>@@" + accHead);
                for (int j = 0; j < table.getRowCount(); j++) {
                    //system.out.println("$#@@$ accHead inside table " + table.getValueAt(j, 1));
                    if (CommonUtil.convertObjToStr(table.getValueAt(j, 1)).equals(accHead) && !((Boolean) table.getValueAt(j, 0)).booleanValue()) {
                        chargelst.remove(i--);
                    }
                }
            }
            observable.setChargelst(chargelst);            
        }
         /*
        List finalList = null;
        ArrayList rowList = new ArrayList();
        HashMap chargeMap = new HashMap();
        if (chargelst != null && chargelst.size() > 0) {
            for (int j = 0; j < table.getRowCount(); j++) {
                System.out.println("CommonUtil.convertObjToDouble(table.getValueAt(j, 2)).doubleValue()##########"+CommonUtil.convertObjToDouble(table.getValueAt(j, 2)).doubleValue());
                if (CommonUtil.convertObjToDouble(table.getValueAt(j, 2)).doubleValue()>0) {
                    chargeMap.put("CHARGE_DESC",CommonUtil.convertObjToStr(table.getValueAt(j,1)));
                    chargeMap.put("CHARGE_AMOUNT",CommonUtil.convertObjToStr(table.getValueAt(j,2)));
                    //rowList.add(CommonUtil.convertObjToStr(table.getValueAt(j,1)));
                    //rowList.add(CommonUtil.convertObjToStr(table.getValueAt(j,2)));
                    //System.out.println("finalList##########"+finalList);
                    chargelst = table.getv
                    System.out.println("chargeMap##########"+chargeMap);
                    jList1.setListData(new Vector(map.keySet()));
                    finalList.(chargeMap.keySet().toArray());
                    observable.setFinalChrageMap(chargeMap); 
                    //observable.setChargelst(rowList);
                    //System.out.println("setChargelst##########"+observable.getChargelst());
                }    
           }
        }*/

    }

    private void savePerformed() {
        if (txtBondNo.getText().length() > 0 && observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            if (tdtBondDt.getDateValue().length() <= 0) {
                ClientUtil.showMessageWindow("Bond Date Should be given along with Bond No !!! ");
                return;
            }
        }
        double depAmount = 0.0;
        double lienAmt = 0.0;
        double memNetWorth = 0.0;
        double losAmount = 0.0;
        double pledgeAmt = 0.0;
        HashMap hashmap = new HashMap();
        double valuOfGold = CommonUtil.convertObjToDouble(txtValueOfGold.getText()).doubleValue();
        //  
        double salary = 0.0;
        //=CommonUtil.convertObjToDouble(txtTotalSalary.getText()).doubleValue();
        for (int m = 0; m < tblSalaryDetails.getRowCount(); m++) {
            salary = salary + CommonUtil.convertObjToDouble(((HashMap) bufferList.get(m)).get("NETWORTH")).doubleValue();
        }
        double colleteral = CommonUtil.convertObjToDouble(txtSecurityValue.getText()).doubleValue();
        if (tblDepositDetails != null && tblDepositDetails.getRowCount() > 0) {
            double availbalBalDep = 0.0;
            for (int i = 0; i < tblDepositDetails.getRowCount(); i++) {
                String depNo = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(i, 1));
                String prodtype = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(i, 0));
                String chittalno = txtChittalNo.getText();
                HashMap hmap = new HashMap();
                hmap.put("DEPOSIT_NO", depNo);
                hmap.put("CHITTAL_NO", chittalno);
                List list = ClientUtil.executeQuery("getLienAmountForChecking", hmap);
                if (list != null && list.size() > 0) {
                    HashMap hash = (HashMap) list.get(0);
                    lienAmt = lienAmt + CommonUtil.convertObjToDouble(hash.get("LIEN_AMOUNT")).doubleValue();
                }
                if (prodtype.equals("TD") || prodtype.equals("Deposits")) {
                    List lst = ClientUtil.executeQuery("getAvailableBalForDep", hmap);
                    if (lst != null && lst.size() > 0) {
                        hmap = (HashMap) lst.get(0);
                        depAmount = depAmount + CommonUtil.convertObjToDouble(hmap.get("AVAILABLE_BALANCE")).doubleValue();
                        hmap.put("MEMBER_NO", hmap.get("CUST_ID"));
                         hmap.put("CUST_ID", hmap.get("CUST_ID"));
                        List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hmap);
                        if (lst1 != null && lst1.size() > 0) {
                            ClientUtil.displayAlert("Customer is death marked please select another customerId");
                            return;
                        }
                    }
                } else {
                    String mdsNo = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(i, 1));
                    hashmap.put("CHITTAL_NO", mdsNo);

                    List lst1 = ClientUtil.executeQuery("getCustIdDeathChecking", hashmap);
                    if (lst1 != null && lst1.size() > 0) {
                        hashmap = (HashMap) lst1.get(0);
                        hashmap.put("MEMBER_NO", hashmap.get("CUST_ID"));
                        hashmap.put("CUST_ID", hashmap.get("CUST_ID"));
                        lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
                        if (lst1 != null && lst1.size() > 0) {
                            ClientUtil.displayAlert("Customer is death marked please select another customerId");
                            return;
                        }
                    }
                    depAmount =+ CommonUtil.convertObjToDouble(tblDepositDetails.getValueAt(i, 3)).doubleValue();
                    System.out.println("depamt"+depAmount);

                }
            }

        }
        for (int j = 0; j < tblMemberType.getRowCount(); j++) {
            memNetWorth = memNetWorth + CommonUtil.convertObjToDouble(tblMemberType.getValueAt(j, 4)).doubleValue();
            String custid = CommonUtil.convertObjToStr(tblMemberType.getValueAt(j, 0));
            hashmap.put("CUST_ID", custid);
            hashmap.put("MEMBER_NO", custid);
            List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
            if (lst1 != null && lst1.size() > 0) {
                ClientUtil.displayAlert("Customer is death marked please select another customerId");
                return;
            }
        }
        for (int m = 0; m < tblOtherSocietyDetails.getRowCount(); m++) {
            losAmount = losAmount + CommonUtil.convertObjToDouble(tblOtherSocietyDetails.getValueAt(m, 4)).doubleValue();
        }
        for (int n = 0; n < tblCollateral.getRowCount(); n++) {
            pledgeAmt = pledgeAmt + CommonUtil.convertObjToDouble(tblCollateral.getValueAt(n, 3)).doubleValue();
            String custid = CommonUtil.convertObjToStr(tblCollateral.getValueAt(n, 0));
            hashmap.put("CUST_ID", custid);
            hashmap.put("MEMBER_NO", custid);
            List lst1 = ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
            if (lst1 != null && lst1.size() > 0) {
                ClientUtil.displayAlert("Customer is death marked please select another customerId");
                return;
            }
        }
        double tot = valuOfGold + salary + colleteral + depAmount + memNetWorth + losAmount + pledgeAmt + lienAmt;
            System.out.println("totalin "+tot);

        String mdsNo = txtChittalNo.getText();
        HashMap mdsMap = new HashMap();
        String chittalNo = txtChittalNo.getText();
        //String subNo = txtSubNo.getText();    //AJITH Blocked on 13/08/2021
        Integer subNo = CommonUtil.convertObjToInt(txtSubNo.getText()); //AJITH New Line on 13/08/2021
//        if (mdsNo.indexOf("_")!=-1) {
//            chittalNo = mdsNo.substring(0,mdsNo.indexOf("_"));
//            subNo = mdsNo.substring(mdsNo.indexOf("_")+1, mdsNo.length());
//            
//        }

        mdsMap.put("CHITTAL_NO", chittalNo);
        mdsMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
        double noinset = 0.0;
        double instAmt = 0.0;
        double paidInst = 0.0;
        double totAmt = 0.0;
        List mdsList = ClientUtil.executeQuery("getTotalInstAmount", mdsMap);
        List mdsList1 = ClientUtil.executeQuery("getSelctApplnReceiptDetails", mdsMap);
        if (mdsList1 != null && mdsList1.size() > 0) {
            mdsMap = (HashMap) mdsList1.get(0);
            noinset = CommonUtil.convertObjToDouble(mdsMap.get("NO_OF_INSTALLMENTS")).doubleValue();
            instAmt = CommonUtil.convertObjToDouble(mdsMap.get("INST_AMT")).doubleValue();
            totAmt = instAmt * noinset;
        }
        if (mdsList != null && mdsList.size() > 0) {
            mdsMap = (HashMap) mdsList.get(0);
            paidInst = CommonUtil.convertObjToDouble(mdsMap.get("NO_INST_PAID")).doubleValue();
            paidInst = paidInst * instAmt;
        }
        double tobepaidInst = totAmt - paidInst;
        //comm for the mantis id 9258 for telk -Same chittal as lien
       // if (!cbxSameChittal.isSelected()) {
            HashMap map = new HashMap();
            map.put("PROD_ID", observable.getTxtProductId());
            String Against_prized = "N";
            List securityAgainstlist = ClientUtil.executeQuery("getSecurityAgainstFactor", map);
            if (securityAgainstlist != null && securityAgainstlist.size() > 0) {
                if (((HashMap) securityAgainstlist.get(0)).get("SECURITY_PRIZED") != null && !((HashMap) securityAgainstlist.get(0)).get("SECURITY_PRIZED").toString().equals("null") && ((HashMap) securityAgainstlist.get(0)).get("SECURITY_PRIZED").toString().equals("Y")) {
                    Against_prized = "Y";
                } else {
                    Against_prized = "N";
                }

            } else {
                Against_prized = "N";
            }
            if(prizdPend.equals("Y") ||  secPend.equals("Y")){
            if (!cbxDefaulter.isSelected() && !cbxOnlyApplication.isSelected()) {

                if (Against_prized.equals("N")) {
                    //system.out.println("tot..." + tot + "..tobepaidInst..." + tobepaidInst);
                    if (tot < tobepaidInst && observable.getUserDefinedAuction().equalsIgnoreCase("N")) { //Added UserDefinedAuction checking by nithya on 23-12-2019 for KD-1074
                        ClientUtil.displayAlert("Security Amount is lesser than the Amount to be paid..!!");
                        return;
                    }
                } else {
                    if (txtPrizedAmount.getText().equals("")) {
                        ClientUtil.showMessageWindow("The selected Chittal is not prized yet !!! ");
                        btnCancelActionPerformed(null);
                        return;
                    }

                    double prize = CommonUtil.convertObjToDouble(txtPrizedAmount.getText());
                    if (tot < prize && observable.getUserDefinedAuction().equalsIgnoreCase("N")) { //Added UserDefinedAuction checking by nithya on 23-12-2019 for KD-1074
                        ClientUtil.showMessageWindow("Security must be greater than or equal to Prize amount !!! ");
                        return;
                    }
                }
            }
            }


            transactionUI.setButtonEnableDisable(true);
            transactionUI.cancelAction(false);
            transactionUI.resetObjects();
            transactionUI.setCallingApplicantName("");
            transactionUI.setCallingAmount("");
            //finalizeCharges();   
            if(tableFlag){
                chargeTransaction();    
            }            
            if (transNew) {
                //system.out.println("cjkjhjghgg>>>");
                insertTransactionPart();

            }
            //system.out.println("deleteeee");
        //added by rishad for avoiding doubling issue at 05/08/2015
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws InterruptedException /** Execute some operation */
            {
                try {
                    observable.doAction(nomineeUi.getNomineeOB());
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
            bufferList.clear();
            getTableData();
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                //system.out.println("observable.getProxyReturnMap()>>>>>>1234" + observable.getProxyReturnMap());
                //trans details
                if (observable.getProxyReturnMap().get("TRANSFER_TRANS_LIST") != null || observable.getProxyReturnMap().get("CASH_TRANS_LIST") != null) {
                    //system.out.println("bbbkjhbjb");
                    displayTransDetail(observable.getProxyReturnMap());//trans details
                }
                lst.add("SCHEME_NAME");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getProxyReturnMap() != null) {
                    if (observable.getProxyReturnMap().containsKey("SCHEME_NAME")) {
                        lockMap.put("SCHEME_NAME", observable.getProxyReturnMap().get("SCHEME_NAME"));
                    }
                }
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    super.removeEditLock(observable.getTxtSchemeName());
                }
                setEditLockMap(lockMap);
                setEditLock();
                btnCancelActionPerformed(null);
                btnCancel.setEnabled(true);
                lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
            }
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
            btnException.setEnabled(true);
      //  }
    }

    //trans details
    private void displayTransDetail(HashMap proxyResultMap) {
        //system.out.println("@#$@@$@@@$ proxyResultMap : " + proxyResultMap);
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transType = "";
        //system.out.println("jhj>>>>>>>");
        Object keys[] = proxyResultMap.keySet().toArray();
        //system.out.println("jhj>>>>>>>adad");
        ////system.out.println("keeeeeeeeeeeyyy>>>>>>>>>>>"+keys[]);
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        HashMap transIdMap = new HashMap();
        HashMap transTypeMap = new HashMap();
        //system.out.println("keeeeeeeeeeeyyy>>>>>>>>>>>" + keys.length);
        for (int i = 0; i < keys.length; i++) {
            //system.out.println("jhj>>>>>>>adad1211222@@@@" + (proxyResultMap.get(keys[i]) instanceof String));
            if (proxyResultMap.get(keys[i]) instanceof String) {
                //system.out.println("hdfdasd");
                continue;
            }

            //system.out.println("hdfdasd@@@@@");
            tempList = (List) proxyResultMap.get(keys[i]);
            //system.out.println("hdfdasd@@@@@>>>>>" + tempList);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
                //system.out.println("haaaiii11....>>>");
                for (int j = 0; j < tempList.size(); j++) {
                    //system.out.println("haaaiii11....>>>aa");
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        //system.out.println("haaaiii11....>>>bb");
                        transId = (String) transMap.get("TRANS_ID");
                    }
                    cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        //system.out.println("haaaiii11....>>>cc");
                        cashDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                        //system.out.println("haaaiii11....>>>dd");
                        cashDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                    transTypeMap.put(transMap.get("TRANS_ID"), transMap.get("TRANS_TYPE"));
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "CASH");
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                //system.out.println("haaaiii22....>>>");
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        //system.out.println("haaaiii22....>>>aa");
                        transId = (String) transMap.get("BATCH_ID");
                    }
                    transferDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Batch Id : " + transMap.get("BATCH_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        //system.out.println("haaaiii22....>>>bb");
                        transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                        //system.out.println("haaaiii22....>>>cc");
                        transferDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
                }
                transferCount++;
            }
        }
        if (cashCount > 0) {
            displayStr += cashDisplayStr;
        }
        if (transferCount > 0) {
            displayStr += transferDisplayStr;
        }
        ClientUtil.showMessageWindow("" + displayStr);

        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        //system.out.println("#$#$$ yesNo : " + yesNo);
        if (yesNo == 0) {
            //            TTIntegration ttIntgration = null;
            //            HashMap paramMap = new HashMap();
            //            paramMap.put("TransId", transId);
            //            paramMap.put("TransDt", observable.getCurrDt());
            //            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            //            ttIntgration.setParam(paramMap);
            //            ttIntgration.integrationForPrint("ReceiptPayment", false);

            TTIntegration ttIntgration = null;
            HashMap printParamMap = new HashMap();
            printParamMap.put("TransDt", observable.getCurrDt());
            printParamMap.put("BranchId", ProxyParameters.BRANCH_ID);
            Object keys1[] = transIdMap.keySet().toArray();
            for (int i = 0; i < keys1.length; i++) {
                printParamMap.put("TransId", keys1[i]);
                ttIntgration.setParam(printParamMap);
                if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER")) {
                    ttIntgration.integrationForPrint("ReceiptPayment");
                } else if (CommonUtil.convertObjToStr(transTypeMap.get(keys1[i])).equals("DEBIT")) {
                    ttIntgration.integrationForPrint("CashPayment", false);
                } else {
                    ttIntgration.integrationForPrint("CashReceipt", false);
                }
            }
        }
    }

    private void insertTransactionPart() {
        //system.out.println("fgdfgsg>>>@@jhgvhjv===");
        HashMap singleAuthorizeMap = new HashMap();
        java.util.ArrayList arrList = new java.util.ArrayList();
        HashMap authDataMap = new HashMap();
        //  authDataMap.put("ACCT_NUM", observable.getTxtApplNo());
        arrList.add(authDataMap);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            /*1       String [] debitType = {"Cash","Transfer"};
             int option3 = 0 ;
             if(option3 == 0){
             String transType = "";
             //system.out.println("!!! transType :"+transType);
             while (CommonUtil.convertObjToStr(transType).length()==0) {
             //system.out.println("cvvbfgrfr>>>"+authDataMap);
             transType = (String)COptionPane.showInputDialog(null,"Select Transaction Type", "Transaction type", COptionPane.QUESTION_MESSAGE, null, debitType, "");
             if (CommonUtil.convertObjToStr(transType).length()>0) {
             //system.out.println("hjkggcssasa>>>"+authDataMap);
             authDataMap.put("TRANSACTION_PART","TRANSACTION_PART");
             authDataMap.put("TRANS_TYPE",transType.toUpperCase());
             authDataMap.put("LIMIT",txtLoanAmt.getText());
             authDataMap.put("USER_ID",TrueTransactMain.USER_ID);
             // authDataMap.put("STATUS",lblStatus.getText());
             //system.out.println("hjkggcssasa>>>2222aasvfd"+authDataMap);
             1*/
            /*  if(CommonUtil.convertObjToStr(transType.toUpperCase()).equals("CASH")){
             boolean flag = true;
             do {
             String tokenNo = COptionPane.showInputDialog(this,resourceBundle.getString("REMARK_CASH_TRANS"));
             if (tokenNo != null && tokenNo.length()>0) {
             flag = tokenValidation(tokenNo);
             }else{
             flag = true;
             }
             if(flag == false){
             ClientUtil.showAlertWindow("Token is invalid or not issued for you. Please verify.");
             }else{
             authDataMap.put("TOKEN_NO",tokenNo);
             }
             } while (!flag);
             }
             */
            /*2   if(CommonUtil.convertObjToStr(transType.toUpperCase()).equals("TRANSFER")){
             boolean flag = true;
             do {
             String sbAcNo = firstEnteredActNo();
             if(sbAcNo!=null && sbAcNo.length()>0){
             flag = checkingActNo(sbAcNo);
             if(flag == false && finalChecking == false){
             ClientUtil.showAlertWindow("Account No is invalid, Please enter correct no");
             }else{
             //system.out.println("sbAcNo>>>>>>>>>>>"+sbAcNo);
             authDataMap.put("CR_ACT_NUM",sbAcNo);
             }
             finalChecking = false;
             } else {
             ClientUtil.showMessageWindow("Transaction Not Created");
             flag = true;
             authDataMap.remove("TRANSACTION_PART");
             }
             } while (!flag);
             }
             //system.out.println("transop>>>>>>"+transactionUI.getOutputTO());
             //system.out.println("authmap>>>@@@2542"+authDataMap);
             2*/
            //system.out.println("transactionUI.getOutputTO()>>>>!!1111" + transactionUI.getOutputTO());
            observable.setNewTransactionMap(transactionUI.getOutputTO());
        }//else{
        // transType = "Cancel";
        //}
    }

    /**
     * To display a popUp window for viewing existing data
     */
    private void popUp(String currAction) {
        viewType = currAction;
        HashMap viewMap = new HashMap();
        HashMap where = new HashMap();
        if (currAction.equalsIgnoreCase("Edit") || currAction.equalsIgnoreCase("Delete")) {
            if (currAction.equalsIgnoreCase("Delete")) {
                ArrayList lst = new ArrayList();
                lst.add("SCHEME_NAME");
                viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
                lst = null;
                viewMap.put(CommonConstants.MAP_NAME, "getMDSMasterMaintenanceDelete");
            } else if (currAction.equalsIgnoreCase("Edit")) {
                ArrayList lst = new ArrayList();
                lst.add("SCHEME_NAME");
                viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
                lst = null;
                viewMap.put(CommonConstants.MAP_NAME, "getMDSMasterMaintenanceEdit");
            }
        } else if (currAction.equalsIgnoreCase("Enquiry")) {
            ArrayList lst = new ArrayList();
            lst.add("SCHEME_NAME");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "getMDSMasterMaintenanceView");
        } else if (currAction.equalsIgnoreCase("SCHEME_DETAILS")) {

            viewMap.put(CommonConstants.MAP_NAME, "getSelectEachSchemeDetails");
        } else if (currAction.equalsIgnoreCase("CHITTAL_NO")) {
            where.put("SCHEME_NAMES", txtSchemeName.getText());
            viewMap.put(CommonConstants.MAP_WHERE, where);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectChitNoNotinMasterDetails");
        } else if (currAction.equalsIgnoreCase("MEMBER_NO")) {
            viewMap.put(CommonConstants.MAP_NAME, "getMemberDetailsFromMDSApplication");
        } else if (currAction.equalsIgnoreCase("SUB_MEMBER_NO")) {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectCustomerMemberList");
        } else if (currAction == "CREDIT_ACC_NO") {
            viewMap = new HashMap();
            String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
            if (!prodType.equals("GL")) {
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                        + ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString());
            } else {
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
            }
            HashMap whereMap = new HashMap();
            if (cboProdId.getModel() != null && cboProdId.getModel().getSize() > 0) {
                whereMap.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
            }
            if (whereMap.get("SELECTED_BRANCH") == null) {
                whereMap.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
            } else {
                whereMap.put("SELECTED_BRANCH", getSelectedBranchID());
            }
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        } else if (currAction.equalsIgnoreCase("DEPOSIT_ACC_NO")) {
            HashMap whereMap = new HashMap();
            whereMap.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
          //  whereMap.put("CHITTAL_NO",txtChittalNo.getText());
            whereMap.put("PROD_ID", CommonUtil.convertObjToStr(observable.getCbmDepProdID().getKeyForSelected()));
            String prodType = ((ComboBoxModel) cboProductTypeSecurity.getModel()).getKeyForSelected().toString();
            System.out.println("uidtgaefsifygaeiygfgeiagtfy"+whereMap);
            if (prodType.equals("TD")){
                if(cbxSameChittal.isSelected()){
                 viewMap.put(CommonConstants.MAP_NAME, "getMDSMasterDepositNo_Lien"); 
                }
                else
                viewMap.put(CommonConstants.MAP_NAME, "getMDSMasterMaintenanceDepositNo");
            } else {
                viewMap.put(CommonConstants.MAP_NAME, "getMDSChittalNo");
            }
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        } else if (currAction.equalsIgnoreCase("OWNER_MEMBER_NO")) {
            viewMap.put(CommonConstants.MAP_NAME, "getMemeberShipDetailsFromGahan");
        } else if (currAction.equalsIgnoreCase("DOCUMENT_NO")) {
            HashMap whereListMap = new HashMap();
            whereListMap.put("ENTERED_DOCUMENT_NO", verifyDocNo().toString());
            //Added By Suresh
            if (txtOwnerMemNo.getText().length() > 0) {
                whereListMap.put("MEMBERSHIP_NUMBER", txtOwnerMemNo.getText());
            }
            viewMap.put(CommonConstants.MAP_NAME, "getGahanDetailsforLoan");
            viewMap.put(CommonConstants.MAP_WHERE, whereListMap);
        }
         else if(currAction.equalsIgnoreCase("RESOLUTION_NO"))
        {
              viewMap.put(CommonConstants.MAP_NAME, "getBoardResolutionAuth"); 
        }else if (currAction.equalsIgnoreCase("GOLD_SECURITY_STOCK")) {
             viewMap.put(CommonConstants.MAP_NAME, "getCustomerGoldSecurityStock");
        }

        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        new ViewAll(this, viewMap).show();
    }

    private StringBuffer verifyDocNo() {
        StringBuffer addExistDoc = new StringBuffer();
        int count = tblCollateral.getRowCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                if (i == 0) {
                    addExistDoc.append("'" + CommonUtil.convertObjToStr(tblCollateral.getValueAt(i, 6)) + "'");
                } else {
                    addExistDoc.append("," + "'" + CommonUtil.convertObjToStr(tblCollateral.getValueAt(i, 6)) + "'");
                }
            }
        }
        return addExistDoc;
    }

    public void fillData(Object obj) {
        try {
            HashMap hashMap = (HashMap) obj;
            //System.out.println("### fillData Hash : " + hashMap);
            isFilled = true;
            if (hashMap.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
                fromNewAuthorizeUI = true;
                newauthorizeListUI = (NewAuthorizeListUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(false);
                rejectFlag = 1;
            }
            if (hashMap.containsKey("FROM_AUTHORIZE_LIST_UI")) {
                fromAuthorizeUI = true;
                authorizeListUI = (AuthorizeListUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(false);
                rejectFlag = 1;
            }
            if (viewType == "SCHEME_DETAILS") {
                txtSchemeName.setText(CommonUtil.convertObjToStr(hashMap.get("SCHEME_NAME")));
                lblProdDesc.setText(CommonUtil.convertObjToStr(hashMap.get("SCHEME_DESC")));
                observable.setTxtSchemeName(CommonUtil.convertObjToStr(hashMap.get("SCHEME_NAME")));
                prodDesc = CommonUtil.convertObjToStr(hashMap.get("PROD_DESC"));
                observable.setTxtProductId(CommonUtil.convertObjToStr(hashMap.get("PROD_ID")));
                //system.out.println("prodDesc::" + prodDesc);
                resetUIFields();
                //Added BY Suresh 
                if (txtSchemeName.getText().length() > 0) {
                    txtChittalNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID + txtSchemeName.getText()));
                } else {
                    txtChittalNo.setText("");
                }
                observable.setTxtChittalNo(txtChittalNo.getText());
            } 
            
             else if(viewType=="RESOLUTION_NO")
            {
          
             String resolutionno="";
          //  HashMap hash = (HashMap) map;
            resolutionno=CommonUtil.convertObjToStr(hashMap.get("RESOLUTION_ID"));
            txtResolutionNo.setText(resolutionno);
                  tdtResolutionDt.setDateValue((CommonUtil.convertObjToStr(hashMap.get("RESOLUTION_DATE"))));
            
            }

            else if (viewType == "CHITTAL_NO") {
                resetUIFields();
                //Added by sreekrishnan
                if(hashMap.containsKey("THALAYAL") && hashMap.get("THALAYAL")!=null && !hashMap.get("THALAYAL").equals("")
                        && hashMap.get("THALAYAL").equals("Y")){
                    thalayal = true;
                }else{
                    thalayal = false;
                }
                txtSchemeName.setText(CommonUtil.convertObjToStr(hashMap.get("SCHEME_NAME")));
                txtChittalNo.setText(CommonUtil.convertObjToStr(hashMap.get("CHITTAL_NO")));
                lblMembershipName.setText(CommonUtil.convertObjToStr(hashMap.get("MEMBER_NAME")));
                observable.setTxtChittalNo(CommonUtil.convertObjToStr(hashMap.get("CHITTAL_NO")));
                txtDivisionNo.setText(CommonUtil.convertObjToStr(hashMap.get("DIVISION_NO")));
                observable.setTxtDivisionNo(CommonUtil.convertObjToInt(hashMap.get("DIVISION_NO")));    //AJITH
                tdtChitStartDt.setDateValue(CommonUtil.convertObjToStr(hashMap.get("CHIT_START_DT")));
                txtRemarks.setText(CommonUtil.convertObjToStr(hashMap.get("REMARKS")));
                observable.setTdtChitStartDt(CommonUtil.convertObjToStr(hashMap.get("CHIT_START_DT")));
                observable.setTxtRemarks(CommonUtil.convertObjToStr(hashMap.get("REMARKS")));
                observable.setTxtSchemeName(CommonUtil.convertObjToStr(hashMap.get("SCHEME_NAME")));
                observable.setTxtSubNo(CommonUtil.convertObjToInt(hashMap.get("SUB_NO")));  //AJITH
                txtSubNo.setText(CommonUtil.convertObjToStr(hashMap.get("SUB_NO")));
                // txtBondNo.setText(CommonUtil.convertObjToStr(hashMap.get("SUB_NO")));
                observable.setPaymentDetails();
                observable.setReceiptDetails(hashMap);
                txtMemberNo.setText(observable.getTxtMemberNo());
               // lblMembershipName.setText(observable.getLblMemberName());
                lblMembershipType.setText(observable.getLblMemberType());
                lblNomineName.setText(observable.getLblNomineeName());
                btnMemberNo.setEnabled(true);
                txtPrizedAmount.setText(CommonUtil.convertObjToStr(observable.getTxtPrizedAmount()));  //AJITH
                tdtPayDt.setDateValue(observable.getTdtPayDt());
                txtLastInstNo.setText(observable.getTxtLastInstNo());
                tdtLastInstDate.setDateValue(observable.getTdtLastInstDate());
                txtTotalAmountTillDate.setText(CommonUtil.convertObjToStr(observable.getTxtTotalAmountTillDate()));  //AJITH
                txtOverDueInstallments.setText(CommonUtil.convertObjToStr(observable.getTxtOverDueInstallments()));  //AJITH
                txtOverDueAmount.setText(CommonUtil.convertObjToStr(observable.getTxtOverDueAmount()));  //AJITH
                observable.setTxtSchemeName(CommonUtil.convertObjToStr(hashMap.get("SCHEME_NAME")));
                observable.getData(hashMap, nomineeUi.getNomineeOB());
                bufferList.clear();
                bufferList = observable.getSalarySecDetails();
                txtContactNo.setText("");
                getTableData();
                tblMemberType.setModel(observable.getTblMemberTypeDetails());
                tblDepositDetails.setModel(observable.getTblDepositTypeDetails());
                tblOtherSocietyDetails.setModel(observable.getTblOtherSocietyDetails());
                ClientUtil.enableDisable(panMasterMaintenanceDetails, true);
                HashMap whereMap = new HashMap();
                List countList = ClientUtil.executeQuery("getCountDetailsForCoChittal", hashMap);
                if (countList != null && countList.size() > 0) {
                    whereMap = (HashMap) countList.get(0);
                    int count = CommonUtil.convertObjToInt(whereMap.get("COUNT"));
                    if (count > 1) {
                        lblCoChit.setText("Co-Chital");
                    } else {
                        lblCoChit.setText("");
                    }
                }
//                
//              List docDetails=ClientUtil.executeQuery("", map);
//            bufferList.clear();
//            for(int i=0;i<docDetails.size();i++)
//            {HashMap docMap=new HashMap();
//              HashMap temp=new HashMap();
//             docMap=(HashMap)docDetails.get(i);
//              temp.put("DOC_NO",docMap.get("DOCUMENT_NO").toString());
//              temp.put("DOC_TYPE", docMap.get("DOCUMENT_TYPE").toString());
//              temp.put("REG_OFFIC", docMap.get("REGISTRED_OFFICE").toString());
//              temp.put("SNO", docMap.get("SL_NO").toString());
//              temp.put("DOC_DATE", CommonUtil.convertObjToStr(docMap.get("DOCUMENT_DT")));
//             bufferList.add(temp); 
//                      
//            }
//            getTableData();
//                



                populateMasterMaintenanceFields();
                if (!cbxDefaulter.isSelected() == true) {
                    System.out.println("dfgdfgdfgfdgdfg");
                    HashMap bondMap = new HashMap();
                    int LastBonNo = 0;
                    bondMap.put("CHITTAL_NO", txtChittalNo.getText());
                    bondMap.put("SCHEME", txtSchemeName.getText());
                    List bonsetList = ClientUtil.executeQuery("getBondsetValue", bondMap);
                    List LastBondNO = ClientUtil.executeQuery("getLastBondValue", bondMap);
                    bond = "N";
                    if (bonsetList != null && bonsetList.size() > 0) {
                        System.out.println("bonsetList==="+bonsetList);
                        bondNoset = CommonUtil.convertObjToStr(((HashMap) bonsetList.get(0)).get("BONDSET"));
                        bond = bondNoset;
                        if (bondNoset.equals("N")) {
                            System.out.println("bondNosetbondNoset"+bondNoset);
                            LastBonNo = CommonUtil.convertObjToInt(((HashMap) LastBondNO.get(0)).get("LAST_BOND_NO").toString());
                            LastBonNo = LastBonNo + 1;
                            observable.setTxtBordNo(CommonUtil.convertObjToStr(LastBonNo));
                            txtBondNo.setText(CommonUtil.convertObjToStr(LastBonNo));
                           System.out.println("BB11 -- "+txtBondNo.getText());     
//                         observable.updateMDSSchemeBond(LastBondNo);
                            bondNoset = "Y";
                        } else {
                            System.out.println("else bondNoset"+bondNoset);
                            bondNoset = "Y";
                        }

                    } else {
                        LastBonNo = CommonUtil.convertObjToInt(((HashMap) LastBondNO.get(0)).get("LAST_BOND_NO").toString());
                        LastBonNo = LastBonNo + 1;
                        txtBondNo.setText(CommonUtil.convertObjToStr(LastBonNo));
                         System.out.println("BB22 -- "+txtBondNo.getText()); 
//                         observable.updateMDSSchemeBond(LastBondNo);
                        bondNoset = "Y";
                    }
                    // FOR Application No same as bond no
                    HashMap applicationmap = new HashMap();
                    int LastApplNo = 0;
                    applicationmap.put("CHITTAL_NO", txtChittalNo.getText());
                    applicationmap.put("SCHEME", txtSchemeName.getText());
                    List applicationList = ClientUtil.executeQuery("getApplicationValue", applicationmap);
                    System.out.println("applicationList"+applicationList);
                    List LastApplicationNo = ClientUtil.executeQuery("getLastApplicationValue", applicationmap);
                    System.out.println("LastApplicationNo"+LastApplicationNo);
                    application_No = "N";
                    applicationNoset = "N";
                    if (applicationList.size() > 0) {
                        applicationNoset = CommonUtil.convertObjToStr(((HashMap) applicationList.get(0)).get("APPLICATIONSET"));
                        application_No = applicationNoset;
                        if (applicationNoset.equals("N")) {
                            System.out.println("applicationNoset fghgfh"+applicationNoset);
                            LastApplNo = CommonUtil.convertObjToInt(((HashMap) LastApplicationNo.get(0)).get("LAST_APPLICATION_NO").toString());
                            System.out.println("LastApplNo===="+LastApplNo);
                            LastApplNo = LastApplNo + 1;
                            txtApplicationNo.setText("" + LastApplNo);
                            txaApplicationSecRemarks.setText("");
//                         observable.updateMDSSchemeBond(LastBondNo);
                            applicationNoset = "Y";
                        } else {
                            System.out.println("else applicationNoset dsgsdg"+applicationNoset);
                            applicationNoset = "Y";
                        }

                    } else {
                        LastApplNo = CommonUtil.convertObjToInt(((HashMap) LastBondNO.get(0)).get("LAST_APPLICATION_NO").toString());
                        System.out.println("LastApplNofghgfhgfh"+LastApplNo);
                        LastApplNo = LastBonNo + 1;
                        System.out.println("else LastApplNo===="+LastApplNo);
                        txtApplicationNo.setText("" + LastApplNo);
//                         observable.updateMDSSchemeBond(LastBondNo);
                        applicationNoset = "Y";
                        txaApplicationSecRemarks.setText("");
                    }
                }
                //// endss
                calculateTot();
                ClientUtil.enableDisable(panInstallmentDetails, false);
                txtRemarks.setEnabled(false);
                btnCustomerIdFileOpenCr.setEnabled(true);
                tdtBondDt.setEnabled(false);
                if (application_No.equals("N")) {
                    HashMap aMap = new HashMap();
                    aMap.put("SCHEME_ID", prodDesc);
                    aMap.put("DEDUCTION_ACCU", "R");
                    List list = ClientUtil.executeQuery("getChargeDetailsData", aMap);
                    if (list != null && list.size() > 0) {
                        //HashMap maplist=(HashMap)list.get(0);
                        double chrg = 0.0;
                        System.out.println("maplist.size()maplist.size()"+list.size());
                        for(int i=0;i<list.size();i++)
                        {
                            HashMap maplist=(HashMap)list.get(i);
                            System.out.println("maplistmaplist"+maplist);
                            System.out.println("maplistmaplist"+maplist.get("AMOUNT"));
                            chrg = chrg + CommonUtil.convertObjToDouble(maplist.get("AMOUNT"));
                        }
                        System.out.println("chrgchrgchrgchrgchrg"+chrg);
                        if(chrg>0)
                        {                            
                        chrgTableEnableDisable();
                        createChargeTable(prodDesc);
                        //  txtApplicationNo.setText(""+txtBondNo.getText());
                        tdtApplicationDate.setDateValue(CommonUtil.convertObjToStr(currDate));
                        observable.setApplicationNo(txtApplicationNo.getText());
                        chargeAmount();                       
                        transNew = true;
                        }
                    } else {
                        transNew = false;
                        System.out.println("entryCountN#O"+entryCountNO);
                        entryCountNO = 1;
                        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
                        System.out.println("edit charge table");
                        editChargeTable();
                        //   txtApplicationNo.setText(""+txtBondNo.getText());
                        if (tableFlag) {
                            //editChargeAmount();
                            panChargeDetails.setEnabled(false);
                            srpChargeDetails.setEnabled(false);
                            table.remove(0);
                            table.setEnabled(false);
                        }
                        //system.out.println("toooodssa");
                    }
                } else {
                    transNew = false;
                    entryCountNO = 1;
                    transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
                    editChargeTable();
                    //  txtApplicationNo.setText(""+txtBondNo.getText());
                    if (tableFlag) {
                        //editChargeAmount();
                        panChargeDetails.setEnabled(false);
                        srpChargeDetails.setEnabled(false);
                        table.remove(0);
                        table.setEnabled(false);
                    }
                    //system.out.println("toooo");
                }
            } else if (viewType == "MEMBER_NO") {
                txtChittalNo.setText(CommonUtil.convertObjToStr(hashMap.get("CHITTAL_NO")));
                observable.setTxtChittalNo(CommonUtil.convertObjToStr(hashMap.get("CHITTAL_NO")));
                observable.setPaymentDetails();
                observable.setReceiptDetails(hashMap);
                txtMemberNo.setText(CommonUtil.convertObjToStr(hashMap.get("MEMBERSHIP_NO")));
                lblMembershipName.setText(CommonUtil.convertObjToStr(hashMap.get("CUSTOMER")));
                lblMembershipType.setText(CommonUtil.convertObjToStr(hashMap.get("MEMBERSHIP_CLASS")));
                txtSchemeName.setText(CommonUtil.convertObjToStr(observable.getTxtSchemeName()));
                //                txtChittalNo.setText(CommonUtil.convertObjToStr(hashMap.get("CHITTAL_NO")));
                //                observable.setTxtChittalNo(CommonUtil.convertObjToStr(hashMap.get("CHITTAL_NO")));
                txtDivisionNo.setText(CommonUtil.convertObjToStr(observable.getTxtDivisionNo()));
                observable.setTxtDivisionNo(CommonUtil.convertObjToInt(hashMap.get("DIVISION_NO")));    //AJITH
                tdtChitStartDt.setDateValue(CommonUtil.convertObjToStr(hashMap.get("CHIT_START_DT")));
                observable.setTdtChitStartDt(CommonUtil.convertObjToStr(hashMap.get("CHIT_START_DT")));
                txtMemberNo.setText(observable.getTxtMemberNo());
                lblMembershipName.setText(observable.getLblMemberName());
                lblMembershipType.setText(observable.getLblMemberType());
                lblNomineName.setText(observable.getLblNomineeName());
                btnMemberNo.setEnabled(true);
                txtLastInstNo.setText(observable.getTxtLastInstNo());
                tdtLastInstDate.setDateValue(observable.getTdtLastInstDate());
                txtTotalAmountTillDate.setText(CommonUtil.convertObjToStr(observable.getTxtTotalAmountTillDate()));   //AJITH
                txtOverDueInstallments.setText(CommonUtil.convertObjToStr(observable.getTxtOverDueInstallments()));   //AJITH
                txtOverDueAmount.setText(CommonUtil.convertObjToStr(observable.getTxtOverDueAmount()));   //AJITH
                txtPrizedAmount.setText(CommonUtil.convertObjToStr(observable.getTxtPrizedAmount()));   //AJITH
                tdtPayDt.setDateValue(observable.getTdtPayDt());
                txtRemarks.setText(observable.getTxtRemarks());
                observable.setTxtSchemeName(CommonUtil.convertObjToStr(hashMap.get("SCHEME_NAME")));
                observable.getData(hashMap, nomineeUi.getNomineeOB());
                tblMemberType.setModel(observable.getTblMemberTypeDetails());
                tblDepositDetails.setModel(observable.getTblDepositTypeDetails());
                tblOtherSocietyDetails.setModel(observable.getTblOtherSocietyDetails());
                ClientUtil.enableDisable(panMasterMaintenanceDetails, true);
                populateMasterMaintenanceFields();
                calculateTot();
                ClientUtil.enableDisable(panInstallmentDetails, false);
                txtRemarks.setEnabled(false);
                btnCustomerIdFileOpenCr.setEnabled(true);
            } else if (viewType == "SUB_MEMBER_NO") {
                txtMemNo.setText(CommonUtil.convertObjToStr(hashMap.get("MEMBERSHIP_NO")));
                observable.setTxtMemNo(CommonUtil.convertObjToStr(hashMap.get("MEMBERSHIP_NO")));

                txtMemName.setText(CommonUtil.convertObjToStr(hashMap.get("CUSTOMER")));
                observable.setTxtMemNo(CommonUtil.convertObjToStr(hashMap.get("CUSTOMER")));
                txtMemType.setText(CommonUtil.convertObjToStr(hashMap.get("MEMBERSHIP_CLASS")));
                observable.setTxtMemNo(CommonUtil.convertObjToStr(hashMap.get("MEMBERSHIP_CLASS")));

            } else if (viewType.equals("CREDIT_ACC_NO")) {
                String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
                if (prodType != null && !prodType.equals("GL")) {
                    if (prodType.equals("TD")) {
                        hashMap.put("ACCOUNTNO", hashMap.get("ACCOUNTNO") + "_1");
                    }
                    txtCustomerIdCr.setText(CommonUtil.convertObjToStr(hashMap.get("ACCOUNTNO")));
                    observable.setTxtCustomerIdCr(CommonUtil.convertObjToStr(hashMap.get("ACCOUNTNO")));
                } else {
                    txtCustomerIdCr.setText(CommonUtil.convertObjToStr(hashMap.get("A/C HEAD")));
                    observable.setTxtCustomerIdCr(CommonUtil.convertObjToStr(hashMap.get("A/C HEAD")));
                }
            } else if (viewType.equals("DEPOSIT_ACC_NO")) {
                String prodType = ((ComboBoxModel) cboProductTypeSecurity.getModel()).getKeyForSelected().toString();
                if (prodType.equals("TD")) {
                    txtDepNo.setText(CommonUtil.convertObjToStr(hashMap.get("ACT_NUM")));
                    HashMap whereMap = new HashMap();
                    whereMap.put("DEPOSIT NO", hashMap.get("ACT_NUM"));
                    List accountLst = ClientUtil.executeQuery("getSelectDepSubNoAccInfoTO", whereMap);
                    if (accountLst != null && accountLst.size() > 0) {
                        whereMap = (HashMap) accountLst.get(0);
                        tdtDepDt.setDateValue(CommonUtil.convertObjToStr(whereMap.get("DEPOSIT_DT")));
                        if(CommonUtil.convertObjToDouble(whereMap.get("CLEAR_BALANCE")) > 0){
                            txtDepAmount.setText(CommonUtil.convertObjToStr(whereMap.get("AVAILABLE_BALANCE")));
                        }else{
                            txtDepAmount.setText(CommonUtil.convertObjToStr(hashMap.get("AMOUNT")));
                        }                        
                        txtRateOfInterest.setText(CommonUtil.convertObjToStr(whereMap.get("RATE_OF_INT")));
                        txtMaturityValue.setText(CommonUtil.convertObjToStr(whereMap.get("MATURITY_AMT")));
                        txtMaturityDt.setDateValue(CommonUtil.convertObjToStr(whereMap.get("MATURITY_DT")));
                    }
                } else {
                    txtDepNo.setText(CommonUtil.convertObjToStr(hashMap.get("CHITTAL_NO")));
                    tdtDepDt.setDateValue(CommonUtil.convertObjToStr(hashMap.get("START_DT")));
                    txtDepAmount.setText(CommonUtil.convertObjToStr(hashMap.get("INST_AMT")));
                    txtMaturityValue.setText(CommonUtil.convertObjToStr(hashMap.get("PAID_AMOUNT")));
                    txtMaturityDt.setDateValue(CommonUtil.convertObjToStr(hashMap.get("END_DT")));
                }
            } else if (viewType == "OWNER_MEMBER_NO") {
                String memberNo = txtOwnerMemNo.getText();
                if (tblCollateral.getRowCount() > 0) {
                    for (int i = 0; i < tblCollateral.getRowCount(); i++) {
                        String membNo = CommonUtil.convertObjToStr(tblCollateral.getValueAt(i, 0));
//                        if (memberNo.equalsIgnoreCase(membNo) && !updateMode) {
//                            ClientUtil.displayAlert("Member No Already Exists in this Table");
//                            resetCollateralDetails();
//                            btnSecurityCollateral(false);
//                            btnCollateralNew.setEnabled(true);
//                            btnOwnerMemNo.setEnabled(false);
//                            ClientUtil.enableDisable(panCollatetalDetails, false);
//                            return;
//                        }
                    }
                }
                observable.setRdoGahanYes(rdoGahanYes.isSelected());
                observable.setRdoGahanNo(rdoGahanNo.isSelected());
                txtOwnerMemNo.setText(CommonUtil.convertObjToStr(hashMap.get("MEMBERSHIP_NO")));
                observable.setDocGenId(CommonUtil.convertObjToStr(hashMap.get("DOCUMENT_GEN_ID")));
                collateralJointAccountDisplay(txtOwnerMemNo.getText());
                txtOwnerMemberNname.setText(CommonUtil.convertObjToStr(hashMap.get("CUSTOMER")));
                observable.setTxtOwnerMemberNname(CommonUtil.convertObjToStr(hashMap.get("CUSTOMER")));
                observable.setDocGenId("");
            } else if (viewType == "DOCUMENT_NO") {
                HashMap documentMap = new HashMap();
                documentMap.put("DOCUMENT_NO", CommonUtil.convertObjToStr(hashMap.get("DOCUMENT_NO")));
                documentMap.put("DOCUMENT_GEN_ID", CommonUtil.convertObjToStr(hashMap.get("DOCUMENT_GEN_ID")));
                if (getDocumentDetails(documentMap)) {
                    return;
                }
                txtOwnerMemNo.setText(CommonUtil.convertObjToStr(hashMap.get("MEMBERSHIP_NO")));
                observable.setTxtOwnerMemNo(CommonUtil.convertObjToStr(hashMap.get("MEMBERSHIP_NO")));
                txtOwnerMemberNname.setText(CommonUtil.convertObjToStr(hashMap.get("CUSTOMER")));
                observable.setTxtOwnerMemberNname(CommonUtil.convertObjToStr(hashMap.get("CUSTOMER")));
            } else if (viewType == "GOLD_SECURITY_STOCK") {
                System.out.println("GOLD_SECURITY_STOCK :: " + hashMap);
                txtJewelleryDetails.setText(CommonUtil.convertObjToStr(hashMap.get("PARTICULARS")));
                txtGrossWeight.setText(CommonUtil.convertObjToStr(hashMap.get("GROSS_WEIGHT")));
                txtNetWeight.setText(CommonUtil.convertObjToStr(hashMap.get("NET_WEIGHT")));                
                UpdateCalculatedGoldSecurityValue(hashMap);
                txtGoldSecurityId.setText(CommonUtil.convertObjToStr(hashMap.get("GOLD_SECURITY_ID")));
               
            }else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                observable.setTxtSchemeName(CommonUtil.convertObjToStr(hashMap.get("SCHEME_NAME")));
                this.setButtonEnableDisable();
                observable.getData(hashMap, nomineeUi.getNomineeOB());
                bufferList.clear();
                bufferList = observable.getSalarySecDetails();
                getTableData();
                observable.setPaymentDetails();
                observable.setReceiptDetails(hashMap);
                tblMemberType.setModel(observable.getTblMemberTypeDetails());
                tblDepositDetails.setModel(observable.getTblDepositTypeDetails());
                tblOtherSocietyDetails.setModel(observable.getTblOtherSocietyDetails());
                tblCollateral.setModel(observable.getTblCollateralDetails());
                populateMasterMaintenanceFields();
                calculateTot();
                txtRemarks.setEnabled(false);
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                this.setButtonEnableDisable();
                observable.setTxtSchemeName(CommonUtil.convertObjToStr(hashMap.get("SCHEME_NAME")));
                observable.setTxtChittalNo(CommonUtil.convertObjToStr(hashMap.get("CHITTAL_NO")));
                observable.getData(hashMap, nomineeUi.getNomineeOB());
                bufferList.clear();
                bufferList = observable.getSalarySecDetails();
                getTableData();

                observable.setPaymentDetails();
                observable.setReceiptDetails(hashMap);
                tblMemberType.setModel(observable.getTblMemberTypeDetails());
                tblDepositDetails.setModel(observable.getTblDepositTypeDetails());
                tblOtherSocietyDetails.setModel(observable.getTblOtherSocietyDetails());
                tblCollateral.setModel(observable.getTblCollateralDetails());
                populateMasterMaintenanceFields();
                calculateTot();
                txtRemarks.setEnabled(false);
                btnSecurityCollateral(false);
            }
            //  txtChittalNo.setEnabled(true);
            if (viewType == AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                txtChittalNo.setEnabled(false);
            }
            hashMap = null;
            btnCancel.setEnabled(true);
            if (observable.getChkNominee() == true) {
                //            tabTermDeposit.add(nomineeUi);
                tabMasterMaintenanceDetails.add(nomineeUi, "Nominee");
                tabMasterMaintenanceDetails.resetVisits();
                //            ClientUtil.enableDisable(nomineeUi, true);
                nomineeUi.enableDisableNominee_SaveDelete();
                /**
                 * * TO get the Max of the deleted Nominee(s) for the
                 * particular Account-Holder...
                 */
                nomineeUi.callMaxDel(txtChittalNo.getText());
                nomineeUi.resetNomineeTab();
                nomineeUi.setActionType(observable.getActionType());
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW_MODE
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                    ClientUtil.enableDisable(nomineeUi, false);
                } else {
                    ClientUtil.enableDisable(nomineeUi, true);
                }
            }
//            if(hashMap.containsKey("FROM_AUTHORIZE_LIST_UI")){
//                btnSave.setEnabled(false);
//                btnCancel.setEnabled(true);
//                btnAuthorize.setEnabled(true);
//                btnReject.setEnabled(true);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
        if (rejectFlag == 1) {
            btnReject.setEnabled(false);
        }
        
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE){
	        btnAuthorize.setEnabled(true);
	        btnAuthorize.requestFocusInWindow();     
      }
    }

//       private void editChargeAmount(){
//        HashMap appraiserMap = new HashMap();
//        appraiserMap.put("SCHEME_ID",prodDesc);
//        appraiserMap.put("DEDUCTION_ACCU","R");
//        chargelst = ClientUtil.executeQuery("getAllChargeDetailsData", appraiserMap);
//        HashMap chargeMap = new HashMap();
//        if(chargelst!=null && chargelst.size()>0){
//            for(int i=0; i<chargelst.size(); i++){
//                String accHead="";
//                chargeMap = (HashMap)chargelst.get(i);
//                accHead = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_DESC"));
//                //system.out.println("$#@@$ accHead"+accHead);
//                for(int j=0; j<table.getRowCount();j++) {
//                    //system.out.println("$#@@$ accHead inside table "+table.getValueAt(j, 0));
//                    if (CommonUtil.convertObjToStr(table.getValueAt(j, 0)).equals(accHead)) {
//                        double chargeAmt = 0;
//                        if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Sanction Amount")) {
//                            chargeAmt = CommonUtil.convertObjToDouble(txtLoanAmt.getText()).doubleValue() *
//                            CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("CHARGE_RATE"))).doubleValue()/100;
//                            long roundOffType = getRoundOffType(CommonUtil.convertObjToStr(chargeMap.get("ROUND_OFF_TYPE")));
//                            if (roundOffType!=0) {
//                                chargeAmt = rd.getNearest((long)(chargeAmt*roundOffType), roundOffType)/roundOffType;
//                            }
//                            double minAmt = CommonUtil.convertObjToDouble(chargeMap.get("MIN_CHARGE_AMOUNT")).doubleValue();
//                            double maxAmt = CommonUtil.convertObjToDouble(chargeMap.get("MAX_CHARGE_AMOUNT")).doubleValue();
//                            if (chargeAmt<minAmt) {
//                                chargeAmt = minAmt;
//                            }
//                            if (chargeAmt>maxAmt) {
//                                chargeAmt = maxAmt;
//                            }
//                            table.setValueAt(String.valueOf(chargeAmt),j, 1);
//                        }else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Amount Range")) {
//                            chargeAmt = CommonUtil.convertObjToDouble(txtLoanAmt.getText()).doubleValue() *
//                            CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("CHARGE_RATE"))).doubleValue()/100;
//                            table.setValueAt(String.valueOf(chargeAmt),j, 1);
//                        }else if (CommonUtil.convertObjToStr(chargeMap.get("CHARGE_BASE")).equals("Flat Charge")) {
//                            chargeAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(chargeMap.get("FLAT_CHARGE"))).doubleValue();
//                        }
//                        chargeMap.put("CHARGE_AMOUNT",String.valueOf(chargeAmt));
//                    }
//                }
//            }
//            //system.out.println("#$#$$# chargeMap:"+chargeMap);
//            //system.out.println("#$#$$# chargelst:"+chargelst);
//            //charge details
//            //calculating total of selected amount
//            
//            table.revalidate();
//            table.updateUI();
//            
//        }
//    }     // CHARGE_AMOUNT_END
    private void editChargeTable() {
        HashMap tableMap = editBuildData(prodDesc);
        ArrayList dataList = new ArrayList();
        //system.out.println("#$#$$# tableMap:>>>>>>>>>>>" + tableMap);
        dataList = (ArrayList) tableMap.get("DATA");
        //system.out.println("#$#$$# dataList:>>>>>>>>>>>" + dataList);
        //system.out.println("#$#$$# dataList.size():>>>>>>>>>>>" + dataList.size());
        if (dataList != null && dataList.size() > 0) {
            tableFlag = true;
            ArrayList headers;
            if (!cbxDefaulter.isSelected()) {
                panChargeDetails.setVisible(true);
            }
            SimpleTableModel stm = new SimpleTableModel((ArrayList) tableMap.get("DATA"), (ArrayList) tableMap.get("HEAD"));
            table = new JTable(stm);
            table.setSize(430, 110);
            //charge details
            //calculating total of selected amount
            //system.out.println("ggsssggkjf>>>>>>>>>>>.####");
//             table.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                //system.out.println("ggsssggkjf");
//                tableMouseClicked(evt);
//            }
//          });

            //charge end..
            srpChargeDetails = new javax.swing.JScrollPane(table);
            srpChargeDetails.setMinimumSize(new java.awt.Dimension(430, 110));
            srpChargeDetails.setPreferredSize(new java.awt.Dimension(430, 110));
            panChargeDetails.add(srpChargeDetails, new java.awt.GridBagConstraints());
            table.revalidate();
            System.out.println(" lllllllllllledit charge table 222222");
            cbxOnlyApplication.setEnabled(false);
        } else {
            tableFlag = false;
            chrgTableEnableDisable();
        }
    }

    private HashMap editBuildData(String prodDesc) {
        //system.out.println("###################+" + prodDesc);
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_ID", prodDesc);
        whereMap.put("DEDUCTION_ACCU", "R");
        List list = ClientUtil.executeQuery("getChargeDetailsData", whereMap);
        boolean _isAvailable = list.size() > 0 ? true : false;
        ArrayList _heading = null;
        ArrayList data = new ArrayList();
        ArrayList colData = new ArrayList();
        HashMap map;
        Iterator iterator = null;
        if (_isAvailable) {
            map = (HashMap) list.get(0);
            iterator = map.keySet().iterator();
        }
        if (_isAvailable && _heading == null) {
            _heading = new ArrayList();
            while (iterator.hasNext()) {
                _heading.add((String) iterator.next());
            }
        }
        String cellData = "", keyData = "";
        Object obj = null;
        for (int i = 0, j = list.size(); i < j; i++) {
            map = (HashMap) list.get(i);
            colData = new ArrayList();
            iterator = map.values().iterator();
            while (iterator.hasNext()) {
                obj = iterator.next();
                colData.add(CommonUtil.convertObjToStr(obj));
            }
            data.add(colData);
        }
        map = new HashMap();
        //system.out.println("head>>>>>>>>>>.." + _heading);
        map.put("HEAD", _heading);
        //system.out.println("DATA>>>>>>>>>>.." + data);
        map.put("DATA", data);
        return map;
    }

    private boolean getDocumentDetails(HashMap documentMap) {
        //system.out.println("document Map" + documentMap);
        List lst = ClientUtil.executeQuery("getSelectGahanDocumentDetails", documentMap);
        //system.out.println("lst" + lst);
        if (lst != null && lst.size() > 0) {
            HashMap dataMap = (HashMap) lst.get(0);
            //system.out.println("dataMap.." + dataMap);
            updateGahandetails(dataMap);
            return true;
        }
        return false;
    }

    private void updateGahandetails(HashMap map) {
        //system.out.println("map####11111" + map);
        double pledgeAmt = 0;
        double actualPledge = 0;
        double totPledge=0;
        txtDocumentNo.setText(CommonUtil.convertObjToStr(map.get("DOCUMENT_NO")));
        cboDocumentType.setSelectedItem(CommonUtil.convertObjToStr(map.get("DOCUMENT_TYPE")));
        tdtDocumentDate.setDateValue(CommonUtil.convertObjToStr(map.get("DOCUMENT_DT")));
        txtRegisteredOffice.setText(CommonUtil.convertObjToStr(map.get("REGISTRED_OFFICE")));
        cboPledge.setSelectedItem(CommonUtil.convertObjToStr(map.get("PLEDGE")));
        tdtPledgeDate.setDateValue(CommonUtil.convertObjToStr(map.get("PLEDGE_DT")));
        txtPledgeNo.setText(CommonUtil.convertObjToStr(map.get("PLEDGE_NO")));
        actualPledge = checkAvailableSecurity(CommonUtil.convertObjToStr(map.get("DOCUMENT_GEN_ID")));
        
       System.out.println("actualPledge===" + actualPledge);
       Double gahanLnAmt = getPldgeAmountForLoan(CommonUtil.convertObjToStr(map.get("DOCUMENT_GEN_ID")));
        System.out.println("gahanLnAmt====" + gahanLnAmt);
        actualPledge = actualPledge + gahanLnAmt;
        Double gahanMdsAmt = getPldgeAmtForMds(CommonUtil.convertObjToStr(map.get("DOCUMENT_GEN_ID")));
        System.out.println("gahanMdsAmt"+gahanMdsAmt);
//        Double totGahanPledged=getPldgeAmtForMds1(CommonUtil.convertObjToStr(map.get("DOCUMENT_GEN_ID")));
//        System.out.println("totGahanPledged=="+totGahanPledged);
//        gahanMdsAmt=totGahanPledged-gahanMdsAmt;
//        System.out.println("gahanMds pledge amt====" + gahanMdsAmt);
        System.out.println("actualPledgeactualPledge"+actualPledge);
        actualPledge = actualPledge - gahanMdsAmt;
        System.out.println("actualPledgeactualPledge===="+actualPledge);
        
        pledgeAmt = getGahanAvailableSecurity(actualPledge);
        System.out.println("pledgeAmt::::::"+pledgeAmt);
//        Double gahanLnAmt=getPldgeAmountForLoan(CommonUtil.convertObjToStr(map.get("DOCUMENT_GEN_ID")));
//        //system.out.println("gahanLnAmt===="+gahanLnAmt);
//        actualPledge=actualPledge+gahanLnAmt;
//        Double gahanMdsAmt=getPldgeAmtForMds(CommonUtil.convertObjToStr(map.get("DOCUMENT_GEN_ID")));
//        //system.out.println("gahanMdsAmt===="+gahanMdsAmt);
//        actualPledge=actualPledge-gahanMdsAmt;Delete All Gahan Records From th
        //pledgeAmt = getGahanAvailableSecurity(actualPledge);
        //system.out.println("pledgeAmt.." + pledgeAmt);
          HashMap mapProd= new HashMap();
          mapProd.put("PROD_ID", observable.getTxtProductId());
          List lstPrizdPend=ClientUtil.executeQuery("getSelPrizdPend", mapProd);
          if(lstPrizdPend.size()>0 && !lstPrizdPend.isEmpty())
          {
           mapProd= new HashMap();
           mapProd=(HashMap)lstPrizdPend.get(0);
          }  
          String prizdPend=CommonUtil.convertObjToStr(mapProd.get("SECURITY_PRIZED"));
          System.out.println("prizdPend:::::"+prizdPend);
          ///prized//jiby
          if(prizdPend.equals("Y"))    
          {
              Double sanAmt=CommonUtil.convertObjToDouble(txtPrizedAmount.getText().toString());
               if(sanAmt<pledgeAmt)
               {
                txtPledgeAmount.setText(String.valueOf(sanAmt));    
                }
               else
                 txtPledgeAmount.setText(String.valueOf(pledgeAmt));
          }
          ///pending//jiby
          else
          {
            HashMap mdsMap = new HashMap();
        String chittalNo = txtChittalNo.getText();
        //String subNo = txtSubNo.getText();    //AJITH Blocked on 13/08/2021
        Integer subNo = CommonUtil.convertObjToInt(txtSubNo.getText()); //AJITH New Line on 13/08/2021
        mdsMap.put("CHITTAL_NO", chittalNo);
        mdsMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
        double noinset = 0.0;
        double instAmt = 0.0;
        double paidInst = 0.0;
        double totAmt = 0.0;
        List mdsList = ClientUtil.executeQuery("getTotalInstAmount", mdsMap);
        List mdsList1 = ClientUtil.executeQuery("getSelctApplnReceiptDetails", mdsMap);
        if (mdsList1 != null && mdsList1.size() > 0) {
            mdsMap = (HashMap) mdsList1.get(0);
            noinset = CommonUtil.convertObjToDouble(mdsMap.get("NO_OF_INSTALLMENTS")).doubleValue();
            instAmt = CommonUtil.convertObjToDouble(mdsMap.get("INST_AMT")).doubleValue();
            totAmt = instAmt * noinset;
        }
        if (mdsList != null && mdsList.size() > 0) {
            mdsMap = (HashMap) mdsList.get(0);
            paidInst = CommonUtil.convertObjToDouble(mdsMap.get("NO_INST_PAID")).doubleValue();
            paidInst = paidInst * instAmt;
        }
        double tobepaidInst = totAmt - paidInst; 
        if(tobepaidInst<pledgeAmt){
            txtPledgeAmount.setText(CommonUtil.convertObjToStr(tobepaidInst));    
        } else
            txtPledgeAmount.setText(CommonUtil.convertObjToStr(pledgeAmt));
        }
//        Double sanAmt=CommonUtil.convertObjToDouble(txtPrizedAmount.getText().toString());
//        //system.out.println("sanAmtsanAmt====="+sanAmt);
//        if(sanAmt<pledgeAmt)
//        {
//        txtPledgeAmount.setText(String.valueOf(sanAmt));    
//        }
//        else
//        txtPledgeAmount.setText(String.valueOf(pledgeAmt));
        ////To show the value of gahan as a whole
//        HashMap mapGahan= new HashMap();
//        mapGahan.put("DOC_GEN_ID", CommonUtil.convertObjToStr(map.get("DOCUMENT_GEN_ID")));
//        List listTotPldge=ClientUtil.executeQuery("getSelTotPldgeAmt", mapGahan);
//        mapGahan= new HashMap();
//        if(listTotPldge.size()>0 && !listTotPldge.isEmpty())
//                   mapGahan=(HashMap)listTotPldge.get(0);
//        totPledge=CommonUtil.convertObjToDouble(mapGahan.get("TOT_PLDGE_AMT"));
//        txtPledgeAmount.setText(String.valueOf(totPledge));
        /////////////
        txtVillage.setText(CommonUtil.convertObjToStr(map.get("VILLAGE")));
        txtSurveyNo.setText(CommonUtil.convertObjToStr(map.get("SARVEY_NO")));
        txtTotalArea.setText(CommonUtil.convertObjToStr(map.get("TOTAL_AREA")));
        //system.out.println("nnnkkk" + map.get("RIGHT") + "hhhhh" + map.get("NATURE"));
        cboNature.setSelectedItem(CommonUtil.convertObjToStr(map.get("NATURE")));
        cboRight.setSelectedItem(CommonUtil.convertObjToStr(map.get("RIGHT")));
        txtAreaParticular.setText(observable.getTxtAreaParticular());
        observable.setDocGenId(CommonUtil.convertObjToStr(map.get("DOCUMENT_GEN_ID")));
        observable.addPledgeAmountMap(CommonUtil.convertObjToStr(map.get("DOCUMENT_NO")), actualPledge);
    }

    public double getPldgeAmountForLoan(String doc_gen_id) {
        Double gahanForLn = 0.0;
        HashMap mapDocGenId = new HashMap();
        mapDocGenId.put("DOC_GEN_ID", doc_gen_id);
        List lstGahanForLn = ClientUtil.executeQuery("getSelGahanForLn", mapDocGenId);
        if (!lstGahanForLn.isEmpty() && !lstGahanForLn.equals(null)) {
            mapDocGenId = new HashMap();
            mapDocGenId = (HashMap) lstGahanForLn.get(0);
//            if(mapDocGenId.containsValue("CLEARBAL"))
//            {
            gahanForLn = CommonUtil.convertObjToDouble(mapDocGenId.get("CLEARBAL"));
//            }   
        }
        return gahanForLn;
    }

    public double getPldgeAmtForMds(String doc_gen_id) {
        Double gahanForMDS = 0.0;
        HashMap mapDocGenId = new HashMap();
        mapDocGenId.put("DOC_GEN_ID", doc_gen_id);
        List lstGahanForMds = ClientUtil.executeQuery("getSelGahanForMds", mapDocGenId);
        if (!lstGahanForMds.isEmpty() && !lstGahanForMds.equals(null)) {
            mapDocGenId = new HashMap();
            mapDocGenId = (HashMap) lstGahanForMds.get(0);
//            if(mapDocGenId.containsValue("AMOUNT"))
//            {
            gahanForMDS = CommonUtil.convertObjToDouble(mapDocGenId.get("AMOUNT"));
//            }   
        }
        return gahanForMDS;
    }
    public double getPldgeAmtForMds1(String doc_gen_id) {
        Double gahanForMDS = 0.0;
        HashMap mapDocGenId = new HashMap();
        mapDocGenId.put("DOC_GEN_ID", doc_gen_id);
        List lstGahanForMds = ClientUtil.executeQuery("getSelGahanForMds1", mapDocGenId);
        if (!lstGahanForMds.isEmpty() && !lstGahanForMds.equals(null)) {
            mapDocGenId = new HashMap();
            mapDocGenId = (HashMap) lstGahanForMds.get(0);
//            if(mapDocGenId.containsValue("AMOUNT"))
//            {
            gahanForMDS = CommonUtil.convertObjToDouble(mapDocGenId.get("GAHAN_SET"));
//            }   
        }
        return gahanForMDS;
    }

    private double getGahanAvailableSecurity(double maxsecurityAmt) {
        double availableSecuirty = 0;
        double sumGahanTableValue = 0;
        double prizedAmt = 0;
        int count = tblCollateral.getRowCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                sumGahanTableValue += CommonUtil.convertObjToDouble(tblCollateral.getValueAt(i, 3)).doubleValue();
            }
        }
        //        prizedAmt= CommonUtil.convertObjToDouble(txtPrizedAmount.getText()).doubleValue();
//        availableSecuirty=prizedAmt-sumGahanTableValue;
//        if(maxsecurityAmt>=availableSecuirty)
//            return availableSecuirty;
//        else
        //system.out.println("maxsecurityAmt" + maxsecurityAmt);
        return maxsecurityAmt;
    }

    private double checkAvailableSecurity(String docGenId) {
        HashMap map = new HashMap();
        double availableSecurity = 0;
        if (docGenId != null) {
            map.put("DOC_GEN_ID", docGenId);
//            List lst = ClientUtil.executeQuery("getGahanAvailableSecurityforMDS", map);
            
              List lst = ClientUtil.executeQuery("getGahanAvailableSecurity", map);
            if (lst != null && lst.size() > 0) {
                map = (HashMap) lst.get(0);
//                availableSecurity = CommonUtil.convertObjToDouble(map.get("AVAILABLE_SECURITY_VALUE")).doubleValue();
                 availableSecurity = CommonUtil.convertObjToDouble(map.get("TOT_SECURITY_VALUE")).doubleValue();
                
            }
        }
        //system.out.println("availableSecurity...@K.." + availableSecurity);
        return availableSecurity;
    }

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        super.removeEditLock(observable.getTxtSchemeName());
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        viewType = "CANCEL";
        lblStatus.setText("               ");
        bufferList.clear();
        chrgTableEnableDisable();
        getTableData();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(this, false);
        observable.resetForm();
        setModified(false);
        ResolutionSearch.setEnabled(false);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnView.setEnabled(true);
        isFilled = false;
        enableDisableButton(false);
        enableDisableButtons(false);
        lblMembershipName.setText("");
        lblMembershipType.setText("");
        lblNomineName.setText("");
        nomineeUi.resetTable();
        nomineeUi.resetNomineeData();
        nomineeUi.resetNomineeTab();
        nomineeUi.disableNewButton(false);
        lblTotalDepositValue.setText("");
        lblCoChit.setText("");
        lblLockStatusVal.setText("");
        btnNew.setEnabled(false);
        removeGahanRadioButton();
        btnSecurityCollateral(false);
        lblProdDesc.setText(" ");
        if (fromNewAuthorizeUI) {
            this.dispose();
            fromNewAuthorizeUI = false;
            newauthorizeListUI.setFocusToTable();
        }
        if (fromAuthorizeUI) {
            this.dispose();
            fromAuthorizeUI = false;
            authorizeListUI.setFocusToTable();
        }
        observable.set_authorizeMap(null);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        observable.setChargelst(null);
        thalayal =false;
        rdoGoldSecurityExitsNo.setSelected(false);
        rdoGoldSecurityExitsYes.setSelected(false);
    }//GEN-LAST:event_btnCancelActionPerformed
   
    private void clearGoldSecurityFields(){
        txtGoldSecurityId.setText("");
        txtGrossWeight.setText("");
        txtNetWeight.setText("");
        txtValueOfGold.setText("");
        txtJewelleryDetails.setText("");
        txtGoldRemarks.setText("");
        
        txtGoldSecurityId.setEnabled(false);
        txtGrossWeight.setEnabled(true);
        txtNetWeight.setEnabled(true);
        txtValueOfGold.setEnabled(true);
        txtJewelleryDetails.setEnabled(true);
        txtGoldRemarks.setEnabled(true);
    }
    
    private void enableDisableButton(boolean flag) {
        btnSchemeName.setEnabled(flag);
        btnMemberNo.setEnabled(flag);
        btnChittalNo.setEnabled(flag);
        btnCustomerIdFileOpenCr.setEnabled(false);
        btnCaseNew.setEnabled(flag);
        btnCaseSave.setEnabled(flag);
        btnCaseDelete.setEnabled(flag);
    }

    private void enableDisableButtons(boolean flag) {
        btnMemberNew.setEnabled(flag);
        btnMemberSave.setEnabled(flag);
        btnMemberDelete.setEnabled(flag);
        btnDepositNew.setEnabled(flag);
        btnDepositSave.setEnabled(flag);
        btnDepositDelete.setEnabled(flag);
        btnSocietyNew.setEnabled(flag);
        btnSocietySave.setEnabled(flag);
        btnSocietyDelete.setEnabled(flag);
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

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

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
        //        this.dispose();
    }//GEN-LAST:event_mitCloseActionPerformed

    private void btnNew_Property1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew_Property1ActionPerformed

        txtSalaryCertificateNo.setText("");
        txtEmployerName.setText("");
        txtAddress.setText("");
        cboCity.setSelectedItem("");
        txtPinCode.setText("");
        txtDesignation.setText("");
        txtContactNo.setText("");
        txtMemberNum.setText("");
        tdtRetirementDt.setDateValue(null);
        txtTotalSalary.setText("");
        txtNetWorth.setText("");
        txtSalaryRemark.setText("");


        txtSalaryCertificateNo.setEnabled(true);
        txtEmployerName.setEnabled(true);
        txtAddress.setEnabled(true);
        cboCity.setEnabled(true);
        txtPinCode.setEnabled(true);
        txtDesignation.setEnabled(true);
        txtContactNo.setEnabled(true);
        txtMemberNum.setEnabled(true);
        tdtRetirementDt.setEnabled(true);
        txtTotalSalary.setEnabled(true);
        txtNetWorth.setEnabled(true);
        txtSalaryRemark.setEnabled(true);



    }//GEN-LAST:event_btnNew_Property1ActionPerformed

    private void btnlSave_Property1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlSave_Property1ActionPerformed
        // TODO add your handling code here:

        StringBuffer message = new StringBuffer("");
        if (txtSalaryCertificateNo.getText().equals("")) {
            message.append("Enter the Salary Certificate No");
            message.append("\n");
        }
        if (txtEmployerName.getText().equals("")) {
            message.append("Enter the Employer Name");
            message.append("\n");
        }
        if (txtAddress.getText().equals("")) {
            message.append("Enter Address");
            message.append("\n");
        }

        if (cboCity.getSelectedIndex() == 0) {
            message.append("Enter city");
            message.append("\n");
        }

        if (txtPinCode.getText().equals("")) {
            message.append("Enter Pincode");
            message.append("\n");
        }
        if (txtDesignation.getText().equals("")) {
            message.append("Enter Designation");
            message.append("\n");
        }
        if (tdtRetirementDt.getDateValue().equals("")) {
            message.append("Enter Retirement Date");
            message.append("\n");
        }
        if (txtMemberNum.getText().equals("")) {
            message.append("Enter Member No");
            message.append("\n");
        }
        if (txtTotalSalary.getText().equals("")) {
            message.append("Enter Total Salary");
            message.append("\n");
        }

        if (txtNetWorth.getText().equals("")) {
            message.append("Enter NetWorth");
            message.append("\n");
        }

        if (message.length() > 0) {
            displayAlert(message.toString());
            return;
        }




        String salarycertificate = txtSalaryCertificateNo.getText();

        String Emp_name = txtEmployerName.getText();
        String addr = txtAddress.getText();
        String city = CommonUtil.convertObjToStr(cboCity.getSelectedItem());
        String pin = txtPinCode.getText();
        String designation = txtDesignation.getText();

        String contactNo = txtContactNo.getText();
        String date_of_retairmnt = tdtRetirementDt.getDateValue();
        String mem_no = txtMemberNum.getText();
        String tot_sal = txtTotalSalary.getText();
        String netWorth = txtNetWorth.getText();
        String remark = txtSalaryRemark.getText();


        //         int selectrow=tblGahanLandDetails.getSelectedRow();

        String row = CommonUtil.convertObjToStr(new Integer(tblSalaryDetails.getRowCount() + 1));
        //system.out.println("rowwww " + row);
        HashMap hmap = new HashMap();
        if (tblSalaryDetails.getSelectedRow() >= 0) {
            int num = tblSalaryDetails.getSelectedRow();
            String slno = CommonUtil.convertObjToStr(tblSalaryDetails.getValueAt(tblSalaryDetails.getSelectedRow(), 0));
            hmap.put("SL_NO", slno);
            hmap.put("SALARY_CERTIFICATE_NO", salarycertificate);
            hmap.put("EMP_NAME", Emp_name);
            hmap.put("EMP_ADDRESS", addr);
            hmap.put("CITY", city);
            hmap.put("PIN", pin);
            hmap.put("DESIGNATION", designation);
            hmap.put("CONTACT_NO", contactNo);
            hmap.put("RETIREMENT_DT", date_of_retairmnt);
            hmap.put("EMP_MEMBER_NO", mem_no);
            hmap.put("TOTAL_SALARY", tot_sal);
            hmap.put("NETWORTH", netWorth);
            hmap.put("REMARKS", remark);

            bufferList.set(num, hmap);
        } else {
            hmap.put("SL_NO", row);
            hmap.put("SALARY_CERTIFICATE_NO", salarycertificate);
            hmap.put("EMP_NAME", Emp_name);
            hmap.put("EMP_ADDRESS", addr);
            hmap.put("CITY", city);
            hmap.put("PIN", pin);
            hmap.put("DESIGNATION", designation);
            hmap.put("CONTACT_NO", contactNo);
            hmap.put("RETIREMENT_DT", date_of_retairmnt);
            hmap.put("EMP_MEMBER_NO", mem_no);
            hmap.put("TOTAL_SALARY", tot_sal);
            hmap.put("NETWORTH", netWorth);
            hmap.put("REMARKS", remark);
            bufferList.add(hmap);
        }
        txtSalaryCertificateNo.setText("");
        txtEmployerName.setText("");
        txtAddress.setText("");
        cboCity.setSelectedItem("");
        txtPinCode.setText("");
        txtDesignation.setText("");
        txtContactNo.setText("");
        txtMemberNum.setText("");
        tdtRetirementDt.setDateValue(null);
        txtTotalSalary.setText("");
        txtNetWorth.setText("");
        txtSalaryRemark.setText("");


        txtSalaryCertificateNo.setEnabled(false);
        txtEmployerName.setEnabled(false);
        txtAddress.setEnabled(false);
        cboCity.setEnabled(false);
        txtPinCode.setEnabled(false);
        txtDesignation.setEnabled(false);
        txtContactNo.setEnabled(false);
        txtMemberNum.setEnabled(false);
        tdtRetirementDt.setEnabled(false);
        txtTotalSalary.setEnabled(false);
        txtNetWorth.setEnabled(false);
        txtSalaryRemark.setEnabled(false);
        getTableData();
    }//GEN-LAST:event_btnlSave_Property1ActionPerformed

    private void btnDelete_Property1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete_Property1ActionPerformed
        // TODO add your handling code here:
        if (tblSalaryDetails.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to Delete", "Error", JOptionPane.ERROR_MESSAGE);
            //f=false;
            // tabedit=false;
            return;
        } else {
            int row = tblSalaryDetails.getSelectedRow();
            //system.out.println("ROWWWWW>>>" + row + ">>>>>>>>>>" + bufferList.size());

            // drmOB=(DeathReliefMasterOB)buffer1(row);
            //           tdtDrfFromDt.setDateValue(tblInterest.getValueAt(row,0).toString());
            //           txtToDt.setText(tblInterest.getValueAt(row, 1).toString());
            //           txtInterestRate.setText(tblInterest.getValueAt(row, 2).toString());
            txtSalaryCertificateNo.setText("");
            txtEmployerName.setText("");
            txtAddress.setText("");
            cboCity.setSelectedItem("");
            txtPinCode.setText("");
            txtDesignation.setText("");
            txtContactNo.setText("");
            txtMemberNum.setText("");
            tdtRetirementDt.setDateValue(null);
            txtTotalSalary.setText("");
            txtNetWorth.setText("");
            txtSalaryRemark.setText("");


            txtSalaryCertificateNo.setEnabled(false);
            txtEmployerName.setEnabled(false);
            txtAddress.setEnabled(false);
            cboCity.setEnabled(false);
            txtPinCode.setEnabled(false);
            txtDesignation.setEnabled(false);
            txtContactNo.setEnabled(false);
            txtMemberNum.setEnabled(false);
            tdtRetirementDt.setEnabled(false);
            txtTotalSalary.setEnabled(false);
            txtNetWorth.setEnabled(false);
            txtSalaryRemark.setEnabled(false);



            HashMap temp = new HashMap();
            temp = (HashMap) bufferList.get(row);
            //system.out.println("temp  " + temp);
            int no = CommonUtil.convertObjToInt(temp.get("SL_NO").toString());
            bufferList.remove(row);
            //system.out.println("no  " + no + "bufferList   " + bufferList);
            //system.out.println("row+1 " + (row + 1) + " bufferList.size  " + bufferList.size());
            for (int i = row; i < bufferList.size(); i++) {
                HashMap amap = new HashMap();
                amap = (HashMap) bufferList.get(i);
                bufferList.remove(i);
                amap.put("SL_NO", no);
                bufferList.add(i, amap);

                no = no + 1;
                //system.out.println("bufff   " + bufferList + "  dfjd no  " + no);
            }
            // bufferList.remove(row);
            getTableData();
        }

    }//GEN-LAST:event_btnDelete_Property1ActionPerformed

    private void tblSalaryDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSalaryDetailsMouseClicked
        // TODO add your handling code here:
        int row = tblSalaryDetails.getSelectedRow();
        HashMap hmap = new HashMap();
        hmap = (HashMap) bufferList.get(row);

        txtSalaryCertificateNo.setText(hmap.get("SALARY_CERTIFICATE_NO").toString());
        txtEmployerName.setText(hmap.get("EMP_NAME").toString());
        txtAddress.setText(hmap.get("EMP_ADDRESS").toString());
        cboCity.setSelectedItem(hmap.get("CITY").toString());
        txtPinCode.setText(hmap.get("PIN").toString());
        txtDesignation.setText(hmap.get("DESIGNATION").toString());
        txtContactNo.setText(hmap.get("CONTACT_NO").toString());
        txtMemberNum.setText(hmap.get("EMP_MEMBER_NO").toString());
        tdtRetirementDt.setDateValue(CommonUtil.convertObjToStr(hmap.get("RETIREMENT_DT")));
        txtTotalSalary.setText(hmap.get("TOTAL_SALARY").toString());
        txtNetWorth.setText(hmap.get("NETWORTH").toString());
        txtSalaryRemark.setText(hmap.get("REMARKS").toString());

    }//GEN-LAST:event_tblSalaryDetailsMouseClicked

    private void txtSchemeNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSchemeNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSchemeNameActionPerformed

    private void cbxDefaulterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxDefaulterActionPerformed
        if (cbxDefaulter.isSelected()) {
            //Added By Suresh
            txtBondNo.setText("");
            bondNoset = "N";
            tdtBondDt.setDateValue("");
            txtApplicationNo.setText("");
            tdtApplicationDate.setDateValue("");
            observable.setTxtBordNo(txtBondNo.getText());
            observable.setTdtBordDt(tdtBondDt.getDateValue());
            lblTotalTransactionAmtVal.setText("");
            panChargeDetails.setVisible(false);
            System.out.println("llllllllllllcbxDefaulterActionPerformed((((((((((((((");
            cbxOnlyApplication.setEnabled(false);
        } else {
            panChargeDetails.setVisible(true);
            System.out.println("llllllllllllllcbxDefaulterActionPerformedssssssssssssssss");
            cbxOnlyApplication.setEnabled(true);
        }
    }//GEN-LAST:event_cbxDefaulterActionPerformed

    private void panLoanChargeDefMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panLoanChargeDefMouseClicked
        //system.out.println("inn pan LOan Chargee");
        //system.out.println("bbb");
        //  cbxOnlyApplication.isSelected();
    }//GEN-LAST:event_panLoanChargeDefMouseClicked

    private void cbxOnlyApplicationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxOnlyApplicationActionPerformed
        // TODO add your handling code here:
        //system.out.println("in actionPerformed...");
        if (cbxOnlyApplication.isSelected()) {
            // bondNo
            bondNoset = "N";
            txtBondNo.setText("");
            tdtBondDt.setDateValue("");
            txaApplicationSecRemarks.setEnabled(true);
            tdtApplicationDate.setDateValue(CommonUtil.convertObjToStr(currDate));            
//            tdtBondDt.setTtransactionUI.setButtonEnableDisable(true);
            transactionUI.setCallingApplicantName(lblMembershipName.getText());
            transactionUI.setCallingAmount("" + lblTotalTransactionAmtVal.getText());
            transactionUI.setCallingTransType("CASH");             
       }else{
            transactionUI.cancelAction(false);
            transactionUI.resetObjects();
            transactionUI.setCallingApplicantName("");
            transactionUI.setCallingAmount("");
        }     
    }//GEN-LAST:event_cbxOnlyApplicationActionPerformed

private void cbxSameChittalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxSameChittalActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_cbxSameChittalActionPerformed

private void ResolutionSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResolutionSearchActionPerformed
 popUp("RESOLUTION_NO");
    
    // TODO add your handling code here:
}//GEN-LAST:event_ResolutionSearchActionPerformed

    private void tdtChitCloseDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtChitCloseDtFocusLost
        // TODO add your handling code here:
        java.util.Date applnDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtChitCloseDt.getDateValue()));
        java.util.Date curDate = (Date) currDate.clone();
        if (curDate != null && applnDate != null && DateUtil.dateDiff(curDate, applnDate) > 0) {
            ClientUtil.showAlertWindow("Chit Close date should be less than current date");
            tdtChitCloseDt.setDateValue("");
        }
    }//GEN-LAST:event_tdtChitCloseDtFocusLost

    private void rdoGoldSecurityExitsYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoGoldSecurityExitsYesActionPerformed
        // TODO add your handling code here:
         if (rdoGoldSecurityExitsYes.isSelected()) {
            rdoGoldSecurityExitsNo.setSelected(false);   
            txtGoldRemarks.setText("CUSTOMER GOLD STOCK");
            ClientUtil.enableDisable(panGoldTypeDetails, false);
            btnGoldSecurityIdSearch.setEnabled(true); 
            rdoGoldSecurityExitsNo.setEnabled(true);   
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                txtJewelleryDetails.setText(observable.getTxtJewelleryDetails());
                txtGrossWeight.setText(CommonUtil.convertObjToStr(observable.getTxtGrossWeight())); //AJITH
                txtNetWeight.setText(CommonUtil.convertObjToStr(observable.getTxtNetWeight()));  //AJITH
                txtValueOfGold.setText(observable.getTxtValueOfGold());
            }
        } else {
           rdoGoldSecurityExitsNo.setSelected(true);
           btnGoldSecurityIdSearch.setEnabled(false); 
           clearGoldSecurityFields();
        }
    }//GEN-LAST:event_rdoGoldSecurityExitsYesActionPerformed

    private void rdoGoldSecurityExitsNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoGoldSecurityExitsNoActionPerformed
        // TODO add your handling code here:
        if (rdoGoldSecurityExitsNo.isSelected()) {
            rdoGoldSecurityExitsYes.setSelected(false);
            rdoGoldSecurityExitsYes.setEnabled(true);
            btnGoldSecurityIdSearch.setEnabled(false);  
            clearGoldSecurityFields();
        } else {
           rdoGoldSecurityExitsYes.setSelected(true);      
           txtGoldRemarks.setText("CUSTOMER GOLD STOCK");
           ClientUtil.enableDisable(panGoldTypeDetails, false);
           btnGoldSecurityIdSearch.setEnabled(true);
           rdoGoldSecurityExitsYes.setEnabled(true);           
        }
    }//GEN-LAST:event_rdoGoldSecurityExitsNoActionPerformed

    private void btnGoldSecurityIdSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoldSecurityIdSearchActionPerformed
        // TODO add your handling code here:
        popUp("GOLD_SECURITY_STOCK");
    }//GEN-LAST:event_btnGoldSecurityIdSearchActionPerformed

    private void displayAlert(String message) {
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

    private void setButtonEnableDisable() {
//        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());

        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());

        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton ResolutionSearch;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnChittalNo;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustomerIdFileOpenCr;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDelete_Property1;
    private com.see.truetransact.uicomponent.CButton btnDepNo;
    private com.see.truetransact.uicomponent.CButton btnDepositDelete;
    private com.see.truetransact.uicomponent.CButton btnDepositNew;
    private com.see.truetransact.uicomponent.CButton btnDepositSave;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnGoldSecurityIdSearch;
    private com.see.truetransact.uicomponent.CButton btnMemNo;
    private com.see.truetransact.uicomponent.CButton btnMemberDelete;
    private com.see.truetransact.uicomponent.CButton btnMemberNew;
    private com.see.truetransact.uicomponent.CButton btnMemberNo;
    private com.see.truetransact.uicomponent.CButton btnMemberSave;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNew_Property1;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSchemeName;
    private com.see.truetransact.uicomponent.CButton btnSocietyDelete;
    private com.see.truetransact.uicomponent.CButton btnSocietyNew;
    private com.see.truetransact.uicomponent.CButton btnSocietySave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnlSave_Property1;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel3;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CPanel cPanel3;
    private com.see.truetransact.uicomponent.CPanel cPanel4;
    private com.see.truetransact.uicomponent.CPanel cPanel6;
    private com.see.truetransact.uicomponent.CScrollPane cScrollPane1;
    private com.see.truetransact.uicomponent.CComboBox cboCity;
    private com.see.truetransact.uicomponent.CComboBox cboDepProdType;
    private com.see.truetransact.uicomponent.CComboBox cboOtherInstituion;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CComboBox cboProductTypeSecurity;
    private com.see.truetransact.uicomponent.CComboBox cboSecurityType;
    private com.see.truetransact.uicomponent.CComboBox cboSecurityTypeSociety;
    private com.see.truetransact.uicomponent.CCheckBox cbxDefaulter;
    private com.see.truetransact.uicomponent.CCheckBox cbxOnlyApplication;
    private com.see.truetransact.uicomponent.CCheckBox cbxSameChittal;
    private com.see.truetransact.uicomponent.CCheckBox chkNominee;
    private com.see.truetransact.uicomponent.CCheckBox chkStandingInstn;
    private javax.swing.JScrollPane jScrollPane1;
    private com.see.truetransact.uicomponent.CLabel lblAccountNo;
    private com.see.truetransact.uicomponent.CLabel lblAddress;
    private com.see.truetransact.uicomponent.CLabel lblApplicationDate;
    private com.see.truetransact.uicomponent.CLabel lblApplicationNo;
    private com.see.truetransact.uicomponent.CLabel lblBordDt;
    private com.see.truetransact.uicomponent.CLabel lblBordNo;
    private com.see.truetransact.uicomponent.CLabel lblChargeAmount;
    private com.see.truetransact.uicomponent.CLabel lblChitCloseDt;
    private com.see.truetransact.uicomponent.CLabel lblChitStartDt;
    private com.see.truetransact.uicomponent.CLabel lblChittalNo;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblCoChit;
    private com.see.truetransact.uicomponent.CLabel lblContactNo;
    private com.see.truetransact.uicomponent.CLabel lblContactNum;
    private com.see.truetransact.uicomponent.CLabel lblDefaulterMark;
    private com.see.truetransact.uicomponent.CLabel lblDepAmount;
    private com.see.truetransact.uicomponent.CLabel lblDepDt;
    private com.see.truetransact.uicomponent.CLabel lblDepNo;
    private com.see.truetransact.uicomponent.CLabel lblDesignation;
    private com.see.truetransact.uicomponent.CLabel lblDivisionNo;
    private com.see.truetransact.uicomponent.CLabel lblEmployerName;
    private com.see.truetransact.uicomponent.CLabel lblGoldRemarks;
    private com.see.truetransact.uicomponent.CLabel lblGoldSecurityExists;
    private com.see.truetransact.uicomponent.CLabel lblGrossWeight;
    private com.see.truetransact.uicomponent.CLabel lblIssueDtSoceity;
    private com.see.truetransact.uicomponent.CLabel lblJewelleryDetails;
    private com.see.truetransact.uicomponent.CLabel lblLastInstDate;
    private com.see.truetransact.uicomponent.CLabel lblLastInstNo;
    private com.see.truetransact.uicomponent.CLabel lblLockStatus;
    private com.see.truetransact.uicomponent.CLabel lblLockStatusVal;
    private com.see.truetransact.uicomponent.CLabel lblMaturityDateSociety;
    private com.see.truetransact.uicomponent.CLabel lblMaturityDt;
    private com.see.truetransact.uicomponent.CLabel lblMaturityValue;
    private com.see.truetransact.uicomponent.CLabel lblMaturityValueSociety;
    private com.see.truetransact.uicomponent.CLabel lblMemName;
    private com.see.truetransact.uicomponent.CLabel lblMemNetworth;
    private com.see.truetransact.uicomponent.CLabel lblMemNo;
    private com.see.truetransact.uicomponent.CLabel lblMemType;
    private com.see.truetransact.uicomponent.CLabel lblMemberNo;
    private com.see.truetransact.uicomponent.CLabel lblMemberNum;
    private com.see.truetransact.uicomponent.CLabel lblMemberPriority;
    private com.see.truetransact.uicomponent.CLabel lblMemberType;
    private com.see.truetransact.uicomponent.CLabel lblMembershipName;
    private com.see.truetransact.uicomponent.CLabel lblMembershipType;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNetWeight;
    private com.see.truetransact.uicomponent.CLabel lblNetWorth;
    private com.see.truetransact.uicomponent.CLabel lblNomineName;
    private com.see.truetransact.uicomponent.CLabel lblNomineeName;
    private com.see.truetransact.uicomponent.CLabel lblOtherInstituion;
    private com.see.truetransact.uicomponent.CLabel lblOtherInstituionName;
    private com.see.truetransact.uicomponent.CLabel lblOverDueAmount;
    private com.see.truetransact.uicomponent.CLabel lblOverDueInstallments;
    private com.see.truetransact.uicomponent.CLabel lblPayDt;
    private com.see.truetransact.uicomponent.CLabel lblPinCode;
    private com.see.truetransact.uicomponent.CLabel lblPrizedAmount;
    private com.see.truetransact.uicomponent.CLabel lblProdDesc;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    private com.see.truetransact.uicomponent.CLabel lblProductType;
    private com.see.truetransact.uicomponent.CLabel lblProductTypeSecurity;
    private com.see.truetransact.uicomponent.CLabel lblRateOfInterest;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblRemarksSociety;
    private com.see.truetransact.uicomponent.CLabel lblResolutionDt;
    private com.see.truetransact.uicomponent.CLabel lblResolutionNo;
    private com.see.truetransact.uicomponent.CLabel lblRetirementDt;
    private com.see.truetransact.uicomponent.CLabel lblSalaryCertificateNo;
    private com.see.truetransact.uicomponent.CLabel lblSalaryRecovery;
    private com.see.truetransact.uicomponent.CLabel lblSalaryRemark;
    private com.see.truetransact.uicomponent.CLabel lblSameChittal;
    private com.see.truetransact.uicomponent.CLabel lblSchemeName;
    private com.see.truetransact.uicomponent.CLabel lblSecurityAmountSociety;
    private com.see.truetransact.uicomponent.CLabel lblSecurityNoSociety;
    private com.see.truetransact.uicomponent.CLabel lblSecurityRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSecurityType;
    private com.see.truetransact.uicomponent.CLabel lblSecurityTypeSociety;
    private com.see.truetransact.uicomponent.CLabel lblSecurityValue;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace40;
    private com.see.truetransact.uicomponent.CLabel lblSpace41;
    private com.see.truetransact.uicomponent.CLabel lblSpace42;
    private com.see.truetransact.uicomponent.CLabel lblSpace43;
    private com.see.truetransact.uicomponent.CLabel lblSpace44;
    private com.see.truetransact.uicomponent.CLabel lblSpace45;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmountTillDate;
    private com.see.truetransact.uicomponent.CLabel lblTotalDeposit;
    private com.see.truetransact.uicomponent.CLabel lblTotalDepositValue;
    private com.see.truetransact.uicomponent.CLabel lblTotalSalary;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransactionAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblValueOfGold;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
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
    private com.see.truetransact.uicomponent.CPanel panBtnDeposit;
    private com.see.truetransact.uicomponent.CPanel panBtnMemberType;
    private com.see.truetransact.uicomponent.CPanel panBtnOtherSociety;
    private com.see.truetransact.uicomponent.CPanel panCaseDetails;
    private com.see.truetransact.uicomponent.CPanel panCaseTableFields;
    private com.see.truetransact.uicomponent.CPanel panChargeDetails;
    private com.see.truetransact.uicomponent.CPanel panChargeTransfer;
    private com.see.truetransact.uicomponent.CPanel panChittalNo;
    private com.see.truetransact.uicomponent.CPanel panChittalNo1;
    private com.see.truetransact.uicomponent.CPanel panCollateralTypeDetails;
    private com.see.truetransact.uicomponent.CPanel panCustomerNO;
    private com.see.truetransact.uicomponent.CPanel panDepNo;
    private com.see.truetransact.uicomponent.CPanel panDepositDetails;
    private com.see.truetransact.uicomponent.CPanel panDepositTable;
    private com.see.truetransact.uicomponent.CPanel panDepositType;
    private com.see.truetransact.uicomponent.CPanel panGoldTypeDetails;
    private com.see.truetransact.uicomponent.CPanel panInsideMasterMaintenanceDetails;
    private com.see.truetransact.uicomponent.CPanel panInstallmentDetails;
    private com.see.truetransact.uicomponent.CPanel panLoanChargeDef;
    private com.see.truetransact.uicomponent.CPanel panMasterMaintenanceDetails;
    private com.see.truetransact.uicomponent.CPanel panMdsSchemeNameDetails;
    private com.see.truetransact.uicomponent.CPanel panMemberDetails;
    private com.see.truetransact.uicomponent.CPanel panMemberNo;
    private com.see.truetransact.uicomponent.CPanel panMemberNoDetails;
    private com.see.truetransact.uicomponent.CPanel panMemberNumber;
    private com.see.truetransact.uicomponent.CPanel panMemberTypeDetails;
    private com.see.truetransact.uicomponent.CPanel panMemberTypeTable;
    private com.see.truetransact.uicomponent.CPanel panNominee;
    private com.see.truetransact.uicomponent.CPanel panOtherSociety;
    private com.see.truetransact.uicomponent.CPanel panOtherSocietyDetails;
    private com.see.truetransact.uicomponent.CPanel panOtherSocietyTable;
    private com.see.truetransact.uicomponent.CPanel panPrizeDetails;
    private com.see.truetransact.uicomponent.CPanel panPrizedDetails;
    private com.see.truetransact.uicomponent.CPanel panProperty1;
    private com.see.truetransact.uicomponent.CPanel panSalaryDetails;
    private com.see.truetransact.uicomponent.CPanel panSalaryRecovery;
    private com.see.truetransact.uicomponent.CPanel panSalaryRecoveryValue;
    private com.see.truetransact.uicomponent.CPanel panSanctionDetails_Table1;
    private com.see.truetransact.uicomponent.CPanel panSchemeName;
    private com.see.truetransact.uicomponent.CPanel panSecurityDetails;
    private com.see.truetransact.uicomponent.CPanel panSecurityDetails1;
    private com.see.truetransact.uicomponent.CPanel panStanding;
    private com.see.truetransact.uicomponent.CPanel panStandingDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdgEFTProductGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgIsLapsedGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPayableBranchGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPrintServicesGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSalaryRecovery;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSeriesGR;
    private com.see.truetransact.uicomponent.CRadioButton rdoGoldSecurityExitsNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoGoldSecurityExitsYes;
    private com.see.truetransact.uicomponent.CRadioButton rdoSalaryRecovery_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoSalaryRecovery_Yes;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CScrollPane srpMemberType;
    private com.see.truetransact.uicomponent.CScrollPane srpOtherSociety;
    private com.see.truetransact.uicomponent.CScrollPane srpTableDeposit;
    private com.see.truetransact.uicomponent.CScrollPane srpTxtAreaParticulars;
    private com.see.truetransact.uicomponent.CTabbedPane tabMasterMaintenance;
    private com.see.truetransact.uicomponent.CTabbedPane tabMasterMaintenanceDetails;
    private com.see.truetransact.uicomponent.CTable tblDepositDetails;
    private com.see.truetransact.uicomponent.CTable tblMemberType;
    private com.see.truetransact.uicomponent.CTable tblOtherSocietyDetails;
    private com.see.truetransact.uicomponent.CTable tblSalaryDetails;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CDateField tdtApplicationDate;
    private com.see.truetransact.uicomponent.CDateField tdtBondDt;
    private com.see.truetransact.uicomponent.CDateField tdtChitCloseDt;
    private com.see.truetransact.uicomponent.CDateField tdtChitStartDt;
    private com.see.truetransact.uicomponent.CDateField tdtDepDt;
    private com.see.truetransact.uicomponent.CDateField tdtIssueDtSoceity;
    private com.see.truetransact.uicomponent.CDateField tdtLastInstDate;
    private com.see.truetransact.uicomponent.CDateField tdtMaturityDateSociety;
    private com.see.truetransact.uicomponent.CDateField tdtPayDt;
    private com.see.truetransact.uicomponent.CDateField tdtResolutionDt;
    private com.see.truetransact.uicomponent.CDateField tdtRetirementDt;
    private com.see.truetransact.uicomponent.CLabel txaApplicationSec;
    private com.see.truetransact.uicomponent.CTextArea txaApplicationSecRemarks;
    private com.see.truetransact.uicomponent.CTextField txtAddress;
    private com.see.truetransact.uicomponent.CTextField txtApplicationNo;
    private com.see.truetransact.uicomponent.CTextField txtBondNo;
    private com.see.truetransact.uicomponent.CTextField txtChittalNo;
    private com.see.truetransact.uicomponent.CTextField txtContactNo;
    private com.see.truetransact.uicomponent.CTextField txtContactNum;
    private com.see.truetransact.uicomponent.CTextField txtCustomerIdCr;
    private com.see.truetransact.uicomponent.CTextField txtDepAmount;
    private com.see.truetransact.uicomponent.CTextField txtDepNo;
    private com.see.truetransact.uicomponent.CTextField txtDesignation;
    private com.see.truetransact.uicomponent.CTextField txtDivisionNo;
    private com.see.truetransact.uicomponent.CTextField txtEmployerName;
    private com.see.truetransact.uicomponent.CTextField txtGoldRemarks;
    private com.see.truetransact.uicomponent.CTextField txtGoldSecurityId;
    private com.see.truetransact.uicomponent.CTextField txtGrossWeight;
    private com.see.truetransact.uicomponent.CTextArea txtJewelleryDetails;
    private com.see.truetransact.uicomponent.CTextField txtLastInstNo;
    private com.see.truetransact.uicomponent.CDateField txtMaturityDt;
    private com.see.truetransact.uicomponent.CTextField txtMaturityValue;
    private com.see.truetransact.uicomponent.CTextField txtMaturityValueSociety;
    private com.see.truetransact.uicomponent.CTextField txtMemName;
    private com.see.truetransact.uicomponent.CTextField txtMemNetworth;
    private com.see.truetransact.uicomponent.CTextField txtMemNo;
    private com.see.truetransact.uicomponent.CTextField txtMemPriority;
    private com.see.truetransact.uicomponent.CTextField txtMemType;
    private com.see.truetransact.uicomponent.CTextField txtMemberNo;
    private com.see.truetransact.uicomponent.CTextField txtMemberNum;
    private com.see.truetransact.uicomponent.CTextField txtNetWeight;
    private com.see.truetransact.uicomponent.CTextField txtNetWorth;
    private com.see.truetransact.uicomponent.CTextField txtOtherInstituionName;
    private com.see.truetransact.uicomponent.CTextField txtOverDueAmount;
    private com.see.truetransact.uicomponent.CTextField txtOverDueInstallments;
    private com.see.truetransact.uicomponent.CTextField txtPinCode;
    private com.see.truetransact.uicomponent.CTextField txtPrizedAmount;
    private com.see.truetransact.uicomponent.CTextField txtRateOfInterest;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtRemarksSociety;
    private com.see.truetransact.uicomponent.CTextField txtResolutionNo;
    private com.see.truetransact.uicomponent.CTextField txtSalaryCertificateNo;
    private com.see.truetransact.uicomponent.CTextField txtSalaryRemark;
    private com.see.truetransact.uicomponent.CTextField txtSchemeName;
    private com.see.truetransact.uicomponent.CTextField txtSecurityAmountSociety;
    private com.see.truetransact.uicomponent.CTextField txtSecurityNoSociety;
    private com.see.truetransact.uicomponent.CTextField txtSecurityRemarks;
    private com.see.truetransact.uicomponent.CTextField txtSecurityValue;
    private com.see.truetransact.uicomponent.CTextField txtSubNo;
    private com.see.truetransact.uicomponent.CTextField txtTotalAmountTillDate;
    private com.see.truetransact.uicomponent.CTextField txtTotalSalary;
    private com.see.truetransact.uicomponent.CTextField txtValueOfGold;
    // End of variables declaration//GEN-END:variables
    private com.see.truetransact.uicomponent.CPanel panAssetDescription;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CToolBar tbrTokenConfig;
    private com.see.truetransact.uicomponent.CLabel lblCaseNumber;
    private com.see.truetransact.uicomponent.CLabel lblCaseStatus;
    private com.see.truetransact.uicomponent.CLabel lblFillingDt;
    private com.see.truetransact.uicomponent.CLabel lblFillingFees;
    private com.see.truetransact.uicomponent.CLabel lblMiscCharges;
    private com.see.truetransact.uicomponent.CButton btnCaseDelete;
    private com.see.truetransact.uicomponent.CButton btnCaseNew;
    private com.see.truetransact.uicomponent.CButton btnCaseSave;
    private com.see.truetransact.uicomponent.CComboBox cboCaseStatus;
    private com.see.truetransact.uicomponent.CDateField tdtlFillingDt;
    private com.see.truetransact.uicomponent.CTextField txtCaseNumber;
    private com.see.truetransact.uicomponent.CTextField txtFillingFees;
    private com.see.truetransact.uicomponent.CTextField txtMiscCharges;
    private com.see.truetransact.uicomponent.CPanel panCaseDetailBtn;
    private com.see.truetransact.uicomponent.CPanel panCaseTableFieldsDetails;
    private com.see.truetransact.uicomponent.CPanel panScheduleTable;
    private com.see.truetransact.uicomponent.CScrollPane srpCaseTable;
    private com.see.truetransact.uicomponent.CTable tblCaseTable;
    //Security Type
    private com.see.truetransact.uicomponent.CButton btnCollateralDelete;
    private com.see.truetransact.uicomponent.CButton btnCollateralNew;
    private com.see.truetransact.uicomponent.CButton btnCollateralSave;
    private com.see.truetransact.uicomponent.CButton btnSalaryDelete;
    private com.see.truetransact.uicomponent.CButton btnSalaryNew;
    private com.see.truetransact.uicomponent.CButton btnSalarySave;
    private com.see.truetransact.uicomponent.CComboBox cboNature;
    private com.see.truetransact.uicomponent.CLabel lblDocumentDate;
    private com.see.truetransact.uicomponent.CLabel lblDocumentNo;
    private com.see.truetransact.uicomponent.CLabel lblDocumentType;
    private com.see.truetransact.uicomponent.CLabel lblGahanYesNo;
    private com.see.truetransact.uicomponent.CPanel panGahanYesNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoGahanYes;
    private com.see.truetransact.uicomponent.CRadioButton rdoGahanNo;
    private com.see.truetransact.uicomponent.CButtonGroup rdoGahanGroup;
    private com.see.truetransact.uicomponent.CPanel panOwnerMemberNumber;
    private com.see.truetransact.uicomponent.CPanel panDocumentNumber;
    private com.see.truetransact.uicomponent.CLabel lblNature;
    private com.see.truetransact.uicomponent.CLabel lblNetWorth1;
    private com.see.truetransact.uicomponent.CLabel lblOwnerMemberNname;
    private com.see.truetransact.uicomponent.CLabel lblOwnerMemberNo;
    private com.see.truetransact.uicomponent.CLabel lblPledge;
    private com.see.truetransact.uicomponent.CLabel lblPledgeAmount;
    private com.see.truetransact.uicomponent.CLabel lblPledgeDate;
    private com.see.truetransact.uicomponent.CLabel lblPledgeNo;
    private com.see.truetransact.uicomponent.CLabel lblRegisteredOffice;
    private com.see.truetransact.uicomponent.CLabel lblSurveyNo;
    private com.see.truetransact.uicomponent.CLabel lblTotalArea;
    private com.see.truetransact.uicomponent.CLabel lblVillage;
    private com.see.truetransact.uicomponent.CLabel lblPledgeType;
    private com.see.truetransact.uicomponent.CTextField txtPledgeType;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private com.see.truetransact.uicomponent.CPanel panBtnCollateralType;
    private com.see.truetransact.uicomponent.CPanel panBtnSalaryType;
    private com.see.truetransact.uicomponent.CPanel panCollateralTable;
    private com.see.truetransact.uicomponent.CPanel panCollateralJointTable;
    private com.see.truetransact.uicomponent.CPanel panCollateralTypeDetails1;
    private com.see.truetransact.uicomponent.CPanel panCollatetalDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpCollateralTable;
    private com.see.truetransact.uicomponent.CScrollPane srpCollateralJointTable;
    private com.see.truetransact.uicomponent.CTable tblCollateral;
    private com.see.truetransact.uicomponent.CTable tblJointCollateral;
    private com.see.truetransact.uicomponent.CTextField txtDocumentNo;
    private com.see.truetransact.uicomponent.CComboBox cboDocumentType;
    private com.see.truetransact.uicomponent.CTextField txtRegisteredOffice;
    private com.see.truetransact.uicomponent.CTextField txtOwnerMemberNname;
    private com.see.truetransact.uicomponent.CComboBox cboPledge;
    private com.see.truetransact.uicomponent.CTextField txtPledgeAmount;
    private com.see.truetransact.uicomponent.CTextField txtPledgeNo;
    private com.see.truetransact.uicomponent.CTextField txtOwnerMemNo;
    private com.see.truetransact.uicomponent.CButton btnOwnerMemNo;
    private com.see.truetransact.uicomponent.CButton btnDocumentNo;
    private com.see.truetransact.uicomponent.CComboBox cboRight;
    private com.see.truetransact.uicomponent.CDateField tdtDocumentDate;
    private com.see.truetransact.uicomponent.CDateField tdtPledgeDate;
    private com.see.truetransact.uicomponent.CTextField txtVillage;
    private com.see.truetransact.uicomponent.CTextField txtSurveyNo;
    private com.see.truetransact.uicomponent.CLabel lblRight;
    private com.see.truetransact.uicomponent.CTextField txtTotalArea;
    private com.see.truetransact.uicomponent.CTextArea txtAreaParticular;

    private void tblCollateralMousePressed(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        updateMode = true;
        updateTab = tblCollateral.getSelectedRow();
        observable.setCollateralTypeData(false);
        String st = CommonUtil.convertObjToStr(tblCollateral.getValueAt(tblCollateral.getSelectedRow(), 0));
        //observable.populateCollateralDetails(st);
        observable.populateCollateralDetails(st+"_"+(updateTab+1));
        populateCollateralFields();
        collateralJointAccountDisplay(txtOwnerMemNo.getText());
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            btnSecurityCollateral(false);
            ClientUtil.enableDisable(panCollatetalDetails, false);
        } else {
            btnSecurityCollateral(true);
            ClientUtil.enableDisable(panCollatetalDetails, true);
            btnCollateralNew.setEnabled(false);
        }
        txtOwnerMemNo.setEnabled(false);
        txtOwnerMemberNname.setEnabled(false);
        btnCollateralNew.setEnabled(true);
        System.out.println("iiiiiiiiiiiiii"+CommonUtil.convertObjToStr(tblCollateral.getValueAt(tblCollateral.getSelectedRow(), 4)));
        observable.setOldSurvyNo(CommonUtil.convertObjToStr(tblCollateral.getValueAt(tblCollateral.getSelectedRow(), 4)));
    }

    public void populateCollateralFields() {
        if (observable.isRdoGahanYes()) {
            rdoGahanYes.setSelected(observable.isRdoGahanYes());
        } else {
            rdoGahanNo.setSelected(observable.isRdoGahanNo());
        }
        txtOwnerMemNo.setText(observable.getTxtOwnerMemNo());
        txtOwnerMemberNname.setText(observable.getTxtOwnerMemberNname());
        txtDocumentNo.setText(observable.getTxtDocumentNo());
        cboDocumentType.setSelectedItem(observable.getCboDocumentType());
        tdtDocumentDate.setDateValue(observable.getTdtDocumentDate());
        txtRegisteredOffice.setText(observable.getTxtRegisteredOffice());
        cboPledge.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboPledge()));
        tdtPledgeDate.setDateValue(observable.getTdtPledgeDate());
        txtPledgeNo.setText(observable.getTxtPledgeNo());
        txtPledgeAmount.setText(observable.getTxtPledgeAmount());
        txtVillage.setText(observable.getTxtVillage());
        txtSurveyNo.setText(observable.getTxtSurveyNo());
        txtTotalArea.setText(observable.getTxtTotalArea());
        cboNature.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboNature()));
        cboRight.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboRight()));
        txtAreaParticular.setText(observable.getTxtAreaParticular());
    }

    private void rdoGahanYesActionPerformed(java.awt.event.ActionEvent evt) {
        //system.out.println("rdoGahanYesActionPerformed   ");
        if (rdoGahanYes.isSelected()) {
            ClientUtil.enableDisable(panCollatetalDetails, false);
            btnDocumentNo.setEnabled(true);
            txtDocumentNo.setEnabled(true);
            btnOwnerMemNo.setEnabled(false);
            panGahanYesNo.setEnabled(true);
            txtPledgeAmount.setEnabled(true);
            ClientUtil.enableDisable(panGahanYesNo, true);
            //Added By Suresh
            txtOwnerMemNo.setEnabled(true);
            btnOwnerMemNo.setEnabled(true);
            resetCollateralDetails();
        } else {
            btnDocumentNo.setEnabled(false);
            btnOwnerMemNo.setEnabled(true);
            ClientUtil.enableDisable(panCollatetalDetails, true);
        }
    }

    private void resetCollateralDetails() {
        txtOwnerMemNo.setText("");
        txtOwnerMemberNname.setText("");
        txtDocumentNo.setText("");
        cboDocumentType.setSelectedItem("");
        tdtDocumentDate.setDateValue("");
        txtRegisteredOffice.setText("");
        cboPledge.setSelectedItem("");
        tdtPledgeDate.setDateValue("");
        txtPledgeNo.setText("");
        txtPledgeAmount.setText("");
        txtVillage.setText("");
        txtSurveyNo.setText("");
        txtTotalArea.setText("");
        cboNature.setSelectedItem("");
        cboRight.setSelectedItem("");
        txtAreaParticular.setText("");
    }

    private void rdoGahanNoActionPerformed(java.awt.event.ActionEvent evt) {
        //system.out.println("rdoGahanNoActionPerformed   ");
        if (rdoGahanNo.isSelected()) {
            //comm by jiby
//            if (tblCollateral.getRowCount() != 0) {
//                ClientUtil.displayAlert("Delete All Gahan Records From the Table then Select Gahan No Option");
//                rdoGahanYes.setSelected(true);
//                return;
//            }
            ///////////////
            ClientUtil.enableDisable(panCollatetalDetails, true);
            btnDocumentNo.setEnabled(false);
            btnOwnerMemNo.setEnabled(true);
            ClientUtil.enableDisable(panGahanYesNo, true);
            resetCollateralDetails();
            
          } else {
            btnDocumentNo.setEnabled(true);
            txtDocumentNo.setEnabled(true);
            btnOwnerMemNo.setEnabled(false);
            ClientUtil.enableDisable(panCollatetalDetails, false);
            rdoGahanYes.setEnabled(true);
        }
    }

    private void btnDocumentNoActionPerformed(java.awt.event.ActionEvent evt) {
        //system.out.println("btnDocumentNoActionPerformed   ");
        popUp("DOCUMENT_NO");
    }

    private void txtDocumentNoFocusLost(java.awt.event.FocusEvent evt) {
        HashMap docMap = new HashMap();
        String docNo = CommonUtil.convertObjToStr(txtDocumentNo.getText());
        docMap.put("DOCUMENT_NUMBER", docNo);
        if (rdoGahanYes.isSelected()) {
            List lst = ClientUtil.executeQuery("getGahanDetailsforLoan", docMap);
            if (lst != null && lst.size() > 0) {
                viewType = "DOCUMENT_NO";
                HashMap resultDocument = (HashMap) lst.get(0);
                fillData(resultDocument);
            } else {
                ClientUtil.displayAlert("Invalid Document No");
                return;
            }
        } else if (rdoGahanNo.isSelected()) {
            List lst = ClientUtil.executeQuery("getGahanAvailableOrNot", docMap);
            if (lst != null && lst.size() > 0) {
                ClientUtil.displayAlert("Document Number already available in Gahan Details" + "\n" + "Enter different document no");
            }
            return;
        }
    }

    private void collateralJointAccountDisplay(String memNo) {
        observable.updateCollateralJointDetails(memNo);
        tblJointCollateral.setModel(observable.getTblJointCollateral());
    }

    private void btnCollateralSaveActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        try {
            if (txtOwnerMemNo.getText().length() == 0) {
                ClientUtil.showAlertWindow("OwnerMember Number should not be empty");
             }
            else if(txtSurveyNo.getText().length()==0)
            {
                ClientUtil.showAlertWindow("Survey Number should not be empty");
            }
            else {
                if(observable.getOldSurvyNo()==null ||observable.getOldSurvyNo().equals(""))
            {
                System.out.println("getOldSurvyNo"); 
                observable.setOldSurvyNo(txtSurveyNo.getText().toString());
            }
                updateCollateralFields();
                rowCount=tblCollateral.getRowCount();
                System.out.println("rowCount innnnnnnnn"+rowCount+"tblCollateral.getSelectedRow()"+tblCollateral.getSelectedRow()+"btnNew.isEnabled()"+btnNew.isEnabled());
               
                if(tblCollateral.getSelectedRow()>=0 && (btnCollateralNew.isEnabled()))
                {
                    System.out.println("kiiiii");
                   rowCount= tblCollateral.getSelectedRow()+1;
                  
                }
                else
                {
                    System.out.println("miiiii");
                if(rowCount==0)
                {
                    rowCount=1;
                }
                else
                {
                    rowCount=rowCount+1;
                }
                }
                //.out.println("rowCount====="+rowCount);
                observable.setRowCoun(rowCount);
                observable.addCollateralTable(updateTab, updateMode);
                tblCollateral.setModel(observable.getTblCollateralDetails());
                observable.resetCollateralDetails();
                resetCollateralDetails();
                ClientUtil.enableDisable(panCollatetalDetails, false);
                btnSecurityCollateral(false);
                btnCollateralNew.setEnabled(true);
                btnOwnerMemNo.setEnabled(false);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCollateralFields() {
        observable.setRdoGahanYes(rdoGahanYes.isSelected());
        observable.setRdoGahanNo(rdoGahanNo.isSelected());
        observable.setTxtOwnerMemNo(txtOwnerMemNo.getText());
        observable.setTxtOwnerMemberNname(txtOwnerMemberNname.getText());
        observable.setTxtDocumentNo(txtDocumentNo.getText());
        observable.setCboDocumentType(CommonUtil.convertObjToStr(cboDocumentType.getSelectedItem()));
        observable.setTdtDocumentDate(tdtDocumentDate.getDateValue());
        observable.setTxtRegisteredOffice(txtRegisteredOffice.getText());
        observable.setCboPledge(CommonUtil.convertObjToStr(cboPledge.getSelectedItem()));
        observable.setTxtPledgeType(txtPledgeType.getText());
        observable.setTdtPledgeDate(tdtPledgeDate.getDateValue());
        observable.setTxtPledgeNo(txtPledgeNo.getText());
        observable.setTxtPledgeAmount(txtPledgeAmount.getText());
        observable.setTxtVillage(txtVillage.getText());
        observable.setTxtSurveyNo(txtSurveyNo.getText());
        observable.setTxtTotalArea(txtTotalArea.getText());
        observable.setCboNature(CommonUtil.convertObjToStr(cboNature.getSelectedItem()));
        observable.setCboRight(CommonUtil.convertObjToStr(cboRight.getSelectedItem()));
        observable.setTxtAreaParticular(txtAreaParticular.getText());
    }

    private void btnCollateralNewActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (txtSchemeName.getText().length() > 0 && txtChittalNo.getText().length() > 0) {
            HashMap MdsDetailsMap = new HashMap();
            MdsDetailsMap.put("CHITTAL_NO", txtChittalNo.getText());
            MdsDetailsMap.put("SCHEME_NAME", txtSchemeName.getText());
            MdsDetailsMap.put("SUB_NO", CommonUtil.convertObjToInt(txtSubNo.getText()));    //AJITH
            List lst = ClientUtil.executeQuery("getSelectRecordDetails", MdsDetailsMap);
            if (lst != null && lst.size() > 0) {
                updateMode = false;
                observable.setCollateralTypeData(true);
                btnSecurityCollateral(false);
                btnCollateralSave.setEnabled(true);
                if (rdoGahanYes.isSelected()) {
                    rdoGahanYesActionPerformed(null);
                }
                ClientUtil.enableDisable(panGahanYesNo, true);
                txtOwnerMemberNname.setEnabled(false);
                btnOwnerMemNo.setEnabled(false);
            } else {

                updateMode = false;
                observable.setCollateralTypeData(true);
                btnSecurityCollateral(false);
                btnCollateralSave.setEnabled(true);
                if (rdoGahanYes.isSelected()) {
                    rdoGahanYesActionPerformed(null);
                }
                if(rdoGahanNo.isSelected()){
            rdoGahanNoActionPerformed(null);
            //btnOwnerMemNo.setEnabled(true);
            btnOwnerMemNo.setEnabled(true);
        System.out.println("updateMode in collateral new"+updateMode);
        observable.setOldSurvyNo("");
        
        }
                ClientUtil.enableDisable(panGahanYesNo, true);
                txtOwnerMemberNname.setEnabled(false);
                btnOwnerMemNo.setEnabled(false);
                // ClientUtil.showMessageWindow("Please Enter Prized Money Details for This Chittal !!!");
            }
             if(rdoGahanNo.isSelected()){  
                 rdoGahanNoActionPerformed(null);        
                 btnOwnerMemNo.setEnabled(true);     
             }
             updateTab=-1;
        } else {
            ClientUtil.showMessageWindow("Please Select Scheme Name And Chittal No !!!");
        }
    }

    private void txtPledgeAmountFocusLost(java.awt.event.FocusEvent evt) {
        //Uncommented by Jeffin John on 21Jan15
        if (rdoGahanYes.isSelected() && txtPledgeAmount.getText().length() > 0) {
            HashMap resultMap = observable.validatePledgeAmount(CommonUtil.convertObjToStr(txtDocumentNo.getText()), CommonUtil.convertObjToDouble(txtPledgeAmount.getText()).doubleValue());
            if (resultMap != null && resultMap.size() > 0) {
                ClientUtil.displayAlert("Pledge Amount Should not Exceed available Secuirty value");
                txtPledgeAmount.setText(CommonUtil.convertObjToStr(resultMap.get("PLEDGE_AMT")));
            }
        }
    }

    private void btnCollateralDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        int selRow=tblCollateral.getSelectedRow();
        String s = CommonUtil.convertObjToStr(tblCollateral.getValueAt(tblCollateral.getSelectedRow(), 0));
        observable.deleteCollateralTableData(s+"_"+(selRow+1), tblCollateral.getSelectedRow());
        //observable.deleteCollateralTableData(s, tblCollateral.getSelectedRow());
        updateTab = -1;
        observable.resetCollateralDetails();
        resetCollateralDetails();
        ClientUtil.enableDisable(panCollatetalDetails, false);
        btnSecurityCollateral(false);
        btnCollateralNew.setEnabled(true);
    }

    private void txtOwnerMemNoFocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        if (txtOwnerMemNo.getText().length() > 0) {
            HashMap listMap = new HashMap();
            listMap.put("MEMBERSHIP_NO", txtOwnerMemNo.getText());
            java.util.List lst = ClientUtil.executeQuery("getMemeberShipDetails", listMap);
            if (lst != null && lst.size() > 0) {
                listMap = (HashMap) lst.get(0);
                viewType = "OWNER_MEMBER_NO";
                fillData(listMap);
            } else {
                ClientUtil.showAlertWindow("Invalid Member No");
                resetCollateralDetails();
                observable.setTxtOwnerMemNo("");
            }
        }
    }

    private void btnOwnerMemNoActionPerformed(java.awt.event.ActionEvent evt) {
        popUp("OWNER_MEMBER_NO");
    }
    
      private double checkAvailableGoldStockSecurity(String goldSecurityId) {
        HashMap map = new HashMap();
        double availableSecurity = 0;
        if (goldSecurityId != null) {
            map.put("GOLD_SECURITY_ID", goldSecurityId);
            List lst = ClientUtil.executeQuery("getGoldStockAvailableSecurityforLoan", map);
            if (lst != null && lst.size() > 0) {
                map = (HashMap) lst.get(0);
                availableSecurity = CommonUtil.convertObjToDouble(map.get("AVAILABLE_SECURITY_VALUE")).doubleValue();
            }
        }
        return availableSecurity;
    }
     
     public double getGoldStockPldgeAmountForLoan(String goldStockId) {
        Double gahanForLn = 0.0;
        HashMap mapDocGenId = new HashMap();
        mapDocGenId.put("GOLD_SECURITY_ID", goldStockId);
        List lstGahanForLn = ClientUtil.executeQuery("getSelectGoldStockExistsForLoan", mapDocGenId);
        if (!lstGahanForLn.isEmpty() && !lstGahanForLn.equals(null)) {
            mapDocGenId = new HashMap();
            mapDocGenId = (HashMap) lstGahanForLn.get(0);
            gahanForLn = CommonUtil.convertObjToDouble(mapDocGenId.get("PLEDGE_AMOUNT"));
        }
        return gahanForLn;
    }
     
     public double getGoldStockPldgeAmtForMds(String goldSecurityId) {
        Double goldForMDS = 0.0;
        HashMap mapgoldSecurityId = new HashMap();
        mapgoldSecurityId.put("GOLD_SECURITY_ID", goldSecurityId);
        List lstGoldSecurityForMds = ClientUtil.executeQuery("getSelectGoldStockExistsForMds", mapgoldSecurityId);
        if (!lstGoldSecurityForMds.isEmpty() && !lstGoldSecurityForMds.equals(null)) {
            mapgoldSecurityId = new HashMap();
            mapgoldSecurityId = (HashMap) lstGoldSecurityForMds.get(0);
            goldForMDS = CommonUtil.convertObjToDouble(mapgoldSecurityId.get("AMOUNT"));
        }
        return goldForMDS;
    }
     
    private double getGoldStockAvailableSecurity(double maxsecurityAmt) {
        double availableSecuirty = 0;
        double sumGahanTableValue = 0;
        double loanAmt = 0;        
        sumGahanTableValue += CommonUtil.convertObjToDouble(txtValueOfGold.getText());          
        loanAmt = CommonUtil.convertObjToDouble(txtPrizedAmount.getText()).doubleValue();
        availableSecuirty = loanAmt - sumGahanTableValue;
        return maxsecurityAmt;
    } 
    
    private void UpdateCalculatedGoldSecurityValue(HashMap map) {
        System.out.println("map####" + map);
        double pledgeAmt = 0;
        double actualPledge = 0;
        List lst = ClientUtil.executeQuery("getUnAuthGoldStockSecurityDetails", map);
        if (lst != null && lst.size() > 0) {
            ClientUtil.displayAlert("Already record is pending for authorization for this member");
            return;
        }        
        actualPledge = checkAvailableGoldStockSecurity(CommonUtil.convertObjToStr(map.get("GOLD_SECURITY_ID")));        
        Double goldLnAmt = getGoldStockPldgeAmountForLoan(CommonUtil.convertObjToStr(map.get("GOLD_SECURITY_ID")));        
        actualPledge = actualPledge - goldLnAmt;
        Double goldMdsAmt = getGoldStockPldgeAmtForMds(CommonUtil.convertObjToStr(map.get("GOLD_SECURITY_ID")));        
        actualPledge = actualPledge - goldMdsAmt;
        pledgeAmt = getGoldStockAvailableSecurity(actualPledge);        
        double sanAmt = getAmountForSecurity();
        double totalSecurity = calculateSecurityAmount();
        sanAmt = sanAmt - totalSecurity;
        if (sanAmt < pledgeAmt) {
            txtValueOfGold.setText(String.valueOf(sanAmt));
        } else {
            txtValueOfGold.setText(String.valueOf(pledgeAmt));
        }       
    }
    
     private void enableDisableGoldType(boolean flag) {
        txtJewelleryDetails.setEnabled(true);
        txtGoldRemarks.setEnabled(flag);
        txtValueOfGold.setEnabled(flag);
        txtNetWeight.setEnabled(flag);
        txtGrossWeight.setEnabled(flag);
        btnGoldSecurityIdSearch.setEnabled(flag);
        rdoGoldSecurityExitsYes.setEnabled(true);
        rdoGoldSecurityExitsNo.setEnabled(true);
    }
  
    private double calculateSecurityAmount() {
        double netWorth = 0.0;
        double memNetWorth = 0.0;
        double pledgeAmount = 0.0;
        double losAmount = 0.0;
        double vehicleNetworth = 0.0;
        HashMap hashmap = new HashMap();
        double depAmount = 0.0;
        for (int i = 0; i < tblSalaryDetails.getRowCount(); i++) {
            netWorth = netWorth + CommonUtil.convertObjToDouble(tblSalaryDetails.getValueAt(i, 5)).doubleValue();
        }
        for (int j = 0; j < tblMemberType.getRowCount(); j++) {
            memNetWorth = memNetWorth + CommonUtil.convertObjToDouble(tblMemberType.getValueAt(j, 4)).doubleValue();
        }
        for (int k = 0; k < tblCollateral.getRowCount(); k++) {
            pledgeAmount = pledgeAmount + CommonUtil.convertObjToDouble(tblCollateral.getValueAt(k, 3)).doubleValue();
        }
        for (int l = 0; l < tblDepositDetails.getRowCount(); l++) {
            String depNo = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(l, 1));
            String prodtype = CommonUtil.convertObjToStr(tblDepositDetails.getValueAt(l, 0));
            HashMap hmap = new HashMap();
            hmap.put("DEPOSIT_NO", depNo);
            if (prodtype.equals("TD") || prodtype.equals("Deposits")) {
                List lst = ClientUtil.executeQuery("getAvailableBalForDep", hmap);
                if (lst != null && lst.size() > 0) {
                    depAmount = depAmount + CommonUtil.convertObjToDouble(tblDepositDetails.getValueAt(l, 3)).doubleValue();
                }
            } else {
                depAmount = depAmount + CommonUtil.convertObjToDouble(tblDepositDetails.getValueAt(l, 3)).doubleValue();
            }

        }
        for (int m = 0; m < tblOtherSocietyDetails.getRowCount(); m++) {
            losAmount = losAmount + CommonUtil.convertObjToDouble(tblOtherSocietyDetails.getValueAt(m, 4)).doubleValue();
        }
        //double tot = netWorth + memNetWorth + pledgeAmount + depAmount + losAmount + vehicleNetworth;
        double tot = netWorth + memNetWorth + pledgeAmount + depAmount + losAmount;
        return tot;
    }

    
    private double getAmountForSecurity() {
        HashMap mapProd = new HashMap();        
        double amountForSecurityCalculation = 0.0;
        mapProd.put("PROD_ID", observable.getTxtProductId());
        List lstPrizdPend = ClientUtil.executeQuery("getSelPrizdPend", mapProd);
        if (lstPrizdPend.size() > 0 && !lstPrizdPend.isEmpty()) {
            mapProd = new HashMap();
            mapProd = (HashMap) lstPrizdPend.get(0);
        }
        String securityForPrizedAmount = CommonUtil.convertObjToStr(mapProd.get("SECURITY_PRIZED"));
        String securityForPendingAmount = CommonUtil.convertObjToStr(mapProd.get("SECURITY_PENDING"));
        HashMap mdsMap = new HashMap();
        String chittalNo = txtChittalNo.getText();
        //String subNo = txtSubNo.getText();    //AJITH Blocked on 13/08/2021
        Integer subNo = CommonUtil.convertObjToInt(txtSubNo.getText()); //AJITH New Line on 13/08/2021
        mdsMap.put("CHITTAL_NO", chittalNo);
        mdsMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
        double noinset = 0.0;
        double instAmt = 0.0;
        double paidInst = 0.0;
        double totAmt = 0.0;
        List mdsList = ClientUtil.executeQuery("getTotalInstAmount", mdsMap);
        List mdsList1 = ClientUtil.executeQuery("getSelctApplnReceiptDetails", mdsMap);
        if (mdsList1 != null && mdsList1.size() > 0) {
            mdsMap = (HashMap) mdsList1.get(0);
            noinset = CommonUtil.convertObjToDouble(mdsMap.get("NO_OF_INSTALLMENTS")).doubleValue();
            instAmt = CommonUtil.convertObjToDouble(mdsMap.get("INST_AMT")).doubleValue();
            totAmt = instAmt * noinset;
        }
        if (mdsList != null && mdsList.size() > 0) {
            mdsMap = (HashMap) mdsList.get(0);
            paidInst = CommonUtil.convertObjToDouble(mdsMap.get("NO_INST_PAID")).doubleValue();
            paidInst = paidInst * instAmt;
        }
        double tobepaidInst = totAmt - paidInst;
        Double prizedAmount = CommonUtil.convertObjToDouble(txtPrizedAmount.getText().toString());
        if (securityForPrizedAmount.equals("Y")) {
            amountForSecurityCalculation = prizedAmount;
        } else if (securityForPendingAmount.equals("Y")) {
            amountForSecurityCalculation = tobepaidInst;
        } else {
            if (prizedAmount < paidInst) {
                amountForSecurityCalculation = prizedAmount;
            } else {
                amountForSecurityCalculation = tobepaidInst;
            }           
        }
       return amountForSecurityCalculation; 
    }

    private void btnSecurityCollateral(boolean flag) {
        btnCollateralNew.setEnabled(flag);
        btnCollateralSave.setEnabled(flag);
        btnCollateralDelete.setEnabled(flag);
    }

    private void addGahanRadioBtns() {
        rdoGahanGroup = new CButtonGroup();
        rdoGahanGroup.add(rdoGahanYes);
        rdoGahanGroup.add(rdoGahanNo);
    }

    private void removeGahanRadioButton() {
        rdoGahanGroup.remove(rdoGahanYes);
        rdoGahanGroup.remove(rdoGahanNo);

    }

    public static void main(String[] arg) {
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        MDSMasterMaintenanceUI gui = new MDSMasterMaintenanceUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }
}
