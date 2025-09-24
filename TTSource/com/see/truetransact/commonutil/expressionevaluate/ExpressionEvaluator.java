/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ExpressionEvaluator.java
 *
 * Created on September 17, 2004, 12:07 PM
 */

package com.see.truetransact.commonutil.expressionevaluate;

//package com.see.iie.utils;

/***************************** ExpressionEvaluator *****************************************
 * The class ExpressionEvaluator can be used for differt things. Either you will transform
 * an infix form to a postfix form or you like to calculate a postfix format.
 * The following calculation methods are possible:
 * - Subtraction "-"
 * - Addition "+"
 * - Multiplication "-"
 * - Division "/"
 * - Calculation with power "^"
 * - also brackets "(" ")", "[" "]", "{" "}".
 * - ! 'll be treated as binary operator instead of unrary operator. Before passing the Expression !Exp
 *   to the Expression Evaluator it'll be converted to Exp!true. 
 *   
 * ****************************************************************************/

import java.util.Stack;
import java.util.HashMap;

public class ExpressionEvaluator {
    
    // This is the amount of the valid digits which can be used for the calculation
    private static String digits = "0123456789";
    // In this line you see the legal operators for calculation
    private static String operators = "+-*/^%<>=&|@#$!";
    /*
     * Following operators stands for 
     * & -> &&, | -> ||, = -> ==, @ -> <=,  # -> >=,  $ -> !=.
     */
    // This is the default constructor, the class can not be instantiated
    private ExpressionEvaluator() {
    }
    
    /******************************************************************************/
    
