package br.unoeste.fipp.template.caixa.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lucas
 */
public class Banco 
{
    private static Banco instance;
    private static final String URL = "jdbc:postgresql://127.0.0.1:5432/caixa";
    private static final String usuario = "postgres";
    private static final String senha = "postgres123";
    private Connection conn;
    
    private Banco() {}
    
    public static Banco getInstance()
    {
        if (Banco.instance == null)
        {
            Banco.instance = new Banco();
        }
        
        return Banco.instance;
    }
    
    public Connection getConnection()
    {
        try 
        {
            if (conn == null || (conn != null && conn.isClosed()))
            {
                Class.forName("org.postgresql.Driver");
                conn = DriverManager.getConnection(URL,usuario, senha);
            }
        }
        catch (SQLException | ClassNotFoundException ex) 
        {
            conn = null;
            Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return conn;
    }
}
