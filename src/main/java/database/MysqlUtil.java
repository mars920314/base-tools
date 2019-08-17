package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Properties;

/*
	1. DriverManager：管理一组 JDBC 驱动程序的基本服务
	* getConnection(String url, String user, String password)：试图建立到给定数据库URL的连接, 返回类型Connection
	2. Connection：与特定数据库的连接
	* createStatement()：创建一个Statement对象来将SQL语句发送到数据库
	* close()：立即释放此连接
	* setAutoCommit(boolean autoCommit)：将此连接的自动提交模式设置为给定状态，默认为自动提交
	* commit()：当设置为手动提交时，则执行update，delete，insert的时候都要调用此方法
	* setAutoCommit(boolean autoCommit)：将此连接的自动提交模式设置为给定状态
	* setSavepoint()：在当前事务中创建一个未命名的保存点savepoint，并返回表示它的savepoint
	3. Statement：用于执行静态SQL语句并返回它所生成结果的对象
	* executeQuery(String sql)：执行给定的SQL语句，该语句返回单个ResultSet对象
	* executeUpdate(String sql)：执行给定SQL语句，该语句可能为INSERT、UPDATE、DELETE语句，或者不返回任何内容的SQL语句（如SQL DDL语句）。返回类型为int
	* close()：立即释放此Statement
	4. PreparedStatement：Statement的子接口，表示预编译的SQL语句的对象
	* executeQuery()：在此PreparedStatement对象中执行SQL查询，并返回该查询生成的ResultSet对象
	* executeUpdate()：同理
	* setString(int parameterIndex, String x)：将指定参数设置为给定Java String值
	5. ResultSet：表示数据库结果集的数据表
	* getString(int columnIndex)：以Java编程语言中String的形式获取此ResultSet对象的当前行中指定列的值
	* getObject(int columnIndex)
	* next()：将光标从当前位置向前移一行，返回Boolean类型的值
	* getMetaData()：获取此ResultSet对象的列的编号、类型和属性。返回类型ResultSetMetaData
	6. ResultSetMetaData：用于获取关于ResultSet对象中列的类型和属性信息的对象
	* getColumnCount()：返回此ResultSet对象中的列数。返回类型为int
 */
public class MysqlUtil {
	
	private LinkedList<Connection> connectionPool = null;
	
	private Integer pool = 20;
	private String url = null;
	private String user= null;
	private String password = null;
	
	public MysqlUtil(Properties prop){
		if(prop==null)
			return;
		this.connectionPool = new LinkedList<Connection>();
		if(prop.contains("pool"))
			this.pool = Integer.valueOf(prop.getProperty("pool"));
		if(prop.contains("url"))
			this.url = prop.getProperty("url");
		if(prop.contains("user"))
			this.user = prop.getProperty("user");
		if(prop.contains("password"))
			this.password = prop.getProperty("password");
		try {
			// 驱动注册程序  --内部执行了RegisterDriver
			Class.forName("com.mysql.jdbc.Driver");
			for(int i=0;i<this.pool;i++){
				Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
				this.connectionPool.add(connection);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ResultSet executeQuery(String sql) throws SQLException{
		// 获取连接对象
		Connection connection = this.connectionPool.poll();
		if(connection!=null){
			// 创建Statement
			Statement statement = connection.createStatement();
			// 发送sql语句，执行sql语句，得到返回结果
			ResultSet resultSet = statement.executeQuery(sql);
			// 关闭Statement，返还连接对象。如果抛出异常，则不会返还错误的连接对象
			statement.close();
			this.connectionPool.add(connection);
			return resultSet;
		}
		else
			return null;
	}
	
	public int executeUpdate(String sql) throws SQLException{
		// 获取连接对象
		Connection connection = this.connectionPool.poll();
		if(connection!=null){
			// 创建Statement
			Statement statement = connection.createStatement();
			// 发送sql语句，执行sql语句，得到返回结果
			int result = statement.executeUpdate(sql);
			// 关闭Statement，返还连接对象。如果抛出异常，则不会返还错误的连接对象
			statement.close();
			this.connectionPool.add(connection);
			return result;
		}
		else
			return -1;
	}

}
