package hij.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import hij.db.impl.DBHelperImpl;

/**
 * ���ݿ���ظ�����
 * @author XuminRong
 *
 */
public class DBHelper {

	/**
	 * ȡ��ResultSet�е�ֵ
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
	 * ȡ��ResultSet�е�ֵ
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
	 * ����ResultSetװ�ض���
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
	 * ȡ��Set���б� 
	 * @param cls
	 * @param set
	 * @return
	 * @throws SQLException
	 */
	public static <T> List<T> getResultsList(Class<T> cls, ResultSet set) throws SQLException {
		return DBHelperImpl.getResultsList(cls,set);
	}

	/**
	 * ����ĳ�еĶ���
	 * @param cls
	 * @param set
	 * @param index
	 * @return
	 */
	public static <T> T getResultData(Class<T> cls, ResultSet set, int index) {
		return DBHelperImpl.getResultData(cls,set, index);
	}
	
	/**
	 * ȡ�õ�һ������
	 * @param cls
	 * @param set
	 * @return
	 */
	public static <T> T getFirst(Class<T> cls, ResultSet set) {
		return DBHelperImpl.getFirst(cls, set);
	}
}