    /* This Method does our calculation.
     * You have to assign two number and the operator (of type char)
     * It uses the form "operand operator operand" to evaluate.
     */
    private static Object calculate(Object operand1, Object operand2, Object op) throws ExpressionEvaluatorException {
        // so what are we calculating? first we decide which operator we got
        boolean opOneNULL = false;
        boolean opTwoNULL = false;
        Double actOp1 = new Double("0");
        Double actOp2 = new Double("0");
        
        try {
            actOp1 = new Double((String)operand1);
        } catch (Exception e) {
            opOneNULL = true;
        }
        try {
            actOp2 = new Double((String)operand2);
        } catch (Exception e) {
            opTwoNULL = true;
        }
        
        /*
         * Following variables are added to compute the Logical expression.
         * For logical expression operand can only be of Boolean type.
         */
        boolean opOneNULLB = false;
        boolean opTwoNULLB = false;
        Boolean actOp1B = new Boolean("false");
        Boolean actOp2B = new Boolean("false");
        
        try {
            actOp1B = new Boolean((String)operand1);
        } catch (Exception e) {
            opOneNULLB = true;
        }
        try {
            actOp2B = new Boolean((String)operand2);
        } catch (Exception e) {
            opTwoNULLB = true;
        }

        boolean strOpnd = false;
        if(((String)operand1).indexOf("\"")!=-1 || ((String)operand2).indexOf("\"")!=-1)
            strOpnd = true;
        
        switch (((Character)op).charValue()) {
            // if it is a "+", it add the both operands and returns the result
            case '+': {
                if (strOpnd)
                    return ((operand1 == null ? "" : convert2Str((String)operand1)) + (operand2 == null ? "" : convert2Str((String)operand2))).replaceAll("\"\"","");
                return
                        new Double(
                            (opOneNULL ? (new Double(0).doubleValue()) : (actOp1.doubleValue()))
                                +
                            (opTwoNULL ? (new Double(0).doubleValue()) : (actOp2.doubleValue()))
                        ).toString();
            }                
            // if it is a "-", it returns the result of the substraction
            case '-': return
                        new Double(
                            (opOneNULL ? (new Double(0).doubleValue()) : (actOp1.doubleValue()))
                                -
                            (opTwoNULL ? (new Double(0).doubleValue()) : (actOp2.doubleValue()))
                        ).toString();
            // if it is a "*", it returns the result of the multiplication
            case '*': return
                        new Double(
                            (opOneNULL ? (new Double(1).doubleValue()) : (actOp1.doubleValue()))
                                *
                            (opTwoNULL ? (new Double(1).doubleValue()) : (actOp2.doubleValue()))
                        ).toString();
            /* if it is a "/", it returns the result of the division
             * But be careful, if the second operator is zero, a "division by zero"
             * ExpressionEvaluatorException is thrown since it isn't a valid expression.
             */
            case '/': {
                if ((opTwoNULL ? (new Double(1).doubleValue()) : (actOp2.doubleValue())) != 0) {
                    return
                        new Double(
                            (opOneNULL ? (new Double(1).doubleValue()) : (actOp1.doubleValue()))
                                /
                            (opTwoNULL ? (new Double(1).doubleValue()) : (actOp2.doubleValue()))
                        ).toString();
                } else {
                    throw new ExpressionEvaluatorException("Division by Zero");
                }
            }
            case '^': {
                // If it is a "^", it returns the result of the calculation with power
                int result = 1;
                /* For that we use a for-loop to evaluate.
                   It makes it possible that the base/ operand1 is multiplicated with itself
                   The frequency of multiplication with itself is given by the second operand
                 */
                 actOp1 = (opOneNULL ? (new Double(1)) : actOp1);
                 actOp2 = (opTwoNULL ? (new Double(1)) : actOp2);

                for (double i = 1; i <= actOp2.doubleValue(); i++) {
                    result *= actOp1.doubleValue();
                }
                return  new Double(result).toString();
            }
	    // If it is a "%", it returns the result of the calculation with modulus	
            case '%': return
                 new Double(
                            (opOneNULL ? (new Double(0).doubleValue()) : (actOp1.intValue()))
                                %
                            (opTwoNULL ? (new Double(0).doubleValue()) : (actOp2.intValue()))
                        ).toString();
                            
            // If it is a "<", it returns the result of relation either true of false.
            case '<': return
                 new Boolean(
                            (opOneNULL ? (new Double(0).doubleValue()) : (actOp1.intValue()))
                                <
                            (opTwoNULL ? (new Double(0).doubleValue()) : (actOp2.intValue()))
                        ).toString();                
                            
             // If it is a ">", it returns the result of relation either true of false.
             case '>': return
                 new Boolean(
                            (opOneNULL ? (new Double(0).doubleValue()) : (actOp1.intValue()))
                                >
                            (opTwoNULL ? (new Double(0).doubleValue()) : (actOp2.intValue()))
                        ).toString();                               
            
            // If it is a "=", it returns the result of relation either true of false. 
            case '=':{
                if (strOpnd) 
                    return new Boolean((operand1 == null ? "" : (String)operand1).equals((operand2 == null ? "" : (String)operand2))).toString();
                return
                new Boolean(
                            (opOneNULL ? (new Double(0).doubleValue()) : (actOp1.intValue()))
                                ==
                            (opTwoNULL ? (new Double(0).doubleValue()) : (actOp2.intValue()))
                        ).toString();                               
            }                
            // If it is a "@", it returns the result of relation(<=) either true of false.
            case '@': return
                 new Boolean(
                            (opOneNULL ? (new Double(0).doubleValue()) : (actOp1.intValue()))
                                <=
                            (opTwoNULL ? (new Double(0).doubleValue()) : (actOp2.intValue()))
                        ).toString();                
             
             // If it is a "#", it returns the result of relation(>=) either true of false.
             case '#': return
                 new Boolean(
                            (opOneNULL ? (new Double(0).doubleValue()) : (actOp1.intValue()))
                                >=
                            (opTwoNULL ? (new Double(0).doubleValue()) : (actOp2.intValue()))
                        ).toString();                               
                            
             // If it is a "@", it returns the result of relation(!=) either true of false.
             case '$':{
                if (strOpnd) 
                    return new Boolean(!(operand1 == null ? "" : (String)operand1).equals((operand2 == null ? "" : (String)operand2))).toString();
                 return
                 new Boolean(
                            (opOneNULL ? (new Double(0).doubleValue()) : (actOp1.intValue()))
                                !=
                            (opTwoNULL ? (new Double(0).doubleValue()) : (actOp2.intValue()))
                        ).toString();                               
             }                
             // If it is a "&", it returns the result of logical exp either true of false for &&.
             case '&': return
                 new Boolean(
                            (opOneNULLB ? (new Boolean(false).booleanValue()) : (actOp1B.booleanValue()))
                                &&
                            (opTwoNULLB ? (new Boolean(false).booleanValue()) : (actOp2B.booleanValue()))
                        ).toString();                               
                            
            // If it is a "|", it returns the result of logical exp either true of false for |.
            case '|': return
                 new Boolean(
                            (opOneNULLB ? (new Boolean(false).booleanValue()) : (actOp1B.booleanValue()))
                                ||
                            (opTwoNULLB ? (new Boolean(false).booleanValue()) : (actOp2B.booleanValue()))
                        ).toString();

            // If it is a "!", it returns the result of logical exp either true of false for !.                
            case '!': return
                 new Boolean(
                            (opOneNULLB ? (new Boolean(false).booleanValue()) : (actOp1B.booleanValue()))
                                ^
                            (opTwoNULLB ? (new Boolean(false).booleanValue()) : (actOp2B.booleanValue()))
                        ).toString();                
                            // In case of an nonexisting operator, it will return minus 1
            default : return new Double(-1).toString();
        }
    }
    
