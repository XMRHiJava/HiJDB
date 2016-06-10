package hij.db;

import java.sql.Connection;

/**
 * ���ݿ������������ṩ�ӿ�
 * @author XuminRong
 *
 */
public interface ICreator {

	/**
	 * ������ݿ�������Ϣ
	 * @return
	 */
	public String getDriver();
	
	/**
	 * �������ݿ�����
	 * @param conn
	 * @param user
	 * @param pwd
	 * @return
	 */
	public Connection createConnection(String conn, String user, String pwd);

	
	/**
	 * �������ݿ�����
	 * @param conn
	 * @param user
	 * @param pwd
	 * @param isTrans
	 * @return
	 */
	public Connection createConnection(String conn, String user, String pwd, boolean isTrans);
	
	/**
	 * �ر����ݿ�����
	 * @param conn
	 */
	public void closeConnection(Connection conn);
	/**
	 * �ر����ݿ�����
	 */
	public void closeConnection();
	
	/**
	 * @param conn
	 * @param isTrans
	 */
	public void closeConnection(Connection conn, boolean isTrans);
	
	/**
	 * ִ����������Ƿ�ر�
	 * @param isCloseAfterExecute
	 */
	public void setCloseAfterExecute(boolean isCloseAfterExecute);
}
