/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * CheckForRights.java
 *
 * Created on March 28, 2007, 6:34 PM
 */

package com.see.truetransact.commonutil;

import java.util.HashMap;
import java.util.ArrayList;
import java.awt.Container;
import java.awt.Component;
import javax.swing.JToolBar;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.CButton;

/**
 *
 * @author  Rajesh
 */
public class CheckForRights {
    private static boolean newAllow=false, edtAllow=false,delAllow=false;
    private static boolean authAllow=false,exceptAllow=false,printAllow=false;
    private static boolean interbranchAllow=false;
    private static ArrayList components=null;
    private static int componentsSize = 0;
    /** Creates a new instance of CheckForRights */
    public static boolean checkForRights(CInternalFrame parent) {
        HashMap hash = new HashMap();
        hash = parent.getScreenConfig();
//        System.out.println("#### Hash : "+hash);
        if(hash.get("newAllowed")!=null)
            newAllow=(((String)hash.get("newAllowed")).equals("Y") ? true : false);
        if(hash.get("editAllowed")!=null)
            edtAllow=(((String)hash.get("editAllowed")).equals("Y") ? true : false);
        if(hash.get("deleteAllowed")!=null)
            delAllow=(((String)hash.get("deleteAllowed")).equals("Y") ? true : false);
        if(hash.get("authRejAllowed")!=null)
            authAllow=(((String)hash.get("authRejAllowed")).equals("Y") ? true : false);
        if(hash.get("exceptionAllowed")!=null)
            exceptAllow=(((String)hash.get("exceptionAllowed")).equals("Y") ? true : false);
        if(hash.get("printAllowed")!=null)
            printAllow=(((String)hash.get("printAllowed")).equals("Y") ? true : false);
        if(hash.get("interbranchAllowed")!=null)
            interbranchAllow=(((String)hash.get("interbranchAllowed")).equals("Y") ? true : false);
//        makeVisibleOrNot(parent);
        components = findComponentList(parent, null);
        componentsSize = components.size();
//        System.out.println("#### Components size : "+componentsSize);
        makeVisibleOrNot();
        return interbranchAllow;
    }

    
    private static ArrayList findComponentList(Container cont, ArrayList outList) {
        if (outList == null) {
            outList = new ArrayList();
        }
        if (cont == null ) {
            return outList;
        }
        
        Component children[] = cont.getComponents();
        for (int i = 0; i < children.length; i++) {
            if ( !(children[i] instanceof CButton)) {
                findComponentList((Container) children[i], outList);
            }
            else{
                outList.add(children[i]);
            }
        }
        return outList;
    }
    
    private static void makeVisibleOrNot() {
        for (int i = 0; i < componentsSize; i++) {
            if ((components.get(i) != null)) {
                if (components.get(i) instanceof CButton) {
                    //System.out.println("#### Button getToolTipText : " + ((CButton)components.get(i)).getToolTipText());
                    CButton btn = (CButton) components.get(i);
                    String btnName = btn.getName();
                    String btnToolTip = btn.getToolTipText();
                    if (btn.isVisible()) {
                        if (btnName != null && (btnName.equals("btnNew")) || btnToolTip != null && (btnToolTip.equals("New"))) {
                            btn.setVisible(newAllow);
                        }
                        if (btnName != null && (btnName.equals("btnEdit")) || btnToolTip != null && (btnToolTip.equals("Edit"))) {
                            btn.setVisible(edtAllow);
                        }
                        if (btnName != null && (btnName.equals("btnDelete")) || btnToolTip != null && (btnToolTip.equals("Delete"))) {
                            btn.setVisible(delAllow);
                        }
                        if (btnName != null && (btnName.equals("btnAuthorize")) || btnToolTip != null && (btnToolTip.equals("Authorize"))) {
                            btn.setVisible(authAllow);
                        }
                        if (btnName != null && (btnName.equals("btnReject")) || btnToolTip != null && (btnToolTip.equals("Reject"))) {
                            btn.setVisible(authAllow);
                        }
                        if (btnName != null && (btnName.equals("btnException")) || btnToolTip != null && (btnToolTip.equals("Exception"))) {
    //                        System.out.println("#### exceptAllow : "+exceptAllow);
                            btn.setVisible(exceptAllow);
    //                        System.out.println("#### btnException.isVisible() : "+btn.isVisible());
                        }
                        if (btnName != null && (btnName.equals("btnPrint")) || btnToolTip != null && (btnToolTip.equals("Print"))) {
    //                        System.out.println("#### printAllow : "+printAllow);
                            btn.setVisible(printAllow);
    //                        System.out.println("#### btnprint.isVisible() : "+btn.isVisible());
                        }
                    }
                } else {
//                    System.out.println("#### Else part...");
                }
            }
        }
    }
    
}
