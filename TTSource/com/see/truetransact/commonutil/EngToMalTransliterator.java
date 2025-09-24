/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * English to Malayalam Transliterator
 */
package com.see.truetransact.commonutil;
import com.see.truetransact.ui.common.*;

import java.util.Arrays;

/**
 * Author   : Rajesh
 * Company  : Fincuro Solutions Pvt. Ltd.
 * Location : Bangalore
 * Date of Completion : 17-04-2015
 */
public class EngToMalTransliterator {
    
    static String replace2[]={"aa","ee","oo","ai","ou"};
    static String replace2new[]={"A","^","U","I","<"};
    static String replace3[]={" aa"," ee"," oo"," ou"," am"};
    static String replace3new[]={" A"," ^"," U"," <"," `"};
    static String vow[]={"a","A","i","^","u","U","~","e","E","I","o","O","<"};
    static String vowml[]={"A","B","C","CU","D","Du","E","F","G","sF","H","Hm","Hu","Aw"};
    static String cons[]={"k","K","g","G","c","C","j","J","t","T","d","D","N","n","p","P","b","B","m","y","r","l","v","S","s","h","L","Z","R","f","M",".",",","'","\"",";",":","?","!","/","-","_","+","=","(",")","&","%","@","$","1","2","3","4","5","6","7","8","9","0"};
    static String consml[]={"I","J","K","L","N","O","P","Q","S","T","Z","U","W","\\","]","^","_","`","a","b","c","e","h","i","k","l","f","g","d","^","½",".",",","'","\"",";",":","?","!","ഽ","-","_","+","=","(",")","&","%","@","ഃ","൧","h","k","À","൫","¶q","൭","{h","³","w"};
    static String symbols[]={"a","A","i","^","u","U","~","e","E","I","o","O","<","`","\\"};
    static String symml[]={"","m","n","o","p","q","r","s","t","ss","sm","tm","u","w","v"};
    static String hchar[]={"k","g","N","c","j","n","t","T","d","D","p","b","s","z"};
    static String hcharml[]={"J","L","M","N","Q","R","X","Y","[","V","^","`","j","g"};

    static String special1[] = {"n", "n", "m"};
    static String special2[] = {"g", "j", "p"};
    static String special3[] = {"M", "R", "¼"};
        
