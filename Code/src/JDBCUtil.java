/*
 * Title：JDBCUtil.java
 * Created by 栗子 at  01/06/2021 01:28:28
 * Github：https://github.com/jordanpep/DB2-JLU
 */

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class JDBCUtil {

    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    private ResultSetMetaData rsmt;
    //    public static final String URL = "jdbc:mysql://www.peppal.cn:3306/db2";
//    public static final String USER = "db2";
//    public static final String PASSWORD = "2ymdnWpTnZLNdnnE";
    public static final String URL = "jdbc:db2://192.168.44.131:50000/SAMPLE";
    public static final String USER = "db2admin";
    public static final String PASSWORD = "db2admin";

    private void conn() {
        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver");
//            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int update(String sql) {
        System.out.println("update 执行 sql 语句：" + sql);
        int rows = 0;
        try {
            conn();
            stmt = conn.createStatement();
            rows = stmt.executeUpdate(sql);
        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(
//                    null,
//                    "Error reading message.\n> SQL State: "
//                            + e.getSQLState()
//                            + "\n> Message: "
//                            + e.getMessage(),
//                    "Error Code: " + e.getErrorCode(),
//                    JOptionPane.ERROR_MESSAGE
//            );
            JOptionPane.showMessageDialog(null, "数据溢出","错误",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            close();
        }
        return rows;
    }

    public ArrayList<HashMap<String, String>> query(String sql) {
        System.out.println("query 执行 sql 语句：" + sql);
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            conn();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            rsmt = rs.getMetaData();
            while (rs.next()) {
                HashMap<String, String> mp = new HashMap<String, String>();
                for (int i = 1; i <= rsmt.getColumnCount(); i++) {
                    mp.put(rsmt.getColumnName(i), rs.getString(i));
                }
                list.add(mp);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error reading message.\n> SQL State: "
                            + e.getSQLState()
                            + "\n> Message: "
                            + e.getMessage(),
                    "Error Code: " + e.getErrorCode(),
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        } finally {
            close();
        }
        return list;
    }

    private void close() {
        // TODO Auto-generated method stub
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error reading message.\n> SQL State: "
                            + e.getSQLState()
                            + "\n> Message: "
                            + e.getMessage(),
                    "Error Code: " + e.getErrorCode(),
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }
}
