/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSMasterMaintenanceOB.java
 *
 * Created on Mon Jun 13 18:24:58 IST 2011
 */

package com.see.truetransact.ui.mdsapplication.mdsmastermaintenance;


import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.common.nominee.NomineeTO;
import com.see.truetransact.transferobject.mdsapplication.mdsmastermaintenance.*;
import com.see.truetransact.transferobject.termloan.LoansSecurityGoldStockTO;
import com.see.truetransact.transferobject.termloan.TermLoanCaseDetailTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.nominee.NomineeOB;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.uicomponent.CObservable;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author
 */

public class MDSMasterMaintenanceOB extends CObservable{
    
    private String txtRemarks = "";
    private Double txtOverDueAmount = 0.0; //AJITH
    private Double txtTotalAmountTillDate = 0.0; //AJITH
    private String txtLastInstNo = "";
    private Integer txtOverDueInstallments = 0; //AJITH
    private String tdtLastInstDate = "";
    private String txtChittalNo = "";
    private String tdtChitStartDt = "";
    private String tdtChitCloseDt = "";
    private Integer txtDivisionNo = 0;   //AJITH
    private Integer txtSubNo = 0;   //AJITH
    private String txtSchemeName = "";
    private String txtMemberNo = "";
    private String txtBordNo = "";
    private String tdtBordDt = "";
    private Double txtPrizedAmount = 0.0;  //AJITH
    private String tdtResolutionDt = "";
    private String tdtPayDt = "";
    private String txtResolutionNo = "";
    private String txtContactNo = "";
    private String txtMemberNum = "";
    private String txtSalaryRemark = "";
    private String txtDesignation = "";
    private String txtAddress = "";
    private String txtEmployerName = "";
    private String txtSalaryCertificateNo = "";
    private Double txtTotalSalary = 0.0;  //AJITH
    private String txtNetWorth = "";
    private Integer txtPinCode = 0;  //AJITH
    private String cboCity = "";
    private String tdtRetirementDt = "";
    private String txtMemNetworth = "";
    private String txtMemPriority = "";
    private String txtContactNum = "";
    private String txtMemType = "";
    private String txtMemName = "";
    private String txtMemNo = "";
    private String txtSecurityRemarks = "";
    private String cboSecurityType = "";
    private String txtGoldRemarks = "";
    private String txtValueOfGold = "";
    private Double txtNetWeight = 0.0;  //AJITH
    private Double txtGrossWeight = 0.0;    //AJITH
    private String txtJewelleryDetails = "";
    private String txtDepAmount = "";
    private String txtMaturityDt = "";
    private String txtMaturityValue = "";
    private String txtProductId = "";
    private String txtRateOfInterest = "";
    private String txtDepNo = "";
    private String cbxDefaulter="";
    private String oldSurvyNo="";
    HashMap finalChrageMap =null;
    private String userDefinedAuction = "N"; //Added by nithya on 23-12-2019 for KD-1074
    private String rdoGoldSecurityStockExists = "N"; // Added by nithya on 07-03-2020 for KD-1379
    private String txtGoldSecurityId = "";

    public HashMap getFinalChrageMap() {
        return finalChrageMap;
    }

    public void setFinalChrageMap(HashMap finalChrageMap) {
        this.finalChrageMap = finalChrageMap;
    }

    public String getOldSurvyNo() {
        return oldSurvyNo;
    }

    public void setOldSurvyNo(String oldSurvyNo) {
        this.oldSurvyNo = oldSurvyNo;
    }
    private int rowCoun;

    public int getRowCoun() {
        return rowCoun;
    }

    public void setRowCoun(int rowCoun) {
        this.rowCoun = rowCoun;
    }


    public String getApplicationSecurityRemarks() {
        return applicationSecurityRemarks;
    }

    public void setApplicationSecurityRemarks(String applicationSecurityRemarks) {
        this.applicationSecurityRemarks = applicationSecurityRemarks;
    }
    private String cbxOnlyAppliction="";
    private String applicationSet="";
    private String applicationSecurityRemarks="";
    public String getApplicationSet() {
        return applicationSet;
    }

    public void setApplicationSet(String applicationSet) {
        this.applicationSet = applicationSet;
    }
    public String getCbxOnlyAppliction() {
        return cbxOnlyAppliction;
    }

    public void setCbxOnlyAppliction(String cbxOnlyAppliction) {
        this.cbxOnlyAppliction = cbxOnlyAppliction;
    }
    
    public String getCbxDefaulter() {
        return cbxDefaulter;
    }

    public void setCbxDefaulter(String cbxDefaulter) {
        this.cbxDefaulter = cbxDefaulter;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }
    private String applicationNo="";
    private String applicationDate="";

    public String getBondNoSet() {
        return bondNoSet;
    }

    public void setBondNoSet(String bondNoSet) {
        this.bondNoSet = bondNoSet;
    }
    private String tdtDepDt = "";
    private String bondNoSet="";
   //
    private String cbxSamechittal="";
    private ComboBoxModel cbmCity,cbmSecurity;
    private String lblMemberName = "";
    private String lblMemberType = "";
    private String lblNomineeName = "";
    private String lblSecurityValue = "";
    private boolean chkStandingInstn = false;
    private boolean chkNominee = false;
    private ComboBoxModel cbmProdType;
    private ComboBoxModel cbmProdTypeSecurity;
    private ComboBoxModel cbmProdId;
    private String txtCustomerIdCr = "";
    private String rdoSalaryRecovery = "";
    private String lockStatus = "";
    private boolean memberTypeData = false;
    private boolean depositTypeData = false;
    private ComboBoxModel cbmDepProdID;
    private String cboDepProdID = "";
    private String cboProductTypeSecurity = "";
    private EnhancedTableModel tblMemberTypeDetails;
    private EnhancedTableModel tblDepositTypeDetails;
    final ArrayList tableTitleDepositList = new ArrayList();
    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    Date curDate = null;
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(MDSMasterMaintenanceOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap _authorizeMap;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int result;
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    private MDSMasterMaintenanceTO objMaintenanceTO;
    private MDSMemberTypeTO objMemberTypeTO;
    private MDSDepositTypeTO objDepositTypeTO;
    private MDSSocietyTypeTO objMDSSocietyTypeTO;
    private LinkedHashMap memberTypeMap;
    private LinkedHashMap deletedMemberTypeMap;
    private LinkedHashMap depositTypeMap;
    private LinkedHashMap deletedDepositTypeMap;
    
    //Security Details
    private ComboBoxModel cbmNature;
    private ComboBoxModel cbmRight;
    private ComboBoxModel cbmPledge;
    private ComboBoxModel cbmDocumentType;
    private String txtOwnerMemNo = "";
    private String txtOwnerMemberNname = "";
    private String txtDocumentNo = "";
    private String txtDocumentType = "";
    private String tdtDocumentDate = "";
    private String txtRegisteredOffice = "";
    private String cboPledge = "";
    private String tdtPledgeDate = "";
    private String txtPledgeNo = "";
    private String txtPledgeAmount = "";
    private String txtVillage = "";
    private String txtSurveyNo = "";
    private String txtTotalArea = "";
    private String cboNature = "";
    private String cboRight = "";
    private String cboDocumentType="";
    private String txtAreaParticular = "";
    private String txtPledgeType="";
    private String docGenId="";
    private boolean rdoGahanYes=false;
    private boolean rdoGahanNo=false;
    private boolean collateralTypeData = false;
    private EnhancedTableModel tblCollateralDetails;
    private EnhancedTableModel tblJointCollateral;
    private HashMap pledgeValMap=new HashMap();
    private MDSMasterSecurityLandTO objMDSMasterSecurityLandTO;
    private LinkedHashMap collateralMap;
    private LinkedHashMap deletedCollateralMap;
    final ArrayList tableCollateralTitle = new ArrayList();
    final ArrayList tableCollateralJointTitle = new ArrayList();
    
    //Case Details
    private String cboCaseStatus = "";
    private String txtCaseNumber = "";
    private String tdtlFillingDt = "";
    private String txtFillingFees = "";
    private String txtMiscCharges = "";
    private ComboBoxModel cbmCaseStatus;
    private LinkedHashMap caseDetaillMap;
    private LinkedHashMap deletedCaseDetaillMap;
    private EnhancedTableModel tblCaseDetails;
    final ArrayList caseTableTitle = new ArrayList();
    private boolean newCaseData = false;
    private ArrayList caseTabValues = new ArrayList(); 
    
    //Other Society Details
    private String cboOtherInstituion = "";
    private String cboSecurityTypeSociety = "";
    private String txtOtherInstituionName = "";
    private String txtSecurityNoSociety = "";
    private String txtSecurityAmountSociety = "";
    private String txtMaturityValueSociety = "";
    private String txtRemarksSociety = "";
    private String tdtIssueDtSoceity = "";
    private String tdtMaturityDateSociety = "";
    private ComboBoxModel cbmOtherInstituion;
    private ComboBoxModel cbmSecurityTypeSociety;
    private EnhancedTableModel tblOtherSocietyDetails;
    final ArrayList tableTitleOtherSociety = new ArrayList();
    private boolean societyTypeData = false;
    private LinkedHashMap societyTypeMap;
    private LinkedHashMap deletedSocietyTypeMap;

    public HashMap getNewTransactionMap() {
        return newTransactionMap;
    }

    public void setNewTransactionMap(HashMap newTransactionMap) {
        this.newTransactionMap = newTransactionMap;
    }

    public List getChargelst() {
        return chargelst;
    }

    public void setChargelst(List chargelst) {
        this.chargelst = chargelst;
    }

    public HashMap getOperationMap() {
        return operationMap;
    }

    public void setOperationMap(HashMap operationMap) {
        this.operationMap = operationMap;
    }
    
      private HashMap newTransactionMap; //charge details
     private List chargelst = null; //charge details
      private HashMap operationMap; //charge details
    
    private String txtChargeApplicationNo="";

    public String getTxtChargeApplicationNo() {
        return txtChargeApplicationNo;
    }

    public void setTxtChargeApplicationNo(String txtChargeApplicationNo) {
        this.txtChargeApplicationNo = txtChargeApplicationNo;
    }

    public String getTdtChargeApplicationDate() {
        return tdtChargeApplicationDate;
    }

    public void setTdtChargeApplicationDate(String tdtChargeApplicationDate) {
        this.tdtChargeApplicationDate = tdtChargeApplicationDate;
    }
    private String tdtChargeApplicationDate="";
    
      private LinkedHashMap transactionDetailsTO = null; //trans details

    public LinkedHashMap getTransactionDetailsTO() {
        return transactionDetailsTO;
    }

    public void setTransactionDetailsTO(LinkedHashMap transactionDetailsTO) {
        this.transactionDetailsTO = transactionDetailsTO;
    }

    public LinkedHashMap getDeletedTransactionDetailsTO() {
        return deletedTransactionDetailsTO;
    }

    public void setDeletedTransactionDetailsTO(LinkedHashMap deletedTransactionDetailsTO) {
        this.deletedTransactionDetailsTO = deletedTransactionDetailsTO;
    }

    public LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }

    public void setAllowedTransactionDetailsTO(LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }
    private LinkedHashMap deletedTransactionDetailsTO = null; //trans details
  private LinkedHashMap allowedTransactionDetailsTO = null; //trans details
 private TransactionOB transactionOB; //trans details
  private HashMap authMap = new HashMap(); //trans details
   private Date currDt=null; //trans details

    public Date getCurrDt() {
        return currDt;
    }

    public void setCurrDt(Date currDt) {
        this.currDt = currDt;
    }
  
    public Date getCurDate() {
        return curDate;
    }

    public void setCurDate(Date curDate) {
        this.curDate = curDate;
    }

    public HashMap getAuthMap() {
        return authMap;
    }

    public void setAuthMap(HashMap authMap) {
        this.authMap = authMap;
    }

    public double getTxtAmount() {
        return txtAmount;
    }

    public void setTxtAmount(double txtAmount) {
        this.txtAmount = txtAmount;
    }
      private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs"; //trans details
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs"; //trans details
    private double txtAmount; //trans details
    
    
        List salarySecDetails=new ArrayList();

    public List getSalarySecDetails() {
        return salarySecDetails;
    }

    public void setSalarySecDetails(List salarySecDetails) {
        this.salarySecDetails = salarySecDetails;
    }
        
    public MDSMasterMaintenanceOB() {
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "MDSMasterMaintenanceJNDI");
            map.put(CommonConstants.HOME, "mdsapplication.mdsmastermaintenance.MDSMasterMaintenanceHome");
            map.put(CommonConstants.REMOTE, "mdsapplication.mdsmastermaintenance.MDSMasterMaintenance");
            curDate = ClientUtil.getCurrentDate();
           currDt=ClientUtil.getCurrentDate(); //trans details
            setMemberTableTile();
            setDepositTableTile();
            setSocietyTableTile();
            setCaseDetailTile();
            setCollateralTableTile();
            setCollateralJointTableTile();
            tblCaseDetails= new EnhancedTableModel(null, caseTableTitle);
            tblMemberTypeDetails = new EnhancedTableModel(null, tableTitle);
            tblDepositTypeDetails = new EnhancedTableModel(null, tableTitleDepositList);
            tblOtherSocietyDetails= new EnhancedTableModel(null, tableTitleOtherSociety);
            tblCollateralDetails = new EnhancedTableModel(null, tableCollateralTitle);
            tblJointCollateral = new EnhancedTableModel(null, tableCollateralJointTitle);
            fillDropdown();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    private void setMemberTableTile() throws Exception{
        tableTitle.add("Member No");
        tableTitle.add("Name");
        tableTitle.add("Member Type");
        tableTitle.add("Contact No");
        tableTitle.add("Networth");
        tableTitle.add("Priority");
        IncVal = new ArrayList();
    }
    
    private void setCollateralTableTile() throws Exception{
        tableCollateralTitle.add("Member No");
        tableCollateralTitle.add("Name");
        tableCollateralTitle.add("Doc No");
        tableCollateralTitle.add("Pledge Amt");
        tableCollateralTitle.add("SurveyNo");
        tableCollateralTitle.add("TotalArea");
        tableCollateralTitle.add("Pledge No");
        IncVal = new ArrayList();
    }
    
    private void setCollateralJointTableTile() throws Exception{
        tableCollateralJointTitle.add("Cust Id");
        tableCollateralJointTitle.add("Name");
        tableCollateralJointTitle.add("Constitution");
        IncVal = new ArrayList();
    }
    
    private void setDepositTableTile() throws Exception{
        tableTitleDepositList.add("Prod Type");
        tableTitleDepositList.add("Dep No");
        tableTitleDepositList.add("Dep Amt");
        tableTitleDepositList.add("Matur Val");
        IncVal = new ArrayList();
    }
    
    private void setSocietyTableTile() throws Exception{
        tableTitleOtherSociety.add("SecurityNo");
        tableTitleOtherSociety.add("Security Type");
        tableTitleOtherSociety.add("Institution");
        tableTitleOtherSociety.add("Name");
        tableTitleOtherSociety.add("Amount");
        IncVal = new ArrayList();
    }
    
    private void setCaseDetailTile() throws Exception{
        caseTableTitle.add("Status");
        caseTableTitle.add("Number");
        caseTableTitle.add("File Date");
        IncVal = new ArrayList();
    }
    
