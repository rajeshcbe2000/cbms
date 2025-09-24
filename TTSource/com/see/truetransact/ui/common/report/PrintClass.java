/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * PrintClass.java
 *
 * Created on May 25, 2004, 5:18 PM
 */

package com.see.truetransact.ui.common.report;

import com.see.iie.tools.useradmin.util.ClientConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author TOSHIBA
 */
public class PrintClass {
    
    public void displayTransDetail(HashMap proxyResultMap,int action,Date editDate)
    {
      
       try
        {
        System.out.println("@#$@@$@@@$ proxyResultMap : " + proxyResultMap);
        System.out.println("@#$@@$@@@$ Actionnnnnnnnn : " + action);
        System.out.println("@#$@@$@@@$ dateeeeeeeeeee : " + editDate);
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transType = "";
        Object keys[] = proxyResultMap.keySet().toArray();
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        ArrayList crList=new ArrayList();
        ArrayList drList=new ArrayList();
        
        for (int i = 0; i < keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List) proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("TRANS_ID");
                    }
                    cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        cashDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                        cashDisplayStr += "   Account Head : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                }
       
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER")!=-1) {
                for (int j=0; j<tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j==0) {
                        transId = (String)transMap.get("MODULE_NO_CONFIGMODULE_NO_CONFIGMODULE_NO_CONFIGMODULE_NO_CONFIGMODULE_NO_CONFIGMODULE_NO_CONFIGMODULE_NO_CONFIG");
                    }
                    transferDisplayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                    "   Batch Id : "+transMap.get("BATCH_ID")+
                    "   Trans Type : "+transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        transferDisplayStr +="   Account No : "+transMap.get("ACT_NUM")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }else{
                        transferDisplayStr += "   Account Head : "+transMap.get("AC_HD_ID")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }
                }
                transferCount++;
            }
        }
        if (cashCount > 0) {
            displayStr += cashDisplayStr;
        }
        if (transferCount > 0) {
            displayStr += transferDisplayStr;
        }
        if (!displayStr.equals("")) {
            ClientUtil.showMessageWindow("" + displayStr);
        }

        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE, COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        System.out.println("#$#$$ yesNo : " + yesNo);
        if (yesNo == 0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
           // paramMap.put("TransId", transId);
            paramMap.put("TransDt", ClientUtil.getCurrentDateProperFormat());
            System.out.println("ClientUtil.getCurrentDateProperFormat()"+ClientUtil.getCurrentDateProperFormat());
            if (action==ClientConstants.ACTIONTYPE_EDIT) {
               System.out.println("editttt" + action);
              paramMap.put("TransDt",editDate);
              System.out.println("datee" + editDate);
            }

            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            
            
             for (int i = 0; i < keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List) proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {

                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    
                    if(transMap.get("TRANS_TYPE").equals("DEBIT"))
                    {
                        drList.add(tempList.get(j));
                       
                    }else
                    {
                        crList.add(tempList.get(j));
                    }
                       
                    }
                }
             else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("SINGLE_TRANS_ID");
                         paramMap.put("TransId",transId);
                    }

                    break;
                }
             }
                
            }
             System.out.println("crList==="+crList);
             for(int i=0;i<crList.size();i++)
             {
                 HashMap mapCr=(HashMap)crList.get(i);
                 if(i==0)
                 {
                   paramMap.put("FromTransIdCr",mapCr.get("SINGLE_TRANS_ID"));  
                 }
                 else
                 {
                     paramMap.put("ToTransIdCr",mapCr.get("SINGLE_TRANS_ID"));  
                  }
                  
             }
             if(!paramMap.containsKey("ToTransIdCr"))
                     {
                        paramMap.put("ToTransIdCr",paramMap.get("FromTransIdCr")); 
                     }
             System.out.println("drList==="+drList);
              for(int i=0;i<drList.size();i++)
             {
                 HashMap mapDr=(HashMap)drList.get(i);
                 if(i==0)
                 {
                   paramMap.put("FromTransIdDr",mapDr.get("SINGLE_TRANS_ID"));  
                 }
                 else
                 {
                     paramMap.put("ToTransIdDr",mapDr.get("SINGLE_TRANS_ID")); 
                  }
                  
             }
             if(!paramMap.containsKey("ToTransIdDr"))
                     {
                        paramMap.put("ToTransIdDr",paramMap.get("FromTransIdDr"));  
                     }
                   
            System.out.println("paramMap==="+paramMap);
            ttIntgration.setParam(paramMap);
            String reportName = "";

            if (transferCount > 0) {
                reportName = "IndentReceiptPayment";
            } 
            if(drList.size()>0)
            {
                reportName = "IndentCashPayment";
            }
            

          ttIntgration.integrationForPrint(reportName, false);
          if(crList.size()>0)
            {
               reportName = "IndentCashReceipt"; 
            }
          ttIntgration.integrationForPrint(reportName, false);
          
        }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
