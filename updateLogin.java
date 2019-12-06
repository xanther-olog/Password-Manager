package pwd_manager;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
public class updateLogin {
    void initUpdateLoginGui(JFrame mf, String owner) {
        mf.setVisible(false);
        JFrame jf=new JFrame("Update login for "+owner);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jf.setLocation(dim.width/2-jf.getSize().width/2, dim.height/2-jf.getSize().height/2);
        ArrayList<String> tempList=new ArrayList<>();
        tempList=new utilities().getTitleList(owner);
        String titleList[]=tempList.toArray(new String[tempList.size()]);
        JLabel title=new JLabel("Select login to update:");
        title.setBounds(10,10,350,30);
        title.setFont(new Font("Serif",Font.PLAIN,22));
        JComboBox l=new JComboBox(titleList);
        l.setBounds(350,13,200,30);
        l.setFont(new Font("Serif",Font.PLAIN,18));
        JLabel login=new JLabel("New user login:");
        login.setBounds(10,60,200,30);
        login.setFont(new Font("Serif",Font.PLAIN,22));
        JTextField titleEntry=new JTextField();
        titleEntry.setBounds(350,60,200,30);
        titleEntry.setFont(new Font("Serif",Font.PLAIN,22));
        JButton submit=new JButton("Update");
        submit.setBounds(120,125,100,50);
        JButton exit=new JButton("Exit");
        exit.setBounds(320,125,100,50);
        submit.addActionListener((ActionEvent evt) -> {
            String t=String.valueOf(l.getSelectedItem());
            initiateUpdateLogin(mf,jf,owner,t,titleEntry.getText());
        });
        exit.addActionListener((ActionEvent evt) -> {
            mf.setVisible(true);
            jf.dispose();
        });
        jf.add(title);
        jf.add(l);
        jf.add(submit);
        jf.add(exit);
        jf.add(login);
        jf.add(titleEntry);
        jf.setLayout(null);
        jf.setSize(575,210);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    private void initiateUpdateLogin(JFrame mf, JFrame jf, String owner, String t, String login) {
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
        String DB_URL = "jdbc:mysql://localhost:3306/pwd";
        String USER = "root";
        String PASS = "";
        try{
        Class.forName(JDBC_DRIVER);
        Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
        PreparedStatement stmt=conn.prepareStatement("UPDATE passwords SET login=? WHERE title=? AND name=?;");
        stmt.setString(1,login);
        stmt.setString(2,t);
        stmt.setString(3,owner);
        int cnt=0;
        cnt=stmt.executeUpdate();
        if(cnt==1){
            JOptionPane.showMessageDialog(jf,"Login updated!","Note",JOptionPane.INFORMATION_MESSAGE);
            exitToMainMenu(mf,jf);
        }
        stmt.close();
        conn.close();
        }catch(SQLException se){
            se.printStackTrace();
            JOptionPane.showMessageDialog(jf,"Login NOT updated!","Error",JOptionPane.ERROR_MESSAGE);
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(jf,"Login NOT updated!","Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    public void exitToMainMenu(JFrame mf,JFrame jf){
        mf.setVisible(true);
        jf.dispose();
    }  
}