    private void fillDropdown() throws Exception{
        try{
            lookupMap = new HashMap();
            lookupMap.put(CommonConstants.MAP_NAME,null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("CUSTOMER.CITY");
            lookup_keys.add("SECURITY_TYPE");
            lookup_keys.add("LOSINSTITUTION");
            lookup_keys.add("LOSSECURITYTYPE");
            lookup_keys.add("TERMLOAN.DOCUMENT_TYPE");
            lookup_keys.add("SECURITY.NATURE");
            lookup_keys.add("SECURITY.RIGHT");
            lookup_keys.add("PRODUCTTYPE");
            lookup_keys.add("TERM_LOAN.CASE_TYPE");
            lookup_keys.add("SECURITY.PLEDGE");
            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
            cbmCity= new ComboBoxModel(key,value);
            makeNull();
            
//            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
//            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap)keyValue.get("SECURITY_TYPE"));
            cbmSecurity= new ComboBoxModel(key,value);
            makeNull();
            
//            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
//            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap)keyValue.get("LOSINSTITUTION"));
            cbmOtherInstituion= new ComboBoxModel(key,value);
            makeNull();
            
//            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
//            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap)keyValue.get("LOSSECURITYTYPE"));
            cbmSecurityTypeSociety= new ComboBoxModel(key,value);
            makeNull();
            
//            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
//            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap)keyValue.get("TERMLOAN.DOCUMENT_TYPE"));
            setCbmDocumentType(new ComboBoxModel(key,value));
            makeNull();
            
//            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
//            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap)keyValue.get("SECURITY.NATURE"));
            setCbmNature(new ComboBoxModel(key,value));
            makeNull();
            
//            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
//            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap)keyValue.get("SECURITY.RIGHT"));
            setCbmRight(new ComboBoxModel(key,value));
            makeNull();
            
//            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
//            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap)keyValue.get("SECURITY.PLEDGE"));
            setCbmPledge(new ComboBoxModel(key,value));
            makeNull();
            
//            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
//            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap)keyValue.get("TERM_LOAN.CASE_TYPE"));
            cbmCaseStatus = new ComboBoxModel(key,value);
            makeNull();
            
//            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
//            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
            cbmProdType= new ComboBoxModel(key,value);
            cbmProdType.removeKeyAndElement("TD");
            cbmProdType.removeKeyAndElement("TL");
            makeNull();
            
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
            key.add("TD");
            value.add("Deposits");
            key.add("MDS");
            value.add("MDS");
            cbmProdTypeSecurity = new ComboBoxModel(key,value);
            makeNull();

//            HashMap lookUpHash = new HashMap();
//            lookUpHash.put(CommonConstants.MAP_NAME,"deposit_getProdId");
//            lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
//            keyValue = ClientUtil.populateLookupData(lookUpHash);
//            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
//            cbmDepProdID = new ComboBoxModel(key,value);
//            makeNull();
            
            cbmProdId = new ComboBoxModel();
            
            cbmDepProdID = new ComboBoxModel();
            
        }catch(NullPointerException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get("KEY");
        value = (ArrayList)keyValue.get("VALUE");
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    private void makeNull(){
        key=null;
        value=null;
    }
    
    public void setCbmProdId(String prodType) {
        if (prodType.equals("GL")) {
            key = new ArrayList();
            value = new ArrayList();
        } else {
            try {
                HashMap lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + prodType);
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        cbmProdId = new ComboBoxModel(key,value);
        this.cbmProdId = cbmProdId;
        setChanged();
    }
    
    public void setCbmProdTypeSecurity(String prodType) {
        try {
            HashMap lookUpHash = new HashMap();
            if (prodType.equals("MDS")) {
                lookUpHash.put(CommonConstants.MAP_NAME,"Lock.getAccProductMDS");
            }else{
                lookUpHash.put(CommonConstants.MAP_NAME,"deposit_getProdId");
            }
            lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        cbmDepProdID= new ComboBoxModel(key,value);
        this.cbmDepProdID = cbmDepProdID;
        setChanged();
    }
    
    public void addCollateralTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            final MDSMasterSecurityLandTO objMDSMasterSecurityLandTO = new MDSMasterSecurityLandTO();
            MDSMasterSecurityLandTO obj =new MDSMasterSecurityLandTO();
            if( collateralMap == null ){
                collateralMap = new LinkedHashMap();
            }
            if(rowSelected !=-1 &&(!tblCollateralDetails.getDataArrayList().isEmpty())){
                ArrayList list=(ArrayList) tblCollateralDetails.getDataArrayList().get(rowSelected);
                 //obj =(MDSMasterSecurityLandTO)collateralMap.get(list.get(0));
                obj =(MDSMasterSecurityLandTO)collateralMap.get(list.get(0)+"_"+(rowSelected+1));
                
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isCollateralTypeData() || (!obj.getStatus().equals(CommonConstants.STATUS_MODIFIED))){
                    objMDSMasterSecurityLandTO.setStatusDt(curDate);
                    objMDSMasterSecurityLandTO.setStatusBy(TrueTransactMain.USER_ID);
                    objMDSMasterSecurityLandTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objMDSMasterSecurityLandTO.setStatusDt(curDate);
                    objMDSMasterSecurityLandTO.setStatusBy(TrueTransactMain.USER_ID);
                    objMDSMasterSecurityLandTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            }else{
                objMDSMasterSecurityLandTO.setStatusDt(curDate);
                objMDSMasterSecurityLandTO.setStatusBy(TrueTransactMain.USER_ID);
                objMDSMasterSecurityLandTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            if(isRdoGahanYes())
                objMDSMasterSecurityLandTO.setGahanYesNo("Y");
            else  if(isRdoGahanNo())
                objMDSMasterSecurityLandTO.setGahanYesNo("N");
            else
            {
                objMDSMasterSecurityLandTO.setGahanYesNo("");
            }
            objMDSMasterSecurityLandTO.setChittalNo(getTxtChittalNo());
            objMDSMasterSecurityLandTO.setSubNo(getTxtSubNo());
            objMDSMasterSecurityLandTO.setMemberNo(getTxtOwnerMemNo());
            objMDSMasterSecurityLandTO.setMemberName(getTxtOwnerMemberNname());
            objMDSMasterSecurityLandTO.setDocumentNo(getTxtDocumentNo());
            objMDSMasterSecurityLandTO.setDocumentType(getCboDocumentType());
            objMDSMasterSecurityLandTO.setDocumentDt(getProperDateFormat(getTdtDocumentDate()));
            objMDSMasterSecurityLandTO.setRegisteredOffice(getTxtRegisteredOffice());
            objMDSMasterSecurityLandTO.setPledge(getCboPledge());
            objMDSMasterSecurityLandTO.setPledgeDt(getProperDateFormat(getTdtPledgeDate()));
            objMDSMasterSecurityLandTO.setPledgeNo(getTxtPledgeNo());
            objMDSMasterSecurityLandTO.setPledgeAmount(CommonUtil.convertObjToDouble(getTxtPledgeAmount()));
            objMDSMasterSecurityLandTO.setVillage(getTxtVillage());
            objMDSMasterSecurityLandTO.setSurveyNo(getTxtSurveyNo());
            objMDSMasterSecurityLandTO.setTotalArea(getTxtTotalArea());
            objMDSMasterSecurityLandTO.setNature(getCboNature());
            objMDSMasterSecurityLandTO.setRight(getCboRight());
            objMDSMasterSecurityLandTO.setRemarks(getTxtAreaParticular());
            objMDSMasterSecurityLandTO.setDocGenId(getDocGenId());
            //collateralMap.put(objMDSMasterSecurityLandTO.getMemberNo(),objMDSMasterSecurityLandTO);
            objMDSMasterSecurityLandTO.setOldSurvyNo(getOldSurvyNo());
            System.out.println("objTermLoanSecurityLandTO.getMemberNo()===="+objMDSMasterSecurityLandTO.getMemberNo());
            System.out.println("getRowCoun()----88888"+getRowCoun());
           collateralMap.put(objMDSMasterSecurityLandTO.getMemberNo()+"_"+(getRowCoun()),objMDSMasterSecurityLandTO);
             //collateralMap.put(objTermLoanSecurityLandTO.getMemberNo()+"_"+(rowSelected+1),objTermLoanSecurityLandTO);
            System.out.println("collateralMap====uuuuuiiiiiiiiiiooooo"+collateralMap);
            updateCollateralDetails(rowSel,objMDSMasterSecurityLandTO,updateMode);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void deleteCollateralTableData(String val, int row){
        if(deletedCollateralMap == null){
            deletedCollateralMap = new LinkedHashMap();
        }
        MDSMasterSecurityLandTO objMDSMasterSecurityLandTO = (MDSMasterSecurityLandTO) collateralMap.get(val);
        objMDSMasterSecurityLandTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedCollateralMap.put(CommonUtil.convertObjToStr(tblCollateralDetails.getValueAt(row,0)),collateralMap.get(val));
        Object obj;
        obj=val;
        collateralMap.remove(val);
        tblCollateralDetails.setDataArrayList(null,tableCollateralTitle);
        try{
            populateCollateralTable();
            objMDSMasterSecurityLandTO.setOldSurvyNo(getOldSurvyNo());
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateCollateralTable()  throws Exception{
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(collateralMap.keySet());
        ArrayList addList =new ArrayList(collateralMap.keySet());
        int length = incDataList.size();
        LinkedHashMap tempCollateralmp= new LinkedHashMap();
        for(int i=0; i<length; i++){
            ArrayList incTabRow = new ArrayList();
            MDSMasterSecurityLandTO objMDSMasterSecurityLandTO = (MDSMasterSecurityLandTO) collateralMap.get(addList.get(i));
            IncVal.add(objMDSMasterSecurityLandTO);
            if(!objMDSMasterSecurityLandTO.getStatus().equals("DELETED")){
                incTabRow.add(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getMemberNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getMemberName()));
                incTabRow.add(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getDocumentNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getPledgeAmount()));
                incTabRow.add(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getSurveyNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getTotalArea()));
                incTabRow.add(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getPledgeNo()));
                tblCollateralDetails.addRow(incTabRow);
                tempCollateralmp.put(objMDSMasterSecurityLandTO.getMemberNo()+"_"+(i+1), objMDSMasterSecurityLandTO);
            }
        }
         collateralMap= new LinkedHashMap();
        collateralMap.putAll(tempCollateralmp);
        System.out.println("collateralMap====for test"+collateralMap);
    }
    
    public void populateCollateralDetails(String row){
        try{
            resetCollateralDetails();
            System.out.println("collateralMap==="+collateralMap);
            final MDSMasterSecurityLandTO objMDSMasterSecurityLandTO = (MDSMasterSecurityLandTO)collateralMap.get(row);
            populateCollateralTableData(objMDSMasterSecurityLandTO);
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateCollateralTableData(MDSMasterSecurityLandTO objMDSMasterSecurityLandTO)  throws Exception{
       if(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getGahanYesNo()).equals("Y"))
            setRdoGahanYes(true);
       else if(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getGahanYesNo()).equals("N"))
           setRdoGahanNo(true);
        setTxtOwnerMemNo(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getMemberNo()));
        setTxtOwnerMemberNname(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getMemberName()));
        setTxtDocumentNo(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getDocumentNo()));
        setCboDocumentType(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getDocumentType()));
        setTdtDocumentDate(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getDocumentDt()));
        setTxtRegisteredOffice(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getRegisteredOffice()));
        setCboPledge(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getPledge()));
        setTdtPledgeDate(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getPledgeDt()));
        setTxtPledgeNo(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getPledgeNo()));
        setTxtPledgeAmount(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getPledgeAmount()));
        setTxtVillage(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getVillage()));
        setTxtSurveyNo(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getSurveyNo()));
        setTxtTotalArea(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getTotalArea()));
        setCboNature(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getNature()));
        setCboRight(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getRight()));
        setTxtAreaParticular(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getRemarks()));
    }
    
    private void updateCollateralDetails(int rowSel,  MDSMasterSecurityLandTO objMDSMasterSecurityLandTO, boolean updateMode)  throws Exception{
        System.out.println("tblCollateralDetails=="+tblCollateralDetails+"rowSel=="+rowSel);
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        if(!(rowSel==-1)){
            System.out.println("rowSel===="+rowSel);
        for(int i = tblCollateralDetails.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblCollateralDetails.getDataArrayList().get(j)).get(0);
            System.out.println("");
            if(getTxtOwnerMemNo().equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblCollateralDetails.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(getTxtOwnerMemNo());
                IncParRow.add(getTxtOwnerMemberNname());
                IncParRow.add(getTxtDocumentNo());
                IncParRow.add(getTxtPledgeAmount());
                IncParRow.add(getTxtSurveyNo());
                IncParRow.add(getTxtTotalArea());
                IncParRow.add(getTxtPledgeNo());
                tblCollateralDetails.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        }
        if(!rowExists){
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(getTxtOwnerMemNo());
            IncParRow.add(getTxtOwnerMemberNname());
            IncParRow.add(getTxtDocumentNo());
            IncParRow.add(getTxtPledgeAmount());
            IncParRow.add(getTxtSurveyNo());
            IncParRow.add(getTxtTotalArea());
            IncParRow.add(getTxtPledgeNo());
            tblCollateralDetails.insertRow(tblCollateralDetails.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
    
    public Date getProperDateFormat(Object obj) {
        Date currDt = null;
        if (obj!=null && obj.toString().length()>0) {
            Date tempDt= DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDt=(Date)curDate.clone();
            currDt.setDate(tempDt.getDate());
            currDt.setMonth(tempDt.getMonth());
            currDt.setYear(tempDt.getYear());
        }
        return currDt;
    }
    
    public void updateCollateralJointDetails(String memberNo){
        HashMap dataMap =new HashMap();
        ArrayList totList=new ArrayList();
        dataMap.put("MEMBER_NO",memberNo);
        dataMap.put("DOCUMENT_GEN_ID",getDocGenId());
        ArrayList IncParRow=null;
        List lst=null;
        //List lst=ClientUtil.executeQuery("getGahanJointDetailsforLoan", dataMap);
        System.out.println("isRdoGahanYes ::" + isRdoGahanYes());
        if(isRdoGahanYes()==true){
            lst=ClientUtil.executeQuery("getGahanJointDetailsforLoanFromGahan", dataMap);
        }else{
            lst=ClientUtil.executeQuery("getGahanJointDetailsforLoan", dataMap);
        }
        if(lst !=null && lst.size()>0){
            for(int i=0;i<lst.size();i++){
                HashMap resultMap=(HashMap)lst.get(i);
                IncParRow = new ArrayList();
                IncParRow.add(resultMap.get("CUST_ID"));
                IncParRow.add(resultMap.get("CUSTOMER"));
                IncParRow.add(resultMap.get("CONSTITUTION"));
                totList.add(IncParRow);
            }
        }
        tblJointCollateral.setDataArrayList(totList,tableCollateralJointTitle);
    }
    
    public HashMap validatePledgeAmount(String docNumber,double pledgeAmt){
        HashMap tempMap=new HashMap();
        HashMap returnMap=new HashMap();
        System.out.println("pledgeValMap===="+pledgeValMap);
        if(pledgeValMap !=null){
            for(int i=0;i<pledgeValMap.size();i++){
                 tempMap=(HashMap)pledgeValMap.get(docNumber);
                if(tempMap !=null && CommonUtil.convertObjToStr(tempMap.get("DOC_NO")).equals(docNumber)){
                    if(CommonUtil.convertObjToDouble(tempMap.get("PLEDGE_AMT")).doubleValue()<pledgeAmt){
                        returnMap.putAll(tempMap);
                        return returnMap;
                    }
                }
            }
        }
        return returnMap;
    }
    
    public void addPledgeAmountMap(String docNumber,double pledgeAmt){
        try
        {
            if(pledgeValMap==null){
                pledgeValMap= new HashMap();
            }
        HashMap tempMap=new HashMap();
        tempMap.put("DOC_NO",docNumber);
        tempMap.put("PLEDGE_AMT",new Double(pledgeAmt));
        pledgeValMap.put(docNumber,tempMap);
        }
        catch(Exception e)
        {
            System.out.println("eeeeeeeeeeeee"+e);
            e.printStackTrace();
        }
    }
    
    public void setTermLoanCaseDetailTable(ArrayList caseList, String acctNum){
        try{
            TermLoanCaseDetailTO termLoanCaseTO;
            if( caseDetaillMap == null ){
                caseDetaillMap = new LinkedHashMap();
            }
            ArrayList caseRecordList;
            ArrayList tabCaseRecords = new ArrayList();
            // To retrieve the Case Details from the Serverside
            for (int i = caseList.size() - 1,j = 0;i >= 0;--i,++j){
                termLoanCaseTO = (TermLoanCaseDetailTO) caseList.get(j);
                caseRecordList = new ArrayList();
                caseRecordList.add(CommonUtil.convertObjToStr(termLoanCaseTO.getCaseStatus()));
                caseRecordList.add(CommonUtil.convertObjToStr(termLoanCaseTO.getCaseNo()));
                caseRecordList.add(CommonUtil.convertObjToStr(termLoanCaseTO.getFillingDt()));
                tabCaseRecords.add(caseRecordList);
                termLoanCaseTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
                caseDetaillMap.put(CommonUtil.convertObjToStr(termLoanCaseTO.getCaseStatus()), termLoanCaseTO);
                caseRecordList = null;
            }
            caseTabValues.clear();
            caseTabValues = tabCaseRecords;
            tblCaseDetails.setDataArrayList(caseTabValues, caseTableTitle);
            tabCaseRecords = null;
        }catch(Exception e){
            log.info("Error in setTermLoanCaseDetailTable()..."+e);
            parseException.logException(e,true);
        }
    }            
          
    
    
    public void addToCaseTable(int rowSelected){
        try{
            int rowSel=rowSelected;
            final TermLoanCaseDetailTO objCaseDetailTO = new TermLoanCaseDetailTO();
            if( caseDetaillMap == null ){
                caseDetaillMap = new LinkedHashMap();
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewCaseData()){
                    objCaseDetailTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                }else{
                    objCaseDetailTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
                }
            }else{
                objCaseDetailTO.setCommand(CommonConstants.TOSTATUS_INSERT);
            }
            objCaseDetailTO.setCaseStatus(getCboCaseStatus());
            objCaseDetailTO.setCaseNo(getTxtCaseNumber());
            objCaseDetailTO.setFillingDt(DateUtil.getDateMMDDYYYY(getTdtlFillingDt()));
            objCaseDetailTO.setFillingFees(getTxtFillingFees());
            objCaseDetailTO.setMiscCharges(getTxtMiscCharges());
            caseDetaillMap.put(objCaseDetailTO.getCaseStatus(),objCaseDetailTO);
            updateCaseDetails(rowSel,objCaseDetailTO);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void updateCaseDetails(int rowSel, TermLoanCaseDetailTO objCaseDetailTO)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblCaseDetails.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblCaseDetails.getDataArrayList().get(j)).get(0);
            if(getCboCaseStatus().equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblCaseDetails.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(getCboCaseStatus());
                IncParRow.add(getTxtCaseNumber());
                IncParRow.add(getTdtlFillingDt());
                tblCaseDetails.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        if(!rowExists){
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(getCboCaseStatus());
            IncParRow.add(getTxtCaseNumber());
            IncParRow.add(getTdtlFillingDt());
            tblCaseDetails.insertRow(tblCaseDetails.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
    
    
    public void populateCaseDetails(String row){
        try{
            resetCaseDetails();
            final TermLoanCaseDetailTO objCaseDetailTO = (TermLoanCaseDetailTO)caseDetaillMap.get(row);
            populateTableData(objCaseDetailTO);
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void resetCaseDetails() {
        setCboCaseStatus("");
        setTxtFillingFees("");
        setTdtlFillingDt("");
        setTxtCaseNumber("");
        setTxtMiscCharges("");
    }
    
    private void populateTableData(TermLoanCaseDetailTO objCaseDetailTO)  throws Exception{
        setCboCaseStatus(CommonUtil.convertObjToStr(objCaseDetailTO.getCaseStatus()));
        setTxtCaseNumber(CommonUtil.convertObjToStr(objCaseDetailTO.getCaseNo()));
        setTdtlFillingDt(CommonUtil.convertObjToStr(objCaseDetailTO.getFillingDt()));
        setTxtFillingFees(CommonUtil.convertObjToStr(objCaseDetailTO.getFillingFees()));
        setTxtMiscCharges(CommonUtil.convertObjToStr(objCaseDetailTO.getMiscCharges()));
    }
    
    public void deleteCaseTableData(String val, int row){
        if(deletedCaseDetaillMap == null){
            deletedCaseDetaillMap = new LinkedHashMap();
        }
        TermLoanCaseDetailTO objCaseDetailTO = (TermLoanCaseDetailTO) caseDetaillMap.get(val);
        objCaseDetailTO.setCommand(CommonConstants.TOSTATUS_DELETE);
        deletedCaseDetaillMap.put(CommonUtil.convertObjToStr(tblCaseDetails.getValueAt(row,0)),caseDetaillMap.get(val));
        Object obj;
        obj=val;
        caseDetaillMap.remove(val);
        caseDetaillMap.put(val,objCaseDetailTO);
        resetCaseTableValues();
        try{
            populateCaseTable();
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateCaseTable()  throws Exception{
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(caseDetaillMap.keySet());
        ArrayList addList =new ArrayList(caseDetaillMap.keySet());
        int length = incDataList.size();
        for(int i=0; i<length; i++){
            ArrayList incTabRow = new ArrayList();
            TermLoanCaseDetailTO objCaseDetailTO = (TermLoanCaseDetailTO) caseDetaillMap.get(addList.get(i));
            IncVal.add(objCaseDetailTO);
            if(!objCaseDetailTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)){
                incTabRow.add(CommonUtil.convertObjToStr(objCaseDetailTO.getCaseStatus()));
                incTabRow.add(CommonUtil.convertObjToStr(objCaseDetailTO.getCaseNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objCaseDetailTO.getFillingDt()));
                tblCaseDetails.addRow(incTabRow);
            }
        }
    }
    
    public void resetCaseTableValues(){
        tblCaseDetails.setDataArrayList(null,caseTableTitle);
    }
    
    public void addMemberTypeTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            final MDSMemberTypeTO objMemberTypeTO = new MDSMemberTypeTO();
            if( memberTypeMap == null ){
                memberTypeMap = new LinkedHashMap();
            }
            
            
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isMemberTypeData()){
                    objMemberTypeTO.setStatusDt(currDt);
                    objMemberTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objMemberTypeTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objMemberTypeTO.setStatusDt(currDt);
                    objMemberTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objMemberTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            }else{
                objMemberTypeTO.setStatusDt(currDt);
                objMemberTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                objMemberTypeTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            objMemberTypeTO.setChittalNo(getTxtChittalNo());
            objMemberTypeTO.setSubNo(getTxtSubNo());
            objMemberTypeTO.setMemberNo(getTxtMemNo());
            objMemberTypeTO.setMemberName(getTxtMemName());
            objMemberTypeTO.setMemberType(getTxtMemType());
            objMemberTypeTO.setContactNo(CommonUtil.convertObjToLong(getTxtContactNum()));  //AJITH
            objMemberTypeTO.setNetworth(getTxtMemNetworth());
            objMemberTypeTO.setPriority(CommonUtil.convertObjToInt(getTxtMemPriority()));
            memberTypeMap.put(objMemberTypeTO.getMemberNo(),objMemberTypeTO);
            updateMemberTypeDetails(rowSel,objMemberTypeTO,updateMode);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void updateMemberTypeDetails(int rowSel,  MDSMemberTypeTO objMDSMemberTypeTO, boolean updateMode)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblMemberTypeDetails.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblMemberTypeDetails.getDataArrayList().get(j)).get(0);
            if(getTxtMemNo().equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblMemberTypeDetails.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(getTxtMemNo());
                IncParRow.add(getTxtMemName());
                IncParRow.add(getTxtMemType());
                IncParRow.add(getTxtContactNum());
                IncParRow.add(getTxtMemNetworth());
                IncParRow.add(getTxtMemPriority());
                IncParRow.add(getTxtMemPriority());
                tblMemberTypeDetails.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        if(!rowExists){
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(getTxtMemNo());
            IncParRow.add(getTxtMemName());
            IncParRow.add(getTxtMemType());
            IncParRow.add(getTxtContactNum());
            IncParRow.add(getTxtMemNetworth());
            IncParRow.add(getTxtMemPriority());
            tblMemberTypeDetails.insertRow(tblMemberTypeDetails.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
    
    public void addDepositTypeTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            final MDSDepositTypeTO objDepositTypeTO = new MDSDepositTypeTO();
            if( depositTypeMap == null ){
                depositTypeMap = new LinkedHashMap();
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isDepositTypeData()){
                    objDepositTypeTO.setStatusDt(currDt);
                    objDepositTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objDepositTypeTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objDepositTypeTO.setStatusDt(currDt);
                    objDepositTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objDepositTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            }else{
                objDepositTypeTO.setStatusDt(currDt);
                objDepositTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                objDepositTypeTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            
            objDepositTypeTO.setChittalNo(getTxtChittalNo());
            objDepositTypeTO.setSubNo(getTxtSubNo());
            objDepositTypeTO.setDepositNo(getTxtDepNo());
//            objDepositTypeTO.setProdId(getTxtProductId());
            objDepositTypeTO.setProdType(CommonUtil.convertObjToStr(cbmProdTypeSecurity.getKeyForSelected()));
            objDepositTypeTO.setProdId(CommonUtil.convertObjToStr(cbmDepProdID.getKeyForSelected()));
            objDepositTypeTO.setAmount(CommonUtil.convertObjToDouble(getTxtDepAmount())); //AJITH
            objDepositTypeTO.setDepositDt(DateUtil.getDateMMDDYYYY(getTdtDepDt()));
            objDepositTypeTO.setMaturityDt(DateUtil.getDateMMDDYYYY(getTxtMaturityDt()));
            objDepositTypeTO.setMaturityValue(CommonUtil.convertObjToDouble(getTxtMaturityValue()));
            objDepositTypeTO.setIntRate(CommonUtil.convertObjToDouble(getTxtRateOfInterest())); //AJITH
            
             if(getCbxSamechittal().equals("Y"))
            {
               objDepositTypeTO.setSameChittal("Y"); 
            }
            else
            {
                 objDepositTypeTO.setSameChittal("N"); 
            }
            
            depositTypeMap.put(objDepositTypeTO.getDepositNo(),objDepositTypeTO);
            updateDepositTypeDetails(rowSel,objDepositTypeTO,updateMode);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    public void addSocietyTypeTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            final MDSSocietyTypeTO objMDSSocietyTypeTO = new MDSSocietyTypeTO();
            if( societyTypeMap == null ){
                societyTypeMap = new LinkedHashMap();
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isSocietyTypeData()){
                    objMDSSocietyTypeTO.setStatusDt(currDt);
                    objMDSSocietyTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objMDSSocietyTypeTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objMDSSocietyTypeTO.setStatusDt(currDt);
                    objMDSSocietyTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objMDSSocietyTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            }else{
                objMDSSocietyTypeTO.setStatusDt(currDt);
                objMDSSocietyTypeTO.setStatusBy(TrueTransactMain.USER_ID);
                objMDSSocietyTypeTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            objMDSSocietyTypeTO.setChittalNo(getTxtChittalNo());
            objMDSSocietyTypeTO.setSubNo(CommonUtil.convertObjToInt(getTxtSubNo()));
            objMDSSocietyTypeTO.setInstitution(CommonUtil.convertObjToStr(cbmOtherInstituion.getKeyForSelected()));
            objMDSSocietyTypeTO.setSecurityType(CommonUtil.convertObjToStr(cbmSecurityTypeSociety.getKeyForSelected()));
            objMDSSocietyTypeTO.setName(getTxtOtherInstituionName());
            objMDSSocietyTypeTO.setSecurityNo(getTxtSecurityNoSociety());
            objMDSSocietyTypeTO.setAmount(CommonUtil.convertObjToDouble(getTxtSecurityAmountSociety()));
            objMDSSocietyTypeTO.setMaturityValue(CommonUtil.convertObjToDouble(getTxtMaturityValueSociety()));
            objMDSSocietyTypeTO.setRemarks(getTxtRemarksSociety());
            objMDSSocietyTypeTO.setIssueDt(DateUtil.getDateMMDDYYYY(getTdtIssueDtSoceity()));
            objMDSSocietyTypeTO.setMatDt(DateUtil.getDateMMDDYYYY(getTdtMaturityDateSociety()));
            societyTypeMap.put(objMDSSocietyTypeTO.getSecurityNo(),objMDSSocietyTypeTO);
            updateSocietyTypeDetails(rowSel,objMDSSocietyTypeTO,updateMode);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void updateSocietyTypeDetails(int rowSel,  MDSSocietyTypeTO objMDSSocietyTypeTO, boolean updateMode)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblOtherSocietyDetails.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblOtherSocietyDetails.getDataArrayList().get(j)).get(0);
            if(getTxtSecurityNoSociety().equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblOtherSocietyDetails.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(getTxtSecurityNoSociety());
                IncParRow.add(CommonUtil.convertObjToStr(cboSecurityTypeSociety));
                IncParRow.add(CommonUtil.convertObjToStr(cboOtherInstituion));
                IncParRow.add(getTxtOtherInstituionName());
                IncParRow.add(getTxtSecurityAmountSociety());
                tblOtherSocietyDetails.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        if(!rowExists){
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(getTxtSecurityNoSociety());
            IncParRow.add(CommonUtil.convertObjToStr(cboSecurityTypeSociety));
            IncParRow.add(CommonUtil.convertObjToStr(cboOtherInstituion));
            IncParRow.add(getTxtOtherInstituionName());
            IncParRow.add(getTxtSecurityAmountSociety());
            tblOtherSocietyDetails.insertRow(tblOtherSocietyDetails.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
    
    public void setPaymentDetails(){
        HashMap listMap = new HashMap();
        listMap.put("CHITTAL_NO",getTxtChittalNo());
        listMap.put("SUB_NO",CommonUtil.convertObjToInt(getTxtSubNo()));    //AJITH Changed From CommonUtil.convertObjToStr(getTxtSubNo())
        List lst = ClientUtil.executeQuery("getMDSMemberDetailsMaster", listMap);
        if(lst!=null && lst.size()>0){
            listMap = (HashMap)lst.get(0);
            int instFreq=0;
            //this.setTxtMemberNo(CommonUtil.convertObjToStr(listMap.get("MEMBER_NO")));
            this.setTxtMemberNo(CommonUtil.convertObjToStr(listMap.get("MEMBERSHIP_NO"))); // Added by nithya on 29-09-2020 for KD 2322
            this.setLblMemberName(CommonUtil.convertObjToStr(listMap.get("MEMBER_NAME")));
            this.setLblMemberType(CommonUtil.convertObjToStr(listMap.get("MEMBER_TYPE")));
            this.setLblNomineeName(CommonUtil.convertObjToStr(""));
            instFreq = CommonUtil.convertObjToInt(listMap.get("INSTALLMENT_FREQUENCY"));
            if(CommonUtil.convertObjToStr(listMap.get("STANDING_INSTN")).equals("Y")){
                setChkStandingInstn(true);
                getCbmProdType().setKeyForSelected(listMap.get("PROD_TYPE"));
                getCbmProdId().setKeyForSelected(listMap.get("PROD_ID"));
                setTxtCustomerIdCr(CommonUtil.convertObjToStr(listMap.get("DR_ACT_NO")));
            }else{
                setChkStandingInstn(false);
            }
            if(CommonUtil.convertObjToStr(listMap.get("NOMINEE")).equals("Y")){
                setChkNominee(true);
            }else{
                setChkNominee(false);
            }
            long totMember = 0;
            HashMap prodMap = new HashMap();
            prodMap.put("SCHEME_NAME",getTxtSchemeName());
            prodMap.put("CHITTAL_NO",getTxtChittalNo());
            prodMap.put("SUB_NO",CommonUtil.convertObjToInt(getTxtSubNo()));  //AJITH Changed From CommonUtil.convertObjToStr(getTxtSubNo())
            lst = ClientUtil.executeQuery("getSchemeNameDetailsNoOfMember", prodMap);
            if(lst!=null && lst.size()>0){
                prodMap = (HashMap)lst.get(0);
                setTxtPrizedAmount(CommonUtil.convertObjToDouble(prodMap.get("PRIZED_AMOUNT")));    //AJITH
                setTdtPayDt(CommonUtil.convertObjToStr(prodMap.get("PAYMENT_DATE")));
                if(prodMap.containsKey("USER_DEFINED_Y_N") && prodMap.get("USER_DEFINED_Y_N") != null && CommonUtil.convertObjToStr(prodMap.get("USER_DEFINED_Y_N")).equalsIgnoreCase("Y")){
                  setUserDefinedAuction("Y");  
                }
            }
            Date InstalDt = null;
            HashMap insDateMap = new HashMap();
            insDateMap.put("DIVISION_NO",getTxtDivisionNo());
            insDateMap.put("SCHEME_NAME",getTxtSchemeName());
            insDateMap.put("CURR_DATE",currDt.clone());
            List insDateLst = ClientUtil.executeQuery("getMDSNextInstallmentDt", insDateMap);
            if(insDateLst!=null && insDateLst.size()>0){
                insDateMap = (HashMap)insDateLst.get(0);
                InstalDt = (Date) insDateMap.get("INST_DT");
                setTdtLastInstDate(CommonUtil.convertObjToStr(InstalDt));
                setTxtLastInstNo(CommonUtil.convertObjToStr(insDateMap.get("INST_NO")));
            }else{
                setTdtLastInstDate(CommonUtil.convertObjToStr(listMap.get("LAST_TRANS_DT")));
                setTxtLastInstNo(CommonUtil.convertObjToStr(listMap.get("INST_COUNT")));
            }
            if(CommonUtil.convertObjToStr(getTdtLastInstDate()).equals("")){
                setTdtLastInstDate(CommonUtil.convertObjToStr(listMap.get("LAST_TRANS_DT")));
            }
            HashMap whereMap = new HashMap();
            whereMap.put("CHITTAL_NO",getTxtChittalNo());
            whereMap.put("SUB_NO",CommonUtil.convertObjToInt(getTxtSubNo()));  //AJITH Changed From CommonUtil.convertObjToStr(getTxtSubNo())
            List list = ClientUtil.executeQuery("getTotalInstAmount", whereMap);
            if(list!=null && list.size()>0){
                whereMap = (HashMap)list.get(0);
                setTxtTotalAmountTillDate(CommonUtil.convertObjToDouble(whereMap.get("PAID_AMT"))); //AJITH
            }
            setTxtRemarks(CommonUtil.convertObjToStr(listMap.get("REMARKS")));
            // the following lines commented by nithya for KD 2322- Already written this code at the start of this function
//            this.setTxtMemberNo(CommonUtil.convertObjToStr(listMap.get("MEMBER_NO")));
//            this.setLblMemberName(CommonUtil.convertObjToStr(listMap.get("MEMBER_NAME")));
//            this.setLblMemberType(CommonUtil.convertObjToStr(listMap.get("MEMBER_TYPE")));
//            this.setLblNomineeName(CommonUtil.convertObjToStr(""));
            setTxtOverDueInstallments(CommonUtil.convertObjToInt(listMap.get("NO_OF_INSTALLMENTS")) -
            CommonUtil.convertObjToInt(listMap.get("INST_COUNT"))); //AJITH
            double overDueAmt = (CommonUtil.convertObjToLong(listMap.get("NO_OF_INSTALLMENTS")) -
            CommonUtil.convertObjToLong(listMap.get("INST_COUNT"))) * CommonUtil.convertObjToLong(listMap.get("INST_AMT"));
            setTxtOverDueAmount(overDueAmt);    //AJITH
            Date InstalDatet = null;
            int instDay = 0;
            HashMap insDtMap = new HashMap();
            insDtMap.put("DIVISION_NO",CommonUtil.convertObjToInt(getTxtDivisionNo()));
            insDtMap.put("SCHEME_NAME",getTxtSchemeName());
            insDtMap.put("CURR_DATE",currDt.clone());
            List insDtLst = ClientUtil.executeQuery("getMDSNextInsDate", insDtMap);
            if(insDtLst!=null && insDtLst.size()>0){
                insDtMap = (HashMap)insDtLst.get(0);
                // The following block changed by Rajesh
//                InstalDatet = (Date) insDtMap.get("INST_DT");
//                int instalDay = InstalDatet.getDate();
//                instDay = instalDay;
                if (insDtMap.get("INST_DT")!=null) {
                    InstalDatet = (Date) insDtMap.get("INST_DT");
                    int instalDay = InstalDt.getDate();
                    instDay = instalDay;
                } else {
                    instDay = CommonUtil.convertObjToInt(listMap.get("INSTALLMENT_DAY"));
                }
                // End
            }else{
                instDay = CommonUtil.convertObjToInt(listMap.get("INSTALLMENT_DAY"));
            }
            Date startDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(listMap.get("SCHEME_START_DT")));
            Date endDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(listMap.get("SCHEME_END_DT")));
            Date currDate = ClientUtil.getCurrentDate();
            if(endDate!=null && endDate.before(currDate)){
               currDate = endDate; 
            }
            int stYear = startDate.getYear()+1900;
            int currYear = currDate.getYear()+1900;
            int stMonth = startDate.getMonth();
            int currMonth = currDate.getMonth();
            int value = 0;
            int pending = 0;
            long count =0;
            HashMap instMap = new HashMap();
            instMap.put("CHITTAL_NO",getTxtChittalNo());
            instMap.put("SUB_NO",CommonUtil.convertObjToInt(getTxtSubNo()));  //AJITH Changed From CommonUtil.convertObjToStr(getTxtSubNo())
            List instLst = ClientUtil.executeQuery("getNoOfInstalmentsPaid", instMap);
            if(instLst!=null && instLst.size()>0){
                instMap = (HashMap)instLst.get(0);
                count = CommonUtil.convertObjToLong(instMap.get("NO_OF_INST"));
            }
            int totInst = CommonUtil.convertObjToInt(listMap.get("NO_OF_INSTALLMENTS"));
            if(instFreq!=7){
            if(stYear == currYear && stMonth == currMonth){
                pending = 0;
                setTxtOverDueInstallments(0);   //AJITH
            }else if(stYear == currYear && stMonth != currMonth){
                pending = 0;
                value = currMonth - stMonth+1;
                int diffMonth = currMonth - stMonth;
                int pendingVal = diffMonth - (int)count;
                //Changed By Suresh
                if(instDay<currDate.getDate()){
//                    pending = pendingVal+1;
                    pending = pendingVal;
                }
                else{
                    pending = pendingVal;
                }
                if(pending>0){
                    setTxtOverDueInstallments(pending); //AJTIH
                }
                else{
                    setTxtOverDueInstallments(0);   //AJITH
                }
                if(totInst == value){
                    setTxtLastInstNo(String.valueOf(value));
                }else if(instDay<currDate.getDate()){
                    setTxtLastInstNo(String.valueOf(value));
                }else{
                    setTxtLastInstNo(String.valueOf(value-1));
                }
            }else{
                int year = currYear - stYear;
                value = (year * 12) + currMonth - stMonth;
                int pendingVal = value - (int)count;
                
                //Changed By Suresh
                if(instDay<currDate.getDate()){
//                    pending = pendingVal+1;
                    pending = pendingVal;
                }
                else{
                    pending = pendingVal;
                }
                if(pending>0){
                    setTxtOverDueInstallments(pending); //AJITH
                }
                else{
                    setTxtOverDueInstallments(0);   //AJITH
                }
            }
            }else{
                //Weekly Frequency
                long diffDays =0;
                int weekly=0;
                diffDays = DateUtil.dateDiff(startDate,currDate);
                System.out.println("######## diffDays: "+diffDays);
                if(diffDays>0){
                    weekly = (int)diffDays/7;
                    int pendingVal = weekly - (int)count;
                    if(instDay<currDate.getDate()){
                        pending = pendingVal+1;
                    }else{
                        pending = pendingVal;
                    }
                }
                if(pending>0)
                    setTxtOverDueInstallments(pending); //AJITH
                else
                    setTxtOverDueInstallments(0);   //AJITH
            }
            setTxtOverDueAmount(CommonUtil.convertObjToDouble(getTxtOverDueInstallments()) * CommonUtil.convertObjToDouble(listMap.get("INST_AMT"))); //AJITH
        }
    }

    private void updateDepositTypeDetails(int rowSel,  MDSDepositTypeTO objDepositTypeTO, boolean updateMode)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblDepositTypeDetails.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblDepositTypeDetails.getDataArrayList().get(j)).get(1);
            if(getTxtDepNo().equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblDepositTypeDetails.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(CommonUtil.convertObjToStr(cboProductTypeSecurity));
                IncParRow.add(getTxtDepNo());
//                IncParRow.add(CommonUtil.convertObjToStr(cbmDepProdID.getKeyForSelected()));
//                IncParRow.add(getTdtDepDt());
                IncParRow.add(getTxtDepAmount());
//                IncParRow.add(getTxtRateOfInterest());
                IncParRow.add(getTxtMaturityValue());
//                IncParRow.add(getTxtMaturityDt());
                tblDepositTypeDetails.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        if(!rowExists){
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(CommonUtil.convertObjToStr(cboProductTypeSecurity));
            IncParRow.add(getTxtDepNo());
//            IncParRow.add(CommonUtil.convertObjToStr(cbmDepProdID.getKeyForSelected()));
//            IncParRow.add(getTdtDepDt());
            IncParRow.add(getTxtDepAmount());
//            IncParRow.add(getTxtRateOfInterest());
            IncParRow.add(getTxtMaturityValue());
//            IncParRow.add(getTxtMaturityDt());
            tblDepositTypeDetails.insertRow(tblDepositTypeDetails.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
    
    public void setReceiptDetails(HashMap map){
        HashMap chittalMap = new HashMap();
        chittalMap.put("CHITTAL_NO",getTxtChittalNo());
        chittalMap.put("SUB_NO",CommonUtil.convertObjToInt(getTxtSubNo())); //AJITH Changed From CommonUtil.convertObjToStr(getTxtSubNo())
        List lst = ClientUtil.executeQuery("getSelctApplnReceiptDetails", chittalMap);
        if(lst!=null && lst.size()>0){
            chittalMap = (HashMap)lst.get(0);            
//            setTxtLastInstNo(CommonUtil.convertObjToStr(chittalMap.get("INST_COUNT")));
//            setTdtLastInstDate(CommonUtil.convertObjToStr(chittalMap.get("LAST_TRANS_DT")));
//            setTxtTotalAmountTillDate(CommonUtil.convertObjToStr(chittalMap.get("TOTAL_BALANCE")));
            setTxtRemarks(CommonUtil.convertObjToStr(chittalMap.get("REMARKS")));            
//            setTxtOverDueInstallments(String.valueOf(CommonUtil.convertObjToLong(chittalMap.get("NO_OF_INSTALLMENTS")) -
//            CommonUtil.convertObjToLong(chittalMap.get("INST_COUNT"))));
//            double overDueAmt = (CommonUtil.convertObjToLong(chittalMap.get("NO_OF_INSTALLMENTS")) -
//            CommonUtil.convertObjToLong(chittalMap.get("INST_COUNT"))) * CommonUtil.convertObjToLong(chittalMap.get("INST_AMT"));
//            setTxtOverDueAmount(String.valueOf(overDueAmt));

//            setTxtnstallmentsPaid(CommonUtil.convertObjToStr(chittalMap.get("INST_COUNT")));
//            setTxtInstallmentsAmountPaid(CommonUtil.convertObjToStr(chittalMap.get("TOTAL_BALANCE")));
//            Date currDate = currDt.clone();
//            int instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
//            Date startDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalMap.get("SCHEME_START_DT")));
//            long totMember = 0;
//            HashMap prodMap = new HashMap();
//            prodMap.put("CHITTAL_NO",getTxtSchemeName());
//            lst = ClientUtil.executeQuery("getSchemeNameDetailsNoOfMember", prodMap);
//            if(lst!=null && lst.size()>0){
//                prodMap = (HashMap)lst.get(0);
//		totMember = CommonUtil.convertObjToLong(prodMap.get("NO_OF_MEMBER_PER_DIVISION"));
//            }
            
//            int stYear = startDate.getYear()+1900;
//            int stMonth = startDate.getMonth();
//            int currYear = currDate.getYear()+1900;
//            int currMonth = currDate.getMonth();
//            int value = 0;
//            int pending = 0;
//            double instAmt = CommonUtil.convertObjToDouble(chittalMap.get("INST_AMT")).doubleValue();
//            long count = CommonUtil.convertObjToLong(chittalMap.get("INST_COUNT"));
//            int totInst = CommonUtil.convertObjToInt(chittalMap.get("NO_OF_INSTALLMENTS"));
//            if(stYear == currYear && stMonth == currMonth){
//                value = 0;
//                pending = 0;
//            }else if(stYear == currYear && stMonth != currMonth){
//                int pendingCount = 0;
//                if(auctDay<currDate.getDate()){
//                    pendingCount = value;
//                    value = (currMonth+1) - (stMonth+1)+1;
//                    if(value == CommonUtil.convertObjToLong(getNoOfMemberPerDiv())){
//                        value = value -1;
//                    }
//                    setTxtInstallmentsDue(String.valueOf(value));
//                    setTxtInstallmentOverDueAmount(String.valueOf(instAmt * value));
//                }else{
//                    value = (currMonth+1) - (stMonth+1);                    
//                    setTxtInstallmentsDue(String.valueOf(value));
//                    setTxtInstallmentOverDueAmount(String.valueOf(instAmt * value));
//                }
//            }else{
//                int year = currYear - stYear;
//                value = (year * 12) + currMonth - stMonth;
//                pending = totInst - (int)count;
//                setTxtInstallmentOverDueAmount(String.valueOf(instAmt * pending));
//                setTxtInstallmentsDue(String.valueOf("0"));
//            }
        }
    }
        
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    public void populateMemberTypeDetails(String row){
        try{
            resetMemberTypeDetails();
            final MDSMemberTypeTO objMemberTypeTO = (MDSMemberTypeTO)memberTypeMap.get(row);
            populateMemberTableData(objMemberTypeTO);
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateMemberTableData(MDSMemberTypeTO objMemberTypeTO)  throws Exception{
        setTxtMemNo(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberNo()));
        setTxtMemType(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberType()));
        setTxtMemName(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberName()));
        setTxtContactNum(CommonUtil.convertObjToStr(objMemberTypeTO.getContactNo()));
        setTxtMemNetworth(CommonUtil.convertObjToStr(objMemberTypeTO.getNetworth()));
        setTxtMemPriority(CommonUtil.convertObjToStr(objMemberTypeTO.getPriority()));
        setChanged();
        notifyObservers();
    }
    
    public void populateDepositTypeDetails(String row){
        try{
            resetDepositTypeDetails();
            final MDSDepositTypeTO objDepositTypeTO = (MDSDepositTypeTO)depositTypeMap.get(row);
            populateDepositTableData(objDepositTypeTO);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void populateSocietyTypeDetails(String row){
        try{
            resetSocietyTypeDetails();
            final MDSSocietyTypeTO objMDSSocietyTypeTO = (MDSSocietyTypeTO)societyTypeMap.get(row);
            populateSocietyTableData(objMDSSocietyTypeTO);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateSocietyTableData(MDSSocietyTypeTO objMDSSocietyTypeTO)  throws Exception{
        setCboOtherInstituion(CommonUtil.convertObjToStr(getCbmOtherInstituion().getDataForKey(CommonUtil.convertObjToStr(objMDSSocietyTypeTO.getInstitution()))));
        setCboSecurityTypeSociety(CommonUtil.convertObjToStr(getCbmSecurityTypeSociety().getDataForKey(CommonUtil.convertObjToStr(objMDSSocietyTypeTO.getSecurityType()))));
        setTxtOtherInstituionName(CommonUtil.convertObjToStr(objMDSSocietyTypeTO.getName()));
        setTxtSecurityNoSociety(CommonUtil.convertObjToStr(objMDSSocietyTypeTO.getSecurityNo()));
        setTxtSecurityAmountSociety(CommonUtil.convertObjToStr(objMDSSocietyTypeTO.getAmount()));
        setTxtMaturityValueSociety(CommonUtil.convertObjToStr(objMDSSocietyTypeTO.getMaturityValue()));
        setTxtRemarksSociety(CommonUtil.convertObjToStr(objMDSSocietyTypeTO.getRemarks()));
        setTdtIssueDtSoceity(CommonUtil.convertObjToStr(objMDSSocietyTypeTO.getIssueDt()));
        setTdtMaturityDateSociety(CommonUtil.convertObjToStr(objMDSSocietyTypeTO.getMatDt()));
        setChanged();
        notifyObservers();
    }
    
    private void populateDepositTableData(MDSDepositTypeTO objDepositTypeTO)  throws Exception{
        setTxtDepNo(CommonUtil.convertObjToStr(objDepositTypeTO.getDepositNo()));
        setCboProductTypeSecurity(CommonUtil.convertObjToStr(getCbmProdTypeSecurity().getDataForKey(CommonUtil.convertObjToStr(objDepositTypeTO.getProdType()))));
        setCboDepProdID(CommonUtil.convertObjToStr(getCbmDepProdID().getDataForKey(CommonUtil.convertObjToStr(objDepositTypeTO.getProdId()))));
        setTxtDepAmount(CommonUtil.convertObjToStr(objDepositTypeTO.getAmount()));
        setTdtDepDt(CommonUtil.convertObjToStr(objDepositTypeTO.getDepositDt()));
        setTxtMaturityDt(CommonUtil.convertObjToStr(objDepositTypeTO.getMaturityDt()));
        setTxtMaturityValue(CommonUtil.convertObjToStr(objDepositTypeTO.getMaturityValue()));
        setTxtRateOfInterest(CommonUtil.convertObjToStr(objDepositTypeTO.getIntRate()));
       
        System.out.println("YOO CBXXXXX"+objDepositTypeTO.getIntRate());
        setCbxSamechittal(CommonUtil.convertObjToStr(objDepositTypeTO.getSameChittal()));
        setChanged();
        notifyObservers();
    }
    
    public void deleteMemberTableData(String val, int row){
        if(deletedMemberTypeMap == null){
            deletedMemberTypeMap = new LinkedHashMap();
        }
        MDSMemberTypeTO objMemberTypeTO = (MDSMemberTypeTO) memberTypeMap.get(val);
        objMemberTypeTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedMemberTypeMap.put(CommonUtil.convertObjToStr(tblMemberTypeDetails.getValueAt(row,0)),memberTypeMap.get(val));
        Object obj;
        obj=val;
        memberTypeMap.remove(val);
        tblMemberTypeDetails.setDataArrayList(null,tableTitle);
        try{
            populateMemberTable();
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateMemberTable()  throws Exception{
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(memberTypeMap.keySet());
        ArrayList addList =new ArrayList(memberTypeMap.keySet());
        int length = incDataList.size();
        for(int i=0; i<length; i++){
            ArrayList incTabRow = new ArrayList();
            MDSMemberTypeTO objMemberTypeTO = (MDSMemberTypeTO) memberTypeMap.get(addList.get(i));
            IncVal.add(objMemberTypeTO);
            if(!objMemberTypeTO.getStatus().equals("DELETED")){
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberType()));
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberName()));
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getContactNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getNetworth()));
                tblMemberTypeDetails.addRow(incTabRow);
            }
        }
        notifyObservers();
    }
    
    public void deleteDepositTableData(String val, int row){
        if(deletedDepositTypeMap == null){
            deletedDepositTypeMap = new LinkedHashMap();
        }
        MDSDepositTypeTO objDepositTypeTO = (MDSDepositTypeTO) depositTypeMap.get(val);
        objDepositTypeTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedDepositTypeMap.put(CommonUtil.convertObjToStr(tblDepositTypeDetails.getValueAt(row,1)),depositTypeMap.get(val));
        Object obj;
        obj=val;
        depositTypeMap.remove(val);
        tblDepositTypeDetails.setDataArrayList(null,tableTitleDepositList);
        try{
            populateDepositTable();
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void deleteSocietyTableData(String val, int row){
        if(deletedSocietyTypeMap== null){
            deletedSocietyTypeMap = new LinkedHashMap();
        }
        MDSSocietyTypeTO objMDSSocietyTypeTO = (MDSSocietyTypeTO) societyTypeMap.get(val);
        objMDSSocietyTypeTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedSocietyTypeMap.put(CommonUtil.convertObjToStr(tblOtherSocietyDetails.getValueAt(row,0)),societyTypeMap.get(val));
        Object obj;
        obj=val;
        societyTypeMap.remove(val);
        tblOtherSocietyDetails.setDataArrayList(null,tableTitleOtherSociety);
        try{
            populateSocietyTable();
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateSocietyTable()  throws Exception{
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(societyTypeMap.keySet());
        ArrayList addList =new ArrayList(societyTypeMap.keySet());
        int length = incDataList.size();
        for(int i=0; i<length; i++){
            ArrayList incTabRow = new ArrayList();
            MDSSocietyTypeTO objMDSSocietyTypeTO = (MDSSocietyTypeTO) societyTypeMap.get(addList.get(i));
            IncVal.add(objMDSSocietyTypeTO);
            if(!objMDSSocietyTypeTO.getStatus().equals("DELETED")){
                incTabRow.add(CommonUtil.convertObjToStr(objMDSSocietyTypeTO.getSecurityNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objMDSSocietyTypeTO.getSecurityType()));
                incTabRow.add(CommonUtil.convertObjToStr(objMDSSocietyTypeTO.getInstitution()));
                incTabRow.add(CommonUtil.convertObjToStr(objMDSSocietyTypeTO.getName()));
                incTabRow.add(CommonUtil.convertObjToStr(objMDSSocietyTypeTO.getAmount()));
                tblOtherSocietyDetails.addRow(incTabRow);
            }
        }
        notifyObservers();
    }
    
    private void populateDepositTable()  throws Exception{
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(depositTypeMap.keySet());
        ArrayList addList =new ArrayList(depositTypeMap.keySet());
        int length = incDataList.size();
        for(int i=0; i<length; i++){
            ArrayList incTabRow = new ArrayList();
            MDSDepositTypeTO objDepositTypeTO = (MDSDepositTypeTO) depositTypeMap.get(addList.get(i));
            IncVal.add(objDepositTypeTO);
            if(!objDepositTypeTO.getStatus().equals("DELETED")){
                incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getProdType()));
                incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getDepositNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getAmount()));
                incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getMaturityValue()));
                tblDepositTypeDetails.addRow(incTabRow);
            }
        }
        notifyObservers();
    }
    
    public void resetMemberTypeDetails() {
        setTxtMemNo("");
        setTxtMemName("");
        setTxtMemType("");
        setTxtContactNum("");
        setTxtMemNetworth("");
        setTxtMemPriority("");
        setChanged();
        ttNotifyObservers();
    }
    
        public void resetDepositTypeDetails() {
        setTxtDepNo("");
        setCboProductTypeSecurity("");
        setCboDepProdID("");
        setTdtDepDt("");
        setTxtDepAmount("");
        setTxtMaturityDt("");
        setTxtMaturityValue("");
        setTxtRateOfInterest("");
        setChanged();
        setCbxSamechittal("");
        ttNotifyObservers();
    }
    
    public void resetSocietyTypeDetails() {
        setCboOtherInstituion("");
        setCboSecurityTypeSociety("");
        setTxtOtherInstituionName("");
        setTxtSecurityNoSociety("");
        setTxtSecurityAmountSociety("");
        setTxtMaturityValueSociety("");
        setTxtRemarksSociety("");
        setTdtIssueDtSoceity("");
        setTdtMaturityDateSociety("");
        setChanged();
        ttNotifyObservers();
    }
    
    public void resetForm(){
        memberTypeMap = null;
        deletedMemberTypeMap = null;
        depositTypeMap = null;
        deletedDepositTypeMap = null;
        societyTypeMap = null;
        deletedSocietyTypeMap = null;
        setTdtPayDt("");
        setTxtPrizedAmount(null);   //AJITH
        setTxtResolutionNo("");
        setTdtResolutionDt("");
        setTxtBordNo("");
        setTdtBordDt("");
        setRdoSalaryRecovery("");
        setLockStatus("");
        setTxtCustomerIdCr("");
        setTxtChittalNo("");
        setTxtDivisionNo(null); //AJITH
        setTxtSubNo(null); //AJITH
        //setTxtPrizedAmount("");  //AJITH Blocked because same code exists in Line 1822
        setTxtPinCode(null); //AJITH
        setTxtContactNo("");
        setTxtTotalSalary(null); //AJITH
        setTdtChitStartDt("");
        setTdtChitCloseDt("");
        setTxtMemberNo("");
        setLblMemberName("");
        setLblMemberType("");
        setLblNomineeName("");
        setTdtPayDt("");
        setTxtResolutionNo("");
        setTdtResolutionDt("");
        setTxtBordNo("");
        setTdtBordDt("");
        setTxtSalaryCertificateNo("");
        setTxtEmployerName("");
        setTxtAddress("");
        setCboCity("");
        setTxtDesignation("");
        setTdtRetirementDt("");
        setTxtMemberNum("");
        setTxtNetWorth("");
        setTxtSalaryRemark("");
        setCboSecurityType("");
        setLblSecurityValue("");
        setTxtSecurityRemarks("");
        setRdoGoldSecurityStockExists("N"); // Added by nithya on 07-03-2020 for KD-1379
        setTxtGoldSecurityId("");
        setTxtJewelleryDetails("");
        setTxtGrossWeight(null);    //AJITH
        setTxtNetWeight(null);  //AJITH
        setTxtValueOfGold("");
        setTxtGoldRemarks("");
        setUserDefinedAuction("N"); //Added by nithya on 23-12-2019 for KD-1074
        setChkStandingInstn(false);
        getCbmProdType().setKeyForSelected("");
        getCbmProdTypeSecurity().setKeyForSelected("");
        getCbmProdId().setKeyForSelected("");
        setTxtCustomerIdCr("");
        collateralMap = null;
        deletedCollateralMap = null;
        resetTableValues();
        resetCaseTableValues();
        tblJointCollateral.setDataArrayList(null,tableCollateralJointTitle);
        tblCollateralDetails.setDataArrayList(null,tableCollateralTitle);
        setChanged();
        ttNotifyObservers();
    }
    
    public void resetCollateralDetails() {
        setTxtOwnerMemNo("");
        setTxtOwnerMemberNname("");
        setTxtDocumentNo("");
        setTxtDocumentType("");
        setTdtDocumentDate("");
        setTxtRegisteredOffice("");
        setCboPledge("");
        setTdtPledgeDate("");
        setTxtPledgeNo("");
        setTxtPledgeAmount("");
        setTxtVillage("");
        setTxtSurveyNo("");
        setTxtTotalArea("");
        setCboNature("");
        setCboRight("");
        setCboDocumentType("");
        setTxtAreaParticular("");
        setRdoGahanNo(false);
        setRdoGahanYes(false);
        setTxtPledgeType("");
    }
    
    public void resetTableValues(){
        tblMemberTypeDetails.setDataArrayList(null,tableTitle);
        tblDepositTypeDetails.setDataArrayList(null,tableTitleDepositList);
        tblOtherSocietyDetails.setDataArrayList(null,tableTitleOtherSociety);
    }
    
    /** To retrieve a particular customer's accountclosing record */
    public void getData(HashMap whereMap,NomineeOB objNomineeOB) {
        try{
            final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
            System.out.println("#@@%@@#%@#data"+data);
            objMaintenanceTO = (MDSMasterMaintenanceTO) ((List) data.get("MasterMaintenanceListTO")).get(0);
            populateMasterMaintenanceData(objMaintenanceTO);
            System.out.println(" ");
            if(((List)data.get("SalarySecDetails")).size()>0)
            {
                setSalarySecDetails(((List)data.get("SalarySecDetails")));
            }
            
            if (((List) data.get("TermLoanCaseDetailTO")).size() > 0){
                if(data.containsKey("TermLoanCaseDetailTO")){
                    setTermLoanCaseDetailTable((ArrayList) (data.get("TermLoanCaseDetailTO")), getTxtChittalNo()); 
                }
            }
            if(data.containsKey("memberListTO")){
                memberTypeMap = (LinkedHashMap)data.get("memberListTO");
                ArrayList addList =new ArrayList(memberTypeMap.keySet());
                for(int i=0;i<addList.size();i++){
                    MDSMemberTypeTO  objMemberTypeTO = (MDSMemberTypeTO)  memberTypeMap.get(addList.get(i));
                    objMemberTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    ArrayList incTabRow = new ArrayList();
                    incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberNo()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberName()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getMemberType()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getContactNo()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getNetworth()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMemberTypeTO.getPriority()));
                    tblMemberTypeDetails.addRow(incTabRow);
                }
            }
            
            if(data.containsKey("depositListTO")){
                depositTypeMap = (LinkedHashMap)data.get("depositListTO");
                ArrayList addList =new ArrayList(depositTypeMap.keySet());
                for(int i=0;i<addList.size();i++){
                    MDSDepositTypeTO  objDepositTypeTO = (MDSDepositTypeTO)  depositTypeMap.get(addList.get(i));
                    objDepositTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    ArrayList incTabRow = new ArrayList();
                    incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getProdType()));
                    incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getDepositNo()));
//                    incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getProdId()));
//                    incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getDepositDt()));
                    incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getAmount()));
//                    incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getIntRate()));
                    incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getMaturityValue()));
//                    incTabRow.add(CommonUtil.convertObjToStr(objDepositTypeTO.getMaturityDt()));
                    tblDepositTypeDetails.addRow(incTabRow);
                }
            }                        
            
            if(data.containsKey("societyListTO")){
                societyTypeMap = (LinkedHashMap)data.get("societyListTO");
                ArrayList addList =new ArrayList(societyTypeMap.keySet());
                for(int i=0;i<addList.size();i++){
                    MDSSocietyTypeTO objMDSSocietyTypeTO = (MDSSocietyTypeTO)  societyTypeMap.get(addList.get(i));
                    objMDSSocietyTypeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    ArrayList incTabRow = new ArrayList();
                    incTabRow.add(CommonUtil.convertObjToStr(objMDSSocietyTypeTO.getSecurityNo()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMDSSocietyTypeTO.getSecurityType()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMDSSocietyTypeTO.getInstitution()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMDSSocietyTypeTO.getName()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMDSSocietyTypeTO.getAmount()));
                    tblOtherSocietyDetails.addRow(incTabRow);
                }
            }
            
            if(data.containsKey("CollateralListTO")){
                collateralMap = (LinkedHashMap)data.get("CollateralListTO");
                ArrayList addList =new ArrayList(collateralMap.keySet());
                for(int i=0;i<addList.size();i++){
                    MDSMasterSecurityLandTO  objMDSMasterSecurityLandTO = (MDSMasterSecurityLandTO)  collateralMap.get(addList.get(i));
                    objMDSMasterSecurityLandTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    ArrayList incTabRow = new ArrayList();
                    incTabRow.add(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getMemberNo()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getMemberName()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getDocumentNo()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getPledgeAmount()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getSurveyNo()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getTotalArea()));
                    incTabRow.add(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getPledgeNo()));
                    tblCollateralDetails.addRow(incTabRow);
                    //collateralMap.put(addList.get(i),objMDSMasterSecurityLandTO);
                     collateralMap.put(CommonUtil.convertObjToStr(objMDSMasterSecurityLandTO.getMemberNo())+"_"+(i+1),objMDSMasterSecurityLandTO);
                        
                       // addPledgeAmountMap(objTermLoanSecurityLandTO.getDocumentNo(),CommonUtil.convertObjToDouble(objTermLoanSecurityLandTO.getPledgeAmount()).doubleValue());
                    addPledgeAmountMap(objMDSMasterSecurityLandTO.getDocumentNo(),CommonUtil.convertObjToDouble(objMDSMasterSecurityLandTO.getPledgeAmount()).doubleValue());
                }
            }
            
            if(getChkNominee()==true && data.containsKey("AccountNomineeList")){
                ArrayList nomineeList = new ArrayList();
                nomineeList =  (ArrayList) data.get("AccountNomineeList");
                NomineeTO objAccountNomineeTO = new NomineeTO();
                objAccountNomineeTO = (NomineeTO) nomineeList.get(0);
                this.setLblNomineeName(CommonUtil.convertObjToStr(objAccountNomineeTO.getNomineeName()));
                objNomineeOB.setNomimeeList(nomineeList);
                objNomineeOB.setNomineeTabData();
                objNomineeOB.ttNotifyObservers();
            }
            // Added by nithya on 07-03-2020 for KD-1379
            if(data.containsKey("CustomerGoldStockSecurityTO")){
                LoansSecurityGoldStockTO objLoansSecurityGoldStockTO = (LoansSecurityGoldStockTO) data.get("CustomerGoldStockSecurityTO");
                setRdoGoldSecurityStockExists("Y");    
                setTxtGoldSecurityId(objLoansSecurityGoldStockTO.getGoldStockId());                
           }            
            // End
            
             //trans details
            System.out.println("mapdata>>>>>1234"+data);
             if(data.containsKey("TRANSACTION_LIST")){
                List list = (List) data.get("TRANSACTION_LIST");
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
            }
            //end..
            
            
            
            
            
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateMasterMaintenanceData(MDSMasterMaintenanceTO objMaintenanceTO) throws Exception{
        this.setTxtSchemeName(CommonUtil.convertObjToStr(objMaintenanceTO.getSchemeName()));
        this.setTxtChittalNo(CommonUtil.convertObjToStr(objMaintenanceTO.getChittalNo()));
        this.setTxtDivisionNo(objMaintenanceTO.getDivisionNo());  //AJITH
        this.setTxtSubNo(objMaintenanceTO.getSubNo());  //AJITH
        this.setRdoSalaryRecovery(CommonUtil.convertObjToStr(objMaintenanceTO.getSalaryRecovery()));
        this.setLockStatus(CommonUtil.convertObjToStr(objMaintenanceTO.getLockStatus()));
//        this.setTxtPrizedAmount(CommonUtil.convertObjToStr(objMaintenanceTO.getPrizedAmount()));
//        this.setTxtTotalAmountTillDate(CommonUtil.convertObjToStr(objMaintenanceTO.getTotalAmount()));
//        this.setTxtOverDueInstallments(CommonUtil.convertObjToStr(objMaintenanceTO.getInstallmentDue()));
//        this.setTxtOverDueAmount(CommonUtil.convertObjToStr(objMaintenanceTO.getInstalOverdueAmt()));
        this.setTxtPinCode(objMaintenanceTO.getPin());  //AJITH
        this.setTxtContactNo(CommonUtil.convertObjToStr(objMaintenanceTO.getContactNo()));
        this.setTxtTotalSalary(objMaintenanceTO.getTotalSalary());  //AJITH
        this.setTdtChitStartDt(CommonUtil.convertObjToStr(objMaintenanceTO.getChitStartDt()));
        if(objMaintenanceTO.getChitCloseDt() == null){
                this.setTdtChitCloseDt(null);
        }else{
            this.setTdtChitCloseDt(CommonUtil.convertObjToStr(objMaintenanceTO.getChitCloseDt()));
        }
        this.setTxtMemberNo(CommonUtil.convertObjToStr(objMaintenanceTO.getMemberNo()));
        this.setLblMemberName(CommonUtil.convertObjToStr(objMaintenanceTO.getMemberName()));
        this.setLblMemberType(CommonUtil.convertObjToStr(objMaintenanceTO.getMemberType()));
        this.setLblNomineeName(CommonUtil.convertObjToStr(objMaintenanceTO.getNomineeName()));
//        this.setTdtPayDt(CommonUtil.convertObjToStr(objMaintenanceTO.getPayDt()));
        this.setTxtResolutionNo(CommonUtil.convertObjToStr(objMaintenanceTO.getResolutionNo()));
        this.setTdtResolutionDt(CommonUtil.convertObjToStr(objMaintenanceTO.getResolutionDt()));
        this.setTxtBordNo(CommonUtil.convertObjToStr(objMaintenanceTO.getBondNo()));
        System.out.println("objMaintenanceTO.getBondNo()"+objMaintenanceTO.getBondNo());
        this.setTdtBordDt(CommonUtil.convertObjToStr(objMaintenanceTO.getBondDt()));
//        this.setTxtLastInstNo(CommonUtil.convertObjToStr(objMaintenanceTO.getLastInstallmentNo()));
//        this.setTdtLastInstDate(CommonUtil.convertObjToStr(objMaintenanceTO.getLastInstallmentDt()));
//        this.setTxtRemarks(CommonUtil.convertObjToStr(objMaintenanceTO.getInstalRemarks()));
        this.setTxtSalaryCertificateNo(CommonUtil.convertObjToStr(objMaintenanceTO.getSalaryCerficateNo()));
        this.setTxtEmployerName(CommonUtil.convertObjToStr(objMaintenanceTO.getEmpName()));
        this.setTxtAddress(CommonUtil.convertObjToStr(objMaintenanceTO.getEmpAddress()));
        this.setCboCity(CommonUtil.convertObjToStr(objMaintenanceTO.getCity()));
        this.setTxtDesignation(CommonUtil.convertObjToStr(objMaintenanceTO.getDesignation()));
        this.setTdtRetirementDt(CommonUtil.convertObjToStr(objMaintenanceTO.getRetirementDt()));
        this.setTxtMemberNum(CommonUtil.convertObjToStr(objMaintenanceTO.getEmpMemberNo()));
        this.setTxtNetWorth(CommonUtil.convertObjToStr(objMaintenanceTO.getNetworth()));
        this.setTxtSalaryRemark(CommonUtil.convertObjToStr(objMaintenanceTO.getSalaryRemarks()));
        this.setCboSecurityType(CommonUtil.convertObjToStr(objMaintenanceTO.getSecurityType()));
        this.setLblSecurityValue(CommonUtil.convertObjToStr(objMaintenanceTO.getSecurityValue()));
        this.setTxtSecurityRemarks(CommonUtil.convertObjToStr(objMaintenanceTO.getSecurityRemarks()));
        this.setTxtJewelleryDetails(CommonUtil.convertObjToStr(objMaintenanceTO.getJewellaryDetails()));
        this.setTxtGrossWeight(objMaintenanceTO.getGrossWeight());  //AJITH
        this.setTxtNetWeight(objMaintenanceTO.getNetWeight());  //AJITH
        this.setTxtValueOfGold(CommonUtil.convertObjToStr(objMaintenanceTO.getGoldValue()));
        this.setTxtGoldRemarks(CommonUtil.convertObjToStr(objMaintenanceTO.getGoldRemarks()));
        this.setApplicationNo(CommonUtil.convertObjToStr(objMaintenanceTO.getApplNo()));
        this.setApplicationDate(CommonUtil.convertObjToStr(objMaintenanceTO.getApplDate()));
        if(objMaintenanceTO.getDefaulter()!=null && !objMaintenanceTO.getDefaulter().equals(""))
        this.setCbxDefaulter(CommonUtil.convertObjToStr(objMaintenanceTO.getDefaulter()));
        else
        this.setCbxDefaulter("N"); 
        
        this.setApplicationSecurityRemarks(CommonUtil.convertObjToStr(objMaintenanceTO.getApplicationSecurityRemarks()));
       
//        if(objMaintenanceTO.getStandingIns().equals("Y")){
//            setChkStandingInstn(true);
//            getCbmProdType().setKeyForSelected(objMaintenanceTO.getProdType());
//            getCbmProdId().setKeyForSelected(objMaintenanceTO.getProdId());
//            setTxtCustomerIdCr(objMaintenanceTO.getDrAccNo());
//        }else{
//            setChkStandingInstn(false);
//        }
        if(objMaintenanceTO.getNominee().equals("Y")){
            this.setChkNominee(true);
        }else{
            this.setChkNominee(false);
        }
        setChanged();
        notifyObservers();
    }
    
    /** To perform the necessary operation */
    public void doAction(NomineeOB objNomineeOB) {
        try {
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null){
                doActionPerform(objNomineeOB);
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    
    /** To perform the necessary action */
    private void doActionPerform(NomineeOB objNomineeOB) throws Exception{
        
        final HashMap data = new HashMap();
        data.put("COMMAND", getCommand());
        String command = getCommand();
        System.out.println("get_authorizeMap()" + get_authorizeMap() + "command" + command);
        if (get_authorizeMap() == null) {
            System.out.println("fhfhgfh");
            objMaintenanceTO = setMasterMaintenanceTOData();
            data.put("MasterMaintenanceDetailsTOData", objMaintenanceTO);
            data.put("MemberTableDetails", memberTypeMap);
            data.put("DepositTableDetails", depositTypeMap);
            data.put("SocietyTableDetails", societyTypeMap);
            data.put("TermLoanCaseDetailsTO", caseDetaillMap);
            if (!getSalarySecDetails().isEmpty()) {
                data.put("SalarySecDetails", getSalarySecDetails());
            }
            if (deletedMemberTypeMap != null && deletedMemberTypeMap.size() > 0) {
                data.put("deletedMemberTypeData", deletedMemberTypeMap);
            }
            if (deletedDepositTypeMap != null && deletedDepositTypeMap.size() > 0) {
                data.put("deletedDepositTypeData", deletedDepositTypeMap);
            }
            if (deletedSocietyTypeMap != null && deletedSocietyTypeMap.size() > 0) {
                data.put("deletedSocietyTypeData", deletedSocietyTypeMap);
            }
            if (collateralMap != null && collateralMap.size() > 0) {
                data.put("CollateralTableDetails", collateralMap);
            }
            if (deletedCollateralMap != null && deletedCollateralMap.size() > 0) {
                data.put("deletedCollateralTypeData", deletedCollateralMap);
            }
            if (objMaintenanceTO.getNominee().equals("Y")) {
                data.put("AccountNomineeTO", objNomineeOB.getNomimeeList());
                data.put("AccountNomineeDeleteTO", objNomineeOB.getDeleteNomimeeList());
            }
            if (getChkStandingInstn() == true) {
                data.put("STANDING_INS", "Y");
                data.put("PROD_TYPE", CommonUtil.convertObjToStr(getCbmProdType().getKeyForSelected()));
                data.put("PROD_ID", CommonUtil.convertObjToStr(getCbmProdId().getKeyForSelected()));
                data.put("DR_ACT_NO", getTxtCustomerIdCr());
            } else {
                data.put("STANDING_INS", "N");
                data.put("PROD_TYPE", "");
                data.put("PROD_ID", "");
                data.put("DR_ACT_NO", "");
            }
        } else {
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        System.out.println("command" + command);
        //trans details
        if (!command.equals(CommonConstants.AUTHORIZESTATUS)) {
            if (transactionDetailsTO == null) {
                transactionDetailsTO = new LinkedHashMap();
            }
            if (deletedTransactionDetailsTO != null) {
                transactionDetailsTO.put(DELETED_TRANS_TOs, deletedTransactionDetailsTO);
                deletedTransactionDetailsTO = null;
            }
            transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
            allowedTransactionDetailsTO = null;
            System.out.println("transaction details1111111111...>>." + transactionDetailsTO);
            data.put("TransactionTO", transactionDetailsTO);
        }
            if(getAuthMap() != null && getAuthMap().size() > 0 ){
                    if( getAuthMap() != null){
                data.put(CommonConstants.AUTHORIZEMAP, getAuthMap());
                    }
                    if(allowedTransactionDetailsTO!=null && allowedTransactionDetailsTO.size()>0){
                        if (transactionDetailsTO == null){
                            transactionDetailsTO = new LinkedHashMap();
                        }
                        transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
                        System.out.println("transaction details222222222...>>."+transactionDetailsTO);
                data.put("TransactionTO",transactionDetailsTO);
                        allowedTransactionDetailsTO = null;
                    }
                    authMap = null;
                }
            System.out.println(" gettransloanamt>>>5645@@@@..."+transactionOB.getTxtDebitAccNo());
            System.out.println(" gettranstype>>>5645..."+transactionOB.getCboTransType());
            System.out.println(" getNewTransactionMap>>>5645..."+ getNewTransactionMap());
             if(getNewTransactionMap() != null ){
            data.put("Transaction Details Data", getNewTransactionMap());
        }
            System.out.println("getFinalChrageMap>>>..."+getFinalChrageMap());
        if(getChargelst() != null && getChargelst().size()>0){ //charge details
            data.put("Charge List Data", getChargelst());
        }
         // Added by nithya on 07-03-2020 for KD-1379
          if(rdoGoldSecurityStockExists.equalsIgnoreCase("Y")){
            LoansSecurityGoldStockTO objCustomerGoldStockSecurityTO = getCustomerGoldStockSecurityTO();
            data.put("CUST_GOLD_SECURITY_STOCK", "CUST_GOLD_SECURITY_STOCK");
            data.put("CustomerGoldStockSecurityTO",objCustomerGoldStockSecurityTO);
         }
        // end 
        
        //if(getFinalChrageMap() != null && getFinalChrageMap().size()>0){ //charge details
        //    data.put("Charge List Data", getFinalChrageMap());
        //} 
         System.out.println("data   >>>==="+data +" Msap======"+map);
        data.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        data.put(CommonConstants.SCREEN,getScreen());
        System.out.println("data in FixedAssets OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        // setProxyReturnMap(proxyResultMap);
        System.out.println("data in LoanApplication OB return... : " + proxyResultMap);
        System.out.println(" gettransloanamt>>>5645@@@@444..." + transactionOB.getTxtDebitAccNo());
        System.out.println(" gettranstype>>>5645@4444..." + transactionOB.getCboTransType());
        System.out.println(" getCommand>>>>>>>..." + getCommand());
        System.out.println("getCbxOnlyAppliction()" + getCbxOnlyAppliction());
        if (proxyResultMap != null && proxyResultMap.containsKey("APPLICATION_NO")) {

//        if (proxyResultMap != null  &&  getCommand()!=null && getCommand().equalsIgnoreCase("INSERT"))
            if ((proxyResultMap.get("APPLICATION_NO") != null) && getCbxOnlyAppliction().equals("Y")) {
                ClientUtil.showMessageWindow("Application No. : " + CommonUtil.convertObjToStr(proxyResultMap.get("APPLICATION_NO")));
            }
//        if (proxyResultMap != null  &&  getCommand()!=null && getCommand().equalsIgnoreCase("UPDATE")){
//            ClientUtil.showMessageWindow("Updated Application No. : " + CommonUtil.convertObjToStr(proxyResultMap.get("APPLICATION_NO")));
//        }
//        if (proxyResultMap != null  &&  getCommand()!=null && getCommand().equalsIgnoreCase("DELETE")){
//            ClientUtil.showMessageWindow("Deleted Application No. : " + CommonUtil.convertObjToStr(proxyResultMap.get("APPLICATION_NO")));
//        }
        }
        if (getResult() != 4) {
            chargelst = null;
        }
        _authorizeMap = null;
        setResult(getActionType());
        setResult(actionType);
    }
    
    
    
    /** To populate data into the screen */
    public MDSMasterMaintenanceTO setMasterMaintenanceTOData() {
        
        final MDSMasterMaintenanceTO objMaintenanceTO = new MDSMasterMaintenanceTO();
        try{
            objMaintenanceTO.setSchemeName(getTxtSchemeName());
            objMaintenanceTO.setChittalNo(getTxtChittalNo());
            objMaintenanceTO.setDivisionNo(getTxtDivisionNo());
            objMaintenanceTO.setSubNo(getTxtSubNo());
            objMaintenanceTO.setPrizedAmount(getTxtPrizedAmount());
            objMaintenanceTO.setTotalAmount(getTxtTotalAmountTillDate());
            objMaintenanceTO.setInstallmentDue(getTxtOverDueInstallments());
            objMaintenanceTO.setInstalOverdueAmt(getTxtOverDueAmount());
//            objMaintenanceTO.setPin(getTxtPinCode());
//            objMaintenanceTO.setContactNo(getTxtContactNo());
//            objMaintenanceTO.setTotalSalary(getTxtTotalSalary());
            objMaintenanceTO.setChitStartDt(DateUtil.getDateMMDDYYYY(getTdtChitStartDt()));
            objMaintenanceTO.setChitCloseDt(DateUtil.getDateMMDDYYYY(getTdtChitCloseDt()));
            objMaintenanceTO.setMemberNo(getTxtMemberNo());
            objMaintenanceTO.setMemberName(getLblMemberName());
            objMaintenanceTO.setMemberType(getLblMemberType());
            objMaintenanceTO.setNomineeName(getLblNomineeName());
            objMaintenanceTO.setPayDt(DateUtil.getDateMMDDYYYY(getTdtPayDt()));
            objMaintenanceTO.setResolutionNo(getTxtResolutionNo());
            objMaintenanceTO.setResolutionDt(DateUtil.getDateMMDDYYYY(getTdtResolutionDt()));
            objMaintenanceTO.setBondNo(getTxtBordNo());
            objMaintenanceTO.setBondDt(DateUtil.getDateMMDDYYYY(getTdtBordDt()));
            objMaintenanceTO.setLastInstallmentNo(getTxtLastInstNo());
            objMaintenanceTO.setLastInstallmentDt(DateUtil.getDateMMDDYYYY(getTdtLastInstDate()));
            objMaintenanceTO.setInstalRemarks(getTxtRemarks());
            objMaintenanceTO.setBondSet(getBondNoSet());
            objMaintenanceTO.setApplicationSet(getApplicationSet());
//            objMaintenanceTO.setSalaryCerficateNo(getTxtSalaryCertificateNo());
//            objMaintenanceTO.setEmpName(getTxtEmployerName());
//            objMaintenanceTO.setEmpAddress(getTxtAddress());
//            objMaintenanceTO.setCity(getCboCity());
//            objMaintenanceTO.setDesignation(getTxtDesignation());
//            objMaintenanceTO.setRetirementDt(DateUtil.getDateMMDDYYYY(getTdtRetirementDt()));
//            objMaintenanceTO.setEmpMemberNo(getTxtMemberNum());
//            objMaintenanceTO.setNetworth(getTxtNetWorth());
//            objMaintenanceTO.setSalaryRemarks(getTxtSalaryRemark());
            objMaintenanceTO.setSecurityType(getCboSecurityType());
            objMaintenanceTO.setSecurityValue(getLblSecurityValue());
            objMaintenanceTO.setSecurityRemarks(getTxtSecurityRemarks());
            objMaintenanceTO.setJewellaryDetails(getTxtJewelleryDetails());
            objMaintenanceTO.setGrossWeight(getTxtGrossWeight());
            objMaintenanceTO.setNetWeight(getTxtNetWeight());
            objMaintenanceTO.setGoldValue(getTxtValueOfGold());
            objMaintenanceTO.setGoldRemarks(getTxtGoldRemarks());
            objMaintenanceTO.setSalaryRecovery(getRdoSalaryRecovery());
           objMaintenanceTO.setApplNo(getApplicationNo());
           objMaintenanceTO.setApplDate(DateUtil.getDateMMDDYYYY(getApplicationDate()));
           objMaintenanceTO.setDefaulter(getCbxDefaulter());
            if(getChkNominee() == true){
                objMaintenanceTO.setNominee("Y");
            }else{
                objMaintenanceTO.setNominee("N");
            }
            objMaintenanceTO.setStatus(getAction());
            objMaintenanceTO.setStatusBy(TrueTransactMain.USER_ID);
//            objMaintenanceTO.setStatusDt(ClientUtil.getCurrentDateWithTime());
            objMaintenanceTO.setStatusDt(currDt);
            objMaintenanceTO.setBranchCode(TrueTransactMain.BRANCH_ID);
            objMaintenanceTO.setApplNo(getApplicationNo());
            objMaintenanceTO.setOnlyApplication(getCbxOnlyAppliction());
            objMaintenanceTO.setApplicationSecurityRemarks(getApplicationSecurityRemarks());
            System.out.println("objMaintenanceTO..."+objMaintenanceTO);
        }catch(Exception e){
            log.info("Error In setMDSMasterMaintenanceTOData()");
            e.printStackTrace();
        }
        return objMaintenanceTO;
    }
    
    
    private String getCommand(){
        String command = null;
        System.out.println("actionType : " + actionType);
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                command = CommonConstants.TOSTATUS_INSERT;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                command = CommonConstants.TOSTATUS_UPDATE;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                command = CommonConstants.TOSTATUS_DELETE;
                break;
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                command = CommonConstants.STATUS_AUTHORIZED;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                command = CommonConstants.STATUS_REJECTED;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                command = CommonConstants.STATUS_EXCEPTION;
            default:
        }
        // System.out.println("command : " + command);
        return command;
    }
    
    private String getAction(){
        String action = null;
        // System.out.println("actionType : " + actionType);
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                action = CommonConstants.STATUS_CREATED;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                action = CommonConstants.STATUS_MODIFIED;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                action = CommonConstants.STATUS_DELETED;
                break;
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                action = CommonConstants.STATUS_AUTHORIZED;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                action = CommonConstants.STATUS_REJECTED;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                action = CommonConstants.STATUS_EXCEPTION;
                break;
            default:
        }
        // System.out.println("command : " + command);
        return action;
    }
    
    
    
    
    // Setter method for txtRemarks
    void setTxtRemarks(String txtRemarks){
        this.txtRemarks = txtRemarks;
        setChanged();
    }
    // Getter method for txtRemarks
    String getTxtRemarks(){
        return this.txtRemarks;
    }

    public Double getTxtOverDueAmount() {
        return txtOverDueAmount;
    }

    public void setTxtOverDueAmount(Double txtOverDueAmount) {
        this.txtOverDueAmount = txtOverDueAmount;
        setChanged();
    }

    public Double getTxtTotalAmountTillDate() {
        return txtTotalAmountTillDate;
    }

    public void setTxtTotalAmountTillDate(Double txtTotalAmountTillDate) {
        this.txtTotalAmountTillDate = txtTotalAmountTillDate;
        setChanged();
    }

    // Setter method for txtLastInstNo
    void setTxtLastInstNo(String txtLastInstNo){
        this.txtLastInstNo = txtLastInstNo;
        setChanged();
    }
    // Getter method for txtLastInstNo
    String getTxtLastInstNo(){
        return this.txtLastInstNo;
    }

    public Integer getTxtOverDueInstallments() {
        return txtOverDueInstallments;
    }

    public void setTxtOverDueInstallments(Integer txtOverDueInstallments) {
        this.txtOverDueInstallments = txtOverDueInstallments;
        setChanged();
    }

    // Setter method for tdtLastInstDate
    void setTdtLastInstDate(String tdtLastInstDate){
        this.tdtLastInstDate = tdtLastInstDate;
        setChanged();
    }
    // Getter method for tdtLastInstDate
    String getTdtLastInstDate(){
        return this.tdtLastInstDate;
    }
    
    // Setter method for txtChittalNo
    void setTxtChittalNo(String txtChittalNo){
        this.txtChittalNo = txtChittalNo;
        setChanged();
    }
    // Getter method for txtChittalNo
    String getTxtChittalNo(){
        return this.txtChittalNo;
    }
    
    // Setter method for tdtChitStartDt
    void setTdtChitStartDt(String tdtChitStartDt){
        this.tdtChitStartDt = tdtChitStartDt;
        setChanged();
    }
    // Getter method for tdtChitStartDt
    String getTdtChitStartDt(){
        return this.tdtChitStartDt;
    }
        // Setter method for tdtChitCloseDt
    void setTdtChitCloseDt(String tdtChitCloseDt){
        this.tdtChitCloseDt = tdtChitCloseDt;
        setChanged();
    }
    // Getter method for tdtChitCloseDt
    String getTdtChitCloseDt(){
        return this.tdtChitCloseDt;
    }

    public Integer getTxtDivisionNo() {
        return txtDivisionNo;
    }

    public void setTxtDivisionNo(Integer txtDivisionNo) {
        this.txtDivisionNo = txtDivisionNo;
        setChanged();
    }

    // Setter method for txtSchemeName
    void setTxtSchemeName(String txtSchemeName){
        this.txtSchemeName = txtSchemeName;
        setChanged();
    }
    // Getter method for txtSchemeName
    String getTxtSchemeName(){
        return this.txtSchemeName;
    }
    
    // Setter method for txtMemberNo
    void setTxtMemberNo(String txtMemberNo){
        this.txtMemberNo = txtMemberNo;
        setChanged();
    }
    // Getter method for txtMemberNo
    String getTxtMemberNo(){
        return this.txtMemberNo;
    }
    
    // Setter method for txtBordNo
    void setTxtBordNo(String txtBordNo){
        this.txtBordNo = txtBordNo;
        setChanged();
    }
    // Getter method for txtBordNo
    String getTxtBordNo(){
        return this.txtBordNo;
    }
    
    // Setter method for tdtBordDt
    void setTdtBordDt(String tdtBordDt){
        this.tdtBordDt = tdtBordDt;
        setChanged();
    }
    // Getter method for tdtBordDt
    String getTdtBordDt(){
        return this.tdtBordDt;
    }

    public Double getTxtPrizedAmount() {
        return txtPrizedAmount;
    }

    public void setTxtPrizedAmount(Double txtPrizedAmount) {
        this.txtPrizedAmount = txtPrizedAmount;
        setChanged();
    }

    // Setter method for tdtResolutionDt
    void setTdtResolutionDt(String tdtResolutionDt){
        this.tdtResolutionDt = tdtResolutionDt;
        setChanged();
    }
    // Getter method for tdtResolutionDt
    String getTdtResolutionDt(){
        return this.tdtResolutionDt;
    }
    
    // Setter method for tdtPayDt
    void setTdtPayDt(String tdtPayDt){
        this.tdtPayDt = tdtPayDt;
        setChanged();
    }
    // Getter method for tdtPayDt
    String getTdtPayDt(){
        return this.tdtPayDt;
    }
    
    // Setter method for txtResolutionNo
    void setTxtResolutionNo(String txtResolutionNo){
        this.txtResolutionNo = txtResolutionNo;
        setChanged();
    }
    // Getter method for txtResolutionNo
    String getTxtResolutionNo(){
        return this.txtResolutionNo;
    }
    
    // Setter method for txtContactNo
    void setTxtContactNo(String txtContactNo){
        this.txtContactNo = txtContactNo;
        setChanged();
    }
    // Getter method for txtContactNo
    String getTxtContactNo(){
        return this.txtContactNo;
    }
    
    // Setter method for txtMemberNum
    void setTxtMemberNum(String txtMemberNum){
        this.txtMemberNum = txtMemberNum;
        setChanged();
    }
    // Getter method for txtMemberNum
    String getTxtMemberNum(){
        return this.txtMemberNum;
    }
    
    // Setter method for txtSalaryRemark
    void setTxtSalaryRemark(String txtSalaryRemark){
        this.txtSalaryRemark = txtSalaryRemark;
        setChanged();
    }
    // Getter method for txtSalaryRemark
    String getTxtSalaryRemark(){
        return this.txtSalaryRemark;
    }
    
    // Setter method for txtDesignation
    void setTxtDesignation(String txtDesignation){
        this.txtDesignation = txtDesignation;
        setChanged();
    }
    // Getter method for txtDesignation
    String getTxtDesignation(){
        return this.txtDesignation;
    }
    
    // Setter method for txtAddress
    void setTxtAddress(String txtAddress){
        this.txtAddress = txtAddress;
        setChanged();
    }
    // Getter method for txtAddress
    String getTxtAddress(){
        return this.txtAddress;
    }
    
    // Setter method for txtEmployerName
    void setTxtEmployerName(String txtEmployerName){
        this.txtEmployerName = txtEmployerName;
        setChanged();
    }
    // Getter method for txtEmployerName
    String getTxtEmployerName(){
        return this.txtEmployerName;
    }
    
    // Setter method for txtSalaryCertificateNo
    void setTxtSalaryCertificateNo(String txtSalaryCertificateNo){
        this.txtSalaryCertificateNo = txtSalaryCertificateNo;
        setChanged();
    }
    // Getter method for txtSalaryCertificateNo
    String getTxtSalaryCertificateNo(){
        return this.txtSalaryCertificateNo;
    }

    public Double getTxtTotalSalary() {
        return txtTotalSalary;
    }

    public void setTxtTotalSalary(Double txtTotalSalary) {
        this.txtTotalSalary = txtTotalSalary;
        setChanged();
    }

    // Setter method for txtNetWorth
    void setTxtNetWorth(String txtNetWorth){
        this.txtNetWorth = txtNetWorth;
        setChanged();
    }
    // Getter method for txtNetWorth
    String getTxtNetWorth(){
        return this.txtNetWorth;
    }

    public Integer getTxtPinCode() {
        return txtPinCode;
    }

    public void setTxtPinCode(Integer txtPinCode) {
        this.txtPinCode = txtPinCode;
        setChanged();
    }

    // Setter method for cboCity
    void setCboCity(String cboCity){
        this.cboCity = cboCity;
        setChanged();
    }
    // Getter method for cboCity
    String getCboCity(){
        return this.cboCity;
    }
    
    // Setter method for tdtRetirementDt
    void setTdtRetirementDt(String tdtRetirementDt){
        this.tdtRetirementDt = tdtRetirementDt;
        setChanged();
    }
    // Getter method for tdtRetirementDt
    String getTdtRetirementDt(){
        return this.tdtRetirementDt;
    }
    
    // Setter method for txtMemNetworth
    void setTxtMemNetworth(String txtMemNetworth){
        this.txtMemNetworth = txtMemNetworth;
        setChanged();
    }
    // Getter method for txtMemNetworth
    String getTxtMemNetworth(){
        return this.txtMemNetworth;
    }
    
    public String getTxtMemPriority() {
        return txtMemPriority;
    }

    public void setTxtMemPriority(String txtMemPriority) {
        this.txtMemPriority = txtMemPriority;
    }  
    
    // Setter method for txtContactNum
    void setTxtContactNum(String txtContactNum){
        this.txtContactNum = txtContactNum;
        setChanged();
    }
    // Getter method for txtContactNum
    String getTxtContactNum(){
        return this.txtContactNum;
    }
    
    // Setter method for txtMemType
    void setTxtMemType(String txtMemType){
        this.txtMemType = txtMemType;
        setChanged();
    }
    // Getter method for txtMemType
    String getTxtMemType(){
        return this.txtMemType;
    }
    
    // Setter method for txtMemName
    void setTxtMemName(String txtMemName){
        this.txtMemName = txtMemName;
        setChanged();
    }
    // Getter method for txtMemName
    String getTxtMemName(){
        return this.txtMemName;
    }
    
    // Setter method for txtMemNo
    void setTxtMemNo(String txtMemNo){
        this.txtMemNo = txtMemNo;
        setChanged();
    }
    // Getter method for txtMemNo
    String getTxtMemNo(){
        return this.txtMemNo;
    }
    
    // Setter method for txtSecurityRemarks
    void setTxtSecurityRemarks(String txtSecurityRemarks){
        this.txtSecurityRemarks = txtSecurityRemarks;
        setChanged();
    }
    // Getter method for txtSecurityRemarks
    String getTxtSecurityRemarks(){
        return this.txtSecurityRemarks;
    }
    
    // Setter method for cboSecurityType
    void setCboSecurityType(String cboSecurityType){
        this.cboSecurityType = cboSecurityType;
        setChanged();
    }
    // Getter method for cboSecurityType
    String getCboSecurityType(){
        return this.cboSecurityType;
    }
    
    // Setter method for txtGoldRemarks
    void setTxtGoldRemarks(String txtGoldRemarks){
        this.txtGoldRemarks = txtGoldRemarks;
        setChanged();
    }
    // Getter method for txtGoldRemarks
    String getTxtGoldRemarks(){
        return this.txtGoldRemarks;
    }
    
    // Setter method for txtValueOfGold
    void setTxtValueOfGold(String txtValueOfGold){
        this.txtValueOfGold = txtValueOfGold;
        setChanged();
    }
    // Getter method for txtValueOfGold
    String getTxtValueOfGold(){
        return this.txtValueOfGold;
    }

    public Double getTxtNetWeight() {
        return txtNetWeight;
    }

    public void setTxtNetWeight(Double txtNetWeight) {
        this.txtNetWeight = txtNetWeight;
        setChanged();
    }

    public Double getTxtGrossWeight() {
        return txtGrossWeight;
    }

    public void setTxtGrossWeight(Double txtGrossWeight) {
        this.txtGrossWeight = txtGrossWeight;
        setChanged();
    }

    // Setter method for txtJewelleryDetails
    void setTxtJewelleryDetails(String txtJewelleryDetails){
        this.txtJewelleryDetails = txtJewelleryDetails;
        setChanged();
    }
    // Getter method for txtJewelleryDetails
    String getTxtJewelleryDetails(){
        return this.txtJewelleryDetails;
    }
    
    // Setter method for txtDepAmount
    void setTxtDepAmount(String txtDepAmount){
        this.txtDepAmount = txtDepAmount;
        setChanged();
    }
    // Getter method for txtDepAmount
    String getTxtDepAmount(){
        return this.txtDepAmount;
    }
    
    // Setter method for txtMaturityDt
    void setTxtMaturityDt(String txtMaturityDt){
        this.txtMaturityDt = txtMaturityDt;
        setChanged();
    }
    // Getter method for txtMaturityDt
    String getTxtMaturityDt(){
        return this.txtMaturityDt;
    }
    
    // Setter method for txtMaturityValue
    void setTxtMaturityValue(String txtMaturityValue){
        this.txtMaturityValue = txtMaturityValue;
        setChanged();
    }
    // Getter method for txtMaturityValue
    String getTxtMaturityValue(){
        return this.txtMaturityValue;
    }
    
    // Setter method for txtProductId
    void setTxtProductId(String txtProductId){
        this.txtProductId = txtProductId;
        setChanged();
    }
    // Getter method for txtProductId
    String getTxtProductId(){
        return this.txtProductId;
    }
    
    // Setter method for txtRateOfInterest
    void setTxtRateOfInterest(String txtRateOfInterest){
        this.txtRateOfInterest = txtRateOfInterest;
        setChanged();
    }
    // Getter method for txtRateOfInterest
    String getTxtRateOfInterest(){
        return this.txtRateOfInterest;
    }
    
    // Setter method for txtDepNo
    void setTxtDepNo(String txtDepNo){
        this.txtDepNo = txtDepNo;
        setChanged();
    }
    // Getter method for txtDepNo
    String getTxtDepNo(){
        return this.txtDepNo;
    }
    
    // Setter method for tdtDepDt
    void setTdtDepDt(String tdtDepDt){
        this.tdtDepDt = tdtDepDt;
        setChanged();
    }
    // Getter method for tdtDepDt
    String getTdtDepDt(){
        return this.tdtDepDt;
    }
    
    /**
     * Getter for property _authorizeMap.
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }
    
    /**
     * Setter for property _authorizeMap.
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }
    
    /**
     * Getter for property lblStatus.
     * @return Value of property lblStatus.
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    
    /**
     * Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }
    
    /**
     * Getter for property result.
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }
    
    /**
     * Setter for property result.
     * @param result New value of property result.
     */
    public void setResult(int result) {
        this.result = result;
    }
    
    /**
     * Getter for property actionType.
     * @return Value of property actionType.
     */
    public int getActionType() {
        return actionType;
    }
    
    /**
     * Setter for property actionType.
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }
    
    /**
     * Getter for property cbmCity.
     * @return Value of property cbmCity.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCity() {
        return cbmCity;
    }
    
    /**
     * Setter for property cbmCity.
     * @param cbmCity New value of property cbmCity.
     */
    public void setCbmCity(com.see.truetransact.clientutil.ComboBoxModel cbmCity) {
        this.cbmCity = cbmCity;
    }
    
    /**
     * Getter for property cbmSecurity.
     * @return Value of property cbmSecurity.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSecurity() {
        return cbmSecurity;
    }
    
    /**
     * Setter for property cbmSecurity.
     * @param cbmSecurity New value of property cbmSecurity.
     */
    public void setCbmSecurity(com.see.truetransact.clientutil.ComboBoxModel cbmSecurity) {
        this.cbmSecurity = cbmSecurity;
    }
    
    /**
     * Getter for property lblMemberName.
     * @return Value of property lblMemberName.
     */
    public java.lang.String getLblMemberName() {
        return lblMemberName;
    }
    
    /**
     * Setter for property lblMemberName.
     * @param lblMemberName New value of property lblMemberName.
     */
    public void setLblMemberName(java.lang.String lblMemberName) {
        this.lblMemberName = lblMemberName;
    }
    
    /**
     * Getter for property lblMemberType.
     * @return Value of property lblMemberType.
     */
    public java.lang.String getLblMemberType() {
        return lblMemberType;
    }
    
    /**
     * Setter for property lblMemberType.
     * @param lblMemberType New value of property lblMemberType.
     */
    public void setLblMemberType(java.lang.String lblMemberType) {
        this.lblMemberType = lblMemberType;
    }
    
    /**
     * Getter for property lblNomineeName.
     * @return Value of property lblNomineeName.
     */
    public java.lang.String getLblNomineeName() {
        return lblNomineeName;
    }
    
    /**
     * Setter for property lblNomineeName.
     * @param lblNomineeName New value of property lblNomineeName.
     */
    public void setLblNomineeName(java.lang.String lblNomineeName) {
        this.lblNomineeName = lblNomineeName;
    }
    
    /**
     * Getter for property lblSecurityValue.
     * @return Value of property lblSecurityValue.
     */
    public java.lang.String getLblSecurityValue() {
        return lblSecurityValue;
    }
    
    /**
     * Setter for property lblSecurityValue.
     * @param lblSecurityValue New value of property lblSecurityValue.
     */
    public void setLblSecurityValue(java.lang.String lblSecurityValue) {
        this.lblSecurityValue = lblSecurityValue;
    }
    
    /**
     * Getter for property memberTypeData.
     * @return Value of property memberTypeData.
     */
    public boolean isMemberTypeData() {
        return memberTypeData;
    }
    
    /**
     * Setter for property memberTypeData.
     * @param memberTypeData New value of property memberTypeData.
     */
    public void setMemberTypeData(boolean memberTypeData) {
        this.memberTypeData = memberTypeData;
    }
    
    /**
     * Getter for property depositTypeData.
     * @return Value of property depositTypeData.
     */
    public boolean isDepositTypeData() {
        return depositTypeData;
    }
    
    /**
     * Setter for property depositTypeData.
     * @param depositTypeData New value of property depositTypeData.
     */
    public void setDepositTypeData(boolean depositTypeData) {
        this.depositTypeData = depositTypeData;
    }
    
    /**
     * Getter for property societyTypeData.
     * @return Value of property societyTypeData.
     */
    public boolean isSocietyTypeData() {
        return societyTypeData;
    }
    
    /**
     * Setter for property societyTypeData.
     * @param societyTypeData New value of property societyTypeData.
     */
    public void setSocietyTypeData(boolean societyTypeData) {
        this.societyTypeData = societyTypeData;
    }
    
    
    
    /**
     * Getter for property tblMemberTypeDetails.
     * @return Value of property tblMemberTypeDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblMemberTypeDetails() {
        return tblMemberTypeDetails;
    }
    
    /**
     * Setter for property tblMemberTypeDetails.
     * @param tblMemberTypeDetails New value of property tblMemberTypeDetails.
     */
    public void setTblMemberTypeDetails(com.see.truetransact.clientutil.EnhancedTableModel tblMemberTypeDetails) {
        this.tblMemberTypeDetails = tblMemberTypeDetails;
    }
    
    
    
    /**
     * Getter for property tblDepositTypeDetails.
     * @return Value of property tblDepositTypeDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblDepositTypeDetails() {
        return tblDepositTypeDetails;
    }
    
    /**
     * Setter for property tblDepositTypeDetails.
     * @param tblDepositTypeDetails New value of property tblDepositTypeDetails.
     */
    public void setTblDepositTypeDetails(com.see.truetransact.clientutil.EnhancedTableModel tblDepositTypeDetails) {
        this.tblDepositTypeDetails = tblDepositTypeDetails;
    }
    
    /**
     * Getter for property chkStandingInstn.
     * @return Value of property chkStandingInstn.
     */
    public boolean getChkStandingInstn() {
        return chkStandingInstn;
    }
    
    /**
     * Setter for property chkStandingInstn.
     * @param chkStandingInstn New value of property chkStandingInstn.
     */
    public void setChkStandingInstn(boolean chkStandingInstn) {
        this.chkStandingInstn = chkStandingInstn;
    }
    
    /**
     * Getter for property chkNominee.
     * @return Value of property chkNominee.
     */
    public boolean getChkNominee() {
        return chkNominee;
    }
    
    /**
     * Setter for property chkNominee.
     * @param chkNominee New value of property chkNominee.
     */
    public void setChkNominee(boolean chkNominee) {
        this.chkNominee = chkNominee;
    }
    
    /**
     * Getter for property cbmProdType.
     * @return Value of property cbmProdType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }
    
    /**
     * Setter for property cbmProdType.
     * @param cbmProdType New value of property cbmProdType.
     */
    public void setCbmProdType(com.see.truetransact.clientutil.ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
    }
    
    /**
     * Getter for property cbmProdId.
     * @return Value of property cbmProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }
    
    /**
     * Setter for property cbmProdId.
     * @param cbmProdId New value of property cbmProdId.
     */
    public void setCbmProdId(com.see.truetransact.clientutil.ComboBoxModel cbmProdId) {
        this.cbmProdId = cbmProdId;
    }
    
    /**
     * Getter for property txtCustomerIdCr.
     * @return Value of property txtCustomerIdCr.
     */
    public java.lang.String getTxtCustomerIdCr() {
        return txtCustomerIdCr;
    }
    
    /**
     * Setter for property txtCustomerIdCr.
     * @param txtCustomerIdCr New value of property txtCustomerIdCr.
     */
    public void setTxtCustomerIdCr(java.lang.String txtCustomerIdCr) {
        this.txtCustomerIdCr = txtCustomerIdCr;
    }
    
    /**
     * Getter for property cboCaseStatus.
     * @return Value of property cboCaseStatus.
     */
    public java.lang.String getCboCaseStatus() {
        return cboCaseStatus;
    }
    
    /**
     * Setter for property cboCaseStatus.
     * @param cboCaseStatus New value of property cboCaseStatus.
     */
    public void setCboCaseStatus(java.lang.String cboCaseStatus) {
        this.cboCaseStatus = cboCaseStatus;
    }
    
    /**
     * Getter for property txtCaseNumber.
     * @return Value of property txtCaseNumber.
     */
    public java.lang.String getTxtCaseNumber() {
        return txtCaseNumber;
    }
    
    /**
     * Setter for property txtCaseNumber.
     * @param txtCaseNumber New value of property txtCaseNumber.
     */
    public void setTxtCaseNumber(java.lang.String txtCaseNumber) {
        this.txtCaseNumber = txtCaseNumber;
    }
    
    /**
     * Getter for property tdtlFillingDt.
     * @return Value of property tdtlFillingDt.
     */
    public java.lang.String getTdtlFillingDt() {
        return tdtlFillingDt;
    }
    
    /**
     * Setter for property tdtlFillingDt.
     * @param tdtlFillingDt New value of property tdtlFillingDt.
     */
    public void setTdtlFillingDt(java.lang.String tdtlFillingDt) {
        this.tdtlFillingDt = tdtlFillingDt;
    }
    
    /**
     * Getter for property txtFillingFees.
     * @return Value of property txtFillingFees.
     */
    public java.lang.String getTxtFillingFees() {
        return txtFillingFees;
    }
    
    /**
     * Setter for property txtFillingFees.
     * @param txtFillingFees New value of property txtFillingFees.
     */
    public void setTxtFillingFees(java.lang.String txtFillingFees) {
        this.txtFillingFees = txtFillingFees;
    }
    
    /**
     * Getter for property txtMiscCharges.
     * @return Value of property txtMiscCharges.
     */
    public java.lang.String getTxtMiscCharges() {
        return txtMiscCharges;
    }
    
    /**
     * Setter for property txtMiscCharges.
     * @param txtMiscCharges New value of property txtMiscCharges.
     */
    public void setTxtMiscCharges(java.lang.String txtMiscCharges) {
        this.txtMiscCharges = txtMiscCharges;
    }
    
    /**
     * Getter for property cbmCaseStatus.
     * @return Value of property cbmCaseStatus.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCaseStatus() {
        return cbmCaseStatus;
    }
    
    /**
     * Setter for property cbmCaseStatus.
     * @param cbmCaseStatus New value of property cbmCaseStatus.
     */
    public void setCbmCaseStatus(com.see.truetransact.clientutil.ComboBoxModel cbmCaseStatus) {
        this.cbmCaseStatus = cbmCaseStatus;
    }
    
    /**
     * Getter for property tblCaseDetails.
     * @return Value of property tblCaseDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblCaseDetails() {
        return tblCaseDetails;
    }
    
    /**
     * Setter for property tblCaseDetails.
     * @param tblCaseDetails New value of property tblCaseDetails.
     */
    public void setTblCaseDetails(com.see.truetransact.clientutil.EnhancedTableModel tblCaseDetails) {
        this.tblCaseDetails = tblCaseDetails;
    }
    
    /**
     * Getter for property newCaseData.
     * @return Value of property newCaseData.
     */
    public boolean isNewCaseData() {
        return newCaseData;
    }
    
    /**
     * Setter for property newCaseData.
     * @param newCaseData New value of property newCaseData.
     */
    public void setNewCaseData(boolean newCaseData) {
        this.newCaseData = newCaseData;
    }

    public Integer getTxtSubNo() {
        return txtSubNo;
    }

    public void setTxtSubNo(Integer txtSubNo) {
        this.txtSubNo = txtSubNo;
    }

    /**
     * Getter for property rdoSalaryRecovery.
     * @return Value of property rdoSalaryRecovery.
     */
    public java.lang.String getRdoSalaryRecovery() {
        return rdoSalaryRecovery;
    }
    
    /**
     * Setter for property rdoSalaryRecovery.
     * @param rdoSalaryRecovery New value of property rdoSalaryRecovery.
     */
    public void setRdoSalaryRecovery(java.lang.String rdoSalaryRecovery) {
        this.rdoSalaryRecovery = rdoSalaryRecovery;
    }
    
    /**
     * Getter for property lockStatus.
     * @return Value of property lockStatus.
     */
    public java.lang.String getLockStatus() {
        return lockStatus;
    }
    
    /**
     * Setter for property lockStatus.
     * @param lockStatus New value of property lockStatus.
     */
    public void setLockStatus(java.lang.String lockStatus) {
        this.lockStatus = lockStatus;
    }
    
    /**
     * Getter for property cbmDepProdID.
     * @return Value of property cbmDepProdID.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDepProdID() {
        return cbmDepProdID;
    }
    
    /**
     * Setter for property cbmDepProdID.
     * @param cbmDepProdID New value of property cbmDepProdID.
     */
    public void setCbmDepProdID(com.see.truetransact.clientutil.ComboBoxModel cbmDepProdID) {
        this.cbmDepProdID = cbmDepProdID;
    }
    
    /**
     * Getter for property cboDepProdID.
     * @return Value of property cboDepProdID.
     */
    public java.lang.String getCboDepProdID() {
        return cboDepProdID;
    }
    
    /**
     * Setter for property cboDepProdID.
     * @param cboDepProdID New value of property cboDepProdID.
     */
    public void setCboDepProdID(java.lang.String cboDepProdID) {
        this.cboDepProdID = cboDepProdID;
    }
    
    /**
     * Getter for property cbmProdTypeSecurity.
     * @return Value of property cbmProdTypeSecurity.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdTypeSecurity() {
        return cbmProdTypeSecurity;
    }
    
    /**
     * Setter for property cbmProdTypeSecurity.
     * @param cbmProdTypeSecurity New value of property cbmProdTypeSecurity.
     */
    public void setCbmProdTypeSecurity(com.see.truetransact.clientutil.ComboBoxModel cbmProdTypeSecurity) {
        this.cbmProdTypeSecurity = cbmProdTypeSecurity;
    }
    
    /**
     * Getter for property cboProductTypeSecurity.
     * @return Value of property cboProductTypeSecurity.
     */
    public java.lang.String getCboProductTypeSecurity() {
        return cboProductTypeSecurity;
    }
    
    /**
     * Setter for property cboProductTypeSecurity.
     * @param cboProductTypeSecurity New value of property cboProductTypeSecurity.
     */
    public void setCboProductTypeSecurity(java.lang.String cboProductTypeSecurity) {
        this.cboProductTypeSecurity = cboProductTypeSecurity;
    }
    
    /**
     * Getter for property txtOwnerMemNo.
     * @return Value of property txtOwnerMemNo.
     */
    public java.lang.String getTxtOwnerMemNo() {
        return txtOwnerMemNo;
    }
    
    /**
     * Setter for property txtOwnerMemNo.
     * @param txtOwnerMemNo New value of property txtOwnerMemNo.
     */
    public void setTxtOwnerMemNo(java.lang.String txtOwnerMemNo) {
        this.txtOwnerMemNo = txtOwnerMemNo;
    }
    
    /**
     * Getter for property txtOwnerMemberNname.
     * @return Value of property txtOwnerMemberNname.
     */
    public java.lang.String getTxtOwnerMemberNname() {
        return txtOwnerMemberNname;
    }
    
    /**
     * Setter for property txtOwnerMemberNname.
     * @param txtOwnerMemberNname New value of property txtOwnerMemberNname.
     */
    public void setTxtOwnerMemberNname(java.lang.String txtOwnerMemberNname) {
        this.txtOwnerMemberNname = txtOwnerMemberNname;
    }
    
    /**
     * Getter for property txtDocumentNo.
     * @return Value of property txtDocumentNo.
     */
    public java.lang.String getTxtDocumentNo() {
        return txtDocumentNo;
    }
    
    /**
     * Setter for property txtDocumentNo.
     * @param txtDocumentNo New value of property txtDocumentNo.
     */
    public void setTxtDocumentNo(java.lang.String txtDocumentNo) {
        this.txtDocumentNo = txtDocumentNo;
    }
    
    /**
     * Getter for property txtDocumentType.
     * @return Value of property txtDocumentType.
     */
    public java.lang.String getTxtDocumentType() {
        return txtDocumentType;
    }
    
    /**
     * Setter for property txtDocumentType.
     * @param txtDocumentType New value of property txtDocumentType.
     */
    public void setTxtDocumentType(java.lang.String txtDocumentType) {
        this.txtDocumentType = txtDocumentType;
    }
    
    /**
     * Getter for property tdtDocumentDate.
     * @return Value of property tdtDocumentDate.
     */
    public java.lang.String getTdtDocumentDate() {
        return tdtDocumentDate;
    }
    
    /**
     * Setter for property tdtDocumentDate.
     * @param tdtDocumentDate New value of property tdtDocumentDate.
     */
    public void setTdtDocumentDate(java.lang.String tdtDocumentDate) {
        this.tdtDocumentDate = tdtDocumentDate;
    }
    
    /**
     * Getter for property txtRegisteredOffice.
     * @return Value of property txtRegisteredOffice.
     */
    public java.lang.String getTxtRegisteredOffice() {
        return txtRegisteredOffice;
    }
    
    /**
     * Setter for property txtRegisteredOffice.
     * @param txtRegisteredOffice New value of property txtRegisteredOffice.
     */
    public void setTxtRegisteredOffice(java.lang.String txtRegisteredOffice) {
        this.txtRegisteredOffice = txtRegisteredOffice;
    }
    
    /**
     * Getter for property cboPledge.
     * @return Value of property cboPledge.
     */
    public java.lang.String getCboPledge() {
        return cboPledge;
    }
    
    /**
     * Setter for property cboPledge.
     * @param cboPledge New value of property cboPledge.
     */
    public void setCboPledge(java.lang.String cboPledge) {
        this.cboPledge = cboPledge;
    }
    
    /**
     * Getter for property tdtPledgeDate.
     * @return Value of property tdtPledgeDate.
     */
    public java.lang.String getTdtPledgeDate() {
        return tdtPledgeDate;
    }
    
    /**
     * Setter for property tdtPledgeDate.
     * @param tdtPledgeDate New value of property tdtPledgeDate.
     */
    public void setTdtPledgeDate(java.lang.String tdtPledgeDate) {
        this.tdtPledgeDate = tdtPledgeDate;
    }
    
    /**
     * Getter for property txtPledgeNo.
     * @return Value of property txtPledgeNo.
     */
    public java.lang.String getTxtPledgeNo() {
        return txtPledgeNo;
    }
    
    /**
     * Setter for property txtPledgeNo.
     * @param txtPledgeNo New value of property txtPledgeNo.
     */
    public void setTxtPledgeNo(java.lang.String txtPledgeNo) {
        this.txtPledgeNo = txtPledgeNo;
    }
    
    /**
     * Getter for property txtPledgeAmount.
     * @return Value of property txtPledgeAmount.
     */
    public java.lang.String getTxtPledgeAmount() {
        return txtPledgeAmount;
    }
    
    /**
     * Setter for property txtPledgeAmount.
     * @param txtPledgeAmount New value of property txtPledgeAmount.
     */
    public void setTxtPledgeAmount(java.lang.String txtPledgeAmount) {
        this.txtPledgeAmount = txtPledgeAmount;
    }
    
    /**
     * Getter for property txtVillage.
     * @return Value of property txtVillage.
     */
    public java.lang.String getTxtVillage() {
        return txtVillage;
    }
    
    /**
     * Setter for property txtVillage.
     * @param txtVillage New value of property txtVillage.
     */
    public void setTxtVillage(java.lang.String txtVillage) {
        this.txtVillage = txtVillage;
    }
    
    /**
     * Getter for property txtSurveyNo.
     * @return Value of property txtSurveyNo.
     */
    public java.lang.String getTxtSurveyNo() {
        return txtSurveyNo;
    }
    
    /**
     * Setter for property txtSurveyNo.
     * @param txtSurveyNo New value of property txtSurveyNo.
     */
    public void setTxtSurveyNo(java.lang.String txtSurveyNo) {
        this.txtSurveyNo = txtSurveyNo;
    }
    
    /**
     * Getter for property txtTotalArea.
     * @return Value of property txtTotalArea.
     */
    public java.lang.String getTxtTotalArea() {
        return txtTotalArea;
    }
    
    /**
     * Setter for property txtTotalArea.
     * @param txtTotalArea New value of property txtTotalArea.
     */
    public void setTxtTotalArea(java.lang.String txtTotalArea) {
        this.txtTotalArea = txtTotalArea;
    }
    
    /**
     * Getter for property cboNature.
     * @return Value of property cboNature.
     */
    public java.lang.String getCboNature() {
        return cboNature;
    }
    
    /**
     * Setter for property cboNature.
     * @param cboNature New value of property cboNature.
     */
    public void setCboNature(java.lang.String cboNature) {
        this.cboNature = cboNature;
    }
    
    /**
     * Getter for property cboRight.
     * @return Value of property cboRight.
     */
    public java.lang.String getCboRight() {
        return cboRight;
    }
    
    /**
     * Setter for property cboRight.
     * @param cboRight New value of property cboRight.
     */
    public void setCboRight(java.lang.String cboRight) {
        this.cboRight = cboRight;
    }
    
    /**
     * Getter for property cboDocumentType.
     * @return Value of property cboDocumentType.
     */
    public java.lang.String getCboDocumentType() {
        return cboDocumentType;
    }
    
    /**
     * Setter for property cboDocumentType.
     * @param cboDocumentType New value of property cboDocumentType.
     */
    public void setCboDocumentType(java.lang.String cboDocumentType) {
        this.cboDocumentType = cboDocumentType;
    }
    
    /**
     * Getter for property txtAreaParticular.
     * @return Value of property txtAreaParticular.
     */
    public java.lang.String getTxtAreaParticular() {
        return txtAreaParticular;
    }
    
    /**
     * Setter for property txtAreaParticular.
     * @param txtAreaParticular New value of property txtAreaParticular.
     */
    public void setTxtAreaParticular(java.lang.String txtAreaParticular) {
        this.txtAreaParticular = txtAreaParticular;
    }
    
    /**
     * Getter for property txtPledgeType.
     * @return Value of property txtPledgeType.
     */
    public java.lang.String getTxtPledgeType() {
        return txtPledgeType;
    }
    
    /**
     * Setter for property txtPledgeType.
     * @param txtPledgeType New value of property txtPledgeType.
     */
    public void setTxtPledgeType(java.lang.String txtPledgeType) {
        this.txtPledgeType = txtPledgeType;
    }
    
    /**
     * Getter for property tblCollateralDetails.
     * @return Value of property tblCollateralDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblCollateralDetails() {
        return tblCollateralDetails;
    }
    
    /**
     * Setter for property tblCollateralDetails.
     * @param tblCollateralDetails New value of property tblCollateralDetails.
     */
    public void setTblCollateralDetails(com.see.truetransact.clientutil.EnhancedTableModel tblCollateralDetails) {
        this.tblCollateralDetails = tblCollateralDetails;
    }
    
    /**
     * Getter for property tblJointCollateral.
     * @return Value of property tblJointCollateral.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblJointCollateral() {
        return tblJointCollateral;
    }
    
    /**
     * Setter for property tblJointCollateral.
     * @param tblJointCollateral New value of property tblJointCollateral.
     */
    public void setTblJointCollateral(com.see.truetransact.clientutil.EnhancedTableModel tblJointCollateral) {
        this.tblJointCollateral = tblJointCollateral;
    }
    /**
     * Getter for property rdoGahanYes.
     * @return Value of property rdoGahanYes.
     */
    public boolean isRdoGahanYes() {
        return rdoGahanYes;
    }
    
    /**
     * Setter for property rdoGahanYes.
     * @param rdoGahanYes New value of property rdoGahanYes.
     */
    public void setRdoGahanYes(boolean rdoGahanYes) {
        this.rdoGahanYes = rdoGahanYes;
    }
    
    /**
     * Getter for property rdoGahanNo.
     * @return Value of property rdoGahanNo.
     */
    public boolean isRdoGahanNo() {
        return rdoGahanNo;
    }
    
    /**
     * Setter for property rdoGahanNo.
     * @param rdoGahanNo New value of property rdoGahanNo.
     */
    public void setRdoGahanNo(boolean rdoGahanNo) {
        this.rdoGahanNo = rdoGahanNo;
    }
    
    /**
     * Getter for property collateralMap.
     * @return Value of property collateralMap.
     */
    public java.util.LinkedHashMap getCollateralMap() {
        return collateralMap;
    }
    
    /**
     * Setter for property collateralMap.
     * @param collateralMap New value of property collateralMap.
     */
    public void setCollateralMap(java.util.LinkedHashMap collateralMap) {
        this.collateralMap = collateralMap;
    }
    
    /**
     * Getter for property cbmNature.
     * @return Value of property cbmNature.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmNature() {
        return cbmNature;
    }
    
    /**
     * Setter for property cbmNature.
     * @param cbmNature New value of property cbmNature.
     */
    public void setCbmNature(com.see.truetransact.clientutil.ComboBoxModel cbmNature) {
        this.cbmNature = cbmNature;
    }
    
    /**
     * Getter for property cbmRight.
     * @return Value of property cbmRight.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRight() {
        return cbmRight;
    }
    
    /**
     * Setter for property cbmRight.
     * @param cbmRight New value of property cbmRight.
     */
    public void setCbmRight(com.see.truetransact.clientutil.ComboBoxModel cbmRight) {
        this.cbmRight = cbmRight;
    }
    
    /**
     * Getter for property cbmPledge.
     * @return Value of property cbmPledge.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPledge() {
        return cbmPledge;
    }
    
    /**
     * Setter for property cbmPledge.
     * @param cbmPledge New value of property cbmPledge.
     */
    public void setCbmPledge(com.see.truetransact.clientutil.ComboBoxModel cbmPledge) {
        this.cbmPledge = cbmPledge;
    }
    
    /**
     * Getter for property cbmDocumentType.
     * @return Value of property cbmDocumentType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDocumentType() {
        return cbmDocumentType;
    }
    
    /**
     * Setter for property cbmDocumentType.
     * @param cbmDocumentType New value of property cbmDocumentType.
     */
    public void setCbmDocumentType(com.see.truetransact.clientutil.ComboBoxModel cbmDocumentType) {
        this.cbmDocumentType = cbmDocumentType;
    }
    
    /**
     * Getter for property collateralTypeData.
     * @return Value of property collateralTypeData.
     */
    public boolean isCollateralTypeData() {
        return collateralTypeData;
    }
    
    /**
     * Setter for property collateralTypeData.
     * @param collateralTypeData New value of property collateralTypeData.
     */
    public void setCollateralTypeData(boolean collateralTypeData) {
        this.collateralTypeData = collateralTypeData;
    }
    
    /**
     * Getter for property docGenId.
     * @return Value of property docGenId.
     */
    public java.lang.String getDocGenId() {
        return docGenId;
    }
    
    /**
     * Setter for property docGenId.
     * @param docGenId New value of property docGenId.
     */
    public void setDocGenId(java.lang.String docGenId) {
        this.docGenId = docGenId;
    }
    
    /**
     * Getter for property pledgeValMap.
     * @return Value of property pledgeValMap.
     */
    public java.util.HashMap getPledgeValMap() {
        return pledgeValMap;
    }
    
    /**
     * Setter for property pledgeValMap.
     * @param pledgeValMap New value of property pledgeValMap.
     */
    public void setPledgeValMap(java.util.HashMap pledgeValMap) {
        this.pledgeValMap = pledgeValMap;
    }
    
    /**
     * Getter for property cboOtherInstituion.
     * @return Value of property cboOtherInstituion.
     */
    public java.lang.String getCboOtherInstituion() {
        return cboOtherInstituion;
    }
    
    /**
     * Setter for property cboOtherInstituion.
     * @param cboOtherInstituion New value of property cboOtherInstituion.
     */
    public void setCboOtherInstituion(java.lang.String cboOtherInstituion) {
        this.cboOtherInstituion = cboOtherInstituion;
    }
    
    /**
     * Getter for property cboSecurityTypeSociety.
     * @return Value of property cboSecurityTypeSociety.
     */
    public java.lang.String getCboSecurityTypeSociety() {
        return cboSecurityTypeSociety;
    }
    
    /**
     * Setter for property cboSecurityTypeSociety.
     * @param cboSecurityTypeSociety New value of property cboSecurityTypeSociety.
     */
    public void setCboSecurityTypeSociety(java.lang.String cboSecurityTypeSociety) {
        this.cboSecurityTypeSociety = cboSecurityTypeSociety;
    }
    
    /**
     * Getter for property txtOtherInstituionName.
     * @return Value of property txtOtherInstituionName.
     */
    public java.lang.String getTxtOtherInstituionName() {
        return txtOtherInstituionName;
    }
    
    /**
     * Setter for property txtOtherInstituionName.
     * @param txtOtherInstituionName New value of property txtOtherInstituionName.
     */
    public void setTxtOtherInstituionName(java.lang.String txtOtherInstituionName) {
        this.txtOtherInstituionName = txtOtherInstituionName;
    }
    
    /**
     * Getter for property txtSecurityAmountSociety.
     * @return Value of property txtSecurityAmountSociety.
     */
    public java.lang.String getTxtSecurityAmountSociety() {
        return txtSecurityAmountSociety;
    }
    
    /**
     * Setter for property txtSecurityAmountSociety.
     * @param txtSecurityAmountSociety New value of property txtSecurityAmountSociety.
     */
    public void setTxtSecurityAmountSociety(java.lang.String txtSecurityAmountSociety) {
        this.txtSecurityAmountSociety = txtSecurityAmountSociety;
    }
    
    /**
     * Getter for property txtSecurityNoSociety.
     * @return Value of property txtSecurityNoSociety.
     */
    public java.lang.String getTxtSecurityNoSociety() {
        return txtSecurityNoSociety;
    }
    
    /**
     * Setter for property txtSecurityNoSociety.
     * @param txtSecurityNoSociety New value of property txtSecurityNoSociety.
     */
    public void setTxtSecurityNoSociety(java.lang.String txtSecurityNoSociety) {
        this.txtSecurityNoSociety = txtSecurityNoSociety;
    }
    
    /**
     * Getter for property txtMaturityValueSociety.
     * @return Value of property txtMaturityValueSociety.
     */
    public java.lang.String getTxtMaturityValueSociety() {
        return txtMaturityValueSociety;
    }
    
    /**
     * Setter for property txtMaturityValueSociety.
     * @param txtMaturityValueSociety New value of property txtMaturityValueSociety.
     */
    public void setTxtMaturityValueSociety(java.lang.String txtMaturityValueSociety) {
        this.txtMaturityValueSociety = txtMaturityValueSociety;
    }
    
    /**
     * Getter for property txtRemarksSociety.
     * @return Value of property txtRemarksSociety.
     */
    public java.lang.String getTxtRemarksSociety() {
        return txtRemarksSociety;
    }
    
    /**
     * Setter for property txtRemarksSociety.
     * @param txtRemarksSociety New value of property txtRemarksSociety.
     */
    public void setTxtRemarksSociety(java.lang.String txtRemarksSociety) {
        this.txtRemarksSociety = txtRemarksSociety;
    }
    
    /**
     * Getter for property tdtIssueDtSoceity.
     * @return Value of property tdtIssueDtSoceity.
     */
    public java.lang.String getTdtIssueDtSoceity() {
        return tdtIssueDtSoceity;
    }
    
    /**
     * Setter for property tdtIssueDtSoceity.
     * @param tdtIssueDtSoceity New value of property tdtIssueDtSoceity.
     */
    public void setTdtIssueDtSoceity(java.lang.String tdtIssueDtSoceity) {
        this.tdtIssueDtSoceity = tdtIssueDtSoceity;
    }
    
    /**
     * Getter for property tdtMaturityDateSociety.
     * @return Value of property tdtMaturityDateSociety.
     */
    public java.lang.String getTdtMaturityDateSociety() {
        return tdtMaturityDateSociety;
    }
    
    /**
     * Setter for property tdtMaturityDateSociety.
     * @param tdtMaturityDateSociety New value of property tdtMaturityDateSociety.
     */
    public void setTdtMaturityDateSociety(java.lang.String tdtMaturityDateSociety) {
        this.tdtMaturityDateSociety = tdtMaturityDateSociety;
    }
    
    /**
     * Getter for property cbmOtherInstituion.
     * @return Value of property cbmOtherInstituion.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmOtherInstituion() {
        return cbmOtherInstituion;
    }
    
    /**
     * Setter for property cbmOtherInstituion.
     * @param cbmOtherInstituion New value of property cbmOtherInstituion.
     */
    public void setCbmOtherInstituion(com.see.truetransact.clientutil.ComboBoxModel cbmOtherInstituion) {
        this.cbmOtherInstituion = cbmOtherInstituion;
    }
    
    /**
     * Getter for property cbmSecurityTypeSociety.
     * @return Value of property cbmSecurityTypeSociety.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSecurityTypeSociety() {
        return cbmSecurityTypeSociety;
    }
    
    /**
     * Setter for property cbmSecurityTypeSociety.
     * @param cbmSecurityTypeSociety New value of property cbmSecurityTypeSociety.
     */
    public void setCbmSecurityTypeSociety(com.see.truetransact.clientutil.ComboBoxModel cbmSecurityTypeSociety) {
        this.cbmSecurityTypeSociety = cbmSecurityTypeSociety;
    }
    
    /**
     * Getter for property tblOtherSocietyDetails.
     * @return Value of property tblOtherSocietyDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblOtherSocietyDetails() {
        return tblOtherSocietyDetails;
    }
    
    /**
     * Setter for property tblOtherSocietyDetails.
     * @param tblOtherSocietyDetails New value of property tblOtherSocietyDetails.
     */
    public void setTblOtherSocietyDetails(com.see.truetransact.clientutil.EnhancedTableModel tblOtherSocietyDetails) {
        this.tblOtherSocietyDetails = tblOtherSocietyDetails;
    }
    
    /**
     * Getter for property cbxSamechittal.
     * @return Value of property cbxSamechittal.
     */
    public String getCbxSamechittal() {
        return cbxSamechittal;
    }
    
    /**
     * Setter for property cbxSamechittal.
     * @param cbxSamechittal New value of property cbxSamechittal.
     */
    public void setCbxSamechittal(String cbxSamechittal) {
        this.cbxSamechittal = cbxSamechittal;
    }
    
    
    
     public com.see.truetransact.ui.common.transaction.TransactionOB getTransactionOB() {
        return transactionOB;
    }
    
    /**
     * Setter for property transactionOB.
     * @param transactionOB New value of property transactionOB.
     */
    public void setTransactionOB(com.see.truetransact.ui.common.transaction.TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }

    public String getUserDefinedAuction() {
        return userDefinedAuction;
    }

    public void setUserDefinedAuction(String userDefinedAuction) {
        this.userDefinedAuction = userDefinedAuction;
    }

    public String getRdoGoldSecurityStockExists() {
        return rdoGoldSecurityStockExists;
    }

    public void setRdoGoldSecurityStockExists(String rdoGoldSecurityStockExists) {
        this.rdoGoldSecurityStockExists = rdoGoldSecurityStockExists;
    }

    public String getTxtGoldSecurityId() {
        return txtGoldSecurityId;
    }

    public void setTxtGoldSecurityId(String txtGoldSecurityId) {
        this.txtGoldSecurityId = txtGoldSecurityId;
    }
    
    // Added by nithya on 07-03-2020 for KD-1379
    private LoansSecurityGoldStockTO getCustomerGoldStockSecurityTO() {        
        LoansSecurityGoldStockTO objCustomerGoldStockSecurityTO = new LoansSecurityGoldStockTO();
        objCustomerGoldStockSecurityTO.setAcctNum(getTxtChittalNo() );
        objCustomerGoldStockSecurityTO.setAsOnDt(curDate);
        objCustomerGoldStockSecurityTO.setPledgeAmount(CommonUtil.convertObjToDouble(getTxtValueOfGold()));
        objCustomerGoldStockSecurityTO.setRemarks(getTxtGoldRemarks());
        objCustomerGoldStockSecurityTO.setStatusDt(curDate);
        objCustomerGoldStockSecurityTO.setStatusBy(TrueTransactMain.USER_ID);
        objCustomerGoldStockSecurityTO.setStatus(CommonConstants.STATUS_CREATED);
        objCustomerGoldStockSecurityTO.setGoldStockId(getTxtGoldSecurityId());
        objCustomerGoldStockSecurityTO.setBranchCode(ProxyParameters.BRANCH_ID);
        objCustomerGoldStockSecurityTO.setProdId(getTxtSchemeName());
        objCustomerGoldStockSecurityTO.setProdType("MDS");
        return objCustomerGoldStockSecurityTO;
    }
    // End
    
    
}