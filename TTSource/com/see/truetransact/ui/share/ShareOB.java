/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareOB.java
 *
 * Created on Sat Dec 25 12:44:40 IST 2004
 */

package com.see.truetransact.ui.share;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.deposit.TableManipulation;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.JointAcctHolderManipulation;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.common.mobile.SMSSubscriptionTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.product.loan.LoanProductAccHeadTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import com.see.truetransact.transferobject.share.ShareAccInfoTO;
import com.see.truetransact.transferobject.share.ShareAcctDetailsTO;
import com.see.truetransact.transferobject.share.ShareJointTO;
import com.see.truetransact.ui.common.transaction.TransactionOB;

// If we are implementing Truetransact branchwise, Send to Resolution & Share Resolution option in HO are not possible
// By Rajesh
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
// end

import com.see.truetransact.ui.common.nominee.*;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;


/**
 *
 * @author  K.R.Jayakrishnan
 */

public class ShareOB extends CObservable{
    Date curDate = null;
    private TransactionOB transactionOB;
    private LinkedHashMap transactionDetailsTO ;
    private LinkedHashMap deletedTransactionDetailsTO;
    private LinkedHashMap allowedTransactionDetailsTO ;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    
    ShareRB objShareRB = new ShareRB();
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    private int result;
    private ProxyFactory proxy;
    private HashMap operationMap;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String amount="";
    JointAcctHolderManipulation objJointAcctHolderManipulation  = new JointAcctHolderManipulation();
    TableManipulation objTableManipulation = new TableManipulation();
    
    //    private HashMap _authorizeMap;
    double balance=0;
    public final String NEW = "New";
    private final String YES = "Y";
    private final String NO = "N";
    
    //--- Declaration for Error Command
    private final String TO_COMMAND_ERROR = "TOCommandError";
    
    //--- Declarations for operationMap
    private final String SHARE_JNDI = "ShareJNDI";
    private final String SHARE_HOME = "share.ShareHome";
    private final String SHARE_REMOTE = "share.Share";
    
    //    public boolean authorised = false;
    //--- Declarations for Joint Account Table
    public LinkedHashMap jntAcctAll;
    HashMap jntAcctSingleRec;
    private EnhancedTableModel tblJointAccnt;
    private ArrayList tblJointAccntColTitle;
    private ArrayList jntAccntRow;
    LinkedHashMap jntAcctTOMap;
    private final String TBL_JNT_ACCNT_COLUMN_1 = "tblJntAccntColumn1";
    private final String TBL_JNT_ACCNT_COLUMN_2 = "tblJntAccntColumn2";
    private final String TBL_JNT_ACCNT_COLUMN_3 = "tblJntAccntColumn3";
    private final String TBL_JNT_ACCNT_COLUMN_4 = "tblJntAccntColumn4";
    private final String TBL_JNT_ACCNT_COLUMN_5 = "tblJntAccntColumn5";
    //--- End of Declarations for Joint Account Table
    private EnhancedTableModel tblDrfTransaction;
    public int noOfShares ;
    //--- Declarations for ShareAccountDetail Table
    String shareAcctDetTxtSerialNo;
    public int shareAcctDetMode; // For "New Record"   ----> mode = 0
    // For "Modification" ----> mode = 1
    private int shareAcctDetRowDel;
    private EnhancedTableModel tblShareAcctDet;
    private EnhancedTableModel tblShareLoanDet;
    private ArrayList shareAcctDetRow;
    private ArrayList tblShareAcctDetColTitle;
    private ArrayList tblShareLoanDetTitle;
    private HashMap shareAcctDetRec;
    public int shareAcctDetNo;
    private int ModifyShareAcctDetNo;
    List list=null;
    HashMap shareAcctDetAll;
    HashMap shareAcctDetCheckValues;
    LinkedHashMap shareAcctDetTOMap;
    String shareAcctDetStatus;
    int shareAcctDetK;
    int shareAcctDetRowCount; // Gets the row count of the duplicate record from tblShareAcctDet
    int shareAcctDetGetSelectedRowCount; // gets the RowCount of the selected record
    int shareAcctDetCount;
    private LinkedHashMap DeleteTranslist;
    ArrayList DeleteShareDet=null;
    //--- String for storing the Customer's Membership Type
    public String membershipType = "";
    ArrayList Deleatelst=null;
    //--- int to store Maximum Nominees Allowed
    public int maxNominee = 0;
    
    // Array list of nominees and PoAs
    private ArrayList nomineeTOList;
    
    private final String TBL_SHR_ACCNT_DET_COLUMN_1 = "tblShrAccntDetColumn1";
    private final String TBL_SHR_ACCNT_DET_COLUMN_2 = "tblShrAccntDetColumn2";
    private final String TBL_SHR_ACCNT_DET_COLUMN_3 = "tblShrAccntDetColumn3";
    private final String TBL_SHR_ACCNT_DET_COLUMN_4 = "tblShrAccntDetColumn4";
    private final String TBL_SHR_ACCNT_DET_COLUMN_5 = "tblShrAccntDetColumn5";
    private final String TBL_SHR_ACCNT_DET_COLUMN_6 = "tblShrAccntDetColumn6";
    //--- End of Declarations for ShareAccountDetail Table
    
    //--- Serial No Declaration
    private final String FLD_SL_NO = "SlNo";
    
    //--- TO Object Declarations and HashMap Declarations for doActionPerform
    HashMap data;
    ShareAccInfoTO objShareAccInfoTO;
    ShareAcctDetailsTO objShareAcctDetailsTO;
    
    private final String SHARE_ACC_INFO_FOR_DAO = "ShareAccInfo";
    private final String SHARE_ACC_DET_FOR_DAO = "ShareAccDet";
    private final String JOINT_ACCNT_FOR_DAO = "JointAccntTO";
    
    //--- Declaration for DB Yes Or No
    private final String FLD_FOR_DB_YES_NO = "DBYesOrNo";
    private final String YES_FULL_STR = "Yes";
    private final String NO_FULL_STR = "No";
    //--- Declaration for Status
    private final String FLD_STATUS  = "Status";
    
    //--- Declarations for Share Acct Details
    private final String FLD_SHR_ACCT_DET_SHR_ACCT_NO = "ShareAcctNo";
    private final String FLD_SHR_ACCT_DET_SHR_NO_FROM = "ShareNoFrom";
    private final String FLD_SHR_ACCT_DET_SHR_NO_TO = "ShareNoTo";
    private final String FLD_SHR_ACCT_DET_NO_OF_SHARES = "NoOfShares";
    private final String FLD_SHR_ACCT_DET_RESOLUTION_NO = "ResolutionNo";
    private final String FLD_SHR_ACCT_DET_SHR_CERT_ISS_DATE = "ShareCertIsssDt";
    private final String FLD_SHR_ACCT_DET_AUTHORIZE = "Authorize";  //Added by Rajesh
    private final String FLD_SHR_ACCT_DET_AUTHORIZE_BY = "AuthorizeBy";  //Added by Rajesh
    private final String FLD_SHR_ACCT_DET_AUTHORIZE_DT = "AuthorizeDt";  //Added by Rajesh
    private final String FLD_SHR_ACCT_DET_STATUS = "Status";  //Added by Rajesh
    private final String FLD_SHR_ACCT_DET_STATUS_BY = "StatusBy";  //Added by Rajesh
    private final String FLD_SHR_ACCT_DET_STATUS_DT = "StatusDt";  //Added by Rajesh
    private final String FLD_SHR_ACCT_DET_SHARE_STATUS = "ShareStatus";  //Added by Rajesh
    private final String FLD_CHANGED = "Changed";  //Added by Rajesh
    private final String FLD_SHR_ACCT_DET_SHARES_VALUE = "SharesValue";
    private final String FLD_SHR_APPL_FEE_VALUE = "SharesAppValue";
    private final String FLD_SHR_MEM_FEE_VALUE = "SharesMemValue";
    private final String FLD_SHR_FEE_VALUE = "SharesFeeValue";
    private final String FLD_SHR_TOT_VALUE = "SharesTotValue";
    
    private final String ACCT_STATUS = "SHARE_ACCOUNT_STATUS";
    private final String CONSTITUTION = "CONSTITUTION";
    private final String SHARE_TYPE = "getShareType";
    private final String PROD_TYPE="PRODUCTTYPE";
    private final String FROMSL_NO="fromSl_No";
    private final String TOSL_NO="toSl_No";
    
    
    // If we are implementing Truetransact branchwise, Send to Resolution & Share Resolution option in HO are not possible
    // By Rajesh
    private LinkedHashMap shareTransDetails;
    
    //Added by Rajesh
    private String authorize = "";
    private String txtShareValue ;
    private String txtTotShareFee;
    private String txtShareApplFee;
    private String txtShareMemFee;
    private String txtShareTotAmount;
    private String txtNoShares;
    private HashMap admissionFeeMap = new HashMap();
    private String txtIDCardNo = "";
    //    added by nikhil for duplicate ID
    private String chkDuplicateIDCardYN = "";
    private String txtIDResolutionNo = "";
    private String tdtIDIssuedDt = "";
    private String tdtIDResolutionDt = "";
//    added by nikhil for Drf Receipt
    private String chkDrfApplicableYN= "";
    private String drfStatus = "";
    private HashMap drfApplicableMap =  new HashMap();
    private boolean drfApplicableFlag = false;
    private String productPaymentAmount = "";
    private String productAmount = "";
    
    private String txtImbp="";
    private String txtEmpRefNoNew="";
    private String txtEmpRefNoOld="";
    private String txtRatification="";
    private  String deletion="";
    private String newMode="";
    private String DueAmt="";
    private String shareStatus="";
    private String shareAcctStatusDueAmt="";
    private String shareAcctStatusAddWithdra="";
    
//  added by nikhil
private String caste = "";
    private String customersShare = "";
    private String govtsShare = "";
    private HashMap serviceTax_Map=null;
    private String lblServiceTaxval="";
    private ServiceTaxDetailsTO objservicetaxDetTo;
    private String fullClosureReq ="N";
    //mobile banking
    private boolean isMobileBanking = false;
    private String txtMobileNo = "";
    private String tdtMobileSubscribedFrom = "";
    SMSSubscriptionTO objSMSSubscriptionTO = null;
    /** Creates a new instance of TermDepositOB */
    public ShareOB() throws Exception{
        curDate = ClientUtil.getCurrentDate();
        proxy = ProxyFactory.createProxy();
        setOperationMap();
        fillDropdown();
        setJntAcccntTblCol();
        setTblShareActDetCol();
        setTblShareLoanDet();
        tblJointAccnt = new EnhancedTableModel(null, tblJointAccntColTitle);
        tblShareAcctDet = new EnhancedTableModel(null, tblShareAcctDetColTitle);
        tblShareLoanDet=new EnhancedTableModel(null,tblShareLoanDetTitle);
    }
    
    //    private static ShareOB shareOB; // singleton object
    //    static {
    //        try {
    //            shareOB = new ShareOB();
    //        } catch(Exception e) {
    //            parseException.logException(e,true);
    //        }
    //    }
    //
    //    public static ShareOB getInstance() {
    //        return shareOB;
    //    }
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, SHARE_JNDI);
        operationMap.put(CommonConstants.HOME, SHARE_HOME);
        operationMap.put(CommonConstants.REMOTE, SHARE_REMOTE);
        createDrfTransTable();
    }
    
    private void setTblShareLoanDet(){
        tblShareLoanDetTitle=new ArrayList();
        tblShareLoanDetTitle.add(objShareRB.getString("tblShareAccLoanColum1"));
        tblShareLoanDetTitle.add(objShareRB.getString("tblShareAccLoanColum2"));
        tblShareLoanDetTitle.add(objShareRB.getString("tblShareAccLoanColum3"));
        tblShareLoanDetTitle.add(objShareRB.getString("tblShareAccLoanColum4"));
        tblShareLoanDetTitle.add(objShareRB.getString("tblShareAccLoanColum5"));
        tblShareLoanDetTitle.add(objShareRB.getString("tblShareAccLoanColum6"));
        tblShareLoanDetTitle.add(objShareRB.getString("tblShareAccLoanColum7"));
    }
    /* To get the command type according to the Action */
    private String getCommand() throws Exception{
        String command = null;
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
            case ClientConstants.ACTIONTYPE_RENEW:
                command = CommonConstants.TOSTATUS_RENEW;
                break;
            default:
        }
        
        return command;
    }
     private void createDrfTransTable() throws Exception {
        final ArrayList drfTransColoumn = new ArrayList();
        drfTransColoumn.add("Transaction ID");
        drfTransColoumn.add("Reciept/Payment");
        drfTransColoumn.add("Amount");
        tblDrfTransaction = new EnhancedTableModel(null, drfTransColoumn);

    }
    public ArrayList populateShareSuspense(HashMap shareMap) {
        ArrayList arr = new ArrayList();
        System.out.println("#### inside populateShareSuspense shareMap : "+shareMap);
        try {
            List lst = ClientUtil.executeQuery("shareAcctFeeForShareAcctNo", shareMap);
            HashMap susMap = new HashMap();
            double aplFee = 0.0;
            double memFee = 0.0;
            double shareFee = 0.0;
            double shareAmt = 0.0;
            double totAmt = 0.0;
            if (lst!=null)
                if(lst.size()>0) {
                    susMap = (HashMap)lst.get(0);
                    aplFee = Double.parseDouble(CommonUtil.convertObjToStr(susMap.get("APPL_FEE")));
                    memFee = Double.parseDouble(CommonUtil.convertObjToStr(susMap.get("MEM_FEE")));
                    shareFee = Double.parseDouble(CommonUtil.convertObjToStr(susMap.get("SHARE_FEE")));
                    shareAmt = Double.parseDouble(CommonUtil.convertObjToStr(susMap.get("SHARE_AMOUNT")));
                    totAmt = aplFee + memFee + shareFee + shareAmt;
                }
            if (totAmt>0) {
                HashMap amtMap = new HashMap();
                amtMap.put("SHARETYPE", CommonUtil.convertObjToStr(susMap.get("SHARE_TYPE")));
                amtMap.put("AMOUNT", String.valueOf(totAmt));
                arr.add(amtMap);
            }
        }catch (Exception e) {
            System.out.println("#### Error in populateShareSuspense() : ");
            e.printStackTrace();
        }
        return arr;
    }
    
    public void doCOShareSuspense(ArrayList shareList) {
        try {
            HashMap suspMap = new HashMap();
            suspMap.put("ShareResolutionInfo", shareList);
            suspMap.put(CommonConstants.MODULE, getModule());
            suspMap.put(CommonConstants.SCREEN, getScreen());
            System.out.println("#### inside doCOShareSuspense suspMap : "+suspMap);
            HashMap proxyResultMap = proxy.execute(suspMap,operationMap);
        }catch (Exception e) {
            System.out.println("#### Error in doCOShareSuspense() : "+e);
        }
    }

    public EnhancedTableModel getTblDrfTransaction() {
        return tblDrfTransaction;
    }

    public void setTblDrfTransaction(EnhancedTableModel tblDrfTransaction) {
        this.tblDrfTransaction = tblDrfTransaction;
    }
    
    /** To perform the appropriate operation */
    public boolean doAction(NomineeOB objNomineeOB,NomineeOB objDrfNomineeOB) throws Exception {
        boolean result = true;
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if(getCommand() != null || getAuthorizeMap() != null){
                    //            System.out.println("getcboAcctStatus():" + getCboAcctStatus());
                    //            System.out.println("getcboConstitution():" + getCboConstitution());
                    //            System.out.println("getcboShareType():" + getCboShareType() );
                    doActionPerform(objNomineeOB,objDrfNomineeOB);
                }
                else{
                    throw new TTException(objShareRB.getString(TO_COMMAND_ERROR));
                }
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            result = false;
            System.out.println("e in ShareOB : " + e);
            
            e.printStackTrace();
            parseException.logException(e,true);
             resetForm();
        }
        return result;
    }
    
    /** To perform the necessary action */
    private void doActionPerform(NomineeOB objNomineeOB,NomineeOB objDrfNomineeOB) throws Exception{
        data = new HashMap();
        String s="";
        String ratification="Y";
        if (transactionDetailsTO == null)
            transactionDetailsTO = new LinkedHashMap();
        System.out.println("$$$$ActionType"+getActionType());
        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            data.put(CommonConstants.USER_ID, _authorizeMap.get(CommonConstants.USER_ID));
            if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
                data.put("SERVICE_TAX_AUTH", "SERVICE_TAX_AUTH");
            }
            if(isDrfApplicableFlag()){
//                data.put("DRF_APPLICABLE", "DRF_APPLICABLE");
                 HashMap drfApplicableMap = new HashMap();
                drfApplicableMap.put("DRF_PRODUCT",CommonUtil.convertObjToStr(getCboDrfProdId()));
                drfApplicableMap.put("DRF_MEMBER",CommonUtil.convertObjToStr(getTxtShareAcctNo()));
                drfApplicableMap.put("DRF_MEMBER_NAME",CommonUtil.convertObjToStr(getLblValCustName()));
                drfApplicableMap.put("DRF_PRODUCT_AMOUNT",productPaymentAmount);
                drfApplicableMap.put("DRF_AMOUNT",productAmount);
                data.put("DRF_APPLICABLE",drfApplicableMap);
            }
        }else if(getDueAmt().equals("True")){
            objShareAccInfoTO = setShareAccInfoData();
            transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
            data.put("TransactionTO",transactionDetailsTO);
            data.put("COLLECTING_DUE_AMOUNT","COLLECTING_DUE_AMOUNT");
            //data.put("SHARE_TYPE",getCboShareType());
            data.put("SHARE_TYPE",CommonUtil.convertObjToStr(cbmShareType.getKeyForSelected()));
            data.put("SHARE_NO",getTxtShareAcctNo());
            data.put("SHARE_ACC_INFO",objShareAccInfoTO);
            data.put("SHARE_ACCT_DETAILS",setShareAcctDetDataForDiffAmt());
            data.put("STATUS",getShareAcctStatusDueAmt());
            data.put("DET_NO",getShareAcctStatusAddWithdra());
        }
        else if(getTxtShareDetShareNoFrom().equals("ADD") || getTxtShareDetShareNoFrom().equals("WITHDRAWAL")){
            //            System.out.println("2getcboAcctStatus():" + getCboAcctStatus());
            //            System.out.println("2getcboConstitution():" + getCboConstitution());
            //            System.out.println("2getcboShareType():" + getCboShareType() );
            
            
            objShareAccInfoTO = setShareAccInfoData();
            s=getTxtRatification();
            String deletion=getDeletion();
            objShareAccInfoTO.setTxtRatification(s);
            objShareAccInfoTO.setCommand(getCommand());            
            data.put(SHARE_ACC_INFO_FOR_DAO, objShareAccInfoTO);
            data.put(SHARE_ACC_DET_FOR_DAO, setShareAcctDetData());
            transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
            data.put("TransactionTO",transactionDetailsTO);
            System.out.println("@@@@deletion"+deletion);
//            if(deletion=="Deleted"){
//                System.out.println("@@@@deletion"+deletion);
//                transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,getTransDetails());
//                data.put("TransactionTO",transactionDetailsTO);
//                data.put("DeletedShareDet", setShareAcctDetData());
//                //                data.put("DeletedTrans",getTransDetails());
//            }
          setDeletion("");
            if(getShareStatus().equals("CLOSED")){
                data.put("SHARE_STATUS","CLOSED");
            }
            // If we are implementing Truetransact branchwise, Send to Resolution & Share Resolution option in HO are not possible
            // By Rajesh
            data.put("ShareTransDetails",shareTransDetails);
//            
            if((drfApplicableFlag && getActionType()==ClientConstants.ACTIONTYPE_NEW) || (drfApplicableFlag && getTxtShareDetShareNoFrom().equals("ADD"))){
                HashMap drfApplicableMap = new HashMap();
                drfApplicableMap.put("DRF_PRODUCT",CommonUtil.convertObjToStr(getCboDrfProdId()));
                drfApplicableMap.put("DRF_MEMBER",CommonUtil.convertObjToStr(getTxtShareAcctNo()));
                drfApplicableMap.put("DRF_MEMBER_NAME",CommonUtil.convertObjToStr(getLblValCustName()));
                drfApplicableMap.put("DRF_PRODUCT_AMOUNT",productPaymentAmount);
                drfApplicableMap.put("DRF_AMOUNT",productAmount);
                data.put("DRF_APPLICABLE",drfApplicableMap);
            }
            
            
            //--- Puts the data if the Constitution is JointAccount
            if(objShareAccInfoTO.getConstitution().equals("JOINT_ACCOUNT")){
                data.put(JOINT_ACCNT_FOR_DAO,setJointAccntData());
            }
            
            /**
             * To Send the Nominee Related Data to the Account-DAO...
             */
            data.put("AccountNomineeTO", objNomineeOB.getNomimeeList());
            data.put("AccountNomineeDeleteTO", objNomineeOB.getDeleteNomimeeList());
            data.put("DRFAccountNomineeTO", objDrfNomineeOB.getNomimeeList());
            data.put("DRFAccountNomineeDeleteTO", objDrfNomineeOB.getDeleteNomimeeList());
            
            data.put(CommonConstants.MODULE, getModule());
            data.put(CommonConstants.SCREEN, getScreen());
            if(getCaste().equals("SC") || getCaste().equals("ST")){
                if(CommonUtil.convertObjToStr(getGovtsShare()).length()> 0 ){
                    System.out.println("@#$@#$@#$inside here :"+getGovtsShare());
                    data.put("GOVTS_SHARE",getGovtsShare());
                    data.put("SC/ST","SC/ST");
                }
            }
            
        }
            if ((getActionType() == ClientConstants.ACTIONTYPE_NEW || getTxtShareDetShareNoFrom().equals("ADD"))&& getServiceTax_Map() != null && getServiceTax_Map().size() > 0
                    && CommonUtil.convertObjToDouble(getServiceTax_Map().get("TOT_TAX_AMT"))>0) { // ADD condition added by nithya on 17-12-2020 for KD-2526
                data.put("serviceTaxDetails", getServiceTax_Map());
                data.put("serviceTaxDetailsTO", setServiceTaxDetails());
            }
        if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            if (transactionDetailsTO.containsKey(NOT_DELETED_TRANS_TOs) && transactionDetailsTO.get(NOT_DELETED_TRANS_TOs) != null) {
                HashMap transMap = (HashMap) transactionDetailsTO.get("NOT_DELETED_TRANS_TOs");
                System.out.println("Transaction with TL" + transMap);
                if (transMap.containsKey("1") && transMap.get("1") != null) {
                    TransactionTO transactionTO = (TransactionTO) transMap.get("1");
                    if (transactionTO.getProductType() != null && transactionTO.getProductType().equals("TL") && transactionTO.getProductId() != null) {
                        if (transactionTO.getDebitAcctNo() != null && transactionTO.getDebitAcctNo().length() > 0) {
                            //double taxAmt = calcServiceTaxAmount(transactionTO.getDebitAcctNo(), transactionTO.getProductId());
                            List taxSettingsList = calcServiceTaxAmount(transactionTO.getDebitAcctNo(), transactionTO.getProductId());    
                            //if (taxAmt > 0) {
                            if (taxSettingsList != null && taxSettingsList.size() > 0) {
                                HashMap ser_Tax_Val = new HashMap();
                                ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, curDate.clone());
                                //ser_Tax_Val.put(ServiceTaxCalculation.TOT_AMOUNT, taxAmt);
                                ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
                                ServiceTaxCalculation objServiceTax = new ServiceTaxCalculation();
                                serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                                if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                                    String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
                                    //serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));                                                                     
                                    serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);                                    
                                }
                                if (getServiceTax_Map() != null && getServiceTax_Map().size() > 0
                                        && CommonUtil.convertObjToDouble(getServiceTax_Map().get("TOT_TAX_AMT")) > 0) {
                                    data.put("serviceTaxDetails", getServiceTax_Map());
                                    data.put("serviceTaxDetailsTO", setServiceTaxDetails());
                                }
                            }
                        }
                    }
                }
            }
        }

        if (getActionType() == ClientConstants.ACTIONTYPE_NEW || getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            data.put("SMSSubscriptionTO", setSMSDetails());
        }

        System.out.println("#$#$#$ Datamap : "+data);
