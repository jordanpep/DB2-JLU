/*
 * Title：lab11.java
 * Created by 栗子 at  01/06/2021 01:28:28
 * Github：https://github.com/jordanpep/DB2-JLU
 */
import java.sql.*;

public class lab11 {
    private static Connection createConnection() {
        Connection conn = null;
        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver");
            String url = "jdbc:db2://192.168.44.131:50000/SAMPLE";
            conn = DriverManager.getConnection(url, "db2admin", "db2admin");
        } catch (SQLException ex1) {
            ex1.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return conn;
    }

    public static void main(String args[]) throws Exception {
        Connection con = createConnection();
        String resume = null;
        String empnum = "000130";//shuxing
        int startper = 0, startper1 = 0, startdpt = 0;
        String sql1 = null, sql2 = null, sql3 = null;
        String empno = null, resumefmt = null;
        Clob resumelob = null;
        sql1 = "SELECT POSSTR(RESUME,'Personal') "
                + "FROM JLU.EMP_RESUME "
                + "WHERE EMPNO = ? AND RESUME_FORMAT = 'ascii' ";
        PreparedStatement stmt1 = con.prepareStatement(sql1);
        stmt1.setString(1, empnum);
        ResultSet rs1 = stmt1.executeQuery();
        while (rs1.next()) {
            startper = rs1.getInt(1);
        } // end while
        sql2 = "SELECT POSSTR(RESUME,'Department') "
                + "FROM JLU.EMP_RESUME "
                + "WHERE EMPNO = ? AND RESUME_FORMAT = 'ascii' ";
        PreparedStatement stmt2 = con.prepareStatement(sql2);
        stmt2.setString(1, empnum);
        ResultSet rs2 = stmt2.executeQuery();
        while (rs2.next()) {
            startdpt = rs2.getInt(1);
        } // end while
        startper1 = startper - 1;
        sql3 = "SELECT EMPNO, RESUME_FORMAT, "
                + "SUBSTR(RESUME,1,?)|| SUBSTR(RESUME,?) AS RESUME "
                + "FROM JLU.EMP_RESUME "
                + "WHERE EMPNO = ? AND RESUME_FORMAT = 'ascii' ";
        PreparedStatement stmt3 = con.prepareStatement(sql3);
        stmt3.setInt(1, startper1);
        stmt3.setInt(2, startdpt);
        stmt3.setString(3, empnum);
        ResultSet rs3 = stmt3.executeQuery();
        while (rs3.next()) {
            empno = rs3.getString(1);
            System.out.println(empno);
            resumefmt = rs3.getString(2);
            System.out.println(resumefmt);
            resumelob = rs3.getClob(3);
            long len = resumelob.length();
            int len1 = (int) len;
            String resumeout = resumelob.getSubString(1, len1);
        }
    } // end while


}

