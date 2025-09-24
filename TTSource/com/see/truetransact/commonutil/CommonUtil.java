/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CommonUtil.java
 *
 * Created on September 16, 2003, 2:22 PM
 */
package com.see.truetransact.commonutil;

import java.text.DecimalFormat;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.exceptionhashmap.ExceptionHashMap;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CInternalFrame;
import java.awt.BorderLayout;
import java.util.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 *
 * @author balachandar
 */

public class CommonUtil {
        private String integerSeperator;
        private char decimalSeperatorChar; 

    /**
     * Creates a new instance of CommonUtil
     */
    public CommonUtil() {        
    }

    public static void serializeObjWrite(String filePath, HashMap obj) throws Exception {
        java.io.FileOutputStream out;
        out = new java.io.FileOutputStream(filePath);
        java.io.ObjectOutputStream dataOut = new java.io.ObjectOutputStream(out);
        dataOut.writeObject(obj);
        dataOut.flush();
        dataOut.close();
    }

    public static HashMap serializeObjRead(String filePath) throws Exception {
        java.io.FileInputStream fstream = new java.io.FileInputStream(filePath);
        java.io.ObjectInputStream input = new java.io.ObjectInputStream(fstream);
        return (HashMap) input.readObject();
    }

    /**
     * Returns Initial Context
     *
     */
    public static Context getInitialContext() throws NamingException {
        System.out.println("Initialize Context....");
        // For JBoss
        Properties prop = new Properties();
        prop.put(Context.INITIAL_CONTEXT_FACTORY, CommonConstants.INITIAL_CONTEXT_FACTORY);
        prop.put(Context.PROVIDER_URL, CommonConstants.PROVIDER_URL);
        prop.put(Context.URL_PKG_PREFIXES, CommonConstants.URL_PKG_PREFIXES);

        return new InitialContext(prop);

        //        // For Weblogic
        //        weblogic.jndi.Environment env = new weblogic.jndi.Environment();
        //        env.setProviderUrl(CommonConstants.PROVIDER_URL);
        //        env.setInitialContextFactory(CommonConstants.INITIAL_CONTEXT_FACTORY);
        //        return env.getInitialContext();
    }

    /**
     * This method is used for padding the the char before the string. ie. Left
     * Padding.
     *
     * @return java.lang.String (Left Padded String)
     * @param strinput java.lang.String (Original String)
     * @param len int (Max. Length)
     * @param ch char (padding Character)
     */
    public static String lpad(String strInput, int len, char ch) {
        if (strInput != null) {
            StringBuffer strOutput = new StringBuffer();

            // Padding the Character upto the length
            for (int i = 0, j = (len - strInput.trim().length()); i < j; i++) {
                strOutput.append(ch);
            }
            strOutput.append(strInput);
            return strOutput.toString();
        } else {
            return strInput;
        }
    }

    /**
     * convertObjToStr used to check the null value in the object.. if it is
     * null, it will return empty string.
     */
    public static String convertObjToStr(Object obj) {
        String returnStr = "";
        if (obj == null) {
            returnStr = "";
        } else if (obj instanceof Double) {
            DecimalFormat objDecimalFormat = new DecimalFormat();
            objDecimalFormat.applyPattern("################.################");
            objDecimalFormat.setDecimalSeparatorAlwaysShown(false);
            returnStr = objDecimalFormat.format(obj);
        } else if (obj instanceof java.util.Date) {
            returnStr = DateUtil.getStringDate((java.util.Date) obj);
        } else if (obj instanceof String) {
            returnStr = ((String) obj).trim();
        } else {
            returnStr = obj.toString();
        }
        return returnStr;
    }

    /**
     * convertObjToStr used to check the null value in the object.. if it is
     * null, it will return empty string.
     */
    public static Double convertObjToDouble(Object obj) {
        Double returnDouble = new Double("0");
        try {
            if (obj == null) {
                return returnDouble;
            }
            returnDouble = new Double(obj.toString());
        } catch (NumberFormatException nfe) {
            // Skip it...
        } catch (NullPointerException npe) {
            // Skip it...
        }
        return returnDouble;
    }

