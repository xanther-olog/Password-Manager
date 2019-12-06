package pwd_manager;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
public class launcher{
    public static void main(String[] args) {
        JFrame jf=new JFrame("Welcome");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jf.setLocation(dim.width/2-jf.getSize().width/2, dim.height/2-jf.getSize().height/2);
        JButton regButton=new JButton("Register");
        JButton loginButton=new JButton("Login");
        JButton exitButton=new JButton("Exit");
        regButton.setBounds(0,0,100,50);
        loginButton.setBounds(102,0,100,50);
        exitButton.setBounds(204,0,100,50);
        regButton.setFocusPainted(false);
        loginButton.setFocusPainted(false);
        exitButton.setFocusPainted(false);
        regButton.addActionListener((ActionEvent evt) -> {
            new reg().initRegGui(jf);
        });
        loginButton.addActionListener((ActionEvent evt) -> {
            new login().initLoginGui(jf);
        });
        exitButton.addActionListener((ActionEvent evt) -> {
            jf.dispose();
        });
        jf.add(regButton);
        jf.add(loginButton);
        jf.add(exitButton);
        jf.setLayout(null);
        jf.setSize(306,82);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
    }
}
