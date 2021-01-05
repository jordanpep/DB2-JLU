/*
 * Title：User.java
 * Created by 栗子 at  01/06/2021 01:28:28
 * Github：https://github.com/jordanpep/DB2-JLU
 */

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private static JDBCUtil jdbc = new JDBCUtil();

    public String getEMPNO() {
        return EMPNO;
    }

    public void setEMPNO(String EMPNO) {
        this.EMPNO = EMPNO;
    }

    public String getFIRSTNME() {
        return FIRSTNME;
    }

    public void setFIRSTNME(String FIRSTNME) {
        this.FIRSTNME = FIRSTNME;
    }

    public String getLASTNAME() {
        return LASTNAME;
    }

    public void setLASTNAME(String LASTNAME) {
        this.LASTNAME = LASTNAME;
    }

    public String getSALARY() {
        return SALARY;
    }

    public void setSALARY(String SALARY) {
        this.SALARY = SALARY;
    }

    public String getBONUS() {
        return BONUS;
    }

    public void setBONUS(String BONUS) {
        this.BONUS = BONUS;
    }

    public String getEDLEVEL() {
        return EDLEVEL;
    }

    public void setEDLEVEL(String EDLEVEL) {
        this.EDLEVEL = EDLEVEL;
    }

    private String EMPNO = "";
    private String FIRSTNME = "null";
    private String LASTNAME = "null";
    private String SALARY = "0";
    private String BONUS = "0";
    private String EDLEVEL = "0";

    @Override
    public String toString() {
        return "EMPNO:" + EMPNO +
                "| FIRSTNME:'" + FIRSTNME + '\'' +
                "| LASTNAME:" + LASTNAME +
                "| SALARY:" + SALARY +
                "| BONUS:" + BONUS +
                "| EDLEVEL:" + EDLEVEL;

    }

    public String add() {
        if (FIRSTNME == "") {
            FIRSTNME = "null";
        }
        if (LASTNAME == "") {
            LASTNAME = "null";
        }
        if (EDLEVEL == null) {
            EDLEVEL = "0";
        }
        if (SALARY == "null") {
            SALARY = "0.0";
        }
        if (BONUS == "null") {
            BONUS = "0.0";
        }
        if (SALARY == "") {
            SALARY = "0.0";
        }
        if (BONUS == "") {
            BONUS = "0.0";
        }
        jdbc.update("INSERT INTO jlu.TEMPL (EMPNO,FIRSTNME,LASTNAME,EDLEVEL,SALARY,BONUS) VALUES(" +
                "'" + EMPNO + "','" + FIRSTNME + "','" + LASTNAME + "'," + EDLEVEL + "," + SALARY + "," + BONUS + ")");
        return toString() + "add Success!";
    }

    public String del() {
        jdbc.update("DELETE FROM jlu.TEMPL WHERE EMPNO = '" + EMPNO + "'");
        return toString() + "del Success";
    }

    public String update() {
        if (FIRSTNME == "") {
            FIRSTNME = "null";
        }
        if (LASTNAME == "") {
            LASTNAME = "null";
        }
        if (EDLEVEL == null) {
            EDLEVEL = "0";
        }
        if (SALARY == "null") {
            SALARY = "0.0";
        }
        if (BONUS == "null") {
            BONUS = "0.0";
        }
        jdbc.update("UPDATE jlu.TEMPL SET " + "FIRSTNME='" + FIRSTNME + "',LASTNAME='" + LASTNAME + "',EDLEVEL=" + EDLEVEL +
                ",SALARY=" + SALARY + ",BONUS=" + BONUS + " WHERE EMPNO = " + EMPNO);
        return toString() + "update Success";
    }

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
