/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PaymentVoucherBean.java
 * 
 * Created on Wed Feb 02 13:11:28 IST 2015
 */
package com.see.truetransact.serverside.payroll.voucherprocessing;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;
import java.util.HashMap;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * PaymentVoucher Bean which calls the DAO.
 *
 */
public class PaymentVoucherBean implements SessionBean, TTDAOImpl {

    PaymentVoucherDAO daoPaymentVoucher = null;

    public void ejbCreate() throws CreateException, ServiceLocatorException {
        daoPaymentVoucher = new PaymentVoucherDAO();
    }

    public void ejbRemove() {
        daoPaymentVoucher = null;
    }

    public void setSessionContext(SessionContext sess) {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public HashMap execute(HashMap obj) throws Exception {
        return daoPaymentVoucher.execute(obj);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return daoPaymentVoucher.executeQuery(obj);
    }
}
