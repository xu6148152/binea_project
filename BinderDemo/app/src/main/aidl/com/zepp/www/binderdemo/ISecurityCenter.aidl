// ISecurityCenter.aidl
package com.zepp.www.binderdemo;

// Declare any non-default types here with import statements

interface ISecurityCenter {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

            String encrypt(String content);
                String decrypt(String password);
}
