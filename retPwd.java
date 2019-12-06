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
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
public class retPwd {
    public void initRetPwdGui(JFrame mainMenuFrame,String owner,String key){
        JFrame jf=new JFrame("Get Password for "+owner);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jf.setLocation(dim.width/2-jf.getSize().width/2, dim.height/2-jf.getSize().height/2);
        ArrayList<String> tempList=new ArrayList<>();
        tempList=new utilities().getTitleList(owner);
        String titleList[]=tempList.toArray(new String[tempList.size()]);
        JLabel title=new JLabel("Select password to get:");
        title.setBounds(10,10,300,30);
        title.setFont(new Font("Serif",Font.PLAIN,22));
        JComboBox l=new JComboBox(titleList);
        l.setBounds(350,13,150,30);
        l.setFont(new Font("Serif",Font.PLAIN,18));
        JButton getButton=new JButton("Get password");
        getButton.setBounds(75,70,200,30);
        JButton exitButton=new JButton("Exit");
        exitButton.setBounds(325,70,100,30);
        getButton.addActionListener((ActionEvent evt) -> {
            String t=String.valueOf(l.getSelectedItem());
            getPassword(jf,owner,t,key);
        });
        exitButton.addActionListener((ActionEvent evt) -> {
            mainMenuFrame.setVisible(true);
            jf.dispose();
        });
        jf.add(title);
        jf.add(l);
        jf.add(getButton);
        jf.add(exitButton);
        jf.setLayout(null);
        jf.setSize(515,145);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }
    public void getPassword(JFrame jf,String owner,String title,String key){
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
        String DB_URL = "jdbc:mysql://localhost:3306/pwd";
        String USER = "root";
        String PASS = "";
        try{
        Class.forName(JDBC_DRIVER);
        Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
        PreparedStatement stmt=conn.prepareStatement("SELECT website,login,pass FROM passwords where name=? and title=?;");
        stmt.setString(1,owner);
        stmt.setString(2,title);
        ResultSet res=stmt.executeQuery();
        res.first();
        String website=res.getString("website");
        String login=res.getString("login");
        String enc_pwd=res.getString("pass");
        String dec_pwd=AES.decrypt(enc_pwd,key);
        System.out.println();
        System.out.println("------------------------------------------------");
        System.out.println("Website: "+website);
        System.out.println("Login: "+login);
        System.out.println("Password: "+dec_pwd);
        System.out.println("------------------------------------------------");
        System.out.println();
        stmt.close();
        conn.close();
        res.close();
        }catch(SQLException se){
            se.printStackTrace();
            System.out.println("DBMS ERROR!");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("FATAL ERROR!");
        }
        
    }
}
