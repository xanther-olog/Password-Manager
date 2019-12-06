package pwd_manager;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
public class reg {
    public void initRegGui(JFrame mainFrame){
        mainFrame.setVisible(false);
        JFrame jf=new JFrame("Registration");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jf.setLocation(dim.width/2-jf.getSize().width/2, dim.height/2-jf.getSize().height/2);
        JLabel ownerName=new JLabel("Owner's Name:");
        ownerName.setBounds(10,10,300,30);
        ownerName.setFont(new Font("Serif",Font.PLAIN,22));
        JTextField ownerEntry=new JTextField();
        ownerEntry.setBounds(260,10,300,30);
        ownerEntry.setFont(new Font("Serif",Font.PLAIN,22));
        JLabel userName=new JLabel("Username:");
        userName.setBounds(10,50,300,30);
        userName.setFont(new Font("Serif",Font.PLAIN,22));
        JTextField userIdEntry=new JTextField();
        userIdEntry.setBounds(260,50,300,30);
        userIdEntry.setFont(new Font("Serif",Font.PLAIN,22));
        JLabel p1Label=new JLabel("Enter Password:");
        p1Label.setBounds(10,90,300,30);
        p1Label.setFont(new Font("Serif",Font.PLAIN,22));
        JPasswordField p1Entry=new JPasswordField();
        p1Entry.setBounds(260,90,300,30);
        p1Entry.setFont(new Font("Serif",Font.PLAIN,22));
        JLabel p2Label=new JLabel("Confirm Password:");
        p2Label.setBounds(10,130,300,30);
        p2Label.setFont(new Font("Serif",Font.PLAIN,22));
        JPasswordField p2Entry=new JPasswordField();
        p2Entry.setBounds(260,130,300,30);
        p2Entry.setFont(new Font("Serif",Font.PLAIN,22));
        JButton submitButton=new JButton("Submit");
        submitButton.setBounds(130,180,100,50);
        JButton exitButton=new JButton("Exit");
        exitButton.setBounds(330,180,100,50);
        submitButton.addActionListener((ActionEvent evt) -> {
            boolean no_pwd_error=new utilities().checkPasswordValidity(p1Entry.getPassword(),p2Entry.getPassword());
            if(no_pwd_error){
                boolean check_existence=checkExistence(userIdEntry.getText());
                if(check_existence){
                    JOptionPane.showMessageDialog(jf,"User already registered!","Note",JOptionPane.INFORMATION_MESSAGE);
                    mainFrame.setVisible(true);
                    jf.dispose();
                }
                else{
                    addToDatabase(jf,ownerEntry.getText(),userIdEntry.getText(),p1Entry.getPassword());
                    mainFrame.setVisible(true);
                    jf.dispose();
                }
            }
            else{
                JOptionPane.showMessageDialog(jf,"Passwords don't match","Error",JOptionPane.WARNING_MESSAGE);
            }
        });
        exitButton.addActionListener((ActionEvent evt) -> {
            mainFrame.setVisible(true);
            jf.dispose();
        });
        jf.add(ownerName);
        jf.add(ownerEntry);
        jf.add(userName);
        jf.add(userIdEntry);
        jf.add(p1Label);
        jf.add(p1Entry);
        jf.add(p2Label);
        jf.add(p2Entry);
        jf.add(submitButton);
        jf.add(exitButton);
        jf.setLayout(null);
        jf.setSize(575,280);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }
    
    public boolean checkExistence(String uname){
        boolean flag=false;
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
        String DB_URL = "jdbc:mysql://localhost:3306/pwd";
        String USER = "root";
        String PASS = "";
        int resultSize=-1;
        try{
        Class.forName(JDBC_DRIVER);
        Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
        PreparedStatement stmt=conn.prepareStatement("SELECT * FROM owners WHERE username=?;");
        stmt.setString(1,uname);
        ResultSet rs=stmt.executeQuery();
        if(rs!=null){
            rs.last();
            resultSize=rs.getRow();
        }
        if(resultSize==1){
            flag=true;
        }
        else{
            flag=false;
        }
        rs.close();
        stmt.close();
        conn.close();
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    return flag;
    }
    public void addToDatabase(JFrame jf,String oname,String uname,char[] pwd){
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
        String DB_URL = "jdbc:mysql://localhost:3306/pwd";
        String USER = "root";
        String PASS = "";
        String temp="";
        for(int i=0;i<pwd.length;i++){
            temp=temp+pwd[i];
        }
        String enc_pwd=AES.encrypt(temp,"default");
        try{
        Class.forName(JDBC_DRIVER);
        Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
        PreparedStatement stmt=conn.prepareStatement("INSERT INTO owners(name,username,password) VALUES(?,?,?);");
        stmt.setString(1,oname);
        stmt.setString(2,uname);
        stmt.setString(3,enc_pwd);
        int cnt=0;
        cnt=stmt.executeUpdate();
        if(cnt==1){
            JOptionPane.showMessageDialog(jf,"Registration Successful!","Note",JOptionPane.INFORMATION_MESSAGE);
        }
        stmt.close();
        conn.close();
        }catch(SQLException se){
            se.printStackTrace();
            JOptionPane.showMessageDialog(jf,"Registration unsuccesful!","Error",JOptionPane.ERROR_MESSAGE);
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(jf,"Registration unsuccesful!","Error",JOptionPane.ERROR_MESSAGE);
        }
    }
}
