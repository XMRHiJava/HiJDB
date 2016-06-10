package hij.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import hij.db.impl.ConnectionProxy;
import hij.db.impl.DBOperateImpl;
import hij.util.generic.HiResult;
import hij.util.generic.IActionP1;
import hij.util.generic.IFunc;
import hij.util.generic.IFuncP1;

/**
 * ���ݿ⽻����
 * @author XuminRong
 *
 * @version 1.0.0.0
 */
public final class DBOperate {

	/**
	 * SQLServer���ݿ�(����ѡ�����ݿ�����)
	 */
	public static final int SQL_Server = 1;
	
	/**
	 * Oracle���ݿ�(����ѡ�����ݿ�����)
	 */
	public static final int Oracle = 2;
	
	/**
	 * MySQL���ݿ�(����ѡ�����ݿ�����)
	 */
	public static final int MySQL = 3;
	
	/**
	 * @param conn_str ���ݿ������ַ���
	 * @param db_type ���ݿ�����,�����final����
	 * @param is_close_after_execute
	 */
	public DBOperate(String conn_str, String user, String pwd, int db_type, boolean is_close_after_execute){
		impl = new DBOperateImpl(conn_str, user, pwd, db_type, is_close_after_execute);
	}
	
	/**
	 * @param conn_str
	 * @param db_type
	 */
	public DBOperate(String conn_str, String user, String pwd, int db_type){
		impl = new DBOperateImpl(conn_str, user, pwd, db_type, true);
	}
	
	
	/**
	 * �ر����ݿ�����
	 */
	public void close(){
		impl.close();
	}
	
	/**
	 * @param sql
	 * @return
	 * @throws SQLException 
	 */
	public int executeNoQuery(String sql) throws SQLException{
		return executeNoQuery(sql, null);
	}
	
	/**
	 * @param sql
	 * @param callback
	 * @return
	 * @throws SQLException
	 */
	public int executeNoQuery(String sql, IActionP1<PreparedStatement> callback) throws SQLException {
	    return impl.executeNoQuery(sql, callback);
	}
	
	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public Object executeScalar(String sql) throws SQLException {
		return executeScalar(sql, null);
	}
	
	/**
	 * @param sql
	 * @param callback
	 * @return
	 * @throws SQLException
	 */
	public Object executeScalar(String sql, IActionP1<PreparedStatement> callback) throws SQLException {
		return executeScalar(Object.class, sql, callback);
	}
	
	/**
	 * @param t
	 * @param sql
	 * @return
	 * @throws SQLException 
	 */
	public <T> T executeScalar(Class<T> t, String sql) throws SQLException{
		return executeScalar(t, sql, null);
	}
	
	/**
	 * @param t
	 * @param sql
	 * @param callback
	 * @return
	 * @throws SQLException
	 */
	public <T> T executeScalar(Class<T> t, String sql, IActionP1<PreparedStatement> callback) throws SQLException{
		return impl.executeScalar(t, sql, callback);
	}

	
	/**
	 * ִ�в�ѯ
	 * @param sql
	 * @param setCallback ��Ҫ�Խ�����еĴ���
	 * @param callback ��Ҫ������ز���
	 * @return
	 * @throws SQLException
	 */
	public boolean  executeQuery(String sql, IFuncP1<ResultSet, Boolean> setCallback, IActionP1<PreparedStatement> callback) throws SQLException{
		return impl.executeQuery(sql, setCallback, callback);
	}

	/**
	 * ���ض����б�
	 * @param cls
	 * @param sql
	 * @return
	 */
	public <T> List<T> executeQuery(Class<T> cls, String sql) {
		return executeQuery(cls, sql, null);
	}
		
	/**
	 * ���ض����б�
	 * @param cls
	 * @param sql
	 * @param callback
	 * @return
	 */
	public <T> List<T> executeQuery(Class<T> cls, String sql, IActionP1<PreparedStatement> callback) {
		try {
			final HiResult<List<T>> result = new HiResult<List<T>>();
			result.set(null);
			impl.executeQuery(sql, new IFuncP1<ResultSet, Boolean>(){

				@Override
				public Boolean handle(ResultSet v) {
					List<T> lst = null;
					try {
						lst = DBHelper.getResultsList(cls, v);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}
					result.set(lst);
					return lst != null;
				}				
			}, callback);
			return result.get();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(sql);
			return null;
		}
	}

	/**
	 * ���ص�һ�д����Ķ���
	 * @param cls
	 * @param sql
	 * @return
	 */
	public <T> T executeQuerySingle(Class<T> cls, String sql) {
		return executeQuerySingle(cls, sql, null);
	}
	
	/**
	 * ���ص�һ�д����Ķ���
	 * @param cls
	 * @param sql
	 * @param callback
	 * @return
	 */
	public <T> T executeQuerySingle(Class<T> cls, String sql, IActionP1<PreparedStatement> callback) {
		try {
			final HiResult<T> result = new HiResult<T>();
			result.set(null);
			impl.executeQuery(sql, new IFuncP1<ResultSet, Boolean>(){

				@Override
				public Boolean handle(ResultSet v) {
						T t = DBHelper.getFirst(cls, v);
						result.set(t);
						return true;
				}				
			}, callback);
			return result.get();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(sql);
			return null;
		}
	}

	/**
	 * @param sql
	 * @param setCallback
	 * @return
	 * @throws SQLException 
	 */
	public boolean  executeQuery(String sql, IFuncP1<ResultSet, Boolean> setCallback) throws SQLException{
		return executeQuery(sql, setCallback, null);
	}
	
	/**
	 * @param evt
	 */
	public void onTrans(IFuncP1<Connection, Boolean> evt) {
		impl.onTrans(evt);
	}

	/**
	 * ���Ҫ֧�ֵ����ݿ����Ͳ��ṩ��������
	 * @param type
	 * @param creator
	 * @return
	 */
	public static boolean addDBCreator(int type, IFunc<ICreator> creator) {
		return ConnectionProxy.addDBCreator(type, creator);
	}
	
	DBOperateImpl impl = null;
}
