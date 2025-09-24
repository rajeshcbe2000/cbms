/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CInternalFrame.java
 *
 * Created on July 28, 2003, 10:37 AM
 */
package com.see.truetransact.uicomponent;

import java.util.HashMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.List;

/**
 * @author annamalai_t1
 * @author bala
 */
public class CInternalFrame extends javax.swing.JInternalFrame {

    HashMap screenConfig = null; // Screen configration from Database
    private String module; // Module Name for the sub class
    private String screen; // Screen name for the sub class
    private boolean modified = false; // is data modified or not flag.
    private String selectedBranchID;
    private String openForEditBy;
    private String screenID;
    private int mode;
    private HashMap editLockMap;
    private boolean switchEnglish = true;
    private java.util.Date currDt = null;
    private boolean isRecord = false;

    /**
     * CInternalFrame constructor comment.
     */
    public CInternalFrame() {
        super();
        initSetupDefault();
    }

    /**
     * CInternalFrame constructor comment.
     *
     * @param title java.lang.String
     */
    public CInternalFrame(String title) {
        super(title);
        initSetupDefault();
    }

    /**
     * CInternalFrame constructor comment.
     *
     * @param title java.lang.String
     * @param resizable boolean
     */
    public CInternalFrame(String title, boolean resizable) {
        super(title, resizable);
        initSetupDefault();
    }

    /**
     * CInternalFrame constructor comment.
     *
     * @param title java.lang.String
     * @param resizable boolean
     * @param closable boolean
     */
    public CInternalFrame(String title, boolean resizable, boolean closable) {
        super(title, resizable, closable);
        initSetupDefault();
    }

    /**
     * CInternalFrame constructor comment.
     *
     * @param title java.lang.String
     * @param resizable boolean
     * @param closable boolean
     * @param maximizable boolean
     */
    public CInternalFrame(
            String title,
            boolean resizable,
            boolean closable,
            boolean maximizable) {
        super(title, resizable, closable, maximizable);
        initSetupDefault();
    }

    /**
     * CInternalFrame constructor comment.
     *
     * @param title java.lang.String
     * @param resizable boolean
     * @param closable boolean
     * @param maximizable boolean
     * @param iconifiable boolean
     */
    public CInternalFrame(
            String title,
            boolean resizable,
            boolean closable,
            boolean maximizable,
            boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
        initSetupDefault();
    }