    /**
     * convertObjToStr used to check the null value in the object.. if it is
     * null, it will return empty string.
     */
    public static int convertObjToInt(Object obj) {
        int returnInt = 0;
        try {
            returnInt = (CommonUtil.convertObjToDouble(obj)).intValue();
        } catch (NullPointerException npe) {
            // Skip it...
        } catch (NumberFormatException nfe) {
            // Skip it...
        }
        return returnInt;
    }

    /**
     * convertObjToStr used to check the null value in the object.. if it is
     * null, it will return empty string.
     */
    public static long convertObjToLong(Object obj) {
        long returnLong = 0;
        try {
            returnLong = Long.parseLong(convertObjToStr(obj));
        } catch (NullPointerException npe) {
            // Skip it...
        } catch (NumberFormatException nfe) {
            // Skip it...
        }
        return returnLong;
    }

    /**
     * gettingProperDateFormat used to set the proper date to be passed to the database.. 
     */
    
    public static Date getProperDate(Date currDt, Date date) {
        if (currDt != null && date != null) {
            Date returnDt = (Date) currDt.clone();
            returnDt.setDate(date.getDate());
            returnDt.setMonth(date.getMonth());
            returnDt.setYear(date.getYear());
            return returnDt;
        }
        return null;
    }

    public static StringBuffer createHTML(List data) {
        return createHTML(data, true, true);
    }

    public static StringBuffer createHTML(List data, boolean createHeader, boolean createFooter) {
        StringBuffer dataFormat = new StringBuffer();
        StringBuffer firstRowData = new StringBuffer();
        if (data != null && data.size() > 0) {
            if (createHeader) {
                dataFormat.append("<html>\n");
                dataFormat.append("\t<head>\n");
                dataFormat.append("\t</head>\n");
                dataFormat.append("\t<body>\n");
            }
            dataFormat.append("\t\t<table cellspacing=1 cellpadding=3 border=1>\n");

            for (int row = 0, rowCount = data.size(); row < rowCount; row++) {
                HashMap dataRow = (HashMap) data.get(row);
                int noOfCols = dataRow.size();
                String colData = "";
                String colName = "";
                Iterator dataCol = dataRow.keySet().iterator();

                while (dataCol.hasNext()) {
                    colData = convertObjToStr(dataCol.next());
                    colName = colData.replace('_', ' ');
                    if (row == 0) {
                        if (colData.equals("TABLE_NAME")) {
                            dataFormat.append("\t\t\t<tr>\n");
                            dataFormat.append("\t\t\t\t<td align=left colspan=");
                            dataFormat.append(noOfCols);
                            dataFormat.append(">&nbsp;&nbsp;&nbsp;<font name=arial size=3 color=#000000><b>");
                            dataFormat.append(dataRow.get(colData));
                            dataFormat.append("</b></font></td>");
                            dataFormat.append("\t\t\t</tr>\n");
                        } else {
                            dataFormat.append("\t\t\t\t<td bgcolor=#FA5858><font name=arial size=3 color=#FFFFFF>");
                            dataFormat.append(colName);

                            firstRowData.append("\t\t\t\t<td><font name=arial size=3 color=#000000>");
                            if (dataRow.get(colData) != null) {
                                firstRowData.append(dataRow.get(colData));
                            } else {
                                firstRowData.append("&nbsp;");
                            }

                            firstRowData.append("</font></td>\n");
                        }
                    } else {

                        if (!colData.equals("TABLE_NAME")) {
                            dataFormat.append("\t\t\t\t<td><font name=arial size=3 color=#000000>");
                            if (dataRow.get(colData) != null) {
                                dataFormat.append(dataRow.get(colData));
                            } else {
                                dataFormat.append("&nbsp;");
                            }
                        }
                    }
                    dataFormat.append("</font></td>\n");
                }

                dataFormat.append("\t\t\t</tr>\n");
                if (firstRowData != null) {
                    dataFormat.append("\t\t\t<tr>\n");
                    dataFormat.append(firstRowData);
                    dataFormat.append("\t\t\t</tr>\n");
                    firstRowData = null;
                }
            }
            dataFormat.append("\t\t</table>\n");
            dataFormat.append("\t\t<br>\n");

            if (createFooter) {
                dataFormat.append("\t</body>\n");
                dataFormat.append("</html>\n");
            }
        }
        return dataFormat;
    }

