package com.yashwanth.kafka;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.security.Provider;
import java.security.Security;
import java.util.Iterator;


public class Encrptor {
    public static void main(String[] args) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        String privateData = "hellow@123456";
        encryptor.setPassword("password");
        encryptor.setAlgorithm("PBEWithMD5AndTripleDES");
        String enyrpted=encryptor.encrypt(privateData);
        assertNotSame(enyrpted,privateData);

        String decrypted=encryptor.decrypt(enyrpted);
        assertSame(decrypted,privateData);
        final Provider[] providers = Security.getProviders();
        for (int i = 0; i < providers.length; i++) {
            final String name = providers[i].getName();
            final double version = providers[i].getVersion();
            System.out.println("Provider[" + i + "]:: " + name + " " + version);
            if (args.length > 0) {
                final Iterator it = providers[i].keySet().iterator();
                while (it.hasNext()) {
                    final String element = (String) it.next();
                    if (element.toLowerCase().startsWith(args[0].toLowerCase())
                        || args[0].equals("-all"))
                        System.out.println("\t" + element);
                }
            }
        }
    }

    private static boolean assertSame(String a, String b) {
        if(a.equals(b)){
            return true;
        }

        throw new RuntimeException(String.format("%s is not same as %s",a,b));
    }

    private static boolean assertNotSame(String a, String b) {
        if(!a.equals(b)){
            return true;
        }

        throw new RuntimeException(String.format("%s is not same as %s",a,b));
    }
}
