package hij.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import hij.db.impl.DBHelperImpl;

/**
 * 数据库相关辅助类
 * @author XuminRong
 *
 */
public class DBHelper {

	/**
	 * 取得ResultSet中的值
	 * @param t
	 * @param set
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 */
	public static <T> T readValue(Class<T> t, ResultSet set, int columnIndex) throws SQLException {
		return DBHelperImpl.readValue(t, set, columnIndex);
	}
	

	/**
	 * 取得ResultSet中的值
	 * @param t
	 * @param set
	 * @param field
	 * @return
	 * @throws SQLException
	 */
	public static <T> T readValue(Class<T> t, ResultSet set, String field) throws SQLException {
		return DBHelperImpl.readValue(t, set, field);
	}
	
	/**
	 * 根据ResultSet装载对象
	 * @param cls
	 * @param t
	 * @param set
	 * @return
	 * @throws SQLException
	 */
	public static <T> boolean fillObject(Class<T> cls, T t, ResultSet set) throws SQLException {
		return DBHelperImpl.fillObject(cls, t, set);
	}

	public static <T> T createObject(Class<T> cls, ResultSet set) {
		return DBHelperImpl.createObject(cls,set);
	}
	
	/**
	 * 取得Set的列表 
	 * @param cls
	 * @param set
	 * @return
	 * @throws SQLException
	 */
	public static <T> List<T> getResultsList(Class<T> cls, ResultSet set) throws SQLException {
		return DBHelperImpl.getResultsList(cls,set);
	}

	/**
	 * 创建某行的对象
	 * @param cls
	 * @param set
	 * @param index
	 * @return
	 */
	public static <T> T getResultData(Class<T> cls, ResultSet set, int index) {
		return DBHelperImpl.getResultData(cls,set, index);
	}
	
	/**
	 * 取得第一个对象
	 * @param cls
	 * @param set
	 * @return
	 */
	public static <T> T getFirst(Class<T> cls, ResultSet set) {
		return DBHelperImpl.getFirst(cls, set);
	}
}
