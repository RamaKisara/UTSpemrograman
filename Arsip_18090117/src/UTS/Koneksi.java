/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UTS;


import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author RamaPC
 */
public class Koneksi {
    public static Connection MySQL(){
        try {
            MysqlDataSource ds = new MysqlDataSource();
            ds.setUser("root");
            ds.setPassword("");
            ds.setServerName("localhost");
            ds.setDatabaseName("arsip");
            ds.setServerTimezone("Asia/Jakarta");
            ds.setPortNumber(3306);
            Connection c = ds.getConnection();
            return c;
        } catch (Exception e) {
            System.err.println(e. getMessage());
            return null;
        }
    }
    public static void main(String[] args) {
        try {
            Connection cn = Koneksi.MySQL();
            System.out.println(cn.getCatalog());
        } catch (Exception e) {
        }
    }
}
