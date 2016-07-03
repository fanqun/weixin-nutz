package net.hzfanqun.weixin.utils;

import java.io.UnsupportedEncodingException;

public class Oauth2Util {

    public static String urlEncodeUTF8(String source) {
        String result = source;
        
        try {
            result = java.net.URLEncoder.encode(source,"utf-8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
