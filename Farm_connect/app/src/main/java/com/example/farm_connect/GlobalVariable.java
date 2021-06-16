package com.example.farm_connect;

public class GlobalVariable {

    public static String globalemail;

    public static String getGlobalglobalemail() {
        for (int i = 0; i < globalemail.length(); i++) {
            if( (globalemail.charAt(i) == '@' || globalemail.charAt(i) == '.')) {
                globalemail = charRemoveAt(globalemail,i);
            }
        }
        return globalemail;
    }
    public static String charRemoveAt(String str, int p) {
        String s = str.substring(0, p) + str.substring(p + 1);
        int i = s.lastIndexOf('g');
        String st = s.substring(0,i);
        return st;
    }



}

