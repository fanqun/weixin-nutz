package net.hzfanqun.weixin.utils;

import org.junit.Test;

/**
 * Created by fanqun on 2016/7/4.
 */
public class oauth2UtilTest {
    @Test
    public void urlEncodeUTF8Test(){
        String oauthUrl = "http://fineliving.ngrok.wendal.cn/hb/oauth/service";
        System.out.println(Oauth2Util.urlEncodeUTF8(oauthUrl));
    }
}
