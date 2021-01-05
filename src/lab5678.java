/*
 * Title：lab5678.java
 * Created by 栗子 at  01/06/2021 01:28:28
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;

public class lab5678 extends JFrame implements ActionListener {
    private static JDBCUtil jdbc = new JDBCUtil();
    static String abc = "";
    //操作面板，用于放置增删改按钮
    private JPanel controlPanel;
    //定义表格的数据模型
    private DefaultTableModel model;
    //定义一个表格
    private JTable table;
    //定义一个滚动面板，用于放置表格
    private JScrollPane scrollPane;
    //增删改按钮
    private JButton addBtn, deleteBtn, updateBtn, fromAddBtn, confirmBtn;
    Set<Integer> number = new HashSet<Integer>();
    List<User> addUsers = new ArrayList<>();
    List<User> updateUsers = new ArrayList<>();

    private String getNo() {
        Random r = new Random(1);
        Integer ans = null;
        do {
            ans = r.nextInt(10000);
        } while (number.contains(ans));
        number.add(ans);
        return ans.toString();
    }

    private JPanel fromAddPane() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2, 5, 5));
        JLabel label1 = new JLabel("EMPNO");
        JTextField jTextField1 = new JTextField();
        panel.add(label1);
        panel.add(jTextField1);
        return panel;
    }

    public lab5678() {
        //设置窗口尺寸
        setBounds(400, 400, 500, 500);
        //JTable的表头标题
        String[] head = {"EMPNO", "FIRSTNME", "LASTNAME", "EDLEVEL", "SALARY", "BONUS"};
        //JTable的初始化数据
        List<HashMap<String, String>> users = jdbc.query("select EMPNO,FIRSTNME,LASTNAME,EDLEVEL,SALARY,BONUS from jlu.TEMPL");
        Object[][] datas = null;
        //初始化JTable的数据模型
        model = new DefaultTableModel(datas, head) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0) return false;
                else return true;
            }
        };
        //初始化表格
        table = new JTable(model);
        for (HashMap<String, String> map : users) {
            Object data[] = null;
            User user = new User();
            user.setEMPNO(map.get("EMPNO").replace(" ", ""));
            user.setFIRSTNME(map.get("FIRSTNME"));
            user.setLASTNAME(map.get("LASTNAME"));
            user.setSALARY(map.get("SALARY"));
            user.setBONUS(map.get("BONUS"));
            user.setEDLEVEL(map.get("EDLEVEL"));
            System.out.println(user.toString());
            String[] gett = new String[6];
            gett[0] = user.getEMPNO();
            gett[1] = user.getFIRSTNME();
            gett[2] = user.getLASTNAME();
            gett[3] = user.getEDLEVEL();
            gett[4] = user.getSALARY();
            gett[5] = user.getBONUS();
            if (gett[0] == "") {
                gett[0] = "null";
            }
            if (gett[1] == "") {
                gett[1] = "null";
            }
            if (gett[2] == "") {
                gett[2] = "null";
            }
            if (gett[3] == "") {
                gett[3] = "null";
            }
            if (gett[4] == "") {
                gett[4] = "null";
            }
            if (gett[4] == "") {
                gett[4] = "null";
            }
            if (gett[5] == "") {
                gett[5] = "null";
            }
            number.add(Integer.valueOf(gett[0].replace(" ", "")));
            model.addRow(gett);
        }
        //初始化滚动面板
        scrollPane = new JScrollPane(table);
        //初始化按钮以及添加监听器
        addBtn = new JButton("增加");
        deleteBtn = new JButton("删除");
        updateBtn = new JButton("修改");
        fromAddBtn = new JButton("子查询插入");
        confirmBtn = new JButton("落实");
        addBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        fromAddBtn.addActionListener(this);
        confirmBtn.addActionListener(this);
        //初始化控制面板
        controlPanel = new JPanel();
        controlPanel.add(addBtn);
        controlPanel.add(deleteBtn);
        controlPanel.add(updateBtn);
        controlPanel.add(fromAddBtn);
        controlPanel.add(confirmBtn);
        //该窗口使用BorderLayout布局
        add(controlPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        //设置表格单元格字体居中显示
        DefaultTableCellRenderer render =
                new DefaultTableCellRenderer();
        render.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumn("EMPNO").setCellRenderer(render);
        table.getColumn("FIRSTNME").setCellRenderer(render);
        //设置表格宽度情况
        DefaultTableColumnModel dcm =
                (DefaultTableColumnModel) table.getColumnModel();
        //设置表格显示的最好宽度，即此时表格显示的宽度。
        dcm.getColumn(0).setPreferredWidth(60);
        //设置表格通过拖动列可以的最小宽度。
        dcm.getColumn(0).setMinWidth(45);
        //设置表格通过拖动列可以的最大宽度。
        dcm.getColumn(0).setMaxWidth(75);
        //给表格设置行高
        table.setRowHeight(35);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                int col = table.getSelectedColumn();
                System.out.println("编辑前：" + table.
                        getValueAt(row, col));
            }
        });
        table.getModel().addTableModelListener(
                new TableModelListener() {
                    public void tableChanged(TableModelEvent e) {
                        if (e.getType() == TableModelEvent.UPDATE) {
                            int row = e.getLastRow();
                            int col = e.getColumn();
                            User temp = new User();
                            temp.setEMPNO((String) table.getValueAt(row, 0).toString());
                            temp.setFIRSTNME((String) table.getValueAt(row, 1).toString());
                            temp.setLASTNAME((String) table.getValueAt(row, 2).toString());
                            temp.setEDLEVEL((String) table.getValueAt(row, 3).toString());
                            temp.setSALARY((String) table.getValueAt(row, 4).toString());
                            temp.setBONUS((String) table.getValueAt(row, 5).toString());
                            updateUsers.add(temp);
                            System.out.println("编辑后:" + table.getValueAt(row, col));
                        }
                    }
                });
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBtn) {
            String no = getNo();
            model.addRow(new Object[]{no, "", "", 0, 0, 0});
            User temp = new User();
            temp.setEMPNO(no);
            addUsers.add(temp);
        } else if (e.getSource() == deleteBtn) {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "请先选择一条记录！");
                return;
            }
            int flag = JOptionPane.showConfirmDialog(null, "删除不可恢复，确定删除吗？", "标题", JOptionPane.YES_NO_OPTION);
            if (flag == 0) {
                User temp = new User();
                temp.setEMPNO((String) table.getValueAt(row, 0));
                System.out.println(temp.del());
                System.out.println("删除");
                model.removeRow(table.getSelectedRow());
            }

        } else if (e.getSource() == updateBtn) {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "请先选择一条记录！");
                return;
            }
        } else if (e.getSource() == fromAddBtn) {
            String ab = JOptionPane.showInputDialog(null, "EMPNO:",
                    "子查询输入", JOptionPane.INFORMATION_MESSAGE);
            List<HashMap<String, String>> users = jdbc.query("select EMPNO,FIRSTNME,LASTNAME,EDLEVEL,SALARY,BONUS from jlu.EMPLOYEE WHERE EMPNO='" + ab + "'");
            for (HashMap<String, String> temp : users) {
                User user = new User();
                user.setEMPNO(temp.get("EMPNO").replace(" ", ""));
                user.setFIRSTNME(temp.get("FIRSTNME"));
                user.setLASTNAME(temp.get("LASTNAME"));
                user.setEDLEVEL(temp.get("EDLEVEL"));
                user.setSALARY(temp.get("SALARY"));
                user.setBONUS(temp.get("BONUS"));
                addUsers.add(user);
                String[] gett = new String[5];
                gett[0] = user.getEMPNO();
                gett[1] = user.getFIRSTNME();
                gett[2] = user.getLASTNAME();
                gett[3] = user.getEDLEVEL().toString();
                gett[4] = user.getSALARY();
                gett[5] = user.getBONUS();
                if (gett[0] == "") {
                    gett[0] = "null";
                }
                if (gett[1] == "") {
                    gett[1] = "null";
                }
                if (gett[2] == "") {
                    gett[2] = "null";
                }
                if (gett[3] == "") {
                    gett[3] = "null";
                }
                if (gett[4] == "") {
                    gett[4] = "null";
                }
                number.add(Integer.valueOf(gett[0]));
                model.addRow(gett);
            }
//            javax.swing.JOptionPane.showConfirmDialog(null,
//                    fromAddPane(),
//                    "子查询插入",
//                    javax.swing.JOptionPane.OK_CANCEL_OPTION,
//                    javax.swing.JOptionPane.PLAIN_MESSAGE);
        } else if (e.getSource() == confirmBtn) {
            for (Iterator<User> it = addUsers.iterator(); it.hasNext(); ) {
                User now = it.next();
                System.out.println(now.add());
                it.remove();
            }
            for (Iterator<User> it = updateUsers.iterator(); it.hasNext(); ) {
                User now = it.next();
                System.out.println(now.update());
                it.remove();
            }
            JOptionPane.showMessageDialog(null, "已落实！", "提示", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new lab5678();
    }
}
