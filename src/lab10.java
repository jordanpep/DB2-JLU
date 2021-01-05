/*
 * Title：lab10.java
 * Created by 栗子 at  01/06/2021 01:28:28
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.io.*;
import java.lang.*;

import static java.awt.Component.CENTER_ALIGNMENT;



class lab10 {
    static {
        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver");


        } catch (Exception e) {
            System.out.println("\n  Error loading DB2 Driver...\n");
            System.out.println(e);
            System.exit(1);
        }
    }

    static ImageIcon imageIcon;

    public static void main(String argv[]) {



        try {
            Connection sample = null;
            String url = "jdbc:db2://192.168.44.131:50000/SAMPLE";
            sample = DriverManager.getConnection(url, "db2admin", "db2admin");
            Statement stmt = sample.createStatement();
            ResultSet rs = stmt.executeQuery("select * from jlu.EMP_PHOTO");
            boolean more = rs.next();
            Integer num = 0;
            JComboBox box = new JComboBox();
            JComboBox box1=new JComboBox();
            while (more) {
                String emmpno = rs.getString(1);
                box.insertItemAt(emmpno, num);
                String photo_format = rs.getString(2);
//                Blob blob = (Blob) rs.getBlob(3);
//                //Blob对象转化为InputStream流
//                InputStream inputStream = blob.getBinaryStream();
//                //要写入的文件
//                File fileOutput = new File("./pic" + num.toString() + ".png");
//                //文件的写入流的定义
//                FileOutputStream fo = new FileOutputStream(fileOutput);
//                int c;
//                //读取流并写入到文件中
//                while ((c = inputStream.read()) != -1) fo.write(c);
//                //流的关闭:
//                fo.close();
                more = rs.next();
                num++;
            }
            box.setSelectedIndex(0);
            rs = stmt.executeQuery("select EMPNO from jlu.EMPLOYEE");
            more = rs.next();
            num = 0;
            while(more){
                String empno=rs.getString(1);
                box1.insertItemAt(empno,num);
                num++;
                more=rs.next();
            }
            box1.setSelectedIndex(0);
            JButton checkBtn = new JButton("查询");
            JButton insertBtn = new JButton("插入");
            JFrame index = new JFrame("查询删除图片练习");
//            JTextField jTextField=new JTextField();
            JLabel jTextArea=new JLabel("  EMPNO:");
            jTextArea.setSize(78,78);
            jTextArea.setAlignmentY(CENTER_ALIGNMENT);
            index.setLocation(500, 300);
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(1,5 , 5, 5));
            panel.add(box);
            panel.add(checkBtn);
            panel.add(jTextArea);
//            panel.add(jTextField);
            panel.add(box1);
            panel.add(insertBtn);
            index.add(panel);
            index.setSize(400, 80);
            index.setVisible(true);

            checkBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        openFile((String)box.getSelectedItem());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });

            insertBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        //创建文件对象:
                        JFileChooser jfc=new JFileChooser();
                        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
                        jfc.showDialog(new JLabel(), "选择");
                        File file=jfc.getSelectedFile();
                        insertFile((String)box1.getSelectedItem(),file.getPath());
                        JOptionPane.showMessageDialog(null,"上传成功");
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insertFile(String no,String filename) throws SQLException, FileNotFoundException {
        Connection sample = null;
        String url = "jdbc:db2://192.168.44.131:50000/SAMPLE";
        sample = DriverManager.getConnection(url, "db2admin", "db2admin");
        Statement stmt = sample.createStatement();
        // 创建插入语句.
        java.sql.PreparedStatement preparedStatement = sample
                .prepareStatement("insert into jlu.emp_photo values(?,'jpeg',?)");
        //创建文件对象:
        File file = new File(filename);
        // 创建流对象:
        BufferedInputStream imageInput = new BufferedInputStream(
                new FileInputStream(file));
        //参数赋值:
        preparedStatement.setString(1,no);
        preparedStatement.setBinaryStream(2, imageInput, (int) file.length());
        //执行语句
        preparedStatement.executeUpdate();
    }
    private static void openFile(String filename) throws SQLException, IOException {
        Connection sample = null;
        String url = "jdbc:db2://192.168.44.131:50000/SAMPLE";
        sample = DriverManager.getConnection(url, "db2admin", "db2admin");
        Statement stmt = sample.createStatement();
        ResultSet rs = stmt.executeQuery("select * from jlu.EMP_PHOTO where EMPNO='"+filename+"' and PHOTO_FORMAT='gif'");
        boolean more = rs.next();
        Integer num = 0;
        JComboBox box = new JComboBox();
        while (more) {
            String emmpno = rs.getString(1);
            box.insertItemAt(emmpno, num);
            String photo_format = rs.getString(2);
           // Blob blob = (Blob) rs.getBlob(3);
            //Blob对象转化为InputStream流
            byte[] bytes=new byte[10240*10];
            InputStream inputStream = rs.getBinaryStream(3);
            inputStream.read(bytes);
            String file="C:/Users/Administrator/Desktop/DB2APP/pic.png";
            File file1=new File(file);
            FileOutputStream fileOutputStream=new FileOutputStream(file1);
            fileOutputStream.write(bytes,0,bytes.length);
            fileOutputStream.close();

           imageIcon=new ImageIcon(file);

         /*   //要写入的文件
            File fileOutput = new File("./pic" + ".png");
            //文件的写入流的定义
            FileOutputStream fo = new FileOutputStream(fileOutput);
            int c;
            //读取流并写入到文件中
            while ((c = inputStream.read()) != -1) fo.write(c);
            //流的关闭:
            fo.close();*/
            more = rs.next();
           // num++;

          /*  JFrame ii=new JFrame("图片");
            ii.setSize(300,300);
            ii.setLocation(550,400);
            JPanel jPanel=new JPanel();
            jPanel.setBounds(10,10,280,280);
            ImageIcon img = new ImageIcon("C:/Users/Administrator/Desktop/DB2APP/pic.png");
            System.out.println(img);
            JLabel imageLabel = new JLabel();


            imageLabel.setBounds(0,0,250,250);
            imageLabel.setIcon(img);
            jPanel.add(imageLabel);
            jPanel.repaint();

            ii.add(jPanel);

            ii.setVisible(true);*/

            //return;
        }

        JFrame ii=new JFrame("图片");
        ii.setSize(300,300);
        ii.setLocation(550,400);
       // JPanel jPanel=new JPanel();
        //jPanel.setBounds(10,10,280,280);
       // ImageIcon img = new ImageIcon("C:/Users/Administrator/Desktop/DB2APP/img2.jpg");
        System.out.println(imageIcon);
        JLabel imageLabel = new JLabel();


        imageLabel.setBounds(0,0,250,250);
        imageLabel.setIcon(imageIcon);
      //  jPanel.add(imageLabel);
       // jPanel.repaint();

        ii.add(imageLabel);

        ii.setVisible(true);

    }
}