/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TransRefGLDAO.java
 *
 * Created on April 2, 2004, 11:00 AM
 */
package com.see.truetransact.serverside.transaction.common.product.gl;

import java.util.HashMap;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.transferobject.transaction.common.product.gl.GLTO;
import com.see.truetransact.transferobject.transaction.common.product.gl.TransRefGLTO;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import java.util.Date;

/**
 *
 * @author bala
 */
public class TransRefGLDAO {

    private SqlMap sqlMap;
    private Date currDt = null;
    /**
     * Creates a new instance of GLUpdateDAO
     */
    public TransRefGLDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public void insertRefGL(GLTO objGLTO, HashMap inputMap) throws Exception {
        System.out.println("insertRefGL inputMap : " + inputMap+" objGLTO : "+objGLTO);
        currDt = ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(inputMap.get("BRANCH_CODE")));
        Double amt = new Double(Math.abs(CommonUtil.convertObjToDouble(objGLTO.getCurBal()).doubleValue()));
        TransRefGLTO objTransRefGLTO = new TransRefGLTO();
        objTransRefGLTO.setAcHdId(objGLTO.getAcHdId());
        objTransRefGLTO.setActNum(null);
        objTransRefGLTO.setAmount(amt);
        objTransRefGLTO.setBranchId(objGLTO.getBranchCode());
        objTransRefGLTO.setInitChannType(null);
        objTransRefGLTO.setInitTransId(null);
        objTransRefGLTO.setInpAmount(amt);
        objTransRefGLTO.setInpCurr(null);
        objTransRefGLTO.setInstDt(setProperDtFormat((java.util.Date) inputMap.get(TransactionDAOConstants.DATE)));
        objTransRefGLTO.setInstrumentNo1(CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.INSTRUMENT_1)));
        objTransRefGLTO.setInstrumentNo2(CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.INSTRUMENT_2)));
        objTransRefGLTO.setInstType(CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.INSTRUMENT_TYPE)));
        //interbranch code
        if (inputMap.containsKey("INTER_BRANCH_TRANS")) {
            objTransRefGLTO.setParticulars(CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.PARTICULARS)) + " - Inter Branch Transaction A/C No : " + CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.ACCT_NO)));
        } else {
            objTransRefGLTO.setParticulars(CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.PARTICULARS)) + " - A/C No : " + CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.ACCT_NO)));
        }
        //end
        objTransRefGLTO.setProdId(null);
        objTransRefGLTO.setProdType(TransactionFactory.GL);
        objTransRefGLTO.setStatus(CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.TO_STATUS)));
        objTransRefGLTO.setStatusDt((java.util.Date) currDt.clone());
        objTransRefGLTO.setTransDt((java.util.Date) inputMap.get(TransactionDAOConstants.TODAY_DT));
        //interbranch code
        if (inputMap.containsKey("OUTWARD_CLEARING")) {
            objTransRefGLTO.setBatchId(CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.TRANS_ID)));
            objTransRefGLTO.setTransId(CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.BATCH_ID)));
        } else {
            objTransRefGLTO.setTransId(CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.TRANS_ID)));
        }
        if (null != objGLTO.getBranchCode() && null != objGLTO.getInitiatedBranch() && !objGLTO.getBranchCode().equals(objGLTO.getInitiatedBranch())) {
            objTransRefGLTO.setTransMode(TransactionDAOConstants.TRANSFER);
        } else {
            objTransRefGLTO.setTransMode(CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.TRANS_MODE)));
        }
        if ( objGLTO.getIbrHierarchy() != null && objGLTO.getIbrHierarchy().length()> 0 ) {
            objTransRefGLTO.setIbrHierarchy(objGLTO.getIbrHierarchy());
        } else {
            objTransRefGLTO.setIbrHierarchy(null);
        }
        objTransRefGLTO.setTransType(CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.TRANS_TYPE)));
        objTransRefGLTO.setInitiatedBranch(objGLTO.getInitiatedBranch());
        if(!inputMap.containsKey("INTER_BRANCH_ROLL_BACK_TRANSACTION") && objTransRefGLTO.getBranchId() != null && objTransRefGLTO.getBranchId().length()>0 && objTransRefGLTO.getInitiatedBranch()!=null && objTransRefGLTO.getInitiatedBranch().length()>0){
            sqlMap.executeUpdate("insertTransRefGL", objTransRefGLTO);
        }
    }
      private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    public static void main(String str[]) {
        try {
            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
