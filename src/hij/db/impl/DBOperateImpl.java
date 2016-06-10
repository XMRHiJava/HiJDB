package hij.db.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import hij.db.DBHelper;
import hij.util.HiTypeHelper;
import hij.util.generic.IActionP1;
import hij.util.generic.IFuncP1;

public final class DBOperateImpl {	
	/**
	 * @param conn_str 数据库连接字符串
	 * @param db_type 数据库类型,请参阅final变量
	 * @param is_close_after_execute
	 */
	public DBOperateImpl(String conn_str, String user, String pwd, int db_type, boolean is_close_after_execute){
		connProxy.init(conn_str, user, pwd, db_type, is_close_after_execute);
	}
	
	/**
	 * 关闭数据库连接
	 * @throws SQLException 
	 */
	public void close() {
		connProxy.close();
	}
	
	/**
	 * @param sql
	 * @param callback
	 * @return
	 * @throws SQLException
	 */
	public int executeNoQuery(String sql, IActionP1<PreparedStatement> callback) throws SQLException {
	    Connection conn = connProxy.getConnection();
	    if (conn == null) {
	    	return -1;
	    }
	    PreparedStatement pstmt = null;
	    try
	    {
	    	pstmt = (PreparedStatement) conn.prepareStatement(sql);
	    	if (callback != null) {
	    		callback.action(pstmt);
	    	}
	        return pstmt.executeUpdate();
	    }
	    finally
	    {
	    	if (pstmt != null) {
	    		pstmt.close();
	    	}	    	
	    	connProxy.closeAfterExecute(conn);
	    }
	}
	
	/**
	 * @param t
	 * @param sql
	 * @param callback
	 * @return
	 * @throws SQLException
	 */
	public <T> T executeScalar(Class<T> t, String sql, IActionP1<PreparedStatement> callback) throws SQLException{
	    Connection conn = connProxy.getConnection();
	    if (conn == null) {
	    	return HiTypeHelper.getDefault(t);
	    }
	    PreparedStatement pstmt = null;
	    try
	    {
	    	pstmt = (PreparedStatement) conn.prepareStatement(sql);
	    	if (callback != null) {
	    		callback.action(pstmt);
	    	}
	    	ResultSet set = pstmt.executeQuery();
	    	if (set == null) {
	    		return HiTypeHelper.getDefault(t);
	    	}
	    	if (!set.first()) {
	    		return HiTypeHelper.getDefault(t);
	    	}
	        return DBHelper.readValue(t, set, 1);
	    }
	    finally
	    {
	    	if (pstmt != null) {
	    		pstmt.close();
	    	}
	    	
	    	connProxy.closeAfterExecute(conn);
	    }
	}
	
	
	/**
	 * @param sql
	 * @return
	 * @throws SQLException 
	 */
	public boolean  executeQuery(String sql, IFuncP1<ResultSet, Boolean> setCallback, IActionP1<PreparedStatement> callback) throws SQLException{
	    Connection conn = connProxy.getConnection();
	    if (conn == null) {
	    	return false;
	    }
	    PreparedStatement pstmt = null;
	    try
	    {
	    	pstmt = (PreparedStatement) conn.prepareStatement(sql);
	    	if (callback != null) {
	    		callback.action(pstmt);
	    	}
	    	ResultSet set = pstmt.executeQuery();
	    	if (set == null) {
	    		return false;
	    	}
	    	if (setCallback != null) {
	    		return setCallback.handle(set);
	    	}
	        return false;
	    }
	    finally
	    {
	    	if (pstmt != null) {
	    		pstmt.close();
	    	}
	    	
	    	connProxy.closeAfterExecute(conn);
	    }
	}
	
	/**
	 * @param evt
	 * @throws SQLException 
	 */
	public boolean onTrans(IFuncP1<Connection, Boolean> evt)  {
		Connection conn = connProxy.getConnection();
	    if (conn == null) {
	    	return false;
	    }
	    try
	    {
	    	conn.setAutoCommit(false);
	    	Boolean ret = evt.handle(conn);
	    	if (ret) {
		    	conn.commit();
		    	return true;
	    	} else {
	    		conn.rollback();
		        return false;
	    	}
	    } catch(Exception ex) {
	    	ex.printStackTrace();	    	
	    	try {
				conn.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return false;
	    }
	    finally
	    {	  
	    	connProxy.closeAfterExecute(conn, true);
	    }
	}
		
	ConnectionProxy connProxy = new ConnectionProxy();
}
