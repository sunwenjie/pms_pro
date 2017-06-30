package com.asgab.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Cryptogram {
  final static int offset = 3;

  /**
   * Encode a string using Base64 encoding. Used when storing passwords as cookies.
   *
   * This is weak encoding in that anyone can use the decodeString routine to reverse the encoding.
   *
   * @param str
   * @return String
   * @throws KANException
   */
  public static String encodeString(final String sourceString) {
    if (sourceString != null && !sourceString.trim().equals("")) {
      final BASE64Encoder encoder = new BASE64Encoder();
      String encodedString = new String(encoder.encode(sourceString.getBytes()));

      if (encodedString != null) {
        // 去掉回车符换行符
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(encodedString);
        encodedString = m.replaceAll("");
      }
      if (encodedString != null && encodedString.length() < 6) {
        encodedString = "***" + encodedString;
      }

      final StringBuffer offsetString = new StringBuffer();
      offsetString.append(encodedString.substring(offset * 1, offset * 2));
      offsetString.append(encodedString.substring(offset * 0, offset * 1));
      offsetString.append(encodedString.substring(offset * 2));

      return offsetString.toString().trim();
    } else {
      return "";
    }

  }

  /**
   * Decode a string using Base64 encoding.
   *
   * @param str
   * @return String
   * @throws IOException
   */
  public static String decodeString(final String sourceString) {
    if (sourceString != null && !sourceString.trim().equals("")) {
      final StringBuffer offsetString = new StringBuffer();
      offsetString.append(sourceString.substring(offset * 1, offset * 2));
      offsetString.append(sourceString.substring(offset * 0, offset * 1));
      offsetString.append(sourceString.substring(offset * 2));

      final BASE64Decoder dec = new BASE64Decoder();
      String value = "";
      try {
        value = new String(dec.decodeBuffer(offsetString.toString().replace("***", "")));
      } catch (IOException e) {
        e.printStackTrace();
      }

      return value;
    } else {
      return "";
    }
  }

  // 这个自带url加密
  public static String encodeId(String id) {
    try {
      return URLEncoder.encode(URLEncoder.encode(encodeString(id), "UTF-8"), "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String decodeId(String id) {
    try {
      return decodeString(URLDecoder.decode(URLDecoder.decode(id, "UTF-8"), "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return "";
  }

  public static void main(String[] args) {
    System.out.println(decodeString("uTDMTUMyN3A="));
  }

}
