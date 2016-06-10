package hij.db;

import java.sql.Connection;

/**
 * 数据库访问相关属性提供接口
 * @author XuminRong
 *
 */
public interface ICreator {

	/**
	 * 获得数据库驱动信息
	 * @return
	 */
	public String getDriver();
	
	/**
	 * 创建数据库连接
	 * @param conn
	 * @param user
	 * @param pwd
	 * @return
	 */
	public Connection createConnection(String conn, String user, String pwd);

	
	/**
	 * 创建数据库连接
	 * @param conn
	 * @param user
	 * @param pwd
	 * @param isTrans
	 * @return
	 */
	public Connection createConnection(String conn, String user, String pwd, boolean isTrans);
	
	/**
	 * 关闭数据库连接
	 * @param conn
	 */
	public void closeConnection(Connection conn);
	/**
	 * 关闭数据库连接
	 */
	public void closeConnection();
	
	/**
	 * @param conn
	 * @param isTrans
	 */
	public void closeConnection(Connection conn, boolean isTrans);
	
	/**
	 * 执行完操作后是否关闭
	 * @param isCloseAfterExecute
	 */
	public void setCloseAfterExecute(boolean isCloseAfterExecute);
}
