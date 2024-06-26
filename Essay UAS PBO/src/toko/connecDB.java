package toko;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connecDB {
    private String url, usr, pwd, db;
    
    public connecDB() {
        db = "toko";
        url = "jdbc:mysql://localhost:8111/" + db;
        usr = "root";
        pwd = "";
    }

    public Connection getConnect() {
        Connection cn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            cn = DriverManager.getConnection(url, usr, pwd);
            System.out.println("Koneksi Berhasil");
        } catch (ClassNotFoundException er) {
            System.out.println("Error #1: " + er.getMessage());
            System.exit(0);
        } catch (SQLException er) {
            System.out.println("Error #2: " + er.getMessage());
        }
        return cn;
    }
    
    public static void main(String[] args) {
        new connecDB().getConnect();
    }
}
