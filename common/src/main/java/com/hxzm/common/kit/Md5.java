package com.hxzm.common.kit;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zhangyaohai on 2017/8/10.
 */
public class Md5 {

    public static String md5_16(String sourceStr){
        return md5_32(sourceStr).substring(8, 24);
    }

   public static String md5_32(String sourceStr) {
       StringBuffer buf = new StringBuffer("");
       try {
           MessageDigest md = MessageDigest.getInstance("MD5");
           md.update(sourceStr.getBytes());
           byte b[] = md.digest();
           int i;

           for (int offset = 0; offset < b.length; offset++) {
               i = b[offset];
               if (i < 0)
                   i += 256;
               if (i < 16)
                   buf.append("0");
               buf.append(Integer.toHexString(i));
           }
       } catch (NoSuchAlgorithmException e) {
           e.printStackTrace();
       }
       return buf.toString();
   }

    public static void main(String[] args) {
        System.out.println(    Md5.md5_32("123456"));
    }

}