    public static StringBuffer createHTML(List data, boolean createHeader, boolean createFooter, String errMsg) {
        StringBuffer dataFormat = new StringBuffer();
        if (!errMsg.equals("")) {
            StringBuffer firstRowData = new StringBuffer();
            if (data != null && data.size() > 0) {
                if (createHeader) {
                    dataFormat.append("<html>\n");
                    dataFormat.append("\t<head>\n");
                    dataFormat.append("\t</head>\n");
                    dataFormat.append("\t<body>\n");
                }
                dataFormat.append("\t\t<table cellspacing=1 cellpadding=3 border=1>\n");

                for (int row = 0, rowCount = data.size(); row < rowCount; row++) {
                    HashMap dataRow = (HashMap) data.get(row);
//                    if(!CommonUtil.convertObjToStr(dataRow.get("ERROR_CLASS")).equals("")){
                    ExceptionHashMap objExceptionHashMap = null;
                    String exceptionConstantClass = (String) dataRow.get("ERROR_CLASS");
                    if (exceptionConstantClass != null && exceptionConstantClass.length() > 0) {
                        try {
                            objExceptionHashMap = (ExceptionHashMap) Class.forName(exceptionConstantClass).newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        HashMap exceptionValueHashMap = objExceptionHashMap.getExceptionHashMap();
                        String strData = "";

                        strData = CommonUtil.convertObjToStr(exceptionValueHashMap.get(dataRow.get("ERROR_MSG")));
                        dataRow.put("ERROR_MSG", strData);
//                        dataRow.remove("ERROR_CLASS");
                    }
                    int noOfCols = dataRow.size();
                    String colData = "";
                    String colName = "";
                    Iterator dataCol = dataRow.keySet().iterator();

                    while (dataCol.hasNext()) {
                        colData = convertObjToStr(dataCol.next());
                        colName = colData.replace('_', ' ');
                        if (row == 0) {
                            if (colData.equals("TABLE_NAME")) {
                                dataFormat.append("\t\t\t<tr>\n");
                                dataFormat.append("\t\t\t\t<td align=left colspan=");
                                dataFormat.append(noOfCols);
                                dataFormat.append(">&nbsp;&nbsp;&nbsp;<font name=arial size=3 color=#000000><b>");
                                dataFormat.append(dataRow.get(colData));
                                dataFormat.append("</b></font></td>");
                                dataFormat.append("\t\t\t</tr>\n");
                            } else {
                                dataFormat.append("\t\t\t\t<td bgcolor=#FA5858><font name=arial size=3 color=#FFFFFF>");
                                dataFormat.append(colName);

                                firstRowData.append("\t\t\t\t<td><font name=arial size=3 color=#000000>");
                                if (dataRow.get(colData) != null) {
                                    firstRowData.append(dataRow.get(colData));
                                } else {
                                    firstRowData.append("&nbsp;");
                                }

                                firstRowData.append("</font></td>\n");
                            }
                        } else {

                            if (!colData.equals("TABLE_NAME")) {
                                dataFormat.append("\t\t\t\t<td><font name=arial size=3 color=#000000>");
                                if (dataRow.get(colData) != null) {
                                    dataFormat.append(dataRow.get(colData));
                                } else {
                                    dataFormat.append("&nbsp;");
                                }
                            }
                        }
                        dataFormat.append("</font></td>\n");
                    }

                    dataFormat.append("\t\t\t</tr>\n");
                    if (firstRowData != null) {
                        dataFormat.append("\t\t\t<tr>\n");
                        dataFormat.append(firstRowData);
                        dataFormat.append("\t\t\t</tr>\n");
                        firstRowData = null;
                    }
                }
                dataFormat.append("\t\t</table>\n");
                dataFormat.append("\t\t<br>\n");

                if (createFooter) {
                    dataFormat.append("\t</body>\n");
                    dataFormat.append("</html>\n");
                }
            }

        }
        return dataFormat;
    }

    public static StringBuffer createHTMLNoData() {
        StringBuffer dataFormat = new StringBuffer();
        dataFormat.append("<html>\n");
        dataFormat.append("\t<head>\n");
        dataFormat.append("\t</head>\n");
        dataFormat.append("\t<body>\n");
        dataFormat.append("<br><br><div align=center><font name=arial size=3 color=#000000><b>");
        dataFormat.append("There are no records to display.</b></font<</div>");
        dataFormat.append("\t</body>\n");
        dataFormat.append("</html>\n");

        return dataFormat;
    }
       
    ///Added by kannan
    public  String getFormattedText(String textValue) {
        if (0 != textValue.length()) {
            char lastChar;
            double currData;
            // 10,00,000,00,00,000
            textValue = textValue.replaceAll(integerSeperator, "");
            textValue = textValue.replaceAll(",", ".");

            lastChar = textValue.charAt(textValue.length() - 1);

            currData = Double.parseDouble(textValue);
            if (currData <= -1) {
                currData = 0;
            }

            return formatCrore(String.valueOf(currData));
        }
        return textValue;
    }

    public static String formatCrore(String str) {
//        System.out.println("#$#$# passed string : "+str);
        try {
            if (str != null && str.trim().length() > 0) {
                java.text.DecimalFormat numberFormat = new java.text.DecimalFormat();
                numberFormat.applyPattern("########################0.00");

                double currData = Double.parseDouble(str.replaceAll(",", ""));
                str = numberFormat.format(currData);

                String num = str.substring(0, str.lastIndexOf(".")).replaceAll(",", "");
                String dec = str.substring(str.lastIndexOf("."));

                String sign = "";
                if (num.substring(0, 1).equals("-")) {
                    sign = num.substring(0, 1);
                    num = num.substring(1, num.length());
                }

                char[] chrArr = num.toCharArray();
                StringBuffer fmtStrB = new StringBuffer();

                for (int i = chrArr.length - 1, j = 0, k = 0; i >= 0; i--) {
                    if ((j == 3 && k == 3) || (j == 2 && k == 5) || (j == 2 && k == 7)) {
                        fmtStrB.insert(0, ",");
                        if (k == 7) {
                            k = 0;
                        }
                        j = 0;
                    }
                    j++;
                    k++;

                    fmtStrB.insert(0, chrArr[i]);
                }
                fmtStrB.append(dec);

                str = fmtStrB.toString();

                str = sign + str;

                if (str.equals(".00")) {
                    str = "0";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
    String string;
    String[] a = {"", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",};
    String[] b = {"hundred", "thousand", "lakh", "crore"};
    String[] c = {"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
    String[] d = {"twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};

    public  String convertNumToWord(int number) {
        int c = 1;
        int rm;
         string = "";
        while (number != 0) {
            switch (c) {
                case 1:
                    rm = number % 100;
                    pass(rm);
                    if (number > 100 && number % 100 != 0) {
                        display("and ");
                    }
                    number /= 100;

                    break;

                case 2:
                    rm = number % 10;
                    if (rm != 0) {
                        display(" ");
                        display(b[0]);
                        display(" ");
                        pass(rm);
                    }
                    number /= 10;
                    break;

                case 3:
                    rm = number % 100;
                    if (rm != 0) {
                        display(" ");
                        display(b[1]);
                        display(" ");
                        pass(rm);
                    }
                    number /= 100;
                    break;

                case 4:
                    rm = number % 100;
                    if (rm != 0) {
                        display(" ");
                        display(b[2]);
                        display(" ");
                        pass(rm);
                    }
                    number /= 100;
                    break;

                case 5:
                    rm = number % 100;
                    if (rm != 0) {
                        display(" ");
                        display(b[3]);
                        display(" ");
                        pass(rm);
                    }
                    number /= 100;
                    break;

            }
            c++;
        }

        return string;
    }

    public  String convertNumToWord(double wholenumber) {
        String string = convertNumToWord(((int) wholenumber));
        string += convertNumToWord(((int) (wholenumber - (int) wholenumber)));

        return string;
    }

    private  void pass(int number) {
        int rm, q;
        if (number < 10) {
            display(a[number]);
        }

        if (number > 9 && number < 20) {
            display(c[number - 10]);
        }

        if (number > 19) {
            rm = number % 10;
            if (rm == 0) {
                q = number / 10;
                display(d[q - 2]);
            } else {
                q = number / 10;
                display(a[rm]);
                display(" ");
                display(d[q - 2]);
            }
        }
    }

    private  void display(String s) {
        String t;
        t = string;
        string = s;
        string += t;        
    }

//    ' Function for conversion of a Currency to words
//    ' Parameter - accept a Currency
//    ' Returns the number in words format
//    '*************************************…
    public static String currencyToWord(double wholeNumber) {
        String Temp = "";
        String Rupees = "", Paisa = "";
        int DecimalPlace = 0, iCount = 0;
        String Hundreds = "", Words = "",Tens = "";
        String Place[] = {" Thousand ", "", " Lakh ", "", " Crore ", "", " Arab ", "", " Kharab "};
//    ' Convert MyNumber to a string, trimming extra spaces.
        DecimalFormat formatter = new DecimalFormat("0.00");
        String MyNumber = formatter.format(wholeNumber);
//    String MyNumber = String.valueOf(wholeNumber).trim();

//    ' Find decimal place.
        DecimalPlace = MyNumber.indexOf(".");        
//    ' If we find decimal place...
        if (DecimalPlace > 0) {
//    ' Convert Paisa
            Temp = lpad(MyNumber.substring(DecimalPlace + 1 , MyNumber.length()), 2, '0');
            if (!Temp.equals("00")) {
                Paisa = " and " + ConvertTens(Temp) + " Paisa";
            }
//    ' Strip off paisa from remainder to convert.
            MyNumber = MyNumber.substring(0, DecimalPlace).trim(); // Trim(Left(MyNumber, DecimalPlace - 1));                      
        }
//    ' Convert last 3 digits of MyNumber to ruppees in word.
        if(MyNumber.length()>2){
        Hundreds = ConvertHundreds(MyNumber.substring(MyNumber.length() - 3, MyNumber.length()));
//    ' Strip off last three digits
        MyNumber = MyNumber.substring(0, MyNumber.length() - 3);
        } else if(MyNumber.length()>1){
            Hundreds = ConvertTens(MyNumber.substring(MyNumber.length() - 2, MyNumber.length()));
//    ' Strip off last two digits
            MyNumber = MyNumber.substring(0, MyNumber.length() - 2);
        } else {
            Hundreds = ConvertDigit(MyNumber.substring(MyNumber.length() - 1, MyNumber.length()));
//    ' Strip off last two digits
            MyNumber = MyNumber.substring(0, MyNumber.length() - 1);
        }
        iCount = 0;
        while (!MyNumber.equals("")) {
//    'Strip last two digits      
            if (MyNumber.length() == 1) {
                Temp = MyNumber;
                if (!Temp.equals("0")) {
                    Words = ConvertDigit(Temp) + Place[iCount] + Words;
                }
                MyNumber = MyNumber.substring(0, MyNumber.length() - 1);                
            }else {            
               Temp = MyNumber.substring(MyNumber.length() - 2, MyNumber.length());
               if (!Temp.equals("00")) {
                   Words = ConvertTens(Temp) + Place[iCount] + Words;
               }
                MyNumber = MyNumber.substring(0, MyNumber.length() - 2);                   
            }                            
            iCount = iCount + 2;
        }
//    System.out.println("Rupees " + Words + Hundreds + Paisa + " Only.");
        return "Rupees : " + Words + Hundreds +  Paisa + " Only.";
    }

//    ' Conversion for hundreds
//    '*************************************…
    private static String ConvertHundreds(String MyNumber) {
        String Result = "";

        //    ' Exit if there is nothing to convert.
        if (new Integer(MyNumber).intValue() == 0) {
            return "";
        }

        //    ' Append leading zeros to number.
        MyNumber = lpad(MyNumber, 3, '0');
        
        //    ' Do we have a hundreds place digit to convert?
        if (!MyNumber.substring(0, 1).equals("0")) {
            Result = ConvertDigit(MyNumber.substring(0, 1)) + " Hundreds ";
        } 

        //    ' Do we have a tens place digit to convert?
        if (!MyNumber.substring(1, 2).equals("0")) {
            Result = Result + ConvertTens(MyNumber.substring(1));
        } else {
            //    ' If not, then convert the ones place digit.
            Result = Result + ConvertDigit(MyNumber.substring(2));
        }

        return Result.trim();
    }

//    ' Conversion for tens
//    '*************************************…
    private static String ConvertTens(String MyTens) {
        String Result = "";

        //    ' Is value between 10 and 19?
        if (MyTens.substring(0, 1).equals("1")) {
            switch (new Integer(MyTens).intValue()) {
                case 10:
                    Result = "Ten";
                    break;
                case 11:
                    Result = "Eleven";
                    break;
                case 12:
                    Result = "Twelve";
                    break;
                case 13:
                    Result = "Thirteen";
                    break;
                case 14:
                    Result = "Fourteen";
                    break;
                case 15:
                    Result = "Fifteen";
                    break;
                case 16:
                    Result = "Sixteen";
                    break;
                case 17:
                    Result = "Seventeen";
                    break;
                case 18:
                    Result = "Eighteen";
                    break;
                case 19:
                    Result = "Nineteen";                    
            }      
            return Result;        
        } else {
            //    ' .. otherwise it's between 20 and 99.
            switch (new Integer(MyTens.substring(0, 1)).intValue()) {
                case 2:
                    Result = "Twenty ";
                    break;
                case 3:
                    Result = "Thirty ";
                    break;
                case 4:
                    Result = "Forty ";
                    break;
                case 5:
                    Result = "Fifty ";
                    break;
                case 6:
                    Result = "Sixty ";
                    break;
                case 7:
                    Result = "Seventy ";
                    break;
                case 8:
                    Result = "Eighty ";
                    break;
                case 9:
                    Result = "Ninety ";                    
            }           
        }
        //    ' Convert ones place digit.
        Result = Result + ConvertDigit(MyTens.substring(MyTens.length() - 1, MyTens.length()));

        return Result;
    }

    private static String ConvertDigit(String MyDigit) {
        String ConvertDigit;
        switch (new Integer(MyDigit).intValue()) {
            case 1:
                ConvertDigit = "One";
                break;
            case 2:
                ConvertDigit = "Two";
                break;
            case 3:
                ConvertDigit = "Three";
                break;
            case 4:
                ConvertDigit = "Four";
                break;
            case 5:
                ConvertDigit = "Five";
                break;
            case 6:
                ConvertDigit = "Six";
                break;
            case 7:
                ConvertDigit = "Seven";
                break;
            case 8:
                ConvertDigit = "Eight";
                break;
            case 9:
                ConvertDigit = "Nine";
                break;
            default:
                ConvertDigit = "";
        }
        return ConvertDigit;
    }

//    public static void main(String args[]) {
//        CurrencyValidation cv = new CurrencyValidation();
//        //	  if (args.length>0) {
//        System.out.println("#$#$ Amount in Crore format : " + cv.formatCrore("4567.34"));
////        System.out.println("#$#$ Amount convertNumToWord : " + cv.convertNumToWord(4567.34));
//        System.out.println("#$#$ Amount convertNumToWord : " + cv.currencyToWord(78349));
//        //	  } else {
//        //        	double d = 7832435.55;
//        //        	System.out.println("Amount in words : "+cv.convertNumToWord(new Double(d).intValue()));
//        //	  }
//    }

    /**
     * This method is used for padding the the char before the string. ie. Left
     * Padding.
     *
     * @return java.lang.String (Left Padded String)
     * @param strinput java.lang.String (Original String)
     * @param len int (Max. Length)
     * @param ch char (padding Character)
     */
//    public static String lpad(String strInput, int len, char ch) {
//        if (strInput != null) {
//            StringBuffer strOutput = new StringBuffer();
//
//            // Padding the Character upto the length
//            for (int i = 0, j = (len - strInput.trim().length()); i < j; i++) {
//                strOutput.append(ch);
//            }
//            strOutput.append(strInput);
//            return strOutput.toString();
//        } else {
//            return strInput;
//        }
//    }

    /**
     * convertObjToStr used to check the null value in the object.. if it is
     * null, it will return empty string.
     */
//    public static String convertObjToStr(Object obj) {
//        String returnStr = "";
//        if (obj == null) {
//            returnStr = "";
//        } else if (obj instanceof Double) {
//            DecimalFormat objDecimalFormat = new DecimalFormat();
//            objDecimalFormat.applyPattern("################.################");
//            objDecimalFormat.setDecimalSeparatorAlwaysShown(false);
//            returnStr = objDecimalFormat.format(obj);
//        } else if (obj instanceof java.util.Date) {
//            returnStr = DateUtil.getStringDate((java.util.Date) obj);
//        } else if (obj instanceof String) {
//            returnStr = ((String) obj).trim();
//        } else {
//            returnStr = obj.toString();
//        }
//        return returnStr;
//    }
//
//    /**
//     * convertObjToStr used to check the null value in the object.. if it is
//     * null, it will return empty string.
//     */
//    public static Double convertObjToDouble(Object obj) {
//        Double returnDouble = new Double("0");
//        try {
//            returnDouble = new Double(obj.toString());
//        } catch (NumberFormatException nfe) {
//            // Skip it...
//        } catch (NullPointerException npe) {
//            // Skip it...
//        }
//        return returnDouble;
//    }
//
//    /**
//     * convertObjToStr used to check the null value in the object.. if it is
//     * null, it will return empty string.
//     */
//    public static int convertObjToInt(Object obj) {
//        int returnInt = 0;
//        try {
//            returnInt = (CommonUtil.convertObjToDouble(obj)).intValue();
//        } catch (NullPointerException npe) {
//            // Skip it...
//        } catch (NumberFormatException nfe) {
//            // Skip it...
//        }
//        return returnInt;
//    }
//
//    /**
//     * convertObjToStr used to check the null value in the object.. if it is
//     * null, it will return empty string.
//     */
//    public static long convertObjToLong(Object obj) {
//        long returnLong = 0;
//        try {
//            returnLong = Long.parseLong(convertObjToStr(obj));
//        } catch (NullPointerException npe) {
//            // Skip it...
//        } catch (NumberFormatException nfe) {
//            // Skip it...
//        }
//        return returnLong;
//    }
//
//    public static java.awt.Image convertBlobToImage(java.sql.Blob oracleBlob) {
//        // get the length of the blob
//        try {
//            long length = oracleBlob.length();
//
//            // print the length of the blob
//            System.out.println("photo blob length " + length);
//            if (length != 0) {
//                java.io.InputStream in = oracleBlob.getBinaryStream();
//                //                fileLength = oracleBlob.getBufferSize();
//                int fileLength = (int) length;
//                byte[] blobBytes = new byte[fileLength];
//                System.out.println("blobBytes.length " + blobBytes.length);
//                in.read(blobBytes);
//                in.close();
//                return new javax.swing.ImageIcon(blobBytes).getImage();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new javax.swing.ImageIcon().getImage();
//    }
 
// This method is used to add progress bar , added by shihad on 26/08/2014    
	public JDialog addProgressBar() {
        JDialog loading = new JDialog();
        JPanel p1 = new JPanel(new BorderLayout());
        Icon icon = new ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/loader.gif"));
        JLabel lab = new JLabel("Please wait...", icon, JLabel.CENTER);
        p1.add(lab, BorderLayout.CENTER);
        loading.setUndecorated(true);
        loading.getContentPane().add(p1);
        loading.pack();
        loading.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loading.setModal(true);
        loading.setLocationRelativeTo(loading.getParent());
        return loading;
	}

	public static void main(String str[]) {
        Double dbl = null;
        System.out.println(CommonUtil.convertObjToDouble(dbl));         
    }
}