    public void initSetupDefault() {
//        setMinimumSize(getPreferredSize());
        currDt = ClientUtil.getCurrentDate();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                cifClosingAlert();
            }
        });
    }

    public void callInternationalize() {
    }

    private void internationalize() {
    }

    /**
     * fillData - empty method used for retriving data and filling in the
     * screen.
     *
     * @param obj Object
     */
    public void fillData(Object obj) {
    }

    /**
     * authorizeStatus - empty method used for process in the cashierApproval
     * screen.
     *
     * @param paramMap HashMap
     */
    public void authorizeStatus(String status) {

    }

    /**
     * authorize - empty method used for process in the screen.
     *
     * @param paramMap HashMap
     */
    public void authorize(HashMap paramMap) {

    }

    /**
     * Getter for property screenConfig.
     *
     * @return Value of property screenConfig.
     *
     */
    public HashMap getScreenConfig() {
        return this.screenConfig;
    }

    /**
     * Setter for property screenConfig.
     *
     * @param screenConfig New value of property screenConfig.
     *
     */
    public void setScreenConfig(HashMap screenConfig) {
        this.screenConfig = screenConfig;
    }

    /**
     * Getter for property module.
     *
     * @return Value of property module.
     *
     */
    public java.lang.String getModule() {
        return module;
    }

    /**
     * Setter for property module.
     *
     * @param module New value of property module.
     *
     */
    public void setModule(java.lang.String module) {
        this.module = module;
    }

    /**
     * Getter for property screen.
     *
     * @return Value of property screen.
     *
     */
    public java.lang.String getScreen() {
        return screen;
    }

    /**
     * Setter for property screen.
     *
     * @param screen New value of property screen.
     *
     */
    public void setScreen(java.lang.String screen) {
        this.screen = screen;
    }

    /**
     * This method checks the Modified status based on the status show alert
     *
     * <code>
     * addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
     * public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
     * cifClosingAlert();
     * }
     * });
     * </code>
     */
    public void cifClosingAlert() {
        if (isModified()) {
            COptionPane.showMessageDialog(this, "Please press Save or Cancel before closing.",
                    "Closing", COptionPane.OK_OPTION);
        } else {
            updateScreenAccessHistory();            
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            javax.swing.JMenu mnu = TrueTransactMain.getMenu();
            String tit = "";
            try {
                tit = this.title.substring(0, (this.title.indexOf("[")) - 1);
            } catch (Exception e) {
                tit = this.title.trim();
            }
//            System.out.println("%%%%%%%%%%%%%%%% Title : "+this.title);
//            System.out.println("%%%%%%%%%%%%%%%% Title substring : " + this.title.substring(0, (this.title.indexOf("["))-1));
            if (tit.length() > 0) {
                int avail = 0;
                for (int m = 4; m < mnu.getItemCount(); m++) {
                    if (mnu.getItem(m).getText().equals(tit)) {
                        avail++;
                        mnu.remove(mnu.getItem(m));
                    }
                }
            }
            this.dispose();
        }
    }
    
    private void updateScreenAccessHistory(){
        if(CommonUtil.convertObjToStr(getScreen()).length() > 0 && CommonUtil.convertObjToStr(getModule()).length() > 0) {
            HashMap historyMap = new HashMap();
            historyMap.put(CommonConstants.SCREEN, getScreen());
            historyMap.put(CommonConstants.MODULE, getModule());
            historyMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            historyMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            historyMap.put("APPL_DT", currDt);
            ClientUtil.execute("updateScreenAccessHistory", historyMap);
        }
    }
    /**
     * Getter for property modified.
     *
     * @return Value of property modified.
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * Setter for property modified.
     *
     * @param modified New value of property modified.
     */
    public void setModified(boolean modified) {
        if (mode == ClientConstants.ACTIONTYPE_VIEW_MODE) {
            this.modified = false;
        } else {
            this.modified = modified;
        }
    }

    /**
     * Getter for property selectedBranchID.
     *
     * @return Value of property selectedBranchID.
     */
    public java.lang.String getSelectedBranchID() {
        return selectedBranchID;
    }

    /**
     * Setter for property selectedBranchID.
     *
     * @param selectedBranchID New value of property selectedBranchID.
     */
    public void setSelectedBranchID(java.lang.String selectedBranchID) {
        this.selectedBranchID = selectedBranchID;
    }

    /**
     * Getter for property openForEditBy.
     *
     * @return Value of property openForEditBy.
     */
    public java.lang.String getOpenForEditBy() {
        return openForEditBy;
    }

    /**
     * Setter for property openForEditBy.
     *
     * @param openForEditBy New value of property openForEditBy.
     */
    public void setOpenForEditBy(java.lang.String openForEditBy) {
        this.openForEditBy = openForEditBy;
    }

    /**
     * Getter for property screenID.
     *
     * @return Value of property screenID.
     */
    public java.lang.String getScreenID() {
        return screenID;
    }

    /**
     * Setter for property screenID.
     *
     * @param screenID New value of property screenID.
     */
    public void setScreenID(java.lang.String screenID) {
        this.screenID = screenID;
    }

    public void modifyTransData(Object obj) {
    }

    public void setEditLock() {
        String lockedBy = "";
        if (editLockMap.containsKey(ClientConstants.RECORD_KEY_COL)) {
            System.out.println("inside RECORD_KEY_COL");
            HashMap map = new HashMap();
            map.put("SCREEN_ID", getScreenID());

            java.util.ArrayList lstRecKey = (java.util.ArrayList) editLockMap.get(ClientConstants.RECORD_KEY_COL);

            StringBuffer strRecKey = new StringBuffer();
            for (int i = 0, j = lstRecKey.size(); i < j; i++) {
                strRecKey.append(editLockMap.get(lstRecKey.get(i)));
            }
            System.out.println("strRecKey=" + strRecKey);
            if (!strRecKey.toString().equals("null")) {
                map.put("RECORD_KEY", strRecKey.toString());
                map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
                map.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
                map.put("CUR_DATE", currDt);
                System.out.println("Record Key Map : " + map);

                strRecKey = null;
                java.util.List lstLock = ClientUtil.executeQuery("selectEditLock", map);

                if (lstLock.size() > 0) {
                    lockedBy = CommonUtil.convertObjToStr(lstLock.get(0));
                    if (!lockedBy.equals(ProxyParameters.USER_ID)) {
                        setMode(ClientConstants.ACTIONTYPE_VIEW_MODE);
                    } else {
                        setMode(ClientConstants.ACTIONTYPE_EDIT);
                    }
                } else {
                    setMode(ClientConstants.ACTIONTYPE_EDIT);
                }

//               setOpenForEditBy(lockedBy);
                if (lockedBy.equals("")) {
                    ClientUtil.execute("insertEditLock", map);
                }
            }
            map = null;
//            lstLock = null;
        }
        setEditLockMap(new HashMap());
//        if (lockedBy.length() > 0 && !lockedBy.equals(ProxyParameters.USER_ID)) {
//            String data = getLockDetails(lockedBy, getScreenID()) ;
//            ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
//        }
    }

    public void insertScreenLock(String recordKey, String viewType) {//Added by Ajay Sharma for Screen Lock on 09-Apr-2014
        HashMap hash = new HashMap();
        hash.put("TRANS_ID", recordKey);
        hash.put("USER_ID", ProxyParameters.USER_ID);
        if (viewType.equals("DELETE")) {
            hash.put("MODE_OF_OPERATION", "DELETE");
        }
        if (viewType.equals("AUTHORIZE")) {
            hash.put("MODE_OF_OPERATION", "AUTHORIZE");
        }
        if (viewType.equals("EDIT")) {
            hash.put("MODE_OF_OPERATION", "EDIT");
        }
        if (viewType.equals("REJECT")) {
            hash.put("MODE_OF_OPERATION", "REJECT");
        }
        if (viewType.equals("RENEW")) {
            hash.put("MODE_OF_OPERATION", "RENEW");
        }
        hash.put("TRANS_DT", currDt);
        hash.put("INITIATED_BRANCH", TrueTransactMain.BRANCH_ID);
        System.out.println("TRANS_ID = " + recordKey + " USER_ID = " + ProxyParameters.USER_ID);
        System.out.println("MODE_OF_OPERATION = " + viewType);
        System.out.println("TRANS_DT = " + currDt + " INITIATED_BRANCH = " + TrueTransactMain.BRANCH_ID);
        ClientUtil.execute("insertauthorizationLock", hash);
    }

    public boolean validateScreenLock(String recordKey) {
        HashMap authDataMap = new HashMap();
        authDataMap.put("TRANS_ID", recordKey);
        authDataMap.put("USER_ID", ProxyParameters.USER_ID);
        authDataMap.put("TRANS_DT", currDt);
        authDataMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        List lst = ClientUtil.executeQuery("selectauthorizationLock", authDataMap);
        StringBuffer open = new StringBuffer();
        System.out.println("lst.size() =" + lst.size());
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                HashMap map = (HashMap) lst.get(i);
                open.append("\n" + "User Id  :" + " ");
                open.append(CommonUtil.convertObjToStr(map.get("OPEN_BY")) + "\n");
                open.append("Mode Of Operation  :" + " ");
                open.append(CommonUtil.convertObjToStr(map.get("MODE_OF_OPERATION")) + " ");
            }
            ClientUtil.showMessageWindow("Already opened by" + open);
            open = new StringBuffer();
            return true;
        }
        return false;
    }

    public void deleteScreenLock(String transID, String oprMode) {
        System.out.println("TRANS_ID1= " + transID + " oprMode1= " + oprMode);
        if (transID.length() > 0) {
            HashMap map = new HashMap();
            map.put("TRANS_ID", transID);
            if (!(oprMode.equals("AUTHORIZE")) || !(oprMode.equals("REJECT"))) {
                map.put("USER_ID", ProxyParameters.USER_ID);
                System.out.println("USER_ID1= " + ProxyParameters.USER_ID);
            }
            map.put("TRANS_DT", currDt);
            map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            System.out.println("**** value in map****=  " + map);
            ClientUtil.execute("DELETE_SCREEN_LOCK", map);
        }
        
    }

    public boolean editLockValidation(String recordKey, String screenID) {
        boolean lock = false;
        String lockedBy = "";
        HashMap map = new HashMap();
        map.put("SCREEN_ID", screenID);
        map.put("RECORD_KEY", recordKey);
        map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        map.put("CUR_DATE", currDt);
        System.out.println("SCREEN_ID= " + screenID + " RECORD_KEY= " + recordKey);
        System.out.println("CommonConstants.USER_ID= " + CommonConstants.USER_ID + " ProxyParameters.USER_ID= " + ProxyParameters.USER_ID);
        System.out.println("BRANCH_ID" + TrueTransactMain.BRANCH_ID + " CUR_DATE= " + currDt);

        List lstLock = ClientUtil.executeQuery("selectEditLock", map);
        if (lstLock != null && lstLock.size() > 0) {
            System.out.println("***inside if***");
            lockedBy = CommonUtil.convertObjToStr(lstLock.get(0));
        }
        setOpenForEditBy(lockedBy);
        if (lockedBy.equals("")) {
            System.out.println("***before call to ***");
            ClientUtil.execute("insertEditLock", map);
        }
        if (lockedBy.length() > 0 && !lockedBy.equals(ProxyParameters.USER_ID)) {
            String data = getLockDetails(lockedBy, getScreenID());
            ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
            lock = true;
        } else {
            lock = false;
        }

        return lock;
    }

     //Added By Revathi (Release Version : 9.3.1.28)
    public boolean editLockValidationMessage(String recordKey, String screenID) {
        boolean lock = false;
        String lockedBy = "";
        HashMap map = new HashMap();
        map.put("SCREEN_ID", screenID);
        map.put("RECORD_KEY", recordKey);
        map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        map.put("CUR_DATE", currDt);
        List lstLock = ClientUtil.executeQuery("selectEditLock", map);
        if (lstLock != null && lstLock.size() > 0) {
            lockedBy = CommonUtil.convertObjToStr(lstLock.get(0));
            setIsRecord(false);
        } else {
            setIsRecord(true);
        }
        setOpenForEditBy(lockedBy);

        if (lockedBy.equals(ProxyParameters.USER_ID)) {
            setIsRecord(true);
        }
        if (lockedBy.length() > 0 && !lockedBy.equals(ProxyParameters.USER_ID)) {
            //String data = getLockDetails(lockedBy, getScreenID());        //Commented By Suresh R 16-Apr-2015 Referred By Rajesh Sir
            //ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
            ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy);
            lock = true;
        } else {
            lock = false;
        }

        return lock;
    }

    public void removeEditLockCancelTrans(String recordKey, String screenID) {//Added by Ajay for Edit Lock in DepositClosing page on 20-Mar-2014
        if (recordKey.length() > 0) {
            HashMap map = new HashMap();
            map.put("SCREEN_ID", screenID);
            map.put("RECORD_KEY", recordKey);
            map.put("USER_ID", TrueTransactMain.USER_ID);
            map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            map.put("CUR_DATE", currDt);
            System.out.println("SCREEN_ID = " + screenID + " RECORD_KEY =" + recordKey);
            System.out.println(" USER_ID =" + TrueTransactMain.USER_ID);
            System.out.println("BRANCH_ID = " + TrueTransactMain.BRANCH_ID + " CUR_DATE = " + currDt);
            ClientUtil.execute("deleteAllEditLock", map);
        }
        setMode(ClientConstants.ACTIONTYPE_CANCEL);
    }
    
  public void removeEditLockCancelIndTrans(String recordKey, String screenID) {
        if (recordKey.length() > 0) {
            HashMap map = new HashMap();
            map.put("SCREEN_ID", screenID);
            map.put("RECORD_KEY", recordKey);
            map.put("USER_ID", TrueTransactMain.USER_ID);
            map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            map.put("CUR_DATE", currDt);
            System.out.println("map");
            System.out.println("SCREEN_ID = " + screenID + " RECORD_KEY =" + recordKey);
            System.out.println(" USER_ID =" + TrueTransactMain.USER_ID);
            System.out.println("BRANCH_ID = " + TrueTransactMain.BRANCH_ID + " CUR_DATE = " + currDt);
            ClientUtil.execute("deleteEditLock", map);
        }
        setMode(ClientConstants.ACTIONTYPE_CANCEL);
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
    }//Addition ends here by Ajay Sharma for Edit and Screen lock 09-Apr-2014

    public void removeEditLock(String recordKey) {
        if (getMode() == ClientConstants.ACTIONTYPE_EDIT
                || getMode() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            HashMap map = new HashMap();
            map.put("SCREEN_ID", getScreenID());
            map.put("RECORD_KEY", recordKey);
            map.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            map.put("CUR_DATE", currDt);
            if (openForEditBy != null && (openForEditBy.length() > 0)) {
                map.put(CommonConstants.USER_ID, openForEditBy);
            } else {
                map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            }
            System.out.println("deleteEditLock : " + map);
            ClientUtil.execute("deleteEditLock", map);
        }
        setMode(ClientConstants.ACTIONTYPE_CANCEL);
        resetButtonMode(null);
    }

    private void resetButtonMode(java.awt.Container cont) {
        java.awt.Component[] children = null;
        if (cont == null) {
            children = getComponents();
        } else {
            children = cont.getComponents();
        }

        for (int i = 0; i < children.length; i++) {
            if ((children[i] != null)) {
                if (children[i] instanceof CButton) {
                    CButton btn = (CButton) children[i];
                    String btnName = btn.getName();
                    if (btnName != null && (btnName.equals("btnSave") || btnName.equals("btnDelete"))) {
                        System.out.println("save ");
                        btn.setMode(ClientConstants.ACTIONTYPE_CANCEL);
                    }
                } else {
                    resetButtonMode((java.awt.Container) children[i]);
                }
            }
        }
        return;
    }

    /**
     * Getter for property mode.
     *
     * @return Value of property mode.
     */
    public int getMode() {
        return mode;
    }

    /**
     * Setter for property mode.
     *
     * @param mode New value of property mode.
     */
    public void setMode(int mode) {
        this.mode = mode;
        if (mode == ClientConstants.ACTIONTYPE_VIEW_MODE) {
            setModified(false);
            ClientUtil.disableAll(this, true);
        }
    }

    /**
     * Getter for property editLockMap.
     *
     * @return Value of property editLockMap.
     */
    public java.util.HashMap getEditLockMap() {
        return editLockMap;
    }

    /**
     * Setter for property editLockMap.
     *
     * @param editLockMap New value of property editLockMap.
     */
    public void setEditLockMap(java.util.HashMap editLockMap) {
        this.editLockMap = editLockMap;
    }

    /**
     * Getter for property switchEnglish.
     *
     * @return Value of property switchEnglish.
     */
    public boolean isSwitchEnglish() {
        return switchEnglish;
    }

    /**
     * Setter for property switchEnglish.
     *
     * @param switchEnglish New value of property switchEnglish.
     */
    public void setSwitchEnglish(boolean switchEnglish) {
        this.switchEnglish = switchEnglish;
    }
     public boolean isIsRecord() {
        return isRecord;
    }

    public void setIsRecord(boolean isRecord) {
        this.isRecord = isRecord;
    }

}