    public static String get_ml(String src) {
        char en[] = ("|" + src).toCharArray();
        String ml = "";
        int n = 0;
        boolean disabled = false, done = false;

	while(n < en.length)
	{
		char prv_ch=0, nxt_ch=0, later_ch=0;
		char ch = en[n];

		if(ch == '{')
		{
			disabled = true;
			ch = 0;
		}

		if(ch == '}') disabled = false;

		if(disabled) // {english}
		{
			n++;
		}
		else
		{
			if(n > 0)                prv_ch = en[n - 1];
			if(n < (en.length - 1)) nxt_ch = en[n + 1];
			if(n < (en.length - 2)) later_ch = en[n + 2];

			for(int i=0; i<replace3.length; i++)
			{
				if(replace3[i].equals("" + ch + nxt_ch + later_ch))
				{
//                                    en = (Arrays.deepToString(Arrays.copyOfRange(en, 0, n+1)) + replace3new[i] +
//                                            Arrays.deepToString(Arrays.copyOfRange(en, n+3, en.length)));
                                    for (int j=0; j<en.length; j++) {
                                        System.out.print(en[j]);
                                    }
                                    char test[] = Arrays.copyOfRange(en, 0, n+1);
                                    for (int j=0; j<test.length; j++) {
                                        System.out.print(test[j]);
                                    }
//					en = en.slice(0, n+1) + replace3new[i] + en.slice(n+3, en.length);
				}
			}

			for(int i=0; i<replace2.length; i++)
			{
				if(replace2[i].equals(nxt_ch +""+ later_ch))
				{
                                    char test[] = Arrays.copyOfRange(en, 0, n+1);
                                    String testStr = "";
//                                    System.out.print("inside replace2:");
                                    for (int j=0; j<test.length; j++) {
                                        System.out.print(test[j]);
                                        testStr+=test[j];
                                    }
                                    System.out.println(":");
                                    testStr+=replace2new[i];
                                    test = Arrays.copyOfRange(en, n+3, en.length);
                                    for (int j=0; j<test.length; j++) {
                                        testStr+=test[j];
                                    }
//                                    System.out.println("testStr:"+testStr);
                                    en = testStr.toCharArray();
//					en = en.slice(0, n+1) + replace2new[i] + en.slice(n+3, en.length);
				}
			}

			done = false;
			n++;
		}
	}

	n = 0;
	disabled = false;

	while(n < en.length)
	{
		char prv_ch = 0, nxt_ch = 0, later_ch = 0;
		char ch = en[n];

		if(ch == '{')
		{
			disabled = true;
			ch = 0;
		}

		if(ch == '}') disabled = false;
		
		if(disabled) // {english}
		{
			ml += ch;
			n++;
		}
		else
		{
			if(n > 0)                prv_ch = en[n - 1];
			if(n < (en.length - 1)) nxt_ch = en[n + 1];
			if(n < (en.length - 2)) later_ch = en[n + 2];


			done = false;

			if(ch == '\n')
			{
//				en = en.slice(0, n) + "|\n|" + en.slice(n+1, en.length);
                                char test[] = Arrays.copyOfRange(en, 0, n);
                                String testStr = "";
                                for (int j=0; j<test.length; j++) {
//                                    System.out.print(test[j]);
                                    testStr+=test[j];
                                }
                                System.out.println(":");
                                testStr+="|\n|";
                                test = Arrays.copyOfRange(en, n+1, en.length);
                                for (int j=0; j<test.length; j++) {
                                    testStr+=test[j];
                                }
//                                System.out.println("testStr inside ch == '\\n':"+testStr);
                                en = testStr.toCharArray();                                
				ml += "\n";
				n++;
			}

			if(done == false)
			{
				if(ch == ' ' || ch == '|')
				{
					String connector = "";
					if(ch == ' ') connector="";

					for(int vi=0; vi<vow.length; vi++)
					{
						if(vow[vi].equals(""+nxt_ch))
						{
							ml += (connector + vowml[vi]);
							done = true;
							n += 2;
						}
					}

					if(done == false)
					{
						ml += connector;
						done = true;
						n++;
					}

				}
			}

	// Some special cases  
			for(int i=0; i<special1.length; i++)
			{
				if(done == false && (ch+"").equals(special1[i]) && (nxt_ch+"").equals(special2[i]))
				{
					String buff = "v";
					for(int si=0; si<symbols.length; si++)
					{
						if(symbols[si].equals(later_ch+"")) buff = symml[si];
					}
					ml += (special3[i] + buff);
					n += 2;
					done = true;
				}
			}

	// m
			if(done == false && ch == 'm')
			{					
				if(nxt_ch != 'm' && nxt_ch != 'a' && nxt_ch != 'A' && nxt_ch != 'i' && nxt_ch != '^' && nxt_ch != 'u' && nxt_ch != 'U' && nxt_ch != '~' && nxt_ch != 'e' && nxt_ch != 'E' && nxt_ch != 'I' && nxt_ch != 'o' && nxt_ch != 'O' && nxt_ch != '<')
				{
					String buff="v";

					for(int si=0; si<symbols.length; si++)
					{
						if(symbols[si].equals(prv_ch+"")) buff = symml[si];
					}

					if(!buff.equals("v"))
					{
						ml += "w";
						n++;
						done = true;
					}
					else
					{
						done = false;
					}
				}
			}

	// h
			if(done == false && nxt_ch == 'h')
			{
				for(int hi=0; hi<hchar.length; hi++)
				{
					if(hchar[hi].equals(ch+""))
					{
						String buff = "v";

						for(int si=0; si<symbols.length; si++)
						{
							if(symbols[si].equals(later_ch+"")) buff = symml[si];
						}

						ml += (hcharml[hi] + buff);
						n += 2;
						done = true;
					}
				}
			}

	// not h
			if(done == false && nxt_ch != 'h')
			{	
                            String buff = "";
				for(int ci=0; ci<cons.length; ci++)
				{
					if(cons[ci].equals(ch+""))
					{

						if(ci < 30)
						{
							buff="v"; // items after 37 are with 'a' in built.
						}
						else
						{
							buff="";
						}

//						symn = 0;

						for(int si=0; si<symbols.length; si++)
						{
							if(symbols[si].equals(nxt_ch+"")) buff = symml[si];
						}
	
						ml += (consml[ci] + buff);
					}
				}

				n++;
				done = true;
			}

			if(done == false)
			{
				n++; // Not a good way! DONE shouldn't be FALSE here.
			}
		}

        }
//        System.out.println(":");
//        for (int j=0; j<en.length; j++) {
//            System.out.print(en[j]);
//        }
//        System.out.println(":");
//        System.out.println();
        System.out.println("In malayalan:"+ml);
        String splt1[] = ml.split("sm");
        if(splt1.length>0){
            String fulch ="";
            for(int t=0;t<splt1.length-1;t++){
                String s = splt1[t];
                System.out.println("string 1 :"+s);
                 System.out.println("string length :"+s.length());
                 String cu = "";
                 if(s.length()>0){
                     
                if(s.length()>1){
                 cu = s.substring(0,s.length()-1);
                 }
                cu=cu+"s"+s.charAt(s.length()-1)+"m";
                fulch=fulch+cu;
                 }
            }
            if(fulch.length()>0){
              ml = fulch+splt1[splt1.length-1];  
            }
            System.out.println("ml------- "+ml);
        }
        String splt2[] = ml.split("ss");
        if(splt2.length>0){
            String fulch ="";
            for(int t=0;t<splt2.length-1;t++){
                String s = splt2[t];
                System.out.println("string 1 :"+s);
                 System.out.println("string length :"+s.length());
                 String cu = "";
                 if(s.length()>0){
                     
                if(s.length()>1){
                 cu = s.substring(0,s.length()-1);
                 }
                cu=cu+"ss"+s.charAt(s.length()-1);
                fulch=fulch+cu;
                 }
            }
            if(fulch.length()>0){
              ml = fulch+splt2[splt2.length-1];  
            }
        }
        String splt3[] = ml.split("tm");
        if(splt3.length>0){
            String fulch ="";
            for(int t=0;t<splt3.length-1;t++){
                String s = splt3[t];
                System.out.println("string 1 :"+s);
                 System.out.println("string length :"+s.length());
                 String cu = "";
                 if(s.length()>0){
                     
                if(s.length()>1){
                 cu = s.substring(0,s.length()-1);
                 }
                cu=cu+"t"+s.charAt(s.length()-1)+"m";
                fulch=fulch+cu;
                 }
            }
            if(fulch.length()>0){
              ml = fulch+splt3[splt3.length-1];  
            }
        }
        return ml;
	}

    
    public static void main (String a[]) {
        String mal = EngToMalTransliterator.get_ml("naadu");
        System.out.println("final output:"+mal);
    }
        
}