//        HashMap proxyResultMap = null;
        HashMap proxyResultMap = proxy.execute(data,operationMap);
        setResult(actionType);
        System.out.println("proxyResultMap : " + proxyResultMap);
        if (proxyResultMap != null && (proxyResultMap.containsKey(CommonConstants.TRANS_ID) || proxyResultMap.containsKey("CASH_TRANS_LIST") || proxyResultMap.containsKey("TRANSFER_TRANS_LIST"))) {
            setProxyReturnMap(proxyResultMap);
            if(proxyResultMap.containsKey(CommonConstants.TRANS_ID)){
                String alertMsg = "";
                if(s.equals(ratification)){
//                    ClientUtil.showMessageWindow("Share Application No: " + CommonUtil.convertObjToStr(proxyResultMap.get(CommonConstants.TRANS_ID)));
                    alertMsg += "Share Application No: ";
                }
                else{
//                    ClientUtil.showMessageWindow("Share Account No: " + CommonUtil.convertObjToStr(proxyResultMap.get(CommonConstants.TRANS_ID)));
                    alertMsg += "Share Account No: ";
                    
                }
                ClientUtil.showMessageWindow(alertMsg + CommonUtil.convertObjToStr(proxyResultMap.get(CommonConstants.TRANS_ID)));
            }
            
        }
        
        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        resetForm();
        data=null;
    }
    
    //----To make all the TO's null
    protected void makeToNull(){
        objShareAccInfoTO = null;
        objShareAcctDetailsTO = null;
    }
    
    
    private ShareAccInfoTO setShareAccInfoData(){
        objShareAccInfoTO = new ShareAccInfoTO();
        try{
            //            System.out.println("3getcboAcctStatus():" + getCboAcctStatus());
            //            System.out.println("3getcboConstitution():" + getCboConstitution());
            //            System.out.println("3getcboShareType():" + getCboShareType() );
            //            System.out.println("333CommonUtil.convertObjToStr(cbmAcctStatus.getKeyForSelected()):" + CommonUtil.convertObjToStr(getCbmAcctStatus().getKeyForSelected()));
            //            System.out.println("cbmAcctStatus.getKeyForSelected():" + cbmAcctStatus.getKeyForSelected());
            //            System.out.println("getCbmAcctStatus():getkeys" + getCbmAcctStatus().getKeys());
            
            objShareAccInfoTO.setCommand(getCommand());
            objShareAccInfoTO.setAcctStatus(CommonUtil.convertObjToStr(cbmAcctStatus.getKeyForSelected()));
            //            System.out.println("CommonUtil.convertObjToStr(cbmAcctStatus.getKeyForSelected()):" + CommonUtil.convertObjToStr(cbmAcctStatus.getKeyForSelected()));
            objShareAccInfoTO.setConstitution(CommonUtil.convertObjToStr(cbmConstitution.getKeyForSelected()));
            //            System.out.println("CommonUtil.convertObjToStr(cbmConstitution.getKeyForSelected()):" + CommonUtil.convertObjToStr(cbmConstitution.getKeyForSelected()));
            objShareAccInfoTO.setApplFee(CommonUtil.convertObjToDouble(getTxtApplFee()));
            objShareAccInfoTO.setConnectedGroup(getTxtConnGrpDet());
            objShareAccInfoTO.setCustId(getTxtCustId());
            objShareAccInfoTO.setTxtRatification(getTxtRatification());
            objShareAccInfoTO.setDirectorRelative(getTxtDirRelDet());
            Date IsIdDt = DateUtil.getDateMMDDYYYY(getTdtIssId());
            if(IsIdDt != null){
                Date isidDate = (Date)curDate.clone();
                isidDate.setDate(IsIdDt.getDate());
                isidDate.setMonth(IsIdDt.getMonth());
                isidDate.setYear(IsIdDt.getYear());
                //            objShareAccInfoTO.setIdIssueDt(DateUtil.getDateMMDDYYYY(getTdtIssId()));
                objShareAccInfoTO.setIdIssueDt(isidDate);
            }else{
                objShareAccInfoTO.setIdIssueDt(DateUtil.getDateMMDDYYYY(getTdtIssId()));
            }
            objShareAccInfoTO.setMemFee(CommonUtil.convertObjToDouble(getTxtMemFee()));
            
            Date NtElDt = DateUtil.getDateMMDDYYYY(getTdtNotEligiblePeriod());
            if(NtElDt != null){
                Date ntelDate = (Date)curDate.clone();
                ntelDate.setDate(NtElDt.getDate());
                ntelDate.setMonth(NtElDt.getMonth());
                ntelDate.setYear(NtElDt.getYear());
                //            objShareAccInfoTO.setNotEligibleDt(DateUtil.getDateMMDDYYYY(getTdtNotEligiblePeriod()));
                objShareAccInfoTO.setNotEligibleDt(ntelDate);
            }else{
                objShareAccInfoTO.setNotEligibleDt(DateUtil.getDateMMDDYYYY(getTdtNotEligiblePeriod()));
            }
            if(getChkNotEligibleStatus()== true){
                objShareAccInfoTO.setNotEligibleLoan(YES);
            } else {
                objShareAccInfoTO.setNotEligibleLoan(NO);
            }
            objShareAccInfoTO.setPropertyDetails(getTxtPropertyDetails());
            objShareAccInfoTO.setRelativeMembers(getTxtRelativeDetails());
            objShareAccInfoTO.setResolutionNo(getTxtResolutionNo());
            objShareAccInfoTO.setShareAmount(CommonUtil.convertObjToDouble(getTxtShareAmt()));
            objShareAccInfoTO.setShareFee(CommonUtil.convertObjToDouble(getTxtShareFee()));
            objShareAccInfoTO.setWelfareFundPaid(getTxtWelFund());
            objShareAccInfoTO.setShareType(CommonUtil.convertObjToStr(cbmShareType.getKeyForSelected()));
            
            //            System.out.println("CommonUtil.convertObjToStr(cbmShareType.getKeyForSelected()):" + CommonUtil.convertObjToStr(cbmShareType.getKeyForSelected()));
            objShareAccInfoTO.setRemarks(CommonUtil.convertObjToStr(getTxtRemarks()));
            objShareAccInfoTO.setCommAddrType(CommonUtil.convertObjToStr(cbmCommAddrType.getKeyForSelected()));
            /* If it is INSERT, Share Acct No. will take Generated Code
               else it will take code which is in textbox */
            if(this.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
                objShareAccInfoTO.setCreatedDt(curDate);
                String newfield=getNewMode();
                if(this.getTxtShareAcctNo().equals(NEW) || newfield.equals("true")){
                    objShareAccInfoTO.setShareAcctNo(null);
                } else {
                    objShareAccInfoTO.setShareAcctNo(getTxtShareAcctNo());
                }
                
                if(this.getTxtApplicationNo().equals(NEW)){
                    objShareAccInfoTO.setTxtApplicationNo(null);
                    
                } else {
                    objShareAccInfoTO.setTxtApplicationNo(getTxtApplicationNo());
                    
                }
            }
            else {
                objShareAccInfoTO.setShareAcctNo(getTxtShareAcctNo());
                objShareAccInfoTO.setTxtApplicationNo(getTxtApplicationNo());
                
            }
            
            objShareAccInfoTO.setCreatedBy(TrueTransactMain.USER_ID);
            objShareAccInfoTO.setStatusBy(TrueTransactMain.USER_ID);
            objShareAccInfoTO.setStatus(CommonConstants.STATUS_CREATED);
            objShareAccInfoTO.setStatusDt(curDate);
            objShareAccInfoTO.setInitiatedBranch(TrueTransactMain.BRANCH_ID);
            if(getActionType()==ClientConstants.ACTIONTYPE_NEW)
                objShareAccInfoTO.setBranchCode(TrueTransactMain.BRANCH_ID);
            else{
                objShareAccInfoTO.setBranchCode(getSelectedBranchID());//TrueTransactMain.BRANCH_ID); 
                // checking for branch code null
                System.out.println("share account number" + getTxtShareAcctNo());
                if (objShareAccInfoTO.getBranchCode() == null || objShareAccInfoTO.getBranchCode().equals("") || objShareAccInfoTO.getBranchCode().length() <= 0) {
                    HashMap mapData = new HashMap();
                    mapData.put("ACT_NUM", getTxtShareAcctNo());
                    List mapDataList = ClientUtil.executeQuery("getShareAccBranchData", mapData);
                    if (mapDataList != null && mapDataList.size() > 0) {
                        mapData = (HashMap) mapDataList.get(0);
                        objShareAccInfoTO.setBranchCode(CommonUtil.convertObjToStr(mapData.get("BRANCH_ID")));
                    }
                }
                // End 24-02-2018
            }                
            objShareAccInfoTO.setCboDivProdType(CommonUtil.convertObjToStr(cbmDivProdType.getKeyForSelected()));
            objShareAccInfoTO.setCboDivProdId(CommonUtil.convertObjToStr(cbmDivProdID.getKeyForSelected()));
            objShareAccInfoTO.setTxtDivAcNo(getTxtDivAcNo());
            objShareAccInfoTO.setCboDivPayMode(CommonUtil.convertObjToStr(cbmDivPayMode.getKeyForSelected()));
            objShareAccInfoTO.setIdCardNo(getTxtIDCardNo());
//            added by nikhil for duplicate ID
            objShareAccInfoTO.setTdtIDIssuedDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtIDIssuedDt())));
            objShareAccInfoTO.setChkDuplicateIDCardYN(CommonUtil.convertObjToStr(getChkDuplicateIDCardYN()));
