package hij.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import hij.util.HiLog;

/**
 * MySQL的默认实现
 * @author Administrator
 *
 */
public class MySQLCreator implements ICreator {

	public MySQLCreator() {
		isCloseAfterExecute = false;
	}
	@Override
	public String getDriver() {
		// TODO Auto-generated method stub
		return "com.mysql.jdbc.Driver";
	}

	@Override
	public Connection createConnection(String conn_str, String user, String pwd) {
		if (connection != null && !isCloseAfterExecute) {
			return connection;
		}

		try {
			Connection cnn =  (Connection) DriverManager.getConnection(conn_str, user, pwd);
			if (!isCloseAfterExecute) {
				connection = cnn;
			}
			return cnn;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			HiLog.error(String.format(
					"connection string (%s) create connection failed, beacuse:%s",
					conn_str, e.getMessage()));
			return null;
		}
	}

	@Override
	public Connection createConnection(String conn, String user, String pwd, boolean isTrans) {
		if (isTrans) {
			try {
				return  (Connection) DriverManager.getConnection(conn, user, pwd);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				HiLog.error(String.format(
						"connection string (%s) create connection failed, beacuse:%s",
						conn, e.getMessage()));
				return null;
			}
		} else {
			return createConnection(conn, user, pwd);
		}
	}

	@Override
	public void closeConnection(Connection conn) {
		closeConnection(conn, false);		
	}

	@Override
	public void closeConnection() {
		if (!isCloseAfterExecute && connection != null) {
			closeConnection(connection, true);		
			connection = null;
		}
		
	}

	@Override
	public void closeConnection(Connection conn, boolean isTrans) {
		if (!isCloseAfterExecute && !isTrans) {
			return;
		}
		if (conn == null) {
			return;
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn = null;
	}

	private boolean isCloseAfterExecute;
	private Connection connection;

	public boolean isCloseAfterExecute() {
		return isCloseAfterExecute;
	}

	public void setCloseAfterExecute(boolean isCloseAfterExecute) {
		this.isCloseAfterExecute = isCloseAfterExecute;
	}
}
