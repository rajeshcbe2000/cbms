/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * TransferBean.java
 *
 * Created on June 23, 2003, 4:20 PM
 */

package com.see.truetransact.serverside.rtgsneftfiletrans.upi;

import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAOImpl;
import java.util.HashMap;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * TransferBean which calls TransferDAO
 *
 * @author  Pranav
 * @modified Pinky
 */

public class UPIQRCodeBean implements SessionBean,TTDAOImpl {
    
    private UPIQRCodeDAO objUPIQRCodeDAO = null;
    
    public void ejbCreate() throws CreateException,ServiceLocatorException {
        objUPIQRCodeDAO = new UPIQRCodeDAO();
    }
    
    public void ejbRemove()  {
        objUPIQRCodeDAO = null;
    }
    
    public void setSessionContext(SessionContext sess) {
    }
    
    public void ejbActivate() {
    }
    
    public void ejbPassivate(){
    }
    
    public HashMap execute(HashMap obj) throws Exception {
        return objUPIQRCodeDAO.execute(obj);
    }
    
    public HashMap executeQuery(HashMap obj) throws Exception {
        return objUPIQRCodeDAO.executeQuery(obj);
    }
}
