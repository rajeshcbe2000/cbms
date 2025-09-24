/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ExceptionResources.java
 *
 * Created on July 10, 2003, 5:43 PM
 */

package com.see.truetransact.clientexception;

import java.util.ListResourceBundle;
import com.see.truetransact.commonutil.CommonConstants;

/** ExceptionResources is a resource bundle for getting appropriate messages based
 * on the exception. This message will be displayed for the user as an alert from
 * some other class
 * @author karthik
 */
public class ClientExceptionRB extends ListResourceBundle{
    
    static final Object[][] contents = {
              {"com.see.truetransact.clientproxy.ClientProxyException", "Contact Administrator!!!"},
              {"com.see.truetransact.clientexception.ComboBoxModelException", "Contact Administrator!!!"},
              {"com.see.truetransact.commonutil.NoCommandException", "Contact Administrator!!!"},
              {"com.see.truetransact.commonutil.ParseException", "Contact Administrator!!!"},
              {"com.see.truetransact.commonutil.TTException", "Contact Administrator!!!"},
              {"com.see.truetransact.serverexception.AuthorizationException", "Contact Administrator!!!"},
              {"com.see.truetransact.serverexception.ServerParseException", "Contact Administrator!!!"},
              {"com.see.truetransact.serverexception.ServiceLocatorException", "Contact Administrator!!!"},
              {"com.see.truetransact.serverexception.TransRollbackException", "Contact Administrator!!!"},
              {CommonConstants.OTHEREXCEPTION, "Contact Administrator!!!"}
      };

    /** Creates a new instance of ExceptionResources */
      public ClientExceptionRB() {
    }
    
    /** To get all the contents
     * @return To return contents
     */    
    public Object[][] getContents() {
        return contents;
    }
    
}
