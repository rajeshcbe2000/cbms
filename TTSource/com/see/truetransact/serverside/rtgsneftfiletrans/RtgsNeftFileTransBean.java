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

package com.see.truetransact.serverside.rtgsneftfiletrans;

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

public class RtgsNeftFileTransBean implements SessionBean,TTDAOImpl {
    
    private RtgsNeftFileTransDAO rtgsNeftFileTransDAO = null;
    
    public void ejbCreate() throws CreateException,ServiceLocatorException {
        rtgsNeftFileTransDAO = new RtgsNeftFileTransDAO();
    }
    
    public void ejbRemove()  {
        rtgsNeftFileTransDAO = null;
    }
    
    public void setSessionContext(SessionContext sess) {
    }
    
    public void ejbActivate() {
    }
    
    public void ejbPassivate(){
    }
    
    public HashMap execute(HashMap obj) throws Exception {
        return rtgsNeftFileTransDAO.execute(obj);
    }
    
    public HashMap executeQuery(HashMap obj) throws Exception {
        return rtgsNeftFileTransDAO.executeQuery(obj);
    }
}
