package hij.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import hij.util.HiCBO;
import hij.util.HiTypeHelper;
import hij.util.generic.IFuncP1;

/**
 * 数据库操作辅助类
 * @author XuminRong
 *
 */
public class DBHelperImpl {
	/**
	 * 取得ResultSet中的值
	 * @param t
	 * @param set
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readValue(Class<T> t, ResultSet set, int columnIndex) throws SQLException {
		if (t == null || set == null) {
			return HiTypeHelper.getDefault(t);
		}		
		
		if (set.getRow() < 1) {
			return HiTypeHelper.getDefault(t);
		}
		
		if (t == Object.class) {
			return (T)set.getObject(columnIndex);
		}
		if (t == Integer.class) {
			Object val = set.getInt(columnIndex);
			return (T)val;
		}
		if (t == int.class) {
			Object val = set.getInt(columnIndex);
			return (T)val;
		}
		if (t == short.class) {
			Object val = set.getShort(columnIndex);
			return (T)val;
		}
		if (t == Boolean.class || t == boolean.class) {
			Object val = set.getBoolean(columnIndex);
			return (T)val;
		}
		if (t == long.class) {
			Object val = set.getLong(columnIndex);
			return (T)val;
		}
		if (t == float.class) {
			Object val = set.getFloat(columnIndex);
			return (T)val;
		}
		if (t == double.class) {
			Object val = set.getDouble(columnIndex);
			return (T)val;
		}
		if (t == String.class) {
			Object val = set.getString(columnIndex);
			return (T)val;
		}
		if (t == java.sql.Date.class) {
			Object val = set.getDate(columnIndex);
			return (T)val;
		}
		if (t == java.sql.Time.class) {
			Object val = set.getTime(columnIndex);
			return (T)val;
		}
		if (t == java.sql.Timestamp.class) {
			Object val = set.getTimestamp(columnIndex);
			return (T)val;
		}
		if (t == Byte.class) {
			Object val = set.getByte(columnIndex);
			return (T)val;
		}
		return (T)set.getObject(columnIndex);		
	}
	

	/**
	 * 取得ResultSet中的值
	 * @param t
	 * @param set
	 * @param columns
	 * @param field
	 * @return
	 * @throws SQLException
	 */
	public static <T> T readValue(Class<T> t, ResultSet set, String field) throws SQLException {
		if (t == null || set == null || field == null) {
			return HiTypeHelper.getDefault(t);
		}
		int index = set.findColumn(field);
		if (index < 1) {
			return HiTypeHelper.getDefault(t);
		}
		return readValue(t, set, index);
	}
	
	/**
	 * 根据ResultSet装载对象
	 * @param cls
	 * @param t
	 * @param set
	 * @param columns
	 * @return
	 * @throws SQLException
	 */
	public static <T> boolean fillObject(Class<T> cls, T t, ResultSet set) throws SQLException {
		if (cls == null || t == null || set == null) {
			return false;
		}
		return HiCBO.fillObjectEx(t, cls, new IFuncP1<String, Object>(){
			@Override
			public final Object handle(String v) {
				try {
				return readValue(Object.class, set,v);
				} catch (Exception ex) {
					ex.printStackTrace();
					return null;
				}
			}			
		});
	}
	
	/**
	 * 创建并装载对象数据
	 * @param cls
	 * @param set
	 * @return
	 */
	public static <T> T createObject(Class<T> cls, ResultSet set) {
		try
		{
			T t = cls.newInstance();
			fillObject(cls, t, set);		
			return t;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 取得Set的列表
	 * @param cls
	 * @param set
	 * @return
	 * @throws SQLException
	 */
	public static <T> List<T> getResultsList(Class<T> cls, ResultSet set) throws SQLException {
		if (cls == null || set == null) {
			return null;
		}		
		
		if (!set.first()) {
			return null;
		}
		
		List<T> list = new ArrayList<T>();
		T t = getFirst(cls, set);
		if (t != null) {
			list.add(t);
		}
		while (set.next()) {
			T it = createObject(cls, set);
			if (it != null) {
				list.add(it);
			}
		}
		return list;
	}

	/**
	 * 创建某行的对象
	 * @param cls
	 * @param set
	 * @param index
	 * @param columns
	 * @return
	 */
	public static <T> T getResultData(Class<T> cls, ResultSet set, int index) {
		if (set == null || cls  == null) {
			return null;
		}
		
		try
		{
			T t = cls.newInstance();
			if (!set.absolute(index)) {
				return null;
			}
			if (!fillObject(cls, t, set)) {
				return null;
			}
			return t;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 取得第一个对象
	 * @param cls
	 * @param set
	 * @return
	 */
	public static <T> T getFirst(Class<T> cls, ResultSet set) {
		return getResultData(cls, set, 1);
	}
}
