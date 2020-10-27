package com.hipreme.mobbuy.utils;
import java.security.MessageDigest;

public class Digest
{
    private static MessageDigest md5;

    public static boolean startMD5() {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            Error.print(e);
            return false;
        }
        return true;
    }

    private static String byteToHex(byte[] byteData)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
    //Snippet
    public static String MD5(String input)
    {
        md5.update(input.getBytes());
        return byteToHex(md5.digest());
    }
}
