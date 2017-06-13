package com.oppsis.app.hftracker.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Statement;
import com.oppsis.app.hftracker.util.PropertyUtil;

public class MysqlJdbc {
    protected  Connection conn;
    protected  Statement statement;

    private static String jdbcUrl;
    private static String jdbcDriver;
    private static String user;
    private static String password;
    
    static{
    	jdbcDriver = PropertyUtil.getInstance().getProperty("db.driver");
    	jdbcUrl = PropertyUtil.getInstance().getProperty("db.url");
    	user = PropertyUtil.getInstance().getProperty("db.user");
    	password = PropertyUtil.getInstance().getProperty("db.password");
    }
    
    public Connection getConnection() throws Exception {
        Connection connection = null; // 创建用于连接数据库的Connection对象
        try {
            Class.forName(jdbcDriver);// 加载Mysql数据驱动
            /**
             * 127.0.0.1指本机，若是链接远程服务机器,则填写远程机器的ip 3306 mysql默认的端口号 test 数据库名称
             * user 数据库用户名称 password 密码
             */
            connection = DriverManager.getConnection(jdbcUrl, user, password);// 创建数据连接

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("链接mysql数据失败");
        }
        return connection; // 返回所建立的数据库连接
    }

    /**
     * 向mysql插入数据记录 返回插入数据的个数
     * 
     * @param sql
     *            要插入的sql语句
     * @return count 插入数据的个数
     * @throws Exception
     */
    public int insert(String sql) throws Exception {
        conn = getConnection(); // 连接到数据库
        try {
            statement = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象
            int count = statement.executeUpdate(sql); // 执行插入操作的sql语句
            conn.close(); // 关闭数据库连接
            return count;// 返回插入数据的个数
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("插入数据失败");
        }
    }

    /**
     * 更新符合要求的记录 返回更新的记录数目
     * 
     * @param sql
     *            更新数据的sql语句
     * @return count 更新数据的个数
     * @throws Exception
     */
    public  int update(String sql) throws Exception {
        conn = getConnection(); // 连接到数据库
        try {
            // 创建用于执行静态sql语句的Statement对象，
            statement = (Statement) conn.createStatement();
            int count = statement.executeUpdate(sql);// 执行更新操作的sql语句，
            conn.close(); // 关闭数据库连接
            return count; // 返回更新数据的个数
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("更新数据失败");
        }
    }

    /**
     * 查询数据库，返回符合要求的记录的数据
     * 
     * @param sql 查询数据的sql语句
     * @throws Exception
     * @return list 
     */
    public  List<Object> query(String sql) throws Exception {

        conn = getConnection(); // 连接到数据库
        try {
            statement = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象
            ResultSet rs = statement.executeQuery(sql); // 执行sql查询语句，返回查询数据的结果集
            List<Object> list=ResultSetToList(rs);
            conn.close(); // 关闭数据库连接
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("查询数据失败");
        }
    }
    /* 删除符合要求的记录，输出情况*/  
    /**
     * 
     * @param sql 删除数据的sql语句
     * @return count 返回删除数据的数量
     * @throws Exception
     */
    public  int delete(String sql) throws Exception {  
        conn = getConnection(); //连接到数据库  
        try {  
            statement = (Statement) conn.createStatement();    //创建用于执行静态sql语句的Statement对象
            int count = statement.executeUpdate(sql);// 执行sql删除语句  
            conn.close();   //关闭数据库连接  
              return count;//返回删除数据的数量
        } catch (SQLException e) {  
            e.printStackTrace();
            throw new Exception("删除数据失败");
        }  
          
    }
    /**
     * 分页查找
     * @param sql 要查找的sql语句
     * @param page 页数
     * @param count 数据条数
     * @return List<Object>
     * @throws Exception
     */
    public  List<Object> findByPage(String sql,  
            int page,int count) throws Exception {  
          conn = getConnection(); //连接到数据库  
        PreparedStatement pre = conn.prepareStatement(sql);  
        pre.setMaxRows(count);  
        ResultSet rs = pre.executeQuery();  
        if(page<1){
            rs.absolute(0);  
        }else{
            page=page*count-1;
            rs.absolute(page);  
        }
        List<Object> list=ResultSetToList(rs);
        return list;  
    }  
    /**
     * ResultSet 转换成List
     */
    public  List<Object> ResultSetToList(ResultSet rs) throws SQLException{
        if (rs == null)
            return Collections.emptyList();
        ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息
        int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
        List<Object> list = new ArrayList<Object>();
        Map<Object, Object> rowData = new HashMap<Object, Object>();
        while (rs.next()) {
            rowData = new HashMap<Object, Object>(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }


}