    /**************************************************************************/
    // this method is used to evaluate a postfixed satck
    //This method'll now return String as Logical and Relational operator is also added.
    // earlier it was returning Double only.
    public static String evaluate(Stack pf) throws ExpressionEvaluatorException {
        // this stack is for storing the digits separatly
        Stack operandStack = new Stack();
        // a variable which represents the current token
        Object currToken;
        // These two variables are useful for calculation,they represent operand1 and operand2
        Object operand1 = null;
        Object operand2 = null;
        // for storing the result of operand1 operator operand2
        Object result = null;
        int size =0;
        size = pf.size();
        /* In the following lines we read out a character
         * from the String as long as the String isn't empty.
         */
        for (int i = 0; i < size; i++) {
            // reads the current token
            currToken = pf.get(i);
            //If the Object read is a digit we store it immediatly in the operand stack
            if (currToken instanceof String) {
                // We push the operand onto the stack.
                operandStack.push(currToken);
            } else if (currToken instanceof Character) {
                // If the object read is one of the operators, we do the following
                // checks if the Character is a legal operator
                if (operators.indexOf(((Character)currToken).charValue()) != -1) {
                    /* Get the first value out of the Stack and save it.
                     * also remove the operand from the stack
                     */
                    operand2 = operandStack.pop();

                    /* Now get the second digit out of the Stack and save it.
                     * also remove the operand from the stack
                     */
                    operand1 = operandStack.pop();
                    /* This is the evaluation of the two operands and the operator.
                     * The sum is stored into the variable  result
                     */
                    result = (String)calculate(operand1, operand2, currToken);

                    // Now we push the result onto the stack again as an Object of type Integer
                    operandStack.push(result);
                }
            } else if(currToken instanceof Stack){
                pf.setElementAt(evaluate((Stack)currToken).toString(), i);
                System.out.println("PF" + pf);
                i--;
            } else {
                /* It is neither a digit or an operator, the expression of the postfix form is wrong.
                 * It won't work, we throw an ExpressionEvaluatorException.
                 */ 
                throw new ExpressionEvaluatorException("invalid expression"); }
        } // end of the for-loop
        if (!operandStack.isEmpty()) {
            result = operandStack.pop();
        }
        // After passing the for-loop we have evaluate the whole assignment and can show the result
        return new String((String)result);
    }
    
    
    /**************************************************************************/
    // This method parse the expression i.e. fill values placeholders and also 
    // covvert infix Stack into Postfix Stack.
    public static String parseAndEvaluate(Stack pf, HashMap valueMap) throws ExpressionEvaluatorException {
        //Fills values in stack first and the conver it to postfixnotation. 
        try{
            return evaluate(infixToPostfix(fillValues(pf,valueMap)));
        } catch (ExpressionEvaluatorException e) {e.printStackTrace();}   
        return null;
    }
    
