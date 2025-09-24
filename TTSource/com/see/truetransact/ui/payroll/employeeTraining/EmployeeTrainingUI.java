/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * EmployeeTrainingUI.java
 *
 * Created on January 20, 2005, 3:03 PM
 */
package com.see.truetransact.ui.payroll.employeeTraining;

/**
 *
 * @author ashokvijayakumar
 *
 * Modified by anjuanand on 08/12/2014
 *
 */
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import java.util.*;

public class EmployeeTrainingUI extends CInternalFrame implements Observer, UIMandatoryField {

    /**
     * Variable Declarations
     */
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.payroll.employeeTraining.EmployeeTrainingRB", ProxyParameters.LANGUAGE);//Creating Instance For ResourceBundle-TokenConfigRB
    private EmployeeTrainingOB observable; //Reference for the Observable Class TokenConfigOB
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final String AUTHORIZE = "Authorize";//Variable used when btnAuthorize is clicked
    int updateTab = -1;
    private boolean updateMode = false;
    boolean isFilled = false;
    private String view = "";
    private Date currDt = null;
    /**
     * Creates new form EmployeeTrainingUI
     */
    public EmployeeTrainingUI() {
        initForm();
    }

    /**
     * Method which is used to initialize the form EmployeeTraining
     */
    private void initForm() {
        initComponents();
        setFieldNames();
        internationalize();
        observable = new EmployeeTrainingOB();
        observable.addObserver(this);
        observable.resetForm();
        initComponentData();
        setMandatoryHashMap();
        setMaxLengths();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panTrainingDetails);
        ClientUtil.enableDisable(panEmpTraning, false);
        setButtonEnableDisable();
        enableDisablePanButton(false);
        enableDisablePanEmpDetails(false);
        txtTrainingID.setEnabled(false);
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
        cboTrainDest.setName("cboTrainDest");
        lbSpace2.setName("lbSpace2");
        lblCondTeam.setName("lblCondTeam");
        lblMsg.setName("lblMsg");
        lblFromDt.setName("lblFromDt");
        lblLocation.setName("lblLocation");
        lblSpace1.setName("lblSpace1");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblSize.setName("lblSize");
        lblStatus.setName("lblStatus");
        lblTrainDest.setName("lblTrainDest");
        lblTrainingID.setName("lblTrainingID");
        lblToDt.setName("lblToDt");
        mbrTokenConfig.setName("mbrTokenConfig");
        panStatus.setName("panStatus");
        panIndEmpDetails.setName("panIndEmpDetails");
        panEmpList.setName("panEmpList");
        panEmpTraning.setName("panEmpTraning");
        panTrainingDetails.setName("panTrainingDetails");
        tblEmpList.setName("tblEmpList");
        txtCondTeam.setName("txtCondTeam");
        txtLocation.setName("txtLocation");
        txtTrainingID.setName("txtTrainingID");
        txtSize.setName("txtSize");
        btnEmp.setName("btnEmp");
        lblNoOfTrainees.setName("lblNoOfTrainees");
        txtNoOfTrainees.setName("txtNoOfTrainees");
        panTrainingData.setName("panTrainingData");
        panEmpBtn.setName("panEmpBtn");
        btnEmpDelete.setName("btnEmpDelete");
        btnEmpNew.setName("btnEmpNew");
        btnEmpSave.setName("btnEmpSave");
        tdtFrom.setName("tdtFrom");
        tdtTo.setName("tdtTo");
        lblSubj.setName("lblSubj");
        txtSubj.setName("txtSubj");
        lblEmpRemarks.setName("lblEmpRemarks");
        txtEmpRemarks.setName("txtEmpRemarks");
    }

    /*
     * Auto Generated Method - internationalize() This method used to assign
     * display texts from the Resource Bundle File.
     */
    private void internationalize() {
        EmployeeTrainingRB resourceBundle = new EmployeeTrainingRB();
        lblTrainingID.setText(resourceBundle.getString("lblTrainingID"));
        lblTrainDest.setText(resourceBundle.getString("lblTrainDest"));
        lblLocation.setText(resourceBundle.getString("lblLocation"));
        lblCondTeam.setText(resourceBundle.getString("lblCondTeam"));
        lblSubj.setText(resourceBundle.getString("lblSubj"));
        lblSize.setText(resourceBundle.getString("lblSize"));
        lblNoOfTrainees.setText(resourceBundle.getString("lblNoOfTrainees"));
        lblFromDt.setText(resourceBundle.getString("lblFromDt"));
        lblToDt.setText(resourceBundle.getString("lblToDt"));
        lblEmpRemarks.setText(resourceBundle.getString("lblEmpRemarks"));
        lblEmpID.setText(resourceBundle.getString("lblEmpID"));
    }

    /*
     * Setting model to the combobox cboTokenType
     */
    private void initComponentData() {
        try {
            cboTrainDest.setModel(observable.getCbmTrainingDest());
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
        txtCondTeam.setText(observable.getTeam());
        cboTrainDest.setModel(observable.getCbmTrainingDest());
        cboTrainDest.setSelectedItem(observable.getCboTrainingDest());
        txtLocation.setText(observable.getLocation());
        txtSize.setText(observable.getTeamSize());
        txtTrainingID.setText(observable.getTxtEmpTrainingID());
        tdtFrom.setDateValue(observable.getTrainingFrom());
        tdtTo.setDateValue(observable.getTrainingTo());
        lblStatus.setText(observable.getLblStatus());
        txtNoOfTrainees.setText(observable.getNoOfTrainees());
        tblEmpList.setModel(observable.getTblEmpDetails());
        txtEmpID.setText(observable.getTxtEmpID());
        lblName.setText(observable.getEmpName());
        txtSubj.setText(observable.getSubj());
        txtEmpRemarks.setText(observable.getRemarks());
    }

    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    public void updateOBFields() {
        observable.setTxtEmpTrainingID(txtTrainingID.getText());
        observable.setCboTrainingDest(CommonUtil.convertObjToStr(cboTrainDest.getSelectedItem()));
        observable.setLocation(txtLocation.getText());
        observable.setTeamSize(txtSize.getText());
        observable.setTrainingFrom(tdtFrom.getDateValue());
        observable.setTrainingTo(tdtTo.getDateValue());
        observable.setNoOfTrainees(txtNoOfTrainees.getText());
        observable.setTeam(txtCondTeam.getText());
        observable.setEmpName(lblName.getText());
        observable.setTxtEmpID(txtEmpID.getText());
        observable.setSubj(txtSubj.getText());
        observable.setRemarks(txtEmpRemarks.getText());
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
        mandatoryMap.put("cboTrainDest", new Boolean(true));
        mandatoryMap.put("txtLocation", new Boolean(false));
        mandatoryMap.put("txtCondTeam", new Boolean(true));
        mandatoryMap.put("txtSize", new Boolean(true));
        mandatoryMap.put("txtNoOfTrainees", new Boolean(true));
        mandatoryMap.put("tdtFrom", new Boolean(true));
        mandatoryMap.put("tdtTo", new Boolean(true));
        mandatoryMap.put("txtEmpRemarks", new Boolean(false));
        mandatoryMap.put("txtSubj", new Boolean(false));
    }

    /*
     * Auto Generated Method - getMandatoryHashMap() Getter method for
     * setMandatoryHashMap().
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
        EmployeeTrainingMRB objMandatoryRB = new EmployeeTrainingMRB();
        cboTrainDest.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTrainDest"));
        txtLocation.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLocation"));
        txtCondTeam.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCondTeam"));
        txtSubj.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSubj"));
        txtSize.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSize"));
        txtNoOfTrainees.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoOfTrainees"));
        tdtFrom.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtFrom"));
        tdtTo.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtTo"));
        txtEmpRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEmpRemarks"));
        txtEmpID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEmpID"));
    }

    /**
     * Used to set Maximum possible lenghts for TextFields
     */
    private void setMaxLengths() {
        txtEmpID.setAllowAll(true);
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

    private void saveAction() {
        final String mandatoryMessage = checkMandatory(panTrainingDetails);
        StringBuffer message = new StringBuffer(mandatoryMessage);
        if (message.length() > 0) {
            displayAlert(message.toString());
        } else {
            int days = Integer.parseInt(txtNoOfTrainees.getText());
            if (tblEmpList.getRowCount() != days) {
                ClientUtil.displayAlert("No Of Rows Should In Table Should Be Equal To Number Of Trainees");
                return;
            } else {
                observable.doAction();
                this.updateTable();
                if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                    HashMap lockMap = new HashMap();
                    ArrayList lst = new ArrayList();
                    lst.add("TRAINING_ID");
                    lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                    if (observable.getProxyReturnMap() != null) {
                        if (observable.getProxyReturnMap().containsKey("TRAINING_ID")) {
                            lockMap.put("TRAINING_ID", observable.getProxyReturnMap().get("TRAINING_ID"));
                        }
                    }
                    if (observable.getResult() == ClientConstants.ACTIONTYPE_EDIT) {
                        lockMap.put("TRAINING_ID", observable.getTxtEmpTrainingID());
                    }
                    setEditLockMap(lockMap);
                    setEditLock();
                    deletescreenLock();
                    settings();
                }
            }
        }
    }

    private void deletescreenLock() {
        HashMap map = new HashMap();
        map.put("USER_ID", ProxyParameters.USER_ID);
        map.put("TRANS_DT", setProperDateFormat(currDt));
        map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }

    /*
     * set the screen after the updation,insertion, deletion
     */
    private void settings() {
        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panEmpTraning, false);
        setButtonEnableDisable();
        observable.setResultStatus();
        lblStatus.setText(observable.getLblStatus());
        this.updateTable();
        tblEmpList.revalidate();
    }

    /*
     * Method used to showPopup ViewAll by Executing a Query
     */
    private void callView(String currAction) {
        viewType = currAction;
        HashMap viewMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit") || currAction.equalsIgnoreCase("Delete")) {
            isFilled = false;
            HashMap map = new HashMap();
            map.put("CURR_DT", setProperDateFormat(currDt));
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getEmployeeTrainingDetails");
        } else if (currAction.equalsIgnoreCase("EMP")) {
            HashMap map = new HashMap();
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getEmployeeDetailsForTraining");
        } else if (currAction.equalsIgnoreCase("Enquiry")) {
            HashMap map = new HashMap();
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getEmployeeTrainingDetails_View");
        }
        new ViewAll(this, viewMap).show();
    }

    /*
     * Fills up the HashMap with data when user selects the row in ViewAll
     * screen
     */
    public void fillData(Object map) {
        try {
            setModified(true);
            HashMap hash = (HashMap) map;
            if (viewType != null) {
                if (viewType.equals(ClientConstants.ACTION_STATUS[2])
                        || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                    HashMap where = new HashMap();
                    where.put("TRAINING_ID", hash.get("TRAINING_ID"));
                    where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                    hash.put(CommonConstants.MAP_WHERE, where);
                    where = null;
                    observable.getData(hash);
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                        ClientUtil.enableDisable(panEmpTraning, false);
                        enableDisablePanButton(false);
                        enableDisablePanEmpDetails(false);
                    } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                        ClientUtil.enableDisable(panEmpTraning, true);
                        enableDisablePanButton(true);
                        enableDisablePanEmpDetails(true);
                    }
                    setButtonEnableDisable();
                    if (viewType.equals(AUTHORIZE)) {
                        ClientUtil.enableDisable(panEmpTraning, false);
                        btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                        btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                        btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    }

                } else if (viewType.equals("EMP")) {
                    txtEmpID.setText(CommonUtil.convertObjToStr(hash.get("EMPLOYEEID")));
                    lblName.setText(CommonUtil.convertObjToStr(hash.get("EMPLOYEE_NAME")));
                }
            }
            if (viewType.equals(ClientConstants.ACTION_STATUS[2]) || viewType.equals(AUTHORIZE)) {
                HashMap screenMap = new HashMap();
                screenMap.put("TRANS_ID", hash.get("TRAINING_ID"));
                screenMap.put("USER_ID", ProxyParameters.USER_ID);
                screenMap.put("TRANS_DT", setProperDateFormat(currDt));
                screenMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                java.util.List lst = ClientUtil.executeQuery("selectauthorizationLock", screenMap);
                if (lst != null && lst.size() > 0) {
                    screenMap = null;
                    StringBuffer open = new StringBuffer();
                    for (int i = 0; i < lst.size(); i++) {
                        screenMap = (HashMap) lst.get(i);
                        open.append("\n" + "User Id  :" + " ");
                        open.append(CommonUtil.convertObjToStr(screenMap.get("OPEN_BY")) + "\n");
                        open.append("Mode Of Operation  :" + " ");
                        open.append(CommonUtil.convertObjToStr(screenMap.get("MODE_OF_OPERATION")) + " ");
                        btnSave.setEnabled(false);
                        ClientUtil.enableDisable(panEmpTraning, false);
                        enableDisablePanButton(false);
                        enableDisablePanEmpDetails(false);
                    }
                    ClientUtil.showMessageWindow("already open by" + open);
                    return;
                } else {
                    hash.put("TRANS_ID", hash.get("TRAINING_ID"));
                    if (viewType.equals(ClientConstants.ACTION_STATUS[2])) {
                        hash.put("MODE_OF_OPERATION", "EDIT");
                    }
                    if (viewType == AUTHORIZE) {
                        hash.put("MODE_OF_OPERATION", "AUTHORIZE");
                    }
                    hash.put("USER_ID", TrueTransactMain.USER_ID);
                    hash.put("TRANS_DT", setProperDateFormat(currDt));
                    hash.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                    ClientUtil.execute("insertauthorizationLock", hash);
                }
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                isFilled = false;
                String lockedBy = "";
                HashMap Lockmap = new HashMap();
                Lockmap.put("SCREEN_ID", getScreenID());
                Lockmap.put("RECORD_KEY", CommonUtil.convertObjToStr(hash.get("TRAINING_ID")));
                Lockmap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
                java.util.List lstLock = ClientUtil.executeQuery("selectEditLock", Lockmap);
                if (lstLock.size() > 0) {
                    lockedBy = CommonUtil.convertObjToStr(lstLock.get(0));
                    if (!lockedBy.equals(ProxyParameters.USER_ID)) {
                        btnSave.setEnabled(false);
                        ClientUtil.enableDisable(panEmpTraning, false);
                        enableDisablePanButton(false);
                        enableDisablePanEmpDetails(false);
                    } else {
                        btnSave.setEnabled(true);
                        ClientUtil.enableDisable(panEmpTraning, true);
                        enableDisablePanButton(true);
                        enableDisablePanEmpDetails(true);
                    }
                } else {
                    btnSave.setEnabled(true);
                    ClientUtil.enableDisable(panEmpTraning, true);
                    enableDisablePanButton(true);
                    enableDisablePanEmpDetails(true);
                }
                setOpenForEditBy(lockedBy);
                if (lockedBy.length() > 0 && !lockedBy.equals(ProxyParameters.USER_ID)) {
                    String data = getLockDetails(lockedBy, getScreenID());
                    ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
                    btnSave.setEnabled(false);
                    ClientUtil.enableDisable(panEmpTraning, false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getLockDetails(String lockedBy, String screenId) {
        HashMap map = new HashMap();
        StringBuffer data = new StringBuffer();
        map.put("LOCKED_BY", lockedBy);
        map.put("SCREEN_ID", screenId);
        java.util.List lstLock = ClientUtil.executeQuery("getLockedDetails", map);
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
     * This will show the alertwindow *
     */
    private int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {resourceBundle.getString("cDialogOK")};
        optionSelected = COptionPane.showOptionDialog(null, resourceBundle.getString(alertMsg), CommonConstants.INFORMATIONTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        return optionSelected;
    }

    /**
     * Method used to do Required operation when user clicks
     * btnAuthorize,btnReject or btnReject *
     */
    public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)) {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getEmpTrainingDetailsAuthorize");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeEmpTraining");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeEmpTrainingDetails");
            mapParam.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            mapParam.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            setModified(true);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);

        } else if (viewType.equals(AUTHORIZE)) {
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTH_DT", setProperDateFormat(currDt));
            singleAuthorizeMap.put("EMP_TRAINING_ID", txtTrainingID.getText());
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            ClientUtil.execute("authorizeEmpTraining", singleAuthorizeMap);
            ClientUtil.execute("authorizeEmpTrainingDetails", singleAuthorizeMap);
            viewType = "";
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(txtTrainingID.getText());
            btnCancelActionPerformed(null);
            observable.setResultStatus();
            lblStatus.setText(authorizeStatus);
        }
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

    private void enableDisablePanButton(boolean flag) {
        btnEmpDelete.setEnabled(flag);
        btnEmpSave.setEnabled(flag);
        btnEmpNew.setEnabled(flag);
    }

    private void enableDisablePanEmpDetails(boolean flag) {
        txtEmpID.setEnabled(flag);
        btnEmp.setEnabled(flag);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panEmpTraning = new com.see.truetransact.uicomponent.CPanel();
        panEmpList = new com.see.truetransact.uicomponent.CPanel();
        srpTokenLost = new com.see.truetransact.uicomponent.CScrollPane();
        tblEmpList = new com.see.truetransact.uicomponent.CTable();
        panTrainingDetails = new com.see.truetransact.uicomponent.CPanel();
        panIndEmpDetails = new com.see.truetransact.uicomponent.CPanel();
        lblName = new com.see.truetransact.uicomponent.CLabel();
        lblEmpID = new com.see.truetransact.uicomponent.CLabel();
        panEmpDetails = new com.see.truetransact.uicomponent.CPanel();
        btnEmp = new com.see.truetransact.uicomponent.CButton();
        txtEmpID = new com.see.truetransact.uicomponent.CTextField();
        panEmpBtn = new com.see.truetransact.uicomponent.CPanel();
        btnEmpNew = new com.see.truetransact.uicomponent.CButton();
        btnEmpSave = new com.see.truetransact.uicomponent.CButton();
        btnEmpDelete = new com.see.truetransact.uicomponent.CButton();
        panTrainingData = new com.see.truetransact.uicomponent.CPanel();
        tdtTo = new com.see.truetransact.uicomponent.CDateField();
        tdtFrom = new com.see.truetransact.uicomponent.CDateField();
        txtSize = new com.see.truetransact.uicomponent.CTextField();
        cboTrainDest = new com.see.truetransact.uicomponent.CComboBox();
        txtTrainingID = new com.see.truetransact.uicomponent.CTextField();
        lblTrainingID = new com.see.truetransact.uicomponent.CLabel();
        lblTrainDest = new com.see.truetransact.uicomponent.CLabel();
        lblLocation = new com.see.truetransact.uicomponent.CLabel();
        lblCondTeam = new com.see.truetransact.uicomponent.CLabel();
        lblSize = new com.see.truetransact.uicomponent.CLabel();
        lblFromDt = new com.see.truetransact.uicomponent.CLabel();
        lblToDt = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfTrainees = new com.see.truetransact.uicomponent.CTextField();
        lblNoOfTrainees = new com.see.truetransact.uicomponent.CLabel();
        txtLocation = new com.see.truetransact.uicomponent.CTextField();
        txtCondTeam = new com.see.truetransact.uicomponent.CTextField();
        txtSubj = new com.see.truetransact.uicomponent.CTextField();
        lblSubj = new com.see.truetransact.uicomponent.CLabel();
        lblEmpRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtEmpRemarks = new com.see.truetransact.uicomponent.CTextField();
        tbrTokenConfig = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lbSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace35 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace36 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace37 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace38 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace39 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
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
        setMaximumSize(new java.awt.Dimension(800, 800));
        setMinimumSize(new java.awt.Dimension(800, 550));
        setPreferredSize(new java.awt.Dimension(800, 550));

        panEmpTraning.setMaximumSize(new java.awt.Dimension(650, 550));
        panEmpTraning.setMinimumSize(new java.awt.Dimension(650, 550));
        panEmpTraning.setPreferredSize(new java.awt.Dimension(650, 550));
        panEmpTraning.setLayout(new java.awt.GridBagLayout());

        panEmpList.setMaximumSize(new java.awt.Dimension(250, 450));
        panEmpList.setMinimumSize(new java.awt.Dimension(250, 450));
        panEmpList.setPreferredSize(new java.awt.Dimension(250, 450));
        panEmpList.setLayout(new java.awt.GridBagLayout());

        srpTokenLost.setMaximumSize(new java.awt.Dimension(240, 450));
        srpTokenLost.setMinimumSize(new java.awt.Dimension(240, 450));
        srpTokenLost.setPreferredSize(new java.awt.Dimension(240, 450));

        tblEmpList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "SL No", "Emp Name", "Emp ID"
            }
        ));
        tblEmpList.setMaximumSize(new java.awt.Dimension(2147483647, 1000));
        tblEmpList.setMinimumSize(new java.awt.Dimension(230, 1000));
        tblEmpList.setPreferredScrollableViewportSize(new java.awt.Dimension(230, 500));
        tblEmpList.setPreferredSize(new java.awt.Dimension(230, 1000));
        tblEmpList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblEmpListMousePressed(evt);
            }
        });
        srpTokenLost.setViewportView(tblEmpList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panEmpList.add(srpTokenLost, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panEmpTraning.add(panEmpList, gridBagConstraints);

        panTrainingDetails.setMaximumSize(new java.awt.Dimension(480, 450));
        panTrainingDetails.setMinimumSize(new java.awt.Dimension(480, 450));
        panTrainingDetails.setPreferredSize(new java.awt.Dimension(480, 450));
        panTrainingDetails.setLayout(new java.awt.GridBagLayout());

        panIndEmpDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Employee Details"));
        panIndEmpDetails.setMaximumSize(new java.awt.Dimension(450, 120));
        panIndEmpDetails.setMinimumSize(new java.awt.Dimension(450, 120));
        panIndEmpDetails.setPreferredSize(new java.awt.Dimension(450, 120));
        panIndEmpDetails.setLayout(new java.awt.GridBagLayout());

        lblName.setMaximumSize(new java.awt.Dimension(300, 21));
        lblName.setMinimumSize(new java.awt.Dimension(300, 21));
        lblName.setPreferredSize(new java.awt.Dimension(300, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panIndEmpDetails.add(lblName, gridBagConstraints);

        lblEmpID.setText("Employee ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 60, 1, 4);
        panIndEmpDetails.add(lblEmpID, gridBagConstraints);

        panEmpDetails.setMinimumSize(new java.awt.Dimension(140, 24));
        panEmpDetails.setPreferredSize(new java.awt.Dimension(140, 24));
        panEmpDetails.setLayout(new java.awt.GridBagLayout());

        btnEmp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnEmp.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnEmp.setMinimumSize(new java.awt.Dimension(25, 25));
        btnEmp.setPreferredSize(new java.awt.Dimension(25, 25));
        btnEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panEmpDetails.add(btnEmp, gridBagConstraints);

        txtEmpID.setMaxLength(128);
        txtEmpID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtEmpID.setName("txtCompany");
        txtEmpID.setValidation(new DefaultValidation());
        txtEmpID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmpIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEmpDetails.add(txtEmpID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panIndEmpDetails.add(panEmpDetails, gridBagConstraints);

        panEmpBtn.setLayout(new java.awt.GridBagLayout());

        btnEmpNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnEmpNew.setToolTipText("New");
        btnEmpNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnEmpNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnEmpNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnEmpNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmpBtn.add(btnEmpNew, gridBagConstraints);

        btnEmpSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnEmpSave.setToolTipText("Save");
        btnEmpSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnEmpSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnEmpSave.setName("btnContactNoAdd");
        btnEmpSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnEmpSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmpBtn.add(btnEmpSave, gridBagConstraints);

        btnEmpDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnEmpDelete.setToolTipText("Delete");
        btnEmpDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnEmpDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnEmpDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnEmpDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmpBtn.add(btnEmpDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panIndEmpDetails.add(panEmpBtn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTrainingDetails.add(panIndEmpDetails, gridBagConstraints);

        panTrainingData.setBorder(javax.swing.BorderFactory.createTitledBorder("Training Details"));
        panTrainingData.setMaximumSize(new java.awt.Dimension(450, 400));
        panTrainingData.setMinimumSize(new java.awt.Dimension(450, 325));
        panTrainingData.setPreferredSize(new java.awt.Dimension(450, 375));
        panTrainingData.setLayout(new java.awt.GridBagLayout());

        tdtTo.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtTo.setName("tdtToDate");
        tdtTo.setNextFocusableComponent(btnEmpNew);
        tdtTo.setPreferredSize(new java.awt.Dimension(101, 19));
        tdtTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingData.add(tdtTo, gridBagConstraints);

        tdtFrom.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtFrom.setName("tdtFromDate");
        tdtFrom.setNextFocusableComponent(tdtTo);
        tdtFrom.setPreferredSize(new java.awt.Dimension(101, 19));
        tdtFrom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingData.add(tdtFrom, gridBagConstraints);

        txtSize.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSize.setNextFocusableComponent(txtNoOfTrainees);
        txtSize.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingData.add(txtSize, gridBagConstraints);

        cboTrainDest.setMinimumSize(new java.awt.Dimension(100, 21));
        cboTrainDest.setNextFocusableComponent(txtLocation);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingData.add(cboTrainDest, gridBagConstraints);

        txtTrainingID.setEditable(false);
        txtTrainingID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTrainingID.setNextFocusableComponent(cboTrainDest);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingData.add(txtTrainingID, gridBagConstraints);

        lblTrainingID.setText("Training ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingData.add(lblTrainingID, gridBagConstraints);

        lblTrainDest.setText("Training Destination ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingData.add(lblTrainDest, gridBagConstraints);

        lblLocation.setText("Location");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingData.add(lblLocation, gridBagConstraints);

        lblCondTeam.setText("Conducting Team");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingData.add(lblCondTeam, gridBagConstraints);

        lblSize.setText("Team Size");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingData.add(lblSize, gridBagConstraints);

        lblFromDt.setText("Training From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingData.add(lblFromDt, gridBagConstraints);

        lblToDt.setText("Training To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingData.add(lblToDt, gridBagConstraints);

        txtNoOfTrainees.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoOfTrainees.setNextFocusableComponent(tdtFrom);
        txtNoOfTrainees.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingData.add(txtNoOfTrainees, gridBagConstraints);

        lblNoOfTrainees.setText("No Of Trainees");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingData.add(lblNoOfTrainees, gridBagConstraints);

        txtLocation.setMaxLength(128);
        txtLocation.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLocation.setName("txtCompany");
        txtLocation.setNextFocusableComponent(txtCondTeam);
        txtLocation.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingData.add(txtLocation, gridBagConstraints);

        txtCondTeam.setMaxLength(128);
        txtCondTeam.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCondTeam.setName("txtCompany");
        txtCondTeam.setNextFocusableComponent(txtSize);
        txtCondTeam.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingData.add(txtCondTeam, gridBagConstraints);

        txtSubj.setMaxLength(128);
        txtSubj.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSubj.setName("txtCompany");
        txtSubj.setNextFocusableComponent(txtSize);
        txtSubj.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingData.add(txtSubj, gridBagConstraints);

        lblSubj.setText("Subject");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingData.add(lblSubj, gridBagConstraints);

        lblEmpRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingData.add(lblEmpRemarks, gridBagConstraints);

        txtEmpRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panTrainingData.add(txtEmpRemarks, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTrainingDetails.add(panTrainingData, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 20);
        panEmpTraning.add(panTrainingDetails, gridBagConstraints);

        getContentPane().add(panEmpTraning, java.awt.BorderLayout.CENTER);

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

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace34);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnEdit);

        lblSpace35.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace35.setText("     ");
        lblSpace35.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace35);

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

        lblSpace36.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace36.setText("     ");
        lblSpace36.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace36.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace36.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace36);

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
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnAuthorize);

        lblSpace37.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace37.setText("     ");
        lblSpace37.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace37.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace37.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace37);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnException);

        lblSpace38.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace38.setText("     ");
        lblSpace38.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace38.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace38.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace38);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnReject);

        lblSpace5.setText("     ");
        tbrTokenConfig.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrTokenConfig.add(btnPrint);

        lblSpace39.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace39.setText("     ");
        lblSpace39.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace39.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace39.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace39);

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

    private void tdtToFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToFocusLost
        // TODO add your handling code here
        if (tdtTo.getDateValue().length() > 0) {
            Date toDt = DateUtil.getDateMMDDYYYY(tdtTo.getDateValue());
            Date fromDt = DateUtil.getDateMMDDYYYY(tdtFrom.getDateValue());
            if (toDt.before(fromDt)) {
                ClientUtil.displayAlert("To Date Should Be Equal To Or Greater Than From Date");
                tdtTo.setDateValue("");
                return;
            }
        }

    }//GEN-LAST:event_tdtToFocusLost

    private void tdtFromFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromFocusLost
        // TODO add your handling code here:
//        if(tdtFrom.getDateValue().length()>0)
//        ClientUtil.validateToDate(tdtFrom, DateUtil.getStringDate(currDt.clone()));
    }//GEN-LAST:event_tdtFromFocusLost

    private void btnEmpDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpDeleteActionPerformed
        // TODO add your handling code here:
        String s = CommonUtil.convertObjToStr(tblEmpList.getValueAt(tblEmpList.getSelectedRow(), 0));
        observable.deleteTableData(s, tblEmpList.getSelectedRow());
        observable.resetEmpDetails();
        lblName.setText("");
        enableDisablePanEmpDetails(false);
    }//GEN-LAST:event_btnEmpDeleteActionPerformed

    private void txtEmpIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmpIDActionPerformed
        // TODO add your handling code here:
        if (txtEmpID.getText().length() > 0) {
            HashMap empMap = new HashMap();
            empMap.put("EMP_CODE", txtEmpID.getText());
            empMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            java.util.List empList = ClientUtil.executeQuery("getEmpDetailsForTransfer", empMap);
            if (empList != null && empList.size() > 0) {
                empMap = null;
                empMap = (HashMap) empList.get(0);
                txtEmpID.setText(CommonUtil.convertObjToStr(empMap.get("EMPLOYEE_CODE")));
                lblName.setText(CommonUtil.convertObjToStr(empMap.get("FNAME")));
            } else {
                ClientUtil.displayAlert("Invalid Employee ID");
                txtEmpID.setText("");
            }
        }
    }//GEN-LAST:event_txtEmpIDActionPerformed

    private void tblEmpListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmpListMousePressed
        // TODO add your handling code here:
        updateOBFields();
        updateMode = true;
        updateTab = tblEmpList.getSelectedRow();
        observable.setNewData(false);
        String st = CommonUtil.convertObjToStr(tblEmpList.getValueAt(tblEmpList.getSelectedRow(), 0));
        observable.populateLeaveDetails(st);
        enableDisablePanButton(true);
        enableDisablePanEmpDetails(true);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            enableDisablePanEmpDetails(false);
            enableDisablePanButton(false);
        } else {
            enableDisablePanEmpDetails(true);
            enableDisablePanButton(true);;
        }
        observable.notifyObservers();
    }//GEN-LAST:event_tblEmpListMousePressed

    private void btnEmpSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpSaveActionPerformed
        // TODO add your handling code here:
        try {
            if (txtEmpID.getText().length() > 0) {
                String empID = txtEmpID.getText();
                int days = Integer.parseInt(txtNoOfTrainees.getText());
                int tableCount = tblEmpList.getRowCount();
                if (tableCount >= days && !updateMode) {
                    ClientUtil.displayAlert("Cant Enter more Than" + "" + txtNoOfTrainees.getText() + "Entries");
                    txtEmpID.setText("");
                    lblName.setText("");
                    return;
                }
                if (tblEmpList.getRowCount() > 0) {
                    for (int i = 0; i < tblEmpList.getRowCount(); i++) {
                        String eID = CommonUtil.convertObjToStr(tblEmpList.getValueAt(i, 2));
                        if (eID.equalsIgnoreCase(empID) && !updateMode) {
                            ClientUtil.displayAlert("Employee Details Already Exists in Table");
                            txtEmpID.setText("");
                            lblName.setText("");
                            return;
                        }
                    }
                }
                updateOBFields();
                observable.addToTable(updateTab, updateMode);
                observable.resetEmpDetails();
                ClientUtil.enableDisable(panEmpDetails, false);
                lblName.setText("");
                txtEmpID.setText("");
                enableDisablePanEmpDetails(false);
            } else {
                ClientUtil.displayAlert("Employee ID Should Not Be Empty...");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnEmpSaveActionPerformed

    private void btnEmpNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpNewActionPerformed
        // TODO add your handling code here:
        enableDisablePanEmpDetails(true);
        txtEmpID.setText("");
        lblName.setText("");
        updateMode = false;
        observable.setNewData(true);
        ClientUtil.enableDisable(panEmpDetails, true);
    }//GEN-LAST:event_btnEmpNewActionPerformed

    private void btnEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpActionPerformed
        // TODO add your handling code here:
        callView("EMP");
    }//GEN-LAST:event_btnEmpActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView("Enquiry");
        btnCheck();
        lblStatus.setText("Enquiry");
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_mitCloseActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
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

    public static void main(String[] args) {
        EmployeeTrainingUI empTran = new EmployeeTrainingUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(empTran);
        j.show();
        empTran.show();
    }

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        setModified(false);
        deletescreenLock();
        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panEmpTraning, false);
        ClientUtil.enableDisable(panIndEmpDetails, false);
        ClientUtil.enableDisable(panEmpBtn, false);
        lblName.setText("");
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        enableDisablePanButton(false);
        viewType = "";
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE && isFilled == true) {
            view = "DELETE";
            isFilled = false;
            observable.doAction();
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                lblStatus.setText("Deleted");
                observable.resetForm();
                this.updateTable();
            } else if (observable.getResult() == ClientConstants.ACTIONTYPE_FAILED) {
                lblStatus.setText("Failed");
                observable.resetForm();
            }
        } else {
            observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
            callView("Delete");
            isFilled = true;
            btnReject.setEnabled(false);
            btnAuthorize.setEnabled(false);
            btnException.setEnabled(false);
            txtTrainingID.setEnabled(false);
            btnDelete.setEnabled(true);
            lblStatus.setText("Delete");
            this.updateTable();
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView("Edit");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        txtTrainingID.setEnabled(false);
        txtEmpID.setEnabled(false);
        btnEmp.setEnabled(false);
        lblStatus.setText("Edit");
        this.updateTable();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        setModified(false);
        updateOBFields();
        saveAction();
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void updateTable() {
        this.tblEmpList.setModel(observable.getTblEmpDetails());
        this.tblEmpList.revalidate();
   }
    
    public Date setProperDateFormat(Object obj) {
        Date currDate = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate = (Date) currDt.clone();
            currDate.setDate(tempDt.getDate());
            currDate.setMonth(tempDt.getMonth());
            currDate.setYear(tempDt.getYear());
        }
        return currDate;
    }

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.resetForm();
        enableDisablePanButton(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        ClientUtil.enableDisable(panTrainingData, true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnDelete.setEnabled(false);
        txtTrainingID.setEnabled(false);
        lblStatus.setText("New");
        this.updateTable();
    }//GEN-LAST:event_btnNewActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnEmp;
    private com.see.truetransact.uicomponent.CButton btnEmpDelete;
    private com.see.truetransact.uicomponent.CButton btnEmpNew;
    private com.see.truetransact.uicomponent.CButton btnEmpSave;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboTrainDest;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblCondTeam;
    private com.see.truetransact.uicomponent.CLabel lblEmpID;
    private com.see.truetransact.uicomponent.CLabel lblEmpRemarks;
    private com.see.truetransact.uicomponent.CLabel lblFromDt;
    private com.see.truetransact.uicomponent.CLabel lblLocation;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblName;
    private com.see.truetransact.uicomponent.CLabel lblNoOfTrainees;
    private com.see.truetransact.uicomponent.CLabel lblSize;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace35;
    private com.see.truetransact.uicomponent.CLabel lblSpace36;
    private com.see.truetransact.uicomponent.CLabel lblSpace37;
    private com.see.truetransact.uicomponent.CLabel lblSpace38;
    private com.see.truetransact.uicomponent.CLabel lblSpace39;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblSubj;
    private com.see.truetransact.uicomponent.CLabel lblToDt;
    private com.see.truetransact.uicomponent.CLabel lblTrainDest;
    private com.see.truetransact.uicomponent.CLabel lblTrainingID;
    private com.see.truetransact.uicomponent.CMenuBar mbrTokenConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panEmpBtn;
    private com.see.truetransact.uicomponent.CPanel panEmpDetails;
    private com.see.truetransact.uicomponent.CPanel panEmpList;
    private com.see.truetransact.uicomponent.CPanel panEmpTraning;
    private com.see.truetransact.uicomponent.CPanel panIndEmpDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTrainingData;
    private com.see.truetransact.uicomponent.CPanel panTrainingDetails;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CScrollPane srpTokenLost;
    private com.see.truetransact.uicomponent.CTable tblEmpList;
    private com.see.truetransact.uicomponent.CToolBar tbrTokenConfig;
    private com.see.truetransact.uicomponent.CDateField tdtFrom;
    private com.see.truetransact.uicomponent.CDateField tdtTo;
    private com.see.truetransact.uicomponent.CTextField txtCondTeam;
    private com.see.truetransact.uicomponent.CTextField txtEmpID;
    private com.see.truetransact.uicomponent.CTextField txtEmpRemarks;
    private com.see.truetransact.uicomponent.CTextField txtLocation;
    private com.see.truetransact.uicomponent.CTextField txtNoOfTrainees;
    private com.see.truetransact.uicomponent.CTextField txtSize;
    private com.see.truetransact.uicomponent.CTextField txtSubj;
    private com.see.truetransact.uicomponent.CTextField txtTrainingID;
    // End of variables declaration//GEN-END:variables
}
