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
public class login {
    public void initLoginGui(JFrame mainFrame){
        mainFrame.setVisible(false);
        JFrame jf=new JFrame("Login");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jf.setLocation(dim.width/2-jf.getSize().width/2, dim.height/2-jf.getSize().height/2);
        JLabel userName=new JLabel("Username:");
        userName.setBounds(10,10,300,30);
        userName.setFont(new Font("Serif",Font.PLAIN,22));
        JTextField userIdEntry=new JTextField();
        userIdEntry.setBounds(180,10,300,30);
        userIdEntry.setFont(new Font("Serif",Font.PLAIN,22));
        JLabel p1Label=new JLabel("Password:");
        p1Label.setBounds(10,60,300,30);
        p1Label.setFont(new Font("Serif",Font.PLAIN,22));
        JPasswordField pEntry=new JPasswordField();
        pEntry.setBounds(180,60,300,30);
        pEntry.setFont(new Font("Serif",Font.PLAIN,22));
        JButton loginButton=new JButton("Login");
        loginButton.setBounds(100,120,100,50);
        JButton exitButton=new JButton("Exit");
        exitButton.setBounds(270,120,100,50);
        loginButton.addActionListener((ActionEvent evt) -> {
            tryLogin(jf,userIdEntry.getText(),pEntry.getPassword());
        });
        
        exitButton.addActionListener((ActionEvent evt) -> {
            mainFrame.setVisible(true);
            jf.dispose();
        });
        jf.add(userName);
        jf.add(userIdEntry);
        jf.add(p1Label);
        jf.add(pEntry);
        jf.add(loginButton);
        jf.add(exitButton);
        jf.setLayout(null);
        jf.setSize(490,220);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }
    public void tryLogin(JFrame jf,String uname,char[] pwd){
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
        String DB_URL = "jdbc:mysql://localhost:3306/pwd";
        String USER = "root";
        String PASS = "";
        String temp="";
        for(int i=0;i<pwd.length;i++){
            temp=temp+pwd[i];
        }
        try{
        Class.forName(JDBC_DRIVER);
            try (Connection conn = DriverManager.getConnection(DB_URL,USER,PASS)) {
                PreparedStatement stmt=conn.prepareStatement("SELECT * FROM owners WHERE username=?;");
                stmt.setString(1,uname);
                int resultSize=-1;
                ResultSet res=stmt.executeQuery();
                if(res!=null){
                    res.last();
                    resultSize=res.getRow();
                }
                if(resultSize!=1){
                    JOptionPane.showMessageDialog(jf,"Incorrect username!","Error",JOptionPane.ERROR_MESSAGE);
                }
                else{
                    String enc_pwd=res.getString("password");
                    String dec_pwd=AES.decrypt(enc_pwd,"default");
                    String oname=res.getString("name");
                    if(temp.equals(dec_pwd)){
                        new mainMenu().mainMenuGui(jf,oname,dec_pwd,uname);
                    }
                    else{
                        JOptionPane.showMessageDialog(jf,"Incorrect Password!","Error",JOptionPane.ERROR_MESSAGE);
                    }
                }
                stmt.close();
                conn.close();
            }
        }catch(SQLException se){
            se.printStackTrace();
            JOptionPane.showMessageDialog(jf,"Login unsuccesful!","Error",JOptionPane.ERROR_MESSAGE);
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(jf,"Login unsuccesful!","Error",JOptionPane.ERROR_MESSAGE);
        }
    }
}
