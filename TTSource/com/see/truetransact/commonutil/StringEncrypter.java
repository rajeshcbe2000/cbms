/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * StringEncrypter.java
 */

package com.see.truetransact.commonutil;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class StringEncrypter {
    public static class EncryptionException extends Exception {
        public EncryptionException(Throwable t) {
            super(t);
        }
    }
    public StringEncrypter() throws EncryptionException {
        this(DES_ENCRYPTION_SCHEME, DEFAULT_ENCRYPTION_KEY);
    }
    
    public StringEncrypter(String encryptionScheme) throws EncryptionException {
        this(encryptionScheme, DEFAULT_ENCRYPTION_KEY);
    }
    
    public StringEncrypter(String encryptionScheme, String encryptionKey)
    throws EncryptionException {
        if(encryptionKey == null)
            throw new IllegalArgumentException("encryption key was null");
        if(encryptionKey.trim().length() < 24)
            throw new IllegalArgumentException("encryption key was less than 24 characters");
        try {
            byte keyAsBytes[] = encryptionKey.getBytes("UTF8");
            if(encryptionScheme.equals("DESede"))
                keySpec = new DESedeKeySpec(keyAsBytes);
            else
                if(encryptionScheme.equals("DES"))
                    keySpec = new DESKeySpec(keyAsBytes);
                else
                    throw new IllegalArgumentException("Encryption scheme not supported: " + encryptionScheme);
            keyFactory = SecretKeyFactory.getInstance(encryptionScheme);
            cipher = Cipher.getInstance(encryptionScheme);
        }
        catch(InvalidKeyException e) {
            throw new EncryptionException(e);
        }
        catch(UnsupportedEncodingException e) {
            throw new EncryptionException(e);
        }
        catch(Exception e) {
            throw new EncryptionException(e);
        }
    }
    
    public String encrypt(String unencryptedString) throws Exception {
        if(unencryptedString == null || unencryptedString.trim().length() == 0)
            throw new IllegalArgumentException("unencrypted string was null or empty");
        byte ciphertext[];
        BASE64Encoder base64encoder;
        javax.crypto.SecretKey key = keyFactory.generateSecret(keySpec);
        cipher.init(1, key);
        byte cleartext[] = unencryptedString.getBytes("UTF8");
        ciphertext = cipher.doFinal(cleartext);
        base64encoder = new BASE64Encoder();
        return base64encoder.encode(ciphertext);
    }
    
    public String decrypt(String encryptedString) throws Exception {
        if(encryptedString == null || encryptedString.trim().length() <= 0)
            throw new IllegalArgumentException("encrypted string was null or empty");
        byte ciphertext[];
        javax.crypto.SecretKey key = keyFactory.generateSecret(keySpec);
        cipher.init(2, key);
        BASE64Decoder base64decoder = new BASE64Decoder();
        byte cleartext[] = base64decoder.decodeBuffer(encryptedString);
        ciphertext = cipher.doFinal(cleartext);
        return bytes2String(ciphertext);
    }
    
    private static String bytes2String(byte bytes[]) {
        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 0; i < bytes.length; i++)
            stringBuffer.append((char)bytes[i]);
        
        return stringBuffer.toString();
    }
    
    public static void main(String arg[]) {
        try {
            StringEncrypter sse = new StringEncrypter("DES");
            System.out.println(sse.encrypt("password123"));
        }
        catch(Exception exe) {
            exe.printStackTrace();
        }
    }
    
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    public static final String DES_ENCRYPTION_SCHEME = "DES";
    public static final String DEFAULT_ENCRYPTION_KEY = "q7oES6mChHgjlYgqx5a1aEVfrOVPHavB1VpPqQTN47NL8mVI1ear9bPKAokPGiXGvezz/XXl3LI";
    private KeySpec keySpec;
    private SecretKeyFactory keyFactory;
    private Cipher cipher;
    private static final String UNICODE_FORMAT = "UTF8";
}