    /******************************************************************************/
    /* These lines set the precedency of the operators
     * For setting priority we use an index of type integer.
     */
    private static int precedency(Object op) {
        char myop = ((Character)op).charValue();
        switch (myop) {
            case '(' : case ')' : case '{' : case '}' : case '[' : case ']' : return  0;
            case '&' : case '|' : return  1;
            case '!' : return  2;
            case '<' : case '>' : case '=' : case '@' : case '#' : case '$' : return  3;
            case '+' : case '-' : return  4;
            case '*' : case '/' : case '%' : return  4;
            case '^' :            return  6;
            default :             return -1;
        }
    }
    
    /**************************************************************************/
    /* This method converts the infix form into a postfix form
     * The infix presentation has to be stored in a stack.
     * It also returns the postfix expression as a stack.
     */
    public static Stack infixToPostfix(Stack iStack) throws ExpressionEvaluatorException {
        // This is a helperstack, in which we push the operators.
        Stack operatorStack = new Stack();
        // Used for the postfix presentation.
        Stack result = new Stack();
        // Variable for the current character which is processed.
        Object currToken;
        int size = iStack.size();
        // The next line reads out the whole List Object by Object.
        for (int i = 0; i < size; i++) {
            // In this line the current Object is stored to handle it.
            currToken = iStack.get(i);
            //If the oibject read is a digit we store it immediatly in the result string
            if (currToken instanceof String) {
                result.push(currToken);
            } else if (currToken instanceof Character) {
                // If the object read is one of the operators, we do the following
                if (operators.indexOf(((Character)currToken).charValue()) != -1) {
                    /* Here we check what we do with the operator
                     * It depends if there already exist an operator in the stack. if ther is one
                     * we have to attend the precendency.
                     */
                    while (!operatorStack.isEmpty() &&
                        ( precedency(currToken) <= precedency(operatorStack.firstElement())
                        )
                        ) {

                        /* As long as the operatorstack isn't empty or our current operator
                        * has a lower or the same priority as the operator at the top
                        * of the stack, we pop the top of the stack and write it to the result string.
                         */
                        result.push(operatorStack.pop());
                    }// End of the while-loop.
                    
                    /* Unless the Stack is empty or the priority
                     * is higher then the one of the first Object in the operator stack
                     * we push it onto the stack.
                     */
                    operatorStack.push(currToken);
                } else if (((Character)currToken).charValue() == '(' || ((Character)currToken).charValue() == '{' || ((Character)currToken).charValue() == '[') {
                    /* Another case is that we read out a brace(i.e. '(','{',
                     * '['), which is also possible in infix, to define a 
                     * priority in evaluating. Exp in between () or {} or [] 
                     * 'll be conside as sub exp. That sub Expession'll be 
                     * extracted from the infix stack and new Postfix stack 'll
                     * be inserted in the result postfix stack.
                     * This 'll be done recursively for all the braces.  
                     */
                    // For an opening brace we use these lines.
                    result.push(infixToPostfix(getSubExpression(iStack,((Character)currToken).charValue(),i)));
                    iStack.insertElementAt("New", i);
                    size=iStack.size();
                    //operatorStack.push(currToken);
                } else if (((Character)currToken).charValue() == ')') {
                    /*
                     * As closing corresponding brace is eliminated when an
                     * opening brace occoures. So if any closing brace occuers 
                     * then it is extra i.e. Invalid Expression.
                     */
                    throw new ExpressionEvaluatorException("Invalid expression : Extra closing Parantheses.");
                }
            } else {
                throw new ExpressionEvaluatorException("invalid expression");
            }
        }//end of the for-loop
        
        /* At the end if there exist no element in the string the
         * transformation of the infix to a postfix presentation is nearly finished.
         * There can still exist some operators in the stack which have to be added
         * to the result String.
         */
        // This is done as long as the operator stack is empty.
        while (!operatorStack.isEmpty()) {
            /* Get the first Object and add it to the postfix String
             * after that delete the first Object of the operator stack.
             */
            result.push(operatorStack.pop());
        }

        return result;
    }
    
