/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ExpressionEvaluatorException.java
 *
 * Created on September 17, 2004, 12:08 PM
 */

package com.see.truetransact.commonutil.expressionevaluate;


/* This class is needed then dealing with a postfix expression.
   In postfix only digits from 0 - 9 and some operators are legal Characters.
   Therefore we need to check if the postfix expression is valid,
   if not this exception will be thrown
*/

public class ExpressionEvaluatorException extends Exception {
  // We just use the super-class Exception to implement this class.
  public ExpressionEvaluatorException(String s) {
    super(s);
  }
}
