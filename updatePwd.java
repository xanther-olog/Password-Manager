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
import javax.swing.JPasswordField;
import javax.swing.WindowConstants;
public class updatePwd {
    public void initUpdateGui(JFrame mf,String owner,String key){
        mf.setVisible(false);
        JFrame jf=new JFrame("Update password for "+owner);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jf.setLocation(dim.width/2-jf.getSize().width/2, dim.height/2-jf.getSize().height/2);
        ArrayList<String> tempList=new ArrayList<>();
        tempList=new utilities().getTitleList(owner);
        String titleList[]=tempList.toArray(new String[tempList.size()]);
        JLabel title=new JLabel("Select password to update:");
        title.setBounds(10,10,350,30);
        title.setFont(new Font("Serif",Font.PLAIN,22));
        JComboBox l=new JComboBox(titleList);
        l.setBounds(350,13,200,30);
        l.setFont(new Font("Serif",Font.PLAIN,18));
        JLabel np=new JLabel("Enter new password:");
        np.setBounds(10,60,350,30);
        np.setFont(new Font("Serif",Font.PLAIN,22));
        JPasswordField p1Entry=new JPasswordField();
        p1Entry.setBounds(350,63,200,30);
        p1Entry.setFont(new Font("Serif",Font.PLAIN,18));
        JLabel npc=new JLabel("Confirm password:");
        npc.setBounds(10,110,350,30);
        npc.setFont(new Font("Serif",Font.PLAIN,22));
        JPasswordField p2Entry=new JPasswordField();
        p2Entry.setBounds(350,113,200,30);
        p2Entry.setFont(new Font("Serif",Font.PLAIN,18));
        JButton submit=new JButton("Submit");
        submit.setBounds(120,173,100,50);
        JButton exit=new JButton("Exit");
        submit.addActionListener((ActionEvent evt) -> {
            boolean pwd_validity=new utilities().checkPasswordValidity(p1Entry.getPassword(),p2Entry.getPassword());
            if(!pwd_validity){
                JOptionPane.showMessageDialog(jf,"Passwords don't match","Error",JOptionPane.WARNING_MESSAGE);
            }
            else{
                String t=String.valueOf(l.getSelectedItem());
                initiateUpdate(mf,jf,owner,t,p1Entry.getPassword(),key);
            }
        });
        exit.setBounds(320,173,100,50);
        exit.addActionListener((ActionEvent evt) -> {
            mf.setVisible(true);
            jf.dispose();
        });
        jf.add(title);
        jf.add(l);
        jf.add(np);
        jf.add(p1Entry);
        jf.add(npc);
        jf.add(p2Entry);
        jf.add(submit);
        jf.add(exit);
        jf.setLayout(null);
        jf.setSize(560,270);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }
    public void initiateUpdate(JFrame mf,JFrame jf,String n,String t,char[] p,String key){
        String temp="";
        for(int i=0;i<p.length;i++){
            temp=temp+p[i];
        }
        String enc_pwd=AES.encrypt(temp,key);
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
        String DB_URL = "jdbc:mysql://localhost:3306/pwd";
        String USER = "root";
        String PASS = "";
        try{
        Class.forName(JDBC_DRIVER);
        Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
        PreparedStatement stmt=conn.prepareStatement("UPDATE passwords SET pass=? WHERE title=? AND name=?;");
        stmt.setString(1,enc_pwd);
        stmt.setString(2,t);
        stmt.setString(3,n);
        int cnt=0;
        cnt=stmt.executeUpdate();
        if(cnt==1){
            JOptionPane.showMessageDialog(jf,"Password updated!","Note",JOptionPane.INFORMATION_MESSAGE);
            exitToMainMenu(mf,jf);
        }
        stmt.close();
        conn.close();
        }catch(SQLException se){
            se.printStackTrace();
            JOptionPane.showMessageDialog(jf,"Password NOT updated!","Error",JOptionPane.ERROR_MESSAGE);
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(jf,"Password NOT updated!","Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    public void exitToMainMenu(JFrame mf,JFrame jf){
        mf.setVisible(true);
        jf.dispose();
    }
}
