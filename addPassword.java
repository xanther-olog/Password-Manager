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
public class addPassword {
    public void initAddPasswordGui(JFrame mainMenuFrame,String owner,String AES_key){
        mainMenuFrame.setVisible(false);
        JFrame jf=new JFrame("Add Password for "+owner);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jf.setLocation(dim.width/2-jf.getSize().width/2, dim.height/2-jf.getSize().height/2);
        JLabel title=new JLabel("Title:");
        title.setBounds(10,10,300,30);
        title.setFont(new Font("Serif",Font.PLAIN,22));
        JLabel website=new JLabel("Website:");
        website.setBounds(10,60,300,30);
        website.setFont(new Font("Serif",Font.PLAIN,22));
        JLabel login=new JLabel("User login:");
        login.setBounds(10,110,300,30);
        login.setFont(new Font("Serif",Font.PLAIN,22));
        JLabel p1=new JLabel("Enter password:");
        p1.setBounds(10,160,300,30);
        p1.setFont(new Font("Serif",Font.PLAIN,22));
        JLabel p2=new JLabel("Confirm password:");
        p2.setBounds(10,210,300,30);
        p2.setFont(new Font("Serif",Font.PLAIN,22));
        JTextField titleEntry=new JTextField();
        titleEntry.setBounds(260,10,300,30);
        titleEntry.setFont(new Font("Serif",Font.PLAIN,22));
        JTextField webEntry=new JTextField();
        webEntry.setBounds(260,60,300,30);
        webEntry.setFont(new Font("Serif",Font.PLAIN,22));
        JTextField loginTF=new JTextField();
        loginTF.setBounds(260,110,300,30);
        loginTF.setFont(new Font("Serif",Font.PLAIN,22));
        JPasswordField p1Entry=new JPasswordField();
        p1Entry.setBounds(260,160,300,30);
        p1Entry.setFont(new Font("Serif",Font.PLAIN,22));
        JPasswordField p2Entry=new JPasswordField();
        p2Entry.setBounds(260,210,300,30);
        p2Entry.setFont(new Font("Serif",Font.PLAIN,22));
        JButton submit=new JButton("Submit");
        submit.setBounds(150,275,100,50);
        JButton exit=new JButton("Exit");
        exit.setBounds(350,275,100,50);
        submit.addActionListener((ActionEvent evt) -> {
            boolean validity=new utilities().checkPasswordValidity(p1Entry.getPassword(),p2Entry.getPassword());
            if(!validity){
                JOptionPane.showMessageDialog(jf,"Passwords don't match","Error",JOptionPane.WARNING_MESSAGE);
            }
            else{
                boolean exists=checkExistingPassword(jf,owner,titleEntry.getText());
                if(!exists){
                    addToDatabase(mainMenuFrame,jf,owner,titleEntry.getText(),webEntry.getText(),loginTF.getText(),p1Entry.getPassword(),AES_key);
                }
                else{
                   JOptionPane.showMessageDialog(jf,"Password already exists!","Note",JOptionPane.INFORMATION_MESSAGE);
                   exitToMainMenu(mainMenuFrame,jf);
                }
            }
        });
        exit.addActionListener((ActionEvent evt) -> {
            mainMenuFrame.setVisible(true);
            jf.dispose();
        });
        jf.add(title);
        jf.add(website);
        jf.add(login);
        jf.add(p1);
        jf.add(p2);
        jf.add(titleEntry);
        jf.add(webEntry);
        jf.add(loginTF);
        jf.add(p1Entry);
        jf.add(p2Entry);
        jf.add(submit);
        jf.add(exit);
        jf.setLayout(null);
        jf.setSize(575,370);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }
    public void addToDatabase(JFrame mf,JFrame jf,String o,String t,String w,String l,char[] pwd,String key){
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
        String DB_URL = "jdbc:mysql://localhost:3306/pwd";
        String USER = "root";
        String PASS = "";
        String temp="";
        for(int i=0;i<pwd.length;i++){
            temp=temp+pwd[i];
        }
        String enc_pwd=AES.encrypt(temp,key);
        try{
        Class.forName(JDBC_DRIVER);
        Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
        PreparedStatement stmt=conn.prepareStatement("INSERT INTO passwords(name,title,website,login,pass) VALUES(?,?,?,?,?);");
        stmt.setString(1,o);
        stmt.setString(2,t);
        stmt.setString(3,w);
        stmt.setString(4,l);
        stmt.setString(5,enc_pwd);
        int cnt=0;
        cnt=stmt.executeUpdate();
        if(cnt==1){
            JOptionPane.showMessageDialog(jf,"Password added!","Note",JOptionPane.INFORMATION_MESSAGE);
            exitToMainMenu(mf,jf);
        }
        stmt.close();
        conn.close();
        }catch(SQLException se){
            se.printStackTrace();
            JOptionPane.showMessageDialog(jf,"Password NOT added!","Error",JOptionPane.ERROR_MESSAGE);
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(jf,"Password NOT added!","Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    public boolean checkExistingPassword(JFrame jf,String o ,String t){
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
        String DB_URL = "jdbc:mysql://localhost:3306/pwd";
        String USER = "root";
        String PASS = "";
        String temp="";
        boolean flag=true;
        try{
        Class.forName(JDBC_DRIVER);
        Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
        PreparedStatement stmt=conn.prepareStatement("SELECT * FROM passwords WHERE title=? AND name=?;");
        stmt.setString(1,t);
        stmt.setString(2,o);
        ResultSet res=stmt.executeQuery();
        int cnt=-1;
        if(res!=null){
            res.last();
            cnt=res.getRow();
        }
        if(cnt==1){
            flag=true;
        }
        else{
            flag=false;
        }
        }catch(SQLException se){
            se.printStackTrace();
            JOptionPane.showMessageDialog(jf,"DBMS Error","Error",JOptionPane.ERROR_MESSAGE);
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(jf,"Fatal Error","Error",JOptionPane.ERROR_MESSAGE);
        }
        return flag;
    }
    public void exitToMainMenu(JFrame mf,JFrame jf){
        mf.setVisible(true);
        jf.dispose();
    }
}