    /*
     * This method 'll extract Sub-Expression (i.e. Expression in between braces "(),{},[]")
     * from the Expression stack. 
     * @param iStack - Expression stack from which Sub Expressoin'll be extracted.
     * @param bCh - braces characher, it'll tell which braces(i.e. '(','{','[').
     * @param bPos - bCh position in iStack.
     *
     * @return stk - Sub-Expression infix stack excluding starting and enclosing braces.
     *
     * @ ExpressionEvaluatorException - 'll throw ExpressionEvaluatorExpressionEvaluatorException if doesn't found the relative enclosing braces.
     */
    private static Stack getSubExpression(Stack iStack, char bCh,int bPos) throws ExpressionEvaluatorException{
        Stack stk = new Stack();
        int size = iStack.size();
        int others = 0;
        Object token;   
        int i=bPos+1;
        
        char endB = ')';
        if(bCh == '{')
            endB='}';
        else if(bCh=='[') 
            endB = ']';
        
        for(;i<size;i++) {
            token = iStack.get(i);
            //iStack.remove(i);
            if(token instanceof Character) {
                if(((Character)token).charValue()==bCh)
                    others++;
                
                if(((Character)token).charValue()== endB)
                    if(others>0){
                        others--;
                    }else
                        break;
            }
            stk.push(token);
        }
        if(i>=size)
           throw new ExpressionEvaluatorException("Invalid Expression."); 
        
        i=0;
        size = stk.size(); 
        for(;i<size+2;i++){ 
            iStack.remove(bPos);
        }    
        return stk;
    }
    
    /* this method will convert a string which has an infix expression into
     * a stack which can further be used to manipulate postfix operation
     */
    public static Stack stringToInfix(String equation) {
        try{
            equation = makeCompatible(equation);
        }catch(ExpressionEvaluatorException e){}    
        Stack data = new Stack();
        String token = "";
        String operators = "+-*/^%()[]{}<>=&|@#$!";
        // go thru the string one by one
        for(int i = 0 ; i < equation.length(); i++) {
            
            char c = equation.charAt(i);

            /* check if the character we just read is an operator or not,
             * if its not add it into the number value denoted by token
             */
            if (operators.indexOf(c) == -1) {
                token = token + c;
            } else {

                /* if its the operator then first push the token in the stack,
                 * if its not the empty
                 * after this push the character also
                 */
                if (!token.equals("")) {
                    data.push(token);
                    token = "";
                }
                data.push(new Character(c));
            }
        }
        // at the end, we have to push the last number also in the stack
        if (!token.equals("")) {
            data.push(new StringBuffer(token).toString());
        }
        
        // At the End it returns the created stack
        return data;
    }
    
    /*
     * This method 'll replace all the two cahr operator with one char. Following'll be  the conversion.
     * && -> &, ||-> |, 
     * <= -> @, >= -> #, != -> $, == -> =
     */
    private static String makeCompatible(String source) throws ExpressionEvaluatorException{
        source = source.replaceAll("&&", "&"); 
        source = source.replaceAll("\\|\\|", "|");
        source = source.replaceAll("==", "=");
        source = source.replaceAll("<=", "@");
        source = source.replaceAll(">=", "#");
        source = source.replaceAll("!=", "\\$");
        
        /*
         * Following code'll convert unrary ! to Exclusive Or(XOR) boolean operator.
         * XOR operator 'll be represented as ! in the  Expression String.
         */
        int index=0;
        String temp ="";
        int length = 0;
        while(true){
            index = source.indexOf("!",index);
            if(index == -1)
                break;
            length = source.length();
            // temp'll have the First half of the Expression i.i before !. 
            temp = "";
            temp = source.substring(0,index) + "[";
            System.out.println("Temp :" +temp);
            char endCh = '\0';
            char startCh = '\0';
            
            //will throw an ExpressionEvaluatorException if ! found at Last.  
            if(index == length-1)
                throw new ExpressionEvaluatorException("! can't be at end.");
            
            // Storing the First Character if any braces(i.e an Expression)  or 
            // null character for value. 
            startCh = source.charAt(index+1);
            // Computing the End of braces. 
            if(startCh == '(')
                endCh = ')';
            else if(startCh == '{')
                endCh = '}';
            else if(startCh == '[')
                endCh = ']';
            
            // others will have a count on same braces, as startCh, in sub expression 
            int others = 0;
            
            int i=0;
            for(i = index+1; i<length;i++) {
                if(endCh == '\0') {
                    // if not is applied on value.
                    if(operators.indexOf(source.charAt(i))!=-1)
                        break;
                    temp = temp + source.substring(i,i+1);
                } else {
                    //if not is applied on Expression.
                    temp = temp + source.substring(i,i+1);
                    if(source.charAt(i) == endCh)
                        others--;
                    if(source.charAt(i) == startCh)
                        others++;
                    
                    if(source.charAt(i) == endCh && others == 0)
                        break;
                }    
            }
            if(others == 0) {
                temp = temp + "~true]";
            } else 
                throw new ExpressionEvaluatorException("Invalid Expression");
            if(i< source.length())
                temp = temp +  source.substring(i+1,source.length());
            source= temp;
            System.out.println("Source : "+source);
        }  
        source = source.replaceAll("~","!");
        return source;
    }
    
