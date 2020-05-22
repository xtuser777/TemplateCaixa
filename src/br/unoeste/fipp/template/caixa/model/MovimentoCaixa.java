package br.unoeste.fipp.template.caixa.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MovimentoCaixa 
{
    private int id;
    private double valor;
    private int tipo;
    private Caixa caixa;
    private Acerto acerto;

    public MovimentoCaixa(int id, double valor, int tipo, Caixa caixa, Acerto acerto) 
    {
        this.id = id;
        this.valor = valor;
        this.tipo = tipo;
        this.caixa = caixa;
        this.acerto = acerto;
    }

    public int getId()
    {
        return id;
    }

    public double getValor() 
    {
        return valor;
    }

    public int getTipo() 
    {
        return tipo;
    }

    public Caixa getCaixa() 
    {
        return caixa;
    }

    public Acerto getAcerto()
    {
        return acerto;
    }
    
    public int salvar(Connection conn)
    {
        if (this.id != 0 || this.tipo == 0 || this.valor <= 0 || this.acerto == null || this.caixa == null) return -5;
        
        String sql = "insert into movimento_caixa(cxa_id,mc_valor,mc_tipo,act_id) values(?,?,?,?) returning mc_id;";
        
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, caixa.getId());
                ps.setDouble(2, valor);
                ps.setInt(3, tipo);
                ps.setInt(4, acerto.getId());

                try(ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int result = rs.getInt("mc_id");
                        this.id = result;
                        return result;
                    } else {
                        return -10;
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return -10;
            }
        }
        
        return -10;
    }
}
