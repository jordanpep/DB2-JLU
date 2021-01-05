/*
 * Title：lab1.java
 * Created by 栗子 at  01/06/2021 01:28:28
 * Github：https://github.com/jordanpep/DB2-JLU
 */

import java.sql.*;
import java.io.*;

import java.lang.*;

class lab1 {
    static {
        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String argv[]) {
        try {
            Connection con = null;
            String url = "jdbc:db2://192.168.44.131:50000/SAMPLE";
            if (argv.length == 0) {
                con = DriverManager.getConnection(url, "db2admin", "db2admin");
            } else if (argv.length == 2) {
                String userid = argv[0];
                String passwd = argv[1];
                con = DriverManager.getConnection(url, userid, passwd);

            } else {
                throw new Exception("\n Usage: java MyJDBC[,username,password]\n");
            }
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery
                    ("SELECT EMPNO, LASTNAME " +
                            " FROM jlu.TEMPL " +
                            " WHERE SALARY > 40000 ");
            while (rs.next()) {
                System.out.println("empno = " + rs.getString(1) +
                        "lastname = " + rs.getString(2));
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}