//            added by Nikhil for Drf Receipt
            objShareAccInfoTO.setChkDrfApplicableYN(CommonUtil.convertObjToStr(getChkDrfApplicableYN()));
            objShareAccInfoTO.setCboDrfProdId(CommonUtil.convertObjToStr(cbmDrfProdId.getKeyForSelected()));
            
            objShareAccInfoTO.setTdtIDResolutionDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtIDResolutionDt())));
            objShareAccInfoTO.setTxtIDResolutionNo(CommonUtil.convertObjToStr(getTxtIDResolutionNo()));
            objShareAccInfoTO.setImbp(CommonUtil.convertObjToDouble(getTxtImbp()));
            objShareAccInfoTO.setEmpRefNoNew(getTxtEmpRefNoNew());
            objShareAccInfoTO.setEmpRefNoOld(getTxtEmpRefNoOld());
            //            System.out.println("4getcboAcctStatus():" + getCboAcctStatus());
            //            System.out.println("4getcboConstitution():" + getCboConstitution());
            //            System.out.println("4getcboShareType():" + getCboShareType() );
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return objShareAccInfoTO;
    }
    
    // Checks a/c no. existence without prod_type & prod_id
    /* To set Joint Account data in the Transfer Object*/
    public LinkedHashMap setJointAccntData(){
        HashMap singleRecordJntAcct;
        jntAcctTOMap = new LinkedHashMap();
        try{
            ShareJointTO objShareJointTO;
            int jntAcctSize = jntAcctAll.size();
            for(int i = 0;i<jntAcctSize;i++){
                singleRecordJntAcct = (HashMap)jntAcctAll.get(CommonUtil.convertObjToStr(jntAcctAll.keySet().toArray()[i]));
                objShareJointTO = new ShareJointTO();
                objShareJointTO.setCustId(CommonUtil.convertObjToStr(singleRecordJntAcct.get("CUST_ID")));
                objShareJointTO.setShareAcctNo(getTxtShareAcctNo());
                objShareJointTO.setStatus(CommonUtil.convertObjToStr(singleRecordJntAcct.get("STATUS")));
                objShareJointTO.setCommand(getCommand());
                jntAcctTOMap.put(String.valueOf(i),objShareJointTO);
                objShareJointTO = null;
                singleRecordJntAcct = null;
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return jntAcctTOMap;
    }
    
     private LinkedHashMap setShareAcctDetDataForDiffAmt() throws Exception {
         int row=tblShareAcctDet.getRowCount();
          LinkedHashMap shareAcctDetTOMap=new LinkedHashMap();
         HashMap hmap = (HashMap)shareAcctDetAll.get(String.valueOf(row-1));
         
         ShareAcctDetailsTO objShareAcctDetailsTO=new ShareAcctDetailsTO();        
         objShareAcctDetailsTO.setShareAcctNo(getTxtShareAcctNo());
         objShareAcctDetailsTO.setShareAcctDetNo(getTxtShareDetShareAcctNo());
         objShareAcctDetailsTO.setNoOfShares(CommonUtil.convertObjToInt(getTxtNoShares()));
         objShareAcctDetailsTO.setStatus("CREATED");
         objShareAcctDetailsTO.setCommand("INSERT");
         objShareAcctDetailsTO.setTxtShareApplFee(0.0);
         objShareAcctDetailsTO.setTxtShareMemFee(0.0);
         objShareAcctDetailsTO.setTxtShareTotAmount(getTxtShareTotAmount());
         objShareAcctDetailsTO.setTxtShareValue(CommonUtil.convertObjToDouble(getTxtShareTotAmount()));
         objShareAcctDetailsTO.setShareNoFrom("DIFFERENTIAL");
         objShareAcctDetailsTO.setStatus("CREATED");
         objShareAcctDetailsTO.setStatusBy(TrueTransactMain.USER_ID);
         objShareAcctDetailsTO.setStatusDt(curDate);
         objShareAcctDetailsTO.setShareCertIssueDt(curDate);
         objShareAcctDetailsTO.setTxtFromSL_No(txtFromSL_No);
         objShareAcctDetailsTO.setTxtToSL_No(txtToSL_No);
         shareAcctDetTOMap.put(String.valueOf(1), objShareAcctDetailsTO);
         return shareAcctDetTOMap;
     }

    private LinkedHashMap setShareAcctDetData() throws Exception {
        HashMap singleRecordShareAcctDet;
        shareAcctDetTOMap = new LinkedHashMap();
        String newField=getNewMode();
        // If we are implementing Truetransact branchwise, Send to Resolution & Share Resolution option in HO are not possible
        // By Rajesh
        shareTransDetails = new LinkedHashMap();
        // end
        //        try{
        ShareAcctDetailsTO objShareAcctDetailsTO;
        int tblShareAcctDetSize = shareAcctDetAll.size();
        for(int i = 0;i<tblShareAcctDetSize;i++){
            singleRecordShareAcctDet = (HashMap)shareAcctDetAll.get(String.valueOf(i));
            if(singleRecordShareAcctDet!=null && singleRecordShareAcctDet.size()>0) {
                objShareAcctDetailsTO = new ShareAcctDetailsTO();
                objShareAcctDetailsTO.setShareAcctDetNo(CommonUtil.convertObjToStr(singleRecordShareAcctDet.get(FLD_SL_NO)));
                objShareAcctDetailsTO.setNoOfShares(CommonUtil.convertObjToInt(singleRecordShareAcctDet.get(FLD_SHR_ACCT_DET_NO_OF_SHARES)));
                if(!newField.equals("true"))
                objShareAcctDetailsTO.setShareAcctNo(CommonUtil.convertObjToStr(singleRecordShareAcctDet.get(FLD_SHR_ACCT_DET_SHR_ACCT_NO)));
                //                objShareAcctDetailsTO.setShareCertIssueDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleRecordShareAcctDet.get(FLD_SHR_ACCT_DET_SHR_CERT_ISS_DATE))));
                Date IsIdDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleRecordShareAcctDet.get(FLD_SHR_ACCT_DET_SHR_CERT_ISS_DATE)));
                if(IsIdDt != null){
                    Date isidDate = (Date)curDate.clone();
                    isidDate.setDate(IsIdDt.getDate());
                    isidDate.setMonth(IsIdDt.getMonth());
                    isidDate.setYear(IsIdDt.getYear());
                    objShareAcctDetailsTO.setShareCertIssueDt(isidDate);
                }else{
                    objShareAcctDetailsTO.setShareCertIssueDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleRecordShareAcctDet.get(FLD_SHR_ACCT_DET_SHR_CERT_ISS_DATE))));
                }
                objShareAcctDetailsTO.setNoOfShares(CommonUtil.convertObjToInt(singleRecordShareAcctDet.get(FLD_SHR_ACCT_DET_NO_OF_SHARES)));
                objShareAcctDetailsTO.setTxtNoShares(CommonUtil.convertObjToStr(singleRecordShareAcctDet.get(FLD_SHR_ACCT_DET_NO_OF_SHARES)));
                objShareAcctDetailsTO.setTxtShareValue(CommonUtil.convertObjToDouble(singleRecordShareAcctDet.get(FLD_SHR_ACCT_DET_SHARES_VALUE)));
                objShareAcctDetailsTO.setTxtShareMemFee(CommonUtil.convertObjToDouble(singleRecordShareAcctDet.get(FLD_SHR_MEM_FEE_VALUE)));
                objShareAcctDetailsTO.setTxtShareApplFee(CommonUtil.convertObjToDouble(singleRecordShareAcctDet.get(FLD_SHR_APPL_FEE_VALUE)));
                objShareAcctDetailsTO.setTxtTotShareFee(CommonUtil.convertObjToDouble(singleRecordShareAcctDet.get(FLD_SHR_FEE_VALUE)));
                objShareAcctDetailsTO.setTxtNoShares(CommonUtil.convertObjToStr(singleRecordShareAcctDet.get(FLD_SHR_ACCT_DET_NO_OF_SHARES)));
                objShareAcctDetailsTO.setTxtShareTotAmount(CommonUtil.convertObjToStr(singleRecordShareAcctDet.get(FLD_SHR_TOT_VALUE)));
                objShareAcctDetailsTO.setTxtFromSL_No(CommonUtil.convertObjToInt(singleRecordShareAcctDet.get(FROMSL_NO)));
                objShareAcctDetailsTO.setTxtToSL_No(CommonUtil.convertObjToInt(singleRecordShareAcctDet.get(TOSL_NO)));
                 //                objShareAcctDetailsTO.setShareCertIssueDt((Date)singleRecordShareAcctDet.get(FLD_SHR_ACCT_DET_SHR_CERT_ISS_DATE));
                objShareAcctDetailsTO.setShareNoFrom(CommonUtil.convertObjToStr(singleRecordShareAcctDet.get(FLD_SHR_ACCT_DET_SHR_NO_FROM)));
                objShareAcctDetailsTO.setShareNoTo(CommonUtil.convertObjToStr(singleRecordShareAcctDet.get(FLD_SHR_ACCT_DET_SHR_NO_TO)));
                if (CommonUtil.convertObjToStr(singleRecordShareAcctDet.get(FLD_FOR_DB_YES_NO)).equals(YES_FULL_STR)) {
                    if (singleRecordShareAcctDet.containsKey(FLD_CHANGED) &&CommonUtil.convertObjToStr(singleRecordShareAcctDet.get(FLD_SHR_ACCT_DET_STATUS)).equals("MODIFIED")) {
                        objShareAcctDetailsTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
                        objShareAcctDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        //                        objShareAcctDetailsTO.setAuthorize(CommonUtil.convertObjToStr(singleRecordShareAcctDet.get(FLD_SHR_ACCT_DET_AUTHORIZE)));
                        //                        objShareAcctDetailsTO.setAuthorizeBy(CommonUtil.convertObjToStr(singleRecordShareAcctDet.get(FLD_SHR_ACCT_DET_AUTHORIZE_BY)));
                        //                        objShareAcctDetailsTO.setAuthorizeDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleRecordShareAcctDet.get(FLD_SHR_ACCT_DET_AUTHORIZE_DT))));
                        //                        objShareAcctDetailsTO.setStatusBy(CommonUtil.convertObjToStr(singleRecordShareAcctDet.get(FLD_SHR_ACCT_DET_STATUS_BY)));
                        //                        objShareAcctDetailsTO.setStatusDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleRecordShareAcctDet.get(FLD_SHR_ACCT_DET_STATUS_DT))));
                        //                        objShareAcctDetailsTO.setShareStatus(CommonUtil.convertObjToStr(singleRecordShareAcctDet.get(FLD_SHR_ACCT_DET_SHARE_STATUS)));
                    }
                    
                    else if (singleRecordShareAcctDet.containsKey(FLD_CHANGED) && CommonUtil.convertObjToStr(singleRecordShareAcctDet.get(FLD_SHR_ACCT_DET_STATUS)).equals("CREATED")) {
                        objShareAcctDetailsTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                        objShareAcctDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
                    }
                    else if ( CommonUtil.convertObjToStr(singleRecordShareAcctDet.get(FLD_SHR_ACCT_DET_STATUS)).equals("DELETED")) {
                        objShareAcctDetailsTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                        objShareAcctDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
                        objShareAcctDetailsTO.setAuthorize(CommonConstants.STATUS_REJECTED);
                        objShareAcctDetailsTO.setAuthorizeBy(TrueTransactMain.USER_ID);
                        objShareAcctDetailsTO.setAuthorizeDt(curDate);
                        
                    }
                    else if(getActionType()==ClientConstants.ACTIONTYPE_DELETE){
                        objShareAcctDetailsTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                        objShareAcctDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
                        
                    }
                    else {
                        objShareAcctDetailsTO.setCommand(null);
                    }
                } else {
                    objShareAcctDetailsTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                    objShareAcctDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
                    objShareAcctDetailsTO.setStatusBy(TrueTransactMain.USER_ID);
                    objShareAcctDetailsTO.setStatusDt(curDate);
                    // If we are implementing Truetransact branchwise, Send to Resolution & Share Resolution option in HO are not possible
                    // By Rajesh
                    objShareAcctDetailsTO.setShareStatus(CommonConstants.STATUS_RESOLUTION_ACCEPT);
                }
                objShareAcctDetailsTO.setStatusBy(TrueTransactMain.USER_ID);
                objShareAcctDetailsTO.setStatusDt(curDate);
                // If we are implementing Truetransact branchwise, Send to Resolution & Share Resolution option in HO are not possible
                // By Rajesh
                objShareAcctDetailsTO.setResolutionNo(CommonUtil.convertObjToStr(singleRecordShareAcctDet.get(FLD_SHR_ACCT_DET_RESOLUTION_NO)));
                // end
                shareAcctDetTOMap.put(String.valueOf(i),objShareAcctDetailsTO);
                // If we are implementing Truetransact branchwise, Send to Resolution & Share Resolution option in HO are not possible
                // By Rajesh
                if(getTxtShareAcctNo()!="")
                    shareTransDetails.put(getTxtShareAcctNo()+"_"+String.valueOf(i), getTransferTrans(objShareAcctDetailsTO));
                else
                    shareTransDetails.put(getTxtApplicationNo()+"-"+String.valueOf(i), getTransferTrans(objShareAcctDetailsTO));
                // end
                objShareAcctDetailsTO = null;
                singleRecordShareAcctDet = null;
            }}
        //        } catch(Exception e){
        //            parseException.logException(e,true);
        //        }
        return shareAcctDetTOMap;
    }
    
    // If we are implementing Truetransact branchwise, Send to Resolution & Share Resolution option in HO are not possible
    // By Rajesh
    private HashMap getTransferTrans(ShareAcctDetailsTO objShareAcctDetailsTO) {
        HashMap transMap = new HashMap();
        double shareTotalAmt = CommonUtil.convertObjToDouble(getTxtMemFee()).doubleValue()
        + CommonUtil.convertObjToDouble(getTxtShareFee()).doubleValue() * objShareAcctDetailsTO.getNoOfShares().doubleValue()
        + CommonUtil.convertObjToDouble(getTxtApplFee()).doubleValue()
        + CommonUtil.convertObjToDouble(getTxtShareAmt()).doubleValue() * objShareAcctDetailsTO.getNoOfShares().doubleValue();
        transMap.put("AMOUNT", new Double(shareTotalAmt));
        return transMap;
    }
    // End
    
    public boolean validateShareNos(HashMap whereMap) {
        boolean isExist=false;
        int cnt = 0;
        List listCnt = null;
        if (txtShareDetShareNoFrom.length()>0) {
            listCnt = (List)ClientUtil.executeQuery("getCountForExistingShareNos",whereMap);
            if(listCnt!=null && listCnt.size() >0){
                cnt = Integer.parseInt(CommonUtil.convertObjToStr(listCnt.get(0)));
            }
        }
        if (txtShareDetShareNoTo.length()>0) {
            whereMap.put("SHARE_NO", txtShareDetShareNoTo);
            listCnt = (List)ClientUtil.executeQuery("getCountForExistingShareNos",whereMap);
            if(listCnt!=null && listCnt.size() >0){
                cnt += Integer.parseInt(CommonUtil.convertObjToStr(listCnt.get(0)));
            }
        }
        isExist = cnt>0 ? true : false;
        listCnt = null;
        whereMap = null;
        return isExist;
    }
    
    /* To Populate the Data */
    public void populateData(HashMap whereMap, NomineeOB objNomineeOB,NomineeOB objDRFNomineeOB) {
        try {
            System.out.println("whereMap:" + whereMap);
            String accountno=CommonUtil.convertObjToStr(whereMap.get("SHARE ACCOUNT NO"));
            String appno=CommonUtil.convertObjToStr(whereMap.get("SHARE APPLICATION NO"));
            String accno="";
            String applno="";
            
            if(!accountno.equals(accno)){
                whereMap.put("SHARE ACCOUNT NO", whereMap.get("SHARE ACCOUNT NO"));
            }
            else {
                whereMap.put("SHARE APPLICATION NO", whereMap.get("SHARE APPLICATION NO"));
            }
            HashMap mapData = proxy.executeQuery(whereMap, operationMap);
            System.out.println("mapData:" + mapData);
            populateOB(mapData, objNomineeOB, objDRFNomineeOB);
            String add="ADD";
            //tblShareAcctDet.setValueAt(add, 0,4);
            // If we are implementing Truetransact branchwise, Send to Resolution & Share Resolution option in HO are not possible
            // By Rajesh
            //            List list = (List)mapData.get("TransactionTO");
            //            transactionOB.setDetails(list);
            // End
            mapData = null;
        } catch( Exception e ) {
            System.out.println("e:" + e);
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    private void setShareLoanTab(List shareLoanlist){
        ArrayList loanList=new ArrayList();
        ArrayList allList=new ArrayList();
        balance=0;
        if(shareLoanlist !=null && shareLoanlist.size()>0){
            System.out.println("shareloanlist###"+shareLoanlist.get(0));
            for (int i = 0; i < shareLoanlist.size(); i++) {
                HashMap map = (HashMap) shareLoanlist.get(i); //for testing
                loanList.add(map.get("CUST_ID"));
                loanList.add(map.get("PRODESC"));
                loanList.add(map.get("ACCT_NUM"));
                loanList.add(map.get("LIMIT_AMT"));
                loanList.add(map.get("ACCT_OPEN_DT"));
                loanList.add(map.get("LOAN_BALANCE_PRINCIPAL"));
                loanList.add(map.get("LOAN_STATUS"));
                //                loanList.add(map.get(map.get("ACCT_NUM")));
                if (CommonUtil.convertObjToDouble(map.get("LOAN_BALANCE_PRINCIPAL")) > 0 ) {
                    balance = balance + CommonUtil.convertObjToDouble(map.get("LOAN_BALANCE_PRINCIPAL"));
                }
                allList.add(loanList);
                loanList = new ArrayList();
                //                break;
            }
           
           tblShareLoanDet.setDataArrayList(allList,tblShareLoanDetTitle);
        }
    }
    private void populateOB(HashMap mapData, NomineeOB objNomineeOB, NomineeOB objDRFNomineeOB) throws Exception{
        ArrayList nomineeList = null;
        ArrayList drfNomineeList = null;//Added By Revathi.L
        List shareAcctDetList;
        HashMap shareAcctMap = (HashMap)((List)mapData.get("ShareAcctInfo")).get(0);
        setShareAcctInfo(shareAcctMap);
        if(mapData.containsKey("ShareAcctDetails")){
            shareAcctDetList = (List) ((List) mapData.get("ShareAcctDetails"));
            setShareAcctDetails(shareAcctDetList);
            
        }
        if(mapData.containsKey("ShareLoanList")) {
            setListShareAcct( ((List) mapData.get("ShareLoanList")));
            List shareLoanlist=(List)(mapData.get("ShareLoanList"));
            setShareLoanTab(shareLoanlist);
        }
        if(mapData.containsKey("AUTH_TRANS_DETAILS")){
            setProxyReturnMap((HashMap) mapData.get("AUTH_TRANS_DETAILS"));
        }
        
        //--- To Populate the Main Customer
        HashMap queryMap = new HashMap();
        queryMap.put("CUST_ID",getTxtCustId());
        populateScreen(queryMap, false);
        
        //--- To populate Communication Address Type
        setCbmCommAddrType(getCbmCommAddrType());
        setCboCommAddrType(CommonUtil.convertObjToStr(getCbmCommAddrType().getDataForKey(CommonUtil.convertObjToStr(shareAcctMap.get("COMM_ADDR_TYPE")))));
        
        //--- populates if the Constitution is Joint Account
        if(getCboConstitution().equals("Joint Account")) {
            List jntAccntDetailsList =  (List) ((List) mapData.get("JointAcctDetails"));
            setJointAcctDetails(jntAccntDetailsList);
            jntAccntDetailsList = null;
        }
        
        /* To set the ArrayList in NomineeOB so as to set the data in the Nominee-Table...*/
        nomineeList =  (ArrayList) mapData.get("AccountNomineeList");
        objNomineeOB.setNomimeeList(nomineeList);
        objNomineeOB.setNomineeTabData();
        
        //Added By Revathi.L
        drfNomineeList =  (ArrayList) mapData.get("DRFAccountNomineeList");
        objDRFNomineeOB.setNomimeeList(drfNomineeList);
        objDRFNomineeOB.setNomineeTabData();
        
        //--- Sets the Maximum allowed nominee
        HashMap where = new HashMap();
        where.put("SHARE_TYPE", shareAcctMap.get("SHARE_TYPE"));
        List feeListData = ClientUtil.executeQuery("getFeeData",where);
        HashMap feeMapData;
        feeMapData = (HashMap) feeListData.get(0);
        maxNominee = Integer.parseInt(CommonUtil.convertObjToStr(feeMapData.get("ALLOWED_NOMINEE")));
        List  list = (List) mapData.get("TransactionTO");
        if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            if (feeMapData != null && feeMapData.containsKey("FULLCLOSURE_REQUIRED")) {
                setFullClosureReq(CommonUtil.convertObjToStr(feeMapData.get("FULLCLOSURE_REQUIRED")));
            }
        }
        ArrayList alist = new ArrayList();
        if (list != null && list.size() > 0) {
            int l = list.size();
            if (l > 0) {
                if (!getTxtApplicationNo().equals("") && ((l == 1) || (l == 2))) {
                    alist.add(list.get(0));
                } else if (!getTxtApplicationNo().equals("") && l >= 3) {
                    alist.add(list.get(l - 2));
                } else {
                    alist.add(list.get(l - 1));
                }
            }
        }
        if (list!= null && list.size()>0) {
            if (getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                transactionOB.setDetails(alist);
                setTrans(list);
            } else {
                setTrans(list);
            }
            //            if(transactionOB.getLoneActNum()!=null)

            //                loanActMap.put("LINK_BATCH_ID",transactionOB.getLoneActNum()) ;
        }
        
        objSMSSubscriptionTO = null;
        if (mapData.containsKey("SMSSubscriptionTO") && ((List) mapData.get("SMSSubscriptionTO")).size() > 0) {
            objSMSSubscriptionTO = (SMSSubscriptionTO) ((List) mapData.get("SMSSubscriptionTO")).get(0);
            objSMSSubscriptionTO.setStatus(CommonConstants.STATUS_MODIFIED);
            setSMSSubscriptionTO(objSMSSubscriptionTO);
        }
        
        objNomineeOB.ttNotifyObservers();
        objDRFNomineeOB.ttNotifyObservers();//Added By Revathi.L
        shareAcctMap = null;
        shareAcctDetList = null;
    }
    
    public void setTransDetails(List transList) {
        try{
            DeleteTranslist=new LinkedHashMap();
            if(transList!=null && transList.size()>0 )
            if(transList.get(0)!=null){
            DeleteTranslist.put("1" ,transList.get(0));
//            java.util.LinkedHashMap hashmap=new java.util.LinkedHashMap();
//                      hashmap.put("0", transList.get(0));
//                       setAllowedTransactionDetailsTO(hashmap);
            transactionOB.setDetails(transList);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
    
    public LinkedHashMap getTransDetails(){
        return  DeleteTranslist;
    }
    
    
    //    public  ArrayList getDeleteShareDet(){
    //        Deleatelst.add(getTxtApplFee());
    //        Deleatelst.add(getTxtShareValue());
    //        Deleatelst.add(getTxtTotShareFee());
    //        Deleatelst.add(getTxtShareMemFee());
    //        Deleatelst.add(getTxtShareTotAmount());
    //        return  Deleatelst;
    //    }
    
    private void setShareAcctInfo(HashMap shareAcctMap){
        //If "NOT_ELIGIBLE_LOAN" is "Y" in database then check the Tax Deductions Checkbox
        if (CommonUtil.convertObjToStr(shareAcctMap.get("NOT_ELIGIBLE_LOAN")).equals(YES)) {
            setChkNotEligibleStatus(true);
        }else {
            setChkNotEligibleStatus(false);
        }
        setStatusBy(CommonUtil.convertObjToStr(shareAcctMap.get("STATUS_BY")));
        //        System.out.println("shareAcctMap:" + shareAcctMap);
        setTxtApplFee(CommonUtil.convertObjToStr(shareAcctMap.get("APPL_FEE")));
        setTxtConnGrpDet(CommonUtil.convertObjToStr(shareAcctMap.get("CONNECTED_GROUP")));
        setTxtCustId(CommonUtil.convertObjToStr(shareAcctMap.get("CUST_ID")));
        setTxtDirRelDet(CommonUtil.convertObjToStr(shareAcctMap.get("DIRECTOR_RELATIVE")));
        setTxtMemFee(CommonUtil.convertObjToStr(shareAcctMap.get("MEM_FEE")));
        setTxtPropertyDetails(CommonUtil.convertObjToStr(shareAcctMap.get("PROPERTY_DETAILS")));
        setTxtRelativeDetails(CommonUtil.convertObjToStr(shareAcctMap.get("RELATIVE_MEMBERS")));
        setTxtResolutionNo(CommonUtil.convertObjToStr(shareAcctMap.get("RESOLUTION_NO")));
        setTxtShareAcctNo(CommonUtil.convertObjToStr(shareAcctMap.get("SHARE_ACCT_NO")));
        setTxtApplicationNo(CommonUtil.convertObjToStr(shareAcctMap.get("SHARE_APPL_NO")));
        setTxtShareAmt(CommonUtil.convertObjToStr(shareAcctMap.get("SHARE_AMOUNT")));
        setTxtShareFee(CommonUtil.convertObjToStr(shareAcctMap.get("SHARE_FEE")));
        setTxtWelFund(CommonUtil.convertObjToStr(shareAcctMap.get("WELFARE_FUND_PAID")));
        setTdtIssId(DateUtil.getStringDate((Date)shareAcctMap.get("ID_ISSUE_DT")));
        setTdtNotEligiblePeriod(DateUtil.getStringDate((Date)shareAcctMap.get("NOT_ELIGIBLE_DT")));
        setCboAcctStatus(CommonUtil.convertObjToStr(getCbmAcctStatus().getDataForKey(CommonUtil.convertObjToStr(shareAcctMap.get("ACCT_STATUS")))));
        setCboConstitution(CommonUtil.convertObjToStr(getCbmConstitution().getDataForKey(CommonUtil.convertObjToStr(shareAcctMap.get("CONSTITUTION")))));
        setTxtRemarks(CommonUtil.convertObjToStr(shareAcctMap.get("REMARKS")));
        setCboShareType(CommonUtil.convertObjToStr(getCbmShareType().getDataForKey(CommonUtil.convertObjToStr(shareAcctMap.get("SHARE_TYPE")))));
        //        setCboCommAddrType(CommonUtil.convertObjToStr(getCbmCommAddrType().getDataForKey(CommonUtil.convertObjToStr(shareAcctMap.get("COMM_ADDR_TYPE")))));
        //        if(CommonUtil.convertObjToStr(shareAcctMap.get("AUTHORIZE")).equals("AUTHORIZED")){
        //            authorised = true;
        //        } else {
        //            authorised = false;
        //        }
        
        setCboDivPayMode(CommonUtil.convertObjToStr(getCbmDivPayMode().getDataForKey(CommonUtil.convertObjToStr(shareAcctMap.get("DIVIDEND_PAY_MODE")))));
        getCbmDivPayMode().setKeyForSelected(CommonUtil.convertObjToStr(shareAcctMap.get("DIVIDEND_PAY_MODE")));
        setCboDivProdType(CommonUtil.convertObjToStr(getCbmDivProdType().getDataForKey(CommonUtil.convertObjToStr(shareAcctMap.get("DIVIDEND_CREDIT_PRODUCT")))));
        getCbmDivProdType().setKeyForSelected(CommonUtil.convertObjToStr(shareAcctMap.get("DIVIDEND_CREDIT_PRODUCT")));
        callProdIdsForProductType(CommonUtil.convertObjToStr(shareAcctMap.get("DIVIDEND_CREDIT_PRODUCT")));
        getCbmDivProdID().setKeyForSelected(CommonUtil.convertObjToStr(shareAcctMap.get("DIVIDEND_CREDIT_PRODUCT_ID")));
         System.out.println("the prod id is"+getCbmDivProdID());
        setCboDivProdId(CommonUtil.convertObjToStr(getCbmDivProdID().getDataForKey(CommonUtil.convertObjToStr(shareAcctMap.get("DIVIDEND_CREDIT_PRODUCT_ID")))));
        System.out.println("the prod id is"+getCboDivProdId());
        
        setTxtDivAcNo(CommonUtil.convertObjToStr(shareAcctMap.get("DIVIDEND_CREDIT_AC")));
        setLblDivAmt(CommonUtil.convertObjToStr(shareAcctMap.get("DIVIDEND_AMOUNT")));
        setLblBalanceAmt(CommonUtil.convertObjToStr(shareAcctMap.get("OUTSTANDING_AMOUNT")));
        setTxtIDCardNo(CommonUtil.convertObjToStr(shareAcctMap.get("ID_CARD_NO")));
//        added by Nikhil for Duplicate ID
        setChkDuplicateIDCardYN(CommonUtil.convertObjToStr(shareAcctMap.get("DUPLICATE_ID")));
//        added by Nikhil for Drf Aplicable
        setChkDrfApplicableYN(CommonUtil.convertObjToStr(shareAcctMap.get("DRF_APPLICABLE")));
        setDrfStatus(CommonUtil.convertObjToStr(shareAcctMap.get("DRF_STATUS")));
        setCboDrfProdId(CommonUtil.convertObjToStr(getCbmDrfProdId().getDataForKey(CommonUtil.convertObjToStr(shareAcctMap.get("DRF_PRODUCT")))));
        
        setTxtIDResolutionNo(CommonUtil.convertObjToStr(shareAcctMap.get("ID_RESOLUTION_NO")));
        setTdtIDIssuedDt(CommonUtil.convertObjToStr(shareAcctMap.get("IDCARD_ISSUE_DT")));
        setTdtIDResolutionDt(CommonUtil.convertObjToStr(shareAcctMap.get("RESOLUTION_DT")));
        
        setTxtImbp(CommonUtil.convertObjToStr(shareAcctMap.get("IMBP")));
        setTxtEmpRefNoNew(CommonUtil.convertObjToStr(shareAcctMap.get("EMP_REFNO_NEW")));
        setTxtEmpRefNoOld(CommonUtil.convertObjToStr(shareAcctMap.get("EMP_REFNO_OLD")));
//        added by nikhil for DRF Applicable
        if(shareAcctMap.containsKey("DRF_APPLICABLE") && shareAcctMap.containsKey("DRF_STATUS")){
            if(CommonUtil.convertObjToStr(shareAcctMap.get("DRF_APPLICABLE")).equals("Y")) 
            {
                setDrfApplicableFlag(true);
            }
        }
    }
    
    private void setShareAcctDetails(List shareAcctDetList){
        shareAcctDetAll = new HashMap();
        HashMap calcShareAcctDet;
        int shareAcctDetListSize = shareAcctDetList.size();
        for(int i=0; i<shareAcctDetListSize; i++){
            calcShareAcctDet = (HashMap) shareAcctDetList.get(i);
            shareAcctDetRec = new HashMap();
            shareAcctDetRec.put(FROMSL_NO, CommonUtil.convertObjToStr(calcShareAcctDet.get("FROMSL_NO")));
            shareAcctDetRec.put(TOSL_NO, CommonUtil.convertObjToStr(calcShareAcctDet.get("TOSL_NO")));
            shareAcctDetRec.put(FLD_SL_NO, CommonUtil.convertObjToStr(calcShareAcctDet.get("SHARE_ACCT_DET_NO")));
            shareAcctDetRec.put(FLD_SHR_ACCT_DET_NO_OF_SHARES, CommonUtil.convertObjToStr(calcShareAcctDet.get("NO_OF_SHARES")));
            shareAcctDetRec.put(FLD_SHR_ACCT_DET_SHR_ACCT_NO, CommonUtil.convertObjToStr(calcShareAcctDet.get("SHARE_ACCT_NO")));
            shareAcctDetRec.put(FLD_SHR_ACCT_DET_SHR_CERT_ISS_DATE, DateUtil.getStringDate((Date) calcShareAcctDet.get("SHARE_CERT_ISSUE_DT")));
            shareAcctDetRec.put(FLD_SHR_ACCT_DET_RESOLUTION_NO, CommonUtil.convertObjToStr(calcShareAcctDet.get("RESOLUTION_NO")));
            shareAcctDetRec.put(FLD_SHR_ACCT_DET_SHR_NO_FROM, CommonUtil.convertObjToStr(calcShareAcctDet.get("SHARE_NO_FROM")));
            shareAcctDetRec.put(FLD_SHR_ACCT_DET_SHR_NO_TO, CommonUtil.convertObjToStr(calcShareAcctDet.get("SHARE_NO_TO")));
            shareAcctDetRec.put(FLD_SHR_ACCT_DET_AUTHORIZE, CommonUtil.convertObjToStr(calcShareAcctDet.get("AUTHORIZE")));  //Added by Rajesh
            shareAcctDetRec.put(FLD_SHR_ACCT_DET_AUTHORIZE_BY, CommonUtil.convertObjToStr(calcShareAcctDet.get("AUTHORIZE_BY")));  //Added by Rajesh
            shareAcctDetRec.put(FLD_SHR_ACCT_DET_AUTHORIZE_DT, CommonUtil.convertObjToStr(calcShareAcctDet.get("AUTHORIZE_DT")));  //Added by Rajesh
            shareAcctDetRec.put(FLD_SHR_ACCT_DET_STATUS_BY, CommonUtil.convertObjToStr(calcShareAcctDet.get("STATUS_BY")));  //Added by Rajesh
            shareAcctDetRec.put(FLD_SHR_ACCT_DET_STATUS_DT, CommonUtil.convertObjToStr(calcShareAcctDet.get("STATUS_DT")));  //Added by Rajesh
            shareAcctDetRec.put(FLD_SHR_ACCT_DET_SHARE_STATUS, CommonUtil.convertObjToStr(calcShareAcctDet.get("SHARE_STATUS")));  //Added by Rajesh
            shareAcctDetRec.put(FLD_STATUS, CommonUtil.convertObjToStr(calcShareAcctDet.get("STATUS")));
            shareAcctDetRec.put(FLD_SHR_ACCT_DET_SHARES_VALUE, CommonUtil.convertObjToStr(calcShareAcctDet.get("SHARE_VALUE")));
            shareAcctDetRec.put(FLD_SHR_APPL_FEE_VALUE,CommonUtil.convertObjToStr(calcShareAcctDet.get("SHARE_APPL_FEE")));
            shareAcctDetRec.put(FLD_SHR_FEE_VALUE, CommonUtil.convertObjToStr(calcShareAcctDet.get("SHARE_FEE")));
            shareAcctDetRec.put(FLD_SHR_MEM_FEE_VALUE, CommonUtil.convertObjToStr(calcShareAcctDet.get("SHARE_MEM_FEE")));
            //--- This is for checking the record is coming from Database Table  or newly
            //--- entered in the UI
            shareAcctDetRec.put(FLD_FOR_DB_YES_NO , YES_FULL_STR);
            shareAcctDetAll.put(String.valueOf(i), shareAcctDetRec);
            shareAcctDetRow = new ArrayList();
            if(!(CommonUtil.convertObjToStr(shareAcctDetRec.get(FLD_STATUS)).equals(CommonConstants.STATUS_DELETED))&& !(CommonUtil.convertObjToStr(shareAcctDetRec.get(FLD_SHR_ACCT_DET_AUTHORIZE)).equals(CommonConstants.STATUS_REJECTED))){
                shareAcctDetRow.add(0, CommonUtil.convertObjToStr(calcShareAcctDet.get("SHARE_ACCT_DET_NO")));
                shareAcctDetRow.add(1, DateUtil.getStringDate((Date) calcShareAcctDet.get("SHARE_CERT_ISSUE_DT")));
                shareAcctDetRow.add(2, CommonUtil.convertObjToStr(calcShareAcctDet.get("NO_OF_SHARES")));
                shareAcctDetRow.add(3, CommonUtil.convertObjToStr(calcShareAcctDet.get("SHARE_VALUE")));
                shareAcctDetRow.add(4, CommonUtil.convertObjToStr(calcShareAcctDet.get("SHARE_NO_FROM")));
                shareAcctDetRow.add(5, CommonUtil.convertObjToStr(calcShareAcctDet.get("AUTHORIZE")));
                tblShareAcctDet.insertRow(tblShareAcctDet.getRowCount(),shareAcctDetRow);
            }
        }
        if(shareAcctDetRec!=null && shareAcctDetRec.size()>0 && shareAcctDetRec.get(FLD_SL_NO)!=null)
            shareAcctDetCount= (int)Integer.parseInt(CommonUtil.convertObjToStr(shareAcctDetRec.get(FLD_SL_NO)));
        shareAcctDetRow = null;
        shareAcctDetRec = null;
        calcShareAcctDet = null;
    }
    
    private void setJointAcctDetails(List jntAccntDetailsList){
        jntAcctAll = new LinkedHashMap();
        HashMap custMapData;
        HashMap calcJointAcct;
        List custListData;
        int jntAccntDetailsListSize = jntAccntDetailsList.size();
        for(int i=0; i<jntAccntDetailsListSize; i++){
            calcJointAcct =  (HashMap) jntAccntDetailsList.get(i);
            jntAcctSingleRec = new HashMap();
            jntAcctSingleRec.put("CUST_ID", CommonUtil.convertObjToStr(calcJointAcct.get("CUST_ID")));
            jntAcctSingleRec.put(FLD_FOR_DB_YES_NO, YES_FULL_STR);
            jntAcctSingleRec.put("STATUS", CommonUtil.convertObjToStr(calcJointAcct.get("STATUS")));
            jntAcctAll.put(CommonUtil.convertObjToStr(calcJointAcct.get("CUST_ID")), jntAcctSingleRec);
            if(!CommonUtil.convertObjToStr(calcJointAcct.get("STATUS")).equals("DELETED")){
                custListData = ClientUtil.executeQuery("getSelectAccInfoTblDisplay",jntAcctSingleRec);
                custMapData = (HashMap) custListData.get(0);
                objJointAcctHolderManipulation.setJntAcctTableData(custMapData,true,tblJointAccnt);
                setTblJointAccnt(objJointAcctHolderManipulation.getTblJointAccnt());
            }
            custMapData = null;
            jntAcctSingleRec = null;
            calcJointAcct = null;
            custListData = null;
        }
    }
    
    private  void setJntAcccntTblCol() throws Exception{
        try{
            tblJointAccntColTitle = new ArrayList();
            tblJointAccntColTitle.add(objShareRB.getString(TBL_JNT_ACCNT_COLUMN_1));
            tblJointAccntColTitle.add(objShareRB.getString(TBL_JNT_ACCNT_COLUMN_2));
            tblJointAccntColTitle.add(objShareRB.getString(TBL_JNT_ACCNT_COLUMN_3));
            tblJointAccntColTitle.add(objShareRB.getString(TBL_JNT_ACCNT_COLUMN_4));
//            added by nikhil for share subsidy
            tblJointAccntColTitle.add(objShareRB.getString(TBL_JNT_ACCNT_COLUMN_5));
        }catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    //--- resets the Joint Accnt Holder Table
    public void resetJntAccntHoldTbl(){
        tblJointAccnt.setDataArrayList(null, tblJointAccntColTitle);
    }
    
    //--- resets the ShareAcctDet Table
    public void resetShareAcctDetTbl(){
        tblShareAcctDet.setDataArrayList(null, tblShareAcctDetColTitle);
    }
    
    
    //--- resets the Customer Details
    public void resetCustDetails(){
        setLblValArea("");
        setLblValCity("");
        setLblValCountry("");
        setLblValCustomerName("");
        setLblValDateOfBirth("");
        setLblValPin("");
        setLblValState("");
        setLblValStreet("");
        setLblValCustName("");
    }
    
    /** resets the UI form */
    public void resetForm(){
        resetShareAccInfo();
        resetFeeDetails();
        resetShareAcctDet();
        resetCustDetails();
        resetJntAccntHoldTbl();
        resetShareAcctDetTbl();
        resetShareLoanTbl();
        setNewMode("");
        resetDrfTransListTable();
        setLblStatus(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL]);
        // nominee and PoA list
        nomineeTOList = null;
        resetObjects();
        setTrans(null);
        setTransDetails(null);
        setDueAmt("");
        setShareStatus("");
        setShareAcctStatusDueAmt("CREATED");
        setShareAcctStatusAddWithdra("");
        setFullClosureReq("");
        resetMobileBankingDetails();
    }
     public void resetDrfTransListTable() {
        for (int i = tblDrfTransaction.getRowCount(); i > 0; i--) {
            tblDrfTransaction.removeRow(0);
        }
    }
    public void resetObjects() {
        shareAcctDetAll = null;
        shareAcctDetCount = 0;
        shareAcctDetRow = null;
        listShareAcct=null;
        serviceTax_Map=null;
    }
    
    public void resetFeeDetails(){
        setTxtApplFee("");
        setTxtMemFee("");
        setTxtShareAmt("");
        setTxtShareFee("");
        setAdmissionFeeMap(null);
    }
    public void resetShareLoanTbl(){
        tblShareLoanDet.setDataArrayList(null,tblShareLoanDetTitle);
    }
    public void resetShareAccInfo(){
        setTxtConnGrpDet("");
        setTxtCustId("");
        setTxtDirRelDet("");
        setTxtPropertyDetails("");
        setTxtRelativeDetails("");
        setTxtResolutionNo("");
        setTxtShareAcctNo("");
        setTxtApplicationNo("");
        setTxtShareDetNoOfShares("");
        setTxtResolutionNo1("");
        setTxtShareDetShareAcctNo("");
        setTxtShareDetShareNoFrom("");
        setTxtShareDetShareNoTo("");
        setTxtWelFund("");
        setChkNotEligibleStatus(false);
        setCboAcctStatus("");
        setCboConstitution("");
        setCboCommAddrType("");
        setCboShareType("");
        setTdtNotEligiblePeriod("");
        setTdtIssId("");
        setCboDivProdId("");
        setCboDivProdType("");
        setTxtDivAcNo("");
        setCboDivPayMode("");
        setLblDivAmt("");
        setLblBalanceAmt("");
        setTxtIDCardNo("");
//        added by nikhil
        setTxtIDResolutionNo("");
        setChkDuplicateIDCardYN("");
        setCboDrfProdId("");
        setChkDrfApplicableYN("");
        setTdtIDResolutionDt("");
        setTdtIDIssuedDt("");
        
        setTxtImbp("");
        setTxtEmpRefNoNew("");
        setTxtEmpRefNoOld("");
//        added by nikhil
        setCaste("");
        setGovtsShare("");
        setCustomersShare("");
    }
    
    public void resetShareAcctDet(){
        setTxtShareDetNoOfShares("");
        setTxtResolutionNo1("");
        setTxtShareDetShareAcctNo("");
        setTxtShareDetShareNoFrom("");
        setTxtShareDetShareNoTo("");
        setTdtShareDetIssShareCert("");
        setTxtShareValue("");
        setTxtTotShareFee("");
        setTxtShareApplFee("");
        setTxtShareMemFee("");
        setTxtShareTotAmount("");
        setTxtNoShares("");
        setLblServiceTaxval("");
        setTxtFromSL_No(0);
        setTxtToSL_No(0);
        
    }
    
    public void populateFeeDetails(){
        try {
            HashMap where = new HashMap();
            //            System.out.println("cboShareType:" + getCboShareType());
            //            System.out.println("cbmShareType.getKeyForSelected():" + CommonUtil.convertObjToStr(getCbmShareType().getKeys()));
            where.put("SHARE_TYPE", CommonUtil.convertObjToStr(cbmShareType.getKeyForSelected()));
            List feeListData = ClientUtil.executeQuery("getFeeData",where);
            //            System.out.println("feeListData:" + feeListData);
            HashMap feeMapData;
            if(feeListData.size()>0){
                feeMapData = (HashMap) feeListData.get(0);
                setTxtApplFee(CommonUtil.convertObjToStr(feeMapData.get("APPLICATION_FEE")));
                calcAddmissionFee(feeMapData);
                //                setTxtMemFee(CommonUtil.convertObjToStr(feeMapData.get("ADMISSION_FEE")));
                setTxtShareAmt(CommonUtil.convertObjToStr(feeMapData.get("FACE_VALUE")));
                setTxtShareFee(CommonUtil.convertObjToStr(feeMapData.get("SHARE_FEE")));
                //--- Sets the Maximum allowed nominee
                maxNominee = Integer.parseInt(CommonUtil.convertObjToStr(feeMapData.get("ALLOWED_NOMINEE")));
            }
            if (getActionType() != ClientConstants.ACTIONTYPE_NEW && TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
                where = new HashMap();
                where.put("ACCT_NUM", getTxtShareAcctNo());
                if (getTxtShareAcctNo() != null && !getTxtShareAcctNo().equals("")) {
                    feeListData = ClientUtil.executeQuery("getServiceTaxAmount", where);
                    if (feeListData != null && feeListData.size() > 0) {
                        HashMap resultmap = (HashMap) feeListData.get(0);
                        if (resultmap != null && resultmap.containsKey("TOTAL_TAX_AMOUNT")) {
                            setLblServiceTaxval(CommonUtil.convertObjToStr(resultmap.get("TOTAL_TAX_AMOUNT")));
                         }
                    }
                }
            }
            feeListData = null;
            feeMapData=null;
            
        } catch( Exception e ) {
            parseException.logException(e,true);
        }
    }
     public void populateDrfTransTable(List drfTransList) {
        System.out.println("######drfTransList:" + drfTransList);

        HashMap drfTransTableMap = new HashMap();
        int length = drfTransList.size();
        for (int i = 0; i < length; i++) {
            ArrayList technicalTabRow = new ArrayList();
            drfTransTableMap = (HashMap) drfTransList.get(i);
            technicalTabRow = new ArrayList();
            technicalTabRow.add(CommonUtil.convertObjToStr(drfTransTableMap.get("DRF_TRANS_ID")));
            technicalTabRow.add(CommonUtil.convertObjToStr(drfTransTableMap.get("RECIEPT_OR_PAYMENT")));
            technicalTabRow.add(CommonUtil.convertObjToStr(drfTransTableMap.get("AMOUNT")));
            tblDrfTransaction.insertRow(tblDrfTransaction.getRowCount(), technicalTabRow);

        }

    }
      public void populateDrfTransData(HashMap hashMap, int panEditDelete) {
          String shareNo = CommonUtil.convertObjToStr(getTxtShareAcctNo());
          HashMap supMap = new HashMap();
          supMap.put("SHARE_NO", shareNo);
          List lstSupName = ClientUtil.executeQuery("getDRFTransIdForSelectedItem", supMap);
          HashMap supMap1 = new HashMap();
          if (lstSupName != null && lstSupName.size() > 0) {
              supMap1 = (HashMap) lstSupName.get(0);
          }
          String drfTransID = CommonUtil.convertObjToStr(supMap1.get("DRF_PROD_ID"));
          String resNo = "";
          Date resDate = null;
          String resoDate = "";
          HashMap whereMap = new HashMap();
          LinkedHashMap dataMap = null;
          List rowList = new ArrayList();
          List depreciationList = new ArrayList();
          String mapNameDT = "";
          String mapNameED = "";
          String mapNameGA = "";
          hashMap.put("PROD_ID", hashMap.get("DRF_PROD_ID"));
          whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
          whereMap.put("DRF_TRANS_ID", drfTransID);
          whereMap.put("DRF_TRANS_ID", drfTransID);
          HashMap drfTransTableMap = new HashMap();
        if (getActionType() != ClientConstants.ACTIONTYPE_NEW) {
         mapNameDT = "getMemberDrfTransDetails";
         List list = ClientUtil.executeQuery(mapNameDT, hashMap);
         for (int i = 0; i < list.size(); i++) {
                System.out.println("iiiiiiiiiiii" + i);
                ArrayList technicalTabRow = new ArrayList();
                drfTransTableMap = (HashMap) list.get(i);
                technicalTabRow = new ArrayList();
                technicalTabRow.add(CommonUtil.convertObjToStr(drfTransTableMap.get("DRF_TRANS_ID")));
                technicalTabRow.add(CommonUtil.convertObjToStr(drfTransTableMap.get("RECIEPT_OR_PAYMENT")));
                technicalTabRow.add(CommonUtil.convertObjToStr(drfTransTableMap.get("AMOUNT")));
                tblDrfTransaction.insertRow(tblDrfTransaction.getRowCount(), technicalTabRow);
                if (!CommonUtil.convertObjToStr(drfTransTableMap.get("RESOLUTION_NO")).equals("")) {
                    resNo = CommonUtil.convertObjToStr(drfTransTableMap.get("RESOLUTION_NO"));
                }
                if (!CommonUtil.convertObjToStr(drfTransTableMap.get("RESOLUTION_DATE")).equals("")) {
                resoDate=CommonUtil.convertObjToStr(drfTransTableMap.get("RESOLUTION_DATE"));
                }

            }

            System.out.println("resNoresNoresNo" + resNo);
            setTxtResolutionNo(resNo);
            }
        whereMap = null;
    }

    public void calcAddmissionFee(HashMap feeMapData){
        System.out.println("#$%#$%#$%feeMapData:"+feeMapData);
        //        admissionFeeMap = new HashMap();
        setAdmissionFeeMap(feeMapData);
        if(CommonUtil.convertObjToStr(feeMapData.get("ADMISSION_FEE_TYPE")).equals("FIXED")){
            setTxtMemFee(CommonUtil.convertObjToStr(feeMapData.get("ADMISSION_FEE")));
        }else if(CommonUtil.convertObjToStr(feeMapData.get("ADMISSION_FEE_TYPE")).equals("PERCENT")){
            if(getTxtMemFee()== null && getTxtMemFee().length() <= 0){
                setTxtMemFee("");
            }
        }
    }
    
    public void checkForDrfApplicable(){
        System.out.println("@#$@#$@#$getChkDrfApplicableYN()"+getChkDrfApplicableYN());
        System.out.println("@#$@#$@#getDrfStatus()"+getDrfStatus());
        if(CommonUtil.convertObjToStr(getChkDrfApplicableYN()).equals("Y")){
            if(getDrfStatus().equals("UNAUTHORIZED")){
                setDrfApplicableFlag(true);
            }else{
                setDrfApplicableFlag(false);
            }
        }
//        where.put("SHARE_TYPE", CommonUtil.convertObjToStr(cbmShareType.getKeyForSelected()));
//            List feeListData = ClientUtil.executeQuery("getFeeData",where);
    }
    
    public void populateScreen(HashMap queryWhereMap, boolean jntAcctNewClicked) {
        try {
            queryWhereMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            List custListData = ClientUtil.executeQuery("getSelectAccInfoDisplay",queryWhereMap );
            if(custListData.size()>0) {
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME,null);
                final ArrayList lookup_keys = new ArrayList();
                lookUpHash.put(CommonConstants.MAP_NAME,"getCustAddrType");
                lookUpHash.put(CommonConstants.PARAMFORQUERY,queryWhereMap);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
                cbmCommAddrType = new ComboBoxModel(key,value);
                makeNull();
                HashMap custMapData;
                custMapData = (HashMap) custListData.get(0);
                if(jntAcctNewClicked==false){//--- If it is Main acctnt,set CustomerId in Main
                    setTxtCustId(CommonUtil.convertObjToStr(custMapData.get("CUST_ID")));
                    objJointAcctHolderManipulation.setJntAcctTableData(custMapData, false, tblJointAccnt);
                    setTblJointAccnt(objJointAcctHolderManipulation.getTblJointAccnt());
                }
                setLblValCustomerName(CommonUtil.convertObjToStr(custMapData.get("Name")));
                setLblValDateOfBirth(DateUtil.getStringDate((Date)custMapData.get("DOB")));
                setLblValStreet(CommonUtil.convertObjToStr(custMapData.get("STREET")));
                setLblValArea(CommonUtil.convertObjToStr(custMapData.get("AREA")));
                setLblValCity(CommonUtil.convertObjToStr(custMapData.get("CITY1")));
                setLblValState(CommonUtil.convertObjToStr(custMapData.get("STATE1")));
                setLblValCountry(CommonUtil.convertObjToStr(custMapData.get("COUNTRY")));
                setLblValPin(CommonUtil.convertObjToStr(custMapData.get("PIN_CODE")));
//                added by nikhil for subsidy
                if(CommonUtil.convertObjToStr(custMapData.get("CASTE")).length() > 0){
                    setCaste(CommonUtil.convertObjToStr(custMapData.get("CASTE")));
                }else{
                    setCaste("");
                }                
                //setLblValCustName(CommonUtil.convertObjToStr(custMapData.get("Name")));
                setLblValCustName(CommonUtil.convertObjToStr((custMapData.get("Name") == null) ? custMapData.get("NAME") : custMapData.get("Name")));
                membershipType = CommonUtil.convertObjToStr(custMapData.get("MEMBERSHIP_CLASS"));
                custListData = null;
                custMapData=null;
            }else {
                cbmCommAddrType = new ComboBoxModel();
                ClientUtil.displayAlert("Invalid Customer Id No");
            }
        } catch( Exception e ) {
            parseException.logException(e,true);
        }
    }
    
    /** Used to Populate the UI TextFields and combo of Share Account Details,
     *  on clicking the Share Account Details table
     *  @param row ---> int data type, row selected from the SaherAcctDetail CTable
     */
    public ArrayList getShareDet(){
        try{
            DeleteShareDet=new ArrayList();
            DeleteShareDet.add(getTxtShareDetShareAcctNo());
            DeleteShareDet.add(getTxtShareDetNoOfShares());
            DeleteShareDet.add(getTxtResolutionNo1());
            DeleteShareDet.add(getTxtShareDetShareNoFrom());
            DeleteShareDet.add(getTxtShareDetShareNoTo());
            DeleteShareDet.add(getTdtShareDetIssShareCert());
            DeleteShareDet.add(getAuthorize());  //Added by Rajesh
            DeleteShareDet.add(getTxtNoShares());
            DeleteShareDet.add(getTxtShareValue());
            DeleteShareDet.add(getTxtShareMemFee());
            DeleteShareDet.add(getTxtShareApplFee());
            DeleteShareDet.add(getTxtTotShareFee());
            DeleteShareDet.add(getTxtNoShares());
            DeleteShareDet.add(getTxtShareTotAmount());
            //  setLblStatus(CommonUtil.convertObjToStr(listShareAcctDetAll.get(FLD_STATUS)));
            
            
        }catch(Exception e){
            parseException.logException(e,true);
            e.printStackTrace();
        }
        return  DeleteShareDet ;
    }
    
    public void populateShareAcctDetFields(int row){
        try{
            HashMap listShareAcctDetAll = (HashMap) shareAcctDetAll.get(String.valueOf(row));
            shareAcctDetMode = 1; // Set the mode for "Modification" of record
            shareAcctDetRowDel = row;
            if(listShareAcctDetAll!=null){
            setTxtShareDetShareAcctNo(CommonUtil.convertObjToStr(listShareAcctDetAll.get(FLD_SHR_ACCT_DET_SHR_ACCT_NO)));
            setTxtShareDetNoOfShares(CommonUtil.convertObjToStr(listShareAcctDetAll.get(FLD_SHR_ACCT_DET_NO_OF_SHARES)));
            setTxtResolutionNo1(CommonUtil.convertObjToStr(listShareAcctDetAll.get(FLD_SHR_ACCT_DET_RESOLUTION_NO)));
            setTxtShareDetShareNoFrom(CommonUtil.convertObjToStr(listShareAcctDetAll.get(FLD_SHR_ACCT_DET_SHR_NO_FROM)));
            setTxtShareDetShareNoTo(CommonUtil.convertObjToStr(listShareAcctDetAll.get(FLD_SHR_ACCT_DET_SHR_NO_TO)));
            setTdtShareDetIssShareCert(CommonUtil.convertObjToStr(listShareAcctDetAll.get(FLD_SHR_ACCT_DET_SHR_CERT_ISS_DATE)));
            setAuthorize(CommonUtil.convertObjToStr(listShareAcctDetAll.get(FLD_SHR_ACCT_DET_AUTHORIZE)));  //Added by Rajesh
            setTxtNoShares(CommonUtil.convertObjToStr(listShareAcctDetAll.get(FLD_SHR_ACCT_DET_NO_OF_SHARES)));
            setTxtShareValue(CommonUtil.convertObjToStr(listShareAcctDetAll.get(FLD_SHR_ACCT_DET_SHARES_VALUE)));
            setTxtShareMemFee(CommonUtil.convertObjToStr(listShareAcctDetAll.get(FLD_SHR_MEM_FEE_VALUE)));
            setTxtShareApplFee(CommonUtil.convertObjToStr(listShareAcctDetAll.get(FLD_SHR_APPL_FEE_VALUE)));
            setTxtTotShareFee(CommonUtil.convertObjToStr(listShareAcctDetAll.get(FLD_SHR_FEE_VALUE)));
            setTxtNoShares(CommonUtil.convertObjToStr(listShareAcctDetAll.get(FLD_SHR_ACCT_DET_NO_OF_SHARES)));
            setTxtShareTotAmount(CommonUtil.convertObjToStr(listShareAcctDetAll.get(FLD_SHR_TOT_VALUE)));
            setLblStatus(CommonUtil.convertObjToStr(listShareAcctDetAll.get(FLD_STATUS)));
            setTxtFromSL_No(CommonUtil.convertObjToInt(listShareAcctDetAll.get(FROMSL_NO)));
            setTxtToSL_No(CommonUtil.convertObjToInt(listShareAcctDetAll.get(TOSL_NO)));
            listShareAcctDetAll = null;
            }
        }catch(Exception e){
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    /** Deletes the Selected Record from the Authorized signatory Ctable
     * @param intRowCount ---> int data type, the Selected Row that has to be deleted
     * @param deleteButtonPresssed ---> boolean data type, Enter whether Delete Button is clicked or not
     */
    public void delShareAcctDet(int intRowCount, boolean deleteButtonPresssed){
        HashMap dataHash = objTableManipulation.delTab(shareAcctDetRowCount, deleteButtonPresssed, shareAcctDetStatus, shareAcctDetAll, shareAcctDetCount, shareAcctDetRowDel, shareAcctDetGetSelectedRowCount, tblShareAcctDet);
        shareAcctDetCount = Integer.parseInt(CommonUtil.convertObjToStr(dataHash.get("COUNT")));
        shareAcctDetAll = (HashMap)dataHash.get("ALL_VALUES");
        tblShareAcctDet = (EnhancedTableModel)dataHash.get("TABLE_MODEL");
        setTblShareAcctDet((EnhancedTableModel)dataHash.get("TABLE_MODEL"));
    }
    
    /** Used to display the Data entry of "Share Account Details" panel in the CTable
     */
    public void populateShareAcctTbl(){
        shareAcctDetRow = new ArrayList();
        shareAcctDetRow.add(ClientUtil.getCurrentDateinDDMMYYYY());
        shareAcctDetRow.add(getTxtNoShares());
        shareAcctDetRow.add(getTxtShareValue());
        shareAcctDetRow.add(getTxtShareDetShareNoFrom());
        shareAcctDetRow.add(null);
        shareAcctDetRec = getNewShareAcctDetValues() ;
        HashMap retHash = objTableManipulation.populateTbl(shareAcctDetCount, shareAcctDetMode, shareAcctDetRowDel, shareAcctDetGetSelectedRowCount, shareAcctDetRowCount, shareAcctDetAll, shareAcctDetRec, tblShareAcctDet, tblShareAcctDetColTitle, shareAcctDetRow, shareAcctDetStatus);
        shareAcctDetCount = Integer.parseInt(CommonUtil.convertObjToStr(retHash.get("COUNT")));
        shareAcctDetRowDel = Integer.parseInt(CommonUtil.convertObjToStr(retHash.get("ROW_DEL")));
        shareAcctDetGetSelectedRowCount = Integer.parseInt(CommonUtil.convertObjToStr(retHash.get("GET_SELECTED_ROW_COUNT")));
        shareAcctDetRowCount = Integer.parseInt(CommonUtil.convertObjToStr(retHash.get("ROW_COUNT" )));
        shareAcctDetAll = (HashMap)retHash.get("ALL_VALUES");
        tblShareAcctDet = (EnhancedTableModel)retHash.get("TABLE_MODEL");
        setTblShareAcctDet((EnhancedTableModel)retHash.get("TABLE_MODEL"));
        shareAcctDetK = Integer.parseInt(CommonUtil.convertObjToStr(retHash.get("INT_CHECK")));
    }
    
    private HashMap getNewShareAcctDetValues(){
        shareAcctDetRec = new HashMap();
        shareAcctDetRec.put(FLD_SL_NO, shareAcctDetTxtSerialNo);
        shareAcctDetRec.put(FLD_SHR_ACCT_DET_NO_OF_SHARES, getTxtNoShares());
        shareAcctDetRec.put(FLD_SHR_ACCT_DET_RESOLUTION_NO, getTxtResolutionNo1());
        shareAcctDetRec.put(FLD_SHR_ACCT_DET_SHR_ACCT_NO, getTxtShareDetShareAcctNo());
        shareAcctDetRec.put(FLD_SHR_ACCT_DET_SHR_NO_FROM, getTxtShareDetShareNoFrom());
        shareAcctDetRec.put(FLD_SHR_ACCT_DET_SHR_NO_TO, getTxtShareDetShareNoTo());
        shareAcctDetRec.put(FLD_SHR_ACCT_DET_SHR_CERT_ISS_DATE, CommonUtil.convertObjToStr(ClientUtil.getCurrentDateinDDMMYYYY()));
        shareAcctDetRec.put(FLD_SHR_ACCT_DET_SHARES_VALUE,getTxtShareValue());
        shareAcctDetRec.put(FLD_SHR_APPL_FEE_VALUE, getTxtShareApplFee());
        shareAcctDetRec.put(FLD_SHR_MEM_FEE_VALUE,getTxtShareMemFee());
        shareAcctDetRec.put(FLD_SHR_FEE_VALUE,getTxtTotShareFee());
        shareAcctDetRec.put(FLD_SHR_TOT_VALUE,getTxtShareTotAmount());
        shareAcctDetRec.put(FROMSL_NO,getTxtFromSL_No());
        shareAcctDetRec.put(TOSL_NO,getTxtToSL_No());
//        Added by Nikhil for subsidy
        if(getCaste()!= null && (getCaste().equals("SC") || getCaste().equals("ST"))){
            shareAcctDetRec.put("GOVERNMENTS_SHARE",getGovtsShare());
            System.out.println("!@@#$@#$@#$inside subsidyshareAcctDetRec:"+shareAcctDetRec);
        }
        
        //--- To represent the data is not from the database
        if (shareAcctDetAll!=null && shareAcctDetAll.containsKey(String.valueOf(shareAcctDetRowDel))) {
            HashMap singleRecordShareAcctDet = (HashMap)shareAcctDetAll.get(String.valueOf(shareAcctDetRowDel));
            shareAcctDetRec.put(FLD_FOR_DB_YES_NO, singleRecordShareAcctDet.get(FLD_FOR_DB_YES_NO));
            singleRecordShareAcctDet = null;
        } else {
            shareAcctDetRec.put(FLD_FOR_DB_YES_NO, NO_FULL_STR);
        }
        //--- To set the status
        shareAcctDetRec.put(FLD_STATUS, shareAcctDetStatus);
        shareAcctDetRec.put(FLD_CHANGED, "");
        return shareAcctDetRec;
    }
    
    public void populateJointAccntTable(HashMap queryWhereMap){
        try {
            jntAcctAll =  objJointAcctHolderManipulation.populateJointAccntTable(queryWhereMap, jntAcctAll, tblJointAccnt);
            setTblJointAccnt(objJointAcctHolderManipulation.getTblJointAccnt());
        } catch( Exception e ) {
            parseException.logException(e,true);
        }
    }
    
    void setTblJointAccnt(EnhancedTableModel tblJointAccnt){
        this.tblJointAccnt = tblJointAccnt;
        setChanged();
    }
    EnhancedTableModel getTblJointAccnt(){
        return this.tblJointAccnt;
    }
    public void setTotalAmount(String amount){
        this.amount=amount;
    }
    
    public String getTotalAmount(){
        return amount;
    }
    public void moveToMain(String mainAccntRow, String strRowSelected , int intRowSelected){
        jntAcctAll = objJointAcctHolderManipulation.moveToMain(mainAccntRow, strRowSelected, intRowSelected, tblJointAccnt, jntAcctAll);
        setTxtCustId(strRowSelected);
        setTblJointAccnt(objJointAcctHolderManipulation.getTblJointAccnt());
    }
    
    public void delJointAccntHolder(String strDelRowCount, int intDelRowCount){
        jntAcctAll =  objJointAcctHolderManipulation.delJointAccntHolder(strDelRowCount, intDelRowCount, tblJointAccnt, jntAcctAll);
        setTblJointAccnt(objJointAcctHolderManipulation.getTblJointAccnt());
    }
    
    private  void setTblShareActDetCol() throws Exception{
        try{
            tblShareAcctDetColTitle = new ArrayList();
            tblShareAcctDetColTitle.add(objShareRB.getString(TBL_SHR_ACCNT_DET_COLUMN_1));
            tblShareAcctDetColTitle.add(objShareRB.getString(TBL_SHR_ACCNT_DET_COLUMN_2));
            tblShareAcctDetColTitle.add(objShareRB.getString(TBL_SHR_ACCNT_DET_COLUMN_3));
            tblShareAcctDetColTitle.add(objShareRB.getString(TBL_SHR_ACCNT_DET_COLUMN_4));
            tblShareAcctDetColTitle.add(objShareRB.getString(TBL_SHR_ACCNT_DET_COLUMN_5));
            tblShareAcctDetColTitle.add(objShareRB.getString(TBL_SHR_ACCNT_DET_COLUMN_6));
        }catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    private String txtRemarks = "";
    private String cboShareType = "";
    private ComboBoxModel cbmShareType;
    private ComboBoxModel cbmCommAddrType;
    private String cboCommAddrType = "";
    private String lblValCustName = "";
    private ComboBoxModel cbmConstitution;
    private ComboBoxModel cbmAcctStatus;
    private ComboBoxModel cbmDivProdType;
    private ComboBoxModel cbmDivPayMode;
    private ComboBoxModel cbmDivProdID;
    private String txtShareAcctNo = "";
    private String txtApplicationNo = "";
    private String txtResolutionNo = "";
    private String txtPropertyDetails = "";
    private String txtRelativeDetails ="";
    private String txtConnGrpDet = "";
    private String txtCustId = "";
    private String cboAcctStatus = "";
    private String cboConstitution = "";
    private String cboDivProdType="";
    private String cboDivProdId="";
//    added by Nikhil For DRF Applicable
    private String cboDrfProdId = "";
    private ComboBoxModel cbmDrfProdId;
    private String cboDivPayMode="";
    private String txtDivAcNo="";
    private String txtApplFee = "";
    private String txtMemFee = "";
    private String txtDirRelDet = "";
    private String txtWelFund = "";
    private String txtDupIdCard = "";
    private String tdtIssId = "";
    private String tdtNotEligiblePeriod = "";
    private boolean chkNotEligibleStatus = false;
    private String txtShareFee = "";
    private String txtShareAmt = "";
    private String txtShareDetShareAcctNo = "";
    private String txtShareDetShareNoFrom = "";
    private String txtShareDetShareNoTo = "";
    private String txtShareDetNoOfShares = "";
    private String txtResolutionNo1 = "";
    private String tdtShareDetIssShareCert = "";
    private String lblBalanceAmt = "";
    private String lblDivAmt="";
    private String lblValCustomerName = "";
    private String lblValDateOfBirth = "";
    private String lblValStreet = "";
    private String lblValArea = "";
    private String lblValCity = "";
    private String lblValState = "";
    private String lblValCountry = "";
    private String lblValPin = "";
    private List  listShareAcct=null;
    private Integer txtFromSL_No=0;
    private Integer txtToSL_No=0;

    public int getTxtFromSL_No() {
        return txtFromSL_No;
    }

    public void setTxtFromSL_No(int txtFromSL_No) {
        this.txtFromSL_No = txtFromSL_No;
    }

    public int getTxtToSL_No() {
        return txtToSL_No;
    }

    public void setTxtToSL_No(int txtToSL_No) {
        this.txtToSL_No = txtToSL_No;
    }
    
    
    // Setter method for txtShareAcctNo
    void setTxtShareAcctNo(String txtShareAcctNo){
        this.txtShareAcctNo = txtShareAcctNo;
        setChanged();
    }
    // Getter method for txtShareAcctNo
    String getTxtShareAcctNo(){
        return this.txtShareAcctNo;
    }
    
    void setTxtApplicationNo(String txtApplicationNo){
        this.txtApplicationNo = txtApplicationNo;
        setChanged();
    }
    // Getter method for txtShareAcctNo
    String getTxtApplicationNo(){
        return this.txtApplicationNo;
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
    
    // Setter method for txtPropertyDetails
    void setTxtPropertyDetails(String txtPropertyDetails){
        this.txtPropertyDetails = txtPropertyDetails;
        setChanged();
    }
    // Getter method for txtPropertyDetails
    String getTxtPropertyDetails(){
        return this.txtPropertyDetails;
    }
    
    // Setter method for txtRelativeDetails
    void setTxtRelativeDetails(String txtRelativeDetails){
        this.txtRelativeDetails = txtRelativeDetails;
        setChanged();
    }
    // Getter method for txtRelativeDetails
    String getTxtRelativeDetails(){
        return this.txtRelativeDetails;
    }
    
    // Setter method for txtConnGrpDet
    void setTxtConnGrpDet(String txtConnGrpDet){
        this.txtConnGrpDet = txtConnGrpDet;
        setChanged();
    }
    // Getter method for txtConnGrpDet
    String getTxtConnGrpDet(){
        return this.txtConnGrpDet;
    }
    
    // Setter method for txtCustId
    void setTxtCustId(String txtCustId){
        this.txtCustId = txtCustId;
        setChanged();
    }
    // Getter method for txtCustId
    String getTxtCustId(){
        return this.txtCustId;
    }
    
    // Setter method for cboAcctStatus
    void setCboAcctStatus(String cboAcctStatus){
        this.cboAcctStatus = cboAcctStatus;
        setChanged();
    }
    // Getter method for cboAcctStatus
    String getCboAcctStatus(){
        return this.cboAcctStatus;
    }
    
    // Setter method for txtApplFee
    void setTxtApplFee(String txtApplFee){
        this.txtApplFee = txtApplFee;
        setChanged();
    }
    // Getter method for txtApplFee
    String getTxtApplFee(){
        return this.txtApplFee;
    }
    
    // Setter method for txtMemFee
    void setTxtMemFee(String txtMemFee){
        this.txtMemFee = txtMemFee;
        setChanged();
    }
    // Getter method for txtMemFee
    String getTxtMemFee(){
        return this.txtMemFee;
    }
    
    // Setter method for txtDirRelDet
    void setTxtDirRelDet(String txtDirRelDet){
        this.txtDirRelDet = txtDirRelDet;
        setChanged();
    }
    // Getter method for txtDirRelDet
    String getTxtDirRelDet(){
        return this.txtDirRelDet;
    }
    
    // Setter method for txtWelFund
    void setTxtWelFund(String txtWelFund){
        this.txtWelFund = txtWelFund;
        setChanged();
    }
    // Getter method for txtWelFund
    String getTxtWelFund(){
        return this.txtWelFund;
    }
    
    
    // Setter method for tdtIssId
    void setTdtIssId(String tdtIssId){
        this.tdtIssId = tdtIssId;
        setChanged();
    }
    // Getter method for tdtIssId
    String getTdtIssId(){
        return this.tdtIssId;
    }
    
    // Setter method for tdtNotEligiblePeriod
    void setTdtNotEligiblePeriod(String tdtNotEligiblePeriod){
        this.tdtNotEligiblePeriod = tdtNotEligiblePeriod;
        setChanged();
    }
    // Getter method for tdtNotEligiblePeriod
    String getTdtNotEligiblePeriod(){
        return this.tdtNotEligiblePeriod;
    }
    
    // Setter method for chkNotEligibleStatus
    void setChkNotEligibleStatus(boolean chkNotEligibleStatus){
        this.chkNotEligibleStatus = chkNotEligibleStatus;
        setChanged();
    }
    // Getter method for chkNotEligibleStatus
    boolean getChkNotEligibleStatus(){
        return this.chkNotEligibleStatus;
    }
    
    // Setter method for txtShareFee
    void setTxtShareFee(String txtShareFee){
        this.txtShareFee = txtShareFee;
        setChanged();
    }
    // Getter method for txtShareFee
    String getTxtShareFee(){
        return this.txtShareFee;
    }
    
    // Setter method for txtShareAmt
    void setTxtShareAmt(String txtShareAmt){
        this.txtShareAmt = txtShareAmt;
        setChanged();
    }
    // Getter method for txtShareAmt
    String getTxtShareAmt(){
        return this.txtShareAmt;
    }
    
    // Setter method for txtShareDetShareAcctNo
    void setTxtShareDetShareAcctNo(String txtShareDetShareAcctNo){
        this.txtShareDetShareAcctNo = txtShareDetShareAcctNo;
        setChanged();
    }
    // Getter method for txtShareDetShareAcctNo
    String getTxtShareDetShareAcctNo(){
        return this.txtShareDetShareAcctNo;
    }
    
    // Setter method for txtShareDetShareNoFrom
    public void setTxtShareDetShareNoFrom(String txtShareDetShareNoFrom){
        this.txtShareDetShareNoFrom = txtShareDetShareNoFrom;
        setChanged();
    }
    // Getter method for txtShareDetShareNoFrom
    public String getTxtShareDetShareNoFrom(){
        return this.txtShareDetShareNoFrom;
    }
    
    // Setter method for txtShareDetShareNoTo
    public void setTxtShareDetShareNoTo(String txtShareDetShareNoTo){
        this.txtShareDetShareNoTo = txtShareDetShareNoTo;
        setChanged();
    }
    // Getter method for txtShareDetShareNoTo
    public String getTxtShareDetShareNoTo(){
        return this.txtShareDetShareNoTo;
    }
    
    // Setter method for txtShareDetNoOfShares
    public void setTxtShareDetNoOfShares(String txtShareDetNoOfShares){
        this.txtShareDetNoOfShares = txtShareDetNoOfShares;
        setChanged();
    }
    // Getter method for txtShareDetNoOfShares
    public String getTxtShareDetNoOfShares(){
        return this.txtShareDetNoOfShares;
    }
    
    // Setter method for tdtShareDetIssShareCert
    void setTdtShareDetIssShareCert(String tdtShareDetIssShareCert){
        this.tdtShareDetIssShareCert = tdtShareDetIssShareCert;
        setChanged();
    }
    // Getter method for tdtShareDetIssShareCert
    String getTdtShareDetIssShareCert(){
        return this.tdtShareDetIssShareCert;
    }
    
    /* To get the Action Type */
    public int getActionType(){
        return this.actionType;
    }
    
    /* To set the Action Type */
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    private HashMap _authorizeMap;
    
    public String getLblStatus(){
        return (this.lblStatus);
    }
    public void setLblStatus(String lblStatus){
        this.lblStatus=lblStatus;
        setChanged();
    }
    
    public void setStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }
    
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    
    public int getResult(){
        return result;
    }
    
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    
    /**
     * Getter for property cbmConstitution.
     * @return Value of property cbmConstitution.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmConstitution() {
        return cbmConstitution;
    }
    
    /**
     * Setter for property cbmConstitution.
     * @param cbmConstitution New value of property cbmConstitution.
     */
    public void setCbmConstitution(com.see.truetransact.clientutil.ComboBoxModel cbmConstitution) {
        this.cbmConstitution = cbmConstitution;
        setChanged();
    }
    
    /**
     * Getter for property cbmAcctStatus.
     * @return Value of property cbmAcctStatus.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmAcctStatus() {
        return cbmAcctStatus;
    }
    
    /**
     * Setter for property cbmAcctStatus.
     * @param cbmAcctStatus New value of property cbmAcctStatus.
     */
    public void setCbmAcctStatus(com.see.truetransact.clientutil.ComboBoxModel cbmAcctStatus) {
        this.cbmAcctStatus = cbmAcctStatus;
        setChanged();
    }
    
    /**
     * Getter for property cboConstitution.
     * @return Value of property cboConstitution.
     */
    public java.lang.String getCboConstitution() {
        return cboConstitution;
    }
    
    /**
     * Setter for property cboConstitution.
     * @param cboConstitution New value of property cboConstitution.
     */
    public void setCboConstitution(java.lang.String cboConstitution) {
        this.cboConstitution = cboConstitution;
        setChanged();
    }
    
    /*To set data for all dropdowns*/
    public void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add(CONSTITUTION);
        lookup_keys.add(ACCT_STATUS);
        lookup_keys.add(SHARE_TYPE);
        lookup_keys.add(PROD_TYPE);
        lookup_keys.add("REMITTANCE_PAYMENT.PAY_MODE");
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CONSTITUTION));
        cbmConstitution = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(PROD_TYPE));
        cbmDivProdType = new ComboBoxModel(key,value);
       
        cbmDivProdType.removeKeyAndElement("GL");
        cbmDivProdType.removeKeyAndElement("SA");
        cbmDivProdType.removeKeyAndElement("TD");
        cbmDivProdType.removeKeyAndElement("TL");
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(ACCT_STATUS));
        cbmAcctStatus = new ComboBoxModel(key,value);
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("REMITTANCE_PAYMENT.PAY_MODE"));
        cbmDivPayMode = new ComboBoxModel(key,value);
        
        
        lookUpHash.put(CommonConstants.MAP_NAME,SHARE_TYPE);
        lookUpHash.put(CommonConstants.PARAMFORQUERY,null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmShareType = new ComboBoxModel(key,value);
        HashMap param = new HashMap();
        param.put(CommonConstants.MAP_NAME,"getDrfProductLookUpForShare");
        param.put(CommonConstants.PARAMFORQUERY, null);
        HashMap where = new HashMap();
        where = null;
        HashMap keyValue = (HashMap) ClientUtil.populateLookupData(param).get(CommonConstants.DATA);
        cbmDrfProdId =new ComboBoxModel((ArrayList)keyValue.get(CommonConstants.KEY),(ArrayList)keyValue.get(CommonConstants.VALUE));
        makeNull();
    }
    
    /* To make the class variables null*/
    private void makeNull(){
        key = null;
        value = null;
        lookUpHash = null;
        keyValue = null;
        
    }
    
    /* Splits the keyValue HashMap into key and value arraylists*/
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /**
     * Getter for property lblValCustomerName.
     * @return Value of property lblValCustomerName.
     */
    public java.lang.String getLblValCustomerName() {
        return lblValCustomerName;
    }
    
    /**
     * Setter for property lblValCustomerName.
     * @param lblValCustomerName New value of property lblValCustomerName.
     */
    public void setLblValCustomerName(java.lang.String lblValCustomerName) {
        this.lblValCustomerName = lblValCustomerName;
    }
    
    /**
     * Getter for property lblValDateOfBirth.
     * @return Value of property lblValDateOfBirth.
     */
    public java.lang.String getLblValDateOfBirth() {
        return lblValDateOfBirth;
    }
    
    /**
     * Setter for property lblValDateOfBirth.
     * @param lblValDateOfBirth New value of property lblValDateOfBirth.
     */
    public void setLblValDateOfBirth(java.lang.String lblValDateOfBirth) {
        this.lblValDateOfBirth = lblValDateOfBirth;
    }
    
    /**
     * Getter for property lblValStreet.
     * @return Value of property lblValStreet.
     */
    public java.lang.String getLblValStreet() {
        return lblValStreet;
    }
    
    /**
     * Setter for property lblValStreet.
     * @param lblValStreet New value of property lblValStreet.
     */
    public void setLblValStreet(java.lang.String lblValStreet) {
        this.lblValStreet = lblValStreet;
    }
    
    /**
     * Getter for property lblValArea.
     * @return Value of property lblValArea.
     */
    public java.lang.String getLblValArea() {
        return lblValArea;
    }
    
    /**
     * Setter for property lblValArea.
     * @param lblValArea New value of property lblValArea.
     */
    public void setLblValArea(java.lang.String lblValArea) {
        this.lblValArea = lblValArea;
    }
    
    /**
     * Getter for property lblValCity.
     * @return Value of property lblValCity.
     */
    public java.lang.String getLblValCity() {
        return lblValCity;
    }
    
    /**
     * Setter for property lblValCity.
     * @param lblValCity New value of property lblValCity.
     */
    public void setLblValCity(java.lang.String lblValCity) {
        this.lblValCity = lblValCity;
    }
    
    /**
     * Getter for property lblValState.
     * @return Value of property lblValState.
     */
    public java.lang.String getLblValState() {
        return lblValState;
    }
    
    /**
     * Setter for property lblValState.
     * @param lblValState New value of property lblValState.
     */
    public void setLblValState(java.lang.String lblValState) {
        this.lblValState = lblValState;
    }
    
    /**
     * Getter for property lblValCountry.
     * @return Value of property lblValCountry.
     */
    public java.lang.String getLblValCountry() {
        return lblValCountry;
    }
    
    /**
     * Setter for property lblValCountry.
     * @param lblValCountry New value of property lblValCountry.
     */
    public void setLblValCountry(java.lang.String lblValCountry) {
        this.lblValCountry = lblValCountry;
    }
    
    /**
     * Getter for property lblValPin.
     * @return Value of property lblValPin.
     */
    public java.lang.String getLblValPin() {
        return lblValPin;
    }
    
    /**
     * Setter for property lblValPin.
     * @param lblValPin New value of property lblValPin.
     */
    public void setLblValPin(java.lang.String lblValPin) {
        this.lblValPin = lblValPin;
    }
    
    /**
     * Getter for property tblShareAcctDet.
     * @return Value of property tblShareAcctDet.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblShareAcctDet() {
        return tblShareAcctDet;
    }
    
    /**
     * Setter for property tblShareAcctDet.
     * @param tblShareAcctDet New value of property tblShareAcctDet.
     */
    public void setTblShareAcctDet(com.see.truetransact.clientutil.EnhancedTableModel tblShareAcctDet) {
        this.tblShareAcctDet = tblShareAcctDet;
        setChanged();
    }
    
    /**
     * Getter for property _authorizeMap.
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap getAuthorizeMap() {
        return _authorizeMap;
    }
    
    /**
     * Setter for property _authorizeMap.
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void setAuthorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }
    
    
    /**
     * Getter for property lblValCustName.
     * @return Value of property lblValCustName.
     */
    public java.lang.String getLblValCustName() {
        return lblValCustName;
    }
    
    /**
     * Setter for property lblValCustName.
     * @param lblValCustName New value of property lblValCustName.
     */
    public void setLblValCustName(java.lang.String lblValCustName) {
        this.lblValCustName = lblValCustName;
    }
    
    /**
     * Getter for property cbmCommAddrType.
     * @return Value of property cbmCommAddrType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCommAddrType() {
        return cbmCommAddrType;
    }
    
    /**
     * Setter for property cbmCommAddrType.
     * @param cbmCommAddrType New value of property cbmCommAddrType.
     */
    public void setCbmCommAddrType(com.see.truetransact.clientutil.ComboBoxModel cbmCommAddrType) {
        this.cbmCommAddrType = cbmCommAddrType;
    }
    
    /**
     * Getter for property cboCommAddrType.
     * @return Value of property cboCommAddrType.
     */
    public java.lang.String getCboCommAddrType() {
        return cboCommAddrType;
    }
    
    /**
     * Setter for property cboCommAddrType.
     * @param cboCommAddrType New value of property cboCommAddrType.
     */
    public void setCboCommAddrType(java.lang.String cboCommAddrType) {
        this.cboCommAddrType = cboCommAddrType;
    }
    
    /**
     * Getter for property cboShareType.
     * @return Value of property cboShareType.
     */
    public java.lang.String getCboShareType() {
        return cboShareType;
    }
    
    /**
     * Setter for property cboShareType.
     * @param cboShareType New value of property cboShareType.
     */
    public void setCboShareType(java.lang.String cboShareType) {
        this.cboShareType = cboShareType;
    }
    
    /**
     * Getter for property cbmShareType.
     * @return Value of property cbmShareType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmShareType() {
        return cbmShareType;
    }
    
    /**
     * Setter for property cbmShareType.
     * @param cbmShareType New value of property cbmShareType.
     */
    public void setCbmShareType(com.see.truetransact.clientutil.ComboBoxModel cbmShareType) {
        this.cbmShareType = cbmShareType;
    }
    
    /**
     * Getter for property txtRemarks.
     * @return Value of property txtRemarks.
     */
    public java.lang.String getTxtRemarks() {
        return txtRemarks;
    }
    
    /**
     * Setter for property txtRemarks.
     * @param txtRemarks New value of property txtRemarks.
     */
    public void setTxtRemarks(java.lang.String txtRemarks) {
        this.txtRemarks = txtRemarks;
    }
    
    // To show alert message
    public int showAlertWindow(String amtLimit) {
        int option = 1;
        try{
            String[] options = {objShareRB.getString("cDialogOK")};
            option = COptionPane.showOptionDialog(null,amtLimit, CommonConstants.WARNINGTITLE,
            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        }catch (Exception e){
            parseException.logException(e,true);
        }
        return option;
    }
    
    /**
     * Setter for property transactionOB.
     * @param transactionOB New value of property transactionOB.
     */
    public void setTransactionOB(TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }
    
    /**
     * Getter for property allowedTransactionDetailsTO.
     * @return Value of property allowedTransactionDetailsTO.
     */
    public java.util.LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }
    
    /**
     * Setter for property allowedTransactionDetailsTO.
     * @param allowedTransactionDetailsTO New value of property allowedTransactionDetailsTO.
     */
    public void setAllowedTransactionDetailsTO(java.util.LinkedHashMap allowedTransactionDetailsTO) {
        // System.out.println("In OB of RemIssue : " + allowedTransactionDetailsTO);
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }
    
    /**
     * Getter for property txtResolutionNo1.
     * @return Value of property txtResolutionNo1.
     */
    public java.lang.String getTxtResolutionNo1() {
        return txtResolutionNo1;
    }
    
    /**
     * Setter for property txtResolutionNo1.
     * @param txtResolutionNo1 New value of property txtResolutionNo1.
     */
    public void setTxtResolutionNo1(java.lang.String txtResolutionNo1) {
        this.txtResolutionNo1 = txtResolutionNo1;
    }
    
    /**
     * Getter for property authorize.
     * @return Value of property authorize.
     */
    public java.lang.String getAuthorize() {
        return authorize;
    }
    
    /**
     * Setter for property authorize.
     * @param authorize New value of property authorize.
     */
    public void setAuthorize(java.lang.String authorize) {
        this.authorize = authorize;
    }
    public void setTrans(List list) {
        this.list=list;
    }
    public List getTrans() {
        return list;
    }
    
    
    /**
     * Getter for property tblShareLoanDet.
     * @return Value of property tblShareLoanDet.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblShareLoanDet() {
        return tblShareLoanDet;
    }
    
    /**
     * Setter for property tblShareLoanDet.
     * @param tblShareLoanDet New value of property tblShareLoanDet.
     */
    public void setTblShareLoanDet(com.see.truetransact.clientutil.EnhancedTableModel tblShareLoanDet) {
        this.tblShareLoanDet = tblShareLoanDet;
    }
    
    /**
     * Getter for property cbmDivProdType.
     * @return Value of property cbmDivProdType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDivProdType() {
        return cbmDivProdType;
    }
    
    /**
     * Setter for property cbmDivProdType.
     * @param cbmDivProdType New value of property cbmDivProdType.
     */
    public void setCbmDivProdType(com.see.truetransact.clientutil.ComboBoxModel cbmDivProdType) {
        this.cbmDivProdType = cbmDivProdType;
    }
    
    /**
     * Getter for property cboDivProdType.
     * @return Value of property cboDivProdType.
     */
    public java.lang.String getCboDivProdType() {
        return cboDivProdType;
    }
    
    /**
     * Setter for property cboDivProdType.
     * @param cboDivProdType New value of property cboDivProdType.
     */
    public void setCboDivProdType(java.lang.String cboDivProdType) {
        this.cboDivProdType = cboDivProdType;
    }
    
    /**
     * Getter for property cbmDivProdID.
     * @return Value of property cbmDivProdID.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDivProdID() {
        return cbmDivProdID;
    }
    
    /**
     * Setter for property cbmDivProdID.
     * @param cbmDivProdID New value of property cbmDivProdID.
     */
    public void setCbmDivProdID(com.see.truetransact.clientutil.ComboBoxModel cbmDivProdID) {
        this.cbmDivProdID = cbmDivProdID;
    }
    
    /**
     * Getter for property cboDivProdId.
     * @return Value of property cboDivProdId.
     */
    public java.lang.String getCboDivProdId() {
        return cboDivProdId;
    }
    
    /**
     * Setter for property cboDivProdId.
     * @param cboDivProdId New value of property cboDivProdId.
     */
    public void setCboDivProdId(java.lang.String cboDivProdId) {
        this.cboDivProdId = cboDivProdId;
    }
    
    public void callProdIdsForProductType(String prodType){
        try {
            if(!prodType.equals("")){
                
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + prodType);
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
                cbmDivProdID= new ComboBoxModel(key,value);
            }else{
                key = new ArrayList();
                value = new ArrayList();
                key.add("");
                value.add("");
                cbmDivProdID= new ComboBoxModel(key,value);
            }
            
        } catch(Exception e){
            System.out.println("Error in cboProductIdActionPerformed()");
        }
        
    }
    
    /**
     * Getter for property txtDivAcNo.
     * @return Value of property txtDivAcNo.
     */
    public java.lang.String getTxtDivAcNo() {
        return txtDivAcNo;
    }
    
    /**
     * Setter for property txtDivAcNo.
     * @param txtDivAcNo New value of property txtDivAcNo.
     */
    public void setTxtDivAcNo(java.lang.String txtDivAcNo) {
        this.txtDivAcNo = txtDivAcNo;
    }
    
    /**
     * Getter for property cbmDivPayMode.
     * @return Value of property cbmDivPayMode.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDivPayMode() {
        return cbmDivPayMode;
    }
    
    /**
     * Setter for property cbmDivPayMode.
     * @param cbmDivPayMode New value of property cbmDivPayMode.
     */
    public void setCbmDivPayMode(com.see.truetransact.clientutil.ComboBoxModel cbmDivPayMode) {
        this.cbmDivPayMode = cbmDivPayMode;
    }
    
    /**
     * Getter for property cboDivPayMode.
     * @return Value of property cboDivPayMode.
     */
    public java.lang.String getCboDivPayMode() {
        return cboDivPayMode;
    }
    
    /**
     * Setter for property cboDivPayMode.
     * @param cboDivPayMode New value of property cboDivPayMode.
     */
    public void setCboDivPayMode(java.lang.String cboDivPayMode) {
        this.cboDivPayMode = cboDivPayMode;
    }
    
    /**
     * Getter for property txtShareValue.
     * @return Value of property txtShareValue.
     */
    public java.lang.String getTxtShareValue() {
        return txtShareValue;
    }
    
    /**
     * Setter for property txtShareValue.
     * @param txtShareValue New value of property txtShareValue.
     */
    public void setTxtShareValue(java.lang.String txtShareValue) {
        this.txtShareValue = txtShareValue;
    }
    
    /**
     * Getter for property txtTotShareFee.
     * @return Value of property txtTotShareFee.
     */
    public java.lang.String getTxtTotShareFee() {
        return txtTotShareFee;
    }
    
    /**
     * Setter for property txtTotShareFee.
     * @param txtTotShareFee New value of property txtTotShareFee.
     */
    public void setTxtTotShareFee(java.lang.String txtTotShareFee) {
        this.txtTotShareFee = txtTotShareFee;
    }
    
    /**
     * Getter for property txtShareApplFee.
     * @return Value of property txtShareApplFee.
     */
    public java.lang.String getTxtShareApplFee() {
        return txtShareApplFee;
    }
    
    /**
     * Setter for property txtShareApplFee.
     * @param txtShareApplFee New value of property txtShareApplFee.
     */
    public void setTxtShareApplFee(java.lang.String txtShareApplFee) {
        this.txtShareApplFee = txtShareApplFee;
    }
    
    /**
     * Getter for property txtShareMemFee.
     * @return Value of property txtShareMemFee.
     */
    public java.lang.String getTxtShareMemFee() {
        return txtShareMemFee;
    }
    
    /**
     * Setter for property txtShareMemFee.
     * @param txtShareMemFee New value of property txtShareMemFee.
     */
    public void setTxtShareMemFee(java.lang.String txtShareMemFee) {
        this.txtShareMemFee = txtShareMemFee;
    }
    
    /**
     * Getter for property txtShareTotAmount.
     * @return Value of property txtShareTotAmount.
     */
    public java.lang.String getTxtShareTotAmount() {
        return txtShareTotAmount;
    }
    public void setDeletion(String deletion){
        this.deletion=deletion;
    }
    public String getDeletion(){
        return deletion;
    }
    /**
     * Setter for property txtShareTotAmount.
     * @param txtShareTotAmount New value of property txtShareTotAmount.
     */
    public void setTxtShareTotAmount(java.lang.String txtShareTotAmount) {
        this.txtShareTotAmount = txtShareTotAmount;
    }
    
    /**
     * Getter for property txtNoShares.
     * @return Value of property txtNoShares.
     */
    public java.lang.String getTxtNoShares() {
        return txtNoShares;
    }
    
    /**
     * Setter for property txtNoShares.
     * @param txtNoShares New value of property txtNoShares.
     */
    public void setTxtNoShares(java.lang.String txtNoShares) {
        this.txtNoShares = txtNoShares;
    }
    
    public boolean checkAcNoWithoutProdType(String actNum ,String cust_id,boolean setBranchOnly) {
        HashMap mapData=new HashMap();
        boolean isExists = false;
        List mapDataList=null;
        try{//dont delete chck selectalldao
            if(actNum!=null)
                mapData.put("ACT_NUM", actNum);
            if(cust_id !=null)
                mapData.put("CUST_ID", cust_id);
            if(setBranchOnly)
                mapDataList = ClientUtil.executeQuery("getShareAccBranchData", mapData);//getActNumFromAllProducts
            else
                mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData);
            System.out.println("#### mapDataList :"+mapDataList);
            if (mapDataList!=null && mapDataList.size()>0) {
                mapData=(HashMap)mapDataList.get(0);
                if(setBranchOnly){
                    setSelectedBranchID(CommonUtil.convertObjToStr(mapData.get("BRANCH_ID")));
                    return true;
                }else{
                    setTxtDivAcNo(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                }
                cbmDivProdType.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                setCboDivProdType(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                setCbmDivProdID(getCboDivProdType());
                setTxtCustId(CommonUtil.convertObjToStr(mapData.get("CUST_ID")));
                //                getProducts();
                cbmDivProdID.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                isExists = true;
            } else {
                //                ArrayList key=new ArrayList();
                //                ArrayList value=new ArrayList();
                //                key.add("");
                //                value.add("");
                setCbmDivProdID("");
                isExists = false;
                //                key = null;
                //                value = null;
                isExists = false;
            }
            mapDataList = null;
        }catch(Exception e){
            e.printStackTrace();
        }
        mapData = null;
        return isExists;
    }
    
    public void setCbmDivProdID(String prodType) {
        if (CommonUtil.convertObjToStr(prodType).length()>1) {
            if (prodType.equals("GL")) {
                key = new ArrayList();
                value = new ArrayList();
            } else {
                try {
                    lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + prodType);
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
        cbmDivProdID = new ComboBoxModel(key,value);
        this.cbmDivProdID = cbmDivProdID;
        setChanged();
    }
    
    /**
     * Getter for property lblBalanceAmt.
     * @return Value of property lblBalanceAmt.
     */
    public java.lang.String getLblBalanceAmt() {
        return lblBalanceAmt;
    }
    
    /**
     * Setter for property lblBalanceAmt.
     * @param lblBalanceAmt New value of property lblBalanceAmt.
     */
    public void setLblBalanceAmt(java.lang.String lblBalanceAmt) {
        this.lblBalanceAmt = lblBalanceAmt;
    }
    
    /**
     * Getter for property lblDivAmt.
     * @return Value of property lblDivAmt.
     */
    public java.lang.String getLblDivAmt() {
        return lblDivAmt;
    }
    
    /**
     * Setter for property lblDivAmt.
     * @param lblDivAmt New value of property lblDivAmt.
     */
    public void setLblDivAmt(java.lang.String lblDivAmt) {
        this.lblDivAmt = lblDivAmt;
    }
    
    /**
     * Getter for property listShareAcct.
     * @return Value of property listShareAcct.
     */
    public java.util.List getListShareAcct() {
        return listShareAcct;
    }
    
    /**
     * Setter for property listShareAcct.
     * @param listShareAcct New value of property listShareAcct.
     */
    public void setListShareAcct(java.util.List listShareAcct) {
        this.listShareAcct = listShareAcct;
    }
    
    /**
     * Getter for property curDate.
     * @return Value of property curDate.
     */
    public java.util.Date getCurDate() {
        return curDate;
    }
    
    /**
     * Setter for property curDate.
     * @param curDate New value of property curDate.
     */
    public void setCurDate(java.util.Date curDate) {
        this.curDate = curDate;
    }
    
    /**
     * Getter for property admissionFeeMap.
     * @return Value of property admissionFeeMap.
     */
    public java.util.HashMap getAdmissionFeeMap() {
        return admissionFeeMap;
    }
    
    /**
     * Setter for property admissionFeeMap.
     * @param admissionFeeMap New value of property admissionFeeMap.
     */
    public void setAdmissionFeeMap(java.util.HashMap admissionFeeMap) {
        this.admissionFeeMap = admissionFeeMap;
    }
    
    /**
     * Getter for property txtIDCardNo.
     * @return Value of property txtIDCardNo.
     */
    public java.lang.String getTxtIDCardNo() {
        return txtIDCardNo;
    }
    
    /**
     * Setter for property txtIDCardNo.
     * @param txtIDCardNo New value of property txtIDCardNo.
     */
    public void setTxtIDCardNo(java.lang.String txtIDCardNo) {
        this.txtIDCardNo = txtIDCardNo;
    }
    
    public java.lang.String getTxtImbp() {
        return txtImbp;
    }
    
    
    public void setTxtImbp(java.lang.String txtImbp) {
        this.txtImbp = txtImbp;
    }
    
    public java.lang.String getTxtEmpRefNoNew() {
        return txtEmpRefNoNew;
    }
    
    
    public void setTxtEmpRefNoNew(java.lang.String txtEmpRefNoNew) {
        this.txtEmpRefNoNew = txtEmpRefNoNew;
    }
    
    public java.lang.String getTxtEmpRefNoOld() {
        return txtEmpRefNoOld;
    }
    
    
    public void setTxtEmpRefNoOld(java.lang.String txtEmpRefNoOld) {
        this.txtEmpRefNoOld = txtEmpRefNoOld;
    }
    public java.lang.String getTxtRatification() {
        return txtRatification;
    }
    
    /**
     * Setter for property txtIDCardNo.
     * @param txtIDCardNo New value of property txtIDCardNo.
     */
    public void setTxtRatification(java.lang.String txtRatification) {
        this.txtRatification = txtRatification;
    }
    
    public void setNewMode(String newMode){
        this.newMode=newMode;
    }
    public String getNewMode(){
        return newMode;
    }
    
    /**
     * Getter for property caste.
     * @return Value of property caste.
     */
    public java.lang.String getCaste() {
        return caste;
    }
    
    /**
     * Setter for property caste.
     * @param caste New value of property caste.
     */
    public void setCaste(java.lang.String caste) {
        this.caste = caste;
    }
    
    /**
     * Getter for property customersShare.
     * @return Value of property customersShare.
     */
    public java.lang.String getCustomersShare() {
        return customersShare;
    }
    
    /**
     * Setter for property customersShare.
     * @param customersShare New value of property customersShare.
     */
    public void setCustomersShare(java.lang.String customersShare) {
        this.customersShare = customersShare;
    }
    
    /**
     * Getter for property govtsShare.
     * @return Value of property govtsShare.
     */
    public java.lang.String getGovtsShare() {
        return govtsShare;
    }
    
    /**
     * Setter for property govtsShare.
     * @param govtsShare New value of property govtsShare.
     */
    public void setGovtsShare(java.lang.String govtsShare) {
        this.govtsShare = govtsShare;
    }
    
    /**
     * Getter for property txtIDResolutionNo.
     * @return Value of property txtIDResolutionNo.
     */
    public java.lang.String getTxtIDResolutionNo() {
        return txtIDResolutionNo;
    }
    
    /**
     * Setter for property txtIDResolutionNo.
     * @param txtIDResolutionNo New value of property txtIDResolutionNo.
     */
    public void setTxtIDResolutionNo(java.lang.String txtIDResolutionNo) {
        this.txtIDResolutionNo = txtIDResolutionNo;
    }
    
    /**
     * Getter for property tdtIDIssuedDt.
     * @return Value of property tdtIDIssuedDt.
     */
    public java.lang.String getTdtIDIssuedDt() {
        return tdtIDIssuedDt;
    }
    
    /**
     * Setter for property tdtIDIssuedDt.
     * @param tdtIDIssuedDt New value of property tdtIDIssuedDt.
     */
    public void setTdtIDIssuedDt(java.lang.String tdtIDIssuedDt) {
        this.tdtIDIssuedDt = tdtIDIssuedDt;
    }
    
    /**
     * Getter for property tdtIDResolutionDt.
     * @return Value of property tdtIDResolutionDt.
     */
    public java.lang.String getTdtIDResolutionDt() {
        return tdtIDResolutionDt;
    }
    
    /**
     * Setter for property tdtIDResolutionDt.
     * @param tdtIDResolutionDt New value of property tdtIDResolutionDt.
     */
    public void setTdtIDResolutionDt(java.lang.String tdtIDResolutionDt) {
        this.tdtIDResolutionDt = tdtIDResolutionDt;
    }
    
    /**
     * Getter for property chkDuplicateIDCardYN.
     * @return Value of property chkDuplicateIDCardYN.
     */
    public java.lang.String getChkDuplicateIDCardYN() {
        return chkDuplicateIDCardYN;
    }
    
    /**
     * Setter for property chkDuplicateIDCardYN.
     * @param chkDuplicateIDCardYN New value of property chkDuplicateIDCardYN.
     */
    public void setChkDuplicateIDCardYN(java.lang.String chkDuplicateIDCardYN) {
        this.chkDuplicateIDCardYN = chkDuplicateIDCardYN;
    }
    
    /**
     * Getter for property chkDrfApplicableYN.
     * @return Value of property chkDrfApplicableYN.
     */
    public java.lang.String getChkDrfApplicableYN() {
        return chkDrfApplicableYN;
    }
    
    /**
     * Setter for property chkDrfApplicableYN.
     * @param chkDrfApplicableYN New value of property chkDrfApplicableYN.
     */
    public void setChkDrfApplicableYN(java.lang.String chkDrfApplicableYN) {
        this.chkDrfApplicableYN = chkDrfApplicableYN;
    }
    
    /**
     * Getter for property drfApplicableMap.
     * @return Value of property drfApplicableMap.
     */
    public java.util.HashMap getDrfApplicableMap() {
        return drfApplicableMap;
    }
    
    /**
     * Setter for property drfApplicableMap.
     * @param drfApplicableMap New value of property drfApplicableMap.
     */
    public void setDrfApplicableMap(java.util.HashMap drfApplicableMap) {
        this.drfApplicableMap = drfApplicableMap;
    }
    
    /**
     * Getter for property drfApplicableFlag.
     * @return Value of property drfApplicableFlag.
     */
    public boolean isDrfApplicableFlag() {
        return drfApplicableFlag;
    }
    
    /**
     * Setter for property drfApplicableFlag.
     * @param drfApplicableFlag New value of property drfApplicableFlag.
     */
    public void setDrfApplicableFlag(boolean drfApplicableFlag) {
        this.drfApplicableFlag = drfApplicableFlag;
    }
    
    /**
     * Getter for property cboDrfProdId.
     * @return Value of property cboDrfProdId.
     */
    public java.lang.String getCboDrfProdId() {
        return cboDrfProdId;
    }
    
    /**
     * Setter for property cboDrfProdId.
     * @param cboDrfProdId New value of property cboDrfProdId.
     */
    public void setCboDrfProdId(java.lang.String cboDrfProdId) {
        this.cboDrfProdId = cboDrfProdId;
    }
    
    /**
     * Getter for property cbmDrfProdId.
     * @return Value of property cbmDrfProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDrfProdId() {
        return cbmDrfProdId;
    }
    
    /**
     * Setter for property cbmDrfProdId.
     * @param cbmDrfProdId New value of property cbmDrfProdId.
     */
    public void setCbmDrfProdId(com.see.truetransact.clientutil.ComboBoxModel cbmDrfProdId) {
        this.cbmDrfProdId = cbmDrfProdId;
    }
    
    /**
     * Getter for property productPaymentAmount.
     * @return Value of property productPaymentAmount.
     */
    public java.lang.String getProductPaymentAmount() {
        return productPaymentAmount;
    }
    
    /**
     * Setter for property productPaymentAmount.
     * @param productPaymentAmount New value of property productPaymentAmount.
     */
    public void setProductPaymentAmount(java.lang.String productPaymentAmount) {
        this.productPaymentAmount = productPaymentAmount;
    }
    
    /**
     * Getter for property productAmount.
     * @return Value of property productAmount.
     */
    public java.lang.String getProductAmount() {
        return productAmount;
    }
    
    /**
     * Setter for property productAmount.
     * @param productAmount New value of property productAmount.
     */
    public void setProductAmount(java.lang.String productAmount) {
        this.productAmount = productAmount;
    }
    
    /**
     * Getter for property drfStatus.
     * @return Value of property drfStatus.
     */
    public java.lang.String getDrfStatus() {
        return drfStatus;
    }
    
    /**
     * Setter for property drfStatus.
     * @param drfStatus New value of property drfStatus.
     */
    public void setDrfStatus(java.lang.String drfStatus) {
        this.drfStatus = drfStatus;
    }
    
    /**
     * Getter for property DueAmt.
     * @return Value of property DueAmt.
     */
    public java.lang.String getDueAmt() {
        return DueAmt;
    }
    
    /**
     * Setter for property DueAmt.
     * @param DueAmt New value of property DueAmt.
     */
    public void setDueAmt(java.lang.String DueAmt) {
        this.DueAmt = DueAmt;
    }
    
    /**
     * Getter for property shareStatus.
     * @return Value of property shareStatus.
     */
    public java.lang.String getShareStatus() {
        return shareStatus;
    }
    
    /**
     * Setter for property shareStatus.
     * @param shareStatus New value of property shareStatus.
     */
    public void setShareStatus(java.lang.String shareStatus) {
        this.shareStatus = shareStatus;
    }
    
    /**
     * Getter for property shareAcctStatusDueAmt.
     * @return Value of property shareAcctStatusDueAmt.
     */
    public java.lang.String getShareAcctStatusDueAmt() {
        return shareAcctStatusDueAmt;
    }
    
    /**
     * Setter for property shareAcctStatusDueAmt.
     * @param shareAcctStatusDueAmt New value of property shareAcctStatusDueAmt.
     */
    public void setShareAcctStatusDueAmt(java.lang.String shareAcctStatusDueAmt) {
        this.shareAcctStatusDueAmt = shareAcctStatusDueAmt;
    }
    
    /**
     * Getter for property shareAcctStatusAddWithdra.
     * @return Value of property shareAcctStatusAddWithdra.
     */
    public java.lang.String getShareAcctStatusAddWithdra() {
        return shareAcctStatusAddWithdra;
    }
    
    /**
     * Setter for property shareAcctStatusAddWithdra.
     * @param shareAcctStatusAddWithdra New value of property shareAcctStatusAddWithdra.
     */
    public void setShareAcctStatusAddWithdra(java.lang.String shareAcctStatusAddWithdra) {
        this.shareAcctStatusAddWithdra = shareAcctStatusAddWithdra;
    }

    public String getLblServiceTaxval() {
        return lblServiceTaxval;
    }

    public void setLblServiceTaxval(String lblServiceTaxval) {
        this.lblServiceTaxval = lblServiceTaxval;
    }

    public HashMap getServiceTax_Map() {
        return serviceTax_Map;
    }

    public void setServiceTax_Map(HashMap serviceTax_Map) {
        this.serviceTax_Map = serviceTax_Map;
    }
    public ServiceTaxDetailsTO setServiceTaxDetails() {
        final ServiceTaxDetailsTO objservicetaxDetTo = new ServiceTaxDetailsTO();
        try {

            objservicetaxDetTo.setCommand(getCommand());
            objservicetaxDetTo.setStatus(CommonConstants.STATUS_CREATED);
            objservicetaxDetTo.setStatusBy(TrueTransactMain.USER_ID);
            objservicetaxDetTo.setAcct_Num(getTxtShareAcctNo());
            objservicetaxDetTo.setParticulars("Share");
            objservicetaxDetTo.setBranchID(ProxyParameters.BRANCH_ID);
            objservicetaxDetTo.setTrans_type("C");
            if (serviceTax_Map != null && serviceTax_Map.containsKey("SERVICE_TAX")) {
                objservicetaxDetTo.setServiceTaxAmt(CommonUtil.convertObjToDouble(serviceTax_Map.get("SERVICE_TAX")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("EDUCATION_CESS")) {
                objservicetaxDetTo.setEducationCess(CommonUtil.convertObjToDouble(serviceTax_Map.get("EDUCATION_CESS")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("HIGHER_EDU_CESS")) {
                objservicetaxDetTo.setHigherCess(CommonUtil.convertObjToDouble(serviceTax_Map.get("HIGHER_EDU_CESS")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("TOT_TAX_AMT")) {
                objservicetaxDetTo.setTotalTaxAmt(CommonUtil.convertObjToDouble(serviceTax_Map.get("TOT_TAX_AMT")));
            }
             if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.SWACHH_CESS)) {
                objservicetaxDetTo.setSwachhCess(CommonUtil.convertObjToDouble(serviceTax_Map.get(ServiceTaxCalculation.SWACHH_CESS)));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS)) {
                objservicetaxDetTo.setKrishiKalyan(CommonUtil.convertObjToDouble(serviceTax_Map.get(ServiceTaxCalculation.KRISHIKALYAN_CESS)));
            }
            double roudVal = objservicetaxDetTo.getTotalTaxAmt() - (objservicetaxDetTo.getServiceTaxAmt() + objservicetaxDetTo.getEducationCess() + objservicetaxDetTo.getHigherCess()+objservicetaxDetTo.getSwachhCess() + objservicetaxDetTo.getKrishiKalyan());

            objservicetaxDetTo.setRoundVal(CommonUtil.convertObjToStr(roudVal));
            objservicetaxDetTo.setStatusDt(curDate);

            if (getCommand().equalsIgnoreCase("INSERT")) {
                objservicetaxDetTo.setCreatedBy(TrueTransactMain.USER_ID);
                objservicetaxDetTo.setCreatedDt(curDate);
            }
     
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objservicetaxDetTo;

    }
     // Code commented by nithya for service tax to gst changes
    
//    public double calcServiceTaxAmount(String accNum, String prodId) throws Exception {
//        HashMap whereMap = new HashMap();
//        whereMap.put("ACT_NUM", accNum);
//        List chargeAmtList = ClientUtil.executeQuery("getChargeDetails", whereMap);
//        double taxAmt = 0;
//        if (chargeAmtList != null && chargeAmtList.size() > 0) {
//            String checkFlag = "N";
//            whereMap = new HashMap();
//            whereMap.put("value", prodId);
//            List accHeadList = ClientUtil.executeQuery("getSelectLoanProductAccHeadTO", whereMap);
//            if (accHeadList != null && accHeadList.size() > 0) {
//                for (int i = 0; i < chargeAmtList.size(); i++) {
//                    HashMap chargeMap = (HashMap) chargeAmtList.get(i);
//                    if (chargeMap != null && chargeMap.size() > 0) {
//
//                        String accId = "";
//
//                        LoanProductAccHeadTO accHeadObj = (LoanProductAccHeadTO) accHeadList.get(0);
//                        String chargetype = "";
//                        if (chargeMap.containsKey("CHARGE_TYPE")) {
//                            chargetype = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_TYPE"));
//                        }
//                        if (chargetype != null && chargetype.equals("EP CHARGE")) {
//                            accId = CommonUtil.convertObjToStr(accHeadObj.getEpCost());
//                        }
//                        if (chargetype != null && chargetype.equals("ARC CHARGE")) {
//                            accId = CommonUtil.convertObjToStr(accHeadObj.getArcCost());
//                        }
//                        if (chargetype != null && chargetype.equals("MISCELLANEOUS CHARGES")) {
//                            accId = CommonUtil.convertObjToStr(accHeadObj.getMiscServChrg());
//                        }
//                        if (chargetype != null && chargetype.equals("POSTAGE CHARGES")) {
//                            accId = CommonUtil.convertObjToStr(accHeadObj.getPostageCharges());
//                        }
//                        if (chargetype != null && chargetype.equals("ADVERTISE CHARGES")) {
//                            accId = CommonUtil.convertObjToStr(accHeadObj.getAdvertisementHead());
//                        }
//                        if (chargetype != null && chargetype.equals("ARBITRARY CHARGES")) {
//                            accId = CommonUtil.convertObjToStr(accHeadObj.getArbitraryCharges());
//                        }
//                        if (chargetype != null && chargetype.equals("LEGAL CHARGES")) {
//                            accId = CommonUtil.convertObjToStr(accHeadObj.getLegalCharges());
//                        }
//                        if (chargetype != null && chargetype.equals("NOTICE CHARGES")) {
//                            accId = CommonUtil.convertObjToStr(accHeadObj.getNoticeCharges());
//                        }
//                        if (chargetype != null && chargetype.equals("INSURANCE CHARGES")) {
//                            accId = CommonUtil.convertObjToStr(accHeadObj.getInsuranceCharges());
//                        }
//                        if (chargetype != null && chargetype.equals("EXECUTION DECREE CHARGES")) {
//                            accId = CommonUtil.convertObjToStr(accHeadObj.getExecutionDecreeCharges());
//                        }
//                        checkFlag = checkServiceTaxApplicable(accId);
//                        if (checkFlag != null && checkFlag.equals("Y")) {
//                            String charge_amt = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_AMT"));
//                            taxAmt = taxAmt + CommonUtil.convertObjToDouble(charge_amt);
//                        }
//                    }
//
//                }
//            }
//        }
//        return taxAmt;
//    }
    
    public List calcServiceTaxAmount(String accNum, String prodId) throws Exception {
        HashMap whereMap = new HashMap();
        whereMap.put("ACT_NUM", accNum);
        List chargeAmtList = ClientUtil.executeQuery("getChargeDetails", whereMap);
        double taxAmt = 0;
        HashMap taxMap;
        List taxSettingsList = new ArrayList();
        if (chargeAmtList != null && chargeAmtList.size() > 0) {
            String checkFlag = "N";
            whereMap = new HashMap();
            HashMap checkForTaxMap = new HashMap();
            whereMap.put("value", prodId);
            List accHeadList = ClientUtil.executeQuery("getSelectLoanProductAccHeadTO", whereMap);
            if (accHeadList != null && accHeadList.size() > 0) {
                for (int i = 0; i < chargeAmtList.size(); i++) {
                    HashMap chargeMap = (HashMap) chargeAmtList.get(i);
                    if (chargeMap != null && chargeMap.size() > 0) {

                        String accId = "";

                        LoanProductAccHeadTO accHeadObj = (LoanProductAccHeadTO) accHeadList.get(0);
                        String chargetype = "";
                        if (chargeMap.containsKey("CHARGE_TYPE")) {
                            chargetype = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_TYPE"));
                        }
                        if (chargetype != null && chargetype.equals("EP CHARGE")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getEpCost());
                        }
                        if (chargetype != null && chargetype.equals("ARC CHARGE")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getArcCost());
                        }
                        if (chargetype != null && chargetype.equals("MISCELLANEOUS CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getMiscServChrg());
                        }
                        if (chargetype != null && chargetype.equals("POSTAGE CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getPostageCharges());
                        }
                        if (chargetype != null && chargetype.equals("ADVERTISE CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getAdvertisementHead());
                        }
                        if (chargetype != null && chargetype.equals("ARBITRARY CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getArbitraryCharges());
                        }
                        if (chargetype != null && chargetype.equals("LEGAL CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getLegalCharges());
                        }
                        if (chargetype != null && chargetype.equals("NOTICE CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getNoticeCharges());
                        }
                        if (chargetype != null && chargetype.equals("INSURANCE CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getInsuranceCharges());
                        }
                        if (chargetype != null && chargetype.equals("EXECUTION DECREE CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getExecutionDecreeCharges());
                        }
                        if (chargetype != null && chargetype.equals("OTHER CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getOthrChrgsHead());
                        }
                        checkForTaxMap = checkServiceTaxApplicable(accId);
                        if(checkForTaxMap.containsKey("SERVICE_TAX_APPLICABLE") && checkForTaxMap.get("SERVICE_TAX_APPLICABLE") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_APPLICABLE")).equalsIgnoreCase("Y")){
                            if(checkForTaxMap.containsKey("SERVICE_TAX_ID") && checkForTaxMap.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_ID")).length() > 0){
                                if(CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")) > 0){
                                   taxMap = new HashMap();
                                   taxMap.put("SETTINGS_ID",checkForTaxMap.get("SERVICE_TAX_ID"));
                                   taxMap.put(ServiceTaxCalculation.TOT_AMOUNT,chargeMap.get("CHARGE_AMT"));
                                   taxSettingsList.add(taxMap);   
                                }                                     
                            }
                        }
                    }

                }
            }
        }
        return taxSettingsList;
    }
    
    
    public HashMap checkServiceTaxApplicable(String accheadId) {
        String checkFlag = "N";
        HashMap checkForTaxMap = new HashMap();
        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
            HashMap whereMap = new HashMap();
            whereMap.put("AC_HD_ID", accheadId);
            List accHeadList = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);
            //System.out.println("accHeadList :::" + accHeadList);
            if (accHeadList != null && accHeadList.size() > 0) {
                HashMap accHeadMap = (HashMap) accHeadList.get(0);
                if (accHeadMap != null && accHeadMap.containsKey("SERVICE_TAX_APPLICABLE")&& accHeadMap.containsKey("SERVICE_TAX_ID")) {
                    checkFlag = CommonUtil.convertObjToStr(accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_APPLICABLE",accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_ID",accHeadMap.get("SERVICE_TAX_ID"));
                }
            }
        }
        return checkForTaxMap;
    }
        
    //End of GST changes
    
//      public String checkServiceTaxApplicable(String accheadId) throws Exception {
//        String checkFlag = "N";
//        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
//            HashMap whereMap = new HashMap();
//            whereMap.put("AC_HD_ID", accheadId);
//            List accHeadList = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);
//            System.out.println("accHeadList :::" + accHeadList);
//            if (accHeadList != null && accHeadList.size() > 0) {
//                HashMap accHeadMap = (HashMap) accHeadList.get(0);
//                if (accHeadMap != null && accHeadMap.containsKey("SERVICE_TAX_APPLICABLE")) {
//                    checkFlag = CommonUtil.convertObjToStr(accHeadMap.get("SERVICE_TAX_APPLICABLE"));
//                }
//            }
//        }
//        return checkFlag;
//    }
public String getFullClosureReq() {
        return fullClosureReq;
    }
 public void setFullClosureReq(String fullClosureReq) {
        this.fullClosureReq = fullClosureReq;
}
 
    private void setSMSSubscriptionTO(SMSSubscriptionTO objSMSSubscriptionTO) {
        setTxtMobileNo(CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()));
        setTdtMobileSubscribedFrom(DateUtil.getStringDate(objSMSSubscriptionTO.getSubscriptionDt()));
        setIsMobileBanking(true);
    }
    
    private void resetMobileBankingDetails(){
        setTxtMobileNo("");
        setTdtMobileSubscribedFrom("");         
        setIsMobileBanking(false);
    }
    
    /**
     * Getter for property txtMobileNo.
     *
     * @return Value of property txtMobileNo.
     */
    public java.lang.String getTxtMobileNo() {
        return txtMobileNo;
    }

    /**
     * Setter for property txtMobileNo.
     *
     * @param txtMobileNo New value of property txtMobileNo.
     */
    public void setTxtMobileNo(java.lang.String txtMobileNo) {
        this.txtMobileNo = txtMobileNo;
    }
    
    public long getMobileNo(String custId) {
        long mobileNo = 0;
        HashMap mobileMap = new HashMap();
        mobileMap.put("CUST_ID",custId);
        mobileMap.put("BRANCH_CODE",ProxyParameters.BRANCH_ID);
        List list = ClientUtil.executeQuery("getSMSContactForDepositMaturedCustomer", mobileMap);
        if (list != null && list.size() > 0) {
            mobileMap = (HashMap)list.get(0);
            mobileNo = CommonUtil.convertObjToLong(mobileMap.get("CONTACT_NO"));
        }
        return mobileNo;
    }
    
    /**
     * Getter for property tdtMobileSubscribedFrom.
     *
     * @return Value of property tdtMobileSubscribedFrom.
     */
    public java.lang.String getTdtMobileSubscribedFrom() {
        return tdtMobileSubscribedFrom;
    }

    /**
     * Setter for property tdtMobileSubscribedFrom.
     *
     * @param tdtMobileSubscribedFrom New value of property
     * tdtMobileSubscribedFrom.
     */
    public void setTdtMobileSubscribedFrom(java.lang.String tdtMobileSubscribedFrom) {
        this.tdtMobileSubscribedFrom = tdtMobileSubscribedFrom;
    }
    
    private SMSSubscriptionTO setSMSDetails() {
        if (isMobileBanking) {
            if (objSMSSubscriptionTO == null) {
                objSMSSubscriptionTO = new SMSSubscriptionTO();
            }
            objSMSSubscriptionTO.setProdType("SH");
            objSMSSubscriptionTO.setProdId(objShareAccInfoTO.getShareType());
            objSMSSubscriptionTO.setActNum(objShareAccInfoTO.getShareAcctNo());

            objSMSSubscriptionTO.setMobileNo(CommonUtil.convertObjToStr(getTxtMobileNo()));
            Date smsSubscriptionDt = DateUtil.getDateMMDDYYYY(getTdtMobileSubscribedFrom());
            if (smsSubscriptionDt != null) {
                Date smsDt = (Date) curDate.clone();
                smsDt.setDate(smsSubscriptionDt.getDate());
                smsDt.setMonth(smsSubscriptionDt.getMonth());
                smsDt.setYear(smsSubscriptionDt.getYear());
                objSMSSubscriptionTO.setSubscriptionDt(smsDt);
            } else {
                objSMSSubscriptionTO.setSubscriptionDt(DateUtil.getDateMMDDYYYY(getTdtMobileSubscribedFrom()));
            }
            if (!CommonUtil.convertObjToStr(objSMSSubscriptionTO.getStatus()).equals(CommonConstants.STATUS_MODIFIED)) {
                objSMSSubscriptionTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            objSMSSubscriptionTO.setStatusBy(ProxyParameters.USER_ID);
            objSMSSubscriptionTO.setStatusDt(curDate);
            objSMSSubscriptionTO.setCreatedBy(ProxyParameters.USER_ID);
        } else {
            if (objSMSSubscriptionTO != null) {
                objSMSSubscriptionTO.setStatus(CommonConstants.STATUS_DELETED);
            } else {
                objSMSSubscriptionTO = null;
            }
        }
        return objSMSSubscriptionTO;
    }
    
    /**
     * Getter for property isMobileBanking.
     *
     * @return Value of property isMobileBanking.
     */
    public boolean getIsMobileBanking() {
        return isMobileBanking;
    }

    /**
     * Setter for property isMobileBanking.
     *
     * @param isMobileBanking New value of property isMobileBanking.
     */
    public void setIsMobileBanking(boolean isMobileBanking) {
        this.isMobileBanking = isMobileBanking;
    }
}
