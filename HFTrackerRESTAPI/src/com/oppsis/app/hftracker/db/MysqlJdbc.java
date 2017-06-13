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
        Connection connection = null; // ���������������ݿ��Connection����
        try {
            Class.forName(jdbcDriver);// ����Mysql��������
            /**
             * 127.0.0.1ָ��������������Զ�̷������,����дԶ�̻�����ip 3306 mysqlĬ�ϵĶ˿ں� test ���ݿ�����
             * user ���ݿ��û����� password ����
             */
            connection = DriverManager.getConnection(jdbcUrl, user, password);// ������������

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("����mysql����ʧ��");
        }
        return connection; // ���������������ݿ�����
    }

    /**
     * ��mysql�������ݼ�¼ ���ز������ݵĸ���
     * 
     * @param sql
     *            Ҫ�����sql���
     * @return count �������ݵĸ���
     * @throws Exception
     */
    public int insert(String sql) throws Exception {
        conn = getConnection(); // ���ӵ����ݿ�
        try {
            statement = (Statement) conn.createStatement(); // ��������ִ�о�̬sql����Statement����
            int count = statement.executeUpdate(sql); // ִ�в��������sql���
            conn.close(); // �ر����ݿ�����
            return count;// ���ز������ݵĸ���
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("��������ʧ��");
        }
    }

    /**
     * ���·���Ҫ��ļ�¼ ���ظ��µļ�¼��Ŀ
     * 
     * @param sql
     *            �������ݵ�sql���
     * @return count �������ݵĸ���
     * @throws Exception
     */
    public  int update(String sql) throws Exception {
        conn = getConnection(); // ���ӵ����ݿ�
        try {
            // ��������ִ�о�̬sql����Statement����
            statement = (Statement) conn.createStatement();
            int count = statement.executeUpdate(sql);// ִ�и��²�����sql��䣬
            conn.close(); // �ر����ݿ�����
            return count; // ���ظ������ݵĸ���
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("��������ʧ��");
        }
    }

    /**
     * ��ѯ���ݿ⣬���ط���Ҫ��ļ�¼������
     * 
     * @param sql ��ѯ���ݵ�sql���
     * @throws Exception
     * @return list 
     */
    public  List<Object> query(String sql) throws Exception {

        conn = getConnection(); // ���ӵ����ݿ�
        try {
            statement = (Statement) conn.createStatement(); // ��������ִ�о�̬sql����Statement����
            ResultSet rs = statement.executeQuery(sql); // ִ��sql��ѯ��䣬���ز�ѯ���ݵĽ����
            List<Object> list=ResultSetToList(rs);
            conn.close(); // �ر����ݿ�����
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("��ѯ����ʧ��");
        }
    }
    /* ɾ������Ҫ��ļ�¼��������*/  
    /**
     * 
     * @param sql ɾ�����ݵ�sql���
     * @return count ����ɾ�����ݵ�����
     * @throws Exception
     */
    public  int delete(String sql) throws Exception {  
        conn = getConnection(); //���ӵ����ݿ�  
        try {  
            statement = (Statement) conn.createStatement();    //��������ִ�о�̬sql����Statement����
            int count = statement.executeUpdate(sql);// ִ��sqlɾ�����  
            conn.close();   //�ر����ݿ�����  
              return count;//����ɾ�����ݵ�����
        } catch (SQLException e) {  
            e.printStackTrace();
            throw new Exception("ɾ������ʧ��");
        }  
          
    }
    /**
     * ��ҳ����
     * @param sql Ҫ���ҵ�sql���
     * @param page ҳ��
     * @param count ��������
     * @return List<Object>
     * @throws Exception
     */
    public  List<Object> findByPage(String sql,  
            int page,int count) throws Exception {  
          conn = getConnection(); //���ӵ����ݿ�  
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
     * ResultSet ת����List
     */
    public  List<Object> ResultSetToList(ResultSet rs) throws SQLException{
        if (rs == null)
            return Collections.emptyList();
        ResultSetMetaData md = rs.getMetaData(); // �õ������(rs)�Ľṹ��Ϣ
        int columnCount = md.getColumnCount(); // ���ش� ResultSet �����е�����
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