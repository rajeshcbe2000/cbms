/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ZeroBalanceAccountCheck.java
 *
 * Created on March 10, 2005, 10:40 AM
 */

package com.see.truetransact.ui.batchprocess.gl;

import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil ;
import com.see.truetransact.commonutil.DateUtil ;
import com.see.truetransact.commonutil.TTException ;
import com.see.truetransact.clientutil.ClientUtil ;
import java.util.HashMap;
import java.util.List;
import com.see.truetransact.ui.TrueTransactMain ;
/**
 *
 * @author  Sunil (152691)
 */
public class ZeroBalanceAccountCheck{
    private String branch = null ;      
    private String BRANCH_CODE = "BRANCH_CODE";
    /** Creates a new instance of ZeroBalanceAccountCheck */
    public ZeroBalanceAccountCheck() throws Exception {
    }
    
    public String executeTask() throws Exception {
        HashMap paramMap = new HashMap();
        paramMap.put(BRANCH_CODE, TrueTransactMain.BRANCH_ID);
        List output = ClientUtil.executeQuery("getZeroBalAccountHeads", paramMap) ;
        paramMap = null ;
        StringBuffer out = CommonUtil.createHTML(output) ;
        if (out.toString().length() == 0 ) {
            out = CommonUtil.createHTMLNoData();
        }
        return out.toString() ;
    }
    
  
    public static void main(String args[]){
        try{
            ZeroBalanceAccountCheck ft = new ZeroBalanceAccountCheck();
            System.out.println (ft.executeTask());
        }catch(Exception E){
            E.printStackTrace();
        }
    }
}
