package pwd_manager;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
public class mainMenu {
    public void mainMenuGui(JFrame loginFrame,String owner,String AES_key,String uname){
        loginFrame.setVisible(false);
        JFrame jf=new JFrame("Welcome "+owner);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jf.setLocation(dim.width/2-jf.getSize().width/2, dim.height/2-jf.getSize().height/2);
        JButton addPwdButton=new JButton("Add new password");
        JButton retrievePwdButton=new JButton("Retrieve password");
        JButton updatePwdButton=new JButton("Update password");
        JButton deletePwdButton=new JButton("Delete password");
        JButton updateLoginButton=new JButton("Update login");
        JButton exitButton=new JButton("Log out");
        addPwdButton.setBounds(10,10,200,50);
        retrievePwdButton.setBounds(230,10,200,50);
        updatePwdButton.setBounds(450,10,200,50);
        deletePwdButton.setBounds(10,70,200,50);
        updateLoginButton.setBounds(230,70,200,50);
        exitButton.setBounds(450,70,200,50);
        addPwdButton.addActionListener((ActionEvent evt) -> {
            new addPassword().initAddPasswordGui(jf,owner,AES_key);
        });
        retrievePwdButton.addActionListener((ActionEvent evt) -> {
            new retPwd().initRetPwdGui(jf,owner,AES_key);
        });
        updatePwdButton.addActionListener((ActionEvent evt) -> {
            new updatePwd().initUpdateGui(jf, owner, AES_key);
        });
        deletePwdButton.addActionListener((ActionEvent evt) -> {
            new delPwd().initDelGui(jf, owner);
        });
        updateLoginButton.addActionListener((ActionEvent evt) -> {
            new updateLogin().initUpdateLoginGui(jf,owner);
        });
        exitButton.addActionListener((ActionEvent evt) -> {
            loginFrame.setVisible(true);
            jf.dispose();
        });
        jf.add(addPwdButton);
        jf.add(retrievePwdButton);
        jf.add(updatePwdButton);
        jf.add(deletePwdButton);
        jf.add(updateLoginButton);
        jf.add(exitButton);
        jf.setLayout(null);
        jf.setSize(660,162);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }
}
