package net.hzfanqun.weixin.modules;

import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.Ioc;
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
	}

	@Override
	public void destroy(NutConfig nc) {
	}

}
