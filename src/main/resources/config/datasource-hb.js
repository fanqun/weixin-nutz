var ioc = {
	dataSource_hb : {
		type : "com.alibaba.druid.pool.DruidDataSource",
		events : {
			depose : "close"
		},
		fields : {
			url : {
				java : "$config.get('hb.db-url')"
			},
			username : {
				java : "$config.get('hb.db-user')"
			},
			password : {
				java : "$config.get('hb.db-pwd')"
			},
			maxActive : {
				java : "$config.get('db-pool-max')"
			},
			initialSize : {
				java : "$config.get('db-pool-init')"
			},
			maxWait : {
				java : "$config.get('db-pool-wait')"
			},
			minIdle : {
				java : "$config.get('db-pool-min')"
			},
			timeBetweenEvictionRunsMillis : {
				java : "$config.get('db-time-betw')"
			},
			minEvictableIdleTimeMillis : {
				java : "$config.get('db-time-met')"
			},
			validationQuery : {
				java : "$config.get('db-query-val')"
			},
			testWhileIdle : {
				java : "$config.get('db-test-idle')"
			},
			testOnBorrow : {
				java : "$config.get('db-test-borr')"
			},
			testOnReturn : {
				java : "$config.get('db-test-return')"
			},
			filters : {
				java : "$config.get('db-filters')"
			}
		}
	},
	dao_hb : {
		type : "org.nutz.dao.impl.NutDao",
		args : [ {
			refer : "dataSource_hb"
		}, {
			refer : "sqlManeger"
		} ]
	}
}