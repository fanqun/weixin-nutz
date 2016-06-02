package net.hzfanqun.weixin.modules;

import org.nutz.mvc.annotation.Encoding;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.SetupBy;
import org.nutz.mvc.annotation.UrlMappingBy;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

/**
 * 
 * 
 * @author fanqun(hzfanqun@gmail.com)
 * @date 2016年5月22日 下午9:09:23
 */
@Modules(scanPackage = true)
@Ok("raw")
@Fail("http:500")
@IocBy(type = ComboIocProvider.class, args = { "*js", "config/", "*anno", "net.hzfanqun" })
@SetupBy(MainSetup.class)
@Encoding(input = "UTF-8", output = "UTF-8")
@UrlMappingBy(value = UrlMappingSet.class)
public class MainModule {
}
