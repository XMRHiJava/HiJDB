package hij.db.impl;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import hij.db.ICreator;
import hij.util.HiLog;
import hij.util.generic.IFunc;

public final class ConnectionProxy {	
	/**
	 * @param conn_str 数据库连接字符串
	 * @param db_type 数据库类型,请参阅final变量
	 * @param is_close_after_execute
	 */
	public boolean init(String conn_str, String user, String pwd, int db_type, boolean is_close_after_execute){
		this.connStr = conn_str;
		this.db_type = db_type;
		this.user = user;
		this.pwd = pwd;
		this.creator = getCreator(db_type);
		if(this.creator == null) {
			return false;
		}
		this.creator.setCloseAfterExecute(is_close_after_execute);
		is_init_success = initDB(creator, db_type);		
		return is_init_success;
	}
	
	
	public Connection getConnection() {
		if (this.creator != null) {
			return this.creator.createConnection(connStr, user, pwd);
		} else {
			return null;
		}
	}
	
	/**
	 * 关闭数据库连接
	 * @throws SQLException 
	 */
	public void close() {
    	this.creator.closeConnection();
	}
	
	public void closeAfterExecute(Connection conn) {
    	this.creator.closeConnection(conn);
	}
	
	public void closeAfterExecute(Connection conn, boolean isTrans) {
    	this.creator.closeConnection(conn, isTrans);
	}
	
	ICreator creator = null;
	boolean is_init_success = false;
	String connStr = "";
	String user = "";
	String pwd = "";
	int db_type = 1;
	
	/**
	 * 添加要支持的数据库类型并提供创建类型
	 *
	 * @param type
	 * @param creator
	 * @return
	 */
	public static boolean addDBCreator(int type, IFunc<ICreator> creator) {
		if(creator == null || creators.containsKey(type)) {
			return false;
		}
		creators.put(type, creator);
		return true;
	}
	
	public static ICreator getCreator(int type) {
		if(!creators.containsKey(type)) {
			return null;
		}
		IFunc<ICreator> factory = creators.get(type);
		if (factory == null) {
			return null;
		}
		return factory.handle();
	}	

	/**
	 * 初始化数据库操作对象,主要实现:
	 * 1) 注册driver
	 * @param creator
	 * @param db_type
	 */
	private static boolean initDB(ICreator creator, int db_type){
		if (creator == null) {
			HiLog.error(String.format(
					"database(%d) not support, please using DBOperate.AddDBCreator set",
					db_type));
			return false;
		}
		String driver = creator.getDriver();
		if (driver == null) {
			return false;
		}
		if (forNames.containsKey(driver)) {
			return true;
		}
		
		try {
			Class.forName(driver);
			return true;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			HiLog.error(String.format(
					"database(%d) driver name(%s) init failed, beacuse (%s) not found",
					db_type, driver, driver));
			return false;
		}
	}

	static Map<Integer, IFunc<ICreator>> creators = new HashMap<Integer, IFunc<ICreator>>();
	static Map<String, String> forNames = new HashMap<String, String>();
}
