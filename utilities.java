package pwd_manager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class utilities {
    public boolean checkPasswordValidity(char[] a , char[] b){
        boolean flag=true;
        if(a.length==b.length){
            for(int i=0;i<a.length;i++){
                if(a[i]!=b[i]){
                    flag=false;
                    break;
                }
            }
        }
        else{
            flag=false;
        }
        return flag;
    }
    public ArrayList getTitleList(String o){
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
        String DB_URL = "jdbc:mysql://localhost:3306/pwd";
        String USER = "root";
        String PASS = "";
        ArrayList<String> titleList=new ArrayList<>();
        try{
        Class.forName(JDBC_DRIVER);
        Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
        PreparedStatement stmt=conn.prepareStatement("SELECT title FROM passwords WHERE name=?;");
        stmt.setString(1,o);
        ResultSet res=stmt.executeQuery();
        while(res.next()){
            String nub=res.getString("title");
            titleList.add(nub);
        }
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
        return titleList;
    }
}
