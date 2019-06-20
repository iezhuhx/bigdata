package com.sink;

import com.cyb.app.commondb.ConnectionUtils;
import com.cyb.app.commondb.DDLUtils;
import com.cyb.app.h2.H2DBConnectionPool;
import com.cyb.app.h2.H2DBInfor;
import com.cyb.app.holiday.Holiday;
import com.cyb.app.holiday.HolidaySQL;
import com.cyb.app.holiday.HolidayUtils;
import com.cyb.utils.bean.RMap;
import com.cyb.utils.date.DateSafeUtil;
import com.cyb.utils.date.DateUnsafeUtil;
import com.cyb.utils.file.FileUtils;
import com.cyb.utils.text.ELUtils;
import com.po.OrderStatics;
import org.apache.commons.dbutils.ResultSetHandler;
import org.h2.jdbcx.JdbcConnectionPool;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者 : iechenyb<br>
 * 类描述: 说点啥<br>
 * 创建时间: 2019年1月23日
 */
public class FlinkH2DbUtils {
	static String tableName = "order1";//订单表 order 为关键字 所以为order1
	static String createSQL ="create table "+tableName+"(name varchar,price varchar,num varchar,ywrq varchar)";

	static String tableName1 = "orderSum";//订单汇总
	static String createSQL1 ="create table "+tableName1+"(name varchar,price varchar,time varchar)";


	static String insertSQL = "insert into "+tableName1+" values('${name}','${price}','${time}')";


	static String querySQL="select price,name,time from orderSum";
	static H2DBInfor dbInfo = new H2DBInfor(H2DBInfor.Server,"","flink","","");
	static JdbcConnectionPool pool = H2DBConnectionPool.getJDBCConnectionPool(dbInfo);

	public static void main(String[] args) throws SQLException, IOException {
	    createTable(tableName,createSQL);
		createTable(tableName1,createSQL1);
		//insertData(new OrderStatics("test",0));
		System.out.println(queryData());
	}


   public static  void insertData(OrderStatics statics) throws SQLException {
	   String sql = ELUtils.el(insertSQL,RMap.build().put("name",statics.getName()).put("price",""+statics.getPrice()).put("ywrq",""));
	   ConnectionUtils dbUtil = new ConnectionUtils(pool.getConnection());
	   dbUtil.insert(pool.getConnection(), sql, new ResultSetHandler<Object>() {
		   @Override
		   public Object handle(ResultSet resultSet) throws SQLException {
			   return resultSet;
		   }
	   });
   }

   public static   List<OrderStatics> queryData() throws SQLException {
	   ConnectionUtils dbUtil = new ConnectionUtils(pool.getConnection());
	   List<OrderStatics> data = dbUtil.queryForList(querySQL,OrderStatics.class);
	   System.out.println("查询订单总数据："+data);
	   return data;
   }

	private static boolean createTable(String name,String sql) throws SQLException {
		ConnectionUtils dbUtil = new ConnectionUtils(pool.getConnection());
		boolean hasTable = DDLUtils.isExist(pool.getConnection(), name);
		if (!hasTable) {
			System.out.println("创建表...");
			dbUtil.update(sql);
		} else {
			System.out.println("表已经存在！");
		}
		return true;
	}
}
