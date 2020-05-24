package br.unoeste.fipp.template.caixa.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author lucas
 */
public class Banco {
    
    private static Banco instance;
    private static final String URL = "jdbc:postgresql://127.0.0.1:5432/caixa";
    private static final String usuario = "postgres";
    private static final String senha = "postgres123";
    private Connection conn;
    
    private Banco() {}
    
    public static Banco getInstance() {
        if (Banco.instance == null) {
            Banco.instance = new Banco();
        }
        
        return Banco.instance;
    }
    
    public boolean open() {
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName("org.postgresql.Driver");
                conn = DriverManager.getConnection(URL,usuario, senha);
            }
            
            return true;
        } catch (SQLException | ClassNotFoundException ex)  {
            conn = null;
            System.out.println(ex.getMessage());
            
            return false;
        }
    }
    
    public Connection getConnection() {
        return conn;
    }
    
    public boolean beginTransaction() {
        try {
            if (conn != null) {
                conn.setAutoCommit(false);
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    
    public boolean commit() {
        try {
            conn.commit();
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    
    public boolean rollback() {
        try {
            conn.rollback();
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    
    public boolean close() {
        try {
            conn.setAutoCommit(true);
            conn.close();
            conn = null;
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
}