    /*
     *This method'll append " at the Start and End id it is not there...
     */
     public static String convert2Str(String str){
        if(str.charAt(0)!='\"' || str.charAt(str.length()-1)!='\"')
            return "\""+str+"\"";
        return str;
    }
    
    /*
     * This method 'll check the validity of the Expression. 
     * @param Expression : Expression need to be checked.
     * @return valid : Expression is vaild or not(true/false).  
     */ 
    public static boolean isValidExpression(String expression) {
        boolean valid = true;
        try{
            if(!expression.equals("")) {
                Stack stk = infixToPostfix(stringToInfix(makeCompatible(expression)));  
            } else
                valid = false;
        } catch(ExpressionEvaluatorException e) {
            valid = false;
        }
        return valid;
    } 
    
    /*
     *This method 'll fill the values in place holders. 
     */
    public static Stack fillValues(Stack pf, HashMap valuesMap) throws ExpressionEvaluatorException{
        int i=0;
        int size = pf.size();
        java.util.Set keySet = valuesMap.keySet();
        Object currToken = null;
        // an - AlphaNumeric, if token is placeholder not a literal.  
        boolean an = false; 
        for(i=0; i < size;i++) {
            currToken = pf.get(i);
            //if the current token is not a operator of braces.
            if(currToken instanceof String){
                an = false;
                try{
                    Double.valueOf((String)currToken).isNaN();
                } catch (NumberFormatException e) {
                    an = true;
                }
                
                if(an && ((String)currToken).equalsIgnoreCase("true") || ((String)currToken).equalsIgnoreCase("false")) {
                    an = false;
                }
                
                if(an && ((String)currToken).indexOf("\"")== -1) {
                    if(keySet.contains((String)currToken)) {
                        System.out.println("Replacing "+currToken);
                        pf.set(i,valuesMap.get((String)currToken));
                    } else {  
                        throw new ExpressionEvaluatorException("Key "+currToken+" Does not have a value."); 
                    }   
                }
            }
            System.out.println((i+1) + " : " +pf);
        }
        return pf;
    }
    
    public static void main(String [] arg) {
        try {
            //String exp = "100*4+3";
            /*
             * Check while generating the MathMl if the Last Char is an enclosing bracket"),},]," 
             * then put whole experssion in bracket and miltiply it with 1.
             * Also if any operand is with unrary - then subtract that opnd from 0.  
             */
            HashMap map = new HashMap();
            map.put("L0001001","false");
            map.put("L0001020","false");
            //map.put("A.B.C","10");
            //map.put("B","true");
            
            String exp = "L0001001||L0001020"; 
            //getA().getB().getC()
            //String exp = "((500+90)>10)";
            //String exp = "!(100*[4*{5+6^2}*(3%5)]<1)&&!(100!=2)";
            if(isValidExpression(exp)) {
                System.out.println(ExpressionEvaluator.infixToPostfix(ExpressionEvaluator.stringToInfix(exp)));
                System.out.println(ExpressionEvaluator.parseAndEvaluate(ExpressionEvaluator.stringToInfix(exp),map));
                /*Stack stk = ExpressionEvaluator.stringToInfix(exp);
                stk = ExpressionEvaluator.infixToPostfix(stk);
                System.out.println("In Main : "+stk);
                System.out.println("In Main : "+stk);
                System.out.println(ExpressionEvaluator.evaluate(stk));*/
            } else 
                System.out.println("Not a valid Expression.");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
