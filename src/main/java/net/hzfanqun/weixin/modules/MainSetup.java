package net.hzfanqun.weixin.modules;

import java.util.List;

import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.Ioc;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;
import org.nutz.mvc.view.UTF8JsonView;

public class MainSetup implements Setup {

    @Override
    public void init(NutConfig nc) {
        UTF8JsonView.CT = "text/plain";
        Ioc ioc = nc.getIoc();
        Dao dao = ioc.get(Dao.class);
        Daos.createTablesInPackage(dao, "net.hzfanqun.weixin", false);
        NutMap cityKey = ioc.get(NutMap.class, "wxCityKeyMap");
        List<Record> list = dao.query("t_citykey", null);
        for (Record r : list) {
            cityKey.put(r.getString("_CITY"), r.getString("_KEY"));
        }
        Logs.get().infof("Read weather citykey completed. A total of %d", cityKey.size());
    }

    @Override
    public void destroy(NutConfig nc) {}

}
