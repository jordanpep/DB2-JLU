/*
 * Title：lab4.java
 * Created by 栗子 at  01/06/2021 01:28:28
 * Github：https://github.com/jordanpep/DB2-JLU
 */
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.io.*;


public class lab4 {
    static {
        try {
            /******************* ??????????????? *****************/
            /* ( 1 ) Load the DB2 Driver                         */
            /*****************************************************/
            Class.forName("com.ibm.db2.jcc.DB2Driver"); // (1)
        } catch (Exception e) {
            System.exit(1);
        }
    }

    private void displayGUI() {
        javax.swing.JOptionPane.showConfirmDialog(null,
                getPanel(),
                "SQL",
                javax.swing.JOptionPane.OK_CANCEL_OPTION,
                javax.swing.JOptionPane.PLAIN_MESSAGE);
    }

    private JPanel getPanel() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Department number:");
        JLabel label1 = new JLabel("ANS:");
        JTextField jTextField = new JTextField();
        JTextField jTextField1 = new JTextField();
        jTextField.setEditable(true); // 设置输入框允许编辑
        jTextField.setColumns(5); // 设置输入框的长度为5个字符
        jTextField1.setEditable(false); // 设置输入框允许编辑
        jTextField1.setColumns(5); // 设置输入框的长度为5个字符
//        jTextField.setText("hi");
        JButton jButton = new JButton("查询");
        System.out.println("按钮被点击");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("按钮被点击");
                jTextField1.setText("hi");
                try {
                    String a = Integer.toString(check(Integer.parseInt(jTextField.getText())));
                    jTextField1.setText(a);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        jButton.setSize(50, 50);

        panel.add(label);
        panel.add(jTextField);
        panel.add(jButton);
        panel.add(label1);
        panel.add(jTextField1);
        return panel;
    }

    public static void main(String args[]) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new lab4().displayGUI();
            }
        });
    }

    private int check(int a) throws Exception {
        int rowCount = 0;
        int mydeptno = 0;
        String deptno = "";
        String outline = " ";
        String name = " ";
        String job = " ";
        String salary = "";
        /***********************************************************************/
        /* Header line                                                         */
        /***********************************************************************/
        String intext =
                "\n ID       NAME      SALARY\n";
        String indash =
                "--------  --------  --------------\n";
        String blanks = "                                                        ";

        /********************** ???????????????????? ***************************/
        /* ( 2 ) Define the variable SQLWarn that is used for SQLWarnings      */
        /***********************************************************************/
//   ??????????     ??????? = null;
        SQLWarning SQLWarn = null;

        /********************** ???????????????????? ****************************/
        /* ( 3 ) Connect to the DB2 Database SAMPLE.                            */
        /************************************************************************/
        String url = "jdbc:db2://192.168.44.131:50000/SAMPLE";
        Connection sample = DriverManager.getConnection(url, "db2admin", "db2admin");
        System.out.println("\n Set AutoCommit off");
        sample.setAutoCommit(false);
        System.out.println("\n Autocommit off");
        try {
            System.out.println("\n Enter the Department number\n");
//            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String s;
//            s = in.readLine();
            s = Integer.toString(a);
            deptno = s.substring(0, 2);
            mydeptno = Integer.parseInt(deptno);

        }  // End try
        catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        try {

            /******************* ??????????????????????? *************************/
            /* ( 4 ) Instantiate the PreparedStatement object name stmt.         */
            /*       Use the prepareStatement() method.                          */
            /*********************************************************************/
            PreparedStatement stmt = sample.prepareStatement(
                    "select id, name,salary from jlu.staff where Dept = ?");
            /******************* ??????????????????????? *************************/
            /* ( 5 ) Set the parameter in the PreparedStatement object stmt to   */
            /*       the variable name mydeptno.                                 */
            /*********************************************************************/
            stmt.setInt(1, mydeptno);
            /******************* ??????????????????????? *************************/
            /* ( 6 ) Declare the ResultSet object rs and assign the results      */
            /*       of the SQL select statement.                                */
            /*********************************************************************/
            ResultSet rs = stmt.executeQuery();
            /************************* ??????????????? ************************/
            /* ( 7 ) If SQLWarning occurs display the warning                 */
            /******************************************************************/
            if ((SQLWarn = stmt.getWarnings()) != null) {
                System.out.println("\n Value of SQLWarn on single row insert to DEP is: \n");
                System.out.println(SQLWarn);
            } // end if
            /************************* ???????????????? ***********************/
            /* ( 8 ) Use the ResultSet next() method to retrieve the first    */
            /*       row of the ResultSet.                                    */
            /******************************************************************/
            boolean more = rs.next();
            System.out.println(intext);
            System.out.println(indash);


            while (more) {
                rowCount++;
                name = rs.getString(1);
                job = rs.getString(2);
                salary = rs.getString(3);
                outline = (name + blanks.substring(0, 10 - name.length())) +
                        (job + blanks.substring(0, 10 - job.length())) +
                        (salary + blanks.substring(0, 12 - salary.length()));
                System.out.println("\n" + outline);
                /******************* ????????????????????? ********************/
                /* ( 9 ) Retrieve the next row of the Result Set              */
                /**************************************************************/
                more = rs.next();
            }
        } catch (Exception e) {
            //System.exit(1);
            e.printStackTrace();
        }

        //  System.out.println(mydeptno);
        return rowCount;
    }
} 